package com.example.examen28noviembre;

// Creo un constructor basico para los empleados
public class Personal {
    private String nombre;
    private String cargo;
    private int sueldo;

    public Personal(String nombre, String cargo, int sueldo) {
        this.nombre = nombre;
        this.cargo = cargo;
        this.sueldo = sueldo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public int getSueldo() {
        return sueldo;
    }
}
