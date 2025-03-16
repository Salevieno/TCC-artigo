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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.example.mainTCC.Analysis;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.Element;
import org.example.structure.MyCanvas;
import org.example.structure.Structure;
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

	
	private static final String[] menuNames = new String[] {
		"Arquivo",
		"Estrutura",
		"Visual",
		"Analise",
		"Resultados",
		"Especial"
	};	

	private JMenuBar menuBar;
	private MenuStructure menuStructure ;
    private MenuView ViewMenu ;
	private JMenu AnalysisMenu ;
	private MenuResults ResultsMenu ;
	private JMenu EspecialMenu;
	private JMenuItem CreateMesh, AssignMaterials, AssignSections, AssignSupports, AssignConcLoads, AssignDistLoads ;	

	private JMenuItem RunAnalysis;
	private JMenuItem Star;

    private boolean ReadyForAnalysis;
	

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

	    int[] ScreenTopLeft = new int[] {0, 0, 0};				// Initial coordinates from the top left of the canvas window 900 720
		int[] mainCanvasSize = new int[] {(int) (0.4 * mainPanel.getSize().getWidth()), (int) (0.8 * mainPanel.getSize().getHeight()), 0} ;
	    mainCanvas = new MyCanvas (new Point(575, 25), mainCanvasSize, new double[] {10, 10, 0}, ScreenTopLeft);	    



		
		/* Defining menu bars */
		menuBar = new JMenuBar();
		menuStructure = new MenuStructure();
		ViewMenu = new MenuView() ;
		AnalysisMenu = new JMenu(menuNames[3]);		// Analysis
		ResultsMenu = new MenuResults() ;
		EspecialMenu = new JMenu(menuNames[5]);		// Especial

		
		EspecialMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(MenuFile.create());
		menuBar.add(menuStructure);
		menuBar.add(ViewMenu);
		menuBar.add(AnalysisMenu);
		menuBar.add(ResultsMenu);
		menuBar.add(EspecialMenu);
		AddAnalysisMenuItems();
		AddEspecialMenuItems();



		setJMenuBar(menuBar);
		setTitle("TCC");				// Super frame sets its title
		setPreferredSize(initialSize) ;		// Super frame sets its size
		setVisible(true);					// Super frame gets into the show
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

	public EastPanel getEastPanel() { return eastPanel ;}

	public WestPanel getWestPanel() { return westPanel ;}

	public SaveLoadFile getSaveLoadFile() { return new SaveLoadFile((JFrame) getParent(), frameTopLeft) ;}
	
	public void setRunAnalysis(boolean state) { RunAnalysis.setEnabled(state) ;}
	
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
		Structure structure = MainPanel.structure;
		// List<Node> nodes = MainPanel.structure.getMesh() != null ? MainPanel.structure.getMesh().getNodes() : null ;
		boolean AnalysisIsComplete = MenuFunctions.AnalysisIsComplete;
		String SelectedElemType = MenuFunctions.SelectedElemType;
		// List<Material> MatTypes = MenuFunctions.matTypes;
		// List<Section> SecTypes = MenuFunctions.secTypes;
		// double[][] ConcLoadTypes = MenuFunctions.ConcLoadType;
		// double[][] DistLoadTypes = MenuFunctions.DistLoadType;
		// double[][] NodalDispTypes = MenuFunctions.NodalDispType;
		// if ( nodes != null )
		// {
		// 	AssignSupports.setEnabled(true);
		// 	if (ConcLoadTypes != null)
		// 	{
		// 		AssignConcLoads.setEnabled(true);
		// 	}
		// 	if (NodalDispTypes != null)
		// 	{
		// 		AssignNodalDisp.setEnabled(true);
		// 	}
		// }
		// if ( elems != null )
		// {
		// 	if (MatTypes != null)
		// 	{
		// 		AssignMaterials.setEnabled(true);
		// 	}
		// 	if (SecTypes != null)
		// 	{
		// 		AssignSections.setEnabled(true);
		// 	}
		// 	if (DistLoadTypes != null)
		// 	{
		// 		AssignDistLoads.setEnabled(true);
		// 	}
		// }
		menuStructure.updateEnable() ;
		if ( SelectedElemType != null & structure.getCoords() != null)
		{
			CreateMesh.setEnabled(true);
		}
		if (AnalysisIsComplete)
		{
			northPanel.getUpperToolbar().enableButtonsScale() ;
			ViewMenu.enableDofNumberView() ;
			ResultsMenu.enableButtons() ;
		}
	}
	
	public void DisableButtons()
	{
		AssignSupports.setEnabled(false);
		AssignConcLoads.setEnabled(false);
		AssignDistLoads.setEnabled(false);
		AssignMaterials.setEnabled(false);
		AssignSections.setEnabled(false);
		AssignDistLoads.setEnabled(false);
		CreateMesh.setEnabled(false);
		ViewMenu.disableDofNumberView() ;
		ResultsMenu.disableButtons() ;
	}

	public void ActivatePostAnalysisView()
	{
		if (!((Double)MainPanel.structure.getU()[0]).isNaN())
		{
			eastPanel.activatePostAnalysisView() ;
			westPanel.getToolbarResults().setVisible(true);
			EnableButtons();
			repaint();
		}
	}
	
	public void AddAnalysisMenuItems()
	{
		/* Defining items in the menu Analysis */
	    String[] AnalysisMenuItemsNames = new String[] {"Rodar análise", "Opções"};
		RunAnalysis = new JMenuItem(AnalysisMenuItemsNames[0], KeyEvent.VK_R);
		RunAnalysis.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (ReadyForAnalysis)
				{
					String[] ButtonNames = new String[] {"Linear elâstica", "Geometria nâo-linear", "Material nâo-linear", "Ambos nâo-lineares"};
					List<JButton> Buttons = new ArrayList<>();
					for (int b = 0; b <= Buttons.size() - 1; b += 1)
					{
						Buttons.add(new JButton (ButtonNames[b])) ;
					}
					InputPanelType2 CIT = new InputPanelType2("Analysis types", Buttons);
					String AnalysisType = CIT.run();
					
					int NIter = 1, NLoadSteps = 1;
					double MaxLoadFactor = 1;
					boolean NonlinearGeo = false, NonlinearMat = false;
					if (AnalysisType.equals(ButtonNames[0]))
					{
						NonlinearGeo = false;
						NonlinearMat = false;
					}
					else if (AnalysisType.equals(ButtonNames[1]))
					{
						NonlinearGeo = true;
						NonlinearMat = false;
					}
					else if (AnalysisType.equals(ButtonNames[2]))
					{
						NonlinearGeo = false;
						NonlinearMat = true;
					}
					else if (AnalysisType.equals(ButtonNames[3]))
					{
						NonlinearGeo = true;
						NonlinearMat = true;
					}

					MenuFunctions.CalcAnalysisParameters(MainPanel.structure);
					Analysis.run(MainPanel.structure, MainPanel.loading, MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, NIter, NLoadSteps, MaxLoadFactor);
				    MenuFunctions.PostAnalysis(MainPanel.structure);
					for (Element elem : MainPanel.structure.getMesh().getElements())
					{
				    	elem.RecordResults(MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getU(), NonlinearMat, NonlinearGeo);
					}
			        ActivatePostAnalysisView();
				}
				else
				{
					System.out.println("Structure is not ready for analysis");
				}
			}
		});
		RunAnalysis.setEnabled(false);
		RunAnalysis.setForeground(palette[5]);
		AnalysisMenu.add(RunAnalysis);
	}
	
	public void AddEspecialMenuItems()
	{
		/* Defining items in the menu Especial */
	    String[] EspecialMenuItemsNames = new String[] {"Estrela"};
		Star = new JMenuItem(EspecialMenuItemsNames[0], KeyEvent.VK_S);
		Star.setForeground(palette[7]);
		EspecialMenu.add(Star);
		Star.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MainPanel.structure = MenuFunctions.Especial();
				ActivatePostAnalysisView();
			}
		});
	}

}