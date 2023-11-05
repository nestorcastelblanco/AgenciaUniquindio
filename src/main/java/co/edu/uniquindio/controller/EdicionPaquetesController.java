package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Destinos;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdicionPaquetesController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    private ArrayList<Destinos> destinosCargados = agencia.getDestinos();
    private ArrayList<Destinos> destinosActuales = agencia.enviarPaqueteEdicion().getDestinos();
    @FXML
    private ComboBox<Destinos> destinosSistema, destinosPaquete;
    @FXML
    private TextField nombrePaquete, servicios, cupos, valor;
    @FXML
    private DatePicker inicio, fin;

    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    public void agregarDestino(ActionEvent actionEvent) {
        if (destinosSistema.getSelectionModel().getSelectedIndex() == -1)
        {
            LOGGER.log(Level.INFO, "Se intento añadir un Destino sin seleccionarlo");
        }else {
            for(int i = 0; i< destinosActuales.size();i++)
            {
                if(destinosActuales.get(i).getNombre().equals(destinosSistema.getSelectionModel().getSelectedItem().getNombre()))
                {
                    System.out.println("El paquete Actual ya cuenta con ese destino");
                }else {
                    destinosActuales.add(destinosSistema.getSelectionModel().getSelectedItem());
                    System.out.println("Se añadio el destino: " + destinosSistema.getSelectionModel().getSelectedItem().getNombre() + " a los destinos del paquete");
                    llenarListaDestinosPaquete();
                }
            }
        }
    }
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
}