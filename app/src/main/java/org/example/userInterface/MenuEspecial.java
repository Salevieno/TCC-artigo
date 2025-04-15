package org.example.userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.Main;
import org.example.analysis.Analysis;
import org.example.loading.Force;
import org.example.loading.Loading;
import org.example.mainTCC.InputDTO;
import org.example.mainTCC.MainPanel;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.ElemShape;
import org.example.structure.ElemType;
import org.example.structure.Element;
import org.example.structure.Material;
import org.example.structure.Node;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.utilidades.Util;
import org.example.view.CentralPanel;

public class MenuEspecial extends JMenu
{

	private JMenuItem Star;

	public static Loading createLoading(Structure structure, int ConcLoadConfig, int[] MeshSizes, int SelConcLoad, int SelDistLoad,
										List<Node> selectedNodes, List<Force> ConcLoadType, List<Element> SelectedElems, double[][] distLoadType)
	{
		Loading loading = new Loading() ;

		if (ConcLoadConfig == 1)
		{
			selectedNodes = new ArrayList<Node>();
			if (structure.getMesh().getElements().get(0).getShape().equals(ElemShape.rectangular))
			{
				int nodeID = (MeshSizes[1] / 2 * (MeshSizes[0] + 1) + MeshSizes[0] / 2) ;
				selectedNodes.add(structure.getMesh().getNodes().get(nodeID));
			}
			else if (structure.getMesh().getElements().get(0).getShape().equals(ElemShape.r8))
			{
				int nodeID = (MeshSizes[1] / 2 * (2 * MeshSizes[0] + 1 + MeshSizes[0] + 1) + MeshSizes[0]) ;
				selectedNodes.add(structure.getMesh().getNodes().get(nodeID));
			}
		}
		MainPanel.getInstance().getWestPanel().getListsPanel().setSelectedID(SelConcLoad);
		MainPanel.getInstance().getCentralPanel().AddConcLoads(loading, selectedNodes, ConcLoadType);


		MainPanel.getInstance().getWestPanel().getListsPanel().setSelectedID(SelDistLoad);
		if (-1 < SelDistLoad)
		{
			// SelectedElems = null;
			// for (int elem = 0; elem <= structure.getMesh().getElements().size() - 1; elem += 1)
			// {
			// 	SelectedElems = Util.AddElem(SelectedElems, elem);
			// }
			MainPanel.getInstance().getCentralPanel().AddDistLoads(structure, loading, SelectedElems, distLoadType);
		}

		return loading ;
	}

