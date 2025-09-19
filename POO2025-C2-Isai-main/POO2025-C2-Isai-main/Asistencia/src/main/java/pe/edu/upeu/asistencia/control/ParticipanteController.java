package pe.edu.upeu.asistencia.control;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.enums.Carrera;
import pe.edu.upeu.asistencia.enums.TipoParticipante;
import pe.edu.upeu.asistencia.modelo.Participante;
import pe.edu.upeu.asistencia.servicio.ParticipanteServicioI;

@Controller
public class ParticipanteController {

    @FXML
    private TextField txtNombres, txtDni, txtApellidos;
    @FXML
    private ComboBox<Carrera> cbxCarrera;
    @FXML
    private ComboBox<TipoParticipante> cbxTipoParticipante;
    @FXML
    private TableView<Participante> tableView;
    ObservableList<Participante> listaParticipante;
    @FXML
    private TableColumn<Participante, String> dniColum, nombreColum, apellidoColum, carreraColum, tipoPartColum;

    @Autowired
    ParticipanteServicioI ps;

    @FXML
    public void initialize() {
        cbxCarrera.getItems().setAll(Carrera.values());
        cbxTipoParticipante.getItems().setAll(TipoParticipante.values());
        definirColumnas();
        ListaParticipantes();

    }

    public void ListaFormulario() {
        txtNombres.setText(" ");
        txtDni.setText("");
        txtApellidos.setText("");
        cbxCarrera.setValue(null);
        cbxTipoParticipante.setValue(null);


    }

    @FXML
    public void registroParticipante() {
        Participante p = new Participante();
        p.setDni(new SimpleStringProperty(txtDni.getText()));
        p.setNombre(new SimpleStringProperty(txtNombres.getText()));
        p.setApellido(new SimpleStringProperty(txtApellidos.getText()));
        p.setCarrera(cbxCarrera.getSelectionModel().getSelectedItem());
        p.setTipoParticipante(cbxTipoParticipante.getSelectionModel().getSelectedItem());
        ps.save(p);
        ListaParticipantes();


    }

    public void definirColumnas() {
        dniColum = new TableColumn<>("DNI");
        nombreColum = new TableColumn<>("Nombre");
        apellidoColum = new TableColumn<>("Apellidos");
        carreraColum = new TableColumn<>("Carrera");
        tipoPartColum = new TableColumn<>("Tipo Participante");
        tableView.getColumns().addAll(dniColum, nombreColum, apellidoColum, carreraColum, tipoPartColum);

    }

    public void ListaParticipantes() {
        dniColum.setCellValueFactory(cellData -> cellData.getValue().getDni());
        nombreColum.setCellValueFactory(cellData -> cellData.getValue().getNombre());
        apellidoColum.setCellValueFactory(cellData -> cellData.getValue().getApellido());
        carreraColum.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCarrera().toString()));
        listaParticipante = FXCollections.observableArrayList(ps.findAll());
        tableView.setItems(listaParticipante);

    }

}
