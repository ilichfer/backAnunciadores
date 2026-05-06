package com.anunciadores.dto;

public class AddPersonaRequest {
    private Integer idPersona;
    private Integer idMinisterio;

    public Integer getIdMinisterio() {
        return this.idMinisterio;
    }

    public void setIdMinisterio(Integer idMinisterio) {
        this.idMinisterio = idMinisterio;
    }

    public Integer getIdPersona() {
        return this.idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }
}