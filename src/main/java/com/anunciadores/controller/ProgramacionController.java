package com.anunciadores.controller;

import com.anunciadores.auth.dto.updateServiceRequest;
import com.anunciadores.dto.CoordinadorDTO;
import com.anunciadores.dto.ProgramationDto;
import com.anunciadores.dto.ServiceDTO;
import com.anunciadores.dto.ServicioResponseDto;
import com.anunciadores.model.Coordinador;
import com.anunciadores.model.Ministerio;
import com.anunciadores.model.Persona;
import com.anunciadores.security.CurrentUser;
import com.anunciadores.service.interfaces.IMInisteryService;
import com.anunciadores.service.interfaces.INotificacionService;
import com.anunciadores.service.interfaces.IPersonaService;
import com.anunciadores.service.interfaces.IServicioService;
import com.anunciadores.util.UtilDate;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Programacion de servicios: agenda, asignaciones, coordinadores e informe del coordinador.
 */
@RestController
@RequestMapping("/api")
public class ProgramacionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgramacionController.class);

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private IMInisteryService ministeryService;

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private INotificacionService notificacionService;

    @Autowired
    private UtilDate utilDate;

    @GetMapping("/events")
    public ResponseEntity<ProgramationDto> events() throws ParseException {
        ProgramationDto result = this.servicioService.findNextServices(this.utilDate.cargarfechaActualBogotaDate());
        return okSiTieneMinisterios(result, "No hay servicios programados próximamente");
    }

    @GetMapping("/findprog")
    public ResponseEntity<ProgramationDto> findprog(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") String date) throws ParseException {
        ProgramationDto result = this.servicioService.findServices(this.utilDate.convertStringToDate(date));
        return okSiTieneMinisterios(result, "No se encontró programación para esta fecha");
    }

    @PostMapping("/updateprog")
    public ResponseEntity<Void> updateprog(@RequestBody updateServiceRequest request) throws ParseException {
        this.ministeryService.updateService(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/schedule/persona/{id}")
    public ResponseEntity<List<ServicioResponseDto>> getProgramacionPersona(@PathVariable Integer id)
            throws ParseException {
        CurrentUser.requireSelfOrAdmin(id);
        return ResponseEntity.ok(this.servicioService.buscarProgramacionMes(id));
    }

    @GetMapping("/findSchedule")
    public ResponseEntity<List<ServicioResponseDto>> consultarMiProgramacion(@RequestParam String fecha,
                                                                             @RequestParam int idMinisterio)
            throws ParseException {
        Date fechaD = this.utilDate.convertStringToDate(fecha);
        return ResponseEntity.ok(this.servicioService.findProgramacionByDateAndMinisterio(fechaD, idMinisterio));
    }

    @PostMapping("/savecordinador")
    public ResponseEntity<Boolean> saveCordinador(@RequestBody CoordinadorDTO cordinador) throws ParseException {
        Coordinador existente = this.servicioService.findCoordinadorByFecha(
                this.utilDate.convertStringToDate(cordinador.getFechaString()));
        Boolean guardado = existente == null
                ? this.servicioService.saveCoordinado(cordinador)
                : this.servicioService.updateCoordinador(existente, cordinador.getIdPersona());
        if (!Boolean.TRUE.equals(guardado)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar coordinador");
        }
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @PostMapping("/saveInformCoordinator")
    public ResponseEntity<String> saveInformCoordinator(@RequestBody CoordinadorDTO cordinador) {
        if (cordinador.getPersona() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Falta la persona del informe");
        }
        CurrentUser.requireSelfOrAdmin(cordinador.getPersona().getId());
        Persona per = this.personaService.findPersonaById(cordinador.getPersona().getId());
        cordinador.setPersona(per);
        if (!Boolean.TRUE.equals(this.servicioService.saveCoordinadorEntity(cordinador))) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al guardar informe del coordinador");
        }
        return ResponseEntity.ok("informe guardado exitosamente");
    }

    @PostMapping("/saveService")
    public ResponseEntity<List<Persona>> saveService(@RequestBody List<ServiceDTO> request) {
        List<Persona> asignados = this.servicioService.saveProgram(request);
        for (ServiceDTO dto : request) {
            if (dto.getIdPersona() != null && dto.getIdMinisterio() != null) {
                notificarAsignacion(dto);
            }
        }
        return ResponseEntity.ok(asignados);
    }

    /** La notificacion es secundaria: si falla no debe deshacer la programacion. */
    private void notificarAsignacion(ServiceDTO dto) {
        try {
            Integer idPersona = Integer.parseInt(dto.getIdPersona());
            Integer idMinisterio = Integer.parseInt(dto.getIdMinisterio());
            String posicion = dto.getIdPosicion() != null
                    ? this.servicioService.findPosicion(Integer.valueOf(dto.getIdPosicion())).getNombrePosicion()
                    : "Servicio";
            Ministerio min = this.servicioService.findByidMnisterio(idMinisterio);
            String nombreMinisterio = min != null ? min.getNombre() : "Ministerio";
            LocalDate fechaLocal = dto.getFechaServicio();
            Date fechaServicio = fechaLocal != null
                    ? Date.from(fechaLocal.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    : new Date();
            this.notificacionService.crearNotificacionAsignacion(
                    idPersona, fechaServicio, idMinisterio, nombreMinisterio, posicion);
        } catch (Exception ex) {
            LOGGER.warn("Error al crear notificación para servicio", ex);
        }
    }

    private ResponseEntity<ProgramationDto> okSiTieneMinisterios(ProgramationDto result, String mensajeVacio) {
        if (result == null || result.getMinistries() == null || result.getMinistries().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, mensajeVacio);
        }
        return ResponseEntity.ok(result);
    }
}
