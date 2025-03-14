package org.example.view;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.example.mainTCC.MenuFunctions;
import org.example.userInterface.DrawingOnAPanel;

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
        DP.setRealStructCenter(MainPanel.structure.getCenter());
        DiagramsPanel.display(MainPanel.structure, MenuFunctions.selectedNodes, MainPanel.SelectedVar, MainPanel.SelectedDiagram, MenuFunctions.AnalysisIsComplete, DP);
        repaint();
    }
}
