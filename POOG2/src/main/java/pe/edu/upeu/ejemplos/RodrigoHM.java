package pe.edu.upeu.ejemplos;
import java.util.ArrayList;
import java.util.List;
public class RodrigoHM {
    static class Persona{
        String nombre;
        double nota;
        public Persona(String nombre, double nota){
            this.nombre=nombre;
            this.nota=nota;
        }
    }
    public static void main (String[]args){
    String[] nombres=new String[3];
    nombres[0]="Rodrigo";
    nombres[1]="Rolando";
    nombres[2]="Angela";

    Persona notas[] = new Persona[2];
    notas[0]= new Persona("Rodrigo",15.0);
    notas[1]= new Persona("Rolando",12.0);

    List<Persona> personas = new ArrayList<>();
    personas.add(new Persona("Rodrigo",15.0));
    personas.add(new Persona("Rodrigo",15.0));

    }
}
