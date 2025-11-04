package pe.edu.cafeteria.service;

import pe.edu.cafeteria.model.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {
    private final List<Cliente> clientes = new ArrayList<>();

    public void agregar(Cliente c) { clientes.add(c); }

    public void eliminar(Cliente c) { clientes.remove(c); }

    public void editar(int idx, Cliente c) { clientes.set(idx, c); }
}
