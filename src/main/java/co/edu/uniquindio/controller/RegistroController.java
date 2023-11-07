package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.utils.Propiedades;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistroController implements Initializable, CambioIdiomaListener {
    @FXML
    private Label titulo, nombre, correo, direccion,identificacion, ciudad, telefono, usuario, contrasena;
    @FXML
    private TextField nombreUsuario, correoUsuario, direccionUsuario, ciudadUsuario, telefonoUsuario, usuarioIngresado, contrasenaIngresada, id;
    @FXML
    private Button botonRegreso, botonRegistro, bttCambiar;
    private final Agencia agencia = Agencia.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
    }
    private void cargarTextos() {
        titulo.setText(propiedades.getResourceBundle().getString("tituloRegistro"));
        nombre.setText(propiedades.getResourceBundle().getString("nombre"));
        correo.setText(propiedades.getResourceBundle().getString("correo"));
        direccion.setText(propiedades.getResourceBundle().getString("direccion"));
        ciudad.setText(propiedades.getResourceBundle().getString("ciudad"));
        telefono.setText(propiedades.getResourceBundle().getString("telefono"));
        usuario.setText(propiedades.getResourceBundle().getString("usuario"));
        contrasena.setText(propiedades.getResourceBundle().getString("contrasena"));
        identificacion.setText(propiedades.getResourceBundle().getString("identificacion"));
        LOGGER.log(Level.INFO,"Se cargaron los textos de la ventana de registro");
    }
    public void registrar(ActionEvent actionEvent) {
        try {
            agencia.registrarCliente(nombreUsuario.getText(), correoUsuario.getText(), direccionUsuario.getText(), ciudadUsuario.getText(), telefonoUsuario.getText(), usuarioIngresado.getText(), contrasenaIngresada.getText(), id.getText());
            LOGGER.log(Level.INFO, "Se registro un nuevo usuario");
            mostrarMensaje(Alert.AlertType.INFORMATION, "Se registro un nuevo usuario");
        } catch (CampoVacioException | CampoRepetido | CampoObligatorioException e) {
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }
    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaPrincipal.fxml", actionEvent,"Se ingreso a la pagina principal");
    }
}
