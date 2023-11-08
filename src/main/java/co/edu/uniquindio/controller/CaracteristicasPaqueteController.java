package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Paquetes;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import co.edu.uniquindio.utils.Propiedades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CaracteristicasPaqueteController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private int numeroImagenes, cont;
    private ArrayList<String> arrayListImagenes = new ArrayList<>();
    @FXML
    private ImageView imageView,estrella1,estrella2,estrella3,estrella4,estrella5;
    @FXML
    private Button btnAnterior;
    @FXML
    private Button btnSiguiente, seleccionar, regresar;
    @FXML
    private Label nombre, destinos, duracion, precioPersona, fechaInicio, fechaFin, cupos,servicios;
    @FXML
    private Label txtSeleccionPaquete, txtNombre, txtDestinos, txtDisponible, txtPrecio, txtInicio, txtFin,txtCupos,txtServicios;
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    public void cargarTextos()
    {
        txtSeleccionPaquete.setText(propiedades.getResourceBundle().getString("seleccionPaquete"));
        txtNombre.setText(propiedades.getResourceBundle().getString("nombrePaquete"));
        txtDisponible.setText(propiedades.getResourceBundle().getString("disponible"));
        txtPrecio.setText(propiedades.getResourceBundle().getString("precioPersona"));
        txtInicio.setText(propiedades.getResourceBundle().getString("fechaI"));
        txtFin.setText(propiedades.getResourceBundle().getString("fechaF"));
        txtCupos.setText(propiedades.getResourceBundle().getString("cupos"));
        txtServicios.setText(propiedades.getResourceBundle().getString("servi"));
        regresar.setText(propiedades.getResourceBundle().getString("bttVolver"));
        seleccionar.setText(propiedades.getResourceBundle().getString("seleccionar"));
        txtDestinos.setText(propiedades.getResourceBundle().getString("nombreDestinos"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
        System.out.println("Se inicio el controlador");
        Paquetes paqueteSeleccionado  = agencia.paqueteSeleccion();
        nombre.setText(paqueteSeleccionado.getNombre());
        destinos.setText(agencia.obtenerNombresCiudades(paqueteSeleccionado.getDestinos()));
        duracion.setText(paqueteSeleccionado.getDuracion() + " dias");
        precioPersona.setText(paqueteSeleccionado.getPrecio()+ " $");
        fechaInicio.setText(paqueteSeleccionado.getInicio().toString());
        fechaFin.setText(paqueteSeleccionado.getFin().toString());
        cupos.setText(paqueteSeleccionado.getNumeroPersonas()+"");
        servicios.setText(paqueteSeleccionado.getServicios());
        for(int i = 0 ; i<paqueteSeleccionado.getDestinos().size();i++)
        {
            arrayListImagenes.addAll(paqueteSeleccionado.getDestinos().get(i).getImagenes());
        }
        System.out.println(arrayListImagenes.toString());
        numeroImagenes = arrayListImagenes.size();
        cont = 0;
        for(int i = 0 ; i<arrayListImagenes.size();i++)
        {
            System.out.println("Imagenes: " + i + " " + arrayListImagenes.get(i));
        }
        Image imagen = new Image(arrayListImagenes.get(cont));
        imageView.setImage(imagen);

        if(paqueteSeleccionado.promedio() > 0 && paqueteSeleccionado.promedio()<=1)
        {
            Image imagenes = new Image("Imagenes/estrella.jpg");
            Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
            estrella1.setImage(imagenes);
            estrella2.setImage(imagenes);
            estrella3.setImage(imagenGris);
            estrella4.setImage(imagenGris);
            estrella5.setImage(imagenGris);
        }if(paqueteSeleccionado.promedio() > 1 && paqueteSeleccionado.promedio()<=2)
        {
            Image imagenes = new Image("Imagenes/estrella.jpg");
            Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
            estrella1.setImage(imagenes);
            estrella2.setImage(imagenes);
            estrella3.setImage(imagenes);
            estrella4.setImage(imagenGris);
            estrella5.setImage(imagenGris);
        }if(paqueteSeleccionado.promedio() >2 && paqueteSeleccionado.promedio()<=3)
        {
            Image imagenes = new Image("Imagenes/estrella.jpg");
            Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
            estrella1.setImage(imagenes);
            estrella2.setImage(imagenes);
            estrella3.setImage(imagenes);
            estrella4.setImage(imagenGris);
            estrella5.setImage(imagenGris);
        }if(paqueteSeleccionado.promedio() > 3 && paqueteSeleccionado.promedio()<=4)
        {
            Image imagenes = new Image("Imagenes/estrella.jpg");
            Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
            estrella1.setImage(imagenes);
            estrella2.setImage(imagenes);
            estrella3.setImage(imagenes);
            estrella4.setImage(imagenes);
            estrella5.setImage(imagenGris);
        }if(paqueteSeleccionado.promedio() > 4 && paqueteSeleccionado.promedio()<=5)
        {
            Image imagenes = new Image("Imagenes/estrella.jpg");
            Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
            estrella1.setImage(imagenes);
            estrella2.setImage(imagenes);
            estrella3.setImage(imagenes);
            estrella4.setImage(imagenes);
            estrella5.setImage(imagenes);
        }
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaClienteSeleccionDestino.fxml", actionEvent,"Se carga la ventana de seleccion de paquete");
    }
    public void atras(ActionEvent actionEvent){
        cont-=1;
        if(cont == -1)
        {
            cont = numeroImagenes;
            Image imagen = new Image(arrayListImagenes.get(arrayListImagenes.size()-1));
            imageView.setImage(imagen);
        }else {
            Image imagen = new Image(arrayListImagenes.get(cont));
            imageView.setImage(imagen);
        }
    }
    public void siguiente(ActionEvent actionEvent) {
        cont+=1;
        if(cont == numeroImagenes)
        {
            cont = 0;
            Image imagen = new Image(arrayListImagenes.get(cont));
            imageView.setImage(imagen);
        }else {
            Image imagen = new Image(arrayListImagenes.get(cont));
            imageView.setImage(imagen);
        }
    }

    public void seleccionado(ActionEvent actionEvent) {
        agencia.loadStage("/paginaRealizarReserva.fxml", actionEvent, "Se ingresa a realizar una reserva");
    }
}
