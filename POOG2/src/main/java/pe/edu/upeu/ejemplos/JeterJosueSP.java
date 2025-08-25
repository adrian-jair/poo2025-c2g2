package pe.edu.upeu.ejemplos;

import java.util.ArrayList;
import java.util.List;


public class JeterJosueSP {
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
        nombres[0]="Jeter";
        nombres[1]="Josue";
        nombres[2]="Florinda";

        JeterJosueSP.Persona notas[] =new JeterJosueSP.Persona[2]; //Arrays Staticos
        notas[0]=new JeterJosueSP.Persona("Ever", 15.0);
        notas[1]=new JeterJosueSP.Persona("Camilo", 12.0);

        List<JeterJosueSP.Persona> personas=new ArrayList<>(); //Arrays Dinamicas
        personas.add(new JeterJosueSP.Persona("Jeter", 15.0));
        personas.add(new JeterJosueSP.Persona("Ruben", 12.0));
        personas.add(new JeterJosueSP.Persona("Jose", 20.0));

        for(JeterJosueSP.Persona persona:personas){
            System.out.println(persona.nombre+" "+persona.nota);
        }
    }
}
