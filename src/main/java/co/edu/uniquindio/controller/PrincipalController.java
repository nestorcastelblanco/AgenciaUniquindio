package co.edu.uniquindio.controller;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.utils.Propiedades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TextField nombreUsuario, correoUsuario, direccionUsuario, ciudadUsuario, telefonoUsuario, usuarioIngresado, contrasenaIngresada, id;
    @FXML
    private Label ingreso, usuario,contrasena;
    @FXML
    private Button botonIngreso, botonRegistro, bttCambiar,botonAdmin;
    private final Agencia agencia = Agencia.getInstance();
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
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
        try{
            agencia.ingresarCliente(usuarioIngresado.getText(),contrasenaIngresada.getText());
            agencia.loadStage("/portalAgencia.fxml",actionEvent,"Se ingresa al portal de la agencia" );
        }catch (CampoRepetido e)
        {
            LOGGER.log(Level.INFO, "Se ingresaron credenciales no validas");
        }
    }
}
