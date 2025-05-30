package org.example.structure;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.Main;
import org.example.loading.ConcLoad;
import org.example.loading.DOF;
import org.example.loading.NodalDisp;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;

import charts.Dataset;
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
		
	private int[] dofs;
	private int[] DOFType;			// DOFs on node
	private double[][][] LoadDisp;		// Load displacement curve of the node [dof][x values][y values]
	private Map<DOF, Dataset> loadDisp ;
	private boolean isSelected ;
	
	public static int size = 6;
	public static int stroke = 1;
	public static Color color = Main.palette[10];

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

	public Node(NodeDTO dto)
	{
		this.ID = dto.getID() ;
		this.coords = dto.getCoords() ;
		this.Sup = dto.getSup() ;
		this.concLoads = dto.getConcLoad() ;
		this.nodalDisps = dto.getNodalDisp() ;
		this.dofs = dto.getDofs() ;
	}

	public Point3D pos(boolean deformed)
	{
		return deformed ? deformedPos() : coords ;
	}
	
	public Point3D deformedPos()
	{
		return new Point3D(coords.x + disp.x, coords.y + disp.y, coords.z + disp.z) ;
	}

	public Point deformedDrawingPos(MyCanvas canvas, int[] dofs, double defScale)
	{
		Point2D.Double canvasCenter = canvas.centerInRealCoords() ;
		double[] deformedCoords = Util.ScaledDefCoords(coords, disp, dofs, defScale);
		double[] rotatedCoord = Util.RotateCoord(deformedCoords, new double[] {canvasCenter.x, canvasCenter.y}, canvas.getAngles().asArray()) ;
		return canvas.inDrawingCoords(new Point2D.Double(rotatedCoord[0], rotatedCoord[1])) ;
	}
	
	public Point undeformedDrawingPos(MyCanvas canvas)
	{
		Point2D.Double canvasCenter = canvas.centerInRealCoords() ;
		double[] rotatedCoord = Util.RotateCoord(coords.asArray(), new double[] {canvasCenter.x, canvasCenter.y}, canvas.getAngles().asArray()) ;
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
		Point2D.Double canvasCenter = canvas.centerInRealCoords() ;
		// double[] rotatedCoord = Util.RotateCoord(coords.asArray(), new double[] {canvasCenter.x, canvasCenter.y}, canvas.getAngles()) ;
		Point3D rotatedCoord = Point3D.rotate(coords, new Point3D(canvasCenter.x, canvasCenter.y, 0.0), canvas.getAngles()) ;
		concLoads.forEach(load -> load.display(rotatedCoord, ElemDOFs, ShowValues, maxLoad, deformed, defScale, canvas, DP)) ;
	}


	public void display(MyCanvas canvas, int[] dofs, boolean deformed, double defScale, boolean selected, DrawPrimitives DP)
	{
		// Point drawingCoords = deformed ? deformedDrawingPos(canvas, dofs, defScale) : undeformedDrawingPos(canvas) ;
		int displaySize = isSelected ? 2 * size : size ;
		Color displayColor = isSelected ? Main.palette[4] : color ;
		DP.drawCircle(drawingPos, displaySize, stroke, Main.palette[0], displayColor);
	}


	public void select() { isSelected = true ;}

	public void unselect() { isSelected = false ;}
	
	public boolean isSelected() { return isSelected ;}


	public int getID() {return ID;}
	public Point3D getOriginalCoords() {return coords;}
	public Point3D getDisp() {return disp;}	
	public int[] getDofs() { return dofs ;}
	public int[] getSup() {return Sup;}
	public List<ConcLoad> getConcLoads() {return concLoads;}
	public List<NodalDisp> getNodalDisps() {return nodalDisps;}
	public int[] getDOFs() { return dofs ;}
	public int[] getDOFType() {return DOFType;}	
	public Point getDrawingPos() { return drawingPos ;}	
	public double[][][] getLoadDisp() {return LoadDisp ;}

	public void setID(int I) {ID = I;}
	public void setOriginalCoords(Point3D C) {coords = C;}
	public void setDisp(Point3D C) {disp = C;}	
	public void setDofs(int[] dofs) { this.dofs = dofs ;}
	public void setSup(int[] S) {Sup = S;}
	public void setConcLoads(List<ConcLoad> C) {concLoads = C;}
	public void setNodalDisps(List<NodalDisp> D) {nodalDisps = D;}
	public void setDOFType(int[] D)
	{
		DOFType = D;
		
		loadDisp = new HashMap<>() ;
		for (int i = 0; i <= DOFType.length - 1; i += 1)
		{
			loadDisp.put(DOF.values()[i], new Dataset()) ;
			loadDisp.get(DOF.values()[i]).addPoint(0, 0) ;
		}
	}
	
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
	public void resetLoadDispCurve()
	{
		LoadDisp = new double[DOFType.length][2][];		// [dof][x values][y values]
	}
	public void addPointToLoadDispCurve(double[] U, double loadfactor)
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

		for (int i = 0; i <= DOFType.length - 1; i += 1)
		{
	    	if (-1 < dofs[i])
	    	{
				loadDisp.get(DOF.values()[i]).addPoint(U[dofs[i]], loadfactor) ;
			}
		}

	}

	public Map<DOF, Dataset> getLoadDisp2() { return loadDisp ;}

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
