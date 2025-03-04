package Utilidades;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import Main.Point3D;
import Main.ReadInput;
import structure.ConcLoads;
import structure.DistLoads;
import structure.ElemShape;
import structure.Element;
import structure.MeshType;
import structure.MyCanvas;
import structure.NodalDisps;
import structure.Nodes;
import structure.Reactions;
import structure.Supports;

public abstract class Util 
{
	public static Color[] ColorPalette()
	{
		/*
		 * Neutral: 0, 1, 2, 3
		 * Primary: 4, 5, 6, 7, 8, 9, 10
		 * Contrast: 5 w 9, 4 w 9, 5 w 8, 6 w 9, 6 w 7, 6 w 8
		 * 
		 * Main background: 3
		 * Menu background: 6
		 * 
		 * Unselected: 0
		 * Selection: 10
		 * Loads: 7
		 * Supports: 7
		 * Displacements: 4
		 * Reactions: 8
		 * */
		Color[] color = new Color[28];
		/*color[0] = new Color(0, 0, 0);			// black
		color[1] = new Color(180, 180, 180);	// gray
		color[2] = new Color(255, 255, 255);	// white
		color[3] = new Color(220, 192, 192);	// red white
		color[4] = new Color(32, 95, 102);	// blue green
		color[5] = new Color(90, 93, 136);	// dark blue
		color[6] = new Color(84, 87, 80);	// dark gray green
		color[7] = new Color(182, 89, 81);	// red
		color[8] = new Color(175, 78, 97);	// red pink
		color[9] = new Color(183, 94, 50);	// orange
		color[10] = new Color(134, 149, 68);	// green yellow
		color[11] = new Color(143, 226, 210);	// light blue*/
		
		color[0] = new Color(35, 31, 31);			// black
		color[1] = new Color(204, 204, 204);	// gray
		color[2] = new Color(250, 246, 246);	// white
		color[3] = new Color(225, 211, 211);	// red white
		color[4] = new Color(32, 95, 102);	// blue green
		color[5] = new Color(90, 93, 136);	// dark blue
		color[6] = new Color(84, 87, 80);	// dark gray green
		color[7] = new Color(228, 137, 92);	// red
		color[8] = new Color(143, 226, 210);	// red pink
		color[9] = new Color(206, 235, 160);	// orange
		color[10] = new Color(237, 100, 91);	// green yellow
		color[11] = new Color(237, 91, 176);	// light blue
		return color;
	}
	
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
	
	public static double dist(int[] Point1, int[] Point2)
	{
		return Math.sqrt(Math.pow(Point2[0] - Point1[0], 2) + Math.pow(Point2[1] - Point1[1], 2));
	}

	public static int[] CalculateNumberOfGridPoints(double[] CanvasDim)
	{
		int[] NPointsMin = new int[] {6, 6}, NPointsMax = new int[] {46, 46};
		int[] NPoints = new int[2];
		NPoints[0] = (int) (NPointsMin[0] + (NPointsMax[0] - NPointsMin[0]) * (CanvasDim[0] % 100) / 100.0);
		NPoints[1] = (int) (NPointsMin[1] + (NPointsMax[1] - NPointsMin[1]) * (CanvasDim[1] % 100) / 100.0);	
		return NPoints;
	}
	
	public static double[] ConvertToRealCoords(int[] OriginalCoords, int[] CanvasPos, int[] CanvasSize, double[] CanvasDim)
	{
		return new double[] {(OriginalCoords[0] - CanvasPos[0])/(double)(CanvasSize[0])*CanvasDim[0], (-OriginalCoords[1] + CanvasPos[1] + CanvasSize[1])/(double)(CanvasSize[1])*CanvasDim[1], 0};
	}

	public static double[] ConvertToRealCoords2(int[] OriginalCoords, double[] CoordsCenter, int[] CanvasPos, int[] CanvasSize, double[] CanvasDim, int[] CanvasCenter, int[] DrawingPos)
	{
		return new double[] {(OriginalCoords[0] - DrawingPos[0] - CanvasCenter[0])*CanvasDim[0]/CanvasSize[0] + CoordsCenter[0], -(OriginalCoords[1] - DrawingPos[1] - CanvasCenter[1])*CanvasDim[1]/CanvasSize[1] + CoordsCenter[1]};
	}

	public static double[] ConvertToRealCoordsPoint3D(int[] OriginalCoords, Point3D CoordsCenter, int[] CanvasPos, int[] CanvasSize, double[] CanvasDim, int[] CanvasCenter, int[] DrawingPos)
	{
		return new double[] {(OriginalCoords[0] - DrawingPos[0] - CanvasCenter[0])*CanvasDim[0]/CanvasSize[0] + CoordsCenter.x, -(OriginalCoords[1] - DrawingPos[1] - CanvasCenter[1])*CanvasDim[1]/CanvasSize[1] + CoordsCenter.y};
	}
	
	public static int[] ConvertToDrawingCoords(double[] OriginalCoords, int[] CanvasPos, int[] CanvasSize, double[] CanvasDim, int[] DrawingPos)
	{
		return new int[] {(int) (CanvasPos[0] + DrawingPos[0] + OriginalCoords[0]/CanvasDim[0]*CanvasSize[0]), (int) (CanvasPos[1] + DrawingPos[1] + CanvasSize[1] - OriginalCoords[1]/CanvasDim[1]*CanvasSize[1]), 0};
	}

	public static int[] ConvertToDrawingCoords2(double[] OriginalCoords, double[] CoordsCenter, int[] CanvasPos, int[] CanvasSize, double[] CanvasDim, int[] CanvasCenter, int[] DrawingPos)
	{
		return new int[] {(int) (DrawingPos[0] + CanvasCenter[0] + (OriginalCoords[0] - CoordsCenter[0])/CanvasDim[0]*CanvasSize[0]), (int) (DrawingPos[1] + CanvasCenter[1] - (OriginalCoords[1] - CoordsCenter[1])/CanvasDim[1]*CanvasSize[1])};
	}

