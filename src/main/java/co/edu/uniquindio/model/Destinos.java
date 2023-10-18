package co.edu.uniquindio.model;

import lombok.*;

import java.io.Serializable;
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Destinos implements Serializable {
    private String nombre, ciudad, descripcion, clima;
}
