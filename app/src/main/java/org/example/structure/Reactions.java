package org.example.structure;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.example.loading.ConcLoad;
import org.example.userInterface.Draw;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

import graphics.DrawPrimitives;

public class Reactions
{
	private int ID;
	private int Node;		// Node
	private double[] Loads;	// Loads [Fx, Fy, Fz, Mx, My, Mz]

	public static Color color = Menus.palette[8];
	public static double[] SumReactions = new double[6];
	
	public Reactions(int ID, int Node, double[] Loads)
	{
		this.ID = ID;
		this.Node = Node;
		this.Loads = Loads;
	}

	
	public static void display3D (List<Node> Node, Reactions[] Reactions, int[] ElemDOFs, boolean ShowValues, Color ReactionsColor,
									boolean condition, double Defscale, MyCanvas canvas, DrawPrimitives DP)
	{
		int MaxArrowSize = 1;
		int thickness = 2;
		double MaxAbsLoad = Util.FindMaxReaction(Reactions);
		for (int l = 0; l <= Reactions.length - 1; l += 1)
		{
			int node = Reactions[l].getNode();
			double[] RealDefCoords = Util.ScaledDefCoords(Node.get(node).getOriginalCoords(), Node.get(node).getDisp(), ElemDOFs, Defscale);
			for (int r = 0; r <= Reactions[l].getLoads().length - 1; r += 1)
			{
				double LoadIntensity = Reactions[l].getLoads()[r];
				if (0 < Math.abs(LoadIntensity))
				{
					double size = MaxArrowSize * LoadIntensity / (double) MaxAbsLoad;
					if (r <= 2)
					{
						ConcLoad.DrawPL3D(new Point3D(RealDefCoords[0], RealDefCoords[1], RealDefCoords[2]), size, thickness, canvas.getAngles(), r, ReactionsColor, canvas, DP);
					}
					else if (r <= 5)
					{
						//DrawMoment3D(DrawingDefCoords, thickness, canvas.getAngles(), DOFsPerNode[dof], true, size, size / 4.0, ReactionsColor);
					}
					if (ShowValues)
					{
						double[] RealTextPos = Arrays.copyOf(RealDefCoords, 3);
						if (r == 0)
						{
							RealTextPos[0] += -2*Math.signum(LoadIntensity);
							RealTextPos[1] += -1*Math.signum(LoadIntensity);
						}
						if (r == 1)
						{
							RealTextPos[0] += 2*Math.signum(LoadIntensity);
						}
						if (r == 2)
						{
							RealTextPos[2] += -0.5*Math.signum(LoadIntensity);
						}
						int[] DrawingDefCoords = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(RealTextPos, MainPanel.structure.getCenter().asArray(), canvas.getAngles()), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());						
						Draw.DrawLoadValues(DrawingDefCoords, ElemDOFs, r, LoadIntensity, ReactionsColor, DP);
					}
				}
			}
		}
	}


	public int getID() {return ID;}
	public int getNode() {return Node;}
	public double[] getLoads() {return Loads;}
	public void setID(int I) {ID = I;}
	public void setNode(int N) {Node = N;}
	public void setLoads(double[] L) {Loads = L;}

	public static void setSumReactions(double[] sumReactions)
	{
		SumReactions = sumReactions;
	}
}
