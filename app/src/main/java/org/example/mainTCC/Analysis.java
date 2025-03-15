package org.example.mainTCC;

import java.util.List;

import org.example.loading.Loading;
import org.example.structure.ConcLoads;
import org.example.structure.DistLoads;
import org.example.structure.ElemType;
import org.example.structure.Element;
import org.example.structure.Material;
import org.example.structure.Mesh;
import org.example.structure.NodalDisps;
import org.example.structure.Node;
import org.example.structure.Reactions;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.structure.Supports;
import org.example.utilidades.Util;

public abstract class Analysis
{

	public static int CalcNFreeDoFs(List<Node> Node, Element[] Elem, Supports[] Sup)
	{
		int NFreeDoFs = 0;
        
        for (int node = 0; node <= Node.size() - 1; node += 1)
        {
			NFreeDoFs += Node.get(node).getDOFType().length;
        }
        
        for (int sup = 0; sup <= Sup.length - 1; sup += 1)
    	{
        	int SupNode = Sup[sup].getNode();
    	    for (int dof = 0; dof <= Node.get(SupNode).getDOFType().length - 1; dof += 1)
        	{
    	    	if (Node.get(SupNode).getDOFType()[dof] <= Sup[sup].getDoFs().length - 1)
    	    	{
    	    		if (Sup[sup].getDoFs()[Node.get(SupNode).getDOFType()[dof]] == 1)
        	        {
        	           NFreeDoFs += -1; 
        	        }
    	    	}
    	    	else
    	    	{
    	    		NFreeDoFs += -1; 
    	    	}
        	}
    	}
    	return NFreeDoFs;
	}
	
	public static int[] DefineFreeDoFTypes(List<Node> nodes)
	{
		int[] FreeDOFTypes = null;
        
        for (int i = 0; i <= nodes.size() - 1; i += 1)
    	{
    	    for (int dof = 0; dof <= nodes.get(i).getDOFType().length - 1; dof += 1)
        	{
    	    	if (-1 < nodes.get(i).dofs[dof])
    	        {
    	    		FreeDOFTypes = Util.AddElem(FreeDOFTypes, nodes.get(i).getDOFType()[dof]);
    	        }
        	}
    	}

    	return FreeDOFTypes;
	}
	
	public static double[] SolveLinearSystem(double[][] A, double[] B)
    {
    	/*This function uses the Cholesky decomposition to solve the system A = Bx and returns the vector x*/
    	int DoF = A.length;
    	double[][] R = new double[DoF][DoF];
    	double[] Z = new double[DoF];
    	double[] x = new double[DoF];
    	double sum = 0;
    	for (int i = 0; i <= DoF - 1; i += 1)
    	{
        	for (int j = 0; j <= DoF - 1; j += 1)
        	{
        		sum = 0;
        		if (i == j)
        		{
        			for (int k = 0; k <= i - 1; k += 1)
        			{
        				sum += R[k][i]*R[k][j];
        			}
        			R[i][i] = Math.pow(A[i][i] - sum, 0.5);
        		}
        		if (i < j)
        		{
        			for (int k = 0; k <= i - 1; k += 1)
        			{
        				sum += R[k][i]*R[k][j];
        			}
        			R[i][j] = 1/R[i][i]*(A[i][j] - sum);
        		}
        	}      	
    	}
    	for (int i = 0; i <= DoF - 1; i += 1)
    	{
    		sum = 0;
    		for (int j = 0; j <= i - 1; j += 1)
        	{
    			sum += -R[j][i]*Z[j];
        	}
    		Z[i] = (B[i] + sum)/R[i][i];
    	}
    	for (int i = 0; i <= DoF - 1; i += 1)
    	{
    		sum = 0;
    		for (int j = 0; j <= i - 1; j += 1)
        	{
    			sum += -R[DoF - i - 1][DoF + j - i]*x[DoF + j - i];
        	}
    		x[DoF - i - 1] = (Z[DoF - i - 1] + sum)/R[DoF - i - 1][DoF - i - 1];
    	}
    	return x;
    }

