package org.example.view;

import java.awt.Dimension;
import java.awt.GridBagLayout ;

import javax.swing.JPanel;

import org.example.Main;
import org.example.main.MainPanel;
import org.example.userInterface.UpperToolbar;

public class NorthPanel extends JPanel
{

	public static final Dimension stdButtonSize ;

    private UpperToolbar upperToolbar ;
    private JPanel buttonsPanel1 ;
    private JPanel buttonsPanel2 ;

    static
    {        
        stdButtonSize = new Dimension(32, 30) ;
    }

    public NorthPanel()
    {
        upperToolbar = new UpperToolbar();
        buttonsPanel1 = MainPanel.stdPanel(new Dimension(7 * stdButtonSize.width + 4, stdButtonSize.height), Main.palette[2]);
        buttonsPanel2 = MainPanel.stdPanel(new Dimension(260, 30), Main.palette[2]);
        
        this.setLayout(new GridBagLayout()) ;
		this.add(buttonsPanel1);
		this.add(upperToolbar);
		this.add(buttonsPanel2);
    }

    public UpperToolbar getUpperToolbar() { return upperToolbar ;}

}
