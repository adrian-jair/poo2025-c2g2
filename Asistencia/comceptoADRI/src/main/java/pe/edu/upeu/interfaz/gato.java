package pe.edu.upeu.interfaz;

public class gato implements animal{
    @Override
    public void emitirsonido(){
        System.out.println("miaaauuu");
    }
    @Override
    public void dormir (){
        System.out.println("zzzz...zzzz");
    }
    public String juega (){
        return "le gusta jugar con el raton ";
    }
}
