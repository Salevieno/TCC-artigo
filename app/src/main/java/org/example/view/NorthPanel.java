package org.example.view;

import java.awt.Dimension;
import java.awt.GridBagLayout ;

import javax.swing.JPanel;

import org.example.userInterface.Menus;
import org.example.userInterface.UpperToolbar;

public class NorthPanel extends JPanel
{

	public static final Dimension stdButtonSize = new Dimension(32, 30) ;

    private UpperToolbar upperToolbar = new UpperToolbar();
    private JPanel buttonsPanel1 = Menus.stdPanel(new Dimension(7 * stdButtonSize.width + 4, stdButtonSize.height), Menus.palette[2]);
    private JPanel buttonsPanel2 = Menus.stdPanel(new Dimension(260, 30), Menus.palette[2]);

    public NorthPanel()
    {
        this.setLayout(new GridBagLayout()) ;
		this.add(buttonsPanel1);
		this.add(upperToolbar);
		this.add(buttonsPanel2);
    }

    public UpperToolbar getUpperToolbar() { return upperToolbar ;}

}
