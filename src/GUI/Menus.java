package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import Component.Structure;
import Component.Nodes;
import Component.Elements;
import Component.MyCanvas;
import Main.Analysis;
import Main.MenuFunctions;
import Utilidades.Util;
import Utilidades.UtilComponents;
import Utilidades.UtilText;

/*
 Pr�ximas adi��es
 *Ajustar o desenho de momento 3D
 *
 *Mnemonics do menu n�o funcionam
 *Selecionar elemento retangular clicando dentro
 */

public class Menus extends JFrame
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* Container and panel variables */
	Container cp;    
	JPanel N1, S1, E1, W1;
	JPanel tb1, tb2;
	JPanel ListsPanel;
	JPanel iPanel;
	JPanel utb, mp;
	JPanel mainpanel;
	JPanel bp1, bp2, bp3;
	JPanel LDpanel;
	JPanel jpMain, jpLD, jpLegend, jpLists, jpInstruction;
	
	JMenuBar menuBar;
    JMenu FileMenu, StructureMenu, ViewMenu, AnalysisMenu, ResultsMenu, EspecialMenu;
	JMenuItem Save, Load;
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
	JButton[] UpperToolbarButton;	// 0: Ligar �m�, 1: Desligar �m�, 2: Atribuir material, 3: Atribuir se��o, 4: Atribuir apoios, 5: Atribuir cargas conc, 6: Atribuir cargas dist, 7: Atribuir desl nodais, 8: +escala, 9: -escala
	JTextArea xPos;
	JTextArea yPos;
	
	Dimension defaultPanelSize = new Dimension(260, 300);
	
	/* Global variables */	
	MyCanvas MainCanvas, LDCanvas, LegendCanvas, ListsCanvas;
	DrawingOnAPanel DP;
	Analysis Anal;
	String Language;
	String[][] AllText;

	int[] FrameTopLeftPos;
    int[] MainPanelPos;	// LDPanelPos, LegendPanelPos
	boolean ShowStructure, ShowElems, ShowReactionArrows, ShowReactionValues, ShowLoadsValues;
    boolean ShowCanvas, ShowGrid, ShowMousePos;
    boolean MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn;
    boolean ReadyForAnalysis;
    boolean[] StepIsComplete;
	private Color[] ColorPalette;
	
	public Menus()
	{		
		Initialization();
		MenuFunctions.Initialization();
		AddMenus();
		setJMenuBar(menuBar);
		setTitle("TCC");								// Super frame sets its title
		setSize(1084, 700);	// Super frame sets its initial window size
		//setBackground(ColorPalette[3]);					// Super frame sets its back ground color
		setVisible(true);								// Super frame shows
		/* Super frame sets its everything. Super frame is so independent! =,) */

		setPanelsMain(jpMain, jpLists);
        jpMain.setFocusable(true);
        jpMain.requestFocus(true);
	}
	
	public void Initialization()
	{	
		FrameTopLeftPos = new int[] {300, 200};
		setLocation(FrameTopLeftPos[0], FrameTopLeftPos[1]);
		ColorPalette = Util.ColorPalette();
		int[] Toolbar1ButtonSize = new int[] {32, 32};
		int[] LDPanelSize = new int[] {10, 100};
		int[] LegendPanelSize = new int[] {10, 100};
		Border jpLDBorder = BorderFactory.createTitledBorder("");
		jpMain = initComponents(jpMain, new int[] {1350, 700}, ColorPalette[2], BorderFactory.createBevelBorder(BevelBorder.RAISED), "Main");	// Creates a JPanel inside the JFrame
		jpMain.setSize(new Dimension(1350, 700));
		jpLegend = initComponents(jpLegend, LegendPanelSize, ColorPalette[3], BorderFactory.createTitledBorder(jpLDBorder, "Legenda", TitledBorder.CENTER, TitledBorder.CENTER), "Legend");	// Creates a JPanel inside the JFrame
		jpLegend.setSize(new Dimension(10, 100));
		jpLD = initComponents(jpLD, LDPanelSize, ColorPalette[3], BorderFactory.createTitledBorder(jpLDBorder, "Curva carga deslocamento", TitledBorder.CENTER, TitledBorder.CENTER), "LD");	// Creates a JPanel inside the JFrame
		jpLD.setSize(new Dimension(0, 100));
		jpLists = initComponents(jpLists, LDPanelSize, ColorPalette[2], BorderFactory.createBevelBorder(BevelBorder.RAISED), "Lists");	// Creates a JPanel inside the JFrame
		jpLists.setSize(new Dimension(10, 100));
		
	    int[] ScreenTopLeft = new int[] {0, 0, 0};				// Initial coordinates from the top left of the canvas window 900 720
	    MainPanelPos = new int[] {FrameTopLeftPos[0] + 7 * Toolbar1ButtonSize[0] + 8, FrameTopLeftPos[1] + 76 + 8, 0};				// O segredo para a posi��o do mouse funcionar
	    MainCanvas = new MyCanvas (new int[] {575, 25, 0}, new int[] {(int) (0.4 * jpMain.getWidth()), (int) (0.8 * jpMain.getHeight()), 0}, new double[] {10, 10, 0}, ScreenTopLeft);	    
	    LDCanvas = new MyCanvas (new int[] {50, 25, 0}, new int[] {(int) (0.0 * jpMain.getWidth()), (int) (0.0 * jpMain.getHeight()), 0}, new double[] {10, 10, 0}, ScreenTopLeft);
	    LegendCanvas = new MyCanvas (new int[] {50, 25, 0}, new int[] {(int) (0.0 * jpMain.getWidth()), (int) (0.0 * jpMain.getHeight()), 0}, new double[] {10, 10, 0}, ScreenTopLeft);
	    ListsCanvas = new MyCanvas (new int[] {50, 25, 0}, new int[] {(int) (0.04 * jpMain.getWidth()), (int) (0.04 * jpMain.getHeight()), 0}, new double[] {10, 10, 0}, ScreenTopLeft);

		SubMenuDisp = new JMenuItem[6];				// ux, uy, uz, tetax, tetay, tetaz
		SubMenuStresses = new JMenuItem[6];			// Sigmax, Sigmay, Sigmaz, Taux, Tauy, Tauz
		SubMenuStrains = new JMenuItem[6];			// ex, ey, ez, gxy, gxz, gyz
		SubMenuInternalForces = new JMenuItem[6];	// Fx, Fy, Fz, Mx, My, Mz
		Language = "Pt-br";
		String fileName = null;
		if (Language.equals("Pt-br"))
		{
			fileName = "Texto_PT-br.txt";
		} else if (Language.equals("En"))
		{
			fileName = "Texto_En.txt";
		}
		AllText = UtilText.ReadTextFile(fileName);
		StepIsComplete = new boolean[9];		// 0 = Elem type; 1 = Struct Coords; 2 = Nodes and Elems; 3 = Mat; 4 = Sec; 5 = Sup; 6 = Conc loads; 7 = Dist loads; 8 = Nodal disps
	}
	
	// Start of GUI
	
	private JPanel createToolbar1()
	{
		/* Bot�es no primeiro painel*/
		JPanel toolbar1Panel = new JPanel();		
	    JButton[] jb = new JButton[28];
	    String[] ButtonNames = new String[jb.length];
	    Color ButtonBGColor = ColorPalette[1];
		toolbar1Panel.setLayout(new GridLayout(4, 0));
		toolbar1Panel.setBackground(ColorPalette[1]);
	    ButtonNames[0] = "Especial";
	    ButtonNames[1] = "Exemplo";
	    ButtonNames[2] = "Criar malha";
	    ButtonNames[3] = "Criar materiais";
	    ButtonNames[4] = "Criar se��es";
	    ButtonNames[5] = "Criar cargas concentradas";
	    ButtonNames[6] = "Criar cargas distribu�das";
	    ButtonNames[7] = "Criar deslocamentos nodais";
	    ButtonNames[8] = "Adicionar materiais aos elementos";
	    ButtonNames[9] = "Adicionar se��es aos elementos";
	    ButtonNames[10] = "Adicionar apoios aos n�s";
	    ButtonNames[11] = "Adicionar cargas concentradas aos n�s";
	    ButtonNames[12] = "Adicionar cargas distribu�das aos elementos";
	    ButtonNames[13] = "Adicionar deslocamentos nodais aos n�s";
	    ButtonNames[14] = "Mostrar graus de liberdade";
	    ButtonNames[15] = "Mostrar n�meros dos n�s";
	    ButtonNames[16] = "Mostrar n�meros dos elementos";
	    ButtonNames[17] = "Mostrar materiais dos elementos";
	    ButtonNames[18] = "Mostrar se��es dos elementos";
	    ButtonNames[19] = "Mostrar n�s";
	    ButtonNames[20] = "Mostrar elementos";
	    ButtonNames[21] = "Mostrar contornos dos elementos";
	    ButtonNames[22] = "Mostrar apoios";
	    ButtonNames[23] = "Mostrar cargas concentradas";
	    ButtonNames[24] = "Mostrar cargas distribu�das";
	    ButtonNames[25] = "Mostrar deslocamentos nodais";
	    ButtonNames[26] = "Mostrar valores das cargas e rea��es";
	    ButtonNames[27] = "Mostrar rea��es";
	    for (int b = 0; b <= jb.length - 1; b += 1)
	    {
			jb[b] = Util.AddButton(null, new int[2], new int[] {32, 32}, 12, new int[] {0, 0, 0, 0}, ButtonBGColor);
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
				StructureMenuCreateMesh("Radial", Anal);
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
		toolbar1Panel.setBackground(ColorPalette[1]);
		
		String[] ResultsNames = new String[]{"Deslocamentos", "Tens�es", "Deforma��es", "For�as Internas"};
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
				MenuFunctions.ShowResult();
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
		uToolbarPanel.setBackground(ColorPalette[2]);
		uToolbarPanel.setPreferredSize(new Dimension(580, 30));

		String[] ButtonNames = Util.LoadAllText(AllText, Language, 20);
		UpperToolbarButton = new JButton[ButtonNames.length];
		int[] ButtonLength = new int[] {62, 80, 138, 100, 50, 52, 50, 50};
		Color ButtonBgColor = ColorPalette[8];
		
		for (int b = 0; b <= UpperToolbarButton.length - 1; b += 1)
		{
			UpperToolbarButton[b] = Util.AddButton(ButtonNames[b], new int[2], new int[] {ButtonLength[b], 30}, 11, new int[] {2, 2, 2, 2}, ButtonBgColor);
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
					MenuFunctions.AddMaterialToElements(MenuFunctions.SelectedElems, MenuFunctions.MatType[MenuFunctions.SelectedMat]);
				}
				if (SecAssignmentIsOn)
				{
					MenuFunctions.AddSectionsToElements(MenuFunctions.SelectedElems, MenuFunctions.SecType[MenuFunctions.SelectedSec]);
				}
				if (DistLoadsAssignmentIsOn)
				{
					MenuFunctions.AddDistLoads();
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
					MenuFunctions.AddSupports();					
				}
				if (ConcLoadsAssignmentIsOn)
				{
					MenuFunctions.AddConcLoads();
				}
				if (NodalDispsAssignmentIsOn)
				{
					MenuFunctions.AddNodalDisps();
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
						Elements.setMatColors(MenuFunctions.MatType);
						for (int elem = 0; elem <= MenuFunctions.Elem.length - 1; elem += 1)
						{
							MenuFunctions.Elem[elem].setMatColor(Elements.MatColors[Util.FindID(MenuFunctions.MatType, MenuFunctions.Elem[elem].getMat())]);
						}
					}
					if (SecAssignmentIsOn)
					{
						Elements.setSecColors(MenuFunctions.SecType);
						for (int elem = 0; elem <= MenuFunctions.Elem.length - 1; elem += 1)
						{
							MenuFunctions.Elem[elem].setSecColor(Elements.SecColors[Util.FindID(MenuFunctions.SecType, MenuFunctions.Elem[elem].getSec())]);
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
				MenuFunctions.SelectedNodes = null;
				MenuFunctions.SelectedElems = null;
				MatAssignmentIsOn = false;
				SecAssignmentIsOn = false;
				SupAssignmentIsOn = false;
				ConcLoadsAssignmentIsOn = false;
				DistLoadsAssignmentIsOn = false;
				NodalDispsAssignmentIsOn = false;
				MenuFunctions.NodeSelectionIsOn = false;
				MenuFunctions.ElemSelectionIsOn = false;
				StepIsComplete = MenuFunctions.CheckSteps();
				ReadyForAnalysis = MenuFunctions.CheckIfAnalysisIsReady();
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
				MenuFunctions.Clean(new boolean[] {MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn});
			}
		});
		
		for (int b = 0; b <= UpperToolbarButton.length - 1; b += 1)
		{
			uToolbarPanel.add(UpperToolbarButton[b]);
		}
		
		return uToolbarPanel;
	}
	
	/* ok */
	private JPanel createBlankPanel(Dimension size, Color bgcolor)
	{
		JPanel blankPanel = new JPanel();
		blankPanel.setLayout(new GridLayout(1, 1));
		blankPanel.setPreferredSize(size);
		blankPanel.setBackground(bgcolor);
		return blankPanel;
	}
	
	private JPanel createNodeInfoPanel(Nodes Node)
	{
		JPanel NodeInfoPanel = new JPanel(new GridLayout(0,1));
		Color TextColor = ColorPalette[4];
		NodeInfoPanel.setBackground(ColorPalette[9]);
		NodeInfoPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[5]));
		NodeInfoPanel.setSize(defaultPanelSize);		

		String OriginalCoords = "", DeformedCoords = "";
		String ConcLoads = String.valueOf(0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0);
		for (int c = 0; c <= Node.getOriginalCoords().length - 1; c+= 1)
		{
			OriginalCoords += String.valueOf(Util.Round(Node.getOriginalCoords()[c], 2));
			DeformedCoords += String.valueOf(Util.Round(Node.getDisp()[c], 2));
			if (c < Node.getOriginalCoords().length - 1)
			{
				OriginalCoords += ",";
				DeformedCoords += ",";
			}
		}
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
		
		JLabel iLabel = new JLabel("Informa��es do n�");
		NodeInfoPanel.add(iLabel);
		iLabel.setForeground(TextColor);
		
		JLabel[] iInfo = new JLabel[4];
		iInfo[0] = new JLabel(" N�: " + String.valueOf(Node.getID()));
		iInfo[1] = new JLabel(" Original pos: " + OriginalCoords);
		iInfo[2] = new JLabel(" Deslocamentos: " + DeformedCoords);
		iInfo[3] = new JLabel(" For�as: " + ConcLoads);
		for (int i = 0; i <= iInfo.length - 1; i += 1)
		{
			NodeInfoPanel.add(iInfo[i]);
		}
		
		return NodeInfoPanel;
	}
	
	private JPanel createElemInfoPanel(Elements Elem)
	{
		JPanel ElemInfoPanel = new JPanel(new GridLayout(0,1));
		Color TextColor = ColorPalette[4];
		
		ElemInfoPanel.setBackground(ColorPalette[9]);
		ElemInfoPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[5]));
		ElemInfoPanel.setSize(defaultPanelSize);		

		String NodesText = "";
		for (int node = 0; node <= Elem.getExternalNodes().length - 1; node += 1)
		{
			NodesText += String.valueOf(Elem.getExternalNodes()[node] + " ");
		}
		String MatText = null;
		if (Elem.getMat() != null)
		{
			MatText = String.valueOf(Util.Round(Elem.getMat()[0], 2)) + " MPa v = " + String.valueOf(Util.Round(Elem.getMat()[1], 1)) + " G = " + String.valueOf(Util.Round(Elem.getMat()[2], 2)) + " Mpa";
		}
		String SecText = null;
		if (Elem.getSec() != null)
		{
			SecText = String.valueOf(Util.Round(Elem.getSec()[0], 0)) + " mm";
		}
		
		JLabel iLabel = new JLabel("Informa��es do elemento");
		ElemInfoPanel.add(iLabel);
		iLabel.setForeground(TextColor);
		
		JLabel[] iInfo = new JLabel[4];
		iInfo[0] = new JLabel(" Elem: " + String.valueOf(Elem.getID()));
		iInfo[1] = new JLabel(" N�s: " + NodesText);
		iInfo[2] = new JLabel(" Material: E = " + MatText);
		iInfo[3] = new JLabel(" Se��o: t = " + SecText);
		
		for (int i = 0; i <= iInfo.length - 1; i += 1)
		{
			ElemInfoPanel.add(iInfo[i]);
		}
		
		return ElemInfoPanel;
	}
	
	private JPanel createInstructionPanel()
	{
		jpInstruction = new JPanel(new GridLayout(0,1));
		jpInstruction.setBackground(ColorPalette[3]);
		jpInstruction.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, null, Util.AddColor(ColorPalette[3], new double[] {50, 50, 50}), null, Util.AddColor(ColorPalette[3], new double[] {-50, -50, -50})));
		jpInstruction.setSize(defaultPanelSize);
		updateInstructionPanel();
		
		return jpInstruction;
	}
	
	private void updateInstructionPanel()
	{		
		jpInstruction.removeAll();

        boolean[] StepIsComplete = MenuFunctions.CheckSteps();
        boolean ReadyForAnalysis = MenuFunctions.CheckIfAnalysisIsReady();
        ImageIcon OkIcon = new ImageIcon("./Icons/OkIcon.png");
		Color TextColor = ColorPalette[0];
		JLabel[] iStep = new JLabel[9];
		String[] Label = new String[iStep.length];		
		Label[0] = "1. Tipo de elemento";
		Label[1] = "2. Estrutura";
		Label[2] = "3. Malha";
		Label[3] = "4. Elementos com materiais";
		Label[4] = "5. Elementos com se��es";
		Label[5] = "6. Apoios";
		Label[6] = "7. Cargas concentradas";
		Label[7] = "8. Cargas distribu�das";
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
			jpInstruction.add(new JLabel("Pronta para an�lise!"));
		}
	}
	
	private JPanel createMousePositionPanel()
	{
		JPanel mousePosPanel = new JPanel();
		mousePosPanel.setBackground(ColorPalette[3]);
		GridLayout gLayout = new GridLayout(1, 0);
		gLayout.setHgap(10);
		mousePosPanel.setLayout(gLayout);
		JLabel iLabel = new JLabel("Mouse Pos: ");
		JTextArea xPosTextArea = new JTextArea();
		JTextArea yPosTextArea = new JTextArea();
		int insets = 5;
		
		xPosTextArea.setEditable(false);
		xPosTextArea.setMargin(new Insets(insets, insets, insets, insets));
		xPosTextArea.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorPalette[0]));
		xPosTextArea.setPreferredSize(new Dimension(30, 20));
		
		yPosTextArea.setEditable(false);
		yPosTextArea.setMargin(new Insets(insets, insets, insets, insets));
		yPosTextArea.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorPalette[0]));
		yPosTextArea.setPreferredSize(new Dimension(30, 20));
		
		this.xPos = xPosTextArea;
		this.yPos = yPosTextArea;
		
		mousePosPanel.add(iLabel);
		mousePosPanel.add(xPosTextArea);
		mousePosPanel.add(yPosTextArea);
		
		return mousePosPanel;
	}
	
	private JPanel createNorthPanels()
	{
		JPanel N = new JPanel(new GridBagLayout());
		utb = createUpperToolBar();
		//utb.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[1]));
		JPanel bp1 = createBlankPanel(new Dimension(7 * 32 + 4, 30), ColorPalette[2]);
		JPanel bp2 = createBlankPanel(new Dimension(260, 30), ColorPalette[2]);
		N.add(bp1);
		N.add(utb);
		N.add(bp2);
		
		return N;
	}
	
	private JPanel createSouthPanels()
	{
		JPanel S = new JPanel(new GridLayout(1, 0));
		mp = createMousePositionPanel();
		mp.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[1]));
		S.add(createBlankPanel(new Dimension(10, 20), ColorPalette[2]));
		S.add(createBlankPanel(new Dimension(10, 20), ColorPalette[2]));
		S.add(mp);
		S.add(createBlankPanel(new Dimension(10, 20), ColorPalette[2]));
		S.add(createBlankPanel(new Dimension(10, 20), ColorPalette[2]));
		
		return S;
	}
	
	private JPanel createEastPanels()
	{		
		JPanel E = new JPanel(new GridLayout(0, 1));
		bp1 = createBlankPanel(defaultPanelSize, ColorPalette[2]);
		bp2 = createBlankPanel(defaultPanelSize, ColorPalette[2]);
		bp3 = createBlankPanel(defaultPanelSize, ColorPalette[2]);
		LDpanel = createBlankPanel(defaultPanelSize, ColorPalette[2]);
		bp1.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[1]));
		bp2.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[1]));
		bp3.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[1]));
		LDpanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[1]));
		E.add(bp1);
		E.add(bp2);
		E.add(bp3);
		E.add(LDpanel);
		
		return E;
	}
	
	private void ResetEastPanels()
	{
		if (MenuFunctions.SelectedNodes != null)
		{
			if (-1 < MenuFunctions.SelectedNodes[0])
			{
				bp1 = createNodeInfoPanel(MenuFunctions.Node[MenuFunctions.SelectedNodes[0]]);
			}
		}
		if (MenuFunctions.SelectedElems != null)
		{
			if (-1 < MenuFunctions.SelectedElems[0])
			{
				bp2 = createElemInfoPanel(MenuFunctions.Elem[MenuFunctions.SelectedElems[0]]);
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
		tb1.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[1]));
		tb2.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[1]));
		ListsPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[1]));
		iPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, ColorPalette[1]));
		W.add(tb1);
		W.add(tb2);
		W.add(ListsPanel);
		W.add(iPanel);
		
		return W;
	}
	
	private void setPanelsMain(JPanel mainplot, JPanel Listsplot)
	{
		JPanel newContentPanel = new JPanel(new GridBagLayout());
		BorderLayout bl = new BorderLayout();
		newContentPanel.setLayout(bl);
		/*GroupLayout cpGLayout = new GroupLayout(newContentPanel);
		newContentPanel.setLayout(cpGLayout);
		cpGLayout.setAutoCreateGaps(true);
		cpGLayout.setAutoCreateContainerGaps(true);*/

		/* upper toolbar and north panels */
		N1 = createNorthPanels();

		/* center panel */
		mainpanel = mainplot;

		/* west panels */
		W1 = createWestPanels(Listsplot);

		/* south panels */
		S1 = createSouthPanels();

		/* east panels */
		E1 = createEastPanels();
		
		newContentPanel.add(N1, BorderLayout.NORTH);
		newContentPanel.add(mainpanel, BorderLayout.CENTER);
		newContentPanel.add(W1, BorderLayout.WEST);
		//newContentPanel.add(S1, BorderLayout.SOUTH);
		newContentPanel.add(E1, BorderLayout.EAST);
	
		this.setContentPane(newContentPanel);
		this.setVisible(true);
	}
	
	private void EnableButtons()
	{
		Object[] StructInfo = MenuFunctions.GetStructInfo();
		Structure Struct = (Structure) StructInfo[0];
		Nodes[] Node = (Nodes[]) StructInfo[1];
		Elements[] Elem = (Elements[]) StructInfo[2];
		boolean AnalysisIsComplete = (boolean) StructInfo[3];

		Object[] TypesInfo = MenuFunctions.GetTypesInfo();
		String SelectedElemType = (String) TypesInfo[0];
		double[][] MatTypes = (double[][]) TypesInfo[1];
		double[][] SecTypes = (double[][]) TypesInfo[2];
		double[][] ConcLoadTypes = (double[][]) TypesInfo[4];
		double[][] DistLoadTypes = (double[][]) TypesInfo[5];
		double[][] NodalDispTypes = (double[][]) TypesInfo[6];
		if ( Node != null )
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
		if ( Elem != null )
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
		if ( SelectedElemType != null & Struct.getCoords() != null)
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
			for (int i = 0; i <= Elem[0].getStrainTypes().length - 1; i += 1)
			{
				if (Elem[0].getStrainTypes()[i] <= 5)
				{
					SubMenuStresses[Elem[0].getStrainTypes()[i]].setEnabled(true);
					SubMenuStrains[Elem[0].getStrainTypes()[i]].setEnabled(true);
				}
			}
			SaveResults.setEnabled(true);
			SaveLoadDispCurve.setEnabled(true);
		}
	}
	
	private void DisableButtons()
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
		if (!((Double)MenuFunctions.Struct.getU()[0]).isNaN())
		{
			E1.remove(LDpanel);
			E1.remove(bp3);
			LDpanel = jpLD;
			bp3 = jpLegend;
			E1.add(bp3);
			E1.add(LDpanel);
			tb2.setVisible(true);
			EnableButtons();
		}
	}
	
	/* ok */
	
	// end of GUI
	
	// Start of adding menus and menu items	
	public void AddMenus()
	{
		/* Defining menu bars */
		String[] MenuNames = Util.LoadAllText(AllText, Language, 0);	
		menuBar = new JMenuBar();
		FileMenu = new JMenu(MenuNames[0]);			// File
		StructureMenu = new JMenu(MenuNames[1]);	// Structure
		ViewMenu = new JMenu(MenuNames[2]);			// View
		AnalysisMenu = new JMenu(MenuNames[3]);		// Analysis
		ResultsMenu = new JMenu(MenuNames[4]);		// Results
		EspecialMenu = new JMenu(MenuNames[5]);		// Especial
		FileMenu.setMnemonic(KeyEvent.VK_A);
		StructureMenu.setMnemonic(KeyEvent.VK_S);
		ViewMenu.setMnemonic(KeyEvent.VK_V);
		ResultsMenu.setMnemonic(KeyEvent.VK_R);
		EspecialMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(FileMenu);
		menuBar.add(StructureMenu);
		menuBar.add(ViewMenu);
		menuBar.add(AnalysisMenu);
		menuBar.add(ResultsMenu);
		menuBar.add(EspecialMenu);
		AddFileMenuItems(); 
		AddStructureMenuItems();
		AddViewMenuItems();
		AddAnalysisMenuItems();
		AddResultsMenuItems();
		AddEspecialMenuItems();
	}

	public void AddFileMenuItems()
	{
		/* Defining items in the menu File */
		Save = new JMenuItem("Save file", KeyEvent.VK_S);
		Load = new JMenuItem("Load file", KeyEvent.VK_L);
		Save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				SaveLoadFile SLF = new SaveLoadFile((JFrame) getParent(), FrameTopLeftPos);
				String FileName = SLF.run().getText();
				MenuFunctions.SaveFile(FileName, AllText, Language, MainCanvas, MenuFunctions.Struct, MenuFunctions.Node, MenuFunctions.Elem, MenuFunctions.Sup, MenuFunctions.ConcLoad, MenuFunctions.DistLoad, MenuFunctions.NodalDisp, MenuFunctions.MatType, MenuFunctions.SecType);
			}
		});
		Load.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.ResetStructure();
				SaveLoadFile SLF = new SaveLoadFile((JFrame) getParent(), FrameTopLeftPos);
				String FileName = SLF.run().getText();
				MenuFunctions.LoadFile("", FileName);
				MainCanvas.setDim(new double[] {1.2*Util.FindMaxInPos(MenuFunctions.Struct.getCoords(), 0), 1.2*Util.FindMaxInPos(MenuFunctions.Struct.getCoords(), 1)});
				ReadyForAnalysis = MenuFunctions.CheckIfAnalysisIsReady();
				if (ReadyForAnalysis)
				{
					RunAnalysis.setEnabled(true);
				}
				ShowCanvas = true;
				ShowGrid = true;
				ShowMousePos = true;
				MenuFunctions.NodeView();
				MenuFunctions.ElemView();
				MenuFunctions.ElemContourView();
				MenuFunctions.SupView();
				MenuFunctions.ConcLoadsView();
				MenuFunctions.DistLoadsView();
				MenuFunctions.NodalDispsView();
				StepIsComplete = MenuFunctions.CheckSteps();
				DisableButtons();
				EnableButtons();
				updateInstructionPanel();
			}
		});
		Save.setForeground(ColorPalette[3]);
		Load.setForeground(ColorPalette[3]);
		FileMenu.add(Save);
		FileMenu.add(Load);
	}
	
	public void AddStructureMenuItems()
	{
		/* Defining items in the menu Structure */
	    String[] StructureMenuItemsNames = Util.LoadAllText(AllText, Language, 2);
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
				String[] ElemTypes = Elements.getElemTypes();
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
				MenuFunctions.DefineElemType(CIT.run());
				StepIsComplete = MenuFunctions.CheckSteps();
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
		DefineElemType.setForeground(ColorPalette[5]);
		CreateNodes.setForeground(ColorPalette[5]);
		CreateMesh.setForeground(ColorPalette[5]);
		CreateMaterials.setForeground(ColorPalette[5]);
		CreateSections.setForeground(ColorPalette[5]);
		CreateConcLoads.setForeground(ColorPalette[5]);
		CreateDistLoads.setForeground(ColorPalette[5]);
		CreateNodalDisp.setForeground(ColorPalette[5]);
		AssignMaterials.setForeground(ColorPalette[5]);
		AssignSections.setForeground(ColorPalette[5]);
		AssignSupports.setForeground(ColorPalette[5]);
		AssignConcLoads.setForeground(ColorPalette[5]);
		AssignDistLoads.setForeground(ColorPalette[5]);
		AssignNodalDisp.setForeground(ColorPalette[5]);
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
		String[] CreateNodesMenuNames = Util.LoadAllText(AllText, Language, 28);
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
					MenuFunctions.Struct.setCoords(StructCoords);
					MenuFunctions.Struct.setCenter(Util.MatrixAverages(MenuFunctions.Struct.getCoords()));
					MenuFunctions.Struct.setMinCoords(new double[] {Util.FindMinInPos(MenuFunctions.Struct.getCoords(), 0), Util.FindMinInPos(MenuFunctions.Struct.getCoords(), 1), Util.FindMinInPos(MenuFunctions.Struct.getCoords(), 2)});
					MenuFunctions.Struct.setMaxCoords(new double[] {Util.FindMaxInPos(MenuFunctions.Struct.getCoords(), 0), Util.FindMaxInPos(MenuFunctions.Struct.getCoords(), 1), Util.FindMaxInPos(MenuFunctions.Struct.getCoords(), 2)});
					MainCanvas.setDim(new double[] {1.2 * MenuFunctions.Struct.getMaxCoords()[0], 1.2 * MenuFunctions.Struct.getMaxCoords()[1], 0});
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
				String[] StructShapes = Structure.getStructureShapes();
				JLabel[] Labels = new JLabel[] {};
				JButton[] Buttons = new JButton[StructShapes.length];
				boolean[] Enabled = new boolean[StructShapes.length];
				for (int b = 0; b <= Buttons.length - 1; b += 1)
				{
					Buttons[b] = new JButton (StructShapes[b]);
				}
				int[][] ButtonSizes = new int[Buttons.length][];
				Arrays.fill(ButtonSizes, new int[] {30, 20});
				Arrays.fill(Enabled, true);
				InputPanelType2 CIT = new InputPanelType2((JFrame) getParent(), "Structure shape", FrameTopLeftPos, Labels, Buttons, Enabled, ButtonSizes);
				MenuFunctions.CreateStructureOnClick(CIT.run());
				MenuFunctions.SnipToGridIsOn = false;
				UpperToolbarButton[0].setEnabled(true);
				UpperToolbarButton[0].setVisible(true);
				updateInstructionPanel();
			}
		});
		TypeNodes.setForeground(ColorPalette[5]);
		ClickNodes.setForeground(ColorPalette[5]);
		CreateNodes.add(ClickNodes);
		CreateNodes.add(TypeNodes);
		
		/* Defining subitems in the menu CreateMesh */
		String[] CreateMeshMenuNames = Util.LoadAllText(AllText, Language, 21);
		CartesianMesh = new JMenuItem(CreateMeshMenuNames[0], KeyEvent.VK_C);
		RadialMesh = new JMenuItem(CreateMeshMenuNames[1], KeyEvent.VK_R);
		CartesianMesh.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateMesh("Cartesian", Anal);
			}
		});
		RadialMesh.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				StructureMenuCreateMesh("Radial", Anal);
			}
		});
		CartesianMesh.setForeground(ColorPalette[5]);
		RadialMesh.setForeground(ColorPalette[5]);
		CreateMesh.add(CartesianMesh);
		CreateMesh.add(RadialMesh);		
	}
	
	public void AddViewMenuItems()
	{
		/* Defining items in the menu View */
	    String[] ViewMenuItemsNames;
		ViewMenuItemsNames = Util.LoadAllText(AllText, Language, 3);
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
		DOFNumberView.setForeground(ColorPalette[5]);
		NodeNumberView.setForeground(ColorPalette[5]);
		ElemNumberView.setForeground(ColorPalette[5]);
		MatView.setForeground(ColorPalette[5]);
		SecView.setForeground(ColorPalette[5]);
		NodeView.setForeground(ColorPalette[5]);
		ElemView.setForeground(ColorPalette[5]);
		ElemContourView.setForeground(ColorPalette[5]);
		SupView.setForeground(ColorPalette[5]);
		ConcLoadsView.setForeground(ColorPalette[5]);
		DistLoadsView.setForeground(ColorPalette[5]);
		NodalDispsView.setForeground(ColorPalette[5]);
		LoadsValuesView.setForeground(ColorPalette[5]);
		ReactionsView.setForeground(ColorPalette[5]);
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

	    String[] ReactionsViewMenuNames;
		ReactionsViewMenuNames = Util.LoadAllText(AllText, Language, 10);
		ReactionArrows = new JMenuItem(ReactionsViewMenuNames[0], KeyEvent.VK_C);
		ReactionValues = new JMenuItem(ReactionsViewMenuNames[1], KeyEvent.VK_C);
		ReactionArrows.setForeground(ColorPalette[5]);
		ReactionValues.setForeground(ColorPalette[5]);
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
	    String[] AnalysisMenuItemsNames;
		AnalysisMenuItemsNames = Util.LoadAllText(AllText, Language, 4);
		RunAnalysis = new JMenuItem(AnalysisMenuItemsNames[0], KeyEvent.VK_R);
		RunAnalysis.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (ReadyForAnalysis)
				{
					String[] ButtonNames = new String[] {"Linear el�stica", "Geometria n�o-linear", "Material n�o-linear", "Ambos n�o-lineares"};
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

					MenuFunctions.CalcAnalysisParameters();
					MenuFunctions.RunAnalysis(MenuFunctions.Struct, MenuFunctions.Node, MenuFunctions.Elem, MenuFunctions.Sup, MenuFunctions.ConcLoad, MenuFunctions.DistLoad, MenuFunctions.NodalDisp, MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, NIter, NLoadSteps, MaxLoadFactor);
				    MenuFunctions.PostAnalysis();
					for (int elem = 0; elem <= MenuFunctions.Elem.length - 1; elem += 1)
					{
				    	MenuFunctions.Elem[elem].RecordResults(MenuFunctions.Node, MenuFunctions.Struct.getU(), NonlinearMat, NonlinearGeo);
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
		RunAnalysis.setForeground(ColorPalette[5]);
		AnalysisMenu.add(RunAnalysis);
	}
	
	public void AddResultsMenuItems()
	{
		/* Defining items in the menu Results */
	    String[] ResultsMenuItemsNames;
		ResultsMenuItemsNames = Util.LoadAllText(AllText, Language, 5);
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
				MenuFunctions.ResultsMenuSaveResults();
			}
		});
		SaveLoadDispCurve.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.SaveLoadDispCurve();
			}
		});
		DeformedShape.setEnabled(false);
		DisplacementContours.setEnabled(false);
		StressContours.setEnabled(false);
		StrainContours.setEnabled(false);
		InternalForcesContours.setEnabled(false);
		SaveResults.setEnabled(false);
		SaveLoadDispCurve.setEnabled(false);
		DeformedShape.setForeground(ColorPalette[7]);
		DisplacementContours.setForeground(ColorPalette[7]);
		StressContours.setForeground(ColorPalette[7]);
		StrainContours.setForeground(ColorPalette[7]);
		InternalForcesContours.setForeground(ColorPalette[7]);
		SaveResults.setForeground(ColorPalette[7]);
		SaveLoadDispCurve.setForeground(ColorPalette[7]);
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
			String[] DisplacementContourMenuNames = Util.LoadAllText(AllText, Language, 26);
			SubMenuDisp[d] = new JMenuItem(DisplacementContourMenuNames[d]);
			SubMenuDisp[d].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 0;
					MenuFunctions.SelectedVar = Util.ElemPosInArray(MenuFunctions.Elem[0].getDOFs(), d2);
					ShowElems = false;
					//DrawDisplacementContours(SelectedVar);
					//ResetE1Panel();
				}
			});
			SubMenuDisp[d].setEnabled(false);
			SubMenuDisp[d].setForeground(ColorPalette[7]);
			DisplacementContours.add(SubMenuDisp[d]);
		}
		
		/* Defining subitems in the menu StressContours */
		for (int s = 0; s <= SubMenuStresses.length - 1; s += 1)
		{
			int s2 = s;
			String[] StressContoursMenuNames = Util.LoadAllText(AllText, Language, 8);
			SubMenuStresses[s] = new JMenuItem(StressContoursMenuNames[s]);
			SubMenuStresses[s].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 1;
					MenuFunctions.SelectedVar = Util.ElemPosInArray(MenuFunctions.Elem[0].getDOFs(), s2);
					ShowElems = false;
					//DrawStressContours(SelectedVar);
					//ResetE1Panel();
				}
			});
			SubMenuStresses[s].setEnabled(false);
			SubMenuStresses[s].setForeground(ColorPalette[7]);
			StressContours.add(SubMenuStresses[s]);
		}
		
		/* Defining subitems in the menu StrainContours */
		for (int s = 0; s <= SubMenuStrains.length - 1; s += 1)
		{
			int s2 = s;
			String[] StrainContoursMenuNames = Util.LoadAllText(AllText, Language, 7);
			SubMenuStrains[s] = new JMenuItem(StrainContoursMenuNames[s]);
			SubMenuStrains[s].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 2;
					MenuFunctions.SelectedVar = Util.ElemPosInArray(MenuFunctions.Elem[0].getDOFs(), s2);
					ShowElems = false;
					//DrawStrainContours(SelectedVar);
					//ResetE1Panel();
				}
			});
			SubMenuStrains[s].setEnabled(false);
			SubMenuStrains[s].setForeground(ColorPalette[7]);
			StrainContours.add(SubMenuStrains[s]);
		}
		
		/* Defining subitems in the menu InternalForcesContours */
		for (int f = 0; f <= SubMenuInternalForces.length - 1; f += 1)
		{
			int f2 = f;
			String[] InternalForcesContoursMenuNames = Util.LoadAllText(AllText, Language, 6);
			SubMenuInternalForces[f] = new JMenuItem(InternalForcesContoursMenuNames[f]);
			SubMenuInternalForces[f].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 3;
					MenuFunctions.SelectedVar = Util.ElemPosInArray(MenuFunctions.Elem[0].getDOFs(), f2);
					ShowElems = false;
					//DrawInternalForcesContours(Util.ElemPosInArray(Elem[0].getDOFs(), f2));
					//ResetE1Panel();
				}
			});
			SubMenuInternalForces[f].setEnabled(false);
			SubMenuInternalForces[f].setForeground(ColorPalette[7]);
			InternalForcesContours.add(SubMenuInternalForces[f]);
		}
	}
	
	public void AddEspecialMenuItems()
	{
		/* Defining items in the menu Especial */
	    String[] EspecialMenuItemsNames;
		EspecialMenuItemsNames = Util.LoadAllText(AllText, Language, 27);
		Star = new JMenuItem(EspecialMenuItemsNames[0], KeyEvent.VK_S);
		Star.setForeground(ColorPalette[7]);
		EspecialMenu.add(Star);
		Star.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Object[] EspecialResults = MenuFunctions.Especial();
				MenuFunctions.Struct = (Structure) EspecialResults[0];
				MenuFunctions.Node = (Nodes[]) EspecialResults[1];
				MenuFunctions.Elem = (Elements[]) EspecialResults[2];
				ActivatePostAnalysisView();
			}
		});
	}
	// End of adding menus and menu items

	/* ok */	
	public void StructureMenuCreateMesh(String MeshType, Analysis Anal)
	{
		JLabel[] Labels = new JLabel[2];
		JButton[] Buttons = new JButton[] {new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[Buttons.length][];
		Arrays.fill(ButtonSizes, new int[] {30, 20});
		if (MeshType.equals("Cartesian"))
		{
			Labels = new JLabel[] {new JLabel ("N� pontos em x"), new JLabel ("N� pontos em y")};
		}
		else if (MeshType.equals("Radial"))
		{
			Labels = new JLabel[] {new JLabel ("N� camadas"), new JLabel ("N� pontos por camada")};
		}
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Propriedades da malha", "Malha", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		int[][] UserDefinedMesh = Util.MatrixDoubleToInt(CI.run());
		MenuFunctions.StructureMenuCreateMesh(MeshType, UserDefinedMesh, MenuFunctions.SelectedElemType);
		MenuFunctions.NodeView();
		MenuFunctions.ElemView();
		StepIsComplete = MenuFunctions.CheckSteps();
		EnableButtons();
		updateInstructionPanel();
	}
	
	public void StructureMenuCreateMaterials()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("E (GPa)"), new JLabel ("v"), new JLabel ("fu (MPa)")};
		JButton[] Buttons = new JButton[] {new JButton ("Add"), new JButton ("Remove"), new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[][] {{100, 20}, {30, 20}, {30, 20}, {30, 20}};
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Materials", "Mat", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		MenuFunctions.DefineMaterialTypes(CI.run());
		EnableButtons();
	}
	
	public void StructureMenuCreateSections()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("espessura (mm)")};
		JButton[] Buttons = new JButton[] {new JButton ("Add"), new JButton ("Remove"), new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[][] {{100, 20}, {50, 20}, {30, 20}, {50, 20}};
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Cross sections", "Sec", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		MenuFunctions.DefineSectionTypes(CI.run());
		EnableButtons();
	}
	
	public void StructureMenuCreateConcLoads()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("Fx (kN)"), new JLabel ("Fy (kN)"), new JLabel ("Fz (kN)"), new JLabel ("Mx (kNm)"), new JLabel ("My (kNm)"), new JLabel ("Mz (kNm)")};
		JButton[] Buttons = new JButton[] {new JButton ("Add"), new JButton ("Remove"), new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[][] {{100, 20}, {50, 20}, {30, 20}, {50, 20}};
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Nodal loads", "Nodal load", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		MenuFunctions.DefineConcLoadTypes(CI.run());
		EnableButtons();
	}
	
	public void StructureMenuCreateDistLoads()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("Load type"), new JLabel ("Load i (kN / kNm)")};
		JButton[] Buttons = new JButton[] {new JButton ("Add"), new JButton ("Remove"), new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[][] {{100, 20}, {50, 20}, {30, 20}, {50, 20}};
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Member loads", "Member load", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		MenuFunctions.DefineDistLoadTypes(CI.run());
		EnableButtons();
	}
	
	public void StructureMenuCreateNodalDisp()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("disp x"), new JLabel ("disp y"), new JLabel ("disp z"), new JLabel ("rot x"), new JLabel ("rot y"), new JLabel ("rot z")};
		JButton[] Buttons = new JButton[] {new JButton ("Add"), new JButton ("Remove"), new JButton ("Ok"), new JButton ("Cancel")};
		int[][] ButtonSizes = new int[][] {{100, 20}, {50, 20}, {30, 20}, {50, 20}};
		InputPanelType1 CI = new InputPanelType1((JFrame) getParent(), "Nodal displacements", "Nodal disp", FrameTopLeftPos, Labels, Buttons, ButtonSizes);
		MenuFunctions.DefineNodalDispTypes(CI.run());
		EnableButtons();
	}

	public void StructureMenuAssignMaterials()
	{
		MatAssignmentIsOn = !MatAssignmentIsOn;
		MenuFunctions.StructureMenuAssignMaterials();
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
		MenuFunctions.StructureMenuAssignSections();
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
		MenuFunctions.StructureMenuAssignSupports();
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
		MenuFunctions.StructureMenuAssignConcLoads();
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
		MenuFunctions.StructureMenuAssignDistLoads();
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
		MenuFunctions.StructureMenuAssignNodalDisps();
		UpperToolbarButton[3].setEnabled(NodalDispsAssignmentIsOn);
		UpperToolbarButton[3].setVisible(NodalDispsAssignmentIsOn);
		UpperToolbarButton[6].setEnabled(NodalDispsAssignmentIsOn);
		UpperToolbarButton[6].setVisible(NodalDispsAssignmentIsOn);
		UpperToolbarButton[7].setEnabled(NodalDispsAssignmentIsOn);
		UpperToolbarButton[7].setVisible(NodalDispsAssignmentIsOn);
	}
	/* ok */
	
	class CustomSizeListsPanel extends JPanel 
	{
		private static final long serialVersionUID = 1L;
		CustomSizeListsPanel(int sizeX, int sizeY) 
	    {
	        setPreferredSize(new Dimension(sizeX, sizeY));	// set a preferred size for the custom panel.
	    }
	    @Override
	    public void paintComponent(Graphics g) 
	    {
	        super.paintComponent(g);
	        DP = new DrawingOnAPanel(g, ListsCanvas, MenuFunctions.Struct.getCenter());
	        MenuFunctions.DrawOnListsPanel(jpLists.getSize(), AllText, Language, new boolean[] {MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn}, DP);
			repaint();
	    }
    }
	class CustomSizeLegendPanel extends JPanel 
	{
		private static final long serialVersionUID = 1L;
		CustomSizeLegendPanel(int sizeX, int sizeY) 
	    {
	        setPreferredSize(new Dimension(sizeX, sizeY));	// set a preferred size for the custom panel.
	    }
	    @Override
	    public void paintComponent(Graphics g) 
	    {
	        super.paintComponent(g);
	        DP = new DrawingOnAPanel(g, LegendCanvas, MenuFunctions.Struct.getCenter());
	        MenuFunctions.DrawOnLegendPanel(jpLegend.getSize(), DP);
			repaint();
	    }
    }
	class CustomSizeLDPanel extends JPanel 
	{
		private static final long serialVersionUID = 1L;
		CustomSizeLDPanel(int sizeX, int sizeY) 
	    {
	        setPreferredSize(new Dimension(sizeX, sizeY));	// set a preferred size for the custom panel.
	    }
	    @Override
	    public void paintComponent(Graphics g) 
	    {
	        super.paintComponent(g);
	        DP = new DrawingOnAPanel(g, LDCanvas, MenuFunctions.Struct.getCenter());
	        MenuFunctions.DrawOnLDPanel(jpLD.getSize(), DP);
			repaint();
	    }
    }
	class CustomSizeMainPanel extends JPanel 
	{
		private static final long serialVersionUID = 1L;
		CustomSizeMainPanel(int sizeX, int sizeY) 
	    {
	        setPreferredSize(new Dimension(sizeX, sizeY));	// set a preferred size for the custom panel.
	    }
	    @Override
	    public void paintComponent(Graphics g) 
	    {
	        super.paintComponent(g);
	        DP = new DrawingOnAPanel(g, MainCanvas, MenuFunctions.Struct.getCenter());
			MenuFunctions.DrawOnMainPanel(jpMain.getSize(), MainPanelPos, MainCanvas.getCenter(), MainCanvas, DP);
			if (MenuFunctions.ShowMousePos)
			{
				double[] mousePos = Util.ConvertToRealCoords(MenuFunctions.MousePos, MainCanvas.getPos(), MainCanvas.getSize(), MainCanvas.getDim());
				xPos.setText(String.valueOf(Util.Round(mousePos[0], 3)));
				yPos.setText(String.valueOf(Util.Round(mousePos[1], 3)));
			}
			repaint();
	    }
    }
	private JPanel initComponents(JPanel jPanel, int[] PanelSize, Color backgcolor, Border border, String PanelName) 
	{     
		if (PanelName.equals("Main"))
		{
	        jPanel = new CustomSizeMainPanel(PanelSize[0], PanelSize[1]);	// we want a Main panel, not a generic JPanel!
		}
		else if (PanelName.equals("Legend"))
		{
	        jPanel = new CustomSizeLegendPanel(PanelSize[0], PanelSize[1]);	// we want a legend panel, not a generic JPanel!
		}
		else if (PanelName.equals("LD"))
		{
	        jPanel = new CustomSizeLDPanel(PanelSize[0], PanelSize[1]);	// we want a LD panel, not a generic JPanel!
		}
		else if (PanelName.equals("Lists"))
		{
	        jPanel = new CustomSizeListsPanel(PanelSize[0], PanelSize[1]);	// we want a Lists panel, not a generic JPanel!
		}
        jPanel.setBackground(backgcolor);
        jPanel.setBorder(border);
        jPanel.addMouseListener(new MouseAdapter() 
        {
			public void mousePressed(MouseEvent evt)
			{	
				if (evt.getButton() == 1)	// Left click
				{
					if (MenuFunctions.StructureCreationIsOn)
					{
						MenuFunctions.StructureCreation(MainPanelPos, MainCanvas);
						EnableButtons();
						updateInstructionPanel();
					}
					if (!MenuFunctions.StructureCreationIsOn)
					{
						StepIsComplete = MenuFunctions.CheckSteps();
						UpperToolbarButton[0].setEnabled(false);
						UpperToolbarButton[0].setVisible(false);
						UpperToolbarButton[1].setEnabled(false);
						UpperToolbarButton[1].setVisible(false);
					}				
					if (MenuFunctions.NodeSelectionIsOn)
					{
						MenuFunctions.NodeAddition(MainCanvas, MainPanelPos);
						if (MenuFunctions.SelectedNodes != null)
						{
							if (-1 < MenuFunctions.SelectedNodes[0])
							{
								ResetEastPanels();
								//AddNodeInfoPanel(MenuFunctions.Node[MenuFunctions.SelectedNodes[0]]);
							}
						}
					}
					if (MenuFunctions.ElemSelectionIsOn)
					{
						MenuFunctions.ElemAddition(MainCanvas, MainPanelPos);
						if (MenuFunctions.SelectedElems != null)
						{
							if (-1 < MenuFunctions.SelectedElems[0])
							{
								ResetEastPanels();
								//AddElemInfoPanel(MenuFunctions.Elem[MenuFunctions.SelectedElems[0]]);
							}
						}
					}
				}
				if (evt.getButton() == 3)	// Right click
				{
					UtilComponents.PrintStructure(StructureMenu.getName(), MenuFunctions.Node, MenuFunctions.Elem, MenuFunctions.MatType, MenuFunctions.SecType, MenuFunctions.Sup, MenuFunctions.ConcLoad, MenuFunctions.DistLoad, MenuFunctions.NodalDisp);
					MenuFunctions.ElemDetailsView();
				}
			}
			public void mouseReleased(MouseEvent evt) 
			{
			    //mouseReleased(evt);
		    }
		});
        jPanel.addMouseMotionListener(new MouseMotionAdapter() 
        {
            public void mouseDragged(MouseEvent evt) 
            {
                //mouseDragged(evt);
            }
        });
        jPanel.addMouseWheelListener(new MouseWheelListener()
        {
			@Override
			public void mouseWheelMoved(MouseWheelEvent evt) 
			{
				MenuFunctions.MouseWheel(MainPanelPos, MainCanvas, evt.getWheelRotation(), new boolean[] {MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn});
				MenuFunctions.updateDiagramScale(MainCanvas, evt.getWheelRotation());
			}       	
        });
        jPanel.addKeyListener(new KeyListener()
        {
			@Override
			public void keyPressed(KeyEvent evt)
			{
				int keyCode = evt.getKeyCode();
				char keychar = evt.getKeyChar();
				char[] ActionKeys = new char[] {'Q', 'W', 'A', 'S', 'Z', 'X','E', 'D', 'C'};	// Q, W, A, S, Z, X: rotation, E: top view, D: front view, C: side view
				if (keychar == Character.toLowerCase(ActionKeys[0]) | keychar == Character.toUpperCase(ActionKeys[0]))
				{
					MainCanvas.setAngles(new double[] {MainCanvas.getAngles()[0] - Math.PI/180.0, MainCanvas.getAngles()[1], MainCanvas.getAngles()[2]});
				}
				if (keychar == Character.toLowerCase(ActionKeys[1]) | keychar == Character.toUpperCase(ActionKeys[1]))
				{
					MainCanvas.setAngles(new double[] {MainCanvas.getAngles()[0] + Math.PI/180.0, MainCanvas.getAngles()[1], MainCanvas.getAngles()[2]});
				}
				if (keychar == Character.toLowerCase(ActionKeys[2]) | keychar == Character.toUpperCase(ActionKeys[2]))
				{
					MainCanvas.setAngles(new double[] {MainCanvas.getAngles()[0], MainCanvas.getAngles()[1] + Math.PI/180.0, MainCanvas.getAngles()[2]});
				}
				if (keychar == Character.toLowerCase(ActionKeys[3]) | keychar == Character.toUpperCase(ActionKeys[3]))
				{
					MainCanvas.setAngles(new double[] {MainCanvas.getAngles()[0], MainCanvas.getAngles()[1] - Math.PI/180.0, MainCanvas.getAngles()[2]});
				}
				if (keychar == Character.toLowerCase(ActionKeys[4]) | keychar == Character.toUpperCase(ActionKeys[4]))
				{
					MainCanvas.setAngles(new double[] {MainCanvas.getAngles()[0], MainCanvas.getAngles()[1], MainCanvas.getAngles()[2] + Math.PI/180.0});
				}
				if (keychar == Character.toLowerCase(ActionKeys[5]) | keychar == Character.toUpperCase(ActionKeys[5]))
				{
					MainCanvas.setAngles(new double[] {MainCanvas.getAngles()[0], MainCanvas.getAngles()[1], MainCanvas.getAngles()[2] - Math.PI/180.0});
				}
				if (keychar == Character.toLowerCase(ActionKeys[6]) | keychar == Character.toUpperCase(ActionKeys[6]))
				{
					MainCanvas.setAngles(new double[] {0, 0, 0});
				}
				if (keychar == Character.toLowerCase(ActionKeys[7]) | keychar == Character.toUpperCase(ActionKeys[7]))
				{
					MainCanvas.setAngles(new double[] {0, -Math.PI/2.0, 0});
				}
				if (keychar == Character.toLowerCase(ActionKeys[8]) | keychar == Character.toUpperCase(ActionKeys[8]))
				{
					MainCanvas.setAngles(new double[] {-Math.PI/2.0, 0, 0});
				}
				if (keyCode == KeyEvent.VK_RIGHT)
				{
					MainCanvas.setDrawingPos(new int[] {MainCanvas.getDrawingPos()[0] + 10, MainCanvas.getDrawingPos()[1], MainCanvas.getDrawingPos()[2]});
				}
				if (keyCode == KeyEvent.VK_LEFT)
				{
					MainCanvas.setDrawingPos(new int[] {MainCanvas.getDrawingPos()[0] - 10, MainCanvas.getDrawingPos()[1], MainCanvas.getDrawingPos()[2]});
				}
				if (keyCode == KeyEvent.VK_UP)
				{
					MainCanvas.setDrawingPos(new int[] {MainCanvas.getDrawingPos()[0], MainCanvas.getDrawingPos()[1] - 10, MainCanvas.getDrawingPos()[2]});
				}
				if (keyCode == KeyEvent.VK_DOWN)
				{
					MainCanvas.setDrawingPos(new int[] {MainCanvas.getDrawingPos()[0], MainCanvas.getDrawingPos()[1] + 10, MainCanvas.getDrawingPos()[2]});
				}
				if (keyCode == KeyEvent.VK_ESCAPE)
				{
					MenuFunctions.SelectedNodes = null;
					MenuFunctions.SelectedElems = null;
				}
			}

			@Override
			public void keyReleased(KeyEvent evt)
			{
				
			}

			@Override
			public void keyTyped(KeyEvent evt)
			{
				
			}        	
        });
        //this.setContentPane(jPanelMain);	// add the component to the frame to see it!
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //pack();
        
        return jPanel;
    }
}
