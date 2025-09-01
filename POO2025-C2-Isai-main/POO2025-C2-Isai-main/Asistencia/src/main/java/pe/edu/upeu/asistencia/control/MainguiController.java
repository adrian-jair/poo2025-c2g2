package pe.edu.upeu.asistencia.control;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.repositorio.ParticipanteRepositorio;

import javax.swing.plaf.MenuBarUI;

@Controller
public class MainguiController {

    @FXML
    private BorderPane bp;

    @FXML
    MenuBar menuBar ;
    @FXML
    TabPane tabPane;


}
