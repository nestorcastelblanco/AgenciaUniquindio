package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Paquetes;
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

public class RegistroGuiaController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private static final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    @FXML
    private Button botonRegreso,botonRegistro;
    @FXML
    private TextField nombre,cedula,idiomas, experiencia;
    @FXML
    private ComboBox<Paquetes> comboPaquete;
    public void regresar (ActionEvent e) {
        agencia.loadStage("/paginaAdministrativa.fxml", e, "Se regresa a la pagina administrativa");
    }

    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        llenarCombo();
    }

    private void llenarCombo() {
        ArrayList<Paquetes> PaquetesCombo = agencia.enviarPaquetes();
        if(agencia.enviarPaquetes().isEmpty())
        {
            LOGGER.log(Level.INFO,"Se intento generar una lista de elementos vacios");
        }else
        {
            PaquetesCombo.stream().toList();
            ObservableList<Paquetes> listaPaquetes = FXCollections.observableArrayList(PaquetesCombo);
            System.out.print("Lista combo" + listaPaquetes);
            comboPaquete.setItems(listaPaquetes);
            cargarAtributos();
        }
    }
    public void cargarAtributos()
    {
        comboPaquete.setCellFactory(new Callback<ListView<Paquetes>, ListCell<Paquetes>>() {
            @Override
            public ListCell<Paquetes> call(ListView<Paquetes> param) {
                return new ListCell<Paquetes>() {
                    @Override
                    protected void updateItem(Paquetes item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getNombre());
                        }
                    }
                };
            }
        });

        // Configurar la fábrica de celdas para mostrar el nombre y la descripción en el menú desplegable
        comboPaquete.setButtonCell(new ListCell<Paquetes>() {
            @Override
            protected void updateItem(Paquetes item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getNombre());
                }
            }
        });
    }
    public void registarGuia(ActionEvent actionEvent) {
        try
        {
            if(comboPaquete.getSelectionModel().getSelectedIndex() != 1)
            {
                agencia.registrarGuia(nombre.getText(),experiencia.getText(),cedula.getText(),idiomas.getText(), comboPaquete.getSelectionModel().getSelectedItem());
                LOGGER.log(Level.INFO,"Se registro un nuevo guia al sistema");
            }else {
                LOGGER.log(Level.INFO, "No se han registrado los campos requeridos");
            }
        } catch (CampoRepetido |CampoObligatorioException|CampoVacioException e) {
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }
    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

}
