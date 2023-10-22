package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Destinos;
import co.edu.uniquindio.model.Guias;
import co.edu.uniquindio.model.Paquetes;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
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
    private final Agencia agencia = Agencia.getInstance();
    private static ArrayList<Guias> arrayGuias  = new ArrayList<>();
    private static ArrayList<Paquetes> arrayPaquetes  = new ArrayList<>();

    private static final Logger LOGGER = Logger.getLogger(PrincipalController.class.getName());
    @FXML
    private Button botonRegreso,botonRegistro, botonCrear, botonCrearDestino, botonDestino;
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
    private TextField personas;
    private ObservableList<String> estadosDisponibles = FXCollections.observableArrayList("PENDIENTE", "CONFIRMADA", "CANCELADA");
    public void regresar (ActionEvent e) {
        agencia.loadStage("/paginaAdministrativa.fxml", e, "Se regresa a la pagina administrativa");
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        arrayGuias = agencia.enviarGuias();
        arrayPaquetes = agencia.enviarPaquetes();
        cargarVariables();
        llenarListaPaquetes();
        llenarListaGuias();
        llenarListaEstados();
    }

    private void cargarVariables() {
        personas.setText(agencia.enviarReservaEdicion().getNumeroPersonas()+"");
        fechaInicio = new DatePicker(agencia.enviarReservaEdicion().getFechaSolicitud());
        fechaFin = new DatePicker(agencia.enviarReservaEdicion().getFechaPlanificada());
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
        try
        {
            agencia.editarReserva(paquetes.getSelectionModel().getSelectedItem(),fechaInicio.getValue(),fechaFin.getValue(),personas.getText(),guias.getSelectionModel().getSelectedItem(),estado.getSelectionModel().getSelectedItem());
            LOGGER.log(Level.INFO,"Se edito un paquete");
        } catch (CampoRepetido | CampoObligatorioException | CampoVacioException e) {
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
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

