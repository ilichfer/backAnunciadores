package com.anunciadores.service;
import com.anunciadores.model.Configuracion;
import com.anunciadores.repository.IConfiguracionRepo;
import com.anunciadores.service.interfaces.IConfiguracionService;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfiguracionServiceImpl implements IConfiguracionService {

    private static final List<String> CLAVES_PESOS = Arrays.asList(
        "PESO_MAESTRO", "PESO_ASISTENCIA", "PESO_PRACTICA", "PESO_EXAMEN"
    );

    private static final Map<String, Double> DEFAULTS = new LinkedHashMap<>();
    static {
        DEFAULTS.put("PESO_MAESTRO", 30.0);
        DEFAULTS.put("PESO_ASISTENCIA", 20.0);
        DEFAULTS.put("PESO_PRACTICA", 20.0);
        DEFAULTS.put("PESO_EXAMEN", 30.0);
    }

    @Autowired
    private IConfiguracionRepo configuracionRepo;

    @Override
    public Map<String, Double> findPesosNota() {
        Map<String, Double> pesos = new LinkedHashMap<>();
        List<Configuracion> configs = configuracionRepo.findByClaveIn(CLAVES_PESOS);
        for (Configuracion c : configs) {
            pesos.put(c.getClave(), c.getValor());
        }
        for (String clave : CLAVES_PESOS) {
            pesos.putIfAbsent(clave, DEFAULTS.get(clave));
        }
        return pesos;
    }

    @Override
    @Transactional
    public void savePesosNota(Map<String, Double> pesos) {
        for (String clave : CLAVES_PESOS) {
            Double valor = pesos.get(clave);
            if (valor == null) valor = DEFAULTS.get(clave);
            Configuracion config = configuracionRepo.findByClave(clave).orElse(null);
            if (config == null) {
                String desc;
                switch (clave) {
                    case "PESO_MAESTRO": desc = "Peso del componente Maestro en la calificación"; break;
                    case "PESO_ASISTENCIA": desc = "Peso del componente Asistencia en la calificación"; break;
                    case "PESO_PRACTICA": desc = "Peso del componente Práctica en la calificación"; break;
                    case "PESO_EXAMEN": desc = "Peso del componente Examen Final en la calificación"; break;
                    default: desc = clave;
                }
                config = new Configuracion(clave, valor, desc);
            } else {
                config.setValor(valor);
            }
            configuracionRepo.save(config);
        }
    }

    // ── Preferencias de envios automaticos ────────────────────────────────────

    public static final String WA_CUMPLEANOS = "WA_CUMPLEANOS";
    public static final String WA_RECORDATORIO_TCD = "WA_RECORDATORIO_TCD";
    public static final String WA_AUSENCIAS = "WA_AUSENCIAS";
    public static final String WA_RESUMEN_SEMANAL = "WA_RESUMEN_SEMANAL";

    private static final Map<String, String> PREFERENCIAS = new LinkedHashMap<>();
    static {
        PREFERENCIAS.put(WA_CUMPLEANOS, "Enviar saludo de cumpleaños por WhatsApp");
        PREFERENCIAS.put(WA_RECORDATORIO_TCD, "Recordar por WhatsApp a quien no ha subido su TCD");
        PREFERENCIAS.put(WA_AUSENCIAS, "Avisar por WhatsApp al coordinador cuando alguien no puede servir");
        PREFERENCIAS.put(WA_RESUMEN_SEMANAL, "Enviar el resumen semanal por WhatsApp a los administradores");
    }

    @Override
    public Map<String, Boolean> findPreferenciasEnvio() {
        Map<String, Boolean> resultado = new LinkedHashMap<>();
        Map<String, Double> guardadas = new LinkedHashMap<>();
        for (Configuracion c : configuracionRepo.findByClaveIn(new java.util.ArrayList<>(PREFERENCIAS.keySet()))) {
            guardadas.put(c.getClave(), c.getValor());
        }
        for (String clave : PREFERENCIAS.keySet()) {
            Double valor = guardadas.get(clave);
            resultado.put(clave, valor != null && valor >= 1);
        }
        return resultado;
    }

    @Override
    @Transactional
    public void savePreferenciasEnvio(Map<String, Boolean> preferencias) {
        for (Map.Entry<String, String> entrada : PREFERENCIAS.entrySet()) {
            Boolean activo = preferencias.get(entrada.getKey());
            if (activo == null) {
                continue;
            }
            Configuracion config = configuracionRepo.findByClave(entrada.getKey())
                    .orElse(new Configuracion(entrada.getKey(), 0, entrada.getValue()));
            config.setValor(activo ? 1 : 0);
            configuracionRepo.save(config);
        }
    }

    @Override
    public boolean isEnvioActivo(String clave) {
        return configuracionRepo.findByClave(clave).map(c -> c.getValor() >= 1).orElse(false);
    }
}
