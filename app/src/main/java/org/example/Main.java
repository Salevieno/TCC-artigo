
/**
	By Salevieno
	Last upgrade date: 01/01/0001	
	Last update: 
	Next improvements:
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