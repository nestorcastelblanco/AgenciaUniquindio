package co.edu.uniquindio.controller;
import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.AgenciaCliente;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import co.edu.uniquindio.utils.Propiedades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
public class MenuAdminController implements Initializable, CambioIdiomaListener {
    @FXML
    private Button botonRegreso,registrarGuia ,registrarDestinos, registrarPaquetes, mostrarGuias,mostrarDestinos,mostrarPaquetes,mostrarReservas,mostrarEstadisticas;
    private final Logger LOGGER = Logger.getLogger(Agencia.class.getName());
    private final Propiedades propiedades = Propiedades.getInstance();
    private final AgenciaCliente agencia = AgenciaCliente.getInstance();
    public void cargarTextos()
    {
        botonRegreso.setText(propiedades.getResourceBundle().getString("bttVolver"));
        registrarGuia.setText(propiedades.getResourceBundle().getString("registrarGuia"));
        registrarDestinos.setText(propiedades.getResourceBundle().getString("registrarDestino"));
        registrarPaquetes.setText(propiedades.getResourceBundle().getString("registrarPaquetes"));
        mostrarGuias.setText(propiedades.getResourceBundle().getString("mostrarGuias"));
        mostrarDestinos.setText(propiedades.getResourceBundle().getString("mostrarDestinos"));
        mostrarPaquetes.setText(propiedades.getResourceBundle().getString("mostrarPaquetes"));
        mostrarReservas.setText(propiedades.getResourceBundle().getString("mostrarReservas"));
        mostrarEstadisticas.setText(propiedades.getResourceBundle().getString("mostrarEstadisticas"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
    public void regresar(ActionEvent actionEvent) {
        agencia.loadStage("/paginaPrincipal.fxml", actionEvent,"Se ingreso a la pagina principal");
    }
    public void registrarGuia(ActionEvent actionEvent) {
        agencia.loadStage("/paginaRegistroGuias.fxml",actionEvent,"Se ingreso a la pagina de registro de Guia");
    }
    public void registrarDestino(ActionEvent actionEvent) {
        agencia.loadStage("/paginaCreacionDestino.fxml",actionEvent,"Se ingreso a la pagina de creacion de Destino");
    }
    public void creacionPaquetes(ActionEvent actionEvent) {
        agencia.loadStage("/paginaCreacionPaquete.fxml", actionEvent,"Se ingreso a la pagina de creacion de Paquetes");
    }

    public void mostrarPaquetes(ActionEvent actionEvent) {
        agencia.loadStage("/paginaVistaPaquetes.fxml", actionEvent, "Se ingresa a la pagina de vista de paquetes");
    }

    public void reservas(ActionEvent actionEvent) {
        agencia.loadStage("/paginaVistaReservas.fxml", actionEvent, "Se ingresa a la pagina de vista de reservas");
    }

    public void estadisticas(ActionEvent actionEvent) {
        agencia.loadStage("/paginaEstadisticas.fxml", actionEvent, "Se ingresa a la pagina de estadisticas");
    }

    public void mostrarGuias(ActionEvent actionEvent) {
        agencia.loadStage("/paginaGuias.fxml", actionEvent, "Se ingreso a la pagina de guias");
    }

    public void mostrarDestinos(ActionEvent actionEvent) {
        agencia.loadStage("/paginaVistaDestinos.fxml",actionEvent,"Se ingreso a la pagina de Destinos");
    }
}
