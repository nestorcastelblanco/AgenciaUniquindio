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
    private static ArrayList<String> lenguajes = new ArrayList<>();
    public void addLenguajes(String idioma) {
        this.lenguajes.add(idioma);
    }

    public ArrayList<String> getLenguajes() {
        return lenguajes;
    }
}
