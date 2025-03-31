package org.example.loading;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import org.example.userInterface.Draw;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;

import graphics.DrawPrimitives;

public class ConcLoad
{
	private int id ;
	private Force force ;

	private static int currentID = 1;
	private static int maxDisplaySize = 1;
	private static int stroke = 2;
	public static Color color = Menus.palette[7];

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


	public static void DrawPL3D(double[] RealPos, double size, int thickness, double[] CanvasAngles, int dof, Color color, MyCanvas canvas, DrawPrimitives DP)
    {
		double[] angle ;
		switch (dof)
		{
			case 0: angle = new double[] {0, 0, 0} ; break ;
			case 1: angle = new double[] {0, 0, 0 - Math.PI/2.0} ; break ;
			case 2: angle = new double[] {0, 0 + Math.PI/2.0, 0} ; break ;
		
			default: return ;
		}
		Draw.DrawArrow3Dto(RealPos, thickness, angle, size, color, canvas, DP);
    }

	public void display(double[] rotatedPoint, int[] ElemDOFs, boolean ShowValues, double maxLoad, boolean deformed, double defScale, MyCanvas canvas, DrawPrimitives DP)
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
					DrawPL3D(rotatedPoint, displaySize, stroke, canvas.getAngles(), ElemDOFs[dof], color, canvas, DP);
				}
				else
				{
					//DrawMoment3D(DrawingDefCoords, thickness, canvas.getAngles(), DOFsPerNode[dof], true, size, size / 4.0, color);
				}
			}
			if (ShowValues)
			{
				// int[] DrawingDefCoords = Util.ConvertToDrawingCoords2Point3D(point, DP.getRealStructCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
				Point drawingDefCoords = canvas.inDrawingCoords(new Point2D.Double(rotatedPoint[0], rotatedPoint[1])) ;
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
