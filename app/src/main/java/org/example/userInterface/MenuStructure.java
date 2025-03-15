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
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

public class MenuStructure extends JMenu
{

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
    JMenuItem DefineElemType = new JMenuItem(StructureMenuItemsNames[0], KeyEvent.VK_E);
    JMenu CreateNodes = new JMenu(StructureMenuItemsNames[1]);
    JMenu CreateMesh = new JMenu(StructureMenuItemsNames[2]);
    JMenuItem CreateMaterials = new JMenuItem(StructureMenuItemsNames[3], KeyEvent.VK_M);
    JMenuItem CreateSections = new JMenuItem(StructureMenuItemsNames[4], KeyEvent.VK_S);
    JMenuItem CreateConcLoads = new JMenuItem(StructureMenuItemsNames[5], KeyEvent.VK_C);
    JMenuItem CreateDistLoads = new JMenuItem(StructureMenuItemsNames[6], KeyEvent.VK_D);
    JMenuItem CreateNodalDisp = new JMenuItem(StructureMenuItemsNames[7]);
    JMenuItem AssignMaterials = new JMenuItem(StructureMenuItemsNames[8]);
    JMenuItem AssignSections = new JMenuItem(StructureMenuItemsNames[9]);
    JMenuItem AssignSupports = new JMenuItem(StructureMenuItemsNames[10]);
    JMenuItem AssignConcLoads = new JMenuItem(StructureMenuItemsNames[11]);
    JMenuItem AssignDistLoads = new JMenuItem(StructureMenuItemsNames[12]);
    JMenuItem AssignNodalDisp = new JMenuItem(StructureMenuItemsNames[13]);

