package co.edu.uniquindio.app;

import co.edu.uniquindio.model.Borrador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderRepeat;
import javafx.stage.Stage;

public class Principal extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader( Principal.class.getResource("/Ventanas/paginaPrincipal.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("");
        stage.show();
    }

    public static void main(String[] args) {
        Borrador.inicializarDatos();
        launch(Principal.class, args );
    }
}
