package co.edu.uniquindio.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservas implements Serializable {
    private LocalDate fechaSolicitud, fechaPlanificada;
    boolean calificacion;
    private Clientes cliente;
    private int numeroPersonas, codigo;
    private Paquetes paquete;
    private float valorTotal;
    private Guias guia;
    private String estado;
}
