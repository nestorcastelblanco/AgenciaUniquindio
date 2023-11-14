package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoRepetido;
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

public class IngresoAdminController implements Initializable, CambioIdiomaListener {
    @FXML
    private TextField nombreUsuario, correoUsuario, direccionUsuario, ciudadUsuario, telefonoUsuario, usuarioIngresado, contrasenaIngresada, id;
    @FXML
    private Label ingreso, usuario,contrasena;
    @FXML
    private Button botonVolver, botonIngreso;
    private final Agencia agencia = Agencia.getInstance();
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    public void cargarTextos(){
        ingreso.setText(propiedades.getResourceBundle().getString("ingreso"));
        usuario.setText(propiedades.getResourceBundle().getString("usuarioIngreso"));
        contrasena.setText(propiedades.getResourceBundle().getString("contrasenaIngreso"));
        botonIngreso.setText(propiedades.getResourceBundle().getString("bttIngreso"));
        botonVolver.setText(propiedades.getResourceBundle().getString("bttVolver"));
    }
    public void admin(ActionEvent actionEvent) {
        try{
            agencia.ingresarAdmin(usuarioIngresado.getText(),contrasenaIngresada.getText());
            agencia.mostrarMensaje(Alert.AlertType.CONFIRMATION, "Credenciales administrativas validas");
            agencia.loadStage("/paginaAdministrativa.fxml",actionEvent,"Se ingresa al portal de administracion" );
        }catch (CampoRepetido e)
        {
            LOGGER.log(Level.INFO, "Se ingresaron credenciales no validas");
            agencia.mostrarMensaje(Alert.AlertType.ERROR, "Se ingresaron credenciales que no son validas");
        }
    }
    public void volver(ActionEvent actionEvent) {
        agencia.loadStage("/paginaPrincipal.fxml", actionEvent, "Se vuelve a la pagina principal");
    }
}
