package main.structure;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import main.gui.Menus;
import main.utilidades.Util;

public class Element
{
	private int ID;
	private ElemType type;			// Type of element
	private ElemShape Shape;
	
	
	private int[] DOFs;				// All DOFs present in the element
	private int[][] DOFsPerNode;	// DOFs in each node of the element
	
	
	private int[] ExternalNodes;	// Nodes on the contour (along the edges) in the counter-clockwise direction
	private int[] InternalNodes;	// Nodes inside the element
	private Material mat ;
	private Section sec;
	private DistLoads[] DistLoads;	// Distributed loads in the node
	
	
	private int[] StrainTypes;		// All strain types present in the element
	private double[] Disp;			// Displacements on the element
	private double[] Stress;		// Stresses on the element
	private double[] Strain;		// Strains on the element
	private double[] IntForces;		// External forces on the element
	
	public static Color color = Menus.palette[10];
	public static Color[] matColors;
	public static Color[] SecColors;
	
	private double[][] UndeformedCoords;
	private double[][] DeformedCoords;
	private double[] CenterCoords;
	private int[][] NodeDOF = null;
	private int[] CumDOFs = null;
	private Color MatColor;
	private Color SecColor;
	
	public Element(int ID, int[] ExternalNodes, int[] InternalNodes, Material mat, Section sec, ElemType type)
	{
		this.ID = ID;
		this.ExternalNodes = ExternalNodes;
		this.InternalNodes = InternalNodes;
		this.mat = mat;
		this.sec = sec;
		DistLoads = null;
		StrainTypes = null;
		Disp = null;
		Stress = null;
		Strain = null;
		IntForces = null;
		this.type = type;
		DefineProperties(type);
	}


	@Override
	public String toString()
	{
		return ID + "	" + type + "	" + ExternalNodes + "	" + mat + "	" + sec + "	" + Arrays.toString(DistLoads) ;
	}


	public int getID() {return ID;}
	public ElemShape getShape() {return Shape;}
	public int[] getDOFs() {return DOFs;}
	public int[][] getDOFsPerNode() {return DOFsPerNode;}
	public int[] getStrainTypes() {return StrainTypes;}
	public int[] getExternalNodes() {return ExternalNodes;}
	public int[] getInternalNodes() {return InternalNodes;}
	public Material getMat() {return mat;}
	public Section getSec() {return sec;}
	public DistLoads[] getDistLoads() {return DistLoads;}
	public double[] getDisp() {return Disp;}
	public double[] getStress() {return Stress;}
	public double[] getStrain() {return Strain;}
	public double[] getIntForces() {return IntForces;}
	public ElemType getType() {return type;}
	public int[][] getNodeDOF() {return NodeDOF;}
	public int[] getCumDOFs() {return CumDOFs;}
	public Color getMatColor () {return MatColor;}
	public Color getSecColor () {return SecColor;}
	public void setID(int I) {ID = I;}
	public void setShape(ElemShape S) {Shape = S;}
	public void setDOFs(int[] D) {DOFs = D;}
	public void setDOFsPerNode(int[][] D) {DOFsPerNode = D;}
	public void setStrainTypes(int[] ST) {StrainTypes = ST;}
	public void setExternalNodes(int[] N) {ExternalNodes = N;}
	public void setInternalNodes(int[] N) {InternalNodes = N;}
	public void setMat(Material M) {mat = M;}
	public void setSec(Section S) {sec = S;}
	public void setDistLoads(DistLoads[] D) {DistLoads = D;}
	public void setDisp(double[] D) {Disp = D;}
	public void setStress(double[] S) {Stress = S;}
	public void setStrain(double[] S) {Strain = S;}
	public void setIntForces(double[] I) {IntForces = I;}
	public void setType(ElemType T) {type = T;}
	public void setCumDOFs(int[] C) {CumDOFs = C;}
	public void setNodeDOF(int[][] N) {NodeDOF = N;}
	public void setMatColor (Color color) {MatColor = color;}
	public void setSecColor (Color color) {SecColor = color;}

	public void DefineProperties(ElemType type)
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

	public Object[] GetProp()
	{
		return new Object[] {Shape, DOFs, StrainTypes, DOFsPerNode};
	}

