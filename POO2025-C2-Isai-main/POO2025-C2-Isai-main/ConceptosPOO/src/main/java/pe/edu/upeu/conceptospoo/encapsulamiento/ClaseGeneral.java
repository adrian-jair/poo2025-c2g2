package pe.edu.upeu.conceptospoo.encapsulamiento;

public class ClaseGeneral {

    public static void main(String[] args) {
        Persona persona = new Persona(); //persona es un objeto
        //persona.nombre="Isai";
        //persona.edad=23;

        //con setters and getters
        persona.setNombre("Mario");
        persona.setEdad(25);

        System.out.println(persona.getNombre());
        System.out.println(persona.getEdad());

        persona.saludo();
    }
}
