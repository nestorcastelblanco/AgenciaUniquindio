package co.edu.uniquindio.controller;
import co.edu.uniquindio.model.AgenciaCliente;
import co.edu.uniquindio.utils.Propiedades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
public class PrincipalController implements Initializable {
    @FXML
    private TextField codigo, usuarioIngresado, contrasenaIngresada;
    @FXML
    private Label ingreso, usuario,contrasena;
    @FXML
    private Button botonIngreso, botonCodigo, botonRegistro, bttCambiar,botonAdmin;
    private final AgenciaCliente agencia = AgenciaCliente.getInstance();
    private final Logger LOGGER = Logger.getLogger(AgenciaCliente.class.getName());
    private final Propiedades propiedades = Propiedades.getInstance();
    private boolean esIngles = false;
    @FXML
    private void cambiarIdioma(ActionEvent event) {
        if (esIngles) {
            Propiedades.getInstance().setLocale(new Locale("es", "ES"));
        } else {
            Propiedades.getInstance().setLocale(Locale.ENGLISH);
        }
        // Invierte el valor de esIngles para la próxima vez
        esIngles = !esIngles;
        ingreso.setText(propiedades.getResourceBundle().getString("ingreso"));
        usuario.setText(propiedades.getResourceBundle().getString("usuarioIngreso"));
        contrasena.setText(propiedades.getResourceBundle().getString("contrasenaIngreso"));
        botonAdmin.setText(propiedades.getResourceBundle().getString("bttAdmin"));
        bttCambiar.setText(propiedades.getResourceBundle().getString("bttIdioma"));
        botonIngreso.setText(propiedades.getResourceBundle().getString("bttIngreso"));
        botonRegistro.setText(propiedades.getResourceBundle().getString("bttRegistro"));
    }
    public void admin(ActionEvent actionEvent) {
        agencia.loadStage("/paginaIngresoAdmin.fxml",actionEvent,"Se ingreso a la pagina de ingreso administrativo");
        LOGGER.log(Level.INFO, "Se ingresa la pagina administrativa");
    }
    public void registrar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaRegistro.fxml", actionEvent ,"Se ingresa a la pestaña de registro");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        agencia.inicializarDatos();
    }
    public void ingresar(ActionEvent actionEvent) {
        if(agencia.ingresarCliente(usuarioIngresado.getText(),contrasenaIngresada.getText(), codigo.getText()).equals("El cliente ha sido verificado correctamente")) {
            agencia.loadStage("/portalAgencia.fxml", actionEvent, "Se ingresa al portal de la agencia");
            mostrarMensaje(Alert.AlertType.CONFIRMATION, "El codigo y credenciales fue validado correctamente");
        }else{
            mostrarMensaje(Alert.AlertType.WARNING,agencia.ingresarCliente(usuarioIngresado.getText(),contrasenaIngresada.getText(), codigo.getText()));
            LOGGER.log(Level.INFO, "Se ingresaron credenciales no validas");
        }
    }

    public void enviarCodigo(ActionEvent actionEvent) {
         if(agencia.enviarCodigo(usuarioIngresado.getText(),contrasenaIngresada.getText()).equals("Se ha enviado el codigo de verificacion")) {
             mostrarMensaje(Alert.AlertType.INFORMATION, "El codigo de verificacion ha sido enviado");
         }else{
            mostrarMensaje(Alert.AlertType.INFORMATION,agencia.enviarCodigo(usuarioIngresado.getText(),contrasenaIngresada.getText()) );
        }
    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}
