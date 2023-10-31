package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.model.*;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import co.edu.uniquindio.model.Agencia;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
public class RealizarReservaController implements Initializable, CambioIdiomaListener {
    private Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    private final Agencia agencia = Agencia.getInstance();
    private Paquetes paquete = new Paquetes();
    private Clientes cliente = new Clientes();
    private ArrayList<Guias> guiasBase = new ArrayList<>();
    @FXML
    private ComboBox<Guias> comboGuia;
    @FXML
    private TextField personas;
    @FXML
    private DatePicker inicio,fin;
    @FXML
    private Label paqueteSeleccionado;

    public RealizarReservaController() {
    }

    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paquete = agencia.paqueteSeleccion();
        cliente = agencia.clienteSesion();
        paqueteSeleccionado.setText(paquete.getNombre());
        llenarListaGuias();
    }
    public void llenarListaGuias() {
        guiasBase = agencia.enviarGuiasPaquete(paquete);
        if(guiasBase.isEmpty())
        {
            LOGGER.log(Level.INFO,"Se intento generar una lista de elementos vacios");
        }else
        {
            guiasBase.stream().toList();
            ObservableList<Guias> listaGuias = FXCollections.observableArrayList(guiasBase);
            System.out.print("Lista combo" + listaGuias);
            comboGuia.setItems(listaGuias);
            cargarAtributos();
        }
    }
    public void cargarAtributos(){
        comboGuia.setCellFactory(new Callback<ListView<Guias>, ListCell<Guias>>() {
        @Override
        public ListCell<Guias> call(ListView<Guias> param) {
            return new ListCell<Guias>() {
                @Override
                protected void updateItem(Guias item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getNombre() + " " + item.getIdentificacion());
                    }
                }
            };
        }
    });

        // Configurar la fábrica de celdas para mostrar el nombre y la descripción en el menú desplegable
        comboGuia.setButtonCell(new ListCell<Guias>() {
            @Override
            protected void updateItem(Guias item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getNombre() + " - " + item.getIdentificacion());
                }
            }
        });
    }
    public void reservar(ActionEvent actionEvent) {
        try
        {
            agencia.realizarReserva(paquete,cliente,inicio.getValue(),fin.getValue(),personas.getText(),comboGuia.getSelectionModel().getSelectedItem(),"PENDIENTE");
            agencia.loadStage("/paginaClienteSeleccionDestino.fxml", actionEvent, "Se regresa al apartado de destinos");
        }
        catch (CampoRepetido| CampoObligatorioException| CampoVacioException e)
        {
            mostrarMensaje(Alert.AlertType.WARNING, e.getMessage());
        }
    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
    public void volver(ActionEvent actionEvent) {
        agencia.loadStage("/paginaCaracteristicasPaquete.fxml",actionEvent,"Se regresa al menu de vista");
    }
}
