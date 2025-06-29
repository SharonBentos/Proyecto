package com.example;

import java.util.*;
import java.io.PrintWriter;

public class CentroMedico {
    private final Paciente[] pacientes = new Paciente[10];
    private Tiempo tiempoActual = new Tiempo(8, 0);
    private final Tiempo tiempoFin = new Tiempo(20, 0);
    private final Configuracion config;
    private boolean finSimulacion = false;
    private final Map<Integer, List<Paciente>> agendaLlegadas;
    private final PrintWriter logWriter;

    public CentroMedico(Configuracion config, PrintWriter logWriter) {
        this.config = config;
        this.agendaLlegadas = config.generarAgenda();
        this.logWriter = logWriter;
    }

    public void iniciarSimulacion(List<Medico> medicos, Enfermero enfermero) {
    Thread reloj = new Thread(new SimuladorReloj(this));
    Thread recepcionista = new Thread(new Recepcionista(this));

    reloj.start();
    recepcionista.start();

    List<Thread> medicosHilos = new ArrayList<>();
    for (Medico medico : medicos) {
        Thread hiloMedico = new Thread(medico);
        medicosHilos.add(hiloMedico);
        hiloMedico.start();
    }

    try {
        reloj.join();
        recepcionista.join();
        for (Thread medico : medicosHilos) {
            medico.join();
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

public synchronized Tiempo getTiempoActual() {
        return new Tiempo(tiempoActual.hora, tiempoActual.minuto);
    }
    public synchronized boolean isFinSimulacion() {
        return finSimulacion;
    }

    public synchronized void agregarPaciente(Paciente p) {
        for (int i = 0; i < pacientes.length; i++) {
            if (pacientes[i] == null) {
                pacientes[i] = p;
                log("[Recepcion] Llega: " + p);
                return;
            }
        }
        log("[Recepcion] No hay lugar para: " + p);
    }

    public synchronized void removerPaciente(Paciente paciente) {
        for (int i = 0; i < pacientes.length; i++) {
            if (pacientes[i] == paciente) {
                pacientes[i] = null;
                return;
            }
        }
    }

    public synchronized Paciente obtenerSiguientePaciente() {
    List<Paciente> lista = new ArrayList<>();
    for (Paciente p : pacientes) {
        if (p != null) lista.add(p);
    }
    if (lista.isEmpty()) return null;

    Paciente siguiente = Collections.max(lista, Comparator.comparingInt(Paciente::getPrioridad));
    removerPaciente(siguiente);
    return siguiente;
}

    public synchronized Paciente[] getPacientes() {
        return pacientes;
    }

    public List<Paciente> obtenerLlegadasActuales() {
        int minutoActual = tiempoActual.minutosTotales();
        return agendaLlegadas.getOrDefault(minutoActual, new ArrayList<>());
    }

    public synchronized void log(String mensaje) {
        logWriter.println(mensaje);
        logWriter.flush();
    }

    public synchronized void avanzarMinuto() {
    tiempoActual.avanzarMinuto();

    for (Paciente p : pacientes) {
        if (p == null) continue;

        p.aumentarPrioridad(); // sigue aumentando prioridad como siempre

        switch (p.getTipo()) {
            case EMERGENCIA:
                if (p.getPrioridad() > 128) {
                    log("Paciente de EMERGENCIA evacuado: " + p);
                    removerPaciente(p);
                    // evacuados.add(p); // opcional
                }
                break;
            case URGENCIA:
                if (p.getPrioridad() > 118) { // luego de  1 hora y 50 pasa a ser de tipo emergencia 
                    log("Paciente URGENCIA ascendido a EMERGENCIA: " + p);
                    p.setTipo(Paciente.Tipo.EMERGENCIA);
                    // promovidos.add(p); // opcional
                }
                break;
            case CARNE_SALUD:
                if (p.getPrioridad() > 186) { // a las 3 horas se va del centro si no fue atendido (6x 3 = 180 + 6) (priori inicial))
                    log("Paciente por tramite de CARNÉ DE SALUD abandonó el centro luego de esperar durante 3 horas: " + p);
                    removerPaciente(p);
                    // abandonos.add(p); // opcional
                }
                break;
            case GENERAL_COMUN:
                if (p.getPrioridad() > 186) { // a las 3 horas se va del centro si no fue atendido
                    log("Paciente GENERAL abandonó el centro luego de esperar durante 3 horas: " + p);
                    removerPaciente(p);
                    // abandonos.add(p); // opcional
                }
                break;
        }
    }

    log("[Reloj] Tiempo actual: " + tiempoActual);

    if (tiempoActual.compareTo(tiempoFin) >= 0) {
        finSimulacion = true;
    }
}

}
