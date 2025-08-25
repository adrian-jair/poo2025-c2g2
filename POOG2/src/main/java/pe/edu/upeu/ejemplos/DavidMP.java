/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.upeu.ejemplos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SOFTWARE
 */
public class DavidMP {

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
        nombres[0]="David";
        nombres[1]="Rafael";
        nombres[2]="Flavia";

        Persona notas[] =new Persona[2]; //Arrays Staticos
        notas[0]=new Persona("David", 15.0);
        notas[1]=new Persona("Rafael", 12.0);

        List<Persona> personas=new ArrayList<>(); //Arrays Dinamicas
        personas.add(new Persona("David", 15.0));
        personas.add(new Persona("Rafael", 12.0));
        personas.add(new Persona("Paul", 20.0));

        for(Persona persona:personas){
            System.out.println(persona.nombre+" "+persona.nota);
        }



    }
    
}
