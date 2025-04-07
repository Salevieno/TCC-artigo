package org.example.mainTCC;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;

import org.example.userInterface.MenuBar;

public class MainFrame extends JFrame
{    
    private static final Point topLeft;
	private static final Dimension initialSize ;

    private static final MainFrame instance ;

    static
    {
		topLeft = new Point(150, 50) ;
		initialSize = new Dimension(1084, 700) ;

        instance = new MainFrame() ;
    }

    private MainFrame()
    {
        pack();
		this.setLocation(topLeft) ;
		this.setTitle("TCC") ;
		this.setPreferredSize(initialSize) ;
		this.setSize(initialSize) ;
        this.setJMenuBar(MenuBar.getInstance()) ;
        this.add(MainPanel.getInstance()) ;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE) ;
    }

    public static Point getTopLeft() { return topLeft ;}

    public static MainFrame getInstance() { return instance ;}
}
