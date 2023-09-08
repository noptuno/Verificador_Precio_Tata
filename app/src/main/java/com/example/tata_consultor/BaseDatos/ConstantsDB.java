package com.example.tata_consultor.BaseDatos;

public class ConstantsDB {
    //General
    public static final String DB_NAME = "tata8.db";
    public static final int DB_VERSION = 8;

    //TABLA PRODUCTOS

    public static final String TABLA_PRODUCTO = "Producto";
    public static final String PRO_CODIGOINT= "_CodigoInt";
    public static final String PRO_CODIGO = "Codigo";
    public static final String PRO_NOMBRE = "Nombre";
    public static final String PRO_MONEDA = "Moneda";
    public static final String PRO_CODBARRAS= "CodBarras";
    public static final String PRO_PRECIO = "Precio";
    public static final String PRO_PRIORIDAD = "Prioridad";
    public static final String PRO_AUX = "Aux";
    public static final String PRO_ESTADO = "Estado";





    public static final String TABLA_PRODUCTO_SQL =
            "CREATE TABLE  " + TABLA_PRODUCTO + "(" +
                    PRO_CODIGOINT + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PRO_CODIGO + " TEXT," +
                    PRO_NOMBRE + " TEXT," +
                    PRO_MONEDA + " TEXT," +
                    PRO_CODBARRAS + " TEXT," +
                    PRO_PRECIO + " TEXT," +
                    PRO_PRIORIDAD + " TEXT," +
                    PRO_AUX + " TEXT," +
                    PRO_ESTADO   + " TEXT);" ;




}
