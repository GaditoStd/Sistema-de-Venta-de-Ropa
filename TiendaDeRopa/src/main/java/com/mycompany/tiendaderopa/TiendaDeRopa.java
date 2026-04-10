/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tiendaderopa;
import javax.swing.JFrame;
import com.mycompany.tiendaderopa.vistas.ClientePanel;
/**
 *
 * @author juanj
 */
public class TiendaDeRopa {

    public static void main(String[] args) {

        JFrame ventana = new JFrame("Tienda de Ropa");
        ventana.setSize(600, 400);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ventana.add(new ClientePanel());

        ventana.setVisible(true);
    }
}
