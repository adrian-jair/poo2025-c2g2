package pe.edu.upeu.sysventas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pe.edu.upeu.sysventas.modelo.Venta;
import pe.edu.upeu.sysventas.servicio.ServicioVenta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistorialVentas {

    private static final DecimalFormat PRICE_FMT = new DecimalFormat("0.00");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Font MONO = Font.font("Consolas", 12);

    /**
     * Muestra un modal con el historial de ventas y herramientas de interacción.
     */
    public static void mostrarModal(Stage owner, ServicioVenta servicioVenta, String usuario) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Historial de Ventas");

        // Obtener ventas (todo o por usuario)
        List<Venta> ventasRaw;
        if (usuario == null || usuario.isEmpty()) ventasRaw = servicioVenta.todos();
        else ventasRaw = servicioVenta.porUsuario(usuario);

        ObservableList<Venta> ventas = FXCollections.observableArrayList(ventasRaw);
        FilteredList<Venta> filtered = new FilteredList<>(ventas, p -> true);

        // Search field
        TextField search = new TextField();
        search.setPromptText("Buscar por ID / Vendedor / Cliente...");
        search.setMaxWidth(Double.MAX_VALUE);

        // ListView con celdas ricas
        ListView<Venta> lv = new ListView<>(filtered);
        lv.setPrefWidth(420);
        lv.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Venta v, boolean empty) {
                super.updateItem(v, empty);
                if (empty || v == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    String cliente = getClienteNombre(v);
                    if ((cliente == null || cliente.equals("-"))) {
                        Map<String, String> datos = readClienteFileForVenta(v.getId());
                        if (datos.containsKey("nombre")) cliente = datos.get("nombre");
                    }
                    String fecha = v.getFechaHora() != null ? DATE_FMT.format(v.getFechaHora()) : "-";
                    String id = nullToDash(v.getId());
                    String vendedor = nullToDash(v.getUsername());
                    String total = "S/" + PRICE_FMT.format(v != null ? v.getTotal() : 0.0);

                    Label lblId = new Label("#" + id);
                    lblId.setStyle("-fx-font-weight:bold; -fx-text-fill: white;");
                    Label lblMeta = new Label(String.format("  %s | Cliente: %s", vendedor, nullToDash(cliente)));
                    lblMeta.setStyle("-fx-text-fill: #dbefff;");
                    Label lblDate = new Label(fecha);
                    lblDate.setStyle("-fx-text-fill: #bcdcff; -fx-font-size:11px;");
                    Label lblTotal = new Label(total);
                    lblTotal.setStyle("-fx-font-weight:bold; -fx-text-fill: #00e5ff;");

                    HBox right = new HBox(lblDate, new Region(), lblTotal);
                    HBox.setHgrow(right.getChildren().get(1), Priority.ALWAYS);

                    HBox row = new HBox(8, lblId, lblMeta);
                    row.setAlignment(Pos.CENTER_LEFT);
                    VBox vbox = new VBox(3, row, right);
                    vbox.setPadding(new Insets(6));
                    vbox.setStyle("-fx-background-radius:8; -fx-border-radius:8; -fx-border-color: rgba(255,255,255,0.04);");

                    // Estilos condicionales: ventas grandes resaltan, ventas con total 0 en rojo
                    if (v.getTotal() >= 100) {
                        vbox.setStyle(vbox.getStyle() + "-fx-background-color: linear-gradient(to right, rgba(0,229,255,0.04), rgba(0,150,200,0.02));");
                    } else if (v.getTotal() <= 0.01) {
                        vbox.setStyle(vbox.getStyle() + "-fx-background-color: rgba(255,60,60,0.04);");
                    }

                    setGraphic(vbox);
                }
            }
        });

        // Panel de vista previa / comprobante
        TextArea preview = new TextArea();
        preview.setEditable(false);
        preview.setWrapText(true);
        preview.setFont(MONO);
        preview.setStyle("-fx-control-inner-background: #041428; -fx-text-fill: #E6F7FF; -fx-border-radius:8; -fx-background-radius:8;");
        preview.setPrefWidth(480);

        // botones
        Button btnVer = new Button("Ver comprobante");
        Button btnExport = new Button("Exportar (.txt)");
        Button btnPrint = new Button("Imprimir");
        Button btnCopy = new Button("Copiar");
        Button btnCerrar = new Button("Cerrar");

        HBox botones = new HBox(8, btnVer, btnExport, btnPrint, btnCopy, btnCerrar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        // Layout principal: izquierda lista + derecha vista previa
        VBox left = new VBox(8, search, lv);
        left.setPadding(new Insets(10));
        left.setPrefWidth(440);

        VBox right = new VBox(8, new Label("Vista previa:"), preview, botones);
        right.setPadding(new Insets(10));
        right.setPrefWidth(520);

        HBox root = new HBox(12, left, right);
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #00121a, #001f2b);");

        Scene sc = new Scene(root, 980, 540);
        stage.setScene(sc);

        // Filtrado dinámico por texto
        search.textProperty().addListener((obs, ov, nv) -> {
            String q = nv == null ? "" : nv.trim().toLowerCase();
            filtered.setPredicate(v -> {
                if (q.isEmpty()) return true;
                if (v == null) return false;
                String id = v.getId() == null ? "" : v.getId().toLowerCase();
                String user = v.getUsername() == null ? "" : v.getUsername().toLowerCase();
                String cliente = getClienteNombre(v);
                if ((cliente == null || cliente.equals("-"))) {
                    Map<String, String> datos = readClienteFileForVenta(v.getId());
                    cliente = datos.getOrDefault("nombre", "-");
                }
                cliente = cliente == null ? "" : cliente.toLowerCase();
                return id.contains(q) || user.contains(q) || cliente.contains(q);
            });
        });

        // actualizar vista previa al seleccionar
        lv.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            if (nv == null) {
                preview.clear();
            } else {
                preview.setText(generateComprobanteText(nv));
            }
        });

        // Acciones botones
        btnVer.setOnAction(e -> {
            Venta sel = lv.getSelectionModel().getSelectedItem();
            if (sel == null) {
                alertWarning("Seleccione una venta para ver su comprobante.");
                return;
            }
            mostrarComprobante(stage, sel); // modal detallado
            // actualiza preview al volver en caso se haya modificado algo (no debería)
            preview.setText(generateComprobanteText(sel));
        });

        btnExport.setOnAction(e -> {
            Venta sel = lv.getSelectionModel().getSelectedItem();
            if (sel == null) { alertWarning("Seleccione una venta para exportar."); return; }
            FileChooser fc = new FileChooser();
            fc.setTitle("Guardar comprobante");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto (.txt)", "*.txt"));
            fc.setInitialFileName("comprobante_" + nullToDash(sel.getId()) + ".txt");
            java.io.File file = fc.showSaveDialog(stage);
            if (file != null) {
                try {
                    Files.writeString(file.toPath(), generateComprobanteText(sel), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    alertInfo("Comprobante exportado a:\n" + file.getAbsolutePath());
                } catch (IOException ex) {
                    alertError("No se pudo guardar el comprobante: " + ex.getMessage());
                }
            }
        });

        btnCopy.setOnAction(e -> {
            Venta sel = lv.getSelectionModel().getSelectedItem();
            if (sel == null) { alertWarning("Seleccione una venta para copiar."); return; }
            ClipboardContent content = new ClipboardContent();
            content.putString(generateComprobanteText(sel));
            Clipboard.getSystemClipboard().setContent(content);
            alertInfo("Comprobante copiado al portapapeles.");
        });

        btnPrint.setOnAction(e -> {
            Venta sel = lv.getSelectionModel().getSelectedItem();
            if (sel == null) { alertWarning("Seleccione una venta para imprimir."); return; }
            // generar una TextArea temporal para imprimir (mantiene el estilo de monospace)
            TextArea toPrint = new TextArea(generateComprobanteText(sel));
            toPrint.setFont(MONO);
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                boolean proceed = job.showPrintDialog(stage);
                if (proceed) {
                    boolean printed = job.printPage(toPrint);
                    if (printed) job.endJob();
                    else alertError("Error al imprimir la página.");
                }
            } else {
                alertError("No se encontró ninguna impresora disponible.");
            }
        });

        btnCerrar.setOnAction(e -> stage.close());

        // Mostrar modal
        stage.showAndWait();
    }

    /**
     * Muestra el comprobante en un modal detallado (como antes, pero con mejoras).
     */
    public static void mostrarComprobante(Stage owner, Venta v) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Comprobante - " + nullToDash(v != null ? v.getId() : null));

        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.setFont(MONO);
        ta.setStyle("-fx-control-inner-background: #00111a; -fx-text-fill: #E6F7FF;");
        ta.setText(generateComprobanteText(v));

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> stage.close());

        Button btnExport = new Button("Exportar (.txt)");
        btnExport.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Guardar comprobante");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto (.txt)", "*.txt"));
            fc.setInitialFileName("comprobante_" + nullToDash(v.getId()) + ".txt");
            java.io.File file = fc.showSaveDialog(stage);
            if (file != null) {
                try {
                    Files.writeString(file.toPath(), ta.getText(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    alertInfo("Comprobante exportado a:\n" + file.getAbsolutePath());
                } catch (IOException ex) {
                    alertError("No se pudo guardar el comprobante: " + ex.getMessage());
                }
            }
        });

        HBox hBtns = new HBox(8, btnExport, btnCerrar);
        hBtns.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(10, ta, hBtns);
        vbox.setPadding(new Insets(10));
        Scene sc = new Scene(vbox, 560, 520);
        stage.setScene(sc);
        stage.showAndWait();
    }

    /**
     * Genera el texto del comprobante (cadena) de forma consistente.
     */
    private static String generateComprobanteText(Venta v) {
        StringBuilder sb = new StringBuilder();

        Map<String, String> datosArchivo = readClienteFileForVenta(v != null ? v.getId() : null);
        sb.append("====================================\n");
        sb.append("            COMPROBANTE\n");
        sb.append("====================================\n");
        sb.append("ID: ").append(nullToDash(v != null ? v.getId() : null)).append("\n");
        sb.append("Vendedor: ").append(nullToDash(v != null ? v.getUsername() : null)).append("\n");

        String nombreDesdeVenta = getClienteNombre(v);
        String dniDesdeVenta = getClienteDni(v);

        String nombre = "-";
        String dni = null;

        if (nombreDesdeVenta != null && !nombreDesdeVenta.equals("-")) nombre = nombreDesdeVenta;
        else if (datosArchivo.containsKey("nombre")) nombre = datosArchivo.get("nombre");

        if (dniDesdeVenta != null && !dniDesdeVenta.trim().isEmpty()) dni = dniDesdeVenta;
        else if (datosArchivo.containsKey("dni")) dni = datosArchivo.get("dni");

        sb.append("Cliente: ").append(nullToDash(nombre)).append("\n");
        if (dni != null && !dni.trim().isEmpty()) sb.append("DNI Cliente: ").append(dni).append("\n");

        sb.append("Fecha: ").append(v != null && v.getFechaHora() != null ? DATE_FMT.format(v.getFechaHora()) : "-").append("\n");
        sb.append("------------------------------------\n");

        if (v != null && v.getItems() != null && !v.getItems().isEmpty()) {
            for (var it : v.getItems()) {
                String prodName = safeGetProductNameFromItem(it);
                int qty = safeGetQtyFromItem(it);
                double unit = safeGetUnitPriceFromItem(it);
                double lineTotal = qty * unit;
                sb.append(String.format("%-28s %4d x S/%6s   = S/%6s\n",
                        truncate(prodName, 28),
                        qty,
                        PRICE_FMT.format(unit),
                        PRICE_FMT.format(lineTotal)
                ));
            }
        } else {
            sb.append("(No hay items registrados)\n");
        }

        sb.append("------------------------------------\n");
        sb.append(String.format("TOTAL: S/%s\n", PRICE_FMT.format(v != null ? v.getTotal() : 0.0)));
        sb.append("====================================\n");
        sb.append("Gracias por su compra!\n");
        sb.append("====================================\n");
        return sb.toString();
    }

    // formato del resumen (muestra cliente también)
    private static String formatResumen(Venta v) {
        String cliente = getClienteNombre(v);
        if ((cliente == null || cliente.equals("-")) && v != null) {
            Map<String, String> datos = readClienteFileForVenta(v.getId());
            if (datos.containsKey("nombre")) cliente = datos.get("nombre");
        }
        String fecha = v != null && v.getFechaHora() != null ? DATE_FMT.format(v.getFechaHora()) : "-";
        return String.format("%s  |  Vendedor: %s  |  Cliente: %s  |  %s  |  S/%s",
                nullToDash(v != null ? v.getId() : null),
                nullToDash(v != null ? v.getUsername() : null),
                nullToDash(cliente),
                fecha,
                PRICE_FMT.format(v != null ? v.getTotal() : 0.0));
    }

    // Intenta obtener nombre del cliente usando varios getters o campos (reflection)
    private static String getClienteNombre(Venta v) {
        if (v == null) return "-";
        String[] getters = {
                "getCliente", "getClienteNombre", "getNombreCliente", "getNombre",
                "getCustomerName", "getClientName", "getClienteName"
        };
        for (String g : getters) {
            try {
                Method m = v.getClass().getMethod(g);
                Object r = m.invoke(v);
                if (r != null) return r.toString();
            } catch (Exception ignored) {}
        }
        String[] fields = {"clienteNombre", "cliente", "nombreCliente", "clienteName", "nombre", "clientName"};
        for (String fName : fields) {
            try {
                Field f = v.getClass().getDeclaredField(fName);
                f.setAccessible(true);
                Object r = f.get(v);
                if (r != null) return r.toString();
            } catch (Exception ignored) {}
        }
        return "-";
    }

    // Intenta obtener DNI del cliente si existe en Venta
    private static String getClienteDni(Venta v) {
        if (v == null) return null;
        String[] getters = {"getDni", "getDNI", "getClienteDni", "getDocumento", "getDocumentoIdentidad", "getClienteDocumento"};
        for (String g : getters) {
            try {
                Method m = v.getClass().getMethod(g);
                Object r = m.invoke(v);
                if (r != null) return r.toString();
            } catch (Exception ignored) {}
        }
        String[] fields = {"clienteDni", "dni", "documentoIdentidad", "documento", "dniCliente"};
        for (String fName : fields) {
            try {
                Field f = v.getClass().getDeclaredField(fName);
                f.setAccessible(true);
                Object r = f.get(v);
                if (r != null) return r.toString();
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static String nullToDash(String s) {
        return (s == null || s.trim().isEmpty()) ? "-" : s;
    }

    // lee archivo data/venta_<id>.txt con formato linea=valor (nombre=...,dni=...)
    private static Map<String, String> readClienteFileForVenta(String ventaId) {
        Map<String, String> map = new HashMap<>();
        if (ventaId == null || ventaId.trim().isEmpty()) return map;
        Path file = Paths.get("data", "venta_" + ventaId + ".txt");
        if (!Files.exists(file)) return map;
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String ln : lines) {
                if (ln == null) continue;
                int ix = ln.indexOf('=');
                if (ix <= 0) continue;
                String k = ln.substring(0, ix).trim();
                String v = ln.substring(ix + 1).trim();
                map.put(k, v);
            }
        } catch (IOException ignored) {}
        return map;
    }

    // Métodos seguros para leer campos de los items (evitan NullPointer/NoSuchMethod)
    private static String safeGetProductNameFromItem(Object item) {
        if (item == null) return "-";
        try {
            Method m = item.getClass().getMethod("getProductName");
            Object r = m.invoke(item);
            if (r != null) return r.toString();
        } catch (Exception ignored) {}
        try {
            Method m = item.getClass().getMethod("getNombre");
            Object r = m.invoke(item);
            if (r != null) return r.toString();
        } catch (Exception ignored) {}
        try {
            Method m = item.getClass().getMethod("getName");
            Object r = m.invoke(item);
            if (r != null) return r.toString();
        } catch (Exception ignored) {}
        try {
            Field f = item.getClass().getDeclaredField("productName");
            f.setAccessible(true);
            Object r = f.get(item);
            if (r != null) return r.toString();
        } catch (Exception ignored) {}
        try {
            Field f = item.getClass().getDeclaredField("nombre");
            f.setAccessible(true);
            Object r = f.get(item);
            if (r != null) return r.toString();
        } catch (Exception ignored) {}
        return "-";
    }

    private static int safeGetQtyFromItem(Object item) {
        if (item == null) return 0;
        try {
            Method m = item.getClass().getMethod("getQty");
            Object r = m.invoke(item);
            if (r instanceof Number) return ((Number) r).intValue();
        } catch (Exception ignored) {}
        try {
            Method m = item.getClass().getMethod("getCantidad");
            Object r = m.invoke(item);
            if (r instanceof Number) return ((Number) r).intValue();
        } catch (Exception ignored) {}
        try {
            Field f = item.getClass().getDeclaredField("qty");
            f.setAccessible(true);
            Object r = f.get(item);
            if (r instanceof Number) return ((Number) r).intValue();
        } catch (Exception ignored) {}
        return 0;
    }

    private static double safeGetUnitPriceFromItem(Object item) {
        if (item == null) return 0.0;
        try {
            Method m = item.getClass().getMethod("getUnitPrice");
            Object r = m.invoke(item);
            if (r instanceof Number) return ((Number) r).doubleValue();
        } catch (Exception ignored) {}
        try {
            Method m = item.getClass().getMethod("getPrecio");
            Object r = m.invoke(item);
            if (r instanceof Number) return ((Number) r).doubleValue();
        } catch (Exception ignored) {}
        try {
            Field f = item.getClass().getDeclaredField("unitPrice");
            f.setAccessible(true);
            Object r = f.get(item);
            if (r instanceof Number) return ((Number) r).doubleValue();
        } catch (Exception ignored) {}
        return 0.0;
    }

    // util: truncate string with ellipsis if too long
    private static String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, Math.max(0, max - 3)) + "...";
    }

    // Alerts util
    private static void alertInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }

    private static void alertWarning(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.showAndWait();
    }

    private static void alertError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.showAndWait();
    }
}
