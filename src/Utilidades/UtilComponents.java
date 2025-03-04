package Utilidades;

import java.util.Arrays;

import structure.ConcLoads;
import structure.DistLoads;
import structure.Elements;
import structure.NodalDisps;
import structure.Nodes;
import structure.Reactions;
import structure.Supports;

public abstract class UtilComponents
{
	public static void PrintNode(Nodes Node)
	{
		System.out.println(Node.getID() + "	" + Arrays.toString(Node.getOriginalCoords()) + "	" + Arrays.toString(Node.getDisp()) + "	" + Node.getSup() + "	" + Arrays.toString(Node.getConcLoads()) + "	" + Arrays.toString(Node.getNodalDisps()));
	}

	public static void PrintElem(Elements Elem)
	{
		System.out.println(Elem.getID() + "	" + Arrays.toString(Elem.getExternalNodes()) + "		" + Elem.getMat() + "	" + Elem.getSec() + "	" + Arrays.toString(Elem.getDistLoads()));
	}
	
	public static void PrintAllNodes(Nodes[] Node)
	{
		System.out.println();
		System.out.println("Nodes");
		System.out.println("ID	Original coords (m)	Displacements (m)	Sup ConcLoads (kN)	NodalDisps (m)");
		for (int node = 0; node <= Node.length - 1; node += 1)
		{
			PrintNode(Node[node]);
		}
	}
	
	public static void PrintAllElems(Elements[] Elem)
	{
		System.out.println();
		System.out.println("Elems");
		System.out.println("ID	Nodes		Mat	Sec	DistLoads (kN/m)");
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			PrintElem(Elem[elem]);
		}
	}
	
	public static void PrintMat(double[] Mat)
	{
		for (int i = 0; i <= Mat.length - 1; i += 1)
		{
			System.out.print(Mat[i] + "	");
		}
		System.out.println();
	}
	
	public static void PrintAllMat(double[][] Mat)
	{
		System.out.println();
		System.out.println("Mats");
		System.out.println("E (GPa)	v");
		for (int mat = 0; mat <= Mat.length - 1; mat += 1)
		{
			PrintMat(Mat[mat]);
		}
	}
	
	public static void PrintSec(double[] Sec)
	{
		for (int i = 0; i <= Sec.length - 1; i += 1)
		{
			System.out.print(Sec[i] + "	");
		}
		System.out.println();
	}
	
	public static void PrintAllSec(double[][] Sec)
	{
		System.out.println();
		System.out.println("Secs");
		System.out.println("thick (mm)");
		for (int sec = 0; sec <= Sec.length - 1; sec += 1)
		{
			PrintSec(Sec[sec]);
		}
	}
	
	public static void PrintSup(Supports Sup)
	{
		System.out.println(Sup.getID() + "	" + Sup.getNode() + "	" + Arrays.toString(Sup.getDoFs()));
	}
	
	public static void PrintAllSups(Supports[] Sups)
	{
		System.out.println();
		System.out.println("Sups");
		System.out.println("Id	Node	DoFs (Fx Fy Fz Mx My Mz)");
		for (int sup = 0; sup <= Sups.length - 1; sup += 1)
		{
			PrintSup(Sups[sup]);
		}
	}
	
	public static void PrintConcLoad(ConcLoads ConcLoad)
	{
		System.out.println(ConcLoad.getID() + "	" + ConcLoad.getNode() + "	" + Arrays.toString(ConcLoad.getLoads()));
	}
	
	public static void PrintAllConcLoads(ConcLoads[] ConcLoads)
	{
		System.out.println();
		System.out.println("ConcLoads");
		System.out.println("Id	Node	Loads Fx Fy Fz Mx My Mz (kN)");
		if (ConcLoads != null)
		{
			for (int load = 0; load <= ConcLoads.length - 1; load += 1)
			{
				PrintConcLoad(ConcLoads[load]);
			}
		}
	}
	
	public static void PrintDistLoad(DistLoads DistLoad)
	{
		System.out.println(DistLoad.getID() + "	" + DistLoad.getElem() + "	" + DistLoad.getType() + "	" + DistLoad.getIntensity());
	}
	
	public static void PrintAllDistLoads(DistLoads[] DistLoads)
	{
		System.out.println();
		System.out.println("DistLoads");
		System.out.println("Id	Elem type	Intensity (kN/m)");
		if (DistLoads != null)
		{
			for (int load = 0; load <= DistLoads.length - 1; load += 1)
			{
				PrintDistLoad(DistLoads[load]);
			}
		}
	}
	
	public static void PrintNodalDisp(NodalDisps NodalDisp)
	{
		System.out.println(NodalDisp.getID() + "	" + NodalDisp.getNode() + "	" + Arrays.toString(NodalDisp.getDisps()));
	}
	
	public static void PrintAllNodalDisps(NodalDisps[] NodalDisps)
	{
		System.out.println();
		System.out.println("NodalDisps");
		System.out.println("Id	Node	Disp ux uy uz thetax thetay thetaz (m)");
		if (NodalDisps != null)
		{
			for (int disp = 0; disp <= NodalDisps.length - 1; disp += 1)
			{
				PrintNodalDisp(NodalDisps[disp]);
			}
		}
	}
	
	public static void PrintReaction(Reactions Reaction)
	{
		System.out.println(Reaction.getID() + "	" + Reaction.getNode() + "	" + Arrays.toString(Reaction.getLoads()));
	}
	
	public static void PrintAllReactions(Reactions[] Reactions)
	{
		System.out.println();
		System.out.println("Reactions");
		System.out.println("Id	Node	Loads Fx Fy Fz Mx My Mz (kN)");
		for (int load = 0; load <= Reactions.length - 1; load += 1)
		{
			PrintReaction(Reactions[load]);
		}
	}
	
	public static void PrintStructure(String StructureName, Nodes[] Node, Elements[] Elem, double[][] Mat, double[][] Sec, Supports[] Sup, ConcLoads[] ConcLoads, DistLoads[] DistLoads, NodalDisps[] NodalDisps)
	{
		System.out.println(" *** Structure information ***");
		System.out.println(StructureName);
		if (Node != null)
		{
			PrintAllNodes(Node);
		}
		if (Elem != null)
		{
			PrintAllElems(Elem);
		}
		if (Mat != null)
		{
			PrintAllMat(Mat);
		}
		if (Sec != null)
		{
			PrintAllSec(Sec);
		}
		if (Sup != null)
		{
			PrintAllSups(Sup);
		}
		if (ConcLoads != null)
		{
			PrintAllConcLoads(ConcLoads);
		}
		if (DistLoads != null)
		{
			PrintAllDistLoads(DistLoads);
		}
		if (NodalDisps != null)
		{
			PrintAllNodalDisps(NodalDisps);
		}
	}
}
