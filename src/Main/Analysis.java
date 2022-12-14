package Main;

import java.util.Arrays;

import Component.ConcLoads;
import Component.DistLoads;
import Component.Elements;
import Component.NodalDisps;
import Component.Nodes;
import Component.Supports;
import Utilidades.Util;
import Component.Reactions;

public class Analysis
{		
	public Nodes[] CreateRadialNodes(double[][] PolygonCoords, int noffsets, int[] nintermediatepoints)
	{
		// Calculate number of nodes in each column
		Nodes[] Node = null;
	    double[] PolygonCenter = Util.CentroidOfPoints(PolygonCoords);
	    double[][] P2 = null;	    
	    for (int i = 0; i <= noffsets - 1; i += 1)
		{
		    double offset = i / (double) noffsets;
		    double[][] P1 = Util.CreateInternalPolygonPoints(PolygonCoords, PolygonCenter, offset);
            double[][] InternalLines = Util.PolygonLines(P1);
            double[][] IntermediatePoints = Util.CreateIntermediatePoints(InternalLines, nintermediatepoints[i], true);
            for (int j = 0; j <=  IntermediatePoints.length - 1; j += 1)
    		{
                P2 = Util.AddElem(P2, IntermediatePoints[j]);
    		}
		}
	    Node = new Nodes[P2.length + 1];
        for (int node = 0; node <= P2.length - 1; node += 1)
		{
		    Node[node] = new Nodes(node, P2[node]);
		}
	    Node[P2.length] = new Nodes(P2.length, PolygonCenter);
	    return Node;
	}
	
	public Nodes[] CreateCartesianNodes(double[][] PolygonCoords, int[] NumberElem, String ElemType)
	{
		Nodes[] Node = null;
		double MinXCoord = Util.FindMinInPos(PolygonCoords, 0), MaxXCoord = Util.FindMaxInPos(PolygonCoords, 0);
		double MinYCoord = Util.FindMinInPos(PolygonCoords, 1), MaxYCoord = Util.FindMaxInPos(PolygonCoords, 1);
		double L = MaxXCoord - MinXCoord, H = MaxYCoord - MinYCoord;
		double dx = L / NumberElem[0], dy = H / NumberElem[1];
		String ElemShape = Elements.DefineShape(ElemType);
		if (ElemShape.equals("Rectangular") | ElemShape.equals("Triangular"))
		{
			Node = new Nodes[(NumberElem[0] + 1)*(NumberElem[1] + 1)];
			for (int i = 0; i <= NumberElem[1]; i += 1)
			{
				for (int j = 0; j <= NumberElem[0]; j += 1)
				{
					Node[i*(NumberElem[0] + 1) + j] = new Nodes(i*(NumberElem[0] + 1) + j, new double[] {MinXCoord + j*dx, MinYCoord + i*dy, 0});
				}
			}
		}
		else if (ElemShape.equals("R8"))
		{
			Node = new Nodes[(2 * NumberElem[0] + 1)*(2 * NumberElem[1] + 1) - NumberElem[0]*NumberElem[1]];
			int nodeID = 0;
			dx = L / (2 * NumberElem[0]);
			dy = H / (2 * NumberElem[1]);
			for (int i = 0; i <= 2 * NumberElem[1]; i += 1)
			{
				if (i % 2 == 0)
				{
					for (int j = 0; j <= 2 * NumberElem[0]; j += 1)
					{
						Node[nodeID] = new Nodes(nodeID, new double[] {MinXCoord + j*dx, MinYCoord + i*dy, 0});
						nodeID += 1;
					}
				}
				else
				{
					for (int j = 0; j <= 2 * NumberElem[0] / 2; j += 1)
					{
						Node[nodeID] = new Nodes(nodeID, new double[] {MinXCoord + 2*j*dx, MinYCoord + i*dy, 0});
						nodeID += 1;
					}
				}
			
			}
		}
		else if (ElemShape.equals("R9"))
		{
			Node = new Nodes[(2*NumberElem[0] + 1)*(2*NumberElem[1] + 1)];
			for (int i = 0; i <= 2*NumberElem[1]; i += 1)
			{
				for (int j = 0; j <= 2*NumberElem[0]; j += 1)
				{
					Node[i*(2*NumberElem[0] + 1) + j] = new Nodes(i*(2*NumberElem[0] + 1) + j, new double[] {MinXCoord + j*dx, MinYCoord + i*dy, 0});
				}
			}
		}
		
		return Node;
	}
	
