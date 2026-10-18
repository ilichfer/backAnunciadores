package com.anunciadores.service;

import com.anunciadores.model.Auditoria;
import com.anunciadores.repository.IAuditoriaRepo;
import com.anunciadores.security.AuthenticatedUser;
import com.anunciadores.security.CurrentUser;
import com.anunciadores.security.RateLimiter;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Bitacora de cambios importantes: quien hizo que y cuando.
 * Si guardar el registro falla, la accion principal no se ve afectada.
 */
@Service
public class AuditoriaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditoriaService.class);

    /** Acciones registradas (se usan tambien como filtro en la pantalla de bitacora). */
    public static final String CAMBIO_ROL = "CAMBIO_ROL";
    public static final String CAMBIO_ESTADO_USUARIO = "CAMBIO_ESTADO_USUARIO";
    public static final String REGISTRO_PERSONA = "REGISTRO_PERSONA";
    public static final String CAMBIO_CONTRASENA = "CAMBIO_CONTRASENA";
    public static final String PROGRAMACION = "PROGRAMACION";
    public static final String COORDINADOR = "COORDINADOR";
    public static final String MINISTERIO = "MINISTERIO";
    public static final String NOTA = "NOTA";
    public static final String CURSO = "CURSO";
    public static final String CONFIGURACION = "CONFIGURACION";
    public static final String IMAGEN_MES = "IMAGEN_MES";
    public static final String CONFIRMACION_TURNO = "CONFIRMACION_TURNO";
    public static final String WHATSAPP = "WHATSAPP";

    @Autowired
    private IAuditoriaRepo auditoriaRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(String accion, String entidad, Object idEntidad, String descripcion) {
        try {
            Auditoria a = new Auditoria();
            a.setFecha(new Date());
            a.setAccion(accion);
            a.setEntidad(entidad);
            a.setIdEntidad(idEntidad != null ? String.valueOf(idEntidad) : null);
            a.setDescripcion(recortar(descripcion, 1000));
            Optional<AuthenticatedUser> usuario = CurrentUser.optional();
            if (usuario.isPresent()) {
                a.setIdUsuario(usuario.get().getId());
                String nombre = usuario.get().getNombre();
                a.setUsuario(recortar(nombre != null ? nombre : "Cédula " + usuario.get().getCedula(), 150));
            } else {
                a.setUsuario("Sin sesión (público)");
            }
            a.setIp(ipActual());
            auditoriaRepo.save(a);
        } catch (Exception e) {
            LOGGER.error("No se pudo registrar en la bitácora: {} {}", accion, descripcion, e);
        }
    }

    public Page<Auditoria> buscar(Date desde, Date hasta, String accion, String texto, int pagina, int tamano) {
        String filtroTexto = texto == null || texto.isBlank() ? null : "%" + texto.trim().toLowerCase() + "%";
        String filtroAccion = accion == null || accion.isBlank() ? null : accion;
        return auditoriaRepo.buscar(desde, hasta, filtroAccion, filtroTexto,
                PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamano, 1), 200)));
    }

    private static String ipActual() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return recortar(RateLimiter.clientIp(request), 45);
        }
        return null;
    }

    private static String recortar(String texto, int max) {
        if (texto == null) {
            return null;
        }
        return texto.length() <= max ? texto : texto.substring(0, max);
    }
}
