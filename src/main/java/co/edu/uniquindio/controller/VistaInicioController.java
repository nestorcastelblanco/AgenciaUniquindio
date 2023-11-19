package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.AgenciaCliente;
import co.edu.uniquindio.model.Destinos;
import co.edu.uniquindio.model.Paquetes;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public
class VistaInicioController implements Initializable, CambioIdiomaListener {
    private final AgenciaCliente agencia = AgenciaCliente.getInstance();
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    @FXML
    private ImageView imagenPaquetes, imagenDestinos;
    @FXML
    private Label labelPaquete, labelDestinos;
    int numeroPaquetes, numeroDestinos, indicePaquete, indiceDestinos, indiceImagenPaquete, indiceImagenDestino, numeroImagenesPaquete, numeroImagenesDestino;
    ArrayList<Paquetes> paquetesCargados = new ArrayList<>();
    ArrayList<Destinos> destinosCargados = new ArrayList<>();
    ArrayList<String> imagenesPaquete = new ArrayList<>();
    ArrayList<String> imagenesDestino = new ArrayList<>();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
    }

    private void inicializarImagenesPaqueteRecursivo(int index, Paquetes paquete) {
        if (index < paquete.getDestinos().size()) {
            imagenesPaquete.addAll(paquete.getDestinos().get(index).getImagenes());
            inicializarImagenesPaqueteRecursivo(index + 1, paquete);
        }
    }
    private void inicializarImagenesDestinoRecursivo(int index, Destinos destinos) {
        if (index < destinos.getImagenes().size()) {
            imagenesDestino.addAll(destinos.getImagenes());
            inicializarImagenesDestinoRecursivo(index + 1, destinos);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        agencia.inicializarDatos();
        indicePaquete = 0;
        indiceDestinos = 0;
        indiceImagenDestino = 0;
        indiceImagenPaquete = 0;
        paquetesCargados = agencia.enviarPaquetes();
        destinosCargados = agencia.enviarDestinos();

        if (paquetesCargados.size() != 0 && destinosCargados.size() != 0 )
        {
            labelPaquete.setText(paquetesCargados.get(indicePaquete).getNombre());
            labelDestinos.setText(destinosCargados.get(indiceDestinos).getCiudad());

            inicializarImagenesPaqueteRecursivo(0, paquetesCargados.get(indicePaquete));
            inicializarImagenesDestinoRecursivo(0, destinosCargados.get(indiceDestinos));

            Image imagenPaquete = new Image(imagenesPaquete.get(0));
            Image imagenDestino = new Image(imagenesPaquete.get(0));

            imagenPaquetes.setImage(imagenPaquete);
            imagenDestinos.setImage(imagenDestino);

            numeroPaquetes = paquetesCargados.size();
            numeroDestinos = destinosCargados.size();
            numeroImagenesDestino = imagenesDestino.size();
            numeroImagenesPaquete = imagenesPaquete.size();
        }
    }

    public void ingresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaPrincipal.fxml", actionEvent, "Se carga la pagina de loggin");
    }

    public void lPaquetes(ActionEvent actionEvent) {
        indicePaquete-=1;
        indiceImagenPaquete = 0;
        if(indicePaquete == -1)
        {
            indicePaquete = numeroPaquetes-1;
            labelPaquete.setText(paquetesCargados.get(indicePaquete).getNombre());
            imagenesPaquete.removeAll(imagenesPaquete);
            inicializarImagenesPaqueteRecursivo(0, paquetesCargados.get(indicePaquete));
            numeroImagenesPaquete = imagenesPaquete.size();
            Image imagen = new Image(imagenesPaquete.get(indiceImagenPaquete));
            imagenPaquetes.setImage(imagen);
        }else {
            labelPaquete.setText(paquetesCargados.get(indicePaquete).getNombre());
            imagenesPaquete.removeAll(imagenesPaquete);
            inicializarImagenesPaqueteRecursivo(0, paquetesCargados.get(indicePaquete));
            numeroImagenesPaquete = imagenesPaquete.size();
            Image imagen = new Image(imagenesPaquete.get(indiceImagenPaquete));
            imagenPaquetes.setImage(imagen);
        }
    }

    public void rPaquetes(ActionEvent actionEvent) {
        indicePaquete+=1;
        indiceImagenPaquete = 0;
        if(indicePaquete == numeroPaquetes)
        {
            indicePaquete = 0;
            labelPaquete.setText(paquetesCargados.get(indicePaquete).getNombre());
            imagenesPaquete.removeAll(imagenesPaquete);
            inicializarImagenesPaqueteRecursivo(0, paquetesCargados.get(indicePaquete));
            numeroImagenesPaquete = paquetesCargados.size();
            Image imagen = new Image(imagenesPaquete.get(indiceImagenPaquete));
            imagenPaquetes.setImage(imagen);
        }else {
            labelPaquete.setText(paquetesCargados.get(indicePaquete).getNombre());
            imagenesPaquete.removeAll(imagenesPaquete);
            inicializarImagenesPaqueteRecursivo(0, paquetesCargados.get(indicePaquete));
            numeroImagenesPaquete = imagenesPaquete.size();
            Image imagen = new Image(imagenesPaquete.get(indiceImagenPaquete));
            imagenPaquetes.setImage(imagen);
        }
    }
    public void lImagenPaquetes(ActionEvent actionEvent) {
        indiceImagenPaquete-=1;
        if(indiceImagenPaquete == -1)
        {
            indiceImagenPaquete = numeroImagenesPaquete-1;
            Image imagen = new Image(imagenesPaquete.get(indiceImagenPaquete));
            imagenPaquetes.setImage(imagen);
        }else{
            Image imagen = new Image(imagenesPaquete.get(indiceImagenPaquete));
            imagenPaquetes.setImage(imagen);
        }
    }

    public void rImagenPaquetes(ActionEvent actionEvent) {
        indiceImagenPaquete+=1;
        if(indiceImagenPaquete == numeroImagenesPaquete)
        {
            indiceImagenPaquete = 0;
            Image imagen = new Image(imagenesPaquete.get(indiceImagenPaquete));
            imagenPaquetes.setImage(imagen);
        }else{
            Image imagen = new Image(imagenesPaquete.get(indiceImagenPaquete));
            imagenPaquetes.setImage(imagen);
        }
    }

    public void lDestinos(ActionEvent actionEvent) {
        indiceDestinos-=1;
        indiceImagenDestino = 0;
        if(indiceDestinos == -1)
        {
            indiceDestinos = numeroDestinos-1;
            labelDestinos.setText(destinosCargados.get(indiceDestinos).getNombre());
            imagenesDestino.removeAll(imagenesDestino);
            imagenesDestino.addAll(destinosCargados.get(indiceDestinos).getImagenes());

            numeroImagenesDestino = imagenesDestino.size();

            Image imagen = new Image(imagenesDestino.get(indiceImagenDestino));
            imagenDestinos.setImage(imagen);

        }else {

            labelDestinos.setText(destinosCargados.get(indiceDestinos).getNombre());

            imagenesDestino.removeAll(imagenesDestino);
            imagenesDestino.addAll(destinosCargados.get(indiceDestinos).getImagenes());

            numeroImagenesDestino = imagenesDestino.size();

            Image imagen = new Image(imagenesDestino.get(indiceImagenDestino));
            imagenDestinos.setImage(imagen);
        }
    }

    public void rDestinos(ActionEvent actionEvent) {
        indiceDestinos+=1;
        indiceImagenDestino = 0;
        if(indiceDestinos == numeroDestinos)
        {

            indiceDestinos = 0;
            labelDestinos.setText(destinosCargados.get(indiceDestinos).getNombre());
            imagenesDestino.removeAll(imagenesDestino);
            imagenesDestino.addAll(destinosCargados.get(indiceDestinos).getImagenes());

            numeroImagenesDestino = imagenesDestino.size();

            Image imagen = new Image(imagenesDestino.get(indiceImagenDestino));
            imagenDestinos.setImage(imagen);

        }else {

            labelDestinos.setText(destinosCargados.get(indiceDestinos).getNombre());

            imagenesDestino.removeAll(imagenesDestino);
            imagenesDestino.addAll(destinosCargados.get(indiceDestinos).getImagenes());

            numeroImagenesDestino = imagenesDestino.size();

            Image imagen = new Image(imagenesDestino.get(indiceImagenDestino));
            imagenDestinos.setImage(imagen);
        }
    }

    public void lImagenDestinos(ActionEvent actionEvent) {
        indiceImagenDestino-=1;
        if(indiceImagenDestino == -1)
        {
            indiceImagenDestino = numeroImagenesDestino-1;
            Image imagen = new Image(imagenesDestino.get(indiceImagenDestino));
            imagenDestinos.setImage(imagen);
        }else{
            Image imagen = new Image(imagenesDestino.get(indiceImagenDestino));
            imagenDestinos.setImage(imagen);
        }
    }

    public void rImagenDestinos(ActionEvent actionEvent) {
        indiceImagenDestino+=1;
        if(indiceImagenDestino == numeroImagenesDestino)
        {
            indiceImagenDestino = 0;
            Image imagen = new Image(imagenesDestino.get(indiceImagenDestino));
            imagenDestinos.setImage(imagen);
        }else{
            Image imagen = new Image(imagenesDestino.get(indiceImagenDestino));
            imagenDestinos.setImage(imagen);
        }
    }
}
