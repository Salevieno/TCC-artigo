package org.example.structure;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.loading.Loading;
import org.example.mainTCC.MenuFunctions;
import org.example.output.Results;
import org.example.userInterface.Menus;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

public class Structure
{
	private String name;
	private StructureShape shape;
	private List<Point3D> coords;	// Structure edge coordinates [x, y, z]
	private Point3D center;
	private Point3D minCoords;
	private Point3D maxCoords;

	private Mesh mesh ;
	private List<Supports> supports;
	public static Reactions[] Reaction;

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
	public void updateMaxCoords() { maxCoords = calcMaxCoords(coords) ;}

	public static Structure create(List<Point3D> coords, MeshType meshType, int[] meshSizes, ElemType elemType,
			Material currentMatType, List<Material> matTypes,
			Section currentSecType, List<Section> secTypes,
			int supConfig)
	{
		/* 1. Criar polígono */
		Structure structure = new Structure("Especial", StructureShape.rectangular, coords);
		
		/* 2. Criar malha */		
		structure.createMesh(meshType, new int[][] {meshSizes}, elemType);
		
		/* 3. Atribuir materiais */
		structure.getMesh().getElements().forEach(elem -> elem.setMat(currentMatType)) ;
		Element.createMatColors(matTypes);
		for (Element elem : structure.getMesh().getElements())
		{
			int matColorID = matTypes.indexOf(elem.getMat()) ;
			elem.setMatColor(Element.matColors[matColorID]);
		}

		/* 4. Atribuir seções */		
		structure.getMesh().getElements().forEach(elem -> elem.setSec(currentSecType)) ;
		Element.setSecColors(secTypes);
		for (Element elem : structure.getMesh().getElements())
		{
			int secID = secTypes.indexOf(elem.getSec()) ;
			elem.setSecColor(Element.SecColors[secID]);
		}
		
		/* 5. Atribuir apoios */
		Supports[] supports = Util.AddEspecialSupports(structure.getMesh().getNodes(), Element.typeToShape(elemType), meshType, new int[] {meshSizes[0], meshSizes[1]}, supConfig);
		for (Supports sup : supports)
		{
			structure.addSupport(sup);
		}

		return structure ;
	}

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

	public static double[][] StructureStiffnessMatrix(int NFreeDOFs, List<Node> nodes, List<Element> elems, boolean nonlinearMat, boolean nonlinearGeo)
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


	public void createMesh(MeshType meshType, int[][] meshInfo, ElemType elemType)
	{
		resetMesh() ;
		mesh = Mesh.CreateMesh(coords, center, meshType, meshInfo, mesh.getNodes(), mesh.getElements(), elemType);
	}

	public void resetMesh()
	{
		mesh = new Mesh(new ArrayList<>(), new ArrayList<>()) ;
	}

	public void printStructure(List<Material> mats, List<Section> secs, List<Supports> sups, Loading loading)
	{
		System.out.println(" *** Structure information ***");
		System.out.println(name);
		if (mesh != null)
		{
			mesh.print() ;
		}
		if (mats != null)
		{			
			System.out.println();
			System.out.println("Mats");
			System.out.println("E (GPa)	v");
			mats.forEach(System.out::println);
		}
		if (secs != null)
		{			
			System.out.println();
			System.out.println("Secs");
			System.out.println("thick (mm)");
			secs.forEach(System.out::println);
		}
		if (sups != null)
		{
			System.out.println();
			System.out.println("Sups");
			System.out.println("Id	Node	DoFs (Fx Fy Fz Mx My Mz)");
			sups.forEach(System.out::println);
		}
		if (MainPanel.loading.getConcLoads() != null)
		{
			System.out.println();
			System.out.println("ConcLoads");
			System.out.println("Id	Node	Loads Fx Fy Fz Mx My Mz (kN)");
			MainPanel.loading.getConcLoads().forEach(System.out::println);
		}
		if (MainPanel.loading.getDistLoads() != null)
		{
			System.out.println();
			System.out.println("DistLoads");
			System.out.println("Id	Elem type	Intensity (kN/m)");
			MainPanel.loading.getDistLoads().forEach(System.out::println);
		}
		if (MainPanel.loading.getNodalDisps() != null)
		{
			System.out.println();
			System.out.println("NodalDisps");
			System.out.println("Id	Node	Disp ux uy uz thetax thetay thetaz (m)");
			MainPanel.loading.getNodalDisps().forEach(System.out::println);
		}
	}

	public void addSupport(Supports sup)
	{
		if (supports == null)
		{
			supports = new ArrayList<>() ;
		}
		supports.add(sup) ;
	}
	
	public void removeSupports() { supports = null ;}

	public String getName() {return name;}
	public StructureShape getShape() {return shape;}
	public List<Point3D> getCoords() {return coords;}
	public Point3D getCenter() {return center;}
	public Point3D getMinCoords() {return minCoords;}
	public Point3D getMaxCoords() {return maxCoords;}
	public Mesh getMesh() { return mesh ;}
	public List<Supports> getSupports() {return supports;}
	public double[][] getK() {return K;}
	public double[] getP() {return P;}
	public double[] getU() {return U;}
	public Results getResults() {return results;}
	public Reactions[] getReactions() {return Reaction;}
	public void setName(String N) {name = N;}
	public void setShape(StructureShape S) {shape = S;}
	public void setCoords(List<Point3D> C) {coords = C;}
	public void setK(double[][] k) {K = k;}
	public void setP(double[] p) {P = p;}
	public void setU(double[] u) {U = u;}
	public void setResults(Results R) {results = R;}
	public void setReactions(Reactions[] R) {Reaction = R;}

	public void assignLoads(int ConcLoadConfig, int[] MeshSizes, int concLoadsID, int distLoadID)
	{

		if (ConcLoadConfig == 1 && concLoadsID != -1)
		{
			List<Node> nodesToReceiveLoads = new ArrayList<Node>();
			if (mesh.getElements().get(0).getShape().equals(ElemShape.rectangular))
			{
				int nodeID = (MeshSizes[1] / 2 * (MeshSizes[0] + 1) + MeshSizes[0] / 2) ;
				nodesToReceiveLoads.add(mesh.getNodes().get(nodeID));
			}
			else if (mesh.getElements().get(0).getShape().equals(ElemShape.r8))
			{
				int nodeID = (MeshSizes[1] / 2 * (2 * MeshSizes[0] + 1 + MeshSizes[0] + 1) + MeshSizes[0]) ;
				nodesToReceiveLoads.add(mesh.getNodes().get(nodeID));
			}
		
			
			if (nodesToReceiveLoads != null)
			{
				nodesToReceiveLoads.forEach(node ->
				{
					// int loadid = loading.getConcLoads().size() - MenuFunctions.selectedNodes.size() + i;
					ConcLoads concLoad = new ConcLoads(1, node, MenuFunctions.ConcLoadType[concLoadsID]) ;
					if (concLoad != null)
					{
						node.addConcLoad(concLoad) ;
					}
				}) ;
			}
		}

		if (mesh != null && mesh.getElements() != null && distLoadID != -1)
		{
			mesh.getElements().forEach(elem -> 
			{
				
				int distLoadType = (int) MenuFunctions.DistLoadType[distLoadID][0] ;
				double distLoadIntensity = MenuFunctions.DistLoadType[distLoadID][1] ;
				DistLoads distLoad = new DistLoads(1, elem.getID(), distLoadType, distLoadIntensity) ;
				if (distLoad != null)
				{
					elem.addDistLoad(distLoad) ;
				}
			}) ;
		}

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
