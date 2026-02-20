package practica.main;

import practica.gui.Controlador;
import practica.gui.Modelo;
import practica.gui.Vista;

public class Principal {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo,vista);
    }
}
