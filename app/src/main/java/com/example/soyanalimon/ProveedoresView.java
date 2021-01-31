package com.example.soyanalimon;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

public class ProveedoresView extends AppCompatActivity {

    public static final int PICK_CONTACT_REQUEST = 1;
    private Uri contactUri;
    Button btnLlamar;
    Button btn_new_pedido;
    Button btnVerContacto;
    Button btnClose;
    Button btn_ver_lista;
    TextView txtNombre;
    TextView txtTelefono;
    TextView txtEmail;
    TextView txtDescripcion;
    TextView txtDir;
    TextView txtProveedorId;
    TextView txtInstagram;
    TextView txtUrl;
    TextView txtIdAndroid;

    public int _Proveedor_Id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores_view);

        btnClose = (Button) findViewById(R.id.btnClose);
//        btn_new_pedido = (Button) findViewById(R.id.btn_new_pedido);
//        btn_ver_lista = (Button) findViewById(R.id.btn_ver_lista);
//        btnVerContacto = (Button) findViewById(R.id.vercontacto);
        txtNombre = (TextView) findViewById(R.id.txtNombreP);
        txtTelefono = (TextView) findViewById(R.id.txtTelefonoP);
        txtEmail = (TextView) findViewById(R.id.txtEmailP);
        txtDir =(TextView) findViewById(R.id.txtDirP);
        txtDescripcion = (TextView) findViewById(R.id.txtDescripcionP);
        txtProveedorId = (TextView) findViewById(R.id.txtProveedorId);
        txtInstagram = (TextView) findViewById(R.id.txtInstagramP);
        txtUrl = (TextView) findViewById(R.id.txtUrlP);
        txtIdAndroid = (TextView) findViewById(R.id.text_id_androidP);
        txtIdAndroid.setVisibility(View.GONE);
        txtDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogDireccion = new AlertDialog.Builder(ProveedoresView.this);
                // Setting Dialog Message
                alertDialogDireccion.setTitle("Direccion");
                alertDialogDireccion.setMessage(txtDir.getText().toString()).setIcon(R.drawable.ic_account_balance_red)
                        .setPositiveButton("Ver en Maps", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(txtDir.getText().toString()));
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);

                            }
                        })
                        .setNeutralButton("Ok", (dialog, which) -> {

                        });

                alertDialogDireccion.setCancelable(true);
                alertDialogDireccion.show();

            }
        });
        txtDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogNota = new AlertDialog.Builder(ProveedoresView.this);
                // Setting Dialog Message
                alertDialogNota.setTitle("Descripcion");
                alertDialogNota.setMessage(txtDescripcion.getText().toString()).setIcon(R.drawable.ic_sticky_note_red)
                        .setNegativeButton("Ok", (dialog, which) -> {
                        });
                alertDialogNota.setCancelable(true);
                alertDialogNota.show();
            }
        });



// para mostrar los datos que viene de clientesmain y editarlos
        _Proveedor_Id = 0;
        Bundle b = getIntent().getExtras();
        _Proveedor_Id = b.getInt("proveedor_id");

        verproveedor();
 /*       btn_new_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //       Intent intent4 = new Intent(ClientesView.this, MainReparaciones.class);
                //       intent4.putExtra("cliente_id", _Cliente_Id);
                //       startActivity(intent4);
                Toast.makeText(ClientesView.this, "Hacer nuevo pedido", Toast.LENGTH_LONG).show();
            }
        });

  */

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
 /*
        btn_ver_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Intent intent2 = new Intent(ClientesView.this, ReparacionesList.class);
                //    intent2.putExtra("cliente_id", _Cliente_Id);
                //    startActivity(intent2);
                Toast.makeText(ClientesView.this, "Ver la lista de los pedidos anteriores", Toast.LENGTH_LONG).show();
            }
        });

  */

