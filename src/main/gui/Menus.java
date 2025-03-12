package main.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

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

	/* panel variables */
	JPanel N1, E1, W1;
	JPanel tb1, tb2;
	JPanel ListsPanel;
	JPanel iPanel;
	JPanel utb, mousePanel;
	JPanel bp1, bp2, bp3;
	JPanel LDpanel;
	JPanel jpInstruction;
	
	private MainPanel mainPanel;
	private LegendPanel legendPanel;
	private DiagramsPanel diagramsPanel;
	private ListPanel jpLists;

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
	
	JButton AssignMaterialButton, AssignSectionButton, AssignSupButton, AssignConcLoadButton, AssignDistLoadButton, AssignNodalDispButton;
	public JButton[] UpperToolbarButton;	// 0: Ligar âmâ, 1: Desligar âmâ, 2: Atribuir material, 3: Atribuir seââo, 4: Atribuir apoios, 5: Atribuir cargas conc, 6: Atribuir cargas dist, 7: Atribuir desl nodais, 8: +escala, 9: -escala

	
	public static final int buttonSize = 32 ;
	
	Dimension defaultPanelSize = new Dimension(260, 300);
	private static final Dimension initialSize = new Dimension(1084, 700) ;
	
	/* Global variables */	
	private static MyCanvas MainCanvas ;

	int[] FrameTopLeftPos;
	boolean ShowElems, ShowReactionArrows, ShowReactionValues, ShowLoadsValues;
    boolean ShowCanvas, ShowGrid, ShowMousePos;
    public boolean MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn;
    boolean ReadyForAnalysis;
    public boolean[] StepIsComplete;	
	
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
	public static final Color[] palette = new Color[]		
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
	} ;

	private static final Menus instance = new Menus();
	
	private Menus()
	{
		FrameTopLeftPos = new int[] {200, 50};
		mainPanel = new MainPanel(new Point(FrameTopLeftPos[0], FrameTopLeftPos[1])) ;
		legendPanel = new LegendPanel();
		diagramsPanel = new DiagramsPanel();
		jpLists = new ListPanel();

		Initialization();

		AddMenus();
		setJMenuBar(menuBar);
		setTitle("TCC");				// Super frame sets its title
		setPreferredSize(initialSize) ;		// Super frame sets its size
		setVisible(true);					// Super frame gets into the show
		pack();
		/* Super frame sets its everything. Super frame is so independent! =,) */

		setPanelsMain(jpLists);
	}
	

	public static Menus getInstance() { return instance ;}
	
	public void Initialization()
	{
		setLocation(FrameTopLeftPos[0], FrameTopLeftPos[1]);
	    int[] ScreenTopLeft = new int[] {0, 0, 0};				// Initial coordinates from the top left of the canvas window 900 720

	    MainCanvas = new MyCanvas (new Point(575, 25), new int[] {(int) (0.4 * mainPanel.getSize().getWidth()), (int) (0.8 * mainPanel.getSize().getHeight()), 0}, new double[] {10, 10, 0}, ScreenTopLeft);	    

		SubMenuDisp = new JMenuItem[6];				// ux, uy, uz, tetax, tetay, tetaz
		SubMenuStresses = new JMenuItem[6];			// Sigmax, Sigmay, Sigmaz, Taux, Tauy, Tauz
		SubMenuStrains = new JMenuItem[6];			// ex, ey, ez, gxy, gxz, gyz
		SubMenuInternalForces = new JMenuItem[6];	// Fx, Fy, Fz, Mx, My, Mz
		StepIsComplete = new boolean[9];		// 0 = Elem type; 1 = Struct Coords; 2 = Nodes and Elems; 3 = Mat; 4 = Sec; 5 = Sup; 6 = Conc loads; 7 = Dist loads; 8 = Nodal disps
	}
	
	public static MyCanvas getMainCanvas() { return MainCanvas ;}
	
	public SaveLoadFile getSaveLoadFile() { return new SaveLoadFile((JFrame) getParent(), FrameTopLeftPos) ;}
	
	public boolean[] getAqueleBooleanGrande() { return new boolean[] {MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn} ;}

	public int[] getFrameTopLeftPos() { return FrameTopLeftPos ;}
	
	public void setRunAnalysis(boolean state) { RunAnalysis.setEnabled(state) ;}
	
	public void setStepIsComplete(boolean[] StepIsComplete) {this.StepIsComplete = StepIsComplete ;}
	
	
	private JPanel createToolbar1()
	{
		/* Botâes no primeiro painel*/
		JPanel toolbar1Panel = new JPanel();		
	    JButton[] jb = new JButton[28];
	    String[] ButtonNames = new String[jb.length];
	    Color ButtonBGColor = palette[1];
		toolbar1Panel.setLayout(new GridLayout(4, 0));
		toolbar1Panel.setBackground(palette[1]);
	    ButtonNames[0] = "Especial";
	    ButtonNames[1] = "Exemplo";
	    ButtonNames[2] = "Criar malha";
	    ButtonNames[3] = "Criar materiais";
	    ButtonNames[4] = "Criar seçõs";
	    ButtonNames[5] = "Criar cargas concentradas";
	    ButtonNames[6] = "Criar cargas distribuâdas";
	    ButtonNames[7] = "Criar deslocamentos nodais";
	    ButtonNames[8] = "Adicionar materiais aos elementos";
	    ButtonNames[9] = "Adicionar seçõs aos elementos";
	    ButtonNames[10] = "Adicionar apoios aos nós";
	    ButtonNames[11] = "Adicionar cargas concentradas aos nós";
	    ButtonNames[12] = "Adicionar cargas distribuâdas aos elementos";
	    ButtonNames[13] = "Adicionar deslocamentos nodais aos nós";
	    ButtonNames[14] = "Mostrar graus de liberdade";
	    ButtonNames[15] = "Mostrar nâmeros dos nós";
	    ButtonNames[16] = "Mostrar nâmeros dos elementos";
	    ButtonNames[17] = "Mostrar materiais dos elementos";
	    ButtonNames[18] = "Mostrar seçõs dos elementos";
	    ButtonNames[19] = "Mostrar nós";
	    ButtonNames[20] = "Mostrar elementos";
	    ButtonNames[21] = "Mostrar contornos dos elementos";
	    ButtonNames[22] = "Mostrar apoios";
	    ButtonNames[23] = "Mostrar cargas concentradas";
	    ButtonNames[24] = "Mostrar cargas distribuâdas";
	    ButtonNames[25] = "Mostrar deslocamentos nodais";
	    ButtonNames[26] = "Mostrar valores das cargas e reaçõs";
	    ButtonNames[27] = "Mostrar reaçõs";
	    for (int b = 0; b <= jb.length - 1; b += 1)
	    {
			jb[b] = AddButton(null, new int[2], new int[] {32, 32}, 12, new int[] {0, 0, 0, 0}, ButtonBGColor);
		    jb[b].setToolTipText(ButtonNames[b]);
			jb[b].setIcon(new ImageIcon("./Icons/Tb1B" + String.valueOf(b + 1) + ".png"));
			jb[b].setFocusable(false);
			jb[b].setHorizontalAlignment(SwingConstants.CENTER);
		    toolbar1Panel.add(jb[b]);
	    }
	    
	    jb[0].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.Especial();
				ActivatePostAnalysisView();
			}
		});
	    jb[1].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//StructureMenuCreateStructureTyping();
				String[] ElemTypes = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
				JLabel[] Labels = new JLabel[] {};
				JButton[] Buttons = new JButton[ElemTypes.length];
				boolean[] Enabled = new boolean[ElemTypes.length];
				Arrays.fill(Enabled, true);
				for (int b = 0; b <= Buttons.length - 1; b += 1)
				{
					Buttons[b] = new JButton (ElemTypes[b]);
				}
				int[][] ButtonSizes = new int[Buttons.length][];
				Arrays.fill(ButtonSizes, new int[] {30, 20});
				InputPanelType2 CIT = new InputPanelType2((JFrame) getParent(), "Elem types", FrameTopLeftPos, Labels, Buttons, Enabled, ButtonSizes);
				String exampleid = CIT.run();
				if (exampleid != null)
				{
					MenuFunctions.RunExample(Integer.parseInt(exampleid));
					ActivatePostAnalysisView();
				}
				//StructureMenuCreateStructureOnClick(StructShapes);
			}
		});
	    jb[2].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//StructureMenuCreateMesh("Cartesian", ElemType, ElemShape, Anal);
				StructureMenuCreateMesh(MeshType.radial);
			}
		});
	    jb[3].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateMaterials();
			}
		});
	    jb[4].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateSections();
			}
		});
	    jb[5].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateConcLoads();
			}
		});
	    jb[6].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateDistLoads();
			}
		});
	    jb[7].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateNodalDisp();
			}
		});
	    jb[8].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignMaterials();
			}
		});
	    jb[9].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignSections();
			}
		});
	    jb[10].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignSupports();
			}
		});
	    jb[11].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignConcLoads();
			}
		});
	    jb[12].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignDistLoads();
			}
		});
	    jb[13].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuAssignNodalDisp();
			}
		});
	    jb[14].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.DOFNumberView();
			}
		});
	    jb[15].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.NodeNumberView();
			}
		});
	    jb[16].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.ElemNumberView();
			}
		});
	    jb[17].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.MatView();
			}
		});
	    jb[18].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.SecView();
			}
		});
	    jb[19].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.NodeView();
			}
		});
	    jb[20].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.ElemView();
			}
		});
	    jb[21].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.ElemContourView();
			}
		});
	    jb[22].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.SupView();
			}
		});
	    jb[23].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.ConcLoadsView();
			}
		});
	    jb[24].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.DistLoadsView();
			}
		});
	    jb[25].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.NodalDispsView();
			}
		});
	    jb[26].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.LoadsValuesView();
				MenuFunctions.ReactionValuesView();
			}
		});
	    jb[27].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.ReactionArrowsView();
			}
		});

		return toolbar1Panel;
	}
	
	private JPanel createToolbar2()
	{
		/* Listas no segundo painel*/
		JPanel toolbar1Panel = new JPanel();
		toolbar1Panel.setLayout(new GridLayout(5, 0));
		toolbar1Panel.setBackground(palette[1]);
		
		String[] ResultsNames = new String[]{"Deslocamentos", "Tensâes", "Deformaçõs", "Forâas Internas"};
		JComboBox<String> cbResults = new JComboBox<>(ResultsNames);
		cbResults.setFocusable(false);
		toolbar1Panel.add(cbResults);
		
		String[] SubResultsNames = new String[]{"X", "Y", "Z", "Tx", "Ty", "Tz"};
		JComboBox<String> cbSubRes = new JComboBox<>(SubResultsNames);
		//cbSubRes.setEnable(false);
		cbSubRes.setFocusable(false);
		toolbar1Panel.add(cbSubRes);
		
		JButton btnCalcular = new JButton("Calcular");
		btnCalcular.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.SelectedDiagram = cbResults.getSelectedIndex();
				MenuFunctions.SelectedVar = cbSubRes.getSelectedIndex();
				MenuFunctions.ShowResult(MainPanel.structure);
			}
		});
		btnCalcular.setFocusable(false);
		toolbar1Panel.add(btnCalcular);
		
		return toolbar1Panel;
	}
	
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
			UpperToolbarButton[b] = AddButton(ButtonNames[b], new int[2], new int[] {ButtonLength[b], 30}, 11, new int[] {2, 2, 2, 2}, ButtonBgColor);
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
					MainPanel.AddMaterialToElements(MenuFunctions.SelectedElems, MenuFunctions.matTypes.get(MainPanel.SelectedMat));
				}
				if (SecAssignmentIsOn)
				{
					MainPanel.AddSectionsToElements(MenuFunctions.SelectedElems, MenuFunctions.secTypes.get(MainPanel.SelectedSec));
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
				StepIsComplete = MenuFunctions.CheckSteps(MainPanel.structure);
				ReadyForAnalysis = MenuFunctions.CheckIfAnalysisIsReady(MainPanel.structure, MainPanel.loading);
				updateInstructionPanel();
				
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
	
	private JPanel createBlankPanel(Dimension size, Color bgcolor)
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
	
	private JPanel createInstructionPanel()
	{
		jpInstruction = new JPanel(new GridLayout(0,1));
		jpInstruction.setBackground(palette[3]);
		jpInstruction.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, null, Util.AddColor(palette[3], new double[] {50, 50, 50}), null, Util.AddColor(palette[3], new double[] {-50, -50, -50})));
		jpInstruction.setSize(defaultPanelSize);
		updateInstructionPanel();
		
		return jpInstruction;
	}
	
	public void updateInstructionPanel()
	{		
		jpInstruction.removeAll();

        boolean[] StepIsComplete = MenuFunctions.CheckSteps(MainPanel.structure);
        boolean ReadyForAnalysis = MenuFunctions.CheckIfAnalysisIsReady(MainPanel.structure, MainPanel.loading);
        ImageIcon OkIcon = new ImageIcon("./Icons/OkIcon.png");
		Color TextColor = palette[0];
		JLabel[] iStep = new JLabel[9];
		String[] Label = new String[iStep.length];		
		Label[0] = "1. Tipo de elemento";
		Label[1] = "2. Estrutura";
		Label[2] = "3. Malha";
		Label[3] = "4. Elementos com materiais";
		Label[4] = "5. Elementos com seçõs";
		Label[5] = "6. Apoios";
		Label[6] = "7. Cargas concentradas";
		Label[7] = "8. Cargas distribuâdas";
		Label[8] = "9. Deslocamentos nodais";
		JLabel FirstLabel = new JLabel("Passo a passo", 2);
		FirstLabel.setForeground(TextColor);
		jpInstruction.add(FirstLabel);
		for (int step = 0; step <= iStep.length - 1; step += 1)
		{
			if (StepIsComplete[step])
			{
				iStep[step] = new JLabel(Label[step], OkIcon, 2);
				iStep[step].setForeground(TextColor);
				iStep[step].setFont(new Font("SansSerif", Font.BOLD, 12));
			}
			else
			{
				iStep[step] = new JLabel("    " + Label[step]);
				iStep[step].setForeground(TextColor);
				iStep[step].setFont(new Font("SansSerif", Font.BOLD, 12));
			}
			jpInstruction.add(iStep[step]);
		}
		if (ReadyForAnalysis)
		{
			jpInstruction.add(new JLabel("Pronta para anâlise!"));
		}
	}
	
	private JPanel createNorthPanels()
	{
		JPanel N = new JPanel(new GridBagLayout());
		utb = createUpperToolBar();
		JPanel bp1 = createBlankPanel(new Dimension(7 * 32 + 4, 30), palette[2]);
		JPanel bp2 = createBlankPanel(new Dimension(260, 30), palette[2]);
		N.add(bp1);
		N.add(utb);
		N.add(bp2);
		
		return N;
	}
	
	private JPanel createEastPanels()
	{		
		JPanel E = new JPanel(new GridLayout(0, 1));
		bp1 = createBlankPanel(defaultPanelSize, palette[2]);
		bp2 = createBlankPanel(defaultPanelSize, palette[2]);
		bp3 = createBlankPanel(defaultPanelSize, palette[2]);
		LDpanel = createBlankPanel(defaultPanelSize, palette[2]);
		bp1.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		bp2.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		bp3.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		LDpanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		E.add(bp1);
		E.add(bp2);
		E.add(bp3);
		E.add(LDpanel);
		
		return E;
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
	
	private JPanel createWestPanels(JPanel Listsplot)
	{
		JPanel W = new JPanel(new GridLayout(0, 1));
		tb1 = createToolbar1();
		tb2 = createToolbar2();
		tb2.setVisible(false);
		ListsPanel = Listsplot;
		iPanel = createInstructionPanel();
		tb1.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		tb2.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		ListsPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		iPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, palette[1]));
		W.add(tb1);
		W.add(tb2);
		W.add(ListsPanel);
		W.add(iPanel);
		
		return W;
	}
	
	private void setPanelsMain(JPanel Listsplot)
	{
		JPanel newContentPanel = new JPanel(new GridBagLayout());
		BorderLayout bl = new BorderLayout();
		newContentPanel.setLayout(bl);

		N1 = createNorthPanels();
		W1 = createWestPanels(Listsplot);
		E1 = createEastPanels();
		
		newContentPanel.add(N1, BorderLayout.NORTH);
		newContentPanel.add(mainPanel, BorderLayout.CENTER);
		newContentPanel.add(W1, BorderLayout.WEST);
		newContentPanel.add(E1, BorderLayout.EAST);
	
		this.setContentPane(newContentPanel);
		this.setVisible(true);
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

	private void ActivatePostAnalysisView()
	{
		if (!((Double)MainPanel.structure.getU()[0]).isNaN())
		{
			E1.remove(LDpanel);
			E1.remove(bp3);
			LDpanel = diagramsPanel;
			bp3 = legendPanel;
			E1.add(bp3);
			E1.add(LDpanel);
			tb2.setVisible(true);
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
				JLabel[] Labels = new JLabel[] {};
				JButton[] Buttons = new JButton[ElemType.values().length];
				boolean[] Enabled = new boolean[ElemType.values().length];
				Arrays.fill(Enabled, true);
				for (int b = 0; b <= Buttons.length - 1; b += 1)
				{
					Buttons[b] = new JButton (ElemType.values()[b].toString());
				}
				int[][] ButtonSizes = new int[Buttons.length][];
				Arrays.fill(ButtonSizes, new int[] {30, 20});
				InputPanelType2 CIT = new InputPanelType2((JFrame) getParent(), "Elem types", FrameTopLeftPos, Labels, Buttons, Enabled, ButtonSizes);
				MainPanel.setElemType(CIT.run());
				StepIsComplete = MenuFunctions.CheckSteps(MainPanel.structure);
				EnableButtons();
				updateInstructionPanel();
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
				JButton[] Buttons = new JButton[] {new JButton ("Add"), new JButton ("Remove"), new JButton ("Ok"), new JButton ("Cancel")};
				int[][] ButtonSizes = new int[][] {{100, 20}, {30, 20}, {30, 20}, {30, 20}};
				InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Coordenadas", "No", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
				double[][] StructCoords = CI.run();
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
					updateInstructionPanel();
				}
			}
		});
		ClickNodes.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("ClickNodes");
				JLabel[] Labels = new JLabel[] {};
				JButton[] Buttons = new JButton[StructureShape.values().length];
				boolean[] Enabled = new boolean[StructureShape.values().length];
				for (int b = 0; b <= Buttons.length - 1; b += 1)
				{
					Buttons[b] = new JButton (StructureShape.values()[b].toString());
				}
				int[][] ButtonSizes = new int[Buttons.length][];
				Arrays.fill(ButtonSizes, new int[] {30, 20});
				Arrays.fill(Enabled, true);
				InputPanelType2 CIT = new InputPanelType2((JFrame) getParent(), "Structure shape", FrameTopLeftPos, Labels, Buttons, Enabled, ButtonSizes);
				MainPanel.CreateStructureOnClick(StructureShape.valueOf(CIT.run()));
				MenuFunctions.SnipToGridIsOn = false;
				UpperToolbarButton[0].setEnabled(true);
				UpperToolbarButton[0].setVisible(true);
				updateInstructionPanel();
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
					JLabel[] Labels = new JLabel[] {};
					JButton[] Buttons = new JButton[4];
					boolean[] Enabled = new boolean[4];
					for (int b = 0; b <= Buttons.length - 1; b += 1)
					{
						Buttons[b] = new JButton (ButtonNames[b]);
					}
					int[][] ButtonSizes = new int[Buttons.length][];
					Arrays.fill(ButtonSizes, new int[] {30, 20});
					Arrays.fill(Enabled, true);
					InputPanelType2 CIT = new InputPanelType2((JFrame) getParent(), "Analysis types", FrameTopLeftPos, Labels, Buttons, Enabled, ButtonSizes);
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
					MenuFunctions.SelectedVar = Util.ElemPosInArray(MainPanel.structure.getMesh().getElements().get(0).getDOFs(), d2);
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
					MenuFunctions.SelectedVar = Util.ElemPosInArray(MainPanel.structure.getMesh().getElements().get(0).getDOFs(), s2);
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
					MenuFunctions.SelectedVar = Util.ElemPosInArray(MainPanel.structure.getMesh().getElements().get(0).getDOFs(), s2);
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
					MenuFunctions.SelectedVar = Util.ElemPosInArray(MainPanel.structure.getMesh().getElements().get(0).getDOFs(), f2);
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
		JButton[] Buttons = new JButton[] {new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[Buttons.length][];
		Arrays.fill(ButtonSizes, new int[] {30, 20});
		if (meshType.equals(MeshType.cartesian))
		{
			Labels = new JLabel[] {new JLabel ("Nâ pontos em x"), new JLabel ("Nâ pontos em y")};
		}
		else if (meshType.equals(MeshType.radial))
		{
			Labels = new JLabel[] {new JLabel ("Nâ camadas"), new JLabel ("Nâ pontos por camada")};
		}
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Propriedades da malha", "Malha", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		int[][] UserDefinedMesh = Util.MatrixDoubleToInt(CI.run());
		
		MainPanel.structure.removeSupports() ;
		MainPanel.loading.clearLoads() ;
		MainPanel.structure.createMesh(meshType, UserDefinedMesh, ElemType.valueOf(MenuFunctions.SelectedElemType.toUpperCase()));
		MenuFunctions.NodeView();
		MenuFunctions.ElemView();
		StepIsComplete = MenuFunctions.CheckSteps(MainPanel.structure);
		EnableButtons();
		updateInstructionPanel();
	}
	
	public void StructureMenuCreateMaterials()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("E (GPa)"), new JLabel ("v"), new JLabel ("fu (MPa)")};
		JButton[] Buttons = new JButton[] {new JButton ("Add"), new JButton ("Remove"), new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[][] {{100, 20}, {30, 20}, {30, 20}, {30, 20}};
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Materials", "Mat", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		double[][] createdMaterials = CI.run() ;
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
		JButton[] Buttons = new JButton[] {new JButton ("Add"), new JButton ("Remove"), new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[][] {{100, 20}, {50, 20}, {30, 20}, {50, 20}};
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Cross sections", "Sec", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		double[][] createdSections = CI.run() ;
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
		JButton[] Buttons = new JButton[] {new JButton ("Add"), new JButton ("Remove"), new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[][] {{100, 20}, {50, 20}, {30, 20}, {50, 20}};
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Nodal loads", "Nodal load", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		MainPanel.DefineConcLoadTypes(CI.run());
		EnableButtons();
	}
	
	public void StructureMenuCreateDistLoads()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("Load type"), new JLabel ("Load i (kN / kNm)")};
		JButton[] Buttons = new JButton[] {new JButton ("Add"), new JButton ("Remove"), new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[][] {{100, 20}, {50, 20}, {30, 20}, {50, 20}};
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Member loads", "Member load", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		MainPanel.DefineDistLoadTypes(CI.run());
		EnableButtons();
	}
	
	public void StructureMenuCreateNodalDisp()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("disp x"), new JLabel ("disp y"), new JLabel ("disp z"), new JLabel ("rot x"), new JLabel ("rot y"), new JLabel ("rot z")};
		JButton[] Buttons = new JButton[] {new JButton ("Add"), new JButton ("Remove"), new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[][] {{100, 20}, {50, 20}, {30, 20}, {50, 20}};
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Nodal displacements", "Nodal disp", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		MainPanel.DefineNodalDispTypes(CI.run());
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


	private static JButton AddButton(String Text, int[] Alignment, int[] Size, int FontSize, int[] margins, Color color)
	{
		JButton NewButton = new JButton(Text);
		NewButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FontSize));
		NewButton.setVerticalAlignment(Alignment[0]);
		NewButton.setHorizontalAlignment(Alignment[1]);
		NewButton.setBackground(color);
		NewButton.setPreferredSize(new Dimension(Size[0], Size[1]));
		NewButton.setMargin(new Insets(margins[0], margins[1], margins[2], margins[3]));
		return NewButton;
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

}