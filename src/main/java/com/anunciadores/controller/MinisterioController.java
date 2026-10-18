package com.anunciadores.controller;

import com.anunciadores.dto.AddPersonaRequest;
import com.anunciadores.dto.Ministry;
import com.anunciadores.dto.PersonaDto;
import com.anunciadores.dto.PosicionDto;
import com.anunciadores.service.interfaces.IMInisteryService;
import com.anunciadores.service.interfaces.IPersonaService;
import com.anunciadores.service.interfaces.IServicioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Ministerios, sus posiciones y las personas que pertenecen a cada uno.
 */
@RestController
@RequestMapping("/api")
public class MinisterioController {

    @Autowired
    private IMInisteryService ministeryService;

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private IPersonaService personaService;

    @GetMapping("/ministries")
    public ResponseEntity<List<Ministry>> ministries() {
        return ResponseEntity.ok(this.ministeryService.getAllministriesWithPositions());
    }

    @GetMapping("/ministries/{idMinistry}")
    public ResponseEntity<Ministry> getMinistry(@PathVariable Integer idMinistry) {
        Ministry ministry = this.ministeryService.getMinistryWithPositions(idMinistry);
        if (ministry == null || ministry.getPositions() == null || ministry.getPositions().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El ministerio no tiene posiciones configuradas");
        }
        return ResponseEntity.ok(ministry);
    }

    @GetMapping("/ministries/{id}/personas")
    public ResponseEntity<List<PersonaDto>> getPersonasByMinisterio(@PathVariable Integer id) {
        return ResponseEntity.ok(this.servicioService.findPersonaByidMnisterio(id));
    }

    @PostMapping("/ministries/addposition")
    public ResponseEntity<Void> addPositionToMinistry(@RequestBody PosicionDto posicionDto) {
        this.servicioService.savePosicion(posicionDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/ministeries/addperson")
    public ResponseEntity<Void> addPersonaToMinistry(@RequestBody AddPersonaRequest request) {
        this.ministeryService.agregarPersonasAMinisterio(request.getIdPersona(), request.getIdMinisterio());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/ministeries/{idMinisterio}/personas/{idPersona}")
    public ResponseEntity<List<PersonaDto>> deletePersonaFromMinistry(@PathVariable int idMinisterio,
                                                                      @PathVariable int idPersona) {
        this.personaService.eliminarPersonaMinisterio(idPersona, idMinisterio);
        return ResponseEntity.ok(this.servicioService.findPersonaByidMnisterio(idMinisterio));
    }
}
