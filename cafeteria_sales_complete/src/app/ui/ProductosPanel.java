package app.ui;

import app.dao.ProductoDAO;
import app.models.Producto;
import app.util.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductosPanel extends JPanel {
    private ProductoDAO dao = new ProductoDAO();
    private JTable tbl;
    private DefaultTableModel model;

    public ProductosPanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.APP_WHITE);

        model = new DefaultTableModel(new Object[]{"ID","Nombre","Precio","Descripcion"},0) {
            public boolean isCellEditable(int r, int c){ return false; }
        };
        tbl = new JTable(model);
        refresh();
        add(new JScrollPane(tbl), BorderLayout.CENTER);

        JPanel buttons = new JPanel(); buttons.setBackground(UIConstants.APP_WHITE);
        JButton add = new JButton("Agregar"), edit = new JButton("Editar"), del = new JButton("Eliminar");
        buttons.add(add); buttons.add(edit); buttons.add(del);
        add(buttons, BorderLayout.SOUTH);

        add.addActionListener(e -> openForm(null));
        edit.addActionListener(e -> {
            int r = tbl.getSelectedRow();
            if (r==-1) { JOptionPane.showMessageDialog(this, "Selecciona un producto"); return; }
            int id = Integer.parseInt(tbl.getValueAt(r,0).toString());
            Producto p = dao.findById(id).orElse(null);
            openForm(p);
        });
        del.addActionListener(e -> {
            int r = tbl.getSelectedRow();
            if (r==-1) { JOptionPane.showMessageDialog(this, "Selecciona un producto"); return; }
            int id = Integer.parseInt(tbl.getValueAt(r,0).toString());
            if (JOptionPane.showConfirmDialog(this, "Eliminar producto?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                dao.delete(id); refresh();
            }
        });
    }

    private void refresh() {
        model.setRowCount(0);
        for (Producto p : dao.getAll()) model.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getDescripcion()});
    }

    private void openForm(Producto p) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Producto", true);
        dlg.setSize(420,260); dlg.setLocationRelativeTo(this);
        JPanel panel = new JPanel(null); panel.setBackground(UIConstants.APP_WHITE);

        JLabel l1 = new JLabel("Nombre:"); l1.setBounds(20,20,80,25); panel.add(l1);
        JTextField tfNom = new JTextField(); tfNom.setBounds(110,20,260,25); panel.add(tfNom);

        JLabel l2 = new JLabel("Precio:"); l2.setBounds(20,60,80,25); panel.add(l2);
        JTextField tfPrecio = new JTextField(); tfPrecio.setBounds(110,60,120,25); panel.add(tfPrecio);

        JLabel l3 = new JLabel("Descripcion:"); l3.setBounds(20,100,80,25); panel.add(l3);
        JTextField tfDesc = new JTextField(); tfDesc.setBounds(110,100,260,25); panel.add(tfDesc);

        JButton save = new JButton("Guardar"); save.setBounds(110,150,100,30);
        save.setBackground(UIConstants.APP_BLUE); save.setForeground(Color.WHITE); panel.add(save);

        if (p!=null) {
            tfNom.setText(p.getNombre());
            tfPrecio.setText(String.valueOf(p.getPrecio()));
            tfDesc.setText(p.getDescripcion());
        }

        save.addActionListener(e -> {
            String nom = tfNom.getText().trim();
            String desc = tfDesc.getText().trim();
            double precio = 0;
            try { precio = Double.parseDouble(tfPrecio.getText().trim()); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(dlg, "Precio invalido"); return; }
            if (nom.isEmpty()) { JOptionPane.showMessageDialog(dlg, "Nombre requerido"); return; }

            if (p==null) dao.add(new Producto(0, nom, precio, desc));
            else { p.setNombre(nom); p.setPrecio(precio); p.setDescripcion(desc); dao.update(p); }
            refresh(); dlg.dispose();
        });

        dlg.add(panel); dlg.setVisible(true);
    }
}