	public static double[][] NumIntegration(List<Node> Node, Element Elem, Material mat, Section sec, int[][] DOFsPerNode, boolean NonlinearMat, boolean NonlinearGeo, double[] strain, int NPoints)
	{
		double[] Points = null;
		double[] Weights = null;
		int NDOFs = 0;
		for (int i = 0; i <= DOFsPerNode.length - 1; i += 1)
		{
			for (int j = 0; j <= DOFsPerNode[i].length - 1; j += 1)
			{
				NDOFs += 1;
			}
		}
		double[][] k = new double[NDOFs][NDOFs];
		double[][] D = Element.BendingConstitutiveMatrix(mat, NonlinearMat, strain);
		if (NPoints == 1)
		{
			Points = new double[] {0};
		}
		else if (NPoints == 2)
		{
			Points = new double[] {-1 / Math.sqrt(3), 1 / Math.sqrt(3)};
		}
		else if (NPoints == 3)
		{
			Points = new double[] {-Math.sqrt(3/5.0), 0, Math.sqrt(3/5.0)};
			Weights = new double[] {0.1714677641, 0.1714677641, 0.1714677641, 0.1714677641, 0.1714677641, 0.1714677641, 0.1714677641, 0.1714677641,
									0.2743484225, 0.2743484225, 0.2743484225, 0.2743484225, 0.2743484225, 0.2743484225, 0.2743484225, 0.2743484225, 0.2743484225, 0.2743484225, 0.2743484225, 0.2743484225,
									0.4389574760, 0.4389574760, 0.4389574760, 0.4389574760, 0.4389574760, 0.4389574760, 
									0.7023319616};
		}

		if (NPoints <= 3)
		{
			double a = Math.abs(Node.get(Elem.getExternalNodes()[2]).getOriginalCoords().x - Node.get(Elem.getExternalNodes()[0]).getOriginalCoords().x) / 2;
			double b = Math.abs(Node.get(Elem.getExternalNodes()[2]).getOriginalCoords().y - Node.get(Elem.getExternalNodes()[0]).getOriginalCoords().y) / 2;
			double[][] Db = Element.BendingConstitutiveMatrix(mat, NonlinearMat, strain);
			double[][] Ds = Element.ShearConstitutiveMatrix2(mat, sec);
			for (int pe = 0; pe <= NPoints - 1; pe += 1)
			{
				for (int pn = 0; pn <= NPoints - 1; pn += 1)
				{
					double e = Points[pe], n = Points[pn];
					if (Elem.getType().equals(ElemType.SM))
					{
						for (int pw = 0; pw <= NPoints - 1; pw += 1)
						{
							double w = Points[pw];							
							double t = sec.getT() / 1000.0;
							double[][] Bb1 = Elem.Bb(e, n, w, Node, sec, NonlinearGeo, 1);
							double[][] Bb2 = Elem.Bb(e, n, w, Node, sec, NonlinearGeo, 2);
							double[][] Bs1 = Elem.Bs(e, n, w, Node, sec, 1);
							double[][] Bs2 = Elem.Bs(e, n, w, Node, sec, 2);
							double[][] Bb = Util.AddMatrix(Bb1, Bb2);
							double[][] Bs = Util.AddMatrix(Bs1, Bs2);
							
							k = Util.MultMatrix(Util.AddMatrix(k, Util.MultMatrix(Util.Transpose(Bb), Util.MultMatrix(Db, Bb))), Weights[pe*NPoints*NPoints + pn*NPoints + pw]);
							k = Util.MultMatrix(Util.AddMatrix(k, Util.MultMatrix(Util.Transpose(Bs), Util.MultMatrix(Ds, Bs))), Weights[pe*NPoints*NPoints + pn*NPoints + pw]);
							k = Util.MultMatrix(k, a * b * t / 2.0);
						}
					}
					else
					{
						double[][] B = Elem.SecondDerivativesb(e, n, Node, sec, NonlinearGeo);
						k = Util.AddMatrix(k, Util.MultMatrix(Util.Transpose(B), Util.MultMatrix(D, B)));
					}
				}
			}
			
			return k;
		}
		else
		{
			System.out.println("Number of points is higher than 3 at Analysis -> NumIntegration");
			return null;
		}
	}
	
