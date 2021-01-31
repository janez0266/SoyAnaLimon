package com.example.soyanalimon;


//todo gregar en el menu el orden que se necesita para los registros (por nombre)
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MainProveedores extends AppCompatActivity {
    ListView proveedores_list;
    ArrayList<String> listado_proveedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_proveedores);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Registro de un nuevo Proveedor", Toast.LENGTH_LONG).show();
                nuevo_proveedor();
            }
        });
        proveedores_list = (ListView) findViewById(R.id.proveedores_list); //clientes_list es el id del listview del layout
        proveedores_list.setEmptyView(findViewById(R.id.emptyListView));
        verListado();
    }

    private ArrayList<String> ListaProveedores() {

        ArrayList<String> datos = new ArrayList<String>();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery("Select * from proveedores", null);
        if (c.moveToFirst()) {
            do {
                String linea1 = " " + c.getInt(0)
                        + " " + c.getString(2)
                        + " \n Tlf: " + c.getString(3);
                //                       + " " + c.getString(3)
                //                       + " " + c.getString(4);
                datos.add(linea1);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return datos;
    }

    public void verListado() {
        listado_proveedores = ListaProveedores();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item,         // LAYOUT TIPO ITEMVIEW PERSONALIZAD  list_item.xml
                R.id.text,                  // textview a usar.. esta dentro de  list_item.xml
                listado_proveedores);          //DATOS DEL ARREGLO QUE SE MOSTRARA

        proveedores_list.setAdapter(adapter);
        proveedores_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //int pos = position + 1;

                int _proveedor_id = Integer.parseInt(listado_proveedores.get(position).split(" ")[1]);
                Toast.makeText(getApplicationContext(), "Muestran los detalles del Proveedor registrado", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(MainProveedores.this, ProveedoresView.class);
                intent2.putExtra("proveedor_id", _proveedor_id);
                startActivity(intent2);



            }
        });
        proveedores_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int _proveedor_id = Integer.parseInt(listado_proveedores.get(position).split(" ")[1]);

                AlertDialog alertDialog = new AlertDialog.Builder(MainProveedores.this)
                        .setTitle("Eliminar Registro: " + _proveedor_id)
                        .setMessage("Esta seguro de Eliminar el Proveedor?")
                        .setIcon(R.drawable.papelera)
                        .setPositiveButton("si", new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(getApplicationContext(), "Operacion no permitida", Toast.LENGTH_LONG).show();

                            }

                        })
                        .setNegativeButton("no", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //no hacer nada
                            }
                        })
                        .create();
                alertDialog.show();
                // también el evento onListItemClick
                return true;
            }
        });

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        verListado();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_proveedores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.nuevo_proveedor) {
            nuevo_proveedor();
            ;

            return true;
        }
        if (id == R.id.nr_registros) {
            DBHelper dbHelper = new DBHelper(MainProveedores.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT proveedor_id FROM proveedores", null);

            Toast.makeText(getApplicationContext(), "Registros totales:  " + c.getCount(), Toast.LENGTH_SHORT).show();
            c.close();
            db.close();
            return true;
        }

        if (id == R.id.cerrar) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void nuevo_proveedor() {

        Intent intent3 = new Intent(MainProveedores.this, ProveedoresModify.class);
        intent3.putExtra("proveedor_id", 0);
        startActivity(intent3);

    }
}