	public double[] calcHalfSize(List<Node> nodes)
	{
		double[] size = new double[2];
		if (Shape.equals(ElemShape.rectangular))
		{
			size[0] = Math.abs(nodes.get(ExternalNodes[2]).getOriginalCoords()[0] - nodes.get(ExternalNodes[0]).getOriginalCoords()[0]) / 2;
			size[1] = Math.abs(nodes.get(ExternalNodes[2]).getOriginalCoords()[1] - nodes.get(ExternalNodes[0]).getOriginalCoords()[1]) / 2;
		}
		else if (Shape.equals(ElemShape.r8) | Shape.equals(ElemShape.r9))
		{
			size[0] = Math.abs(nodes.get(ExternalNodes[4]).getOriginalCoords()[0] - nodes.get(ExternalNodes[0]).getOriginalCoords()[0]) / 2;
			size[1] = Math.abs(nodes.get(ExternalNodes[4]).getOriginalCoords()[1] - nodes.get(ExternalNodes[0]).getOriginalCoords()[1]) / 2;
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
	
	public double[][] getUndeformedCoords(){return UndeformedCoords;}
	
	public double[][] getDeformedCoords(){return DeformedCoords;}
	
	public double[] getCenterCoords(){return CenterCoords;}	
	
	public void setUndeformedCoords(List<Node> nodes)
	{
		UndeformedCoords = new double[ExternalNodes.length][];
		for (int i = 0; i <= ExternalNodes.length - 1; i += 1)
		{
			UndeformedCoords[i] = nodes.get(ExternalNodes[i]).getOriginalCoords();
    	}
	}

	public void setDeformedCoords(List<Node> nodes)
	{
		DeformedCoords = new double[ExternalNodes.length][];
		for (int node = 0; node <= ExternalNodes.length - 1; node += 1)
		{
			DeformedCoords[node] = Util.ScaledDefCoords(nodes.get(ExternalNodes[node]).getOriginalCoords(), nodes.get(ExternalNodes[node]).getDisp(), nodes.get(node).getDOFType(), 1);
    	}
	}
	
	public void setCenterCoords()
	{
		CenterCoords = new double[3];		
		for (int coord = 0; coord <= CenterCoords.length - 1; coord += 1)
		{
			for (int node = 0; node <= ExternalNodes.length - 1; node += 1)
			{					
				CenterCoords[coord] += UndeformedCoords[node][coord] / (double) ExternalNodes.length;
			}
		}
	}
	
	public static void createMatColors(List<Material> MaterialTypes)
	{
		matColors = Util.RandomColors(MaterialTypes.size());
	}

	public static void setSecColors(List<Section> SectionTypes)
	{
		SecColors = Util.RandomColors(SectionTypes.size());
	}	
	
	public double[][] NaturalCoordsShapeFunctions(double e, double n, List<Node> Node)
    {
    	// Calcula as funçõs de forma em um dado ponto (e, n). Unidades 1/mâ e 1/m.
    	double[][] N = null;
		double[] ElemSize = calcHalfSize(Node);
		double a = ElemSize[0];
		double b = ElemSize[1];	
		
		switch (type)
		{
			case KR1:
				N = new double[3][12];
	    		N[0][0] = (1 - e)*(1 - n)*(2 - e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][1] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][2] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;
	    		N[0][3] = (1 + e)*(1 - n)*(2 + e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][4] = (1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][5] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][6] = (1 + e)*(1 + n)*(2 + e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][7] = (1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][8] = (1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][9] = (1 - e)*(1 + n)*(2 - e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][10] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][11] = (1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;  		
	    		N[1][0] = -(3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][1] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 - n) / 8;
	    		N[1][2] = -(Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][3] = (3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][4] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 - n) / 8;
	    		N[1][5] = (Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][6] = (3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][7] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 + n) / 8;
	    		N[1][8] = (Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[1][9] = -(3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][10] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 + n) / 8;
	    		N[1][11] = -(Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[2][0] = -(3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][1] = -(Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][2] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 - e) / 8;		
	    		N[2][3] = -(3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][4] = -(Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][5] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 + e) / 8;
	    		N[2][6] = (3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][7] = (Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][8] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 + e) / 8;
	    		N[2][9] = (3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][10] = (Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][11] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 - e) / 8;
	    		break;
	    		
			case KR2:
	    		N = new double[4][16];
	    		N[0][0] = (2 - 3 * e + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][1] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][2] = (2 - 3 * e + Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][3] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][4] = (2 + 3 * e - Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][5] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][6] = (2 + 3 * e - Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][7] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][8] = (2 + 3 * e - Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][9] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][10] = (2 + 3 * e - Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][11] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][12] = (2 - 3 * e + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][13] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][14] = (2 - 3 * e + Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][15] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[1][0] = 3 * (-1 + Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / (16 * a);
				N[1][1] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / (16 * a);
				N[1][2] = 3 * (-1 + Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][3] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][4] = 3 * (1 - Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / (16 * a);
				N[1][5] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / (16 * a);
				N[1][6] = 3 * (1 - Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][7] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][8] = 3 * (1 - Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / (16 * a);
				N[1][9] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / (16 * a);
				N[1][10] = 3 * (1 - Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][11] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][12] = 3 * (-1 + Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / (16 * a);
				N[1][13] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / (16 * a);
				N[1][14] = 3 * (-1 + Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][15] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[2][0] = (2 - 3 * e + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / (16 * b);
				N[2][1] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / (16 * b);
				N[2][2] = (2 - 3 * e + Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][3] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][4] = (2 + 3 * e - Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / (16 * b);
				N[2][5] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / (16 * b);
				N[2][6] = (2 + 3 * e - Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][7] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][8] = (2 + 3 * e - Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / (16 * b);
				N[2][9] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / (16 * b);
				N[2][10] = (2 + 3 * e - Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][11] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][12] = (2 - 3 * e + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / (16 * b);
				N[2][13] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / (16 * b);
				N[2][14] = (2 - 3 * e + Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][15] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[3][0] = 9 * (1 - Math.pow(e, 2))*(1 - Math.pow(n, 2)) / (8 * a * b);
				N[3][1] = -3 * (1 - Math.pow(n, 2))*(-1 - 2 * e + 3 * Math.pow(e, 2)) / (8 * a * b);
				N[3][2] = -3 * (1 - Math.pow(e, 2))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][3] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][4] = -9 * (1 - Math.pow(e, 2))*(1 - Math.pow(n, 2)) / (8 * a * b);
				N[3][5] = -3 * (1 - Math.pow(n, 2))*(-1 + 2 * e + 3 * Math.pow(e, 2)) / (8 * a * b);
				N[3][6] = 3 * (1 - Math.pow(e, 2))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][7] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][8] = 9 * (1 - Math.pow(e, 2))*(1 - Math.pow(n, 2)) / (8 * a * b);
				N[3][9] = 3 * (1 - Math.pow(n, 2))*(-1 + 2 * e + 3 * Math.pow(e, 2)) / (8 * a * b);
				N[3][10] = 3 * (1 - Math.pow(e, 2))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][11] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][12] = -9 * (1 - Math.pow(e, 2))*(1 - Math.pow(n, 2)) / (8 * a * b);
				N[3][13] = 3 * (1 - Math.pow(n, 2))*(-1 - 2 * e + 3 * Math.pow(e, 2)) / (8 * a * b);
				N[3][14] = -3 * (1 - Math.pow(e, 2))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][15] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				break;
				
			case MR1, MR2:
	    		N = new double[3][12];
	    		N[0][0] = (1 - e)*(1 - n) / 4;
	    		N[0][3] = (1 + e)*(1 - n) / 4;
	    		N[0][6] = (1 + e)*(1 + n) / 4;
	    		N[0][9] = (1 - e)*(1 + n) / 4;
	    		N[1][1] = (1 - e)*(1 - n) / 4;
	    		N[1][4] = (1 + e)*(1 - n) / 4;
	    		N[1][7] = (1 + e)*(1 + n) / 4;
	    		N[1][10] = (1 - e)*(1 + n) / 4;
	    		N[2][2] = (1 - e)*(1 - n) / 4;
	    		N[2][5] = (1 + e)*(1 - n) / 4;
	    		N[2][8] = (1 + e)*(1 + n) / 4;
	    		N[2][11] = (1 - e)*(1 + n) / 4;
	    		break;
	    		
			case R4, Q4:
	    		N = new double[2][8];
	    		N[0][0] = (1 - e)*(1 - n) / 4;
	    		N[0][2] = (1 + e)*(1 - n) / 4;
	    		N[0][4] = (1 + e)*(1 + n) / 4;
	    		N[0][6] = (1 - e)*(1 + n) / 4;
	    		N[1][1] = (1 - e)*(1 - n) / 4;
	    		N[1][3] = (1 + e)*(1 - n) / 4;
	    		N[1][5] = (1 + e)*(1 + n) / 4;
	    		N[1][7] = (1 - e)*(1 + n) / 4;
	    		break;
	    		
			case T3G:
	    		N = new double[2][6];
	    		N[0][0] = e;
	    		N[0][2] = n;
	    		N[0][4] = 1 - e - n;
	    		N[1][1] = e;
	    		N[1][3] = n;
	    		N[1][5] = 1 - e - n;
	    		break;
	    		
			case T6G:
	    		N = new double[2][12];
	    		N[0][0] = e * (2 * e - 1);
	    		N[0][2] = n * (2 * n - 1);
	    		N[0][4] = (1 - e - n) * (2 * (1 - e - n) - 1);
	    		N[0][6] = 4 * n * (1 - e - n);
	    		N[0][8] = 4 * e * (1 - e - n);
	    		N[0][10] = 4*e*n;
	    		N[1][1] = e * (2 * e - 1);
	    		N[1][3] = n * (2 * n - 1);
	    		N[1][5] = (1 - e - n) * (2 * (1 - e - n) - 1);
	    		N[1][7] = 4 * n * (1 - e - n);
	    		N[1][9] = 4 * e * (1 - e - n);
	    		N[1][11] = 4*e*n;
	    		break;
	    		
			case SM:
	    		N = new double[5][20];
	    		N[0][0] = (1 - e)*(1 - n)*(2 - e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][1] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][2] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;
	    		N[0][5] = (1 + e)*(1 - n)*(2 + e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][6] = (1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][7] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][10] = (1 + e)*(1 + n)*(2 + e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][11] = (1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][12] = (1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][15] = (1 - e)*(1 + n)*(2 - e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][16] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][17] = (1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;
	    		N[1][0] = -(3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][1] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 - n) / 8;
	    		N[1][2] = -(Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][5] = (3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][6] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 - n) / 8;
	    		N[1][7] = (Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][10] = (3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][11] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 + n) / 8;
	    		N[1][12] = (Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[1][15] = -(3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][16] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 + n) / 8;
	    		N[1][17] = -(Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[2][0] = -(3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][1] = -(Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][2] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 - e) / 8;		
	    		N[2][5] = -(3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][6] = -(Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][7] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 + e) / 8;
	    		N[2][10] = (3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][11] = (Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][12] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 + e) / 8;
	    		N[2][15] = (3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][16] = (Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][17] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 - e) / 8;
	    		N[3][3] = (1 - e)*(1 - n) / 4;
	    		N[3][8] = (1 + e)*(1 - n) / 4;
	    		N[3][13] = (1 + e)*(1 + n) / 4;
	    		N[3][18] = (1 - e)*(1 + n) / 4;
	    		N[4][4] = (1 - e)*(1 - n) / 4;
	    		N[4][9] = (1 + e)*(1 - n) / 4;
	    		N[4][14] = (1 + e)*(1 + n) / 4;
	    		N[4][19] = (1 - e)*(1 + n) / 4;
	    		break;
	    		
			case SM8:
	    		N = new double[5][28];
	    		N[0][0] = (1 - e)*(1 - n)*(2 - e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][1] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][2] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;
	    		N[0][7] = (1 + e)*(1 - n)*(2 + e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][8] = (1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][9] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][14] = (1 + e)*(1 + n)*(2 + e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][15] = (1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][16] = (1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][21] = (1 - e)*(1 + n)*(2 - e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][22] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][23] = (1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;
	    		N[1][0] = -(3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][1] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 - n) / 8;
	    		N[1][2] = -(Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][7] = (3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][8] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 - n) / 8;
	    		N[1][9] = (Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][14] = (3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][15] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 + n) / 8;
	    		N[1][16] = (Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[1][21] = -(3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][22] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 + n) / 8;
	    		N[1][23] = -(Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[2][0] = -(3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][1] = -(Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][2] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 - e) / 8;		
	    		N[2][7] = -(3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][8] = -(Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][9] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 + e) / 8;
	    		N[2][14] = (3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][15] = (Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][16] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 + e) / 8;
	    		N[2][21] = (3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][22] = (Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][23] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 - e) / 8;
	    		N[3][3] = (1 - e)*(1 - n)*(-1 - e - n) / 4;
	    		N[3][5] = (1 - Math.pow(e, 2))*(1 - n) / 2;
	    		N[3][10] = (1 + e)*(1 - n)*(-1 + e - n) / 4;
	    		N[3][12] = (1 - Math.pow(e, 2))*(1 + n) / 2;
	    		N[3][17] = (1 + e)*(1 + n)*(-1 + e + n) / 4;
	    		N[3][19] = (1 - Math.pow(n, 2))*(1 + e) / 2;
	    		N[3][24] = (1 - e)*(1 + n)*(-1 - e + n) / 4;
	    		N[3][26] = (1 - Math.pow(n, 2))*(1 - e) / 2;
	    		N[4][4] = (1 - e)*(1 - n)*(-1 - e - n) / 4;
	    		N[4][6] = (1 - Math.pow(e, 2))*(1 - n) / 2;
	    		N[4][11] = (1 + e)*(1 - n)*(-1 + e - n) / 4;
	    		N[4][13] = (1 - Math.pow(e, 2))*(1 + n) / 2;
	    		N[4][18] = (1 + e)*(1 + n)*(-1 + e + n) / 4;
	    		N[4][20] = (1 - Math.pow(n, 2))*(1 + e) / 2;
	    		N[4][25] = (1 - e)*(1 + n)*(-1 - e + n) / 4;
	    		N[4][27] = (1 - Math.pow(n, 2))*(1 - e) / 2;
	    		break;
	    		
			case KP3:
	    		N = new double[4][12];
	    		N[0][0] = (2 - 3 * e + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][1] = a * (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][2] = b * (2 - 3 * e + Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][3] = (2 + 3 * e - Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][4] = a * (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][5] = b * (2 + 3 * e - Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][6] = (2 + 3 * e - Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][7] = a * (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][8] = b * (2 + 3 * e - Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][9] = (2 - 3 * e + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][10] = a * (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][11] = b * (2 - 3 * e + Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;		
				N[1][0] = 3 * (-1 + Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[1][1] = a * (-1 - 2 * e + 3 * Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[1][2] = b * 3 * (-1 + Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[1][3] = 3 * (1 - Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[1][4] = a * (-1 + 2 * e + 3 * Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[1][5] = b * 3 * (1 - Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[1][6] = 3 * (1 - Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[1][7] = a * (-1 + 2 * e + 3 * Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[1][8] = b * 3 * (1 - Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[1][9] = 3 * (-1 + Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[1][10] = a * (-1 - 2 * e + 3 * Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[1][11] = b * 3 * (-1 + Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[2][0] = (2 - 3 * e + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / 16;
				N[2][1] = a * (1 - e - Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / 16;
				N[2][2] = b * (2 - 3 * e + Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / 16;
				N[2][3] = (2 + 3 * e - Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / 16;
				N[2][4] = a * (-1 - e + Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / 16;
				N[2][5] = b * (2 + 3 * e - Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / 16;
				N[2][6] = (2 + 3 * e - Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / 16;
				N[2][7] = a * (-1 - e + Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / 16;
				N[2][8] = b * (2 + 3 * e - Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / 16;
				N[2][9] = (2 - 3 * e + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / 16;
				N[2][10] = a * (1 - e - Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / 16;
				N[2][11] = b * (2 - 3 * e + Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / 16;
				break;
				
			case SM_C:
				N = new double[5][24];
	    		N[0] = new double[] {((e*e*e-3*e+2)*(n*n*n-3*n+2))/16,	((e*e*e-e*e-e+1)*(n*n*n-3*n+2))/16,	((e*e*e-3*e+2)*(n*n*n-n*n-n+1))/16,	((e*e*e-e*e-e+1)*(n*n*n-n*n-n+1))/16,	0,	0,	((-e*e*e+3*e+2)*(n*n*n-3*n+2))/16,	((e*e*e+e*e-e-1)*(n*n*n-3*n+2))/16,	((-e*e*e+3*e+2)*(n*n*n-n*n-n+1))/16,	((e*e*e+e*e-e-1)*(n*n*n-n*n-n+1))/16,	0,	0,	((-e*e*e+3*e+2)*(-n*n*n+3*n+2))/16,	((e*e*e+e*e-e-1)*(-n*n*n+3*n+2))/16,	((-e*e*e+3*e+2)*(n*n*n+n*n-n-1))/16,	((e*e*e+e*e-e-1)*(n*n*n+n*n-n-1))/16,	0,	0,	((e*e*e-3*e+2)*(-n*n*n+3*n+2))/16,	((e*e*e-e*e-e+1)*(-n*n*n+3*n+2))/16,	((e*e*e-3*e+2)*(n*n*n+n*n-n-1))/16,	((e*e*e-e*e-e+1)*(n*n*n+n*n-n-1))/16,	0,	0} ;
	    		N[1] = new double[] {((3*e*e-3)*(n*n*n-3*n+2))/(16*a),	((3*e*e-2*e-1)*(n*n*n-3*n+2))/(16*a),	((3*e*e-3)*(n*n*n-n*n-n+1))/(16*a),	((3*e*e-2*e-1)*(n*n*n-n*n-n+1))/(16*a),	0,	0,	((3-3*e*e)*(n*n*n-3*n+2))/(16*a),	((3*e*e+2*e-1)*(n*n*n-3*n+2))/(16*a),	((3-3*e*e)*(n*n*n-n*n-n+1))/(16*a),	((3*e*e+2*e-1)*(n*n*n-n*n-n+1))/(16*a),	0,	0,	((3-3*e*e)*(-n*n*n+3*n+2))/(16*a),	((3*e*e+2*e-1)*(-n*n*n+3*n+2))/(16*a),	((3-3*e*e)*(n*n*n+n*n-n-1))/(16*a),	((3*e*e+2*e-1)*(n*n*n+n*n-n-1))/(16*a),	0,	0,	((3*e*e-3)*(-n*n*n+3*n+2))/(16*a),	((3*e*e-2*e-1)*(-n*n*n+3*n+2))/(16*a),	((3*e*e-3)*(n*n*n+n*n-n-1))/(16*a),	((3*e*e-2*e-1)*(n*n*n+n*n-n-1))/(16*a),	0,	0} ;
				N[2] = new double[] {((e*e*e-3*e+2)*(3*n*n-3))/(16*b),	((e*e*e-e*e-e+1)*(3*n*n-3))/(16*b),	((e*e*e-3*e+2)*(3*n*n-2*n-1))/(16*b),	((e*e*e-e*e-e+1)*(3*n*n-2*n-1))/(16*b),	0,	0,	((-e*e*e+3*e+2)*(3*n*n-3))/(16*b),	((e*e*e+e*e-e-1)*(3*n*n-3))/(16*b),	((-e*e*e+3*e+2)*(3*n*n-2*n-1))/(16*b),	((e*e*e+e*e-e-1)*(3*n*n-2*n-1))/(16*b),	0,	0,	((-e*e*e+3*e+2)*(3-3*n*n))/(16*b),	((e*e*e+e*e-e-1)*(3-3*n*n))/(16*b),	((-e*e*e+3*e+2)*(3*n*n+2*n-1))/(16*b),	((e*e*e+e*e-e-1)*(3*n*n+2*n-1))/(16*b),	0,	0,	((e*e*e-3*e+2)*(3-3*n*n))/(16*b),	((e*e*e-e*e-e+1)*(3-3*n*n))/(16*b),	((e*e*e-3*e+2)*(3*n*n+2*n-1))/(16*b),	((e*e*e-e*e-e+1)*(3*n*n+2*n-1))/(16*b),	0,	0} ;
				N[3] = new double[] {0,	0,	0,	0,	((1-e)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(n+1))/4,	0,	0,	0,	0,	0,	((1-e)*(n+1))/4,	0} ;
				N[4] = new double[] {0,	0,	0,	0,	0,	((1-e)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(n+1))/4,	0,	0,	0,	0,	0,	((1-e)*(n+1))/4} ;	
				break;
				
			case SM_H:
				break;
				
		}
    	
    	return N;
    }

	public double[][] RealCoordsShapeFunctions(double[][] NodesCoords, double[] PointCoords)
    {
    	// Calcula as funçõs de forma de um elemento dadas as suas coordenadas reais. Unidades 1/mâ e 1/m.
    	double[][] N = null;
    	if (type.equals(ElemType.T3G))
    	{
    		N = new double[2][6];
    	    double A = Util.TriArea(NodesCoords);
    	    double x1 = NodesCoords[0][0], x2 = NodesCoords[1][0], x3 = NodesCoords[2][0];
    	    double y1 = NodesCoords[0][1], y2 = NodesCoords[1][1], y3 = NodesCoords[2][1];
    	    double x = PointCoords[0];
    	    double y = PointCoords[1];
    	    double[] alfa = new double[] {x2 * y3 - x3 * y2, x3 * y1 - x1 * y3, x1 * y2 - y1 * x2};
    	    double[] beta = new double[] {y2 - y3, y3 - y1, y1 - y2};
    	    double[] gama = new double[] {x3 - x2, x1 - x3, x2 - x1};
    		double Ni = 1 / (2 * A) * (alfa[0] + beta[0] * x + gama[0] * y);
    		double Nj = 1 / (2 * A) * (alfa[1] + beta[1] * x + gama[1] * y);
    		double Nm = 1 / (2 * A) * (alfa[2] + beta[2] * x + gama[2] * y);
    		N[0][0] = Ni;
    		N[0][2] = Nj;
    		N[0][4] = Nm;
    		N[1][1] = Ni;
    		N[1][3] = Nj;
    		N[1][5] = Nm;
    	}
    	
    	return N;
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
		    	
	    		int[] Nodes = ExternalNodes;
	    		double[][] Coords = new double[Nodes.length][];
	    		for (int node = 0; node <= Nodes.length - 1; node += 1)
	    		{
	    			Coords[node] = new double[nodes.get(Nodes[node]).getOriginalCoords().length];
	    			for (int coord = 0; coord <= nodes.get(Nodes[node]).getOriginalCoords().length - 1; coord += 1)
	        		{
	    				if (NonlinearGeo)
	    				{
	            			Coords[node][coord] = nodes.get(Nodes[node]).getOriginalCoords()[coord] + nodes.get(Nodes[node]).getDisp()[coord];
	    				}
	    				else
	    				{
	            			Coords[node][coord] = nodes.get(Nodes[node]).getOriginalCoords()[coord];
	    				}
	        		}
	    		}
	    		
	    	    double x1 = Coords[0][0], x2 = Coords[1][0], x3 = Coords[2][0];
	    	    double y1 = Coords[0][1], y2 = Coords[1][1], y3 = Coords[2][1];
	    	    double A = Util.TriArea(Coords);
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
    	
    	if (option == 1 & Bs1 != null)
    	{
    		return Bs1 ;
    	}
    	else if (option == 2 & Bs2 != null)
    	{
    		return Bs2 ;
    	}
    	else if (Bs1 != null & Bs2 != null)
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
       
    public double[][] StiffnessMatrix(List<Node> nodes, boolean NonlinearMat, boolean NonlinearGeo)
    {
    	// Calcula a matriz de rigidez do elemento
    	double[][] k = null;
		double[] ElemSize = calcHalfSize(nodes);
		double[][] Db = BendingConstitutiveMatrix(mat, NonlinearMat, Strain);
		double[][] Ds = ShearConstitutiveMatrix2(mat, sec);
		
		switch (type)
		{
			case KR1: k = ElementStiffnessMatrix.KR1StiffnessMatrix(ElemSize, Db, sec); break ;
			case KR2: k = ElementStiffnessMatrix.KR2StiffnessMatrix(ElemSize, Db, sec); break ;
			case MR1: k = ElementStiffnessMatrix.MR1StiffnessMatrix(ElemSize, Db, Ds, sec); break ;
			case MR2: k = ElementStiffnessMatrix.MR2StiffnessMatrix(ElemSize, Db, Ds, sec); break ;
			case R4: k = ElementStiffnessMatrix.R4StiffnessMatrix(ElemSize, Db, sec); break ;
			case Q4: k = ElementStiffnessMatrix.Q4StiffnessMatrix(nodes, ExternalNodes, ElemSize, Db, sec, NonlinearGeo); break ;
			case T3G: 
				
	        	double[][] D = BendingConstitutiveMatrix(mat, NonlinearMat, Strain);
	        	double thick = sec.getT() / 1000.0;
				double Area;
	    		if (!NonlinearGeo)
	        	{
	            	double[][] Coords = new double[ExternalNodes.length][];
	    			for (int node = 0; node <= ExternalNodes.length - 1; node += 1)
	    			{
		    			Coords[node] = new double[nodes.get(ExternalNodes[node]).getOriginalCoords().length];
		    			for (int coord = 0; coord <= nodes.get(ExternalNodes[node]).getOriginalCoords().length - 1; coord += 1)
		        		{
		            		Coords[node][coord] = nodes.get(ExternalNodes[node]).getOriginalCoords()[coord];
		        		}
	    			}     	
	    			Area = Util.TriArea(Coords);
	        	}
	    		else
	        	{
	        		double[][] DefCoords = Util.GetElemNodesDefPos(nodes, ExternalNodes);
	        		Area = Util.TriArea(DefCoords);
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
	    	    
			case SM: k = ElementStiffnessMatrix.SMStiffnessMatrix(ElemSize, Db, Ds, sec); break;
    		
    		/*double[][] kb = NumIntegration(Node, Elem, UserDefinedMat[Elem.getMat()], UserDefinedSec[Elem.getSec()], DOFsPerNode, NonlinearMat, NonlinearGeo, strain, 3);
    		double[][] ks = NumIntegration(Node, Elem, UserDefinedMat[Elem.getMat()], UserDefinedSec[Elem.getSec()], DOFsPerNode, NonlinearMat, NonlinearGeo, strain, 3);
    		k = Util.AddMatrix(kb,  ks);*/
			case SM8: k = ElementStiffnessMatrix.SM8StiffnessMatrix(ElemSize, Db, Ds, sec); break;
			case KP3: k = ElementStiffnessMatrix.KP3StiffnessMatrix(ElemSize, Db, sec); break;
			case SM_C: k = ElementStiffnessMatrix.SM_CStiffnessMatrix(ElemSize, Db, Ds, sec); break;
			case SM_H: k = ElementStiffnessMatrix.SM_HStiffnessMatrix(ElemSize, Db, Ds, sec); break;
			
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
		for (int i = 0; i <= ExternalNodes.length - 1; i += 1)
		{
			int node = ExternalNodes[i];
			ulength += nodes.get(node).getDOFType().length;
		}
		
		double[] u = new double[ulength];
		for (int elemnode = 0; elemnode <= ExternalNodes.length - 1; elemnode += 1)
    	{
			int node = ExternalNodes[elemnode];
			for (int dof = 0; dof <= nodes.get(node).getDOFType().length - 1; dof += 1)
	    	{
				if (-1 < NodeDOF[elemnode][dof])
				{
					u[CumDOFs[elemnode] + dof] = U[NodeDOF[elemnode][dof]];
				}
				else
				{
					u[CumDOFs[elemnode] + dof] = 0;
				}
	    	}
    	}
		return u;
	}
	
	public double[] ForceVec(List<Node> Node, boolean NonlinearMat, boolean NonlinearGeo, double[] U)
	{
		int Nnodes = ExternalNodes.length;
		int[] ElemDOFs = DOFs;
		double[] p = new double[Nnodes * ElemDOFs.length];
		
		double[][] k = StiffnessMatrix(Node, NonlinearMat, NonlinearGeo);
		double[] u = DispVec(Node, U);

		p = Util.MultMatrixVector(k, u);
		
		return p;
	}
	
	public double[] StrainVec(List<Node> Node, double[] U, boolean NonlinearGeo)
	{
		int Nnodes = ExternalNodes.length;
		float[] e = null, n = null;
		double[][] strains = new double[Nnodes][];
		double[] strainvec = new double[Nnodes * StrainTypes.length];
		
		if (ExternalNodes.length == 4)
		{
			e = new float[] {-1, 1, 1, -1};
			n = new float[] {-1, -1, 1, 1};
		}
		else if (ExternalNodes.length == 8)
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
		int Nnodes = ExternalNodes.length;
		float[] e = null, n = null;
		double[] sigmavec = new double[Nnodes * StrainTypes.length];

		if (ExternalNodes.length == 4)
		{
			e = new float[] {-1, 1, 1, -1};
			n = new float[] {-1, -1, 1, 1};
		}
		else if (ExternalNodes.length == 8)
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
		int Nnodes = ExternalNodes.length;
		float[] e = null, n = null;
		int[] StrainsOnElem = StrainTypes;
		double[] forcevec = new double[Nnodes * StrainTypes.length];
    	double[][] forces = new double[Nnodes][];

		if (ExternalNodes.length == 4)
		{
			e = new float[] {-1, 1, 1, -1};
			n = new float[] {-1, -1, 1, 1};
		}
		else if (ExternalNodes.length == 8)
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
}
