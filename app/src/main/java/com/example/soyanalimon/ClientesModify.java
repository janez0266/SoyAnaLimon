package com.example.soyanalimon;



import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;

public class ClientesModify extends AppCompatActivity {

    public static final int PICK_CONTACT_REQUEST = 99;
    private Uri contactUri;
    Button btn_tlf;
    Button btn_Close;
    Button btnVerContacto;
    EditText txt_Nombre;
    EditText txt_Telefono;
    EditText txt_Email;
    EditText txt_Nota;
    EditText txt_Direccion;
    EditText txt_Dni;
    EditText txt_Url;
    TextView txt_cliente_Id;
    TextView txtIdAndroid;
    ImageView imageView3;
    int _Cliente_Id=0;

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_modify);
        //setTitle("Modificar Cliente ");

      //  btn_tlf = (Button) findViewById(R.id.btn_tlf);
        btn_Close = (Button) findViewById(R.id.btn_close);
        btnVerContacto = (Button) findViewById(R.id.btnVerContacto);
        txt_Nombre = (EditText) findViewById(R.id.txt_Nombre);
        txt_Telefono = (EditText) findViewById(R.id.txt_Telefono);
        txt_Email = (EditText) findViewById(R.id.txt_Email);
        txt_Nota = (EditText) findViewById(R.id.txt_Nota);
        txt_Direccion = (EditText) findViewById(R.id.txt_Direccion);
        txt_Dni = (EditText) findViewById(R.id.txt_Dni);
        txt_Url = (EditText) findViewById(R.id.txt_Url);
        txt_cliente_Id = (TextView) findViewById(R.id.txt_cliente_Id);
        txtIdAndroid = (TextView) findViewById(R.id.txtIdAndroid);
        imageView3 = findViewById(R.id.imageView);
        txtIdAndroid.setText("0");