	public Elements[] CreateRadialMesh(Nodes[] Node, int noffsets, String ElemType)
	{
		Elements[] Elem = null;
		String ElemShape = Elements.DefineShape(ElemType);
		if (ElemShape.equals("Rectangular"))	// Rectangular mesh
		{
			int nNodesPerCicle = (Node.length - 1) / noffsets;
	        Elem = new Elements[(int) (nNodesPerCicle * (noffsets - 0.5))];
	        int cont = 0;
	        for (int i = 0; i <= noffsets - 1; i += 1)
			{
	        	int[] elemnodes = null;
			    if (i < noffsets - 1)
			    {
	    		    for (int j = 0; j <= nNodesPerCicle - 2; j += 1)
	        		{
	    		    	elemnodes = new int[] {i*nNodesPerCicle + j, i*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j};
	        		    Elem[cont] = new Elements(cont, elemnodes, null, null, null, ElemType);
	        		    cont += 1;
	        		}
			    	elemnodes = new int[] {(i + 1)*nNodesPerCicle - 1, i * nNodesPerCicle, (i + 1)*nNodesPerCicle, (i + 2)*nNodesPerCicle - 1};
	    		    Elem[cont] = new Elements(cont, elemnodes, null, null, null, ElemType);
	    		    cont += 1;
			    }
			    else
			    {
			    	elemnodes = new int[] {(i + 1) * nNodesPerCicle - 1, i * nNodesPerCicle, i * nNodesPerCicle + 1, Node.length - 1};
	    		    Elem[cont] = new Elements(cont, elemnodes, null, null, null, ElemType);
	    		    cont += 1;
	    		    for (int j = 1; j <= nNodesPerCicle / 2 - 1; j += 1)
	        		{
	    		    	elemnodes = new int[] {i * nNodesPerCicle + 2 * j - 1, i * nNodesPerCicle + 2 * j, i * nNodesPerCicle + 2 * j + 1, Node.length - 1};
	        		    Elem[cont] = new Elements(cont, elemnodes, null, null, null, ElemType);
	        		    cont += 1;
	        		}
			    }
			}
		}
		if (ElemShape.equals("Triangular"))	// Triangular mesh
		{
			// Elements
		    //   1.___.3
		    //    |\  | 
		    //    |2\1|  
		    //  2.|__\|.4
			int nNodesPerCicle = (Node.length - 1) / noffsets;
	        Elem = new Elements[(int) (2*nNodesPerCicle*(noffsets - 0.5))];
	        int cont = 0;
	        for (int i = 0; i <= noffsets - 1; i += 1)
			{
	        	int[] elemnodes = null;
			    if (i < noffsets - 1)
			    {
	    		    for (int j = 0; j <= nNodesPerCicle - 2; j += 1)
	        		{
	    		    	elemnodes = new int[] {i*nNodesPerCicle + j, i*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j};
	        		    Elem[cont] = new Elements(cont, elemnodes, null, null, null, ElemType);
	        		    cont += 1;
	        		    elemnodes = new int[] {i*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j + 1, (i + 1)*nNodesPerCicle + j};
	        		    Elem[cont] = new Elements(cont, elemnodes, null, null, null, ElemType);
	        		    cont += 1;
	        		}
			    	elemnodes = new int[] {(i + 1)*nNodesPerCicle - 1, (i + 1)*nNodesPerCicle, (i + 2)*nNodesPerCicle - 1};
	    		    Elem[cont] = new Elements(cont, elemnodes, null, null, null, ElemType);
	    		    cont += 1;
			    	elemnodes = new int[] {(i + 1)*nNodesPerCicle - 1, i*nNodesPerCicle, (i + 1)*nNodesPerCicle};
	    		    Elem[cont] = new Elements(cont, elemnodes, null, null, null, ElemType);
	    		    cont += 1;
			    }
			    else
			    {
	    		    for (int j = 0; j <= nNodesPerCicle - 2; j += 1)
	        		{
	    		    	elemnodes = new int[] {i*nNodesPerCicle + j, i*nNodesPerCicle + j + 1, Node.length - 1};
	        		    Elem[cont] = new Elements(cont, elemnodes, null, null, null, ElemType);
	        		    cont += 1;
	        		}
			    	elemnodes = new int[] {i*nNodesPerCicle + nNodesPerCicle - 1, i*nNodesPerCicle, Node.length - 1};
	    		    Elem[cont] = new Elements(cont, elemnodes, null, null, null, ElemType);
	    		    cont += 1;
			    }
			}
		}
        return Elem;
	}
	