	public static double[] LoadVector(Mesh mesh, int NFreeDOFs, Loading loading, boolean NonlinearMat, boolean NonlinearGeo, double loadfactor)
	{
		double[] P = new double[NFreeDOFs];
		List<Node> Node = mesh.getNodes();
		List<Element> Elem = mesh.getElements();
		if (loading.getConcLoads() != null)
		{
			for (int load = 0; load <= loading.getConcLoads().size() - 1; load += 1)
			{
				int node = loading.getConcLoads().get(load).getNode();
				for (int dof = 0; dof <= Node.get(node).getDOFType().length - 1; dof += 1)
				{
					if (-1 < Node.get(node).dofs[dof])
					{
						if (Node.get(node).getDOFType()[dof] <= loading.getConcLoads().get(load).getLoads().length - 1)
						{
							P[Node.get(node).dofs[dof]] += loading.getConcLoads().get(load).getLoads()[Node.get(node).getDOFType()[dof]] * loadfactor;
						}
					}
				}
			}
		}
		
		int[] ElemsWithDistLoad = null;
		for (int i = 0; i <= Elem.size() - 1; i += 1)
		{
			if (Elem.get(i).getDistLoads() != null)
			{
				ElemsWithDistLoad = Util.AddElem(ElemsWithDistLoad, i);
			}
		}
		
		if (ElemsWithDistLoad != null)
		{
			for (int e = 0; e <= ElemsWithDistLoad.length - 1; e += 1)
			{
				int elem = ElemsWithDistLoad[e];
				for (int load = 0; load <= Elem.get(elem).getDistLoads().length - 1; load += 1)
				{
					int LoadType = Elem.get(elem).getDistLoads()[load].getType();
					double LoadIntensity = Elem.get(elem).getDistLoads()[load].getIntensity();
					int[] nodes = Elem.get(elem).getExternalNodes();
					double[][] Q = Elem.get(elem).LoadVector(Node);
					double[] p = new double[Elem.get(elem).getDOFs().length];
					if (LoadType == 4)
					{
						p[0] = LoadIntensity;
						double[] q = Util.MultMatrixVector(Util.Transpose(Q), p);
						for (int elemnode = 0; elemnode <= nodes.length - 1; elemnode += 1)
						{
							int node = nodes[elemnode];

							for (int dof = 0; dof <= Node.get(node).getDOFType().length - 1; dof += 1)
							{
								if (-1 < Node.get(node).dofs[dof])
								{									
									P[Node.get(node).dofs[dof]] += q[Elem.get(elem).getCumDOFs()[elemnode] + dof] * loadfactor;
								}
							}
						}
					}
				}
			}
		}
		
		if (loading.getNodalDisps() != null)
		{
			double[] Uapplied = new double[NFreeDOFs];
			for (int disp = 0; disp <= loading.getNodalDisps().size() - 1; disp += 1)
			{
				int node = loading.getNodalDisps().get(disp).getNode();
				for (int dof = 0; dof <= Elem.get(0).getDOFsPerNode().length - 1; dof += 1)
				{
					if (-1 < Node.get(node).dofs[dof])
					{
						if (Node.get(node).getDOFType()[dof] <= loading.getNodalDisps().get(disp).getDisps().length - 1)
						{
							Uapplied[Node.get(node).dofs[dof]] += loading.getNodalDisps().get(disp).getDisps()[Node.get(node).getDOFType()[dof]]*loadfactor;
						}
					}
				}
			}
			for (int node = 0; node <= Node.size() - 1; node += 1)
			{
				double[] Peq = NodeForces(node, Node, Elem, NonlinearMat, NonlinearGeo, Uapplied);
				for (int dof = 0; dof <= Peq.length - 1; dof += 1)
				{
					if (-1 < Node.get(node).dofs[dof])
					{
						P[Node.get(node).dofs[dof]] += Peq[dof];
					}
				}
			}
		}
		return P;
	}

