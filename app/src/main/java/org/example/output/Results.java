package org.example.output;

import java.util.Arrays;
import java.util.List;

import org.example.loading.ConcLoads;
import org.example.mainTCC.Analysis;
import org.example.structure.Mesh;
import org.example.structure.Node;
import org.example.structure.Element;
import org.example.structure.Reactions;
import org.example.structure.Supports;
import org.example.utilidades.Util;

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
	
	public void register(Mesh mesh, List<Supports> Sup, double[] U, boolean NonlinearMat, boolean NonlinearGeo)
	{
		List<Node> Node = mesh.getNodes();
		List<Element> Elem = mesh.getElements();
		double[][][] ElemStrains = new double[Elem.size()][][];
	    double[][][] ElemStresses = new double[Elem.size()][][];
	    double[][][] ElemInternalForces = new double[Elem.size()][][];
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
		{
			int NNodesOnElem = Elem.get(elem).getExternalNodes().length;
			ElemStrains[elem] = new double[NNodesOnElem][Elem.get(elem).getStrainTypes().length];
			ElemStresses[elem] = new double[NNodesOnElem][Elem.get(elem).getStrainTypes().length];
			ElemInternalForces[elem] = new double[NNodesOnElem][Elem.get(elem).getStrainTypes().length];
			for (int elemnode = 0; elemnode <= NNodesOnElem - 1; elemnode += 1)
			{
				int NumberOfDOFsOnNode = Node.get(Elem.get(elem).getExternalNodes()[elemnode]).getDOFType().length;
				for (int dof = 0; dof <= Elem.get(elem).getStrainTypes().length - 1; dof += 1)
				{
					int ID = elemnode * NumberOfDOFsOnNode + dof;
					ElemStrains[elem][elemnode][dof] = Elem.get(elem).getStrain()[ID];
					ElemStresses[elem][elemnode][dof] = Elem.get(elem).getStress()[ID];
					ElemInternalForces[elem][elemnode][dof] = Elem.get(elem).getIntForces()[ID];
				}
			}
		}
        int[] DOFTypesOnNode = Analysis.DefineFreeDoFTypes(Node);
        setDispMin(FindMinDisps(U, Elem.get(0).getDOFs(), DOFTypesOnNode)) ;
        setDispMax(FindMinDisps(U, Elem.get(0).getDOFs(), DOFTypesOnNode)) ;
        setStrainMin(FindMinElemProp(ElemStrains, Elem.size(), Elem.get(0).getStrainTypes().length)) ;
        setStrainMax(FindMaxElemProp(ElemStrains, Elem.size(), Elem.get(0).getStrainTypes().length)) ;
        setStressMin(FindMinElemProp(ElemStresses, Elem.size(), Elem.get(0).getStrainTypes().length)) ;
        setStressMax(FindMaxElemProp(ElemStresses, Elem.size(), Elem.get(0).getStrainTypes().length)) ;
        setInternalForcesMin(FindMinElemProp(ElemInternalForces, Elem.size(), Elem.get(0).getStrainTypes().length)) ;
        setInternalForcesMax(FindMaxElemProp(ElemInternalForces, Elem.size(), Elem.get(0).getStrainTypes().length)) ;
		
		double[][] strains = new double[Elem.size()][3];
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
		{
			strains[elem] = Elem.get(elem).getStrain();
		}
		for (int node = 0; node <= Node.size() - 1; node += 1)
	    {
			if (Analysis.NodeForces(node, Node, Elem, NonlinearMat, NonlinearGeo, U) != null)
			{
				ConcLoads newConcLoad = new ConcLoads(-1, node, Analysis.NodeForces(node, Node, Elem, NonlinearMat, NonlinearGeo, U)) ;
		    	Node.get(node).addConcLoad(newConcLoad);
			}
	    }
		setReactions(Analysis.Reactions(mesh, Sup, NonlinearMat, NonlinearGeo, U)) ;
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

	@Override
	public String toString() {
		return "Results [DispMin=" + Arrays.toString(DispMin) + ", DispMax=" + Arrays.toString(DispMax) + ", StrainMin="
				+ Arrays.toString(StrainMin) + ", StrainMax=" + Arrays.toString(StrainMax) + ", StressMin="
				+ Arrays.toString(StressMin) + ", StressMax=" + Arrays.toString(StressMax) + ", InternalForcesMin="
				+ Arrays.toString(InternalForcesMin) + ", InternalForcesMax=" + Arrays.toString(InternalForcesMax)
				+ ", Reactions=" + Arrays.toString(Reactions) + ", SumReactions=" + Arrays.toString(SumReactions)
				+ ", LoadDisp=" + Arrays.toString(LoadDisp) + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", getDispMin()=" + Arrays.toString(getDispMin()) + ", getDispMax()="
				+ Arrays.toString(getDispMax()) + ", getStrainMin()=" + Arrays.toString(getStrainMin())
				+ ", getStrainMax()=" + Arrays.toString(getStrainMax()) + ", getStressMin()="
				+ Arrays.toString(getStressMin()) + ", getStressMax()=" + Arrays.toString(getStressMax())
				+ ", getInternalForcesMin()=" + Arrays.toString(getInternalForcesMin()) + ", getInternalForcesMax()="
				+ Arrays.toString(getInternalForcesMax()) + ", getReactions()=" + Arrays.toString(getReactions())
				+ ", getSumReactions()=" + Arrays.toString(getSumReactions()) + ", getLoadDisp()="
				+ Arrays.toString(getLoadDisp()) + ", toString()=" + super.toString() + "]";
	}
	
	
}
