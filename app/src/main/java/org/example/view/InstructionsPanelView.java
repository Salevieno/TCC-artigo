package org.example.view;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.example.mainTCC.MenuFunctions;
import org.example.userInterface.DrawingOnAPanel;

public class InstructionsPanelView extends JPanel
{
    
    private static final long serialVersionUID = 1L;
    private static final Dimension initialSize = new Dimension(10, 100) ;

    public InstructionsPanelView()
    {
        setPreferredSize(initialSize);
    }

}
