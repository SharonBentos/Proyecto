package com.example;

import java.util.concurrent.Semaphore;

public class Enfermero {
    // Usamos un Semaphore con un solo permiso para controlar exclusividad
    private final Semaphore disponible = new Semaphore(1, true);

    // Médico intenta "tomar" al enfermero (bloquear el recurso)
    public void atenderConEnfermero() throws InterruptedException {
        disponible.acquire(); // si está ocupado, espera hasta que quede libre
    }

    // Médico "libera" al enfermero cuando termina la atención
    public void liberarEnfermero() {
        disponible.release();
    }

    // Para saber si está libre
    public boolean estaDisponible() {
        return disponible.availablePermits() > 0;
    }
    
}
