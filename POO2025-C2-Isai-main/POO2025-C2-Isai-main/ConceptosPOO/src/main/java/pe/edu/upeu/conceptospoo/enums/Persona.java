package pe.edu.upeu.conceptospoo.enums;

import java.sql.SQLOutput;

enum GENERO{Masculino,Femenino};
enum NACIONALIDAD{Peruano, Venezolano, Boliviano}

public class Persona {
    static String nombre;

    static GENERO genero = GENERO.Masculino;
    static NACIONALIDAD nacionalidad= NACIONALIDAD.Peruano;

    public static void main (String[] args){
        nombre = "Isai";

        System.out.println("Nombre: "+nombre+" es "+genero+" y "+nacionalidad);

        for(GENERO XX: GENERO.values()){
            System.out.println("Genero: "+XX);
        }

        for(NACIONALIDAD XX: NACIONALIDAD.values()){
            System.out.println("Nacionalidad: "+XX);
        }
    }

}

