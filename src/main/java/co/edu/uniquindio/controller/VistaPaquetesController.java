package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VistaPaquetesController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    @FXML
    private TableView<Paquetes> tablaPaquetes;
    @FXML
    private TableColumn<Paquetes, String> paquete;
    @FXML
    private TableColumn<Paquetes, String> destinos;
    @FXML
    private TableColumn<Paquetes, String> personas;
    @FXML
    private TableColumn<Paquetes, String> servicios;
    @FXML
    private TableColumn<Paquetes, String> valorPersona;
    @FXML
    private TableColumn<Paquetes, String> fechaInicio;
    @FXML
    private TableColumn<Paquetes, String> fechaFin;
    private ObservableList<Paquetes> paquetes = FXCollections.observableArrayList(agencia.enviarPaquetes());
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Label txtPaquetesSistema;
    @FXML
    private Button regresar, eliminar, editar;
    public void cargarTextos()
    {
        txtPaquetesSistema.setText(propiedades.getResourceBundle().getString("txtPaquetesSistema"));
        regresar.setText(propiedades.getResourceBundle().getString("bttVolver"));
        eliminar.setText(propiedades.getResourceBundle().getString("eliminarDestino"));
        editar.setText(propiedades.getResourceBundle().getString("bttEditar"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
        paquete.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        destinos.setCellValueFactory(cellData -> new SimpleStringProperty(agencia.obtenerNombresCiudades(cellData.getValue().getDestinos())));
        personas.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroPersonas() + ""));
        servicios.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServicios()));
        valorPersona.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrecio() + ""));
        fechaInicio.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInicio().toString()));
        fechaFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFin().toString()));
        tablaPaquetes.setItems(paquetes);
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaAdministrativa.fxml", actionEvent, "Se regresa a la pagina administrativa");
    }
    public void editar(ActionEvent actionEvent) {
        if (tablaPaquetes.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento editar un paquete sin haberlo seleccionado");
        } else {
            agencia.recibirPaqueteEdicion(tablaPaquetes.getSelectionModel().getSelectedItem());
            agencia.loadStage("/paginaEdicionPaquete.fxml",actionEvent,"Se va a editar un paquete");
        }
    }
    public void eliminar(ActionEvent actionEvent) {
        if (tablaPaquetes.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento cancelar un paquete sin haberlo seleccionado");
        } else {
            agencia.recibirPaqueteCancelacion(tablaPaquetes.getSelectionModel().getSelectedItem());
            agencia.loadStage("/paginaVistaPaquetes.fxml",actionEvent, "Se cargo la pagina actualizada");
        }
    }
}
