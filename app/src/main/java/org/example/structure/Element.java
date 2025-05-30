package org.example.structure;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.Main;
import org.example.loading.DistLoad;
import org.example.mainTCC.MainPanel;
import org.example.output.ResultDiagrams;
import org.example.userInterface.Draw;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.SelectionWindow;

import graphics.Align;
import graphics.DrawPrimitives;

public class Element
{
	private int id;
	private ElemType type;
	private ElemShape Shape;
		
	private int[] DOFs;				// All DOFs present in the element
	private int[][] DOFsPerNode;	// DOFs in each node of the element
	
	
	private List<Node> externalNodes;	// Nodes on the contour (along the edges) in the counter-clockwise direction
	private List<Node> internalNodes;	// Nodes inside the element
	private Material mat ;
	private Section sec;
	private List<DistLoad> distLoads;	// Distributed loads in the node
	
	
	private int[] StrainTypes;		// All strain types present in the element
	private double[] Disp;			// Displacements on the element
	private double[] Stress;		// Stresses on the element
	private double[] Strain;		// Strains on the element
	private double[] IntForces;		// External forces on the element
	
	public static int stroke = 1;
	public static Color color = Main.palette[10];
	public static Color[] matColors;
	public static Color[] SecColors;
	
	private double[][] UndeformedCoords;
	private double[][] DeformedCoords;
	private double[] CenterCoords;
	private int[][] NodeDOF = null;
	private int[] CumDOFs = null;

	private boolean isSelected ;
	private static int currentId = 1 ;
	// private Color MatColor;
	// private Color SecColor;

	public Element(List<Node> ExternalNodes, List<Node> InternalNodes, Material mat, Section sec, ElemType type)
	{
		this.id = currentId;
		this.externalNodes = ExternalNodes;
		this.internalNodes = InternalNodes;
		this.mat = mat;
		this.sec = sec;
		distLoads = new ArrayList<>() ;
		StrainTypes = null;
		Disp = null;
		Stress = null;
		Strain = null;
		IntForces = null;
		this.type = type;
		this.isSelected = false ;
		defineProperties(type);
		currentId += 1;
	}

	public Element(List<Node> ExternalNodes, List<Node> InternalNodes, ElemType type)
	{
		this(ExternalNodes, InternalNodes, null, null, type);
	}

	public Element(List<Node> ExternalNodes, ElemType type)
	{
		this(ExternalNodes, null, type);
	}

	public Element(List<Node> nodes, ElementDTO dto)
	{
		this.id = dto.getID() ;
		this.type = dto.getType() ;
		this.Shape = dto.getShape() ;
		this.mat = dto.getMat() ;
		this.sec = dto.getSec() ;
		this.distLoads = dto.getDistLoads() ;

		this.externalNodes = dto.getExternalNodes() ;
		for (int i = 0 ; i <= externalNodes.size() - 1 ; i += 1)
		{
			Node extNode = externalNodes.get(i) ;
			externalNodes.set(i, nodes.stream().filter(node -> node.getID() == extNode.getID()).findFirst().get()) ;
		}
		defineProperties(type) ;
	}


	public void defineProperties(ElemType type)
	{
		int nodesPerElem ;
		switch (type)
		{
			case KR1, MR1, MR2:
				Shape = ElemShape.rectangular;
				nodesPerElem = 4;
				DOFs = new int[] {2, 3, 4};					// uz, tetax, tetay
				StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
				DOFsPerNode = new int[nodesPerElem][];
				Arrays.fill(DOFsPerNode, DOFs);
				break ;
			
			case KR2:
				Shape = ElemShape.rectangular;
				nodesPerElem = 4;
				DOFs = new int[] {2, 3, 4, 6};				// uz, tetax, tetay, cd (cross derivative)
				StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
				DOFsPerNode = new int[nodesPerElem][];
				Arrays.fill(DOFsPerNode, DOFs);
				break;
				
			case R4:
				Shape = ElemShape.rectangular;
				nodesPerElem = 4;
				DOFs = new int[] {0, 1};					// ux, uy
				StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
				DOFsPerNode = new int[nodesPerElem][];
				Arrays.fill(DOFsPerNode, DOFs);
				break;
				
			case Q4:
				Shape = ElemShape.quad;
				nodesPerElem = 4;
				DOFs = new int[] {0, 1};					// ux, uy
				StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
				DOFsPerNode = new int[nodesPerElem][];
				Arrays.fill(DOFsPerNode, DOFs);
				break;
				
			case T3G:
				Shape = ElemShape.triangular;
				nodesPerElem = 3;
				DOFs = new int[] {0, 1};					// ux, uy
				StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
				DOFsPerNode = new int[nodesPerElem][];
				Arrays.fill(DOFsPerNode, DOFs);
				break;
				
			case T6G:
				Shape = ElemShape.triangular;
				nodesPerElem = 6;
				DOFs = new int[] {0, 1};					// ux, uy
				StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
				DOFsPerNode = new int[nodesPerElem][];
				Arrays.fill(DOFsPerNode, DOFs);
				break;
				
			case SM:
				Shape = ElemShape.rectangular;
				nodesPerElem = 4;
				DOFs = new int[] {2, 3, 4, 7, 8};			// uz, tetax, tetay, phix, phiy
				StrainTypes = new int[] {0, 1, 3, 4, 5};	// ex, ey, gxy, gxz, gyz
				DOFsPerNode = new int[nodesPerElem][];
				Arrays.fill(DOFsPerNode, DOFs);
				break;
				
			case SM8:
				Shape = ElemShape.r8;
				DOFs = new int[] {2, 3, 4, 7, 8};			// uz, tetax, tetay, phix, phiy
				StrainTypes = new int[] {0, 1, 3, 4, 5};	// ex, ey, gxy, gxz, gyz
				//DOFsPerNode = new int[][] {{2, 3, 4, 7, 8}, {7, 8}, {2, 3, 4, 7, 8}, {7, 8}, {2, 3, 4, 7, 8}, {7, 8}, {2, 3, 4, 7, 8}, {7, 8}};
				DOFsPerNode = new int[][] {{2, 3, 4, 7, 8}, {2, 3, 4, 7, 8}, {2, 3, 4, 7, 8}, {2, 3, 4, 7, 8}, {7, 8}, {7, 8}, {7, 8}, {7, 8}};
				break;
				
			case KP3:
				Shape = ElemShape.rectangular;
				nodesPerElem = 4;
				DOFs = new int[] {2, 3, 4};					// uz, tetax, tetay
				StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
				DOFsPerNode = new int[nodesPerElem][];
				Arrays.fill(DOFsPerNode, DOFs);
				break;
				
			case SM_C:
				Shape = ElemShape.rectangular;
				nodesPerElem = 4;
				DOFs = new int[] {2, 3, 4, 6, 7, 8};		// uz, tetax, tetay, cd (cross derivative), phix, phiy
				StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
				DOFsPerNode = new int[nodesPerElem][];
				Arrays.fill(DOFsPerNode, DOFs);
				break;
				
			case SM_H:
				Shape = ElemShape.rectangular;
				nodesPerElem = 4;
				DOFs = new int[] {2, 3, 4, 7, 8};			// uz, tetax, tetay, phix, phiy
				StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
				DOFsPerNode = new int[nodesPerElem][];
				Arrays.fill(DOFsPerNode, DOFs);
				break;
				
		}
		
		Disp = new double[DOFs.length];
		Strain = new double[StrainTypes.length];
		Stress = new double[StrainTypes.length];
		IntForces = new double[StrainTypes.length];
	}

	public int[] cumDOFs()
	{
		int[] CumDOFsOnElem = new int[externalNodes.size()];
        for (int elemnode = 1; elemnode <= externalNodes.size() - 1; elemnode += 1)
        {
        	CumDOFsOnElem[elemnode] = CumDOFsOnElem[elemnode - 1] + externalNodes.get(elemnode - 1).getDOFType().length;
        }
        
        return CumDOFsOnElem;
	}

	public double[] calcHalfSize(List<Node> nodes)
	{
		double[] size = new double[2];
		if (Shape.equals(ElemShape.rectangular))
		{
			size[0] = Math.abs(externalNodes.get(2).getOriginalCoords().x - externalNodes.get(0).getOriginalCoords().x) / 2.0 ;
			size[1] = Math.abs(externalNodes.get(2).getOriginalCoords().y - externalNodes.get(0).getOriginalCoords().y) / 2.0 ;
			// size[0] = Math.abs(nodes.get(externalNodes[2]).getOriginalCoords().x - nodes.get(externalNodes[0]).getOriginalCoords().x) / 2;
			// size[1] = Math.abs(nodes.get(externalNodes[2]).getOriginalCoords().y - nodes.get(externalNodes[0]).getOriginalCoords().y) / 2;
		}
		else if (Shape.equals(ElemShape.r8) | Shape.equals(ElemShape.r9))
		{
			size[0] = Math.abs(externalNodes.get(4).getOriginalCoords().x - externalNodes.get(0).getOriginalCoords().x) / 2.0 ;
			size[1] = Math.abs(externalNodes.get(4).getOriginalCoords().y - externalNodes.get(0).getOriginalCoords().y) / 2.0 ;
			// size[0] = Math.abs(nodes.get(externalNodes[4]).getOriginalCoords().x - nodes.get(externalNodes[0]).getOriginalCoords().x) / 2;
			// size[1] = Math.abs(nodes.get(externalNodes[4]).getOriginalCoords().y - nodes.get(externalNodes[0]).getOriginalCoords().y) / 2;
		}
		return size;
	}

	public static int shapeToNumberNodes(ElemShape shape, ElemType type)
	{
		switch (shape)
		{
			case rectangular: return 4 ;
			case triangular: return type.equals(ElemType.T3G) ? 3 : 6 ;
			case r8: return 8;
			case quad, r9: return -1 ;
			default: System.out.println("Shape is not valid at Elements -> NumberOfNodes"); return -1;
		}
	}
	
	public static ElemShape typeToShape(ElemType type)
	{
		switch (type)
		{
			case KR1, KR2, MR1, MR2, R4, SM, KP3, SM_C, SM_H: return ElemShape.rectangular;			
			case Q4: return ElemShape.quad;				
			case T3G, T6G: return ElemShape.triangular;
			case SM8: return ElemShape.r8;
			default: return null ;
		}
	}
	
	public void addDistLoad(DistLoad distLoad)
	{
		distLoads.add(distLoad) ;
	}

	public void setUndeformedCoords(List<Node> nodes)
	{
		UndeformedCoords = new double[externalNodes.size()][];
		for (int i = 0 ; i <= externalNodes.size() - 1 ; i += 1)
		{
			UndeformedCoords[i] = externalNodes.get(i).getOriginalCoords().asArray();
		}
		// for (int i = 0; i <= externalNodes.length - 1; i += 1)
		// {
		// 	UndeformedCoords[i] = nodes.get(externalNodes[i]).getOriginalCoords().asArray();
    	// }
	}

	public void setDeformedCoords(List<Node> nodes)
	{
		DeformedCoords = new double[externalNodes.size()][];
		for (int i = 0 ; i <= externalNodes.size() - 1 ; i += 1)
		{
			DeformedCoords[i] = Util.ScaledDefCoords(externalNodes.get(i).getOriginalCoords(), externalNodes.get(i).getDisp(), nodes.get(i).getDOFType(), 1) ;
			 externalNodes.get(i).getOriginalCoords().asArray();
		}
		// for (int node = 0; node <= externalNodes.length - 1; node += 1)
		// {
		// 	DeformedCoords[node] = Util.ScaledDefCoords(nodes.get(externalNodes[node]).getOriginalCoords(), nodes.get(externalNodes[node]).getDisp(), nodes.get(node).getDOFType(), 1);
    	// }
	}
	
	public void updateCenterCoords()
	{
		CenterCoords = new double[3];		
		for (int coord = 0; coord <= CenterCoords.length - 1; coord += 1)
		{
			for (int node = 0; node <= externalNodes.size() - 1; node += 1)
			{					
				CenterCoords[coord] += UndeformedCoords[node][coord] / (double) externalNodes.size();
			}
		}
	}
	
	public static void createRandomMatColors(List<Material> MaterialTypes)
	{
		matColors = Util.RandomColors(MaterialTypes.size());
	}

	public static void createRandomSecColors(List<Section> SectionTypes)
	{
		SecColors = Util.RandomColors(SectionTypes.size());
	}	

