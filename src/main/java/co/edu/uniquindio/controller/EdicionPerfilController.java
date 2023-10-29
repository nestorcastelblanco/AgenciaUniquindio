package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdicionPerfilController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    @FXML
    private TextField nombreUsuario, correoUsuario, direccionUsuario, id, ciudadUsuario, telefonoUsuario,usuarioIngresado,contrasenaIngresada;
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        establecerDatos();
        System.out.println();
    }
    public void establecerDatos()
    {
        nombreUsuario.setText(agencia.clienteSesion().getNombreCompleto());
        correoUsuario.setText(agencia.clienteSesion().getCorreo());
        direccionUsuario.setText(agencia.clienteSesion().getDireccion());
        id.setText(agencia.clienteSesion().getIdentificacion());
        ciudadUsuario.setText(agencia.clienteSesion().getCiudad());
        telefonoUsuario.setText(agencia.clienteSesion().getTelefono());
        usuarioIngresado.setText(agencia.clienteSesion().getUsuario());
        contrasenaIngresada.setText(agencia.clienteSesion().getContrasena());
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/portalAgencia.fxml", actionEvent,"Se regresa al portal de la agencia");
    }
    public void editar(ActionEvent actionEvent) {
        try{
            agencia.realizarEdicion(nombreUsuario.getText(),correoUsuario.getText(),direccionUsuario.getText(),id.getText(),ciudadUsuario.getText(),telefonoUsuario.getText(),usuarioIngresado.getText(),contrasenaIngresada.getText());
            LOGGER.log(Level.INFO, "Se realizo la edicion del perfil correctamente");
        } catch (CampoRepetido | CampoVacioException | CampoObligatorioException e) {
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
