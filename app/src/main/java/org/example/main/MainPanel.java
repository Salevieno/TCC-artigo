package org.example.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.example.structure.Structure;
import org.example.view.CentralPanel;
import org.example.view.EastPanel;
import org.example.view.NorthPanel;
import org.example.view.WestPanel;

public class MainPanel extends JPanel
{
    public static final Dimension defaultPanelSize ;
	private final NorthPanel northPanel ;
	private final CentralPanel centralPanel ;
	private final EastPanel eastPanel ;
	private final WestPanel westPanel ;

    private static final MainPanel instance ;

    static
    {
        defaultPanelSize = new Dimension(260, 300);
        instance = new MainPanel() ;
    }

    private MainPanel()
    {
        northPanel = new NorthPanel() ;
        centralPanel = new CentralPanel(MainFrame.getTopLeft()) ;
        eastPanel = new EastPanel() ;
        westPanel = new WestPanel() ;

		this.setLayout(new BorderLayout());
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centralPanel, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);
    }

	public static JPanel stdPanel(Dimension size, Color bgcolor)
	{
		JPanel blankPanel = new JPanel();
		blankPanel.setLayout(new GridLayout(1, 1));
		blankPanel.setPreferredSize(size);
		blankPanel.setBackground(bgcolor);
		return blankPanel;
	}

	public void ActivatePostAnalysisView(Structure structure)
	{
		if (((Double)structure.getU()[0]).isNaN()) { System.out.println("Error: Trying to activate post analysis view with displacement NaN") ; return ;}
		
		eastPanel.getLegendPanel().setStructure(structure) ;
		eastPanel.activatePostAnalysisView() ;
		westPanel.getToolbarResults().setVisible(true);
	}

	public NorthPanel getNorthPanel() { return northPanel ;}

	public CentralPanel getCentralPanel() { return centralPanel ;}

	public EastPanel getEastPanel() { return eastPanel ;}

	public WestPanel getWestPanel() { return westPanel ;}
    
    public static MainPanel getInstance() { return instance ;}

}
