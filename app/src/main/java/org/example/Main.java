
/**
	with love, by Salevieno
**/
package org.example;


import java.awt.Color;
import java.awt.EventQueue;

import org.example.mainTCC.MainFrame;

public class Main
{
		/*
			* Neutral: 0, 1, 2, 3
			* Primary: 4, 5, 6, 7, 8, 9, 10
			* Contrast: 5 w 9, 4 w 9, 5 w 8, 6 w 9, 6 w 7, 6 w 8
			* 
			* Main background: 3
			* Menu background: 6
			* 
			* Unselected: 0
			* Selection: 10
			* Loads: 7
			* Supports: 7
			* Displacements: 4
			* Reactions: 8
		*/
	public static final Color[] palette = new Color[]		
	{
		new Color(35, 31, 31),	// black
		new Color(204, 204, 204),	// gray
		new Color(250, 246, 246),	// white
		new Color(225, 211, 211),	// red white
		new Color(32, 95, 102),	// blue green
		new Color(90, 93, 136),	// dark blue
		new Color(84, 87, 80),	// dark gray green
		new Color(228, 137, 92),	// red
		new Color(143, 226, 210),	// red pink
		new Color(206, 235, 160),	// orange
		new Color(237, 100, 91),	// green yellow
		new Color(237, 91, 176)	// light blue
	};
  	private static final MainFrame mainFrame = MainFrame.getInstance() ;


    public static void main(String[] args)
    {
		EventQueue.invokeLater(() -> {mainFrame.setVisible(true) ;}) ;
    }
}