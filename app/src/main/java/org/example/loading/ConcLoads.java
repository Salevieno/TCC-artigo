package org.example.loading;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Arrays;

import org.example.structure.Node;
import org.example.userInterface.DrawingOnAPanel;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

public class ConcLoads
{
	private int ID;
	private int NodeID;		// Node
	private double[] Loads;	// Loads [Fx, Fy, Fz, Mx, My, Mz]

	private static int maxDisplaySize = 1;
	private static int stroke = 2;
	public static Color color = Menus.palette[7];

	public ConcLoads(int ID, Node Node, double[] Loads)
	{
		this(ID, Node.getID(), Loads);
	}

	public ConcLoads(int ID, int NodeID, double[] Loads)
	{
		this.ID = ID;
		this.NodeID = NodeID;
		this.Loads = Loads;
	}

	public void display(int[] ElemDOFs, boolean ShowValues, double maxLoad, boolean deformed, double defScale, MyCanvas canvas, DrawingOnAPanel DP)
	{
		Node node = MainPanel.structure.getMesh().getNodes().get(NodeID) ;
		double[] point = deformed ? Util.ScaledDefCoords(node.getOriginalCoords(), node.getDisp(), ElemDOFs, defScale) : node.getOriginalCoords().asArray();

		for (int dof = 0; dof <= ElemDOFs.length - 1; dof += 1)
		{
			if (Loads.length <= ElemDOFs[dof]) { continue ;}

			double LoadIntensity = Loads[ElemDOFs[dof]];
			if (0 < Math.abs(LoadIntensity))
			{
				int displaySize = (int)(maxDisplaySize * LoadIntensity / maxLoad);
				if (ElemDOFs[dof] <= 2)
				{
					DP.DrawPL3D(point, displaySize, stroke, canvas.getAngles(), ElemDOFs[dof], color, canvas);
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
				DP.DrawLoadValues(new int[] {drawingDefCoords.x, drawingDefCoords.y, 0}, ElemDOFs, dof, LoadIntensity, color);
			}
		}
		
	}

	public int getID() {return ID;}
	public int getNodeID() {return NodeID;}
	public double[] getLoads() {return Loads;}
	public void setID(int I) {ID = I;}
	public void setNodeID(int N) {NodeID = N;}
	public void setLoads(double[] L) {Loads = L;}

	@Override
	public String toString() {
		return ID + "	" + NodeID + "	" + Arrays.toString(Loads) ;
	}

}
