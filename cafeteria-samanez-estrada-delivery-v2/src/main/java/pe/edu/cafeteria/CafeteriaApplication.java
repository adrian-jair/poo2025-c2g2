package pe.edu.cafeteria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CafeteriaApplication extends Application {
    //anotaciones o decoradores


        @Override
        public void start(Stage stage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clientes.fxml"));
            Scene scene = new Scene(loader.load(), 820, 420);
            stage.setTitle("Cafetería Samanez y Estrada - Clientes");
            stage.setScene(scene);
            stage.show();
        }

        public void main(String[] args) {
            launch();
        }
    }
