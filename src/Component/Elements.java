package Component;

import java.awt.Color;
import java.util.Arrays;

import Main.ElementStiffnessMatrix;
import Utilidades.Util;

public class Elements
{
	private int ID;
	private String Shape;
	private int[] DOFs;				// All DOFs present in the element
	private int[][] DOFsPerNode;	// DOFs in each node of the element
	private int[] ExternalNodes;	// Nodes on the contour (along the edges) in the counter-clockwise direction
	private int[] InternalNodes;	// Nodes inside the element
	private double[] Mat;			// Material
	private double[] Sec;			// Section
	private DistLoads[] DistLoads;	// Distributed loads in the node
	private int[] StrainTypes;		// All strain types present in the element
	private double[] Disp;			// Displacements on the element
	private double[] Stress;		// Stresses on the element
	private double[] Strain;		// Strains on the element
	private double[] IntForces;		// External forces on the element
	private String Type;			// Type of element
	
	private static String[] ElemTypes = new String[] {"KR1", "KR2", "MR1", "MR2", "R4", "Q4", "T3G", "T6G", "SM", "SM8", "KP3", "SM_C", "SM_H"};
	public static Color color = Util.ColorPalette()[10];
	public static Color[] MatColors;
	public static Color[] SecColors;
	
	private double[][] UndeformedCoords;
	private double[][] DeformedCoords;
	private double[] CenterCoords;
	private int[][] NodeDOF = null;
	private int[] CumDOFs = null;
	private Color MatColor;
	private Color SecColor;
	
	public Elements(int ID, int[] ExternalNodes, int[] InternalNodes, double[] Mat, double[] Sec, String Type)
	{
		this.ID = ID;
		this.ExternalNodes = ExternalNodes;
		this.InternalNodes = InternalNodes;
		this.Mat = Mat;
		this.Sec = Sec;
		DistLoads = null;
		StrainTypes = null;
		Disp = null;
		Stress = null;
		Strain = null;
		IntForces = null;
		this.Type = Type;
		DefineProperties(Type);
	}

	public int getID() {return ID;}
	public String getShape() {return Shape;}
	public int[] getDOFs() {return DOFs;}
	public int[][] getDOFsPerNode() {return DOFsPerNode;}
	public int[] getStrainTypes() {return StrainTypes;}
	public int[] getExternalNodes() {return ExternalNodes;}
	public int[] getInternalNodes() {return InternalNodes;}
	public double[] getMat() {return Mat;}
	public double[] getSec() {return Sec;}
	public DistLoads[] getDistLoads() {return DistLoads;}
	public double[] getDisp() {return Disp;}
	public double[] getStress() {return Stress;}
	public double[] getStrain() {return Strain;}
	public double[] getIntForces() {return IntForces;}
	public String getType() {return Type;}
	public static String[] getElemTypes() {return ElemTypes;}
	public int[][] getNodeDOF() {return NodeDOF;}
	public int[] getCumDOFs() {return CumDOFs;}
	public Color getMatColor () {return MatColor;}
	public Color getSecColor () {return SecColor;}
	public void setID(int I) {ID = I;}
	public void setShape(String S) {Shape = S;}
	public void setDOFs(int[] D) {DOFs = D;}
	public void setDOFsPerNode(int[][] D) {DOFsPerNode = D;}
	public void setStrainTypes(int[] ST) {StrainTypes = ST;}
	public void setExternalNodes(int[] N) {ExternalNodes = N;}
	public void setInternalNodes(int[] N) {InternalNodes = N;}
	public void setMat(double[] M) {Mat = M;}
	public void setSec(double[] S) {Sec = S;}
	public void setDistLoads(DistLoads[] D) {DistLoads = D;}
	public void setDisp(double[] D) {Disp = D;}
	public void setStress(double[] S) {Stress = S;}
	public void setStrain(double[] S) {Strain = S;}
	public void setIntForces(double[] I) {IntForces = I;}
	public void setType(String T) {Type = T;}
	public void setCumDOFs(int[] C) {CumDOFs = C;}
	public void setNodeDOF(int[][] N) {NodeDOF = N;}
	public void setMatColor (Color color) {MatColor = color;}
	public void setSecColor (Color color) {SecColor = color;}