// para mostrar los datos que viene de clientesmain y editarlos
        _Cliente_Id =0;

        Bundle b=getIntent().getExtras();
        _Cliente_Id=b.getInt("cliente_id");
        String nombre = b.getString("cliente_nombre");
        String telefono = b.getString("cliente_telefono");
        String email = b.getString("cliente_email");
        String nota = b.getString("cliente_nota");
        String direccion = b.getString("cliente_direccion");
        String url = b.getString("cliente_url");
        String dni = b.getString("cliente_dni");
        String android = b.getString("cliente_android");
        setTitle("Nuevo Cliente");
        if (android == null){
            txtIdAndroid.setText("0");
        }

        //txtIdAndroid.setText(android);
        if (_Cliente_Id != 0){
            txt_cliente_Id.setText("Modificar Cliente: " + String.valueOf(_Cliente_Id));
            txt_Nombre.setText(nombre);
            txt_Telefono.setText(telefono);
            txt_Email.setText(email);
            txt_Nota.setText(nota);
            txt_Direccion.setText(direccion);
            txt_Url.setText(url);
            txt_Dni.setText(dni);
            txtIdAndroid.setText(android);
            setTitle("Modificar Cliente ");
        }


        btn_Close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        /*
        btn_tlf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                vercontacto();

            }
        });

         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clientes_modify, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cliente_guardar) {
            guardar_contacto();
            return true;
        }
        if (id == R.id.contacto) {
            Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(i, PICK_CONTACT_REQUEST);
            return true;
        }
        if (id == R.id.close) {
            finish();
            return true;
        }
        if (id == R.id.inicio) {
            Intent intent = new Intent(ClientesModify.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //todo **ojo** no funcionan contactos... El error esta al leer el nro telefonico y la imagen. el nombre lo lee bien
    public void initPickContacts(View v){

        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                contactUri = intent.getData();
//                Toast.makeText(getApplicationContext(), "onactivityresult:-- " + contactUri, Toast.LENGTH_LONG).show();

                renderContact(contactUri);
            }
        }
    }
    private void renderContact(Uri uri) {

        txt_Nombre.setText(getName(uri));
        txtIdAndroid.setText(getPhone(uri));
        vercontacto();
//        txtTelefono.setText(getPhone(uri));
//        imageView3.setImageBitmap(getPhoto(uri));
    }
    private String getName(Uri uri) {
        String name = null;
        String phone = null;
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(
                uri,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null,
                null,
                null);
        if(c.moveToFirst()){
            name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        }
        c.close();
        return name;
    }
    private String getPhone(Uri uri) {
        String id = null;
        String phone = null;
        //se busca el id del contacto, para luego buscar en la tabla de los nros telefonicos (tablas separadas)
        Cursor contactCursor = getContentResolver().query(
                uri,
                new String[]{ContactsContract.Contacts._ID},
                null,
                null,
                null);
        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();
 //       Toast.makeText(getApplicationContext(), "Contact ID:-- " + id, Toast.LENGTH_LONG).show();
//todo aqui esta el problema de los contactos
        //con el id del contacto, se busca en la tabla telefonos, y se selecciona el nro mobile (type_mobile)
/*
        String selectionArgs =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE+" = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;


        Cursor phoneCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
                selectionArgs,
                new String[] { id },
                null
        );


        if (phoneCursor.moveToFirst()) {
            phone = phoneCursor.getString(0);
        }
        phoneCursor.close();
*/



        phone = "123456789";
        //return phone;
        return id;
    }


    private Bitmap getPhoto(Uri uri) {
        /*
        Foto del contacto y su id
         */
        Bitmap photo = null;
        String id = null;

        /************* CONSULTA ************/
        Cursor contactCursor = getContentResolver().query(
                uri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        /*
        Usar el método de clase openContactPhotoInputStream()
         */
        try {
            InputStream input =
                    ContactsContract.Contacts.openContactPhotoInputStream(
                            getContentResolver(),
                            ContentUris.withAppendedId(
                                    ContactsContract.Contacts.CONTENT_URI,
                                    Long.parseLong(id))
                    );
            if (input != null) {
                /*
                Dar formato tipo Bitmap a los bytes del BLOB
                correspondiente a la foto
                 */
                photo = BitmapFactory.decodeStream(input);
                input.close();
            }

        } catch (IOException iox) { /* Manejo de errores */ }

        return photo;
    }
    public void vercontacto() {


        int contactID = Integer.parseInt(txtIdAndroid.getText().toString());
        if (contactID == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Aviso: el cliente no esta ligado con un contacto de Android... ")
                    .show();

        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
            intent.setData(uri);
            startActivity(intent);
        }

    }
    public void guardar_contacto(){
//        vercontacto();


        if (txt_Nombre.getText().toString().trim().equalsIgnoreCase(""))
            txt_Nombre.setError("This field can not be blank");
        else{

            // se leen las variables del layout

            ContentValues registro = new ContentValues();  //es una clase para guardar datos
            registro.put("telefono", txt_Telefono.getText().toString());
            registro.put("nombre", txt_Nombre.getText().toString());
            registro.put("email", txt_Email.getText().toString());
            registro.put("direccion", txt_Direccion.getText().toString());
            registro.put("nota", txt_Nota.getText().toString());
            registro.put("url", txt_Url.getText().toString());
            registro.put("dni", txt_Dni.getText().toString());
            registro.put("id_android", txtIdAndroid.getText().toString());

            DBHelper dbHelper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor fila = db.rawQuery("select * from clientes where cliente_id=" + String.valueOf(_Cliente_Id), null);
            if (!fila.moveToFirst()) {  //devuelve true o false

                db.insert("clientes", null, registro);    // clientes: nombre de la tabla
                Toast.makeText(getApplicationContext(), "Se cargaron los datos del cliente",
                        Toast.LENGTH_SHORT).show();
            } else {

                db.update("clientes", registro, "cliente_id = ?",
                        new String[]{String.valueOf(_Cliente_Id)});
                //       new String[] { String.valueOf(_Cliente_Id) });
                Toast.makeText(getApplicationContext(), "Cliente Actualizado", Toast.LENGTH_SHORT).show();

            }
            fila.close();
            db.close();
            _Cliente_Id = 0;
            finish();
        }



    }
    public void mensaje(){
        String texto = "Para poder enlazar a un contaco de android, primero seleccione el contacto de la lista que se presenta a continuacion. luego se abrira una ventana y debera copiar el nro de telefono que desea usar y cerrar esa ventana. Seguido, presione el campo TELEFONO hasta que salga el dialogo PEGAR y haga click ...";
        AlertDialog.Builder alertDialogDireccion = new AlertDialog.Builder(ClientesModify.this);
        // Setting Dialog Message
        alertDialogDireccion.setTitle("Instrucciones");
        alertDialogDireccion.setMessage(texto).setIcon(R.drawable.ic_account_balance_red)
                .setNeutralButton("Ok", (dialog, which) -> {

                });

        alertDialogDireccion.setCancelable(true);
        alertDialogDireccion.show();
    }
}