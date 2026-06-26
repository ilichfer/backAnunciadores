package com.anunciadores.dto;

import java.util.List;

public class DashboardStatsDto {
    private int serviciosProximos;
    private int notificacionesPendientes;
    private int serviciosDelMes;
    private int totalServiciosMes;
    private int porcentajeCumplimiento;
    private boolean tcdSubidoHoy;
    private int proximosCumpleanos;
    private List<ProximoServicioDto> proximosServicios;
    private List<ServicioPorMinisterioDto> serviciosPorMinisterio;

    public DashboardStatsDto() {}

    public int getServiciosProximos() {
        return serviciosProximos;
    }

    public void setServiciosProximos(int serviciosProximos) {
        this.serviciosProximos = serviciosProximos;
    }

    public int getNotificacionesPendientes() {
        return notificacionesPendientes;
    }

    public void setNotificacionesPendientes(int notificacionesPendientes) {
        this.notificacionesPendientes = notificacionesPendientes;
    }

    public int getServiciosDelMes() {
        return serviciosDelMes;
    }

    public void setServiciosDelMes(int serviciosDelMes) {
        this.serviciosDelMes = serviciosDelMes;
    }

    public int getTotalServiciosMes() {
        return totalServiciosMes;
    }

    public void setTotalServiciosMes(int totalServiciosMes) {
        this.totalServiciosMes = totalServiciosMes;
    }

    public int getPorcentajeCumplimiento() {
        return porcentajeCumplimiento;
    }

    public void setPorcentajeCumplimiento(int porcentajeCumplimiento) {
        this.porcentajeCumplimiento = porcentajeCumplimiento;
    }

    public boolean isTcdSubidoHoy() {
        return tcdSubidoHoy;
    }

    public void setTcdSubidoHoy(boolean tcdSubidoHoy) {
        this.tcdSubidoHoy = tcdSubidoHoy;
    }

    public int getProximosCumpleanos() {
        return proximosCumpleanos;
    }

    public void setProximosCumpleanos(int proximosCumpleanos) {
        this.proximosCumpleanos = proximosCumpleanos;
    }

    public List<ProximoServicioDto> getProximosServicios() {
        return proximosServicios;
    }

    public void setProximosServicios(List<ProximoServicioDto> proximosServicios) {
        this.proximosServicios = proximosServicios;
    }

    public List<ServicioPorMinisterioDto> getServiciosPorMinisterio() {
        return serviciosPorMinisterio;
    }

    public void setServiciosPorMinisterio(List<ServicioPorMinisterioDto> serviciosPorMinisterio) {
        this.serviciosPorMinisterio = serviciosPorMinisterio;
    }
}
