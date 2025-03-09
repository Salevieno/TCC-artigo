package main.structure;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.gui.Menus;
import main.mainTCC.MenuFunctions;
import main.output.Results;
import main.utilidades.Point3D;
import main.utilidades.Util;

public class Structure
{
	private String name;
	private StructureShape shape;
	private List<Point3D> coords;	// Structure edge coordinates [x, y, z]
	private Point3D center;
	private Point3D minCoords;
	private Point3D maxCoords;

	private Mesh mesh ;

	private double[][] K;		// Stiffness matrix
	private double[] P;			// Load vector
	private double[] U;			// Displacement vector
	
	public int NFreeDOFs;
	private Results results ;
	
	private static final int NCirclePoints = 20;
	public static final Color color = Menus.palette[5];
	
	public Structure(String name, StructureShape shape, List<Point3D> coords)
	{
		this.name = name;
		this.shape = shape;
		this.coords = coords;
		if (coords != null && !coords.isEmpty())
		{
			center = calcCenter(coords);
			minCoords = calcMinCoords(coords);
			maxCoords = calcMaxCoords(coords);
		}
		results = new Results() ;
	}
	
	private static Point3D calcCenter(List<Point3D> coords)
	{
		Point3D center = new Point3D(0, 0, 0);

		for (Point3D coord : coords)
		{
			center.x += coord.x ;
			center.y += coord.y ;
			center.z += coord.z ;
		}

		center.x = center.x / (double)coords.size() ;
		center.y = center.y / (double)coords.size() ;
		center.z = center.z / (double)coords.size() ;
		
		return center;
	}

	public static Point3D calcMinCoords(List<Point3D> coords)
	{
		double minX = coords.stream().map(coord -> coord.x).min(Double::compare).get() ;
		double minY = coords.stream().map(coord -> coord.y).min(Double::compare).get() ;
		double minZ = coords.stream().map(coord -> coord.z).min(Double::compare).get() ;
		return new Point3D(minX, minY, minZ) ;	
	}
	public static Point3D calcMaxCoords(List<Point3D> coords)
	{
		double maxX = coords.stream().map(coord -> coord.x).max(Double::compare).get() ;
		double maxY = coords.stream().map(coord -> coord.y).max(Double::compare).get() ;
		double maxZ = coords.stream().map(coord -> coord.z).max(Double::compare).get() ;
		return new Point3D(maxX, maxY, maxZ) ;	
	}

	public void updateCenter() { center = calcCenter(coords) ;}
	public void updateMinCoords() { minCoords = calcMinCoords(coords) ;}
	public void updateMaxCoords() { minCoords = calcMaxCoords(coords) ;}

	public void addCoordFromMouseClick(Point3D newCoord)
	{
		
		if (coords == null || coords.isEmpty())
		{
			coords = new ArrayList<>() ;
    		coords.add(newCoord) ;
			return ;
		}

		switch(shape)
		{
			case rectangular:
			
				coords.add(new Point3D(coords.get(0).x, newCoord.y, 0.0)) ;
				coords.add(new Point3D(newCoord.x, newCoord.y, 0.0)) ;
				coords.add(new Point3D(newCoord.x, coords.get(0).y, 0.0)) ;
				
				return;

			case circular:

				Point3D center = coords.get(0) ;
				double r = Point3D.dist2D(center, newCoord) ;
				
				coords.get(0).translate(r, 0, 0); ;
				
				for (int node = 1; node <= NCirclePoints - 1; node += 1)
				{
					double angle = node * 2 * Math.PI / (double) NCirclePoints;
					coords.add(new Point3D(center.x + r * Math.cos(angle), center.y + r * Math.sin(angle), 0)) ;
				}

				return ;

			case polygonal:

				coords.add(newCoord) ;

				return ;				
		}

	}

