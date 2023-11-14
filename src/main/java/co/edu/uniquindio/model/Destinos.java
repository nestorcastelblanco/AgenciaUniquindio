package co.edu.uniquindio.model;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Destinos implements Serializable {
    private String nombre, ciudad, descripcion, clima;
    private ArrayList<String> imagenes = new ArrayList<>();
    private ArrayList<Float> calificaciones = new ArrayList<>();
    private int contReservas;
    private int contBusquedas;
    private int numero;

    public void addImagenes(String idioma) {
        this.imagenes.add(idioma);
    }
    public ArrayList<String> getImagenes() {
        return imagenes;
    }
    public void setImagenes(ArrayList<String> imagen) {
        imagenes = imagen;
    }

    public void añadirCalificacion(float calificacion)
    {
        calificaciones.add(calificacion);
    }
    public ArrayList<Float> calificaciones(){
        return this.calificaciones;
    }

}
