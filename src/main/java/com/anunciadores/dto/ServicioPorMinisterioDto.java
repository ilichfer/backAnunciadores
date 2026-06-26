package com.anunciadores.dto;

public class ServicioPorMinisterioDto {
    private String ministerio;
    private int cantidad;

    public ServicioPorMinisterioDto() {}

    public ServicioPorMinisterioDto(String ministerio, int cantidad) {
        this.ministerio = ministerio;
        this.cantidad = cantidad;
    }

    public String getMinisterio() {
        return ministerio;
    }

    public void setMinisterio(String ministerio) {
        this.ministerio = ministerio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
