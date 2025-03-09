package main.mainTCC;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import main.gui.DrawingOnAPanel;
import main.gui.Menus;

public class DiagramsPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    
    private static final Dimension initialSize = new Dimension(0, 100) ;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;

    public DiagramsPanel()
    {
        setSize(initialSize);
        setBackground(Menus.palette[3]);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Curva carga deslocamento", TitledBorder.CENTER, TitledBorder.CENTER));

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
