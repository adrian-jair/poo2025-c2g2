package pe.edu.upeu.cafeteria.servicio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/producto.fxml"));
        primaryStage.setTitle("Cafetería - Gestión de Productos");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    }

    