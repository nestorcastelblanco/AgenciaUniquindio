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
import java.util.logging.Logger;

public class PortalAgenciaController implements Initializable, CambioIdiomaListener {
        private final Agencia agencia = Agencia.getInstance();
        private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
        private ObservableList<Destinos> destinos = FXCollections.observableArrayList(agencia.ordenarPorRepeticiones());
        @FXML
        private TableView<Destinos> tablaDestinos;
        @FXML
        private TableColumn<Destinos, String> colDestinos;
        @Override
        public void onCambioIdioma(CambioIdiomaEvent evento) {
        }
        @Override
        public void initialize(URL location, ResourceBundle resources) {
            colDestinos.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
            tablaDestinos.setItems(destinos);
        }
        public void regresar(ActionEvent actionEvent) {
            agencia.loadStage("/paginaPrincipal.fxml", actionEvent, "Se regresa al login");
        }

    public void editarPerfil(ActionEvent actionEvent) {
            agencia.loadStage("/paginaEdicionPerfil.fxml", actionEvent,"Se ingresa al apartado de edicion de perfil");
    }

    public void seleccionPaquetes(ActionEvent actionEvent) {
            agencia.loadStage("/paginaClienteSeleccionDestino.fxml", actionEvent,"Se ingresa a la pagina de seleccion de paquete");
    }

    public void reservas(ActionEvent actionEvent) {
            agencia.loadStage("/paginaReservasCliente.fxml", actionEvent, "Se ingreso a la visualizacion de reservas");
    }
}
