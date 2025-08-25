package pe.edu.upeu.ejemplos;

import java.util.ArrayList;
import java.util.List;

public class MaykolMM {
    static class Persona{
        String nombre;
        double nota;
        //Constructor
        public Persona(String nombre, double nota){
            this.nombre=nombre;
            this.nota=nota;
        }

    }
    public static void main(String[] args) {

        String[] nombres=new String[3];
        nombres[0]="Maykol";
        nombres[1]="Montalvo";
        nombres[2]="Machaca";

        Persona notas[] =new Persona[2]; //Arrays Staticos
        notas[0]=new Persona("Maykol", 15.0);
        notas[1]=new Persona("Montalvo", 12.0);

        List<Persona> personas=new ArrayList<>(); //Arrays Dinamicas
        personas.add(new Persona("Maykol", 15.0));
        personas.add(new Persona("Montalvo", 12.0));
        personas.add(new Persona("Machaca", 20.0));

        for(Persona persona:personas){
            System.out.println(persona.nombre+" "+persona.nota);
        }
    }
}
