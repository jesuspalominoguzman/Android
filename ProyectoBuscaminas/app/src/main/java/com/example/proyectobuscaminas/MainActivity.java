package com.example.proyectobuscaminas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectobuscaminas.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private int minasTotales = 0;
    private int minasEncontradas = 0;
    private int fallos = 0;
    private int limiteFallos = 0;
    private boolean juegoTerminado = false;

    private ArrayList<Integer> posicionesMinas = new ArrayList<>();
    private ArrayList<Button> botonesJuego = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configuración inicial de la UI
        binding.btnReset.setVisibility(View.GONE);
        binding.checkboxMina.setEnabled(false);

        binding.inputMinas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String texto = s.toString().trim();
                if (!texto.isEmpty()) {
                    try {
                        int cantidadBotones = Integer.parseInt(texto);
                        if (cantidadBotones >= 4 && cantidadBotones <= 30) {
                            iniciarNuevoJuego(cantidadBotones);
                        }
                    } catch (NumberFormatException e) {
                        // Maneja el caso en que el texto no sea un número válido
                        binding.layoutBotones.removeAllViews();
                        binding.checkboxMina.setEnabled(false);
                    }
                } else {
                    // Si el campo está vacío, limpiar el tablero
                    binding.layoutBotones.removeAllViews();
                    binding.checkboxMina.setEnabled(false);
                }
            }
        });

        binding.checkboxMina.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mostrarUnaMinaNoDescubierta();
                // Se deshabilita a sí mismo después de ser usado
                binding.checkboxMina.setEnabled(false);
            }
        });

        binding.btnReset.setOnClickListener(v -> recrearActividad());
    }

    @SuppressLint("StringFormatInvalid")
    private void iniciarNuevoJuego(int cantidadBotones) {
        juegoTerminado = false;
        binding.layoutBotones.removeAllViews();
        botonesJuego.clear();
        posicionesMinas.clear();
        minasEncontradas = 0;
        fallos = 0;

        minasTotales = Math.round(cantidadBotones / 4f);
        limiteFallos = Math.round(cantidadBotones * 0.5f);

        // Resetear la UI
        binding.numMinasTotales.setText(getString(R.string.placeholder_total_minas, minasTotales));
        binding.numMinasEncontradas.setText(getString(R.string.placeholder_minas_encontradas, 0));
        binding.numLimiteFallos.setText(getString(R.string.placeholder_limite_fallos, limiteFallos));
        binding.btnReset.setVisibility(View.GONE);

        // Reactivar y desmarcar el checkbox para el nuevo juego
        binding.checkboxMina.setChecked(false);
        binding.checkboxMina.setEnabled(true);

        generarBotonesConMinas(cantidadBotones, minasTotales);
    }

    private void generarBotonesConMinas(int cantidad, int minasTotales) {
        // Lógica para generar posiciones de minas
        ArrayList<Integer> numerosPosibles = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            numerosPosibles.add(i);
        }
        Collections.shuffle(numerosPosibles);
        posicionesMinas.clear();
        for (int i = 0; i < minasTotales; i++) {
            posicionesMinas.add(numerosPosibles.get(i));
        }

        // Crear y añadir botones al GridLayout
        binding.layoutBotones.setColumnCount(5);

        for (int i = 0; i < cantidad; i++) {
            Button btn = new Button(this);
            btn.setText(String.valueOf(i + 1));
            btn.setTextSize(18);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(4, 4, 4, 4); // --> MEJORA: Añadir márgenes
            btn.setLayoutParams(params);

            final int indice = i;
            btn.setOnClickListener(v -> {
                if (!juegoTerminado && btn.isEnabled()) { // Solo funciona si el juego no ha terminado
                    verificarBoton(indice, btn);
                }
            });

            botonesJuego.add(btn);
            binding.layoutBotones.addView(btn);
        }
    }

    private void verificarBoton(int indice, Button btn) {
        if (posicionesMinas.contains(indice)) {
            minasEncontradas++;
            btn.setText(R.string.icono_mina);
            btn.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
        } else {
            fallos++;
            btn.setText(R.string.icono_fallo);
            btn.setBackgroundColor(Color.parseColor("#F44336")); // Rojo
        }
        btn.setEnabled(false);
        actualizarContadores();

        if (minasEncontradas == minasTotales) {
            mostrarVictoria();
        } else if (fallos >= limiteFallos) {
            mostrarDerrota();
        }
    }

    @SuppressLint("StringFormatInvalid")
    private void actualizarContadores() {
        String textoMinasEncontradas = String.format(getString(R.string.placeholder_minas_encontradas), minasEncontradas);
        binding.numMinasEncontradas.setText(textoMinasEncontradas);

        String textoFallosActuales = String.format(getString(R.string.texto_fallos_actuales), fallos, limiteFallos);
        binding.numLimiteFallos.setText(textoFallosActuales);
    }


    private void terminarJuego(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        binding.btnReset.setVisibility(View.VISIBLE);
        juegoTerminado = true;
        desactivarTodosLosBotones();
    }

    private void mostrarVictoria() {
        terminarJuego(getString(R.string.mensaje_victoria));
    }

    private void mostrarDerrota() {
        // --> MEJORA: Revelar dónde estaban las minas al perder
        for (int i = 0; i < botonesJuego.size(); i++) {
            Button boton = botonesJuego.get(i);
            if (posicionesMinas.contains(i) && boton.isEnabled()) {
                boton.setText(R.string.icono_mina);
                boton.setBackgroundColor(Color.parseColor("#FF9800")); // Naranja
            }
        }
        terminarJuego(getString(R.string.mensaje_derrota));
    }

    private void desactivarTodosLosBotones() {
        for (Button boton : botonesJuego) {
            boton.setEnabled(false);
        }
    }


    private void recrearActividad() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void mostrarUnaMinaNoDescubierta() {
        ArrayList<Integer> minasNoDescubiertas = new ArrayList<>();
        for (int posMina : posicionesMinas) {
            if (botonesJuego.get(posMina).isEnabled()) {
                minasNoDescubiertas.add(posMina);
            }
        }

        if (!minasNoDescubiertas.isEmpty()) {
            Collections.shuffle(minasNoDescubiertas);
            int posAMostrar = minasNoDescubiertas.get(0);
            Button btn = botonesJuego.get(posAMostrar);

            btn.setText(R.string.icono_mina);
            btn.setBackgroundColor(Color.parseColor("#FFEB3B")); // Amarillo
            btn.setEnabled(false);
            minasEncontradas++;
            actualizarContadores();

            if (minasEncontradas == minasTotales) {
                mostrarVictoria();
            }
        } else {
            Toast.makeText(this, "No quedan minas por mostrar", Toast.LENGTH_SHORT).show();
        }
    }
}
