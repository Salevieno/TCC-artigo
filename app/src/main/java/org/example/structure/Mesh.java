package org.example.structure;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.userInterface.DrawingOnAPanel;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

public class Mesh
{
	private List<Node> nodes;
	private List<Element> elems;

    public Mesh(List<Node> node, List<Element> elem)
    {
        this.nodes = node;
        this.elems = elem;
    }

    
	public static void DrawElemDetails(ElemType elemType, MyCanvas canvas, DrawingOnAPanel DP)
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
		DP.DrawNodes3D(nodes, null, Node.color, false, nodes.get(0).getDOFType(), 1, canvas);
		DP.DrawElements3D(new Mesh(nodes, List.of(Elem)), null, false, false, true, false, 1, canvas);
		DP.DrawDOFNumbers(nodes, Node.color, false, canvas);
		DP.DrawDOFSymbols(nodes, Node.color, false, canvas);
		DP.DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1] - (int) (1 * 1.5 * textSize)}, elemType.toString(), "Center", 0, "Bold", textSize, textColor);
		DP.DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1]}, "Graus de liberdade: " + Arrays.toString(Elem.getDOFs()), "Center", 0, "Bold", textSize, textColor);
		DP.DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1] + (int) (1 * 1.5 * textSize)}, "Deformaçõs: " + Arrays.toString(Elem.getStrainTypes()), "Center", 0, "Bold", textSize, textColor);
	}

    public void reset()
    {
        nodes = null ;
        elems = null ;
    }

    public List<Node> getNodes()  { return nodes ;}
    public List<Element> getElements() { return elems ;}

	private boolean hasNodes() { return nodes != null && !nodes.isEmpty() ;}

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

	public static Mesh CreateMesh(List<Point3D> structureCoords, Point3D structureCenter, MeshType meshType, int[][] meshInfo, List<Node> nodes, List<Element> elems, ElemType elemType)
	{		
		if (elemType == null)
		{
			System.out.println("\nElement shape is null at Menus -> StructureMenuCreateMesh") ;
			return null ;
		}

		int noffsets = meshInfo[0][0];
	    int[] nintermediatepoints = new int[noffsets];
		Arrays.fill(nintermediatepoints, meshInfo[0][1]);
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
	
	public static int[] ElemsSelection(MyCanvas canvas, double[] StructCenter, Mesh mesh, Point MousePos, int[] DPPos, int[] SelectedElems, Point SelWindowInitPos,
	double[] DiagramScales, boolean ShowSelWindow, boolean ShowDeformedStructure)
	{
		int ElemMouseIsOn = ElemMouseIsOn(mesh, MousePos, StructCenter, canvas, ShowDeformedStructure);
		if (ShowSelWindow)
		{
			int[] ElemsInSelWindow = Util.ElemsInsideWindow(mesh, StructCenter, new int[] {SelWindowInitPos.x, SelWindowInitPos.y}, MousePos, DPPos,
			canvas, DiagramScales[1], ShowDeformedStructure);
			if (ElemsInSelWindow != null)
			{
				for (int i = 0; i <= ElemsInSelWindow.length - 1; i += 1)
				{
					SelectedElems = Util.AddElem(SelectedElems, ElemsInSelWindow[i]);
				}
			}
			/*else if (ElemMouseIsOn == -1)
			{
				SelectedElems = null;
			}*/
		}
		else if (-1 < ElemMouseIsOn)
		{
			SelectedElems = Util.AddElem(SelectedElems, ElemMouseIsOn);
			/*for (int elem = 0; elem <= Elem.length - 1; elem += 1)
			{
				double[] RealMousePos = ConvertToRealCoords2(MousePos, StructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
				if (Util.MouseIsOnElem(Node, Elem[elem], RealMousePos, canvas.getPos(), canvas.getSize(), canvas.getDrawingPos(), ShowDeformedStructure))
			    {
					SelectedElems = Util.AddElem(SelectedElems, elem);
			    }
			}*/
		}
		
		return SelectedElems;
	}

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


	public void displayElements(MyCanvas canvas, double Defscale, boolean showmatcolor, boolean showseccolor, boolean showcontour, boolean showdeformed, DrawingOnAPanel DP)
	{		
		int thick = 1;
		double[] RealCanvasCenter = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), DP.getRealStructCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		
		for (int elem = 0; elem <= elems.size() - 1; elem += 1)
		{
			int[] Nodes = elems.get(elem).getExternalNodes();
			int[] ElemDOFs = elems.get(elem).getDOFs();
			// int[][] DrawingCoord = new int[Nodes.length][2]; 
			List<Point> DrawingCoord = new ArrayList<>() ;
			int[] xCoords = new int[Nodes.length + 1], yCoords = new int[Nodes.length + 1];
			Color color = new Color(0, 100, 55);
			if (elems.get(elem).getMat() != null)
			{
				color = Util.AddColor(color, new double[] {0, -50, 100});
			}
			if (elems.get(elem).getSec() != null)
			{
				color = Util.AddColor(color, new double[] {0, -50, 100});
			}
			if (showmatcolor && elems.get(elem).getMat() != null)
			{
				color = elems.get(elem).getMat().getColor() ;
			}
			if (showseccolor && elems.get(elem).getSec() != null)
			{
				color = elems.get(elem).getSec().getColor() ;
			}
			for (int node = 0; node <= Nodes.length - 1; node += 1)
			{
				if (showdeformed)
				{
					double[] DeformedCoords = Util.ScaledDefCoords(nodes.get(Nodes[node]).getOriginalCoords().asArray(), nodes.get(Nodes[node]).getDisp(), ElemDOFs, Defscale);
					double[] rotatedCoords = Util.RotateCoord(DeformedCoords, RealCanvasCenter, canvas.getAngles()) ;
					Point drawingCoord = canvas.inDrawingCoords(new Point2D.Double(rotatedCoords[0], rotatedCoords[1])) ;
					DrawingCoord.add(drawingCoord) ;
					// DrawingCoord[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(DeformedCoords, RealCanvasCenter, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), RealCanvasCenter, canvas.getDrawingPos());
				}
				else
				{
					double[] OriginalCoords = Util.GetNodePos(nodes.get(Nodes[node]), showdeformed);
					double[] rotatedCoords = Util.RotateCoord(OriginalCoords, RealCanvasCenter, canvas.getAngles()) ;
					Point drawingCoord = canvas.inDrawingCoords(new Point2D.Double(rotatedCoords[0], rotatedCoords[1])) ;
					DrawingCoord.add(drawingCoord) ;
					// DrawingCoord[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(OriginalCoords, RealCanvasCenter, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
				}
				xCoords[node] = DrawingCoord.get(node).x;
				yCoords[node] = DrawingCoord.get(node).y;
			}
			xCoords[Nodes.length] = DrawingCoord.get(0).x;
			yCoords[Nodes.length] = DrawingCoord.get(0).y;
			DP.DrawPolygon(xCoords, yCoords, thick, false, true, Color.black, color);
			if (showcontour)
			{
				DP.DrawPolygon(xCoords, yCoords, thick, true, false, Color.black, color);
			}
			// if (SelectedElems != null)
			// {
				// for (int i = 0; i <= SelectedElems.length - 1; i += 1)
				// {
				// 	if (elem == SelectedElems[i])
				// 	{
				// 		DP.DrawPolygon(xCoords, yCoords, thick, false, true, Color.black, Color.red);
				// 	}
				// }
			// }
		}
	}

	public void displayNodes(List<Node> selectedNodes, Color NodeColor, boolean deformed, double Defscale, MyCanvas canvas, DrawingOnAPanel DP)
	{
		int[] DOFsPerNode = elems.get(0).getDOFs() ;
		int size = 6;
		int thick = 1;
		// double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), DP.getRealStructCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		Point2D.Double Center = canvas.inRealCoords(new Point(canvas.getCenter()[0], canvas.getCenter()[1])) ;
		List<Point> drawingCoords = new ArrayList<>() ;
		for (int node = 0; node <= nodes.size() - 1; node += 1)
		{
			// int[][] DrawingCoords = new int[nodes.size()][3];
			if (deformed)
			{
				double[] DeformedCoords = Util.ScaledDefCoords(nodes.get(node).getOriginalCoords().asArray(), nodes.get(node).getDisp(), DOFsPerNode, Defscale);
				double[] rotatedCoord = Util.RotateCoord(DeformedCoords, new double[] {Center.x, Center.y}, canvas.getAngles()) ;
				drawingCoords.add(canvas.inDrawingCoords(new Point2D.Double(rotatedCoord[0], rotatedCoord[1]))) ;
			}
			else
			{
				double[] rotatedCoord = Util.RotateCoord(Util.GetNodePos(nodes.get(node), deformed), new double[] {Center.x, Center.y}, canvas.getAngles()) ;
				drawingCoords.add(canvas.inDrawingCoords(new Point2D.Double(rotatedCoord[0], rotatedCoord[1]))) ;
			}
			DP.DrawCircle(drawingCoords.get(node), size, thick, false, true, Color.black, NodeColor);
			if (selectedNodes != null)
			{
				for (int i = 0; i <= selectedNodes.size() - 1; i += 1)
				{
					if (node == selectedNodes.get(i).getID())
					{
						DP.DrawCircle(drawingCoords.get(node), 2*size, thick, false, true, Color.black, Color.red);
					}
				}
			}
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
