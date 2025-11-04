package app.dao;

import app.models.Cliente;
import app.util.Storage;

import java.util.ArrayList;
import java.util.Optional;

public class ClienteDAO {
    private static final String FILE = "clientes.dat";
    private ArrayList<Cliente> list;

    public ClienteDAO() {
        list = Storage.loadList(FILE);
        if (list.isEmpty()) seed();
    }

    private void seed() {
        list.add(new Cliente(1, "Juan Perez", "999111222", "Av. Central 123"));
        list.add(new Cliente(2, "María López", "988333444", "Jr. Las Flores 45"));
        save();
    }

    private void save() { Storage.save(FILE, list); }

    public ArrayList<Cliente> getAll() { return list; }

    public void add(Cliente c) {
        int next = list.stream().mapToInt(Cliente::getId).max().orElse(0) + 1;
        c.setId(next);
        list.add(c);
        save();
    }

    public void update(Cliente c) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == c.getId()) {
                list.set(i, c);
                save();
                return;
            }
        }
    }

    public void delete(int id) {
        list.removeIf(x -> x.getId() == id);
        save();
    }

    public Optional<Cliente> findById(int id) {
        return list.stream().filter(x -> x.getId() == id).findFirst();
    }
}
