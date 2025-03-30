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
	private int ID;
	private Force force ;

	private static int maxDisplaySize = 1;
	private static int stroke = 2;
	public static Color color = Menus.palette[7];

	public ConcLoad(int ID, Force force)
	{
		this.ID = ID;
		this.force = force;
	}

	public ConcLoad(int ID, double[] loads)
	{
		this(ID, new Force(loads)) ;
	}


	public static void DrawPL3D(double[] RealPos, double size, int thickness, double[] CanvasAngles, int dof, Color color, MyCanvas canvas, DrawPrimitives DP)
    {
    	if (dof == 0)		// Fx
    	{
			double[] angle = new double[] {0, 0, 0};
			Draw.DrawArrow3Dto(RealPos, thickness, angle, size, size / 4.0, color, canvas, DP);
    	}
    	else if (dof == 1)	// Fy
    	{
			double[] angle = new double[] {0, 0, 0 - Math.PI/2.0};
			Draw.DrawArrow3Dto(RealPos, thickness, angle, size, size / 4.0, color, canvas, DP);	
    	}
    	else if (dof == 2)	// Fz
    	{
			double[] angle = new double[] {0, 0 + Math.PI/2.0, 0};
			Draw.DrawArrow3Dto(RealPos, thickness, angle, size, size / 4.0, color, canvas, DP);
    	}
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

	public int getID() {return ID;}
	public Force getForce() {return force;}
	public void setID(int I) {ID = I;}
	public void setForce(Force force) {this.force = force;}

	@Override
	public String toString() {
		return ID + "	" + force ;
	}

}
