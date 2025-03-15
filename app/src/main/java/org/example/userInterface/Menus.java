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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.example.mainTCC.Analysis;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.Element;
import org.example.structure.MyCanvas;
import org.example.structure.Node;
import org.example.structure.Structure;
import org.example.utilidades.Util;
import org.example.view.DiagramsPanel;
import org.example.view.LegendPanel;
import org.example.view.ListPanel;
import org.example.view.MainPanel;
import org.example.view.NorthPanel;

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
	
	public static final Dimension defaultPanelSize = new Dimension(260, 300);
	private static final Dimension initialSize = new Dimension(1084, 700) ;
	
	private static MyCanvas MainCanvas ;

	/* panel variables */
	private final ToolbarButtons toolbarButtons = new ToolbarButtons() ;
	private final ToolbarResults toolbarResults = new ToolbarResults() ;
	private final InstructionsPanel instructionsPanel = new InstructionsPanel() ;
	private final NorthPanel northPanel = new NorthPanel() ;
	JPanel E1, W1;
	JPanel mousePanel;
	JPanel bp1, bp2, bp3;
	JPanel LDpanel;
	
	private MainPanel mainPanel;
	private LegendPanel legendPanel;
	private DiagramsPanel diagramsPanel;
	private ListPanel listsPanel ;

	JMenuBar menuBar;
	private MenuStructure menuStructure ;
    public JMenu ViewMenu, AnalysisMenu, ResultsMenu, EspecialMenu;
	JMenuItem CreateMesh, CreateNodes, CreateMaterials, CreateSections, CreateConcLoads, CreateDistLoads, CreateNodalDisp, AssignMaterials, AssignSections, AssignSupports, AssignConcLoads, AssignDistLoads, AssignNodalDisp;	
	JMenuItem DOFNumberView, NodeNumberView, ElemNumberView, MatView, SecView, NodeView, ElemView, ElemContourView, SupView, LoadsValuesView, ConcLoadsView, DistLoadsView, NodalDispsView, ReactionsView;

	JMenuItem ReactionValues, ReactionArrows;
	JMenuItem RunAnalysis;
	JMenuItem DisplacementContours, DeformedShape, StressContours, StrainContours, InternalForcesContours, SaveResults, SaveLoadDispCurve;
	JMenuItem[] SubMenuDisp;				// ux, uy, uz, tetax, tetay, tetaz
	JMenuItem[] SubMenuStresses;			// Sigmax, Sigmay, Sigmaz, Taux, Tauy, Tauz
	JMenuItem[] SubMenuStrains;				// ex, ey, ez, gxy, gxz, gyz
	JMenuItem[] SubMenuInternalForces;		// Fx, Fy, Fz, Mx, My, Mz
	JMenuItem Star;
	
	boolean ShowElems, ShowReactionArrows, ShowReactionValues, ShowLoadsValues;
    boolean ShowCanvas, ShowGrid, ShowMousePos;
    boolean ReadyForAnalysis;
	
	public static final Point frameTopLeft;
	public static final Color[] palette  ;
	private static final Menus instance ;

	static
	{
		frameTopLeft = new Point(150, 50);

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
		
		mainPanel = new MainPanel(frameTopLeft) ;
		legendPanel = new LegendPanel();
		diagramsPanel = new DiagramsPanel();
		listsPanel = new ListPanel() ;

		setLocation(frameTopLeft);

	    int[] ScreenTopLeft = new int[] {0, 0, 0};				// Initial coordinates from the top left of the canvas window 900 720
		int[] mainCanvasSize = new int[] {(int) (0.4 * mainPanel.getSize().getWidth()), (int) (0.8 * mainPanel.getSize().getHeight()), 0} ;
	    MainCanvas = new MyCanvas (new Point(575, 25), mainCanvasSize, new double[] {10, 10, 0}, ScreenTopLeft);	    

		SubMenuDisp = new JMenuItem[6];				// ux, uy, uz, tetax, tetay, tetaz
		SubMenuStresses = new JMenuItem[6];			// Sigmax, Sigmay, Sigmaz, Taux, Tauy, Tauz
		SubMenuStrains = new JMenuItem[6];			// ex, ey, ez, gxy, gxz, gyz
		SubMenuInternalForces = new JMenuItem[6];	// Fx, Fy, Fz, Mx, My, Mz


		AddMenus();
		setJMenuBar(menuBar);
		setTitle("TCC");				// Super frame sets its title
		setPreferredSize(initialSize) ;		// Super frame sets its size
		setVisible(true);					// Super frame gets into the show
		pack();
		/* Super frame sets its everything. Super frame is so independent! =,) */

		JPanel newContentPanel = new JPanel(new GridBagLayout());
		BorderLayout bl = new BorderLayout();
		newContentPanel.setLayout(bl);

		/* West panels */
		W1 = new JPanel(new GridLayout(0, 1));		
		W1.add(toolbarButtons);
		W1.add(toolbarResults);
		W1.add(listsPanel);
		W1.add(instructionsPanel);

		/* East panels */
		E1 = new JPanel(new GridLayout(0, 1));
		bp1 = stdPanel(defaultPanelSize, palette[2]);
		bp2 = stdPanel(defaultPanelSize, palette[2]);
		bp3 = stdPanel(defaultPanelSize, palette[2]);
		LDpanel = stdPanel(defaultPanelSize, palette[2]);
		bp1.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		bp2.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		bp3.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		LDpanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		E1.add(bp1);
		E1.add(bp2);
		E1.add(bp3);
		E1.add(legendPanel) ;
		E1.add(LDpanel);
		
		newContentPanel.add(northPanel, BorderLayout.NORTH);
		newContentPanel.add(mainPanel, BorderLayout.CENTER);
		newContentPanel.add(W1, BorderLayout.WEST);
		newContentPanel.add(E1, BorderLayout.EAST);
	
		this.setContentPane(newContentPanel);
		this.setVisible(true);
	}
	

	public static Menus getInstance() { return instance ;}
	
	public static MyCanvas getMainCanvas() { return MainCanvas ;}
	
	public MenuStructure getMenuStructure() { return menuStructure ;}

	public NorthPanel getNorthPanel() { return northPanel ;}

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
	
	private JPanel createNodeInfoPanel(Node Node)
	{
		JPanel NodeInfoPanel = new JPanel(new GridLayout(0,1));
		Color TextColor = palette[4];
		NodeInfoPanel.setBackground(palette[9]);
		NodeInfoPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[5]));
		NodeInfoPanel.setSize(defaultPanelSize);		

		String OriginalCoords = "", DeformedCoords = "";
		String ConcLoads = String.valueOf(0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0);
		OriginalCoords += String.valueOf(Util.Round(Node.getOriginalCoords().x, 2)) + "," ;
		OriginalCoords += String.valueOf(Util.Round(Node.getOriginalCoords().y, 2)) + "," ;
		OriginalCoords += String.valueOf(Util.Round(Node.getOriginalCoords().z, 2)) ;
		DeformedCoords += String.valueOf(Util.Round(Node.getDisp()[0], 2)) + "," ;
		DeformedCoords += String.valueOf(Util.Round(Node.getDisp()[1], 2)) + "," ;
		DeformedCoords += String.valueOf(Util.Round(Node.getDisp()[2], 2)) + "," ;
		if (Node.getConcLoads() != null)
		{
			ConcLoads = "";
			for (int load = 0; load <= Node.getConcLoads().length - 1; load += 1)
			{
				for (int dof = 0; dof <= 6 - 1; dof += 1)
				{
					ConcLoads += String.valueOf(Util.Round(Node.getConcLoads()[load].getLoads()[dof], 2) + ", ");
				}
			}
		}
		
		JLabel iLabel = new JLabel("Informaçõs do nâ");
		NodeInfoPanel.add(iLabel);
		iLabel.setForeground(TextColor);
		
		JLabel[] iInfo = new JLabel[4];
		iInfo[0] = new JLabel(" Nâ: " + String.valueOf(Node.getID()));
		iInfo[1] = new JLabel(" Original pos: " + OriginalCoords);
		iInfo[2] = new JLabel(" Deslocamentos: " + DeformedCoords);
		iInfo[3] = new JLabel(" Forâas: " + ConcLoads);
		for (int i = 0; i <= iInfo.length - 1; i += 1)
		{
			NodeInfoPanel.add(iInfo[i]);
		}
		
		return NodeInfoPanel;
	}
	
	private JPanel createElemInfoPanel(Element elem)
	{
		JPanel ElemInfoPanel = new JPanel(new GridLayout(0,1));
		Color TextColor = palette[4];
		
		ElemInfoPanel.setBackground(palette[9]);
		ElemInfoPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[5]));
		ElemInfoPanel.setSize(defaultPanelSize);		

		String NodesText = "";
		for (int node = 0; node <= elem.getExternalNodes().length - 1; node += 1)
		{
			NodesText += String.valueOf(elem.getExternalNodes()[node] + " ");
		}
		String MatText = null;
		if (elem.getMat() != null)
		{
			MatText = String.valueOf(Util.Round(elem.getMat().getE(), 2)) + " MPa v = " + String.valueOf(Util.Round(elem.getMat().getV(), 1)) + " G = " + String.valueOf(Util.Round(elem.getMat().getG(), 2)) + " Mpa";
		}
		String SecText = null;
		if (elem.getSec() != null)
		{
			SecText = String.valueOf(Util.Round(elem.getSec().getT(), 0)) + " mm";
		}
		
		JLabel iLabel = new JLabel("Informaçõs do elemento");
		ElemInfoPanel.add(iLabel);
		iLabel.setForeground(TextColor);
		
		JLabel[] iInfo = new JLabel[4];
		iInfo[0] = new JLabel(" Elem: " + String.valueOf(elem.getID()));
		iInfo[1] = new JLabel(" Nâs: " + NodesText);
		iInfo[2] = new JLabel(" Material: E = " + MatText);
		iInfo[3] = new JLabel(" Seââo: t = " + SecText);
		
		for (int i = 0; i <= iInfo.length - 1; i += 1)
		{
			ElemInfoPanel.add(iInfo[i]);
		}
		
		return ElemInfoPanel;
	}
	
	public void ResetEastPanels()
	{
		if (MenuFunctions.selectedNodes != null)
		{
			int nodeID = MenuFunctions.selectedNodes.get(0).getID() ;
			if (-1 < nodeID)
			{
				bp1 = createNodeInfoPanel(MainPanel.structure.getMesh().getNodes().get(nodeID)) ;
			}
		}
		if (MenuFunctions.SelectedElems != null)
		{
			int elemID = MenuFunctions.SelectedElems[0] ;
			if (-1 < elemID)
			{
				bp2 = createElemInfoPanel(MainPanel.structure.getMesh().getElements().get(elemID)) ;
			}
		}
		E1.removeAll();
		E1.add(bp1);
		E1.add(bp2);
		E1.add(bp3);
		E1.add(LDpanel);
		repaint();
	}
		
	public void EnableButtons()
	{
		Structure structure = MainPanel.structure;
		// List<Node> nodes = MainPanel.structure.getMesh() != null ? MainPanel.structure.getMesh().getNodes() : null ;
		List<Element> elems = MainPanel.structure.getMesh() != null ? MainPanel.structure.getMesh().getElements() : null;
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
			DOFNumberView.setEnabled(true);
			DeformedShape.setEnabled(true);
			DisplacementContours.setEnabled(true);
			StressContours.setEnabled(true);
			StrainContours.setEnabled(true);
			InternalForcesContours.setEnabled(true);
			northPanel.getUpperToolbar().enableButtonsScale() ;
			for (int i = 0; i <= elems.get(0).getStrainTypes().length - 1; i += 1)
			{
				if (elems.get(0).getStrainTypes()[i] <= 5)
				{
					SubMenuStresses[elems.get(0).getStrainTypes()[i]].setEnabled(true);
					SubMenuStrains[elems.get(0).getStrainTypes()[i]].setEnabled(true);
				}
			}
			SaveResults.setEnabled(true);
			SaveLoadDispCurve.setEnabled(true);
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
		DOFNumberView.setEnabled(false);
		DeformedShape.setEnabled(false);
		DisplacementContours.setEnabled(false);
		StressContours.setEnabled(false);
		StrainContours.setEnabled(false);
		InternalForcesContours.setEnabled(false);
		for (int i = 0; i <= SubMenuDisp.length - 1; i += 1)
		{
			SubMenuDisp[i].setEnabled(false);
			SubMenuInternalForces[i].setEnabled(false);
		}
		for (int i = 0; i <= SubMenuStresses.length - 1; i += 1)
		{
			SubMenuStresses[i].setEnabled(false);
			SubMenuStrains[i].setEnabled(false);
		}
		SaveResults.setEnabled(false);
	}

	public void ActivatePostAnalysisView()
	{
		if (!((Double)MainPanel.structure.getU()[0]).isNaN())
		{
			E1.remove(LDpanel);
			E1.remove(bp3);
			LDpanel = diagramsPanel;
			bp3 = legendPanel;
			E1.add(bp3);
			E1.add(LDpanel);
			toolbarResults.setVisible(true);
			EnableButtons();
		}
	}
	
	
	public void AddMenus()
	{
		/* Defining menu bars */
		String[] MenuNames = new String[] {
			    "Arquivo",
			    "Estrutura",
			    "Visual",
			    "Analise",
			    "Resultados",
			    "Especial"
			};	
		menuBar = new JMenuBar();
		menuStructure = new MenuStructure();
		ViewMenu = new JMenu(MenuNames[2]);			// View
		AnalysisMenu = new JMenu(MenuNames[3]);		// Analysis
		ResultsMenu = new JMenu(MenuNames[4]);		// Results
		EspecialMenu = new JMenu(MenuNames[5]);		// Especial

		ViewMenu.setMnemonic(KeyEvent.VK_V);
		ResultsMenu.setMnemonic(KeyEvent.VK_R);
		EspecialMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(MenuFile.create());
		menuBar.add(menuStructure);
		menuBar.add(ViewMenu);
		menuBar.add(AnalysisMenu);
		menuBar.add(ResultsMenu);
		menuBar.add(EspecialMenu);
		AddViewMenuItems();
		AddAnalysisMenuItems();
		AddResultsMenuItems();
		AddEspecialMenuItems();
	}


	public void AddViewMenuItems()
	{
		/* Defining items in the menu View */
	    String[] ViewMenuItemsNames = new String[] {
			    "Nos",
			    "Elementos",
			    "Graus de liberdade",
			    "Numeros dos nos",
			    "Numeros dos elementos",
			    "Materiais",
			    "Secoes",
			    "Contorno dos elementos",
			    "Apoios",
			    "Cargas concentradas",
			    "Cargas distribuidas",
			    "Deslocamentos nodais",
			    "Valores das cargas",
			    "Reacoes"
			};
		NodeView = new JMenuItem(ViewMenuItemsNames[0]);
		ElemView = new JMenuItem(ViewMenuItemsNames[1]);
		DOFNumberView = new JMenuItem(ViewMenuItemsNames[2], KeyEvent.VK_D);
		NodeNumberView = new JMenuItem(ViewMenuItemsNames[3], KeyEvent.VK_N);
		ElemNumberView = new JMenuItem(ViewMenuItemsNames[4], KeyEvent.VK_E);
		MatView = new JMenuItem(ViewMenuItemsNames[5], KeyEvent.VK_M);
		SecView = new JMenuItem(ViewMenuItemsNames[6], KeyEvent.VK_S);
		ElemContourView = new JMenuItem(ViewMenuItemsNames[7]);
		SupView = new JMenuItem(ViewMenuItemsNames[8]);
		ConcLoadsView = new JMenuItem(ViewMenuItemsNames[9], KeyEvent.VK_C);
		DistLoadsView = new JMenuItem(ViewMenuItemsNames[10]);
		NodalDispsView = new JMenuItem(ViewMenuItemsNames[11]);
		LoadsValuesView = new JMenuItem(ViewMenuItemsNames[12]);
		ReactionsView = new JMenu(ViewMenuItemsNames[13]);
		DOFNumberView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowDOFNumber = !ShowDOFNumber;
				MenuFunctions.DOFNumberView();
			}
		});
		NodeNumberView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowNodeNumber = !ShowNodeNumber;
				MenuFunctions.NodeNumberView();
			}
		});
		ElemNumberView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowElemNumber = !ShowElemNumber;
				MenuFunctions.ElemNumberView();
			}
		});
		MatView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowMatColor = !ShowMatColor;
				MenuFunctions.MatView();
			}
		});
		SecView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowSecColor = !ShowSecColor;
				MenuFunctions.SecView();
			}
		});
		NodeView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowNodes = !ShowNodes;
				MenuFunctions.NodeView();
			}
		});
		ElemView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowElems = !ShowElems;
				MenuFunctions.ElemView();
			}
		});
		ElemContourView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowElemContour = !ShowElemContour;
				MenuFunctions.ElemContourView();
			}
		});
		SupView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowSup = !ShowSup;
				MenuFunctions.SupView();
			}
		});
		LoadsValuesView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowLoadsValues = !ShowLoadsValues;
				MenuFunctions.LoadsValuesView();
			}
		});
		ConcLoadsView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowConcLoads = !ShowConcLoads;
				MenuFunctions.ConcLoadsView();
			}
		});
		DistLoadsView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowDistLoads = !ShowDistLoads;
				MenuFunctions.DistLoadsView();
			}
		});
		NodalDispsView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowNodalDisps = !ShowNodalDisps;
				MenuFunctions.NodalDispsView();
			}
		});
		DOFNumberView.setForeground(palette[5]);
		NodeNumberView.setForeground(palette[5]);
		ElemNumberView.setForeground(palette[5]);
		MatView.setForeground(palette[5]);
		SecView.setForeground(palette[5]);
		NodeView.setForeground(palette[5]);
		ElemView.setForeground(palette[5]);
		ElemContourView.setForeground(palette[5]);
		SupView.setForeground(palette[5]);
		ConcLoadsView.setForeground(palette[5]);
		DistLoadsView.setForeground(palette[5]);
		NodalDispsView.setForeground(palette[5]);
		LoadsValuesView.setForeground(palette[5]);
		ReactionsView.setForeground(palette[5]);
		DOFNumberView.setEnabled(false);
		ViewMenu.add(DOFNumberView);
		ViewMenu.add(NodeNumberView);
		ViewMenu.add(ElemNumberView);
		ViewMenu.add(MatView);
		ViewMenu.add(SecView);
		ViewMenu.add(NodeView);
		ViewMenu.add(ElemView);
		ViewMenu.add(ElemContourView);
		ViewMenu.add(SupView);
		ViewMenu.add(ConcLoadsView);
		ViewMenu.add(DistLoadsView);
		ViewMenu.add(NodalDispsView);
		ViewMenu.add(LoadsValuesView);
		ViewMenu.add(ReactionsView);

		/* Defining subitems in the menu ReactionsView */

	    String[] ReactionsViewMenuNames = new String[] {"Desenhos", "Valores"};
		ReactionArrows = new JMenuItem(ReactionsViewMenuNames[0], KeyEvent.VK_C);
		ReactionValues = new JMenuItem(ReactionsViewMenuNames[1], KeyEvent.VK_C);
		ReactionArrows.setForeground(palette[5]);
		ReactionValues.setForeground(palette[5]);
		ReactionArrows.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				ShowReactionArrows = !ShowReactionArrows;
				repaint();
			}
		});
		ReactionValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				ShowReactionValues = !ShowReactionValues;
				repaint();
			}
		});
		ReactionsView.add(ReactionArrows);
		ReactionsView.add(ReactionValues);
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
	
	public void AddResultsMenuItems()
	{
		/* Defining items in the menu Results */
	    String[] ResultsMenuItemsNames = new String[] {
	    	    "Estrutura deformada",
	    	    "Deslocamentos",
	    	    "Tensoes",
	    	    "Deformacoes",
	    	    "Forcas internas",
	    	    "Salvar resultados",
	    	    "Salvar curva carga-desl"
	    	};
		DeformedShape = new JMenuItem(ResultsMenuItemsNames[0], KeyEvent.VK_D);
		DisplacementContours = new JMenu(ResultsMenuItemsNames[1]);
		StressContours = new JMenu(ResultsMenuItemsNames[2]);
		StrainContours = new JMenu(ResultsMenuItemsNames[3]);
		InternalForcesContours  = new JMenu(ResultsMenuItemsNames[4]);
		SaveResults = new JMenuItem(ResultsMenuItemsNames[5], KeyEvent.VK_S);
		SaveLoadDispCurve = new JMenuItem(ResultsMenuItemsNames[6]);
		DeformedShape.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.DeformedStructureView();
			}
		});
		/*DisplacementContours.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				SelectedDiagram = 0;
				SelectedVar = 2;
				DrawDisplacementContours(Util.ElemPosInArray(DOFsPerNode, 2), DOFsPerNode);
				MainCanvas.setTitle("Deslocamentos (x" + String.valueOf(DiagramsScales[1]) + ")");
			}
		});*/
		SaveResults.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.ResultsMenuSaveResults(MainPanel.structure);
			}
		});
		SaveLoadDispCurve.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.SaveLoadDispCurve(MainPanel.structure);
			}
		});
		DeformedShape.setEnabled(false);
		DisplacementContours.setEnabled(false);
		StressContours.setEnabled(false);
		StrainContours.setEnabled(false);
		InternalForcesContours.setEnabled(false);
		SaveResults.setEnabled(false);
		SaveLoadDispCurve.setEnabled(false);
		DeformedShape.setForeground(palette[7]);
		DisplacementContours.setForeground(palette[7]);
		StressContours.setForeground(palette[7]);
		StrainContours.setForeground(palette[7]);
		InternalForcesContours.setForeground(palette[7]);
		SaveResults.setForeground(palette[7]);
		SaveLoadDispCurve.setForeground(palette[7]);
		ResultsMenu.add(DeformedShape);
		ResultsMenu.add(DisplacementContours);
		ResultsMenu.add(StressContours);
		ResultsMenu.add(StrainContours);
		ResultsMenu.add(InternalForcesContours);
		ResultsMenu.add(SaveResults);
		ResultsMenu.add(SaveLoadDispCurve);
		

		/* Defining subitems in the menu DisplacementContour */
		for (int d = 0; d <= SubMenuDisp.length - 1; d += 1)
		{
			int d2 = d;
			String[] DisplacementContourMenuNames = new String[] {
				    "ux",
				    "uy",
				    "uz",
				    "tetax",
				    "tetay",
				    "tetaz"
				};
			SubMenuDisp[d] = new JMenuItem(DisplacementContourMenuNames[d]);
			SubMenuDisp[d].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 0;
					MainPanel.SelectedVar = Util.ElemPosInArray(MainPanel.structure.getMesh().getElements().get(0).getDOFs(), d2);
					ShowElems = false;
					//DrawDisplacementContours(SelectedVar);
					//ResetE1Panel();
				}
			});
			SubMenuDisp[d].setEnabled(false);
			SubMenuDisp[d].setForeground(palette[7]);
			DisplacementContours.add(SubMenuDisp[d]);
		}
		
		/* Defining subitems in the menu StressContours */
		for (int s = 0; s <= SubMenuStresses.length - 1; s += 1)
		{
			int s2 = s;
			String[] StressContoursMenuNames = new String[] {
				    "Sigma x",
				    "Sigma y",
				    "Sigma z",
				    "Tau xy",
				    "Tau xz",
				    "Tau yz"
				};
			SubMenuStresses[s] = new JMenuItem(StressContoursMenuNames[s]);
			SubMenuStresses[s].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 1;
					MainPanel.SelectedVar = Util.ElemPosInArray(MainPanel.structure.getMesh().getElements().get(0).getDOFs(), s2);
					ShowElems = false;
					//DrawStressContours(SelectedVar);
					//ResetE1Panel();
				}
			});
			SubMenuStresses[s].setEnabled(false);
			SubMenuStresses[s].setForeground(palette[7]);
			StressContours.add(SubMenuStresses[s]);
		}
		
		/* Defining subitems in the menu StrainContours */
		for (int s = 0; s <= SubMenuStrains.length - 1; s += 1)
		{
			int s2 = s;
			String[] StrainContoursMenuNames = new String[] {
				    "Deformacao x",
				    "Deformacao y",
				    "Deformacao z",
				    "Deformacao xy",
				    "Deformacao xz",
				    "Deformacao yz"
				};
			SubMenuStrains[s] = new JMenuItem(StrainContoursMenuNames[s]);
			SubMenuStrains[s].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 2;
					MainPanel.SelectedVar = Util.ElemPosInArray(MainPanel.structure.getMesh().getElements().get(0).getDOFs(), s2);
					ShowElems = false;
					//DrawStrainContours(SelectedVar);
					//ResetE1Panel();
				}
			});
			SubMenuStrains[s].setEnabled(false);
			SubMenuStrains[s].setForeground(palette[7]);
			StrainContours.add(SubMenuStrains[s]);
		}
		
		/* Defining subitems in the menu InternalForcesContours */
		for (int f = 0; f <= SubMenuInternalForces.length - 1; f += 1)
		{
			int f2 = f;
			String[] InternalForcesContoursMenuNames = new String[] {
				    "Fx",
				    "Fy",
				    "Fz",
				    "Mx",
				    "My",
				    "Mz"
				} ;
			SubMenuInternalForces[f] = new JMenuItem(InternalForcesContoursMenuNames[f]);
			SubMenuInternalForces[f].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 3;
					MainPanel.SelectedVar = Util.ElemPosInArray(MainPanel.structure.getMesh().getElements().get(0).getDOFs(), f2);
					ShowElems = false;
					//DrawInternalForcesContours(Util.ElemPosInArray(Elem[0].getDOFs(), f2));
					//ResetE1Panel();
				}
			});
			SubMenuInternalForces[f].setEnabled(false);
			SubMenuInternalForces[f].setForeground(palette[7]);
			InternalForcesContours.add(SubMenuInternalForces[f]);
		}
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
	
	public void showCanvasOn()
	{
		ShowCanvas = true ;
	}

	public void showGrid()
	{
		ShowGrid = true ;
	}

	public void showMousePos()
	{
		ShowMousePos = true ;
	}


    public InstructionsPanel getInstructionsPanel()
	{
		return instructionsPanel;
    }

}