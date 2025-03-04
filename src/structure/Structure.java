package structure;

import java.awt.Color;
import Main.Analysis;
import Utilidades.Util;

public class Structure
{
	private String Name;
	private StructureShape Shape;
	private double[][] Coords;	// Structure edge coordinates [x, y, z]
	private double[] Center;	// Structure center coordinates [x, y, z]
	private double[] MinCoords;	// Structure minimum coordinates [x, y, z]
	private double[] MaxCoords;	// Structure minimum coordinates [x, y, z]
	private double[] Size;		// Structure size in the directions of the axis [x, y, z]
	
	private double[][] K;		// Stiffness matrix
	private double[] P;			// Load vector
	private double[] U;			// Displacement vector
	
	public int NFreeDOFs;
	private double[] DispMin;
	private double[] DispMax;
	private double[] StrainMin;
	private double[] StrainMax;
	private double[] StressMin;
	private double[] StressMax;
	private double[] InternalForcesMin;
	private double[] InternalForcesMax;
	private Reactions[] Reactions;
	private double[] SumReactions;
	private double[][][][] LoadDisp;
	
	public static Color color = Util.ColorPalette()[5];
	
	public Structure(String Name, StructureShape Shape, double[][] Coords)
	{
		this.Name = Name;
		this.Shape = Shape;
		this.Coords = Coords;
		Center = null;
		if (Coords != null)
		{
			Center = Util.MatrixAverages(Coords);
		}
		MinCoords = null;
		MaxCoords = null;
		Size = null;
		//LoadDisp = new double[DOFsPerNode[0].length][Node.length][2][1];
	}

	public String getName() {return Name;}
	public StructureShape getShape() {return Shape;}
	public double[][] getCoords() {return Coords;}
	public double[] getCenter() {return Center;}
	public double[] getMinCoords() {return MinCoords;}
	public double[] getMaxCoords() {return MaxCoords;}
	public double[] getSize() {return Size;}
	public double[][] getK() {return K;}
	public double[] getP() {return P;}
	public double[] getU() {return U;}
	public double[] getDispMin() {return DispMin;}
	public double[] getDispMax() {return DispMax;}
	public double[] getStrainMin() {return StrainMin;}
	public double[] getStrainMax() {return StrainMax;}
	public double[] getStressMin() {return StressMin;}
	public double[] getStressMax() {return StressMax;}
	public double[] getInternalForcesMin() {return InternalForcesMin;}
	public double[] getInternalForcesMax() {return InternalForcesMax;}
	public Reactions[] getReactions() {return Reactions;}
	public double[] getSumReactions() {return SumReactions;}
	public double[][][][] getLoadDisp() {return LoadDisp;}
	public void setName(String N) {Name = N;}
	public void setShape(StructureShape S) {Shape = S;}
	public void setCoords(double[][] C) {Coords = C;}
	public void setCenter(double[] C) {Center = C;}
	public void setMinCoords(double[] MinC) {MinCoords = MinC;}
	public void setMaxCoords(double[] MaxC) {MaxCoords = MaxC;}
	public void setSize(double[] S) {Size = S;}
	public void setK(double[][] k) {K = k;}
	public void setP(double[] p) {P = p;}
	public void setU(double[] u) {U = u;}
	public void setDispMin(double[] D) {DispMin = D;}
	public void setDispMax(double[] D) {DispMax = D;}
	public void setStrainMin(double[] S) {StrainMin = S;}
	public void setStrainMax(double[] S) {StrainMax = S;}
	public void setStressMin(double[] S) {StressMin = S;}
	public void setStressMax(double[] S) {StressMax = S;}
	public void setInternalForcesMin(double[] I) {InternalForcesMin = I;}
	public void setInternalForcesMax(double[] I) {InternalForcesMax = I;}
	public void setReactions(Reactions[] R) {Reactions = R;}
	public void setSumReactions(double[] S) {SumReactions = S;}
	public void setLoadDisp(double[][][][] L) {LoadDisp = L;}
	