	public List<Node> CreateCartesianNodes(int[] NumberElem, ElemType elemType)
	{
		double MinXCoord = Structure.calcMinCoords(coords).x ;
		double MinYCoord = Structure.calcMinCoords(coords).y ;
		double MaxXCoord = Structure.calcMaxCoords(coords).x ;
		double MaxYCoord = Structure.calcMaxCoords(coords).y ;

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
					Node[i*(NumberElem[0] + 1) + j] = new Node(i*(NumberElem[0] + 1) + j, new double[] {MinXCoord + j*dx, MinYCoord + i*dy, 0});
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
						Node[nodeID] = new Node(nodeID, new double[] {MinXCoord + j*dx, MinYCoord + i*dy, 0});
						nodeID += 1;
					}
				}
				else
				{
					for (int j = 0; j <= 2 * NumberElem[0] / 2; j += 1)
					{
						Node[nodeID] = new Node(nodeID, new double[] {MinXCoord + 2*j*dx, MinYCoord + i*dy, 0});
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
					Node[i*(2*NumberElem[0] + 1) + j] = new Node(i*(2*NumberElem[0] + 1) + j, new double[] {MinXCoord + j*dx, MinYCoord + i*dy, 0});
				}
			}
		}
		
		return Arrays.asList(Node);
	}

	public List<Node> CreateRadialNodes(int noffsets, int[] nintermediatepoints)
	{
		// Calculate number of nodes in each column
		Node[] nodes = null;
	    double[][] P2 = null;	    
	    for (int i = 0; i <= noffsets - 1; i += 1)
		{
		    double offset = i / (double) noffsets;
		    double[][] P1 = Util.CreateInternalPolygonPoints(coords, center, offset);
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
		    nodes[node] = new Node(node, P2[node]);
		}
	    nodes[P2.length] = new Node(P2.length, center, true);
	    return Arrays.asList(nodes);
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

	public static double[][] StructureStiffnessMatrix(int NFreeDOFs, List<Node> nodes, List<Element> elems, Supports[] sups, boolean nonlinearMat, boolean nonlinearGeo)
    {
        double[][] K = new double[NFreeDOFs][NFreeDOFs];
        		
        for (int elem = 0; elem <= elems.size() - 1; elem += 1)
        {
	        double[][] k = elems.get(elem).StiffnessMatrix(nodes, nonlinearMat, nonlinearGeo);
	        int LocalDOFi = 0, LocalDOFj = 0;
	        for (int elemnodei = 0; elemnodei <= elems.get(elem).getExternalNodes().length - 1; elemnodei += 1)
            {
                int nodei = elems.get(elem).getExternalNodes()[elemnodei];
                for (int elemnodej = 0; elemnodej <= elems.get(elem).getExternalNodes().length - 1; elemnodej += 1)
                {
                    int nodej = elems.get(elem).getExternalNodes()[elemnodej];
                    for (int dofi = 0; dofi <= nodes.get(nodei).getDOFType().length - 1; dofi += 1)
        	        {
        	            for (int dofj = 0; dofj <= nodes.get(nodej).getDOFType().length - 1; dofj += 1)
            	        {
        	            	int GlobalDOFi = nodes.get(nodei).dofs[dofi], GlobalDOFj = nodes.get(nodej).dofs[dofj];
            	            if (-1 < GlobalDOFi & -1 < GlobalDOFj)
            	            {
            	               K[GlobalDOFi][GlobalDOFj] += k[LocalDOFj][LocalDOFi];
            	            }
        	            	LocalDOFj += 1;
            	        }
    	            	LocalDOFi += 1;
    	            	LocalDOFj += -nodes.get(nodej).getDOFType().length;
        	        }
    	        	LocalDOFi += -nodes.get(nodei).getDOFType().length;
	            	LocalDOFj += nodes.get(nodej).getDOFType().length;
                }
	        	LocalDOFi += nodes.get(nodei).getDOFType().length;
                LocalDOFj = 0;
            }
        }
        return K;
    }

	private static Mesh CreateMesh(MeshType meshType, int[][] meshInfo, List<Node> nodes, List<Element> elems, ElemType elemType)
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
				nodes = MenuFunctions.Struct.CreateCartesianNodes(new int[] {noffsets, nintermediatepoints[0]}, elemType);
				elems = Structure.CreateCartesianMesh(MenuFunctions.Struct.getMesh().getNodes(), new int[] {noffsets, nintermediatepoints[0]}, elemType);
				break ;
				
			case radial:
				nodes = MenuFunctions.Struct.CreateRadialNodes(noffsets, nintermediatepoints);
				elems = Structure.CreateRadialMesh(MenuFunctions.Struct.getMesh().getNodes(), noffsets, elemType);
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

	public void createMesh(MeshType meshType, int[][] meshInfo, ElemType elemType)
	{
		mesh = new Mesh(new ArrayList<>(), new ArrayList<>()) ;
		mesh = CreateMesh(meshType, meshInfo, mesh.getNodes(), mesh.getElements(), elemType);
	}

	public String getName() {return name;}
	public StructureShape getShape() {return shape;}
	public List<Point3D> getCoords() {return coords;}
	public Point3D getCenter() {return center;}
	public Point3D getMinCoords() {return minCoords;}
	public Point3D getMaxCoords() {return maxCoords;}
	public Mesh getMesh() { return mesh ;}
	public double[][] getK() {return K;}
	public double[] getP() {return P;}
	public double[] getU() {return U;}
	public Results getResults() {return results;}
	public void setName(String N) {name = N;}
	public void setShape(StructureShape S) {shape = S;}
	public void setCoords(List<Point3D> C) {coords = C;}
	public void setCenter(Point3D C) {center = C;}
	public void setMinCoords(Point3D MinC) {minCoords = MinC;}
	public void setMaxCoords(Point3D MaxC) {maxCoords = MaxC;}
	public void setK(double[][] k) {K = k;}
	public void setP(double[] p) {P = p;}
	public void setU(double[] u) {U = u;}
	
	public int[][] NodeDOF(List<Node> Node, Supports[] Sup)
    {
        int[][] nodeDOF = new int[Node.size()][];
        boolean NodeHasSup;
        int supID;
        int cont = 0;
        for (int node = 0; node <= Node.size() - 1; node += 1)
    	{
        	nodeDOF[node] = new int[Node.get(node).getDOFType().length];
    	    NodeHasSup = false;
    	    supID = -1;
	        for (int sup = 0; sup <= Sup.length - 1; sup += 1)
    	    {
    	        if (Sup[sup].getNode() == node)
    	        {
    	            NodeHasSup = true;
    	            supID = sup;
    	        }
    	    }
    	    for (int dof = 0; dof <= Node.get(node).getDOFType().length - 1; dof += 1)
    	    {
        	    if (NodeHasSup)
        	    {
        	    	if (Node.get(node).getDOFType()[dof] <= Sup[supID].getDoFs().length - 1)
        	    	{
            	        if (Sup[supID].getDoFs()[Node.get(node).getDOFType()[dof]] == 0)
            	        {
            	            nodeDOF[node][dof] = cont;
            	            cont += 1;
            	        }
            	        else
            	        {
            	            nodeDOF[node][dof] = -1;
            	        }
        	    	}
        	        else	// option 1
        	        {
        	        	nodeDOF[node][dof] = -1;
        	        }
        	    }
        	    else
        	    {
        	        nodeDOF[node][dof] = cont;
        	        cont += 1;
        	    }
    	    }  
    	}
    	return nodeDOF;
    }

	@Override
	public String toString()
	{
		return "Structure [name=" + name + ", shape=" + shape + ", coords=" + coords + ", center=" + center
				+ ", minCoords=" + minCoords + ", maxCoords=" + maxCoords + ", K=" + Arrays.toString(K) + ", P="
				+ Arrays.toString(P) + ", U=" + Arrays.toString(U) + ", NFreeDOFs=" + NFreeDOFs + ", results=" + results
				+ "]";
	}
	
}
