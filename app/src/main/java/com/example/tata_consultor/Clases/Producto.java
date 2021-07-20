package com.example.tata_consultor.Clases;

import java.io.Serializable;

public class Producto implements Serializable {

    private Integer CodigoInt;
    private String Codigo;
    private String Nombre = null;
    private String Moneda = null;
    private String Precio = null;
    private String CodBarras = null;
    private String Prioridad = null;
    private String Aux = null;
    private String Estado = null;



    public Producto() {
    }

    public Producto(Integer codigoInt,String codigo ,String nombre, String moneda, String precio, String codBarras, String prioridad, String aux,String estado) {
        CodigoInt = codigoInt;
        Codigo = codigo;
        Nombre = nombre;
        Moneda = moneda;
        Precio = precio;
        CodBarras = codBarras;
        Prioridad = prioridad;
        Aux = aux;
        Estado = estado;

    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public Integer getCodigoInt() {
        return CodigoInt;
    }

    public void setCodigoInt(Integer codigoInt) {
        CodigoInt = codigoInt;
    }



    public String  getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getMoneda() {
        return Moneda;
    }

    public void setMoneda(String moneda) {
        Moneda = moneda;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getCodBarras() {
        return CodBarras;
    }

    public void setCodBarras(String codBarras) {
        CodBarras = codBarras;
    }

    public String getPrioridad() {
        return Prioridad;
    }

    public void setPrioridad(String prioridad) {
        Prioridad = prioridad;
    }

    public String getAux() {
        return Aux;
    }

    public void setAux(String aux) {
        Aux = aux;
    }


}
