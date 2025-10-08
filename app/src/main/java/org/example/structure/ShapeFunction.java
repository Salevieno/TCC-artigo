package org.example.structure;

import java.util.List;

import org.example.output.ResultDiagrams;

public abstract class ShapeFunction
{

    public static double[][] naturalCoordsShapeFunctions(ElemType type, double[] size, double e, double n, List<Node> Node)
    {
    	// Calcula as funçõs de forma em um dado ponto (e, n). Unidades 1/mâ e 1/m.
    	double[][] N = null;
		double a = size[0];
		double b = size[1];	
		
		switch (type)
		{
			case KR1:
				N = new double[3][12];
	    		N[0][0] = (1 - e)*(1 - n)*(2 - e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][1] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][2] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;
	    		N[0][3] = (1 + e)*(1 - n)*(2 + e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][4] = (1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][5] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][6] = (1 + e)*(1 + n)*(2 + e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][7] = (1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][8] = (1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][9] = (1 - e)*(1 + n)*(2 - e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][10] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][11] = (1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;  		
	    		N[1][0] = -(3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][1] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 - n) / 8;
	    		N[1][2] = -(Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][3] = (3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][4] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 - n) / 8;
	    		N[1][5] = (Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][6] = (3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][7] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 + n) / 8;
	    		N[1][8] = (Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[1][9] = -(3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][10] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 + n) / 8;
	    		N[1][11] = -(Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[2][0] = -(3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][1] = -(Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][2] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 - e) / 8;		
	    		N[2][3] = -(3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][4] = -(Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][5] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 + e) / 8;
	    		N[2][6] = (3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][7] = (Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][8] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 + e) / 8;
	    		N[2][9] = (3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][10] = (Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][11] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 - e) / 8;
	    		break;
	    		
			case KR2:
	    		N = new double[4][16];
	    		N[0][0] = (2 - 3 * e + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][1] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][2] = (2 - 3 * e + Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][3] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][4] = (2 + 3 * e - Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][5] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][6] = (2 + 3 * e - Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][7] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][8] = (2 + 3 * e - Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][9] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][10] = (2 + 3 * e - Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][11] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][12] = (2 - 3 * e + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][13] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][14] = (2 - 3 * e + Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][15] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[1][0] = 3 * (-1 + Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / (16 * a);
				N[1][1] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / (16 * a);
				N[1][2] = 3 * (-1 + Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][3] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][4] = 3 * (1 - Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / (16 * a);
				N[1][5] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / (16 * a);
				N[1][6] = 3 * (1 - Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][7] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][8] = 3 * (1 - Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / (16 * a);
				N[1][9] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / (16 * a);
				N[1][10] = 3 * (1 - Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][11] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][12] = 3 * (-1 + Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / (16 * a);
				N[1][13] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / (16 * a);
				N[1][14] = 3 * (-1 + Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[1][15] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / (16 * a);
				N[2][0] = (2 - 3 * e + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / (16 * b);
				N[2][1] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / (16 * b);
				N[2][2] = (2 - 3 * e + Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][3] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][4] = (2 + 3 * e - Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / (16 * b);
				N[2][5] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / (16 * b);
				N[2][6] = (2 + 3 * e - Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][7] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][8] = (2 + 3 * e - Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / (16 * b);
				N[2][9] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / (16 * b);
				N[2][10] = (2 + 3 * e - Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][11] = (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][12] = (2 - 3 * e + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / (16 * b);
				N[2][13] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / (16 * b);
				N[2][14] = (2 - 3 * e + Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[2][15] = (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (16 * b);
				N[3][0] = 9 * (1 - Math.pow(e, 2))*(1 - Math.pow(n, 2)) / (8 * a * b);
				N[3][1] = -3 * (1 - Math.pow(n, 2))*(-1 - 2 * e + 3 * Math.pow(e, 2)) / (8 * a * b);
				N[3][2] = -3 * (1 - Math.pow(e, 2))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][3] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][4] = -9 * (1 - Math.pow(e, 2))*(1 - Math.pow(n, 2)) / (8 * a * b);
				N[3][5] = -3 * (1 - Math.pow(n, 2))*(-1 + 2 * e + 3 * Math.pow(e, 2)) / (8 * a * b);
				N[3][6] = 3 * (1 - Math.pow(e, 2))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][7] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][8] = 9 * (1 - Math.pow(e, 2))*(1 - Math.pow(n, 2)) / (8 * a * b);
				N[3][9] = 3 * (1 - Math.pow(n, 2))*(-1 + 2 * e + 3 * Math.pow(e, 2)) / (8 * a * b);
				N[3][10] = 3 * (1 - Math.pow(e, 2))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][11] = (-1 + 2 * e + 3 * Math.pow(e, 2))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][12] = -9 * (1 - Math.pow(e, 2))*(1 - Math.pow(n, 2)) / (8 * a * b);
				N[3][13] = 3 * (1 - Math.pow(n, 2))*(-1 - 2 * e + 3 * Math.pow(e, 2)) / (8 * a * b);
				N[3][14] = -3 * (1 - Math.pow(e, 2))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				N[3][15] = (-1 - 2 * e + 3 * Math.pow(e, 2))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / (8 * a * b);
				break;
				
			case MR1, MR2:
	    		N = new double[3][12];
	    		N[0][0] = (1 - e)*(1 - n) / 4;
	    		N[0][3] = (1 + e)*(1 - n) / 4;
	    		N[0][6] = (1 + e)*(1 + n) / 4;
	    		N[0][9] = (1 - e)*(1 + n) / 4;
	    		N[1][1] = (1 - e)*(1 - n) / 4;
	    		N[1][4] = (1 + e)*(1 - n) / 4;
	    		N[1][7] = (1 + e)*(1 + n) / 4;
	    		N[1][10] = (1 - e)*(1 + n) / 4;
	    		N[2][2] = (1 - e)*(1 - n) / 4;
	    		N[2][5] = (1 + e)*(1 - n) / 4;
	    		N[2][8] = (1 + e)*(1 + n) / 4;
	    		N[2][11] = (1 - e)*(1 + n) / 4;
	    		break;
	    		
			case R4, Q4:
	    		N = new double[2][8];
	    		N[0][0] = (1 - e)*(1 - n) / 4;
	    		N[0][2] = (1 + e)*(1 - n) / 4;
	    		N[0][4] = (1 + e)*(1 + n) / 4;
	    		N[0][6] = (1 - e)*(1 + n) / 4;
	    		N[1][1] = (1 - e)*(1 - n) / 4;
	    		N[1][3] = (1 + e)*(1 - n) / 4;
	    		N[1][5] = (1 + e)*(1 + n) / 4;
	    		N[1][7] = (1 - e)*(1 + n) / 4;
	    		break;
	    		
			case T3G:
	    		N = new double[2][6];
	    		N[0][0] = e;
	    		N[0][2] = n;
	    		N[0][4] = 1 - e - n;
	    		N[1][1] = e;
	    		N[1][3] = n;
	    		N[1][5] = 1 - e - n;
	    		break;
	    		
			case T6G:
	    		N = new double[2][12];
	    		N[0][0] = e * (2 * e - 1);
	    		N[0][2] = n * (2 * n - 1);
	    		N[0][4] = (1 - e - n) * (2 * (1 - e - n) - 1);
	    		N[0][6] = 4 * n * (1 - e - n);
	    		N[0][8] = 4 * e * (1 - e - n);
	    		N[0][10] = 4*e*n;
	    		N[1][1] = e * (2 * e - 1);
	    		N[1][3] = n * (2 * n - 1);
	    		N[1][5] = (1 - e - n) * (2 * (1 - e - n) - 1);
	    		N[1][7] = 4 * n * (1 - e - n);
	    		N[1][9] = 4 * e * (1 - e - n);
	    		N[1][11] = 4*e*n;
	    		break;
	    		
			case SM:
	    		N = new double[5][20];
	    		N[0][0] = (1 - e)*(1 - n)*(2 - e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][1] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][2] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;
	    		N[0][5] = (1 + e)*(1 - n)*(2 + e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][6] = (1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][7] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][10] = (1 + e)*(1 + n)*(2 + e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][11] = (1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][12] = (1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][15] = (1 - e)*(1 + n)*(2 - e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][16] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][17] = (1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;
	    		N[1][0] = -(3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][1] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 - n) / 8;
	    		N[1][2] = -(Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][5] = (3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][6] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 - n) / 8;
	    		N[1][7] = (Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][10] = (3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][11] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 + n) / 8;
	    		N[1][12] = (Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[1][15] = -(3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][16] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 + n) / 8;
	    		N[1][17] = -(Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[2][0] = -(3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][1] = -(Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][2] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 - e) / 8;		
	    		N[2][5] = -(3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][6] = -(Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][7] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 + e) / 8;
	    		N[2][10] = (3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][11] = (Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][12] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 + e) / 8;
	    		N[2][15] = (3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][16] = (Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][17] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 - e) / 8;
	    		N[3][3] = (1 - e)*(1 - n) / 4;
	    		N[3][8] = (1 + e)*(1 - n) / 4;
	    		N[3][13] = (1 + e)*(1 + n) / 4;
	    		N[3][18] = (1 - e)*(1 + n) / 4;
	    		N[4][4] = (1 - e)*(1 - n) / 4;
	    		N[4][9] = (1 + e)*(1 - n) / 4;
	    		N[4][14] = (1 + e)*(1 + n) / 4;
	    		N[4][19] = (1 - e)*(1 + n) / 4;
	    		break;
	    		
			case SM8:
	    		N = new double[5][28];
	    		N[0][0] = (1 - e)*(1 - n)*(2 - e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][1] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][2] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;
	    		N[0][7] = (1 + e)*(1 - n)*(2 + e - n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][8] = (1 + e)*(Math.pow(e, 2) - 1)*(1 - n)*a/8;
	    		N[0][9] = (-1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][14] = (1 + e)*(1 + n)*(2 + e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][15] = (1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][16] = (1 + n)*(Math.pow(n, 2) - 1)*(1 + e)*b/8;
	    		N[0][21] = (1 - e)*(1 + n)*(2 - e + n - Math.pow(e, 2) - Math.pow(n, 2))/8;
	    		N[0][22] = (-1 + e)*(Math.pow(e, 2) - 1)*(1 + n)*a/8;
	    		N[0][23] = (1 + n)*(Math.pow(n, 2) - 1)*(1 - e)*b/8;
	    		N[1][0] = -(3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][1] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 - n) / 8;
	    		N[1][2] = -(Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][7] = (3 * (1 - Math.pow(e, 2)) + n*(-1 - n))*(1 - n) / (8 * a);
	    		N[1][8] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 - n) / 8;
	    		N[1][9] = (Math.pow(n, 2) - 1)*(n - 1)*b / (8 * a);
	    		N[1][14] = (3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][15] = (3 * Math.pow(e, 2) + 2 * e - 1)*(1 + n) / 8;
	    		N[1][16] = (Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[1][21] = -(3 * (1 - Math.pow(e, 2)) + n*(1 - n))*(1 + n) / (8 * a);
	    		N[1][22] = (3 * Math.pow(e, 2) - 2 * e - 1)*(1 + n) / 8;
	    		N[1][23] = -(Math.pow(n, 2) - 1)*(n + 1)*b / (8 * a);
	    		N[2][0] = -(3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][1] = -(Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][2] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 - e) / 8;		
	    		N[2][7] = -(3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][8] = -(Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][9] = (3 * Math.pow(n, 2) - 2 * n - 1)*(1 + e) / 8;
	    		N[2][14] = (3 * (1 - Math.pow(n, 2)) + e*(1 - e))*(1 + e) / (8 * b);
	    		N[2][15] = (Math.pow(e, 2) - 1)*(e + 1)*a / (8 * b);
	    		N[2][16] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 + e) / 8;
	    		N[2][21] = (3 * (1 - Math.pow(n, 2)) + e*(-1 - e))*(1 - e) / (8 * b);
	    		N[2][22] = (Math.pow(e, 2) - 1)*(e - 1)*a / (8 * b);
	    		N[2][23] = (3 * Math.pow(n, 2) + 2 * n - 1)*(1 - e) / 8;
	    		N[3][3] = (1 - e)*(1 - n)*(-1 - e - n) / 4;
	    		N[3][5] = (1 - Math.pow(e, 2))*(1 - n) / 2;
	    		N[3][10] = (1 + e)*(1 - n)*(-1 + e - n) / 4;
	    		N[3][12] = (1 - Math.pow(e, 2))*(1 + n) / 2;
	    		N[3][17] = (1 + e)*(1 + n)*(-1 + e + n) / 4;
	    		N[3][19] = (1 - Math.pow(n, 2))*(1 + e) / 2;
	    		N[3][24] = (1 - e)*(1 + n)*(-1 - e + n) / 4;
	    		N[3][26] = (1 - Math.pow(n, 2))*(1 - e) / 2;
	    		N[4][4] = (1 - e)*(1 - n)*(-1 - e - n) / 4;
	    		N[4][6] = (1 - Math.pow(e, 2))*(1 - n) / 2;
	    		N[4][11] = (1 + e)*(1 - n)*(-1 + e - n) / 4;
	    		N[4][13] = (1 - Math.pow(e, 2))*(1 + n) / 2;
	    		N[4][18] = (1 + e)*(1 + n)*(-1 + e + n) / 4;
	    		N[4][20] = (1 - Math.pow(n, 2))*(1 + e) / 2;
	    		N[4][25] = (1 - e)*(1 + n)*(-1 - e + n) / 4;
	    		N[4][27] = (1 - Math.pow(n, 2))*(1 - e) / 2;
	    		break;
	    		
			case KP3:
	    		N = new double[4][12];
	    		N[0][0] = (2 - 3 * e + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][1] = a * (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][2] = b * (2 - 3 * e + Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][3] = (2 + 3 * e - Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][4] = a * (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[0][5] = b * (2 + 3 * e - Math.pow(e, 3))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][6] = (2 + 3 * e - Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][7] = a * (-1 - e + Math.pow(e, 2) + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][8] = b * (2 + 3 * e - Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[0][9] = (2 - 3 * e + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][10] = a * (1 - e - Math.pow(e, 2) + Math.pow(e, 3))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[0][11] = b * (2 - 3 * e + Math.pow(e, 3))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;		
				N[1][0] = 3 * (-1 + Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[1][1] = a * (-1 - 2 * e + 3 * Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[1][2] = b * 3 * (-1 + Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[1][3] = 3 * (1 - Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[1][4] = a * (-1 + 2 * e + 3 * Math.pow(e, 2))*(2 - 3 * n + Math.pow(n, 3)) / 16;
				N[1][5] = b * 3 * (1 - Math.pow(e, 2))*(1 - n - Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[1][6] = 3 * (1 - Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[1][7] = a * (-1 + 2 * e + 3 * Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[1][8] = b * 3 * (1 - Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[1][9] = 3 * (-1 + Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[1][10] = a * (-1 - 2 * e + 3 * Math.pow(e, 2))*(2 + 3 * n - Math.pow(n, 3)) / 16;
				N[1][11] = b * 3 * (-1 + Math.pow(e, 2))*(-1 - n + Math.pow(n, 2) + Math.pow(n, 3)) / 16;
				N[2][0] = (2 - 3 * e + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / 16;
				N[2][1] = a * (1 - e - Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / 16;
				N[2][2] = b * (2 - 3 * e + Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / 16;
				N[2][3] = (2 + 3 * e - Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / 16;
				N[2][4] = a * (-1 - e + Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (-1 + Math.pow(n, 2)) / 16;
				N[2][5] = b * (2 + 3 * e - Math.pow(e, 3))*(-1 - 2 * n + 3 * Math.pow(n, 2)) / 16;
				N[2][6] = (2 + 3 * e - Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / 16;
				N[2][7] = a * (-1 - e + Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / 16;
				N[2][8] = b * (2 + 3 * e - Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / 16;
				N[2][9] = (2 - 3 * e + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / 16;
				N[2][10] = a * (1 - e - Math.pow(e, 2) + Math.pow(e, 3)) * 3 * (1 - Math.pow(n, 2)) / 16;
				N[2][11] = b * (2 - 3 * e + Math.pow(e, 3))*(-1 + 2 * n + 3 * Math.pow(n, 2)) / 16;
				break;
				
			case SM_C:
				N = new double[5][24];
	    		N[0] = new double[] {((e*e*e-3*e+2)*(n*n*n-3*n+2))/16,	((e*e*e-e*e-e+1)*(n*n*n-3*n+2))/16,	((e*e*e-3*e+2)*(n*n*n-n*n-n+1))/16,	((e*e*e-e*e-e+1)*(n*n*n-n*n-n+1))/16,	0,	0,	((-e*e*e+3*e+2)*(n*n*n-3*n+2))/16,	((e*e*e+e*e-e-1)*(n*n*n-3*n+2))/16,	((-e*e*e+3*e+2)*(n*n*n-n*n-n+1))/16,	((e*e*e+e*e-e-1)*(n*n*n-n*n-n+1))/16,	0,	0,	((-e*e*e+3*e+2)*(-n*n*n+3*n+2))/16,	((e*e*e+e*e-e-1)*(-n*n*n+3*n+2))/16,	((-e*e*e+3*e+2)*(n*n*n+n*n-n-1))/16,	((e*e*e+e*e-e-1)*(n*n*n+n*n-n-1))/16,	0,	0,	((e*e*e-3*e+2)*(-n*n*n+3*n+2))/16,	((e*e*e-e*e-e+1)*(-n*n*n+3*n+2))/16,	((e*e*e-3*e+2)*(n*n*n+n*n-n-1))/16,	((e*e*e-e*e-e+1)*(n*n*n+n*n-n-1))/16,	0,	0} ;
	    		N[1] = new double[] {((3*e*e-3)*(n*n*n-3*n+2))/(16*a),	((3*e*e-2*e-1)*(n*n*n-3*n+2))/(16*a),	((3*e*e-3)*(n*n*n-n*n-n+1))/(16*a),	((3*e*e-2*e-1)*(n*n*n-n*n-n+1))/(16*a),	0,	0,	((3-3*e*e)*(n*n*n-3*n+2))/(16*a),	((3*e*e+2*e-1)*(n*n*n-3*n+2))/(16*a),	((3-3*e*e)*(n*n*n-n*n-n+1))/(16*a),	((3*e*e+2*e-1)*(n*n*n-n*n-n+1))/(16*a),	0,	0,	((3-3*e*e)*(-n*n*n+3*n+2))/(16*a),	((3*e*e+2*e-1)*(-n*n*n+3*n+2))/(16*a),	((3-3*e*e)*(n*n*n+n*n-n-1))/(16*a),	((3*e*e+2*e-1)*(n*n*n+n*n-n-1))/(16*a),	0,	0,	((3*e*e-3)*(-n*n*n+3*n+2))/(16*a),	((3*e*e-2*e-1)*(-n*n*n+3*n+2))/(16*a),	((3*e*e-3)*(n*n*n+n*n-n-1))/(16*a),	((3*e*e-2*e-1)*(n*n*n+n*n-n-1))/(16*a),	0,	0} ;
				N[2] = new double[] {((e*e*e-3*e+2)*(3*n*n-3))/(16*b),	((e*e*e-e*e-e+1)*(3*n*n-3))/(16*b),	((e*e*e-3*e+2)*(3*n*n-2*n-1))/(16*b),	((e*e*e-e*e-e+1)*(3*n*n-2*n-1))/(16*b),	0,	0,	((-e*e*e+3*e+2)*(3*n*n-3))/(16*b),	((e*e*e+e*e-e-1)*(3*n*n-3))/(16*b),	((-e*e*e+3*e+2)*(3*n*n-2*n-1))/(16*b),	((e*e*e+e*e-e-1)*(3*n*n-2*n-1))/(16*b),	0,	0,	((-e*e*e+3*e+2)*(3-3*n*n))/(16*b),	((e*e*e+e*e-e-1)*(3-3*n*n))/(16*b),	((-e*e*e+3*e+2)*(3*n*n+2*n-1))/(16*b),	((e*e*e+e*e-e-1)*(3*n*n+2*n-1))/(16*b),	0,	0,	((e*e*e-3*e+2)*(3-3*n*n))/(16*b),	((e*e*e-e*e-e+1)*(3-3*n*n))/(16*b),	((e*e*e-3*e+2)*(3*n*n+2*n-1))/(16*b),	((e*e*e-e*e-e+1)*(3*n*n+2*n-1))/(16*b),	0,	0} ;
				N[3] = new double[] {0,	0,	0,	0,	((1-e)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(n+1))/4,	0,	0,	0,	0,	0,	((1-e)*(n+1))/4,	0} ;
				N[4] = new double[] {0,	0,	0,	0,	0,	((1-e)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(1-n))/4,	0,	0,	0,	0,	0,	((e+1)*(n+1))/4,	0,	0,	0,	0,	0,	((1-e)*(n+1))/4} ;	
				break;
				
			case SM_H:
				break;
				
		}
    	
    	return N;
    }

	public static double[][] RealCoordsShapeFunctions(ElemType type, double[][] NodesCoords, double[] PointCoords)
    {
    	// Calcula as funçõs de forma de um elemento dadas as suas coordenadas reais. Unidades 1/mâ e 1/m.
    	double[][] N = null;
    	if (type.equals(ElemType.T3G))
    	{
    		N = new double[2][6];
    	    double A = ResultDiagrams.TriArea(NodesCoords);
    	    double x1 = NodesCoords[0][0], x2 = NodesCoords[1][0], x3 = NodesCoords[2][0];
    	    double y1 = NodesCoords[0][1], y2 = NodesCoords[1][1], y3 = NodesCoords[2][1];
    	    double x = PointCoords[0];
    	    double y = PointCoords[1];
    	    double[] alfa = new double[] {x2 * y3 - x3 * y2, x3 * y1 - x1 * y3, x1 * y2 - y1 * x2};
    	    double[] beta = new double[] {y2 - y3, y3 - y1, y1 - y2};
    	    double[] gama = new double[] {x3 - x2, x1 - x3, x2 - x1};
    		double Ni = 1 / (2 * A) * (alfa[0] + beta[0] * x + gama[0] * y);
    		double Nj = 1 / (2 * A) * (alfa[1] + beta[1] * x + gama[1] * y);
    		double Nm = 1 / (2 * A) * (alfa[2] + beta[2] * x + gama[2] * y);
    		N[0][0] = Ni;
    		N[0][2] = Nj;
    		N[0][4] = Nm;
    		N[1][1] = Ni;
    		N[1][3] = Nj;
    		N[1][5] = Nm;
    	}
    	
    	return N;
    }
}
