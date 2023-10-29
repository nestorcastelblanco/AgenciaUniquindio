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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdicionPaquetesController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private static ArrayList<Destinos> destinosCombo = new ArrayList<>();
    private static ArrayList<Destinos> destinosComboActuales = new ArrayList<>();
    private static ArrayList<Destinos> destinosSeleccionados = new ArrayList<>();
    private static ArrayList<Destinos> destinosPrevios = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    @FXML
    private Button botonRegreso,botonRegistro, botonCrear, botonCrearDestino, botonDestino;
    @FXML
    private ComboBox<Destinos> destinos, destinosActuales;
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFin;
    @FXML
    private TextField nombre,servicios,personas, valor;
    private ArrayList<Destinos> destinosComparados = new ArrayList<>();
    public void regresar (ActionEvent e) {
        agencia.loadStage("/paginaAdministrativa.fxml", e, "Se regresa a la pagina administrativa");
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        destinosCombo = agencia.enviarDestinos();
        destinosSeleccionados = agencia.enviarPaqueteEdicion().getDestinos();
        evaluarDestinos();
        System.out.println("destCombosCargados: " +destinosCombo.toString());
        System.out.println("destSeleccionados: " +destinosSeleccionados.toString());
        destinosPrevios = agencia.enviarPaqueteEdicion().getDestinos();
        System.out.println("destPrevios: " +destinosPrevios.toString());
        cargarVariables();
        llenarListaDestinosActuales();
        llenarListaDestinos();
    }

    private void cargarVariables() {
        nombre.setText(agencia.enviarPaqueteEdicion().getNombre());
        servicios.setText(agencia.enviarPaqueteEdicion().getServicios());
        personas.setText(agencia.enviarPaqueteEdicion().getNumeroPersonas()+"");
        valor.setText(agencia.enviarPaqueteEdicion().getPrecio()+"");
        fechaInicio = new DatePicker(agencia.enviarPaqueteEdicion().getInicio());
        fechaFin = new DatePicker(agencia.enviarPaqueteEdicion().getFin());
    }

    public void llenarListaDestinos() {
        if(agencia.enviarDestinos().isEmpty())
        {
            LOGGER.log(Level.INFO,"Se intento generar una lista de elementos vacios");
        }else
        {
            destinosCombo.stream().toList();
            ObservableList<Destinos> listaDestinos = FXCollections.observableArrayList(destinosCombo);
            System.out.print("Lista comboDestinos" + listaDestinos);
            destinos.setItems(listaDestinos);
            cargarAtributos();
        }
    }
    public void evaluarDestinos()
    {
        //Contiene los destinos de la clase Agencia
        //destinosCombo
        //Contiene los destinos de el paquete a cambiar
        //destinosComboActuales
        ArrayList<Destinos> aux = new ArrayList<>();
        for(int i = 0 ; i<destinosCombo.size();i++)
        {
            for (int j = 0 ; j<destinosSeleccionados.size();j++)
            {
                if(destinosCombo.get(i).getNombre().equals(destinosSeleccionados.get(j).getNombre()))
                {
                    destinosCombo.set(i,destinosSeleccionados.get(j));
                }
            }
        }
    }
    public void llenarListaDestinosActuales() {
        destinosComboActuales = destinosPrevios;
        if(agencia.enviarPaqueteEdicion().getDestinos().isEmpty())
        {
            LOGGER.log(Level.INFO,"Se intento generar una lista de elementos vacios");
        }else
        {
            destinosComboActuales.stream().toList();
            ObservableList<Destinos> listaDestinos = FXCollections.observableArrayList(destinosComboActuales);
            System.out.println("Lista comboActuales" + listaDestinos);
            destinosActuales.setItems(listaDestinos);
            cargarAtributosActuales();
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
    public void cargarAtributosActuales()
    {
        destinosActuales.setCellFactory(new Callback<ListView<Destinos>, ListCell<Destinos>>() {
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
        destinosActuales.setButtonCell(new ListCell<Destinos>() {
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
            agencia.editarPaquete(nombre.getText(),destinosSeleccionados,fechaInicio.getValue(),fechaFin.getValue(),servicios.getText(),personas.getText(),valor.getText());
            LOGGER.log(Level.INFO,"Se edito un paquete");
            nombre.setText("");servicios.setText("");personas.setText("");valor.setText("");
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
                destinosPrevios = destinosSeleccionados;
                llenarListaDestinosActuales();
                LOGGER.log(Level.INFO, "Se establecio un nuevo destino  " + destinos.getSelectionModel().getSelectedItem().getNombre() + " " + destinos.getSelectionModel().getSelectedItem().getCiudad());
            }
        }
    }
    public void crearDestino(ActionEvent actionEvent) {
        agencia.loadStage("/paginaCreacionDestino.fxml", actionEvent, "Se ingresa a crear un Destino");
    }
    public void eliminarDestino(ActionEvent actionEvent) {
        if (destinosActuales.getSelectionModel().getSelectedIndex() == -1) {
            LOGGER.log(Level.INFO, "Se intento registrar un Destino sin haberlo seleccionado");
        } else {
            if (destinosPrevios.contains(destinosActuales.getSelectionModel().getSelectedItem())) {
                for (int i = 0; i<destinosPrevios.size();i++)
                {
                    if(destinosPrevios.get(i).equals(destinosActuales.getSelectionModel().getSelectedItem()))
                    {
                        destinosPrevios.remove(i);
                    }
                }
                destinosSeleccionados = destinosPrevios;
                LOGGER.log(Level.INFO, "El destino seleccionado ya fue eliminado del paquete");
                llenarListaDestinosActuales();
            }
        }
    }
}

