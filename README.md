# SimuladorCentroMedico - Trabajo Obligatorio Sistemas Operativos

## Descripción general

Este proyecto simula el funcionamiento de un centro médico bajo restricciones de recursos, modelando conceptos de planificación de procesos inspirados en sistemas operativos. El programa permite generar diferentes escenarios de llegada de pacientes y distribuye su atención en base a la disponibilidad de recursos, tiempo y tipo de paciente.

Actualmente el simulador está implementado con:

* Lectura de pacientes desde archivo.
* Reloj de simulación.
* Carga de pacientes por agenda.
* Planificación de atención con **priorización de emergencias**.
* Separación de colas de emergencias y generales.
* Salida de la simulación a un archivo de texto (`resultado_simulacion.txt`).

---

## Objetivo general del simulador

* Modelar el trabajo del centro médico como un sistema con recursos limitados (CPU, memoria, dispositivos de E/S).
* Permitir definir los horarios de llegada de pacientes desde un archivo.
* Ejecutar una simulación minuto a minuto, gestionando la carga de pacientes en cola y su posterior asignación.
* Implementar lógica de planificación de atención donde las emergencias siempre tienen prioridad.
* Registrar el desarrollo de la simulación en un archivo para facilitar el análisis de los resultados.
* Preparar la arquitectura de hilos para incorporar futuras mejoras (aging, múltiples médicos, estadísticas, etc.).

---

## Lógica de planificación implementada

* Se utilizan **dos colas separadas**:

  * Una para pacientes de tipo **EMERGENCIA**.
  * Otra para pacientes de tipo **GENERAL**.
* El planificador:

  * Siempre termina de atender el paciente actual (no hay interrupción durante la atención).
  * Al finalizar, revisa primero si hay emergencias en la cola.
  * Si hay emergencias, las atiende antes de seguir con los generales.
  * Si no hay emergencias, continúa atendiendo generales en orden de llegada (FIFO).

Este comportamiento refleja el funcionamiento de muchos sistemas operativos reales: prioridad absoluta sin preempción.

---

## Ejemplo de funcionamiento con los datos actuales

Archivo de configuración actual:

```
8;05;GENERAL;15;Axel
8;10;GENERAL;15;Dibot
8;15;EMERGENCIA;10;Sharon
9;30;GENERAL;15;Gaston
9;30;EMERGENCIA;15;Guzman
10;00;GENERAL;15;Manuela
10;01;GENERAL;15;Manuel
10;02;GENERAL;15;Juan
10;03;EMERGENCIA;10;Emiliano
10;45;EMERGENCIA;10;Abril
11;15;GENERAL;15;Sol
12;15;EMERGENCIA;15;Echagüe
```

### Caso particular a destacar:

* A las 10:00 ingresan 1 pacientes generales.
* El planificador comienza a atender al primer general (toma 15 minutos).
* Mientras tanto:

  * A las 10:01 llega otro general.
  * A las 10:02 llega otro general.
  * A las 10:03 llega una emergencia.
* Cuando finaliza el primer general, el planificador revisa las colas:

  * Encuentra la emergencia esperando.
  * Atiende inmediatamente a la emergencia.
  * Luego continúa atendiendo los generales que siguen en la cola.

Este es el comportamiento esperado:
Se garantiza la atención prioritaria de emergencias, sin interrumpir la atención que ya está en curso.

---

## Arquitectura del código

### 1. SimuladorCentroMedico.java

* Es el `main` del programa.
* Carga la configuración.
* Inicializa el centro médico.
* Lanza los hilos de reloj, recepcionista y planificador.
* Redirige toda la salida a `resultado_simulacion.txt`.

### 2. CentroMedico.java

* Es el núcleo del sistema.
* Lleva el control del tiempo.
* Administra las colas de pacientes.
* Controla el inicio y fin de la simulación.
* Registra los logs de la simulación.

### 3. Tiempo.java

* Maneja el tiempo de la simulación.
* Permite avanzar minutos, comparar tiempos y dar formato `HH:mm`.

### 4. Paciente.java

* Representa cada paciente.
* Guarda el tipo (GENERAL o EMERGENCIA), el horario de llegada y la duración de la atención.
* Tiene su propio `toString()` para mostrar información.

### 5. Configuracion.java

* Lee el archivo `configuracion.txt`.
* Convierte las líneas en pacientes y genera la agenda de llegadas minuto a minuto.

### 6. SimuladorReloj.java

* Simula el reloj interno del centro médico.
* Cada 100ms avanza 1 minuto de simulación.
* Termina cuando el horario llega a las 20:00.

### 7. Recepcionista.java

* Cada minuto verifica la agenda y agrega los pacientes que deben ingresar en ese momento.
* Los pacientes son distribuidos en las colas correspondientes.

### 8. Planificador.java

* Atiende pacientes según las reglas de planificación.
* Siempre prioriza emergencias.
* Registra la atención de cada paciente en el archivo de salida.

---

## Formato del archivo de configuración (configuracion.txt)

Cada línea tiene la forma:

```
HORA;MINUTO;TIPO;CANTIDAD;TIEMPO_ATENCION
```

Por ejemplo:

```
10;03;EMERGENCIA;1;10
```

Significa: a las 10:03 llega una emergencia que requiere 10 minutos de atención.

---

## Ciclo de ejecución

1. Se lee el archivo de configuración.
2. Se inicializa el reloj a las 08:00.
3. En cada minuto:

   * El recepcionista carga los pacientes que deben ingresar.
   * Los pacientes son puestos en sus respectivas colas.
   * El planificador atiende:

     * Emergencias primero.
     * Generales después.
4. La simulación finaliza automáticamente al llegar a las 20:00.