	public static double[] run(Structure struct, Loading loading,
										boolean NonlinearMat, boolean NonlinearGeo, int NIter, int NLoadSteps, double MaxLoadFactor)
	{
		/*
		 * NIter = Nâmero de iteraçõs em cada passo (para convergir)
		 * NLoadSteps = Nâmero de incrementos de carga (nâmero de passos)
		 * MaxLoadFactor = Fator de carga final (valor multiplicando a carga)
		 * */
		long AnalysisTime = System.currentTimeMillis();
		double loadinc = MaxLoadFactor / (double) NLoadSteps;
		for (int loadstep = 0; loadstep <= NLoadSteps - 1; loadstep += 1)
		{
			double loadfactor = 0 + (loadstep + 1)*loadinc;
			struct.setP(LoadVector(struct.getMesh(), struct.NFreeDOFs, loading, NonlinearMat, NonlinearGeo, loadfactor));
		    for (int iter = 0; iter <= NIter - 1; iter += 1)
			{
		    	struct.setK(Structure.StructureStiffnessMatrix(struct.NFreeDOFs, struct.getMesh().getNodes(), struct.getMesh().getElements(), NonlinearMat, NonlinearGeo));
		    	struct.setU(SolveLinearSystem(struct.getK(), struct.getP()));
			    for (int node = 0; node <= struct.getMesh().getNodes().size() - 1; node += 1)
			    {
			    	struct.getMesh().getNodes().get(node).setDisp(GetNodeDisplacements(struct.getMesh().getNodes(), struct.getU())[node]);
			    }
			    if (NonlinearMat)
			    {
					for (int elem = 0; elem <= struct.getMesh().getElements().size() - 1; elem += 1)
				    {
						struct.getMesh().getElements().get(elem).setStrain(struct.getMesh().getElements().get(elem).StrainVec(struct.getMesh().getNodes(), struct.getU(), NonlinearGeo));
				    }
			    }
				/*for (int elem = 0; elem <= Elem.length - 1; elem += 1)
			    {
					Elem[elem].setIntForces(Elem[elem].InternalForcesVec(Node, struct.getU(), NonlinearMat, NonlinearGeo));
			    }*/
				//UtilText.PrintMatrix(struct.getK());
			    //UtilText.PrintVector(struct.getP());
		        //UtilText.PrintVector(struct.getU());
				System.out.println("iter: " + iter + " max disp: " + Util.FindMaxAbs(struct.getU()));
			}
			for (int node = 0; node <= struct.getMesh().getNodes().size() - 1; node += 1)
			{
				struct.getMesh().getNodes().get(node).addLoadDispCurve(struct.getU(), loadfactor);
			}
		}
		AnalysisTime = System.currentTimeMillis() - AnalysisTime;
		System.out.println("Tempo de anâlise = " + AnalysisTime / 1000.0 + " seg");
		if (((Double)struct.getU()[0]).isNaN())
		{
			System.out.println("Displacement results are NaN at Menus -> RunAnalysis");
		}
		
		return struct.getU();
	}

    public static double DispOnPoint(List<Node> Node, Element Elem, double e, double n, int dof, double[] u)
    {
    	double[] N = Elem.NaturalCoordsShapeFunctions(e, n, Node)[dof];
    	//System.out.println(Arrays.toString(N));
    	//System.out.println(Arrays.toString(u));
    	//System.out.println(Util.MultVector(N, u));
    	return Util.MultVector(N, u);
    }

