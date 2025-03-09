package main.mainTCC;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import main.gui.DrawingOnAPanel;
import main.gui.Menus;

public class ListPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private static final Dimension initialSize = new Dimension(10, 100) ;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;

    public ListPanel()
    {
        setPreferredSize(initialSize);
        setBackground(Menus.palette[2]);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        DP.setG(g);
        DP.setRealStructCenter(MenuFunctions.struct.getCenter());
        if (Menus.getInstance() != null)
        {
            MenuFunctions.DrawOnListsPanel(initialSize, Menus.getInstance().getAqueleBooleanGrande(), DP);
        }
        repaint();
    }
}
