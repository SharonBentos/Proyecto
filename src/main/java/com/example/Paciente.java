package com.example;

public class Paciente {
    public enum Tipo {
        EMERGENCIA, URGENCIA, GENERAL_COMUN, CARNE_SALUD
    }

    private final String nombre;
    private Tipo tipo;
    private final Tiempo tiempoLlegada;
    private final int duracionAtencion;
    private int prioridad;
    public final boolean tieneExamenOdontologico;

    public Paciente(String nombre, Tipo tipo, Tiempo tiempoLlegada, int duracionAtencion, boolean examenOdontologico) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tiempoLlegada = tiempoLlegada;
        this.duracionAtencion = duracionAtencion;
        this.prioridad = calcularPrioridadBase(tipo);
        this.tieneExamenOdontologico = examenOdontologico;
    }

    private int calcularPrioridadBase(Tipo tipo) {
        return switch (tipo) {
            case EMERGENCIA -> 118;
            case URGENCIA -> 8;
            case CARNE_SALUD, GENERAL_COMUN -> 6;
        };
    }

    public void aumentarPrioridad() {
        this.prioridad++;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public String getNombre() {
        return nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }
    

    public Tiempo getTiempoLlegada() {
        return tiempoLlegada;
    }

    public int getDuracionAtencion() {
        return duracionAtencion;
    }

    @Override
    public String toString() {
        return "Paciente{Nombre=" + nombre + ", tipo=" + tipo + ", llegada=" + tiempoLlegada +
                ", prioridad=" + prioridad + ", atencion=" + duracionAtencion + "min}";
    }
    public int getTiempoAtencion() {
        return duracionAtencion;   
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

}
