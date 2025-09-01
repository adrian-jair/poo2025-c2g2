package pe.edu.upeu.conceptospoo.claseinterna;

public class ClaseExterna {
    int a=1,b=2; // clase general

    int operacion(){
        return a+b;
    }

    class ClaseInterna1{
        int r;
        void mensaje(){
            r=a+b;
            System.out.println("La suma es: "+r);
        }
    }

    class ClaseInterna2{
        void otraOperacion(){
            System.out.println("La operación es: "+(a-b));
        }
    }

    public static void main(String[] args) {
        ClaseExterna ce = new ClaseExterna();
        /*
        ce.a=8; //declarar aquí o arriba
        ce.b=2;
        */

        System.out.println("La operación es : "+ce.operacion());

        ClaseInterna1 ci1 = ce.new ClaseInterna1();
        ci1.mensaje();

        ClaseInterna2 ci2 = ce.new ClaseInterna2();
        ci2.otraOperacion();

    }
}

class ClaseExternax{ // clase que se puede instanciar objetos

}