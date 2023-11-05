package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Destinos;
import co.edu.uniquindio.model.Paquetes;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VistaInicioController implements Initializable, CambioIdiomaListener {
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
    @FXML
    private TableView<Destinos> tablaDestinos;
    @FXML
    private TableColumn<Destinos, String> colNombre, colCiudad, colDescripcion, colClima;

    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        agencia.inicializarDatos();
        ObservableList<Paquetes> paquetes = FXCollections.observableArrayList(agencia.enviarPaquetes());
        ObservableList<Destinos> destinosA = FXCollections.observableArrayList(agencia.enviarDestinos());
        cargarTablas(paquetes,destinosA);
    }
    public void cargarTablas(ObservableList<Paquetes> paquetes, ObservableList<Destinos> destinosA)
    {
        paquete.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        destinos.setCellValueFactory(cellData -> new SimpleStringProperty(agencia.obtenerNombresCiudades(cellData.getValue().getDestinos())));
        personas.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroPersonas() + ""));
        servicios.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServicios()));
        valorPersona.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrecio() + ""));
        fechaInicio.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInicio().toString()));
        fechaFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFin().toString()));
        tablaPaquetes.setItems(paquetes);

        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCiudad()));
        colClima.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClima()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        tablaDestinos.setItems(destinosA);
    }
    public void ingresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaPrincipal.fxml", actionEvent, "Se carga la pagina de loggin");
    }
}
