package pe.edu.upeu.asistencia.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// con lombok, decoradores o notaciones
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Asistencia {
    private String nombre;
    private String estado;
}
