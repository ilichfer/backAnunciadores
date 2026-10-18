package com.anunciadores.controller;

import com.anunciadores.dto.DashboardStatsDto;
import com.anunciadores.dto.PersonaDto;
import com.anunciadores.dto.ProximoServicioDto;
import com.anunciadores.dto.ServicioPorMinisterioDto;
import com.anunciadores.model.Ministerio;
import com.anunciadores.model.Servicio;
import com.anunciadores.model.Tdc;
import com.anunciadores.security.CurrentUser;
import com.anunciadores.service.interfaces.INotificacionService;
import com.anunciadores.service.interfaces.IPersonaService;
import com.anunciadores.service.interfaces.IServicioService;
import com.anunciadores.service.interfaces.ITdcService;
import com.anunciadores.util.UtilDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Estadisticas personales del dashboard.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private INotificacionService notificacionService;

    @Autowired
    private ITdcService tdcService;

    @Autowired
    private IPersonaService personaService;

    @Autowired
    private UtilDate utilDate;

    @GetMapping("/stats/{idPersona}")
    public ResponseEntity<DashboardStatsDto> getDashboardStats(@PathVariable Integer idPersona) throws ParseException {
        CurrentUser.requireSelfOrAdmin(idPersona);
        DashboardStatsDto stats = new DashboardStatsDto();

        Date fechaActual = utilDate.cargarfechaActualBogotaDate();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date fechaLimite7dias = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date inicioMes = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date finMes = cal.getTime();

        List<Servicio> serviciosProximos =
                servicioService.getServiciosProximosPersona(idPersona, fechaActual, fechaLimite7dias);
        stats.setServiciosProximos(serviciosProximos.size());

        List<Servicio> serviciosMes = servicioService.getServiciosMesPersona(idPersona, inicioMes, finMes);
        int totalServiciosMes = serviciosMes.size();
        int serviciosAsistidos = 0;
        for (Servicio s : serviciosMes) {
            if ("ASISTIO".equalsIgnoreCase(s.getAsistencia())) {
                serviciosAsistidos++;
            }
        }
        stats.setServiciosDelMes(totalServiciosMes);
        stats.setTotalServiciosMes(totalServiciosMes);
        stats.setPorcentajeCumplimiento(totalServiciosMes > 0 ? (serviciosAsistidos * 100) / totalServiciosMes : 0);

        Integer countNotif = notificacionService.countNotificacionesNoLeidas(idPersona);
        stats.setNotificacionesPendientes(countNotif != null ? countNotif : 0);

        List<Tdc> tdcHoy = tdcService.getTdcByFechaAndPersonaList(fechaActual, idPersona);
        stats.setTcdSubidoHoy(tdcHoy != null && !tdcHoy.isEmpty());

        stats.setProximosCumpleanos(contarCumpleanosProximos(personaService.findBirthdayByMonth()));

        List<ProximoServicioDto> proximosServicios = new ArrayList<>();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        for (Servicio s : serviciosProximos) {
            if (s.getFechaServicio() != null) {
                String nombreMinisterio = "";
                if (s.getIdMinisterio() > 0) {
                    Ministerio min = servicioService.findByidMnisterio(s.getIdMinisterio());
                    if (min != null) {
                        nombreMinisterio = min.getNombre();
                    }
                }
                proximosServicios.add(new ProximoServicioDto(formato.format(s.getFechaServicio()), "", nombreMinisterio, ""));
            }
        }
        stats.setProximosServicios(proximosServicios);

        List<ServicioPorMinisterioDto> serviciosPorMinisterio = new ArrayList<>();
        for (Object[] row : servicioService.getServiciosPorMinisterio(idPersona, inicioMes, finMes)) {
            String nombreMin = row[0] != null ? row[0].toString() : "";
            int cantidad = row[1] != null ? ((Number) row[1]).intValue() : 0;
            serviciosPorMinisterio.add(new ServicioPorMinisterioDto(nombreMin, cantidad));
        }
        stats.setServiciosPorMinisterio(serviciosPorMinisterio);

        return ResponseEntity.ok(stats);
    }

    /** Cumpleanos del mes entre hoy y los proximos 7 dias (fecha en formato dd/MM/...). */
    private int contarCumpleanosProximos(List<PersonaDto> cumpleaneros) {
        int diaHoy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int proximos = 0;
        for (PersonaDto p : cumpleaneros) {
            if (p.getFechanacimiento() == null) {
                continue;
            }
            String[] partes = p.getFechanacimiento().split("/");
            if (partes.length >= 2) {
                try {
                    int diaCumple = Integer.parseInt(partes[0]);
                    if (diaCumple >= diaHoy && diaCumple <= diaHoy + 7) {
                        proximos++;
                    }
                } catch (NumberFormatException ignored) {
                    // fecha con formato inesperado: no se cuenta
                }
            }
        }
        return proximos;
    }
}
