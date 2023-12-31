package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.AgenciaCliente;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import co.edu.uniquindio.utils.Propiedades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdicionDestinoController implements Initializable, CambioIdiomaListener {
    private final AgenciaCliente agencia = AgenciaCliente.getInstance();
    private static final Logger LOGGER = Logger.getLogger(AgenciaCliente.class.getName());
    @FXML
    private Button botonRegreso,botonEditar;
    @FXML
    private TextField nombre,ciudad,descripcion,clima;
    @FXML
    private Button botonImagenes;
    ArrayList<String> imagePaths = new ArrayList<>();
    public void regresar (ActionEvent e) {
        agencia.loadStage("/paginaAdministrativa.fxml", e, "Se regresa a la pagina administrativa");
    }
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Label txtNombre,txtDestino,txtCiudad,txtDescripcion,txtClima,txtImagenes;
    public void cargarTextos()
    {
        txtNombre.setText(propiedades.getResourceBundle().getString("nombre"));
        txtDestino.setText(propiedades.getResourceBundle().getString("establecerDestino"));
        txtCiudad.setText(propiedades.getResourceBundle().getString("ciudad"));
        txtDescripcion.setText(propiedades.getResourceBundle().getString("descripcion"));
        txtClima.setText(propiedades.getResourceBundle().getString("clima"));
        txtImagenes.setText(propiedades.getResourceBundle().getString("imagenes"));
        botonRegreso.setText(propiedades.getResourceBundle().getString("bttVolver"));
        botonEditar.setText(propiedades.getResourceBundle().getString("bttEditar"));
        botonImagenes.setText(propiedades.getResourceBundle().getString("cargarImagen"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
        nombre.setText(agencia.enviarDestinoEdicion().getNombre());
        ciudad.setText(agencia.enviarDestinoEdicion().getCiudad());
        descripcion.setText(agencia.enviarDestinoEdicion().getDescripcion());
        clima.setText(agencia.enviarDestinoEdicion().getClima());
        imagePaths = agencia.enviarDestinoEdicion().getImagenes();
        imagePaths.removeAll(imagePaths);
    }
    public void registrarDestino(ActionEvent actionEvent) {
        String mensaje = agencia.editarDestino(nombre.getText(),ciudad.getText(),descripcion.getText(),imagePaths, clima.getText());
        if(mensaje.equals("Se edito un destino correctamente")){
            LOGGER.log(Level.INFO,"Se edito un  Destino en el sistema");
            mostrarMensaje(Alert.AlertType.INFORMATION, "Se edito un Destino en el sistema");
            agencia.loadStage("/paginaEdicionDestino.fxml", actionEvent,"Se carga la pagina de edicion de destinos");
        }else{
            mostrarMensaje(Alert.AlertType.INFORMATION, mensaje);
        }

    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    public void cargarImagenes(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imágenes");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.gif", "*.bmp"));

        // Mostrar el cuadro de diálogo para seleccionar archivos
        File[] selectedFiles = fileChooser.showOpenMultipleDialog(stage).toArray(new File[0]);

        // Obtener las direcciones absolutas y guardarlas en el ArrayList
        if ((selectedFiles != null && selectedFiles.length > 0)){
            for (File file : selectedFiles) {
                imagePaths.add(file.getAbsolutePath());
            }
            System.out.println("Direcciones de las imágenes seleccionadas:");
            for (String path : imagePaths) {
                System.out.println(path);
            }
        }else{
            mostrarMensaje(Alert.AlertType.INFORMATION, "No se cargaron imagenes");
        }
    }


}
