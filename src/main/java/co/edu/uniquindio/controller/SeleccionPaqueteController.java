package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Paquetes;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SeleccionPaqueteController implements Initializable, CambioIdiomaListener {

    private final Agencia agencia = Agencia.getInstance();
    @FXML
    private ImageView imageView;
    @FXML
    private Button btnAnterior;
    @FXML
    private Button btnSiguiente;

    private Paquetes paquetes;
    private int indicePaquete;
    private int indiceImagen;

    @FXML
    private void onAnteriorClicked() {
        if (indiceImagen > 0) {
            indiceImagen--;
        } else {
            indiceImagen = paquetes.getDestinos().get(indicePaquete).getImagenes().size() - 1;
        }
        mostrarImagenActual();
    }

    @FXML
    private void onSiguienteClicked() {
        int totalImagenes = paquetes.getDestinos().get(indicePaquete).getImagenes().size();
        indiceImagen = (indiceImagen + 1) % totalImagenes;
        mostrarImagenActual();
    }

    private void mostrarImagenActual() {
        String rutaImagen = paquetes.getDestinos().get(indicePaquete).getImagenes().get(indiceImagen);
        Image imagen = new Image("file:" + rutaImagen);
        imageView.setImage(imagen);
    }

    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.paquetes = agencia.paqueteSeleccion();
        this.indicePaquete = 0;
        this.indiceImagen = 0;
        mostrarImagenActual();
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaSeleccionPaquete.fxml", actionEvent,"Se carga la ventana de seleccion de paquete");
    }
}
