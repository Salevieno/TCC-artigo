package org.example.view;

import java.awt.Dimension;
import java.awt.GridBagLayout ;

import javax.swing.JPanel;

import org.example.userInterface.Menus;
import org.example.userInterface.UpperToolbar;

public class NorthPanel extends JPanel
{
    UpperToolbar utb = new UpperToolbar();
    JPanel bp1 = Menus.stdPanel(new Dimension(7 * 32 + 4, 30), Menus.palette[2]);
    JPanel bp2 = Menus.stdPanel(new Dimension(260, 30), Menus.palette[2]);

    public NorthPanel()
    {
        this.setLayout(new GridBagLayout()) ;
		this.add(bp1);
		this.add(utb);
		this.add(bp2);
    }

    public UpperToolbar getUpperToolbar() { return utb ;}

}
