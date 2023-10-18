package co.edu.uniquindio.model;
import lombok.*;
import java.io.Serializable;
import java.util.ArrayList;
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Guias implements Serializable {
    private String nombre, identificacion, exp;
    private ArrayList<String> lenguajes;
}
