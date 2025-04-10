package org.example.mainTCC;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.analysis.Analysis;
import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.Force;
import org.example.loading.Loading;
import org.example.loading.NodalDisp;
import org.example.output.SaveOutput;
import org.example.service.MenuViewService;
import org.example.structure.ElemShape;
import org.example.structure.ElemType;
import org.example.structure.Element;
import org.example.structure.Material;
import org.example.structure.Node;
import org.example.structure.Reactions;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.structure.Supports;
import org.example.userInterface.MenuBar;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.CentralPanel;

public abstract class MenuFunctions
{

	public static Point mousePos;
	public static boolean AnalysisIsComplete;
	
	public static double[] DiagramScales;
	
	public static String SelectedElemType;
	public static List<Force> concLoadTypes ;
	public static double[][] DistLoadType, NodalDispType;
	public static int[][] SupType;
	public static boolean NonlinearMat;
	public static boolean NonlinearGeo;
	
	public static Point ElemSelectionWindowInitialPos;
	private static MenuViewService view = MenuViewService.getInstance() ;
	
	static
	{
		mousePos = new Point();
		
		SupType = Supports.Types;
		
		NonlinearMat = false;
		NonlinearGeo = false;
		
		AnalysisIsComplete = false;

		DiagramScales = new double[2];		

	    ElemSelectionWindowInitialPos = new Point();
	}

	public static Point GetRelMousePos(int[] PanelPos)
 	{
		return new Point(MouseInfo.getPointerInfo().getLocation().x - PanelPos[0], MouseInfo.getPointerInfo().getLocation().y - PanelPos[1]) ;
 	}

	public static void updateMousePosRelToPanelPos(int[] panelPos) { mousePos = GetRelMousePos(panelPos) ;}


	public static boolean CheckIfAnalysisIsReady(Structure structure, Loading loading)
	{
		if (structure.getMesh() != null &&
			structure.getMesh().getNodes() != null && structure.getMesh().getElements() != null &&
			structure.getMesh().allElementsHaveMat() && structure.getMesh().allElementsHaveSec() && structure.getSupports() != null &&
			loading != null)
		{
			return true;
		}
		
		return false;
		
	}

	/* Results toolbar functions */
	
