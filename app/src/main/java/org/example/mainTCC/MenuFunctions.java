package org.example.mainTCC;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.example.loading.Loading;
import org.example.output.Results;
import org.example.output.SaveOutput;
import org.example.structure.ConcLoads;
import org.example.structure.DistLoads;
import org.example.structure.ElemType;
import org.example.structure.Element;
import org.example.structure.Material;
import org.example.structure.MeshType;
import org.example.structure.MyCanvas;
import org.example.structure.NodalDisps;
import org.example.structure.Node;
import org.example.structure.Reactions;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.structure.Supports;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

public abstract class MenuFunctions
{

	public static Point mousePos;
	public static boolean ShowNodes ;
	public static boolean ShowElems, ShowConcLoads, ShowDistLoads, ShowNodalDisps, ShowReactionArrows, ShowReactionValues, ShowLoadsValues, ShowSup;
	public static boolean ShowDOFNumber, ShowNodeNumber, ShowElemNumber, ShowElemContour, ShowMatColor, ShowSecColor, ShowElemDetails;
	public static boolean SnipToGridIsOn;
	public static boolean AnalysisIsComplete;
	public static boolean ShowDeformedStructure ;
	public static boolean NodeSelectionIsOn, ElemSelectionIsOn;
	
	
	public static int[] SelectedElems;
	public static List<Node> selectedNodes ;
	
	public static double[] DiagramScales;
	
	public static String SelectedElemType;
	public static List<Material> matTypes ;
	public static List<Section> secTypes ;
	public static double[][] ConcLoadType, DistLoadType, NodalDispType;
	public static int[][] SupType;
	public static boolean NonlinearMat;
	public static boolean NonlinearGeo;
	
	public static Point ElemSelectionWindowInitialPos;
	
	static
	{
		matTypes = new ArrayList<>() ;
		secTypes = new ArrayList<>() ;
		mousePos = new Point();
		SnipToGridIsOn = false;
		ShowDeformedStructure = false;			
		ShowLoadsValues = true;
		
		SupType = Supports.Types;
		
		NonlinearMat = false;
		NonlinearGeo = false;
		
		AnalysisIsComplete = false;
		NodeSelectionIsOn = false;
		ElemSelectionIsOn = false;

		DiagramScales = new double[2];
		
		selectedNodes = null;
		SelectedElems = null;
		

	    ElemSelectionWindowInitialPos = new Point();
	}

	public static Point GetRelMousePos(int[] PanelPos)
 	{
		return new Point(MouseInfo.getPointerInfo().getLocation().x - PanelPos[0], MouseInfo.getPointerInfo().getLocation().y - PanelPos[1]) ;
 	}

	public static void updateMousePosRelToPanelPos(int[] panelPos) { mousePos = GetRelMousePos(panelPos) ;}

	public static Point ClosestGridNodePos(MyCanvas canvas, Point MousePos)
	{
		int[] NGridPoints = MyCanvas.CalculateNumberOfGridPoints(canvas.getDimension());
		Point GridNodePos = MousePos;
		canvas.setGridSpacing(new double[] {canvas.getSize()[0]/(double)(NGridPoints[0]), canvas.getSize()[1]/(double)(NGridPoints[1])});
		double SnipPower = 1;
		for (int i = 0; i <= NGridPoints[0]; i += 1)
		{	
			for (int j = 0; j <= NGridPoints[1]; j += 1)
			{	
				int[] Pos = new int[] {(int) (canvas.getPos().x + (double)(i)/NGridPoints[0]*canvas.getSize()[0]), (int) (canvas.getPos().y + (double)(j)/NGridPoints[1]*canvas.getSize()[1])};
				if (Math.abs(MousePos.x - Pos[0]) <= SnipPower*canvas.getGridSpacing()[0]/2 & Math.abs(MousePos.y - Pos[1]) <= SnipPower*canvas.getGridSpacing()[1]/2)
				{
					GridNodePos = new Point(Pos[0], Pos[1]) ;
				}
			}
		}
		return GridNodePos;
	}

