package output;

import main.Analysis;
import structure.Element;
import structure.Nodes;
import structure.Reactions;
import structure.Supports;
import utilidades.Util;

public class Results
{
	private double[] DispMin;
	private double[] DispMax;
	private double[] StrainMin;
	private double[] StrainMax;
	private double[] StressMin;
	private double[] StressMax;
	private double[] InternalForcesMin;
	private double[] InternalForcesMax;
	private Reactions[] Reactions;
	private double[] SumReactions;
	private double[][][][] LoadDisp;
	
	public void register(Nodes[] Node, Element[] Elem, Supports[] Sup, double[] U, boolean NonlinearMat, boolean NonlinearGeo)
	{
		double[][][] ElemStrains = new double[Elem.length][][];
	    double[][][] ElemStresses = new double[Elem.length][][];
	    double[][][] ElemInternalForces = new double[Elem.length][][];
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			int NNodesOnElem = Elem[elem].getExternalNodes().length;
			ElemStrains[elem] = new double[NNodesOnElem][Elem[elem].getStrainTypes().length];
			ElemStresses[elem] = new double[NNodesOnElem][Elem[elem].getStrainTypes().length];
			ElemInternalForces[elem] = new double[NNodesOnElem][Elem[elem].getStrainTypes().length];
			for (int elemnode = 0; elemnode <= NNodesOnElem - 1; elemnode += 1)
			{
				int NumberOfDOFsOnNode = Node[Elem[elem].getExternalNodes()[elemnode]].getDOFType().length;
				for (int dof = 0; dof <= Elem[elem].getStrainTypes().length - 1; dof += 1)
				{
					int ID = elemnode * NumberOfDOFsOnNode + dof;
					ElemStrains[elem][elemnode][dof] = Elem[elem].getStrain()[ID];
					ElemStresses[elem][elemnode][dof] = Elem[elem].getStress()[ID];
					ElemInternalForces[elem][elemnode][dof] = Elem[elem].getIntForces()[ID];
				}
			}
		}
        int[] DOFTypesOnNode = Analysis.DefineFreeDoFTypes(Node, Elem, Sup);
        setDispMin(FindMinDisps(U, Elem[0].getDOFs(), DOFTypesOnNode)) ;
        setDispMax(FindMinDisps(U, Elem[0].getDOFs(), DOFTypesOnNode)) ;
        setStrainMin(FindMinElemProp(ElemStrains, Elem.length, Elem[0].getStrainTypes().length)) ;
        setStrainMax(FindMaxElemProp(ElemStrains, Elem.length, Elem[0].getStrainTypes().length)) ;
        setStressMin(FindMinElemProp(ElemStresses, Elem.length, Elem[0].getStrainTypes().length)) ;
        setStressMax(FindMaxElemProp(ElemStresses, Elem.length, Elem[0].getStrainTypes().length)) ;
        setInternalForcesMin(FindMinElemProp(ElemInternalForces, Elem.length, Elem[0].getStrainTypes().length)) ;
        setInternalForcesMax(FindMaxElemProp(ElemInternalForces, Elem.length, Elem[0].getStrainTypes().length)) ;
		
