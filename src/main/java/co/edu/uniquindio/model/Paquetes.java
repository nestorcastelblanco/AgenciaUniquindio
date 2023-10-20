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
    private LocalDate inicio, fin;
    private String duracion, servicios, nombre;
    private int numeroPersonas;
    private float precio;
    public void addDestino(Destinos destinos) {
        this.destinos.add(destinos);
    }
    public ArrayList<Destinos> getImagenes() {
        return destinos;
    }
}
