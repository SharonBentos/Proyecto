package com.example;

import java.io.*;
import java.util.*;

public class Configuracion {
    private final List<String> lineas = new ArrayList<>();

    public static Configuracion leerConfiguracion(String archivo) {
        Configuracion config = new Configuracion();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                config.lineas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    // Genera la agenda: clave = minuto del día, valor = lista de pacientes que llegan en ese momento
    public Map<Integer, List<Paciente>> generarAgenda() {
        Map<Integer, List<Paciente>> agenda = new HashMap<>();
        for (String l : lineas) {
            if (l.isEmpty() || l.startsWith("#")) continue;

            try {
                String[] partes = l.split(";");
                String nombre = partes[0];
                Paciente.Tipo tipo = Paciente.Tipo.valueOf(partes[1]);
                String[] horaMinuto = partes[2].split(":");
                int hora = Integer.parseInt(horaMinuto[0]);
                int minuto = Integer.parseInt(horaMinuto[1]);
                int tiempoAtencion = Integer.parseInt(partes[3]);

                Tiempo llegada = new Tiempo(hora, minuto);
                Paciente p = new Paciente(nombre, tipo, llegada, tiempoAtencion);

                int tiempoEnMinutos = llegada.minutosTotales();
                agenda.putIfAbsent(tiempoEnMinutos, new ArrayList<>());
                agenda.get(tiempoEnMinutos).add(p);
            } catch (Exception e) {
                System.err.println("Error al procesar línea: " + l);
                e.printStackTrace();
            }
        }
        return agenda;
    }
}
