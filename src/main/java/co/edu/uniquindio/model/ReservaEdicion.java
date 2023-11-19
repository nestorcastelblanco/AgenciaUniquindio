package co.edu.uniquindio.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaEdicion implements Serializable {
    private Paquetes paquetes;
    private LocalDate fechaInicio,fechaFin;
    private String agregarPersonas,quitarPersonas;
    private Guias guias;
    private String estado;
}
