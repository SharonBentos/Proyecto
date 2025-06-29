package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SimuladorCentroMedico {
    public static void main(String[] args) {
        try (PrintWriter logWriter = new PrintWriter(new FileWriter("src/main/java/com/example/resultado_simulacion.txt"))) {
            Configuracion config = Configuracion.leerConfiguracion("src/main/java/com/example/configuracion.txt");
            CentroMedico centro = new CentroMedico(config, logWriter);

            Enfermero enfermero = new Enfermero();

            // Crear y arrancar médicos, pasándoles el enfermero
            Medico medico1 = new Medico(1, centro, enfermero);
            Medico medico2 = new Medico(2, centro, enfermero);
            Medico medico3 = new Medico(3, centro, enfermero);
            List<Medico> medicos = new ArrayList<>();
            medicos.add(medico1);
            medicos.add(medico2);
            medicos.add(medico3);
            // ... si quieres más médicos simplemente los agregas aquí

            centro.iniciarSimulacion(medicos, enfermero);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}