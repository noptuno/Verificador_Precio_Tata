package com.example.tata_consultor.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.tata_consultor.Clases.Producto;

import java.util.ArrayList;


public class ProductosDB {

        private SQLiteDatabase db;
        private ProductosDB.DBHelper dbHelper;

        public ProductosDB(Context context) {
            dbHelper = new ProductosDB.DBHelper(context);
        }


        private void openReadableDB() {
            db = dbHelper.getReadableDatabase();
        }

        private void openWriteableDB() {
            db = dbHelper.getWritableDatabase();
        }

        private void closeDB() {
            if (db != null) {
                db.close();
            }
        }


        private ContentValues clienteMapperContentValues(Producto producto) {
            ContentValues cv = new ContentValues();
            cv.put(ConstantsDB.PRO_CODIGOINT, producto.getCodigoInt());
            cv.put(ConstantsDB.PRO_CODIGO, producto.getCodigo());
            cv.put(ConstantsDB.PRO_NOMBRE, producto.getNombre());
            cv.put(ConstantsDB.PRO_MONEDA, producto.getMoneda());
            cv.put(ConstantsDB.PRO_CODBARRAS, producto.getCodBarras());
            cv.put(ConstantsDB.PRO_PRECIO, producto.getPrecio());
            cv.put(ConstantsDB.PRO_PRIORIDAD, producto.getPrioridad());
            cv.put(ConstantsDB.PRO_AUX, producto.getAux());
            cv.put(ConstantsDB.PRO_ESTADO, producto.getEstado());
            return cv;
        }

    public void eliminarProductos() {
        this.openWriteableDB();
        db.delete(ConstantsDB.TABLA_PRODUCTO, null, null);
        this.closeDB();
    }


    public void updatecodigoproduto(Producto producto) {

        this.openWriteableDB();
        String where = ConstantsDB.PRO_CODIGOINT + "= ?";
        db.update(ConstantsDB.TABLA_PRODUCTO, clienteMapperContentValues(producto), where, new String[]{String.valueOf(producto.getCodigoInt())});
        db.close();
    }


    public void eliminarProducto(int codigoProducto) {
        this.openWriteableDB();
        String where = ConstantsDB.PRO_CODIGOINT + "= ?";
        db.delete(ConstantsDB.TABLA_PRODUCTO, where, new String[]{String.valueOf(codigoProducto)});
        this.closeDB();
    }

    public long insertarProducto(Producto producto) {
        this.openWriteableDB();
        long rowID = db.insert(ConstantsDB.TABLA_PRODUCTO, null, clienteMapperContentValues(producto));
        this.closeDB();
        return rowID;
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, ConstantsDB.DB_NAME, null, ConstantsDB.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(ConstantsDB.TABLA_PRODUCTO_SQL);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }



    public ArrayList loadProducto() {

        ArrayList<Producto> list = new ArrayList<>();
        String where = ConstantsDB.PRO_CODIGOINT;
        this.openReadableDB();
        String[] campos = new String[]{ConstantsDB.PRO_CODIGOINT,ConstantsDB.PRO_CODIGO,ConstantsDB.PRO_NOMBRE,ConstantsDB.PRO_MONEDA, ConstantsDB.PRO_CODBARRAS, ConstantsDB.PRO_PRECIO,  ConstantsDB.PRO_PRIORIDAD, ConstantsDB.PRO_AUX, ConstantsDB.PRO_ESTADO};
        Cursor c = db.query(ConstantsDB.TABLA_PRODUCTO, campos, null, null, null, null, where +" DESC");

        try {
            while (c.moveToNext()) {
                Producto producto = new Producto();
                producto.setCodigoInt(c.getInt(0));
                producto.setCodigo(c.getString(1));
                producto.setNombre(c.getString(2));
                producto.setMoneda(c.getString(3));
                producto.setCodBarras(c.getString(4));
                producto.setPrecio(c.getString(5));
                producto.setPrioridad(c.getString(6));
                producto.setAux(c.getString(7));
                producto.setEstado(c.getString(8));
                list.add(producto);

            }
        } finally {
            c.close();
        }
        this.closeDB();
        return list;
    }

    }


