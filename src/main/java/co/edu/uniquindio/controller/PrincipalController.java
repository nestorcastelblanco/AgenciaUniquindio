package co.edu.uniquindio.controller;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.model.Borrador;
import co.edu.uniquindio.model.Propiedades;
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
import java.util.logging.Logger;
public class PrincipalController implements Initializable {
    @FXML
    private TextField nombreUsuario, correoUsuario, direccionUsuario, ciudadUsuario, telefonoUsuario, usuarioIngresado, contrasenaIngresada, id;
    @FXML
    private Label ingreso, usuario,contrasena;
    @FXML
    private Button botonIngreso, botonRegistro, bttCambiar,botonAdministracion;
    private final Borrador borrador = Borrador.getInstance();
    private final Logger LOGGER = Logger.getLogger(PrincipalController.class.getName());
    private final Propiedades propiedades = Propiedades.getInstance();
    private boolean esIngles = false;
    @FXML
    private void cambiarIdioma(ActionEvent event) {
        // Cambia el idioma
        if (esIngles) {
            Propiedades.getInstance().setLocale(new Locale("es", "ES"));
        } else {
            Propiedades.getInstance().setLocale(Locale.ENGLISH);
        }
        // Invierte el valor de esIngles para la próxima vez
        esIngles = !esIngles;
        // Actualiza la interfaz de usuario
        cargarTextos();
    }
    public void registrar(ActionEvent actionEvent) {
        borrador.loadStage("/Ventanas/paginaRegistro.fxml", actionEvent ,"Se ingresa a la pestaña de registro");
    }
    public void cargarTextos(){
        ingreso.setText(propiedades.getResourceBundle().getString("ingreso"));
        usuario.setText(propiedades.getResourceBundle().getString("usuarioIngreso"));
        contrasena.setText(propiedades.getResourceBundle().getString("contrasenaIngreso"));
        botonAdministracion.setText(propiedades.getResourceBundle().getString("bttAdmin"));
        bttCambiar.setText(propiedades.getResourceBundle().getString("bttIdioma"));
        botonIngreso.setText(propiedades.getResourceBundle().getString("bttIngreso"));
        botonRegistro.setText(propiedades.getResourceBundle().getString("bttRegistro"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
