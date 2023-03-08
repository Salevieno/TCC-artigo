package Component;

import java.awt.Color;

import Utilidades.Util;

public class NodalDisps
{
	private int ID;
	private int Node;		// Node
	private double[] Disps;	// Loads [Fx, Fy, Fz, Mx, My, Mz]

	public static Color color = Util.ColorPalette()[7];
	public NodalDisps(int ID, int Node, double[] Disps)
	{
		this.ID = ID;
		this.Node = Node;
		this.Disps = Disps;
	}

	public int getID() {return ID;}
	public int getNode() {return Node;}
	public double[] getDisps() {return Disps;}
	public void setID(int I) {ID = I;}
	public void setNode(int N) {Node = N;}
	public void setDisps(double[] L) {Disps = L;}
}
