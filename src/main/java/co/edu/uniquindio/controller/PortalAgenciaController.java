package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class PortalAgenciaController implements Initializable, CambioIdiomaListener {
        private final Agencia agencia = Agencia.getInstance();
        private final EdicionPerfilController edicionPerfilController = new EdicionPerfilController();
        private final Logger LOGGER = Logger.getLogger(PrincipalController.class.getName());
        @Override
        public void onCambioIdioma(CambioIdiomaEvent evento) {
        }
        @Override
        public void initialize(URL location, ResourceBundle resources) {
        }
        public void regresar(ActionEvent actionEvent) {
            agencia.loadStage("/paginaPrincipal.fxml", actionEvent, "Se regresa al login");
        }

    public void editarPerfil(ActionEvent actionEvent) {
            agencia.loadStage("/paginaEdicionPerfil.fxml", actionEvent,"Se ingresa al apartado de edicion de perfil");
    }

    public void seleccionPaquetes(ActionEvent actionEvent) {
            agencia.loadStage("/paginaClienteSeleccionDestino.fxml", actionEvent,"Se ingresa a la pagina de seleccion de paquete");
    }
}
