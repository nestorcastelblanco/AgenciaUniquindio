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
public class Guias implements Serializable {
    private String nombre, identificacion, exp;
    private float promedioCalificacion;
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

    public float promedio()
    {
        float promedio = 0;
        for(int i = 0 ; i< calificaciones.size();i++)
        {
            promedio += (float)calificaciones.get(i);
        }
        return promedio/calificaciones.size();
    }
}
