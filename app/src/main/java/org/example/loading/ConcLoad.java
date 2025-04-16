package org.example.loading;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.example.Main;
import org.example.structure.Mesh;
import org.example.userInterface.Draw;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;

import graphics.DrawPrimitives;

public class ConcLoad
{
	private int id ;
	private Force force ;

	private static int currentID ;
	private static int maxDisplaySize ;
	private static int stroke ;
	public static Color color ;
	private static List<Force> types ;

	static
	{
		currentID = 1;
		maxDisplaySize = 1;
		stroke = 2;
		color = Main.palette[7];
		types = new ArrayList<>() ;
	}

	public ConcLoad(Force force)
	{
		this.id = currentID;
		this.force = force;
		currentID += 1;
	}

	public ConcLoad(double[] loads)
	{
		this(new Force(loads)) ;
	}
	
	public static List<Force> getTypes() { return types ;}
	public static void resetTypes() { types = new ArrayList<>() ;}
	public static void addType(Force type) { types.add(type) ;}
	public static void removeType(Force type) { types.remove(type) ;}


	public static void DrawPL3D(Point3D RealPos, double size, int thickness, double[] CanvasAngles, int dof, Color color, MyCanvas canvas, DrawPrimitives DP)
    {
		Draw.DrawArrow3Dto(RealPos, thickness, Mesh.dofAngles(dof), size, color, canvas, DP);
    }

	public void display(Point3D rotatedPoint, int[] ElemDOFs, boolean ShowValues, double maxLoad, boolean deformed, double defScale, MyCanvas canvas, DrawPrimitives DP)
	{
		for (int dof = 0; dof <= ElemDOFs.length - 1; dof += 1)
		{
			if (Force.qtdDOFs <= ElemDOFs[dof]) { continue ;}

			double LoadIntensity = force.array()[ElemDOFs[dof]];
			if (0 < Math.abs(LoadIntensity))
			{
				int displaySize = (int)(maxDisplaySize * LoadIntensity / maxLoad);
				if (ElemDOFs[dof] <= 2)
				{
					DrawPL3D(rotatedPoint, displaySize, stroke, canvas.getAngles().asArray(), ElemDOFs[dof], color, canvas, DP);
				}
				else
				{
					//DrawMoment3D(DrawingDefCoords, thickness, canvas.getAngles(), DOFsPerNode[dof], true, size, size / 4.0, color);
				}
			}
			if (ShowValues)
			{
				// int[] DrawingDefCoords = Util.ConvertToDrawingCoords2Point3D(point, DP.getRealStructCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
				Point drawingDefCoords = canvas.inDrawingCoords(rotatedPoint.asDoublePoint()) ;
				Draw.DrawLoadValues(new int[] {drawingDefCoords.x, drawingDefCoords.y, 0}, ElemDOFs, dof, LoadIntensity, color, DP);
			}
		}
		
	}

	public int getId() {return id;}
	public Force getForce() {return force;}
	public void setId(int I) {id = I;}
	public void setForce(Force force) {this.force = force;}

	@Override
	public String toString() {
		return id + "	" + force ;
	}

}
