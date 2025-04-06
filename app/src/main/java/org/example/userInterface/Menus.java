package org.example.userInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.example.analysis.Analysis;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.Element;
import org.example.structure.Structure;
import org.example.utilidades.MyCanvas;
import org.example.view.EastPanel;
import org.example.view.MainPanel;
import org.example.view.NorthPanel;
import org.example.view.WestPanel;

/*
 Prâximas adiçõs
 *Ajustar o desenho de momento 3D
 *
 *Mnemonics do menu nâo funcionam
 *Selecionar elemento retangular clicando dentro
 */

public class Menus extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public static final Dimension defaultPanelSize ;
	private static final Dimension initialSize ;
	private static final Menus instance ;
	public static final Point frameTopLeft;
	public static final Color[] palette  ;
	
	private final NorthPanel northPanel = new NorthPanel() ;
	private final MainPanel mainPanel = new MainPanel(frameTopLeft) ;
	private final EastPanel eastPanel = new EastPanel() ;
	private final WestPanel westPanel = new WestPanel() ;
	
	private final MyCanvas mainCanvas ;

	private JMenuBar menuBar;
	private MenuStructure menuStructure ;
    private MenuView viewMenu ;
	private MenuAnalysis analysisMenu ;
	private MenuResults resultsMenu ;
	private MenuEspecial especialMenu;	


	static
	{
		frameTopLeft = new Point(150, 50);
		defaultPanelSize = new Dimension(260, 300);
		initialSize = new Dimension(1084, 700) ;

		/*
			* Neutral: 0, 1, 2, 3
			* Primary: 4, 5, 6, 7, 8, 9, 10
			* Contrast: 5 w 9, 4 w 9, 5 w 8, 6 w 9, 6 w 7, 6 w 8
			* 
			* Main background: 3
			* Menu background: 6
			* 
			* Unselected: 0
			* Selection: 10
			* Loads: 7
			* Supports: 7
			* Displacements: 4
			* Reactions: 8
		*/
		palette = new Color[]		
		{
			new Color(35, 31, 31),	// black
			new Color(204, 204, 204),	// gray
			new Color(250, 246, 246),	// white
			new Color(225, 211, 211),	// red white
			new Color(32, 95, 102),	// blue green
			new Color(90, 93, 136),	// dark blue
			new Color(84, 87, 80),	// dark gray green
			new Color(228, 137, 92),	// red
			new Color(143, 226, 210),	// red pink
			new Color(206, 235, 160),	// orange
			new Color(237, 100, 91),	// green yellow
			new Color(237, 91, 176)	// light blue
		};

		instance = new Menus();
	}
	
	private Menus()
	{

		setLocation(frameTopLeft);

	    Point ScreenTopLeft = new Point() ;				// Initial coordinates from the top left of the canvas window 900 720
		Dimension mainCanvasSize = new Dimension((int) (0.4 * mainPanel.getSize().getWidth()), (int) (0.8 * mainPanel.getSize().getHeight())) ;
	    mainCanvas = new MyCanvas(new Point(575, 25), mainCanvasSize, new Point2D.Double(10, 10), ScreenTopLeft);	    

		menuBar = new JMenuBar();
		menuStructure = new MenuStructure();
		viewMenu = new MenuView() ;
		analysisMenu = new MenuAnalysis() ;
		resultsMenu = new MenuResults() ;
		especialMenu = new MenuEspecial() ;
		
		menuBar.add(MenuFile.create());
		menuBar.add(menuStructure);
		menuBar.add(viewMenu);
		menuBar.add(analysisMenu);
		menuBar.add(resultsMenu);
		menuBar.add(especialMenu);

		this.setJMenuBar(menuBar);
		this.setTitle("TCC");				// Super frame sets its title
		this.setPreferredSize(initialSize) ;		// Super frame sets its size
		this.setVisible(true);					// Super frame gets into the show
		this.setDefaultCloseOperation(EXIT_ON_CLOSE) ;
		pack();
		/* Super frame sets its everything. Super frame is so independent! =,) */

		JPanel newContentPanel = new JPanel(new GridBagLayout());
		BorderLayout bl = new BorderLayout();
		newContentPanel.setLayout(bl);		
		newContentPanel.add(northPanel, BorderLayout.NORTH);
		newContentPanel.add(mainPanel, BorderLayout.CENTER);
		newContentPanel.add(westPanel, BorderLayout.WEST);
		newContentPanel.add(eastPanel, BorderLayout.EAST);
	
		this.setContentPane(newContentPanel);
		this.setVisible(true);
	}
	

	public static Menus getInstance() { return instance ;}
	
	public MyCanvas getMainCanvas() { return mainCanvas ;}
	
	public MenuStructure getMenuStructure() { return menuStructure ;}

	public NorthPanel getNorthPanel() { return northPanel ;}

	public MainPanel getMainPanel() { return mainPanel ;}

	public EastPanel getEastPanel() { return eastPanel ;}

	public WestPanel getWestPanel() { return westPanel ;}

	public MenuAnalysis getMenuAnalysis() { return analysisMenu ;}

	public SaveLoadFile getSaveLoadFile() { return new SaveLoadFile((JFrame) getParent(), frameTopLeft) ;}
	
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
		boolean AnalysisIsComplete = MenuFunctions.AnalysisIsComplete;
		MenuStructure.updateEnabledSubMenus() ;
		if (AnalysisIsComplete)
		{
			northPanel.getUpperToolbar().enableButtonsScale() ;
			viewMenu.enableDofNumberView() ;
			resultsMenu.enableButtons() ;
		}
	}
	
	public void DisableButtons()
	{
		viewMenu.disableDofNumberView() ;
		resultsMenu.disableButtons() ;
	}

	public void ActivatePostAnalysisView(Structure structure)
	{
		if (((Double)structure.getU()[0]).isNaN()) { System.out.println("Error: Trying to activate post analysis view with displacement NaN") ; return ;}
		
		eastPanel.getLegendPanel().setStructure(structure) ;
		eastPanel.activatePostAnalysisView() ;
		westPanel.getToolbarResults().setVisible(true);
		EnableButtons();
		repaint();
	}

}