	public static Structure LoadFile(String Path, String FileName)
	{
		if (!FileName.equals("")) 
		{
			Structure structure = new Structure(null, null, null);
			String[][] Input = ReadInput.ReadTxtFile(Path + FileName + ".txt");			// Loads the input file (.txt)
			if (Input != null)
			{
				structure.setName(Input[0][2]);
				List<Point3D> StructCoords = new ArrayList<>() ;
				for (int coord = 0; coord <= Input[1].length - 4; coord += 1)
				{
					String[] Line = Input[1][coord + 2].split("	");
					StructCoords.add(new Point3D(Double.parseDouble(Line[0]), Double.parseDouble(Line[1]), Double.parseDouble(Line[2]))) ;
				}
				structure.setCoords(StructCoords);
				structure.updateCenter() ;
				structure.resetMesh();

				List<Material> matTypes = new ArrayList<>() ;
				for (int mat = 0; mat <= Input[4].length - 4; mat += 1)
				{
					String[] Line = Input[4][mat + 2].split("	");
					Material newMaterial = new Material(Double.parseDouble(Line[1]), Double.parseDouble(Line[2]), Double.parseDouble(Line[3])) ;
					matTypes.add(newMaterial) ;
//					MatType[mat] = new double[] {Double.parseDouble(Line[1]), Double.parseDouble(Line[2]), Double.parseDouble(Line[3])};
					
				}
				List<Section> secTypes = new ArrayList<>() ;
				for (int sec = 0; sec <= Input[5].length - 4; sec += 1)
				{
					String[] Line = Input[5][sec + 2].split("	");
					secTypes.add(new Section(Double.parseDouble(Line[1]))) ;
				}
				
				for (int node = 0; node <= Input[2].length - 4; node += 1)
				{
					String[] Line = Input[2][node + 2].split("	");
					Node NewNode;
					NewNode = new Node(-1, null);
					NewNode.setID(Integer.parseInt(Line[0]));
					NewNode.setOriginalCoords(new Point3D(Double.parseDouble(Line[1]), Double.parseDouble(Line[2]), Double.parseDouble(Line[3])));
					structure.getMesh().getNodes().add(NewNode) ;
				}
				for (int elem = 0; elem <= Input[3].length - 4; elem += 1)
				{
					String[] Line = Input[3][elem + 2].split("	");
					ElemType elemType = ElemType.valueOf(Line[1].toUpperCase()) ;
					Element NewElem = new Element(null, null, null, null, elemType);
					int NumberOfElemNodes = Element.shapeToNumberNodes(NewElem.getShape(), elemType);
					int[] ElemNodes = null;
					for (int elemnode = 0; elemnode <= NumberOfElemNodes - 1; elemnode += 1)
					{
						ElemNodes = Util.AddElem(ElemNodes, Integer.parseInt(Line[elemnode + 2]));
					}
					NewElem.setExternalNodes(structure.getMesh().getNodesByID(ElemNodes)) ;
					NewElem.setMat(matTypes.get(Integer.parseInt(Line[NumberOfElemNodes + 2])));
					NewElem.setSec(secTypes.get(Integer.parseInt(Line[NumberOfElemNodes + 3])));
					structure.getMesh().getElements().add(NewElem) ;
				}
				
				for (int sup = 0; sup <= Input[6].length - 4; sup += 1)
				{
					String[] Line = Input[6][sup + 2].split("	");
					Supports NewSup;
					NewSup = new Supports(-1, -1, null);
					NewSup.setID(Integer.parseInt(Line[0]));
					NewSup.setNode(Integer.parseInt(Line[1]));
					NewSup.setDoFs(new int[] {Integer.parseInt(Line[2]), Integer.parseInt(Line[3]), Integer.parseInt(Line[4]), Integer.parseInt(Line[5]), Integer.parseInt(Line[6]), Integer.parseInt(Line[7])});
					structure.addSupport(NewSup);
					structure.getMesh().getNodes().get(Integer.parseInt(Line[1])).setSup(NewSup.getDoFs());
				}
				for (int concload = 0; concload <= Input[7].length - 4; concload += 1)
				{
					String[] Line = Input[7][concload + 2].split("	");
					ConcLoad NewConcLoad;
					NewConcLoad = new ConcLoad(new Force());
					NewConcLoad.setId(Integer.parseInt(Line[0]));
					// NewConcLoad.setNodeID(Integer.parseInt(Line[1]));
					NewConcLoad.setForce(new Force(new double[] {Double.parseDouble(Line[2]), Double.parseDouble(Line[3]), Double.parseDouble(Line[4]), Double.parseDouble(Line[5]), Double.parseDouble(Line[6]), Double.parseDouble(Line[7])}));
					concLoadTypes.add(NewConcLoad.getForce()) ;
					//  = Util.AddElem(concLoadTypes, new double[] {NewConcLoad.getNodeID(), NewConcLoad.getForce()[0], NewConcLoad.getForce()[1], NewConcLoad.getForce()[2], NewConcLoad.getForce()[3], NewConcLoad.getForce()[4], NewConcLoad.getForce()[5]});
					CentralPanel.loading.addConcLoad(NewConcLoad);
				}
				for (int distload = 0; distload <= Input[8].length - 4; distload += 1)
				{
					String[] Line = Input[8][distload + 2].split("	");
					DistLoad NewDistLoad;
					NewDistLoad = new DistLoad(-1, -1);
					NewDistLoad.setId(Integer.parseInt(Line[0]));
					// NewDistLoad.setElem(Integer.parseInt(Line[1]));
					NewDistLoad.setType(Integer.parseInt(Line[2]));
					NewDistLoad.setIntensity(Double.parseDouble(Line[3]));
					DistLoadType = Util.AddElem(DistLoadType, new double[] {-1, NewDistLoad.getType(), NewDistLoad.getIntensity()});
					CentralPanel.loading.addDistLoad(NewDistLoad);
				}
				for (int nodaldisp = 0; nodaldisp <= Input[9].length - 4; nodaldisp += 1)
				{
					String[] Line = Input[9][nodaldisp + 2].split("	");
					NodalDisp NewNodalDisp;
					NewNodalDisp = new NodalDisp(-1, -1, null);
					NewNodalDisp.setID(Integer.parseInt(Line[0]));
					NewNodalDisp.setNode(Integer.parseInt(Line[1]));
					NewNodalDisp.setDisps(new double[] {Double.parseDouble(Line[2]), Double.parseDouble(Line[3]), Double.parseDouble(Line[4]), Double.parseDouble(Line[5]), Double.parseDouble(Line[6]), Double.parseDouble(Line[7])});
					NodalDispType = Util.AddElem(NodalDispType, new double[] {NewNodalDisp.getNode(), NewNodalDisp.getDisps()[0], NewNodalDisp.getDisps()[1], NewNodalDisp.getDisps()[2], NewNodalDisp.getDisps()[3], NewNodalDisp.getDisps()[4], NewNodalDisp.getDisps()[5]});
					CentralPanel.loading.addNodalDisp(NewNodalDisp);
				}

				System.out.println("Structure loaded successfully");
				structure.printStructure(matTypes, secTypes, structure.getSupports(), CentralPanel.loading);
				return structure ;
			}
			else
			{
				System.out.println("Arquivo de input nâo encontrado");
				return null ;
			}
		}
		else
		{
			System.out.println("Arquivo de input nâo encontrado");
			return null ;
		}
	}
	
	
	/* Analysis menu functions */
	public static void CalcAnalysisParameters(Structure structure, Loading loading)
	{
		structure.calcAndAssignDOFs() ;
		structure.getMesh().assignElementDOFs() ;
		for (Element elem : structure.getMesh().getElements())
		{
			int[][] ElemNodeDOF = null;
			for (Node node : elem.getExternalNodes())
	    	{
				ElemNodeDOF = Util.AddElem(ElemNodeDOF, structure.getMesh().getNodes().get(node.getID()).getDofs());
	    	}
			elem.setNodeDOF(ElemNodeDOF);
		}
		if (concLoadTypes != null)
		{
			for (int loadid = 0; loadid <= concLoadTypes.size() - 1; loadid += 1)
			{
				int nodeid = (int) concLoadTypes.get(loadid).y;
				if (-1 < nodeid)
				{
					loading.getConcLoads().set(loadid, new ConcLoad(concLoadTypes.get(loadid))) ;
					structure.getMesh().getNodes().get(nodeid).addConcLoad(loading.getConcLoads().get(loadid));
				}
			}
		}

		// for (DistLoad distLoad : loading.getDistLoads())
		// {
		// 	int elemid = distLoad.getElem() ;
		// 	if (-1 < elemid)
		// 	{
		// 		struct.getMesh().getElements().get(elemid).addDistLoad(distLoad) ;
		// 	}
		// }

		if (DistLoadType != null)
		{
			for (int loadid = 0; loadid <= DistLoadType.length - 1; loadid += 1)
			{
				int elemid = (int) DistLoadType[loadid][0];
				int LoadType = (int) DistLoadType[loadid][1];
				double Intensity = DistLoadType[loadid][2];
				if (-1 < elemid)
				{
					DistLoad distLoad = new DistLoad(LoadType, Intensity) ;
					loading.getDistLoads().set(loadid, distLoad) ;
					structure.getMesh().getElements().get(elemid).addDistLoad(distLoad);
				}
			}
		}
	}

