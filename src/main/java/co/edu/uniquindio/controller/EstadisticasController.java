package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.Agencia;
import co.edu.uniquindio.model.Destinos;
import co.edu.uniquindio.model.Guias;
import co.edu.uniquindio.model.Paquetes;
import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import co.edu.uniquindio.utils.Propiedades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EstadisticasController implements Initializable, CambioIdiomaListener {
    private final Agencia agencia = Agencia.getInstance();
    @FXML
    private BarChart<?,?> destinosReservados, destinosBuscados,mejoresGuias,paquetesReservados;
    @FXML
    private CategoryAxis destinosReservadosX,destinosBuscadosX,mejoresGuiasX,paquetesReservadosX;
    @FXML
    private NumberAxis destinosReservadosYdestinosBuscadosY,mejoresGuiasY,paquetesReservadosY;
    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        cargarTextos();
    }
    @FXML
    private Label txtEstadisticas;
    @FXML
    private Button botonRegreso;
    public void cargarTextos()
    {
        txtEstadisticas.setText(propiedades.getResourceBundle().getString("estadisticas"));
        botonRegreso.setText(propiedades.getResourceBundle().getString("bttVolver"));
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Propiedades.getInstance().addCambioIdiomaListener(this);
        cargarTextos();
        cargarDestinosReservados();
        cargarDestinosBuscados();
        cargarMejoresGuias();
        cargarPaquetesReservados();
    }

    private void cargarDestinosReservados() {
        XYChart.Series setDestinosReservados = new XYChart.Series<>();
        ArrayList<Destinos> destinos = agencia.enviarDestinos();
        for (int i =0; i<destinos.size();i++)
        {
            System.out.println(destinos.get(i).getNombre()+", "+destinos.get(i).getContReservas());
            setDestinosReservados.getData().add(new XYChart.Data<>(destinos.get(i).getNombre(),destinos.get(i).getContReservas()));
        }
        destinosReservados.getData().addAll(setDestinosReservados);
    }
    private void cargarDestinosBuscados() {
        XYChart.Series setDestinosBuscados = new XYChart.Series<>();

        for (int i =0; i<agencia.enviarDestinos().size();i++)
        {
            System.out.println("Buscados" + agencia.enviarDestinos().get(i).getNombre() + " cont: " + agencia.enviarDestinos().get(i).getContBusquedas());
            setDestinosBuscados.getData().add(new XYChart.Data<>(agencia.enviarDestinos().get(i).getNombre(),agencia.enviarDestinos().get(i).getContBusquedas()));
        }
        destinosBuscados.getData().addAll(setDestinosBuscados);
    }
    private void cargarMejoresGuias() {
        XYChart.Series setMejoresGuias = new XYChart.Series<>();
        ArrayList<Guias> guias = agencia.enviarGuias();
        for (int i =0; i<guias.size();i++)
        {

            setMejoresGuias.getData().add(new XYChart.Data<>(guias.get(i).getNombre(),agencia.promedioGuias(guias.get(i))));
        }
        mejoresGuias.getData().addAll(setMejoresGuias);
    }
    private void cargarPaquetesReservados() {
        XYChart.Series setPaquetesReservados = new XYChart.Series<>();
        ArrayList<Paquetes> paquetes = agencia.enviarPaquetes();
        for (int i =0; i<paquetes.size();i++)
        {
            setPaquetesReservados.getData().add(new XYChart.Data<>(paquetes.get(i).getNombre(),paquetes.get(i).getCantReservas()));
        }
        paquetesReservados.getData().addAll(setPaquetesReservados);
    }

    public void volver(ActionEvent actionEvent) {
        agencia.loadStage("/paginaAdministrativa.fxml", actionEvent, "Se vuelve a la pagina administrativa");
    }
}
