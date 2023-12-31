package co.edu.uniquindio.model;
import lombok.*;
import java.io.Serializable;
import java.util.ArrayList;
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Guias extends ArrayList<Guias> implements Serializable {
    private String nombre, identificacion, exp;
    private Integer promedioCalificacion;
    private int contViajes;
    private Paquetes paquete;
    private ArrayList<String> lenguajes = new ArrayList<>();
    private ArrayList<Integer> calificaciones = new ArrayList<>();

    public void addLenguajes(String idioma) {
        lenguajes.add(idioma);
    }
    public ArrayList<String> getLenguajes() {
        return lenguajes;
    }

    public void addCalificacion(Integer idioma) {
        calificaciones.add(idioma);
    }
    public ArrayList<Integer> getCalificaciones() {
        return calificaciones;
    }

}