	public void DefineProperties(String Type)
	{
		/* Element List
		 * 
		 * KR1, MR1 e MR2: Retângulo de Kirchhoff 1 e Retângulo de Mindlin 1 e 2 (Placa)
		 * KR2: Retângulo de Kirchhoff 2 (Placa)
		 * R4: Retângulo de 4 nós (Chapa)
		 * Q4: Quadrilátero de 4 nós (Chapa)
		 * T3G: Triângulo de 3 pontos de Gauss (Chapa)
		 * T6G: Triângulo de 6 pontos de Gauss (Chapa)
		 * SM: Retângulo de alta ordem 5 dof (Placa)
		 * SM8: Retângulo de 8 nós de alta ordem 5 dof
		 * KP3: Retângulo de 4 nós com funções de Hermite 3a ordem
		 * SM_C: Retângulo de 4 nós com funções de Hermite 3a ordem e derivada cruzada
		 * SM_H: Retângulo de 4 nós com funções de Hermite 3a ordem
		 * 
		 * */
		if (Type.equals(ElemTypes[0]) | Type.equals(ElemTypes[2]) | Type.equals(ElemTypes[3]))	// KR1, MR1 e MR2
		{
			Shape = "Rectangular";
			int NodesPerElem = 4;
			DOFs = new int[] {2, 3, 4};					// uz, tetax, tetay
			StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
			DOFsPerNode = new int[NodesPerElem][];
			Arrays.fill(DOFsPerNode, DOFs);
		}
		else if (Type.equals(ElemTypes[1]))												// KR2
		{
			Shape = "Rectangular";
			int NodesPerElem = 4;
			DOFs = new int[] {2, 3, 4, 6};				// uz, tetax, tetay, cd (cross derivative)
			StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
			DOFsPerNode = new int[NodesPerElem][];
			Arrays.fill(DOFsPerNode, DOFs);
		}
		else if (Type.equals(ElemTypes[4]))												// R4
		{
			Shape = "Rectangular";
			int NodesPerElem = 4;
			DOFs = new int[] {0, 1};					// ux, uy
			StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
			DOFsPerNode = new int[NodesPerElem][];
			Arrays.fill(DOFsPerNode, DOFs);
		}
		else if (Type.equals(ElemTypes[5]))												// Q4
		{
			Shape = "Quad";
			int NodesPerElem = 4;
			DOFs = new int[] {0, 1};					// ux, uy
			StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
			DOFsPerNode = new int[NodesPerElem][];
			Arrays.fill(DOFsPerNode, DOFs);
		}
		else if (Type.equals(ElemTypes[6]))												// T3G
		{
			Shape = "Triangular";
			int NodesPerElem = 3;
			DOFs = new int[] {0, 1};					// ux, uy
			StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
			DOFsPerNode = new int[NodesPerElem][];
			Arrays.fill(DOFsPerNode, DOFs);
		}
		else if (Type.equals(ElemTypes[7]))												// T6G
		{
			Shape = "Triangular";
			int NodesPerElem = 6;
			DOFs = new int[] {0, 1};					// ux, uy
			StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
			DOFsPerNode = new int[NodesPerElem][];
			Arrays.fill(DOFsPerNode, DOFs);
		}
		else if (Type.equals(ElemTypes[8]))												// SM
		{
			Shape = "Rectangular";
			int NodesPerElem = 4;
			DOFs = new int[] {2, 3, 4, 7, 8};			// uz, tetax, tetay, phix, phiy
			StrainTypes = new int[] {0, 1, 3, 4, 5};	// ex, ey, gxy, gxz, gyz
			DOFsPerNode = new int[NodesPerElem][];
			Arrays.fill(DOFsPerNode, DOFs);
		}
		else if (Type.equals(ElemTypes[9]))												// SM8
		{
			Shape = "R8";
			DOFs = new int[] {2, 3, 4, 7, 8};			// uz, tetax, tetay, phix, phiy
			StrainTypes = new int[] {0, 1, 3, 4, 5};	// ex, ey, gxy, gxz, gyz
			//DOFsPerNode = new int[][] {{2, 3, 4, 7, 8}, {7, 8}, {2, 3, 4, 7, 8}, {7, 8}, {2, 3, 4, 7, 8}, {7, 8}, {2, 3, 4, 7, 8}, {7, 8}};
			DOFsPerNode = new int[][] {{2, 3, 4, 7, 8}, {2, 3, 4, 7, 8}, {2, 3, 4, 7, 8}, {2, 3, 4, 7, 8}, {7, 8}, {7, 8}, {7, 8}, {7, 8}};
		}
		else if (Type.equals(ElemTypes[10]))											// KP3
		{
			Shape = "Rectangular";
			int NodesPerElem = 4;
			DOFs = new int[] {2, 3, 4};					// uz, tetax, tetay
			StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
			DOFsPerNode = new int[NodesPerElem][];
			Arrays.fill(DOFsPerNode, DOFs);
		}
		else if (Type.equals(ElemTypes[11]))											// SM_C
		{
			Shape = "Rectangular";
			int NodesPerElem = 4;
			DOFs = new int[] {2, 3, 4, 6, 7, 8};		// uz, tetax, tetay, cd (cross derivative), phix, phiy
			StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
			DOFsPerNode = new int[NodesPerElem][];
			Arrays.fill(DOFsPerNode, DOFs);
		}
		else if (Type.equals(ElemTypes[12]))											// SM_H
		{
			Shape = "Rectangular";
			int NodesPerElem = 4;
			DOFs = new int[] {2, 3, 4, 7, 8};			// uz, tetax, tetay, phix, phiy
			StrainTypes = new int[] {0, 1, 3};			// ex, ey, gxy
			DOFsPerNode = new int[NodesPerElem][];
			Arrays.fill(DOFsPerNode, DOFs);
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

	public double[] Size(Nodes[] Node)
	{
		double[] size = new double[2];
		if (Shape.equals("Rectangular"))
		{
			size[0] = Math.abs(Node[ExternalNodes[2]].getOriginalCoords()[0] - Node[ExternalNodes[0]].getOriginalCoords()[0]) / 2;
			size[1] = Math.abs(Node[ExternalNodes[2]].getOriginalCoords()[1] - Node[ExternalNodes[0]].getOriginalCoords()[1]) / 2;
		}
		else if (Shape.equals("R8") | Shape.equals("R9"))
		{
			size[0] = Math.abs(Node[ExternalNodes[4]].getOriginalCoords()[0] - Node[ExternalNodes[0]].getOriginalCoords()[0]) / 2;
			size[1] = Math.abs(Node[ExternalNodes[4]].getOriginalCoords()[1] - Node[ExternalNodes[0]].getOriginalCoords()[1]) / 2;
		}
		return size;
	}

	public int NumberOfNodes(String Shape)
	{
		if (Shape.equals("Rectangular"))
		{
			return 4;
		}
		else if (Shape.equals("Triangular"))
		{
			if (Type.equals("T6G"))
			{
				return 6;
			}
			else
			{
				return 3;
			}
		}
		else if (Shape.equals("R8"))
		{
			return 8;
		}
		else
		{
			System.out.println("Shape is not valid at Elements -> NumberOfNodes");
			return -1;
		}
	}
	
	public static String DefineShape(String Type)
	{
		String Shape = null;
		if (Type.equals(ElemTypes[0]) | Type.equals(ElemTypes[1]) | Type.equals(ElemTypes[2]) | Type.equals(ElemTypes[3]) | Type.equals(ElemTypes[4]) | Type.equals(ElemTypes[8]) | Type.equals(ElemTypes[10]) | Type.equals(ElemTypes[11]) | Type.equals(ElemTypes[12]))
		{
			Shape = "Rectangular";
		}
		else if (Type.equals(ElemTypes[5]))												// Q4
		{
			Shape = "Quad";
		}
		else if (Type.equals(ElemTypes[6]))												// T3G
		{
			Shape = "Triangular";
		}
		else if (Type.equals(ElemTypes[7]))												// T6G
		{
			Shape = "Triangular";
		}
		else if (Type.equals(ElemTypes[9]))												// SM8
		{
			Shape = "R8";
		}
		
		return Shape;
	}
	
	public double[][] getUndeformedCoords(){return UndeformedCoords;}
	
	public double[][] getDeformedCoords(){return DeformedCoords;}
	
	public double[] getCenterCoords(){return CenterCoords;}	
	
	public void setUndeformedCoords(Nodes[] Node)
	{
		UndeformedCoords = new double[ExternalNodes.length][];
		for (int node = 0; node <= ExternalNodes.length - 1; node += 1)
		{
			UndeformedCoords[node] = Node[ExternalNodes[node]].getOriginalCoords();
    	}
	}

	public void setDeformedCoords(Nodes[] Node)
	{
		DeformedCoords = new double[ExternalNodes.length][];
		for (int node = 0; node <= ExternalNodes.length - 1; node += 1)
		{
			DeformedCoords[node] = Util.ScaledDefCoords(Node[ExternalNodes[node]].getOriginalCoords(), Node[ExternalNodes[node]].getDisp(), Node[node].getDOFType(), 1);
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
	
	public static void setMatColors(double[][] MaterialTypes)
	{
		MatColors = Util.RandomColors(MaterialTypes.length);
	}

	public static void setSecColors(double[][] SectionTypes)
	{
		SecColors = Util.RandomColors(SectionTypes.length);
	}	
	
	public double[][] NaturalCoordsShapeFunctions(double e, double n, Nodes[] Node)
    {
    	// Calcula as funções de forma em um dado ponto (e, n). Unidades 1/m² e 1/m.
    	double[][] N = null;
		double[] ElemSize = Size(Node);
		double a = ElemSize[0];
		double b = ElemSize[1];	
    	if (Type.equals(ElemTypes[0]))
    	{
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
    	}
    	else if (Type.equals(ElemTypes[1]))
    	{
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
    	}
    	else if (Type.equals(ElemTypes[2]) | Type.equals(ElemTypes[3]))
    	{
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
    	}
    	else if (Type.equals(ElemTypes[4]) | Type.equals(ElemTypes[5]))
    	{
    		N = new double[2][8];
    		N[0][0] = (1 - e)*(1 - n) / 4;
    		N[0][2] = (1 + e)*(1 - n) / 4;
    		N[0][4] = (1 + e)*(1 + n) / 4;
    		N[0][6] = (1 - e)*(1 + n) / 4;
    		N[1][1] = (1 - e)*(1 - n) / 4;
    		N[1][3] = (1 + e)*(1 - n) / 4;
    		N[1][5] = (1 + e)*(1 + n) / 4;
    		N[1][7] = (1 - e)*(1 + n) / 4;
    	}
    	else if (Type.equals(ElemTypes[6]))
    	{
    		N = new double[2][6];
    		N[0][0] = e;
    		N[0][2] = n;
    		N[0][4] = 1 - e - n;
    		N[1][1] = e;
    		N[1][3] = n;
    		N[1][5] = 1 - e - n;
    	}
    	else if (Type.equals(ElemTypes[7]))
    	{
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
    	}
    	else if (Type.equals(ElemTypes[8]))
    	{
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
    	}
    	else if (Type.equals(ElemTypes[9]))
    	{
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
    	}
    	else if (Type.equals(ElemTypes[10]))
    	{
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
		}
    	else if (Type.equals(ElemTypes[11]))
    	{   	
    		N = new double[5][24];
    		N[0] = new double[] {((e*e*e-3*e+2)*(n*n*n-3*n+2))/16,	((e*e*e-e*e-e+1)*(n*n*n-3*n+2))/16,	((e*e*e-3*e+2)*(n*n*n-n*n-n+1))/16,	((e*e*e-e*e-e+1)*(n*n*n-n*n-n+1))/16,	0,	0,	((-e*e*e+3*e+2)*(n*n*n-3*n+2))/16,	((e*e*e+e*e-e-1)*(n*n*n-3*n+2))/16,	((-e*e*e+3*e+2)*(n*n*n-n*n-n+1))/16,	((e*e*e+e*e-e-1)*(n*n*n-n*n-n+1))/16,	0,	0,	((-e*e*e+3*e+2)*(-n*n*n+3*n+2))/16,	((e*e*e+e*e-e-1)*(-n*n*n+3*n+2))/16,	((-e*e*e+3*e+2)*(n*n*n+n*n-n-1))/16,	((e*e*e+e*e-e-1)*(n*n*n+n*n-n-1))/16,	0,	0,	((e*e*e-3*e+2)*(-n*n*n+3*n+2))/16,	((e*e*e-e*e-e+1)*(-n*n*n+3*n+2))/16,	((e*e*e-3*e+2)*(n*n*n+n*n-n-1))/16,	((e*e*e-e*e-e+1)*(n*n*n+n*n-n-1))/16,	0,	0} ;
    		N[1] = new double[] {((3*e*e-3)*(n*n*n-3*n+2))/(16*a),	((3*e*e-2*e-1)*(n*n*n-3*n+2))/(16*a),	((3*e*e-3)*(n*n*n-n*n-n+1))/(16*a),	((3*e*e-2*e-1)*(n*n*n-n*n-n+1))/(16*a),	0,	0,	((3-3*e*e)*(n*n*n-3*n+2))/(16*a),	((3*e*e+2*e-1)*(n*n*n-3*n+2))/(16*a),	((3-3*e*e)*(n*n*n-n*n-n+1))/(16*a),	((3*e*e+2*e-1)*(n*n*n-n*n-n+1))/(16*a),	0,	0,	((3-3*e*e)*(-n*n*n+3*n+2))/(16*a),	((3*e*e+2*e-1)*(-n*n*n+3*n+2))/(16*a),	((3-3*e*e)*(n*n*n+n*n-n-1))/(16*a),	((3*e*e+2*e-1)*(n*n*n+n*n-n-1))/(16*a),	0,	0,	((3*e*e-3)*(-n*n*n+3*n+2))/(16*a),	((3*e*e-2*e-1)*(-n*n*n+3*n+2))/(16*a),	((3*e*e-3)*(n*n*n+n*n-n-1))/(16*a),	((3*e*e-2*e-1)*(n*n*n+n*n-n-1))/(16*a),	0,	0} ;
			N[2] = new double[] {((e*e*e-3*e+2)*(3*n*n-3))/(16*b),	((e*e*e-e*e-e+1)*(3*n*n-3))/(16*b),	((e*e*e-3*e+2)*(3*n*n-2*n-1))/(16*b),	((e*e*e-e*e-e+1)*(3*n*n-2*n-1))/(16*b),	0,	0,	((-e*e*e+3*e+2)*(3*n*n-3))/(16*b),	((e*e*e+e*e-e-1)*(3*n*n-3))/(16*b),	((-e*e*e+3*e+2)*(3*n*n-2*n-1))/(16*b),	((e*e*e+e*e-e-1)*(3*n*n-2*n-1))/(16*b),	0,	0,	((-e*e*e+3*e+2)*(3-3*n*n))/(16*b),	((e*e*e+e*e-e-1)*(3-3*n*n))/(16*b),	((-e*e*e+3*e+2)*(3*n*n+2*n-1))/(16*b),	((e*e*e+e*e-e-1)*(3*n*n+2*n-1))/(16*b),	0,	0,	((e*e*e-3*e+2)*(3-3*n*n))/(16*b),	((e*e*e-e*e-e+1)*(3-3*n*n))/(16*b),	((e*e*e-3*e+2)*(3*n*n+2*n-1))/(16*b),	((e*e*e-e*e-e+1)*(3*n*n+2*n-1))/(16*b),	0,	0} ;
			N[3] = new double[] {0,	0,	0,	0,	((1-e)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(n+1))/4,	0,	0,	0,	0,	0,	((1-e)*(n+1))/4,	0} ;
			N[4] = new double[] {0,	0,	0,	0,	0,	((1-e)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(n+1))/4,	0,	0,	0,	0,	0,	((1-e)*(n+1))/4} ;  			
    	}
    	
    	return N;
    }

	public double[][] RealCoordsShapeFunctions(double[][] NodesCoords, double[] PointCoords)
    {
    	// Calcula as funções de forma de um elemento dadas as suas coordenadas reais. Unidades 1/m² e 1/m.
    	double[][] N = null;
    	if (Type.equals(ElemTypes[6]))
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
	
    public double[][] SecondDerivativesb(double e, double n, Nodes[] Node, double[] Sec, boolean NonlinearGeo)
    {
    	// Calcula as derivadas segundas das funções de forma em um dado ponto (e, n). Unidades 1/m² e 1/m.
    	double[][] B = null;
		double[] ElemSize = Size(Node);
		double a = ElemSize[0];
		double b = ElemSize[1];
		double t = Sec[0] / 1000.0;
    	if (Type.equals(ElemTypes[0]))
    	{
    	
    		B = new double[3][12];
    		B[0] = new double[] {(-((1-e)*(1-n))/4-((-2*e-1)*(1-n))/4)/(a*a),	((a*e*(1-n))/2+(a*(e-1)*(1-n))/4)/(a*a),	0,	(((1-2*e)*(1-n))/4-((e+1)*(1-n))/4)/(a*a),	((a*(e+1)*(1-n))/4+(a*e*(1-n))/2)/(a*a),	0,	(((1-2*e)*(n+1))/4-((e+1)*(n+1))/4)/(a*a),	((a*(e+1)*(n+1))/4+(a*e*(n+1))/2)/(a*a),	0,	(-((1-e)*(n+1))/4-((-2*e-1)*(n+1))/4)/(a*a),	((a*e*(n+1))/2+(a*(e-1)*(n+1))/4)/(a*a),	0} ;
			B[1] = new double[] {(-((1-e)*(1-n))/4-((1-e)*(-2*n-1))/4)/(b*b),	0,	((b*(1-e)*n)/2+(b*(1-e)*(n-1))/4)/(b*b),	(-((e+1)*(1-n))/4-((e+1)*(-2*n-1))/4)/(b*b),	0,	((b*(e+1)*n)/2+(b*(e+1)*(n-1))/4)/(b*b),	(((e+1)*(1-2*n))/4-((e+1)*(n+1))/4)/(b*b),	0,	((b*(e+1)*(n+1))/4+(b*(e+1)*n)/2)/(b*b),	(((1-e)*(1-2*n))/4-((1-e)*(n+1))/4)/(b*b),	0,	((b*(1-e)*(n+1))/4+(b*(1-e)*n)/2)/(b*b)} ;
			B[2] = new double[] {(2*((-n*n-n-e*e-e+2)/8-((-2*n-1)*(1-n))/8-((-2*e-1)*(1-e))/8))/(a*b),	(2*(-(a*(e*e-1))/8-(a*(e-1)*e)/4))/(a*b),	(2*(-(b*(n*n-1))/8-(b*(n-1)*n)/4))/(a*b),	(2*(-(-n*n-n-e*e+e+2)/8+((-2*n-1)*(1-n))/8-((1-2*e)*(e+1))/8))/(a*b),	(2*(-(a*(e*e-1))/8-(a*e*(e+1))/4))/(a*b),	(2*((b*(n*n-1))/8+(b*(n-1)*n)/4))/(a*b),	(2*((-n*n+n-e*e+e+2)/8+((1-2*n)*(n+1))/8+((1-2*e)*(e+1))/8))/(a*b),	(2*((a*(e*e-1))/8+(a*e*(e+1))/4))/(a*b),	(2*((b*(n*n-1))/8+(b*n*(n+1))/4))/(a*b),	(2*(-(-n*n+n-e*e-e+2)/8-((1-2*n)*(n+1))/8+((-2*e-1)*(1-e))/8))/(a*b),	(2*((a*(e*e-1))/8+(a*(e-1)*e)/4))/(a*b),	(2*(-(b*(n*n-1))/8-(b*n*(n+1))/4))/(a*b)} ;  			
    	

			B[0] = new double[] {(-((1-e)*(1-n))/4-((-2*e-1)*(1-n))/4)/(a*a),	((a*e*(1-n))/2+(a*(e-1)*(1-n))/4)/(a*a),	0,	(((1-2*e)*(1-n))/4-((e+1)*(1-n))/4)/(a*a),	((a*(e+1)*(1-n))/4+(a*e*(1-n))/2)/(a*a),	0,	(((1-2*e)*(n+1))/4-((e+1)*(n+1))/4)/(a*a),	((a*(e+1)*(n+1))/4+(a*e*(n+1))/2)/(a*a),	0,	(-((1-e)*(n+1))/4-((-2*e-1)*(n+1))/4)/(a*a),	((a*e*(n+1))/2+(a*(e-1)*(n+1))/4)/(a*a),	0} ;
			B[1] = new double[] {(-((1-e)*(1-n))/4-((1-e)*(-2*n-1))/4)/(b*b),	0,	((b*(1-e)*n)/2+(b*(1-e)*(n-1))/4)/(b*b),	(-((e+1)*(1-n))/4-((e+1)*(-2*n-1))/4)/(b*b),	0,	((b*(e+1)*n)/2+(b*(e+1)*(n-1))/4)/(b*b),	(((e+1)*(1-2*n))/4-((e+1)*(n+1))/4)/(b*b),	0,	((b*(e+1)*(n+1))/4+(b*(e+1)*n)/2)/(b*b),	(((1-e)*(1-2*n))/4-((1-e)*(n+1))/4)/(b*b),	0,	((b*(1-e)*(n+1))/4+(b*(1-e)*n)/2)/(b*b)} ;
			B[2] = new double[] {(2*((-n*n-n-e*e-e+2)/8-((-2*n-1)*(1-n))/8-((-2*e-1)*(1-e))/8))/(a*b),	(2*(-(a*(e*e-1))/8-(a*(e-1)*e)/4))/(a*b),	(2*(-(b*(n*n-1))/8-(b*(n-1)*n)/4))/(a*b),	(2*(-(-n*n-n-e*e+e+2)/8+((-2*n-1)*(1-n))/8-((1-2*e)*(e+1))/8))/(a*b),	(2*(-(a*(e*e-1))/8-(a*e*(e+1))/4))/(a*b),	(2*((b*(n*n-1))/8+(b*(n-1)*n)/4))/(a*b),	(2*((-n*n+n-e*e+e+2)/8+((1-2*n)*(n+1))/8+((1-2*e)*(e+1))/8))/(a*b),	(2*((a*(e*e-1))/8+(a*e*(e+1))/4))/(a*b),	(2*((b*(n*n-1))/8+(b*n*(n+1))/4))/(a*b),	(2*(-(-n*n+n-e*e-e+2)/8-((1-2*n)*(n+1))/8+((-2*e-1)*(1-e))/8))/(a*b),	(2*((a*(e*e-1))/8+(a*(e-1)*e)/4))/(a*b),	(2*(-(b*(n*n-1))/8-(b*n*(n+1))/4))/(a*b)} ;
				
    	}
    	else if (Type.equals(ElemTypes[1]))
    	{
    		
    		B = new double[3][16];
    		B[0] = new double[] {(3*e*(n*n*n-3*n+2))/(8*a*a),	((6*e-2)*(n*n*n-3*n+2))/(16*a*a),	(3*e*(n*n*n-n*n-n+1))/(8*a*a),	((6*e-2)*(n*n*n-n*n-n+1))/(16*a*a),	-(3*e*(n*n*n-3*n+2))/(8*a*a),	((6*e+2)*(n*n*n-3*n+2))/(16*a*a),	-(3*e*(n*n*n-n*n-n+1))/(8*a*a),	((6*e+2)*(n*n*n-n*n-n+1))/(16*a*a),	-(3*e*(-n*n*n+3*n+2))/(8*a*a),	((6*e+2)*(-n*n*n+3*n+2))/(16*a*a),	-(3*e*(n*n*n+n*n-n-1))/(8*a*a),	((6*e+2)*(n*n*n+n*n-n-1))/(16*a*a),	(3*e*(-n*n*n+3*n+2))/(8*a*a),	((6*e-2)*(-n*n*n+3*n+2))/(16*a*a),	(3*e*(n*n*n+n*n-n-1))/(8*a*a),	((6*e-2)*(n*n*n+n*n-n-1))/(16*a*a)} ;
			B[1] = new double[] {(3*(e*e*e-3*e+2)*n)/(8*b*b),	(3*(e*e*e-e*e-e+1)*n)/(8*b*b),	((e*e*e-3*e+2)*(6*n-2))/(16*b*b),	((e*e*e-e*e-e+1)*(6*n-2))/(16*b*b),	(3*(-e*e*e+3*e+2)*n)/(8*b*b),	(3*(e*e*e+e*e-e-1)*n)/(8*b*b),	((-e*e*e+3*e+2)*(6*n-2))/(16*b*b),	((e*e*e+e*e-e-1)*(6*n-2))/(16*b*b),	-(3*(-e*e*e+3*e+2)*n)/(8*b*b),	-(3*(e*e*e+e*e-e-1)*n)/(8*b*b),	((-e*e*e+3*e+2)*(6*n+2))/(16*b*b),	((e*e*e+e*e-e-1)*(6*n+2))/(16*b*b),	-(3*(e*e*e-3*e+2)*n)/(8*b*b),	-(3*(e*e*e-e*e-e+1)*n)/(8*b*b),	((e*e*e-3*e+2)*(6*n+2))/(16*b*b),	((e*e*e-e*e-e+1)*(6*n+2))/(16*b*b)} ;
			B[2] = new double[] {((3*e*e-3)*(3*n*n-3))/(8*a*b),	((3*e*e-2*e-1)*(3*n*n-3))/(8*a*b),	((3*e*e-3)*(3*n*n-2*n-1))/(8*a*b),	((3*e*e-2*e-1)*(3*n*n-2*n-1))/(8*a*b),	((3-3*e*e)*(3*n*n-3))/(8*a*b),	((3*e*e+2*e-1)*(3*n*n-3))/(8*a*b),	((3-3*e*e)*(3*n*n-2*n-1))/(8*a*b),	((3*e*e+2*e-1)*(3*n*n-2*n-1))/(8*a*b),	((3-3*e*e)*(3-3*n*n))/(8*a*b),	((3*e*e+2*e-1)*(3-3*n*n))/(8*a*b),	((3-3*e*e)*(3*n*n+2*n-1))/(8*a*b),	((3*e*e+2*e-1)*(3*n*n+2*n-1))/(8*a*b),	((3*e*e-3)*(3-3*n*n))/(8*a*b),	((3*e*e-2*e-1)*(3-3*n*n))/(8*a*b),	((3*e*e-3)*(3*n*n+2*n-1))/(8*a*b),	((3*e*e-2*e-1)*(3*n*n+2*n-1))/(8*a*b)} ; 			
    	
    	}
    	else if (Type.equals(ElemTypes[2]) | Type.equals(ElemTypes[3]))
    	{
    		B = new double[3][12];
    		B[0] = new double[] {0, -(1 - n) / (4 * a), 0, 0, (1 - n) / (4 * a), 0, 0, (1 + n) / (4 * a), 0, 0, -(1 + n) / (4 * a), 0} ;
    		B[1] = new double[] {0, -(1 - e) / (4 * b), 0, 0, -(1 + e) / (4 * b), 0, 0, (1 + e) / (4 * b), 0, 0, (1 - e) / (4 * b), 0} ;
    		B[2] = new double[] {-(1 - e) / (4 * b), -(1 - n) / (4 * a), 0, -(1 + e) / (4 * b), (1 - n) / (4 * a), 0, (1 + e) / (4 * b), (1 + n) / (4 * a), 0, (1 - e) / (4 * b), -(1 + n) / (4 * a), 0} ;
    	
    	}
    	else if (Type.equals(ElemTypes[4]) | Type.equals(ElemTypes[5]))
    	{
    	
    		B = new double[3][8];
    		B[0] = new double[] {0, -(1 - n) / (4 * a), 0, (1 - n) / (4 * a), 0, (1 + n) / (4 * a), 0, -(1 + n) / (4 * a)} ;
    		B[1] = new double[] {-(1 - e) / (4 * b), 0, -(1 + e) / (4 * b), 0, (1 + e) / (4 * b), 0, (1 - e) / (4 * b), 0} ;
    		B[2] = new double[] {-(1 - e) / (4 * b), -(1 - n) / (4 * a), -(1 + e) / (4 * b), (1 - n) / (4 * a), (1 + e) / (4 * b), (1 + n) / (4 * a), (1 - e) / (4 * b), -(1 + n) / (4 * a)} ;
    	
    	}
    	else if (Type.equals(ElemTypes[6]))
    	{
    	
    		int[] Nodes = ExternalNodes;
    		double[][] Coords = new double[Nodes.length][];
    		for (int node = 0; node <= Nodes.length - 1; node += 1)
    		{
    			Coords[node] = new double[Node[Nodes[node]].getOriginalCoords().length];
    			for (int coord = 0; coord <= Node[Nodes[node]].getOriginalCoords().length - 1; coord += 1)
        		{
    				if (NonlinearGeo)
    				{
            			Coords[node][coord] = Node[Nodes[node]].getOriginalCoords()[coord] + Node[Nodes[node]].getDisp()[coord];
    				}
    				else
    				{
            			Coords[node][coord] = Node[Nodes[node]].getOriginalCoords()[coord];
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
    	
    	}
    	else if (Type.equals(ElemTypes[7]))
    	{
    		
    	}
    	else if (Type.equals(ElemTypes[8]))
    	{    		
    		double z = t / 2;	// Strains vary with the thickness, they are maximum at the extremes and the bending ones are 0 at the middle
    		B = new double[5][8];
    		B[0] = new double[] {((3*e*n-3*e)*z)/(4*a*a),	(((3*e-1)*n-3*e+1)*z)/(4*a),	0,	-((20*n-20)*z*z*z+(15-15*n)*t*t*z)/(48*a*t*t),	0,	-((3*e*n-3*e)*z)/(4*a*a),	(((3*e+1)*n-3*e-1)*z)/(4*a),	0,	((20*n-20)*z*z*z+(15-15*n)*t*t*z)/(48*a*t*t),	0,	((3*e*n+3*e)*z)/(4*a*a),	-(((3*e+1)*n+3*e+1)*z)/(4*a),	0,	-((20*n+20)*z*z*z+(-15*n-15)*t*t*z)/(48*a*t*t),	0,	-((3*e*n+3*e)*z)/(4*a*a),	-(((3*e-1)*n+3*e-1)*z)/(4*a),	0,	((20*n+20)*z*z*z+(-15*n-15)*t*t*z)/(48*a*t*t),	0} ;
			B[1] = new double[] {((3*e-3)*n*z)/(4*b*b),	0,	(((3*e-3)*n-e+1)*z)/(4*b),	0,	-((20*e-20)*z*z*z+(15-15*e)*t*t*z)/(48*b*t*t),	-((3*e+3)*n*z)/(4*b*b),	0,	-(((3*e+3)*n-e-1)*z)/(4*b),	0,	((20*e+20)*z*z*z+(-15*e-15)*t*t*z)/(48*b*t*t),	((3*e+3)*n*z)/(4*b*b),	0,	-(((3*e+3)*n+e+1)*z)/(4*b),	0,	-((20*e+20)*z*z*z+(-15*e-15)*t*t*z)/(48*b*t*t),	-((3*e-3)*n*z)/(4*b*b),	0,	(((3*e-3)*n+e-1)*z)/(4*b),	0,	((20*e-20)*z*z*z+(15-15*e)*t*t*z)/(48*b*t*t)} ;
			B[2] = new double[] {((3*n*n+3*e*e-4)*z)/(4*a*b),	((3*e*e-2*e-1)*z)/(4*b),	((3*n*n-2*n-1)*z)/(4*a),	-((20*e-20)*z*z*z+(15-15*e)*t*t*z)/(48*b*t*t),	-((20*n-20)*z*z*z+(15-15*n)*t*t*z)/(48*a*t*t),	-((3*n*n+3*e*e-4)*z)/(4*a*b),	((3*e*e+2*e-1)*z)/(4*b),	-((3*n*n-2*n-1)*z)/(4*a),	((20*e+20)*z*z*z+(-15*e-15)*t*t*z)/(48*b*t*t),	((20*n-20)*z*z*z+(15-15*n)*t*t*z)/(48*a*t*t),	((3*n*n+3*e*e-4)*z)/(4*a*b),	-((3*e*e+2*e-1)*z)/(4*b),	-((3*n*n+2*n-1)*z)/(4*a),	-((20*e+20)*z*z*z+(-15*e-15)*t*t*z)/(48*b*t*t),	-((20*n+20)*z*z*z+(-15*n-15)*t*t*z)/(48*a*t*t),	-((3*n*n+3*e*e-4)*z)/(4*a*b),	-((3*e*e-2*e-1)*z)/(4*b),	((3*n*n+2*n-1)*z)/(4*a),	((20*e-20)*z*z*z+(15-15*e)*t*t*z)/(48*b*t*t),	((20*n+20)*z*z*z+(-15*n-15)*t*t*z)/(48*a*t*t)} ;    			
			B[3] = new double[] {0,	0,	0,	-(((20*e-20)*n-20*e+20)*z*z+((5-5*e)*n+5*e-5)*t*t)/(16*t*t),	0,	0,	0,	0,	(((20*e+20)*n-20*e-20)*z*z+((-5*e-5)*n+5*e+5)*t*t)/(16*t*t),	0,	0,	0,	0,	-(((20*e+20)*n+20*e+20)*z*z+((-5*e-5)*n-5*e-5)*t*t)/(16*t*t),	0,	0,	0,	0,	(((20*e-20)*n+20*e-20)*z*z+((5-5*e)*n-5*e+5)*t*t)/(16*t*t),	0} ;
			B[4] = new double[] {0,	0,	0,	0,	-(((20*e-20)*n-20*e+20)*z*z+((5-5*e)*n+5*e-5)*t*t)/(16*t*t),	0,	0,	0,	0,	(((20*e+20)*n-20*e-20)*z*z+((-5*e-5)*n+5*e+5)*t*t)/(16*t*t),	0,	0,	0,	0,	-(((20*e+20)*n+20*e+20)*z*z+((-5*e-5)*n-5*e-5)*t*t)/(16*t*t),	0,	0,	0,	0,	(((20*e-20)*n+20*e-20)*z*z+((5-5*e)*n-5*e+5)*t*t)/(16*t*t)} ;
    		 
    	}
    	else if (Type.equals(ElemTypes[9]))
    	{
    		double z = t / 2;	// Strains vary with the thickness, they are maximum at the extremes and the bending ones are 0 at the middle
    		B = new double[5][28];
    		B[0] = new double[] {((3*e*n-3*e)*z)/(4*a*a),	(((3*e-1)*n-3*e+1)*z)/(4*a),	0,	((20*n*n+(40*e-20)*n-40*e)*z*z*z+(-15*n*n+(15-30*e)*n+30*e)*t*t*z)/(48*a*t*t),	0,	-((20*e*n-20*e)*z*z*z+(15*e-15*e*n)*t*t*z)/(12*a*t*t),	0,	-((3*e*n-3*e)*z)/(4*a*a),	(((3*e+1)*n-3*e-1)*z)/(4*a),	0,	-((20*n*n+(-40*e-20)*n+40*e)*z*z*z+(-15*n*n+(30*e+15)*n-30*e)*t*t*z)/(48*a*t*t),	0,	((20*n*n-20)*z*z*z+(15-15*n*n)*t*t*z)/(24*a*t*t),	0,	((3*e*n+3*e)*z)/(4*a*a),	-(((3*e+1)*n+3*e+1)*z)/(4*a),	0,	-((20*n*n+(40*e+20)*n+40*e)*z*z*z+(-15*n*n+(-30*e-15)*n-30*e)*t*t*z)/(48*a*t*t),	0,	((20*e*n+20*e)*z*z*z+(-15*e*n-15*e)*t*t*z)/(12*a*t*t),	0,	-((3*e*n+3*e)*z)/(4*a*a),	-(((3*e-1)*n+3*e-1)*z)/(4*a),	0,	((20*n*n+(20-40*e)*n-40*e)*z*z*z+(-15*n*n+(30*e-15)*n+30*e)*t*t*z)/(48*a*t*t),	0,	-((20*n*n-20)*z*z*z+(15-15*n*n)*t*t*z)/(24*a*t*t),	0} ;
			B[1] = new double[] {((3*e-3)*n*z)/(4*b*b),	0,	(((3*e-3)*n-e+1)*z)/(4*b),	0,	(((40*e-40)*n+20*e*e-20*e)*z*z*z+((30-30*e)*n-15*e*e+15*e)*t*t*z)/(48*b*t*t),	0,	-((20*e*e-20)*z*z*z+(15-15*e*e)*t*t*z)/(24*b*t*t),	-((3*e+3)*n*z)/(4*b*b),	0,	-(((3*e+3)*n-e-1)*z)/(4*b),	0,	-(((40*e+40)*n-20*e*e-20*e)*z*z*z+((-30*e-30)*n+15*e*e+15*e)*t*t*z)/(48*b*t*t),	0,	((20*e+20)*n*z*z*z+(-15*e-15)*n*t*t*z)/(12*b*t*t),	((3*e+3)*n*z)/(4*b*b),	0,	-(((3*e+3)*n+e+1)*z)/(4*b),	0,	-(((40*e+40)*n+20*e*e+20*e)*z*z*z+((-30*e-30)*n-15*e*e-15*e)*t*t*z)/(48*b*t*t),	0,	((20*e*e-20)*z*z*z+(15-15*e*e)*t*t*z)/(24*b*t*t),	-((3*e-3)*n*z)/(4*b*b),	0,	(((3*e-3)*n+e-1)*z)/(4*b),	0,	(((40*e-40)*n-20*e*e+20*e)*z*z*z+((30-30*e)*n+15*e*e-15*e)*t*t*z)/(48*b*t*t),	0,	-((20*e-20)*n*z*z*z+(15-15*e)*n*t*t*z)/(12*b*t*t)} ;
			B[2] = new double[] {((3*n*n+3*e*e-4)*z)/(4*a*b),	((3*e*e-2*e-1)*z)/(4*b),	((3*n*n-2*n-1)*z)/(4*a),	(((40*e-40)*n+20*e*e-20*e)*z*z*z+((30-30*e)*n-15*e*e+15*e)*t*t*z)/(48*b*t*t),	((20*n*n+(40*e-20)*n-40*e)*z*z*z+(-15*n*n+(15-30*e)*n+30*e)*t*t*z)/(48*a*t*t),	-((20*e*e-20)*z*z*z+(15-15*e*e)*t*t*z)/(24*b*t*t),	-((20*e*n-20*e)*z*z*z+(15*e-15*e*n)*t*t*z)/(12*a*t*t),	-((3*n*n+3*e*e-4)*z)/(4*a*b),	((3*e*e+2*e-1)*z)/(4*b),	-((3*n*n-2*n-1)*z)/(4*a),	-(((40*e+40)*n-20*e*e-20*e)*z*z*z+((-30*e-30)*n+15*e*e+15*e)*t*t*z)/(48*b*t*t),	-((20*n*n+(-40*e-20)*n+40*e)*z*z*z+(-15*n*n+(30*e+15)*n-30*e)*t*t*z)/(48*a*t*t),	((20*e+20)*n*z*z*z+(-15*e-15)*n*t*t*z)/(12*b*t*t),	((20*n*n-20)*z*z*z+(15-15*n*n)*t*t*z)/(24*a*t*t),	((3*n*n+3*e*e-4)*z)/(4*a*b),	-((3*e*e+2*e-1)*z)/(4*b),	-((3*n*n+2*n-1)*z)/(4*a),	-(((40*e+40)*n+20*e*e+20*e)*z*z*z+((-30*e-30)*n-15*e*e-15*e)*t*t*z)/(48*b*t*t),	-((20*n*n+(40*e+20)*n+40*e)*z*z*z+(-15*n*n+(-30*e-15)*n-30*e)*t*t*z)/(48*a*t*t),	((20*e*e-20)*z*z*z+(15-15*e*e)*t*t*z)/(24*b*t*t),	((20*e*n+20*e)*z*z*z+(-15*e*n-15*e)*t*t*z)/(12*a*t*t),	-((3*n*n+3*e*e-4)*z)/(4*a*b),	-((3*e*e-2*e-1)*z)/(4*b),	((3*n*n+2*n-1)*z)/(4*a),	(((40*e-40)*n-20*e*e+20*e)*z*z*z+((30-30*e)*n+15*e*e-15*e)*t*t*z)/(48*b*t*t),	((20*n*n+(20-40*e)*n-40*e)*z*z*z+(-15*n*n+(30*e-15)*n+30*e)*t*t*z)/(48*a*t*t),	-((20*e-20)*n*z*z*z+(15-15*e)*n*t*t*z)/(12*b*t*t),	-((20*n*n-20)*z*z*z+(15-15*n*n)*t*t*z)/(24*a*t*t)} ;
			B[3] = new double[] {0,	0,	0,	(((20*e-20)*n*n+(20*e*e-20*e)*n-20*e*e+20)*z*z+((5-5*e)*n*n+(5*e-5*e*e)*n+5*e*e-5)*t*t)/(16*t*t),	0,	-(((20*e*e-20)*n-20*e*e+20)*z*z+((5-5*e*e)*n+5*e*e-5)*t*t)/(8*t*t),	0,	0,	0,	0,	-(((20*e+20)*n*n+(-20*e*e-20*e)*n+20*e*e-20)*z*z+((-5*e-5)*n*n+(5*e*e+5*e)*n-5*e*e+5)*t*t)/(16*t*t),	0,	(((20*e+20)*n*n-20*e-20)*z*z+((-5*e-5)*n*n+5*e+5)*t*t)/(8*t*t),	0,	0,	0,	0,	-(((20*e+20)*n*n+(20*e*e+20*e)*n+20*e*e-20)*z*z+((-5*e-5)*n*n+(-5*e*e-5*e)*n-5*e*e+5)*t*t)/(16*t*t),	0,	(((20*e*e-20)*n+20*e*e-20)*z*z+((5-5*e*e)*n-5*e*e+5)*t*t)/(8*t*t),	0,	0,	0,	0,	(((20*e-20)*n*n+(20*e-20*e*e)*n-20*e*e+20)*z*z+((5-5*e)*n*n+(5*e*e-5*e)*n+5*e*e-5)*t*t)/(16*t*t),	0,	-(((20*e-20)*n*n-20*e+20)*z*z+((5-5*e)*n*n+5*e-5)*t*t)/(8*t*t),	0} ;
			B[4] = new double[] {0,	0,	0,	0,	(((20*e-20)*n*n+(20*e*e-20*e)*n-20*e*e+20)*z*z+((5-5*e)*n*n+(5*e-5*e*e)*n+5*e*e-5)*t*t)/(16*t*t),	0,	-(((20*e*e-20)*n-20*e*e+20)*z*z+((5-5*e*e)*n+5*e*e-5)*t*t)/(8*t*t),	0,	0,	0,	0,	-(((20*e+20)*n*n+(-20*e*e-20*e)*n+20*e*e-20)*z*z+((-5*e-5)*n*n+(5*e*e+5*e)*n-5*e*e+5)*t*t)/(16*t*t),	0,	(((20*e+20)*n*n-20*e-20)*z*z+((-5*e-5)*n*n+5*e+5)*t*t)/(8*t*t),	0,	0,	0,	0,	-(((20*e+20)*n*n+(20*e*e+20*e)*n+20*e*e-20)*z*z+((-5*e-5)*n*n+(-5*e*e-5*e)*n-5*e*e+5)*t*t)/(16*t*t),	0,	(((20*e*e-20)*n+20*e*e-20)*z*z+((5-5*e*e)*n-5*e*e+5)*t*t)/(8*t*t),	0,	0,	0,	0,	(((20*e-20)*n*n+(20*e-20*e*e)*n-20*e*e+20)*z*z+((5-5*e)*n*n+(5*e*e-5*e)*n+5*e*e-5)*t*t)/(16*t*t),	0,	-(((20*e-20)*n*n-20*e+20)*z*z+((5-5*e)*n*n+5*e-5)*t*t)/(8*t*t)} ;
				
    	}
    	else if (Type.equals(ElemTypes[10]))
    	{
    		B = new double[5][12];
    		B[0] = new double[] {((3*e*e-3)*(n*n*n-3*n+2))/16,	(a*(3*e*e-2*e-1)*(n*n*n-3*n+2))/16,	(b*(3*e*e-3)*(n*n*n-n*n-n+1))/16,	((3-3*e*e)*(n*n*n-3*n+2))/16,	(a*(3*e*e+2*e-1)*(n*n*n-3*n+2))/16,	(b*(3-3*e*e)*(n*n*n-n*n-n+1))/16,	((3-3*e*e)*(-n*n*n+3*n+2))/16,	(a*(3*e*e+2*e-1)*(-n*n*n+3*n+2))/16,	(b*(3-3*e*e)*(n*n*n+n*n-n-1))/16,	((3*e*e-3)*(-n*n*n+3*n+2))/16,	(a*(3*e*e-2*e-1)*(-n*n*n+3*n+2))/16,	(b*(3*e*e-3)*(n*n*n+n*n-n-1))/16} ;
			B[1] = new double[] {((e*e*e-3*e+2)*(3*n*n-3))/16,	(a*(e*e*e-e*e-e+1)*(3*n*n-3))/16,	(b*(e*e*e-3*e+2)*(3*n*n-2*n-1))/16,	((-e*e*e+3*e+2)*(3*n*n-3))/16,	(a*(e*e*e+e*e-e-1)*(3*n*n-3))/16,	(b*(-e*e*e+3*e+2)*(3*n*n-2*n-1))/16,	((-e*e*e+3*e+2)*(3-3*n*n))/16,	(a*(e*e*e+e*e-e-1)*(3-3*n*n))/16,	(b*(-e*e*e+3*e+2)*(3*n*n+2*n-1))/16,	((e*e*e-3*e+2)*(3-3*n*n))/16,	(a*(e*e*e-e*e-e+1)*(3-3*n*n))/16,	(b*(e*e*e-3*e+2)*(3*n*n+2*n-1))/16} ;
			B[2] = new double[] {(3*e*(n*n*n-3*n+2))/8,	(a*(6*e-2)*(n*n*n-3*n+2))/16,	(3*b*e*(n*n*n-n*n-n+1))/8,	-(3*e*(n*n*n-3*n+2))/8,	(a*(6*e+2)*(n*n*n-3*n+2))/16,	-(3*b*e*(n*n*n-n*n-n+1))/8,	-(3*e*(-n*n*n+3*n+2))/8,	(a*(6*e+2)*(-n*n*n+3*n+2))/16,	-(3*b*e*(n*n*n+n*n-n-1))/8,	(3*e*(-n*n*n+3*n+2))/8,	(a*(6*e-2)*(-n*n*n+3*n+2))/16,	(3*b*e*(n*n*n+n*n-n-1))/8} ;
			B[3] = new double[] {(3*(e*e*e-3*e+2)*n)/8,	(3*a*(e*e*e-e*e-e+1)*n)/8,	(b*(e*e*e-3*e+2)*(6*n-2))/16,	(3*(-e*e*e+3*e+2)*n)/8,	(3*a*(e*e*e+e*e-e-1)*n)/8,	(b*(-e*e*e+3*e+2)*(6*n-2))/16,	-(3*(-e*e*e+3*e+2)*n)/8,	-(3*a*(e*e*e+e*e-e-1)*n)/8,	(b*(-e*e*e+3*e+2)*(6*n+2))/16,	-(3*(e*e*e-3*e+2)*n)/8,	-(3*a*(e*e*e-e*e-e+1)*n)/8,	(b*(e*e*e-3*e+2)*(6*n+2))/16} ;
			B[4] = new double[] {((3*e*e-3)*(3*n*n-3))/16,	(a*(3*e*e-2*e-1)*(3*n*n-3))/16,	(b*(3*e*e-3)*(3*n*n-2*n-1))/16,	((3-3*e*e)*(3*n*n-3))/16,	(a*(3*e*e+2*e-1)*(3*n*n-3))/16,	(b*(3-3*e*e)*(3*n*n-2*n-1))/16,	((3-3*e*e)*(3-3*n*n))/16,	(a*(3*e*e+2*e-1)*(3-3*n*n))/16,	(b*(3-3*e*e)*(3*n*n+2*n-1))/16,	((3*e*e-3)*(3-3*n*n))/16,	(a*(3*e*e-2*e-1)*(3-3*n*n))/16,	(b*(3*e*e-3)*(3*n*n+2*n-1))/16} ;
    			
    	}
    	else if (Type.equals(ElemTypes[11]))
    	{
    		
    	}
    	else if (Type.equals(ElemTypes[12]))
    	{
    		
    	}
    	
    	return B;
    }    
    
	public double[][] Bb(double e, double n, double w, Nodes[] Node, double[] Sec, boolean NonlinearGeo, int option)
	{
    	double[][] Bb1 = null,  Bb2 = null;
    	double t = Sec[0] / 1000.0;
		double[] ElemSize = Size(Node);
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
    
    public double[][] Bs(double e, double n, double w, Nodes[] Node, double[] Sec, int option)
	{
		double[][] Bs1 = null,  Bs2 = null;
    	double t = Sec[0] / 1000.0;
		double a1 = 4 / (3.0 * Math.pow(t, 2));
		double[] ElemSize = Size(Node);
		double a = ElemSize[0];
		double b = ElemSize[1];	
    	if (Type.equals(ElemTypes[8]))
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
     
    public double[][] LoadVector(Nodes[] Node)
	{
		double[][] Q = null;
		double[] ElemSize = Size(Node);
		double a = ElemSize[0];
		double b = ElemSize[1];	
		if (Type.equals("KR1"))
		{
			Q = new double[3][12];
			
			Q[0] = new double[] {a*b,	(a*a*b)/3,	(a*b*b)/3,	a*b,	-(a*a*b)/3,	(a*b*b)/3,	a*b,	-(a*a*b)/3,	-(a*b*b)/3,	a*b,	(a*a*b)/3,	-(a*b*b)/3} ;
			Q[1] = new double[] {-a*b,	0,	-(a*b*b)/3,	a*b,	0,	(a*b*b)/3,	a*b,	0,	-(a*b*b)/3,	-a*b,	0,	(a*b*b)/3} ;
			Q[2] = new double[] {-a*b,	-(a*a*b)/3,	0,	-a*b,	(a*a*b)/3,	0,	a*b,	-(a*a*b)/3,	0,	a*b,	(a*a*b)/3,	0} ;
				
		}
		if (Type.equals("KP3"))
		{
			Q = new double[3][12];
			
			Q[0] = new double[] {a*b,	(a*a*b)/3,	(a*b*b)/3,	a*b,	-(a*a*b)/3,	(a*b*b)/3,	a*b,	-(a*a*b)/3,	-(a*b*b)/3,	a*b,	(a*a*b)/3,	-(a*b*b)/3} ;
			Q[1] = new double[] {-a*b,	0,	-(a*b*b)/3,	a*b,	0,	(a*b*b)/3,	a*b,	0,	-(a*b*b)/3,	-a*b,	0,	(a*b*b)/3} ;
			Q[2] = new double[] {-a*b,	-(a*a*b)/3,	0,	-a*b,	(a*a*b)/3,	0,	a*b,	-(a*a*b)/3,	0,	a*b,	(a*a*b)/3,	0} ;
				
		}
		else if (Type.equals("SM") | Type.equals("SM_H"))
		{
			Q = new double[5][20];
			
			Q[0] = new double[] {a*b,	(a*a*b)/3,	(a*b*b)/3,	0,	0,	a*b,	-(a*a*b)/3,	(a*b*b)/3,	0,	0,	a*b,	-(a*a*b)/3,	-(a*b*b)/3,	0,	0,	a*b,	(a*a*b)/3,	-(a*b*b)/3,	0,	0} ;
			Q[1] = new double[] {-b,	0,	-b*b/3,	0,	0,	b,	0,	b*b/3,	0,	0,	b,	0,	-b*b/3,	0,	0,	-b,	0,	b*b/3,	0,	0} ;
			Q[2] = new double[] {-a,	-a*a/3,	0,	0,	0,	-a,	a*a/3,	0,	0,	0,	a,	-a*a/3,	0,	0,	0,	a,	a*a/3,	0,	0,	0} ;
			Q[3] = new double[] {0,	0,	0,	a*b,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	a*b,	0} ;
			Q[4] = new double[] {0,	0,	0,	0,	a*b,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	a*b} ;
				
		}
		else if (Type.equals("SM") | Type.equals("SM_C"))
		{
			Q = new double[5][24];
			
			Q[0] = new double[] {a*b,	(a*b)/3,	(a*b)/3,	(a*b)/9,	0,	0,	a*b,	-(a*b)/3,	(a*b)/3,	-(a*b)/9,	0,	0,	a*b,	-(a*b)/3,	-(a*b)/3,	(a*b)/9,	0,	0,	a*b,	(a*b)/3,	-(a*b)/3,	-(a*b)/9,	0,	0} ;
			Q[1] = new double[] {-b,	0,	-b/3,	0,	0,	0,	b,	0,	b/3,	0,	0,	0,	b,	0,	-b/3,	0,	0,	0,	-b,	0,	b/3,	0,	0,	0} ;
			Q[2] = new double[] {-a,	-a/3,	0,	0,	0,	0,	-a,	a/3,	0,	0,	0,	0,	a,	-a/3,	0,	0,	0,	0,	a,	a/3,	0,	0,	0,	0} ;
			Q[3] = new double[] {0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b,	0} ;
			Q[4] = new double[] {0,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b,	0,	0,	0,	0,	0,	a*b} ;
			
		}
		else if (Type.equals("SM8"))
		{
			Q = new double[5][28];
						
			Q[0] = new double[] {a*b,	(a*a*b)/3,	(a*b*b)/3,	0,	0,	0,	0,	a*b,	-(a*a*b)/3,	(a*b*b)/3,	0,	0,	0,	0,	a*b,	-(a*a*b)/3,	-(a*b*b)/3,	0,	0,	0,	0,	a*b,	(a*a*b)/3,	-(a*b*b)/3,	0,	0,	0,	0} ;
			Q[1] = new double[] {-b,	0,	-b*b/3,	0,	0,	0,	0,	b,	0,	b*b/3,	0,	0,	0,	0,	b,	0,	-b*b/3,	0,	0,	0,	0,	-b,	0,	b*b/3,	0,	0,	0,	0} ;
			Q[2] = new double[] {-a,	-a*a/3,	0,	0,	0,	0,	0,	-a,	a*a/3,	0,	0,	0,	0,	0,	a,	-a*a/3,	0,	0,	0,	0,	0,	a,	a*a/3,	0,	0,	0,	0,	0} ;
			Q[3] = new double[] {0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0} ;
			Q[4] = new double[] {0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3,	0,	0,	0,	0,	-(a*b)/3,	0,	(4*a*b)/3} ;
					
		}		 
		
		return Q;
	}

    public double[][] BendingConstitutiveMatrix(double[] Mat, boolean NonlinearMat, double[] strain)
    {
    	// Calcula a matriz com propriedades elásticas do elemento. Unidade: N.m
    	double E = Mat[0] * Math.pow(10, 6), v = Mat[1], fu = Mat[2] * Math.pow(10, 3);	
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
    
    public double[][] ShearConstitutiveMatrix(double[] Mat, double[] Sec)
    {
    	double[][] C = new double[2][2];
    	double E = Mat[0] * Math.pow(10, 6), v = Mat[1];
    	double t = Sec[0] / 1000.0;
    	double k = 5 / 6.0;
		double G = E / (2 * (1 + v));
		C[0][0] = G * k * t; C[0][1] = 0;
		C[1][0] = 0;         C[1][1] = G * k * t;
		return C;
    }
    
    public double[][] ShearConstitutiveMatrix2(double[] Mat, double[] Sec)
    {
    	double[][] C = new double[2][2];
    	double E = Mat[0] * Math.pow(10, 6), v = Mat[1];
		double G = E / (2 * (1 + v));
		C[0][0] = G; C[0][1] = 0;
		C[1][0] = 0; C[1][1] = G;
		return C;
    }
       
    public double[][] StiffnessMatrix(Nodes[] Node, boolean NonlinearMat, boolean NonlinearGeo)
    {
    	// Calcula a matriz de rigidez do elemento
    	double[][] k = null;
		double[] ElemSize = Size(Node);
		double[][] Db = BendingConstitutiveMatrix(Mat, NonlinearMat, Strain);
		double[][] Ds = ShearConstitutiveMatrix2(Mat, Sec);
    	if (Type.equals(ElemTypes[0]))
    	{
    		k = ElementStiffnessMatrix.KR1StiffnessMatrix(ElemSize, Db, Sec);
    	}
    	else if (Type.equals(ElemTypes[1]))
    	{
    		k = ElementStiffnessMatrix.KR2StiffnessMatrix(ElemSize, Db, Sec);
    	}
    	else if (Type.equals(ElemTypes[2]))
    	{
    		k = ElementStiffnessMatrix.MR1StiffnessMatrix(ElemSize, Db, Ds, Sec);
    	}
    	else if (Type.equals(ElemTypes[3]))
    	{
    		k = ElementStiffnessMatrix.MR2StiffnessMatrix(ElemSize, Db, Ds, Sec);
    	}
    	else if (Type.equals(ElemTypes[4]))
    	{
    		k = ElementStiffnessMatrix.R4StiffnessMatrix(ElemSize, Db, Sec);
    	}
    	else if (Type.equals(ElemTypes[5]))
    	{
    		k = ElementStiffnessMatrix.Q4StiffnessMatrix(Node, ExternalNodes, ElemSize, Db, Sec, NonlinearGeo);
    	}
    	else if (Type.equals(ElemTypes[6]))
    	{
        	double[][] D = BendingConstitutiveMatrix(Mat, NonlinearMat, Strain);
        	double thick = Sec[0] / 1000.0;							// Espessura
			double Area;
    		if (!NonlinearGeo)
        	{
            	double[][] Coords = new double[ExternalNodes.length][];
    			for (int node = 0; node <= ExternalNodes.length - 1; node += 1)
    			{
	    			Coords[node] = new double[Node[ExternalNodes[node]].getOriginalCoords().length];
	    			for (int coord = 0; coord <= Node[ExternalNodes[node]].getOriginalCoords().length - 1; coord += 1)
	        		{
	            		Coords[node][coord] = Node[ExternalNodes[node]].getOriginalCoords()[coord];
	        		}
    			}     	
    			Area = Util.TriArea(Coords);
        	}
    		else
        	{
        		double[][] DefCoords = Util.GetElemNodesDefPos(Node, ExternalNodes);
        		Area = Util.TriArea(DefCoords);
        	}
    	    double[][] B = SecondDerivativesb(0, 0, Node, Sec, NonlinearGeo);
    	    k = Util.MultMatrix(Util.MultMatrix(Util.Transpose(B), D), B);
    	    for (int i = 0; i <= k.length - 1; i += 1)
        	{
        		for (int j = 0; j <= k[i].length - 1; j += 1)
        		{
        			k[i][j] = Area * k[i][j] * thick;
        		}
        	}
    	}
    	else if (Type.equals(ElemTypes[8]))
    	{
    		k = ElementStiffnessMatrix.SMStiffnessMatrix(ElemSize, Db, Ds, Sec);
    		/*double[][] kb = NumIntegration(Node, Elem, UserDefinedMat[Elem.getMat()], UserDefinedSec[Elem.getSec()], DOFsPerNode, NonlinearMat, NonlinearGeo, strain, 3);
    		double[][] ks = NumIntegration(Node, Elem, UserDefinedMat[Elem.getMat()], UserDefinedSec[Elem.getSec()], DOFsPerNode, NonlinearMat, NonlinearGeo, strain, 3);
    		k = Util.AddMatrix(kb,  ks);*/
    	}
    	else if (Type.equals(ElemTypes[9]))
    	{
    		k = ElementStiffnessMatrix.SM8StiffnessMatrix(ElemSize, Db, Ds, Sec);
    	}
    	else if (Type.equals(ElemTypes[10]))
    	{
    		k = ElementStiffnessMatrix.KP3StiffnessMatrix(ElemSize, Db, Sec);
    	}
    	else if (Type.equals(ElemTypes[11]))
    	{
    		k = ElementStiffnessMatrix.SM_CStiffnessMatrix(ElemSize, Db, Ds, Sec);
    	}
    	else if (Type.equals(ElemTypes[12]))
    	{
    		k = ElementStiffnessMatrix.SM_HStiffnessMatrix(ElemSize, Db, Ds, Sec);
    	}
    	else
    	{
    		System.out.println("Elem type not identified at Analysis ->  ElementStiffnessMatrix");
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
    
    public double[] StrainsOnPoint(Nodes[] Node, double e, double n, double[] U, double[] Sec, boolean NonlinearGeo)
	{
		double[] strain = new double[3];
		
		double[] u = DispVec(Node, U);
		//double[][] B = ElementStuff.SecondDerivativesb(e, n, Node, Elem, ElemTypes, Sec, NonlinearGeo);
		double[][] B = SecondDerivativesb(e, n, Node, Sec, NonlinearGeo);		
		strain = Util.MultMatrixVector(B, u);
		
		return strain;
	}
    
    public double[] ForcesOnPoint(Nodes[] Node, int[] StrainsOnElem, double[] Mat, double[] Sec, boolean NonlinearMat, double e, double n, double[] U, boolean NonlinearGeo)
	{
		double[] forces = new double[3];
		double thick = Sec[0] / 1000.0;
    	double[] strain = StrainsOnPoint(Node, e, n, U, Sec, NonlinearGeo);
    	double[][] Db = BendingConstitutiveMatrix(Mat, NonlinearMat, strain);
    	double[][] Ds = ShearConstitutiveMatrix(Mat, Sec);

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

    public double[] StressesOnPoint(Nodes[] Node, Elements Elem, double e, double n, double[] Mat, int[][] DOFsOnNode, double[] U, double[] Sec, boolean NonlinearMat, boolean NonlinearGeo)
	{
		double[] strain = new double[3];
		double[] sigma = new double[3];
		
		strain = StrainsOnPoint(Node, e, n, U, Sec, NonlinearGeo);
    	double[][] D = Elem.BendingConstitutiveMatrix(Mat, NonlinearMat, strain);
		sigma = Util.MultMatrixVector(D, strain);
		
		return sigma;
	}
          
  	public double[] DispVec(Nodes[] Node, double[] U)
	{
		int ulength = 0;
		for (int i = 0; i <= ExternalNodes.length - 1; i += 1)
		{
			int node = ExternalNodes[i];
			ulength += Node[node].getDOFType().length;
		}
		
		double[] u = new double[ulength];
		for (int elemnode = 0; elemnode <= ExternalNodes.length - 1; elemnode += 1)
    	{
			int node = ExternalNodes[elemnode];
			for (int dof = 0; dof <= Node[node].getDOFType().length - 1; dof += 1)
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
	
	public double[] ForceVec(Nodes[] Node, boolean NonlinearMat, boolean NonlinearGeo, double[] U)
	{
		int Nnodes = ExternalNodes.length;
		int[] ElemDOFs = DOFs;
		double[] p = new double[Nnodes * ElemDOFs.length];
		
		double[][] k = StiffnessMatrix(Node, NonlinearMat, NonlinearGeo);
		double[] u = DispVec(Node, U);

		p = Util.MultMatrixVector(k, u);
		
		return p;
	}
	
	public double[] StrainVec(Nodes[] Node, double[] U, boolean NonlinearGeo)
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
			strains[node] = StrainsOnPoint(Node, e[node], n[node], U, Sec, NonlinearGeo);
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
		
	public double[] StressesVec(Nodes[] Node, double[] U, boolean NonlinearMat, boolean NonlinearGeo)
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
    	double[][] Db = BendingConstitutiveMatrix(Mat, NonlinearMat, strainvec);
    	double[][] Ds = ShearConstitutiveMatrix(Mat, Sec);
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
    		strains[node] = StrainsOnPoint(Node, e[node], n[node], U, Sec, NonlinearGeo);
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
	
	public double[] InternalForcesVec(Nodes[] Node, double[] U, boolean NonlinearMat, boolean NonlinearGeo)
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
    		forces[node] = ForcesOnPoint(Node, StrainsOnElem, Mat, Sec, NonlinearMat, e[node], n[node], U, NonlinearGeo);
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

	public void RecordResults(Nodes[] Node, double[] U, boolean NonlinearMat, boolean NonlinearGeo)
	{
		setDisp(DispVec(Node, U));
		setStrain(StrainVec(Node, U, NonlinearGeo));
		setStress(StressesVec(Node, U, NonlinearMat, NonlinearGeo));
		setIntForces(InternalForcesVec(Node, U, NonlinearMat, NonlinearGeo));
    
	}
}
