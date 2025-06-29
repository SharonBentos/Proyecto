package com.example;

public class SimuladorReloj implements Runnable {
    private final CentroMedico centro;

    public SimuladorReloj(CentroMedico centro) {
        this.centro = centro;
    }

    public void run() {
        while (!centro.isFinSimulacion()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            centro.avanzarMinuto();
        }
    }
}