package main.mainTCC;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.gui.DrawingOnAPanel;

public class InstructionsPanel extends JPanel
{
    
    private static final long serialVersionUID = 1L;
    private static final Dimension initialSize = new Dimension(10, 100) ;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;

    public InstructionsPanel()
    {
        setPreferredSize(initialSize);
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        DP.setG(g);
        DP.setRealStructCenter(MenuFunctions.struct.getCenter());
        MenuFunctions.DrawOnLDPanel(initialSize, DP);
        repaint();
    }
}
