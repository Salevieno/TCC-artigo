package org.example.mainTCC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import org.example.structure.Structure;
import org.example.utilidades.MyCanvas;
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
    
	private final MyCanvas mainCanvas ;

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

		this.setLayout(new GridBagLayout());
		this.setLayout(new BorderLayout());
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centralPanel, BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(eastPanel, BorderLayout.EAST);
        
		Dimension mainCanvasSize = new Dimension((int) (0.4 * centralPanel.getSize().getWidth()), (int) (0.8 * centralPanel.getSize().getHeight())) ;
	    mainCanvas = new MyCanvas(new Point(575, 25), mainCanvasSize, new Point2D.Double(10, 10), new Point());	    
    }

	public static JPanel stdPanel(Dimension size, Color bgcolor)
	{
		JPanel blankPanel = new JPanel();
		blankPanel.setLayout(new GridLayout(1, 1));
		blankPanel.setPreferredSize(size);
		blankPanel.setBackground(bgcolor);
		return blankPanel;
	}
    
	public void EnableButtons()
	{
		if (MenuFunctions.AnalysisIsComplete)
		{
			northPanel.getUpperToolbar().enableButtonsScale() ;
		}
	}

	public void ActivatePostAnalysisView(Structure structure)
	{
		if (((Double)structure.getU()[0]).isNaN()) { System.out.println("Error: Trying to activate post analysis view with displacement NaN") ; return ;}
		
		eastPanel.getLegendPanel().setStructure(structure) ;
		eastPanel.activatePostAnalysisView() ;
		westPanel.getToolbarResults().setVisible(true);
	}

	public MyCanvas getMainCanvas() { return mainCanvas ;}

	public NorthPanel getNorthPanel() { return northPanel ;}

	public CentralPanel getCentralPanel() { return centralPanel ;}

	public EastPanel getEastPanel() { return eastPanel ;}

	public WestPanel getWestPanel() { return westPanel ;}
    
    public static MainPanel getInstance() { return instance ;}

}
