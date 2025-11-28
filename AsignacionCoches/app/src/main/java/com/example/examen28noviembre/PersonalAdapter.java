package com.example.examen28noviembre;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

// Extiendo el Array adapter de mi clase personal para poder usr los datos de mi array en el lv
public class PersonalAdapter extends ArrayAdapter<Personal> {

    private Activity context; // Le paso el contexto del mainActivitu
    private ArrayList<Personal> listaPersonal;
    private ArrayList<Coches> listaCoches;

    // Creo un constuctor para el adaptador
    public PersonalAdapter(Activity context, ArrayList<Personal> listaPersonal, ArrayList<Coches> listaCoches) {
        super(context, R.layout.item_empleado, listaPersonal);
        this.context = context;
        this.listaPersonal = listaPersonal;
        this.listaCoches = listaCoches;
    }

    // Este metodo se repite por cada elemento del listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;

        // Aqui cojo el xml para introducirlo en el listview
        if (item == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            item = inflater.inflate(R.layout.item_empleado, null);
        }


        Personal empleado = listaPersonal.get(position);

        TextView tvNombre = item.findViewById(R.id.tv_nombre);
        TextView tvCargo = item.findViewById(R.id.tv_cargo);
        TextView tvSueldo = item.findViewById(R.id.tv_sueldo);
        TextView tvCoches = item.findViewById(R.id.tv_coches);


        tvNombre.setText("Nombre: " + empleado.getNombre());
        tvCargo.setText("Cargo: " + empleado.getCargo());
        tvSueldo.setText("Sueldo: " + empleado.getSueldo() + "€");

        ArrayList<Coches> cochesAsignados = obtenerCochesAsignados(empleado);

        // Aqui hago un Stringbuilder para diseñar la lista de los cohes
        StringBuilder cochesTexto = new StringBuilder();
        for (int i = 0; i < cochesAsignados.size(); i++) {
            Coches coche = cochesAsignados.get(i);
            cochesTexto.append("• ").append(coche.getMarca()).append(" - ").append(coche.getColor());
            if (i < cochesAsignados.size() - 1) {
                cochesTexto.append("\n");
            }
        }
        tvCoches.setText(cochesTexto.toString());

        return item;
    }


    // Hago un metodo para calcular que coches puede usar cada empleado
    private ArrayList<Coches> obtenerCochesAsignados(Personal empleado) {
        ArrayList<Coches> cochesAsignados = new ArrayList<>();

        // Si es informatico puede usar todos los coches
        if (empleado.getCargo().equalsIgnoreCase("Informatico")) {
            cochesAsignados.addAll(listaCoches);
            return cochesAsignados;
        }

        // Aqui voy leyendo los colores de los coches, para hacer que si cobran mas de 2000 puedan usar los blancos y negros, y si no los otros
        for (Coches coche : listaCoches) {
            String color = coche.getColor();

            if (empleado.getSueldo() >= 2000) {
                if (color.equalsIgnoreCase("Negro") || color.equalsIgnoreCase("Blanco")) {
                    cochesAsignados.add(coche);
                }
            }
            else {
                if (!color.equalsIgnoreCase("Negro") && !color.equalsIgnoreCase("Blanco")) {
                    cochesAsignados.add(coche);
                }
            }
        }

        return cochesAsignados;
    }
}
