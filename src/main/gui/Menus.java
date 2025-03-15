package main.gui;
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
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import main.mainTCC.Analysis;
import main.mainTCC.MenuFunctions;
import main.structure.ElemType;
import main.structure.Element;
import main.structure.Material;
import main.structure.MeshType;
import main.structure.MyCanvas;
import main.structure.Node;
import main.structure.Section;
import main.structure.Structure;
import main.structure.StructureShape;
import main.utilidades.Point3D;
import main.utilidades.Util;
import main.view.DiagramsPanel;
import main.view.LegendPanel;
import main.view.ListPanel;
import main.view.MainPanel;

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

	public static final int buttonSize = 32 ;
	
	public static final Dimension defaultPanelSize = new Dimension(260, 300);
	private static final Dimension initialSize = new Dimension(1084, 700) ;
	
	private static MyCanvas MainCanvas ;

	/* panel variables */
	private final ToolbarButtons toolbarButtons = new ToolbarButtons() ;
	private final ToolbarResults toolbarResults = new ToolbarResults() ;
	private final InstructionsPanel instructionsPanel = new InstructionsPanel() ;
	JPanel N1, E1, W1;
	JPanel mousePanel;
	JPanel bp1, bp2, bp3;
	JPanel LDpanel;
	
	private MainPanel mainPanel;
	private LegendPanel legendPanel;
	private DiagramsPanel diagramsPanel;
	private ListPanel listsPanel ;

	JMenuBar menuBar;
    public JMenu StructureMenu, ViewMenu, AnalysisMenu, ResultsMenu, EspecialMenu;
	JMenuItem DefineElemType, CreateMesh, CreateNodes, CreateMaterials, CreateSections, CreateConcLoads, CreateDistLoads, CreateNodalDisp, AssignMaterials, AssignSections, AssignSupports, AssignConcLoads, AssignDistLoads, AssignNodalDisp;	
	JMenuItem TypeNodes, ClickNodes;
	JMenuItem DOFNumberView, NodeNumberView, ElemNumberView, MatView, SecView, NodeView, ElemView, ElemContourView, SupView, LoadsValuesView, ConcLoadsView, DistLoadsView, NodalDispsView, ReactionsView;

	JMenuItem CartesianMesh, RadialMesh;
	JMenuItem ReactionValues, ReactionArrows;
	JMenuItem RunAnalysis;
	JMenuItem DisplacementContours, DeformedShape, StressContours, StrainContours, InternalForcesContours, SaveResults, SaveLoadDispCurve;
	JMenuItem[] SubMenuDisp;				// ux, uy, uz, tetax, tetay, tetaz
	JMenuItem[] SubMenuStresses;			// Sigmax, Sigmay, Sigmaz, Taux, Tauy, Tauz
	JMenuItem[] SubMenuStrains;				// ex, ey, ez, gxy, gxz, gyz
	JMenuItem[] SubMenuInternalForces;		// Fx, Fy, Fz, Mx, My, Mz
	JMenuItem Star;
	
	public JButton[] UpperToolbarButton;	// 0: Ligar âmâ, 1: Desligar âmâ, 2: Atribuir material, 3: Atribuir seââo, 4: Atribuir apoios, 5: Atribuir cargas conc, 6: Atribuir cargas dist, 7: Atribuir desl nodais, 8: +escala, 9: -escala

	boolean ShowElems, ShowReactionArrows, ShowReactionValues, ShowLoadsValues;
    boolean ShowCanvas, ShowGrid, ShowMousePos;
    public boolean MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn;
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

		/* North panels */
		N1 = new JPanel(new GridBagLayout());
		JPanel utb = createUpperToolBar();
		JPanel bp1 = stdPanel(new Dimension(7 * 32 + 4, 30), palette[2]);
		JPanel bp2 = stdPanel(new Dimension(260, 30), palette[2]);
		N1.add(bp1);
		N1.add(utb);
		N1.add(bp2);

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
		
		newContentPanel.add(N1, BorderLayout.NORTH);
		newContentPanel.add(mainPanel, BorderLayout.CENTER);
		newContentPanel.add(W1, BorderLayout.WEST);
		newContentPanel.add(E1, BorderLayout.EAST);
	
		this.setContentPane(newContentPanel);
		this.setVisible(true);
	}
	

	public static Menus getInstance() { return instance ;}
	
	public static MyCanvas getMainCanvas() { return MainCanvas ;}
	
	public SaveLoadFile getSaveLoadFile() { return new SaveLoadFile((JFrame) getParent(), frameTopLeft) ;}
	
	public boolean[] getAqueleBooleanGrande() { return new boolean[] {MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn} ;}

	public void setRunAnalysis(boolean state) { RunAnalysis.setEnabled(state) ;}
	
	
	private JPanel createUpperToolBar()
	{
		JPanel uToolbarPanel = new JPanel();
		uToolbarPanel.setLayout(new GridBagLayout());
		uToolbarPanel.setBackground(palette[2]);
		uToolbarPanel.setPreferredSize(new Dimension(580, 30));

		String[] ButtonNames = new String[] {
			    "Ligar ima",
			    "Desligar ima",
			    "Atribuir aos elementos",
			    "Atribuir aos nos",
			    "+escala",
			    "-escala",
			    "Concluir",
			    "Limpar"
			};
		UpperToolbarButton = new JButton[ButtonNames.length];
		int[] ButtonLength = new int[] {62, 80, 138, 100, 50, 52, 50, 50};
		Color ButtonBgColor = palette[8];
		
		for (int b = 0; b <= UpperToolbarButton.length - 1; b += 1)
		{
			UpperToolbarButton[b] = ToolbarButtons.AddButton(ButtonNames[b], new int[2], new int[] {ButtonLength[b], 30}, 11, new int[] {2, 2, 2, 2}, ButtonBgColor);
			UpperToolbarButton[b].setEnabled(false);
			UpperToolbarButton[b].setVisible(false);
			UpperToolbarButton[b].setFocusable(false);
		}
		
		/* Buttons: 
		 * 0: snip to grid on
		 * 1: snip to grid off
		 * 2: add materials, sections and  dist loads to elements
		 * 3: add supports, concentrated loads and nodal displacements to nodes
		 * 4: increase diagrams scale
		 * 5: decrease diagrams scale
		 * 6: done
		 * 7: clean
		 * */
		
		UpperToolbarButton[0].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.SnipToGridIsOn = true;
				UpperToolbarButton[0].setEnabled(false);
				UpperToolbarButton[0].setVisible(false);
				UpperToolbarButton[1].setEnabled(true);
				UpperToolbarButton[1].setVisible(true);
			}
		});
		UpperToolbarButton[1].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.SnipToGridIsOn = false;
				UpperToolbarButton[1].setEnabled(false);
				UpperToolbarButton[1].setVisible(false);
				UpperToolbarButton[0].setEnabled(true);
				UpperToolbarButton[0].setVisible(true);
			}
		});
		UpperToolbarButton[2].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (MatAssignmentIsOn)
				{
					MainPanel.AddMaterialToElements(MenuFunctions.SelectedElems, MenuFunctions.matTypes.get(MainPanel.selectedMatID));
				}
				if (SecAssignmentIsOn)
				{
					MainPanel.AddSectionsToElements(MenuFunctions.SelectedElems, MenuFunctions.secTypes.get(MainPanel.selectedSecID));
				}
				if (DistLoadsAssignmentIsOn)
				{
					MainPanel.AddDistLoads();
				}
			}
		});
		UpperToolbarButton[3].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (SupAssignmentIsOn)
				{
					MainPanel.AddSupports();					
				}
				if (ConcLoadsAssignmentIsOn)
				{
					MainPanel.AddConcLoads();
				}
				if (NodalDispsAssignmentIsOn)
				{
					MainPanel.AddNodalDisps();
				}
			}
		});
		UpperToolbarButton[4].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.DiagramScales[1] += 0.1*MenuFunctions.DiagramScales[1];
			}
		});
		UpperToolbarButton[5].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.DiagramScales[1] += -0.1*MenuFunctions.DiagramScales[1];
			}
		});
		UpperToolbarButton[6].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (MatAssignmentIsOn | SecAssignmentIsOn | DistLoadsAssignmentIsOn)
				{
					UpperToolbarButton[2].setEnabled(false);
					UpperToolbarButton[2].setVisible(false);
					
					if (MatAssignmentIsOn)
					{
						System.out.println("Mat types at menus: " + MenuFunctions.matTypes);
						Element.createMatColors(MenuFunctions.matTypes);
						for (Element elem : MainPanel.structure.getMesh().getElements())
						{
							int colorID = MenuFunctions.matTypes.indexOf(elem.getMat()) ;
							if (colorID != -1)
							{
								elem.setMatColor(Element.matColors[colorID]);
							}
						}
					}
					if (SecAssignmentIsOn)
					{
						Element.setSecColors(MenuFunctions.secTypes);
						for (Element elem : MainPanel.structure.getMesh().getElements())
						{
							int colorID = MenuFunctions.secTypes.indexOf(elem.getSec()) ;
							elem.setSecColor(Element.SecColors[colorID]);
						}
					}
				}
				if (SupAssignmentIsOn | ConcLoadsAssignmentIsOn | NodalDispsAssignmentIsOn)
				{
					UpperToolbarButton[3].setEnabled(false);
					UpperToolbarButton[3].setVisible(false);
				}
				E1.remove(bp1);
				E1.remove(bp2);
				UpperToolbarButton[6].setEnabled(false);
				UpperToolbarButton[6].setVisible(false);
				UpperToolbarButton[7].setEnabled(false);
				UpperToolbarButton[7].setVisible(false);
				MenuFunctions.selectedNodes = null;
				MenuFunctions.SelectedElems = null;
				MatAssignmentIsOn = false;
				SecAssignmentIsOn = false;
				SupAssignmentIsOn = false;
				ConcLoadsAssignmentIsOn = false;
				DistLoadsAssignmentIsOn = false;
				NodalDispsAssignmentIsOn = false;
				MenuFunctions.NodeSelectionIsOn = false;
				MenuFunctions.ElemSelectionIsOn = false;
				instructionsPanel.updateStepsCompletion() ;
				ReadyForAnalysis = MenuFunctions.CheckIfAnalysisIsReady(MainPanel.structure, MainPanel.loading);
				
				if (ReadyForAnalysis)
				{
					RunAnalysis.setEnabled(true);
				}
			}
		});
		UpperToolbarButton[7].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.Clean(MainPanel.structure, new boolean[] {MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn});
			}
		});
		
		for (int b = 0; b <= UpperToolbarButton.length - 1; b += 1)
		{
			uToolbarPanel.add(UpperToolbarButton[b]);
		}
		
		return uToolbarPanel;
	}
	
	private static JPanel stdPanel(Dimension size, Color bgcolor)
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
		List<Node> nodes = MainPanel.structure.getMesh() != null ? MainPanel.structure.getMesh().getNodes() : null ;
		List<Element> elems = MainPanel.structure.getMesh() != null ? MainPanel.structure.getMesh().getElements() : null;
		boolean AnalysisIsComplete = MenuFunctions.AnalysisIsComplete;
		String SelectedElemType = MenuFunctions.SelectedElemType;
		List<Material> MatTypes = MenuFunctions.matTypes;
		List<Section> SecTypes = MenuFunctions.secTypes;
		double[][] ConcLoadTypes = MenuFunctions.ConcLoadType;
		double[][] DistLoadTypes = MenuFunctions.DistLoadType;
		double[][] NodalDispTypes = MenuFunctions.NodalDispType;
		if ( nodes != null )
		{
			AssignSupports.setEnabled(true);
			if (ConcLoadTypes != null)
			{
				AssignConcLoads.setEnabled(true);
			}
			if (NodalDispTypes != null)
			{
				AssignNodalDisp.setEnabled(true);
			}
		}
		if ( elems != null )
		{
			if (MatTypes != null)
			{
				AssignMaterials.setEnabled(true);
			}
			if (SecTypes != null)
			{
				AssignSections.setEnabled(true);
			}
			if (DistLoadTypes != null)
			{
				AssignDistLoads.setEnabled(true);
			}
		}
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
			UpperToolbarButton[4].setEnabled(true);
			UpperToolbarButton[4].setVisible(true);
			UpperToolbarButton[5].setEnabled(true);
			UpperToolbarButton[5].setVisible(true);
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
		StructureMenu = new JMenu(MenuNames[1]);	// Structure
		ViewMenu = new JMenu(MenuNames[2]);			// View
		AnalysisMenu = new JMenu(MenuNames[3]);		// Analysis
		ResultsMenu = new JMenu(MenuNames[4]);		// Results
		EspecialMenu = new JMenu(MenuNames[5]);		// Especial
		
		StructureMenu.setMnemonic(KeyEvent.VK_S);
		ViewMenu.setMnemonic(KeyEvent.VK_V);
		ResultsMenu.setMnemonic(KeyEvent.VK_R);
		EspecialMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(MenuFile.create());
		menuBar.add(StructureMenu);
		menuBar.add(ViewMenu);
		menuBar.add(AnalysisMenu);
		menuBar.add(ResultsMenu);
		menuBar.add(EspecialMenu);
		AddStructureMenuItems();
		AddViewMenuItems();
		AddAnalysisMenuItems();
		AddResultsMenuItems();
		AddEspecialMenuItems();
	}

	
	
	public void AddStructureMenuItems()
	{
		/* Defining items in the menu Structure */
	    String[] StructureMenuItemsNames = new String[] {
	    	    "Definir tipo dos elementos",
	    	    "Criar nos",
	    	    "Criar malha",
	    	    "Criar materiais",
	    	    "Criar secoes",
	    	    "Criar cargas concentradas",
	    	    "Criar cargas distribuidas",
	    	    "Criar deslocamentos nodais",
	    	    "Colocar materiais",
	    	    "Colocar secoes",
	    	    "Colocar apoios",
	    	    "Colocar cargas concentradas",
	    	    "Colocar cargas distribuidas",
	    	    "Colocar deslocamentos nodais"
	    	};
		DefineElemType = new JMenuItem(StructureMenuItemsNames[0], KeyEvent.VK_E);
		CreateNodes = new JMenu(StructureMenuItemsNames[1]);
		CreateMesh = new JMenu(StructureMenuItemsNames[2]);
		CreateMaterials = new JMenuItem(StructureMenuItemsNames[3], KeyEvent.VK_M);
		CreateSections = new JMenuItem(StructureMenuItemsNames[4], KeyEvent.VK_S);
		CreateConcLoads = new JMenuItem(StructureMenuItemsNames[5], KeyEvent.VK_C);
		CreateDistLoads = new JMenuItem(StructureMenuItemsNames[6], KeyEvent.VK_D);
		CreateNodalDisp = new JMenuItem(StructureMenuItemsNames[7]);
		AssignMaterials = new JMenuItem(StructureMenuItemsNames[8]);
		AssignSections = new JMenuItem(StructureMenuItemsNames[9]);
		AssignSupports = new JMenuItem(StructureMenuItemsNames[10]);
		AssignConcLoads = new JMenuItem(StructureMenuItemsNames[11]);
		AssignDistLoads = new JMenuItem(StructureMenuItemsNames[12]);
		AssignNodalDisp = new JMenuItem(StructureMenuItemsNames[13]);
		
		DefineElemType.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				List<JButton> buttons = new ArrayList<JButton>();
				for (ElemType elemType : ElemType.values())
				{
					JButton newButton = new JButton(elemType.toString()) ;
					newButton.setSize(new Dimension(30, 20)) ;
					newButton.setEnabled(true) ;
					buttons.add(newButton) ;
				}
				InputPanelType2 CIT = new InputPanelType2("Elem types", buttons);
				MainPanel.setElemType(CIT.run());
				System.out.println(MenuFunctions.SelectedElemType);
				instructionsPanel.updateStepsCompletion() ;
				EnableButtons();
			}
		});
		CreateMaterials.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateMaterials();
			}
		});
		CreateSections.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateSections();
			}
		});
		CreateConcLoads.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateConcLoads();
			}
		});
		CreateDistLoads.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateDistLoads();
			}
		});
		CreateNodalDisp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateNodalDisp();
			}
		});
		AssignMaterials.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignMaterials();
			}
		});
		AssignSections.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignSections();
			}
		});
		AssignSupports.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignSupports();
			}
		});
		AssignConcLoads.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignConcLoads();
			}
		});
		AssignDistLoads.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignDistLoads();
			}
		});
		AssignNodalDisp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignNodalDisp();
			}
		});
		
		CreateMesh.setEnabled(false);
		AssignMaterials.setEnabled(false);
		AssignSections.setEnabled(false);
		AssignSupports.setEnabled(false);
		AssignConcLoads.setEnabled(false);
		AssignDistLoads.setEnabled(false);
		AssignNodalDisp.setEnabled(false);
		DefineElemType.setForeground(palette[5]);
		CreateNodes.setForeground(palette[5]);
		CreateMesh.setForeground(palette[5]);
		CreateMaterials.setForeground(palette[5]);
		CreateSections.setForeground(palette[5]);
		CreateConcLoads.setForeground(palette[5]);
		CreateDistLoads.setForeground(palette[5]);
		CreateNodalDisp.setForeground(palette[5]);
		AssignMaterials.setForeground(palette[5]);
		AssignSections.setForeground(palette[5]);
		AssignSupports.setForeground(palette[5]);
		AssignConcLoads.setForeground(palette[5]);
		AssignDistLoads.setForeground(palette[5]);
		AssignNodalDisp.setForeground(palette[5]);
		StructureMenu.add(DefineElemType);
		StructureMenu.add(CreateNodes);
		StructureMenu.add(CreateMesh);
		StructureMenu.add(CreateMaterials);
		StructureMenu.add(CreateSections);
		StructureMenu.add(CreateConcLoads);
		StructureMenu.add(CreateDistLoads);
		StructureMenu.add(CreateNodalDisp);
		StructureMenu.add(AssignMaterials);
		StructureMenu.add(AssignSections);
		StructureMenu.add(AssignSupports);
		StructureMenu.add(AssignConcLoads);
		StructureMenu.add(AssignDistLoads);
		StructureMenu.add(AssignNodalDisp);

		/* Defining subitems in the menu CreateNodes */
		String[] CreateNodesMenuNames = new String[] {"Digitar", "Clicar"};
		TypeNodes = new JMenuItem(CreateNodesMenuNames[0], KeyEvent.VK_C);
		ClickNodes = new JMenuItem(CreateNodesMenuNames[1], KeyEvent.VK_C);
		TypeNodes.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JLabel[] Labels = new JLabel[] {new JLabel ("x (m)"), new JLabel ("y (m)"), new JLabel ("z (m)")};
				InputPanelType1 CI = new InputPanelType1("Coordenadas", "No", Labels, false);
				double[][] StructCoords = CI.retrieveInput();
				System.out.println(Arrays.deepToString(StructCoords));

				if (StructCoords != null)
				{
					EnableButtons();

					List<Point3D> structCoordsAsPoints = new ArrayList<Point3D>() ;
					for (int i = 0 ; i <= StructCoords.length - 1 ; i += 1)
					{
						structCoordsAsPoints.add(new Point3D(StructCoords[i][0], StructCoords[i][1], StructCoords[i][2])) ;
					}

					MainPanel.structure.setCoords(structCoordsAsPoints);
					MainPanel.structure.updateCenter() ;
					MainPanel.structure.updateMinCoords() ;
					MainPanel.structure.updateMaxCoords() ;
					MainCanvas.setDimension(new double[] {1.2 * MainPanel.structure.getMaxCoords().x, 1.2 * MainPanel.structure.getMaxCoords().y, 0});
					MainCanvas.setDrawingPos(new int[2]);
					instructionsPanel.updateStepsCompletion() ;
				}
			}
		});
		ClickNodes.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("ClickNodes");
				List<JButton> Buttons = new ArrayList<>();
				for (int b = 0; b <= Buttons.size() - 1; b += 1)
				{
					Buttons.add(new JButton (StructureShape.values()[b].toString())) ;
				}
				InputPanelType2 CIT = new InputPanelType2("Structure shape", Buttons);
				// String input = CIT.run() ;
				// MainPanel.CreateStructureOnClick(StructureShape.valueOf(input));

				MenuFunctions.SnipToGridIsOn = false;
				UpperToolbarButton[0].setEnabled(true);
				UpperToolbarButton[0].setVisible(true);
				instructionsPanel.updateStepsCompletion() ;
			}
		});
		TypeNodes.setForeground(palette[5]);
		ClickNodes.setForeground(palette[5]);
		CreateNodes.add(ClickNodes);
		CreateNodes.add(TypeNodes);
		
		/* Defining subitems in the menu CreateMesh */
		String[] CreateMeshMenuNames = MeshType.valuesAsString();
		CartesianMesh = new JMenuItem(CreateMeshMenuNames[0], KeyEvent.VK_C);
		RadialMesh = new JMenuItem(CreateMeshMenuNames[1], KeyEvent.VK_R);
		CartesianMesh.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateMesh(MeshType.cartesian);
			}
		});
		RadialMesh.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateMesh(MeshType.radial);
			}
		});
		CartesianMesh.setForeground(palette[5]);
		RadialMesh.setForeground(palette[5]);
		CreateMesh.add(CartesianMesh);
		CreateMesh.add(RadialMesh);		
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

	
	public void StructureMenuCreateMesh(MeshType meshType)
	{
		JLabel[] Labels = new JLabel[2];
		if (meshType.equals(MeshType.cartesian))
		{
			Labels = new JLabel[] {new JLabel ("N° pontos em x"), new JLabel ("N° pontos em y")};
		}
		else if (meshType.equals(MeshType.radial))
		{
			Labels = new JLabel[] {new JLabel ("N° camadas"), new JLabel ("N° pontos por camada")};
		}
		InputPanelType1 CI = new InputPanelType1("Propriedades da malha", "Malha", Labels, false);
		double[][] input = CI.retrieveInput() ;
		int[][] UserDefinedMesh = Util.MatrixDoubleToInt(input);
		
		MainPanel.structure.removeSupports() ;
		MainPanel.loading.clearLoads() ;
		MainPanel.structure.createMesh(meshType, UserDefinedMesh, ElemType.valueOf(MenuFunctions.SelectedElemType.toUpperCase()));
		MenuFunctions.NodeView();
		MenuFunctions.ElemView();
		instructionsPanel.updateStepsCompletion() ;
		EnableButtons();
	}
	
	public void StructureMenuCreateMaterials()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("E (GPa)"), new JLabel ("v"), new JLabel ("fu (MPa)")};
		InputPanelType1 CI = new InputPanelType1("Materials", "Mat", Labels, true);
		double[][] createdMaterials = CI.retrieveInput() ;
		System.out.println("\ncreated materials" + Arrays.deepToString(createdMaterials));
		List<Material> mats = new ArrayList<>() ;
		for (int i = 0 ; i <= createdMaterials.length - 1 ; i += 1)
		{
			mats.add(new Material(createdMaterials[i][0], createdMaterials[i][1], createdMaterials[i][2])) ;
		}
		MainPanel.setMaterials(mats);
		EnableButtons();
	}
	
	public void StructureMenuCreateSections()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("espessura (mm)")};
		InputPanelType1 CI = new InputPanelType1("Cross sections", "Sec", Labels, true);
		double[][] createdSections = CI.retrieveInput() ;
		List<Section> secs = new ArrayList<>() ;
		for (int i = 0 ; i <= createdSections.length - 1 ; i += 1)
		{
			secs.add(new Section(createdSections[i][0])) ;
		}
		MainPanel.setSections(secs);
		EnableButtons();
	}
	
	public void StructureMenuCreateConcLoads()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("Fx (kN)"), new JLabel ("Fy (kN)"), new JLabel ("Fz (kN)"), new JLabel ("Mx (kNm)"), new JLabel ("My (kNm)"), new JLabel ("Mz (kNm)")};
		InputPanelType1 CI = new InputPanelType1("Nodal loads", "Nodal load", Labels, true);
		MainPanel.DefineConcLoadTypes(CI.retrieveInput());
		EnableButtons();
	}
	
	public void StructureMenuCreateDistLoads()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("Load type"), new JLabel ("Load i (kN / kNm)")};
		InputPanelType1 CI = new InputPanelType1("Member loads", "Member load", Labels, true);
		MainPanel.DefineDistLoadTypes(CI.retrieveInput());
		EnableButtons();
	}
	
	public void StructureMenuCreateNodalDisp()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("disp x"), new JLabel ("disp y"), new JLabel ("disp z"), new JLabel ("rot x"), new JLabel ("rot y"), new JLabel ("rot z")};
		InputPanelType1 CI = new InputPanelType1("Nodal displacements", "Nodal disp", Labels, true);
		MainPanel.DefineNodalDispTypes(CI.retrieveInput());
		EnableButtons();
	}

	public void StructureMenuAssignMaterials()
	{
		MatAssignmentIsOn = !MatAssignmentIsOn;
		MainPanel.StructureMenuAssignMaterials();
		UpperToolbarButton[2].setEnabled(MatAssignmentIsOn);
		UpperToolbarButton[2].setVisible(MatAssignmentIsOn);
		UpperToolbarButton[6].setEnabled(MatAssignmentIsOn);
		UpperToolbarButton[6].setVisible(MatAssignmentIsOn);
		UpperToolbarButton[7].setEnabled(MatAssignmentIsOn);
		UpperToolbarButton[7].setVisible(MatAssignmentIsOn);
	}
	
	public void StructureMenuAssignSections()
	{
		SecAssignmentIsOn = !SecAssignmentIsOn;
		MainPanel.StructureMenuAssignSections();
		UpperToolbarButton[2].setEnabled(SecAssignmentIsOn);
		UpperToolbarButton[2].setVisible(SecAssignmentIsOn);
		UpperToolbarButton[6].setEnabled(SecAssignmentIsOn);
		UpperToolbarButton[6].setVisible(SecAssignmentIsOn);
		UpperToolbarButton[7].setEnabled(SecAssignmentIsOn);
		UpperToolbarButton[7].setVisible(SecAssignmentIsOn);
	}
	
	public void StructureMenuAssignSupports()
	{
		SupAssignmentIsOn = !SupAssignmentIsOn;
		MainPanel.StructureMenuAssignSupports();
		UpperToolbarButton[3].setEnabled(SupAssignmentIsOn);
		UpperToolbarButton[3].setVisible(SupAssignmentIsOn);
		UpperToolbarButton[6].setEnabled(SupAssignmentIsOn);
		UpperToolbarButton[6].setVisible(SupAssignmentIsOn);
		UpperToolbarButton[7].setEnabled(SupAssignmentIsOn);
		UpperToolbarButton[7].setVisible(SupAssignmentIsOn);
	}
	
	public void StructureMenuAssignConcLoads()
	{
		ConcLoadsAssignmentIsOn = !ConcLoadsAssignmentIsOn;
		MainPanel.StructureMenuAssignConcLoads();
		UpperToolbarButton[3].setEnabled(ConcLoadsAssignmentIsOn);
		UpperToolbarButton[3].setVisible(ConcLoadsAssignmentIsOn);
		UpperToolbarButton[6].setEnabled(ConcLoadsAssignmentIsOn);
		UpperToolbarButton[6].setVisible(ConcLoadsAssignmentIsOn);
		UpperToolbarButton[7].setEnabled(ConcLoadsAssignmentIsOn);
		UpperToolbarButton[7].setVisible(ConcLoadsAssignmentIsOn);
	}
	
	public void StructureMenuAssignDistLoads()
	{
		DistLoadsAssignmentIsOn = !DistLoadsAssignmentIsOn;
		MainPanel.StructureMenuAssignDistLoads();
		UpperToolbarButton[2].setEnabled(DistLoadsAssignmentIsOn);
		UpperToolbarButton[2].setVisible(DistLoadsAssignmentIsOn);
		UpperToolbarButton[6].setEnabled(DistLoadsAssignmentIsOn);
		UpperToolbarButton[6].setVisible(DistLoadsAssignmentIsOn);
		UpperToolbarButton[7].setEnabled(DistLoadsAssignmentIsOn);
		UpperToolbarButton[7].setVisible(DistLoadsAssignmentIsOn);
	}
	
	public void StructureMenuAssignNodalDisp()
	{
		NodalDispsAssignmentIsOn = !NodalDispsAssignmentIsOn;
		MainPanel.StructureMenuAssignNodalDisps();
		UpperToolbarButton[3].setEnabled(NodalDispsAssignmentIsOn);
		UpperToolbarButton[3].setVisible(NodalDispsAssignmentIsOn);
		UpperToolbarButton[6].setEnabled(NodalDispsAssignmentIsOn);
		UpperToolbarButton[6].setVisible(NodalDispsAssignmentIsOn);
		UpperToolbarButton[7].setEnabled(NodalDispsAssignmentIsOn);
		UpperToolbarButton[7].setVisible(NodalDispsAssignmentIsOn);
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