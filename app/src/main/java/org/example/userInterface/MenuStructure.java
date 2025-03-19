package org.example.userInterface;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.mainTCC.MenuFunctions;
import org.example.structure.ElemType;
import org.example.structure.Element;
import org.example.structure.Material;
import org.example.structure.MeshType;
import org.example.structure.Node;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.structure.StructureShape;
import org.example.userInterface.InputDialogs.CreateMeshDialog;
import org.example.userInterface.InputDialogs.DefineElementTypeDialog;
import org.example.userInterface.InputDialogs.InputDialogWithGrid;
import org.example.userInterface.InputDialogs.StructureShapeDialog;
import org.example.userInterface.InputDialogs.InputDialogWithButtons;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

public class MenuStructure extends JMenu
{

    private static final String[] StructureMenuItemsNames = new String[] {
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
    private final JMenuItem DefineElemType = new JMenuItem(StructureMenuItemsNames[0], KeyEvent.VK_E);
    private final JMenu CreateNodes = new JMenu(StructureMenuItemsNames[1]);
    private final static JMenu CreateMesh = new JMenu(StructureMenuItemsNames[2]);
    private final JMenuItem CreateMaterials = new JMenuItem(StructureMenuItemsNames[3], KeyEvent.VK_M);
    private final JMenuItem CreateSections = new JMenuItem(StructureMenuItemsNames[4], KeyEvent.VK_S);
    private final JMenuItem CreateConcLoads = new JMenuItem(StructureMenuItemsNames[5], KeyEvent.VK_C);
    private final JMenuItem CreateDistLoads = new JMenuItem(StructureMenuItemsNames[6], KeyEvent.VK_D);
    private final JMenuItem CreateNodalDisp = new JMenuItem(StructureMenuItemsNames[7]);
    private final static JMenuItem AssignMaterials = new JMenuItem(StructureMenuItemsNames[8]);
    private final static JMenuItem AssignSections = new JMenuItem(StructureMenuItemsNames[9]);
    private final static JMenuItem AssignSupports = new JMenuItem(StructureMenuItemsNames[10]);
    private final static JMenuItem AssignConcLoads = new JMenuItem(StructureMenuItemsNames[11]);
    private final static JMenuItem AssignDistLoads = new JMenuItem(StructureMenuItemsNames[12]);
    private final static JMenuItem AssignNodalDisp = new JMenuItem(StructureMenuItemsNames[13]);

	private static StructureShape structureShape ;
	private static MeshType meshType ;
	private static double[][] createdMaterials ;
	private static double[][] meshData ;

	private static final DefineElementTypeDialog elemTypesInputPanel ;
	private static final StructureShapeDialog structureShapeInputPanel ;
	private static final CreateMeshDialog createMeshInputPanel ;
	// private static final InputDialogWithGrid materialsInputPanel ;

	static
	{
		
		elemTypesInputPanel = new DefineElementTypeDialog();
		structureShapeInputPanel = new StructureShapeDialog();
		createMeshInputPanel = new CreateMeshDialog();

		
		JLabel[] Labels = new JLabel[] {new JLabel ("E (GPa)"), new JLabel ("v"), new JLabel ("fu (MPa)")};		
		ActionWithDoubleArray receiveCreatedMaterials = (double[][] newMaterials) -> createdMaterials = newMaterials;
		Runnable setMainPanelMaterials = () -> {
			for (int i = 0 ; i <= createdMaterials.length - 1 ; i += 1)
			{
				MainPanel.addMaterial(new Material(createdMaterials[i][0], createdMaterials[i][1], createdMaterials[i][2])) ;
			}
			updateEnabledSubMenus();
		} ;
		// materialsInputPanel = new InputDialogWithGrid("Materials", "Mat", Labels, true, receiveCreatedMaterials, setMainPanelMaterials);

	
	}

    public MenuStructure()
    {
        this.setText("Estrutura");
        this.setMnemonic(KeyEvent.VK_S);
    
		DefineElemType.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				elemTypesInputPanel.activate() ;
			}
		});
		CreateMaterials.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				// materialsInputPanel.activate() ;
			}
		});
		CreateSections.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				createSections();
			}
		});
		CreateConcLoads.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				createConcLoads();
			}
		});
		CreateDistLoads.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				createDistLoads();
			}
		});
		CreateNodalDisp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				createNodalDisp();
			}
		});
		AssignMaterials.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Menus.getInstance().getMainPanel().activateMaterialAssignment();
			}
		});
		AssignSections.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Menus.getInstance().getMainPanel().activateSectionAssignment();
			}
		});
		AssignSupports.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Menus.getInstance().getMainPanel().activateSupportAssignment();
			}
		});
		AssignConcLoads.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Menus.getInstance().getMainPanel().activateConcLoadAssignment();
			}
		});
		AssignDistLoads.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Menus.getInstance().getMainPanel().activateDistLoadAssignment();
			}
		});
		AssignNodalDisp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Menus.getInstance().getMainPanel().activateNodalDispAssignment();
			}
		});
		
		CreateMesh.setEnabled(false);
		AssignMaterials.setEnabled(false);
		AssignSections.setEnabled(false);
		AssignSupports.setEnabled(false);
		AssignConcLoads.setEnabled(false);
		AssignDistLoads.setEnabled(false);
		AssignNodalDisp.setEnabled(false);
		DefineElemType.setForeground(Menus.palette[5]);
		CreateNodes.setForeground(Menus.palette[5]);
		CreateMesh.setForeground(Menus.palette[5]);
		CreateMaterials.setForeground(Menus.palette[5]);
		CreateSections.setForeground(Menus.palette[5]);
		CreateConcLoads.setForeground(Menus.palette[5]);
		CreateDistLoads.setForeground(Menus.palette[5]);
		CreateNodalDisp.setForeground(Menus.palette[5]);
		AssignMaterials.setForeground(Menus.palette[5]);
		AssignSections.setForeground(Menus.palette[5]);
		AssignSupports.setForeground(Menus.palette[5]);
		AssignConcLoads.setForeground(Menus.palette[5]);
		AssignDistLoads.setForeground(Menus.palette[5]);
		AssignNodalDisp.setForeground(Menus.palette[5]);
		this.add(DefineElemType);
		this.add(CreateNodes);
		this.add(CreateMesh);
		this.add(CreateMaterials);
		this.add(CreateSections);
		this.add(CreateConcLoads);
		this.add(CreateDistLoads);
		this.add(CreateNodalDisp);
		this.add(AssignMaterials);
		this.add(AssignSections);
		this.add(AssignSupports);
		this.add(AssignConcLoads);
		this.add(AssignDistLoads);
		this.add(AssignNodalDisp);

		/* Defining subitems in the menu CreateNodes */
		String[] CreateNodesMenuNames = new String[] {"Digitar", "Clicar"};
		JMenuItem TypeNodes = new JMenuItem(CreateNodesMenuNames[0], KeyEvent.VK_C);
		JMenuItem ClickNodes = new JMenuItem(CreateNodesMenuNames[1], KeyEvent.VK_C);
		TypeNodes.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				// JLabel[] Labels = new JLabel[] {new JLabel ("x (m)"), new JLabel ("y (m)"), new JLabel ("z (m)")};
				// InputPanelType1 CI = new InputPanelType1("Coordenadas", "No", Labels, false);
				// double[][] StructCoords = CI.retrieveInput();
				// System.out.println(Arrays.deepToString(StructCoords));

				// if (StructCoords != null)
				// {
				// 	updateEnable();

				// 	List<Point3D> structCoordsAsPoints = new ArrayList<Point3D>() ;
				// 	for (int i = 0 ; i <= StructCoords.length - 1 ; i += 1)
				// 	{
				// 		structCoordsAsPoints.add(new Point3D(StructCoords[i][0], StructCoords[i][1], StructCoords[i][2])) ;
				// 	}

				// 	MainPanel.structure.setCoords(structCoordsAsPoints);
				// 	MainPanel.structure.updateCenter() ;
				// 	MainPanel.structure.updateMinCoords() ;
				// 	MainPanel.structure.updateMaxCoords() ;
				// 	Menus.getInstance().getMainCanvas().setDimension(new double[] {1.2 * MainPanel.structure.getMaxCoords().x, 1.2 * MainPanel.structure.getMaxCoords().y, 0});
				// 	Menus.getInstance().getMainCanvas().setDrawingPos(new int[2]);
				// 	Menus.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion() ;
				// }
			}
		});
		ClickNodes.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				structureShapeInputPanel.activate() ;
			}
		});
		TypeNodes.setForeground(Menus.palette[5]);
		ClickNodes.setForeground(Menus.palette[5]);
		CreateNodes.add(ClickNodes);
		CreateNodes.add(TypeNodes);
		
		/* Defining subitems in the menu CreateMesh */
		String[] CreateMeshMenuNames = MeshType.valuesAsString();
		JMenuItem CartesianMesh = new JMenuItem(CreateMeshMenuNames[0], KeyEvent.VK_C);
		JMenuItem RadialMesh = new JMenuItem(CreateMeshMenuNames[1], KeyEvent.VK_R);
		CartesianMesh.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				createMeshInputPanel.activate() ;
			}
		});
		RadialMesh.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				// createMeshInputPanel.activate() ;
			}
		});
		CartesianMesh.setForeground(Menus.palette[5]);
		RadialMesh.setForeground(Menus.palette[5]);
		CreateMesh.add(CartesianMesh);
		CreateMesh.add(RadialMesh);
    }

	public void createSections()
	{
		// JLabel[] Labels = new JLabel[] {new JLabel ("espessura (mm)")};
		// InputPanelType1 CI = new InputPanelType1("Cross sections", "Sec", Labels, true);
		// double[][] createdSections = CI.retrieveInput() ;
		// List<Section> secs = new ArrayList<>() ;
		// for (int i = 0 ; i <= createdSections.length - 1 ; i += 1)
		// {
		// 	secs.add(new Section(createdSections[i][0])) ;
		// }
		// MainPanel.setSections(secs);
		// updateEnable();
	}
	
	public void createConcLoads()
	{
		// JLabel[] Labels = new JLabel[] {new JLabel ("Fx (kN)"), new JLabel ("Fy (kN)"), new JLabel ("Fz (kN)"), new JLabel ("Mx (kNm)"), new JLabel ("My (kNm)"), new JLabel ("Mz (kNm)")};
		// InputPanelType1 CI = new InputPanelType1("Nodal loads", "Nodal load", Labels, true);
		// MainPanel.DefineConcLoadTypes(CI.retrieveInput());
		// updateEnable();
	}
	
	public void createDistLoads()
	{
		// JLabel[] Labels = new JLabel[] {new JLabel ("Load type"), new JLabel ("Load i (kN / kNm)")};
		// InputPanelType1 CI = new InputPanelType1("Member loads", "Member load", Labels, true);
		// MainPanel.DefineDistLoadTypes(CI.retrieveInput());
		// updateEnable();
	}
	
	public void createNodalDisp()
	{
		// JLabel[] Labels = new JLabel[] {new JLabel ("disp x"), new JLabel ("disp y"), new JLabel ("disp z"), new JLabel ("rot x"), new JLabel ("rot y"), new JLabel ("rot z")};
		// InputPanelType1 CI = new InputPanelType1("Nodal displacements", "Nodal disp", Labels, true);
		// MainPanel.DefineNodalDispTypes(CI.retrieveInput());
		// updateEnable();
	}

	public static void updateEnabledSubMenus()
	{
		Structure structure = MainPanel.structure;
		List<Node> nodes = MainPanel.structure.getMesh() != null ? MainPanel.structure.getMesh().getNodes() : null ;
		List<Element> elems = MainPanel.structure.getMesh() != null ? MainPanel.structure.getMesh().getElements() : null;
		boolean AnalysisIsComplete = MenuFunctions.AnalysisIsComplete;
		String SelectedElemType = MenuFunctions.SelectedElemType;
		List<Material> MatTypes = MainPanel.matTypes;
		List<Section> SecTypes = MainPanel.secTypes;
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
		if ( SelectedElemType != null && structure.getCoords() != null)
		{
			CreateMesh.setEnabled(true);
		}
		if (AnalysisIsComplete)
		{
			// DOFNumberView.setEnabled(true);
			// DeformedShape.setEnabled(true);
			// DisplacementContours.setEnabled(true);
			// StressContours.setEnabled(true);
			// StrainContours.setEnabled(true);
			// InternalForcesContours.setEnabled(true);
			Menus.getInstance().getNorthPanel().getUpperToolbar().enableButtonsScale() ;
			// for (int i = 0; i <= elems.get(0).getStrainTypes().length - 1; i += 1)
			// {
			// 	if (elems.get(0).getStrainTypes()[i] <= 5)
			// 	{
			// 		SubMenuStresses[elems.get(0).getStrainTypes()[i]].setEnabled(true);
			// 		SubMenuStrains[elems.get(0).getStrainTypes()[i]].setEnabled(true);
			// 	}
			// }
			// SaveResults.setEnabled(true);
			// SaveLoadDispCurve.setEnabled(true);
		}
	}
	
}
