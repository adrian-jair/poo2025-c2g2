package pe.edu.upeu.conceptospoo.interfaz;

public class Gato implements Animal{

    @Override
    public void emitirSonido() {
        System.out.println("Miau Miau");
    }

    @Override
    public void dormir() {
        System.out.println("GrrGrrGrr");
    }

    public String juega(){
        return "Le gusta jugar con el ratón";
    }
}