	public static void PostAnalysis(Structure structure)
	{
		/* Record results and set up their view*/
		structure.setU(structure.getU());
		Reactions[] reactions = Analysis.Reactions(structure.getMesh(), structure.getSupports(), NonlinearMat, NonlinearGeo, structure.getU());
		structure.setReactions(reactions);
		for (Element elem : structure.getMesh().getElements())
		{
			elem.RecordResults(structure.getMesh().getNodes(), structure.getU(), NonlinearMat, NonlinearGeo);
		}
		structure.getResults().register(structure.getMesh(), structure.getSupports(), structure.getU(), NonlinearMat, NonlinearGeo);
		System.out.println(structure.getResults()) ;

		view.nodes = true;
		view.elems = true;
		view.reactionArrows = true;
		view.deformedStructure = true;

		CentralPanel.nodeSelectionIsActive = true;
		CentralPanel.elemSelectionIsActive = true;
		AnalysisIsComplete = true;
		DiagramScales[1] = 1;
		double MaxDisp = Util.FindMaxAbs(structure.getU());
		if (0 < MaxDisp)
		{
			DiagramScales[1] = 1 / MaxDisp;
		}
		Reactions.setSumReactions(Analysis.SumReactions(reactions));

		MainPanel.getInstance().getWestPanel().setStructure(structure) ;
		CentralPanel.activateNodeSelection() ;
		CentralPanel.deactivateElemSelection() ;
				
		System.out.println("Max disp: " + MaxDisp);
	}
	
