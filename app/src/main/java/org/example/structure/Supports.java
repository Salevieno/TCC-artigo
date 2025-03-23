package org.example.structure;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

import graphics.DrawPrimitives;

public class Supports
{
	private int ID;
	private int Node;		// Node
	private int[] DoFs;		// Restrained DoFs

	public static int[][] Types = new int[][] {{1, 0, 0, 0, 0, 0}, {0, 1, 0, 0, 0, 0}, {0, 0, 1, 0, 0, 0}, {1, 1, 0, 0, 0, 0}, {1, 1, 0, 0, 0, 1}, {1, 1, 1, 1, 1, 1}};
	public static Color color = Menus.palette[7];
	
	public Supports(int ID, Node Node, int[] DoFs)
	{
		this(ID, Node.getID(), DoFs);
	}

	public Supports(int ID, int Node, int[] DoFs)
	{
		this.ID = ID;
		this.Node = Node;
		this.DoFs = DoFs;
	}


	public static int typeFromDOFs (int[] DoFs)
	{
		int suptype = -1;
		
		if (DoFs[0] == 1 && DoFs[1] == 0 && DoFs[2] == 0 && DoFs[3] == 0 && DoFs[4] == 0 && DoFs[5] == 0)
		{
			suptype = 0;																				// roller in the x dir
		}
		if (DoFs[0] == 0 && DoFs[1] == 1 && DoFs[2] == 0 && DoFs[3] == 0 && DoFs[4] == 0 && DoFs[5] == 0)
		{
			suptype = 1;																				// roller in the y dir
		}
		if (DoFs[0] == 0 && DoFs[1] == 0 && DoFs[2] == 1 && DoFs[3] == 0 && DoFs[4] == 0 && DoFs[5] == 0)
		{
			suptype = 2;																				// roller in the z dir
		}
		if (DoFs[0] == 1 && DoFs[1] == 1 && DoFs[2] == 0 && DoFs[3] == 0 && DoFs[4] == 0 && DoFs[5] == 0)
		{
			suptype = 3;																				// pin in the x-y dir
		}
		if (DoFs[0] == 1 && DoFs[1] == 1 && DoFs[2] == 0 && DoFs[3] == 0 && DoFs[4] == 0 && DoFs[5] == 1)
		{
			suptype = 4;																				// cantilever xyz
		}
		if (DoFs[0] == 1 && DoFs[1] == 1 && DoFs[2] == 1 && DoFs[3] == 1 && DoFs[4] == 1 && DoFs[5] == 1)
		{
			suptype = 5;																				// full cantilever
		}
		
		return suptype;
	}

	public int typeFromDOFs() { return typeFromDOFs(DoFs) ;}

	public void dispaly(MyCanvas canvas, DrawPrimitives DP)
	{		
		int size = 6;
		int thick = 2;
		int suptype = typeFromDOFs();
		// int[] rotatedCoords = Util.RotateCoord(MainPanel.structure.getMesh().getNodes().get(Node).getOriginalCoords(), Center, canvas.getAngles()) ;
		Point Coords = canvas.inDrawingCoords(MainPanel.structure.getMesh().getNodes().get(Node).getOriginalCoords().asDoublePoint()) ;
		if (suptype == 0)
		{
			double[] angles = new double[] {-canvas.getAngles()[0], canvas.getAngles()[1], -canvas.getAngles()[2]};
			DrawRoller(Coords, thick, angles, size, color, DP);
		}
		else if (suptype == 1)
		{
			double[] angles = new double[] {-canvas.getAngles()[0], canvas.getAngles()[1], -canvas.getAngles()[2] - Math.PI/2.0};
			DrawRoller(Coords, thick, angles, size, color, DP);
		}
		else if (suptype == 2)
		{
			double[] angles = new double[] {-canvas.getAngles()[0], canvas.getAngles()[1] + Math.PI/2.0, -canvas.getAngles()[2]};
			DrawRoller(Coords, thick, angles, size, color, DP);
		}
		else if (suptype == 3)
		{
			DrawPin(Coords, thick, 0, size, color, DP);
		}
		else if (suptype == 4)
		{
			DrawCantilever(Coords, thick, -Math.PI/2, size, color, DP);
		}
		else if (suptype == 5)
		{
			DrawCantilever(Coords, thick, -Math.PI/2, size, color, DP);
		}
		
	}

	private void DrawRoller(Point pos, int thickness, double[] angles, int size, Color color, DrawPrimitives DP)
	{
		DP.drawCircle(pos, size, color) ;
	}
	
