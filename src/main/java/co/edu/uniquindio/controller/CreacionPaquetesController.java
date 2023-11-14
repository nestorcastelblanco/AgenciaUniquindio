package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Destinos;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import co.edu.uniquindio.utils.Propiedades;
import com.sun.javafx.scene.shape.ArcHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreacionPaquetesController implements Initializable, CambioIdiomaListener {
    private static final Agencia agencia = Agencia.getInstance();
    ArrayList<Destinos> destinosCombo =   new ArrayList<>(agencia.enviarDestinos());
     ArrayList<Destinos> destinosSeleccionados = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    @FXML
    private Button botonRegreso,botonCrear, botonCrearDestino, botonDestino,botonAgregarCupon, botonCancelarCupon;
    @FXML
    private ComboBox<Destinos> destinos;
    @FXML
    private DatePicker fechaInicio, fechaFin, fechaCupon1, fechaCupon2;
    @FXML
    private TextField nombre,servicios,personas, valor, cupon, valorCupon;
    @FXML
    private Label txtValorCupon, txtCupon,txtRangoFechas;
    private boolean stateCupon;
    public void regresar (ActionEvent e) {
        agencia.loadStage("/paginaAdministrativa.fxml", e, "Se regresa a la pagina administrativa");
    }
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Label txtEstablecerPaquete, txtNombre,txtDestino,txtInicio, txtFinal, txtServicios,txtPersonas,txtValor;
    public void cargarTextos()
    {
        txtEstablecerPaquete.setText(propiedades.getResourceBundle().getString("establecerPaquete"));
        txtNombre.setText(propiedades.getResourceBundle().getString("nombre"));
        txtDestino.setText(propiedades.getResourceBundle().getString("txtDestinos"));
        txtValor.setText(propiedades.getResourceBundle().getString("precioPersona"));
        txtInicio.setText(propiedades.getResourceBundle().getString("fechaI"));
        txtFinal.setText(propiedades.getResourceBundle().getString("fechaF"));
        txtPersonas.setText(propiedades.getResourceBundle().getString("cupos"));
        txtServicios.setText(propiedades.getResourceBundle().getString("servi"));
        botonRegreso.setText(propiedades.getResourceBundle().getString("bttVolver"));
        botonCrear.setText(propiedades.getResourceBundle().getString("crear"));
        botonCrearDestino.setText(propiedades.getResourceBundle().getString("creaDestino"));
        botonDestino.setText(propiedades.getResourceBundle().getString("agregaDestino"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stateCupon = false;
        txtCupon.setVisible(false);
        txtValorCupon.setVisible(false);
        cupon.setVisible(false);
        valorCupon.setVisible(false);
        txtRangoFechas.setVisible(false);
        fechaCupon1.setVisible(false);
        fechaCupon2.setVisible(false);
        cupon.setText(null);
        valorCupon.setText(null);
        fechaCupon1.setValue(null);
        fechaCupon2.setValue(null);
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
        destinosCombo = agencia.enviarDestinos();
        llenarListaDestinos();
    }
    public void llenarListaDestinos() {
        if(agencia.enviarDestinos().isEmpty())
        {
            LOGGER.log(Level.INFO,"Se intento generar una lista de elementos vacios");
        }else
        {
            destinosCombo.stream().toList();
            ObservableList<Destinos> listaDestinos = FXCollections.observableArrayList(destinosCombo);
            System.out.print("Lista combo" + listaDestinos);
            ArrayList<Destinos> dest = new ArrayList<>(listaDestinos);
            destinos.setItems(listaDestinos);
            cargarAtributos();
        }
    }

    public void cargarAtributos()
    {
        destinos.setCellFactory(new Callback<ListView<Destinos>, ListCell<Destinos>>() {
            @Override
            public ListCell<Destinos> call(ListView<Destinos> param) {
                return new ListCell<Destinos>() {
                    @Override
                    protected void updateItem(Destinos item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getNombre() + " " + item.getCiudad());
                        }
                    }
                };
            }
        });

        // Configurar la fábrica de celdas para mostrar el nombre y la descripción en el menú desplegable
        destinos.setButtonCell(new ListCell<Destinos>() {
            @Override
            protected void updateItem(Destinos item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getNombre() + " - " + item.getDescripcion());
                }
            }
        });
    }

    public void registrarPaquete(ActionEvent actionEvent) {
        try
        {
            if (stateCupon)
            {
                agencia.registrarPaqueteCupon(nombre.getText(),destinosSeleccionados,fechaInicio.getValue(),fechaFin.getValue(),servicios.getText(),personas.getText(),valor.getText(), cupon.getText(),valorCupon.getText(), fechaCupon1.getValue(), fechaCupon2.getValue());
                agencia.mostrarMensaje(Alert.AlertType.CONFIRMATION, "Se registro un nuevo Paquete al sistema");
                LOGGER.log(Level.INFO,"Se registro un nuevo Paquete al sistema");
                agencia.loadStage("/paginaCreacionPaquete.fxml", actionEvent, "Se vuelve a cargar la pagina");
            }else{
                agencia.registrarPaquete(nombre.getText(),destinosSeleccionados,fechaInicio.getValue(),fechaFin.getValue(),servicios.getText(),personas.getText(),valor.getText());
                nombre.setText("");destinosSeleccionados.clear();servicios.setText("");personas.setText("");valor.setText("");
                agencia.mostrarMensaje(Alert.AlertType.CONFIRMATION, "Se registro un nuevo Paquete al sistema");
                LOGGER.log(Level.INFO,"Se registro un nuevo Paquete al sistema");
            }
        } catch (CampoRepetido | CampoObligatorioException | CampoVacioException e) {
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }
    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
    public void agregarDestino(ActionEvent actionEvent) {
        if(destinos.getSelectionModel().getSelectedIndex() == -1 )
        {
            LOGGER.log(Level.INFO, "Se intento registrar un Destino sin haberlo seleccionado");
            agencia.mostrarMensaje(Alert.AlertType.INFORMATION, "Se intento registrar un Destino sin haberlo seleccionado");
        }else {
            if (destinosSeleccionados.contains(destinos.getSelectionModel().getSelectedItem()))
            {
                LOGGER.log(Level.INFO,"El destino seleccionado ya fue ingresado al paquete");
                agencia.mostrarMensaje(Alert.AlertType.INFORMATION, "El destino seleccionado ya fue ingresado al paquete");
            }else {
                destinosSeleccionados.add(destinos.getSelectionModel().getSelectedItem());
                System.out.println("Arraylist de destinos seleccionados: " + destinosSeleccionados.toString());
                agencia.mostrarMensaje(Alert.AlertType.INFORMATION, "Se establecio un nuevo destino");
                LOGGER.log(Level.INFO, "Se establecio un nuevo destino  " + destinos.getSelectionModel().getSelectedItem().getNombre() + " " + destinos.getSelectionModel().getSelectedItem().getCiudad());
            }
        }
    }

    public void crearDestino(ActionEvent actionEvent) {
        agencia.loadStage("/paginaCreacionDestino.fxml", actionEvent,"Se ingresa a crear un Destino");
    }

    public void agregarCupon(ActionEvent actionEvent) {
        stateCupon = true;
        txtCupon.setVisible(true);
        txtValorCupon.setVisible(true);
        cupon.setVisible(true);
        valorCupon.setVisible(true);
        txtRangoFechas.setVisible(true);
        fechaCupon1.setVisible(true);
        fechaCupon2.setVisible(true);
    }

    public void cancelarCupon(ActionEvent actionEvent) {
        stateCupon = false;
        txtCupon.setVisible(false);
        txtValorCupon.setVisible(false);
        cupon.setVisible(false);
        valorCupon.setVisible(false);
        txtRangoFechas.setVisible(false);
        fechaCupon1.setVisible(false);
        fechaCupon2.setVisible(false);
        cupon.setText(null);
        valorCupon.setText(null);
        fechaCupon1.setValue(null);
        fechaCupon2.setValue(null);
    }
}
