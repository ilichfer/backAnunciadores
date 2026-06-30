package com.anunciadores.dto;

public class ImagenMensualDto {

    private String url;
    private Integer mes;
    private Integer anio;
    private String tipo;

    public ImagenMensualDto() {
    }

    public ImagenMensualDto(String url, Integer mes, Integer anio, String tipo) {
        this.url = url;
        this.mes = mes;
        this.anio = anio;
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
