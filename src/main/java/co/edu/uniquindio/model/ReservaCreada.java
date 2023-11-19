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
public class ReservaCreada implements Serializable {
    private LocalDate fechaSolicitud, fechaPlanificada;
    private Clientes cliente;
    private String numeroPersonas;
    private Paquetes paquete;
    private double valorTotal, descuento;
    private Guias guia;
    private String estado;
}
