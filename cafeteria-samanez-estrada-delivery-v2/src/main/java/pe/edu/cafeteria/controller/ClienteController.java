package pe.edu.cafeteria.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pe.edu.cafeteria.model.Cliente;
import pe.edu.cafeteria.service.ClienteService;

public class ClienteController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDni;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colDni;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colDireccion;

    private final ClienteService service = new ClienteService();
    private final ObservableList<Cliente> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colDni.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDni()));
        colTelefono.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTelefono()));
        colDireccion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDireccion()));
        tablaClientes.setItems(lista);
    }

    @FXML
    public void agregar() {
        if (txtNombre.getText().isBlank() || txtDni.getText().isBlank()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Nombre y DNI son obligatorios", ButtonType.OK);
            a.showAndWait();
            return;
        }
        Cliente c = new Cliente(txtNombre.getText(), txtDni.getText(), txtTelefono.getText(), txtDireccion.getText());
        service.agregar(c);
        lista.add(c);
        limpiar();
    }

    @FXML
    public void eliminar() {
        Cliente sel = tablaClientes.getSelectionModel().getSelectedItem();
        if (sel != null) {
            service.eliminar(sel);
            lista.remove(sel);
        }
    }

    @FXML
    public void editar() {
        int idx = tablaClientes.getSelectionModel().getSelectedIndex();
        if (idx >= 0) {
            if (txtNombre.getText().isBlank() || txtDni.getText().isBlank()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Nombre y DNI son obligatorios", ButtonType.OK);
                a.showAndWait();
                return;
            }
            Cliente c = new Cliente(txtNombre.getText(), txtDni.getText(), txtTelefono.getText(), txtDireccion.getText());
            service.editar(idx, c);
            lista.set(idx, c);
            limpiar();
        }
    }

    @FXML
    public void cargarSeleccion() {
        Cliente sel = tablaClientes.getSelectionModel().getSelectedItem();
        if (sel != null) {
            txtNombre.setText(sel.getNombre());
            txtDni.setText(sel.getDni());
            txtTelefono.setText(sel.getTelefono());
            txtDireccion.setText(sel.getDireccion());
        }
    }

    @FXML
    public void solicitarDelivery() {
        // Mostrar dialogo para ingresar direccion siempre
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Solicitar Delivery");
        dialog.setHeaderText(null);
        dialog.setContentText("Dirección:"); // sin signos de interrogación

        dialog.showAndWait().ifPresent(direccion -> {
            if (direccion.isBlank()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "La dirección no puede estar vacía", ButtonType.OK);
                a.showAndWait();
                return;
            }
            Cliente sel = tablaClientes.getSelectionModel().getSelectedItem();
            if (sel != null) {
                sel.setDireccion(direccion);
                int idx = tablaClientes.getSelectionModel().getSelectedIndex();
                lista.set(idx, sel);
                txtDireccion.setText(direccion);
            } else {
                // crear nuevo cliente si no hay seleccionado
                if (txtNombre.getText().isBlank() || txtDni.getText().isBlank()) {
                    Alert a = new Alert(Alert.AlertType.WARNING, "Para solicitar delivery sin seleccionar, ingresa Nombre y DNI.", ButtonType.OK);
                    a.showAndWait();
                    return;
                }
                Cliente c = new Cliente(txtNombre.getText(), txtDni.getText(), txtTelefono.getText(), direccion);
                service.agregar(c);
                lista.add(c);
                limpiar();
            }
            // Mostrar confirmación
            Alert confirm = new Alert(Alert.AlertType.INFORMATION, "Delivery confirmado", ButtonType.OK);
            confirm.setHeaderText(null);
            confirm.showAndWait();
        });
    }

    private void limpiar() {
        txtNombre.clear();
        txtDni.clear();
        txtTelefono.clear();
        txtDireccion.clear();
    }
}
