package com.anunciadores.service.interfaces;
import com.anunciadores.model.Configuracion;
import java.util.Map;

public interface IConfiguracionService {
    Map<String, Double> findPesosNota();
    void savePesosNota(Map<String, Double> pesos);
}
