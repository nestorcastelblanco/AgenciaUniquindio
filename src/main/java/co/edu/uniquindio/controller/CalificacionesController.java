package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.AgenciaCliente;
import co.edu.uniquindio.model.Destinos;
import co.edu.uniquindio.model.Guias;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import co.edu.uniquindio.utils.Propiedades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.net.URL;
import java.time.format.DecimalStyle;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalificacionesController implements Initializable, CambioIdiomaListener {
    private final AgenciaCliente agencia = AgenciaCliente.getInstance();
    private static final Logger LOGGER = Logger.getLogger(AgenciaCliente.class.getName());
    private ArrayList<Destinos> arrayListDestinos = agencia.reservaCalificacion();
    @FXML
    private ImageView estrellaDes1,estrellaDes2,estrellaDes3,estrellaDes4,estrellaDes5;
    @FXML
    private ImageView estrellaGuia1,estrellaGuia2,estrellaGuia3,estrellaGuia4,estrellaGuia5;
    @FXML
    private Button  bttDestino,bttDestino1,bttDestino2,bttDestino3,bttDestino4,bttDestino5, regresar, enviar;
    @FXML
    private Button bttGuia,bttGuia1,bttGuia2,bttGuia3,bttGuia4,bttGuia5;
    @FXML
    private ComboBox<Destinos> destinosCombo;
    @FXML
    private Label guiaLabel, txtDestinos, txtComentarios;
    @FXML
    private TextField comentario;
    private ArrayList<Integer> calificacionDestinos = new ArrayList<>();
    private int calificacionGuia;
    private boolean stateDestinos = false, stateGuia = false;
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    public void cargarTextos()
    {
        regresar.setText(propiedades.getResourceBundle().getString("bttVolver"));
        enviar.setText(propiedades.getResourceBundle().getString("enviar"));
        bttDestino.setText(propiedades.getResourceBundle().getString("calificarDestino"));
        bttGuia.setText(propiedades.getResourceBundle().getString("calificar"));
        regresar.setText(propiedades.getResourceBundle().getString("bttVolver"));
        txtDestinos.setText(propiedades.getResourceBundle().getString("txtDestinos"));
        txtComentarios.setText(propiedades.getResourceBundle().getString("txtComentarios"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
        Guias guia = agencia.guiaCalificacion();
        System.out.println("GUIA DE LA RESERVA" + guia);
        if(guia.equals("SIN GUIA") || guia == null || guia.isEmpty())
        {
            guiaLabel.setVisible(false);
            estrellaGuia1.setVisible(false);
            estrellaGuia2.setVisible(false);
            estrellaGuia3.setVisible(false);
            estrellaGuia4.setVisible(false);
            estrellaGuia5.setVisible(false);
            bttGuia.setVisible(false);
            bttGuia1.setVisible(false);
            bttGuia2.setVisible(false);
            bttGuia3.setVisible(false);
            bttGuia4.setVisible(false);
            bttGuia5.setVisible(false);
            stateGuia = true;
        }
        guiaLabel.setText(agencia.guiaCalificacion().getNombre());
        /*for(int i = 0 ; i<arrayListDestinos.size();i++)
        {
            calificacionDestinos.add(0);
        }

         */
        calificacion(0);
        llenarCombo();
    }
    public void calificacion (int ind)
    {
        if(ind<arrayListDestinos.size())
        {
            calificacionDestinos.add(0);
            calificacion(ind+1);
        }
    }
    public void llenarCombo() {
        if(arrayListDestinos.isEmpty())
        {
            LOGGER.log(Level.INFO,"Se intento generar una lista de elementos vacios");
        }else
        {
            arrayListDestinos.stream().toList();
            ObservableList<Destinos> listaDestinos = FXCollections.observableArrayList(arrayListDestinos);
            System.out.print("Lista combo" + listaDestinos);
            destinosCombo.setItems(listaDestinos);
            cargarAtributos();
        }
    }

    public void cargarAtributos()
    {
        destinosCombo.setCellFactory(new Callback<ListView<Destinos>, ListCell<Destinos>>() {
            @Override
            public ListCell<Destinos> call(ListView<Destinos> param) {
                return new ListCell<Destinos>() {
                    @Override
                    protected void updateItem(Destinos item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getNombre() + " " + item.getCiudad());
                        }
                    }
                };
            }
        });
        // Configurar la fábrica de celdas para mostrar el nombre y la descripción en el menú desplegable
        destinosCombo.setButtonCell(new ListCell<Destinos>() {
            @Override
            protected void updateItem(Destinos item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getNombre() + " - " + item.getDescripcion());
                }
            }
        });
    }
    public void dest1(ActionEvent actionEvent) {
        if(destinosCombo.getSelectionModel().getSelectedIndex() == -1)
        {
             LOGGER.log(Level.INFO, "No se ha seleccionado un destino");
        }else {
            Image imagen = new Image("Imagenes/estrella.jpg");
            Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
            estrellaDes1.setImage(imagen);
            estrellaDes2.setImage(imagenGris);
            estrellaDes3.setImage(imagenGris);
            estrellaDes4.setImage(imagenGris);
            estrellaDes5.setImage(imagenGris);
            calificacionDestinos.set(destinosCombo.getSelectionModel().getSelectedIndex(),1);
        }
    }
    public void dest2(ActionEvent actionEvent) {
        if(destinosCombo.getSelectionModel().getSelectedIndex() == -1)
        {
            LOGGER.log(Level.INFO, "No se ha seleccionado un destino");
        }else {
            Image imagen = new Image("Imagenes/estrella.jpg");
            Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
            estrellaDes1.setImage(imagen);
            estrellaDes2.setImage(imagen);
            estrellaDes3.setImage(imagenGris);
            estrellaDes4.setImage(imagenGris);
            estrellaDes5.setImage(imagenGris);
            calificacionDestinos.set(destinosCombo.getSelectionModel().getSelectedIndex(),2);
        }
    }
    public void dest3(ActionEvent actionEvent) {
        if(destinosCombo.getSelectionModel().getSelectedIndex() == -1)
        {
            LOGGER.log(Level.INFO, "No se ha seleccionado un destino");
        }else {
            Image imagen = new Image("Imagenes/estrella.jpg");
            Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
            estrellaDes1.setImage(imagen);
            estrellaDes2.setImage(imagen);
            estrellaDes3.setImage(imagen);
            estrellaDes4.setImage(imagenGris);
            estrellaDes5.setImage(imagenGris);
            calificacionDestinos.set(destinosCombo.getSelectionModel().getSelectedIndex(),3);
        }
    }
    public void dest4(ActionEvent actionEvent) {
        if(destinosCombo.getSelectionModel().getSelectedIndex() == -1)
        {
            LOGGER.log(Level.INFO, "No se ha seleccionado un destino");
        }else {
            Image imagen = new Image("Imagenes/estrella.jpg");
            Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
            estrellaDes1.setImage(imagen);
            estrellaDes2.setImage(imagen);
            estrellaDes3.setImage(imagen);
            estrellaDes4.setImage(imagen);
            estrellaDes5.setImage(imagenGris);
            calificacionDestinos.set(destinosCombo.getSelectionModel().getSelectedIndex(),4);
        }
    }
    public void dest5(ActionEvent actionEvent) {
        if(destinosCombo.getSelectionModel().getSelectedIndex() == -1)
        {
            LOGGER.log(Level.INFO, "No se ha seleccionado un destino");
        }else {
            Image imagen = new Image("Imagenes/estrella.jpg");
            estrellaDes1.setImage(imagen);
            estrellaDes2.setImage(imagen);
            estrellaDes3.setImage(imagen);
            estrellaDes4.setImage(imagen);
            estrellaDes5.setImage(imagen);
            calificacionDestinos.set(destinosCombo.getSelectionModel().getSelectedIndex(),5);
        }
    }
    public void calificarDestino(ActionEvent actionEvent) {
        if(destinosCombo.getSelectionModel().getSelectedIndex() == -1 )
        {
            LOGGER.log(Level.INFO, "No se ha seleccionado algun destino");
            mostrarMensaje(Alert.AlertType.ERROR, "No se ha seleccionado algun destino");
        }else {
            if(calificacionDestinos.get(destinosCombo.getSelectionModel().getSelectedIndex()) == 0)
            {
                LOGGER.log(Level.INFO, "No se ha calificado el destino aún");
                mostrarMensaje(Alert.AlertType.ERROR, "No se ha calificado el destino aún");
            }else {
                    if(calificacionDestinos.contains(0))
                    {
                        LOGGER.log(Level.INFO, "No se han calificado todos los destinos");
                        mostrarMensaje(Alert.AlertType.ERROR, "No se han calificado todos los destinos");
                    }else {
                        LOGGER.log(Level.INFO, "Todos los destinos calificados");
                        mostrarMensaje(Alert.AlertType.CONFIRMATION, "Todos los destinos calificados");
                        stateDestinos = true;
                    }
            }
        }
    }
    public void calificarGuia(ActionEvent actionEvent) {
        if(calificacionGuia == 0)
        {
            LOGGER.log(Level.INFO, "Ingrese una calificacion para el guia");
            mostrarMensaje(Alert.AlertType.ERROR, "Ingrese una calificacion para el guia");
            stateGuia = false;
        }else {
            LOGGER.log(Level.INFO, "La calificacion es correcta");
            mostrarMensaje(Alert.AlertType.CONFIRMATION, "La calificacion es correcta");
            stateGuia = true;
        }
    }
    public void guia1(ActionEvent actionEvent) {
            Image imagen = new Image("Imagenes/estrella.jpg");
            Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
            estrellaGuia1.setImage(imagen);
            estrellaGuia2.setImage(imagenGris);
            estrellaGuia3.setImage(imagenGris);
            estrellaGuia4.setImage(imagenGris);
            estrellaGuia5.setImage(imagenGris);
            calificacionGuia = 1;
    }
    public void guia2(ActionEvent actionEvent) {
        Image imagen = new Image("Imagenes/estrella.jpg");
        Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
        estrellaGuia1.setImage(imagen);
        estrellaGuia2.setImage(imagen);
        estrellaGuia3.setImage(imagenGris);
        estrellaGuia4.setImage(imagenGris);
        estrellaGuia5.setImage(imagenGris);
        calificacionGuia = 2;
    }
    public void guia3(ActionEvent actionEvent) {
        Image imagen = new Image("Imagenes/estrella.jpg");
        Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
        estrellaGuia1.setImage(imagen);
        estrellaGuia2.setImage(imagen);
        estrellaGuia3.setImage(imagen);
        estrellaGuia4.setImage(imagenGris);
        estrellaGuia5.setImage(imagenGris);
        calificacionGuia = 3;
    }
    public void guia4(ActionEvent actionEvent) {
        Image imagen = new Image("Imagenes/estrella.jpg");
        Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
        estrellaGuia1.setImage(imagen);
        estrellaGuia2.setImage(imagen);
        estrellaGuia3.setImage(imagen);
        estrellaGuia4.setImage(imagen);
        estrellaGuia5.setImage(imagenGris);
        calificacionGuia = 4;
    }
    public void guia5(ActionEvent actionEvent) {
        Image imagen = new Image("Imagenes/estrella.jpg");
        Image imagenGris = new Image("Imagenes/estrellaGris.jpg");
        estrellaGuia1.setImage(imagen);
        estrellaGuia2.setImage(imagen);
        estrellaGuia3.setImage(imagen);
        estrellaGuia4.setImage(imagen);
        estrellaGuia5.setImage(imagen);
        calificacionGuia = 5;
    }

    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaReservasCliente.fxml", actionEvent, "Se regresa a la vista de reservas");
    }
    public void enviarCalificaciones(ActionEvent actionEvent) {
        if(agencia.guiaCalificacion().equals("SIN GUIA") || agencia.guiaCalificacion() == null)
        {
            if (stateDestinos && !comentario.getText().isEmpty()){
                agencia.cargarCalificaciones(arrayListDestinos,calificacionDestinos, agencia.reservaCalificacionPaquete());
                mostrarMensaje(Alert.AlertType.CONFIRMATION, "La calificacion se ha completado correctamente");
            }else{
                mostrarMensaje(Alert.AlertType.WARNING, "La calificacion no se ha completado correctamente");
                LOGGER.log(Level.INFO, "La calificacion no se ha completado correctamente, complete los cambios necesarios");
            }
        }else{
            if (stateGuia && stateDestinos && !comentario.getText().isEmpty()){
                agencia.cargarCalificacionesCompleta(arrayListDestinos,calificacionDestinos,calificacionGuia, agencia.guiaCalificacion(), agencia.reservaCalificacionPaquete());
                mostrarMensaje(Alert.AlertType.CONFIRMATION, "La calificacion se ha completado correctamente");
            }else{
                mostrarMensaje(Alert.AlertType.WARNING, "La calificacion no se ha completado correctamente");
                LOGGER.log(Level.INFO, "La calificacion no se ha completado correctamente, complete los cambios necesarios");
            }
        }
    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}
