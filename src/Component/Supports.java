package Component;

import java.awt.Color;

import Utilidades.Util;

public class Supports
{
	private int ID;
	private int Node;		// Node
	private int[] DoFs;		// Restrained DoFs

	public static int[][] Types = new int[][] {{1, 0, 0, 0, 0, 0}, {0, 1, 0, 0, 0, 0}, {0, 0, 1, 0, 0, 0}, {1, 1, 0, 0, 0, 0}, {1, 1, 0, 0, 0, 1}, {1, 1, 1, 1, 1, 1}};
	public static Color color = Util.ColorPalette()[7];
	
	public Supports(int ID, int Node, int[] DoFs)
	{
		this.ID = ID;
		this.Node = Node;
		this.DoFs = DoFs;
	}

	public int getID() {return ID;}
	public int getNode() {return Node;}
	public int[] getDoFs() {return DoFs;}
	public void setID(int I) {ID = I;}
	public void setNode(int N) {Node = N;}
	public void setDoFs(int[] D) {DoFs = D;}
}
