package com.maxescobar.registro_java_firebase.Models;

import java.util.Objects;

public class Registro {
    private String idRegistro;
    private String nombre;
    private String telefono;
    private String detalle;
    private String alias;
    private String fechaRegistro;
    private String timestamp;

    public Registro(String idRegistro, String nombre, String telefono, String detalle, String alias, String fechaRegistro, String timestamp) {
        this.idRegistro = idRegistro;
        this.nombre = nombre;
        this.telefono = telefono;
        this.detalle = detalle;
        this.alias = alias;
        this.fechaRegistro = fechaRegistro;
        this.timestamp = timestamp;
    }

    public String getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registro registro = (Registro) o;
        return Objects.equals(idRegistro, registro.idRegistro) && Objects.equals(nombre, registro.nombre) && Objects.equals(telefono, registro.telefono) && Objects.equals(detalle, registro.detalle) && Objects.equals(alias, registro.alias) && Objects.equals(fechaRegistro, registro.fechaRegistro) && Objects.equals(timestamp, registro.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRegistro, nombre, telefono, detalle, alias, fechaRegistro, timestamp);
    }

    @Override
    public String toString() {
        return "Registro{" +
                "idRegistro='" + idRegistro + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", detalle='" + detalle + '\'' +
                ", alias='" + alias + '\'' +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
