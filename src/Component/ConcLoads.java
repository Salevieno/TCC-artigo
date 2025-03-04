package Component;

import java.awt.Color;
import java.util.Arrays;

import Utilidades.Util;

public class ConcLoads
{
	private int ID;
	private int Node;		// Node
	private double[] Loads;	// Loads [Fx, Fy, Fz, Mx, My, Mz]

	public static Color color = Util.ColorPalette()[7];
	public ConcLoads(int ID, int Node, double[] Loads)
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

	@Override
	public String toString() {
		return "ConcLoads [ID=" + ID + ", Node=" + Node + ", Loads=" + Arrays.toString(Loads) + "]";
	}

}
