package com.example;

public class Medico implements Runnable {
    private final CentroMedico centro;
    private final Enfermero enfermero;
    private final String nombre;
    private Paciente pacienteActual = null;
    private int id;

    public Medico(int id, CentroMedico centro, Enfermero enfermero) {
    this.id = id;
    this.centro = centro;
    this.nombre = "Medico " + id;
    this.enfermero = enfermero; // 💡 ahora sí se lo asignás correctamente
}



   @Override
public void run() {
    while (!centro.isFinSimulacion() || tienePacientesPendientes()) {
        boolean atendio = false;

        try {
            enfermero.atenderConEnfermero(); // espera hasta que el enfermero esté disponible

            try {
                synchronized (centro) {
                    pacienteActual = centro.obtenerSiguientePaciente();

                    if (pacienteActual == null) {
                        Thread.sleep(100); // espera un poco antes de volver al ciclo
                        continue;
                    }

                    centro.log("[" + nombre + "] Enfermero disponible, selecciona y atiende a: " + pacienteActual);
                }

                // Simula la atención médica
                Thread.sleep(pacienteActual.getDuracionAtencion() * 100);
                centro.log("[" + nombre + "] Termina atención de: " + pacienteActual);

                synchronized (centro) {
                    centro.removerPaciente(pacienteActual);
                    pacienteActual = null;
                }

                atendio = true;

            } finally {
                enfermero.liberarEnfermero();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

    private boolean tienePacientesPendientes() {
        synchronized (centro) {
            return pacienteActual != null;
        }
    }
}
