package com.anunciadores.dto;
public class ProximoServicioDto {
    private String fecha;
    private String hora;
    private String ministerio;
    private String posicion;
    public ProximoServicioDto() {}
    public ProximoServicioDto(String fecha, String hora, String ministerio, String posicion) {
        this.fecha = fecha;
        this.hora = hora;
        this.ministerio = ministerio;
        this.posicion = posicion;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }
    public String getMinisterio() {
        return ministerio;
    }
    public void setMinisterio(String ministerio) {
        this.ministerio = ministerio;
    }
    public String getPosicion() {
        return posicion;
    }
    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }
}
