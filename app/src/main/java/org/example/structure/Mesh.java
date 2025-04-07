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
import org.example.userInterface.MenuBar;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.CentralPanel;
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

	public Mesh(MeshDTO dto)
	{
		this.nodes = dto.createNodes() ;
		this.elems = dto.createElements(nodes) ;
	}
    
	
	public static Point3D dofAngles(int dof)
	{
		switch (dof)
		{
			case 0: return new Point3D(0, 0, 0) ;
			case 1: return new Point3D(0, 0, 0 - Math.PI/2.0) ;
			case 2: return new Point3D(0, 0 + Math.PI/2.0, 0) ;
		
			default: return null ;
		}
	}


	public int[] defineDOFsOnNode()
	{
		int[] DOFsOnNode = null;  
		int[][] DOFsPerNode = (int[][]) elems.get(0).getDOFsPerNode() ;
		for (Element elem : elems)
		{
        	for (int elemnode = 0; elemnode <= elem.getExternalNodes().size() - 1; elemnode += 1)
        	{
        		DOFsOnNode = DOFsPerNode[elemnode];
        	}
		}
        
        return DOFsOnNode;
	}

	public void assignElementDOFs()
	{
		for (Element elem : elems)
		{
			elem.setCumDOFs(elem.cumDOFs());
        	elem.setUndeformedCoords(nodes);
        	elem.updateCenterCoords();
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

	public void assignMaterials(Material mat)
	{
		if (mat == null) { return ;}
		if (!hasElementsSelected()) { return ;}		
		
		List<Element> selectedElems = getSelectedElements() ;
		selectedElems.forEach(elem -> elem.setMat(mat)) ;
		
		unselectAllElements() ;
	}

	public void assignSections(Section sec)
	{
		if (sec == null) { return ;}		
		if (!hasElementsSelected()) { return ;}
		
		List<Element> selectedElems = getSelectedElements() ;
		selectedElems.forEach(elem -> elem.setSec(sec)) ;
		
		unselectAllElements() ;
	}

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
				nodes = createCartesianNodes(structureCoords, new int[] {noffsets, qtdIntermediatePoints}, elemType);
				elems = CreateCartesianMesh(nodes, new Point(noffsets, qtdIntermediatePoints), elemType);
				break ;
				
			case radial:
				nodes = createRadialNodes(structureCoords, structureCenter, noffsets, nintermediatepoints);
				elems = CreateRadialMesh(nodes, noffsets, elemType);
				break ;
				
			default:
				System.out.println("\nMesh type not defined at Menus -> StructureMenuCreateMesh") ;
				return null;
		}
		for (int i = 0; i <= elems.size() - 1; i += 1)
		{
        	elems.get(i).setUndeformedCoords(nodes);
        	elems.get(i).updateCenterCoords();
		}

		return new Mesh(nodes, elems) ;
	}
	
	public static Mesh CreateMesh(List<Point3D> structureCoords, Point3D structureCenter, MeshType meshType, int[][] meshInfo, List<Node> nodes, List<Element> elems, ElemType elemType)
	{
		return CreateMesh(structureCoords, structureCenter, meshType, meshInfo[0][0], meshInfo[0][1], nodes, elems, elemType) ;
	}

	public static List<Node> createCartesianNodes(List<Point3D> structureCoords, int[] NumberElem, ElemType elemType)
	{
		double xMin = Structure.calcMinCoords(structureCoords).x ;
		double yMin = Structure.calcMinCoords(structureCoords).y ;
		double xMax = Structure.calcMaxCoords(structureCoords).x ;
		double yMax = Structure.calcMaxCoords(structureCoords).y ;

		double L = xMax - xMin, H = yMax - yMin;
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
					Node[i*(NumberElem[0] + 1) + j] = new Node(i*(NumberElem[0] + 1) + j, new Point3D(xMin + j*dx, yMin + i*dy, 0));
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
						Node[nodeID] = new Node(nodeID, new Point3D(xMin + j*dx, yMin + i*dy, 0));
						nodeID += 1;
					}
				}
				else
				{
					for (int j = 0; j <= 2 * NumberElem[0] / 2; j += 1)
					{
						Node[nodeID] = new Node(nodeID, new Point3D(xMin + 2*j*dx, yMin + i*dy, 0));
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
					Node[i*(2*NumberElem[0] + 1) + j] = new Node(i*(2*NumberElem[0] + 1) + j, new Point3D(xMin + j*dx, yMin + i*dy, 0));
				}
			}
		}
		
		return Arrays.asList(Node);
	}
	
	public static List<Node> createRadialNodes(List<Point3D> structureCoords, Point3D structureCenter, int noffsets, int[] nintermediatepoints)
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

	private static List<Node> getNodesByID(List<Node> meshNodes, int[] nodeIDs)
	{
		List<Node> nodes = new ArrayList() ;
		for (int nodeID : nodeIDs)
		{
			Node nodeFound = meshNodes.stream().filter(node -> node.getID() == nodeID).findFirst().get() ;
			nodes.add(nodeFound) ;
		}

		return nodes ;
	}

	public List<Node> getNodesByID(int[] nodeIDs)
	{
		return getNodesByID(nodes, nodeIDs) ;
	}
	
	public static List<Element> CreateCartesianMesh(List<Node> nodes, Point numElems, ElemType elemType)
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
			int[] NNodes = new int[] {numElems.x + 1, numElems.y + 1};
	        Elem = new Element[numElems.x*numElems.y];
			for (int j = 0; j <= numElems.y - 1; j += 1)
			{
				for (int i = 0; i <= numElems.x - 1; i += 1)
				{
					int ElemID = i + j*numElems.x;
					int[] ElemNodes = new int[] {i + j*NNodes[0], i + j*NNodes[0] + 1, (j + 1)*NNodes[0] + i + 1, (j + 1)*NNodes[0] + i};
		        	Elem[ElemID] = new Element(getNodesByID(nodes, ElemNodes), null, null, null, elemType);
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
			int[] NNodes = new int[] {2 * numElems.x + 1, 2 * numElems.y + 1};
	        Elem = new Element[numElems.x * numElems.y];
			for (int j = 0; j <= numElems.y - 1; j += 1)
			{
				for (int i = 0; i <= numElems.x - 1; i += 1)
				{
					int ElemID = i + j*numElems.x;
					int[] ElemNodes = new int[] {2*i + 2*j*NNodes[0] - j*numElems.x, 					2*i + 2*j*NNodes[0] - j*numElems.x + 1, 						2*i + 2*j*NNodes[0] - j*numElems.x + 2,
												 2*i + (2*j + 1)*NNodes[0] - j*numElems.x - i + 1, 		2*i + (2*j + 2)*NNodes[0] - (j + 1)*numElems.x + 2, 		2*i + (2*j + 2)*NNodes[0] - (j + 1)*numElems.x + 1,
												 2*i + (2*j + 2)*NNodes[0] - (j + 1)*numElems.x,		 	2*i + (2*j + 1)*NNodes[0] - j*numElems.x - i};
		        	Elem[ElemID] = new Element(getNodesByID(nodes, ElemNodes), null, null, null, elemType);
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
			int[] NNodes = new int[] {2 * numElems.x + 1, 2 * numElems.y + 1};
	        Elem = new Element[numElems.x*numElems.y];
			for (int j = 0; j <= numElems.y - 1; j += 1)
			{
				for (int i = 0; i <= numElems.x - 1; i += 1)
				{
					int ElemID = i + j*numElems.x;
					int[] ElemExtNodes = new int[] {2*i + 2*j*NNodes[0], 2*i + 2*j*NNodes[0] + 1, 2*i + 2*j*NNodes[0] + 2,
													2*i + (2*j + 1)*NNodes[0] + 2, 2*i + (2*j + 2)*NNodes[0] + 2, 2*i + (2*j + 2)*NNodes[0] + 1, 2*i + (2*j + 2)*NNodes[0], 2*i + (2*j + 1)*NNodes[0]};
					int[] ElemIntNodes = new int[] {2*i + (2*j + 1)*NNodes[0] + 1};
					Elem[ElemID] = new Element(getNodesByID(nodes, ElemExtNodes), getNodesByID(nodes, ElemIntNodes), null, null, elemType);
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
			Elem = new Element[2 * numElems.x * numElems.y];
			int NumRows = numElems.x;
		    for (int j = 0; j <= NumRows - 1; j += 1)
		    {
				int NumberElemInCol = numElems.y + numElems.y;
		        for (int i = 0; i <= NumberElemInCol / 2 - 1; i += 1)
		        {
		        	int ElemID = 2 * i + j*NumberElemInCol;
		        	int[] elemnodes1 = new int[] {i + j * (numElems.x + 1), i + j * (numElems.x + 1) + 1, i + (j + 1) * (numElems.y + 1)};
		        	int[] elemnodes2 = new int[] {i + (j + 1) * (numElems.y + 1) + 1, i + (j + 1) * (numElems.y + 1), i + j * (numElems.x + 1) + 1};
		        	Elem[ElemID] = new Element(getNodesByID(nodes, elemnodes1), null, null, null, elemType);
		        	Elem[ElemID + 1] = new Element(getNodesByID(nodes, elemnodes2), null, null, null, elemType);
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
	        		    Elem[cont] = new Element(getNodesByID(Node, elemnodes), null, null, null, elemType);
	        		    cont += 1;
	        		}
			    	elemnodes = new int[] {(i + 1)*nNodesPerCicle - 1, i * nNodesPerCicle, (i + 1)*nNodesPerCicle, (i + 2)*nNodesPerCicle - 1};
	    		    Elem[cont] = new Element(getNodesByID(Node, elemnodes), null, null, null, elemType);
	    		    cont += 1;
			    }
			    else
			    {
			    	elemnodes = new int[] {(i + 1) * nNodesPerCicle - 1, i * nNodesPerCicle, i * nNodesPerCicle + 1, Node.size() - 1};
	    		    Elem[cont] = new Element(getNodesByID(Node, elemnodes), null, null, null, elemType);
	    		    cont += 1;
	    		    for (int j = 1; j <= nNodesPerCicle / 2 - 1; j += 1)
	        		{
	    		    	elemnodes = new int[] {i * nNodesPerCicle + 2 * j - 1, i * nNodesPerCicle + 2 * j, i * nNodesPerCicle + 2 * j + 1, Node.size() - 1};
	        		    Elem[cont] = new Element(getNodesByID(Node, elemnodes), null, null, null, elemType);
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
	        		    Elem[cont] = new Element(getNodesByID(Node, elemnodes), null, null, null, elemType);
	        		    cont += 1;
	        		    elemnodes = new int[] {i*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j};
	        		    Elem[cont] = new Element(getNodesByID(Node, elemnodes), null, null, null, elemType);
	        		    cont += 1;
	        		}
			    	elemnodes = new int[] {(i + 1)*nNodesPerCicle - 1, (i + 1)*nNodesPerCicle, (i + 2)*nNodesPerCicle - 1};
	    		    Elem[cont] = new Element(getNodesByID(Node, elemnodes), null, null, null, elemType);
	    		    cont += 1;
			    	elemnodes = new int[] {(i + 1)*nNodesPerCicle - 1, i*nNodesPerCicle, (i + 1)*nNodesPerCicle};
	    		    Elem[cont] = new Element(getNodesByID(Node, elemnodes), null, null, null, elemType);
	    		    cont += 1;
			    }
			    else
			    {
	    		    for (int j = 0; j <= nNodesPerCicle - 2; j += 1)
	        		{
	    		    	elemnodes = new int[] {i*nNodesPerCicle + j, i*nNodesPerCicle + j + 1, Node.size() - 1};
	        		    Elem[cont] = new Element(getNodesByID(Node, elemnodes), null, null, null, elemType);
	        		    cont += 1;
	        		}
			    	elemnodes = new int[] {i*nNodesPerCicle + nNodesPerCicle - 1, i*nNodesPerCicle, Node.size() - 1};
	    		    Elem[cont] = new Element(getNodesByID(Node, elemnodes), null, null, null, elemType);
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
		elems.forEach(elem -> elem.displayNumber(canvas, nodes, deformed, DP)) ;
	}

	public void displayNodes(boolean deformed, double Defscale, MyCanvas canvas, DrawPrimitives DP)
	{
		int[] dofs = elems.get(0).getDOFs() ;
		nodes.forEach(node -> node.display(canvas, dofs, deformed, Defscale, deformed, DP)) ;
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
