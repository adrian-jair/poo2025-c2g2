package pe.edu.upeu.cafeteria.control;

import pe.edu.upeu.cafeteria.modelo.Producto;
import pe.edu.upeu.cafeteria.repositorio.ProductoRepositorio;
import java.util.List;

public class ProductoController {
    private  ProductoRepositorio repositorio = new ProductoRepositorio();

    public void crearProducto(Producto p) {
        repositorio.crear(p);
    }

    public List<Producto> listarProductos() {
        return repositorio.obtenerTodos();
    }

    public void actualizarProducto(Producto p) {
        if (!repositorio.actualizar(p)) {
            System.out.println(" No se encontró el producto con ID: " + p.getId());
        }
    }

    public void eliminarProducto(int id) {
        if (!repositorio.eliminar(id)) {
            System.out.println(" No se encontró producto con ID: " + id);
        }
    }
}
