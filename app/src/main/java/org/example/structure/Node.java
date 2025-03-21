package org.example.structure;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import org.example.loading.ConcLoads;
import org.example.loading.NodalDisps;
import org.example.userInterface.DrawingOnAPanel;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;

public class Node
{
	private int ID;				// ID
	private Point3D coords;		// undeformed coordinates
	private double[] Disp;		// Displacements [ux, uy, uz]
	private int[] Sup;			// Support in the node
	private ConcLoads[] ConcLoad;	// Concentrated loads in the node
	private NodalDisps[] NodalDisp;// Nodal displacements in the node
		
	public int[] dofs;
	private int[] DOFType;			// DOFs on node
	public double[][][] LoadDisp;		// Load displacement curve of the node [dof][x values][y values]
	
	public static int size = 6;
	public static int stroke = 1;
	public static Color color = Menus.palette[10];

	public Node(int ID, Point3D coords)
	{
		this.ID = ID;
		this.coords = coords;
		Disp = coords != null ? coords.asArray() : null;
		Sup = null;
		ConcLoad = null;
		NodalDisp = null;
		DOFType = null;
	}

	
	public double[] deformedPos()
	{
		return new double[] {coords.x + Disp[0], coords.y + Disp[1], coords.z + Disp[2]} ;
	}

	public Point deformedDrawingPos(MyCanvas canvas, double defScale)
	{
		Point2D.Double canvasCenter = canvas.inRealCoords(new Point(canvas.getCenter()[0], canvas.getCenter()[1])) ;
		double[] deformedCoords = Util.ScaledDefCoords(coords.asArray(), Disp, dofs, defScale);
		double[] rotatedCoord = Util.RotateCoord(deformedCoords, new double[] {canvasCenter.x, canvasCenter.y}, canvas.getAngles()) ;
		return canvas.inDrawingCoords(new Point2D.Double(rotatedCoord[0], rotatedCoord[1])) ;
	}
	
	public Point undeformedDrawingPos(MyCanvas canvas)
	{
		Point2D.Double canvasCenter = canvas.inRealCoords(new Point(canvas.getCenter()[0], canvas.getCenter()[1])) ;
		double[] rotatedCoord = Util.RotateCoord(coords.asArray(), new double[] {canvasCenter.x, canvasCenter.y}, canvas.getAngles()) ;
		return canvas.inDrawingCoords(new Point2D.Double(rotatedCoord[0], rotatedCoord[1])) ;
	}



	public void display(MyCanvas canvas, int[] dofs, boolean deformed, double defScale, boolean selected, DrawingOnAPanel DP)
	{
		Point drawingCoords = deformed ? deformedDrawingPos(canvas, defScale) : undeformedDrawingPos(canvas) ;
		DP.DrawCircle(drawingCoords, size, stroke, false, true, Color.black, color);
		// if (selectedNodes != null)
		// {
		// 	for (int i = 0; i <= selectedNodes.size() - 1; i += 1)
		// 	{
		// 		if (node == selectedNodes.get(i).getID())
		// 		{
		// 			DP.DrawCircle(drawingCoords.get(node), 2*size, stroke, false, true, Color.black, Color.red);
		// 		}
		// 	}
		// }
	}
	
	public int getID() {return ID;}
	public Point3D getOriginalCoords() {return coords;}
	public double[] getDisp() {return Disp;}
	public int[] getSup() {return Sup;}
	public ConcLoads[] getConcLoads() {return ConcLoad;}
	public NodalDisps[] getNodalDisps() {return NodalDisp;}
	public int[] getDOFs() { return dofs ;}
	public int[] getDOFType() {return DOFType;}
	public void setID(int I) {ID = I;}
	public void setOriginalCoords(Point3D C) {coords = C;}
	public void setDisp(double[] C) {Disp = C;}
	public void setSup(int[] S) {Sup = S;}
	public void setConcLoads(ConcLoads[] C) {ConcLoad = C;}
	public void setNodalDisps(NodalDisps[] D) {NodalDisp = D;}
	public void setDOFType(int[] D) {DOFType = D;}
	
	public void calcdofs(List<Supports> Sup, int cont)
	{
        boolean NodeHasSup;
        int supID;
    	dofs = new int[DOFType.length];
	    NodeHasSup = false;
	    supID = -1;
        for (int sup = 0; sup <= Sup.size() - 1; sup += 1)
	    {
	        if (Sup.get(sup).getNode() == ID)
	        {
	            NodeHasSup = true;
	            supID = sup;
	        }
	    }
        for (int dof = 0; dof <= DOFType.length - 1; dof += 1)
	    {
    	    if (NodeHasSup)
    	    {
    	    	if (DOFType[dof] <= Sup.get(supID).getDoFs().length - 1)
    	    	{
        	        if (Sup.get(supID).getDoFs()[DOFType[dof]] == 0)
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
	public void addConcLoad(ConcLoads newConcLoad)
	{
		ConcLoad = Util.IncreaseArraySize(ConcLoad, 1);
		ConcLoad[ConcLoad.length - 1] = newConcLoad;
	}
	public void addNodalDisp(NodalDisps newNodalDisp)
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

	@Override
	public String toString()
	{
		return ID + "	(" + coords.toString() + ")	" + Arrays.toString(Sup) + "	" + Arrays.toString(ConcLoad) + "	" + Arrays.toString(NodalDisp) + "	" + Arrays.toString(DOFType) ;
	}

	// @Override
	// public String toString()
	// {
	// 	return ID + "	(" + coords.toString() + ")	" + Arrays.toString(Disp)
	// 			+ "	" + Arrays.toString(Sup) + "	" + Arrays.toString(ConcLoad) + "	"
	// 			+ Arrays.toString(NodalDisp) + "	" + Arrays.toString(dofs) + "	"
	// 			+ Arrays.toString(DOFType) + "	" + Arrays.toString(LoadDisp);
	// }
}
