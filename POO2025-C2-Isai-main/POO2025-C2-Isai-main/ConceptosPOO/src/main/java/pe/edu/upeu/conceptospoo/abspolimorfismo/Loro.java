package pe.edu.upeu.conceptospoo.abspolimorfismo;

public class Loro extends Animal{
    @Override
    public void sonidoAnimal(){
        System.out.println("Holis, como te va!!");
    }

    @Override
    public void dormir(){
        System.out.println("No hagas bulla tengo sueño...Zzzzz");
    }
}
