
/**
	with love, by Salevieno
**/
package org.example;


import java.awt.EventQueue;

import org.example.userInterface.Menus;

public class Main
{
	private static final Menus menusInstance = Menus.getInstance() ;

    public static void main(String[] args)
    {
		EventQueue.invokeLater(() -> {menusInstance.setVisible(true) ;}) ;
    }
}