/*        btnVerContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vercontacto();

            }
        });


 */

    }

    public void verproveedor() {

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from proveedores where proveedor_id = "
                + String.valueOf(_Proveedor_Id), null);
        if (cursor.moveToFirst()) {
            do {
                txtNombre.setText(cursor.getString(cursor.getColumnIndex("nombre")));
                txtTelefono.setText(cursor.getString(cursor.getColumnIndex("telefono")));
                txtEmail.setText(cursor.getString(cursor.getColumnIndex("email")));
                txtDir.setText(cursor.getString(cursor.getColumnIndex("direccion")));
                txtDescripcion.setText(cursor.getString(cursor.getColumnIndex("descripcion")));
                txtInstagram.setText(cursor.getString(cursor.getColumnIndex("instagram")));
                txtUrl.setText(cursor.getString(cursor.getColumnIndex("url")));
                txtIdAndroid.setText(cursor.getString(cursor.getColumnIndex("id_android")));

                txtProveedorId.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("proveedor_id"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

    }

    public void vercontacto() {
        // Toast.makeText(ClientesView.this, "Funcion no activada por los momentos", Toast.LENGTH_LONG).show();

        int contactID = Integer.parseInt(txtIdAndroid.getText().toString());
        if (contactID == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Aviso: el proveedor no esta ligado con un contacto de Android... ")
                    .show();

        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
            intent.setData(uri);
            startActivity(intent);
        }


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        verproveedor();
    }

    public void sendMessage() {

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Enviar SMS")//Título del diálogo
                .setMessage("Esta seguro de enviar un Mensaje de Texto " +
                        "al Proveedor?")
                .setIcon(R.drawable.ic_warning_red)
                .setPositiveButton("Enviar SMS", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SmsManager smsManager = SmsManager.getDefault();
                        String contactUri = txtTelefono.getText().toString();
                        if (contactUri != null) {
                            String strMessage = "** Saludos ** ";
                            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                            sendIntent.setType("vnd.android-dir/mms-sms");
                            sendIntent.putExtra("address", contactUri);
                            sendIntent.putExtra("sms_body", strMessage);
                            startActivity(sendIntent);
                            //Toast.makeText(ClientesView.this, "Mensaje Enviado", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(ProveedoresView.this, "Selecciona un contacto primero", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "Mensaje SMS cancelado", Toast.LENGTH_LONG).show();
                    }
                })
                .create();
        alertDialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_proveedores_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.edit) {
            Intent intent2 = new Intent(ProveedoresView.this, ProveedoresModify.class);
            intent2.putExtra("Proveedor_id", _Proveedor_Id);
            intent2.putExtra("proveedor_instagram", txtInstagram.getText().toString());
            intent2.putExtra("proveedor_nombre", txtNombre.getText().toString());
            intent2.putExtra("proveedor_telefono", txtTelefono.getText().toString());
            intent2.putExtra("proveedor_email", txtEmail.getText().toString());
            intent2.putExtra("proveedor_descripcion", txtDescripcion.getText().toString());
            intent2.putExtra("proveedor_direccion", txtDir.getText().toString());
            intent2.putExtra("proveedor_url", txtUrl.getText().toString());
            intent2.putExtra("proveedor_android", txtIdAndroid.getText().toString());
            startActivity(intent2);

            // Toast.makeText(getApplicationContext(), "Editar el cliente", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.delete) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Pop-up/Eliminar Registro: " + _Proveedor_Id)
                    .setMessage("Esta seguro de Eliminar el registro?")
                    .setIcon(R.drawable.papelera)
                    .setPositiveButton("si", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            Toast.makeText(getApplicationContext(),
                                    "Operacion no permitida", Toast.LENGTH_LONG).show();
                        }

                    })
                    .setNegativeButton("no", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //no hacer nada
                        }
                    })
                    .create();
            alertDialog.show();
            return true;
        }

        if (id == R.id.sms) {
            sendMessage();
            return true;
        }
        if (id == R.id.VerContactoAndroid) {
            vercontacto();
            return true;
        }
        if (id == R.id.sms_whatsapp) {

            String msj = "Saludos:  ";
            Intent intentw = new Intent(Intent.ACTION_VIEW);
            String uri = "whatsapp://send?phone=" + txtTelefono.getText().toString() + "&text=" + msj;
            intentw.setData(Uri.parse(uri));
            startActivity(intentw);

            return true;
        }
        if (id == R.id.inicio) {
            Intent intent = new Intent(ProveedoresView.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.close) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}