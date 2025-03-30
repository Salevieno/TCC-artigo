package org.example.structure;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.loading.ConcLoad;
import org.example.loading.Force;
import org.example.loading.NodalDisp;
import org.example.userInterface.Draw;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

import graphics.Align;
import graphics.DrawPrimitives;

public class Node
{
	private int ID;				// ID
	private Point3D coords;		// undeformed coordinates
	private Point drawingPos;
	private Point3D disp;		// Displacements [ux, uy, uz]
	private int[] Sup;			// Support in the node
	private List<ConcLoad> concLoads;	// Concentrated loads in the node
	private List<NodalDisp> nodalDisps;// Nodal displacements in the node
		
	public int[] dofs;
	private int[] DOFType;			// DOFs on node
	public double[][][] LoadDisp;		// Load displacement curve of the node [dof][x values][y values]
	private boolean isSelected ;
	
	public static int size = 6;
	public static int stroke = 1;
	public static Color color = Menus.palette[10];

	public Node(int ID, Point3D coords)
	{
		this.ID = ID;
		this.coords = coords;
		this.drawingPos = null ;
		this.disp = new Point3D(0, 0, 0) ;
		Sup = null;
		concLoads = null;
		nodalDisps = null;
		DOFType = null;
		isSelected = false ;
	}

	
	public double[] deformedPos()
	{
		return new double[] {coords.x + disp.x, coords.y + disp.y, coords.z + disp.z} ;
	}

	public Point deformedDrawingPos(MyCanvas canvas, int[] dofs, double defScale)
	{
		Point2D.Double canvasCenter = canvas.inRealCoords(new Point(canvas.getCenter()[0], canvas.getCenter()[1])) ;
		double[] deformedCoords = Util.ScaledDefCoords(coords, disp, dofs, defScale);
		double[] rotatedCoord = Util.RotateCoord(deformedCoords, new double[] {canvasCenter.x, canvasCenter.y}, canvas.getAngles()) ;
		return canvas.inDrawingCoords(new Point2D.Double(rotatedCoord[0], rotatedCoord[1])) ;
	}
	
	public Point undeformedDrawingPos(MyCanvas canvas)
	{
		Point2D.Double canvasCenter = canvas.inRealCoords(new Point(canvas.getCenter()[0], canvas.getCenter()[1])) ;
		double[] rotatedCoord = Util.RotateCoord(coords.asArray(), new double[] {canvasCenter.x, canvasCenter.y}, canvas.getAngles()) ;
		return canvas.inDrawingCoords(new Point2D.Double(rotatedCoord[0], rotatedCoord[1])) ;
	}

	public void updateDrawingPos(MyCanvas canvas, boolean deformed, double defScale)
	{
		drawingPos = deformed ? deformedDrawingPos(canvas, dofs, defScale) : undeformedDrawingPos(canvas) ;
	}

	public void displayNumber(MyCanvas canvas, boolean deformed, DrawPrimitives DP)
	{
		int offset = 6;
		Point numberPos = new Point(drawingPos) ;
		numberPos.translate(offset, offset) ;
		DP.drawText(numberPos, Align.topLeft, String.valueOf(ID), Node.color) ;	
	}

	public void displayConcLoads(int[] ElemDOFs, boolean ShowValues, double maxLoad, boolean deformed, double defScale, MyCanvas canvas, DrawPrimitives DP)
	{
		Point2D.Double canvasCenter = canvas.inRealCoords(new Point(canvas.getCenter()[0], canvas.getCenter()[1])) ;
		double[] rotatedCoord = Util.RotateCoord(coords.asArray(), new double[] {canvasCenter.x, canvasCenter.y}, canvas.getAngles()) ;
		concLoads.forEach(load -> load.display(rotatedCoord, ElemDOFs, ShowValues, maxLoad, deformed, defScale, canvas, DP)) ;
	}


	public void display(MyCanvas canvas, int[] dofs, boolean deformed, double defScale, boolean selected, DrawPrimitives DP)
	{
		// Point drawingCoords = deformed ? deformedDrawingPos(canvas, dofs, defScale) : undeformedDrawingPos(canvas) ;
		int displaySize = isSelected ? 2 * size : size ;
		Color displayColor = isSelected ? Menus.palette[4] : color ;
		DP.drawCircle(drawingPos, displaySize, stroke, Menus.palette[0], displayColor);
	}


	public void select() { isSelected = true ;}

	public void unselect() { isSelected = false ;}
	
	public boolean isSelected() { return isSelected ;}


	public int getID() {return ID;}
	public Point3D getOriginalCoords() {return coords;}
	public Point3D getDisp() {return disp;}
	public int[] getSup() {return Sup;}
	public List<ConcLoad> getConcLoads() {return concLoads;}
	public List<NodalDisp> getNodalDisps() {return nodalDisps;}
	public int[] getDOFs() { return dofs ;}
	public int[] getDOFType() {return DOFType;}	
	public Point getDrawingPos() { return drawingPos ;}
	public void setID(int I) {ID = I;}
	public void setOriginalCoords(Point3D C) {coords = C;}
	public void setDisp(Point3D C) {disp = C;}
	public void setSup(int[] S) {Sup = S;}
	public void setConcLoads(List<ConcLoad> C) {concLoads = C;}
	public void setNodalDisps(List<NodalDisp> D) {nodalDisps = D;}
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
	public void addConcLoad(ConcLoad newConcLoad)
	{
		if (concLoads == null)
		{
			concLoads = new ArrayList<>() ;
		}
		concLoads.add(newConcLoad);
	}
	public void addNodalDisp(NodalDisp newNodalDisp)
	{
		if (nodalDisps == null)
		{
			nodalDisps = new ArrayList<>() ;
		}
		nodalDisps.add(newNodalDisp);
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
		return ID + "	(" + coords.toString() + ")	" + Arrays.toString(Sup) + "	" + concLoads + "	" + nodalDisps + "	" + Arrays.toString(DOFType) ;
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