    // private void DrawRoller3D(Point pos, int thickness, double[] angles, int size, Color color)
    // {
    // 	// DP.DrawCircle3D(pos, size, thickness, angles, color);
    // 	// DrawBase3D(pos, thickness, angles, size, color, DP);
    // }

    private void DrawPin(Point pos, int stroke, double angle, int size, Color color, DrawPrimitives DP)
    {
		List<Point> contour = List.of(new Point(pos.x - size/2, pos.y + size), new Point(pos.x + size/2, pos.y + size), pos) ;
		Point basePos = new Point(pos.x, pos.y + size) ;
    	// DP.drawPolygon(contour, thickness, true, true, color, color);  
		DP.drawPolygon(contour, stroke, color) ;
    	DrawBase(basePos, stroke, angle, (int) (1.8*size), color, DP);
    }

    private void DrawCantilever(Point Pos, int thickness, double angle, int size, Color color, DrawPrimitives DP)
    {
    	DrawBase(Pos, thickness, angle, (int) (1.8*size), color, DP);
    }

    // private void DrawBase3D(Point3D Pos, int thickness, double[] angles, int size, Color color)
    // {
	// 	Point3D point1 = new Point3D(Pos.x - 2*size, Pos.y - 5*size/4, Pos.z) ;
	// 	Point3D point2 = new Point3D(Pos.x - 2*size, Pos.y + 5*size/4, Pos.z) ;
    // 	int NHair = 6;
    // 	double HairInclination = 0.12;
    // 	point1 = Util.RotateCoord(point1, Pos, angles);
    // 	point2 = Util.RotateCoord(point2, Pos, angles);
    // 	DP.DrawLine(point1, point2, thickness, color);
    // 	for (int i = 0; i <= NHair - 1; i += 1)
    // 	{
    // 		double inc = i/(double)(NHair - 1);		// From 0 to 1
    // 		int[] sizes = new int[] {point2.x - point1.x, point2.y - point1.y, point2.z - point1.z};
    // 		int[] LineInitPos = new int[] {(int) (point1.x + inc*sizes[0]*Math.cos(angles[0])), (int) (point1.y + inc*sizes[1]*Math.cos(angles[1])), (int) (point1.z + inc*sizes[2]*Math.cos(angles[2]))};
    // 		int[] LineFinalPos = new int[] {(int) (point1.x + inc*sizes[0]*Math.cos(angles[0]) - HairInclination*size), (int) (point1.y + inc*sizes[1]*Math.cos(angles[1]) + HairInclination*size), (int) (point1.z + inc*sizes[2]*Math.cos(angles[2]))};
    //     	DP.DrawLine(LineInitPos, LineFinalPos, thickness, color);
 	// 	}    	
    // }
    private void DrawBase(Point Pos, int thickness, double angle, int size, Color color, DrawPrimitives DP)
    {
    	int[][] PosInit = new int[][] {{(int) (Pos.x - 0.5*size), Pos.y}};
    	int[][] PosFinal = new int[][] {{(int) (Pos.x + 0.5*size), Pos.y}};
    	PosInit = Util.Rotation2D(new int[] {Pos.x, Pos.y}, PosInit, angle);
    	PosFinal = Util.Rotation2D(new int[] {Pos.x, Pos.y}, PosFinal, angle);
    	DP.drawLine(new Point(PosInit[0][0], PosInit[0][1]), new Point(PosFinal[0][0], PosFinal[0][1]), thickness, color);
    	for (int i = 0; i <= 10; i += 1)
    	{
			Point point1 = new Point((int) (PosInit[0][0] + size*i/10*Math.cos(angle)), (int) (PosInit[0][1] - size*i/10*Math.sin(angle))) ;
			Point point2 = new Point((int) (PosInit[0][0] - 0.12*size + size*i/10*Math.cos(angle)), (int) (PosInit[0][1] + 0.12*size - size*i/10*Math.sin(angle))) ;
        	DP.drawLine(point1, point2, thickness, color);
 		}    	
    }

	public int getID() {return ID;}
	public int getNode() {return Node;}
	public int[] getDoFs() {return DoFs;}
	public void setID(int I) {ID = I;}
	public void setNode(int N) {Node = N;}
	public void setDoFs(int[] D) {DoFs = D;}

	@Override
	public String toString() {
		return ID + "	" + Node + "	" + Arrays.toString(DoFs);
	}
	
}
