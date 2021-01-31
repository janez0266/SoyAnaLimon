package com.example.soyanalimon;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by janez0266 on 20/01/2021.
 */
public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "soyanalimon.db";


    public static String CREATE_TABLE_CLIENTES = "CREATE TABLE clientes ("
            //tipo string con datos
            + "cliente_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "dni TEXT, "
            + "nombre TEXT, "
            + "telefono TEXT, "
            + "direccion TEXT, "
            + "email TEXT, "
            + "nota TEXT, "
            + "url TEXT, "
            + "id_android TEXT)";

    public static String CREATE_TABLE_PEDIDOS = "CREATE TABLE pedidos ("
            + "pedido_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "cliente_id INTEGER, "
            + "fecha_pedido TEXT, "
            + "fecha_entrega TEXT, "
            + "monto TEXT, "
            + "vendedor TEXT, "
            + "entregado TEXT, "
            + "nota TEXT)";

    public static String CREATE_TABLE_PROVEEDORES = "CREATE TABLE proveedores ("
            + "proveedor_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "id_android TEXT, "
            + "nombre TEXT, "
            + "telefono TEXT, "
            + "email TEXT, "
            + "descripcion TEXT, "
            + "url TEXT, "
            + "instagram TEXT, "
            + "direccion TEXT)";
    public static String CREATE_TABLE_PRODUCTOS = "CREATE TABLE productos ("
            + "producto_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "cliente_id INTEGER, "
            + "pedido_id INTEGER, "
            + "nombre TEXT, "
            + "descripcion TEXT, "
            + "ruta_imagen TEXT, "
            + "precio TEXT, "
            + "favorito TEXT, "
            + "nota TEXT) ";

    DBHelper(Context context) {                        // constructor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {               // aqui se crea la base de datos
        //All necessary tables you like to create will create here

        db.execSQL(CREATE_TABLE_CLIENTES);
        db.execSQL(CREATE_TABLE_PEDIDOS);
        db.execSQL(CREATE_TABLE_PROVEEDORES);
        db.execSQL(CREATE_TABLE_PRODUCTOS);

        poblarDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS clientes");
        db.execSQL("DROP TABLE IF EXISTS pedidos");
        db.execSQL("DROP TABLE IF EXISTS productos");
        db.execSQL("DROP TABLE IF EXISTS proveedores");

        // Create tables again
        db.execSQL(CREATE_TABLE_CLIENTES);
        db.execSQL(CREATE_TABLE_PEDIDOS);
        db.execSQL(CREATE_TABLE_PROVEEDORES);
        db.execSQL(CREATE_TABLE_PRODUCTOS);

       poblarDB(db);


    }

    public void poblarDB(SQLiteDatabase db){
        db.execSQL("INSERT INTO clientes (id_android, dni, nombre, telefono, nota, direccion, email, url) " +
                "VALUES ('0', '25932191', 'Anais Reyes', '+54 9 11 2409-0795', 'Diseñadora Grafica', 'Padilla 1045, Villa Crespo, CABA', 'aplicacionesjca@gmail.com', 'https://linktr.ee/SoyAnaLimon')");
        db.execSQL("INSERT INTO clientes (id_android, dni, nombre, telefono, nota, direccion, email, url) " +
                "VALUES ('0', '25932191', 'Julio Miguel Añez', '+54 9 11 2655-1172', 'Gerente Administrativo', 'Padilla 1045, Villa Crespo, CABA', 'julioman300@gmail.com', 'https://janez0266.wixsite.com/jcacomputer')");
        db.execSQL("INSERT INTO clientes (id_android, dni, nombre, telefono, nota, direccion, email, url) " +
                "VALUES ('0', '7777777', 'JCA Computer', '+5891122552913', 'Reparacion de computadoras', 'Calle 95Ñ', 'aplicacionesjca@gmail.com', 'https://janez0266.wixsite.com/jcacomputer')");
        db.execSQL("INSERT INTO proveedores (id_android, nombre, telefono, email, descripcion, url, instagram, direccion)" +
                "VALUES ('0', 'Grafica UNO', '+54 9 11 48548384', 'graficacamargo@yahoo.com.ar', 'Diseño grafico, ploteos, banners, fotocopias, sublimacion, tazas, remeras y otros souvenirs', ' ', 'instagram.com/grafica_uno_', 'Camargo 608, CABA')");
        db.execSQL("INSERT INTO proveedores (id_android, nombre, telefono, email, descripcion, url, instagram, direccion)" +
                "VALUES ('0', 'COPIFIL Grafica digital', '+54 9 11 6962-7265', 'pedidos@copifil.com', 'Diseño grafico, ploteos, banners, fotocopias, sublimacion, tazas, remeras y otros souvenirs', 'www.copifil.com', 'instagram.com/copifil', 'Camargo 586, CABA')");
    }

}