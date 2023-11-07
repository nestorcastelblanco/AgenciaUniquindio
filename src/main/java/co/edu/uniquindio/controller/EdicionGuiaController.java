package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Paquetes;
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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdicionGuiaController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private static final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    @FXML
    private Button botonRegreso,botonEditar;
    @FXML
    private TextField nombre,cedula,idiomas, experiencia;
    @FXML
    private ComboBox<Paquetes> comboPaquete;
    public void regresar (ActionEvent e) {
        agencia.loadStage("/paginaAdministrativa.fxml", e, "Se regresa a la pagina administrativa");
    }
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Label txtGuia, txtNombre,txtCedula,txtIdiomas,txtExperiencia,txtPaqueteCubrir;
    public void cargarTextos()
    {
        txtGuia.setText(propiedades.getResourceBundle().getString("edicionGuia"));
        txtNombre.setText(propiedades.getResourceBundle().getString("nombre"));
        txtCedula.setText(propiedades.getResourceBundle().getString("identificacion"));
        txtIdiomas.setText(propiedades.getResourceBundle().getString("idioma"));
        txtExperiencia.setText(propiedades.getResourceBundle().getString("experiencia"));
        txtPaqueteCubrir.setText(propiedades.getResourceBundle().getString("paqueteCubrir"));
        botonRegreso.setText(propiedades.getResourceBundle().getString("bttVolver"));
        botonEditar.setText(propiedades.getResourceBundle().getString("bttEditar"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
        nombre.setText(agencia.recibirGuiaEdicion().getNombre());
        cedula.setText(agencia.recibirGuiaEdicion().getIdentificacion());
        idiomas.setText(agencia.obtenerIdiomas(agencia.recibirGuiaEdicion().getLenguajes()));
        experiencia.setText(agencia.recibirGuiaEdicion().getExp());
        llenarCombo();
    }

    private void llenarCombo() {
        ArrayList<Paquetes> PaquetesCombo = agencia.enviarPaquetes();
        System.out.println(PaquetesCombo.toString());
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
    public void editar(ActionEvent actionEvent) {
        try
        {
            System.out.println(comboPaquete.getSelectionModel().getSelectedIndex());
            if(comboPaquete.getSelectionModel().getSelectedIndex() != -1)
            {
                agencia.editarGuia(nombre.getText(),experiencia.getText(),cedula.getText(),idiomas.getText(), comboPaquete.getSelectionModel().getSelectedItem());
                LOGGER.log(Level.INFO,"Se registro un nuevo guia al sistema");
            }else {
                LOGGER.log(Level.INFO, "No se ha seleccionado un paquete");
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
