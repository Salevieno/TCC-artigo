package org.example.loading;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Arrays;

import org.example.structure.Node;
import org.example.userInterface.Draw;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

import graphics.DrawPrimitives;

public class ConcLoad
{
	private int ID;
	private int NodeID;
	private Force force ;

	private static int maxDisplaySize = 1;
	private static int stroke = 2;
	public static Color color = Menus.palette[7];

	public ConcLoad(int ID, Node Node, double[] Loads)
	{
		this(ID, Node.getID(), Loads);
	}

	public ConcLoad(int ID, Node Node, Force force)
	{
		this(ID, Node.getID(), force);
	}

	public ConcLoad(int ID, int NodeID, double[] loads)
	{
		this.ID = ID;
		this.NodeID = NodeID;
		this.force = new Force(loads);
	}

	public ConcLoad(int ID, int NodeID, Force force)
	{
		this.ID = ID;
		this.NodeID = NodeID;
		this.force = force;
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

	public void display(int[] ElemDOFs, boolean ShowValues, double maxLoad, boolean deformed, double defScale, MyCanvas canvas, DrawPrimitives DP)
	{
		Node node = MainPanel.structure.getMesh().getNodes().get(NodeID) ;
		double[] point = deformed ? Util.ScaledDefCoords(node.getOriginalCoords(), node.getDisp(), ElemDOFs, defScale) : node.getOriginalCoords().asArray();

		for (int dof = 0; dof <= ElemDOFs.length - 1; dof += 1)
		{
			if (Force.qtdDOFs <= ElemDOFs[dof]) { continue ;}

			double LoadIntensity = force.array()[ElemDOFs[dof]];
			if (0 < Math.abs(LoadIntensity))
			{
				int displaySize = (int)(maxDisplaySize * LoadIntensity / maxLoad);
				if (ElemDOFs[dof] <= 2)
				{
					DrawPL3D(point, displaySize, stroke, canvas.getAngles(), ElemDOFs[dof], color, canvas, DP);
				}
				else
				{
					//DrawMoment3D(DrawingDefCoords, thickness, canvas.getAngles(), DOFsPerNode[dof], true, size, size / 4.0, color);
				}
			}
			if (ShowValues)
			{
				// int[] DrawingDefCoords = Util.ConvertToDrawingCoords2Point3D(point, DP.getRealStructCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
				Point drawingDefCoords = canvas.inDrawingCoords(new Point2D.Double(point[0], point[1])) ;
				Draw.DrawLoadValues(new int[] {drawingDefCoords.x, drawingDefCoords.y, 0}, ElemDOFs, dof, LoadIntensity, color, DP);
			}
		}
		
	}

	public int getID() {return ID;}
	public int getNodeID() {return NodeID;}
	public Force getForce() {return force;}
	public void setID(int I) {ID = I;}
	public void setNodeID(int N) {NodeID = N;}
	public void setForce(Force force) {this.force = force;}

	@Override
	public String toString() {
		return ID + "	" + NodeID + "	" + force ;
	}

}
