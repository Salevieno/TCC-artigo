package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import GUI.DrawingOnAPanel;
import Output.Results;
import Utilidades.Util;
import Utilidades.UtilComponents;
import structure.ConcLoads;
import structure.DistLoads;
import structure.ElemShape;
import structure.ElemType;
import structure.Element;
import structure.Material;
import structure.MeshType;
import structure.MyCanvas;
import structure.NodalDisps;
import structure.Nodes;
import structure.Reactions;
import structure.Section;
import structure.Structure;
import structure.StructureShape;
import structure.Supports;

public abstract class MenuFunctions
{	
	private static Color[] ColorPalette;
	public static int[] MousePos;
	private static boolean ShowStructure, ShowNodes, ShowElems, ShowConcLoads, ShowDistLoads, ShowNodalDisps, ShowReactionArrows, ShowReactionValues, ShowLoadsValues, ShowSup;
	private static boolean ShowDOFNumber, ShowNodeNumber, ShowElemNumber, ShowElemContour, ShowMatColor, ShowSecColor, ShowElemDetails;
	private static boolean ShowCanvas, ShowGrid;
	public static boolean ShowMousePos;
	public static boolean StructureCreationIsOn;
	public static boolean SnipToGridIsOn;
	private static boolean ShowNodeSelectionWindow, ShowElemSelectionWindow;
	public static boolean AnalysisIsComplete;
	private static boolean ShowDisplacementContour, ShowDeformedStructure, ShowStressContour, ShowStrainContour, ShowInternalForces;
	public static boolean NodeSelectionIsOn, ElemSelectionIsOn;
	
	public static Structure Struct;
	public static Nodes[] Node;
	public static Element[] Elem;
	public static Supports[] Sup;
	public static ConcLoads[] ConcLoad;
	public static DistLoads[] DistLoad;
	public static NodalDisps[] NodalDisp;
	public static Reactions[] Reaction;
	
	public static int[] SelectedNodes, SelectedElems;
	public static int SelectedMat;
	public static int SelectedSec;
	private static int SelectedSup;
	private static int SelectedConcLoad;
	private static int SelectedDistLoad;
	private static int SelectedNodalDisp;
	
	public static int SelectedDiagram = -1;
	public static int SelectedVar = -1;
	public static double[] DiagramScales;
	
	public static String SelectedElemType;
	public static List<Material> matTypes ;
	public static List<Section> secTypes ;
	public static double[][] ConcLoadType, DistLoadType, NodalDispType;
	private static int[][] SupType;
	public static boolean NonlinearMat;
	public static boolean NonlinearGeo;
	
	private static int[] NodeSelectionWindowInitialPos, ElemSelectionWindowInitialPos;
	
	static
	{
		matTypes = new ArrayList<>() ;
		secTypes = new ArrayList<>() ;
	}
	
	public static void Initialization()
	{	
		ColorPalette = Util.ColorPalette();
		MousePos = new int[2];
		ShowCanvas = true;
		ShowGrid = true;
		ShowMousePos = true;
		StructureCreationIsOn = false;
		SnipToGridIsOn = false;
		ShowDeformedStructure = false;
			
		/*ShowStructure = false;
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
		ShowSecColor = false;*/
		ShowLoadsValues = true;
		/*ShowNodeSelectionWindow = false;
		ShowElemSelectionWindow = false;*/
		
		SupType = Supports.Types;
		
		NonlinearMat = false;
		NonlinearGeo = false;
		
		AnalysisIsComplete = false;
		NodeSelectionIsOn = false;
		ElemSelectionIsOn = false;
		
		Struct = new Structure(null, null, null);

		SelectedMat = -1;
		SelectedSec = -1;
		SelectedSup = -1;
		SelectedConcLoad = -1;
		SelectedDistLoad = -1;
		SelectedNodalDisp = -1;
		DiagramScales = new double[2];
		
		SelectedNodes = null;
		SelectedElems = null;
		

	    NodeSelectionWindowInitialPos = new int[2];
	    ElemSelectionWindowInitialPos = new int[2];
	}

	public static Object[] GetStructInfo()
	{
		return new Object[] {Struct, Node, Elem, AnalysisIsComplete};
	}
	
	public static Object[] GetTypesInfo()
	{
		return new Object[] {SelectedElemType, matTypes, secTypes, SupType, ConcLoadType, DistLoadType, NodalDispType};
	}

	public static int[] ClosestGridNodePos(MyCanvas canvas, int[] MousePos)
	{
		int[] NGridPoints = Util.CalculateNumberOfGridPoints(canvas.getDim());
		int[] GridNodePos = MousePos;
		canvas.setGridSpacing(new double[] {canvas.getSize()[0]/(double)(NGridPoints[0]), canvas.getSize()[1]/(double)(NGridPoints[1])});
		double SnipPower = 1;
		for (int i = 0; i <= NGridPoints[0]; i += 1)
		{	
			for (int j = 0; j <= NGridPoints[1]; j += 1)
			{	
				int[] Pos = new int[] {(int) (canvas.getPos()[0] + (double)(i)/NGridPoints[0]*canvas.getSize()[0]), (int) (canvas.getPos()[1] + (double)(j)/NGridPoints[1]*canvas.getSize()[1])};
				if (Math.abs(MousePos[0] - Pos[0]) <= SnipPower*canvas.getGridSpacing()[0]/2 & Math.abs(MousePos[1] - Pos[1]) <= SnipPower*canvas.getGridSpacing()[1]/2)
				{
					GridNodePos = new int[] {Pos[0], Pos[1]};
				}
			}
		}
		return GridNodePos;
	}

	public static double[][] GetCoord(StructureShape structshape, MyCanvas canvas, double[][] Coords, int[] MousePos, boolean SnipToGridIsOn)
	{
		int[] NewDrawingCoord;
		double[][] NewCoord;
		if (SnipToGridIsOn)
		{		    					
			NewDrawingCoord = ClosestGridNodePos(canvas, MousePos);
		}
		else
		{
			NewDrawingCoord = MousePos;
		}	
		if (Coords == null)
		{
    		Coords = Util.AddElem(Coords, new double[] {NewDrawingCoord[0], NewDrawingCoord[1]});
		}
		else
		{
			if (structshape.equals(StructureShape.rectangular))
			{
	    		Coords = Util.AddElem(Coords, new double[] {Coords[0][0], NewDrawingCoord[1]});
	    		Coords = Util.AddElem(Coords, new double[] {NewDrawingCoord[0], NewDrawingCoord[1]});
	    		Coords = Util.AddElem(Coords, new double[] {NewDrawingCoord[0], Coords[0][1]});
			}
			else if (structshape.equals(StructureShape.circular))
			{
				double[] Center = new double[] {Coords[0][0], Coords[0][1], 0};
				double[] Point2 = new double[] {NewDrawingCoord[0], NewDrawingCoord[1], 0};
				double r = Util.dist(Center, Point2);
				int NPoints = 20;
				Coords[0][0] += r;
				for (int node = 1; node <= NPoints - 1; node += 1)
				{
					double angle = node * 2 * Math.PI / (double) NPoints;
					Coords = Util.AddElem(Coords, new double[] {Center[0] + r*Math.cos(angle), Center[1] + r*Math.sin(angle), 0});
				}
			}
			else if (structshape.equals(StructureShape.polygonal))
			{
	    		Coords = Util.AddElem(Coords, new double[] {NewDrawingCoord[0], NewDrawingCoord[1]});
			}
		}
		NewCoord = Coords;
		return NewCoord;
	}
	
	public static boolean CheckIfAnalysisIsReady()
	{
		if (Node != null & Elem != null & Util.AllElemsHaveMat(Elem) & Util.AllElemsHaveSec(Elem) & Sup != null & (ConcLoad != null | DistLoad != null | NodalDisp != null))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean[] CheckSteps()
	{
		boolean[] StepIsComplete = new boolean[9];
        if (Elem != null)
        {
    		if (Util.AllElemsHaveElemType(Elem))
    		{
    			StepIsComplete[0] = true;
    		}
        }
		if (SelectedElemType != null)
		{
			StepIsComplete[0] = true;
		}
        if (Struct.getCoords() != null)
        {
    		StepIsComplete[1] = true;
        }
        if (Node != null & Elem != null)
        {
    		StepIsComplete[2] = true;
        }
        if (Elem != null)
        {
    		if (Util.AllElemsHaveMat(Elem))
    		{
    			StepIsComplete[3] = true;
    		}
    		if (Util.AllElemsHaveSec(Elem))
    		{
    			StepIsComplete[4] = true;
    		}
        }
		if (Sup != null)
		{
			StepIsComplete[5] = true;
		}
		if (ConcLoad != null)
		{
			StepIsComplete[6] = true;
		}
		if (DistLoad != null)
		{
			StepIsComplete[7] = true;
		}
		if (NodalDisp != null)
		{
			StepIsComplete[8] = true;
		}
		//System.out.println(Arrays.toString(StepIsComplete));
		return StepIsComplete;
	}
	
	/* Upper toolbar button functions */
	public static void Clean(boolean[] AssignmentIsOn)
	{
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			if (AssignmentIsOn[0])
			{
				Elem[elem].setMat(null);
			}
			if (AssignmentIsOn[1])
			{
				Elem[elem].setSec(null);
			}
		}
		if (AssignmentIsOn[2])
		{
			for (int node = 0; node <= Node.length - 1; node += 1)
			{
				Node[node].setSup(null);
			}
			Sup = null;
		}
		if (AssignmentIsOn[3])
		{
			ConcLoad = null;
		}
		if (AssignmentIsOn[4])
		{
			DistLoad = null;
		}
		if (AssignmentIsOn[5])
		{
			NodalDisp = null;
		}
	}
	