    public MenuStructure()
    {
        this.setText("Estrutura");
        this.setMnemonic(KeyEvent.VK_S);
    
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
				Menus.getInstance().getInstructionsPanel().updateStepsCompletion() ;
				updateEnable();
			}
		});
		CreateMaterials.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				createMaterials();
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
				JLabel[] Labels = new JLabel[] {new JLabel ("x (m)"), new JLabel ("y (m)"), new JLabel ("z (m)")};
				InputPanelType1 CI = new InputPanelType1("Coordenadas", "No", Labels, false);
				double[][] StructCoords = CI.retrieveInput();
				System.out.println(Arrays.deepToString(StructCoords));

				if (StructCoords != null)
				{
					updateEnable();

					List<Point3D> structCoordsAsPoints = new ArrayList<Point3D>() ;
					for (int i = 0 ; i <= StructCoords.length - 1 ; i += 1)
					{
						structCoordsAsPoints.add(new Point3D(StructCoords[i][0], StructCoords[i][1], StructCoords[i][2])) ;
					}

					MainPanel.structure.setCoords(structCoordsAsPoints);
					MainPanel.structure.updateCenter() ;
					MainPanel.structure.updateMinCoords() ;
					MainPanel.structure.updateMaxCoords() ;
					Menus.getMainCanvas().setDimension(new double[] {1.2 * MainPanel.structure.getMaxCoords().x, 1.2 * MainPanel.structure.getMaxCoords().y, 0});
					Menus.getMainCanvas().setDrawingPos(new int[2]);
					Menus.getInstance().getInstructionsPanel().updateStepsCompletion() ;
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
				// InputPanelType2 CIT = new InputPanelType2("Structure shape", Buttons);
				// String input = CIT.run() ;
				// MainPanel.CreateStructureOnClick(StructureShape.valueOf(input));

				MenuFunctions.SnipToGridIsOn = false;
				Menus.getInstance().UpperToolbarButton[0].setEnabled(true);
				Menus.getInstance().UpperToolbarButton[0].setVisible(true);
				Menus.getInstance().getInstructionsPanel().updateStepsCompletion() ;
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
		CartesianMesh.setForeground(Menus.palette[5]);
		RadialMesh.setForeground(Menus.palette[5]);
		CreateMesh.add(CartesianMesh);
		CreateMesh.add(RadialMesh);
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
		Menus.getInstance().getInstructionsPanel().updateStepsCompletion() ;
		updateEnable();
	}

    
	public void createMaterials()
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
		updateEnable();
	}   

	public void createSections()
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
		updateEnable();
	}
	
	public void createConcLoads()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("Fx (kN)"), new JLabel ("Fy (kN)"), new JLabel ("Fz (kN)"), new JLabel ("Mx (kNm)"), new JLabel ("My (kNm)"), new JLabel ("Mz (kNm)")};
		InputPanelType1 CI = new InputPanelType1("Nodal loads", "Nodal load", Labels, true);
		MainPanel.DefineConcLoadTypes(CI.retrieveInput());
		updateEnable();
	}
	
	public void createDistLoads()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("Load type"), new JLabel ("Load i (kN / kNm)")};
		InputPanelType1 CI = new InputPanelType1("Member loads", "Member load", Labels, true);
		MainPanel.DefineDistLoadTypes(CI.retrieveInput());
		updateEnable();
	}
	
	public void createNodalDisp()
	{
		JLabel[] Labels = new JLabel[] {new JLabel ("disp x"), new JLabel ("disp y"), new JLabel ("disp z"), new JLabel ("rot x"), new JLabel ("rot y"), new JLabel ("rot z")};
		InputPanelType1 CI = new InputPanelType1("Nodal displacements", "Nodal disp", Labels, true);
		MainPanel.DefineNodalDispTypes(CI.retrieveInput());
		updateEnable();
	}

	public void StructureMenuAssignMaterials()
	{
		Menus.getInstance().MatAssignmentIsOn = !Menus.getInstance().MatAssignmentIsOn;
		MainPanel.StructureMenuAssignMaterials();
		Menus.getInstance().UpperToolbarButton[2].setEnabled(Menus.getInstance().MatAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[2].setVisible(Menus.getInstance().MatAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setEnabled(Menus.getInstance().MatAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setVisible(Menus.getInstance().MatAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setEnabled(Menus.getInstance().MatAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setVisible(Menus.getInstance().MatAssignmentIsOn);
	}
	
	public void StructureMenuAssignSections()
	{
		Menus.getInstance().SecAssignmentIsOn = !Menus.getInstance().SecAssignmentIsOn;
		MainPanel.StructureMenuAssignSections();
		Menus.getInstance().UpperToolbarButton[2].setEnabled(Menus.getInstance().SecAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[2].setVisible(Menus.getInstance().SecAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setEnabled(Menus.getInstance().SecAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setVisible(Menus.getInstance().SecAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setEnabled(Menus.getInstance().SecAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setVisible(Menus.getInstance().SecAssignmentIsOn);
	}
	
	public void StructureMenuAssignSupports()
	{
		Menus.getInstance().SupAssignmentIsOn = !Menus.getInstance().SupAssignmentIsOn;
		MainPanel.StructureMenuAssignSupports();
		Menus.getInstance().UpperToolbarButton[3].setEnabled(Menus.getInstance().SupAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[3].setVisible(Menus.getInstance().SupAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setEnabled(Menus.getInstance().SupAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setVisible(Menus.getInstance().SupAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setEnabled(Menus.getInstance().SupAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setVisible(Menus.getInstance().SupAssignmentIsOn);
	}
	
	public void StructureMenuAssignConcLoads()
	{
		Menus.getInstance().ConcLoadsAssignmentIsOn = !Menus.getInstance().ConcLoadsAssignmentIsOn;
		MainPanel.StructureMenuAssignConcLoads();
		Menus.getInstance().UpperToolbarButton[3].setEnabled(Menus.getInstance().ConcLoadsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[3].setVisible(Menus.getInstance().ConcLoadsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setEnabled(Menus.getInstance().ConcLoadsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setVisible(Menus.getInstance().ConcLoadsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setEnabled(Menus.getInstance().ConcLoadsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setVisible(Menus.getInstance().ConcLoadsAssignmentIsOn);
	}
	
	public void StructureMenuAssignDistLoads()
	{
		Menus.getInstance().DistLoadsAssignmentIsOn = !Menus.getInstance().DistLoadsAssignmentIsOn;
		MainPanel.StructureMenuAssignDistLoads();
		Menus.getInstance().UpperToolbarButton[2].setEnabled(Menus.getInstance().DistLoadsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[2].setVisible(Menus.getInstance().DistLoadsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setEnabled(Menus.getInstance().DistLoadsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setVisible(Menus.getInstance().DistLoadsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setEnabled(Menus.getInstance().DistLoadsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setVisible(Menus.getInstance().DistLoadsAssignmentIsOn);
	}
	
	public void StructureMenuAssignNodalDisp()
	{
		Menus.getInstance().NodalDispsAssignmentIsOn = !Menus.getInstance().NodalDispsAssignmentIsOn;
		MainPanel.StructureMenuAssignNodalDisps();
		Menus.getInstance().UpperToolbarButton[3].setEnabled(Menus.getInstance().NodalDispsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[3].setVisible(Menus.getInstance().NodalDispsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setEnabled(Menus.getInstance().NodalDispsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[6].setVisible(Menus.getInstance().NodalDispsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setEnabled(Menus.getInstance().NodalDispsAssignmentIsOn);
		Menus.getInstance().UpperToolbarButton[7].setVisible(Menus.getInstance().NodalDispsAssignmentIsOn);
	}

	public void updateEnable()
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
			// DOFNumberView.setEnabled(true);
			// DeformedShape.setEnabled(true);
			// DisplacementContours.setEnabled(true);
			// StressContours.setEnabled(true);
			// StrainContours.setEnabled(true);
			// InternalForcesContours.setEnabled(true);
			Menus.getInstance().UpperToolbarButton[4].setEnabled(true);
			Menus.getInstance().UpperToolbarButton[4].setVisible(true);
			Menus.getInstance().UpperToolbarButton[5].setEnabled(true);
			Menus.getInstance().UpperToolbarButton[5].setVisible(true);
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
