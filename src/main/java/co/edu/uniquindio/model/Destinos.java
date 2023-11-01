package co.edu.uniquindio.model;

import lombok.*;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Destinos implements Serializable {
    private String nombre, ciudad, descripcion, clima;
    private ArrayList<String> imagenes = new ArrayList<>();
    private int contReservas;
    private int contBusquedas;
    public void addImagenes(String idioma) {
        this.imagenes.add(idioma);
    }
    public ArrayList<String> getImagenes() {
        return imagenes;
    }
    public void setImagenes(ArrayList<String> imagen) {
        imagenes = imagen;
    }
}
