package main.structure;

import java.awt.Color;
import java.util.Arrays;

import main.userInterface.Menus;

public class Supports
{
	private int ID;
	private int Node;		// Node
	private int[] DoFs;		// Restrained DoFs

	public static int[][] Types = new int[][] {{1, 0, 0, 0, 0, 0}, {0, 1, 0, 0, 0, 0}, {0, 0, 1, 0, 0, 0}, {1, 1, 0, 0, 0, 0}, {1, 1, 0, 0, 0, 1}, {1, 1, 1, 1, 1, 1}};
	public static Color color = Menus.palette[7];
	
	public Supports(int ID, Node Node, int[] DoFs)
	{
		this(ID, Node.getID(), DoFs);
	}

	public Supports(int ID, int Node, int[] DoFs)
	{
		this.ID = ID;
		this.Node = Node;
		this.DoFs = DoFs;
	}


	public static int typeFromDOFs (int[] DoFs)
	{
		int suptype = -1;
		
		if (DoFs[0] == 1 & DoFs[1] == 0 & DoFs[2] == 0 & DoFs[3] == 0 & DoFs[4] == 0 & DoFs[5] == 0)
		{
			suptype = 0;																				// roller in the x dir
		}
		if (DoFs[0] == 0 & DoFs[1] == 1 & DoFs[2] == 0 & DoFs[3] == 0 & DoFs[4] == 0 & DoFs[5] == 0)
		{
			suptype = 1;																				// roller in the y dir
		}
		if (DoFs[0] == 0 & DoFs[1] == 0 & DoFs[2] == 1 & DoFs[3] == 0 & DoFs[4] == 0 & DoFs[5] == 0)
		{
			suptype = 2;																				// roller in the z dir
		}
		if (DoFs[0] == 1 & DoFs[1] == 1 & DoFs[2] == 0 & DoFs[3] == 0 & DoFs[4] == 0 & DoFs[5] == 0)
		{
			suptype = 3;																				// pin in the x-y dir
		}
		if (DoFs[0] == 1 & DoFs[1] == 1 & DoFs[2] == 0 & DoFs[3] == 0 & DoFs[4] == 0 & DoFs[5] == 1)
		{
			suptype = 4;																				// cantilever xyz
		}
		if (DoFs[0] == 1 & DoFs[1] == 1 & DoFs[2] == 1 & DoFs[3] == 1 & DoFs[4] == 1 & DoFs[5] == 1)
		{
			suptype = 5;																				// full cantilever
		}
		
		return suptype;
	}

	public int typeFromDOFs() { return typeFromDOFs(DoFs) ;}

	public int getID() {return ID;}
	public int getNode() {return Node;}
	public int[] getDoFs() {return DoFs;}
	public void setID(int I) {ID = I;}
	public void setNode(int N) {Node = N;}
	public void setDoFs(int[] D) {DoFs = D;}

	@Override
	public String toString() {
		return ID + "	" + Node + "	" + Arrays.toString(DoFs);
	}
	
}