	public static int[] ConvertToDrawingCoords2Point3D(double[] OriginalCoords, Point3D CoordsCenter, int[] CanvasPos, int[] CanvasSize, double[] CanvasDim, int[] CanvasCenter, int[] DrawingPos)
	{
		return new int[] {(int) (DrawingPos[0] + CanvasCenter[0] + (OriginalCoords[0] - CoordsCenter.x)/CanvasDim[0]*CanvasSize[0]), (int) (DrawingPos[1] + CanvasCenter[1] - (OriginalCoords[1] - CoordsCenter.y)/CanvasDim[1]*CanvasSize[1])};
	}
	
	public static int ConvertToDrawingSize(int[] CanvasPos, int[] PanelPos, int[] CanvasSize, double[] CanvasDim, int[] DrawingPos, double size)
	{
		return (ConvertToDrawingCoords(new double[] {0, 0}, CanvasPos, CanvasSize, CanvasDim, DrawingPos)[1] - ConvertToDrawingCoords(new double[] {0, size}, CanvasPos, CanvasSize, CanvasDim, DrawingPos)[1]);
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
	
	public static int ElemMouseIsOn(Nodes[] Node, Element[] Elem, int[] MousePos, double[] StructCenter, int[] CanvasPos, int[] CanvasSize, double[] CanvasDim, int[] CanvasCenter, int[] DrawingPos, boolean condition)
	{
		double[] RealMousePos = ConvertToRealCoords2(MousePos, StructCenter, CanvasPos, CanvasSize, CanvasDim, CanvasCenter, CanvasPos);
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			if (MouseIsOnElem(Node, Elem[elem], RealMousePos, CanvasPos, CanvasSize, DrawingPos, condition))
		    {
				return elem;
		    }
		}
		return -1;
	}
	
	public static int NodeMouseIsOn(Nodes[] Node, int[] MousePos, int[] CanvasPos, int[] CanvasSize, double[] CanvasDim, int[] DrawingPos, double prec, boolean condition)
	{
		for (int node = 0; node <= Node.length - 1; node += 1)
		{
			if (MouseIsOnNode(Node, MousePos, CanvasPos, CanvasSize, CanvasDim, DrawingPos, node, prec, condition))
		    {
				return node;
		    }
		}
		return -1;
	}
	
	public static double Bhaskara(double a, double b, double c, String SqrtSign)
	{
		if (SqrtSign.equals("+"))
		{
			return (-b + Math.sqrt(b*b - 4*a*c))/(2*a);
		}
		else
		{
			return (-b - Math.sqrt(b*b - 4*a*c))/(2*a);
		}
	}
	
	public static double TriArea(double[][] Coords)
    {
        double x1 = Coords[0][0], x2 = Coords[1][0], x3 = Coords[2][0];
    	double y1 = Coords[0][1], y2 = Coords[1][1], y3 = Coords[2][1];
    	double A = (x1 * y2 + x2 * y3 + x3 * y1 - y1 * x2 - y2 * x3 - y3 * x1) / 2.0;
    	return A;
    }

	public static double FindMinAbs(double[] SetOfValues)
	{
		double MinValue = SetOfValues[0];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			if (Math.abs(SetOfValues[v]) < MinValue)
			{
				MinValue = Math.abs(SetOfValues[v]);
			}
		}
		return MinValue;
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

