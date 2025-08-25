package pe.edu.upeu.ejemplos;

import java.util.ArrayList;
import java.util.List;

public class LeodanRP {
    static class Persona{
        String nombre;
        double nota;

        //CONSTRUCTOR
        public Persona(String nombre, double nota){
            this.nombre=nombre;
            this.nota=nota;

        }
    }

    public static void main(String[] args) {

        String [] nombres=new String[3];
        nombres[0]="leodan";
        nombres[1]="isaac";
        nombres[2]="maria";


        Persona notas[] = new Persona[2]; //ARRAY STATICOS
        notas[0]=new Persona("leodan", 15.0);
        notas[1]=new Persona("isaac", 25.0);

        List<Persona> personas=new ArrayList<>(); //ARRAYS DINAMICOS
        personas.add(new Persona("leodan", 15.0));
        personas.add(new Persona("isaac", 25.0));
        personas.add(new Persona("maria", 15.0));



    }
}