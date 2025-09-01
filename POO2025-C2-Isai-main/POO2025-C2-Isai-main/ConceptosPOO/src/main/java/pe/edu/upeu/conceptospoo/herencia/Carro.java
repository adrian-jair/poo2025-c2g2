package pe.edu.upeu.conceptospoo.herencia;

public class Carro extends Vehiculo{

    private String modelo="Modelo X";
    private String color="Blanco";

    void caracteristicas(){
        marca = marca; //atributo del carro
        System.out.println("El Vehículo tiene las siguientes características:");
        System.out.println(marca+" - "+modelo+" de color "+color);
        System.out.println("Emite el siguiente sonido: "+ sonido());
    }

    public static void main(String[] args) {
    Carro miCarro=new Carro();
    miCarro.caracteristicas();

    }
}