	public static Point3D getCoordFromMouseClick(MyCanvas canvas, Point MousePos, boolean SnipToGridIsOn)
	{
		
		if (SnipToGridIsOn)
		{
			Point closestGridPoint = ClosestGridNodePos(canvas, MousePos) ;
			return new Point3D(closestGridPoint.x, closestGridPoint.y, 0.0) ;
		}

		Point2D.Double mousePosRealCoords = canvas.inRealCoords(MousePos) ;
		return new Point3D(mousePosRealCoords.x, mousePosRealCoords.y, 0.0) ;

	}

	public static boolean CheckIfAnalysisIsReady(Structure struct, Loading loading)
	{
		if (struct.getMesh() != null &&
			struct.getMesh().getNodes() != null & struct.getMesh().getElements() != null &
			Util.AllElemsHaveMat(struct.getMesh().getElements()) &
			Util.AllElemsHaveSec(struct.getMesh().getElements()) &
			struct.getSupports() != null & loading != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	/* Upper toolbar button functions */
	public static void Clean(Structure struct, boolean[] AssignmentIsOn)
	{
		for (int elem = 0; elem <= struct.getMesh().getElements().size() - 1; elem += 1)
		{
			if (AssignmentIsOn[0])
			{
				struct.getMesh().getElements().get(elem).setMat(null);
			}
			if (AssignmentIsOn[1])
			{
				struct.getMesh().getElements().get(elem).setSec(null);
			}
		}
		if (AssignmentIsOn[2])
		{
			for (int node = 0; node <= struct.getMesh().getNodes().size() - 1; node += 1)
			{
				struct.getMesh().getNodes().get(node).setSup(null);
			}
			struct.removeSupports() ;
		}
		if (AssignmentIsOn[3])
		{
			MainPanel.loading.setConcLoads(null) ;
		}
		if (AssignmentIsOn[4])
		{
			MainPanel.loading.setDistLoads(null) ;
		}
		if (AssignmentIsOn[5])
		{
			MainPanel.loading.setNodalDisps(null) ;
		}
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
					NewNode.setDisp(new double[3]);
					structure.getMesh().getNodes().add(NewNode) ;
				}
				for (int elem = 0; elem <= Input[3].length - 4; elem += 1)
				{
					String[] Line = Input[3][elem + 2].split("	");
					ElemType elemType = ElemType.valueOf(Line[1].toUpperCase()) ;
					Element NewElem = new Element(Integer.parseInt(Line[0]), null, null, null, null, elemType);
					int NumberOfElemNodes = Element.shapeToNumberNodes(NewElem.getShape(), elemType);
					int[] ElemNodes = null;
					for (int elemnode = 0; elemnode <= NumberOfElemNodes - 1; elemnode += 1)
					{
						ElemNodes = Util.AddElem(ElemNodes, Integer.parseInt(Line[elemnode + 2]));
					}
					NewElem.setExternalNodes(ElemNodes);
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
					ConcLoads NewConcLoad;
					NewConcLoad = new ConcLoads(-1, -1, null);
					NewConcLoad.setID(Integer.parseInt(Line[0]));
					NewConcLoad.setNode(Integer.parseInt(Line[1]));
					NewConcLoad.setLoads(new double[] {Double.parseDouble(Line[2]), Double.parseDouble(Line[3]), Double.parseDouble(Line[4]), Double.parseDouble(Line[5]), Double.parseDouble(Line[6]), Double.parseDouble(Line[7])});
					ConcLoadType = Util.AddElem(ConcLoadType, new double[] {NewConcLoad.getNode(), NewConcLoad.getLoads()[0], NewConcLoad.getLoads()[1], NewConcLoad.getLoads()[2], NewConcLoad.getLoads()[3], NewConcLoad.getLoads()[4], NewConcLoad.getLoads()[5]});
					MainPanel.loading.addConcLoad(NewConcLoad);
				}
				for (int distload = 0; distload <= Input[8].length - 4; distload += 1)
				{
					String[] Line = Input[8][distload + 2].split("	");
					DistLoads NewDistLoad;
					NewDistLoad = new DistLoads(-1, -1, -1, -1);
					NewDistLoad.setID(Integer.parseInt(Line[0]));
					NewDistLoad.setElem(Integer.parseInt(Line[1]));
					NewDistLoad.setType(Integer.parseInt(Line[2]));
					NewDistLoad.setIntensity(Double.parseDouble(Line[3]));
					DistLoadType = Util.AddElem(DistLoadType, new double[] {NewDistLoad.getElem(), NewDistLoad.getType(), NewDistLoad.getIntensity()});
					MainPanel.loading.addDistLoad(NewDistLoad);
				}
				for (int nodaldisp = 0; nodaldisp <= Input[9].length - 4; nodaldisp += 1)
				{
					String[] Line = Input[9][nodaldisp + 2].split("	");
					NodalDisps NewNodalDisp;
					NewNodalDisp = new NodalDisps(-1, -1, null);
					NewNodalDisp.setID(Integer.parseInt(Line[0]));
					NewNodalDisp.setNode(Integer.parseInt(Line[1]));
					NewNodalDisp.setDisps(new double[] {Double.parseDouble(Line[2]), Double.parseDouble(Line[3]), Double.parseDouble(Line[4]), Double.parseDouble(Line[5]), Double.parseDouble(Line[6]), Double.parseDouble(Line[7])});
					NodalDispType = Util.AddElem(NodalDispType, new double[] {NewNodalDisp.getNode(), NewNodalDisp.getDisps()[0], NewNodalDisp.getDisps()[1], NewNodalDisp.getDisps()[2], NewNodalDisp.getDisps()[3], NewNodalDisp.getDisps()[4], NewNodalDisp.getDisps()[5]});
					MainPanel.loading.addNodalDisp(NewNodalDisp);
				}

				System.out.println("Structure loaded successfully");
				structure.printStructure(matTypes, secTypes, structure.getSupports(), MainPanel.loading);
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

	/* Visual menu functions */
	
	public static void DOFNumberView()
	{
		ShowDOFNumber = !ShowDOFNumber;
	}
	
	public static void NodeNumberView()
	{
		ShowNodeNumber = !ShowNodeNumber;
	}
	
	public static void ElemNumberView()
	{
		ShowElemNumber = !ShowElemNumber;
	}
	
	public static void MatView()
	{
		ShowMatColor = !ShowMatColor;
	}
	
	public static void SecView()
	{
		ShowSecColor = !ShowSecColor;
	}
	
	public static void NodeView()
	{
		ShowNodes = !ShowNodes;
	}
	
	public static void ElemView()
	{
		ShowElems = !ShowElems;
	}
	
	public static void ElemContourView()
	{
		ShowElemContour = !ShowElemContour;
	}
	
	public static void SupView()
	{
		ShowSup = !ShowSup;
	}
	
	public static void ConcLoadsView()
	{
		ShowConcLoads = !ShowConcLoads;
	}
	
	public static void DistLoadsView()
	{
		ShowDistLoads = !ShowDistLoads;
	}
	
	public static void NodalDispsView()
	{
		ShowNodalDisps = !ShowNodalDisps;
	}
	
	public static void LoadsValuesView()
	{
		ShowLoadsValues = !ShowLoadsValues;
	}
	
	public static void ReactionArrowsView()
	{
		ShowReactionArrows= !ShowReactionArrows;
	}
	
	public static void ReactionValuesView()
	{
		ShowReactionValues = !ShowReactionValues;
	}
	
	public static void ElemDetailsView()
	{
		ShowElemDetails = !ShowElemDetails;
	}
	
	/* Analysis menu functions */
	public static void CalcAnalysisParameters(Structure struct)
	{
		struct.NFreeDOFs = -1;
		for (int node = 0; node <= struct.getMesh().getNodes().size() - 1; node += 1)
        {
			struct.getMesh().getNodes().get(node).setDOFType(Util.DefineDOFsOnNode(struct.getMesh().getElements()));
			struct.getMesh().getNodes().get(node).calcdofs(struct.getSupports(), struct.NFreeDOFs + 1);
			for (int dof = 0; dof <= struct.getMesh().getNodes().get(node).dofs.length - 1; dof += 1)
	        {
				if (-1 < struct.getMesh().getNodes().get(node).dofs[dof])
				{
					struct.NFreeDOFs = struct.getMesh().getNodes().get(node).dofs[dof];
				}
	        }
			struct.getMesh().getNodes().get(node).setLoadDispCurve();
        }
		struct.NFreeDOFs += 1;
		for (Element elem : struct.getMesh().getElements())
		{
			elem.setCumDOFs(Util.CumDOFsOnElem(struct.getMesh().getNodes(), elem.getExternalNodes().length));
        	elem.setUndeformedCoords(struct.getMesh().getNodes());
        	elem.setCenterCoords();
		}
		for (int elem = 0; elem <= struct.getMesh().getElements().size() - 1; elem += 1)
		{
			int[][] ElemNodeDOF = null;
			for (int elemnode = 0; elemnode <= struct.getMesh().getElements().get(elem).getExternalNodes().length - 1; elemnode += 1)
	    	{
				int node = struct.getMesh().getElements().get(elem).getExternalNodes()[elemnode];
				ElemNodeDOF = Util.AddElem(ElemNodeDOF, struct.getMesh().getNodes().get(node).dofs);
	    	}
			struct.getMesh().getElements().get(elem).setNodeDOF(ElemNodeDOF);
		}
		if (ConcLoadType != null)
		{
			for (int loadid = 0; loadid <= ConcLoadType.length - 1; loadid += 1)
			{
				int nodeid = (int) ConcLoadType[loadid][1];
				if (-1 < nodeid)
				{
					MainPanel.loading.getConcLoads().set(loadid, new ConcLoads(loadid, nodeid, ConcLoadType[loadid])) ;
					struct.getMesh().getNodes().get(nodeid).setConcLoads(Util.AddElem(struct.getMesh().getNodes().get(nodeid).getConcLoads(), MainPanel.loading.getConcLoads().get(loadid)));
				}
			}
		}
		if (DistLoadType != null)
		{
			for (int loadid = 0; loadid <= DistLoadType.length - 1; loadid += 1)
			{
				int elemid = (int) DistLoadType[loadid][0];
				int LoadType = (int) DistLoadType[loadid][1];
				double Intensity = DistLoadType[loadid][2];
				if (-1 < elemid)
				{
					MainPanel.loading.getDistLoads().set(loadid, new DistLoads(loadid, elemid, LoadType, Intensity)) ;
					struct.getMesh().getElements().get(elemid).setDistLoads(Util.AddElem(struct.getMesh().getElements().get(elemid).getDistLoads(), MainPanel.loading.getDistLoads().get(loadid)));
				}
			}
		}
	}
	

	public static void PostAnalysis(Structure struct)
	{
		/* Record results and set up their view*/
		struct.setU(struct.getU());
		Reactions[] reactions = Analysis.Reactions(struct.getMesh(), struct.getSupports(), NonlinearMat, NonlinearGeo, struct.getU());
		struct.setReactions(reactions);
		for (Element elem : struct.getMesh().getElements())
		{
			elem.RecordResults(struct.getMesh().getNodes(), struct.getU(), NonlinearMat, NonlinearGeo);
		}
		struct.getResults().register(struct.getMesh(), struct.getSupports(), struct.getU(), NonlinearMat, NonlinearGeo);
		ShowNodes = true;
		ShowElems = true;
		NodeSelectionIsOn = true;
		ElemSelectionIsOn = true;
		AnalysisIsComplete = true;
		ShowReactionArrows = true;
		ShowDeformedStructure = true;
		DiagramScales[1] = 1;
		double MaxDisp = Util.FindMaxAbs(struct.getU());
		if (0 < MaxDisp)
		{
			DiagramScales[1] = 1 / MaxDisp;
		}
		System.out.println("Max disp: " + MaxDisp);
		Reactions.setSumReactions(Analysis.SumReactions(reactions));		
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
						if (-1 < structure.getMesh().getNodes().get(node).dofs[dof])
						{
							vars[sec][node][dof + 1] = String.valueOf(structure.getU()[structure.getMesh().getNodes().get(node).dofs[dof]]);
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
						for (int elemnode = 0; elemnode <= elem.getExternalNodes().length - 1; elemnode += 1)
						{
							if (node == elem.getExternalNodes()[elemnode])
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
	
	public static void DeformedStructureView()
	{
		ShowDeformedStructure = !ShowDeformedStructure;
	}
	
	public static void SaveLoadDispCurve(Structure structure)
	{
		if (-1 < MainPanel.SelectedVar)
		{
			int nodeid = selectedNodes.get(0).getID();
			double[][][] loaddisp = structure.getMesh().getNodes().get(nodeid).LoadDisp;
			String[] Sections = new String[] {"Deslocamentos", "Fator de carga"};
			String[][][] vars = new String[Sections.length][structure.getMesh().getNodes().get(nodeid).dofs.length][loaddisp[0][0].length];
			for (int sec = 0; sec <= Sections.length - 1; sec += 1)
			{
				for (int dof = 0; dof <= structure.getMesh().getNodes().get(nodeid).dofs.length - 1; dof += 1)
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
	
	public static Structure Especial()
	{
		/* Load input */
		Object[] InputData = Util.LoadEspecialInput();
		List<Point3D> EspecialCoords = (List<Point3D>) InputData[0];
		MeshType meshType = MeshType.valueOf(((String) InputData[1]).toLowerCase());
		String[] EspecialElemTypes = (String[]) InputData[2];
		int[][] EspecialMeshSizes = (int[][]) InputData[3];

		double[][] inputMatTypes = (double[][]) InputData[4] ;
		for (int i = 0 ; i <= inputMatTypes.length - 1 ; i += 1)
		{
			matTypes.add(new Material(inputMatTypes[i][0], inputMatTypes[i][1], inputMatTypes[i][2])) ;
		}
		
		double[][] inputSecTypes = (double[][]) InputData[5] ;
		for (int i = 0 ; i <= inputSecTypes.length - 1 ; i += 1)
		{
			secTypes.add(new Section(inputSecTypes[i][0])) ;
		}

		ConcLoadType = (double[][]) InputData[6];
		DistLoadType = (double[][]) InputData[7];
		int[] SupConfig = (int[]) InputData[8];

		/* Define structure parameters */
		int ConcLoadConfig = 1;
		int DistLoadConfig = 1;
		
		int[] NumPar = new int[] {EspecialElemTypes.length, EspecialMeshSizes.length, matTypes.size(), secTypes.size(), SupConfig.length, ConcLoadType.length, DistLoadType.length};	// 0: Elem, 1: Mesh, 2: Mat, 3: Sec, 4: Sup, 5: Conc load, 6: Dist load
		int[] Par = new int[NumPar.length];
		if (ConcLoadType.length == 0)
		{
			Par[5] = -1;
		}
		if (DistLoadType.length == 0)
		{
			Par[6] = -1;
		}
		int NumberOfRuns = Util.ProdVec(NumPar);

		ElemType elemType = ElemType.valueOf(EspecialElemTypes[Par[0]].toUpperCase());
		String[] Sections = new String[] {"Anâlise		Elem	Malha (nx x ny)	Mat		Sec		Apoio		Carga		Min deslocamento (m)		Max deslocamento (m)		Desl. sob a carga (m)		Tempo (seg)"};
		String[][][] vars = new String[Sections.length][][];
		
		vars[0] = new String[NumberOfRuns][11];
		for (int run = 0; run <= NumberOfRuns - 1; run += 1)
		{				
			int[] MeshSize = EspecialMeshSizes[Par[1]];
			int Mat = Par[2];
			int Sec = Par[3];
			int supConfig = SupConfig[Par[4]];
			int SelConcLoad = Par[5];
			int SelDistLoad = Par[6];
			

			Object[] Structure = MainPanel.CreatureStructure(EspecialCoords, meshType, MeshSize, elemType, matTypes.get(Mat), matTypes, secTypes.get(Sec), secTypes,
																supConfig, SelConcLoad, SelDistLoad, ConcLoadConfig, DistLoadConfig);
			List<ConcLoads> ConcLoad = (List<ConcLoads>) Structure[3];
			List<DistLoads> DistLoad = (List<DistLoads>) Structure[4];
			List<NodalDisps> NodalDisp = (List<NodalDisps>) Structure[5];
			Loading loading = new Loading(ConcLoad, DistLoad, NodalDisp) ;
			
			// System.out.println("meshType: " + meshType);
			// System.out.println("MeshSize: " + MeshSize);
			// System.out.println("elemType: " + elemType);
			// System.out.println("matTypes.get(Mat): " + matTypes.get(Mat));
			// System.out.println("matTypes: " + matTypes);
			// System.out.println("secTypes.get(Sec): " + secTypes.get(Sec));
			// System.out.println("secTypes: " + secTypes);
			// System.out.println("supConfig: " + supConfig);
			// System.out.println("SelConcLoad: " + SelConcLoad);
			// System.out.println("SelDistLoad: " + SelDistLoad);
			// System.out.println("ConcLoadConfig: " + ConcLoadConfig);
			// System.out.println("DistLoadConfig: " + DistLoadConfig);
			
			System.out.print("Anâlise num " + run + ": ");
			//UtilComponents.PrintStructure(MeshType, Node, Elem, EspecialMat, EspecialSec, Sup, ConcLoads, DistLoads, NodalDisps);

			boolean NonlinearMat = true;
			boolean NonlinearGeo = false;
			Analysis.run(MainPanel.structure, loading, NonlinearMat, NonlinearGeo, 10, 5, 15.743);
			
			/* Analysis is complete */
			PostAnalysis(MainPanel.structure);

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
		//Re.SaveOutput("Especial", Sections, vars);
		
		return MainPanel.structure;
	}

	public static void RunExample(int exampleID)
	{
		
		Structure structure = new Structure(null, null, null);
		MainPanel.loading.clearLoads() ;
		MainPanel.resetDisplay() ;
		resetDisplay();
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
		MainPanel.structure = structure ;
 		CalcAnalysisParameters(MainPanel.structure);
		long AnalysisTime = System.currentTimeMillis();
		Analysis.run(MainPanel.structure, MainPanel.loading, NonlinearMat, NonlinearGeo, 1, 1, 1);
		PostAnalysis(MainPanel.structure);
		AnalysisTime = System.currentTimeMillis() - AnalysisTime;
		for (Element elem : MainPanel.structure.getMesh().getElements())
		{
			elem.RecordResults(MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getU(), NonlinearMat, NonlinearGeo);
        	elem.setDeformedCoords(MainPanel.structure.getMesh().getNodes());
		}
		MainPanel.structure.getResults().register(MainPanel.structure.getMesh(), MainPanel.structure.getSupports(), MainPanel.structure.getU(), NonlinearMat, NonlinearGeo);
		NodeSelectionIsOn = true;
		ElemSelectionIsOn = true;
		AnalysisIsComplete = true;
		ShowReactionArrows = true;
		ShowReactionValues = true;
		ShowDeformedStructure = true;
		double MaxDisp = Util.FindMaxAbs(MainPanel.structure.getU());
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
		if (AnalysisIsComplete & !Util.MouseIsInsideCanvas(mousePos, MainCanvas))
		{
			DiagramScales[1] += -Util.Round(10 * Math.log10(DiagramScales[1]) * WheelRot, 10);
			DiagramScales[1] = Math.max(DiagramScales[1], 0.001);
		}
	}

	public static void resetDisplay()
	{
		matTypes = null;
		secTypes = null;
		ConcLoadType = null ;
		DistLoadType = null ;
		NodalDispType = null ;
		ShowNodes = false;
		ShowElems = false;
		ShowConcLoads = false;
		ShowDistLoads = false;
		ShowNodalDisps = false;
		ShowReactionArrows = false;
		ShowReactionValues = false;
		ShowSup = false;
		ShowDOFNumber = false;
		ShowNodeNumber = false;
		ShowElemNumber = false;
		ShowElemContour = true;
		ShowMatColor = false;
		ShowSecColor = false;
		// StructureCreationIsOn = false;
		ShowLoadsValues = true;
		AnalysisIsComplete = false;
		ShowDeformedStructure = false;
	}
}
