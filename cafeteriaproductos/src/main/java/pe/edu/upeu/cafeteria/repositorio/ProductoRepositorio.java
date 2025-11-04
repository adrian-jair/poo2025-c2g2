package pe.edu.upeu.cafeteria.repositorio;

import pe.edu.upeu.cafeteria.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositorio {
    private List<Producto> productos = new ArrayList<>();

    public void crear(Producto producto) {
        productos.add(producto);
        System.out.println("Producto agregado: " + producto);
    }

    public List<Producto> obtenerTodos() {
        return productos;
    }

    public boolean actualizar(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == producto.getId()) {
                productos.set(i, producto);
                System.out.println("Producto actualizado: " + producto);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(int id) {
        return productos.removeIf(p -> p.getId() == id);
    }
}
