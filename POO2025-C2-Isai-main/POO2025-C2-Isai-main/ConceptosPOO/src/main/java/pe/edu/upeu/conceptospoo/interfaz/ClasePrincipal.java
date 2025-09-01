package pe.edu.upeu.conceptospoo.interfaz;

public class ClasePrincipal {
    public static void main(String[] args) {
        Loro loro= new Loro();
        loro.emitirSonido();
        loro.dormir();
        System.out.println("-");
        Gato gato = new Gato();
        gato.emitirSonido();
        gato.juega();
        gato.dormir();
    }
}
