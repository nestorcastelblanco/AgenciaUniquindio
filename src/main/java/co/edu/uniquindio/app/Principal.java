package co.edu.uniquindio.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Principal extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader( Principal.class.getResource("/paginaVistaInicio.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("TRAVEL UNIQUINDIO");
        stage.show();
    }
    public static void main(String[] args) {
        launch(Principal.class, args );
    }
}
