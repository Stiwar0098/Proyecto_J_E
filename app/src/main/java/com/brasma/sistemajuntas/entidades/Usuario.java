package com.brasma.sistemajuntas.entidades;

public class Usuario {
    private String nombre, cedula, numero, usuario, contraseña, uid;

    public Usuario() {
    }

    public Usuario(String nombre, String cedula, String numero, String usuario, String contraseña, String uid) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.numero = numero;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
