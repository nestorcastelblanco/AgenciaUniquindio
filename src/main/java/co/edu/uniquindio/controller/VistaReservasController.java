package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Paquetes;
import co.edu.uniquindio.model.Reservas;
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

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VistaReservasController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    @FXML
    private TableView<Reservas> tablaReservas;
    @FXML
    private TableColumn<Reservas, String> paquete;
    @FXML
    private TableColumn<Reservas, String> guia;
    @FXML
    private TableColumn<Reservas, String> cliente;
    @FXML
    private TableColumn<Reservas, String> personas;
    @FXML
    private TableColumn<Reservas, String> estado;
    @FXML
    private TableColumn<Reservas, String> fechaInicio;
    @FXML
    private TableColumn<Reservas, String> fechaFin;
    private ObservableList<Reservas> reservaciones = FXCollections.observableArrayList();
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Label txtReservasSistema;
    @FXML
    private Button regresar, eliminar, editar;
    public void cargarTextos()
    {
        txtReservasSistema.setText(propiedades.getResourceBundle().getString("txtReservasSistema"));
        regresar.setText(propiedades.getResourceBundle().getString("bttVolver"));
        eliminar.setText(propiedades.getResourceBundle().getString("eliminarDestino"));
        editar.setText(propiedades.getResourceBundle().getString("bttEditar"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reservaciones = FXCollections.observableArrayList(agencia.enviarReservas());
        paquete.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaquete().getNombre()));
        guia.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGuia().getNombre()));
        cliente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getNombreCompleto()));
        personas.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroPersonas()+""));
        estado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado()));
        fechaInicio.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaSolicitud().toString()));
        fechaFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaPlanificada().toString()));
        tablaReservas.setItems(reservaciones);
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaAdministrativa.fxml", actionEvent, "Se regresa a la pagina administrativa");
    }
    public void editar(ActionEvent actionEvent) {
        if (tablaReservas.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento editar un paquete sin haberlo seleccionado");
        } else {
            agencia.recibirReservaEdicion(tablaReservas.getSelectionModel().getSelectedItem());
            agencia.loadStage("/paginaEdicionReserva.fxml",actionEvent,"Se va a editar una reserva");
        }
    }
    public void eliminar(ActionEvent actionEvent) {
        if (tablaReservas.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento cancelar una reserva sin haberlo seleccionado");
        } else {
            agencia.recibirReservaCancelacion(tablaReservas.getSelectionModel().getSelectedItem());
            agencia.loadStage("/paginaVistaReservas.fxml",actionEvent, "Se cargo la pagina actualizada");
        }
    }
}
