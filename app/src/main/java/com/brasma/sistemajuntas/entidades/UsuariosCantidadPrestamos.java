package com.brasma.sistemajuntas.entidades;

public class UsuariosCantidadPrestamos {
    private int cantidad;

    public UsuariosCantidadPrestamos(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
