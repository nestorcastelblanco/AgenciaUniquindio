package co.edu.uniquindio.model;
import com.sun.javafx.scene.shape.ArcHelper;
import lombok.*;
import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Clientes implements Serializable {
    private ArrayList<String> busquedas = new ArrayList<>();
    private String nombreCompleto, correo, direccion,identificacion,ciudad, telefono, usuario, contrasena;

    public void añadirBusqueda(String busqueda)
    {
        busquedas.add(busqueda);
    }
    public ArrayList<String> getBusquedas(){
        return busquedas;
    }

}