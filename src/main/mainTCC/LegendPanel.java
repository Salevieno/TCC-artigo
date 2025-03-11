package main.mainTCC;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import main.gui.DrawingOnAPanel;
import main.gui.Menus;

public class LegendPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private static final Dimension initialSize = new Dimension(10, 100) ;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;

    public LegendPanel()
    {
        setPreferredSize(initialSize);
        setBackground(Menus.palette[3]);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Legenda", TitledBorder.CENTER, TitledBorder.CENTER));
		
    }
		
    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        DP.setG(g);
        DP.setRealStructCenter(MainPanel.structure.getCenter());
        MenuFunctions.DrawOnLegendPanel(MainPanel.structure, this.getSize(), DP);
        repaint();
    }
}
