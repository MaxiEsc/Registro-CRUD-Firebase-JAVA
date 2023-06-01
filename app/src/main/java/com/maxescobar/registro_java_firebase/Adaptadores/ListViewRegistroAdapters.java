package com.maxescobar.registro_java_firebase.Adaptadores;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.maxescobar.registro_java_firebase.Models.Registro;
import com.maxescobar.registro_java_firebase.R;

import java.util.ArrayList;

public class ListViewRegistroAdapters extends BaseAdapter {

    //Nuestro variable context
    Context context;
    //Arreglo de Registros
    ArrayList<Registro> registroDato;
    LayoutInflater layoutInflater;
    //Nuestro objeto de tipo registro
    Registro registroModel;

    public ListViewRegistroAdapters(Context contex, ArrayList<Registro> registroDate) {
        this.context = contex;
        this.registroDato = registroDate;
        //Aqui se va a instanciar parte de la vista personalizada en la aplicacion
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return registroDato.size();
    }

    @Override
    public Object getItem(int position) {
        return registroDato.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vistafila = convertView;
        if (vistafila == null){
            vistafila = layoutInflater.inflate(R.layout.listaregistro,
                    null,
                    true);
        }
        //Se enlazan las vistas
        TextView nombres = vistafila.findViewById(R.id.Nombrelbl);
        TextView telefono = vistafila.findViewById(R.id.Telefonolbl);
        TextView detalle = vistafila.findViewById(R.id.Detailslbl);
        TextView alias = vistafila.findViewById(R.id.Aliaslbl);
        TextView fechaRegistro = vistafila.findViewById(R.id.Fechalbl);

        //Obtenemos el registro individual
        registroModel = registroDato.get(position);

        //Ahora enviamos el dato obtenido a la vista
        nombres.setText(registroModel.getNombre());
        telefono.setText(registroModel.getTelefono());
        detalle.setText(registroModel.getDetalle());
        alias.setText(registroModel.getAlias());
        fechaRegistro.setText(registroModel.getFechaRegistro());

        //Retornamos la vista para que renderize
        return vistafila;
    }

}
