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
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdicionReservasController implements Initializable, CambioIdiomaListener {
    private final AgenciaCliente agencia = AgenciaCliente.getInstance();
    private static ArrayList<Guias> arrayGuias  = new ArrayList<>();
    private static ArrayList<Paquetes> arrayPaquetes  = new ArrayList<>();

    private static final Logger LOGGER = Logger.getLogger(AgenciaCliente.class.getName());
    @FXML
    private Button botonRegreso;
    @FXML
    private ComboBox<Guias> guias;
    @FXML
    private ComboBox<Paquetes> paquetes;
    @FXML
    private ComboBox<String> estado;
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFin;
    @FXML
    private TextField agregarPersonas, quitarPersonas;
    private ObservableList<String> estadosDisponibles = FXCollections.observableArrayList("PENDIENTE", "CONFIRMADA", "CANCELADA");
    public void regresar (ActionEvent e) {
        agencia.loadStage("/paginaAdministrativa.fxml", e, "Se regresa a la pagina administrativa");
    }
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Label txtReserva, txtEstado,txtPaquete,txtGuia,txtInicio,txtFinal,txtAñadirPersonas,txtQuitarPersonas,txtValor;
    @FXML
    private Button botonEditar;
    public void cargarTextos()
    {
        txtReserva.setText(propiedades.getResourceBundle().getString("edicionReserva"));
        txtEstado.setText(propiedades.getResourceBundle().getString("estado"));
        txtPaquete.setText(propiedades.getResourceBundle().getString("paquete"));
        txtGuia.setText(propiedades.getResourceBundle().getString("guia"));
        txtInicio.setText(propiedades.getResourceBundle().getString("fechaI"));
        txtFinal.setText(propiedades.getResourceBundle().getString("fechaF"));
        txtAñadirPersonas.setText(propiedades.getResourceBundle().getString("anadirPersonas"));
        txtQuitarPersonas.setText(propiedades.getResourceBundle().getString("quitarPersonas"));
        botonRegreso.setText(propiedades.getResourceBundle().getString("bttVolver"));
        botonEditar.setText(propiedades.getResourceBundle().getString("bttEditar"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
        arrayGuias = agencia.enviarGuias();
        arrayPaquetes = agencia.enviarPaquetes();
        llenarListaPaquetes();
        llenarListaGuias();
        llenarListaEstados();
        cargarVariables();
    }

    private void cargarVariables() {
        fechaInicio.setValue(agencia.enviarReservaEdicion().getFechaSolicitud());
        fechaFin.setValue(agencia.enviarReservaEdicion().getFechaPlanificada());
        paquetes.getSelectionModel().select(agencia.enviarReservaEdicion().getPaquete());
        guias.getSelectionModel().select(agencia.enviarReservaEdicion().getGuia());
        estado.getSelectionModel().select(agencia.enviarReservaEdicion().getEstado());
        agregarPersonas.setText("0");
        quitarPersonas.setText("0");
    }

    public void llenarListaPaquetes() {
        if(agencia.enviarPaquetes().isEmpty())
        {
            LOGGER.log(Level.INFO,"Se intento generar una lista de elementos vacios");
        }else
        {
            arrayPaquetes.stream().toList();
            ObservableList<Paquetes> listaDestinos = FXCollections.observableArrayList(arrayPaquetes);
            System.out.print("Lista comboDestinos" + listaDestinos);
            paquetes.setItems(listaDestinos);
            cargarAtributos();
        }
    }
    public void cargarAtributos()
    {
        paquetes.setCellFactory(new Callback<ListView<Paquetes>, ListCell<Paquetes>>() {
            @Override
            public ListCell<Paquetes> call(ListView<Paquetes> param) {
                return new ListCell<Paquetes>() {
                    @Override
                    protected void updateItem(Paquetes item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getNombre() + " " + item.getServicios());
                        }
                    }
                };
            }
        });
        // Configurar la fábrica de celdas para mostrar el nombre y la descripción en el menú desplegable
        paquetes.setButtonCell(new ListCell<Paquetes>() {
            @Override
            protected void updateItem(Paquetes item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getNombre() + " - " + item.getServicios());
                }
            }
        });
    }
    public void llenarListaGuias() {
        if(agencia.enviarGuias().isEmpty())
        {
            LOGGER.log(Level.INFO,"Se intento generar una lista de elementos vacios");
            mostrarMensaje(Alert.AlertType.ERROR, "Debe seleccionar un elemento");
        }else
        {
            arrayGuias.stream().toList();
            ObservableList<Guias> listaDestinos = FXCollections.observableArrayList(arrayGuias);
            System.out.print("Lista comboDestinos" + listaDestinos);
            guias.setItems(listaDestinos);
            cargarAtributosGuias();
        }
    }
    public void cargarAtributosGuias()
    {
        guias.setCellFactory(new Callback<ListView<Guias>, ListCell<Guias>>() {
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
        guias.setButtonCell(new ListCell<Guias>() {
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
    public void registrarPaquete(ActionEvent actionEvent) {
        String mensaje = agencia.editarReserva(paquetes.getSelectionModel().getSelectedItem(),fechaInicio.getValue(),fechaFin.getValue(),agregarPersonas.getText(),quitarPersonas.getText(),guias.getSelectionModel().getSelectedItem(),estado.getSelectionModel().getSelectedItem());
        if(mensaje.equals("La reserva se edito correctamente")){
            mostrarMensaje(Alert.AlertType.CONFIRMATION, "Se edito una reserva correctamente");
            LOGGER.log(Level.INFO,"Se edito una reserva");
        }else{
            mostrarMensaje(Alert.AlertType.CONFIRMATION, "No se logro editar la reserva");
        }
    }
    public void llenarListaEstados() {
        estado.setItems(estadosDisponibles);
    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}

