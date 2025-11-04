package app.dao;

import app.models.Producto;
import app.util.Storage;

import java.util.ArrayList;
import java.util.Optional;

public class ProductoDAO {
    private static final String FILE = "productos.dat";
    private ArrayList<Producto> list;

    public ProductoDAO() {
        list = Storage.loadList(FILE);
        if (list.isEmpty()) seed();
    }

    private void seed() {
        list.add(new Producto(1, "Café Americano", 5.0, "Taza de café americano"));
        list.add(new Producto(2, "Café con Leche", 6.5, "Café con leche espumosa"));
        list.add(new Producto(3, "Sándwich", 8.0, "Sándwich de jamón y queso"));
        save();
    }

    private void save() { Storage.save(FILE, list); }

    public ArrayList<Producto> getAll() { return list; }

    public void add(Producto p) {
        int next = list.stream().mapToInt(Producto::getId).max().orElse(0) + 1;
        p.setId(next);
        list.add(p);
        save();
    }

    public void update(Producto p) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == p.getId()) {
                list.set(i, p);
                save();
                return;
            }
        }
    }

    public void delete(int id) {
        list.removeIf(x -> x.getId() == id);
        save();
    }

    public Optional<Producto> findById(int id) {
        return list.stream().filter(x -> x.getId() == id).findFirst();
    }
}
