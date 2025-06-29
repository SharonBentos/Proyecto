package com.example;

public class Tiempo implements Comparable<Tiempo> {
    public int hora;
    public int minuto;

    public Tiempo(int hora, int minuto) {
        this.hora = hora;
        this.minuto = minuto;
    }

    public void avanzarMinuto() {
        minuto++;
        if (minuto == 60) {
            minuto = 0;
            hora++;
        }
    }

    public int minutosTotales() {
        return hora * 60 + minuto;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hora, minuto);
    }

    @Override
    public int compareTo(Tiempo o) {
        return Integer.compare(this.minutosTotales(), o.minutosTotales());
    }

    public Tiempo sumarMinutos(int minutos) {
        int totalMinutos = this.minutosTotales() + minutos;
        int nuevaHora = totalMinutos / 60;
        int nuevoMinuto = totalMinutos % 60;
        return new Tiempo(nuevaHora, nuevoMinuto); 
    }
}