	public static Structure Especial()
	{
		/* Load input */
		InputDTO inputDTO = Util.LoadEspecialInput("examples/Especial.txt");
		List<Material> materials = new ArrayList<>() ;
		List<Section> sections = new ArrayList<>() ;

		for (int i = 0 ; i <= inputDTO.getInputMatTypes().length - 1 ; i += 1)
		{
			materials.add(new Material(inputDTO.getInputMatTypes()[i][0], inputDTO.getInputMatTypes()[i][1], inputDTO.getInputMatTypes()[i][2])) ;
		}
		
		for (int i = 0 ; i <= inputDTO.getInputSecTypes().length - 1 ; i += 1)
		{
			sections.add(new Section(inputDTO.getInputSecTypes()[i][0])) ;
		}
		List<Force> concLoadTypes = new ArrayList<>() ;
		for (double[] forceType : inputDTO.getConcLoadType())
		{
			concLoadTypes.add(new Force(forceType)) ;
		}
		double[][] distLoadType = inputDTO.getDistLoadType() ;
		int[] SupConfig = inputDTO.getSupConfig() ;
		
		/* Define structure parameters */
		int ConcLoadConfig = 1;
		// int DistLoadConfig = 1;
		
		int[] NumPar = new int[] {inputDTO.getEspecialElemTypes().length, inputDTO.getEspecialMeshSizes().length, materials.size(), sections.size(), SupConfig.length, concLoadTypes.size(), distLoadType.length};	// 0: Elem, 1: Mesh, 2: Mat, 3: Sec, 4: Sup, 5: Conc load, 6: Dist load
		int[] Par = new int[NumPar.length];
		if (concLoadTypes.size() == 0)
		{
			Par[5] = -1;
		}
		if (distLoadType.length == 0)
		{
			Par[6] = -1;
		}
		int NumberOfRuns = Util.ProdVec(NumPar);
		
		ElemType elemType = ElemType.valueOf(inputDTO.getEspecialElemTypes()[Par[0]].toUpperCase());
		String[] Sections = new String[] {"Anâlise		Elem	Malha (nx x ny)	Mat		Sec		Apoio		Carga		Min deslocamento (m)		Max deslocamento (m)		Desl. sob a carga (m)		Tempo (seg)"};
		String[][][] vars = new String[Sections.length][][];
		
		vars[0] = new String[NumberOfRuns][11];
		Structure structure2 = null ;
		for (int run = 0; run <= NumberOfRuns - 1; run += 1)
		{				
			int[] MeshSize = inputDTO.getEspecialMeshSizes()[Par[1]];
			int Mat = Par[2];
			int Sec = Par[3];
			int supConfig = SupConfig[Par[4]];
			int SelConcLoad = Par[5];
			int SelDistLoad = Par[6];
			
			structure2 = Structure.create(inputDTO.getEspecialCoords(), inputDTO.getMeshType(), MeshSize, elemType,
					materials.get(Mat), materials, sections.get(Sec), sections, supConfig) ;
			Loading loading = createLoading(structure2, ConcLoadConfig, MeshSize, SelConcLoad, SelDistLoad,
					structure2.getMesh().getSelectedNodes(), concLoadTypes, structure2.getMesh().getElements(), distLoadType) ;
		
			MenuAnalysis.CalcAnalysisParameters(structure2, loading, concLoadTypes, distLoadType);
			MainPanel.getInstance().getCentralPanel().setStructure(structure2) ;
			MainPanel.getInstance().getCentralPanel().updateDrawings() ;

			// structure2.assignLoads(ConcLoadConfig, MeshSize, SelConcLoad, SelDistLoad) ;
			MainPanel.getInstance().getEastPanel().getLegendPanel().setStructure(structure2) ;

			System.out.println("\nStructure 2");
			System.out.println(structure2.getMesh().toString());
			
			System.out.println("\nloading");
			System.out.println(loading);

			System.out.print("Anâlise num " + run + ": ");

			boolean nonlinearMat = true;
			boolean nonlinearGeo = false;
			Analysis.run(structure2, loading, nonlinearMat, nonlinearGeo, 10, 5, 15.743);

			System.out.println("\nresults structure 2");
			System.out.println(Arrays.toString(structure2.getU()));
			
			/* Analysis is complete */
			MenuAnalysis.PostAnalysis(structure2, nonlinearMat, nonlinearGeo);

			/*vars[0][run][0] = String.valueOf(run) + "	";
			vars[0][run][1] = ElemType + "	";
			vars[0][run][2] = MeshSize[0] + " x " + MeshSize[1] + "	";
			vars[0][run][3] = Mat + "	";
			vars[0][run][4] = Sec + "	";
			vars[0][run][5] = supConfig + "	";
			vars[0][run][6] = ConcLoadConfig + "	";

	        System.out.println(Arrays.toString(ConcLoadNodes));
	        System.out.println(Arrays.deepToString(Struct.getnodeDOF()));
	        System.out.println(Struct.getnodeDOF()[ConcLoadNodes[0]][0]);
			vars[0][run][7] = String.valueOf(Util.FindMinDisps(Struct.getU(), Elem[0].getDOFs(), Analysis.DefineFreeDoFTypes(Node, Elem, Sup, Struct.getnodeDOF()))[0]) + "	";
			vars[0][run][8] = String.valueOf(Util.FindMaxDisps(Struct.getU(), Elem[0].getDOFs(), Analysis.DefineFreeDoFTypes(Node, Elem, Sup, Struct.getnodeDOF()))[0]) + "	";
			if (0 < NumPar[5])
			{
				vars[0][run][9] = String.valueOf(Struct.getU()[Struct.getnodeDOF()[ConcLoadNodes[0]][0]] + "	");
			}
			else
			{
				vars[0][run][9] = String.valueOf("-	");
			}*/
			//vars[0][run][10] = String.valueOf(AnalysisTime / 1000.0);


			for (int par = 0; par <= NumPar.length - 1; par += 1)
			{
				if (Par[par] < NumPar[par] - 1)
				{
					Par[par] += 1;
					for (int i = 0; i <= par - 1; i += 1)
					{
						Par[i] = 0;
					}
					par = NumPar.length - 1;
				}
			}
		}
		MainPanel.getInstance().ActivatePostAnalysisView(structure2);
		MenuBar.getInstance().updateEnabledMenus() ;

		//Re.SaveOutput("Especial", Sections, vars);
		
		return structure2;
	}

    public MenuEspecial()
    {
        this.setText("Especial") ;
        this.setMnemonic(KeyEvent.VK_E) ;

		Star = new JMenuItem("Estrela", KeyEvent.VK_S);
		Star.setForeground(Main.palette[7]);
		this.add(Star);
		Star.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MainPanel.getInstance().getCentralPanel().setStructure(Especial());
				MainPanel.getInstance().ActivatePostAnalysisView(MainPanel.getInstance().getCentralPanel().getStructure());
				MenuBar.getInstance().updateEnabledMenus() ;
			}
		}) ;	

    }
}
