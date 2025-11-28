package com.example.examen28noviembre;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Creo los ArrayLists necesarios para guardar variables del mismo tipo
    private ArrayList<Personal> listaPersonal;
    private ArrayList<Coches> listaCoches;
    private PersonalAdapter adaptador;
    private ListView lvEmpleados;

    // Creo los elementos con los que voy a añadir nuevos coches
    private EditText etMarca;
    private EditText etColor;
    private Button btnAgregarCoche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Aqui inicializo el listviw
        lvEmpleados = findViewById(R.id.lv_empleados);

        // Aqui voy asignando cada uno de los elementos que tengo en el xml para añadir coches al main
        etMarca = findViewById(R.id.et_marca);
        etColor = findViewById(R.id.et_color);
        btnAgregarCoche = findViewById(R.id.btn_agregar_coche);
        btnAgregarCoche.setOnClickListener(this);


        // Cargar datos de empleados y coches
        cargarDatosPersonal();
        cargarDatosCoches();

        // Creo el adaptador como constructor
        adaptador = new PersonalAdapter(this, listaPersonal, listaCoches);
        lvEmpleados.setAdapter(adaptador);
    }
    private void cargarDatosPersonal() {
        listaPersonal = new ArrayList<>();

        listaPersonal.add(new Personal("Angel", "Director", 3000));
        listaPersonal.add(new Personal("Maria", "Oficial", 1500));
        listaPersonal.add(new Personal("Juan", "Medico", 2700));
        listaPersonal.add(new Personal("Guadalupe", "Informatico", 1700));
    }
    private void cargarDatosCoches() {
        listaCoches = new ArrayList<>();

        listaCoches.add(new Coches("Audi", "Negro"));
        listaCoches.add(new Coches("BMW", "Blanco"));
        listaCoches.add(new Coches("Toyota", "Rojo"));
        listaCoches.add(new Coches("Honda", "Verde"));
        listaCoches.add(new Coches("Citroën", "Amarillo"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_agregar_coche) {
            agregarNuevoCoche();
        }
    }

    private void agregarNuevoCoche() {
        String marca = etMarca.getText().toString().trim();
        String color = etColor.getText().toString().trim();


        // Añado un toast para advertir si los campos estan vacios
        if (marca.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, R.string.toast_campos_vacios, Toast.LENGTH_SHORT).show();
            return;
        }

        Coches nuevoCoche = new Coches(marca, color);
        listaCoches.add(nuevoCoche);

        // Actualizo el listview para reflejar los cambios
        adaptador.notifyDataSetChanged();

        // Vacio los campos de texto para la proxima introducción
        etMarca.setText("");
        etColor.setText("");

        // Confirmo que se ha intrducido un coche
        Toast.makeText(this, R.string.toast_coche_agregado, Toast.LENGTH_SHORT).show();
    }
}