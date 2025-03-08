package main.structure;

import java.awt.Color;

import main.output.Results;
import main.utilidades.Point3D;
import main.utilidades.Util;

public class Structure
{
	private String Name;
	private StructureShape Shape;
	private double[][] Coords;	// Structure edge coordinates [x, y, z]
	private Point3D center;
	private Point3D minCoords;
	private Point3D maxCoords;

	private double[][] K;		// Stiffness matrix
	private double[] P;			// Load vector
	private double[] U;			// Displacement vector
	
	public int NFreeDOFs;
	private Results results ;
	
	public static Color color = Util.ColorPalette()[5];
	
	public Structure(String Name, StructureShape Shape, double[][] Coords)
	{
		this.Name = Name;
		this.Shape = Shape;
		this.Coords = Coords;
		center = null;
		if (Coords != null)
		{
			center = Util.MatrixAveragesToPoint3D(Coords);
		}
		minCoords = null;
		maxCoords = null;
		results = new Results() ;
	}

	public String getName() {return Name;}
	public StructureShape getShape() {return Shape;}
	public double[][] getCoords() {return Coords;}
	public Point3D getCenter() {return center;}
	public Point3D getMinCoords() {return minCoords;}
	public Point3D getMaxCoords() {return maxCoords;}
	public double[][] getK() {return K;}
	public double[] getP() {return P;}
	public double[] getU() {return U;}
	public Results getResults() {return results;}
	public void setName(String N) {Name = N;}
	public void setShape(StructureShape S) {Shape = S;}
	public void setCoords(double[][] C) {Coords = C;}
	public void setCenter(Point3D C) {center = C;}
	public void setMinCoords(Point3D MinC) {minCoords = MinC;}
	public void setMaxCoords(Point3D MaxC) {maxCoords = MaxC;}
	public void setK(double[][] k) {K = k;}
	public void setP(double[] p) {P = p;}
	public void setU(double[] u) {U = u;}
	
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
	
}
