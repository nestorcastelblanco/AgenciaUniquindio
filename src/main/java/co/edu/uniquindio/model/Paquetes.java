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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Paquetes implements Serializable {
    private ArrayList<Destinos> destinos;
    private LocalDate inicio, fin;

    @EqualsAndHashCode.Include
    private String nombre;

    private String duracion, servicios;
    private int numeroPersonas, cantReservas;
    private float precio;
}
