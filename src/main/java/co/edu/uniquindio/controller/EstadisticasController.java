package co.edu.uniquindio.controller;

import co.edu.uniquindio.utils.CambioIdiomaEvent;
import co.edu.uniquindio.utils.CambioIdiomaListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class EstadisticasController implements Initializable, CambioIdiomaListener {
    @FXML
    private BarChart<?,?> destinosReservados, destinosBuscados,mejoresGuias,paquetesReservados;
    @FXML
    private CategoryAxis destinosReservadosX,destinosBuscadosX,mejoresGuiasX,paquetesReservadosX;
    @FXML
    private NumberAxis destinosReservadosYdestinosBuscadosY,mejoresGuiasY,paquetesReservadosY;
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDestinosReservados();
        cargarDestinosBuscados();
        cargarMejoresGuias();
        cargarPaquetesReservados();
    }

    private void cargarDestinosReservados() {
        XYChart.Series setDestinosReservados = new XYChart.Series<>();
    }
    private void cargarDestinosBuscados() {
        XYChart.Series setDestinosBuscados = new XYChart.Series<>();
    }
    private void cargarMejoresGuias() {
        XYChart.Series setMejoresGuias = new XYChart.Series<>();
    }
    private void cargarPaquetesReservados() {
        XYChart.Series setPaquetesReservados = new XYChart.Series<>();
    }
}
