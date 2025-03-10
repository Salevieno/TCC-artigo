package main.mainTCC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.gui.DrawingOnAPanel;
import main.gui.Menus;
import main.output.Results;
import main.output.SaveOutput;
import main.structure.ConcLoads;
import main.structure.DistLoads;
import main.structure.ElemType;
import main.structure.Element;
import main.structure.Material;
import main.structure.Mesh;
import main.structure.MeshType;
import main.structure.MyCanvas;
import main.structure.NodalDisps;
import main.structure.Node;
import main.structure.Reactions;
import main.structure.Section;
import main.structure.Structure;
import main.structure.StructureShape;
import main.structure.Supports;
import main.utilidades.Point3D;
import main.utilidades.Util;

public abstract class MenuFunctions
{

	public static Point mousePos;
	public static boolean ShowNodes ;
	public static boolean ShowElems, ShowConcLoads, ShowDistLoads, ShowNodalDisps, ShowReactionArrows, ShowReactionValues, ShowLoadsValues, ShowSup;
	public static boolean ShowDOFNumber, ShowNodeNumber, ShowElemNumber, ShowElemContour, ShowMatColor, ShowSecColor, ShowElemDetails;
	private static boolean ShowCanvas;
	public static boolean ShowMousePos;
	public static boolean StructureCreationIsOn;
	public static boolean SnipToGridIsOn;
	public static boolean ShowElemSelectionWindow;
	public static boolean AnalysisIsComplete;
	public static boolean ShowDisplacementContour, ShowStressContour, ShowStrainContour, ShowInternalForces;
	public static boolean ShowDeformedStructure ;
	public static boolean NodeSelectionIsOn, ElemSelectionIsOn;
	
	public static Structure struct;
	public static ConcLoads[] ConcLoad;
	public static DistLoads[] DistLoad;
	public static NodalDisps[] NodalDisp;
	public static Reactions[] Reaction;
	
	public static int[] SelectedNodes, SelectedElems;
	public static int SelectedMat;
	public static int SelectedSec;
	public static int SelectedSup;
	public static int SelectedConcLoad;
	public static int SelectedDistLoad;
	public static int SelectedNodalDisp;
	
	public static int SelectedDiagram = -1;
	public static int SelectedVar = -1;
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
		ShowCanvas = true;
		ShowMousePos = true;
		StructureCreationIsOn = false;
		SnipToGridIsOn = false;
		ShowDeformedStructure = false;			
		ShowLoadsValues = true;
		
		SupType = Supports.Types;
		
		NonlinearMat = false;
		NonlinearGeo = false;
		
		AnalysisIsComplete = false;
		NodeSelectionIsOn = false;
		ElemSelectionIsOn = false;
		
		struct = new Structure(null, null, null);

		SelectedMat = -1;
		SelectedSec = -1;
		SelectedSup = -1;
		SelectedConcLoad = -1;
		SelectedDistLoad = -1;
		SelectedNodalDisp = -1;
		DiagramScales = new double[2];
		
		SelectedNodes = null;
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

