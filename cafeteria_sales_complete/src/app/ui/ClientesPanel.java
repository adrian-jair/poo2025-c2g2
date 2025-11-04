package app.ui;

import app.dao.ClienteDAO;
import app.models.Cliente;
import app.util.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientesPanel extends JPanel {
    private ClienteDAO dao = new ClienteDAO();
    private JTable tbl;
    private DefaultTableModel model;

    public ClientesPanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.APP_WHITE);

        model = new DefaultTableModel(new Object[]{"ID","Nombre","Telefono","Direccion"},0) {
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
            if (r==-1) { JOptionPane.showMessageDialog(this, "Selecciona un cliente"); return; }
            int id = Integer.parseInt(tbl.getValueAt(r,0).toString());
            Cliente c = dao.findById(id).orElse(null);
            openForm(c);
        });
        del.addActionListener(e -> {
            int r = tbl.getSelectedRow();
            if (r==-1) { JOptionPane.showMessageDialog(this, "Selecciona un cliente"); return; }
            int id = Integer.parseInt(tbl.getValueAt(r,0).toString());
            if (JOptionPane.showConfirmDialog(this, "Eliminar cliente?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                dao.delete(id); refresh();
            }
        });
    }

    private void refresh() {
        model.setRowCount(0);
        for (Cliente c : dao.getAll()) model.addRow(new Object[]{c.getId(), c.getNombre(), c.getTelefono(), c.getDireccion()});
    }

    private void openForm(Cliente c) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cliente", true);
        dlg.setSize(420,240); dlg.setLocationRelativeTo(this);
        JPanel panel = new JPanel(null); panel.setBackground(UIConstants.APP_WHITE);

        JLabel l1 = new JLabel("Nombre:"); l1.setBounds(20,20,80,25); panel.add(l1);
        JTextField tfNom = new JTextField(); tfNom.setBounds(110,20,260,25); panel.add(tfNom);

        JLabel l2 = new JLabel("Telefono:"); l2.setBounds(20,60,80,25); panel.add(l2);
        JTextField tfTel = new JTextField(); tfTel.setBounds(110,60,140,25); panel.add(tfTel);

        JLabel l3 = new JLabel("Direccion:"); l3.setBounds(20,100,80,25); panel.add(l3);
        JTextField tfDir = new JTextField(); tfDir.setBounds(110,100,260,25); panel.add(tfDir);

        JButton save = new JButton("Guardar"); save.setBounds(110,150,100,30);
        save.setBackground(UIConstants.APP_BLUE); save.setForeground(Color.WHITE); panel.add(save);

        if (c!=null) {
            tfNom.setText(c.getNombre());
            tfTel.setText(c.getTelefono());
            tfDir.setText(c.getDireccion());
        }

        save.addActionListener(e -> {
            String nom = tfNom.getText().trim();
            String tel = tfTel.getText().trim();
            String dir = tfDir.getText().trim();
            if (nom.isEmpty()) { JOptionPane.showMessageDialog(dlg, "Nombre requerido"); return; }
            if (c==null) dao.add(new Cliente(0, nom, tel, dir));
            else { c.setNombre(nom); c.setTelefono(tel); c.setDireccion(dir); dao.update(c); }
            refresh(); dlg.dispose();
        });

        dlg.add(panel); dlg.setVisible(true);
    }
}
