package co.edu.uniquindio.model;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paquetes implements Serializable {
    private ArrayList<Destinos> paquetes;
    private LocalDate inicio, fin;
    private String duracion, servicios;
    private int numeroPersonas;
    private float precio;
}
