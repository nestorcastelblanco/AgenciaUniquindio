package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.utils.Propiedades;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MenuAdminController implements Initializable, CambioIdiomaListener {
    @FXML
    private Label titulo, nombre, identificacion, labelIdiomas,experiencia;
    @FXML
    private TextField nombreUsuario, id,idiomas, exp;
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    private final Propiedades propiedades = Propiedades.getInstance();
    private final Agencia agencia = Agencia.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
    }
    private void cargarTextos() {
    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaPrincipal.fxml", actionEvent,"Se ingreso a la pagina principal");
    }
    public void registrarGuia(ActionEvent actionEvent) {
        agencia.loadStage("/paginaRegistroGuias.fxml",actionEvent,"Se ingreso a la pagina de registro de Guia");
    }
    public void registrarDestino(ActionEvent actionEvent) {
        agencia.loadStage("/paginaCreacionDestino.fxml",actionEvent,"Se ingreso a la pagina de creacion de Destino");
    }
    public void creacionPaquetes(ActionEvent actionEvent) {
        agencia.loadStage("/paginaCreacionPaquete.fxml", actionEvent,"Se ingreso a la pagina de creacion de Paquetes");
    }

    public void mostrarPaquetes(ActionEvent actionEvent) {
        agencia.loadStage("/paginaVistaPaquetes.fxml", actionEvent, "Se ingresa a la pagina de vista de paquetes");
    }

    public void reservas(ActionEvent actionEvent) {
        agencia.loadStage("/paginaVistaReservas.fxml", actionEvent, "Se ingresa a la pagina de vista de reservas");
    }

    public void estadisticas(ActionEvent actionEvent) {
        agencia.loadStage("/paginaEstadisticas.fxml", actionEvent, "Se ingresa a la pagina de estadisticas");
    }

    public void mostrarGuias(ActionEvent actionEvent) {
        agencia.loadStage("/paginaGuias.fxml", actionEvent, "Se ingreso a la pagina de guias");
    }

    public void mostrarDestinos(ActionEvent actionEvent) {
        agencia.loadStage("/paginaVistaDestinos.fxml",actionEvent,"Se ingreso a la pagina de Destinos");
    }
}
