package org.example.utilidades;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.loading.ConcLoads;
import org.example.loading.DistLoads;
import org.example.loading.NodalDisps;
import org.example.mainTCC.InputDTO;
import org.example.mainTCC.ReadInput;
import org.example.structure.ElemShape;
import org.example.structure.Element;
import org.example.structure.Mesh;
import org.example.structure.MeshType;
import org.example.structure.Node;
import org.example.structure.Reactions;
import org.example.structure.Supports;

public abstract class Util 
{	
	
	public static Color[] RandomColors(int n)
	{
		Color[] RColors = new Color[n];
		for (int c = 0; c <= n - 1; c += 1)
		{
			RColors[c] = new Color((int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random()));
		}
		
		return RColors;
	}
	
	public static double dist(double[] Point1, double[] Point2)
	{
		return Math.sqrt(Math.pow(Point2[0] - Point1[0], 2) + Math.pow(Point2[1] - Point1[1], 2));
	}
	
	public static double dist(Point Point1, Point Point2)
	{
		return Math.sqrt(Math.pow(Point2.x - Point1.x, 2) + Math.pow(Point2.y - Point1.y, 2));
	}

	public static double[] ConvertToRealCoordsPoint3D(int[] OriginalCoords, Point3D CoordsCenter, Point CanvasPos, int[] CanvasSize, double[] CanvasDim, int[] CanvasCenter, int[] DrawingPos)
	{
		return new double[] {(OriginalCoords[0] - DrawingPos[0] - CanvasCenter[0])*CanvasDim[0]/CanvasSize[0] + CoordsCenter.x, -(OriginalCoords[1] - DrawingPos[1] - CanvasCenter[1])*CanvasDim[1]/CanvasSize[1] + CoordsCenter.y};
	}
	

	public static int[] ConvertToDrawingCoords2Point3D(double[] OriginalCoords, Point3D CoordsCenter, Point CanvasPos, int[] CanvasSize, double[] CanvasDim, int[] CanvasCenter, int[] DrawingPos)
	{
		return new int[] {(int) (DrawingPos[0] + CanvasCenter[0] + (OriginalCoords[0] - CoordsCenter.x)/CanvasDim[0]*CanvasSize[0]), (int) (DrawingPos[1] + CanvasCenter[1] - (OriginalCoords[1] - CoordsCenter.y)/CanvasDim[1]*CanvasSize[1])};
	}
	

	public static double[] InNaturalCoordsTriangle(double[][] Coords, double[] Point)
	{
		double[] natCoords = new double[3];
		double TriangleArea = TriArea(new double[][] {Coords[0], Coords[1], Coords[2]});
		
		natCoords[0] = TriArea(new double[][] {Point, Coords[1], Coords[2]}) / TriangleArea;
		natCoords[1] = TriArea(new double[][] {Point, Coords[2], Coords[0]}) / TriangleArea;
		natCoords[2] = 1 - natCoords[0] - natCoords[1];
		return natCoords;
	}
	
	public static double[] InNaturalCoordsRect(double[] CenterCoords, double L, double H, double[] Point)
	{
		return new double[] {2 * (Point[0] - CenterCoords[0]) / L, 2 * (Point[1] - CenterCoords[1]) / H};
	}
	
	public static double[] ScaledDefCoords(double[] OriginalCoords, double[] Disp, int[] DOFsOnNode, double scale)
	{
		double[] DeformedCoords = new double[OriginalCoords.length];
		for (int dim = 0; dim <= DeformedCoords.length - 1; dim += 1)
		{
			int dof = ElemPosInArray(DOFsOnNode, dim);
			if (-1 < dof)
			{
				DeformedCoords[dim] = OriginalCoords[dim] + scale * Disp[dof];
			}
			else
			{
				DeformedCoords[dim] = OriginalCoords[dim];
			}
		}
		return DeformedCoords;
	}
		

	public static double TriArea(double[][] Coords)
    {
        double x1 = Coords[0][0], x2 = Coords[1][0], x3 = Coords[2][0];
    	double y1 = Coords[0][1], y2 = Coords[1][1], y3 = Coords[2][1];
    	double A = (x1 * y2 + x2 * y3 + x3 * y1 - y1 * x2 - y2 * x3 - y3 * x1) / 2.0;
    	return A;
    }


