package co.edu.uniquindio.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paquetes implements Serializable {
    private ArrayList<Destinos> destinos;
    private ArrayList<Float> calificaciones;
    private LocalDate inicio, fin;
    private String nombre;
    private String duracion, servicios;
    private int numeroPersonas, cantReservas;
    private float precio;
    public void añadirCalificacion(Float calificacion)
    {
        calificaciones.add(calificacion);
    }
    public ArrayList<Float> calificaciones(){
        return this.calificaciones;
    }
    public float promedio()
    {
        float promedio = 0;
        for(int i = 0 ; i< calificaciones.size();i++)
        {
            promedio += (float)calificaciones.get(i);
        }
        return promedio/calificaciones.size();
    }
}
