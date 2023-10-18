package co.edu.uniquindio.model;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservas implements Serializable {
    private LocalDate fechaSolicitud, fechaPlanificada;
    private Clientes cliente;
    private int numeroPersonas;
    private Paquetes paquete;
    private Guias guia;
    private String estado;
}
