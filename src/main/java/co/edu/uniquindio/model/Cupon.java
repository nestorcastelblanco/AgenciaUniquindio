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
public class Cupon implements Serializable {
    private Paquetes paquete;
    private LocalDate inicio, fin;
    private String cupon, personas;
}
