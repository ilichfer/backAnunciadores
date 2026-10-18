package com.anunciadores.service.interfaces;
import java.util.Map;

public interface IConfiguracionService {
    Map<String, Double> findPesosNota();
    void savePesosNota(Map<String, Double> pesos);

    /** Interruptores de los envios automaticos por WhatsApp (todos apagados por defecto). */
    Map<String, Boolean> findPreferenciasEnvio();
    void savePreferenciasEnvio(Map<String, Boolean> preferencias);
    boolean isEnvioActivo(String clave);
}
