package co.edu.uniquindio.model;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class GuiaRegistro implements Serializable {
    private String nombre, identificacion, exp;
    private float promedioCalificacion;
    private Paquetes paquete;
    private String lenguajes;

}
