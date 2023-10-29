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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
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

    public void regresar (ActionEvent e) {
        agencia.loadStage("/paginaAdministrativa.fxml", e, "Se regresa a la pagina administrativa");
    }

    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registarGuia(ActionEvent actionEvent) {
        try
        {
            agencia.registrarGuia(nombre.getText(),experiencia.getText(),cedula.getText(),idiomas.getText());
            LOGGER.log(Level.INFO,"Se registro un nuevo guia al sistema");
        } catch (CampoRepetido |CampoObligatorioException|CampoVacioException e) {
            throw new RuntimeException(e);
        }
    }

}