		double[] mousePosRealCoords = Util.ConvertToRealCoords(MousePos, new int[] {canvas.getPos().x, canvas.getPos().y}, canvas.getSize(), canvas.getDimension()) ;
		return new Point3D(mousePosRealCoords[0], mousePosRealCoords[1], 0.0) ;

	}

	public static double[][] GetCoordFromMouseClic2k(StructureShape structshape, MyCanvas canvas, double[][] Coords, Point MousePos, boolean SnipToGridIsOn)
	{
		Point NewDrawingCoord;
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
    		Coords = Util.AddElem(Coords, new double[] {NewDrawingCoord.x, NewDrawingCoord.y});
		}
		else
		{
			switch(structshape)
			{
				case rectangular:
				
					Coords = Util.AddElem(Coords, new double[] {Coords[0][0], NewDrawingCoord.y});
					Coords = Util.AddElem(Coords, new double[] {NewDrawingCoord.x, NewDrawingCoord.y});
					Coords = Util.AddElem(Coords, new double[] {NewDrawingCoord.x, Coords[0][1]});
					
					break;

				case circular:

					double[] Center = new double[] {Coords[0][0], Coords[0][1], 0};
					double[] Point2 = new double[] {NewDrawingCoord.x, NewDrawingCoord.y, 0};
					double r = Util.dist(Center, Point2);
					int NPoints = 20;
					Coords[0][0] += r;
					for (int node = 1; node <= NPoints - 1; node += 1)
					{
						double angle = node * 2 * Math.PI / (double) NPoints;
						Coords = Util.AddElem(Coords, new double[] {Center[0] + r*Math.cos(angle), Center[1] + r*Math.sin(angle), 0});
					}

					break ;

				case polygonal:

	    			Coords = Util.AddElem(Coords, new double[] {NewDrawingCoord.x, NewDrawingCoord.y});

					break ;
					
			}
		}
		NewCoord = Coords;
		return NewCoord;
	}
	
	public static boolean CheckIfAnalysisIsReady()
	{
		if (MenuFunctions.struct.getMesh() != null &&
			MenuFunctions.struct.getMesh().getNodes() != null & MenuFunctions.struct.getMesh().getElements() != null &
			Util.AllElemsHaveMat(MenuFunctions.struct.getMesh().getElements()) &
			Util.AllElemsHaveSec(MenuFunctions.struct.getMesh().getElements()) &
			MenuFunctions.struct.getSupports() != null & (ConcLoad != null | DistLoad != null | NodalDisp != null))
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
        if (MenuFunctions.struct.getMesh() != null && MenuFunctions.struct.getMesh().getElements() != null)
        {
    		if (Util.AllElemsHaveElemType(MenuFunctions.struct.getMesh().getElements()))
    		{
    			StepIsComplete[0] = true;
    		}
        }
		if (SelectedElemType != null)
		{
			StepIsComplete[0] = true;
		}
        if (struct.getCoords() != null)
        {
    		StepIsComplete[1] = true;
        }
        if (MenuFunctions.struct.getMesh() != null && MenuFunctions.struct.getMesh().getNodes() != null && MenuFunctions.struct.getMesh().getElements() != null)
        {
    		StepIsComplete[2] = true;
        }
        if (MenuFunctions.struct.getMesh() != null && MenuFunctions.struct.getMesh().getElements() != null)
        {
    		if (Util.AllElemsHaveMat(MenuFunctions.struct.getMesh().getElements()))
    		{
    			StepIsComplete[3] = true;
    		}
    		if (Util.AllElemsHaveSec(MenuFunctions.struct.getMesh().getElements()))
    		{
    			StepIsComplete[4] = true;
    		}
        }
		if (MenuFunctions.struct.getSupports() != null)
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
		for (int elem = 0; elem <= MenuFunctions.struct.getMesh().getElements().size() - 1; elem += 1)
		{
			if (AssignmentIsOn[0])
			{
				MenuFunctions.struct.getMesh().getElements().get(elem).setMat(null);
			}
			if (AssignmentIsOn[1])
			{
				MenuFunctions.struct.getMesh().getElements().get(elem).setSec(null);
			}
		}
		if (AssignmentIsOn[2])
		{
			for (int node = 0; node <= MenuFunctions.struct.getMesh().getNodes().size() - 1; node += 1)
			{
				MenuFunctions.struct.getMesh().getNodes().get(node).setSup(null);
			}
			MenuFunctions.struct.removeSupports() ;
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
		for (int node = 0; node <= MenuFunctions.struct.getMesh().getElements().get(0).getExternalNodes().length - 1; node += 1)
		{
			SelectedVar = Util.ElemPosInArray(MenuFunctions.struct.getMesh().getElements().get(0).getDOFsPerNode()[node], SelectedVar);
			if (-1 < SelectedVar)
			{
				node = MenuFunctions.struct.getMesh().getElements().get(0).getExternalNodes().length - 1;
			}
		}
		if (SelectedDiagram == 0 & -1 < SelectedVar)
		{
			//DrawDisplacementContours(SelectedVar);
			struct.getResults().setDispMin(Results.FindMinDisps(struct.getU(), MenuFunctions.struct.getMesh().getElements().get(0).getDOFs(), Analysis.DefineFreeDoFTypes(MenuFunctions.struct.getMesh().getNodes()))) ;
			struct.getResults().setDispMax(Results.FindMaxDisps(struct.getU(), MenuFunctions.struct.getMesh().getElements().get(0).getDOFs(), Analysis.DefineFreeDoFTypes(MenuFunctions.struct.getMesh().getNodes()))) ;
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
	public static void SaveFile(String FileName, MyCanvas MainCanvas, Structure Struct, List<Node> nodes, List<Element> elems,
			List<Supports> Sup, ConcLoads[] ConcLoads, DistLoads[] DistLoads, NodalDisps[] NodalDisps, List<Material> UserDefinedMat, List<Section> UserDefinedSec)
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
			values[1] = new Object[Struct.getCoords().size()][3];
			for (int i = 0; i <= Struct.getCoords().size() - 1; i += 1)
			{
				values[1][i][0] = Struct.getCoords().get(i).x;
				values[1][i][1] = Struct.getCoords().get(i).y;
				values[1][i][2] = Struct.getCoords().get(i).z;
			}
		}
		if (nodes != null)
		{
			values[2] = new Object[nodes.size()][7];
			for (int i = 0; i <= nodes.size() - 1; i += 1)
			{
				Node node = nodes.get(i);
				values[2][i][0] = node.getID();
				values[2][i][1] = Util.Round(node.getOriginalCoords().x, 8);
				values[2][i][2] = Util.Round(node.getOriginalCoords().y, 8);
				values[2][i][3] = Util.Round(node.getOriginalCoords().z, 8);
				values[2][i][4] = node.getSup();
				values[2][i][5] = node.getConcLoads();
				values[2][i][6] = node.getNodalDisps();
			}
		}
		if (elems != null)
		{
			values[3] = new Object[elems.size()][elems.get(0).getExternalNodes().length + 4];
			for (int i = 0; i <= elems.size() - 1; i += 1)
			{
				Element elem = elems.get(i) ;
				int NElemNodes = elem.getExternalNodes().length;
				values[3][i][0] = elem.getID();
				values[3][i][1] = elem.getType();
				for (int elemnode = 0; elemnode <= NElemNodes - 1; elemnode += 1)
				{
					values[3][i][elemnode + 2] = elem.getExternalNodes()[elemnode];
				}
				values[3][i][NElemNodes + 2] = elem.getMat();
				values[3][i][NElemNodes + 3] = elem.getSec();
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
			values[6] = new Object[Sup.size()][8];
			for (int sup = 0; sup <= Sup.size() - 1; sup += 1)
			{
				values[6][sup][0] = Sup.get(sup).getID();
				values[6][sup][1] = Sup.get(sup).getNode();
				values[6][sup][2] = Sup.get(sup).getDoFs()[0];
				values[6][sup][3] = Sup.get(sup).getDoFs()[1];
				values[6][sup][4] = Sup.get(sup).getDoFs()[2];
				values[6][sup][5] = Sup.get(sup).getDoFs()[3];
				values[6][sup][6] = Sup.get(sup).getDoFs()[4];
				values[6][sup][7] = Sup.get(sup).getDoFs()[5];
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
		values[10][0][4] = Util.Round(MainCanvas.getDimension()[0], 8);
		values[10][0][5] = Util.Round(MainCanvas.getDimension()[1], 8);
		values[10][0][6] = Util.Round(MainCanvas.getAngles()[0], 8);
		values[10][0][7] = Util.Round(MainCanvas.getAngles()[1], 8);
		values[10][0][8] = Util.Round(MainCanvas.getAngles()[2], 8);
		SaveOutput.SaveStructureToTxt(FileName, InputSections, InputVariables, values);		// Save the structure in an input file (.txt)
	}

	public static void LoadFile(String Path, String FileName)
	{
		if (!FileName.equals("")) 
		{			
			String[][] Input = ReadInput.ReadTxtFile(Path + FileName + ".txt");			// Loads the input file (.txt)
			if (Input != null)
			{
				struct.setName(Input[0][2]);
				List<Point3D> StructCoords = new ArrayList<>() ;
				for (int coord = 0; coord <= Input[1].length - 4; coord += 1)
				{
					String[] Line = Input[1][coord + 2].split("	");
					StructCoords.add(new Point3D(Double.parseDouble(Line[0]), Double.parseDouble(Line[1]), Double.parseDouble(Line[2]))) ;
				}
				struct.setCoords(StructCoords);
				struct.updateCenter() ;

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
					MenuFunctions.struct.getMesh().getNodes().add(NewNode) ;
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
					MenuFunctions.struct.getMesh().getElements().add(NewElem) ;
				}
				
				for (int sup = 0; sup <= Input[6].length - 4; sup += 1)
				{
					String[] Line = Input[6][sup + 2].split("	");
					Supports NewSup;
					NewSup = new Supports(-1, -1, null);
					NewSup.setID(Integer.parseInt(Line[0]));
					NewSup.setNode(Integer.parseInt(Line[1]));
					NewSup.setDoFs(new int[] {Integer.parseInt(Line[2]), Integer.parseInt(Line[3]), Integer.parseInt(Line[4]), Integer.parseInt(Line[5]), Integer.parseInt(Line[6]), Integer.parseInt(Line[7])});
					MenuFunctions.struct.addSupport(NewSup);
					MenuFunctions.struct.getMesh().getNodes().get(Integer.parseInt(Line[1])).setSup(NewSup.getDoFs());
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
		MenuFunctions.struct.printStructure(matTypes, secTypes, MenuFunctions.struct.getSupports(), ConcLoad, DistLoad, NodalDisp);
	}

	/* Structure menu functions */

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
		struct.NFreeDOFs = -1;
		for (int node = 0; node <= MenuFunctions.struct.getMesh().getNodes().size() - 1; node += 1)
        {
			MenuFunctions.struct.getMesh().getNodes().get(node).setDOFType(Util.DefineDOFsOnNode(MenuFunctions.struct.getMesh().getElements()));
			MenuFunctions.struct.getMesh().getNodes().get(node).calcdofs(MenuFunctions.struct.getSupports(), struct.NFreeDOFs + 1);
			for (int dof = 0; dof <= MenuFunctions.struct.getMesh().getNodes().get(node).dofs.length - 1; dof += 1)
	        {
				if (-1 < MenuFunctions.struct.getMesh().getNodes().get(node).dofs[dof])
				{
					struct.NFreeDOFs = MenuFunctions.struct.getMesh().getNodes().get(node).dofs[dof];
				}
	        }
			MenuFunctions.struct.getMesh().getNodes().get(node).setLoadDispCurve();
        }
		struct.NFreeDOFs += 1;
		for (Element elem : MenuFunctions.struct.getMesh().getElements())
		{
			elem.setCumDOFs(Util.CumDOFsOnElem(MenuFunctions.struct.getMesh().getNodes(), elem.getExternalNodes().length));
        	elem.setUndeformedCoords(MenuFunctions.struct.getMesh().getNodes());
        	elem.setCenterCoords();
		}
		for (int elem = 0; elem <= MenuFunctions.struct.getMesh().getElements().size() - 1; elem += 1)
		{
			int[][] ElemNodeDOF = null;
			for (int elemnode = 0; elemnode <= MenuFunctions.struct.getMesh().getElements().get(elem).getExternalNodes().length - 1; elemnode += 1)
	    	{
				int node = MenuFunctions.struct.getMesh().getElements().get(elem).getExternalNodes()[elemnode];
				ElemNodeDOF = Util.AddElem(ElemNodeDOF, MenuFunctions.struct.getMesh().getNodes().get(node).dofs);
	    	}
			MenuFunctions.struct.getMesh().getElements().get(elem).setNodeDOF(ElemNodeDOF);
		}
		if (ConcLoadType != null)
		{
			for (int loadid = 0; loadid <= ConcLoadType.length - 1; loadid += 1)
			{
				int nodeid = (int) ConcLoadType[loadid][1];
				if (-1 < nodeid)
				{
					ConcLoad[loadid] = new ConcLoads(loadid, nodeid, ConcLoadType[loadid]);
					MenuFunctions.struct.getMesh().getNodes().get(nodeid).setConcLoads(Util.AddElem(MenuFunctions.struct.getMesh().getNodes().get(nodeid).getConcLoads(), ConcLoad[loadid]));
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
					MenuFunctions.struct.getMesh().getElements().get(elemid).setDistLoads(Util.AddElem(MenuFunctions.struct.getMesh().getElements().get(elemid).getDistLoads(), DistLoad[loadid]));
				}
			}
		}
	}
	

	public static void PostAnalysis()
	{
		/* Record results and set up their view*/
		struct.setU(struct.getU());
		Reaction = Analysis.Reactions(MenuFunctions.struct.getMesh(), MenuFunctions.struct.getSupports(), NonlinearMat, NonlinearGeo, struct.getU());
		for (Element elem : MenuFunctions.struct.getMesh().getElements())
		{
			elem.RecordResults(MenuFunctions.struct.getMesh().getNodes(), struct.getU(), NonlinearMat, NonlinearGeo);
		}
		struct.getResults().register(MenuFunctions.struct.getMesh(), MenuFunctions.struct.getSupports(), struct.getU(), NonlinearMat, NonlinearGeo);
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
		Reactions.setSumReactions(Analysis.SumReactions(Reaction));		
	}
	
	/* Results menu functions */
	public static void ResultsMenuSaveResults()
	{
		String[] Sections = new String[] {"Min Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Max Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Min Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Max Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Reacoes \nno	Fx (kN)	Fy (kN)	Fz (kN)	Mx (kNm)	My (kNm)	Mz (kNm)", "Soma das reacoes \nFx (kN)	Fy (kN)	Fz (kN)	Mx (kNm)	My (kNm)	Mz (kNm)"};
		String[][][] vars = new String[Sections.length][][];
		vars[0] = new String[1][3];
		vars[1] = new String[1][3];
		vars[2] = new String[MenuFunctions.struct.getMesh().getNodes().size()][];
		vars[3] = new String[1][3];
		vars[4] = new String[1][3];
		vars[5] = new String[MenuFunctions.struct.getMesh().getNodes().size()][];
		vars[6] = new String[Reaction.length][];
		vars[7] = new String[1][Reactions.SumReactions.length];
		for (int sec = 0; sec <= Sections.length - 1; sec += 1)
		{
			if (sec == 0)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(struct.getResults().getDispMin()[dof]);
				}
			}
			if (sec == 1)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(struct.getResults().getDispMax()[dof]);
				}
			}
			if (sec == 3)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(struct.getResults().getInternalForcesMin()[dof]);
				}
			}
			if (sec == 4)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(struct.getResults().getInternalForcesMax()[dof]);
				}
			}
			for (int node = 0; node <= vars[sec].length - 1; node += 1)
			{
				if (sec == 2)
				{
					vars[sec][node] = new String[MenuFunctions.struct.getMesh().getNodes().get(node).getDOFType().length + 1];
					vars[sec][node][0] = String.valueOf(node);
					for (int dof = 0; dof <= MenuFunctions.struct.getMesh().getNodes().get(node).getDOFType().length - 1; dof += 1)
					{
						if (-1 < MenuFunctions.struct.getMesh().getNodes().get(node).dofs[dof])
						{
							vars[sec][node][dof + 1] = String.valueOf(struct.getU()[MenuFunctions.struct.getMesh().getNodes().get(node).dofs[dof]]);
						}
						else
						{
							vars[sec][node][dof + 1] = String.valueOf(0);
						}
					}
				}
				if (sec == 5)
				{
					vars[sec][node] = new String[MenuFunctions.struct.getMesh().getNodes().get(node).getDOFType().length + 1];
					vars[sec][node][0] = String.valueOf(node);
					for (int i = 0; i <= MenuFunctions.struct.getMesh().getElements().size() - 1; i += 1)
					{
						Element elem = MenuFunctions.struct.getMesh().getElements().get(i);
						for (int elemnode = 0; elemnode <= elem.getExternalNodes().length - 1; elemnode += 1)
						{
							if (node == elem.getExternalNodes()[elemnode])
							{
								for (int dof = 0; dof <= MenuFunctions.struct.getMesh().getNodes().get(node).getDOFType().length - 1; dof += 1)
								{
									vars[sec][node][dof + 1] = String.valueOf(elem.getIntForces()[elemnode*MenuFunctions.struct.getMesh().getNodes().get(node).getDOFType().length + dof]);
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
		SaveOutput.SaveOutput(struct.getName(), Sections, vars);
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
			double[][][] loaddisp = MenuFunctions.struct.getMesh().getNodes().get(nodeid).LoadDisp;
			String[] Sections = new String[] {"Deslocamentos", "Fator de carga"};
			String[][][] vars = new String[Sections.length][MenuFunctions.struct.getMesh().getNodes().get(nodeid).dofs.length][loaddisp[0][0].length];
			for (int sec = 0; sec <= Sections.length - 1; sec += 1)
			{
				for (int dof = 0; dof <= MenuFunctions.struct.getMesh().getNodes().get(nodeid).dofs.length - 1; dof += 1)
				{
					for (int loadinc = 0; loadinc <= loaddisp[dof][sec].length - 1; loadinc += 1)
					{
						vars[sec][dof][loadinc] = String.valueOf(loaddisp[dof][sec][loadinc]);
					}
				}
			}
			SaveOutput.SaveOutput(struct.getName(), Sections, vars);
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
			ConcLoads[] ConcLoad = (ConcLoads[]) Structure[3];
			DistLoads[] DistLoad = (DistLoads[]) Structure[4];
			NodalDisps[] NodalDisp = (NodalDisps[]) Structure[5];
			
			System.out.println("Struct coords: " + struct.getCoords());
			System.out.println("Struct shape: " + struct.getShape());
			System.out.println("Struct center: " + struct.getCenter());
			System.out.println("Struct min coords: " + struct.getMinCoords());
			System.out.println("Struct max coords: " + struct.getMaxCoords());
			System.out.println("Struct free dofs: " + struct.NFreeDOFs);
			System.out.println("meshType: " + meshType);
			System.out.println("MeshSize: " + MeshSize);
			System.out.println("elemType: " + elemType);
			System.out.println("matTypes.get(Mat): " + matTypes.get(Mat));
			System.out.println("matTypes: " + matTypes);
			System.out.println("secTypes.get(Sec): " + secTypes.get(Sec));
			System.out.println("secTypes: " + secTypes);
			System.out.println("supConfig: " + supConfig);
			System.out.println("SelConcLoad: " + SelConcLoad);
			System.out.println("SelDistLoad: " + SelDistLoad);
			System.out.println("ConcLoadConfig: " + ConcLoadConfig);
			System.out.println("DistLoadConfig: " + DistLoadConfig);
			
			System.out.print("Anâlise num " + run + ": ");
			//UtilComponents.PrintStructure(MeshType, Node, Elem, EspecialMat, EspecialSec, Sup, ConcLoads, DistLoads, NodalDisps);

			boolean NonlinearMat = true;
			boolean NonlinearGeo = false;
			Analysis.run(struct, ConcLoad, DistLoad, NodalDisp, NonlinearMat, NonlinearGeo, 10, 5, 15.743);
			
			/* Analysis is complete */
			PostAnalysis();

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
		
		return struct;
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
		Analysis.run(struct, ConcLoad, DistLoad, NodalDisp, NonlinearMat, NonlinearGeo, 1, 1, 1);
		PostAnalysis();
		AnalysisTime = System.currentTimeMillis() - AnalysisTime;
		for (Element elem : MenuFunctions.struct.getMesh().getElements())
		{
			elem.RecordResults(MenuFunctions.struct.getMesh().getNodes(), struct.getU(), NonlinearMat, NonlinearGeo);
        	elem.setDeformedCoords(MenuFunctions.struct.getMesh().getNodes());
		}
		struct.getResults().register(MenuFunctions.struct.getMesh(), MenuFunctions.struct.getSupports(), struct.getU(), NonlinearMat, NonlinearGeo);
		NodeSelectionIsOn = true;
		ElemSelectionIsOn = true;
		AnalysisIsComplete = true;
		ShowReactionArrows = true;
		ShowReactionValues = true;
		ShowDeformedStructure = true;
		double MaxDisp = Util.FindMaxAbs(struct.getU());
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
	
	public static void DrawOnListsPanel(Dimension jpListSize, boolean[] AssignmentIsOn, DrawingOnAPanel DP)
	{
		
		List<Material> matType = MenuFunctions.matTypes;
		List<Section> SecType = MenuFunctions.secTypes;
		int[][] SupType = MenuFunctions.SupType;
		double[][] ConcLoadType = MenuFunctions.ConcLoadType;
		double[][] DistLoadType = MenuFunctions.DistLoadType;
		double[][] NodalDispType = MenuFunctions.NodalDispType;

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
				DrawLegend(LegendPos, "Red to green", "Campo de deslocamentos (m)", LegendSize,
				struct.getResults().getDispMin()[SelectedVar], struct.getResults().getDispMax()[SelectedVar], 1, DP);
			}
			if (ShowStressContour & MenuFunctions.struct.getMesh().getNodes() != null & MenuFunctions.struct.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, "Red to green", "Campo de tensoes (MPa)", LegendSize,
				struct.getResults().getStressMin()[SelectedVar], struct.getResults().getStressMax()[SelectedVar], 1000, DP);
			}
			if (ShowStrainContour & MenuFunctions.struct.getMesh().getNodes() != null & MenuFunctions.struct.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, "Red to green", "Campo de deformacoes", LegendSize,
				struct.getResults().getStrainMin()[SelectedVar], struct.getResults().getStrainMax()[SelectedVar], 1, DP);
			}
			if (ShowInternalForces & MenuFunctions.struct.getMesh().getNodes() != null & MenuFunctions.struct.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, "Red to green", "Forcas internas (kN ou kNm)", LegendSize,
				struct.getResults().getInternalForcesMin()[SelectedVar], struct.getResults().getInternalForcesMax()[SelectedVar], 1, DP);
			}
		}
	}

	public static void DrawLegend(int[] Pos, String ColorSystem, String Title, int[] Size, double MinValue, double MaxValue, double unitfactor, DrawingOnAPanel DP)
	{
		double sx, sy;
		int BarLength;
		int TitleSize = Math.max(Size[0], Size[1]) / 16;
		int FontSize = Math.max(Size[0], Size[1]) / 18;
		int NumCat = 10, NumLines, NumColumns;
		NumLines = 2;
		NumColumns = (1 + NumCat) / NumLines;
		BarLength = (Size[0] / NumColumns)/2;
		sx = BarLength;
		sy = Size[1] / (double)(NumLines);
		DP.DrawText(new int[] {Pos[0] + Size[0] / 2, (int) (Pos[1] - 1.3 * FontSize)}, Title, "Center", 0, "Bold", TitleSize, Color.magenta);
		DP.DrawWindow(Pos, Size[0], Size[1], 1, Color.white, Color.blue);
		for (int i = 0; i <= NumCat - 1; i += 1)
		{
			double value = (MaxValue - MinValue)*i/(NumCat - 1) + MinValue;
			Color color = Util.FindColor(value, MinValue, MaxValue, ColorSystem);
			int[] InitPos = new int[] {(int) (Pos[0] + 2*(i % NumColumns)*sx + sx/2), (int) (Pos[1] + (i / NumColumns) * sy + sy / 4)};
			DP.DrawLine(InitPos, new int[] {InitPos[0] + BarLength, InitPos[1]}, 2, color);
			DP.DrawText(new int[] {InitPos[0] + BarLength/2, (int) (InitPos[1] + FontSize / 2 + FontSize / 4)}, String.valueOf(Util.Round(value / unitfactor, 2)), "Center", 0, "Plain", FontSize, color);
		}
	}
	
	public static void DrawOnLDPanel(Dimension jpLDSize, DrawingOnAPanel DP)
	{
		if (AnalysisIsComplete)
		{
			if (SelectedNodes != null)
			{
				int nodeid = SelectedNodes[0];
				if (MenuFunctions.struct.getMesh().getNodes().get(nodeid) != null)
				{
					Dimension LDPanelSize = jpLDSize;
					int[] CurvePos = new int[] {(int) (0.38 * LDPanelSize.getWidth()), (int) (0.8 * LDPanelSize.getHeight())};
					int[] CurveSize = new int[] {(int) (0.8 * LDPanelSize.getWidth()), (int) (0.6 * LDPanelSize.getHeight())};
					if (1 < SelectedNodes.length)
					{
						double[] Xaxisvalues = new double[SelectedNodes.length], Yaxisvalues = new double[SelectedNodes.length];
						int dir = -1;
						int dof = SelectedVar;
						
						Point3D FirstNodePos = MenuFunctions.struct.getMesh().getNodes().get(SelectedNodes[0]).getOriginalCoords();
						Point3D FinalNodePos = MenuFunctions.struct.getMesh().getNodes().get(SelectedNodes[SelectedNodes.length - 1]).getOriginalCoords();
						if (FinalNodePos.y - FirstNodePos.y <= FinalNodePos.x - FirstNodePos.x)
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
							double minCoord = dir == 0 ? Structure.calcMinCoords(struct.getCoords()).x : Structure.calcMinCoords(struct.getCoords()).y ;
							if (dir == 0)
							{
								Xaxisvalues[node] = MenuFunctions.struct.getMesh().getNodes().get(nodeID).getOriginalCoords().x - minCoord;
							}
							else
							{
								Xaxisvalues[node] = MenuFunctions.struct.getMesh().getNodes().get(nodeID).getOriginalCoords().y - minCoord;
							}
						}
						if (SelectedDiagram == 0)
						{
							for (int node = 0; node <= SelectedNodes.length - 1; node += 1)
							{
								int nodeID = SelectedNodes[node];
								Yaxisvalues[node] = MenuFunctions.struct.getMesh().getNodes().get(nodeID).getDisp()[dof];
							}
						}
						else if (SelectedDiagram == 1)
						{
							for (int node = 0; node <= SelectedNodes.length - 1; node += 1)
							{
								int nodeID = SelectedNodes[node];
								int elemID = -1;
								for (int i = 0; i <= MenuFunctions.struct.getMesh().getElements().size() - 1; i += 1)
								{
									if (Util.ArrayContains(MenuFunctions.struct.getMesh().getElements().get(i).getExternalNodes(), nodeID))
									{
										elemID = i;
									}
								}
								Yaxisvalues[node] = MenuFunctions.struct.getMesh().getElements().get(elemID).getStress()[dof];
							}
						}
						else if (SelectedDiagram == 2)
						{
							for (int node = 0; node <= SelectedNodes.length - 1; node += 1)
							{
								int nodeID = SelectedNodes[node];
								int elemID = -1;
								for (int i = 0; i <= MenuFunctions.struct.getMesh().getElements().size() - 1; i += 1)
								{
									if (Util.ArrayContains(MenuFunctions.struct.getMesh().getElements().get(i).getExternalNodes(), nodeID))
									{
										elemID = i;
									}
								}
								Yaxisvalues[node] = MenuFunctions.struct.getMesh().getElements().get(elemID).getStrain()[dof];
							}
						}
						else if (SelectedDiagram == 3)
						{
							for (int node = 0; node <= SelectedNodes.length - 1; node += 1)
							{
								int nodeID = SelectedNodes[node];
								int elemID = -1;
								for (int i = 0; i <= MenuFunctions.struct.getMesh().getElements().size() - 1; i += 1)
								{
									if (Util.ArrayContains(MenuFunctions.struct.getMesh().getElements().get(i).getExternalNodes(), nodeID))
									{
										elemID = i;
									}
								}
								Yaxisvalues[node] = MenuFunctions.struct.getMesh().getElements().get(elemID).getIntForces()[dof];
							}
						}
						DP.Draw2DPlot(CurvePos, Math.min(CurveSize[0], CurveSize[1]), "Resultados na seââo", "x var", "y var",
						Xaxisvalues, Yaxisvalues, Util.FindMin(Xaxisvalues), Util.FindMin(Yaxisvalues),
						Util.FindMaxAbs(Xaxisvalues), Util.FindMaxAbs(Yaxisvalues), 2, 2, Menus.palette[5], Menus.palette[10]);
					}
					else if (-1 < SelectedNodes[0])
					{
						if (-1 < SelectedVar)
						{						
							double[] XValues = MenuFunctions.struct.getMesh().getNodes().get(nodeid).LoadDisp[SelectedVar][0];
							double[] YValues = MenuFunctions.struct.getMesh().getNodes().get(nodeid).LoadDisp[SelectedVar][1];
							DP.Draw2DPlot(CurvePos, Math.min(CurveSize[0], CurveSize[1]),
							"Curva carga-deslocamento", "u (mm)", "Fator de carga", XValues, YValues,
							Util.FindMin(XValues), Util.FindMin(YValues), Util.FindMaxAbs(XValues), Util.FindMaxAbs(YValues), 3, 3, Menus.palette[5], Menus.palette[10]);					
						}
					}
				}
			}
		}						
	}
		
	/* Mouse functions */
	public static void StructureCreation(int[] MainPanelPos, MyCanvas MainCanvas)
	{
		List<Point3D> StructCoords = struct.getCoords();
		   
		if (Util.MouseIsInside(mousePos, new int[2], MainCanvas.getPos(), MainCanvas.getSize()[0], MainCanvas.getSize()[1]))
	    {
			if (struct.getShape().equals(StructureShape.rectangular) | struct.getShape().equals(StructureShape.circular))
			{
				if (StructCoords != null)
				{
    				StructureCreationIsOn = false;
				}
				Point3D newCoord = getCoordFromMouseClick(MainCanvas, mousePos, SnipToGridIsOn) ;
				struct.addCoordFromMouseClick(newCoord) ;
				System.out.println("Mouse pos: " + mousePos);
				System.out.println("New coord: " + newCoord);
				System.out.println(struct);
			}
			else if (struct.getShape().equals(StructureShape.polygonal))
			{
				int prec = 10;
				if (StructCoords != null)
				{
					if (Util.dist(new double[] {mousePos.x, mousePos.y}, new double[] {StructCoords.get(0).x, StructCoords.get(0).y}) < prec)
					{
	    				StructureCreationIsOn = false;
					}
				}
				Point3D newCoord = getCoordFromMouseClick(MainCanvas, mousePos, SnipToGridIsOn) ;
				struct.addCoordFromMouseClick(newCoord) ;
			}
			else
			{
				System.out.println("Structure shape not identified at Menus -> CreateStructure");
			}
	    }

		// struct.setCoords(StructCoords);
		if (!StructureCreationIsOn)
		{
			// for (int c = 0; c <= StructCoords.size() - 1; c += 1)
			// {
			// 	double[] realCoords = Util.ConvertToRealCoords(new int[] {(int) StructCoords.get(c).x, (int) StructCoords.get(c).y, 0}, new int[] {MainCanvas.getPos().x, MainCanvas.getPos().y}, MainCanvas.getSize(), MainCanvas.getDimension()) ;
			// 	StructCoords.set(c, new Point3D(realCoords[0], realCoords[1], realCoords[2])) ;
			// }
			// if (struct.getShape().equals(StructureShape.polygonal))
			// {
			// 	StructCoords.set(StructCoords.size() - 1, StructCoords.get(0)) ;
			// }
			struct.updateCenter() ;
			MainCanvas.setCenter(Util.ConvertToDrawingCoords(struct.getCenter().asArray(), MainCanvas.getPos(), MainCanvas.getSize(), MainCanvas.getDimension(), MainCanvas.getDrawingPos()));
		}
	}

	
	public static void ElemAddition(MyCanvas MainCanvas, int[] MainPanelPos)
	{
		if (MenuFunctions.struct.getMesh().getElements() != null)
		{
			SelectedElems = null;
			SelectedElems = Mesh.ElemsSelection(MainCanvas, struct.getCenter().asArray(), MenuFunctions.struct.getMesh(), mousePos, MainPanelPos, SelectedElems, ElemSelectionWindowInitialPos, DiagramScales, ShowElemSelectionWindow, ShowDeformedStructure);
			int ElemMouseIsOn = Mesh.ElemMouseIsOn(MenuFunctions.struct.getMesh(), mousePos, struct.getCenter().asArray(), MainCanvas.getPos(), MainCanvas.getSize(), MainCanvas.getDimension(), MainCanvas.getCenter(), MainCanvas.getDrawingPos(), ShowDeformedStructure);
			if (ElemMouseIsOn == -1 | (ShowElemSelectionWindow & -1 < ElemMouseIsOn))
			{
				ShowElemSelectionWindow = !ShowElemSelectionWindow;
				ElemSelectionWindowInitialPos = mousePos;
			}
		}
	}
	
	public static void MouseWheel(int[] MainPanelPos, MyCanvas MainCanvas, int WheelRot, boolean[] AssignmentIsOn)
	{
		boolean MouseIsInMainCanvas = Util.MouseIsInside(mousePos, MainPanelPos, MainCanvas.getPos(), MainCanvas.getSize()[0], MainCanvas.getSize()[1]);
		if (ShowCanvas & Util.MouseIsInside(mousePos, MainPanelPos, MainCanvas.getPos(), MainCanvas.getSize()[0], MainCanvas.getSize()[1]))
		{
			MainCanvas.getDimension()[0] += Util.Round(0.2*Math.log10(MainCanvas.getDimension()[0])*WheelRot, 1);
			MainCanvas.getDimension()[1] += Util.Round(0.2*Math.log10(MainCanvas.getDimension()[1])*WheelRot, 1);
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
		if (AnalysisIsComplete & !Util.MouseIsInsideCanvas(mousePos, MainCanvas))
		{
			DiagramScales[1] += -Util.Round(10 * Math.log10(DiagramScales[1]) * WheelRot, 10);
			DiagramScales[1] = Math.max(DiagramScales[1], 0.001);
		}
	}

	public static void ResetStructure()
	{
		struct = new Structure(null, null, null);
		MenuFunctions.struct.getMesh().reset() ;
		matTypes = null;
		secTypes = null;
		MenuFunctions.struct.removeSupports() ;
		ConcLoadType = null ;
		ConcLoad = null;
		DistLoadType = null ;
		DistLoad = null;
		NodalDispType = null ;
		NodalDisp = null;
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
