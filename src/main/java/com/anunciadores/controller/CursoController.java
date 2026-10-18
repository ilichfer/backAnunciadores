package com.anunciadores.controller;

import com.anunciadores.dto.CursoDto;
import com.anunciadores.dto.PersonaDto;
import com.anunciadores.model.Curso;
import com.anunciadores.model.Inscripciones;
import com.anunciadores.model.NotasCurso;
import com.anunciadores.model.Persona;
import com.anunciadores.repository.INotasCursoRepo;
import com.anunciadores.repository.IPersonaRepo;
import com.anunciadores.repository.InscripcionRepo;
import com.anunciadores.security.CurrentUser;
import com.anunciadores.service.interfaces.ICursoService;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Cursos, estudiantes inscritos, profesor y notas.
 */
@RestController
@RequestMapping("/api")
public class CursoController {

    @Autowired
    private ICursoService cursoService;

    @Autowired
    private InscripcionRepo inscripcionRepo;

    @Autowired
    private IPersonaRepo personaRepo;

    @Autowired
    private INotasCursoRepo notasCursoRepo;

    @GetMapping("/cursos")
    public ResponseEntity<List<Curso>> listarCursos() {
        return ResponseEntity.ok(cursoService.findAll());
    }

    @GetMapping("/cursos/{id}")
    public ResponseEntity<Curso> getCurso(@PathVariable int id) {
        return ResponseEntity.ok(buscarCurso(id));
    }

    @PostMapping("/cursos")
    public ResponseEntity<Curso> crearCurso(@RequestBody CursoDto cursoDto) throws ParseException {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.save(cursoDto));
    }

    @PutMapping("/cursos/{id}")
    public ResponseEntity<Curso> actualizarCurso(@PathVariable int id, @RequestBody CursoDto cursoDto) throws ParseException {
        cursoDto.setId(id);
        return ResponseEntity.ok(cursoService.save(cursoDto));
    }

    @PutMapping("/cursos/{id}/desactivar")
    public ResponseEntity<String> desactivarCurso(@PathVariable int id) throws ParseException {
        cursoService.desactivarCurso(buscarCurso(id));
        return ResponseEntity.ok("Curso desactivado correctamente");
    }

    @GetMapping("/cursos/{id}/estudiantes")
    public ResponseEntity<List<Persona>> listarEstudiantesCurso(@PathVariable int id) {
        return ResponseEntity.ok(estudiantesDelCurso(id));
    }

    @PostMapping("/cursos/{id}/estudiantes")
    public ResponseEntity<String> inscribirEstudiante(@PathVariable int id, @RequestBody Map<String, Integer> body) {
        Integer idPersona = body.get("idPersona");
        if (idPersona == null) {
            return ResponseEntity.badRequest().body("idPersona es requerido");
        }
        if (inscripcionRepo.findByIdCursoAndIdPersona(id, idPersona).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El estudiante ya está inscrito en este curso");
        }
        Inscripciones inscripcion = new Inscripciones();
        inscripcion.setIdCurso(id);
        inscripcion.setIdPersona(idPersona);
        inscripcionRepo.save(inscripcion);
        return ResponseEntity.status(HttpStatus.CREATED).body("Estudiante inscrito correctamente");
    }

    @DeleteMapping("/cursos/{id}/estudiantes/{idPersona}")
    @Transactional
    public ResponseEntity<String> desinscribirEstudiante(@PathVariable int id, @PathVariable int idPersona) {
        inscripcionRepo.deleteByIdCursoAndIdPersona(id, idPersona);
        return ResponseEntity.ok("Estudiante desinscrito correctamente");
    }

    @GetMapping("/cursos/{id}/profesor")
    public ResponseEntity<Persona> getProfesorCurso(@PathVariable int id) {
        Persona profesor = buscarCurso(id).getProfesor();
        if (profesor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay profesor asignado a este curso");
        }
        return ResponseEntity.ok(profesor);
    }

    @PutMapping("/cursos/{id}/profesor")
    public ResponseEntity<String> asignarProfesor(@PathVariable int id, @RequestBody Map<String, Integer> body) throws ParseException {
        Integer idPersona = body.get("idPersona");
        if (idPersona == null) {
            return ResponseEntity.badRequest().body("idPersona es requerido");
        }
        Curso curso = buscarCurso(id);
        if (!personaRepo.existsById(idPersona)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
        }
        CursoDto cursoDto = new CursoDto();
        cursoDto.setId(curso.getId());
        cursoDto.setNombreCurso(curso.getNombreCurso());
        cursoDto.setFechaInicio(curso.getFechaInicio());
        cursoDto.setFechaFin(curso.getFechaFin());
        cursoDto.setValorTotal(curso.getValorTotal());
        cursoDto.setProfesor(idPersona);
        cursoService.save(cursoDto);
        return ResponseEntity.ok("Profesor asignado correctamente");
    }

    @GetMapping("/cursos/{id}/notas")
    public ResponseEntity<List<PersonaDto>> listarNotasCurso(@PathVariable int id) throws ParseException {
        return ResponseEntity.ok(cursoService.buscarNotasXPersonas(id, estudiantesDelCurso(id)));
    }

    @PostMapping("/cursos/{id}/notas")
    public ResponseEntity<NotasCurso> guardarNota(@PathVariable int id, @RequestBody Map<String, Object> body)
            throws ParseException {
        if (!(body.get("idPersona") instanceof Number)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idPersona es requerido");
        }
        int idPersona = ((Number) body.get("idPersona")).intValue();
        NotasCurso notas = cursoService.findNotasByCurso(id, idPersona);
        if (notas == null) {
            notas = new NotasCurso();
            notas.setId(id * 100000 + idPersona);
            notas.setCurso(buscarCurso(id));
            notas.setPersona(personaRepo.findById(idPersona).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada")));
        }
        notas.setNotaMaestro(nota(body, "notaMaestro"));
        notas.setNotaAsistencia(nota(body, "notaAsistencia"));
        notas.setNotaPractica(nota(body, "notaPractica"));
        notas.setNotaExamenFinal(nota(body, "notaExamenFinal"));
        return ResponseEntity.ok(cursoService.saveNotasCurso(notas));
    }

    /** Historial de notas: cada persona ve el suyo; ADMIN puede ver el de cualquiera. */
    @GetMapping("/personas/{id}/notas-historico")
    public ResponseEntity<List<NotasCurso>> getHistoricoNotas(@PathVariable int id) {
        CurrentUser.requireSelfOrAdmin(id);
        return ResponseEntity.ok(notasCursoRepo.findHistoricoNotasConCurso(id));
    }

    private Curso buscarCurso(int id) {
        Curso curso = cursoService.findCursoById(id);
        if (curso == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado");
        }
        return curso;
    }

    private List<Persona> estudiantesDelCurso(int idCurso) {
        List<Persona> estudiantes = new ArrayList<>();
        for (Inscripciones ins : inscripcionRepo.findByIdCurso(idCurso)) {
            personaRepo.findById(ins.getIdPersona()).ifPresent(estudiantes::add);
        }
        return estudiantes;
    }

    private static double nota(Map<String, Object> body, String campo) {
        Object valor = body.get(campo);
        return valor instanceof Number ? ((Number) valor).doubleValue() : 0;
    }
}
