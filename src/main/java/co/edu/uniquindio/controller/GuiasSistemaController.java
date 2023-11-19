package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.AgenciaCliente;
import co.edu.uniquindio.model.Guias;
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
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuiasSistemaController implements Initializable, CambioIdiomaListener {
    private final AgenciaCliente agencia = AgenciaCliente.getInstance();
    private final Logger LOGGER = Logger.getLogger(AgenciaCliente.class.getName());
    @FXML
    private TableView<Guias> tablaGuias;
    @FXML
    private TableColumn<Guias, String> nombres;
    @FXML
    private TableColumn<Guias, String> identificacion;
    @FXML
    private TableColumn<Guias, String> experiencia;
    @FXML
    private TableColumn<Guias, String> paquete;
    @FXML
    private TableColumn<Guias, String> idiomas;
    private ObservableList<Guias> guias = FXCollections.observableArrayList();
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Label txtGuiaSistema;
    @FXML
    private Button botonRegreso,botonEditar,botonEliminar;
    public void cargarTextos()
    {
        txtGuiaSistema.setText(propiedades.getResourceBundle().getString("tituloVistaGuias"));
        botonRegreso.setText(propiedades.getResourceBundle().getString("bttVolver"));
        botonEditar.setText(propiedades.getResourceBundle().getString("bttEditar"));
        botonEliminar.setText(propiedades.getResourceBundle().getString("bttEliminar"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
        guias = FXCollections.observableArrayList(agencia.enviarGuias());
        paquete.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaquete().getNombre()));
        nombres.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        identificacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdentificacion()));
        experiencia.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExp()+" años"));
        idiomas.setCellValueFactory(cellData -> new SimpleStringProperty(agencia.obtenerIdiomas(cellData.getValue().getLenguajes())));
        tablaGuias.setItems(guias);
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaAdministrativa.fxml", actionEvent, "Se regresa al portal de administracion");
    }
    public void eliminar(ActionEvent actionEvent) {
        if (tablaGuias.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento eliminar un guia sin haberlo seleccionado");
            mostrarMensaje(Alert.AlertType.ERROR, "Se intento eliminr un guia sin seleccionarlo");
        } else {
            agencia.recibirGuiaEliminado(tablaGuias.getSelectionModel().getSelectedItem());
            agencia.loadStage("/paginaGuias.fxml",actionEvent, "Se cargo la pagina actualizada");
        }
    }

    public void editar(ActionEvent actionEvent) {
        if (tablaGuias.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento editar un guia sin haberlo seleccionado");
            mostrarMensaje(Alert.AlertType.ERROR, "Se intento editar un guia sin haberlo seleccionado");
        } else {
            agencia.enviarGuiaEdicion(tablaGuias.getSelectionModel().getSelectedItem());
            agencia.loadStage("/paginaEdicionGuia.fxml", actionEvent, "Se carga la ventana de edicion de guia");
        }
    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}