	public static double FindMax(double[] SetOfValues)
	{
		double MaxValue = SetOfValues[0];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			if (MaxValue < SetOfValues[v])
			{
				MaxValue = SetOfValues[v];
			}
		}
		return MaxValue;
	}
	
	public static int FindMin(int[][] SetOfValues)
	{
		int MinValue = SetOfValues[0][0];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			for (int v2 = 0; v2 <= SetOfValues[v].length - 1; v2 += 1)
			{
				if (SetOfValues[v][v2] < MinValue)
				{
					MinValue = SetOfValues[v][v2];
				}
			}
		}
		return MinValue;
	}
	
	public static double FindMin(double[][] SetOfValues)
	{
		double MinValue = SetOfValues[0][0];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			for (int v2 = 0; v2 <= SetOfValues[v].length - 1; v2 += 1)
			{
				if (SetOfValues[v][v2] < MinValue)
				{
					MinValue = SetOfValues[v][v2];
				}
			}
		}
		return MinValue;
	}
	
	public static int FindMax(int[][] SetOfValues)
	{
		int MaxValue = SetOfValues[0][0];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			for (int v2 = 0; v2 <= SetOfValues[v].length - 1; v2 += 1)
			{
				if (MaxValue < SetOfValues[v][v2])
				{
					MaxValue = SetOfValues[v][v2];
				}
			}
		}
		return MaxValue;
	}
	
	public static double FindMax(double[][] SetOfValues)
	{
		double MaxValue = SetOfValues[0][0];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			for (int v2 = 0; v2 <= SetOfValues[v].length - 1; v2 += 1)
			{
				if (MaxValue < SetOfValues[v][v2])
				{
					MaxValue = SetOfValues[v][v2];
				}
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
	
	public static double FindMinInPos(double[][] SetOfValues, int pos)
	{
		double MinValue = SetOfValues[0][pos];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			if (SetOfValues[v][pos] < MinValue)
			{
				MinValue = SetOfValues[v][pos];
			}
		}
		return MinValue;
	}

	public static double FindMaxInPos(double[][] SetOfValues, int pos)
	{
		double MaxValue = SetOfValues[0][pos];
		for (int v = 0; v <= SetOfValues.length - 1; v += 1)
		{
			if (MaxValue < SetOfValues[v][pos])
			{
				MaxValue = SetOfValues[v][pos];
			}
		}
		return MaxValue;
	}
	
 	public static String[] AddElem(String[] OriginalArray, String NewElem)
	{
		if (OriginalArray == null)
		{
			return new String[] {NewElem};
		}
		else
		{
			String[] NewArray = new String[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
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
 	
 	public static Nodes[] AddElem(Nodes[] OriginalArray, Nodes NewElem)
 	{
 		if (OriginalArray == null)
		{
			return new Nodes[] {NewElem};
		}
		else
		{
			Nodes[] NewArray = new Nodes[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
 	}

 	public static Element[] AddElem(Element[] OriginalArray, Element NewElem)
 	{
 		if (OriginalArray == null)
		{
			return new Element[] {NewElem};
		}
		else
		{
			Element[] NewArray = new Element[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
 	}
 	
 	public static Supports[] AddElem(Supports[] OriginalArray, Supports NewElem)
	{
		if (OriginalArray == null)
		{
			return new Supports[] {NewElem};
		}
		else
		{
			Supports[] NewArray = new Supports[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
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
 	
 	public static int[][] AddElem(int[][] OriginalArray, int[] NewElem, boolean ElemMustBeUnique)
	{
		if (OriginalArray == null)
		{
			return new int[][] {NewElem};
		}
		else
		{
			if (!ElemMustBeUnique | !ArrayContains(OriginalArray, NewElem))
			{
				int[][] NewArray = new int[OriginalArray.length + 1][];
				for (int i = 0; i <= OriginalArray.length - 1; i += 1)
				{
					NewArray[i] = OriginalArray[i];
				}
				NewArray[OriginalArray.length] = NewElem;
				return NewArray;
			}
			else
			{
				return OriginalArray;
			}
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

	public static DistLoads[] IncreaseArraySize(DistLoads[] OriginalArray, int size)
	{
		if (OriginalArray == null)
		{
			return new DistLoads[size];
		}
		else
		{
			DistLoads[] NewArray = new DistLoads[OriginalArray.length + size];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
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
	
	public static String[][] IncreaseArraySize(String[][] OriginalArray, int size)
	{
		if (OriginalArray == null)
		{
			return new String[size][];
		}
		else
		{
			String[][] NewArray = new String[OriginalArray.length + size][];
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

	public static boolean ArrayContains(int[][] OriginalArray, int[] elem)
	{
		boolean arraycontains = true;
		if (OriginalArray != null & elem != null)
		{
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				if (OriginalArray[i][0] == elem[0] & OriginalArray[i].length == elem.length)
				{
					arraycontains = true;
					for (int j = 1; j <= OriginalArray[i].length - 1; j += 1)
					{
						if (OriginalArray[i][j] != elem[j])
						{
							j = OriginalArray[i].length - 1;
							arraycontains = false;
						}
					}
					if (arraycontains)
					{
						return arraycontains;
					}
				}
			}
			return false;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean ArrayContains(double[][] OriginalArray, double[] elem)
	{
		boolean arraycontains = true;
		if (OriginalArray != null & elem != null)
		{
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				if (OriginalArray[i][0] == elem[0] & OriginalArray[i].length == elem.length)
				{
					arraycontains = true;
					for (int j = 1; j <= OriginalArray[i].length - 1; j += 1)
					{
						if (OriginalArray[i][j] != elem[j])
						{
							j = OriginalArray[i].length - 1;
							arraycontains = false;
						}
					}
					if (arraycontains)
					{
						return arraycontains;
					}
				}
			}
			return false;
		}
		else
		{
			return false;
		}
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
	
	public static double ProdVec(double[] Vector)
	{
		double prod = 1.0;
		for (int i = 0; i <= Vector.length - 1; i += 1)
		{
			prod = prod*Vector[i];
		}
		return prod;
	}

	public static int[] EqualMatrix(int[] A)
	{
		return Arrays.copyOf(A, A.length);
	}
	
	public static double[] EqualMatrix(double[] A)
	{
		return Arrays.copyOf(A, A.length);
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
	
	public static int[] MultMatrixVector (double[][] A, int[] b)
	{
		/*This function multiplies a matrix and a vector*/		
        int aColumns = A[0].length;
        int bRows = b.length;
		int[] C = new int[bRows];	
		if (aColumns != bRows) 
        {
            throw new IllegalArgumentException("Number of columns in A (" + aColumns + ") did not match the number of rows in b (" + bRows + ").");
        }	
        for (int i = 0; i < bRows; i += 1)
        { 
            for (int j = 0; j < aColumns; j += 1)
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
	
	public static double[] RotateCoordPoint3D(double[] OriCoord, Point3D RefPoint, double[] angle)
	{
		return RotateCoord(OriCoord, RefPoint.asArray(), angle) ;
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
    
	public static int TextLength(String Text, Font TextFont, int size, Graphics G)
	{
		FontMetrics metrics = G.getFontMetrics(TextFont);
		return (int)(metrics.stringWidth(Text)*0.05*size);
	}
	
	public static int TextHeight(int TextSize)
	{
		return (int)(0.8*TextSize);
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
	
	public static double Average(double[] values)
	{
		double avr = 0;
		for (int i = 0; i <= values.length - 1; i += 1)
		{
			avr += values[i];
		}
		avr = avr / (double)values.length;
		return avr;
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
	
	public static double[] MatrixAverages(double[][] values)
	{
		double[] avr = new double[values[0].length];
		for (int i = 0; i <= values[0].length - 1; i += 1)
		{
			for (int j = 0; j <= values.length - 1; j += 1)
			{
				avr[i] += values[j][i];
			}
			avr[i] = avr[i] / (double)values.length;
		}
		return avr;
	}
	
	public static Point3D MatrixAveragesToPoint3D(double[][] values)
	{
		double[] avrArray = MatrixAverages(values) ;
		return new Point3D(avrArray[0], avrArray[1], avrArray[2]);
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
	
	public static int[] CenterStructure(int[] DrawingPos, int[] CanvasCenter, int[] DrawingCoords)
	{
		return new int[] {DrawingPos[0] - (DrawingCoords[0] - CanvasCenter[0]), DrawingPos[1] - (DrawingCoords[1] - CanvasCenter[1])};
	}

	public static JButton AddButton(ImageIcon Icon, int[] Alignment, int[] Size, Color color)
	{
		JButton NewButton = new JButton();
		NewButton.setIcon(Icon);
		NewButton.setVerticalAlignment(Alignment[0]);
		NewButton.setHorizontalAlignment(Alignment[1]);
		NewButton.setBackground(color);
		NewButton.setPreferredSize(new Dimension(Size[0], Size[1]));	
		return NewButton;
	}
	
	public static JButton AddButton(String Text, int[] Alignment, int[] Size, int FontSize, int[] margins, Color color)
	{
		JButton NewButton = new JButton(Text);
		NewButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FontSize));
		NewButton.setVerticalAlignment(Alignment[0]);
		NewButton.setHorizontalAlignment(Alignment[1]);
		NewButton.setBackground(color);
		NewButton.setPreferredSize(new Dimension(Size[0], Size[1]));
		NewButton.setMargin(new Insets(margins[0], margins[1], margins[2], margins[3]));
		return NewButton;
	}
	
	public static double[] CentroidOfPoints(double[][] Coords)
	{
	    if (Coords != null)
	    {
    	    if (1 < Coords.length)
    	    {
    	        double[] Centroid = new double[3];
        	    for (int i = 0; i <= Coords.length - 1; i += 1)
        		{
        		    Centroid[0] += Coords[i][0];
        		    Centroid[1] += Coords[i][1];
        		    Centroid[2] += Coords[i][2];
        		}
        		Centroid[0] = Centroid[0] / (double) Coords.length;
        		Centroid[1] = Centroid[1] / (double) Coords.length;
        		Centroid[2] = Centroid[2] / (double) Coords.length;
        		return Centroid;
    	    }
    	    else
    	    {
    	       return Coords[0];
    	    }
	    }
	    else
	    {
	        return null;    
	    }
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
	
	public static double[][] CreateInternalPolygonPoints(double[][] Coords, double[] PolygonCenter, double offset)
	{
	    double[][] Points = new double[Coords.length][3];
	    for (int i = 0; i <= Coords.length - 1; i += 1)
		{
		    double[] Line = new double[] {PolygonCenter[0], PolygonCenter[1], 0, Coords[i][0], Coords[i][1], 0};
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

	public static double[][] DetermineStructureMinMaxCoords(double[][] Coords)
	{
		double[] MinCoords = new double[] {Coords[0][0], Coords[0][1]}, MaxCoords = new double[] {Coords[0][0], Coords[0][1]};	// Create a new array to avoid bugs		
		for (int i = 0; i <= Coords.length - 1; i += 1)
		{
			for (int j = 0; j <= 2 - 1; j += 1)
			{
				if (Coords[i][j] < MinCoords[j])
				{
					MinCoords[j] = Coords[i][j];
				}
				if (MaxCoords[j] < Coords[i][j])
				{
					MaxCoords[j] = Coords[i][j];
				}
			}
		}
		return new double[][] {MinCoords, MaxCoords};
	}
	
	public static double UpdateStructureScale(double[][] Coords, int MaxSize)
	{
		 // Find min and max coordinates and returns the scale. Currently only works for 2D
		double[][] MinMaxCoords = DetermineStructureMinMaxCoords(Coords);
		double[] MinCoords = MinMaxCoords[0], MaxCoords = MinMaxCoords[1];
		if (Math.max(MaxCoords[0] - MinCoords[0], MaxCoords[1] - MinCoords[1]) <= Math.pow(10, -5))
		{
			return 1;
		}
		else
		{
			return MaxSize/Math.max(MaxCoords[0] - MinCoords[0], MaxCoords[1] - MinCoords[1]);
		}
	}
	
	public static int[] GetAbsMousePos()
 	{
		return new int[] {MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y};
 	}
	
	public static int[] GetRelMousePos(int[] PanelPos)
 	{
		return new int[] {MouseInfo.getPointerInfo().getLocation().x - PanelPos[0], MouseInfo.getPointerInfo().getLocation().y - PanelPos[1]};
 	}
		
	public static boolean MouseIsInside(int[] MousePos, int[] PanelPos, int[] RectPos, int L, int H)
	{
		int[] rectPos = new int[] {RectPos[0], RectPos[1]};
		if (rectPos[0] <= MousePos[0] & rectPos[1] <= MousePos[1] & MousePos[0] <= rectPos[0] + L & MousePos[1] <= rectPos[1] + H)
		{
			return true;
		} 
		else
		{
			return false;
		}
	}

	public static boolean MouseIsOnElem(Nodes[] Node, Element Elem, double[] MousePos, int[] CanvasPos, int[] CanvasSize, int[] DrawingPos, boolean condition)
	{
		ElemShape elemShape = Elem.getShape();
		if (elemShape.equals(ElemShape.rectangular))
		{
			double x0 = GetNodePos(Node[Elem.getExternalNodes()[0]], condition)[0];
			double y0 = GetNodePos(Node[Elem.getExternalNodes()[0]], condition)[1];
			double x1 = GetNodePos(Node[Elem.getExternalNodes()[2]], condition)[0];
			double y1 = GetNodePos(Node[Elem.getExternalNodes()[2]], condition)[1];
			double[] InitPoint = new double[] {CanvasPos[0] - DrawingPos[0] + x0, CanvasPos[1] + CanvasSize[1] - DrawingPos[1] + y0}; 
			double[] FinalPoint = new double[] {CanvasPos[0] - DrawingPos[0] + x1, CanvasPos[1] + CanvasSize[1] - DrawingPos[1] + y1};
			if (InitPoint[0] <= MousePos[0] & MousePos[0] <= FinalPoint[0] & FinalPoint[1] <= MousePos[1] & MousePos[1] <= InitPoint[1])
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
		    double[] v1 = GetNodePos(Node[Elem.getExternalNodes()[0]], condition);
		    double[] v2 = GetNodePos(Node[Elem.getExternalNodes()[1]], condition);
		    double[] v3 = GetNodePos(Node[Elem.getExternalNodes()[2]], condition);
		    float d1 = (float) ((MousePos[0] - v2[0]) * (v1[1] - v2[1]) - (v1[0] - v2[0]) * (MousePos[1] - v2[1]));
		    float d2 = (float) ((MousePos[0] - v3[0]) * (v2[1] - v3[1]) - (v2[0] - v3[0]) * (MousePos[1] - v3[1]));
		    float d3 = (float) ((MousePos[0] - v1[0]) * (v3[1] - v1[1]) - (v3[0] - v1[0]) * (MousePos[1] - v1[1]));
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
			double x0 = GetNodePos(Node[Elem.getExternalNodes()[0]], condition)[0];
			double y0 = GetNodePos(Node[Elem.getExternalNodes()[0]], condition)[1];
			double x1 = GetNodePos(Node[Elem.getExternalNodes()[4]], condition)[0];
			double y1 = GetNodePos(Node[Elem.getExternalNodes()[4]], condition)[1];
			double[] InitPoint = new double[] {CanvasPos[0] - DrawingPos[0] + x0, CanvasPos[1] + CanvasSize[1] - DrawingPos[1] + y0}; 
			double[] FinalPoint = new double[] {CanvasPos[0] - DrawingPos[0] + x1, CanvasPos[1] + CanvasSize[1] - DrawingPos[1] + y1};
			if (InitPoint[0] <= MousePos[0] & MousePos[0] <= FinalPoint[0] & FinalPoint[1] <= MousePos[1] & MousePos[1] <= InitPoint[1])
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
	
	public static boolean MouseIsOnNode(Nodes[] Node, int[] MousePos, int[] CanvasPos, int[] CanvasSize, double[] CanvasDim, int[] DrawingPos, int node, double prec, boolean condition)
	{
		int[] NodePos = ConvertToDrawingCoords(GetNodePos(Node[node], condition), CanvasPos, CanvasSize, CanvasDim, DrawingPos);
		double Dist = Math.sqrt(Math.pow(MousePos[0] - NodePos[0], 2) + Math.pow(MousePos[1] - NodePos[1], 2));
		if (Dist <= prec)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean MouseIsInsideCanvas(int[] MousePos, MyCanvas canvas)
	{
		int[] rectPos = new int[] {canvas.getPos()[0], canvas.getPos()[1]};
		if (rectPos[0] <= MousePos[0] & rectPos[1] <= MousePos[1] & MousePos[0] <= rectPos[0] + canvas.getDim()[0] & MousePos[1] <= rectPos[1] + canvas.getDim()[1])
		{
			return true;
		} 
		else
		{
			return false;
		}
	}
	
	public static boolean AreAssigned(int[][] Members, String Prop)
	{
		for (int member = 0; member <= Members.length - 1; member += 1)
		{
			if (Prop.equals("Mat") & Members[member][2] == -1)
			{
				return false;
			}
			if (Prop.equals("Sec") & Members[member][3] == -1)
			{
				return false;
			}
		}
		return true;
	}
	
	public static String[] DefineDiagramsNames(String MemberType, String DiagramType)
	{
		if (MemberType != null)
		{
			if (DiagramType.equals("Force"))
			{
				if (MemberType.equals("T2") | MemberType.equals("T3"))
				{
					return new String[] {"Axial"};
				}
				else if (MemberType.equals("F2"))
				{
					return new String[] {"Axial", "Shear y", "Moment z"};
				}
				else if (MemberType.equals("F3"))
				{
					return new String[] {"Axial", "Shear y", "Shear z", "Torsion", "Moment y", "Moment z"};
				}
			}
			else if (DiagramType.equals("Stress"))
			{
				if (MemberType.equals("T2") | MemberType.equals("T3"))
				{
					return new String[] {"Axial"};
				}
				else if (MemberType.equals("F2"))
				{
					return new String[] {"Axial", "Shear y"};
				}
				else if (MemberType.equals("F3"))
				{
					return new String[] {"Axial", "Shear y", "Shear z"};
				}
			}
		}
		return new String[] {};
	}
	
	public static int FindID (String[] Array, String Elem)
	{
		for (int i = 0; i <= Array.length - 1; i += 1)
		{
			if (Array[i].equals(Elem))
			{
				return i;
			}
		}
		return -1;
	}

	public static int FindID (double[][] Array, double[] Elem)
	{
		for (int i = 0; i <= Array.length - 1; i += 1)
		{
			if (Array[i].equals(Elem))
			{
				return i;
			}
		}
		return -1;
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

	public static int[] NodesInsideWindow(Nodes[] Node, double[] RealStructCenter, int[] WindowTopLeft, int[] WindowBotRight, int[] CanvasPos, int[] CanvasCenter, int[] CanvasSize, double[] CanvasDim, int[] DrawingPos, int[] DOFsOnNode, double Defscale, boolean condition)
	{
		int[] NodesInsideWindow = null;
		for (int node = 0; node <= Node.length - 1; node += 1)
		{
			//double[] RealNodePos = GetNodePos(Node[node], condition);
			double[] RealNodePos = Util.ScaledDefCoords(Node[node].getOriginalCoords(), Node[node].getDisp(), DOFsOnNode, Defscale);
			//int[] DrawingCoords = ConvertToDrawingCoords(RealNodePos, CanvasPos, CanvasSize, CanvasDim, DrawingPos);
			int[] DrawingCoords = Util.ConvertToDrawingCoords2(RealNodePos, RealStructCenter, CanvasPos, CanvasSize, CanvasDim, CanvasCenter, DrawingPos);
			if (WindowTopLeft[0] <= DrawingCoords[0] & DrawingCoords[0] <= WindowBotRight[0] & WindowTopLeft[1] <= DrawingCoords[1] & DrawingCoords[1] <= WindowBotRight[1])
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
	
	public static int[] ElemsInsideWindow(Nodes[] Node, Element[] Elem, double[] RealStructCenter, int[] WindowTopLeft, int[] WindowBotRight, int[] PanelPos, int[] CanvasPos, int[] CanvasCenter, int[] CanvasSize, double[] CanvasDim, int[] DrawingPos, double Defscale, boolean condition)
	{
		int[] ElemsInsideWindow = null;
		int[] ElemDOFs = Elem[0].getDOFs();
		int[] NodesInWindow = NodesInsideWindow(Node, RealStructCenter, WindowTopLeft, WindowBotRight, CanvasPos, CanvasCenter, CanvasSize, CanvasDim, DrawingPos, ElemDOFs, Defscale, condition);
		boolean ElemIsInWindow;
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			ElemIsInWindow = true;
			for (int node = 0; node <= Elem[elem].getExternalNodes().length - 1; node += 1)
			{
				if (NodesInWindow != null)
				{
					if (!ArrayContains(NodesInWindow, Elem[elem].getExternalNodes()[node]))
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
	
	public static boolean AllElemsHaveMat(Element[] Elem)
	{
		if (Elem != null)
		{
			boolean allElemsHaveMat = true;
			for (int elem = 0; elem <= Elem.length - 1; elem += 1)
			{
				if (Elem[elem].getMat() == null)
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
	
	public static boolean AllElemsHaveSec(Element[] Elem)
	{
		if (Elem != null)
		{
			boolean allElemsHaveSec = true;
			for (int elem = 0; elem <= Elem.length - 1; elem += 1)
			{
				if (Elem[elem].getSec() == null)
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
	
	public static boolean AllElemsHaveElemType(Element[] Elem)
	{
		boolean allElemsHaveMat = true;
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			if (Elem[elem].getType() == null)
			{
				allElemsHaveMat = false;
			}
		}
		return allElemsHaveMat;
	}
	
	public static int[] DefineDOFsOnNode(Element[] Elem)
	{
		int[] DOFsOnNode = null;  
		int[][] DOFsPerNode = (int[][]) Elem[0].GetProp()[3];
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
        {
        	for (int elemnode = 0; elemnode <= Elem[elem].getExternalNodes().length - 1; elemnode += 1)
        	{
        		DOFsOnNode = DOFsPerNode[elemnode];
        	}
        }
        
        return DOFsOnNode;
	}
	
	public static int[] CumDOFsOnElem(Nodes[] Node, int NumberOfNodes)
	{
		int[] CumDOFsOnElem = new int[NumberOfNodes];
        for (int elemnode = 1; elemnode <= NumberOfNodes - 1; elemnode += 1)
        {
        	CumDOFsOnElem[elemnode] = CumDOFsOnElem[elemnode - 1] + Node[elemnode - 1].getDOFType().length;
        }
        
        return CumDOFsOnElem;
	}
	
	/*public static Sup[] AddSupports(Nodes[] Node, Sup[] sup, int[] SelectedNodes, int[][] SupTypes, int SelectedSupType)
	{
		sup = Util.IncreaseArraySize(sup, SelectedNodes.length);
		if (-1 < SelectedSupType & SelectedNodes != null)
		{
			for (int i = 0; i <= SelectedNodes.length - 1; i += 1)
			{
				if (-1 < SelectedNodes[i])
				{
					sup[sup.length - SelectedNodes.length + i] = new Sup(sup.length - SelectedNodes.length + i, SelectedNodes[i], SupTypes[SelectedSupType]);
					Node[SelectedNodes[i]].setSup(SelectedSupType);
				}
			}
		}
		
		return sup;
	}*/
	
	/*public Supports[] DefineSupports(Nodes[] Node, int[] SelectedNodes, int[] SupType)
	{
		Supports[] sup = null;
		sup = MenuFunctions.AddSupports(Node, sup, SelectedNodes, SupType);
		
		return sup;
	}
	
	public static ConcLoads[] DefineConcLoads(Nodes[] Node, int[] SelectedNodes, double[] UserDefinedConcLoads)
	{
		ConcLoads[] concloads = null;
		concloads = MenuFunctions.AddConcLoads(Node, concloads, SelectedNodes, UserDefinedConcLoads);
		
		return concloads;
	}
	
	public static DistLoads[] DefineDistLoads(Elements[] Elem, int[] SelectedNodes, double[] UserDefinedDistLoads)
	{
		DistLoads[] distloads = null;
		distloads = MenuFunctions.AddDistLoadsToElements(Elem, distloads, SelectedNodes, UserDefinedDistLoads);
		
		return distloads;
	}*/
	
	public static Object[] LoadEspecialInput()
	{
		String fileName = "Especial_input.txt";
		String[][] Input = ReadInput.ReadTxtFile(fileName);		
		double[][] EspecialCoords = new double[Input[0].length - 2][3];
		for (int coord = 0; coord <= Input[0].length - 3; coord += 1)
		{
			String[] Line = Input[0][coord + 1].split(",");
			EspecialCoords[coord][0] = Double.parseDouble(Line[0]);
			EspecialCoords[coord][1] = Double.parseDouble(Line[1]);
			EspecialCoords[coord][2] = Double.parseDouble(Line[2]);
		}		
		String MeshType = Input[1][1];
		String[] EspecialElemTypes = Arrays.copyOfRange(Input[2], 1, Input[2].length - 1);		
		int[][] EspecialMeshSizes = new int[Input[3].length - 2][2];
		for (int mesh = 0; mesh <= Input[3].length - 3; mesh += 1)
		{
			String[] Line = Input[3][mesh + 1].split(",");
			EspecialMeshSizes[mesh][0] = Integer.parseInt(Line[0]);
			EspecialMeshSizes[mesh][1] = Integer.parseInt(Line[1]);
		}
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
		
		return new Object[] {EspecialCoords, MeshType, EspecialElemTypes, EspecialMeshSizes, EspecialMat, EspecialSec, EspecialConcLoads, EspecialDistLoads, EspecialSupConfig};
	}
	
	public static Supports[] AddSupports(Nodes[] Node, Supports[] sup, int[] SelectedNodes, int[] SupType)
	{
		sup = Util.IncreaseArraySize(sup, SelectedNodes.length);
		for (int i = 0; i <= SelectedNodes.length - 1; i += 1)
		{
			int supid = sup.length - SelectedNodes.length + i;
			if (-1 < SelectedNodes[i])
			{
				sup[supid] = new Supports(supid, SelectedNodes[i], SupType);
				Node[SelectedNodes[i]].setSup(SupType);
			}
		}
		
		return sup;
	}
	
	public static Supports[] AddEspecialSupports(Nodes[] Node, ElemShape elemShape, MeshType meshType, int[] NElem, int SupConfig)
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
	
	
	
	/*public static DistLoads[] AddEspecialDistLoads(Elements[] Elem, double[][] UserDefinedDistLoads, int DistLoadConfig, int DistLoadType)
	{
		if (UserDefinedDistLoads != null)
		{
			if (0 < UserDefinedDistLoads.length)
			{
				int[] DistLoadElems = null;
				if (DistLoadConfig == 1)
				{
					DistLoadElems = new int[Elem.length];
					for (int elem = 0; elem <= Elem.length - 1; elem += 1)
					{
						DistLoadElems[elem] = elem;
					}
				}
				DistLoads[] distloads = DefineDistLoads(Elem, DistLoadElems, UserDefinedDistLoads[DistLoadType]);
				
				return distloads;
			}
		}
		
		return null;
	}*/
	
	public static int[] NodalDoFs (String MemberType)
	{
		/*This function defines the active DoFs in each node according to the member type*/		
		int[] DoFsPerNode = {};
		if (MemberType.equals("T2"))
		{
			DoFsPerNode = new int[] {0, 1};
		}	
		if (MemberType.equals("T3"))
		{
			DoFsPerNode = new int[] {0, 1, 2};
		}	
		if (MemberType.equals("B2"))
		{
			DoFsPerNode = new int[] {1, 5};
		}	
		if (MemberType.equals("B3"))
		{
			DoFsPerNode = new int[] {1, 4, 5};
		}	
		if (MemberType.equals("F2"))
		{
			DoFsPerNode = new int[] {0, 1, 5};
		}	
		if (MemberType.equals("F3"))
		{
			DoFsPerNode = new int[] {0, 1, 2, 3, 4, 5};
		}	
		return DoFsPerNode;
	}
	
	public static int[] CountDoFs (String MemberType, double[][] Coords, int[][] Sup)
	{
		/*This function counts the number of free, restrained, and total DoFs*/		
		int NumberTotalDoFs;
		int NumberFreeDoFs;
		int NumberRestDoFs;
		int NumberSup = Sup.length;	
		int[] DoFsPerNode = NodalDoFs(MemberType);
		int NumberDoFsPerNode = DoFsPerNode.length;
		
		NumberTotalDoFs = Coords.length*NumberDoFsPerNode;
		NumberFreeDoFs = NumberTotalDoFs;                       // Number of free DoFs = Total number of DoFs
		for (int i = 0; i <= NumberSup - 1; ++i)
		{
			for (int j = 0; j <= NumberDoFsPerNode - 1; ++j)
			{
				NumberFreeDoFs += -Sup[i][DoFsPerNode[j] + 1];  // Reduces the restrained DoFs from the number of free DoFs
			}
		}
		NumberRestDoFs = NumberTotalDoFs - NumberFreeDoFs;
		return new int[] {NumberFreeDoFs, NumberRestDoFs};
	}
	
	public static int[][] NumberDoFs (String MemberType, double[][] Coords, int[][] Sup)
	{
		/*This function numbers the DoFs in each node and stores the numbering in a matrix
		  Free DoFs are numbered starting at 0
		  Restrained DoFs are numbered starting at the number of free DoFs
		  This function miscalculates the number of RestDoFs if there are 2 or more supports in the same node*/		
		int[][] NodeDoF;
		boolean FoundSupport;
		int CountFreeDoFs = 0;
		int CountRestDoFs = 0;
		int[] DoFsPerNode = NodalDoFs(MemberType);
		int NumberNodes = Coords.length;
		int NumberSup = Sup.length;
		int NumberDoFsPerNode =  DoFsPerNode.length;
		int NumberFreeDoFs = CountDoFs(MemberType, Coords, Sup)[0];	

		/*Numbering the DoFs and storing them in the NodeDoF matrix*/
		NodeDoF = new int[NumberNodes][NumberDoFsPerNode];
		for (int node = 0; node <= NumberNodes - 1; node += 1)
		{
			FoundSupport = false;
			for (int j = 0; j <= NumberSup - 1; j += 1)
			{
				if (Sup[j][0] == node)
				{
					FoundSupport = true;
					for (int k = 0; k <= NumberDoFsPerNode - 1; k += 1)
					{
						if (Sup[j][DoFsPerNode[k] + 1] == 1)
						{
							NodeDoF[node][k] = CountRestDoFs + NumberFreeDoFs;
							CountRestDoFs += 1;
						}
						else
						{
							NodeDoF[node][k] = CountFreeDoFs;
							CountFreeDoFs += 1;
						}
					}	
				}	
			}
			if (!FoundSupport)
			{
				for (int k = 0; k <= NumberDoFsPerNode - 1; k += 1)
				{
					NodeDoF[node][k] = CountFreeDoFs;
					CountFreeDoFs += 1;
				}	
			}
		}
		return NodeDoF;
	}

	public static boolean NodeHasSup (int[][] Sup, int node)
	{
		if (Sup == null)
		{
			return false;
		}
		for (int i = 0; i <= Sup.length - 1; i += 1)
		{
			if (Sup[i][0] == node)
			{
				return true;
			}
		}
		return false;
	}

	public static int SupType (int[] DoFs)
	{
		int suptype = -1;
		
		if (DoFs[0] == 1 & DoFs[1] == 0 & DoFs[2] == 0 & DoFs[3] == 0 & DoFs[4] == 0 & DoFs[5] == 0)
		{
			suptype = 0;																				// roller in the x dir
		}
		if (DoFs[0] == 0 & DoFs[1] == 1 & DoFs[2] == 0 & DoFs[3] == 0 & DoFs[4] == 0 & DoFs[5] == 0)
		{
			suptype = 1;																				// roller in the y dir
		}
		if (DoFs[0] == 0 & DoFs[1] == 0 & DoFs[2] == 1 & DoFs[3] == 0 & DoFs[4] == 0 & DoFs[5] == 0)
		{
			suptype = 2;																				// roller in the z dir
		}
		if (DoFs[0] == 1 & DoFs[1] == 1 & DoFs[2] == 0 & DoFs[3] == 0 & DoFs[4] == 0 & DoFs[5] == 0)
		{
			suptype = 3;																				// pin in the x-y dir
		}
		if (DoFs[0] == 1 & DoFs[1] == 1 & DoFs[2] == 0 & DoFs[3] == 0 & DoFs[4] == 0 & DoFs[5] == 1)
		{
			suptype = 4;																				// cantilever xyz
		}
		if (DoFs[0] == 1 & DoFs[1] == 1 & DoFs[2] == 1 & DoFs[3] == 1 & DoFs[4] == 1 & DoFs[5] == 1)
		{
			suptype = 5;																				// full cantilever
		}
		
		return suptype;
	}

	public static double FindMaxConcLoad(ConcLoads[] ConcLoads)
	{
		if (ConcLoads != null)
		{
			double MaxLoad = ConcLoads[0].getLoads()[0];
			for (int l = 0; l <= ConcLoads.length - 1; l += 1)
			{
				for (int i = 0; i <= ConcLoads[l].getLoads().length - 1; i += 1)
				{
					if (MaxLoad < Math.abs(ConcLoads[l].getLoads()[i]))
					{
						MaxLoad = Math.abs(ConcLoads[l].getLoads()[i]);
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

	public static double FindMaxDistLoad(DistLoads[] DistLoads)
	{
		if (DistLoads != null)
		{
			double MaxLoad = DistLoads[0].getIntensity();
			for (int l = 0; l <= DistLoads.length - 1; l += 1)
			{
				if (MaxLoad < Math.abs(DistLoads[l].getIntensity()))
				{
					MaxLoad = Math.abs(DistLoads[l].getIntensity());
				}
			}
			return MaxLoad;
		}
		else
		{
			return 0;
		}
	}

	public static double FindMaxNodalDisp(NodalDisps[] NodalDisps)
	{
		if (NodalDisps != null)
		{
			double MaxLoad = NodalDisps[0].getDisps()[0];
			for (int l = 0; l <= NodalDisps.length - 1; l += 1)
			{
				for (int i = 0; i <= NodalDisps[l].getDisps().length - 1; i += 1)
				{
					if (MaxLoad < Math.abs(NodalDisps[l].getDisps()[i]))
					{
						MaxLoad = Math.abs(NodalDisps[l].getDisps()[i]);
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

	public static double[] GetNodePos(Nodes Node, boolean condition)
	{
		if (condition)
		{
			return GetNodeDefPos(Node);
		}
		else
		{
			return Node.getOriginalCoords();
		}
	}
	
	public static double[] GetNodeDefPos(Nodes Node)
	{
		double[] DefCoords = new double[Node.getOriginalCoords().length];
		for (int coord = 0; coord <= Node.getOriginalCoords().length - 1; coord += 1)
		{
			DefCoords[coord] = Node.getOriginalCoords()[coord] + Node.getDisp()[coord];
		}
		return DefCoords;
	}
	
	public static double[][] GetElemNodesDefPos(Nodes[] Node, int[] ElemNodes)
	{
		double[][] DefCoords = new double[ElemNodes.length][];
		for (int node = 0; node <= ElemNodes.length - 1; node += 1)
		{
			DefCoords[node] = new double[Node[ElemNodes[node]].getOriginalCoords().length];
			for (int coord = 0; coord <= Node[ElemNodes[node]].getOriginalCoords().length - 1; coord += 1)
    		{
				DefCoords[node][coord] = Node[ElemNodes[node]].getOriginalCoords()[coord] + Node[ElemNodes[node]].getDisp()[coord];
    		}
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

	public static int[] ElemsSelection(MyCanvas canvas, double[] StructCenter, Nodes[] Node, Element[] Elem, int[] MousePos, int[] DPPos, int[] SelectedElems, int[] SelWindowInitPos, double[] DiagramScales, boolean ShowSelWindow, boolean ShowDeformedStructure)
	{
		int ElemMouseIsOn = ElemMouseIsOn(Node, Elem, MousePos, StructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos(), ShowDeformedStructure);
		if (ShowSelWindow)
		{
			int[] ElemsInSelWindow = ElemsInsideWindow(Node, Elem, StructCenter, SelWindowInitPos, MousePos, DPPos, canvas.getPos(), canvas.getCenter(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos(), DiagramScales[1], ShowDeformedStructure);
			if (ElemsInSelWindow != null)
			{
				for (int i = 0; i <= ElemsInSelWindow.length - 1; i += 1)
				{
					SelectedElems = Util.AddElem(SelectedElems, ElemsInSelWindow[i]);
				}
			}
			/*else if (ElemMouseIsOn == -1)
			{
				SelectedElems = null;
			}*/
		}
		else if (-1 < ElemMouseIsOn)
		{
			SelectedElems = Util.AddElem(SelectedElems, ElemMouseIsOn);
			/*for (int elem = 0; elem <= Elem.length - 1; elem += 1)
			{
				double[] RealMousePos = ConvertToRealCoords2(MousePos, StructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
				if (Util.MouseIsOnElem(Node, Elem[elem], RealMousePos, canvas.getPos(), canvas.getSize(), canvas.getDrawingPos(), ShowDeformedStructure))
			    {
					SelectedElems = Util.AddElem(SelectedElems, elem);
			    }
			}*/
		}
		
		return SelectedElems;
	}
	
	public static int[] NodesSelection(MyCanvas canvas, double[] StructCenter, Nodes[] Node, int[] MousePos, int[] DPPos, int[] SelectedNodes, int[] SelWindowInitPos, int[] ElemDOFs, double[] DiagramScales, boolean ShowSelWindow, boolean ShowDeformedStructure)
	{
		double prec = 4;
		int NodeMouseIsOn = Util.NodeMouseIsOn(Node, MousePos, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos(), prec, ShowDeformedStructure);
		if (-1 < NodeMouseIsOn)
		{
			if (!ShowSelWindow)
			{
				SelectedNodes = null;
				SelectedNodes = Util.AddElem(SelectedNodes, NodeMouseIsOn);
			}
			else
			{
				SelectedNodes = null;
				int[] NodesInsideNodeSelectionWindow = Util.NodesInsideWindow(Node, StructCenter, SelWindowInitPos, MousePos, canvas.getPos(), canvas.getCenter(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos(), ElemDOFs, DiagramScales[1], ShowDeformedStructure);
				if (NodesInsideNodeSelectionWindow != null)
				{
					for (int i = 0; i <= NodesInsideNodeSelectionWindow.length - 1; i += 1)
					{
						SelectedNodes = Util.AddElem(SelectedNodes, NodesInsideNodeSelectionWindow[i]);
					}
				}		
			}
		}
		else
		{
			if (ShowSelWindow)
			{
				SelectedNodes = null;
				int[] NodesInsideNodeSelectionWindow = Util.NodesInsideWindow(Node, StructCenter, SelWindowInitPos, MousePos, canvas.getPos(), canvas.getCenter(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos(), ElemDOFs, DiagramScales[1], ShowDeformedStructure);
				if (NodesInsideNodeSelectionWindow != null)
				{
					for (int i = 0; i <= NodesInsideNodeSelectionWindow.length - 1; i += 1)
					{
						SelectedNodes = Util.AddElem(SelectedNodes, NodesInsideNodeSelectionWindow[i]);
					}
				}
			}
		}
		
		return SelectedNodes;
	}

	public static int SnipToNode(double[][] NodePos, int[] MousePos, MyCanvas canvas, int[] DPPos)
	{
		int mindist = 10;
		for (int node = 0; node <= NodePos.length - 1; node += 1)
		{
			int[] Pos = Util.ConvertToDrawingCoords(NodePos[node], canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos());
			if (Util.dist(new double[] {MousePos[0], MousePos[1]}, new double[] {Pos[0], Pos[1]}) <= mindist)
			{
				return node;
			}
		}
		return -1;
	}
}