    public static double StrainOnElemContour(List<Node> Node, Element Elem, double e, double n, int dof, double[] s)
    {
    	double[] N = Elem.NaturalCoordsShapeFunctions(e, n, Node)[dof];
    	return Util.MultVector(N, s);
    }
    
    public static double StressOnElemContour(List<Node> Node, Element Elem, double e, double n, int dof, double[] s)
    {
    	double[] N;
    	N = Elem.NaturalCoordsShapeFunctions(e, n, Node)[dof];
    	return Util.MultVector(N, s);
    }
    
    public static double ForceOnElemContour(List<Node> Node, Element Elem, double e, double n, int dof, double[] p)
    {
    	double[] N;
    	N = Elem.NaturalCoordsShapeFunctions(e, n, Node)[dof];
    	return Util.MultVector(N, p);
    }
    
	public static double[][] GetNodeDisplacements(List<Node> Node, double[] u)
    {
        int NumDim = 6;
        double[][] NodeDisp = new double[Node.size()][3];
        for (int node = 0; node <= Node.size() - 1; node += 1)
    	{
    	    for (int dim = 0; dim <= NumDim - 1; dim += 1)
        	{
    	    	int dofPos = Util.ElemPosInArray(Node.get(node).getDOFType(), dim);
    	    	if (-1 < dofPos)
    	    	{
        	        if (-1 < Node.get(node).dofs[dofPos])
        	        {
        	           NodeDisp[node][dofPos] = u[Node.get(node).dofs[dofPos]];
        	        }
        	        else
        	        {
        	            NodeDisp[node][dofPos] = 0;
        	        }
    	    	}
        	}
    	}
    	return NodeDisp;
    }

	public static Reactions[] Reactions(Mesh mesh, List<Supports> Sup, boolean NonlinearMat, boolean NonlinearGeo, double[] U)
	{
		Reactions[] R = new Reactions[Sup.size()];
		for (int node = 0; node <= Sup.size() - 1; node += 1)
		{
			int nodeID = Sup.get(node).getNode();
			double[] NodeForces = NodeForces(nodeID, mesh.getNodes(), mesh.getElements(), NonlinearMat, NonlinearGeo, U);
			R[node] = new Reactions(node, nodeID, NodeForces);
		}	
		return R;
	}
	
	public static double[] SumReactions(Reactions[] Reactions)
	{
		double[] sumReactions = new double[6];
		for (int dof = 0; dof <= sumReactions.length - 1; dof += 1)
		{
			for (int node = 0; node <= Reactions.length - 1; node += 1)
			{
				sumReactions[dof] += Reactions[node].getLoads()[dof];
			}
		}
		return sumReactions;
	}
	
	public static double[] NodeForces(int node, List<Node> nodes, List<Element> elems, boolean NonlinearMat, boolean NonlinearGeo, double[] U)
	{
		double[] forces = new double[6];
		for (int elem = 0; elem <= elems.size() - 1; elem += 1)
        {
			for (int elemnode = 0; elemnode <= elems.get(elem).getExternalNodes().length - 1; elemnode += 1)
	        {
				int NodeID = elems.get(elem).getExternalNodes()[elemnode];
				if (node == NodeID)
				{
					double[] p = elems.get(elem).ForceVec(nodes, NonlinearMat, NonlinearGeo, U);
					for (int dof = 0; dof <= nodes.get(node).getDOFType().length - 1; dof += 1)
			        {
						if (nodes.get(node).getDOFType()[dof] <= 5)
						{
							forces[nodes.get(node).getDOFType()[dof]] += p[elems.get(elem).getCumDOFs()[elemnode] + dof];
						}
						else if (nodes.get(node).getDOFType()[dof] == 7)
						{
							forces[3] += -p[elems.get(elem).getCumDOFs()[elemnode] + dof];
						}
						else if (nodes.get(node).getDOFType()[dof] == 8)
						{
							forces[4] += -p[elems.get(elem).getCumDOFs()[elemnode] + dof];
						}
			        }
				}
	        }
        }
		
		return forces;
	}

}