	/* Results menu functions */
	public static void ResultsMenuSaveResults(Structure structure)
	{
		String[] Sections = new String[] {"Min Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Max Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Min Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Max Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Reacoes \nno	Fx (kN)	Fy (kN)	Fz (kN)	Mx (kNm)	My (kNm)	Mz (kNm)", "Soma das reacoes \nFx (kN)	Fy (kN)	Fz (kN)	Mx (kNm)	My (kNm)	Mz (kNm)"};
		String[][][] vars = new String[Sections.length][][];
		vars[0] = new String[1][3];
		vars[1] = new String[1][3];
		vars[2] = new String[structure.getMesh().getNodes().size()][];
		vars[3] = new String[1][3];
		vars[4] = new String[1][3];
		vars[5] = new String[structure.getMesh().getNodes().size()][];
		vars[6] = new String[structure.getReactions().length][];
		vars[7] = new String[1][Reactions.SumReactions.length];
		for (int sec = 0; sec <= Sections.length - 1; sec += 1)
		{
			if (sec == 0)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(structure.getResults().getDispMin()[dof]);
				}
			}
			if (sec == 1)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(structure.getResults().getDispMax()[dof]);
				}
			}
			if (sec == 3)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(structure.getResults().getInternalForcesMin()[dof]);
				}
			}
			if (sec == 4)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(structure.getResults().getInternalForcesMax()[dof]);
				}
			}
			for (int node = 0; node <= vars[sec].length - 1; node += 1)
			{
				if (sec == 2)
				{
					vars[sec][node] = new String[structure.getMesh().getNodes().get(node).getDOFType().length + 1];
					vars[sec][node][0] = String.valueOf(node);
					for (int dof = 0; dof <= structure.getMesh().getNodes().get(node).getDOFType().length - 1; dof += 1)
					{
						if (-1 < structure.getMesh().getNodes().get(node).getDofs()[dof])
						{
							vars[sec][node][dof + 1] = String.valueOf(structure.getU()[structure.getMesh().getNodes().get(node).getDofs()[dof]]);
						}
						else
						{
							vars[sec][node][dof + 1] = String.valueOf(0);
						}
					}
				}
				if (sec == 5)
				{
					vars[sec][node] = new String[structure.getMesh().getNodes().get(node).getDOFType().length + 1];
					vars[sec][node][0] = String.valueOf(node);
					for (int i = 0; i <= structure.getMesh().getElements().size() - 1; i += 1)
					{
						Element elem = structure.getMesh().getElements().get(i);
						for (int elemnode = 0; elemnode <= elem.getExternalNodes().size() - 1; elemnode += 1)
						{
							if (node == elem.getExternalNodes().get(elemnode).getID())
							{
								for (int dof = 0; dof <= structure.getMesh().getNodes().get(node).getDOFType().length - 1; dof += 1)
								{
									vars[sec][node][dof + 1] = String.valueOf(elem.getIntForces()[elemnode*structure.getMesh().getNodes().get(node).getDOFType().length + dof]);
								}
							}
						}
					}
				}
				if (sec == 6)
				{
					vars[sec][node] = new String[structure.getReactions()[node].getLoads().length + 1];
					vars[sec][node][0] = String.valueOf(node);
					for (int dof = 0; dof <= structure.getReactions()[node].getLoads().length - 1; dof += 1)
					{
						vars[sec][node][dof + 1] = String.valueOf(structure.getReactions()[node].getLoads()[dof]);
					}
				}
			}
			if (sec == 7)
			{
				for (int dof = 0; dof <= Reactions.SumReactions.length - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(Reactions.SumReactions[dof]);
				}
			}
		}
		SaveOutput.SaveOutput(structure.getName(), Sections, vars);
	}
	
	public static void SaveLoadDispCurve(Structure structure)
	{
		if (-1 < CentralPanel.SelectedVar)
		{
			int nodeid = structure.getMesh().getSelectedNodes().get(0).getID();
			double[][][] loaddisp = structure.getMesh().getNodes().get(nodeid).getLoadDisp();
			String[] Sections = new String[] {"Deslocamentos", "Fator de carga"};
			String[][][] vars = new String[Sections.length][structure.getMesh().getNodes().get(nodeid).getDofs().length][loaddisp[0][0].length];
			for (int sec = 0; sec <= Sections.length - 1; sec += 1)
			{
				for (int dof = 0; dof <= structure.getMesh().getNodes().get(nodeid).getDofs().length - 1; dof += 1)
				{
					for (int loadinc = 0; loadinc <= loaddisp[dof][sec].length - 1; loadinc += 1)
					{
						vars[sec][dof][loadinc] = String.valueOf(loaddisp[dof][sec][loadinc]);
					}
				}
			}
			SaveOutput.SaveOutput(structure.getName(), Sections, vars);
		}
	}

	/* Especial menu functions */
	
	public static Loading createLoading(Structure structure, int ConcLoadConfig, int[] MeshSizes, int SelConcLoad, int SelDistLoad,
										List<Node> selectedNodes, List<Force> ConcLoadType, List<Element> SelectedElems, double[][] DistLoadType)
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
			MainPanel.getInstance().getCentralPanel().AddDistLoads(structure, loading, SelectedElems, DistLoadType);
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
		concLoadTypes = new ArrayList<>() ;
		for (double[] forceType : inputDTO.getConcLoadType())
		{
			concLoadTypes.add(new Force(forceType)) ;
		}
		DistLoadType = inputDTO.getDistLoadType() ;
		int[] SupConfig = inputDTO.getSupConfig() ;
		
		/* Define structure parameters */
		int ConcLoadConfig = 1;
		// int DistLoadConfig = 1;
		
		int[] NumPar = new int[] {inputDTO.getEspecialElemTypes().length, inputDTO.getEspecialMeshSizes().length, materials.size(), sections.size(), SupConfig.length, concLoadTypes.size(), DistLoadType.length};	// 0: Elem, 1: Mesh, 2: Mat, 3: Sec, 4: Sup, 5: Conc load, 6: Dist load
		int[] Par = new int[NumPar.length];
		if (concLoadTypes.size() == 0)
		{
			Par[5] = -1;
		}
		if (DistLoadType.length == 0)
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
					structure2.getMesh().getSelectedNodes(), MenuFunctions.concLoadTypes, structure2.getMesh().getElements(), MenuFunctions.DistLoadType) ;
		
			MenuFunctions.CalcAnalysisParameters(structure2, loading);
			CentralPanel.structure = structure2 ;
			MainPanel.getInstance().getCentralPanel().updateDrawings() ;

			// structure2.assignLoads(ConcLoadConfig, MeshSize, SelConcLoad, SelDistLoad) ;
			MainPanel.getInstance().getEastPanel().getLegendPanel().setStructure(structure2) ;

			System.out.println("\nStructure 2");
			System.out.println(structure2.getMesh().toString());
			
			System.out.println("\nloading");
			System.out.println(loading);

			System.out.print("Anâlise num " + run + ": ");

			boolean NonlinearMat = true;
			boolean NonlinearGeo = false;
			Analysis.run(structure2, loading, NonlinearMat, NonlinearGeo, 10, 5, 15.743);

			System.out.println("\nresults structure 2");
			System.out.println(Arrays.toString(structure2.getU()));
			
			/* Analysis is complete */
			PostAnalysis(structure2);

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

	public static void RunExample(int exampleID)
	{
		
		Structure structure = new Structure(null, null, null);
		CentralPanel.loading.clearLoads() ;
		MainPanel.getInstance().getCentralPanel().resetDisplay() ;
		view.reset() ;
		reset() ;
		if (exampleID == 0)
		{
 			structure = LoadFile(".\\Exemplos\\", "0-KR1");
		}
 		if (exampleID == 1)
		{
			structure = LoadFile(".\\Exemplos\\", "1-KR2");
		}
 		if (exampleID == 2)
		{
			structure = LoadFile(".\\Exemplos\\", "2-MR1");
		}
 		if (exampleID == 3)
		{
			structure = LoadFile(".\\Exemplos\\", "3-MR2");
		}
 		if (exampleID == 4)
 		{
			structure = LoadFile(".\\Exemplos\\", "4-R4");
 		}
 		if (exampleID == 5)
 		{
			structure = LoadFile(".\\Exemplos\\", "5-Q4");
 		}
 		if (exampleID == 6)
 		{
			structure = LoadFile(".\\Exemplos\\", "6-T3G");
 		}
 		if (exampleID == 7)
 		{
			structure = LoadFile(".\\Exemplos\\", "7-T6G");
 		}
 		if (exampleID == 8)
 		{
			structure = LoadFile(".\\Exemplos\\", "8-SM");
 		}
 		if (exampleID == 9)
 		{
			structure = LoadFile(".\\Exemplos\\", "9-SM8");
 		}
 		if (exampleID == 10)
 		{
			structure = LoadFile(".\\Exemplos\\", "10-KP3");
 		}
 		if (exampleID == 11)
 		{
			structure = LoadFile(".\\Exemplos\\", "11-SM_C");
 		}
 		if (exampleID == 12)
 		{
			structure = LoadFile(".\\Exemplos\\", "12-SM_H");
 		}
 		if (exampleID == 13)
 		{
			structure = LoadFile(".\\Exemplos\\", "13-vigadeaco");
 		}
		CentralPanel.structure = structure ;
 		CalcAnalysisParameters(CentralPanel.structure, CentralPanel.loading);
		long AnalysisTime = System.currentTimeMillis();
		Analysis.run(CentralPanel.structure, CentralPanel.loading, NonlinearMat, NonlinearGeo, 1, 1, 1);
		PostAnalysis(CentralPanel.structure);
		AnalysisTime = System.currentTimeMillis() - AnalysisTime;
		for (Element elem : CentralPanel.structure.getMesh().getElements())
		{
			elem.RecordResults(CentralPanel.structure.getMesh().getNodes(), CentralPanel.structure.getU(), NonlinearMat, NonlinearGeo);
        	elem.setDeformedCoords(CentralPanel.structure.getMesh().getNodes());
		}
		CentralPanel.structure.getResults().register(CentralPanel.structure.getMesh(), CentralPanel.structure.getSupports(), CentralPanel.structure.getU(), NonlinearMat, NonlinearGeo);
		CentralPanel.nodeSelectionIsActive = true;
		CentralPanel.elemSelectionIsActive = true;

		AnalysisIsComplete = true;

		view.reactionArrows = true;
		view.reactionValues = true;
		view.deformedStructure = true;

		double MaxDisp = Util.FindMaxAbs(CentralPanel.structure.getU());
		if (0 < MaxDisp)
		{
			DiagramScales[1] = 1 / MaxDisp;
		}
		else
		{
			DiagramScales[1] = 1;
		}
	}

	
	public static void updateDiagramScale(MyCanvas MainCanvas, int WheelRot)
	{
		if (AnalysisIsComplete && !Util.MouseIsInsideCanvas(mousePos, MainCanvas))
		{
			DiagramScales[1] += -Util.Round(10 * Math.log10(DiagramScales[1]) * WheelRot, 10);
			DiagramScales[1] = Math.max(DiagramScales[1], 0.001);
		}
	}

	public static void reset()
	{
		concLoadTypes = null ;
		DistLoadType = null ;
		NodalDispType = null ;
		AnalysisIsComplete = false;
	}
}
