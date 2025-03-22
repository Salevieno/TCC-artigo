package org.example.loading;

import java.awt.Color;
import java.util.Arrays;

import org.example.structure.Node;
import org.example.userInterface.Menus;

public class NodalDisp
{
	private int ID;
	private int Node;		// Node
	private double[] Disps;	// Loads [Fx, Fy, Fz, Mx, My, Mz]

	public static Color color = Menus.palette[7];
	
	public NodalDisp(int ID, Node Node, double[] Disps)
	{
		this(ID, Node.getID(), Disps);
	}
	
	public NodalDisp(int ID, int Node, double[] Disps)
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

	@Override
	public String toString() {
		return ID + "	" + Node + "	" + Arrays.toString(Disps) ;
	}

}
