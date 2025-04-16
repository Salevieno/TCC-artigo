package org.example.loading;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.Main;
import org.example.structure.Node;

public class NodalDisp
{
	private int ID;
	private int Node;		// Node
	private double[] Disps;	// Loads [Fx, Fy, Fz, Mx, My, Mz]

	public static Color color = Main.palette[7];
	private static List<Double[]> types ;
	
	static
	{
		types = new ArrayList<>() ;
	}

	public NodalDisp(int ID, Node Node, Double[] Disps)
	{
		this(ID, Node.getID(), Arrays.stream(Disps).mapToDouble(Double::doubleValue).toArray()) ;
	}

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

	public static List<Double[]> getTypes() { return types ;}
	public static void resetTypes() { types = new ArrayList<>() ;}
	public static void addType(Double[] type) { types.add(type) ;}
	public static void removeType(Double[] type) { types.remove(type) ;}

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
