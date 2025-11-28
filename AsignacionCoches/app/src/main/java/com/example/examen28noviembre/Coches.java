package com.example.examen28noviembre;


// Creo un constructor basico para los coches
public class Coches {
    private String marca;
    private String color;

    public Coches(String marca, String color) {
        this.marca = marca;
        this.color = color;
    }

    public String getMarca() {
        return marca;
    }

    public String getColor() {
        return color;
    }
}
