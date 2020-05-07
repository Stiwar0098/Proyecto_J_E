package com.brasma.sistemajuntas.entidades;

import java.io.Serializable;

public class PrestamoPrincipal implements Serializable {
    private String nombreUsuario, cedula;
    private double totalPrestado, totalPagado, pendiente;

    public PrestamoPrincipal() {
    }

    public PrestamoPrincipal(String nombreUsuario, String cedula, double totalPrestado, double totalPagado) {
        this.nombreUsuario = nombreUsuario;
        this.cedula = cedula;
        this.totalPrestado = totalPrestado;
        this.totalPagado = totalPagado;
        this.pendiente = totalPrestado - totalPagado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public double getTotalPrestado() {
        return totalPrestado;
    }

    public void setTotalPrestado(double totalPrestado) {
        this.totalPrestado = totalPrestado;
    }

    public double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }

    public double getPendiente() {
        return pendiente;
    }

    public void setPendiente(double pendiente) {
        this.pendiente = pendiente;
    }
}