	/* Results toolbar functions */
	public static void ShowResult()
	{
		ShowElems = false;
		for (int node = 0; node <= Elem[0].getExternalNodes().length - 1; node += 1)
		{
			SelectedVar = Util.ElemPosInArray(Elem[0].getDOFsPerNode()[node], SelectedVar);
			if (-1 < SelectedVar)
			{
				node = Elem[0].getExternalNodes().length - 1;
			}
		}
		if (SelectedDiagram == 0 & -1 < SelectedVar)
		{
			//DrawDisplacementContours(SelectedVar);
			Struct.setDispMin(Util.FindMinDisps(Struct.getU(), Elem[0].getDOFs(), Analysis.DefineFreeDoFTypes(Node, Elem, Sup)));
			Struct.setDispMax(Util.FindMaxDisps(Struct.getU(), Elem[0].getDOFs(), Analysis.DefineFreeDoFTypes(Node, Elem, Sup)));
			ShowDisplacementContour = false;
			ShowDisplacementContour = true;
			ShowStrainContour = false;
			ShowInternalForces = false;
		}
		else if (SelectedDiagram == 1 & -1 < SelectedVar)
		{
			ShowDisplacementContour = false;
			ShowStressContour = true;
			ShowStrainContour = false;
			ShowInternalForces = false;
		}
		else if (SelectedDiagram == 2 & -1 < SelectedVar)
		{
			ShowDisplacementContour = false;
			ShowStressContour = false;
			ShowStrainContour = true;
			ShowInternalForces = false;
		}
		else if (SelectedDiagram == 3 & -1 < SelectedVar)
		{
			ShowDisplacementContour = false;
			ShowStressContour = false;
			ShowStrainContour = false;
			ShowInternalForces = true;
		}
	}
	
	/* File menu functions */
	public static void SaveFile(String FileName, MyCanvas MainCanvas, Structure Struct, Nodes[] Node, Element[] Elem,
			Supports[] Sup, ConcLoads[] ConcLoads, DistLoads[] DistLoads, NodalDisps[] NodalDisps, List<Material> UserDefinedMat, List<Section> UserDefinedSec)
	{
		Struct.setName(FileName);
		String[] InputSections = new String[] {
			    "Nome da estrutura",
			    "Coordenadas da estrutura",
			    "Nos",
			    "Elementos",
			    "Materiais",
			    "Secoes",
			    "Apoios",
			    "Cargas concentradas",
			    "Cargas distribuidas",
			    "Deslocamentos aplicados",
			    "Desenho"
			};
		String[][] InputVariables = new String[11][];
		InputVariables[0] = new String[] {"Nome"};
		InputVariables[1] = new String[] {"x (m)	y (m)	z (m)"};
		InputVariables[2] = new String[] {"Id	x (m)	y (m)	z (m)	Sup	Carga conc	Desl"};
		InputVariables[3] = new String[] {"Id	Tipo	No1	No2	No3	Mat	Sec"};
		InputVariables[4] = new String[] {"Id	E (GPa)	v	fu (MPa)"};
		InputVariables[5] = new String[] {"Id	Espessura (mm)"};
		InputVariables[6] = new String[] {"Id	No	Rx	Ry	Rz	Tetax	Tetay	Tetaz"};
		InputVariables[7] = new String[] {"Id	No	Fx (kN)	Fy (kN)	Fz (kN)	Mx (kNm)	My (kNm)	Mz (kNm)"};
		InputVariables[8] = new String[] {"Id	Elem	Tipo	Valor inicial (kN)	Valor final (kN)	Dist inicial (mm)	Dist final (mm)"};
		InputVariables[9] = new String[] {"Id	No	Fx	Fy	Fz	Mx	My	Mz"};
		InputVariables[10] = new String[] {"Pos des x (m)	Pos des y (m)	Centro x (m)	Centro y (m)	Dim x (m)	Dim y (m)	Tetax	Tetay	Tetaz"};
		Object[][][] values = new Object[11][][];

		values[0] = new Object[1][1];
		values[0][0][0] = FileName;
		if (Struct.getCoords() != null)
		{
			values[1] = new Object[Struct.getCoords().length][3];
			for (int coord = 0; coord <= Struct.getCoords().length - 1; coord += 1)
			{
				values[1][coord][0] = Struct.getCoords()[coord][0];
				values[1][coord][1] = Struct.getCoords()[coord][1];
				values[1][coord][2] = Struct.getCoords()[coord][2];
			}
		}
		if (Node != null)
		{
			values[2] = new Object[Node.length][7];
			for (int node = 0; node <= Node.length - 1; node += 1)
			{
				values[2][node][0] = Node[node].getID();
				values[2][node][1] = Util.Round(Node[node].getOriginalCoords()[0], 8);
				values[2][node][2] = Util.Round(Node[node].getOriginalCoords()[1], 8);
				values[2][node][3] = Util.Round(Node[node].getOriginalCoords()[2], 8);
				values[2][node][4] = Node[node].getSup();
				values[2][node][5] = Node[node].getConcLoads();
				values[2][node][6] = Node[node].getNodalDisps();
			}
		}
		if (Elem != null)
		{
			values[3] = new Object[Elem.length][Elem[0].getExternalNodes().length + 4];
			for (int elem = 0; elem <= Elem.length - 1; elem += 1)
			{
				int NElemNodes = Elem[elem].getExternalNodes().length;
				values[3][elem][0] = Elem[elem].getID();
				values[3][elem][1] = Elem[elem].getType();
				for (int elemnode = 0; elemnode <= NElemNodes - 1; elemnode += 1)
				{
					values[3][elem][elemnode + 2] = Elem[elem].getExternalNodes()[elemnode];
				}
				values[3][elem][NElemNodes + 2] = Elem[elem].getMat();
				values[3][elem][NElemNodes + 3] = Elem[elem].getSec();
			}
		}
		if (UserDefinedMat != null)
		{
			values[4] = new Object[UserDefinedMat.size()][4];
			for (int mat = 0; mat <= UserDefinedMat.size() - 1; mat += 1)
			{
				values[4][mat][0] = mat;
				values[4][mat][1] = UserDefinedMat.get(mat).getE();
				values[4][mat][2] = UserDefinedMat.get(mat).getV();
				values[4][mat][3] = UserDefinedMat.get(mat).getG();
			}
		}
		if (UserDefinedSec != null)
		{
			values[5] = new Object[UserDefinedSec.size()][2];
			for (int sec = 0; sec <= UserDefinedSec.size() - 1; sec += 1)
			{
				values[5][sec][0] = sec;
				values[5][sec][1] = UserDefinedSec.get(sec).getT();
			}
		}
		if (Sup != null)
		{
			values[6] = new Object[Sup.length][8];
			for (int sup = 0; sup <= Sup.length - 1; sup += 1)
			{
				values[6][sup][0] = Sup[sup].getID();
				values[6][sup][1] = Sup[sup].getNode();
				values[6][sup][2] = Sup[sup].getDoFs()[0];
				values[6][sup][3] = Sup[sup].getDoFs()[1];
				values[6][sup][4] = Sup[sup].getDoFs()[2];
				values[6][sup][5] = Sup[sup].getDoFs()[3];
				values[6][sup][6] = Sup[sup].getDoFs()[4];
				values[6][sup][7] = Sup[sup].getDoFs()[5];
			}
		}
		if (ConcLoads != null)
		{
			values[7] = new Object[ConcLoads.length][8];
			for (int load = 0; load <= ConcLoads.length - 1; load += 1)
			{
				values[7][load][0] = ConcLoads[load].getID();
				values[7][load][1] = ConcLoads[load].getNode();
				values[7][load][2] = ConcLoads[load].getLoads()[0];
				values[7][load][3] = ConcLoads[load].getLoads()[1];
				values[7][load][4] = ConcLoads[load].getLoads()[2];
				values[7][load][5] = ConcLoads[load].getLoads()[3];
				values[7][load][6] = ConcLoads[load].getLoads()[4];
				values[7][load][7] = ConcLoads[load].getLoads()[5];
			}
		}
		if (DistLoads != null)
		{
			values[8] = new Object[DistLoads.length][4];
			for (int load = 0; load <= DistLoads.length - 1; load += 1)
			{
				values[8][load][0] = DistLoads[load].getID();
				values[8][load][1] = DistLoads[load].getElem();
				values[8][load][2] = DistLoads[load].getType();
				values[8][load][3] = DistLoads[load].getIntensity();
			}
		}
		if (NodalDisps != null)
		{
			values[9] = new Object[NodalDisps.length][8];
			for (int dist = 0; dist <= NodalDisps.length - 1; dist += 1)
			{
				values[9][dist][0] = NodalDisps[dist].getID();
				values[9][dist][1] = NodalDisps[dist].getNode();
				values[9][dist][2] = NodalDisps[dist].getDisps()[0];
				values[9][dist][3] = NodalDisps[dist].getDisps()[1];
				values[9][dist][4] = NodalDisps[dist].getDisps()[2];
				values[9][dist][5] = NodalDisps[dist].getDisps()[3];
				values[9][dist][6] = NodalDisps[dist].getDisps()[4];
				values[9][dist][7] = NodalDisps[dist].getDisps()[5];
			}
		}
		values[10] = new Object[1][9];
		values[10][0][0] = MainCanvas.getDrawingPos()[0];
		values[10][0][1] = MainCanvas.getDrawingPos()[1];
		values[10][0][2] = MainCanvas.getCenter()[0];
		values[10][0][3] = MainCanvas.getCenter()[1];
		values[10][0][4] = Util.Round(MainCanvas.getDim()[0], 8);
		values[10][0][5] = Util.Round(MainCanvas.getDim()[1], 8);
		values[10][0][6] = Util.Round(MainCanvas.getAngles()[0], 8);
		values[10][0][7] = Util.Round(MainCanvas.getAngles()[1], 8);
		values[10][0][8] = Util.Round(MainCanvas.getAngles()[2], 8);
		Results.SaveStructure(FileName, InputSections, InputVariables, values);		// Save the structure in an input file (.txt)
	}

