package com.mycompany.tiendaderopa;

import com.mycompany.tiendaderopa.vistas.MainFrame;

/**
 *  Hallazgo 10: Se elimina la lógica duplicada de arranque.
 * TiendaDeRopa es el punto de entrada del pom.xml, pero delega COMPLETAMENTE
 * a MainFrame.main() para que Nimbus se configure igual en ambos casos.
 * Así hay UN SOLO comportamiento visual sin importar cómo se ejecute.
 *
 * @author juanj
 */
public class TiendaDeRopa {

    public static void main(String[] args) {
        // Delegar al main de MainFrame para tener un solo punto de entrada con Nimbus
        MainFrame.main(args);
    }
}