package co.edu.uniquindio.model;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionesCompletas implements Serializable {
    private ArrayList<Destinos> destinos;
    private ArrayList<Integer> calificacionDestinos;
    private int calificacionGuia;
    private Guias guia;
    private Paquetes paquete;
}
