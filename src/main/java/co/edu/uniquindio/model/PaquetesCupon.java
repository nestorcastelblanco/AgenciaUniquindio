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
public class PaquetesCupon implements Serializable {
    private ArrayList<Destinos> destinos;
    private ArrayList<Float> calificaciones;
    private LocalDate inicio, fin, inicioCupon, finCupon;
    private String nombre;
    private String duracion, servicios;
    private String numeroPersonas, cantReservas;
    private String precio, valorCupon;
    private String cupon;
    public void añadirCalificacion(Float calificacion)
    {
        calificaciones.add(calificacion);
    }
    public ArrayList<Float> calificaciones(){
        return this.calificaciones;
    }

}
