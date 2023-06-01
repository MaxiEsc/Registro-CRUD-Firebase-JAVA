package com.maxescobar.registro_java_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxescobar.registro_java_firebase.Adaptadores.ListViewRegistroAdapters;
import com.maxescobar.registro_java_firebase.Models.Registro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //Nuestra lista de registro de datos
    private ArrayList<Registro> listaRegistros = new ArrayList<Registro>();
    //Con esto tipo de dato se adaptaria a la clase creada de parte de BaseAdapter
    ArrayAdapter<Registro> arrayAdapter; //Una forma de hacerlo
    //Nuestro adaptador creado
    ListViewRegistroAdapters listViewRegistroAdapters;
    //El linear_layout que editaremos
    LinearLayout linearLayoutEditar;
    ListView listaRegistroPersonas;


    //los editText de donde se recogeran los datos
    EditText inputNombre, inputTelefono, inputDetalle, inputAlias;
    //Lista que renderizara nuestros registros
    Button btnCancelar;
    //Esta variable cargar los datos del elemento seleccionado
    Registro itemSeleccionado;

    //Datos por parte de Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputNombre = (EditText) findViewById(R.id.etNombre);
        inputTelefono = (EditText) findViewById(R.id.etTelefono);
        inputAlias = (EditText) findViewById(R.id.etAlias);
        inputDetalle = (EditText) findViewById(R.id.etDetalle);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        listaRegistroPersonas = (ListView) findViewById(R.id.lvRegistros);
        linearLayoutEditar = (LinearLayout) findViewById(R.id.linearLayoutEditar);

        try {
            listaRegistroPersonas.setOnItemClickListener((parent, view, position, id) -> {
                itemSeleccionado = (Registro) parent.getItemAtPosition(position);
                inputNombre.setText(itemSeleccionado.getNombre());
                inputTelefono.setText(itemSeleccionado.getTelefono());
                inputAlias.setText(itemSeleccionado.getAlias());
                inputDetalle.setText(itemSeleccionado.getDetalle());
                //Ahora se hace visible el layout previamente puesto como invisible en el diseño
                linearLayoutEditar.setVisibility(View.VISIBLE);
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            btnCancelar.setOnClickListener(v -> {
                linearLayoutEditar.setVisibility(View.GONE);
                itemSeleccionado = null;
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        inicializarFirebase();
        listarRegistro();
    }

    private void inicializarFirebase() {

        try {

            FirebaseApp.initializeApp(this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void listarRegistro() {
        databaseReference.child("Registro").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaRegistros.clear();
                for (DataSnapshot objeto : snapshot.getChildren()) {
                    Registro r = objeto.getValue(Registro.class);
                    listaRegistros.add(r);
                }
                //Generar adptador propio
                listViewRegistroAdapters = new ListViewRegistroAdapters(MainActivity.this,listaRegistros);
                 arrayAdapter = new ArrayAdapter<Registro>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1
                        , listaRegistros
                );

                listaRegistroPersonas.setAdapter(listViewRegistroAdapters);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String nombres = inputNombre.getText().toString();
        String telefono = inputTelefono.getText().toString();
        String detalles = inputDetalle.getText().toString();
        String alias = inputAlias.getText().toString();

        if (item.getItemId() == R.id.menu_Agregar) {
            insertar();
        }
        if (item.getItemId() == R.id.menu_Borrar) {
            borrar();
        }
        if (item.getItemId() == R.id.menu_Guardar) {
            guardar(nombres, telefono, detalles, alias);
        }

        return super.onOptionsItemSelected(item);
    }

    public void insertar() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View miView = getLayoutInflater().inflate(R.layout.insertar, null);
        Button btnInsertar = (Button) miView.findViewById(R.id.IngresarBtnAceptar);
        final EditText mInputNombres = (EditText) miView.findViewById(R.id.etIngresarNombre);
        final EditText mInputTelefono = (EditText) miView.findViewById(R.id.etIngresarTelefono);
        final EditText mInputAlias = (EditText) miView.findViewById(R.id.etIngresarAlias);
        final EditText mInputDetalle = (EditText) miView.findViewById(R.id.etIngresarDetalle);

        mBuilder.setView(miView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombres = mInputNombres.getText().toString();
                String telefono = mInputTelefono.getText().toString();
                String detalles = mInputDetalle.getText().toString();
                String alias = mInputAlias.getText().toString();

                if (nombres.isEmpty() || detalles.isEmpty() || alias.isEmpty()) {
                    showError(mInputNombres, "Nombre Incorrecto o campo vacio (Min. 3 letras)");
                    showError(mInputDetalle, "Detalle Incorrecto o campo vacio (Min. 3 letras)");
                    showError(mInputAlias, "Alias Incorrecto o campo vacio (Min. 3 letras)");
                } else if (telefono.isEmpty() || telefono.length() < 9) {
                    showError(mInputTelefono, "Telefono Incorrecto o campo vacio (Min. 9 numeros)");
                } else {
                    Registro r = new Registro();
                    r.setIdRegistro(UUID.randomUUID().toString());
                    r.setNombre(nombres);
                    r.setTelefono(telefono);
                    r.setDetalle(detalles);
                    r.setAlias(alias);
                    r.setFechaRegistro(getFechaNormal(getFechaMilisegundos()));
                    r.setTimestamp(getFechaMilisegundos() * -1);
                    databaseReference.child("Registro").child(r.getIdRegistro()).setValue(r);
                    Toast.makeText(MainActivity.this,
                            "Registrado correctamente",
                            Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            }
        });
    }

    public void borrar() {
        if (itemSeleccionado != null) {
            Registro r = new Registro();
            r.setIdRegistro(itemSeleccionado.getIdRegistro());
            databaseReference.child("Registro").child(r.getIdRegistro()).removeValue();
            linearLayoutEditar.setVisibility(View.GONE);
            itemSeleccionado = null;
            Toast.makeText(this, "Eliminado Correctamente", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Seleccione un Registro que desee eliminar", Toast.LENGTH_LONG).show();
        }
    }

    public void guardar(String nombre, String telefono, String alias, String detalle) {
        if (itemSeleccionado != null) {
            if (validarEntradas() == false) {
                Registro r = new Registro();
                r.setIdRegistro(itemSeleccionado.getIdRegistro());
                r.setNombre(nombre);
                r.setTelefono(telefono);
                r.setAlias(alias);
                r.setDetalle(detalle);
                r.setFechaRegistro(itemSeleccionado.getFechaRegistro());
                r.setTimestamp(itemSeleccionado.getTimestamp());
                databaseReference.child("Registro").child(r.getIdRegistro()).setValue(r);
                Toast.makeText(this, "Actualizado Correctamente", Toast.LENGTH_LONG).show();
                linearLayoutEditar.setVisibility(View.GONE);
                itemSeleccionado = null;
            }
        }else {
            Toast.makeText(this, "Seleccione un Registro", Toast.LENGTH_LONG).show();
        }

    }

    public void showError(EditText input, String a) {
        input.requestFocus();
        input.setError(a);
    }

    public long getFechaMilisegundos() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public String getFechaNormal(long f) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));
        return sdf.format(f);
    }

    public boolean validarEntradas() {
        String nombre = inputNombre.getText().toString();
        String telefono = inputTelefono.getText().toString();
        String alias = inputAlias.getText().toString();
        String detalle = inputDetalle.getText().toString();
        if (nombre.isEmpty() || nombre.length() < 3) {
            showError(inputNombre, "Nombre invalido. (Min 3 letras)");
            return true;
        } else if (telefono.isEmpty() || telefono.length() < 9) {
            showError(inputTelefono, "Telefono invalido (Min 9 números)");
            return true;
        } else if (alias.isEmpty() || alias.length() < 1) {
            showError(inputAlias, "Alias invalido (Min 1 letra)");
            return true;
        } else if (detalle.isEmpty() || detalle.length() < 1) {
            showError(inputDetalle, "Alias invalido (Min 1 letra)");
            return true;
        }

        return false;
    }

}