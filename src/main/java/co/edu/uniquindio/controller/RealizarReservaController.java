package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.model.*;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import co.edu.uniquindio.utils.Propiedades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
public class RealizarReservaController implements Initializable, CambioIdiomaListener {
    private Logger LOGGER = Logger.getLogger(AgenciaCliente.class.getName());
    private final AgenciaCliente agencia = AgenciaCliente.getInstance();
    private Paquetes paquete = new Paquetes();
    private Clientes cliente = new Clientes();
    private ArrayList<Guias> guiasBase = new ArrayList<>();
    @FXML
    private ComboBox<Guias> comboGuia;
    @FXML
    private TextField personas, cupon;
    @FXML
    private DatePicker inicio,fin;
    @FXML
    private Label paqueteSeleccionado;

    public RealizarReservaController() {
    }
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Button regresar,reservar;
    @FXML
    private Label txtConfirmacion, txtPaquete, cantPersonas, fechaInicio, fechaFin, seleccionGuia;
    float valorDescuento = 0;
    public void cargarTextos()
    {
        txtConfirmacion.setText(propiedades.getResourceBundle().getString("pagConfirmacion"));
        txtPaquete.setText(propiedades.getResourceBundle().getString("paqueteU"));
        cantPersonas.setText(propiedades.getResourceBundle().getString("cantidadPersonas"));
        fechaInicio.setText(propiedades.getResourceBundle().getString("fechaI"));
        fechaFin.setText(propiedades.getResourceBundle().getString("fechaF"));
        seleccionGuia.setText(propiedades.getResourceBundle().getString("seleccionGuia"));
        reservar.setText(propiedades.getResourceBundle().getString("reservar"));
        regresar.setText(propiedades.getResourceBundle().getString("bttVolver"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
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
    public void reservar(ActionEvent actionEvent) throws CampoRepetido,CampoObligatorioException,CampoVacioException{
        String mensaje = agencia.realizarReserva(paquete,cliente,inicio.getValue(),fin.getValue(),personas.getText(),comboGuia.getSelectionModel().getSelectedItem(),"PENDIENTE", valorDescuento);
        if(mensaje.equals("Se genero la reserva correctamente")){
            mostrarMensaje(Alert.AlertType.CONFIRMATION, "Se ha generado la reserva correctamente");
            agencia.loadStage("/paginaClienteSeleccionDestino.fxml", actionEvent, "Se regresa al apartado de destinos");
        }else{
            mostrarMensaje(Alert.AlertType.WARNING, "No se ha generado la reserva correctamente");
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

    public void validar(ActionEvent actionEvent) {
        double valor = agencia.verificarCupon(paquete,inicio.getValue(), fin.getValue(),cupon.getText(), personas.getText());
        valorDescuento = (float) valor;
        if(valorDescuento == 0){
            mostrarMensaje(Alert.AlertType.INFORMATION, "El cupon ingresado no fue valido");
        }else{
            mostrarMensaje(Alert.AlertType.CONFIRMATION, "El cupon ingresado es correcto y se aplicara un descuento de: " + valorDescuento);
        }
    }
}
