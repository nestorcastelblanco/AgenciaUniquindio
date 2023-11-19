package co.edu.uniquindio.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteIngreso implements Serializable {
    private String usuario, contrasena, codigo;
}