	public static double FindMaxAbs(double[] SetOfValues)
	{
		double MaxValue = SetOfValues[0];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			if (MaxValue < Math.abs(SetOfValues[v]))
			{
				MaxValue = Math.abs(SetOfValues[v]);
			}
		}
		return MaxValue;
	}

	public static int FindMin(int[] SetOfValues)
	{
		int MinValue = SetOfValues[0];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			if (SetOfValues[v] < MinValue)
			{
				MinValue = SetOfValues[v];
			}
		}
		return MinValue;
	}
	
	public static double FindMin(double[] SetOfValues)
	{
		double MinValue = SetOfValues[0];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			if (SetOfValues[v] < MinValue)
			{
				MinValue = SetOfValues[v];
			}
		}
		return MinValue;
	}

	public static int FindMax(int[] SetOfValues)
	{
		int MaxValue = SetOfValues[0];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			if (MaxValue < SetOfValues[v])
			{
				MaxValue = SetOfValues[v];
			}
		}
		return MaxValue;
	}

	public static double[] FindMinPerPos(double[][] SetOfValues)
	{
		double[] MinValues = new double[SetOfValues[0].length];
		for (int pos = 0; pos <= SetOfValues[0].length - 1; pos += 1)
		{
			MinValues[pos] = SetOfValues[0][pos];
		}
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			for (int pos = 0; pos <= SetOfValues[v].length - 1; pos += 1)
			{
				if (SetOfValues[v][pos] < MinValues[pos])
				{
					MinValues[pos] = SetOfValues[v][pos];
				}
			}
		}
		return MinValues;
	}

	public static double[] FindMaxPerPos(double[][] SetOfValues)
	{
		double[] MinValues = new double[SetOfValues[0].length];
		for (int pos = 0; pos <= SetOfValues[0].length - 1; pos += 1)
		{
			MinValues[pos] = SetOfValues[0][pos];
		}
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			for (int pos = 0; pos <= SetOfValues[v].length - 1; pos += 1)
			{
				if (MinValues[pos] < SetOfValues[v][pos])
				{
					MinValues[pos] = SetOfValues[v][pos];
				}
			}
		}
		
		return MinValues;
	}
	

 	public static double[] AddElem(double[] OriginalArray, double NewElem)
	{
		if (OriginalArray == null)
		{
			return new double[] {NewElem};
		}
		else
		{
			double[] NewArray = new double[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}

 	public static double[] JoinVec(double[] OriginalArray, double[] NewVec)
	{
		if (OriginalArray == null)
		{
			return NewVec;
		}
		else
		{
			double[] NewArray = new double[OriginalArray.length + NewVec.length];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			for (int i = 0; i <= NewVec.length - 1; i += 1)
			{
				NewArray[OriginalArray.length + i] = NewVec[i];
			}
			return NewArray;
		}
	}
 	
 	public static ConcLoads[] AddElem(ConcLoads[] OriginalArray, ConcLoads NewElem)
 	{
 		if (OriginalArray == null)
		{
			return new ConcLoads[] {NewElem};
		}
		else
		{
			ConcLoads[] NewArray = new ConcLoads[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
 	}
 	
 	public static DistLoads[] AddElem(DistLoads[] OriginalArray, DistLoads NewElem)
 	{
 		if (OriginalArray == null)
		{
			return new DistLoads[] {NewElem};
		}
		else
		{
			DistLoads[] NewArray = new DistLoads[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
 	}
 	
 	public static NodalDisps[] AddElem(NodalDisps[] OriginalArray, NodalDisps NewElem)
 	{
 		if (OriginalArray == null)
		{
			return new NodalDisps[] {NewElem};
		}
		else
		{
			NodalDisps[] NewArray = new NodalDisps[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
 	}
 	
	public static String[][] AddElem(String[][] OriginalArray, String[] NewElem)
	{
		if (OriginalArray == null)
		{
			return new String[][] {NewElem};
		}
		else
		{
			String[][] NewArray = new String[OriginalArray.length + 1][];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}
	
	public static int[][] AddElem(int[][] OriginalArray, int[] NewElem)
	{
		if (OriginalArray == null)
		{
			return new int[][] {NewElem};
		}
		else
		{
			int[][] NewArray = new int[OriginalArray.length + 1][];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}
	
	public static double[][] AddElem(double[][] OriginalArray, double[] NewElem)
	{
		if (OriginalArray == null)
		{
			return new double[][] {NewElem};
		}
		else
		{
			double[][] NewArray = new double[OriginalArray.length + 1][];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}
	

	public static Supports[] IncreaseArraySize(Supports[] OriginalArray, int size)
	{
		if (OriginalArray == null)
		{
			return new Supports[size];
		}
		else
		{
			Supports[] NewArray = new Supports[OriginalArray.length + size];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			return NewArray;
		}
	}

	public static ConcLoads[] IncreaseArraySize(ConcLoads[] OriginalArray, int size)
	{
		if (OriginalArray == null)
		{
			ConcLoads[] NewArray = new ConcLoads[size];
			for (int i = 0; i <= NewArray.length - 1; i += 1)
			{
				NewArray[i] = new ConcLoads(-1, -1, null);
			}
			return NewArray;
		}
		else
		{
			ConcLoads[] NewArray = new ConcLoads[OriginalArray.length + size];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			for (int i = OriginalArray.length; i <= OriginalArray.length + size - 1; i += 1)
			{
				NewArray[i] = new ConcLoads(-1, -1, null);
			}
			return NewArray;
		}
	}

	public static NodalDisps[] IncreaseArraySize(NodalDisps[] OriginalArray, int size)
	{
		if (OriginalArray == null)
		{
			return new NodalDisps[size];
		}
		else
		{
			NodalDisps[] NewArray = new NodalDisps[OriginalArray.length + size];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			return NewArray;
		}
	}
	
	public static int[] AddElem(int[] OriginalArray, int NewElem)
	{
		if (OriginalArray == null)
		{
			return new int[] {NewElem};
		}
		else
		{
			int[] NewArray = new int[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}
	

	public static boolean ArrayContains(int[] OriginalArray, int elem)
	{
		for (int i = 0; i <= OriginalArray.length - 1; i += 1)
		{
			if (OriginalArray[i] == elem)
			{
				return true;
			}
		}
		return false;
	}

	public static int ElemPosInArray(int[] OriginalArray, int elem)
	{
		int pos = -1;
		for (int i = 0; i <= OriginalArray.length - 1; i += 1)
		{
			if (OriginalArray[i] == elem)
			{
				return i;
			}
		}
		return pos;
	}
	

	public static int ProdVec(int[] Vector)
	{
		int prod = 1;
		for (int i = 0; i <= Vector.length - 1; i += 1)
		{
			if (0 < Vector[i])
			{
				prod = prod*Vector[i];
			}
		}
		return prod;
	}

	public static double[][] AddMatrix(double[][] A, double[][] B)
	{
		/*This function adds two matrices*/
        int aRows = A.length, aColumns = A[0].length;
        int bRows = B.length, bColumns = B[0].length;
        double[][] C = new double[aRows][bColumns];
		if (aColumns != bColumns | aRows != bRows) 
        {
            System.out.println("The dimensions of matrices A and B do no match in Util -> AddMatrix");
        }
		else
		{
	        for (int i = 0; i < aRows; i += 1)
	        { 
	            for (int j = 0; j < bColumns; j += 1)
	            {
	            	C[i][j] = A[i][j] + B[i][j];
	            }
	        }
		}
        return C;
	}
	
	public static double[][] MultMatrix(double[][] A, double b)
	{
		/*This function multiplies a matrix by a scalar*/
		
        int aRows = A.length, aColumns = A[0].length;
        double[][] B = new double[aRows][aColumns];
        for (int i = 0; i < aRows; i += 1)
        { 
            for (int j = 0; j < aColumns; j += 1)
            {
            	B[i][j] = b * A[i][j];
            }
        }
        return B;
	}
	
	public static double[][] MultMatrix (double[][] A, double[][] B) 
	{
		/*This function multiplies two matrices*/
		
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;
        double[][] C = new double[aRows][bColumns];
        
        if (aColumns != bRows) 
        {
            throw new IllegalArgumentException("Number of columns in A (" + aColumns + ") did not match the number of rows in B (" + bRows + ").");
        }

        for (int i = 0; i < aRows; ++i)
        { 
            for (int j = 0; j < bColumns; ++j)
            {
                for (int k = 0; k < aColumns; ++k) 
                {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }
	
	public static int[][] MultMatrix (int[][] A, double[][] B) 
	{
		/*This function multiplies two matrices*/
		
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;
        int[][] C = new int[aRows][bColumns];
        
        if (aColumns != bRows) 
        {
            throw new IllegalArgumentException("Number of columns in A (" + aColumns + ") did not match the number of rows in B (" + bRows + ").");
        }

        for (int i = 0; i < aRows; ++i)
        { 
            for (int j = 0; j < bColumns; ++j)
            {
                for (int k = 0; k < aColumns; ++k) 
                {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }
	
	public static double[] MultMatrixVector (double[][] A, double[] b)
	{
		/*This function multiplies a matrix and a vector*/
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = b.length;
		double[] C = new double[aRows];
		if (aColumns != bRows) 
        {
            System.out.println("Number of columns in A (" + aColumns + ") did not match the number of rows in b (" + bRows + ") at Util -> MultMatrixVector");
        }	
        for (int i = 0; i <= aRows - 1; i += 1)
        { 
            for (int j = 0; j <= bRows - 1; j += 1)
            {
            	C[i] += A[i][j] * b[j];
            }
        }      
		return C;
	}

	public static double[] MultVector (double[] a, double b)
	{
		/*This function multiplies two vectors*/
		
        int aColumns = a.length;
		double[] C = new double[a.length];
		
        for (int i = 0; i < aColumns; ++i)
        { 
        	C[i] += a[i] * b;
        }   		
		return C;
	}
	
	public static double MultVector (double[] a, double[] b)
	{
		/*This function multiplies two vectors*/
		
        int aColumns = a.length;
        int bRows = b.length;
		double C = 0;
		
		if (aColumns != bRows) 
        {
			System.out.println("Number of columns in a (" + aColumns + ") did not match the number of rows in b (" + bRows + ").");
        }
		
        for (int i = 0; i < bRows; ++i)
        { 
        	C += a[i] * b[i];
        }   		
		return C;
	}
	
	public static double[][] Transpose (double[][] matrix) 
	{
		/*This function calculates the transpose of a matrix*/
		
        int matrixRows = matrix.length;
        int matrixColumns = matrix[0].length;
		double[][] matrixTranspose = new double[matrixColumns][matrixRows];
        for (int i = 0; i <= matrixRows - 1; i += 1)
        { 
            for (int j = 0; j <= matrixColumns - 1; j += 1)
            {
                matrixTranspose[j][i] = matrix[i][j];
            }
        }
		return matrixTranspose;
	}


	public static int[] RotateCoord(int[] OriCoord, int[] RefPoint, double[] angle)
	{
		int[] coord = new int[3];
		int[] oriCoord = new int[3];
		coord[0] = OriCoord[0];
		coord[1] = OriCoord[1];
		oriCoord[0] = OriCoord[0];
		oriCoord[1] = OriCoord[1];
		if (3 <= OriCoord.length)
		{
			coord[2] = OriCoord[2];
			oriCoord[2] = OriCoord[2];
		}
		else
		{
			OriCoord = AddElem(OriCoord, 0);
		}
		if (angle.length < 3)
		{
			angle = AddElem(angle, 0);
		}
		if (RefPoint.length < 3)
		{
			RefPoint = AddElem(RefPoint, 0);
		}
		// Rotaciona o ponto informado (OriCoord) ao redor do ponto de referância (RefPoint)
		// Rotation around z
		coord[0] = (int) ((oriCoord[0] - RefPoint[0]) * Math.cos(angle[2]) - (oriCoord[1] - RefPoint[1]) * Math.sin(angle[2]));
		coord[1] = (int) ((oriCoord[0] - RefPoint[0]) * Math.sin(angle[2]) + (oriCoord[1] - RefPoint[1]) * Math.cos(angle[2]));
		oriCoord[0] = coord[0] + RefPoint[0];
		oriCoord[1] = coord[1] + RefPoint[1];
		// Rotation around y
		coord[0] = (int) ((oriCoord[0] - RefPoint[0]) * Math.cos(angle[1]) - (oriCoord[2] - RefPoint[2]) * Math.sin(angle[1]));
		coord[2] = (int) ((oriCoord[0] - RefPoint[0]) * Math.sin(angle[1]) + (oriCoord[2] - RefPoint[2]) * Math.cos(angle[1]));
		oriCoord[0] = coord[0] + RefPoint[0];
		oriCoord[2] = coord[2] + RefPoint[2];
		// Rotation around x
		coord[1] = (int) ((oriCoord[1] - RefPoint[1]) * Math.cos(angle[0]) - (oriCoord[2] - RefPoint[2]) * Math.sin(angle[0]));
		coord[2] = (int) ((oriCoord[1] - RefPoint[1]) * Math.sin(angle[0]) + (oriCoord[2] - RefPoint[2]) * Math.cos(angle[0]));
		oriCoord[1] = coord[1] + RefPoint[1];
		oriCoord[2] = coord[2] + RefPoint[2];
		
		coord[0] = oriCoord[0];
		coord[1] = oriCoord[1];
		coord[2] = oriCoord[2];
		return coord;
	}
	
	public static double[] RotateCoord(double[] OriCoord, double[] RefPoint, double[] angle)
	{
		double[] coord = new double[3];
		double[] oriCoord = new double[3];
		coord[0] = OriCoord[0];
		coord[1] = OriCoord[1];
		oriCoord[0] = OriCoord[0];
		oriCoord[1] = OriCoord[1];
		if (3 <= OriCoord.length)
		{
			coord[2] = OriCoord[2];
			oriCoord[2] = OriCoord[2];
		}
		else
		{
			OriCoord = AddElem(OriCoord, 0);
		}
		if (angle.length < 3)
		{
			angle = AddElem(angle, 0);
		}
		if (RefPoint.length < 3)
		{
			RefPoint = AddElem(RefPoint, 0);
		}
		// Rotaciona o ponto informado (OriCoord) ao redor do ponto de referância (RefPoint)
		// Rotation around z
		coord[0] = (oriCoord[0] - RefPoint[0]) * Math.cos(angle[2]) - (oriCoord[1] - RefPoint[1]) * Math.sin(angle[2]);
		coord[1] = (oriCoord[0] - RefPoint[0]) * Math.sin(angle[2]) + (oriCoord[1] - RefPoint[1]) * Math.cos(angle[2]);
		oriCoord[0] = coord[0] + RefPoint[0];
		oriCoord[1] = coord[1] + RefPoint[1];
		// Rotation around y
		coord[0] = (oriCoord[0] - RefPoint[0]) * Math.cos(angle[1]) - (oriCoord[2] - RefPoint[2]) * Math.sin(angle[1]);
		coord[2] = (oriCoord[0] - RefPoint[0]) * Math.sin(angle[1]) + (oriCoord[2] - RefPoint[2]) * Math.cos(angle[1]);
		oriCoord[0] = coord[0] + RefPoint[0];
		oriCoord[2] = coord[2] + RefPoint[2];
		// Rotation around x
		coord[1] = (oriCoord[1] - RefPoint[1]) * Math.cos(angle[0]) - (oriCoord[2] - RefPoint[2]) * Math.sin(angle[0]);
		coord[2] = (oriCoord[1] - RefPoint[1]) * Math.sin(angle[0]) + (oriCoord[2] - RefPoint[2]) * Math.cos(angle[0]);
		oriCoord[1] = coord[1] + RefPoint[1];
		oriCoord[2] = coord[2] + RefPoint[2];
		
		coord[0] = oriCoord[0];
		coord[1] = oriCoord[1];
		coord[2] = oriCoord[2];
		return coord;
	}

    public static int[][] MatrixDoubleToInt(double[][] matrix)
    {
    	int[][] newmatrix = new int[matrix.length][];
    	
    	for (int i = 0; i <= matrix.length - 1; i += 1)
    	{
    		newmatrix[i] = new int[matrix[i].length];
    		for (int j = 0; j <= matrix[i].length - 1; j += 1)
        	{
        		newmatrix[i][j] = (int) (matrix[i][j]);
        	}
    	}
    	
    	return newmatrix;
    }
    

	public static float Round(Double num, int decimals)
	{
		if (!num.isNaN())
		{
			return BigDecimal.valueOf(num).round(new MathContext(decimals, RoundingMode.HALF_UP)).floatValue();
		}
		else
		{
			System.out.println("Number is NaN at Util -> Round");
			return 0;
		}
	}

	public static int Average(int[] values)
	{
		int avr = 0;
		for (int i = 0; i <= values.length - 1; i += 1)
		{
			avr += values[i];
		}
		avr = avr / (int)values.length;
		return avr;
	}
	
	public static int clamp(int value, int min, int max)
	{
		return Math.min(Math.max(value, min), max);
	}

	public static double clamp(double value, double min, double max)
	{
		return Math.min(Math.max(value, min), max);
	}
	
	

	public static Color AverageColor(Color[] colors)
	{
		double red = 0, green = 0, blue = 0;
		for (int i = 0; i <= colors.length - 1; i += 1)
		{
			red += colors[i].getRed();
			green += colors[i].getGreen();
			blue += colors[i].getBlue();
		}
		red = red / (double)colors.length;
		green = green / (double)colors.length;
		blue = blue / (double)colors.length;
		return new Color((int)red, (int)green, (int)blue);
	}

	public static String[] FitText(String inputstring, int NumberOfChars)
	{
		String[] newstring = new String[inputstring.length()];
		int CharsExeeding = 0;		
		int i = 0;
		int FirstChar = 0;
		int LastChar = 0;
		do
		{
			FirstChar = i*NumberOfChars - CharsExeeding;
			LastChar = FirstChar + Math.min(NumberOfChars, Math.min((i + 1)*NumberOfChars, inputstring.length() - i*NumberOfChars) + CharsExeeding);
			char[] chararray = new char[NumberOfChars];
			inputstring.getChars(FirstChar, LastChar, chararray, 0);
			if (chararray[LastChar - FirstChar - 1] != ' ' & chararray[LastChar - FirstChar - 1] != '.' & chararray[LastChar - FirstChar - 1] != '?' & chararray[LastChar - FirstChar - 1] != '!' & chararray[LastChar - FirstChar - 1] != '/' & chararray[LastChar - FirstChar - 1] != ':')
			{
				for (int j = chararray.length - 1; 0 <= j; j += -1)
				{
					CharsExeeding += 1;
					LastChar += -1;
					if (chararray[j] == ' ' | chararray[j] == '.' | chararray[j] == '?' | chararray[j] == '!' | chararray[j] == '/' | chararray[j] == ':')
					{
						char[] chararray2 = new char[NumberOfChars];
						inputstring.getChars(Math.min(Math.max(0, FirstChar), inputstring.length()), LastChar, chararray2, 0);
						newstring[i] = String.valueOf(chararray2);
						CharsExeeding += -1;
						j = 0;
					}
				}
			}
			else
			{
				newstring[i] = String.valueOf(chararray);
			}
			i += 1;
		} while(LastChar != inputstring.length() & i != inputstring.length());		
		String[] newstring2 = new String[i];
		for (int j = 0; j <= newstring2.length - 1; j += 1)
		{
			newstring2[j] = newstring[j];
		}
		return newstring2;
	}

	public static double[][] PolygonLines(double[][] Coords)
    {
        double[][] Lines = new double[Coords.length][6];
        for (int i = 0; i <= Coords.length - 2; i += 1)
		{
		    Lines[i][0] = Coords[i][0];
		    Lines[i][1] = Coords[i][1];
		    Lines[i][2] = 0;
		    Lines[i][3] = Coords[i + 1][0];
		    Lines[i][4] = Coords[i + 1][1];
		    Lines[i][5] = 0;
		}
	    Lines[Coords.length - 1][0] = Coords[Coords.length - 1][0];
	    Lines[Coords.length - 1][1] = Coords[Coords.length - 1][1];
	    Lines[Coords.length - 1][2] = 0;
	    Lines[Coords.length - 1][3] = Coords[0][0];
	    Lines[Coords.length - 1][4] = Coords[0][1];
	    Lines[Coords.length - 1][5] = 0;
	    return Lines;
    }
	
	public static double[] CreatePointInLine(double[] Line, double pos)
	{
	    double[] point = new double[3];
	    point[0] = Line[0] + (Line[3] - Line[0])*pos;
	    point[1] = Line[1] + (Line[4] - Line[1])*pos;
	    point[2] = Line[2] + (Line[5] - Line[2])*pos;
	    return point;
	}
	
	public static double[][] CreateIntermediatePoints(double[][] Lines, int npoints, boolean IncludeExtremes)
	{
	    double[][] Points = new double[Lines.length*npoints][3];
	    if (IncludeExtremes)
	    {
	        for (int i = 0; i <= Lines.length - 1; i += 1)
    		{
    		    for (int n = 0; n <= npoints - 1; n += 1)
        		{
        		    double pos = n / (double) npoints;
        		    Points[i*npoints + n] = CreatePointInLine(Lines[i], pos);
        		}
    		}
	    }
	    else
	    {
    	    for (int i = 0; i <= Lines.length - 1; i += 1)
    		{
    		    for (int n = 0; n <= npoints - 1; n += 1)
        		{
        		    double pos = (n + 1) / (double) (npoints + 1);
        		    Points[i*npoints + n] = CreatePointInLine(Lines[i], pos);
        		}
    		}
	    }
		return Points;
	}
	
	public static double[][] CreateInternalPolygonPoints(List<Point3D> Coords, Point3D PolygonCenter, double offset)
	{
	    double[][] Points = new double[Coords.size()][3];
	    for (int i = 0; i <= Coords.size() - 1; i += 1)
		{
		    double[] Line = new double[] {PolygonCenter.x, PolygonCenter.y, 0, Coords.get(i).x, Coords.get(i).y, 0};
		    Points[i] = CreatePointInLine(Line, 1 - offset);
		}
		return Points;
	}
	
	public static int[][] Rotation2D(int[] PointOfRotation, int[][] OriginalCoords, double theta)
	{
		int[][] TranslatedCoords = new int[OriginalCoords.length][OriginalCoords[0].length];
		for (int i = 0; i <= OriginalCoords.length - 1; i += 1)
		{
			for (int j = 0; j <= OriginalCoords[i].length - 1; j += 1)
			{
				TranslatedCoords[i][j] = OriginalCoords[i][j] - PointOfRotation[j];
			}
		}
		double[][] T = new double[][] {{Math.cos(theta), -Math.sin(theta)}, {Math.sin(theta), Math.cos(theta)}};
		int[][] NewCoords = MultMatrix(TranslatedCoords, T);
		for (int i = 0; i <= NewCoords.length - 1; i += 1)
		{
			for (int j = 0; j <= NewCoords[i].length - 1; j += 1)
			{
				NewCoords[i][j] += PointOfRotation[j];
			}
		}
		return NewCoords;
	}

		
	public static boolean MouseIsInside(Point MousePos, int[] PanelPos, int[] RectPos, int L, int H)
	{
		int[] rectPos = new int[] {RectPos[0], RectPos[1]};
		if (rectPos[0] <= MousePos.x & rectPos[1] <= MousePos.y & MousePos.x <= rectPos[0] + L & MousePos.y <= rectPos[1] + H)
		{
			return true;
		} 
		else
		{
			return false;
		}
	}
	
	public static boolean MouseIsInside(Point MousePos, int[] PanelPos, Point RectPos, int L, int H)
	{
		return MouseIsInside(MousePos, PanelPos, new int[] {RectPos.x, RectPos.y}, L, H) ;
	}

	public static boolean MouseIsOnElem(List<Node> nodes, Element elems, double[] MousePosRealCoords, int[] CanvasPos, int[] CanvasSize, int[] DrawingPos, boolean condition)
	{
		ElemShape elemShape = elems.getShape();
		if (elemShape.equals(ElemShape.rectangular))
		{
			double x0 = GetNodePos(nodes.get(elems.getExternalNodes()[0]), condition)[0];
			double y0 = GetNodePos(nodes.get(elems.getExternalNodes()[0]), condition)[1];
			double x1 = GetNodePos(nodes.get(elems.getExternalNodes()[2]), condition)[0];
			double y1 = GetNodePos(nodes.get(elems.getExternalNodes()[2]), condition)[1];
			double[] InitPoint = new double[] {CanvasPos[0] - DrawingPos[0] + x0, CanvasPos[1] + CanvasSize[1] - DrawingPos[1] + y0}; 
			double[] FinalPoint = new double[] {CanvasPos[0] - DrawingPos[0] + x1, CanvasPos[1] + CanvasSize[1] - DrawingPos[1] + y1};
			if (InitPoint[0] <= MousePosRealCoords[0] & MousePosRealCoords[0] <= FinalPoint[0] & FinalPoint[1] <= MousePosRealCoords[1] & MousePosRealCoords[1] <= InitPoint[1])
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (elemShape.equals(ElemShape.triangular))
		{
		    double[] v1 = GetNodePos(nodes.get(elems.getExternalNodes()[0]), condition);
		    double[] v2 = GetNodePos(nodes.get(elems.getExternalNodes()[1]), condition);
		    double[] v3 = GetNodePos(nodes.get(elems.getExternalNodes()[2]), condition);
		    float d1 = (float) ((MousePosRealCoords[0] - v2[0]) * (v1[1] - v2[1]) - (v1[0] - v2[0]) * (MousePosRealCoords[1] - v2[1]));
		    float d2 = (float) ((MousePosRealCoords[0] - v3[0]) * (v2[1] - v3[1]) - (v2[0] - v3[0]) * (MousePosRealCoords[1] - v3[1]));
		    float d3 = (float) ((MousePosRealCoords[0] - v1[0]) * (v3[1] - v1[1]) - (v3[0] - v1[0]) * (MousePosRealCoords[1] - v1[1]));
		    boolean has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
		    boolean  has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);
		    
			if (!(has_neg && has_pos))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (elemShape.equals(ElemShape.r8))
		{
			double x0 = GetNodePos(nodes.get(elems.getExternalNodes()[0]), condition)[0];
			double y0 = GetNodePos(nodes.get(elems.getExternalNodes()[0]), condition)[1];
			double x1 = GetNodePos(nodes.get(elems.getExternalNodes()[4]), condition)[0];
			double y1 = GetNodePos(nodes.get(elems.getExternalNodes()[4]), condition)[1];
			double[] InitPoint = new double[] {CanvasPos[0] - DrawingPos[0] + x0, CanvasPos[1] + CanvasSize[1] - DrawingPos[1] + y0}; 
			double[] FinalPoint = new double[] {CanvasPos[0] - DrawingPos[0] + x1, CanvasPos[1] + CanvasSize[1] - DrawingPos[1] + y1};
			if (InitPoint[0] <= MousePosRealCoords[0] & MousePosRealCoords[0] <= FinalPoint[0] & FinalPoint[1] <= MousePosRealCoords[1] & MousePosRealCoords[1] <= InitPoint[1])
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			System.out.println("Element shape not identified at Util -> MouseIsOnElem");
		}
		return false;
	}
	
	public static boolean MouseIsOnNode(List<Node> nodes, Point MousePos, MyCanvas canvas, int node, double prec, boolean condition)
	{
		double[] nodePos = GetNodePos(nodes.get(node), condition) ;
		Point NodePos = canvas.inDrawingCoords(new Point2D.Double(nodePos[0], nodePos[1])) ; // ConvertToDrawingCoords(GetNodePos(nodes.get(node), condition), CanvasPos, CanvasSize, CanvasDim, DrawingPos);

		double Dist = Math.sqrt(Math.pow(MousePos.x - NodePos.x, 2) + Math.pow(MousePos.y - NodePos.y, 2));
		if (Dist <= prec)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean MouseIsInsideCanvas(Point MousePos, MyCanvas canvas)
	{
		int[] rectPos = new int[] {canvas.getPos().x, canvas.getPos().y};
		if (rectPos[0] <= MousePos.x & rectPos[1] <= MousePos.y & MousePos.x <= rectPos[0] + canvas.getDimension()[0] & MousePos.y <= rectPos[1] + canvas.getDimension()[1])
		{
			return true;
		} 
		else
		{
			return false;
		}
	}
	
	public static Color FindColor(double value, double min, double max, String Style)
	{
		int red = -1, green = -1, blue = -1;
		double MaxAbs = Math.max(Math.abs(min), max);
		if (Style.equals("Red to green"))
		{
			red = (int) Math.max(255*(-value/MaxAbs), 0);
			green = (int) Math.max(255*(value/MaxAbs), 0);
			blue = (int) Math.max(255*(1 - Math.abs(value)/MaxAbs), 0);
		}
		else if (Style.equals("Purple to green"))
		{
			
		}
		else
		{
			System.out.println("Color system not found at Util -> FindColor");
		}
		return new Color(red, green, blue);
	}

	public static int[] NodesInsideWindow(List<Node> Node, double[] RealStructCenter, int[] WindowTopLeft, Point WindowBotRight, MyCanvas canvas, int[] DOFsOnNode, double Defscale, boolean condition)
	{
		int[] NodesInsideWindow = null;
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			//double[] RealNodePos = GetNodePos(Node.get(node), condition);
			double[] RealNodePos = Util.ScaledDefCoords(Node.get(node).getOriginalCoords().asArray(), Node.get(node).getDisp(), DOFsOnNode, Defscale);
			//int[] DrawingCoords = ConvertToDrawingCoords(RealNodePos, CanvasPos, CanvasSize, CanvasDim, DrawingPos);
			Point DrawingCoords = canvas.inDrawingCoords(new Point2D.Double(RealNodePos[0], RealNodePos[1])); ;
			// Util.ConvertToDrawingCoords2(RealNodePos, RealStructCenter, new int[] {canvas.getPos().x, canvas.getPos().y}, canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			if (WindowTopLeft[0] <= DrawingCoords.x & DrawingCoords.x <= WindowBotRight.x & WindowTopLeft[1] <= DrawingCoords.y & DrawingCoords.y <= WindowBotRight.y)
			{
				NodesInsideWindow = AddElem(NodesInsideWindow, node);
			}
		}
		if (NodesInsideWindow != null)
		{
			return Arrays.copyOfRange(NodesInsideWindow, 0, NodesInsideWindow.length);
		}
		else
		{
			return null;
		}
	}

	// public static int[] NodesInsideWindow(List<Node> Node, double[] RealStructCenter, int[] WindowTopLeft, int[] WindowBotRight, MyCanvas canvas, int[] DOFsOnNode, double Defscale, boolean condition)
	// {
	// 	return NodesInsideWindow(Node, RealStructCenter, WindowTopLeft, WindowBotRight, canvas, DOFsOnNode, Defscale, condition) ;
	// }

	// public static int[] NodesInsideWindow(List<Node> Node, double[] RealStructCenter, int[] WindowTopLeft, Point WindowBotRight, Point CanvasPos, int[] CanvasCenter, int[] CanvasSize, double[] CanvasDim, int[] DrawingPos, int[] DOFsOnNode, double Defscale, boolean condition)
	// {
	// 	return NodesInsideWindow(Node, RealStructCenter, WindowTopLeft, new int[] {WindowBotRight.x, WindowBotRight.y}, new int[] {CanvasPos.x, CanvasPos.y}, CanvasCenter, CanvasSize,
	// 			CanvasDim, DrawingPos, DOFsOnNode, Defscale, condition) ;
	// }
	
	public static int[] ElemsInsideWindow(Mesh mesh, double[] RealStructCenter, int[] WindowTopLeft, Point WindowBotRight, int[] PanelPos,
						MyCanvas canvas, double Defscale, boolean condition)
	{
		List<Node> Node = mesh.getNodes();
		List<Element> Elem = mesh.getElements();
		int[] ElemsInsideWindow = null;
		int[] ElemDOFs = Elem.get(0).getDOFs();
		int[] NodesInWindow = NodesInsideWindow(Node, RealStructCenter, WindowTopLeft, WindowBotRight, canvas, ElemDOFs, Defscale, condition);
		boolean ElemIsInWindow;
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
		{
			ElemIsInWindow = true;
			for (int node = 0; node <= Elem.get(elem).getExternalNodes().length - 1; node += 1)
			{
				if (NodesInWindow != null)
				{
					if (!ArrayContains(NodesInWindow, Elem.get(elem).getExternalNodes()[node]))
					{
						ElemIsInWindow = false;
					}
				}
				else
				{
					ElemIsInWindow = false;
				}
			}
			if (ElemIsInWindow)
			{
				ElemsInsideWindow = AddElem(ElemsInsideWindow, elem);
			}
		}
		if (ElemsInsideWindow != null)
		{
			return Arrays.copyOfRange(ElemsInsideWindow, 0, ElemsInsideWindow.length);
		}
		else
		{
			return null;
		}
	}

	public static boolean AllElemsHaveMat(List<Element> Elem)
	{
		if (Elem != null)
		{
			boolean allElemsHaveMat = true;
			for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
			{
				if (Elem.get(elem).getMat() == null)
				{
					allElemsHaveMat = false;
				}
			}
			return allElemsHaveMat;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean AllElemsHaveSec(List<Element> Elem)
	{
		if (Elem != null)
		{
			boolean allElemsHaveSec = true;
			for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
			{
				if (Elem.get(elem).getSec() == null)
				{
					allElemsHaveSec = false;
				}
			}
			return allElemsHaveSec;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean AllElemsHaveElemType(List<Element> Elem)
	{
		boolean allElemsHaveMat = true;
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
		{
			if (Elem.get(elem).getType() == null)
			{
				allElemsHaveMat = false;
			}
		}
		return allElemsHaveMat;
	}
	
	public static int[] DefineDOFsOnNode(List<Element> Elem)
	{
		int[] DOFsOnNode = null;  
		int[][] DOFsPerNode = (int[][]) Elem.get(0).getDOFsPerNode() ;
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
        {
        	for (int elemnode = 0; elemnode <= Elem.get(elem).getExternalNodes().length - 1; elemnode += 1)
        	{
        		DOFsOnNode = DOFsPerNode[elemnode];
        	}
        }
        
        return DOFsOnNode;
	}
	
	public static int[] CumDOFsOnElem(List<Node> Node, int NumberOfNodes)
	{
		int[] CumDOFsOnElem = new int[NumberOfNodes];
        for (int elemnode = 1; elemnode <= NumberOfNodes - 1; elemnode += 1)
        {
        	CumDOFsOnElem[elemnode] = CumDOFsOnElem[elemnode - 1] + Node.get(elemnode - 1).getDOFType().length;
        }
        
        return CumDOFsOnElem;
	}
	
	public static InputDTO LoadEspecialInput(String fileName)
	{
		String[][] Input = ReadInput.ReadTxtFile(fileName);

		List<Point3D> especialCoords = new ArrayList<>();
		for (int coord = 0; coord <= Input[0].length - 3; coord += 1)
		{
			String[] Line = Input[0][coord + 1].split(",");
			especialCoords.add(new Point3D(Double.parseDouble(Line[0]), Double.parseDouble(Line[1]), Double.parseDouble(Line[2]))) ;
		}

		String MeshTypeInput = Input[1][1];
		String[] EspecialElemTypes = Arrays.copyOfRange(Input[2], 1, Input[2].length - 1);		
		int[][] EspecialMeshSizes = new int[Input[3].length - 2][2];
		for (int mesh = 0; mesh <= Input[3].length - 3; mesh += 1)
		{
			String[] Line = Input[3][mesh + 1].split(",");
			EspecialMeshSizes[mesh][0] = Integer.parseInt(Line[0]);
			EspecialMeshSizes[mesh][1] = Integer.parseInt(Line[1]);
		}
		MeshType meshType = MeshType.valueOf(MeshTypeInput.toLowerCase()) ;

		double[][] EspecialMat = new double[Input[4].length - 2][3];
		for (int mat = 0; mat <= Input[4].length - 3; mat += 1)
		{
			String[] Line = Input[4][mat + 1].split(",");
			EspecialMat[mat][0] = Double.parseDouble(Line[0]);
			EspecialMat[mat][1] = Double.parseDouble(Line[1]);
			EspecialMat[mat][2] = Double.parseDouble(Line[2]);
		}
		double[][] EspecialSec = new double[Input[5].length - 2][1];
		for (int sec = 0; sec <= Input[5].length - 3; sec += 1)
		{
			String[] Line = Input[5][sec + 1].split(",");
			EspecialSec[sec][0] = Double.parseDouble(Line[0]);
		}
		double[][] EspecialConcLoads = new double[Input[6].length - 2][6];
		for (int concload = 0; concload <= Input[6].length - 3; concload += 1)
		{
			String[] Line = Input[6][concload + 1].split(",");
			EspecialConcLoads[concload][0] = Double.parseDouble(Line[0]);
			EspecialConcLoads[concload][1] = Double.parseDouble(Line[1]);
			EspecialConcLoads[concload][2] = Double.parseDouble(Line[2]);
			EspecialConcLoads[concload][3] = Double.parseDouble(Line[3]);
			EspecialConcLoads[concload][4] = Double.parseDouble(Line[4]);
			EspecialConcLoads[concload][5] = Double.parseDouble(Line[5]);
		}
		double[][] EspecialDistLoads = new double[Input[7].length - 2][5];
		for (int distload = 0; distload <= Input[7].length - 3; distload += 1)
		{
			String[] Line = Input[7][distload + 1].split(",");
			EspecialDistLoads[distload][0] = Double.parseDouble(Line[0]);
			EspecialDistLoads[distload][1] = Double.parseDouble(Line[1]);
			EspecialDistLoads[distload][2] = Double.parseDouble(Line[2]);
			EspecialDistLoads[distload][3] = Double.parseDouble(Line[3]);
			EspecialDistLoads[distload][4] = Double.parseDouble(Line[4]);
		}
		int[] EspecialSupConfig = new int[Input[8].length - 2];
		for (int supconfig = 0; supconfig <= Input[8].length - 3; supconfig += 1)
		{
			String[] Line = Input[8][supconfig + 1].split(",");
			EspecialSupConfig[supconfig] = Integer.parseInt(Line[0]);
		}
		
		return new InputDTO(especialCoords, meshType, EspecialElemTypes, EspecialMeshSizes, EspecialMat, EspecialSec,
							EspecialConcLoads, EspecialDistLoads, EspecialSupConfig) ;
	}
	
	public static Supports[] AddSupports(List<Node> Node, Supports[] sup, int[] SelectedNodes, int[] SupType)
	{
		sup = Util.IncreaseArraySize(sup, SelectedNodes.length);
		for (int i = 0; i <= SelectedNodes.length - 1; i += 1)
		{
			int supid = sup.length - SelectedNodes.length + i;
			if (-1 < SelectedNodes[i])
			{
				sup[supid] = new Supports(supid, SelectedNodes[i], SupType);
				Node.get(SelectedNodes[i]).setSup(SupType);
			}
		}
		
		return sup;
	}
	
	public static Supports[] AddEspecialSupports(List<Node> Node, ElemShape elemShape, MeshType meshType, int[] NElem, int SupConfig)
	{
		int SupType = -1;
		Supports[] sup = null;
		int[] LeftNodes = null, RightNodes = null, TopNodes = null, BottomNodes = null;
		int[][] SupTypes = Supports.Types;
		if (meshType.equals(MeshType.cartesian))
		{
			if (elemShape.equals(ElemShape.rectangular))
			{
				LeftNodes = new int[NElem[1] + 1];
				for (int node = 0; node <= NElem[1]; node += 1)
				{
					int nodeID = node*(NElem[0] + 1);
					LeftNodes[node] = nodeID;
				}
				RightNodes = new int[NElem[1] + 1];
				for (int node = 0; node <= NElem[1]; node += 1)
				{
					int nodeID = (node + 1)*(NElem[0] + 1) - 1;
					RightNodes[node] = nodeID;
				}
				BottomNodes = new int[NElem[0] + 1];
				for (int node = 0; node <= NElem[0]; node += 1)
				{
					int nodeID = node;
					BottomNodes[node] = nodeID;
				}
				TopNodes = new int[NElem[0] + 1];
				for (int node = 0; node <= NElem[0]; node += 1)
				{
					int nodeID = NElem[1]*(NElem[0] + 1) + node;
					TopNodes[node] = nodeID;
				}
			}
			else if (elemShape.equals(ElemShape.r8))
			{
				int Ne = 2 * NElem[0] + 1, No = NElem[0] + 1; // Number of nodes on the even and odd rows
				LeftNodes = new int[2 * NElem[1] + 1];
				for (int node = 0; node <= LeftNodes.length - 1; node += 1)
				{
					int nodeID = -1;
					if (node % 2 == 0)
					{
						nodeID = node / 2 * (Ne + No);
					}
					else
					{
						nodeID = node / 2 * (Ne + No) + Ne;
					}
					LeftNodes[node] = nodeID;
				}
				RightNodes = new int[2 * NElem[1] + 1];
				for (int node = 0; node <= RightNodes.length - 1; node += 1)
				{
					int nodeID = -1;
					if (node % 2 == 0)
					{
						nodeID = node / 2 * (Ne + No) + Ne - 1;
					}
					else
					{
						nodeID = node / 2 * (Ne + No) + Ne + No - 1;
					}
					RightNodes[node] = nodeID;
				}
				BottomNodes = new int[2 * NElem[0] + 1];
				for (int node = 0; node <= BottomNodes.length - 1; node += 1)
				{
					int nodeID = node;
					BottomNodes[node] = nodeID;
				}
				TopNodes = new int[2 * NElem[0] + 1];
				for (int node = 0; node <= TopNodes.length - 1; node += 1)
				{
					int nodeID = node + NElem[1]*(No + Ne);
					TopNodes[node] = nodeID;
				}
			}
			
			if (SupConfig == 1)
			{
				SupType = 5;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, RightNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(BottomNodes, 1, BottomNodes.length - 1), SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(TopNodes, 1, TopNodes.length - 1), SupTypes[SupType]);
			}
			else if (SupConfig == 2)
			{
				SupType = 5;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, RightNodes, SupTypes[SupType]);
			}
			else if (SupConfig == 3)
			{
				SupType = 5;
				sup = AddSupports(Node, sup, BottomNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, TopNodes, SupTypes[SupType]);
			}
			else if (SupConfig == 4)
			{
				SupType = 5;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(TopNodes, 1, TopNodes.length - 1), SupTypes[SupType]);
			}
			else if (SupConfig == 5)
			{
				SupType = 5;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
			}
			else if (SupConfig == 6)
			{
				SupType = 5;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, RightNodes, SupTypes[SupType]);
				SupType = 2;
				sup = AddSupports(Node, sup, Arrays.copyOfRange(BottomNodes, 1, BottomNodes.length - 1), SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(TopNodes, 1, TopNodes.length - 1), SupTypes[SupType]);
			}
			else if (SupConfig == 7)
			{
				SupType = 2;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, RightNodes, SupTypes[SupType]);
				SupType = 5;
				sup = AddSupports(Node, sup, Arrays.copyOfRange(BottomNodes, 1, BottomNodes.length - 1), SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(TopNodes, 1, TopNodes.length - 1), SupTypes[SupType]);
			}
			else if (SupConfig == 8)
			{
				SupType = 5;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(TopNodes, 1, TopNodes.length - 1), SupTypes[SupType]);
				SupType = 2;
				sup = AddSupports(Node, sup, RightNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(BottomNodes, 1, BottomNodes.length - 1), SupTypes[SupType]);
			}
			else if (SupConfig == 9)
			{
				SupType = 2;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, RightNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(TopNodes, 1, TopNodes.length - 1), SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(BottomNodes, 1, BottomNodes.length - 1), SupTypes[SupType]);
			}
			else if (SupConfig == 10)
			{
				SupType = 2;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, RightNodes, SupTypes[SupType]);
			}
			else if (SupConfig == 11)
			{
				SupType = 2;
				sup = AddSupports(Node, sup, TopNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, BottomNodes, SupTypes[SupType]);
			}
			else if (SupConfig == 12)
			{
				SupType = 2;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(BottomNodes, 1, BottomNodes.length), SupTypes[SupType]);
			}
			else if (SupConfig == 13)
			{
				SupType = 5;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
				SupType = 2;
				sup = AddSupports(Node, sup, RightNodes, SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(TopNodes, 1, TopNodes.length - 1), SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(BottomNodes, 1, BottomNodes.length - 1), SupTypes[SupType]);
			}
			else if (SupConfig == 14)
			{
				SupType = 5;
				sup = AddSupports(Node, sup, LeftNodes, SupTypes[SupType]);
				SupType = 2;
				sup = AddSupports(Node, sup, Arrays.copyOfRange(TopNodes, 1, TopNodes.length - 1), SupTypes[SupType]);
				sup = AddSupports(Node, sup, Arrays.copyOfRange(BottomNodes, 1, BottomNodes.length - 1), SupTypes[SupType]);
			}
		}
		else if (meshType.equals(MeshType.radial))
		{
			
		}
		
		return sup;
	}
	

	public static double FindMaxConcLoad(List<ConcLoads> concLoads)
	{
		if (concLoads != null && !concLoads.isEmpty())
		{
			double MaxLoad = concLoads.get(0).getLoads()[0];
			for (int l = 0; l <= concLoads.size() - 1; l += 1)
			{
				for (int i = 0; i <= concLoads.get(l).getLoads().length - 1; i += 1)
				{
					if (MaxLoad < Math.abs(concLoads.get(l).getLoads()[i]))
					{
						MaxLoad = Math.abs(concLoads.get(l).getLoads()[i]);
					}
				}
			}
			return MaxLoad;
		}
		else
		{
			return 0;
		}
	}

	public static double FindMaxDistLoad(List<DistLoads> DistLoads)
	{
		if (DistLoads != null && !DistLoads.isEmpty())
		{
			double MaxLoad = DistLoads.get(0).getIntensity();
			for (int l = 0; l <= DistLoads.size() - 1; l += 1)
			{
				if (MaxLoad < Math.abs(DistLoads.get(l).getIntensity()))
				{
					MaxLoad = Math.abs(DistLoads.get(l).getIntensity());
				}
			}
			return MaxLoad;
		}
		else
		{
			return 0;
		}
	}

	public static double FindMaxReaction(Reactions[] Reactions)
	{
		if (Reactions != null)
		{
			double MaxLoad = Reactions[0].getLoads()[0];
			for (int l = 0; l <= Reactions.length - 1; l += 1)
			{
				for (int i = 0; i <= Reactions[l].getLoads().length - 1; i += 1)
				{
					if (MaxLoad < Math.abs(Reactions[l].getLoads()[i]))
					{
						MaxLoad = Math.abs(Reactions[l].getLoads()[i]);
					}
				}
			}
			return MaxLoad;
		}
		else
		{
			return 0;
		}
	}

	public static double[] GetNodePos(Node node, boolean condition)
	{
		if (condition)
		{
			return node.deformedPos();
		}
		else
		{
			return node.getOriginalCoords().asArray();
		}
	}
	
	public static double[][] GetElemNodesDefPos(List<Node> nodes, int[] ElemNodes)
	{
		double[][] DefCoords = new double[ElemNodes.length][];
		for (int node = 0; node <= ElemNodes.length - 1; node += 1)
		{
			DefCoords[node] = new double[3];
			DefCoords[node][0] = nodes.get(ElemNodes[node]).getOriginalCoords().x + nodes.get(ElemNodes[node]).getDisp()[0];
			DefCoords[node][1] = nodes.get(ElemNodes[node]).getOriginalCoords().y + nodes.get(ElemNodes[node]).getDisp()[1];
			DefCoords[node][2] = nodes.get(ElemNodes[node]).getOriginalCoords().z + nodes.get(ElemNodes[node]).getDisp()[2];
		}
		return DefCoords;
	}

	public static Color AddColor(Color OriginalColor, double[] ColorAddition)
	{
		int NewRed = (int)(Math.max(0, Math.min(255, OriginalColor.getRed() + ColorAddition[0])));
		int NewGreen = (int)(Math.max(0, Math.min(255, OriginalColor.getGreen() + ColorAddition[1])));
		int NewBlue = (int)(Math.max(0, Math.min(255, OriginalColor.getBlue() + ColorAddition[2])));
		return new Color(NewRed, NewGreen, NewBlue);
	}

}