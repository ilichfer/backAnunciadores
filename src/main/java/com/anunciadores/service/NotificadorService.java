package com.anunciadores.service;

import com.anunciadores.model.Notificacion;
import com.anunciadores.model.Persona;
import com.anunciadores.service.interfaces.IConfiguracionService;
import com.anunciadores.service.interfaces.INotificacionService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Punto unico para avisar a una persona: notificacion en la app (campana) + push al celular,
 * y opcionalmente WhatsApp si el administrador activo ese tipo de envio.
 */
@Service
public class NotificadorService {

    @Autowired
    private INotificacionService notificacionService;

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private IConfiguracionService configuracionService;

    /** Guarda la notificacion en la app; el push se envia desde NotificacionServiceImpl. */
    public Notificacion notificar(Integer idPersona, String tipo, String titulo, String mensaje) {
        Notificacion n = new Notificacion();
        n.setIdPersona(idPersona);
        n.setTipo(tipo);
        n.setTitulo(titulo);
        n.setMensaje(mensaje);
        n.setLeida(false);
        n.setEnviada(false);
        n.setFechaCreacion(new Date());
        return notificacionService.crearNotificacion(n);
    }

    /**
     * Envia WhatsApp solo si el interruptor indicado esta activo y la persona tiene celular.
     * Devuelve true si se envio.
     */
    public boolean whatsapp(Persona persona, String texto, String preferencia) {
        if (persona == null || !configuracionService.isEnvioActivo(preferencia)) {
            return false;
        }
        return whatsAppService.enviarTexto(persona.getCelular(), texto);
    }

    /** Igual que whatsapp(...) pero en segundo plano, para no demorar la respuesta al usuario. */
    @Async
    public void whatsappAsync(Persona persona, String texto, String preferencia) {
        whatsapp(persona, texto, preferencia);
    }
}
