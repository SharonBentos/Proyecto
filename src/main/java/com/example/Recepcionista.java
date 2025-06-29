package com.example;

import java.util.List;

public class Recepcionista implements Runnable {
    private final CentroMedico centro;

    public Recepcionista(CentroMedico centro) {
        this.centro = centro;
    }

    public void run() {
        while (!centro.isFinSimulacion()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<Paciente> llegadas = centro.obtenerLlegadasActuales();
            for (Paciente p : llegadas) {
                centro.agregarPaciente(p);
            }
        }
    }
}