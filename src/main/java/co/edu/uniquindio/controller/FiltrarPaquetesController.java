package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Paquetes;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FiltrarPaquetesController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private final Logger LOGGER = Logger.getLogger(PrincipalController.class.getName());
    @FXML
    private TextField paquetesFiltro,destino,persona,services,valor;
    @FXML
    private DatePicker inicio,fin;
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
    public FiltrarPaquetesController() {
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paquete.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        destinos.setCellValueFactory(cellData -> new SimpleStringProperty(agencia.obtenerNombresDestinos(cellData.getValue().getDestinos())));
        personas.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroPersonas() + ""));
        servicios.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServicios()));
        valorPersona.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrecio() + ""));
        fechaInicio.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInicio().toString()));
        fechaFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFin().toString()));
        tablaPaquetes.setItems(paquetes);

        paquetesFiltro.textProperty().addListener((observable, oldValue, newValue) ->
                tablaPaquetes.setItems(filtrarPorNombre(newValue)));

        // Configurar el filtro para el destino
        destino.textProperty().addListener((observable, oldValue, newValue) ->
                tablaPaquetes.setItems(filtrarPorDestino(newValue)));

        persona.textProperty().addListener((observable, oldValue, newValue) ->
                tablaPaquetes.setItems(filtrarPorPersonas(newValue)));

        // Configurar el filtro para los servicios
        services.textProperty().addListener((observable, oldValue, newValue) ->
                tablaPaquetes.setItems(filtrarPorServicios(newValue)));

        // Configurar el filtro para el valor por persona
        valor.textProperty().addListener((observable, oldValue, newValue) ->
                tablaPaquetes.setItems(filtrarPorValorPersona(newValue)));

        // Configurar el filtro para la fecha de inicio
        inicio.valueProperty().addListener((observable, oldValue, newValue) ->
                tablaPaquetes.setItems(filtrarPorFechaInicio(newValue)));

        // Configurar el filtro para la fecha final
        fin.valueProperty().addListener((observable, oldValue, newValue) ->
                tablaPaquetes.setItems(filtrarPorFechaFin(newValue)));
    }

    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaAdministrativa.fxml", actionEvent, "Se regresa a la pagina administrativa");
    }

    public void editar(ActionEvent actionEvent) {
        if (tablaPaquetes.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento editar un paquete sin haberlo seleccionado");
        } else {
            agencia.recibirPaqueteSeleccionado(tablaPaquetes.getSelectionModel().getSelectedItem());
            agencia.loadStage("/paginaSeleccionPaquete.fxml",actionEvent,"Se va a observar un paquete");
        }
    }
    private ObservableList<Paquetes> filtrarPorNombre(String nombre) {
        return paquetes.filtered(paquete -> paquete.getNombre().toLowerCase().contains(nombre.toLowerCase()));
    }

    private ObservableList<Paquetes> filtrarPorDestino(String destino) {
        return paquetes.filtered(paquete ->
                agencia.obtenerNombresDestinos(paquete.getDestinos()).toLowerCase().contains(destino.toLowerCase()));
    }
    private ObservableList<Paquetes> filtrarPorPersonas(String personas) {
        return paquetes.filtered(paquete -> String.valueOf(paquete.getNumeroPersonas()).contains(personas));
    }

    private ObservableList<Paquetes> filtrarPorServicios(String servicios) {
        return paquetes.filtered(paquete -> paquete.getServicios().toLowerCase().contains(servicios.toLowerCase()));
    }

    private ObservableList<Paquetes> filtrarPorValorPersona(String valorPersona) {
        return paquetes.filtered(paquete -> {
            try {
                double valor = Double.parseDouble(valorPersona);
                return paquete.getPrecio() == valor;
            } catch (NumberFormatException e) {
                return false;
            }
        });
    }
    private ObservableList<Paquetes> filtrarPorFechaInicio(LocalDate fechaInicio) {
        return paquetes.filtered(paquete -> paquete.getInicio().isEqual(fechaInicio));
    }
    private ObservableList<Paquetes> filtrarPorFechaFin(LocalDate fechaFin) {
        return paquetes.filtered(paquete -> paquete.getFin().isEqual(fechaFin));
    }
}
