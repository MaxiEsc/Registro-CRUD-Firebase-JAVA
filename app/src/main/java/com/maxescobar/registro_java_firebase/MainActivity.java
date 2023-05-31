package com.maxescobar.registro_java_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maxescobar.registro_java_firebase.Adaptadores.ListViewRegistroAdapters;
import com.maxescobar.registro_java_firebase.Models.Registro;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Nuestra lista de registro de datos
    private ArrayList<Registro> listaRegistros = new ArrayList<Registro>();
    //Con esto tipo de dato se adaptaria a la clase creada de parte de BaseAdapter
    ArrayAdapter<Registro> arrayAdapter; //Una forma de hacerlo
    //Nuestro adaptador creado
    ListViewRegistroAdapters listViewRegistroAdapters;
    //El linear_layout que editaremos
    LinearLayout linearLayoutEditar;

    //los editText de donde se recogeran los datos
    EditText inputNombre, inputTelefono, inputDetalle, inputAlias;

    //Lista que renderizara nuestros registros
    ListView listaRegistroPersonas;
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

        listaRegistroPersonas = (ListView) findViewById(R.id.lvRegistros);
        linearLayoutEditar = (LinearLayout) findViewById(R.id.linearLayoutEditar);

        listaRegistroPersonas.setOnClickListener((View.OnClickListener) (parent, view, position, id) -> {
//                itemSeleccionado = (Registro) parent.getItemAtPosition(position);
                inputNombre.setText(itemSeleccionado.getNombre());
                inputTelefono.setText(itemSeleccionado.getTelefono());
                inputAlias.setText(itemSeleccionado.getAlias());
                inputDetalle.setText(itemSeleccionado.getDetalle());
                //Ahora se hace visible el layout previamente puesto como invisble en el diseño
                linearLayoutEditar.setVisibility(View.VISIBLE);
            });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutEditar.setVisibility(View.GONE);
                itemSeleccionado = null;
            }
        });

        inicializarFirebase();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarRegistro(){
        databaseReference.child("Registro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaRegistros.clear();
                for (DataSnapshot objeto : snapshot.getChildren()){
                    Registro r = objeto.getValue(Registro.class);
                    listaRegistros.add(r);
                }

                //Generar adptador propio
                arrayAdapter = new ArrayAdapter<Registro>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1
                        ,listaRegistros
                );

                listaRegistroPersonas.setAdapter(arrayAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.registro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String nombres = inputNombre.getText().toString();
        String telefono = inputTelefono.getText().toString();
        String detalles = inputDetalle.getText().toString();
        String alias = inputAlias.getText().toString();

        if (item.getItemId() == R.id.menu_Agregar){
            insertar();
        }
        if (item.getItemId() == R.id.menu_Borrar){
            borrar();
        }
        if (item.getItemId() == R.id.menu_Guardar){
            guardar();
        }

        return super.onOptionsItemSelected(item);
    }

    public void insertar()
}