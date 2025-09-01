package pe.edu.upeu.conceptospoo.encapsulamiento;

public class Persona {
    private String nombre;
    private int edad;

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void saludo(){
        System.out.println("Hola, soy "+nombre+" y mi edad es: "+edad);
    }

}
