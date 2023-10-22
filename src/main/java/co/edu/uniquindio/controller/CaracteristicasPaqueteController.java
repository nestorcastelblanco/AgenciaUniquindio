package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Paquetes;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
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
    private ImageView imageView;
    @FXML
    private Button btnAnterior;
    @FXML
    private Button btnSiguiente;
    @FXML
    private Label nombre, destinos, duracion, precioPersona, fechaInicio, fechaFin, cupos,servicios;

    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Paquetes paqueteSeleccionado  = agencia.paqueteSeleccion();
        //System.out.println(paqueteSeleccionado.getDestinos().get(0).getImagenes().get(0));
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
        Image imagen = new Image(arrayListImagenes.get(cont));
        imageView.setImage(imagen);
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaCaracteristicasPaquete.fxml", actionEvent,"Se carga la ventana de seleccion de paquete");
    }

    public void atras(ActionEvent actionEvent){
        cont-=1;
        if(cont == -1)
        {
            cont = numeroImagenes;
            Image imagen = new Image(arrayListImagenes.get(cont));
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