	public static void displayTypeDetails(ElemType elemType, MyCanvas canvas, DrawPrimitives DP)
	{
		Point3D RealStructCenter = new Point3D(5, 5, 0);
		double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		ElemShape elemShape = Element.typeToShape(elemType);
		List<Node> nodes = new ArrayList<>();
		Element Elem = null;
		if (elemShape.equals(ElemShape.rectangular))
		{
			nodes.add(new Node(0, new Point3D(1, 1, 0))) ;
			nodes.add(new Node(1, new Point3D(9, 1, 0))) ;
			nodes.add(new Node(2, new Point3D(9, 9, 0))) ;
			nodes.add(new Node(3, new Point3D(1, 9, 0))) ;
			Elem = new Element(nodes, elemType);
		}
		else if (elemShape.equals(ElemShape.quad))
		{
			nodes.add(new Node(0, new Point3D(1, 1, 0))) ;
			nodes.add(new Node(1, new Point3D(9, 3, 0))) ;
			nodes.add(new Node(2, new Point3D(7, 9, 0))) ;
			nodes.add(new Node(3, new Point3D(3, 7, 0))) ;
			Elem = new Element(nodes, elemType);
		}
		else if (elemShape.equals(ElemShape.triangular))
		{
			nodes.add(new Node(0, new Point3D(1, 1, 0))) ;
			nodes.add(new Node(1, new Point3D(9, 5, 0))) ;
			nodes.add(new Node(2, new Point3D(1, 9, 0))) ;
			Elem = new Element(nodes, elemType);
		}
		else if (elemShape.equals(ElemShape.r8))
		{
			nodes.add(new Node(0, new Point3D(1, 1, 0))) ;
			nodes.add(new Node(1, new Point3D(9, 1, 0))) ;
			nodes.add(new Node(2, new Point3D(9, 9, 0))) ;
			nodes.add(new Node(3, new Point3D(1, 9, 0))) ;
			nodes.add(new Node(3, new Point3D(5, 1, 0))) ;
			nodes.add(new Node(3, new Point3D(5, 9, 0))) ;
			nodes.add(new Node(3, new Point3D(1, 5, 0))) ;
			nodes.add(new Node(3, new Point3D(9, 5, 0))) ;
			Elem = new Element(nodes, elemType) ;
			// TODO testar elem r8
			// Elem = new Element(0, new int[] {0, 4, 1, 7, 2, 5, 3, 6}, elemType);
		}
		int[] DrawingStructCenter = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(RealStructCenter.asArray(), Center, canvas.getAngles().asArray()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		int textSize = 16;
		Color textColor = Main.palette[8];
		
		for (int node = 0; node <= nodes.size() - 1; node += 1)
		{
			nodes.get(node).setDOFType(Elem.getDOFsPerNode()[node]);
		}
		Elem.setCumDOFs(Elem.cumDOFs());
		for (int node = 0; node <= nodes.size() - 1; node += 1)
		{
			nodes.get(node).setDofs(new int[Elem.getDOFsPerNode()[node].length]);
			for (int dof = 0; dof <= Elem.getDOFsPerNode()[node].length - 1; dof += 1)
			{
				nodes.get(node).getDOFs()[dof] = Elem.getCumDOFs()[node] + dof;
			}
		}
		DrawNodes3D(nodes, null, Node.color, false, nodes.get(0).getDOFType(), 1, canvas, DP);
		draw3D(new Mesh(nodes, List.of(Elem)), null, false, false, true, false, 1, canvas, DP);
		Draw.DrawDOFNumbers(nodes, Node.color, false, canvas, DP);
		DrawDOFSymbols(nodes, Node.color, false, canvas, DP);
		// DP.DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1] - (int) (1 * 1.5 * textSize)}, elemType.toString(), "Center", 0, "Bold", textSize, textColor);
		// DP.DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1]}, "Graus de liberdade: " + Arrays.toString(Elem.getDOFs()), "Center", 0, "Bold", textSize, textColor);
		// DP.DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1] + (int) (1 * 1.5 * textSize)}, "Deformaçõs: " + Arrays.toString(Elem.getStrainTypes()), "Center", 0, "Bold", textSize, textColor);
	
		Point p1 = new Point(DrawingStructCenter[0], DrawingStructCenter[1] - (int) (1 * 1.5 * textSize)) ;
		Point p2 = new Point(DrawingStructCenter[0], DrawingStructCenter[1]) ;
		Point p3 = new Point(DrawingStructCenter[0], DrawingStructCenter[1] + (int) (1 * 1.5 * textSize)) ;

		DP.drawText(p1, Align.center, elemType.toString(), textColor) ;
		DP.drawText(p2, Align.center, "Graus de liberdade: " + Arrays.toString(Elem.getDOFs()), textColor) ;
		DP.drawText(p3, Align.center, "Deformaçõs: " + Arrays.toString(Elem.getStrainTypes()), textColor) ;
	}
	
	private static void DrawNodes3D(List<Node> Node, List<Node> selectedNodes, Color NodeColor, boolean deformed,
									int[] DOFsPerNode, double Defscale, MyCanvas canvas, DrawPrimitives DP)
	{
		int size = 6;
		double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), MainPanel.getInstance().getCentralPanel().getStructure().getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			int[][] DrawingCoords = new int[Node.size()][3];
			if (deformed)
			{
				double[] DeformedCoords = Util.ScaledDefCoords(Node.get(node).getOriginalCoords(), Node.get(node).getDisp(), DOFsPerNode, Defscale);
				DrawingCoords[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(DeformedCoords, Center, canvas.getAngles().asArray()), MainPanel.getInstance().getCentralPanel().getStructure().getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			}
			else
			{
				DrawingCoords[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(Util.GetNodePos(Node.get(node), deformed), Center, canvas.getAngles().asArray()), MainPanel.getInstance().getCentralPanel().getStructure().getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			}
			Point drawingCoords = new Point(DrawingCoords[node][0], DrawingCoords[node][1]) ;
			DP.drawCircle(drawingCoords, size, NodeColor);
			if (selectedNodes != null)
			{
				for (int i = 0; i <= selectedNodes.size() - 1; i += 1)
				{
					if (node == selectedNodes.get(i).getID())
					{
						DP.drawCircle(drawingCoords, 2*size, Main.palette[4]);
					}
				}
			}
		}
	}

	private static void DrawDOFSymbols(List<Node> Node, Color NodeColor, boolean deformed, MyCanvas canvas, DrawPrimitives DP)
	{
		Color ForceDOFColor = Main.palette[8];
		Color MomentDOFColor = Main.palette[9];
		Color CrossDerivativeDOFColor = Main.palette[11];
		Color ShearRotationDOFColor = Main.palette[11];
		int thickness = 2;
		double arrowsize = 0.5;
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			double[] NodeRealPos = Util.GetNodePos(Node.get(node), deformed);
			for (int dof = 0; dof <= Node.get(node).getDOFs().length - 1; dof += 1)
			{
				// TODO use Mesh.dofAngles(dof)
				if (Node.get(node).getDOFType()[dof] == 0)
				{
					Draw.DrawArrow3Dto(Node.get(node).pos(deformed), new Point3D(0, 0, 0), arrowsize, ForceDOFColor, canvas, DP);
				}
				if (Node.get(node).getDOFType()[dof] == 1)
				{
					Draw.DrawArrow3Dto(Node.get(node).pos(deformed), new Point3D(0, 0, Math.PI / 2), arrowsize, ForceDOFColor, canvas, DP);
				}
				if (Node.get(node).getDOFType()[dof] == 2)
				{
					Draw.DrawArrow3Dto(Node.get(node).pos(deformed), new Point3D(0, Math.PI / 2, 0), arrowsize, ForceDOFColor, canvas, DP);
				}
				if (Node.get(node).getDOFType()[dof] == 3)
				{
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 1.5 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas, DP);
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 1.8 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas, DP);
				}
				if (Node.get(node).getDOFType()[dof] == 4)
				{
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 1.5 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas, DP);
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 1.8 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas, DP);
				}
				if (Node.get(node).getDOFType()[dof] == 5)
				{
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, Math.PI / 2, 0}, 1.5 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas, DP);
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, Math.PI / 2, 0}, 1.8 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas, DP);
				}
				if (Node.get(node).getDOFType()[dof] == 6)
				{
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 4}, 1.5 * arrowsize, 0.3 * arrowsize, CrossDerivativeDOFColor, canvas, DP);
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 4}, 1.8 * arrowsize, 0.3 * arrowsize, CrossDerivativeDOFColor, canvas, DP);
				}
				if (Node.get(node).getDOFType()[dof] == 7)
				{
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 0.7 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor, canvas, DP);
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 1.0 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor, canvas, DP);
				}
				if (Node.get(node).getDOFType()[dof] == 8)
				{
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 0.7 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor, canvas, DP);
					Draw.DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 1.0 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor, canvas, DP);
				}
			}	
		}	
	}

	public void displayNumber(MyCanvas canvas, List<Node> nodes, boolean deformed, DrawPrimitives DP)
	{
		// Point3D nodePos = new Point3D(Util.GetNodePos(node, deformed)[0], Util.GetNodePos(node, deformed)[1], Util.GetNodePos(node, deformed)[2]) ;
		// Point nodeDrawingPos = canvas.inDrawingCoords(node.getOriginalCoords()) ;
		Point centerDrawingPos = canvas.inDrawingCoords(new Point2D.Double(CenterCoords[0], CenterCoords[1])) ;
		DP.drawText(centerDrawingPos, Align.center, String.valueOf(id), Element.color) ;
		// for (int elemnode = 0; elemnode <= externalNodes.length - 1; elemnode += 1)
		// {
		// 	int nodeID = externalNodes[elemnode];
		// 	// DrawingCoords[0] += Util.ConvertToDrawingCoords(Util.GetNodePos(Node.get(node), deformed), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getDrawingPos())[0];
		// 	// DrawingCoords[1] += Util.ConvertToDrawingCoords(Util.GetNodePos(Node.get(node), deformed), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getDrawingPos())[1];
		// }
		// DrawingCoords[0] = DrawingCoords[0] / externalNodes.size();
		// DrawingCoords[1] = DrawingCoords[1] / externalNodes.size();
		// DrawText(DrawingCoords, Integer.toString(elem), "Center", 0, "Bold", FontSize, NodeColor);		
		// DP.drawText(new Point(DrawingCoords[0], DrawingCoords[1]), Align.center, String.valueOf(ID), Element.color) ;
	}

	public void display(MyCanvas canvas, List<Node> nodes, boolean showmatcolor, boolean showseccolor, boolean showcontour, boolean showdeformed, double defScale, DrawPrimitives DP)
	{
		// double[] RealCanvasCenter = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		// int[][] DrawingCoord = new int[Nodes.length][2]; 
		Point2D.Double canvasCenter = canvas.centerInRealCoords() ;
		List<Point> DrawingCoord = new ArrayList<>() ;
		int[] xCoords = new int[externalNodes.size() + 1], yCoords = new int[externalNodes.size() + 1];
		Color color = new Color(0, 100, 55);
		if (mat != null)
		{
			color = Util.AddColor(color, new double[] {0, -50, 100});
		}
		if (sec != null)
		{
			color = Util.AddColor(color, new double[] {0, -50, 100});
		}
		if (showmatcolor && mat != null && mat.getColor() != null)
		{
			color = mat.getColor() ;
		}
		if (showseccolor && sec != null && sec.getColor() != null)
		{
			color = sec.getColor() ;
		}
		if (isSelected)
		{
			color = Main.palette[4] ;
		}
		for (int i = 0; i <= externalNodes.size() - 1; i += 1)
		{
			Node node = externalNodes.get(i) ;
			if (showdeformed)
			{
				double[] DeformedCoords = Util.ScaledDefCoords(node.getOriginalCoords(), node.getDisp(), DOFs, defScale);
				double[] rotatedCoords = Util.RotateCoord(DeformedCoords, new double[] {canvasCenter.x, canvasCenter.y}, canvas.getAngles().asArray()) ;
				Point drawingCoord = canvas.inDrawingCoords(new Point2D.Double(rotatedCoords[0], rotatedCoords[1])) ;
				DrawingCoord.add(drawingCoord) ;
				// DrawingCoord[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(DeformedCoords, RealCanvasCenter, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), RealCanvasCenter, canvas.getDrawingPos());
			}
			else
			{
				double[] OriginalCoords = Util.GetNodePos(node, showdeformed);
				double[] rotatedCoords = Util.RotateCoord(OriginalCoords, new double[] {canvasCenter.x, canvasCenter.y}, canvas.getAngles().asArray()) ;
				Point drawingCoord = canvas.inDrawingCoords(new Point2D.Double(rotatedCoords[0], rotatedCoords[1])) ;
				DrawingCoord.add(drawingCoord) ;
				// DrawingCoord[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(OriginalCoords, RealCanvasCenter, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			}
			xCoords[i] = DrawingCoord.get(i).x;
			yCoords[i] = DrawingCoord.get(i).y;
		}
		xCoords[externalNodes.size()] = DrawingCoord.get(0).x;
		yCoords[externalNodes.size()] = DrawingCoord.get(0).y;
		DP.drawPolygon(xCoords, yCoords, stroke, color);
		if (showcontour)
		{
			DP.drawPolyLine(xCoords, yCoords, stroke, Main.palette[0]);
		}
	}

	public double[][] NaturalCoordsShapeFunctions(double e, double n, List<Node> Node)
    {
		return ShapeFunction.naturalCoordsShapeFunctions(type, calcHalfSize(Node), e, n, Node) ;
    }
	
    public double[][] SecondDerivativesb(double e, double n, List<Node> nodes, Section sec, boolean NonlinearGeo)
    {
    	// Calcula as derivadas segundas das funçõs de forma em um dado ponto (e, n). Unidades 1/mâ e 1/m.
    	double[][] B = null;
		double[] ElemSize = calcHalfSize(nodes);
		double a = ElemSize[0];
		double b = ElemSize[1];
		double t = sec.getT() / 1000.0;
		double z ;
		
		switch (type)
		{
			case KR1:
	    	
	    		B = new double[3][12];
	    		B[0] = new double[] {(-((1-e)*(1-n))/4-((-2*e-1)*(1-n))/4)/(a*a),	((a*e*(1-n))/2+(a*(e-1)*(1-n))/4)/(a*a),	0,	(((1-2*e)*(1-n))/4-((e+1)*(1-n))/4)/(a*a),	((a*(e+1)*(1-n))/4+(a*e*(1-n))/2)/(a*a),	0,	(((1-2*e)*(n+1))/4-((e+1)*(n+1))/4)/(a*a),	((a*(e+1)*(n+1))/4+(a*e*(n+1))/2)/(a*a),	0,	(-((1-e)*(n+1))/4-((-2*e-1)*(n+1))/4)/(a*a),	((a*e*(n+1))/2+(a*(e-1)*(n+1))/4)/(a*a),	0} ;
				B[1] = new double[] {(-((1-e)*(1-n))/4-((1-e)*(-2*n-1))/4)/(b*b),	0,	((b*(1-e)*n)/2+(b*(1-e)*(n-1))/4)/(b*b),	(-((e+1)*(1-n))/4-((e+1)*(-2*n-1))/4)/(b*b),	0,	((b*(e+1)*n)/2+(b*(e+1)*(n-1))/4)/(b*b),	(((e+1)*(1-2*n))/4-((e+1)*(n+1))/4)/(b*b),	0,	((b*(e+1)*(n+1))/4+(b*(e+1)*n)/2)/(b*b),	(((1-e)*(1-2*n))/4-((1-e)*(n+1))/4)/(b*b),	0,	((b*(1-e)*(n+1))/4+(b*(1-e)*n)/2)/(b*b)} ;
				B[2] = new double[] {(2*((-n*n-n-e*e-e+2)/8-((-2*n-1)*(1-n))/8-((-2*e-1)*(1-e))/8))/(a*b),	(2*(-(a*(e*e-1))/8-(a*(e-1)*e)/4))/(a*b),	(2*(-(b*(n*n-1))/8-(b*(n-1)*n)/4))/(a*b),	(2*(-(-n*n-n-e*e+e+2)/8+((-2*n-1)*(1-n))/8-((1-2*e)*(e+1))/8))/(a*b),	(2*(-(a*(e*e-1))/8-(a*e*(e+1))/4))/(a*b),	(2*((b*(n*n-1))/8+(b*(n-1)*n)/4))/(a*b),	(2*((-n*n+n-e*e+e+2)/8+((1-2*n)*(n+1))/8+((1-2*e)*(e+1))/8))/(a*b),	(2*((a*(e*e-1))/8+(a*e*(e+1))/4))/(a*b),	(2*((b*(n*n-1))/8+(b*n*(n+1))/4))/(a*b),	(2*(-(-n*n+n-e*e-e+2)/8-((1-2*n)*(n+1))/8+((-2*e-1)*(1-e))/8))/(a*b),	(2*((a*(e*e-1))/8+(a*(e-1)*e)/4))/(a*b),	(2*(-(b*(n*n-1))/8-(b*n*(n+1))/4))/(a*b)} ;  			
	    	
	
				B[0] = new double[] {(-((1-e)*(1-n))/4-((-2*e-1)*(1-n))/4)/(a*a),	((a*e*(1-n))/2+(a*(e-1)*(1-n))/4)/(a*a),	0,	(((1-2*e)*(1-n))/4-((e+1)*(1-n))/4)/(a*a),	((a*(e+1)*(1-n))/4+(a*e*(1-n))/2)/(a*a),	0,	(((1-2*e)*(n+1))/4-((e+1)*(n+1))/4)/(a*a),	((a*(e+1)*(n+1))/4+(a*e*(n+1))/2)/(a*a),	0,	(-((1-e)*(n+1))/4-((-2*e-1)*(n+1))/4)/(a*a),	((a*e*(n+1))/2+(a*(e-1)*(n+1))/4)/(a*a),	0} ;
				B[1] = new double[] {(-((1-e)*(1-n))/4-((1-e)*(-2*n-1))/4)/(b*b),	0,	((b*(1-e)*n)/2+(b*(1-e)*(n-1))/4)/(b*b),	(-((e+1)*(1-n))/4-((e+1)*(-2*n-1))/4)/(b*b),	0,	((b*(e+1)*n)/2+(b*(e+1)*(n-1))/4)/(b*b),	(((e+1)*(1-2*n))/4-((e+1)*(n+1))/4)/(b*b),	0,	((b*(e+1)*(n+1))/4+(b*(e+1)*n)/2)/(b*b),	(((1-e)*(1-2*n))/4-((1-e)*(n+1))/4)/(b*b),	0,	((b*(1-e)*(n+1))/4+(b*(1-e)*n)/2)/(b*b)} ;
				B[2] = new double[] {(2*((-n*n-n-e*e-e+2)/8-((-2*n-1)*(1-n))/8-((-2*e-1)*(1-e))/8))/(a*b),	(2*(-(a*(e*e-1))/8-(a*(e-1)*e)/4))/(a*b),	(2*(-(b*(n*n-1))/8-(b*(n-1)*n)/4))/(a*b),	(2*(-(-n*n-n-e*e+e+2)/8+((-2*n-1)*(1-n))/8-((1-2*e)*(e+1))/8))/(a*b),	(2*(-(a*(e*e-1))/8-(a*e*(e+1))/4))/(a*b),	(2*((b*(n*n-1))/8+(b*(n-1)*n)/4))/(a*b),	(2*((-n*n+n-e*e+e+2)/8+((1-2*n)*(n+1))/8+((1-2*e)*(e+1))/8))/(a*b),	(2*((a*(e*e-1))/8+(a*e*(e+1))/4))/(a*b),	(2*((b*(n*n-1))/8+(b*n*(n+1))/4))/(a*b),	(2*(-(-n*n+n-e*e-e+2)/8-((1-2*n)*(n+1))/8+((-2*e-1)*(1-e))/8))/(a*b),	(2*((a*(e*e-1))/8+(a*(e-1)*e)/4))/(a*b),	(2*(-(b*(n*n-1))/8-(b*n*(n+1))/4))/(a*b)} ;
					
				break;
				
			case KR2:
	    		
	    		B = new double[3][16];
	    		B[0] = new double[] {(3*e*(n*n*n-3*n+2))/(8*a*a),	((6*e-2)*(n*n*n-3*n+2))/(16*a*a),	(3*e*(n*n*n-n*n-n+1))/(8*a*a),	((6*e-2)*(n*n*n-n*n-n+1))/(16*a*a),	-(3*e*(n*n*n-3*n+2))/(8*a*a),	((6*e+2)*(n*n*n-3*n+2))/(16*a*a),	-(3*e*(n*n*n-n*n-n+1))/(8*a*a),	((6*e+2)*(n*n*n-n*n-n+1))/(16*a*a),	-(3*e*(-n*n*n+3*n+2))/(8*a*a),	((6*e+2)*(-n*n*n+3*n+2))/(16*a*a),	-(3*e*(n*n*n+n*n-n-1))/(8*a*a),	((6*e+2)*(n*n*n+n*n-n-1))/(16*a*a),	(3*e*(-n*n*n+3*n+2))/(8*a*a),	((6*e-2)*(-n*n*n+3*n+2))/(16*a*a),	(3*e*(n*n*n+n*n-n-1))/(8*a*a),	((6*e-2)*(n*n*n+n*n-n-1))/(16*a*a)} ;
				B[1] = new double[] {(3*(e*e*e-3*e+2)*n)/(8*b*b),	(3*(e*e*e-e*e-e+1)*n)/(8*b*b),	((e*e*e-3*e+2)*(6*n-2))/(16*b*b),	((e*e*e-e*e-e+1)*(6*n-2))/(16*b*b),	(3*(-e*e*e+3*e+2)*n)/(8*b*b),	(3*(e*e*e+e*e-e-1)*n)/(8*b*b),	((-e*e*e+3*e+2)*(6*n-2))/(16*b*b),	((e*e*e+e*e-e-1)*(6*n-2))/(16*b*b),	-(3*(-e*e*e+3*e+2)*n)/(8*b*b),	-(3*(e*e*e+e*e-e-1)*n)/(8*b*b),	((-e*e*e+3*e+2)*(6*n+2))/(16*b*b),	((e*e*e+e*e-e-1)*(6*n+2))/(16*b*b),	-(3*(e*e*e-3*e+2)*n)/(8*b*b),	-(3*(e*e*e-e*e-e+1)*n)/(8*b*b),	((e*e*e-3*e+2)*(6*n+2))/(16*b*b),	((e*e*e-e*e-e+1)*(6*n+2))/(16*b*b)} ;
				B[2] = new double[] {((3*e*e-3)*(3*n*n-3))/(8*a*b),	((3*e*e-2*e-1)*(3*n*n-3))/(8*a*b),	((3*e*e-3)*(3*n*n-2*n-1))/(8*a*b),	((3*e*e-2*e-1)*(3*n*n-2*n-1))/(8*a*b),	((3-3*e*e)*(3*n*n-3))/(8*a*b),	((3*e*e+2*e-1)*(3*n*n-3))/(8*a*b),	((3-3*e*e)*(3*n*n-2*n-1))/(8*a*b),	((3*e*e+2*e-1)*(3*n*n-2*n-1))/(8*a*b),	((3-3*e*e)*(3-3*n*n))/(8*a*b),	((3*e*e+2*e-1)*(3-3*n*n))/(8*a*b),	((3-3*e*e)*(3*n*n+2*n-1))/(8*a*b),	((3*e*e+2*e-1)*(3*n*n+2*n-1))/(8*a*b),	((3*e*e-3)*(3-3*n*n))/(8*a*b),	((3*e*e-2*e-1)*(3-3*n*n))/(8*a*b),	((3*e*e-3)*(3*n*n+2*n-1))/(8*a*b),	((3*e*e-2*e-1)*(3*n*n+2*n-1))/(8*a*b)} ; 			
	    	
				break;
				
			case MR1, MR2:

	    		B = new double[3][12];
	    		B[0] = new double[] {0, -(1 - n) / (4 * a), 0, 0, (1 - n) / (4 * a), 0, 0, (1 + n) / (4 * a), 0, 0, -(1 + n) / (4 * a), 0} ;
	    		B[1] = new double[] {0, -(1 - e) / (4 * b), 0, 0, -(1 + e) / (4 * b), 0, 0, (1 + e) / (4 * b), 0, 0, (1 - e) / (4 * b), 0} ;
	    		B[2] = new double[] {-(1 - e) / (4 * b), -(1 - n) / (4 * a), 0, -(1 + e) / (4 * b), (1 - n) / (4 * a), 0, (1 + e) / (4 * b), (1 + n) / (4 * a), 0, (1 - e) / (4 * b), -(1 + n) / (4 * a), 0} ;
	    	
	    		break;
	    		
			case R4, Q4:
		    	
	    		B = new double[3][8];
	    		B[0] = new double[] {0, -(1 - n) / (4 * a), 0, (1 - n) / (4 * a), 0, (1 + n) / (4 * a), 0, -(1 + n) / (4 * a)} ;
	    		B[1] = new double[] {-(1 - e) / (4 * b), 0, -(1 + e) / (4 * b), 0, (1 + e) / (4 * b), 0, (1 - e) / (4 * b), 0} ;
	    		B[2] = new double[] {-(1 - e) / (4 * b), -(1 - n) / (4 * a), -(1 + e) / (4 * b), (1 - n) / (4 * a), (1 + e) / (4 * b), (1 + n) / (4 * a), (1 - e) / (4 * b), -(1 + n) / (4 * a)} ;
	    	
	    		break;
	    		
			case T3G:
		    	
	    		// int[] Nodes = externalNodes;
	    		double[][] Coords = new double[externalNodes.size()][];
	    		for (int i = 0; i <= externalNodes.size() - 1; i += 1)
	    		{
					Node node = externalNodes.get(i) ;
	    			Coords[i] = new double[3];
	    			if (NonlinearGeo)
					{
						Coords[i][0] = node.getOriginalCoords().x + node.getDisp().x;
						Coords[i][1] = node.getOriginalCoords().y + node.getDisp().y;
						Coords[i][2] = node.getOriginalCoords().z + node.getDisp().z;
					}
					else
					{
						Coords[i][0] = node.getOriginalCoords().x;
						Coords[i][1] = node.getOriginalCoords().y;
						Coords[i][2] = node.getOriginalCoords().z;
					}
	    		}
	    		
	    	    double x1 = Coords[0][0], x2 = Coords[1][0], x3 = Coords[2][0];
	    	    double y1 = Coords[0][1], y2 = Coords[1][1], y3 = Coords[2][1];
	    	    double A = ResultDiagrams.TriArea(Coords);
	    	    double[] beta = new double[] {y2 - y3, y3 - y1, y1 - y2};
	    	    double[] gama = new double[] {x3 - x2, x1 - x3, x2 - x1};
	    	    B = new double[][] {{beta[0], 0, beta[1], 0, beta[2], 0}, {0, gama[0], 0, gama[1], 0, gama[2]}, {gama[0], beta[0], gama[1], beta[1], gama[2], beta[2]}};
	    	    for (int i = 0; i <= B.length - 1; i += 1)
	    	    {
	    	        for (int j = 0; j <= B[i].length - 1; j += 1)
	        	    {
	        	        B[i][j] = B[i][j]/(2*A);
	        	    }
	    	    }
	    	    
	    	    break;
	    	    
			case T6G: break;
			
			case SM:

	    		z = t / 2;	// Strains vary with the thickness, they are maximum at the extremes and the bending ones are 0 at the middle
	    		B = new double[5][8];
	    		B[0] = new double[] {((3*e*n-3*e)*z)/(4*a*a),	(((3*e-1)*n-3*e+1)*z)/(4*a),	0,	-((20*n-20)*z*z*z+(15-15*n)*t*t*z)/(48*a*t*t),	0,	-((3*e*n-3*e)*z)/(4*a*a),	(((3*e+1)*n-3*e-1)*z)/(4*a),	0,	((20*n-20)*z*z*z+(15-15*n)*t*t*z)/(48*a*t*t),	0,	((3*e*n+3*e)*z)/(4*a*a),	-(((3*e+1)*n+3*e+1)*z)/(4*a),	0,	-((20*n+20)*z*z*z+(-15*n-15)*t*t*z)/(48*a*t*t),	0,	-((3*e*n+3*e)*z)/(4*a*a),	-(((3*e-1)*n+3*e-1)*z)/(4*a),	0,	((20*n+20)*z*z*z+(-15*n-15)*t*t*z)/(48*a*t*t),	0} ;
				B[1] = new double[] {((3*e-3)*n*z)/(4*b*b),	0,	(((3*e-3)*n-e+1)*z)/(4*b),	0,	-((20*e-20)*z*z*z+(15-15*e)*t*t*z)/(48*b*t*t),	-((3*e+3)*n*z)/(4*b*b),	0,	-(((3*e+3)*n-e-1)*z)/(4*b),	0,	((20*e+20)*z*z*z+(-15*e-15)*t*t*z)/(48*b*t*t),	((3*e+3)*n*z)/(4*b*b),	0,	-(((3*e+3)*n+e+1)*z)/(4*b),	0,	-((20*e+20)*z*z*z+(-15*e-15)*t*t*z)/(48*b*t*t),	-((3*e-3)*n*z)/(4*b*b),	0,	(((3*e-3)*n+e-1)*z)/(4*b),	0,	((20*e-20)*z*z*z+(15-15*e)*t*t*z)/(48*b*t*t)} ;
				B[2] = new double[] {((3*n*n+3*e*e-4)*z)/(4*a*b),	((3*e*e-2*e-1)*z)/(4*b),	((3*n*n-2*n-1)*z)/(4*a),	-((20*e-20)*z*z*z+(15-15*e)*t*t*z)/(48*b*t*t),	-((20*n-20)*z*z*z+(15-15*n)*t*t*z)/(48*a*t*t),	-((3*n*n+3*e*e-4)*z)/(4*a*b),	((3*e*e+2*e-1)*z)/(4*b),	-((3*n*n-2*n-1)*z)/(4*a),	((20*e+20)*z*z*z+(-15*e-15)*t*t*z)/(48*b*t*t),	((20*n-20)*z*z*z+(15-15*n)*t*t*z)/(48*a*t*t),	((3*n*n+3*e*e-4)*z)/(4*a*b),	-((3*e*e+2*e-1)*z)/(4*b),	-((3*n*n+2*n-1)*z)/(4*a),	-((20*e+20)*z*z*z+(-15*e-15)*t*t*z)/(48*b*t*t),	-((20*n+20)*z*z*z+(-15*n-15)*t*t*z)/(48*a*t*t),	-((3*n*n+3*e*e-4)*z)/(4*a*b),	-((3*e*e-2*e-1)*z)/(4*b),	((3*n*n+2*n-1)*z)/(4*a),	((20*e-20)*z*z*z+(15-15*e)*t*t*z)/(48*b*t*t),	((20*n+20)*z*z*z+(-15*n-15)*t*t*z)/(48*a*t*t)} ;    			
				B[3] = new double[] {0,	0,	0,	-(((20*e-20)*n-20*e+20)*z*z+((5-5*e)*n+5*e-5)*t*t)/(16*t*t),	0,	0,	0,	0,	(((20*e+20)*n-20*e-20)*z*z+((-5*e-5)*n+5*e+5)*t*t)/(16*t*t),	0,	0,	0,	0,	-(((20*e+20)*n+20*e+20)*z*z+((-5*e-5)*n-5*e-5)*t*t)/(16*t*t),	0,	0,	0,	0,	(((20*e-20)*n+20*e-20)*z*z+((5-5*e)*n-5*e+5)*t*t)/(16*t*t),	0} ;
				B[4] = new double[] {0,	0,	0,	0,	-(((20*e-20)*n-20*e+20)*z*z+((5-5*e)*n+5*e-5)*t*t)/(16*t*t),	0,	0,	0,	0,	(((20*e+20)*n-20*e-20)*z*z+((-5*e-5)*n+5*e+5)*t*t)/(16*t*t),	0,	0,	0,	0,	-(((20*e+20)*n+20*e+20)*z*z+((-5*e-5)*n-5*e-5)*t*t)/(16*t*t),	0,	0,	0,	0,	(((20*e-20)*n+20*e-20)*z*z+((5-5*e)*n-5*e+5)*t*t)/(16*t*t)} ;
	    		 
				break;
				
			case SM8:
				
	    		z = t / 2;	// Strains vary with the thickness, they are maximum at the extremes and the bending ones are 0 at the middle
	    		B = new double[5][28];
	    		B[0] = new double[] {((3*e*n-3*e)*z)/(4*a*a),	(((3*e-1)*n-3*e+1)*z)/(4*a),	0,	((20*n*n+(40*e-20)*n-40*e)*z*z*z+(-15*n*n+(15-30*e)*n+30*e)*t*t*z)/(48*a*t*t),	0,	-((20*e*n-20*e)*z*z*z+(15*e-15*e*n)*t*t*z)/(12*a*t*t),	0,	-((3*e*n-3*e)*z)/(4*a*a),	(((3*e+1)*n-3*e-1)*z)/(4*a),	0,	-((20*n*n+(-40*e-20)*n+40*e)*z*z*z+(-15*n*n+(30*e+15)*n-30*e)*t*t*z)/(48*a*t*t),	0,	((20*n*n-20)*z*z*z+(15-15*n*n)*t*t*z)/(24*a*t*t),	0,	((3*e*n+3*e)*z)/(4*a*a),	-(((3*e+1)*n+3*e+1)*z)/(4*a),	0,	-((20*n*n+(40*e+20)*n+40*e)*z*z*z+(-15*n*n+(-30*e-15)*n-30*e)*t*t*z)/(48*a*t*t),	0,	((20*e*n+20*e)*z*z*z+(-15*e*n-15*e)*t*t*z)/(12*a*t*t),	0,	-((3*e*n+3*e)*z)/(4*a*a),	-(((3*e-1)*n+3*e-1)*z)/(4*a),	0,	((20*n*n+(20-40*e)*n-40*e)*z*z*z+(-15*n*n+(30*e-15)*n+30*e)*t*t*z)/(48*a*t*t),	0,	-((20*n*n-20)*z*z*z+(15-15*n*n)*t*t*z)/(24*a*t*t),	0} ;
				B[1] = new double[] {((3*e-3)*n*z)/(4*b*b),	0,	(((3*e-3)*n-e+1)*z)/(4*b),	0,	(((40*e-40)*n+20*e*e-20*e)*z*z*z+((30-30*e)*n-15*e*e+15*e)*t*t*z)/(48*b*t*t),	0,	-((20*e*e-20)*z*z*z+(15-15*e*e)*t*t*z)/(24*b*t*t),	-((3*e+3)*n*z)/(4*b*b),	0,	-(((3*e+3)*n-e-1)*z)/(4*b),	0,	-(((40*e+40)*n-20*e*e-20*e)*z*z*z+((-30*e-30)*n+15*e*e+15*e)*t*t*z)/(48*b*t*t),	0,	((20*e+20)*n*z*z*z+(-15*e-15)*n*t*t*z)/(12*b*t*t),	((3*e+3)*n*z)/(4*b*b),	0,	-(((3*e+3)*n+e+1)*z)/(4*b),	0,	-(((40*e+40)*n+20*e*e+20*e)*z*z*z+((-30*e-30)*n-15*e*e-15*e)*t*t*z)/(48*b*t*t),	0,	((20*e*e-20)*z*z*z+(15-15*e*e)*t*t*z)/(24*b*t*t),	-((3*e-3)*n*z)/(4*b*b),	0,	(((3*e-3)*n+e-1)*z)/(4*b),	0,	(((40*e-40)*n-20*e*e+20*e)*z*z*z+((30-30*e)*n+15*e*e-15*e)*t*t*z)/(48*b*t*t),	0,	-((20*e-20)*n*z*z*z+(15-15*e)*n*t*t*z)/(12*b*t*t)} ;
				B[2] = new double[] {((3*n*n+3*e*e-4)*z)/(4*a*b),	((3*e*e-2*e-1)*z)/(4*b),	((3*n*n-2*n-1)*z)/(4*a),	(((40*e-40)*n+20*e*e-20*e)*z*z*z+((30-30*e)*n-15*e*e+15*e)*t*t*z)/(48*b*t*t),	((20*n*n+(40*e-20)*n-40*e)*z*z*z+(-15*n*n+(15-30*e)*n+30*e)*t*t*z)/(48*a*t*t),	-((20*e*e-20)*z*z*z+(15-15*e*e)*t*t*z)/(24*b*t*t),	-((20*e*n-20*e)*z*z*z+(15*e-15*e*n)*t*t*z)/(12*a*t*t),	-((3*n*n+3*e*e-4)*z)/(4*a*b),	((3*e*e+2*e-1)*z)/(4*b),	-((3*n*n-2*n-1)*z)/(4*a),	-(((40*e+40)*n-20*e*e-20*e)*z*z*z+((-30*e-30)*n+15*e*e+15*e)*t*t*z)/(48*b*t*t),	-((20*n*n+(-40*e-20)*n+40*e)*z*z*z+(-15*n*n+(30*e+15)*n-30*e)*t*t*z)/(48*a*t*t),	((20*e+20)*n*z*z*z+(-15*e-15)*n*t*t*z)/(12*b*t*t),	((20*n*n-20)*z*z*z+(15-15*n*n)*t*t*z)/(24*a*t*t),	((3*n*n+3*e*e-4)*z)/(4*a*b),	-((3*e*e+2*e-1)*z)/(4*b),	-((3*n*n+2*n-1)*z)/(4*a),	-(((40*e+40)*n+20*e*e+20*e)*z*z*z+((-30*e-30)*n-15*e*e-15*e)*t*t*z)/(48*b*t*t),	-((20*n*n+(40*e+20)*n+40*e)*z*z*z+(-15*n*n+(-30*e-15)*n-30*e)*t*t*z)/(48*a*t*t),	((20*e*e-20)*z*z*z+(15-15*e*e)*t*t*z)/(24*b*t*t),	((20*e*n+20*e)*z*z*z+(-15*e*n-15*e)*t*t*z)/(12*a*t*t),	-((3*n*n+3*e*e-4)*z)/(4*a*b),	-((3*e*e-2*e-1)*z)/(4*b),	((3*n*n+2*n-1)*z)/(4*a),	(((40*e-40)*n-20*e*e+20*e)*z*z*z+((30-30*e)*n+15*e*e-15*e)*t*t*z)/(48*b*t*t),	((20*n*n+(20-40*e)*n-40*e)*z*z*z+(-15*n*n+(30*e-15)*n+30*e)*t*t*z)/(48*a*t*t),	-((20*e-20)*n*z*z*z+(15-15*e)*n*t*t*z)/(12*b*t*t),	-((20*n*n-20)*z*z*z+(15-15*n*n)*t*t*z)/(24*a*t*t)} ;
				B[3] = new double[] {0,	0,	0,	(((20*e-20)*n*n+(20*e*e-20*e)*n-20*e*e+20)*z*z+((5-5*e)*n*n+(5*e-5*e*e)*n+5*e*e-5)*t*t)/(16*t*t),	0,	-(((20*e*e-20)*n-20*e*e+20)*z*z+((5-5*e*e)*n+5*e*e-5)*t*t)/(8*t*t),	0,	0,	0,	0,	-(((20*e+20)*n*n+(-20*e*e-20*e)*n+20*e*e-20)*z*z+((-5*e-5)*n*n+(5*e*e+5*e)*n-5*e*e+5)*t*t)/(16*t*t),	0,	(((20*e+20)*n*n-20*e-20)*z*z+((-5*e-5)*n*n+5*e+5)*t*t)/(8*t*t),	0,	0,	0,	0,	-(((20*e+20)*n*n+(20*e*e+20*e)*n+20*e*e-20)*z*z+((-5*e-5)*n*n+(-5*e*e-5*e)*n-5*e*e+5)*t*t)/(16*t*t),	0,	(((20*e*e-20)*n+20*e*e-20)*z*z+((5-5*e*e)*n-5*e*e+5)*t*t)/(8*t*t),	0,	0,	0,	0,	(((20*e-20)*n*n+(20*e-20*e*e)*n-20*e*e+20)*z*z+((5-5*e)*n*n+(5*e*e-5*e)*n+5*e*e-5)*t*t)/(16*t*t),	0,	-(((20*e-20)*n*n-20*e+20)*z*z+((5-5*e)*n*n+5*e-5)*t*t)/(8*t*t),	0} ;
				B[4] = new double[] {0,	0,	0,	0,	(((20*e-20)*n*n+(20*e*e-20*e)*n-20*e*e+20)*z*z+((5-5*e)*n*n+(5*e-5*e*e)*n+5*e*e-5)*t*t)/(16*t*t),	0,	-(((20*e*e-20)*n-20*e*e+20)*z*z+((5-5*e*e)*n+5*e*e-5)*t*t)/(8*t*t),	0,	0,	0,	0,	-(((20*e+20)*n*n+(-20*e*e-20*e)*n+20*e*e-20)*z*z+((-5*e-5)*n*n+(5*e*e+5*e)*n-5*e*e+5)*t*t)/(16*t*t),	0,	(((20*e+20)*n*n-20*e-20)*z*z+((-5*e-5)*n*n+5*e+5)*t*t)/(8*t*t),	0,	0,	0,	0,	-(((20*e+20)*n*n+(20*e*e+20*e)*n+20*e*e-20)*z*z+((-5*e-5)*n*n+(-5*e*e-5*e)*n-5*e*e+5)*t*t)/(16*t*t),	0,	(((20*e*e-20)*n+20*e*e-20)*z*z+((5-5*e*e)*n-5*e*e+5)*t*t)/(8*t*t),	0,	0,	0,	0,	(((20*e-20)*n*n+(20*e-20*e*e)*n-20*e*e+20)*z*z+((5-5*e)*n*n+(5*e*e-5*e)*n+5*e*e-5)*t*t)/(16*t*t),	0,	-(((20*e-20)*n*n-20*e+20)*z*z+((5-5*e)*n*n+5*e-5)*t*t)/(8*t*t)} ;
				
				break;
				
			case KP3:

	    		B = new double[5][12];
	    		B[0] = new double[] {((3*e*e-3)*(n*n*n-3*n+2))/16,	(a*(3*e*e-2*e-1)*(n*n*n-3*n+2))/16,	(b*(3*e*e-3)*(n*n*n-n*n-n+1))/16,	((3-3*e*e)*(n*n*n-3*n+2))/16,	(a*(3*e*e+2*e-1)*(n*n*n-3*n+2))/16,	(b*(3-3*e*e)*(n*n*n-n*n-n+1))/16,	((3-3*e*e)*(-n*n*n+3*n+2))/16,	(a*(3*e*e+2*e-1)*(-n*n*n+3*n+2))/16,	(b*(3-3*e*e)*(n*n*n+n*n-n-1))/16,	((3*e*e-3)*(-n*n*n+3*n+2))/16,	(a*(3*e*e-2*e-1)*(-n*n*n+3*n+2))/16,	(b*(3*e*e-3)*(n*n*n+n*n-n-1))/16} ;
				B[1] = new double[] {((e*e*e-3*e+2)*(3*n*n-3))/16,	(a*(e*e*e-e*e-e+1)*(3*n*n-3))/16,	(b*(e*e*e-3*e+2)*(3*n*n-2*n-1))/16,	((-e*e*e+3*e+2)*(3*n*n-3))/16,	(a*(e*e*e+e*e-e-1)*(3*n*n-3))/16,	(b*(-e*e*e+3*e+2)*(3*n*n-2*n-1))/16,	((-e*e*e+3*e+2)*(3-3*n*n))/16,	(a*(e*e*e+e*e-e-1)*(3-3*n*n))/16,	(b*(-e*e*e+3*e+2)*(3*n*n+2*n-1))/16,	((e*e*e-3*e+2)*(3-3*n*n))/16,	(a*(e*e*e-e*e-e+1)*(3-3*n*n))/16,	(b*(e*e*e-3*e+2)*(3*n*n+2*n-1))/16} ;
				B[2] = new double[] {(3*e*(n*n*n-3*n+2))/8,	(a*(6*e-2)*(n*n*n-3*n+2))/16,	(3*b*e*(n*n*n-n*n-n+1))/8,	-(3*e*(n*n*n-3*n+2))/8,	(a*(6*e+2)*(n*n*n-3*n+2))/16,	-(3*b*e*(n*n*n-n*n-n+1))/8,	-(3*e*(-n*n*n+3*n+2))/8,	(a*(6*e+2)*(-n*n*n+3*n+2))/16,	-(3*b*e*(n*n*n+n*n-n-1))/8,	(3*e*(-n*n*n+3*n+2))/8,	(a*(6*e-2)*(-n*n*n+3*n+2))/16,	(3*b*e*(n*n*n+n*n-n-1))/8} ;
				B[3] = new double[] {(3*(e*e*e-3*e+2)*n)/8,	(3*a*(e*e*e-e*e-e+1)*n)/8,	(b*(e*e*e-3*e+2)*(6*n-2))/16,	(3*(-e*e*e+3*e+2)*n)/8,	(3*a*(e*e*e+e*e-e-1)*n)/8,	(b*(-e*e*e+3*e+2)*(6*n-2))/16,	-(3*(-e*e*e+3*e+2)*n)/8,	-(3*a*(e*e*e+e*e-e-1)*n)/8,	(b*(-e*e*e+3*e+2)*(6*n+2))/16,	-(3*(e*e*e-3*e+2)*n)/8,	-(3*a*(e*e*e-e*e-e+1)*n)/8,	(b*(e*e*e-3*e+2)*(6*n+2))/16} ;
				B[4] = new double[] {((3*e*e-3)*(3*n*n-3))/16,	(a*(3*e*e-2*e-1)*(3*n*n-3))/16,	(b*(3*e*e-3)*(3*n*n-2*n-1))/16,	((3-3*e*e)*(3*n*n-3))/16,	(a*(3*e*e+2*e-1)*(3*n*n-3))/16,	(b*(3-3*e*e)*(3*n*n-2*n-1))/16,	((3-3*e*e)*(3-3*n*n))/16,	(a*(3*e*e+2*e-1)*(3-3*n*n))/16,	(b*(3-3*e*e)*(3*n*n+2*n-1))/16,	((3*e*e-3)*(3-3*n*n))/16,	(a*(3*e*e-2*e-1)*(3-3*n*n))/16,	(b*(3*e*e-3)*(3*n*n+2*n-1))/16} ;
	    		
				break;
				
			case SM_C, SM_H: break;
				
		}
    	
    	return B;
    }    
    
	public double[][] Bb(double e, double n, double w, List<Node> Node, Section sec, boolean NonlinearGeo, int option)
	{
    	double[][] Bb1 = null,  Bb2 = null;
    	double t = sec.getT() / 1000.0;
		double[] ElemSize = calcHalfSize(Node);
		double a = ElemSize[0];
		double b = ElemSize[1];
    	
    	if (option == 1)
    	{
    		Bb1 = new double[3][12];
			Bb1[0] = new double[] {(-((1-e)*(1-n))/4-((-2*e-1)*(1-n))/4)/Math.pow(a, 2),	((a*e*(1-n))/2+(a*(e-1)*(1-n))/4)/Math.pow(a, 2),	0,	-(5*(1-n))/(4*a),	0,	(((1-2*e)*(1-n))/4-((e+1)*(1-n))/4)/Math.pow(a, 2),	((a*(e+1)*(1-n))/4+(a*e*(1-n))/2)/Math.pow(a, 2),	0,	(5*(1-n))/(4*a),	0,	(((1-2*e)*(n+1))/4-((e+1)*(n+1))/4)/Math.pow(a, 2),	((a*(e+1)*(n+1))/4+(a*e*(n+1))/2)/Math.pow(a, 2),	0,	(5*(n+1))/(4*a),	0,	(-((1-e)*(n+1))/4-((-2*e-1)*(n+1))/4)/Math.pow(a, 2),	((a*e*(n+1))/2+(a*(e-1)*(n+1))/4)/Math.pow(a, 2),	0,	-(5*(n+1))/(4*a),	0} ;
			Bb1[1] = new double[] {(-((1-e)*(1-n))/4-((1-e)*(-2*n-1))/4)/Math.pow(b, 2),	0,	((b*(1-e)*n)/2+(b*(1-e)*(n-1))/4)/Math.pow(b, 2),	0,	-(5*(1-e))/(4*b),	(-((e+1)*(1-n))/4-((e+1)*(-2*n-1))/4)/Math.pow(b, 2),	0,	((b*(e+1)*n)/2+(b*(e+1)*(n-1))/4)/Math.pow(b, 2),	0,	-(5*(e+1))/(4*b),	(((e+1)*(1-2*n))/4-((e+1)*(n+1))/4)/Math.pow(b, 2),	0,	((b*(e+1)*(n+1))/4+(b*(e+1)*n)/2)/Math.pow(b, 2),	0,	(5*(e+1))/(4*b),	(((1-e)*(1-2*n))/4-((1-e)*(n+1))/4)/Math.pow(b, 2),	0,	((b*(1-e)*(n+1))/4+(b*(1-e)*n)/2)/Math.pow(b, 2),	0,	(5*(1-e))/(4*b)} ;
			Bb1[2] = new double[] {(2*((-Math.pow(n, 2)-n-Math.pow(e, 2)-e+2)/8-((-2*n-1)*(1-n))/8-((-2*e-1)*(1-e))/8))/(a*b),	(2*(-(a*(Math.pow(e, 2)-1))/8-(a*(e-1)*e)/4))/(a*b),	(2*(-(b*(Math.pow(n, 2)-1))/8-(b*(n-1)*n)/4))/(a*b),	-(5*(1-e))/(4*b),	-(5*(1-n))/(4*a),	(2*(-(-Math.pow(n, 2)-n-Math.pow(e, 2)+e+2)/8+((-2*n-1)*(1-n))/8-((1-2*e)*(e+1))/8))/(a*b),	(2*(-(a*(Math.pow(e, 2)-1))/8-(a*e*(e+1))/4))/(a*b),	(2*((b*(Math.pow(n, 2)-1))/8+(b*(n-1)*n)/4))/(a*b),	-(5*(e+1))/(4*b),	(5*(1-n))/(4*a),	(2*((-Math.pow(n, 2)+n-Math.pow(e, 2)+e+2)/8+((1-2*n)*(n+1))/8+((1-2*e)*(e+1))/8))/(a*b),	(2*((a*(Math.pow(e, 2)-1))/8+(a*e*(e+1))/4))/(a*b),	(2*((b*(Math.pow(n, 2)-1))/8+(b*n*(n+1))/4))/(a*b),	(5*(e+1))/(4*b),	(5*(n+1))/(4*a),	(2*(-(-Math.pow(n, 2)+n-Math.pow(e, 2)-e+2)/8-((1-2*n)*(n+1))/8+((-2*e-1)*(1-e))/8))/(a*b),	(2*((a*(Math.pow(e, 2)-1))/8+(a*(e-1)*e)/4))/(a*b),	(2*(-(b*(Math.pow(n, 2)-1))/8-(b*n*(n+1))/4))/(a*b),	(5*(1-e))/(4*b),	-(5*(n+1))/(4*a)} ;			
			Bb1 = Util.MultMatrix(Bb1, w / 4.0);
			
    		return Bb1 ;
    	}
    	else
    	{	
    		Bb2 = new double[3][12];
			Bb2[0] = new double[] {-(20*(-((1-e)*(1-n))/4-((-2*e-1)*(1-n))/4))/(3*Math.pow(a, 2)*Math.pow(t, 2)),	-(20*((a*e*(1-n))/2+(a*(e-1)*(1-n))/4))/(3*Math.pow(a, 2)*Math.pow(t, 2)),	0,	(5*(1-n))/(3*a*Math.pow(t, 2)),	0,	-(20*(((1-2*e)*(1-n))/4-((e+1)*(1-n))/4))/(3*Math.pow(a, 2)*Math.pow(t, 2)),	-(20*((a*(e+1)*(1-n))/4+(a*e*(1-n))/2))/(3*Math.pow(a, 2)*Math.pow(t, 2)),	0,	-(5*(1-n))/(3*a*Math.pow(t, 2)),	0,	-(20*(((1-2*e)*(n+1))/4-((e+1)*(n+1))/4))/(3*Math.pow(a, 2)*Math.pow(t, 2)),	-(20*((a*(e+1)*(n+1))/4+(a*e*(n+1))/2))/(3*Math.pow(a, 2)*Math.pow(t, 2)),	0,	-(5*(n+1))/(3*a*Math.pow(t, 2)),	0,	-(20*(-((1-e)*(n+1))/4-((-2*e-1)*(n+1))/4))/(3*Math.pow(a, 2)*Math.pow(t, 2)),	-(20*((a*e*(n+1))/2+(a*(e-1)*(n+1))/4))/(3*Math.pow(a, 2)*Math.pow(t, 2)),	0,	(5*(n+1))/(3*a*Math.pow(t, 2)),	0} ;
			Bb2[1] = new double[] {-(20*(-((1-e)*(1-n))/4-((1-e)*(-2*n-1))/4))/(3*Math.pow(b, 2)*Math.pow(t, 2)),	0,	-(20*((b*(1-e)*n)/2+(b*(1-e)*(n-1))/4))/(3*Math.pow(b, 2)*Math.pow(t, 2)),	0,	(5*(1-e))/(3*b*Math.pow(t, 2)),	-(20*(-((e+1)*(1-n))/4-((e+1)*(-2*n-1))/4))/(3*Math.pow(b, 2)*Math.pow(t, 2)),	0,	-(20*((b*(e+1)*n)/2+(b*(e+1)*(n-1))/4))/(3*Math.pow(b, 2)*Math.pow(t, 2)),	0,	(5*(e+1))/(3*b*Math.pow(t, 2)),	-(20*(((e+1)*(1-2*n))/4-((e+1)*(n+1))/4))/(3*Math.pow(b, 2)*Math.pow(t, 2)),	0,	-(20*((b*(e+1)*(n+1))/4+(b*(e+1)*n)/2))/(3*Math.pow(b, 2)*Math.pow(t, 2)),	0,	-(5*(e+1))/(3*b*Math.pow(t, 2)),	-(20*(((1-e)*(1-2*n))/4-((1-e)*(n+1))/4))/(3*Math.pow(b, 2)*Math.pow(t, 2)),	0,	-(20*((b*(1-e)*(n+1))/4+(b*(1-e)*n)/2))/(3*Math.pow(b, 2)*Math.pow(t, 2)),	0,	-(5*(1-e))/(3*b*Math.pow(t, 2))} ;
			Bb2[2] = new double[] {-(40*((-Math.pow(n, 2)-n-Math.pow(e, 2)-e+2)/8-((-2*n-1)*(1-n))/8-((-2*e-1)*(1-e))/8))/(3*a*b*Math.pow(t, 2)),	-(40*(-(a*(Math.pow(e, 2)-1))/8-(a*(e-1)*e)/4))/(3*a*b*Math.pow(t, 2)),	-(40*(-(b*(Math.pow(n, 2)-1))/8-(b*(n-1)*n)/4))/(3*a*b*Math.pow(t, 2)),	(5*(1-e))/(3*b*Math.pow(t, 2)),	(5*(1-n))/(3*a*Math.pow(t, 2)),	-(40*(-(-Math.pow(n, 2)-n-Math.pow(e, 2)+e+2)/8+((-2*n-1)*(1-n))/8-((1-2*e)*(e+1))/8))/(3*a*b*Math.pow(t, 2)),	-(40*(-(a*(Math.pow(e, 2)-1))/8-(a*e*(e+1))/4))/(3*a*b*Math.pow(t, 2)),	-(40*((b*(Math.pow(n, 2)-1))/8+(b*(n-1)*n)/4))/(3*a*b*Math.pow(t, 2)),	(5*(e+1))/(3*b*Math.pow(t, 2)),	-(5*(1-n))/(3*a*Math.pow(t, 2)),	-(40*((-Math.pow(n, 2)+n-Math.pow(e, 2)+e+2)/8+((1-2*n)*(n+1))/8+((1-2*e)*(e+1))/8))/(3*a*b*Math.pow(t, 2)),	-(40*((a*(Math.pow(e, 2)-1))/8+(a*e*(e+1))/4))/(3*a*b*Math.pow(t, 2)),	-(40*((b*(Math.pow(n, 2)-1))/8+(b*n*(n+1))/4))/(3*a*b*Math.pow(t, 2)),	-(5*(e+1))/(3*b*Math.pow(t, 2)),	-(5*(n+1))/(3*a*Math.pow(t, 2)),	-(40*(-(-Math.pow(n, 2)+n-Math.pow(e, 2)-e+2)/8-((1-2*n)*(n+1))/8+((-2*e-1)*(1-e))/8))/(3*a*b*Math.pow(t, 2)),	-(40*((a*(Math.pow(e, 2)-1))/8+(a*(e-1)*e)/4))/(3*a*b*Math.pow(t, 2)),	-(40*(-(b*(Math.pow(n, 2)-1))/8-(b*n*(n+1))/4))/(3*a*b*Math.pow(t, 2)),	-(5*(1-e))/(3*b*Math.pow(t, 2)),	(5*(n+1))/(3*a*Math.pow(t, 2))} ;				
			Bb2 = Util.MultMatrix(Bb2, Math.pow(w, 3) / 4.0);
    		
			return Bb2 ;
    	}
	}    
    
    public double[][] Bs(double e, double n, double w, List<Node> Node, Section sec, int option)
	{
		double[][] Bs1 = null,  Bs2 = null;
    	double t = sec.getT() / 1000.0;
		double a1 = 4 / (3.0 * Math.pow(t, 2));
		double[] ElemSize = calcHalfSize(Node);
		double a = ElemSize[0];
		double b = ElemSize[1];	
    	if (type.equals(ElemType.SM))
    	{
    		Bs1 = new double[2][12];
    		Bs2 = new double[2][12];
    		
			Bs1[0] = new double[] {(((-2*e-1)*(1-e)*(1-n))/8-((1-n)*(-Math.pow(n, 2)-n-Math.pow(e, 2)-e+2))/8)/a,	((a*(Math.pow(e, 2)-1)*(1-n))/8+(a*(e-1)*e*(1-n))/4)/a,	-(b*(n-1)*(Math.pow(n, 2)-1))/(8*a),	((1-e)*(1-n))/4,	0,	(((1-n)*(-Math.pow(n, 2)-n-Math.pow(e, 2)+e+2))/8+((1-2*e)*(e+1)*(1-n))/8)/a,	((a*(Math.pow(e, 2)-1)*(1-n))/8+(a*e*(e+1)*(1-n))/4)/a,	(b*(n-1)*(Math.pow(n, 2)-1))/(8*a),	((e+1)*(1-n))/4,	0,	(((n+1)*(-Math.pow(n, 2)+n-Math.pow(e, 2)+e+2))/8+((1-2*e)*(e+1)*(n+1))/8)/a,	((a*(Math.pow(e, 2)-1)*(n+1))/8+(a*e*(e+1)*(n+1))/4)/a,	(b*(n+1)*(Math.pow(n, 2)-1))/(8*a),	((e+1)*(n+1))/4,	0,	(((-2*e-1)*(1-e)*(n+1))/8-((n+1)*(-Math.pow(n, 2)+n-Math.pow(e, 2)-e+2))/8)/a,	((a*(Math.pow(e, 2)-1)*(n+1))/8+(a*(e-1)*e*(n+1))/4)/a,	-(b*(n+1)*(Math.pow(n, 2)-1))/(8*a),	((1-e)*(n+1))/4,	0} ;
			Bs1[1] = new double[] {(((1-e)*(-2*n-1)*(1-n))/8-((1-e)*(-Math.pow(n, 2)-n-Math.pow(e, 2)-e+2))/8)/b,	-(a*(e-1)*(Math.pow(e, 2)-1))/(8*b),	((b*(1-e)*(Math.pow(n, 2)-1))/8+(b*(1-e)*(n-1)*n)/4)/b,	0,	((1-e)*(1-n))/4,	(((e+1)*(-2*n-1)*(1-n))/8-((e+1)*(-Math.pow(n, 2)-n-Math.pow(e, 2)+e+2))/8)/b,	-(a*(e+1)*(Math.pow(e, 2)-1))/(8*b),	((b*(e+1)*(Math.pow(n, 2)-1))/8+(b*(e+1)*(n-1)*n)/4)/b,	0,	((e+1)*(1-n))/4,	(((e+1)*(-Math.pow(n, 2)+n-Math.pow(e, 2)+e+2))/8+((e+1)*(1-2*n)*(n+1))/8)/b,	(a*(e+1)*(Math.pow(e, 2)-1))/(8*b),	((b*(e+1)*(Math.pow(n, 2)-1))/8+(b*(e+1)*n*(n+1))/4)/b,	0,	((e+1)*(n+1))/4,	(((1-e)*(-Math.pow(n, 2)+n-Math.pow(e, 2)-e+2))/8+((1-e)*(1-2*n)*(n+1))/8)/b,	(a*(e-1)*(Math.pow(e, 2)-1))/(8*b),	((b*(1-e)*(Math.pow(n, 2)-1))/8+(b*(1-e)*n*(n+1))/4)/b,	0,	((1-e)*(n+1))/4} ;
			Bs1 = Util.MultMatrix(Bs1, 5.0 / 4.0);
			
			Bs2[0] = new double[] {(((-2*e-1)*(1-e)*(1-n))/8-((1-n)*(-Math.pow(n, 2)-n-Math.pow(e, 2)-e+2))/8)/a,	((a*(Math.pow(e, 2)-1)*(1-n))/8+(a*(e-1)*e*(1-n))/4)/a,	-(b*(n-1)*(Math.pow(n, 2)-1))/(8*a),	((1-e)*(1-n))/4,	0,	(((1-n)*(-Math.pow(n, 2)-n-Math.pow(e, 2)+e+2))/8+((1-2*e)*(e+1)*(1-n))/8)/a,	((a*(Math.pow(e, 2)-1)*(1-n))/8+(a*e*(e+1)*(1-n))/4)/a,	(b*(n-1)*(Math.pow(n, 2)-1))/(8*a),	((e+1)*(1-n))/4,	0,	(((n+1)*(-Math.pow(n, 2)+n-Math.pow(e, 2)+e+2))/8+((1-2*e)*(e+1)*(n+1))/8)/a,	((a*(Math.pow(e, 2)-1)*(n+1))/8+(a*e*(e+1)*(n+1))/4)/a,	(b*(n+1)*(Math.pow(n, 2)-1))/(8*a),	((e+1)*(n+1))/4,	0,	(((-2*e-1)*(1-e)*(n+1))/8-((n+1)*(-Math.pow(n, 2)+n-Math.pow(e, 2)-e+2))/8)/a,	((a*(Math.pow(e, 2)-1)*(n+1))/8+(a*(e-1)*e*(n+1))/4)/a,	-(b*(n+1)*(Math.pow(n, 2)-1))/(8*a),	((1-e)*(n+1))/4,	0} ;
			Bs2[1] = new double[] {(((1-e)*(-2*n-1)*(1-n))/8-((1-e)*(-Math.pow(n, 2)-n-Math.pow(e, 2)-e+2))/8)/b,	-(a*(e-1)*(Math.pow(e, 2)-1))/(8*b),	((b*(1-e)*(Math.pow(n, 2)-1))/8+(b*(1-e)*(n-1)*n)/4)/b,	0,	((1-e)*(1-n))/4,	(((e+1)*(-2*n-1)*(1-n))/8-((e+1)*(-Math.pow(n, 2)-n-Math.pow(e, 2)+e+2))/8)/b,	-(a*(e+1)*(Math.pow(e, 2)-1))/(8*b),	((b*(e+1)*(Math.pow(n, 2)-1))/8+(b*(e+1)*(n-1)*n)/4)/b,	0,	((e+1)*(1-n))/4,	(((e+1)*(-Math.pow(n, 2)+n-Math.pow(e, 2)+e+2))/8+((e+1)*(1-2*n)*(n+1))/8)/b,	(a*(e+1)*(Math.pow(e, 2)-1))/(8*b),	((b*(e+1)*(Math.pow(n, 2)-1))/8+(b*(e+1)*n*(n+1))/4)/b,	0,	((e+1)*(n+1))/4,	(((1-e)*(-Math.pow(n, 2)+n-Math.pow(e, 2)-e+2))/8+((1-e)*(1-2*n)*(n+1))/8)/b,	(a*(e-1)*(Math.pow(e, 2)-1))/(8*b),	((b*(1-e)*(Math.pow(n, 2)-1))/8+(b*(1-e)*n*(n+1))/4)/b,	0,	((1-e)*(n+1))/4} ;				
			Bs2 = Util.MultMatrix(Bs2, -15 * a1 * Math.pow(w, 2) / 4.0);
		}
    	
    	if (option == 1 && Bs1 != null)
    	{
    		return Bs1 ;
    	}
    	else if (option == 2 && Bs2 != null)
    	{
    		return Bs2 ;
    	}
    	else if (Bs1 != null && Bs2 != null)
    	{
    		return Util.AddMatrix(Bs1, Bs2) ;
    	}
    	else
    	{
    		return null;
    	}
	}
     
    public double[][] LoadVector(List<Node> Node)
	{
		double[][] Q = null;
		double[] ElemSize = calcHalfSize(Node);
		double a = ElemSize[0];
		double b = ElemSize[1];
		
		switch (type)
		{
			case KR1:
				
				Q = new double[3][12];
				
				Q[0] = new double[] {a*b,	(a*a*b)/3,	(a*b*b)/3,	a*b,	-(a*a*b)/3,	(a*b*b)/3,	a*b,	-(a*a*b)/3,	-(a*b*b)/3,	a*b,	(a*a*b)/3,	-(a*b*b)/3} ;
				Q[1] = new double[] {-a*b,	0,	-(a*b*b)/3,	a*b,	0,	(a*b*b)/3,	a*b,	0,	-(a*b*b)/3,	-a*b,	0,	(a*b*b)/3} ;
				Q[2] = new double[] {-a*b,	-(a*a*b)/3,	0,	-a*b,	(a*a*b)/3,	0,	a*b,	-(a*a*b)/3,	0,	a*b,	(a*a*b)/3,	0} ;
					
				break;
				
			case KP3:

				Q = new double[3][12];
				
				Q[0] = new double[] {a*b,	(a*a*b)/3,	(a*b*b)/3,	a*b,	-(a*a*b)/3,	(a*b*b)/3,	a*b,	-(a*a*b)/3,	-(a*b*b)/3,	a*b,	(a*a*b)/3,	-(a*b*b)/3} ;
				Q[1] = new double[] {-a*b,	0,	-(a*b*b)/3,	a*b,	0,	(a*b*b)/3,	a*b,	0,	-(a*b*b)/3,	-a*b,	0,	(a*b*b)/3} ;
				Q[2] = new double[] {-a*b,	-(a*a*b)/3,	0,	-a*b,	(a*a*b)/3,	0,	a*b,	-(a*a*b)/3,	0,	a*b,	(a*a*b)/3,	0} ;
					
				break;
				
			case SM, SM_H:

				Q = new double[5][20];
				
				Q[0] = new double[] {a*b,	(a*a*b)/3,	(a*b*b)/3,	0,	0,	a*b,	-(a*a*b)/3,	(a*b*b)/3,	0,	0,	a*b,	-(a*a*b)/3,	-(a*b*b)/3,	0,	0,	a*b,	(a*a*b)/3,	-(a*b*b)/3,	0,	0} ;
				Q[1] = new double[] {-b,	0,	-b*b/3,	0,	0,	b,	0,	b*b/3,	0,	0,	b,	0,	-b*b/3,	0,	0,	-b,	0,	b*b/3,	0,	0} ;
				Q[2] = new double[] {-a,	-a*a/3,	0,	0,	0,	-a,	a*a/3,	0,	0,	0,	a,	-a*a/3,	0,	0,	0,	a,	a*a/3,	0,	0,	0} ;
				Q[3] = new double[] {0,	0,	0,	a*b,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	a*b,	0} ;
				Q[4] = new double[] {0,	0,	0,	0,	a*b,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	a*b} ;
					
				break;
				
			case SM_C:

				Q = new double[5][24];
				
				Q[0] = new double[] {a*b,	(a*b)/3,	(a*b)/3,	(a*b)/9,	0,	0,	a*b,	-(a*b)/3,	(a*b)/3,	-(a*b)/9,	0,	0,	a*b,	-(a*b)/3,	-(a*b)/3,	(a*b)/9,	0,	0,	a*b,	(a*b)/3,	-(a*b)/3,	-(a*b)/9,	0,	0} ;
				Q[1] = new double[] {-b,	0,	-b/3,	0,	0,	0,	b,	0,	b/3,	0,	0,	0,	b,	0,	-b/3,	0,	0,	0,	-b,	0,	b/3,	0,	0,	0} ;
				Q[2] = new double[] {-a,	-a/3,	0,	0,	0,	0,	-a,	a/3,	0,	0,	0,	0,	a,	-a/3,	0,	0,	0,	0,	a,	a/3,	0,	0,	0,	0} ;
				Q[3] = new double[] {0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b,	0} ;
				Q[4] = new double[] {0,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b} ;
				
				break;
				
			case SM8:

				Q = new double[5][28];
							
				Q[0] = new double[] {a*b,	(a*a*b)/3,	(a*b*b)/3,	0,	0,	0,	0,	a*b,	-(a*a*b)/3,	(a*b*b)/3,	0,	0,	0,	0,	a*b,	-(a*a*b)/3,	-(a*b*b)/3,	0,	0,	0,	0,	a*b,	(a*a*b)/3,	-(a*b*b)/3,	0,	0,	0,	0} ;
				Q[1] = new double[] {-b,	0,	-b*b/3,	0,	0,	0,	0,	b,	0,	b*b/3,	0,	0,	0,	0,	b,	0,	-b*b/3,	0,	0,	0,	0,	-b,	0,	b*b/3,	0,	0,	0,	0} ;
				Q[2] = new double[] {-a,	-a*a/3,	0,	0,	0,	0,	0,	-a,	a*a/3,	0,	0,	0,	0,	0,	a,	-a*a/3,	0,	0,	0,	0,	0,	a,	a*a/3,	0,	0,	0,	0,	0} ;
				Q[3] = new double[] {0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0} ;
				Q[4] = new double[] {0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3} ;
					
				break;
				
			default: break;
			
		}	 
		
		return Q;
	}

    public static double[][] BendingConstitutiveMatrix(Material mat, boolean NonlinearMat, double[] strain)
    {
    	// Calcula a matriz com propriedades elâsticas do elemento. Unidade: N.m
    	double E = mat.getE() * Math.pow(10, 6) ;
    	double v = mat.getV() ;
    	double fu = mat.getG() * Math.pow(10, 3);	
    	double[][] D = new double[3][3];
    	if (NonlinearMat)
    	{
    		E = -Math.pow(E, 2) / (2 * fu) * Math.abs(strain[0]) + E;
    		E = Math.max(E, 0);
    	}
    	D[0][0] = 1;  D[0][1] = v;  D[0][2] = 0;
    	D[1][0] = v;  D[1][1] = 1;  D[1][2] = 0;
    	D[2][0] = 0;  D[2][1] = 0;  D[2][2] = (1 - v) / 2;
    	for (int i = 0; i <= D.length - 1; i += 1)
    	{
    	    for (int j = 0; j <= D[i].length - 1; j += 1)
        	{
        	    D[i][j] = D[i][j] * E / (1 - Math.pow(v, 2));
        	}  
    	}
    	return D;
    }
    
    public double[][] ShearConstitutiveMatrix(Material mat, Section sec)
    {
    	double[][] C = new double[2][2];
    	double E = mat.getE() * Math.pow(10, 6) ;
    	double v = mat.getV();
    	double t = sec.getT() / 1000.0;
    	double k = 5 / 6.0;
		double G = E / (2 * (1 + v));
		C[0][0] = G * k * t; C[0][1] = 0;
		C[1][0] = 0;         C[1][1] = G * k * t;
		return C;
    }
    
    public static double[][] ShearConstitutiveMatrix2(Material mat, Section sec)
    {
    	double[][] C = new double[2][2];
    	double E = mat.getE() * Math.pow(10, 6) ;
    	double v = mat.getV();
		double G = E / (2 * (1 + v));
		C[0][0] = G; C[0][1] = 0;
		C[1][0] = 0; C[1][1] = G;
		return C;
    }
       
	public double[][] nodesDefPos()
	{
		double[][] DefCoords = new double[externalNodes.size()][];
		for (int i = 0; i <= externalNodes.size() - 1; i += 1)
		{
			Node node = externalNodes.get(i) ;
			DefCoords[i] = new double[3];
			DefCoords[i][0] = node.getOriginalCoords().x + node.getDisp().x;
			DefCoords[i][1] = node.getOriginalCoords().y + node.getDisp().y;
			DefCoords[i][2] = node.getOriginalCoords().z + node.getDisp().z;
		}
		return DefCoords;
	}

    public double[][] StiffnessMatrix(List<Node> nodes, boolean NonlinearMat, boolean NonlinearGeo)
    {
    	// Calcula a matriz de rigidez do elemento

		if (mat == null) { System.out.println("Error: Elem without material while trying to calculate the stiffness matrix.") ; return null ;}
		if (sec == null) { System.out.println("Error: Elem without section while trying to calculate the stiffness matrix.") ; return null ;}

    	double[][] k = null;
		double[] ElemSize = calcHalfSize(nodes);
		double[][] Db = BendingConstitutiveMatrix(mat, NonlinearMat, Strain);
		double[][] Ds = ShearConstitutiveMatrix2(mat, sec);
		
		switch (type)
		{
			case KR1: k = StiffnessMatrix.KR1StiffnessMatrix(ElemSize, Db, sec); break ;
			case KR2: k = StiffnessMatrix.KR2StiffnessMatrix(ElemSize, Db, sec); break ;
			case MR1: k = StiffnessMatrix.MR1StiffnessMatrix(ElemSize, Db, Ds, sec); break ;
			case MR2: k = StiffnessMatrix.MR2StiffnessMatrix(ElemSize, Db, Ds, sec); break ;
			case R4: k = StiffnessMatrix.R4StiffnessMatrix(ElemSize, Db, sec); break ;
			case Q4: k = StiffnessMatrix.Q4StiffnessMatrix(nodes, externalNodes, ElemSize, Db, sec, NonlinearGeo); break ;
			case T3G: 
				
	        	double[][] D = BendingConstitutiveMatrix(mat, NonlinearMat, Strain);
	        	double thick = sec.getT() / 1000.0;
				double Area;
	    		if (!NonlinearGeo)
	        	{
	            	double[][] Coords = new double[externalNodes.size()][];
	    			for (int i = 0; i <= externalNodes.size() - 1; i += 1)
	    			{
		    			Coords[i] = new double[3];
		    			Coords[i][0] = externalNodes.get(i).getOriginalCoords().x ;
						Coords[i][1] = externalNodes.get(i).getOriginalCoords().y ;
						Coords[i][2] = externalNodes.get(i).getOriginalCoords().z ;
	    			}     	
	    			Area = ResultDiagrams.TriArea(Coords);
	        	}
	    		else
	        	{
	        		double[][] DefCoords = nodesDefPos() ;
	        		Area = ResultDiagrams.TriArea(DefCoords);
	        	}
	    	    double[][] B = SecondDerivativesb(0, 0, nodes, sec, NonlinearGeo);
	    	    k = Util.MultMatrix(Util.MultMatrix(Util.Transpose(B), D), B);
	    	    for (int i = 0; i <= k.length - 1; i += 1)
	        	{
	        		for (int j = 0; j <= k[i].length - 1; j += 1)
	        		{
	        			k[i][j] = Area * k[i][j] * thick;
	        		}
	        	}
	    	    
	    	    break ;
	    	    
			case T6G: break ;
	    	    
			case SM: k = StiffnessMatrix.SMStiffnessMatrix(ElemSize, Db, Ds, sec); break;
    		
    		/*double[][] kb = NumIntegration(Node, Elem, UserDefinedMat[Elem.getMat()], UserDefinedSec[Elem.getSec()], DOFsPerNode, NonlinearMat, NonlinearGeo, strain, 3);
    		double[][] ks = NumIntegration(Node, Elem, UserDefinedMat[Elem.getMat()], UserDefinedSec[Elem.getSec()], DOFsPerNode, NonlinearMat, NonlinearGeo, strain, 3);
    		k = Util.AddMatrix(kb,  ks);*/
			case SM8: k = StiffnessMatrix.SM8StiffnessMatrix(ElemSize, Db, Ds, sec); break;
			case KP3: k = StiffnessMatrix.KP3StiffnessMatrix(ElemSize, Db, sec); break;
			case SM_C: k = StiffnessMatrix.SM_CStiffnessMatrix(ElemSize, Db, Ds, sec); break;
			case SM_H: k = StiffnessMatrix.SM_HStiffnessMatrix(ElemSize, Db, Ds, sec); break;
			
		}
		
		for (int i = 0; i <= k.length - 1; i += 1)
		{
			for (int j = i; j <= k.length - 1; j += 1)
			{
				k[j][i] = k[i][j];
			}
		}
		
    	return k;
    }
    
    public double[] StrainsOnPoint(List<Node> Node, double e, double n, double[] U, Section sec, boolean NonlinearGeo)
	{
		double[] strain = new double[3];
		
		double[] u = DispVec(Node, U);
		//double[][] B = ElementStuff.SecondDerivativesb(e, n, Node, Elem, ElemTypes, Sec, NonlinearGeo);
		double[][] B = SecondDerivativesb(e, n, Node, sec, NonlinearGeo);		
		strain = Util.MultMatrixVector(B, u);
		
		return strain;
	}
    
    public double[] ForcesOnPoint(List<Node> Node, int[] StrainsOnElem, Material mat, Section sec, boolean NonlinearMat, double e, double n, double[] U, boolean NonlinearGeo)
	{
		double[] forces = new double[3];
		double thick = sec.getT() / 1000.0;
    	double[] strain = StrainsOnPoint(Node, e, n, U, sec, NonlinearGeo);
    	double[][] Db = BendingConstitutiveMatrix(mat, NonlinearMat, strain);
    	double[][] Ds = ShearConstitutiveMatrix(mat, sec);

    	double[] strainb = new double[] {strain[0], strain[1], strain[2]};
    	forces = Util.MultMatrixVector(Db, strainb);
    	if (StrainTypes.length == 5)
    	{
        	double[] strains = new double[] {strain[3], strain[4]};
        	forces = Util.JoinVec(forces, Util.MultMatrixVector(Ds, strains));
    	}
    	forces = Util.MultVector(forces, Math.pow(thick, 3)/12.0);
    	
		
		return forces;
	}

    public double[] StressesOnPoint(List<Node> Node, Element Elem, double e, double n, Material mat, int[][] DOFsOnNode, double[] U, Section sec, boolean NonlinearMat, boolean NonlinearGeo)
	{
		double[] strain = new double[3];
		double[] sigma = new double[3];
		
		strain = StrainsOnPoint(Node, e, n, U, sec, NonlinearGeo);
    	double[][] D = Element.BendingConstitutiveMatrix(mat, NonlinearMat, strain);
		sigma = Util.MultMatrixVector(D, strain);
		
		return sigma;
	}
          
  	public double[] DispVec(List<Node> nodes, double[] U)
	{
		int ulength = 0;
		for (Node node : externalNodes)
		{
			ulength += node.getDOFType().length ;
		}
		double[] u = new double[ulength];
		for (int i = 0; i <= externalNodes.size() - 1; i += 1)
    	{
			Node node = externalNodes.get(i) ;
			for (int dof = 0; dof <= node.getDOFType().length - 1; dof += 1)
	    	{
				if (-1 < NodeDOF[i][dof])
				{
					u[CumDOFs[i] + dof] = U[NodeDOF[i][dof]];
				}
				else
				{
					u[CumDOFs[i] + dof] = 0;
				}
	    	}
    	}
		return u;
	}
	
	public double[] ForceVec(List<Node> Node, boolean NonlinearMat, boolean NonlinearGeo, double[] U)
	{
		int Nnodes = externalNodes.size();
		int[] ElemDOFs = DOFs;
		double[] p = new double[Nnodes * ElemDOFs.length];
		
		double[][] k = StiffnessMatrix(Node, NonlinearMat, NonlinearGeo);
		double[] u = DispVec(Node, U);

		p = Util.MultMatrixVector(k, u);
		
		return p;
	}
	
	public double[] StrainVec(List<Node> Node, double[] U, boolean NonlinearGeo)
	{
		int Nnodes = externalNodes.size();
		float[] e = null, n = null;
		double[][] strains = new double[Nnodes][];
		double[] strainvec = new double[Nnodes * StrainTypes.length];
		
		if (externalNodes.size() == 4)
		{
			e = new float[] {-1, 1, 1, -1};
			n = new float[] {-1, -1, 1, 1};
		}
		else if (externalNodes.size() == 8)
		{
			e = new float[] {-1, 1, 1, -1, (float) 0.5, (float) 0.5, -1, 1};
			n = new float[] {-1, -1, 1, 1, -1, 1, (float) 0.5, (float) 0.5};
		}
		for (int node = 0; node <= Nnodes - 1; node += 1)
		{
			strains[node] = StrainsOnPoint(Node, e[node], n[node], U, sec, NonlinearGeo);
		}
		for (int node = 0; node <= Nnodes - 1; node += 1)
		{
			for (int strain = 0; strain <= StrainTypes.length - 1; strain += 1)
			{
				strainvec[StrainTypes.length * node + strain] = strains[node][strain];
			}
		}

		return strainvec;
	}
		
	public double[] StressesVec(List<Node> Node, double[] U, boolean NonlinearMat, boolean NonlinearGeo)
	{
		int Nnodes = externalNodes.size();
		float[] e = null, n = null;
		double[] sigmavec = new double[Nnodes * StrainTypes.length];

		if (externalNodes.size() == 4)
		{
			e = new float[] {-1, 1, 1, -1};
			n = new float[] {-1, -1, 1, 1};
		}
		else if (externalNodes.size() == 8)
		{
			e = new float[] {-1, 1, 1, -1, (float) 0.5, (float) 0.5, -1, 1};
			n = new float[] {-1, -1, 1, 1, -1, 1, (float) 0.5, (float) 0.5};
		}
    	double strainvec[] = StrainVec(Node, U, NonlinearGeo);
    	double[][] Db = BendingConstitutiveMatrix(mat, NonlinearMat, strainvec);
    	double[][] Ds = ShearConstitutiveMatrix(mat, sec);
    	double[][] D;
    	
    	if (3 < DOFs.length)
    	{
    		D = new double[5][5];
        	D[0][0] = Db[0][0];
        	D[1][0] = Db[1][0];
        	D[2][0] = Db[2][0];
        	D[0][1] = Db[0][1];
        	D[1][1] = Db[1][1];
        	D[2][1] = Db[2][1];
        	D[0][2] = Db[0][2];
        	D[1][2] = Db[1][2];
        	D[2][2] = Db[2][2];
        	D[3][3] = Ds[0][0];
        	D[3][4] = Ds[0][1];
        	D[4][3] = Ds[1][0];
        	D[4][4] = Ds[1][1];
    	}
    	else
    	{
    		D = Db;
    	}
		double[][] strains = new double[Nnodes][];
    	double[][] sigma = new double[Nnodes][];
		
    	for (int node = 0; node <= Nnodes - 1; node += 1)
		{
    		strains[node] = StrainsOnPoint(Node, e[node], n[node], U, sec, NonlinearGeo);
		}
    	for (int node = 0; node <= Nnodes - 1; node += 1)
		{
        	sigma[node] = Util.MultMatrixVector(D, strains[node]);
		}
    	for (int node = 0; node <= Nnodes - 1; node += 1)
		{
			for (int stress = 0; stress <= StrainTypes.length - 1; stress += 1)
			{
				sigmavec[StrainTypes.length * node + stress] = sigma[node][stress];
			}
		}
    	
    	return sigmavec;
	}
	
	public double[] InternalForcesVec(List<Node> Node, double[] U, boolean NonlinearMat, boolean NonlinearGeo)
	{
		int Nnodes = externalNodes.size();
		float[] e = null, n = null;
		int[] StrainsOnElem = StrainTypes;
		double[] forcevec = new double[Nnodes * StrainTypes.length];
    	double[][] forces = new double[Nnodes][];

		if (externalNodes.size() == 4)
		{
			e = new float[] {-1, 1, 1, -1};
			n = new float[] {-1, -1, 1, 1};
		}
		else if (externalNodes.size() == 8)
		{
			e = new float[] {-1, 1, 1, -1, (float) 0.5, (float) 0.5, -1, 1};
			n = new float[] {-1, -1, 1, 1, -1, 1, (float) 0.5, (float) 0.5};
		}
    	for (int node = 0; node <= Nnodes - 1; node += 1)
		{
    		forces[node] = ForcesOnPoint(Node, StrainsOnElem, mat, sec, NonlinearMat, e[node], n[node], U, NonlinearGeo);
        }
    	for (int node = 0; node <= Nnodes - 1; node += 1)
		{
			for (int force = 0; force <= StrainTypes.length - 1; force += 1)
			{
				forcevec[StrainTypes.length * node + force] = forces[node][force];
			}
		}
    	
    	return forcevec;
	}

	public void RecordResults(List<Node> Node, double[] U, boolean NonlinearMat, boolean NonlinearGeo)
	{
		setDisp(DispVec(Node, U));
		setStrain(StrainVec(Node, U, NonlinearGeo));
		setStress(StressesVec(Node, U, NonlinearMat, NonlinearGeo));
		setIntForces(InternalForcesVec(Node, U, NonlinearMat, NonlinearGeo));
    
	}

	public static void draw3D(Mesh mesh, int[] SelectedElems,
						boolean showmatcolor, boolean showseccolor, boolean showcontour, boolean showdeformed,
						double Defscale, MyCanvas canvas, DrawPrimitives DP)
	{
		List<Element> Elem = mesh.getElements();
		double[] RealCanvasCenter = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), MainPanel.getInstance().getCentralPanel().getStructure().getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
		{
			List<Node> Nodes = Elem.get(elem).getExternalNodes();
			int[] ElemDOFs = Elem.get(elem).getDOFs();
			int[][] DrawingCoord = new int[Nodes.size()][2]; 
			int[] xCoords = new int[Nodes.size() + 1], yCoords = new int[Nodes.size() + 1];
			Color color = new Color(0, 100, 55);
			if (Elem.get(elem).getMat() != null)
			{
				color = Util.AddColor(color, new double[] {0, -50, 100});
			}
			if (Elem.get(elem).getSec() != null)
			{
				color = Util.AddColor(color, new double[] {0, -50, 100});
			}
			if (showmatcolor && Elem.get(elem).getMat() != null)
			{
				color = Elem.get(elem).getMat().getColor() ;
			}
			if (showseccolor && Elem.get(elem).getSec() != null)
			{
				color = Elem.get(elem).getSec().getColor() ;
			}
			for (int node = 0; node <= Nodes.size() - 1; node += 1)
			{
				if (showdeformed)
				{
					double[] DeformedCoords = Util.ScaledDefCoords(Nodes.get(node).getOriginalCoords(), Nodes.get(node).getDisp(), ElemDOFs, Defscale);
					DrawingCoord[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(DeformedCoords, RealCanvasCenter, canvas.getAngles().asArray()), MainPanel.getInstance().getCentralPanel().getStructure().getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
				}
				else
				{
					double[] OriginalCoords = Util.GetNodePos(Nodes.get(node), showdeformed);
					DrawingCoord[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(OriginalCoords, RealCanvasCenter, canvas.getAngles().asArray()), MainPanel.getInstance().getCentralPanel().getStructure().getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
				}
				xCoords[node] = DrawingCoord[node][0];
				yCoords[node] = DrawingCoord[node][1];
			}
			xCoords[Nodes.size()] = DrawingCoord[0][0];
			yCoords[Nodes.size()] = DrawingCoord[0][1];
			// DrawPolygon(xCoords, yCoords, thick, false, true, Color.black, color);
			DP.drawPolygon(xCoords, yCoords, color) ;
			if (showcontour)
			{
				// DrawPolygon(xCoords, yCoords, thick, true, false, Color.black, color);
				DP.drawPolyLine(xCoords, yCoords, color) ;
			}
			if (SelectedElems != null)
			{
				for (int i = 0; i <= SelectedElems.length - 1; i += 1)
				{
					if (elem == SelectedElems[i])
					{
						// DrawPolygon(xCoords, yCoords, thick, false, true, Color.black, Color.red);
						DP.drawPolygon(xCoords, yCoords, Main.palette[4]) ;
					}
				}
			}
		}
	}

	public boolean isInside(SelectionWindow selectionWindow, MyCanvas canvas, List<Node> meshNodes)
	{
		for (Node node : externalNodes)
		{
			Point nodeDrawingCoords = canvas.inDrawingCoords(node.getOriginalCoords()) ;
			if (!selectionWindow.contains(nodeDrawingCoords))
			{
				return false ;
			}
		}
		// for (int nodeID : externalNodes)
		// {
		// 	Point nodeDrawingCoords = canvas.inDrawingCoords(meshNodes.get(nodeID).getOriginalCoords()) ;
		// 	if (!selectionWindow.contains(nodeDrawingCoords))
		// 	{
		// 		return false ;
		// 	}
		// }

		return true ;
	}

	public void select() { isSelected = true ;}

	public void unselect() { isSelected = false ;}


	public int getId() {return id;}
	public ElemShape getShape() {return Shape;}
	public int[] getDOFs() {return DOFs;}
	public int[][] getDOFsPerNode() {return DOFsPerNode;}
	public int[] getStrainTypes() {return StrainTypes;}
	public List<Node> getExternalNodes() {return externalNodes;}
	public List<Node> getInternalNodes() {return internalNodes;}
	public Material getMat() {return mat;}
	public Section getSec() {return sec;}
	public List<DistLoad> getDistLoads() {return distLoads;}
	public double[] getDisp() {return Disp;}
	public double[] getStress() {return Stress;}
	public double[] getStrain() {return Strain;}
	public double[] getIntForces() {return IntForces;}
	public ElemType getType() {return type;}
	public int[][] getNodeDOF() {return NodeDOF;}
	public int[] getCumDOFs() {return CumDOFs;}	
	public double[][] getUndeformedCoords(){ return UndeformedCoords ;}
	public double[][] getDeformedCoords(){ return DeformedCoords ;}
	public double[] getCenterCoords(){ return CenterCoords ;}
	public boolean isSelected() { return isSelected ;}
	// public Color getMatColor () {return MatColor;}
	// public Color getSecColor () {return SecColor;}
	public void setId(int I) {id = I;}
	public void setShape(ElemShape S) {Shape = S;}
	public void setDOFs(int[] D) {DOFs = D;}
	public void setDOFsPerNode(int[][] D) {DOFsPerNode = D;}
	public void setStrainTypes(int[] ST) {StrainTypes = ST;}
	public void setExternalNodes(List<Node> N) {externalNodes = N;}
	public void setInternalNodes(List<Node> N) {internalNodes = N;}
	public void setMat(Material M) {mat = M;}
	public void setSec(Section S) {sec = S;}
	public void setDisp(double[] D) {Disp = D;}
	public void setStress(double[] S) {Stress = S;}
	public void setStrain(double[] S) {Strain = S;}
	public void setIntForces(double[] I) {IntForces = I;}
	public void setType(ElemType T) {type = T;}
	public void setCumDOFs(int[] C) {CumDOFs = C;}
	public void setNodeDOF(int[][] N) {NodeDOF = N;}
	// public void setMatColor (Color color) {MatColor = color;}
	// public void setSecColor (Color color) {SecColor = color;}

	// TODO
	public Node getBottomLeftNode() { return externalNodes.get(3) ;}
	public Node getTopRightNode() { return externalNodes.get(1) ;}


	@Override
	public String toString()
	{
		return id + "	" + type + "	" + externalNodes + "	" + mat + "	" + sec + "	" + distLoads ;
	}

}
