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
}