	public static void LoadFile(String Path, String FileName)
	{
		if (!FileName.equals("")) 
		{			
			String[][] Input = ReadInput.ReadTxtFile(Path + FileName + ".txt");			// Loads the input file (.txt)
			if (Input != null)
			{
				Struct.setName(Input[0][2]);
				double[][] StructCoords = null;
				for (int coord = 0; coord <= Input[1].length - 4; coord += 1)
				{
					String[] Line = Input[1][coord + 2].split("	");
					double[] NewCoord = new double[] {Double.parseDouble(Line[0]), Double.parseDouble(Line[1]), Double.parseDouble(Line[2])};
					StructCoords = Util.AddElem(StructCoords, NewCoord);
				}
				Struct.setCoords(StructCoords);
				Struct.setCenter(Util.MatrixAverages(Struct.getCoords()));

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
					Nodes NewNode;
					NewNode = new Nodes(-1, null);
					NewNode.setID(Integer.parseInt(Line[0]));
					NewNode.setOriginalCoords(new double[] {Double.parseDouble(Line[1]), Double.parseDouble(Line[2]), Double.parseDouble(Line[3])});
					NewNode.setDisp(new double[3]);
					Node = Util.AddElem(Node, NewNode);
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
					Elem = Util.AddElem(Elem, NewElem);
				}
				
				for (int sup = 0; sup <= Input[6].length - 4; sup += 1)
				{
					String[] Line = Input[6][sup + 2].split("	");
					Supports NewSup;
					NewSup = new Supports(-1, -1, null);
					NewSup.setID(Integer.parseInt(Line[0]));
					NewSup.setNode(Integer.parseInt(Line[1]));
					NewSup.setDoFs(new int[] {Integer.parseInt(Line[2]), Integer.parseInt(Line[3]), Integer.parseInt(Line[4]), Integer.parseInt(Line[5]), Integer.parseInt(Line[6]), Integer.parseInt(Line[7])});
					Sup = Util.AddElem(Sup, NewSup);
					Node[Integer.parseInt(Line[1])].setSup(NewSup.getDoFs());
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
					ConcLoad = Util.AddElem(ConcLoad, NewConcLoad);
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
					DistLoad = Util.AddElem(DistLoad, NewDistLoad);
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
					NodalDisp = Util.AddElem(NodalDisp, NewNodalDisp);
				}
			}
			else
			{
				System.out.println("Arquivo de input nâo encontrado");
			}
		}
		UtilComponents.PrintStructure(FileName, Node, Elem, matTypes, secTypes, Sup, ConcLoad, DistLoad, NodalDisp);
	}

	/* Structure menu functions */
	public static void setElemType(String ElemType)
	{
		SelectedElemType = ElemType;
	}

	public static void CreateStructureOnClick(StructureShape structureShape)
	{
		Struct.setShape(structureShape);
		StructureCreationIsOn = !StructureCreationIsOn;
	}
	
 	public static void AddMaterialToElements(int[] Elems, Material mat)
	{
		if (Elem != null & Elems != null & mat != null)
		{
			for (int i = 0; i <= Elems.length - 1; i += 1)
			{
				if (-1 < Elems[i])
				{
					Elem[Elems[i]].setMat(mat);
				}
			}
			SelectedElems = null;
		}
	}

	public static void AddSectionsToElements(int[] Elems, Section sec)
	{
		if (Elem != null & sec != null & Elems != null)
		{
			for (int i = 0; i <= Elems.length - 1; i += 1)
			{
				if (-1 < Elems[i])
				{
					Elem[Elems[i]].setSec(sec);
				}
			}
			SelectedElems = null;
		}
	}
	
	public static void AddSupports()
	{
		if (-1 < SelectedSup & SelectedNodes != null & SupType != null )
		{
			Sup = Util.IncreaseArraySize(Sup, SelectedNodes.length);
			for (int i = 0; i <= SelectedNodes.length - 1; i += 1)
			{
				int supid = Sup.length - SelectedNodes.length + i;
				if (-1 < SelectedNodes[i])
				{
					Sup[supid] = new Supports(supid, SelectedNodes[i], SupType[SelectedSup]);
					Node[SelectedNodes[i]].setSup(SupType[SelectedSup]);
				}
			}
			ShowSup = true;
			SelectedNodes = null;
		}
	}
	
	public static void AddConcLoads()
	{
		if (-1 < SelectedConcLoad & SelectedNodes != null & ConcLoadType != null)
		{
			ConcLoad = Util.IncreaseArraySize(ConcLoad, SelectedNodes.length);
			for (int i = 0; i <= SelectedNodes.length - 1; i += 1)
			{
				int loadid = ConcLoad.length - SelectedNodes.length + i;
				if (-1 < SelectedNodes[i])
				{
					ConcLoad[loadid] = new ConcLoads(loadid, SelectedNodes[i], ConcLoadType[SelectedConcLoad]);
					Node[SelectedNodes[i]].AddConcLoads(ConcLoad[loadid]);
				}
			}
			ShowConcLoads = true;
			SelectedNodes = null;
		}
	}
	
	public static void AddDistLoads()
	{
		if (-1 < SelectedDistLoad & SelectedElems != null & DistLoadType != null)
		{
			DistLoad = Util.IncreaseArraySize(DistLoad, SelectedElems.length);
			for (int i = 0; i <= SelectedElems.length - 1; i += 1)
			{
				int loadid = DistLoad.length - SelectedElems.length + i;
				if (-1 < SelectedElems[i])
				{
					int elem = SelectedElems[i];
					int LoadType = (int) DistLoadType[SelectedDistLoad][0];
					double Intensity = DistLoadType[SelectedDistLoad][1];
					DistLoad[loadid] = new DistLoads(loadid, SelectedElems[i], LoadType, Intensity);
					Elem[elem].setDistLoads(Util.AddElem(Elem[elem].getDistLoads(), DistLoad[loadid]));
				}
			}
			ShowDistLoads = true;
			SelectedElems = null;
		}
	}
	
	public static void AddNodalDisps()
	{
		if (-1 < SelectedNodalDisp & SelectedNodes != null & NodalDispType != null)
		{
			NodalDisp = Util.IncreaseArraySize(NodalDisp, SelectedNodes.length);
			for (int i = 0; i <= SelectedNodes.length - 1; i += 1)
			{
				int dispid = NodalDisp.length - SelectedNodes.length + i;
				if (-1 < SelectedNodes[i])
				{
					NodalDisp[dispid] = new NodalDisps(dispid, SelectedNodes[i], NodalDispType[SelectedNodalDisp]);
					Node[SelectedNodes[i]].AddNodalDisps(NodalDisp[dispid]);
				}
			}
			ShowNodalDisps = true;
			SelectedNodes = null;
		}
	}
	
	public static void StructureMenuCreateMesh(MeshType meshType, int[][] Mesh, ElemType elemType)
	{
		Sup = null;
		ConcLoad = null;
		DistLoad = null;
		NodalDisp = null;
		
		if (elemType == null)
		{
			System.out.println("\nElement shape is null at Menus -> StructureMenuCreateMesh") ;
			return ;
		}

		int noffsets = Mesh[0][0];
	    int[] nintermediatepoints = new int[noffsets];
		Arrays.fill(nintermediatepoints, Mesh[0][1]);
		switch (meshType)
		{
			case cartesian:
				Node = Analysis.CreateCartesianNodes(Struct.getCoords(), new int[] {noffsets, nintermediatepoints[0]}, elemType);
				Elem = Analysis.CreateCartesianMesh(Node, new int[] {noffsets, nintermediatepoints[0]}, elemType);
				break ;
				
			case radial:
				Node = Analysis.CreateRadialNodes(Struct.getCoords(), noffsets, nintermediatepoints);
				Elem = Analysis.CreateRadialMesh(Node, noffsets, elemType);
				break ;
				
			default:
				System.out.println("\nMesh type not defined at Menus -> StructureMenuCreateMesh") ;
				return ;
		}
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
        	Elem[elem].setUndeformedCoords(Node);
        	Elem[elem].setCenterCoords();
		}
	}
	
	public static void setMaterials(List<Material> materials)
	{
		matTypes = materials;
	}

	public static void setSections(List<Section> sections)
	{
		secTypes = sections;
	}

	public static void DefineSupportTypes(int[][] Supports)
	{
		SupType = Supports;
	}

	public static void DefineConcLoadTypes(double[][] ConcLoads)
	{
		ConcLoadType = ConcLoads;
	}

	public static void DefineDistLoadTypes(double[][] DistLoads)
	{
		DistLoadType = DistLoads;
	}

	public static void DefineNodalDispTypes(double[][] NodalDisps)
	{
		NodalDispType = NodalDisps;
	}
	
	public static void StructureMenuAssignMaterials()
	{
		ElemSelectionIsOn = !ElemSelectionIsOn;
		SelectedMat = 0;
	}
	
	public static void StructureMenuAssignSections()
	{
		ElemSelectionIsOn = !ElemSelectionIsOn;
		SelectedSec = 0;
	}
	
	public static void StructureMenuAssignSupports()
	{
		NodeSelectionIsOn = !NodeSelectionIsOn;
		SelectedSup = 0;
	}
	
	public static void StructureMenuAssignConcLoads()
	{
		NodeSelectionIsOn = !NodeSelectionIsOn;
		SelectedConcLoad = 0;
	}
	
	public static void StructureMenuAssignDistLoads()
	{
		ElemSelectionIsOn = !ElemSelectionIsOn;
		SelectedDistLoad = 0;
	}
	
	public static void StructureMenuAssignNodalDisps()
	{
		NodeSelectionIsOn = !NodeSelectionIsOn;
		SelectedNodalDisp = 0;
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
	public static void CalcAnalysisParameters()
	{
		Struct.NFreeDOFs = -1;
		for (int node = 0; node <= Node.length - 1; node += 1)
        {
			Node[node].setDOFType(Util.DefineDOFsOnNode(Elem));
			Node[node].calcdofs(Sup, Struct.NFreeDOFs + 1);
			for (int dof = 0; dof <= Node[node].dofs.length - 1; dof += 1)
	        {
				if (-1 < Node[node].dofs[dof])
				{
					Struct.NFreeDOFs = Node[node].dofs[dof];
				}
	        }
			Node[node].setLoadDispCurve();
        }
		Struct.NFreeDOFs += 1;
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			Elem[elem].setCumDOFs(Util.CumDOFsOnElem(Node, Elem[elem].getExternalNodes().length));
        	Elem[elem].setUndeformedCoords(Node);
        	Elem[elem].setCenterCoords();
		}
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			int[][] ElemNodeDOF = null;
			for (int elemnode = 0; elemnode <= Elem[elem].getExternalNodes().length - 1; elemnode += 1)
	    	{
				int node = Elem[elem].getExternalNodes()[elemnode];
				ElemNodeDOF = Util.AddElem(ElemNodeDOF, Node[node].dofs);
	    	}
			Elem[elem].setNodeDOF(ElemNodeDOF);
		}
		if (ConcLoadType != null)
		{
			for (int loadid = 0; loadid <= ConcLoadType.length - 1; loadid += 1)
			{
				int nodeid = (int) ConcLoadType[loadid][1];
				if (-1 < nodeid)
				{
					ConcLoad[loadid] = new ConcLoads(loadid, nodeid, ConcLoadType[loadid]);
					Node[nodeid].setConcLoads(Util.AddElem(Node[nodeid].getConcLoads(), ConcLoad[loadid]));
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
					DistLoad[loadid] = new DistLoads(loadid, elemid, LoadType, Intensity);
					Elem[elemid].setDistLoads(Util.AddElem(Elem[elemid].getDistLoads(), DistLoad[loadid]));
				}
			}
		}
	}
	
	public static double[] RunAnalysis(Structure struct, Nodes[] Node, Element[] Elem, Supports[] Sup, ConcLoads[] ConcLoad, DistLoads[] DistLoad, NodalDisps[] NodalDisp, boolean NonlinearMat, boolean NonlinearGeo, int NIter, int NLoadSteps, double MaxLoadFactor)
	{
		/*
		 * NIter = Nâmero de iteraçõs em cada passo (para convergir)
		 * NLoadSteps = Nâmero de incrementos de carga (nâmero de passos)
		 * MaxLoadFactor = Fator de carga final (valor multiplicando a carga)
		 * */
		long AnalysisTime = System.currentTimeMillis();
		double loadinc = MaxLoadFactor / (double) NLoadSteps;
		for (int loadstep = 0; loadstep <= NLoadSteps - 1; loadstep += 1)
		{
			double loadfactor = 0 + (loadstep + 1)*loadinc;
			struct.setP(Analysis.LoadVector(Node, Elem, Struct.NFreeDOFs, ConcLoad, DistLoad, NodalDisp, NonlinearMat, NonlinearGeo, loadfactor));
		    for (int iter = 0; iter <= NIter - 1; iter += 1)
			{
		    	struct.setK(Analysis.StructureStiffnessMatrix(Struct.NFreeDOFs, Node, Elem, Sup, NonlinearMat, NonlinearGeo));
		    	struct.setU(Util.SolveLinearSystem(struct.getK(), struct.getP()));
			    for (int node = 0; node <= Node.length - 1; node += 1)
			    {
			    	Node[node].setDisp(Analysis.GetNodeDisplacements(Node, struct.getU())[node]);
			    }
			    if (NonlinearMat)
			    {
					for (int elem = 0; elem <= Elem.length - 1; elem += 1)
				    {
						Elem[elem].setStrain(Elem[elem].StrainVec(Node, struct.getU(), NonlinearGeo));
				    }
			    }
				/*for (int elem = 0; elem <= Elem.length - 1; elem += 1)
			    {
					Elem[elem].setIntForces(Elem[elem].InternalForcesVec(Node, struct.getU(), NonlinearMat, NonlinearGeo));
			    }*/
				//UtilText.PrintMatrix(struct.getK());
			    //UtilText.PrintVector(struct.getP());
		        //UtilText.PrintVector(struct.getU());
				System.out.println("iter: " + iter + " max disp: " + Util.FindMaxAbs(struct.getU()));
			}
			for (int node = 0; node <= Node.length - 1; node += 1)
			{
				Node[node].addLoadDispCurve(Struct.getU(), loadfactor);
			}
		}
		AnalysisTime = System.currentTimeMillis() - AnalysisTime;
		System.out.println("Tempo de anâlise = " + AnalysisTime / 1000.0 + " seg");
		if (((Double)struct.getU()[0]).isNaN())
		{
			System.out.println("Displacement results are NaN at Menus -> RunAnalysis");
		}
		
		return struct.getU();
	}

	public static void PostAnalysis()
	{
		/* Record results and set up their view*/
		Struct.setU(Struct.getU());
		Reaction = Analysis.Reactions(Node, Elem, Sup, NonlinearMat, NonlinearGeo, Struct.getU());
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			Elem[elem].RecordResults(Node, Struct.getU(), NonlinearMat, NonlinearGeo);
		}
		Struct.RecordResults(Node, Elem, Sup, Struct.getU(), NonlinearMat, NonlinearGeo);
		ShowNodes = true;
		ShowElems = true;
		NodeSelectionIsOn = true;
		ElemSelectionIsOn = true;
		AnalysisIsComplete = true;
		ShowReactionArrows = true;
		ShowDeformedStructure = true;
		DiagramScales[1] = 1;
		double MaxDisp = Util.FindMaxAbs(Struct.getU());
		if (0 < MaxDisp)
		{
			DiagramScales[1] = 1 / MaxDisp;
		}
		System.out.println("Max disp: " + MaxDisp);
		Reactions.setSumReactions(Analysis.SumReactions(Reaction));		
	}
	
	/* Results menu functions */
	public static void ResultsMenuSaveResults()
	{
		String[] Sections = new String[] {"Min Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Max Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Min Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Max Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Reacoes \nno	Fx (kN)	Fy (kN)	Fz (kN)	Mx (kNm)	My (kNm)	Mz (kNm)", "Soma das reacoes \nFx (kN)	Fy (kN)	Fz (kN)	Mx (kNm)	My (kNm)	Mz (kNm)"};
		String[][][] vars = new String[Sections.length][][];
		vars[0] = new String[1][3];
		vars[1] = new String[1][3];
		vars[2] = new String[Node.length][];
		vars[3] = new String[1][3];
		vars[4] = new String[1][3];
		vars[5] = new String[Node.length][];
		vars[6] = new String[Reaction.length][];
		vars[7] = new String[1][Reactions.SumReactions.length];
		for (int sec = 0; sec <= Sections.length - 1; sec += 1)
		{
			if (sec == 0)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(Struct.getDispMin()[dof]);
				}
			}
			if (sec == 1)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(Struct.getDispMax()[dof]);
				}
			}
			if (sec == 3)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(Struct.getInternalForcesMin()[dof]);
				}
			}
			if (sec == 4)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(Struct.getInternalForcesMax()[dof]);
				}
			}
			for (int node = 0; node <= vars[sec].length - 1; node += 1)
			{
				if (sec == 2)
				{
					vars[sec][node] = new String[Node[node].getDOFType().length + 1];
					vars[sec][node][0] = String.valueOf(node);
					for (int dof = 0; dof <= Node[node].getDOFType().length - 1; dof += 1)
					{
						if (-1 < Node[node].dofs[dof])
						{
							vars[sec][node][dof + 1] = String.valueOf(Struct.getU()[Node[node].dofs[dof]]);
						}
						else
						{
							vars[sec][node][dof + 1] = String.valueOf(0);
						}
					}
				}
				if (sec == 5)
				{
					vars[sec][node] = new String[Node[node].getDOFType().length + 1];
					vars[sec][node][0] = String.valueOf(node);
					for (int elem = 0; elem <= Elem.length - 1; elem += 1)
					{
						for (int elemnode = 0; elemnode <= Elem[elem].getExternalNodes().length - 1; elemnode += 1)
						{
							if (node == Elem[elem].getExternalNodes()[elemnode])
							{
								for (int dof = 0; dof <= Node[node].getDOFType().length - 1; dof += 1)
								{
									vars[sec][node][dof + 1] = String.valueOf(Elem[elem].getIntForces()[elemnode*Node[node].getDOFType().length + dof]);
								}
							}
						}
					}
				}
				if (sec == 6)
				{
					vars[sec][node] = new String[Reaction[node].getLoads().length + 1];
					vars[sec][node][0] = String.valueOf(node);
					for (int dof = 0; dof <= Reaction[node].getLoads().length - 1; dof += 1)
					{
						vars[sec][node][dof + 1] = String.valueOf(Reaction[node].getLoads()[dof]);
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
		Results.SaveOutput(Struct.getName(), Sections, vars);
	}
	
	public static void DeformedStructureView()
	{
		ShowDeformedStructure = !ShowDeformedStructure;
	}
	
	public static void SaveLoadDispCurve()
	{
		if (-1 < SelectedVar)
		{
			int nodeid = SelectedNodes[0];
			double[][][] loaddisp = Node[nodeid].LoadDisp;
			String[] Sections = new String[] {"Deslocamentos", "Fator de carga"};
			String[][][] vars = new String[Sections.length][Node[nodeid].dofs.length][loaddisp[0][0].length];
			for (int sec = 0; sec <= Sections.length - 1; sec += 1)
			{
				for (int dof = 0; dof <= Node[nodeid].dofs.length - 1; dof += 1)
				{
					for (int loadinc = 0; loadinc <= loaddisp[dof][sec].length - 1; loadinc += 1)
					{
						vars[sec][dof][loadinc] = String.valueOf(loaddisp[dof][sec][loadinc]);
					}
				}
			}
			Results.SaveOutput(Struct.getName(), Sections, vars);
		}
	}

	/* Especial menu functions */
	public static Object[] CreatureStructure(double[][] StructCoords, MeshType meshType, int[] MeshSizes, ElemType elemType,
			Material CurrentMatType, List<Material> matTypes, Section CurrentSecType, List<Section> secTypes,
			int SupConfig, int SelConcLoad, int SelDistLoad, int ConcLoadConfig, int DistLoadConfig)
	{
		/* Tipo de elemento, materiais, seçõs, apoios e cargas jâ estâo definidos */
		
		/* 1. Criar polâgono */
		Struct = new Structure(null, null, StructCoords);
		
		/* 2. Criar malha */
		StructureMenuCreateMesh(meshType, new int[][] {MeshSizes}, elemType);
		
		/* 3. Atribuir materiais */
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			SelectedElems = Util.AddElem(SelectedElems, elem);
		}
		AddMaterialToElements(SelectedElems, CurrentMatType);
		Element.createMatColors(matTypes);
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			int matColorID = matTypes.indexOf(Elem[elem].getMat()) ;
			Elem[elem].setMatColor(Element.matColors[matColorID]);
		}

		/* 4. Atribuir seçõs */
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			SelectedElems = Util.AddElem(SelectedElems, elem);
		}
		AddSectionsToElements(SelectedElems, CurrentSecType);
		Element.setSecColors(secTypes);
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			int secID = secTypes.indexOf(Elem[elem].getSec()) ;
			Elem[elem].setSecColor(Element.SecColors[secID]);
		}
		
		/* 5. Atribuir apoios */
		Sup = Util.AddEspecialSupports(Node, Element.typeToShape(elemType), meshType, new int[] {MeshSizes[0], MeshSizes[1]}, SupConfig);

		/* 6. Atribuir cargas */
		if (ConcLoadConfig == 1)
		{
			if (Elem[0].getShape().equals(ElemShape.rectangular))
			{
				SelectedNodes = Util.AddElem(SelectedNodes, (MeshSizes[1] / 2 * (MeshSizes[0] + 1) + MeshSizes[0] / 2));
			}
			else if (Elem[0].getShape().equals(ElemShape.r8))
			{
				SelectedNodes = Util.AddElem(SelectedNodes, (MeshSizes[1] / 2 * (2 * MeshSizes[0] + 1 + MeshSizes[0] + 1) + MeshSizes[0]));
			}
		}
		SelectedConcLoad = SelConcLoad;
		AddConcLoads();
		SelectedDistLoad = SelDistLoad;
		if (-1 < SelDistLoad)
		{
			SelectedElems = null;
			for (int elem = 0; elem <= Elem.length - 1; elem += 1)
			{
				SelectedElems = Util.AddElem(SelectedElems, elem);
			}
			AddDistLoads();
		}
		
		/* 7. Calcular parâmetros para a anâlise */
		CalcAnalysisParameters();
		
		SelectedNodes = null;
		SelectedElems = null;
		return new Object[] {Node, Elem, Sup, ConcLoad, DistLoad, null};
	}
	
	public static Object[] Especial()
	{
		/* Load input */
		Object[] InputData = Util.LoadEspecialInput();
		double[][] EspecialCoords = (double[][]) InputData[0];
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
		Object[] EspecialResults = null;
		
		vars[0] = new String[NumberOfRuns][11];
		for (int run = 0; run <= NumberOfRuns - 1; run += 1)
		{				
			int[] MeshSize = EspecialMeshSizes[Par[1]];
			int Mat = Par[2];
			int Sec = Par[3];
			int supConfig = SupConfig[Par[4]];
			int SelConcLoad = Par[5];
			int SelDistLoad = Par[6];
			Object[] Structure = CreatureStructure(EspecialCoords, meshType, MeshSize, elemType, matTypes.get(Mat), matTypes, secTypes.get(Sec), secTypes, supConfig, SelConcLoad, SelDistLoad, ConcLoadConfig, DistLoadConfig);
			Supports[] Sup = (Supports[]) Structure[2];
			ConcLoads[] ConcLoad = (ConcLoads[]) Structure[3];
			DistLoads[] DistLoad = (DistLoads[]) Structure[4];
			NodalDisps[] NodalDisp = (NodalDisps[]) Structure[5];
			System.out.println("Sup: " + Arrays.toString(Sup));
			System.out.println("ConcLoad: " + Arrays.toString(ConcLoad));
			System.out.println("DistLoad: " + Arrays.toString(DistLoad));
			System.out.println("NodalDisp: " + Arrays.toString(NodalDisp));
			
			System.out.print("Anâlise num " + run + ": ");
			//UtilComponents.PrintStructure(MeshType, Node, Elem, EspecialMat, EspecialSec, Sup, ConcLoads, DistLoads, NodalDisps);

			boolean NonlinearMat = true;
			boolean NonlinearGeo = false;
			RunAnalysis(Struct, Node, Elem, Sup, ConcLoad, DistLoad, NodalDisp, NonlinearMat, NonlinearGeo, 10, 5, 15.743);
			
			/* Analysis is complete */
			PostAnalysis();
			EspecialResults = new Object[] {Struct, Node, Elem};

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
		
		return EspecialResults;
	}

	public static void RunExample(int exampleID)
	{
		ResetStructure();
		if (exampleID == 0)
		{
 			LoadFile(".\\Exemplos\\", "0-KR1");
		}
 		if (exampleID == 1)
		{
 			LoadFile(".\\Exemplos\\", "1-KR2");
		}
 		if (exampleID == 2)
		{
 			LoadFile(".\\Exemplos\\", "2-MR1");
		}
 		if (exampleID == 3)
		{
 			LoadFile(".\\Exemplos\\", "3-MR2");
		}
 		if (exampleID == 4)
 		{
 			LoadFile(".\\Exemplos\\", "4-R4");
 		}
 		if (exampleID == 5)
 		{
 			LoadFile(".\\Exemplos\\", "5-Q4");
 		}
 		if (exampleID == 6)
 		{
 			LoadFile(".\\Exemplos\\", "6-T3G");
 		}
 		if (exampleID == 7)
 		{
 			LoadFile(".\\Exemplos\\", "7-T6G");
 		}
 		if (exampleID == 8)
 		{
 			LoadFile(".\\Exemplos\\", "8-SM");
 		}
 		if (exampleID == 9)
 		{
 			LoadFile(".\\Exemplos\\", "9-SM8");
 		}
 		if (exampleID == 10)
 		{
 			LoadFile(".\\Exemplos\\", "10-KP3");
 		}
 		if (exampleID == 11)
 		{
 			LoadFile(".\\Exemplos\\", "11-SM_C");
 		}
 		if (exampleID == 12)
 		{
 			LoadFile(".\\Exemplos\\", "12-SM_H");
 		}
 		if (exampleID == 13)
 		{
 			LoadFile(".\\Exemplos\\", "13-vigadeaco");
 		}
 		CalcAnalysisParameters();
		long AnalysisTime = System.currentTimeMillis();
		RunAnalysis(Struct, Node, Elem, Sup, ConcLoad, DistLoad, NodalDisp, NonlinearMat, NonlinearGeo, 1, 1, 1);
		PostAnalysis();
		AnalysisTime = System.currentTimeMillis() - AnalysisTime;
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			Elem[elem].RecordResults(Node, Struct.getU(), NonlinearMat, NonlinearGeo);
        	Elem[elem].setDeformedCoords(Node);
		}
		Struct.RecordResults(Node, Elem, Sup, Struct.getU(), NonlinearMat, NonlinearGeo);
		NodeSelectionIsOn = true;
		ElemSelectionIsOn = true;
		AnalysisIsComplete = true;
		ShowReactionArrows = true;
		ShowReactionValues = true;
		ShowDeformedStructure = true;
		double MaxDisp = Util.FindMaxAbs(Struct.getU());
		if (0 < MaxDisp)
		{
			DiagramScales[1] = 1 / MaxDisp;
		}
		else
		{
			DiagramScales[1] = 1;
		}
	}
	
	/* Draw on panels */
	public static void DrawCanvasElements(Dimension jpMainSize, MyCanvas canvas, DrawingOnAPanel DP, boolean ShowCanvas, boolean ShowGrid, boolean ShowMousePos, int[] DPPos)
	{
		Dimension MainPanelSize = jpMainSize;
		canvas.setPos(new int[] {(int) (0.1 * MainPanelSize.getWidth()), (int) (0.1 * MainPanelSize.getHeight())});
		canvas.setSize(new int[] {(int) (0.8 * MainPanelSize.getWidth()), (int) (0.8 * MainPanelSize.getHeight())});
		canvas.setCenter(new int[] {canvas.getPos()[0] + canvas.getSize()[0] / 2, canvas.getPos()[1] + canvas.getSize()[1] / 2});
		int[] LittleAxisPos = new int[] {canvas.getPos()[0] + canvas.getSize()[0] + 10, canvas.getPos()[1] - 10, 0};
		int[] BigAxisPos = new int[] {canvas.getPos()[0], canvas.getPos()[1] + canvas.getSize()[1], 0};
		DP.DrawAxis(LittleAxisPos, canvas.getSize()[0] / 15, canvas.getSize()[1] / 15, 10, canvas.getAngles());
		DP.DrawAxis(BigAxisPos, canvas.getSize()[0] + 20, canvas.getSize()[1] + 20, 20, new double[] {0, 0, 0});
		DP.DrawText(new int[] {canvas.getPos()[0] + canvas.getSize()[0], canvas.getPos()[1] + canvas.getSize()[1] + 15, 0}, String.valueOf(Util.Round(canvas.getDim()[0], 3)) + " m", "Center", 0, "Bold", 13, ColorPalette[7]);
		DP.DrawText(new int[] {canvas.getPos()[0] + 30, canvas.getPos()[1] - 10, 0}, String.valueOf(Util.Round(canvas.getDim()[1], 3)) + " m", "Center", 0, "Bold", 13, ColorPalette[10]);
		if (ShowCanvas)
		{
			DP.DrawCanvas(canvas.getTitle(), new double[] {Util.Round(canvas.getGridSpacing()[0], 1), Util.Round(canvas.getGridSpacing()[1], 1)});
		}
		if (ShowGrid)
		{
			DP.DrawGrid(2);
		}
		double[] RealMousePos = Util.ConvertToRealCoords(MenuFunctions.MousePos, canvas.getPos(), canvas.getSize(), canvas.getDim());
		DP.DrawMousePos(new int[] {BigAxisPos[0] + canvas.getSize()[0] / 2 - 60, BigAxisPos[1] + 40}, RealMousePos, ColorPalette[3], ColorPalette[0]);
	}
	
	public static void DrawOnMainPanel(Dimension jpMainSize, int[] MainPanelPos, int[] MainCanvasCenter, MyCanvas MainCanvas, DrawingOnAPanel DP)
	{
		MousePos = Util.GetRelMousePos(MainPanelPos);
		DrawCanvasElements(jpMainSize, MainCanvas, DP, ShowCanvas, ShowGrid, ShowMousePos, MainPanelPos);
		if (ShowStructure & Struct.getCoords() != null & Elem == null)
		{
			DP.DrawStructureContour3D(Struct.getCoords(), Structure.color);
		}
		
		DP.DrawCircle(MainCanvasCenter, 10, 1, false, true, ColorPalette[0], ColorPalette[7]);
		if (StructureCreationIsOn & Struct.getCoords() != null)
		{
			DP.DrawElemAddition(Struct.getCoords(), MousePos, 2, Struct.getShape(), ColorPalette[6]);
		}
		if (ShowElems & Elem != null)
		{
			if (ShowDeformedStructure)
			{
				MainCanvas.setTitle("Estrutura deformada (x " + String.valueOf(Util.Round(DiagramScales[1], 3)) + ")");
			}
			DP.DrawElements3D(Node, Elem, SelectedElems, ShowMatColor, ShowSecColor, ShowElemContour, ShowDeformedStructure, DiagramScales[1]);
		}
		if (ShowNodes & Node != null)
		{
			DP.DrawNodes3D(Node, SelectedNodes, Nodes.color, ShowDeformedStructure, Elem[0].getDOFs(), DiagramScales[1]);
		}
		if (AnalysisIsComplete)
		{
			DrawResults(MainCanvas, Struct, Node, Elem, SelectedElems, SelectedVar, ShowElemContour, ShowDeformedStructure, DiagramScales, ShowDisplacementContour, ShowStressContour, ShowStrainContour, ShowInternalForces, NonlinearMat, NonlinearGeo, DP);
			if (ShowReactionArrows & Reaction != null)
			{
				DP.DrawReactions3D(Node, Reaction, Elem[0].getDOFs(), ShowReactionValues, Reactions.color, ShowDeformedStructure, DiagramScales[1]);
			}
		}
		if (ShowSup & Sup != null)
		{
			DP.DrawSup3D(Node, Sup, Supports.color);
		}
		if (ShowConcLoads & ConcLoad != null)
		{
			DP.DrawConcLoads3D(Node, ConcLoad, Elem[0].getDOFs(), ShowLoadsValues, ConcLoads.color, ShowDeformedStructure, DiagramScales[1]);
		}
		if (ShowDistLoads & DistLoad != null)
		{
			DP.DrawDistLoads3D(Node, Elem, DistLoad, ShowLoadsValues, DistLoads.color, ShowDeformedStructure, Elem[0].getDOFs(), DiagramScales[1]);
		}
		if (ShowNodalDisps & NodalDisp != null)
		{
			DP.DrawNodalDisps3D(Node, NodalDisp, Elem[0].getDOFs(), ShowLoadsValues, NodalDisps.color, ShowDeformedStructure, DiagramScales[1]);
		}
		if (ShowDOFNumber & Node != null)
		{
			DP.DrawDOFNumbers(Node, Nodes.color, ShowDeformedStructure);
		}
		if (ShowNodeNumber & Node != null)
		{
			DP.DrawNodeNumbers(Node, Nodes.color, ShowDeformedStructure);
		}
		if (ShowElemNumber & Elem != null)
		{
			DP.DrawElemNumbers(Node, Elem, Nodes.color, ShowDeformedStructure);
		}
		if (ShowNodeSelectionWindow)
		{
			DP.DrawSelectionWindow(NodeSelectionWindowInitialPos, MousePos);
		}
		if (ShowElemSelectionWindow)
		{
			DP.DrawSelectionWindow(ElemSelectionWindowInitialPos, MousePos);
		}
		if (ShowElemDetails)
		{
			DP.DrawElemDetails(ElemType.valueOf(SelectedElemType.toUpperCase()));
		}
	}
	
	public static void DrawOnListsPanel(Dimension jpListSize, boolean[] AssignmentIsOn, DrawingOnAPanel DP)
	{
		Object[] TypesInfo = MenuFunctions.GetTypesInfo();
		List<Material> matType = (List<Material>) TypesInfo[1];
		List<Section> SecType = (List<Section>) TypesInfo[2];
		int[][] SupType = (int[][]) TypesInfo[3];
		double[][] ConcLoadType = (double[][]) TypesInfo[4];
		double[][] DistLoadType = (double[][]) TypesInfo[5];
		double[][] NodalDispType = (double[][]) TypesInfo[6];
		if (AssignmentIsOn[0] & matType != null)
		{
			String[] MatNames = new String[] {
				    "Nome",
				    "E (GPa)",
				    "v",
				    "fu (MPa)"
				};
//			TODO DP.DrawLists(jpListSize, SelectedMat, MatNames, "Materials list", "Mat", matType);
		}
		if (AssignmentIsOn[1] & SecType != null)
		{
			String[] SecNames = new String[] {"Nome", "Espessura (mm)"};
//			DP.DrawLists(jpListSize, SelectedSec, SecNames, "Sections list", "Sec", SecType);
		}
		if (AssignmentIsOn[2] & SupType != null)
		{
			String[] SupNames = new String[] {
				    "Nome",
				    "Rx",
				    "Ry",
				    "Rz",
				    "Tetax",
				    "Tetay",
				    "Tetaz"
				};
			DP.DrawLists(jpListSize, SelectedSup, SupNames, "Supports list", "Sup", SupType);
		}
		if (AssignmentIsOn[3] & ConcLoadType != null)
		{
			String[] ConcLoadNames = new String[] {
				    "Nome",
				    "Fx (kN)",
				    "Fy (kN)",
				    "Fz (kN)",
				    "Mx (kN)",
				    "My (kN)",
				    "Mz (kN)"
				};
			DP.DrawLists(jpListSize, SelectedConcLoad, ConcLoadNames, "Conc loads list", "Conc load", ConcLoadType);
		}
		if (AssignmentIsOn[4] & DistLoadType != null)
		{
			String[] DistLoadNames = new String[] {
				    "Nome",
				    "Tipo",
				    "Pini (kN/m)",
				    "Pfin (kN/m)",
				    "Distini (m)",
				    "Distfin (m)"
				};
			DP.DrawLists(jpListSize, SelectedDistLoad, DistLoadNames, "Dist loads list", "Dist load", DistLoadType);
		}
		if (AssignmentIsOn[5] & NodalDispType != null)
		{
			String[] NodalDispNames = new String[] {
				    "Nome",
				    "desl x (m)",
				    "desl y (m)",
				    "desl z (m)",
				    "rot x",
				    "rot y",
				    "rot z"
				};
			DP.DrawLists(jpListSize, SelectedNodalDisp, NodalDispNames, "Nodal disps list", "Nodal disp", NodalDispType);
		}
	}
	
	public static void DrawOnLegendPanel(Dimension jpListSize, DrawingOnAPanel DP)
	{
		if (-1 < SelectedVar)
		{
			int[] LegendPos = new int[] {(int) (0.1 * jpListSize.getWidth()), (int) (0.3 * jpListSize.getHeight())};
			int[] LegendSize = new int[] {(int) (0.8 * jpListSize.getWidth()), (int) (0.6 * jpListSize.getHeight())};
			if (ShowDisplacementContour)
			{
				DP.DrawLegend(LegendPos, "Red to green", "Campo de deslocamentos (m)", LegendSize, Struct.getDispMin()[SelectedVar], Struct.getDispMax()[SelectedVar], 1);
			}
			if (ShowStressContour & Node != null & Elem != null)
			{
				DP.DrawLegend(LegendPos, "Red to green", "Campo de tensoes (MPa)", LegendSize, Struct.getStressMin()[SelectedVar], Struct.getStressMax()[SelectedVar], 1000);
			}
			if (ShowStrainContour & Node != null & Elem != null)
			{
				DP.DrawLegend(LegendPos, "Red to green", "Campo de deformacoes", LegendSize, Struct.getStrainMin()[SelectedVar], Struct.getStrainMax()[SelectedVar], 1);
			}
			if (ShowInternalForces & Node != null & Elem != null)
			{
				DP.DrawLegend(LegendPos, "Red to green", "Forcas internas (kN ou kNm)", LegendSize, Struct.getInternalForcesMin()[SelectedVar], Struct.getInternalForcesMax()[SelectedVar], 1);
			}
		}
	}
	
	public static void DrawOnLDPanel(Dimension jpLDSize, DrawingOnAPanel DP)
	{
		if (AnalysisIsComplete)
		{
			if (SelectedNodes != null)
			{
				int nodeid = SelectedNodes[0];
				if (Node[nodeid] != null)
				{
					Dimension LDPanelSize = jpLDSize;
					int[] CurvePos = new int[] {(int) (0.38 * LDPanelSize.getWidth()), (int) (0.8 * LDPanelSize.getHeight())};
					int[] CurveSize = new int[] {(int) (0.8 * LDPanelSize.getWidth()), (int) (0.6 * LDPanelSize.getHeight())};
					if (1 < SelectedNodes.length)
					{
						double[] Xaxisvalues = new double[SelectedNodes.length], Yaxisvalues = new double[SelectedNodes.length];
						int dir = -1;
						int dof = SelectedVar;
						
						double[] FirstNodePos = Node[SelectedNodes[0]].getOriginalCoords();
						double[] FinalNodePos = Node[SelectedNodes[SelectedNodes.length - 1]].getOriginalCoords();
						if (FinalNodePos[1] - FirstNodePos[1] <= FinalNodePos[0] - FirstNodePos[0])
						{
							dir = 0;
						}
						else
						{
							dir = 1;
						}
						
						for (int node = 0; node <= SelectedNodes.length - 1; node += 1)
						{
							int nodeID = SelectedNodes[node];
							Xaxisvalues[node] = Node[nodeID].getOriginalCoords()[dir] - Util.FindMinInPos(Struct.getCoords(), dir);
						}
						if (SelectedDiagram == 0)
						{
							for (int node = 0; node <= SelectedNodes.length - 1; node += 1)
							{
								int nodeID = SelectedNodes[node];
								Yaxisvalues[node] = Node[nodeID].getDisp()[dof];
							}
						}
						else if (SelectedDiagram == 1)
						{
							for (int node = 0; node <= SelectedNodes.length - 1; node += 1)
							{
								int nodeID = SelectedNodes[node];
								int elemID = -1;
								for (int elem = 0; elem <= Elem.length - 1; elem += 1)
								{
									if (Util.ArrayContains(Elem[elem].getExternalNodes(), nodeID))
									{
										elemID = elem;
									}
								}
								Yaxisvalues[node] = Elem[elemID].getStress()[dof];
							}
						}
						else if (SelectedDiagram == 2)
						{
							for (int node = 0; node <= SelectedNodes.length - 1; node += 1)
							{
								int nodeID = SelectedNodes[node];
								int elemID = -1;
								for (int elem = 0; elem <= Elem.length - 1; elem += 1)
								{
									if (Util.ArrayContains(Elem[elem].getExternalNodes(), nodeID))
									{
										elemID = elem;
									}
								}
								Yaxisvalues[node] = Elem[elemID].getStrain()[dof];
							}
						}
						else if (SelectedDiagram == 3)
						{
							for (int node = 0; node <= SelectedNodes.length - 1; node += 1)
							{
								int nodeID = SelectedNodes[node];
								int elemID = -1;
								for (int elem = 0; elem <= Elem.length - 1; elem += 1)
								{
									if (Util.ArrayContains(Elem[elem].getExternalNodes(), nodeID))
									{
										elemID = elem;
									}
								}
								Yaxisvalues[node] = Elem[elemID].getIntForces()[dof];
							}
						}
						DP.Draw2DPlot(CurvePos, Math.min(CurveSize[0], CurveSize[1]), "Resultados na seââo", "x var", "y var", Xaxisvalues, Yaxisvalues, Util.FindMin(Xaxisvalues), Util.FindMin(Yaxisvalues), Util.FindMaxAbs(Xaxisvalues), Util.FindMaxAbs(Yaxisvalues), 2, 2, ColorPalette[5], ColorPalette[10]);
					}
					else if (-1 < SelectedNodes[0])
					{
						if (-1 < SelectedVar)
						{						
							double[] XValues = Node[nodeid].LoadDisp[SelectedVar][0];
							double[] YValues = Node[nodeid].LoadDisp[SelectedVar][1];
							DP.Draw2DPlot(CurvePos, Math.min(CurveSize[0], CurveSize[1]), "Curva carga-deslocamento", "u (mm)", "Fator de carga", XValues, YValues, Util.FindMin(XValues), Util.FindMin(YValues), Util.FindMaxAbs(XValues), Util.FindMaxAbs(YValues), 3, 3, ColorPalette[5], ColorPalette[10]);					
						}
					}
				}
			}
		}						
	}
	
	public static void DrawResults(MyCanvas canvas, Structure Struct, Nodes[] Node, Element[] Elem, int[] SelectedElems, int selectedvar,
			boolean ShowElemContour, boolean ShowDeformedStructure, double[] DiagramsScales, boolean ShowDisplacementContour, boolean ShowStressContour,
			boolean ShowStrainContour, boolean ShowInternalForces, boolean NonlinearMat, boolean NonlinearGeo, DrawingOnAPanel DP)
	{
		if (-1 < selectedvar & Node != null & Elem != null)
		{
			if (ShowDisplacementContour)
			{
				canvas.setTitle("Deslocamentos (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
				DP.DrawContours3D(Elem, Node, SelectedElems, Struct.getCenter(), ShowElemContour, ShowDeformedStructure, DiagramsScales[1], Struct.getDispMin()[selectedvar], Struct.getDispMax()[selectedvar], "Displacement", selectedvar, NonlinearMat, NonlinearGeo, "Red to green");
			}
			else if (ShowStressContour)
			{
				canvas.setTitle("Tensâes (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
				DP.DrawContours3D(Elem, Node, SelectedElems, Struct.getCenter(), ShowElemContour, ShowDeformedStructure, DiagramsScales[1], Struct.getStressMin()[selectedvar], Struct.getStressMax()[selectedvar], "Stress", selectedvar,  NonlinearMat, NonlinearGeo, "Red to green");
			}
			else if (ShowStrainContour)
			{
				canvas.setTitle("Deformaçõs (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
				DP.DrawContours3D(Elem, Node, SelectedElems, Struct.getCenter(), ShowElemContour, ShowDeformedStructure, DiagramsScales[1], Struct.getStrainMin()[selectedvar], Struct.getStrainMax()[selectedvar], "Strain", selectedvar, NonlinearMat, NonlinearGeo, "Red to green");
			}
			else if (ShowInternalForces)
			{
				canvas.setTitle("Forâas internas (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
				DP.DrawContours3D(Elem, Node, SelectedElems, Struct.getCenter(), ShowElemContour, ShowDeformedStructure, DiagramsScales[1], Struct.getInternalForcesMin()[selectedvar], Struct.getInternalForcesMax()[selectedvar], "Force", selectedvar, NonlinearMat, NonlinearGeo, "Red to green");
			}
		}
	}
	
	/* Mouse functions */
	public static void StructureCreation(int[] MainPanelPos, MyCanvas MainCanvas)
	{
		double[][] StructCoords = Struct.getCoords();
		   
		if (Util.MouseIsInside(MousePos, new int[2], MainCanvas.getPos(), MainCanvas.getSize()[0], MainCanvas.getSize()[1]))
	    {
			if (Struct.getShape().equals(StructureShape.rectangular) | Struct.getShape().equals(StructureShape.circular))
			{
				if (StructCoords != null)
				{
    				StructureCreationIsOn = false;
				}
				StructCoords = GetCoord(Struct.getShape(), MainCanvas, StructCoords, MousePos, SnipToGridIsOn);
			}
			else if (Struct.getShape().equals(StructureShape.polygonal))
			{
				int prec = 10;
				if (StructCoords != null)
				{
					if (Util.dist(new double[] {MousePos[0], MousePos[1]}, StructCoords[0]) < prec)
					{
	    				StructureCreationIsOn = false;
					}
				}
				StructCoords = GetCoord(Struct.getShape(), MainCanvas, StructCoords, MousePos, SnipToGridIsOn);
			}
			else
			{
				System.out.println("Structure shape not identified at Menus -> CreateStructure");
			}
	    }

		Struct.setCoords(StructCoords);
		if (!StructureCreationIsOn)
		{
			for (int c = 0; c <= StructCoords.length - 1; c += 1)
			{
				StructCoords[c] = Util.ConvertToRealCoords(new int[] {(int) StructCoords[c][0], (int) StructCoords[c][1], 0}, MainCanvas.getPos(), MainCanvas.getSize(), MainCanvas.getDim());
			}
			if (Struct.getShape().equals(StructureShape.polygonal))
			{
				StructCoords[StructCoords.length - 1] = StructCoords[0];
			}
			Struct.setCenter(Util.MatrixAverages(Struct.getCoords()));
			MainCanvas.setCenter(Util.ConvertToDrawingCoords(Struct.getCenter(), MainCanvas.getPos(), MainCanvas.getSize(), MainCanvas.getDim(), MainCanvas.getDrawingPos()));
			ShowStructure = true;
		}
	}

	public static void NodeAddition(MyCanvas MainCanvas, int[] MainPanelPos)
	{
		if (Node != null)
		{
			SelectedNodes = null;
			SelectedNodes = Util.NodesSelection(MainCanvas, Struct.getCenter(), Node, MousePos, MainPanelPos, SelectedNodes, NodeSelectionWindowInitialPos, Elem[0].getDOFs(), DiagramScales, ShowNodeSelectionWindow, ShowDeformedStructure);
			int NodeMouseIsOn = Util.NodeMouseIsOn(Node, MousePos, MainCanvas.getPos(), MainCanvas.getSize(), MainCanvas.getDim(), MainCanvas.getDrawingPos(), 4, ShowDeformedStructure);
			if (NodeMouseIsOn == -1 | (ShowNodeSelectionWindow & -1 < NodeMouseIsOn))
			{
				ShowNodeSelectionWindow = !ShowNodeSelectionWindow;
				NodeSelectionWindowInitialPos = MousePos;
			}
		}
	}
	
	public static void ElemAddition(MyCanvas MainCanvas, int[] MainPanelPos)
	{
		if (Elem != null)
		{
			SelectedElems = null;
			SelectedElems = Util.ElemsSelection(MainCanvas, Struct.getCenter(), Node, Elem, MousePos, MainPanelPos, SelectedElems, ElemSelectionWindowInitialPos, DiagramScales, ShowElemSelectionWindow, ShowDeformedStructure);
			int ElemMouseIsOn = Util.ElemMouseIsOn(Node, Elem, MousePos, Struct.getCenter(), MainCanvas.getPos(), MainCanvas.getSize(), MainCanvas.getDim(), MainCanvas.getCenter(), MainCanvas.getDrawingPos(), ShowDeformedStructure);
			if (ElemMouseIsOn == -1 | (ShowElemSelectionWindow & -1 < ElemMouseIsOn))
			{
				ShowElemSelectionWindow = !ShowElemSelectionWindow;
				ElemSelectionWindowInitialPos = MousePos;
			}
		}
	}
	
	public static void MouseWheel(int[] MainPanelPos, MyCanvas MainCanvas, int WheelRot, boolean[] AssignmentIsOn)
	{
		boolean MouseIsInMainCanvas = Util.MouseIsInside(MousePos, MainPanelPos, MainCanvas.getPos(), MainCanvas.getSize()[0], MainCanvas.getSize()[1]);
		if (ShowCanvas & Util.MouseIsInside(MousePos, MainPanelPos, MainCanvas.getPos(), MainCanvas.getSize()[0], MainCanvas.getSize()[1]))
		{
			MainCanvas.getDim()[0] += Util.Round(0.2*Math.log10(MainCanvas.getDim()[0])*WheelRot, 1);
			MainCanvas.getDim()[1] += Util.Round(0.2*Math.log10(MainCanvas.getDim()[1])*WheelRot, 1);
		}
		if (AssignmentIsOn[0] & !MouseIsInMainCanvas)
		{
			SelectedMat += WheelRot;
			SelectedMat = Math.min(Math.max(SelectedMat, 0), matTypes.size() - 1);
		}
		if (AssignmentIsOn[1] & !MouseIsInMainCanvas)
		{
			SelectedSec += WheelRot;
			SelectedSec = Math.min(Math.max(SelectedSec, 0), secTypes.size() - 1);
		}
		if (AssignmentIsOn[2] & !MouseIsInMainCanvas)
		{
			SelectedSup += WheelRot;
			SelectedSup = Math.min(Math.max(SelectedSup, 0), SupType.length - 1);
		}
		if (AssignmentIsOn[3] & !MouseIsInMainCanvas)
		{
			SelectedConcLoad += WheelRot;
			SelectedConcLoad = Math.min(Math.max(SelectedConcLoad, 0), ConcLoadType.length - 1);
		}
		if (AssignmentIsOn[4] & !MouseIsInMainCanvas)
		{
			SelectedDistLoad += WheelRot;
			SelectedDistLoad = Math.min(Math.max(SelectedDistLoad, 0), DistLoadType.length - 1);
		}
		if (AssignmentIsOn[5] & !MouseIsInMainCanvas)
		{
			SelectedNodalDisp += WheelRot;
			SelectedNodalDisp = Math.min(Math.max(SelectedNodalDisp, 0), NodalDispType.length - 1);
		}
	}
	
	public static void updateDiagramScale(MyCanvas MainCanvas, int WheelRot)
	{
		if (AnalysisIsComplete & !Util.MouseIsInsideCanvas(MousePos, MainCanvas))
		{
			DiagramScales[1] += -Util.Round(10 * Math.log10(DiagramScales[1]) * WheelRot, 10);
			DiagramScales[1] = Math.max(DiagramScales[1], 0.001);
		}
	}

	public static void ResetStructure()
	{
		Struct = new Structure(null, null, null);
		Node = null;
		Elem = null;
		matTypes = null;
		secTypes = null;
		Sup = null;
		ConcLoadType = null ;
		ConcLoad = null;
		DistLoadType = null ;
		DistLoad = null;
		NodalDispType = null ;
		NodalDisp = null;
		ShowStructure = false;
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
		ShowNodeSelectionWindow = false;
		ShowElemSelectionWindow = false;
		StructureCreationIsOn = false;
		ShowLoadsValues = true;
		AnalysisIsComplete = false;
		ShowDisplacementContour = false;
		ShowDeformedStructure = false;
		ShowStressContour = false;
		ShowStrainContour = false;
		ShowInternalForces = false;
	}
}