		double[][] strains = new double[Elem.length][3];
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			strains[elem] = Elem[elem].getStrain();
		}
		for (int node = 0; node <= Node.length - 1; node += 1)
	    {
			if (Analysis.NodeForces(node, Node, Elem, NonlinearMat, NonlinearGeo, U) != null)
			{
		    	Node[node].AddConcLoads(Analysis.NodeForces(node, Node, Elem, NonlinearMat, NonlinearGeo, U));
			}
	    }
		setReactions(Analysis.Reactions(Node, Elem, Sup, NonlinearMat, NonlinearGeo, U)) ;
		setSumReactions(Analysis.SumReactions(getReactions())) ;
	}

	public static double[] FindMinDisps(double[] SetOfValues, int[] ElemDOFs, int[] DOFTypesOnNode)
	{
		double[] MinValue = new double[ElemDOFs.length];
		for (int doftype = 0; doftype <= ElemDOFs.length - 1; doftype += 1)
		{
			for (int v = 0; v <= SetOfValues.length - 1; v += 1)
			{
				if (ElemDOFs[doftype] == DOFTypesOnNode[v])
				{
					if (SetOfValues[v] < MinValue[doftype])
					{
						MinValue[doftype] = SetOfValues[v];
					}
				}
			}
		}
		
		return MinValue;
	}
	
	public static double[] FindMaxDisps(double[] SetOfValues, int[] ElemDOFs, int[] DOFTypesOnNode)
	{
		double[] MaxValue = new double[ElemDOFs.length];
		for (int doftype = 0; doftype <= ElemDOFs.length - 1; doftype += 1)
		{
			for (int v = 0; v <= SetOfValues.length - 1; v += 1)
			{
				if (ElemDOFs[doftype] == DOFTypesOnNode[v])
				{
					if (MaxValue[doftype] < SetOfValues[v])
					{
						MaxValue[doftype] = SetOfValues[v];
					}
				}
			}
		}
		
		return MaxValue;
	}

	public static double[] FindMinElemProp(double[][][] ElemProp, int NumElem, int NumPropTypes)
	{
		double[] Minvalue = Util.FindMinPerPos(ElemProp[0]);
		for (int elem = 0; elem <= NumElem - 1; elem += 1)
		{
			double[] MinPerElem = Util.FindMinPerPos(ElemProp[elem]);
			for (int strain = 0; strain <= NumPropTypes - 1; strain += 1)
			{
				if (MinPerElem[strain] < Minvalue[strain])
				{
					Minvalue[strain] = MinPerElem[strain];
				}
			}
		}
		
		return Minvalue;
	}
	
	public static double[] FindMaxElemProp(double[][][] ElemProp, int NumElem, int NumPropTypes)
	{
		double[] Maxvalue = Util.FindMaxPerPos(ElemProp[0]);
		for (int elem = 0; elem <= NumElem - 1; elem += 1)
		{
			double[] MaxPerElem = Util.FindMaxPerPos(ElemProp[elem]);
			for (int strain = 0; strain <= NumPropTypes - 1; strain += 1)
			{
				if (Maxvalue[strain] < MaxPerElem[strain])
				{
					Maxvalue[strain] = MaxPerElem[strain];
				}
			}
		}
		
		return Maxvalue;
	}
	
	public double[] getDispMin()
	{
		return DispMin;
	}
	public void setDispMin(double[] dispMin)
	{
		DispMin = dispMin;
	}
	public double[] getDispMax()
	{
		return DispMax;
	}
	public void setDispMax(double[] dispMax)
	{
		DispMax = dispMax;
	}
	public double[] getStrainMin()
	{
		return StrainMin;
	}
	public void setStrainMin(double[] strainMin)
	{
		StrainMin = strainMin;
	}
	public double[] getStrainMax()
	{
		return StrainMax;
	}
	public void setStrainMax(double[] strainMax)
	{
		StrainMax = strainMax;
	}
	public double[] getStressMin()
	{
		return StressMin;
	}
	public void setStressMin(double[] stressMin)
	{
		StressMin = stressMin;
	}
	public double[] getStressMax()
	{
		return StressMax;
	}
	public void setStressMax(double[] stressMax)
	{
		StressMax = stressMax;
	}
	public double[] getInternalForcesMin()
	{
		return InternalForcesMin;
	}
	public void setInternalForcesMin(double[] internalForcesMin)
	{
		InternalForcesMin = internalForcesMin;
	}
	public double[] getInternalForcesMax()
	{
		return InternalForcesMax;
	}
	public void setInternalForcesMax(double[] internalForcesMax)
	{
		InternalForcesMax = internalForcesMax;
	}
	public Reactions[] getReactions()
	{
		return Reactions;
	}
	public void setReactions(Reactions[] reactions)
	{
		Reactions = reactions;
	}
	public double[] getSumReactions()
	{
		return SumReactions;
	}
	public void setSumReactions(double[] sumReactions)
	{
		SumReactions = sumReactions;
	}
	public double[][][][] getLoadDisp()
	{
		return LoadDisp;
	}
	public void setLoadDisp(double[][][][] loadDisp)
	{
		LoadDisp = loadDisp;
	}
	
	
}
