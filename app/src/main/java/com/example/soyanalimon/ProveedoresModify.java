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

public class ProveedoresModify extends AppCompatActivity {

    public static final int PICK_CONTACT_REQUEST = 99;
    private Uri contactUri;
    Button btn_tlf;
    Button btn_Close;
    Button btnVerContacto;
    EditText txt_Nombre;
    EditText txt_Telefono;
    EditText txt_Email;
    EditText txt_Descripcion;
    EditText txt_Direccion;
    EditText txt_Instagram;
    EditText txt_Url;
    TextView txt_proveedor_Id;
    TextView txtIdAndroid;
    ImageView imageView3;
    int _Proveedor_Id=0;

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores_modify);
        //setTitle("Modificar Cliente ");

        //  btn_tlf = (Button) findViewById(R.id.btn_tlf);
        btn_Close = (Button) findViewById(R.id.btn_close);
        btnVerContacto = (Button) findViewById(R.id.btnVerContacto);
        txt_Nombre = (EditText) findViewById(R.id.txt_NombreP);
        txt_Telefono = (EditText) findViewById(R.id.txt_TelefonoP);
        txt_Email = (EditText) findViewById(R.id.txt_EmailP);
        txt_Descripcion = (EditText) findViewById(R.id.txt_DescripcionP);
        txt_Direccion = (EditText) findViewById(R.id.txt_DireccionP);
        txt_Instagram = (EditText) findViewById(R.id.txt_InstagramP);
        txt_Url = (EditText) findViewById(R.id.txt_UrlP);
        txt_proveedor_Id = (TextView) findViewById(R.id.txt_proveedor_Id);
        txtIdAndroid = (TextView) findViewById(R.id.txtIdAndroidP);
        imageView3 = findViewById(R.id.imageView9);
        txtIdAndroid.setText("0");

// para mostrar los datos que viene de clientesmain y editarlos
        _Proveedor_Id =0;

        Bundle b=getIntent().getExtras();
        _Proveedor_Id=b.getInt("proveedor_id");
        String nombre = b.getString("proveedor_nombre");
        String telefono = b.getString("proveedor_telefono");
        String email = b.getString("proveedor_email");
        String descripcion = b.getString("proveedor_descripcion");
        String direccion = b.getString("proveedor_direccion");
        String url = b.getString("proveedor_url");
        String instagram = b.getString("proveedor_instagram");
        String android = b.getString("proveedor_android");
        setTitle("Nuevo Proveedor");
        if (android == null){
            txtIdAndroid.setText("0");
        }

        //txtIdAndroid.setText(android);
        if (_Proveedor_Id != 0){
            txt_proveedor_Id.setText("Modificar proveedor: " + String.valueOf(_Proveedor_Id));
            txt_Nombre.setText(nombre);
            txt_Telefono.setText(telefono);
            txt_Email.setText(email);
            txt_Descripcion.setText(descripcion);
            txt_Direccion.setText(direccion);
            txt_Url.setText(url);
            txt_Instagram.setText(instagram);
            txtIdAndroid.setText(android);
            setTitle("Modificar Proveedor ");
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
        getMenuInflater().inflate(R.menu.menu_proveedor_modify, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.proveedor_guardar) {
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
            Intent intent = new Intent(ProveedoresModify.this, MainActivity.class);
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
            builder.setMessage("Aviso: el Proveedor no esta ligado con un contacto de Android... ")
                    .show();

        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactID));
            intent.setData(uri);
            startActivity(intent);
        }

    }
    public void guardar_contacto(){

        if (txt_Nombre.getText().toString().trim().equalsIgnoreCase(""))
            txt_Nombre.setError("This field can not be blank");
        else{

            // se leen las variables del layout

            ContentValues registro = new ContentValues();  //es una clase para guardar datos
            registro.put("telefono", txt_Telefono.getText().toString());
            registro.put("nombre", txt_Nombre.getText().toString());
            registro.put("email", txt_Email.getText().toString());
            registro.put("direccion", txt_Direccion.getText().toString());
            registro.put("descripciom", txt_Descripcion.getText().toString());
            registro.put("url", txt_Url.getText().toString());
            registro.put("instagram", txt_Instagram.getText().toString());
            registro.put("id_android", txtIdAndroid.getText().toString());

            DBHelper dbHelper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor fila = db.rawQuery("select * from proveedores where proveedor_id=" + String.valueOf(_Proveedor_Id), null);
            if (!fila.moveToFirst()) {  //devuelve true o false

                db.insert("proveedores", null, registro);    // clientes: nombre de la tabla
                Toast.makeText(getApplicationContext(), "Se cargaron los datos del Proveedor",
                        Toast.LENGTH_SHORT).show();
            } else {

                db.update("proveedores", registro, "proveedor_id = ?",
                        new String[]{String.valueOf(_Proveedor_Id)});
                //       new String[] { String.valueOf(_Cliente_Id) });
                Toast.makeText(getApplicationContext(), "Proveedor Actualizado", Toast.LENGTH_SHORT).show();

            }
            fila.close();
            db.close();
            _Proveedor_Id = 0;
            finish();
        }



    }
    public void mensaje(){
        String texto = "Para poder enlazar a un contaco de android, primero seleccione el contacto de la lista que se presenta a continuacion. luego se abrira una ventana y debera copiar el nro de telefono que desea usar y cerrar esa ventana. Seguido, presione el campo TELEFONO hasta que salga el dialogo PEGAR y haga click ...";
        AlertDialog.Builder alertDialogDireccion = new AlertDialog.Builder(ProveedoresModify.this);
        // Setting Dialog Message
        alertDialogDireccion.setTitle("Instrucciones");
        alertDialogDireccion.setMessage(texto).setIcon(R.drawable.ic_account_balance_red)
                .setNeutralButton("Ok", (dialog, which) -> {

                });

        alertDialogDireccion.setCancelable(true);
        alertDialogDireccion.show();
    }
}