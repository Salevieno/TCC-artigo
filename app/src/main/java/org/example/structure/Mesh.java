package org.example.structure;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.example.mainTCC.MenuFunctions;
import org.example.userInterface.Draw;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.MainPanel;
import org.example.view.SelectionWindow;

import graphics.Align;
import graphics.DrawPrimitives;

public class Mesh
{
	private List<Node> nodes;
	private List<Element> elems;

    public Mesh(List<Node> node, List<Element> elem)
    {
        this.nodes = node;
        this.elems = elem;
    }

    
	public static void DrawElemDetails(ElemType elemType, MyCanvas canvas, DrawPrimitives DP)
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
			Elem = new Element(0, new int[] {0, 1, 2, 3}, elemType);
		}
		else if (elemShape.equals(ElemShape.quad))
		{
			nodes.add(new Node(0, new Point3D(1, 1, 0))) ;
			nodes.add(new Node(1, new Point3D(9, 3, 0))) ;
			nodes.add(new Node(2, new Point3D(7, 9, 0))) ;
			nodes.add(new Node(3, new Point3D(3, 7, 0))) ;
			Elem = new Element(0, new int[] {0, 1, 2, 3}, elemType);
		}
		else if (elemShape.equals(ElemShape.triangular))
		{
			nodes.add(new Node(0, new Point3D(1, 1, 0))) ;
			nodes.add(new Node(1, new Point3D(9, 5, 0))) ;
			nodes.add(new Node(2, new Point3D(1, 9, 0))) ;
			Elem = new Element(0, new int[] {0, 1, 2}, elemType);
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
			Elem = new Element(0, new int[] {0, 4, 1, 7, 2, 5, 3, 6}, elemType);
		}
		int[] DrawingStructCenter = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(RealStructCenter.asArray(), Center, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		int textSize = 16;
		Color textColor = Menus.palette[8];
		
		for (int node = 0; node <= nodes.size() - 1; node += 1)
		{
			nodes.get(node).setDOFType(Elem.getDOFsPerNode()[node]);
		}
		Elem.setCumDOFs(Util.CumDOFsOnElem(nodes, Elem.getExternalNodes().length));
		for (int node = 0; node <= nodes.size() - 1; node += 1)
		{
			nodes.get(node).dofs = new int[Elem.getDOFsPerNode()[node].length];
			for (int dof = 0; dof <= Elem.getDOFsPerNode()[node].length - 1; dof += 1)
			{
				nodes.get(node).dofs[dof] = Elem.getCumDOFs()[node] + dof;
			}
		}
		DrawNodes3D(nodes, null, Node.color, false, nodes.get(0).getDOFType(), 1, canvas, DP);
		Element.draw3D(new Mesh(nodes, List.of(Elem)), null, false, false, true, false, 1, canvas, DP);
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
		double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			int[][] DrawingCoords = new int[Node.size()][3];
			if (deformed)
			{
				double[] DeformedCoords = Util.ScaledDefCoords(Node.get(node).getOriginalCoords(), Node.get(node).getDisp(), DOFsPerNode, Defscale);
				DrawingCoords[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(DeformedCoords, Center, canvas.getAngles()), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			}
			else
			{
				DrawingCoords[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(Util.GetNodePos(Node.get(node), deformed), Center, canvas.getAngles()), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			}
			Point drawingCoords = new Point(DrawingCoords[node][0], DrawingCoords[node][1]) ;
			DP.drawCircle(drawingCoords, size, NodeColor);
			if (selectedNodes != null)
			{
				for (int i = 0; i <= selectedNodes.size() - 1; i += 1)
				{
					if (node == selectedNodes.get(i).getID())
					{
						DP.drawCircle(drawingCoords, 2*size, Menus.palette[4]);
					}
				}
			}
		}
	}

	
	private static void DrawDOFSymbols(List<Node> Node, Color NodeColor, boolean deformed, MyCanvas canvas, DrawPrimitives DP)
	{
		Color ForceDOFColor = Menus.palette[8];
		Color MomentDOFColor = Menus.palette[9];
		Color CrossDerivativeDOFColor = Menus.palette[11];
		Color ShearRotationDOFColor = Menus.palette[11];
		int thickness = 2;
		double arrowsize = 0.5;
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			double[] NodeRealPos = Util.GetNodePos(Node.get(node), deformed);
			for (int dof = 0; dof <= Node.get(node).dofs.length - 1; dof += 1)
			{
				if (Node.get(node).getDOFType()[dof] == 0)
				{
					Draw.DrawArrow3Dto(NodeRealPos, thickness, new double[] {0, 0, 0}, arrowsize, 0.3 * arrowsize, ForceDOFColor, canvas, DP);
				}
				if (Node.get(node).getDOFType()[dof] == 1)
				{
					Draw.DrawArrow3Dto(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, arrowsize, 0.3 * arrowsize, ForceDOFColor, canvas, DP);
				}
				if (Node.get(node).getDOFType()[dof] == 2)
				{
					Draw.DrawArrow3Dto(NodeRealPos, thickness, new double[] {0, Math.PI / 2, 0}, arrowsize, 0.3 * arrowsize, ForceDOFColor, canvas, DP);
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


    public void reset()
    {
        nodes = null ;
        elems = null ;
    }

    public List<Node> getNodes()  { return nodes ;}
    public List<Element> getElements() { return elems ;}

	private boolean hasNodes() { return nodes != null && !nodes.isEmpty() ;}

	public boolean hasNodesSelected()
	{
		return nodes.stream().filter(node -> node.isSelected()).findAny().isPresent() ;
	}
	public boolean hasElementsSelected()
	{
		return elems.stream().filter(elem -> elem.isSelected()).findAny().isPresent() ;
	}

	public List<Node> getSelectedNodes() { return nodes.stream().filter(node -> node.isSelected()).collect(Collectors.toList()) ;}
	public List<Element> getSelectedElements() { return elems.stream().filter(elem -> elem.isSelected()).collect(Collectors.toList()) ;}

	private boolean hasElements() { return elems != null && !elems.isEmpty() ;}

	public boolean allElementsHaveType()
	{

		if (!hasElements()) { return false ;}

		for (Element elem : elems)
		{
			if (elem.getType() == null)
			{
				return false;
			}
		}

		return true;
	}

	public boolean allElementsHaveMat()
	{

		if (!hasElements()) { return false ;}

		for (Element elem : elems)
		{
			if (elem.getMat() == null)
			{
				return false;
			}
		}

		return true;
	}

	public boolean allElementsHaveSec()
	{

		if (!hasElements()) { return false ;}

		for (Element elem : elems)
		{
			if (elem.getSec() == null)
			{
				return false;
			}
		}

		return true;
	}

	public static Mesh CreateMesh(List<Point3D> structureCoords, Point3D structureCenter, MeshType meshType, int noffsets, int qtdIntermediatePoints, List<Node> nodes, List<Element> elems, ElemType elemType)
	{		
		if (elemType == null)
		{
			System.out.println("\nElement shape is null at Menus -> StructureMenuCreateMesh") ;
			return null ;
		}

	    int[] nintermediatepoints = new int[noffsets];
		Arrays.fill(nintermediatepoints, qtdIntermediatePoints);
		switch (meshType)
		{
			case cartesian:
				nodes = CreateCartesianNodes(structureCoords, new int[] {noffsets, nintermediatepoints[0]}, elemType);
				elems = CreateCartesianMesh(nodes, new int[] {noffsets, nintermediatepoints[0]}, elemType);
				break ;
				
			case radial:
				nodes = CreateRadialNodes(structureCoords, structureCenter, noffsets, nintermediatepoints);
				elems = CreateRadialMesh(nodes, noffsets, elemType);
				break ;
				
			default:
				System.out.println("\nMesh type not defined at Menus -> StructureMenuCreateMesh") ;
				return null;
		}
		for (int i = 0; i <= elems.size() - 1; i += 1)
		{
        	elems.get(i).setUndeformedCoords(nodes);
        	elems.get(i).setCenterCoords();
		}

		return new Mesh(nodes, elems) ;
	}
	
	public static Mesh CreateMesh(List<Point3D> structureCoords, Point3D structureCenter, MeshType meshType, int[][] meshInfo, List<Node> nodes, List<Element> elems, ElemType elemType)
	{
		return CreateMesh(structureCoords, structureCenter, meshType, meshInfo[0][0], meshInfo[0][1], nodes, elems, elemType) ;
	}

	public static List<Node> CreateCartesianNodes(List<Point3D> structureCoords, int[] NumberElem, ElemType elemType)
	{
		double MinXCoord = Structure.calcMinCoords(structureCoords).x ;
		double MinYCoord = Structure.calcMinCoords(structureCoords).y ;
		double MaxXCoord = Structure.calcMaxCoords(structureCoords).x ;
		double MaxYCoord = Structure.calcMaxCoords(structureCoords).y ;

		double L = MaxXCoord - MinXCoord, H = MaxYCoord - MinYCoord;
		double dx = L / NumberElem[0], dy = H / NumberElem[1];
		ElemShape elemShape = Element.typeToShape(elemType);

		Node[] Node = null;
		if (elemShape.equals(ElemShape.rectangular) | elemShape.equals(ElemShape.triangular))
		{
			Node = new Node[(NumberElem[0] + 1)*(NumberElem[1] + 1)];
			for (int i = 0; i <= NumberElem[1]; i += 1)
			{
				for (int j = 0; j <= NumberElem[0]; j += 1)
				{
					Node[i*(NumberElem[0] + 1) + j] = new Node(i*(NumberElem[0] + 1) + j, new Point3D(MinXCoord + j*dx, MinYCoord + i*dy, 0));
				}
			}
		}
		else if (elemShape.equals(ElemShape.r8))
		{
			Node = new Node[(2 * NumberElem[0] + 1)*(2 * NumberElem[1] + 1) - NumberElem[0]*NumberElem[1]];
			int nodeID = 0;
			dx = L / (2 * NumberElem[0]);
			dy = H / (2 * NumberElem[1]);
			for (int i = 0; i <= 2 * NumberElem[1]; i += 1)
			{
				if (i % 2 == 0)
				{
					for (int j = 0; j <= 2 * NumberElem[0]; j += 1)
					{
						Node[nodeID] = new Node(nodeID, new Point3D(MinXCoord + j*dx, MinYCoord + i*dy, 0));
						nodeID += 1;
					}
				}
				else
				{
					for (int j = 0; j <= 2 * NumberElem[0] / 2; j += 1)
					{
						Node[nodeID] = new Node(nodeID, new Point3D(MinXCoord + 2*j*dx, MinYCoord + i*dy, 0));
						nodeID += 1;
					}
				}
			
			}
		}
		else if (elemShape.equals(ElemShape.r9))
		{
			Node = new Node[(2*NumberElem[0] + 1)*(2*NumberElem[1] + 1)];
			for (int i = 0; i <= 2*NumberElem[1]; i += 1)
			{
				for (int j = 0; j <= 2*NumberElem[0]; j += 1)
				{
					Node[i*(2*NumberElem[0] + 1) + j] = new Node(i*(2*NumberElem[0] + 1) + j, new Point3D(MinXCoord + j*dx, MinYCoord + i*dy, 0));
				}
			}
		}
		
		return Arrays.asList(Node);
	}
	
	public static List<Node> CreateRadialNodes(List<Point3D> structureCoords, Point3D structureCenter, int noffsets, int[] nintermediatepoints)
	{
		// Calculate number of nodes in each column
		Node[] nodes = null;
	    double[][] P2 = null;	    
	    for (int i = 0; i <= noffsets - 1; i += 1)
		{
		    double offset = i / (double) noffsets;
		    double[][] P1 = Util.CreateInternalPolygonPoints(structureCoords, structureCenter, offset);
            double[][] InternalLines = Util.PolygonLines(P1);
            double[][] IntermediatePoints = Util.CreateIntermediatePoints(InternalLines, nintermediatepoints[i], true);
            for (int j = 0; j <=  IntermediatePoints.length - 1; j += 1)
    		{
                P2 = Util.AddElem(P2, IntermediatePoints[j]);
    		}
		}
	    nodes = new Node[P2.length + 1];
        for (int node = 0; node <= P2.length - 1; node += 1)
		{
		    nodes[node] = new Node(node, new Point3D(P2[node][0], P2[node][1], P2[node][2]));
		}
	    nodes[P2.length] = new Node(P2.length, structureCenter);
	    return Arrays.asList(nodes);
	}
	
	public static List<Element> CreateCartesianMesh(List<Node> Node, int[] NElems, ElemType elemType)
	{
		Element[] Elem = null;
		ElemShape elemShape = Element.typeToShape(elemType);
		if (elemShape.equals(ElemShape.rectangular))
		{
			// Elements
		    //   4 ____ 3
		    //    |    | 
		    //    |    |  
		    //   1|____|2
			int[] NNodes = new int[] {NElems[0] + 1, NElems[1] + 1};
	        Elem = new Element[NElems[0]*NElems[1]];
			for (int j = 0; j <= NElems[1] - 1; j += 1)
			{
				for (int i = 0; i <= NElems[0] - 1; i += 1)
				{
					int ElemID = i + j*NElems[0];
					int[] ElemNodes = new int[] {i + j*NNodes[0], i + j*NNodes[0] + 1, (j + 1)*NNodes[0] + i + 1, (j + 1)*NNodes[0] + i};
		        	Elem[ElemID] = new Element(ElemID, ElemNodes, null, null, null, elemType);
				}
			}
		}
		else if (elemShape.equals(ElemShape.r8))
		{
			// Elements
		    //   7____6____ 5
		    //    |        | 
		    //   8|        |4
		    //    |        |  
		    //   1|________|3
			//		  2
			int[] NNodes = new int[] {2 * NElems[0] + 1, 2 * NElems[1] + 1};
	        Elem = new Element[NElems[0] * NElems[1]];
			for (int j = 0; j <= NElems[1] - 1; j += 1)
			{
				for (int i = 0; i <= NElems[0] - 1; i += 1)
				{
					int ElemID = i + j*NElems[0];
					int[] ElemNodes = new int[] {2*i + 2*j*NNodes[0] - j*NElems[0], 					2*i + 2*j*NNodes[0] - j*NElems[0] + 1, 						2*i + 2*j*NNodes[0] - j*NElems[0] + 2,
												 2*i + (2*j + 1)*NNodes[0] - j*NElems[0] - i + 1, 		2*i + (2*j + 2)*NNodes[0] - (j + 1)*NElems[0] + 2, 		2*i + (2*j + 2)*NNodes[0] - (j + 1)*NElems[0] + 1,
												 2*i + (2*j + 2)*NNodes[0] - (j + 1)*NElems[0],		 	2*i + (2*j + 1)*NNodes[0] - j*NElems[0] - i};
		        	Elem[ElemID] = new Element(ElemID, ElemNodes, null, null, null, elemType);
				}
			}
		}
		else if (elemShape.equals(ElemShape.r9))
		{
			// Elements
		    //   7____6____ 5
		    //    |        | 
		    //   8|   9    |4
		    //    |        |  
		    //   1|________|3
			//		  2
			int[] NNodes = new int[] {2 * NElems[0] + 1, 2 * NElems[1] + 1};
	        Elem = new Element[NElems[0]*NElems[1]];
			for (int j = 0; j <= NElems[1] - 1; j += 1)
			{
				for (int i = 0; i <= NElems[0] - 1; i += 1)
				{
					int ElemID = i + j*NElems[0];
					int[] ElemExtNodes = new int[] {2*i + 2*j*NNodes[0], 2*i + 2*j*NNodes[0] + 1, 2*i + 2*j*NNodes[0] + 2,
													2*i + (2*j + 1)*NNodes[0] + 2, 2*i + (2*j + 2)*NNodes[0] + 2, 2*i + (2*j + 2)*NNodes[0] + 1, 2*i + (2*j + 2)*NNodes[0], 2*i + (2*j + 1)*NNodes[0]};
					int[] ElemIntNodes = new int[] {2*i + (2*j + 1)*NNodes[0] + 1};
					Elem[ElemID] = new Element(ElemID, ElemExtNodes, ElemIntNodes, null, null, elemType);
				}
			}
		}
		else if (elemShape.equals(ElemShape.triangular))
		{
			// Elements
		    //   1.___.3
		    //    |\  | 
		    //    |2\1|  
		    //  2.|__\|.4
			Elem = new Element[2 * NElems[0] * NElems[1]];
			int NumRows = NElems[0];
		    for (int j = 0; j <= NumRows - 1; j += 1)
		    {
				int NumberElemInCol = NElems[1] + NElems[1];
		        for (int i = 0; i <= NumberElemInCol / 2 - 1; i += 1)
		        {
		        	int ElemID = 2 * i + j*NumberElemInCol;
		        	int[] elemnodes1 = new int[] {i + j * (NElems[0] + 1), i + j * (NElems[0] + 1) + 1, i + (j + 1) * (NElems[1] + 1)};
		        	int[] elemnodes2 = new int[] {i + (j + 1) * (NElems[1] + 1) + 1, i + (j + 1) * (NElems[1] + 1), i + j * (NElems[0] + 1) + 1};
		        	Elem[ElemID] = new Element(ElemID, elemnodes1, null, null, null, elemType);
		        	Elem[ElemID + 1] = new Element(ElemID + 1, elemnodes2, null, null, null, elemType);
		        }
		    }
		}
		return Arrays.asList(Elem);
	}

	public static List<Element> CreateRadialMesh(List<Node> Node, int noffsets, ElemType elemType)
	{
		Element[] Elem = null;
		ElemShape elemShape = Element.typeToShape(elemType);
		if (elemShape.equals(ElemShape.rectangular))
		{
			int nNodesPerCicle = (Node.size() - 1) / noffsets;
	        Elem = new Element[(int) (nNodesPerCicle * (noffsets - 0.5))];
	        int cont = 0;
	        for (int i = 0; i <= noffsets - 1; i += 1)
			{
	        	int[] elemnodes = null;
			    if (i < noffsets - 1)
			    {
	    		    for (int j = 0; j <= nNodesPerCicle - 2; j += 1)
	        		{
	    		    	elemnodes = new int[] {i*nNodesPerCicle + j, i*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j};
	        		    Elem[cont] = new Element(cont, elemnodes, null, null, null, elemType);
	        		    cont += 1;
	        		}
			    	elemnodes = new int[] {(i + 1)*nNodesPerCicle - 1, i * nNodesPerCicle, (i + 1)*nNodesPerCicle, (i + 2)*nNodesPerCicle - 1};
	    		    Elem[cont] = new Element(cont, elemnodes, null, null, null, elemType);
	    		    cont += 1;
			    }
			    else
			    {
			    	elemnodes = new int[] {(i + 1) * nNodesPerCicle - 1, i * nNodesPerCicle, i * nNodesPerCicle + 1, Node.size() - 1};
	    		    Elem[cont] = new Element(cont, elemnodes, null, null, null, elemType);
	    		    cont += 1;
	    		    for (int j = 1; j <= nNodesPerCicle / 2 - 1; j += 1)
	        		{
	    		    	elemnodes = new int[] {i * nNodesPerCicle + 2 * j - 1, i * nNodesPerCicle + 2 * j, i * nNodesPerCicle + 2 * j + 1, Node.size() - 1};
	        		    Elem[cont] = new Element(cont, elemnodes, null, null, null, elemType);
	        		    cont += 1;
	        		}
			    }
			}
		}
		
		if (elemShape.equals(ElemShape.triangular))
		{
			// Elements
		    //   1.___.3
		    //    |\  | 
		    //    |2\1|  
		    //  2.|__\|.4
			int nNodesPerCicle = (Node.size() - 1) / noffsets;
	        Elem = new Element[(int) (2*nNodesPerCicle*(noffsets - 0.5))];
	        int cont = 0;
	        for (int i = 0; i <= noffsets - 1; i += 1)
			{
	        	int[] elemnodes = null;
			    if (i < noffsets - 1)
			    {
	    		    for (int j = 0; j <= nNodesPerCicle - 2; j += 1)
	        		{
	    		    	elemnodes = new int[] {i*nNodesPerCicle + j, i*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j};
	        		    Elem[cont] = new Element(cont, elemnodes, null, null, null, elemType);
	        		    cont += 1;
	        		    elemnodes = new int[] {i*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j};
	        		    Elem[cont] = new Element(cont, elemnodes, null, null, null, elemType);
	        		    cont += 1;
	        		}
			    	elemnodes = new int[] {(i + 1)*nNodesPerCicle - 1, (i + 1)*nNodesPerCicle, (i + 2)*nNodesPerCicle - 1};
	    		    Elem[cont] = new Element(cont, elemnodes, null, null, null, elemType);
	    		    cont += 1;
			    	elemnodes = new int[] {(i + 1)*nNodesPerCicle - 1, i*nNodesPerCicle, (i + 1)*nNodesPerCicle};
	    		    Elem[cont] = new Element(cont, elemnodes, null, null, null, elemType);
	    		    cont += 1;
			    }
			    else
			    {
	    		    for (int j = 0; j <= nNodesPerCicle - 2; j += 1)
	        		{
	    		    	elemnodes = new int[] {i*nNodesPerCicle + j, i*nNodesPerCicle + j + 1, Node.size() - 1};
	        		    Elem[cont] = new Element(cont, elemnodes, null, null, null, elemType);
	        		    cont += 1;
	        		}
			    	elemnodes = new int[] {i*nNodesPerCicle + nNodesPerCicle - 1, i*nNodesPerCicle, Node.size() - 1};
	    		    Elem[cont] = new Element(cont, elemnodes, null, null, null, elemType);
	    		    cont += 1;
			    }
			}
		}
		
        return Arrays.asList(Elem);
	}
	
	public static int NodeMouseIsOn(List<Node> Node, Point MousePos, MyCanvas canvas, double prec, boolean condition)
	{
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			if (Util.MouseIsOnNode(Node, MousePos, canvas, node, prec, condition))
		    {
				return node;
		    }
		}
		return -1;
	}

	// public static int[] ElemsSelection(MyCanvas canvas, double[] StructCenter, Mesh mesh, Point MousePos, int[] DPPos, Point SelWindowInitPos,
	// 	double[] DiagramScales, boolean ShowSelWindow, boolean ShowDeformedStructure)
	// {
	// 	int ElemMouseIsOn = ElemMouseIsOn(mesh, MousePos, StructCenter, canvas, ShowDeformedStructure);
	// 	int[] SelectedElems = new int[] ;
	// 	if (ShowSelWindow)
	// 	{
	// 		int[] ElemsInSelWindow = Util.ElemsInsideWindow(mesh, StructCenter, new int[] {SelWindowInitPos.x, SelWindowInitPos.y}, MousePos, DPPos, canvas, DiagramScales[1], ShowDeformedStructure);
	// 		if (ElemsInSelWindow != null)
	// 		{
	// 			for (int i = 0; i <= ElemsInSelWindow.length - 1; i += 1)
	// 			{
	// 				SelectedElems = Util.AddElem(SelectedElems, ElemsInSelWindow[i]);
	// 			}
	// 		}
	// 		/*else if (ElemMouseIsOn == -1)
	// 		{
	// 			SelectedElems = null;
	// 		}*/
	// 	}
	// 	else if (-1 < ElemMouseIsOn)
	// 	{
	// 		SelectedElems = Util.AddElem(SelectedElems, ElemMouseIsOn);
	// 		/*for (int elem = 0; elem <= Elem.length - 1; elem += 1)
	// 		{
	// 			double[] RealMousePos = ConvertToRealCoords2(MousePos, StructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
	// 			if (Util.MouseIsOnElem(Node, Elem[elem], RealMousePos, canvas.getPos(), canvas.getSize(), canvas.getDrawingPos(), ShowDeformedStructure))
	// 		    {
	// 				SelectedElems = Util.AddElem(SelectedElems, elem);
	// 		    }
	// 		}*/
	// 	}
		
	// 	return SelectedElems;
	// }

	public static int ElemMouseIsOn(Mesh mesh, Point MousePos, double[] StructCenter, MyCanvas canvas, boolean condition)
	{
		List<Node> Node = mesh.getNodes();
		List<Element> Elem = mesh.getElements();
		Point2D.Double RealMousePos = canvas.inRealCoords(MousePos) ;
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
		{
			if (Util.MouseIsOnElem(Node, Elem.get(elem), new double[] {RealMousePos.x, RealMousePos.y}, new int[] {canvas.getPos().x, canvas.getPos().y}, canvas.getSize(), canvas.getDrawingPos(), condition))
		    {
				return elem;
		    }
		}
		return -1;
	}

	
	public void unselectAllNodes()
	{
		nodes.forEach(Node::unselect) ;
	}
	public void unselectAllElements()
	{
		elems.forEach(Element::unselect) ;
	}

	public void displayElements(MyCanvas canvas, double defScale, boolean showmatcolor, boolean showseccolor, boolean showcontour, boolean showdeformed, DrawPrimitives DP)
	{		
		elems.forEach(elem -> elem.display(canvas, nodes, showmatcolor, showseccolor, showcontour, showdeformed, defScale, DP)) ;
	}

	public void displayElemNumbers(Mesh mesh, Color NodeColor, boolean deformed, MyCanvas canvas, DrawPrimitives DP)
	{
		List<Node> Node = mesh.getNodes();
		List<Element> Elem = mesh.getElements();
		// int FontSize = 13;
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
		{
			int[] DrawingCoords = new int[2];
			for (int elemnode = 0; elemnode <= Elem.get(elem).getExternalNodes().length - 1; elemnode += 1)
			{
				int nodeID = Elem.get(elem).getExternalNodes()[elemnode];
				Node node = Node.get(nodeID) ;
				Point3D nodeDeformedPos = new Point3D(Util.GetNodePos(node, deformed)[0], Util.GetNodePos(node, deformed)[1], Util.GetNodePos(node, deformed)[2]) ;
				// Point3D nodeDeformedPos = new Point3D(node.deformedPos()[0], deformed)[0], Util.GetNodePos(Node.get(nodeID), deformed)[1], Util.GetNodePos(Node.get(nodeID), deformed)[2]) ;
				Point nodeDeformedPosInDrawingCoords = canvas.inDrawingCoords(nodeDeformedPos) ;
				DrawingCoords[0] += nodeDeformedPosInDrawingCoords.x;
				DrawingCoords[1] += nodeDeformedPosInDrawingCoords.y;
				// DrawingCoords[0] += Util.ConvertToDrawingCoords(Util.GetNodePos(Node.get(node), deformed), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getDrawingPos())[0];
				// DrawingCoords[1] += Util.ConvertToDrawingCoords(Util.GetNodePos(Node.get(node), deformed), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getDrawingPos())[1];
			}
			DrawingCoords[0] = DrawingCoords[0] / Elem.get(elem).getExternalNodes().length;
			DrawingCoords[1] = DrawingCoords[1] / Elem.get(elem).getExternalNodes().length;
			// DrawText(DrawingCoords, Integer.toString(elem), "Center", 0, "Bold", FontSize, NodeColor);		
			DP.drawText(new Point(DrawingCoords[0], DrawingCoords[1]), Align.center, String.valueOf(elem), NodeColor) ;
		}	
	}

	public void displayNodes(boolean deformed, double Defscale, MyCanvas canvas, DrawPrimitives DP)
	{
		int[] dofs = elems.get(0).getDOFs() ;
		nodes.forEach(node -> node.display(canvas, dofs, deformed, Defscale, deformed, DP)) ;
	}

	public void displayNodeNumbers(List<Node> Node, Color NodeColor, boolean deformed, MyCanvas canvas, DrawPrimitives DP)
	{
		int offset = 6;
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			// int[][] DrawingCoords = new int[Node.size()][];
			int[] DrawingCoords = Util.ConvertToDrawingCoords2Point3D(Util.GetNodePos(Node.get(node), deformed), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			Point pos = new Point((int)(DrawingCoords[0] + offset), (int)(DrawingCoords[1] + 14)) ;
			DP.drawText(pos, Align.topLeft, String.valueOf(node), NodeColor) ;		
		}	
	}

	public void display(MyCanvas canvas, double Defscale, boolean showmatcolor, boolean showseccolor, boolean showcontour, boolean showdeformed, DrawPrimitives DP)
	{
		if (elems != null && ! elems.isEmpty())
		{
			displayElements(canvas, Defscale, showmatcolor, showseccolor, showcontour, showdeformed, DP) ;
		}
		if (nodes != null && !nodes.isEmpty())
		{
			displayNodes(showdeformed, MenuFunctions.DiagramScales[1], canvas, DP) ;
		}
	}

	private void printNodes()
	{
		System.out.println("\nNodes");
		System.out.println("ID	Original coords (m)	Displacements (m)	Sup ConcLoads (kN)	NodalDisps (m)");		
		nodes.forEach(System.out::println);
	}
	
	private void printElems()
	{
		System.out.println("\nElems");
		System.out.println("ID	Type	Nodes		Mat	Sec	DistLoads (kN/m)");
		elems.forEach(System.out::println);
	}

	public void print()
	{
		printNodes();
		printElems();
	}
	

    @Override
    public String toString()
    {
		String string = "" ;
        string += "Mesh: " ;
		string += "\nnodes: " ;
		string += "\ncoords	sup	concLoad	nodalDisp" ;
		for (Node node : nodes)
		{
			string += "\n" + node.toString() ;
		}
		string += "\nelements: " ;
		string += "\nid	type	externalNodes	mat	sec	distLoads" ;
		for (Element elem : elems)
		{
			string += "\n" + elem.toString() ;
		}

		return string ;
    }

}