	public Elements[] CreateCartesianMesh(Nodes[] Node, int[] NElems, String ElemType)
	{
		Elements[] Elem = null;
		String ElemShape = Elements.DefineShape(ElemType);
		if (ElemShape.equals("Rectangular"))	// Rectangular mesh
		{
			// Elements
		    //   4 ____ 3
		    //    |    | 
		    //    |    |  
		    //   1|____|2
			int[] NNodes = new int[] {NElems[0] + 1, NElems[1] + 1};
	        Elem = new Elements[NElems[0]*NElems[1]];
			for (int j = 0; j <= NElems[1] - 1; j += 1)
			{
				for (int i = 0; i <= NElems[0] - 1; i += 1)
				{
					int ElemID = i + j*NElems[0];
					int[] ElemNodes = new int[] {i + j*NNodes[0], i + j*NNodes[0] + 1, (j + 1)*NNodes[0] + i + 1, (j + 1)*NNodes[0] + i};
		        	Elem[ElemID] = new Elements(ElemID, ElemNodes, null, null, null, ElemType);
				}
			}
		}
		else if (ElemShape.equals("R8"))	// Rectangular mesh
		{
			// Elements
		    //   7____6____ 5
		    //    |        | 
		    //   8|        |4
		    //    |        |  
		    //   1|________|3
			//		  2
			int[] NNodes = new int[] {2 * NElems[0] + 1, 2 * NElems[1] + 1};
	        Elem = new Elements[NElems[0] * NElems[1]];
			for (int j = 0; j <= NElems[1] - 1; j += 1)
			{
				for (int i = 0; i <= NElems[0] - 1; i += 1)
				{
					int ElemID = i + j*NElems[0];
					int[] ElemNodes = new int[] {2*i + 2*j*NNodes[0] - j*NElems[0], 					2*i + 2*j*NNodes[0] - j*NElems[0] + 1, 						2*i + 2*j*NNodes[0] - j*NElems[0] + 2,
												 2*i + (2*j + 1)*NNodes[0] - j*NElems[0] - i + 1, 		2*i + (2*j + 2)*NNodes[0] - (j + 1)*NElems[0] + 2, 		2*i + (2*j + 2)*NNodes[0] - (j + 1)*NElems[0] + 1,
												 2*i + (2*j + 2)*NNodes[0] - (j + 1)*NElems[0],		 	2*i + (2*j + 1)*NNodes[0] - j*NElems[0] - i};
		        	Elem[ElemID] = new Elements(ElemID, ElemNodes, null, null, null, ElemType);
				}
			}
		}
		else if (ElemShape.equals("R9"))
		{
			// Elements
		    //   7____6____ 5
		    //    |        | 
		    //   8|   9    |4
		    //    |        |  
		    //   1|________|3
			//		  2
			int[] NNodes = new int[] {2 * NElems[0] + 1, 2 * NElems[1] + 1};
	        Elem = new Elements[NElems[0]*NElems[1]];
			for (int j = 0; j <= NElems[1] - 1; j += 1)
			{
				for (int i = 0; i <= NElems[0] - 1; i += 1)
				{
					int ElemID = i + j*NElems[0];
					int[] ElemExtNodes = new int[] {2*i + 2*j*NNodes[0], 2*i + 2*j*NNodes[0] + 1, 2*i + 2*j*NNodes[0] + 2,
													2*i + (2*j + 1)*NNodes[0] + 2, 2*i + (2*j + 2)*NNodes[0] + 2, 2*i + (2*j + 2)*NNodes[0] + 1, 2*i + (2*j + 2)*NNodes[0], 2*i + (2*j + 1)*NNodes[0]};
					int[] ElemIntNodes = new int[] {2*i + (2*j + 1)*NNodes[0] + 1};
					Elem[ElemID] = new Elements(ElemID, ElemExtNodes, ElemIntNodes, null, null, ElemType);
				}
			}
		}
		else if (ElemShape.equals("Triangular"))	// Triangular mesh
		{
			// Elements
		    //   1.___.3
		    //    |\  | 
		    //    |2\1|  
		    //  2.|__\|.4
			Elem = new Elements[2 * NElems[0] * NElems[1]];
			int NumRows = NElems[0];
		    for (int j = 0; j <= NumRows - 1; j += 1)
		    {
				int NumberElemInCol = NElems[1] + NElems[1];
		        for (int i = 0; i <= NumberElemInCol / 2 - 1; i += 1)
		        {
		        	int ElemID = 2 * i + j*NumberElemInCol;
		        	int[] elemnodes1 = new int[] {i + j * (NElems[0] + 1), i + j * (NElems[0] + 1) + 1, i + (j + 1) * (NElems[1] + 1)};
		        	int[] elemnodes2 = new int[] {i + (j + 1) * (NElems[1] + 1) + 1, i + (j + 1) * (NElems[1] + 1), i + j * (NElems[0] + 1) + 1};
		        	Elem[ElemID] = new Elements(ElemID, elemnodes1, null, null, null, ElemType);
		        	Elem[ElemID + 1] = new Elements(ElemID + 1, elemnodes2, null, null, null, ElemType);
		        }
		    }
		}
		return Elem;
	}
	
