package pe.edu.upeu.asistencia.enums;

public enum CARRERA {
    Sistemas(FACULTAD.FIA),
    Civil(FACULTAD.FIA),

    Administracion(FACULTAD.FCE),

    Nutricion(FACULTAD.FCS),

    Educacion(FACULTAD.FACIHED),

    General(FACULTAD.GENERAL);

    private FACULTAD facultad ;

    CARRERA (FACULTAD facultad){
        this.facultad = facultad;
    }
    public FACULTAD getFacultad() {return facultad;}
}
