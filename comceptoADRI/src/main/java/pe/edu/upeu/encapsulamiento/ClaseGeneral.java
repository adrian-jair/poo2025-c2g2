package pe.edu.upeu.encapsulamiento;

public class ClaseGeneral {
    public static void main(String[] args) {
        persona persona = new persona();

        //persona.nombre ="adrian ";
        //persona.edad=18;
        persona.setNombre("yoselin ");
        persona.setEdad(18);


        System.out.println("nombre : "+persona.getNombre());
        System.out.println("edad : "+persona.getEdad());

        persona.saludar();
    }
}
