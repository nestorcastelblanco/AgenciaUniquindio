package co.edu.uniquindio.controller;

import co.edu.uniquindio.exceptions.CampoObligatorioException;
import co.edu.uniquindio.exceptions.CampoRepetido;
import co.edu.uniquindio.exceptions.CampoVacioException;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistroDestinoController implements Initializable, CambioIdiomaListener {
    private final AgenciaCliente agencia = AgenciaCliente.getInstance();
    private static final Logger LOGGER = Logger.getLogger(AgenciaCliente.class.getName());
    @FXML
    private Button botonRegreso,botonRegistro;
    @FXML
    private TextField nombre,ciudad,descripcion, imagenes,clima;
    @FXML
    private Button botonImagenes;
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Label txtNombre,txtDestinoReg,txtCiudad,txtDescripcion,txtClima,txtImagenes;
    public void cargarTextos()
    {
        txtNombre.setText(propiedades.getResourceBundle().getString("nombre"));
        txtDestinoReg.setText(propiedades.getResourceBundle().getString("registroDestino"));
        txtCiudad.setText(propiedades.getResourceBundle().getString("ciudad"));
        txtDescripcion.setText(propiedades.getResourceBundle().getString("descripcion"));
        txtClima.setText(propiedades.getResourceBundle().getString("clima"));
        txtImagenes.setText(propiedades.getResourceBundle().getString("imagenes"));
        botonRegreso.setText(propiedades.getResourceBundle().getString("bttVolver"));
        botonRegistro.setText(propiedades.getResourceBundle().getString("bttRegistro"));
        botonImagenes.setText(propiedades.getResourceBundle().getString("imagenes"));
    }
    ArrayList<String> imagePaths = new ArrayList<>();
    public void regresar (ActionEvent e) {
        agencia.loadStage("/paginaAdministrativa.fxml", e, "Se regresa a la pagina administrativa");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
    }
    public void registrarDestino(ActionEvent actionEvent) throws CampoRepetido,CampoVacioException,CampoObligatorioException {
        String mensaje = agencia.registrarDestino(nombre.getText(),ciudad.getText(),descripcion.getText(),imagePaths, clima.getText());
        if(mensaje.equals("El registro de destino fue exitoso")){
            LOGGER.log(Level.INFO,"Se registro un nuevo Destino al sistema");
            mostrarMensaje(Alert.AlertType.INFORMATION, "Se registro un nuevo destino");
            agencia.loadStage("/paginaCreacionDestino.fxml", actionEvent,"Se vuelve a cargar la pagina de registro");
        }else{
            mostrarMensaje(Alert.AlertType.WARNING, mensaje);
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
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        if(selectedFiles != null && !selectedFiles.isEmpty()){

        File[] imagenes = selectedFiles.toArray(new File[0]);

            // Obtener las direcciones absolutas y guardarlas en el ArrayList
            if (imagenes != null) {
                for (File file : imagenes) {
                    imagePaths.add(file.getAbsolutePath());
                }
            }
            System.out.println("Direcciones de las imágenes seleccionadas:");
            for (String path : imagePaths) {
                System.out.println(path);
            }

        }

    }
}
