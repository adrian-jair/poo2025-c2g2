package pe.edu.upeu.ejemplos;

import java.util.ArrayList;
import java.util.List;

public class IsaiAU {
    static class Persona{
        String nombre;
        double nota;
        // constructor
        public Persona(String nombre, double nota){
            this.nombre=nombre;
            this.nota=nota;
        }
    }
    public static void main(String[] args) {
        String [] nombres =new String[3];

        nombres[0]="Isai";
        nombres[1]="Celso";
        nombres[2]="Inocencia";

        Persona notas[]=new Persona[2]; //Arrays staticos

        notas[0]=new Persona("Isai",20.0);
        notas[1]=new Persona("Celso",19.0);

        List<Persona> personas=new ArrayList<>(); //arrays dinámicos
        personas.add(new Persona("Isai",20.0));
        personas.add(new Persona("Celso",19.0));
    }
}
