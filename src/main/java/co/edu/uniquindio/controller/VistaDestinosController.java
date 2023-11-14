package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Destinos;
import co.edu.uniquindio.model.Paquetes;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import co.edu.uniquindio.utils.Propiedades;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VistaDestinosController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());

    @FXML
    private TableView<Destinos> tablaDestinos;
    @FXML
    private TableColumn<Destinos, String> colNombre, colCiudad, colDescripcion, colClima;

    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Label txtDestinosSistema;
    @FXML
    private Button regresar, eliminar, editar;
    public void cargarTextos()
    {
        txtDestinosSistema.setText(propiedades.getResourceBundle().getString("txtDestinosSistema"));
        regresar.setText(propiedades.getResourceBundle().getString("bttVolver"));
        eliminar.setText(propiedades.getResourceBundle().getString("eliminarDestino"));
        editar.setText(propiedades.getResourceBundle().getString("bttEditar"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
        agencia.inicializarDatos();
        ObservableList<Destinos> destinosA = FXCollections.observableArrayList(agencia.enviarDestinos());
        cargarTablas(destinosA);
    }
    public void cargarTablas(ObservableList<Destinos> destinosA)
    {
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCiudad()));
        colClima.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClima()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        tablaDestinos.setItems(destinosA);
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaAdministrativa.fxml", actionEvent, "Se carga la pagina de administrador");
    }

    public void eliminar(ActionEvent actionEvent) {
        if (tablaDestinos.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento cancelar un destino sin haberlo seleccionado");
            agencia.mostrarMensaje(Alert.AlertType.ERROR, "Se intento eliminar un destino sin haberlo seleccionado");
        } else {
            agencia.recibirDestinoCancelacion(tablaDestinos.getSelectionModel().getSelectedItem());
            agencia.loadStage("/paginaVistaDestinos.fxml",actionEvent, "Se cargo la pagina actualizada");
        }
    }
    public void editar(ActionEvent actionEvent) {
        if (tablaDestinos.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento editar un destino sin haberlo seleccionado");
            agencia.mostrarMensaje(Alert.AlertType.ERROR, "Se intento editar un destino sin haberlo seleccionado");
        } else {
            agencia.recibirDestinoEdicion(tablaDestinos.getSelectionModel().getSelectedItem());
            agencia.loadStage("/paginaEdicionDestino.fxml",actionEvent,"Se va a editar un destino");
        }
    }
}
