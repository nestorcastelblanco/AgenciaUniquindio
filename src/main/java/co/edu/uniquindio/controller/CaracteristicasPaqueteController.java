package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Paquetes;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class CaracteristicasPaqueteController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
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
        nombre.setText(paqueteSeleccionado.getNombre());
        destinos.setText(agencia.obtenerNombresCiudades(paqueteSeleccionado.getDestinos()));
        duracion.setText(paqueteSeleccionado.getDuracion() + " dias");
        precioPersona.setText(paqueteSeleccionado.getPrecio()+ " $");
        fechaInicio.setText(paqueteSeleccionado.getInicio().toString());
        fechaFin.setText(paqueteSeleccionado.getFin().toString());
        cupos.setText(paqueteSeleccionado.getNumeroPersonas()+"");
        servicios.setText(paqueteSeleccionado.getServicios());
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaCaracteristicasPaquete.fxml", actionEvent,"Se carga la ventana de seleccion de paquete");
    }
}
