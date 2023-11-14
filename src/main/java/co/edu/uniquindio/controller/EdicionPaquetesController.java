package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Destinos;
import co.edu.uniquindio.model.Paquetes;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdicionPaquetesController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    private ArrayList<Destinos> destinosCargados = agencia.getDestinos();
    private ArrayList<Destinos> destinosActuales = new ArrayList<>();
    @FXML
    private ComboBox<Destinos> destinosSistema, destinosPaquete;
    @FXML
    private TextField nombrePaquete, servicios, cupos, valor;
    @FXML
    private DatePicker inicio, fin;
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Label txtPaquetes, txtNombre,txtDestino,txtDestinosActuales,txtInicio,txtFinal,txtServicios,txtCupos,txtValor;
    @FXML Button botonDestino,eliminarDestino, botonRegreso, botonEditar;
    public void cargarTextos()
    {
        txtPaquetes.setText(propiedades.getResourceBundle().getString("edicionPaquete"));
        txtNombre.setText(propiedades.getResourceBundle().getString("nombre"));
        txtDestino.setText(propiedades.getResourceBundle().getString("txtDestinos"));
        txtDestinosActuales.setText(propiedades.getResourceBundle().getString("destinosActuales"));
        txtInicio.setText(propiedades.getResourceBundle().getString("fechaI"));
        txtFinal.setText(propiedades.getResourceBundle().getString("fechaF"));
        txtServicios.setText(propiedades.getResourceBundle().getString("servi"));
        txtCupos.setText(propiedades.getResourceBundle().getString("adicionarCupos"));
        txtValor.setText(propiedades.getResourceBundle().getString("precioPersona"));
        botonDestino.setText(propiedades.getResourceBundle().getString("agregaDestino"));
        eliminarDestino.setText(propiedades.getResourceBundle().getString("eliminarDestino"));
        botonRegreso.setText(propiedades.getResourceBundle().getString("bttVolver"));
        botonEditar.setText(propiedades.getResourceBundle().getString("bttEditar"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
        destinosActuales = agencia.enviarPaqueteEdicion().getDestinos();
        nombrePaquete.setText(agencia.enviarPaqueteEdicion().getNombre());
        servicios.setText(agencia.enviarPaqueteEdicion().getServicios());
        cupos.setText(agencia.enviarPaqueteEdicion().getNumeroPersonas()+"");
        valor.setText(agencia.enviarPaqueteEdicion().getPrecio()+"");
        inicio.setValue(agencia.enviarPaqueteEdicion().getInicio());
        fin.setValue(agencia.enviarPaqueteEdicion().getFin());
        llenarListaDestinosSistema();
        llenarListaDestinosPaquete();
    }
    public void llenarListaDestinosSistema() {
        if(destinosCargados.isEmpty())
        {
            LOGGER.log(Level.INFO,"Se intento generar una lista de elementos vacios");
        }else
        {
            destinosCargados.stream().toList();
            ObservableList<Destinos> listaDestinosCargados = FXCollections.observableArrayList(destinosCargados);
            System.out.print("Lista combo " + listaDestinosCargados);
            destinosSistema.setItems(listaDestinosCargados);
            cargarAtributos();
        }
    }
    public void cargarAtributos()
    {
        destinosSistema.setCellFactory(new Callback<ListView<Destinos>, ListCell<Destinos>>() {
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
        destinosSistema.setButtonCell(new ListCell<Destinos>() {
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

    public void llenarListaDestinosPaquete() {
        if(destinosActuales.isEmpty())
        {
            LOGGER.log(Level.INFO,"Se intento generar una lista de elementos vacios");
        }else
        {
            destinosActuales.stream().toList();
            ObservableList<Destinos> listaDestinosPaquete = FXCollections.observableArrayList(destinosActuales);
            System.out.print("Lista combo " + listaDestinosPaquete);
            destinosPaquete.setItems(listaDestinosPaquete);
            cargarAtributosPaquete();
        }
    }
    public void cargarAtributosPaquete()
    {
        destinosPaquete.setCellFactory(new Callback<ListView<Destinos>, ListCell<Destinos>>() {
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
        destinosPaquete.setButtonCell(new ListCell<Destinos>() {
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
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaVistaPaquetes.fxml", actionEvent, "Se regresa a la vista de paquetes de administrador");
    }
    public void editarPaquete(ActionEvent actionEvent) {
        try
        {
            agencia.editarPaquetes(agencia.getPAQUETE_EDICION(),nombrePaquete.getText(), destinosActuales, inicio.getValue(), fin.getValue(), servicios.getText(), cupos.getText(), valor.getText());
            agencia.mostrarMensaje(Alert.AlertType.CONFIRMATION, "El paquete fue editado");
        }catch (CampoRepetido|CampoVacioException|CampoObligatorioException e)
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
    /*public void agregarDestino(ActionEvent actionEvent) {
        boolean state = true;
        if (destinosSistema.getSelectionModel().getSelectedIndex() == -1)
        {
            LOGGER.log(Level.INFO, "Se intento añadir un Destino sin seleccionarlo");
        }else {
            for(int i = 0; i< destinosActuales.size();i++)
            {
                System.out.println("Destinos Actuales: " + destinosActuales.get(i).getNombre());
                System.out.println("Destinos Seleccionado: " + destinosActuales.get(i).getNombre());
                if(destinosActuales.get(i).getNombre().equals(destinosSistema.getSelectionModel().getSelectedItem().getNombre()))
                {
                    state = false;
                    System.out.println("El paquete Actual ya cuenta con ese destino");
                }
            }
            if(state)
            {
                destinosActuales.add(destinosSistema.getSelectionModel().getSelectedItem());
                System.out.println("Se añadio el destino: " + destinosSistema.getSelectionModel().getSelectedItem().getNombre() + " a los destinos del paquete");
                llenarListaDestinosPaquete();
            }
        }
    }

     */
    public void agregarDestino(ActionEvent actionEvent) {
        if (destinosSistema.getSelectionModel().isEmpty()) {
            LOGGER.log(Level.INFO, "Se intentó añadir un destino sin seleccionarlo");
            agencia.mostrarMensaje(Alert.AlertType.ERROR, "No se ha seleccionado algun destino");
            return;
        }

        Destinos destinoSeleccionado = destinosSistema.getSelectionModel().getSelectedItem();

        if (destinosActuales.stream().anyMatch(destino -> destino.getNombre().equals(destinoSeleccionado.getNombre()))) {
            System.out.println("El paquete actual ya cuenta con ese destino");
        } else {
            destinosActuales.add(destinoSeleccionado);
            agencia.mostrarMensaje(Alert.AlertType.CONFIRMATION, "Se añadio el destino");
            System.out.println("Se añadió el destino: " + destinoSeleccionado.getNombre() + " a los destinos del paquete");
            llenarListaDestinosPaquete();
        }
    }

    public void eliminarDestino(ActionEvent actionEvent) {
        if (destinosPaquete.getSelectionModel().isEmpty()) {
            LOGGER.log(Level.INFO, "Se intentó eliminar un destino sin seleccionarlo");
            agencia.mostrarMensaje(Alert.AlertType.ERROR, "No se ha seleccionado un destino para eliminar");
            return;
        }

        Destinos destinoSeleccionado = destinosPaquete.getSelectionModel().getSelectedItem();

        if (destinosActuales.contains(destinoSeleccionado)) {
            destinosActuales.remove(destinoSeleccionado);
            llenarListaDestinosPaquete();
        } else {
            System.out.println("El paquete actual no cuenta con ese destino");
            LOGGER.log(Level.INFO, "El paquete actual no cuenta con ese destino");
        }
    }
    /*
    public void eliminarDestino(ActionEvent actionEvent) {
        if (destinosPaquete.getSelectionModel().getSelectedIndex() == -1)
        {
            LOGGER.log(Level.INFO, "Se intento eliminar un Destino sin seleccionarlo");
        }else {
            if(destinosActuales.contains(destinosPaquete.getSelectionModel().getSelectedItem()))
            {
                for (int i = 0 ; i<destinosActuales.size() ; i++)
                {
                    if (destinosActuales.get(i).getNombre().equals(destinosPaquete.getSelectionModel().getSelectedItem().getNombre()))
                    {
                        destinosActuales.remove(i);
                    }
                }
                llenarListaDestinosPaquete();
            }else {
                System.out.println("El paquete Actual ya cuenta con ese destino");
                LOGGER.log(Level.INFO, "El paquete Actual ya cuenta con ese destino");
            }
        }
    }

     */
}