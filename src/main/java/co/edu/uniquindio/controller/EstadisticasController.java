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

    private void cargarDestinosReservadosRecursivo(ArrayList<Destinos> destinos, int index, XYChart.Series setDestinosReservados) {
        if (index < destinos.size()) {
            System.out.println(destinos.get(index).getNombre() + ", " + destinos.get(index).getContReservas());
            setDestinosReservados.getData().add(new XYChart.Data<>(destinos.get(index).getNombre(), destinos.get(index).getContReservas()));
            cargarDestinosReservadosRecursivo(destinos, index + 1, setDestinosReservados);
        }
    }
    private void cargarDestinosReservados() {
        XYChart.Series setDestinosReservados = new XYChart.Series<>();
        ArrayList<Destinos> destinos = agencia.enviarDestinos();
        cargarDestinosReservadosRecursivo(destinos, 0, setDestinosReservados);
        destinosReservados.getData().addAll(setDestinosReservados);
    }
    /*
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

     */
    private void cargarDestinosBuscadosRecursivo(ArrayList<Destinos> destinos, int index, XYChart.Series setDestinosBuscados) {
        if (index < destinos.size()) {
            System.out.println("Buscados" + destinos.get(index).getNombre() + " cont: " + destinos.get(index).getContBusquedas());
            setDestinosBuscados.getData().add(new XYChart.Data<>(destinos.get(index).getNombre(), destinos.get(index).getContBusquedas()));
            cargarDestinosBuscadosRecursivo(destinos, index + 1, setDestinosBuscados);
        }
    }

    private void cargarDestinosBuscados() {
        XYChart.Series setDestinosBuscados = new XYChart.Series<>();
        ArrayList<Destinos> destinos = agencia.enviarDestinos();
        cargarDestinosBuscadosRecursivo(destinos, 0, setDestinosBuscados);
        destinosBuscados.getData().addAll(setDestinosBuscados);
    }
    /*
    private void cargarDestinosBuscados() {
        XYChart.Series setDestinosBuscados = new XYChart.Series<>();

        for (int i =0; i<agencia.enviarDestinos().size();i++)
        {
            System.out.println("Buscados" + agencia.enviarDestinos().get(i).getNombre() + " cont: " + agencia.enviarDestinos().get(i).getContBusquedas());
            setDestinosBuscados.getData().add(new XYChart.Data<>(agencia.enviarDestinos().get(i).getNombre(),agencia.enviarDestinos().get(i).getContBusquedas()));
        }
        destinosBuscados.getData().addAll(setDestinosBuscados);
    }
     */
    private void cargarMejoresGuiasRecursivo(ArrayList<Guias> guias, int index, XYChart.Series setMejoresGuias) {
        if (index < guias.size()) {
            System.out.println(guias.get(index).getNombre());
            System.out.println(agencia.promedioGuias(guias.get(index)));
            if (Float.isNaN(agencia.promedioGuias(guias.get(index))))
            {
                setMejoresGuias.getData().add(new XYChart.Data<>(guias.get(index).getNombre(),0));
                cargarMejoresGuiasRecursivo(guias, index + 1, setMejoresGuias);
            }else {
                setMejoresGuias.getData().add(new XYChart.Data<>(guias.get(index).getNombre(), agencia.promedioGuias(guias.get(index))));
                cargarMejoresGuiasRecursivo(guias, index + 1, setMejoresGuias);
            }
        }
    }

    private void cargarMejoresGuias() {
        XYChart.Series setMejoresGuias = new XYChart.Series<>();
        ArrayList<Guias> guias = agencia.enviarGuias();
        cargarMejoresGuiasRecursivo(guias, 0, setMejoresGuias);
        mejoresGuias.getData().addAll(setMejoresGuias);
    }
    /*
    private void cargarMejoresGuias() {
        XYChart.Series setMejoresGuias = new XYChart.Series<>();
        ArrayList<Guias> guias = agencia.enviarGuias();
        for (int i =0; i<guias.size();i++)
        {

            setMejoresGuias.getData().add(new XYChart.Data<>(guias.get(i).getNombre(),agencia.promedioGuias(guias.get(i))));
        }
        mejoresGuias.getData().addAll(setMejoresGuias);
    }         */

    private void cargarPaquetesReservadosRecursivo(ArrayList<Paquetes> paquetes, int index, XYChart.Series setPaquetesReservados) {
        if (index < paquetes.size()) {
            setPaquetesReservados.getData().add(new XYChart.Data<>(paquetes.get(index).getNombre(), paquetes.get(index).getCantReservas()));
            cargarPaquetesReservadosRecursivo(paquetes, index + 1, setPaquetesReservados);
        }
    }

    private void cargarPaquetesReservados() {
        XYChart.Series setPaquetesReservados = new XYChart.Series<>();
        ArrayList<Paquetes> paquetes = agencia.enviarPaquetes();
        cargarPaquetesReservadosRecursivo(paquetes, 0, setPaquetesReservados);
        paquetesReservados.getData().addAll(setPaquetesReservados);
    }
    /*
    private void cargarPaquetesReservados() {
        XYChart.Series setPaquetesReservados = new XYChart.Series<>();
        ArrayList<Paquetes> paquetes = agencia.enviarPaquetes();
        for (int i =0; i<paquetes.size();i++)
        {
            setPaquetesReservados.getData().add(new XYChart.Data<>(paquetes.get(i).getNombre(),paquetes.get(i).getCantReservas()));
        }
        paquetesReservados.getData().addAll(setPaquetesReservados);
    }

     */

    public void volver(ActionEvent actionEvent) {
        agencia.loadStage("/paginaAdministrativa.fxml", actionEvent, "Se vuelve a la pagina administrativa");
    }
}
