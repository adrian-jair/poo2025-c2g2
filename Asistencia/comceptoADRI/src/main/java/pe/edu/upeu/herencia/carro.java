package pe.edu.upeu.herencia;

public class carro extends Veiculo{
    String color ="negro";
    String modelo = "tesla";


    void caracteristicas (){
        marca="toyota";
        System.out.println("el vehiculo tiene las siguientes caracteristicas : ");
        System.out.println("marca : "+marca);
        System.out.println("modelo : "+modelo);
        System.out.println("color : "+modelo);
        System.out.println("y emitir el siguiente sonido "+ sonido());

    }

    public static void main(String[] args) {
        carro carro = new carro();
        carro.caracteristicas();

    }
}