	public int[][] NodeDOF(Nodes[] Node, Supports[] Sup)
    {
        int[][] nodeDOF = new int[Node.length][];
        boolean NodeHasSup;
        int supID;
        int cont = 0;
        for (int node = 0; node <= Node.length - 1; node += 1)
    	{
        	nodeDOF[node] = new int[Node[node].getDOFType().length];
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
    	    for (int dof = 0; dof <= Node[node].getDOFType().length - 1; dof += 1)
    	    {
        	    if (NodeHasSup)
        	    {
        	    	if (Node[node].getDOFType()[dof] <= Sup[supID].getDoFs().length - 1)
        	    	{
            	        if (Sup[supID].getDoFs()[Node[node].getDOFType()[dof]] == 0)
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
	public void RecordResults(Nodes[] Node, Element[] Elem, Supports[] Sup, double[] U, boolean NonlinearMat, boolean NonlinearGeo)
	{
		double[][][] ElemStrains = new double[Elem.length][][];
	    double[][][] ElemStresses = new double[Elem.length][][];
	    double[][][] ElemInternalForces = new double[Elem.length][][];
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			int NNodesOnElem = Elem[elem].getExternalNodes().length;
			ElemStrains[elem] = new double[NNodesOnElem][Elem[elem].getStrainTypes().length];
			ElemStresses[elem] = new double[NNodesOnElem][Elem[elem].getStrainTypes().length];
			ElemInternalForces[elem] = new double[NNodesOnElem][Elem[elem].getStrainTypes().length];
			for (int elemnode = 0; elemnode <= NNodesOnElem - 1; elemnode += 1)
			{
				int NumberOfDOFsOnNode = Node[Elem[elem].getExternalNodes()[elemnode]].getDOFType().length;
				for (int dof = 0; dof <= Elem[elem].getStrainTypes().length - 1; dof += 1)
				{
					int ID = elemnode * NumberOfDOFsOnNode + dof;
					ElemStrains[elem][elemnode][dof] = Elem[elem].getStrain()[ID];
					ElemStresses[elem][elemnode][dof] = Elem[elem].getStress()[ID];
					ElemInternalForces[elem][elemnode][dof] = Elem[elem].getIntForces()[ID];
				}
			}
		}
        int[] DOFTypesOnNode = Analysis.DefineFreeDoFTypes(Node, Elem, Sup);
		DispMin = Util.FindMinDisps(U, Elem[0].getDOFs(), DOFTypesOnNode);
		DispMax = Util.FindMaxDisps(U, Elem[0].getDOFs(), DOFTypesOnNode);
		StrainMin = Util.FindMinElemProp(ElemStrains, Elem.length, Elem[0].getStrainTypes().length);
		StrainMax = Util.FindMaxElemProp(ElemStrains, Elem.length, Elem[0].getStrainTypes().length);
		StressMin = Util.FindMinElemProp(ElemStresses, Elem.length, Elem[0].getStrainTypes().length);
		StressMax = Util.FindMaxElemProp(ElemStresses, Elem.length, Elem[0].getStrainTypes().length);
		InternalForcesMin = Util.FindMinElemProp(ElemInternalForces, Elem.length, Elem[0].getStrainTypes().length);
		InternalForcesMax = Util.FindMaxElemProp(ElemInternalForces, Elem.length, Elem[0].getStrainTypes().length);
		
		double[][] strains = new double[Elem.length][3];
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			strains[elem] = Elem[elem].getStrain();
		}
		for (int node = 0; node <= Node.length - 1; node += 1)
	    {
			if (Analysis.NodeForces(node, Node, Elem, NonlinearMat, NonlinearGeo, U) != null)
			{
		    	Node[node].AddConcLoads(Analysis.NodeForces(node, Node, Elem, NonlinearMat, NonlinearGeo, U));
			}
	    }
		Reactions = Analysis.Reactions(Node, Elem, Sup, NonlinearMat, NonlinearGeo, U);
		SumReactions = Analysis.SumReactions(Reactions);		
	}
}