	public int CalcNFreeDoFs(Nodes[] Node, Elements[] Elem, Supports[] Sup)
	{
		int NFreeDoFs = 0;
        
        for (int node = 0; node <= Node.length - 1; node += 1)
        {
			NFreeDoFs += Node[node].getDOFType().length;
        }
        
        for (int sup = 0; sup <= Sup.length - 1; sup += 1)
    	{
        	int SupNode = Sup[sup].getNode();
    	    for (int dof = 0; dof <= Node[SupNode].getDOFType().length - 1; dof += 1)
        	{
    	    	if (Node[SupNode].getDOFType()[dof] <= Sup[sup].getDoFs().length - 1)
    	    	{
    	    		if (Sup[sup].getDoFs()[Node[SupNode].getDOFType()[dof]] == 1)
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
	
	public int[] DefineFreeDoFTypes(Nodes[] Node, Elements[] Elem, Supports[] Sup)
	{
		int[] FreeDOFTypes = null;
        
        for (int node = 0; node <= Node.length - 1; node += 1)
    	{
    	    for (int dof = 0; dof <= Node[node].getDOFType().length - 1; dof += 1)
        	{
    	    	if (-1 < Node[node].dofs[dof])
    	        {
    	    		FreeDOFTypes = Util.AddElem(FreeDOFTypes, Node[node].getDOFType()[dof]);
    	        }
        	}
    	}

    	return FreeDOFTypes;
	}
	
	public double[][] NumIntegration(Nodes[] Node, Elements Elem, double[] Mat, double[] Sec, int[][] DOFsPerNode, boolean NonlinearMat, boolean NonlinearGeo, double[] strain, int NPoints)
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
		double[][] D = Elem.BendingConstitutiveMatrix(Mat, NonlinearMat, strain);
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
			double a = Math.abs(Node[Elem.getExternalNodes()[2]].getOriginalCoords()[0] - Node[Elem.getExternalNodes()[0]].getOriginalCoords()[0]) / 2;
			double b = Math.abs(Node[Elem.getExternalNodes()[2]].getOriginalCoords()[1] - Node[Elem.getExternalNodes()[0]].getOriginalCoords()[1]) / 2;
			double[][] Db = Elem.BendingConstitutiveMatrix(Mat, NonlinearMat, strain);
			double[][] Ds = Elem.ShearConstitutiveMatrix2(Mat, Sec);
			for (int pe = 0; pe <= NPoints - 1; pe += 1)
			{
				for (int pn = 0; pn <= NPoints - 1; pn += 1)
				{
					double e = Points[pe], n = Points[pn];
					if (Elem.getType().equals("SM"))
					{
						for (int pw = 0; pw <= NPoints - 1; pw += 1)
						{
							double w = Points[pw];							
							double t = Sec[0] / 1000.0;
							double[][] Bb1 = Elem.Bb(e, n, w, Node, Sec, NonlinearGeo, 1);
							double[][] Bb2 = Elem.Bb(e, n, w, Node, Sec, NonlinearGeo, 2);
							double[][] Bs1 = Elem.Bs(e, n, w, Node, Sec, 1);
							double[][] Bs2 = Elem.Bs(e, n, w, Node, Sec, 2);
							double[][] Bb = Util.AddMatrix(Bb1, Bb2);
							double[][] Bs = Util.AddMatrix(Bs1, Bs2);
							
							k = Util.MultMatrix(Util.AddMatrix(k, Util.MultMatrix(Util.Transpose(Bb), Util.MultMatrix(Db, Bb))), Weights[pe*NPoints*NPoints + pn*NPoints + pw]);
							k = Util.MultMatrix(Util.AddMatrix(k, Util.MultMatrix(Util.Transpose(Bs), Util.MultMatrix(Ds, Bs))), Weights[pe*NPoints*NPoints + pn*NPoints + pw]);
							k = Util.MultMatrix(k, a * b * t / 2.0);
						}
					}
					else
					{
						double[][] B = Elem.SecondDerivativesb(e, n, Node, Sec, NonlinearGeo);
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
	
	public double[][] StructureStiffnessMatrix(int NFreeDOFs, Nodes[] Node, Elements[] Elem, Supports[] Sup, boolean NonlinearMat, boolean NonlinearGeo)
    {
        double[][] K = new double[NFreeDOFs][NFreeDOFs];
        		
        for (int elem = 0; elem <= Elem.length - 1; elem += 1)
        {
	        double[][] k = Elem[elem].StiffnessMatrix(Node, NonlinearMat, NonlinearGeo);
	        int LocalDOFi = 0, LocalDOFj = 0;
	        for (int elemnodei = 0; elemnodei <= Elem[elem].getExternalNodes().length - 1; elemnodei += 1)
            {
                int nodei = Elem[elem].getExternalNodes()[elemnodei];
                for (int elemnodej = 0; elemnodej <= Elem[elem].getExternalNodes().length - 1; elemnodej += 1)
                {
                    int nodej = Elem[elem].getExternalNodes()[elemnodej];
                    for (int dofi = 0; dofi <= Node[nodei].getDOFType().length - 1; dofi += 1)
        	        {
        	            for (int dofj = 0; dofj <= Node[nodej].getDOFType().length - 1; dofj += 1)
            	        {
        	            	int GlobalDOFi = Node[nodei].dofs[dofi], GlobalDOFj = Node[nodej].dofs[dofj];
            	            if (-1 < GlobalDOFi & -1 < GlobalDOFj)
            	            {
            	               K[GlobalDOFi][GlobalDOFj] += k[LocalDOFj][LocalDOFi];
            	            }
        	            	LocalDOFj += 1;
            	        }
    	            	LocalDOFi += 1;
    	            	LocalDOFj += -Node[nodej].getDOFType().length;
        	        }
    	        	LocalDOFi += -Node[nodei].getDOFType().length;
	            	LocalDOFj += Node[nodej].getDOFType().length;
                }
	        	LocalDOFi += Node[nodei].getDOFType().length;
                LocalDOFj = 0;
            }
        }
        return K;
    }
	
	public double[] LoadVector(Nodes[] Node, Elements[] Elem, int NFreeDOFs, ConcLoads[] ConcLoad, DistLoads[] DistLoad, NodalDisps[] NodalDisp, boolean NonlinearMat, boolean NonlinearGeo, double loadfactor)
	{
		double[] P = new double[NFreeDOFs];
		if (ConcLoad != null)
		{
			for (int load = 0; load <= ConcLoad.length - 1; load += 1)
			{
				int node = ConcLoad[load].getNode();
				for (int dof = 0; dof <= Node[node].getDOFType().length - 1; dof += 1)
				{
					if (-1 < Node[node].dofs[dof])
					{
						if (Node[node].getDOFType()[dof] <= ConcLoad[load].getLoads().length - 1)
						{
							P[Node[node].dofs[dof]] += ConcLoad[load].getLoads()[Node[node].getDOFType()[dof]] * loadfactor;
						}
					}
				}
			}
		}
		
		int[] ElemsWithDistLoad = null;
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			if (Elem[elem].getDistLoads() != null)
			{
				ElemsWithDistLoad = Util.AddElem(ElemsWithDistLoad, elem);
			}
		}
		
		if (ElemsWithDistLoad != null)
		{
			for (int e = 0; e <= ElemsWithDistLoad.length - 1; e += 1)
			{
				int elem = ElemsWithDistLoad[e];
				for (int load = 0; load <= Elem[elem].getDistLoads().length - 1; load += 1)
				{
					int LoadType = Elem[elem].getDistLoads()[load].getType();
					double LoadIntensity = Elem[elem].getDistLoads()[load].getIntensity();
					int[] nodes = Elem[elem].getExternalNodes();
					double[][] Q = Elem[elem].LoadVector(Node);
					double[] p = new double[Elem[elem].getDOFs().length];
					if (LoadType == 4)
					{
						p[0] = LoadIntensity;
						double[] q = Util.MultMatrixVector(Util.Transpose(Q), p);
						for (int elemnode = 0; elemnode <= nodes.length - 1; elemnode += 1)
						{
							int node = nodes[elemnode];

							for (int dof = 0; dof <= Node[node].getDOFType().length - 1; dof += 1)
							{
								if (-1 < Node[node].dofs[dof])
								{									
									P[Node[node].dofs[dof]] += q[Elem[elem].getCumDOFs()[elemnode] + dof] * loadfactor;
								}
							}
						}
					}
				}
			}
		}
		
		if (NodalDisp != null)
		{
			double[] Uapplied = new double[NFreeDOFs];
			for (int disp = 0; disp <= NodalDisp.length - 1; disp += 1)
			{
				int node = NodalDisp[disp].getNode();
				for (int dof = 0; dof <= Elem[0].getDOFsPerNode().length - 1; dof += 1)
				{
					if (-1 < Node[node].dofs[dof])
					{
						if (Node[node].getDOFType()[dof] <= NodalDisp[disp].getDisps().length - 1)
						{
							Uapplied[Node[node].dofs[dof]] += NodalDisp[disp].getDisps()[Node[node].getDOFType()[dof]]*loadfactor;
						}
					}
				}
			}
			for (int node = 0; node <= Node.length - 1; node += 1)
			{
				double[] Peq = NodeForces(node, Node, Elem, NonlinearMat, NonlinearGeo, Uapplied);
				for (int dof = 0; dof <= Peq.length - 1; dof += 1)
				{
					if (-1 < Node[node].dofs[dof])
					{
						P[Node[node].dofs[dof]] += Peq[dof];
					}
				}
			}
		}
		return P;
	}
	
    public double DispOnPoint(Nodes[] Node, Elements Elem, double e, double n, int dof, double[] u)
    {
    	double[] N = Elem.NaturalCoordsShapeFunctions(e, n, Node)[dof];
    	//System.out.println(Arrays.toString(N));
    	//System.out.println(Arrays.toString(u));
    	//System.out.println(Util.MultVector(N, u));
    	return Util.MultVector(N, u);
    }

    public double StrainOnElemContour(Nodes[] Node, Elements Elem, double e, double n, int dof, double[] s)
    {
    	double[] N = Elem.NaturalCoordsShapeFunctions(e, n, Node)[dof];
    	return Util.MultVector(N, s);
    }
    
    public double StressOnElemContour(Nodes[] Node, Elements Elem, double e, double n, int dof, double[] s)
    {
    	double[] N;
    	N = Elem.NaturalCoordsShapeFunctions(e, n, Node)[dof];
    	return Util.MultVector(N, s);
    }
    
    public double ForceOnElemContour(Nodes[] Node, Elements Elem, double e, double n, int dof, double[] p)
    {
    	double[] N;
    	N = Elem.NaturalCoordsShapeFunctions(e, n, Node)[dof];
    	return Util.MultVector(N, p);
    }
    
	public double[][] GetNodeDisplacements(Nodes[] Node, double[] u)
    {
        int NumDim = 6;
        double[][] NodeDisp = new double[Node.length][Node[0].getOriginalCoords().length];
        for (int node = 0; node <= Node.length - 1; node += 1)
    	{
    	    for (int dim = 0; dim <= NumDim - 1; dim += 1)
        	{
    	    	int dofPos = Util.ElemPosInArray(Node[node].getDOFType(), dim);
    	    	if (-1 < dofPos)
    	    	{
        	        if (-1 < Node[node].dofs[dofPos])
        	        {
        	           NodeDisp[node][dofPos] = u[Node[node].dofs[dofPos]];
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

	public Reactions[] Reactions(Nodes[] Node, Elements[] Elem, Supports[] Sup, boolean NonlinearMat, boolean NonlinearGeo, double[] U)
	{
		Reactions[] R = new Reactions[Sup.length];
		for (int node = 0; node <= Sup.length - 1; node += 1)
		{
			int nodeID = Sup[node].getNode();
			double[] NodeForces = NodeForces(nodeID, Node, Elem, NonlinearMat, NonlinearGeo, U);
			R[node] = new Reactions(node, nodeID, NodeForces);
		}	
		return R;
	}
	
	public double[] SumReactions(Reactions[] Reactions)
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
	
	public double[] NodeForces(int node, Nodes[] Node, Elements[] Elem, boolean NonlinearMat, boolean NonlinearGeo, double[] U)
	{
		double[] forces = new double[6];
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
        {
			for (int elemnode = 0; elemnode <= Elem[elem].getExternalNodes().length - 1; elemnode += 1)
	        {
				int NodeID = Elem[elem].getExternalNodes()[elemnode];
				if (node == NodeID)
				{
					double[] p = Elem[elem].ForceVec(Node, NonlinearMat, NonlinearGeo, U);
					for (int dof = 0; dof <= Node[node].getDOFType().length - 1; dof += 1)
			        {
						if (Node[node].getDOFType()[dof] <= 5)
						{
							forces[Node[node].getDOFType()[dof]] += p[Elem[elem].getCumDOFs()[elemnode] + dof];
						}
						else if (Node[node].getDOFType()[dof] == 7)
						{
							forces[3] += -p[Elem[elem].getCumDOFs()[elemnode] + dof];
						}
						else if (Node[node].getDOFType()[dof] == 8)
						{
							forces[4] += -p[Elem[elem].getCumDOFs()[elemnode] + dof];
						}
			        }
				}
	        }
        }
		
		return forces;
	}
}