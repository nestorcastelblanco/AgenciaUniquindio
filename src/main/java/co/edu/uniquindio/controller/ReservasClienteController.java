package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Reservas;
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

public class ReservasClienteController implements Initializable, CambioIdiomaListener {
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
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(int i = 0 ; i<agencia.enviarReservasCliente().size();i++) {
            System.out.println(agencia.enviarReservasCliente().get(i).getCliente().getNombreCompleto() + " Codigo: " + agencia.enviarReservasCliente().get(i).getCodigo() + " Estado Calificacion: " + agencia.enviarReservasCliente().get(i).isCalificacion());
        }
        reservaciones = FXCollections.observableArrayList(agencia.enviarReservasCliente());
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
        agencia.loadStage("/portalAgencia.fxml", actionEvent, "Se regresa al portal");
    }
    public void cancelar(ActionEvent actionEvent) {
        if (tablaReservas.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento cancelar un paquete sin haberlo seleccionado");
        } else {
            agencia.recibirReservaCancelacion(tablaReservas.getSelectionModel().getSelectedItem());
            agencia.loadStage("/paginaReservasCliente.fxml",actionEvent, "Se cargo la pagina actualizada");
        }
    }

    public void calificar(ActionEvent actionEvent) {
        if (tablaReservas.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento calificar un paquete sin haberlo seleccionado");
        } else {
            if(agencia.recibirReservaCalificacion(tablaReservas.getSelectionModel().getSelectedItem()))
            {
                agencia.loadStage("/paginaCalificacion.fxml", actionEvent, "Se cargo la pagina de calificacion de Destinos");
            }else {
                LOGGER.log(Level.INFO, "Se trato de ingresar a calificar un paquete sin haber terminado");
            }
        }
    }
}
