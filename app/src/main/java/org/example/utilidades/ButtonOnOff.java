package org.example.utilidades;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ButtonOnOff extends JButton
{

    private boolean state ;
    private final ImageIcon imgOff ;
    private final ImageIcon imgOn ;

    private static final Dimension size ;

    static
    {
        size = new Dimension(30, 30) ;
    }

    public ButtonOnOff(String imgOffPath, String imgOnPath)
    {
        this.imgOff = new ImageIcon(imgOffPath) ;
        this.imgOn = new ImageIcon(imgOnPath) ;
        this.setIcon(imgOff) ;
        this.setPreferredSize(size) ;
        this.setBackground(null) ;
        this.setBorder(null) ;
        this.state = false ;
    }

    public void switchState()
    {
        state = !state ;
        setIcon(state ? imgOn : imgOff) ;
    }

    public boolean getState() { return state ;}
}
