package co.edu.uniquindio.model;
import lombok.*;
import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clientes implements Serializable {
    private String nombreCompleto, correo, direccion,identificacion,ciudad, telefono, usuario, contrasena;
}