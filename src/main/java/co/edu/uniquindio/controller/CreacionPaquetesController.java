package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Destinos;
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

public class CreacionPaquetesController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private static ArrayList<Destinos> destinosCombo = new ArrayList<>();
    private static ArrayList<Destinos> destinosSeleccionados = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    @FXML
    private Button botonRegreso,botonRegistro, botonCrear, botonCrearDestino, botonDestino;
    @FXML
    private ComboBox<Destinos> destinos;
    @FXML
    private DatePicker fechaInicio, fechaFin;
    @FXML
    private TextField nombre,servicios,personas, valor;
    public void regresar (ActionEvent e) {
        agencia.loadStage("/paginaAdministrativa.fxml", e, "Se regresa a la pagina administrativa");
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        llenarListaDestinos();
    }
    public void llenarListaDestinos() {
        destinosCombo = agencia.enviarDestinos();
        if(agencia.enviarDestinos().isEmpty())
        {
            LOGGER.log(Level.INFO,"Se intento generar una lista de elementos vacios");
        }else
        {
            destinosCombo.stream().toList();
            ObservableList<Destinos> listaDestinos = FXCollections.observableArrayList(destinosCombo);
            System.out.print("Lista combo" + listaDestinos);
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
            agencia.registrarPaquete(nombre.getText(),destinosSeleccionados,fechaInicio.getValue(),fechaFin.getValue(),servicios.getText(),personas.getText(),valor.getText());
            LOGGER.log(Level.INFO,"Se registro un nuevo Destino al sistema");
            nombre.setText("");destinosSeleccionados.clear();servicios.setText("");personas.setText("");valor.setText("");
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
        }else {
            if (destinosSeleccionados.contains(destinos.getSelectionModel().getSelectedItem()))
            {
                LOGGER.log(Level.INFO,"El destino seleccionado ya fue ingresado al paquete");
            }else {
                destinosSeleccionados.add(destinos.getSelectionModel().getSelectedItem());
                System.out.println("Arraylist de destinos seleccionados: " + destinosSeleccionados.toString());
                LOGGER.log(Level.INFO, "Se establecio un nuevo destino  " + destinos.getSelectionModel().getSelectedItem().getNombre() + " " + destinos.getSelectionModel().getSelectedItem().getCiudad());
            }
        }
    }

    public void crearDestino(ActionEvent actionEvent) {
        agencia.loadStage("/paginaCreacionDestino.fxml", actionEvent,"Se ingresa a crear un Destino");
    }
}
