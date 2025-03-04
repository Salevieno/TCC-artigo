package structure;

import java.awt.Color;

import Utilidades.Util;

public class Reactions
{
	private int ID;
	private int Node;		// Node
	private double[] Loads;	// Loads [Fx, Fy, Fz, Mx, My, Mz]

	public static Color color = Util.ColorPalette()[8];
	public static double[] SumReactions = new double[6];
	
	public Reactions(int ID, int Node, double[] Loads)
	{
		this.ID = ID;
		this.Node = Node;
		this.Loads = Loads;
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
