package structure;

import java.awt.Color;

import utilidades.Util;

public class Nodes
{
	private int ID;				// ID
	private double[] OriCoords;	// Original coordinates [x, y, z]
	private double[] Disp;		// Displacements [ux, uy, uz]
	private int[] Sup;			// Support in the node
	private ConcLoads[] ConcLoad;	// Concentrated loads in the node
	private NodalDisps[] NodalDisp;// Nodal displacements in the node
	
	public static Color color = Util.ColorPalette()[10];
	
	public int[] dofs;
	private int[] DOFType;			// DOFs on node
	public double[][][] LoadDisp;		// Load displacement curve of the node [dof][x values][y values]

	public Nodes(int ID, double[] OriCoords)
	{
		this.ID = ID;
		this.OriCoords = OriCoords;
		Disp = OriCoords;
		Sup = null;
		ConcLoad = null;
		NodalDisp = null;
		DOFType = null;
	}

	public int getID() {return ID;}
	public double[] getOriginalCoords() {return OriCoords;}
	public double[] getDisp() {return Disp;}
	public int[] getSup() {return Sup;}
	public ConcLoads[] getConcLoads() {return ConcLoad;}
	public NodalDisps[] getNodalDisps() {return NodalDisp;}
	public int[] getDOFType() {return DOFType;}
	public void setID(int I) {ID = I;}
	public void setOriginalCoords(double[] C) {OriCoords = C;}
	public void setDisp(double[] C) {Disp = C;}
	public void setSup(int[] S) {Sup = S;}
	public void setConcLoads(ConcLoads[] C) {ConcLoad = C;}
	public void setNodalDisps(NodalDisps[] D) {NodalDisp = D;}
	public void setDOFType(int[] D) {DOFType = D;}
	
	public void calcdofs(Supports[] Sup, int cont)
	{
        boolean NodeHasSup;
        int supID;
    	dofs = new int[DOFType.length];
	    NodeHasSup = false;
	    supID = -1;
        for (int sup = 0; sup <= Sup.length - 1; sup += 1)
	    {
	        if (Sup[sup].getNode() == ID)
	        {
	            NodeHasSup = true;
	            supID = sup;
	        }
	    }
        for (int dof = 0; dof <= DOFType.length - 1; dof += 1)
	    {
    	    if (NodeHasSup)
    	    {
    	    	if (DOFType[dof] <= Sup[supID].getDoFs().length - 1)
    	    	{
        	        if (Sup[supID].getDoFs()[DOFType[dof]] == 0)
        	        {
        	            dofs[dof] = cont;
        	            cont += 1;
        	        }
        	        else
        	        {
        	        	dofs[dof] = -1;
        	        }
    	    	}
    	        else	// option 1
    	        {
    	        	dofs[dof] = -1;
    	        }
    	    }
    	    else
    	    {
    	    	dofs[dof] = cont;
    	        cont += 1;
    	    }
	    }
	}
	
	public void AddConcLoads(double[] concLoads)
	{
		if (ConcLoad == null)
		{
			ConcLoad = Util.IncreaseArraySize(ConcLoad, 1);
		}
		ConcLoad[ConcLoad.length - 1].setLoads(concLoads);
	}
	public void AddNodalDisps(double[] nodalDisp)
	{
		if (NodalDisp == null)
		{
			NodalDisp = Util.IncreaseArraySize(NodalDisp, 1);
		}
		NodalDisp[NodalDisp.length - 1].setDisps(nodalDisp);
	}
	public void AddConcLoads(ConcLoads newConcLoad)
	{
		ConcLoad = Util.IncreaseArraySize(ConcLoad, 1);
		ConcLoad[ConcLoad.length - 1] = newConcLoad;
	}
	public void AddNodalDisps(NodalDisps newNodalDisp)
	{
		NodalDisp = Util.IncreaseArraySize(NodalDisp, 1);
		NodalDisp[NodalDisp.length - 1] = newNodalDisp;
	}
	public void setLoadDispCurve()
	{
		LoadDisp = new double[DOFType.length][2][];		// [dof][x values][y values]
	}
	public void addLoadDispCurve(double[] U, double loadfactor)
	{
		for (int dof = 0; dof <= DOFType.length - 1; dof += 1)
		{
	    	if (-1 < dofs[dof])
	    	{
		    	LoadDisp[dof][0] = Util.AddElem(LoadDisp[dof][0], U[dofs[dof]]);
		    	LoadDisp[dof][1] = Util.AddElem(LoadDisp[dof][1], loadfactor);
	    	}
	    	else
	    	{
		    	LoadDisp[dof][0] = Util.AddElem(LoadDisp[dof][0], 0);
		    	LoadDisp[dof][1] = Util.AddElem(LoadDisp[dof][1], 0);
	    	}
		}
	}
}
