package org.example.loading;

import java.awt.Color;
import java.util.Arrays;

import org.example.structure.Node;
import org.example.userInterface.Menus;

public class ConcLoads
{
	private int ID;
	private int NodeID;		// Node
	private double[] Loads;	// Loads [Fx, Fy, Fz, Mx, My, Mz]

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
