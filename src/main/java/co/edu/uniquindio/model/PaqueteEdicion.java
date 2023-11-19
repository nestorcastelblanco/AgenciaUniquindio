package co.edu.uniquindio.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PaqueteEdicion implements Serializable {
    private Paquetes paquete;
    private String nombre, servicios, cupos, valor;
    private ArrayList<Destinos>destinos;
    private LocalDate inicio, fin;
}
