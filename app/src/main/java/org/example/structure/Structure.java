package org.example.structure;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.Main;
import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.Loading;
import org.example.mainTCC.MainPanel;
import org.example.output.Diagram;
import org.example.output.ResultDiagrams;
import org.example.output.Results;
import org.example.service.MenuViewService;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;

import graphics.DrawPrimitives;

public class Structure
{
	private String name;
	private StructureShape shape;
	private List<Point3D> coords;
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
	private ResultDiagrams resultDiagrams ;
	
	private static final int NCirclePoints = 20;
	public static final Color color = Main.palette[5];
	
	public Structure()
	{

	}

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
		resultDiagrams = new ResultDiagrams() ;
	}
	
	public Structure(StructureDTO dto)
	{
		this.name = dto.getName() ;
		this.shape = dto.getShape() ;
		this.coords = dto.getCoords() ;
		if (coords != null && !coords.isEmpty())
		{
			center = calcCenter(coords);
			minCoords = calcMinCoords(coords);
			maxCoords = calcMaxCoords(coords);
		}
		this.mesh = new Mesh(dto.getMeshDTO()) ;
		this.supports = dto.getSupports() ;
		this.results = dto.getResults() ;
		resultDiagrams = new ResultDiagrams() ;
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

	public void calcAndAssignDOFs()
	{
		NFreeDOFs = -1;
		for (Node node : mesh.getNodes())
		{
			node.setDOFType(mesh.defineDOFsOnNode());
			node.calcdofs(getSupports(), NFreeDOFs + 1);
			for (int dof = 0; dof <= node.getDofs().length - 1; dof += 1)
	        {
				if (-1 < node.getDofs()[dof])
				{
					NFreeDOFs = node.getDofs()[dof];
				}
	        }
			node.resetLoadDispCurve();
		}
		NFreeDOFs += 1;
	}

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
		Element.createRandomMatColors(matTypes);
		for (Element elem : structure.getMesh().getElements())
		{
			int matColorID = matTypes.indexOf(elem.getMat()) ;
			// elem.setMatColor(Element.matColors[matColorID]);
		}

		/* 4. Atribuir seções */		
		structure.getMesh().getElements().forEach(elem -> elem.setSec(currentSecType)) ;
		Element.createRandomSecColors(secTypes);
		for (Element elem : structure.getMesh().getElements())
		{
			int secID = secTypes.indexOf(elem.getSec()) ;
			// elem.setSecColor(Element.SecColors[secID]);
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

			if (k == null) { System.out.println("Error: Elem stiffness matrix null while calculating structure stiffness matrix") ; return null ;}
	        
			int LocalDOFi = 0, LocalDOFj = 0;
	        for (int elemnodei = 0; elemnodei <= elems.get(elem).getExternalNodes().size() - 1; elemnodei += 1)
            {
                int nodei = elems.get(elem).getExternalNodes().get(elemnodei).getID();
                for (int elemnodej = 0; elemnodej <= elems.get(elem).getExternalNodes().size() - 1; elemnodej += 1)
                {
                    int nodej = elems.get(elem).getExternalNodes().get(elemnodej).getID();
                    for (int dofi = 0; dofi <= nodes.get(nodei).getDOFType().length - 1; dofi += 1)
        	        {
        	            for (int dofj = 0; dofj <= nodes.get(nodej).getDOFType().length - 1; dofj += 1)
            	        {
        	            	int GlobalDOFi = nodes.get(nodei).getDofs()[dofi] ;
							int GlobalDOFj = nodes.get(nodej).getDofs()[dofj] ;
            	            if (-1 < GlobalDOFi && -1 < GlobalDOFj)
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

	public void createMesh(MeshType meshType, int nOffsets, int qtdIntermediatePoints, ElemType elemType)
	{
		resetMesh() ;
		mesh = Mesh.CreateMesh(coords, center, meshType, nOffsets, qtdIntermediatePoints, mesh.getNodes(), mesh.getElements(), elemType);
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
		if (MainPanel.getInstance().getCentralPanel().getLoading().getConcLoads() != null)
		{
			System.out.println();
			System.out.println("ConcLoads");
			System.out.println("Id	Node	Loads Fx Fy Fz Mx My Mz (kN)");
			MainPanel.getInstance().getCentralPanel().getLoading().getConcLoads().forEach(System.out::println);
		}
		if (MainPanel.getInstance().getCentralPanel().getLoading().getDistLoads() != null)
		{
			System.out.println();
			System.out.println("DistLoads");
			System.out.println("Id	Elem type	Intensity (kN/m)");
			MainPanel.getInstance().getCentralPanel().getLoading().getDistLoads().forEach(System.out::println);
		}
		if (MainPanel.getInstance().getCentralPanel().getLoading().getNodalDisps() != null)
		{
			System.out.println();
			System.out.println("NodalDisps");
			System.out.println("Id	Node	Disp ux uy uz thetax thetay thetaz (m)");
			MainPanel.getInstance().getCentralPanel().getLoading().getNodalDisps().forEach(System.out::println);
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

	public void updateDrawings(MyCanvas canvas)
	{
		if (mesh == null) { return ;}

		mesh.getNodes().forEach(node -> node.updateDrawingPos(canvas, false, 1.0)) ;
	}

	public void displayShape(MyCanvas canvas, DrawPrimitives DP)
	{		
		int countourStroke = 2;
		List<Point> drawingCoords = new ArrayList<>() ;

		for (Point3D coord : coords)
		{
			Point drawingCoord = canvas.inDrawingCoords(new Point2D.Double(coord.x, coord.y)) ;
			drawingCoords.add(drawingCoord) ;
		}

		DP.drawPolygon(drawingCoords, countourStroke, color);
		DP.drawPolyLine(drawingCoords, countourStroke, color);
	}

	public void displayMesh(MyCanvas canvas, double Defscale, boolean showmatcolor, boolean showseccolor, boolean showcontour, boolean showdeformed, DrawPrimitives DP)
	{
		mesh.display(canvas, Defscale, showmatcolor, showseccolor, showcontour, showdeformed, DP) ;
	}

	public void displaySupports(MyCanvas canvas, DrawPrimitives DP)
	{
		supports.forEach(sup -> sup.dispaly(canvas, DP)) ;
	}

	public void displayConcLoads(MyCanvas canvas, boolean deformed, DrawPrimitives DP)
	{
		double maxLoad = Util.FindMaxConcLoad(MainPanel.getInstance().getCentralPanel().getLoading().getConcLoads());
		int[] dofs = mesh.getElements().get(0).getDOFs() ;
		for (Node node : mesh.getNodes())
		{
			if (node.getConcLoads() == null) { continue ;}
			node.displayConcLoads(dofs, MenuViewService.getInstance().loadsValues, maxLoad, deformed, MainPanel.getInstance().getCentralPanel().getDiagramScales().y, canvas, DP) ;
			// for (ConcLoad concLoad : node.getConcLoads())
			// {
			// 	int[] dofs = mesh.getElements().get(0).getDOFs() ;
			// 	concLoad.display(dofs, MenuFunctions.ShowLoadsValues, maxLoad, deformed, MenuFunctions.DiagramScales[1], canvas, DP) ;
			// }
		}
	}

	public void displayDiagrams(MyCanvas canvas, Diagram diagram, int selectedVar, DrawPrimitives DP)
	{
		if (resultDiagrams == null) { return ;}

		resultDiagrams.setDiagram(diagram) ;
		resultDiagrams.setSelectedVar(selectedVar) ;
		resultDiagrams.display(canvas, mesh, results, DP) ;
	}

	public void displayDiagrams(MyCanvas canvas, DrawPrimitives DP)
	{
		if (resultDiagrams == null) { return ;}

		resultDiagrams.display(canvas, mesh, results, DP) ;
	}

	public void display(MyCanvas canvas, boolean showmatcolor, boolean showseccolor, boolean showcontour, boolean showdeformed, DrawPrimitives DP)
	{
		if (mesh == null && coords != null && !coords.isEmpty())
		{
			displayShape(canvas, DP) ;
		}
		if (mesh != null)
		{
			mesh.display(canvas, NCirclePoints, showmatcolor, showseccolor, showcontour, showdeformed, DP) ;
		}
	}


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
	public ResultDiagrams getResultDiagrams() { return resultDiagrams ;}
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
					ConcLoad concLoad = new ConcLoad(ConcLoad.getTypes().get(concLoadsID)) ;
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
				
				int distLoadType = (int) (double) DistLoad.getTypes().get(distLoadID)[0] ;
				double distLoadIntensity = DistLoad.getTypes().get(distLoadID)[1] ;
				DistLoad distLoad = new DistLoad(distLoadType, distLoadIntensity) ;
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
