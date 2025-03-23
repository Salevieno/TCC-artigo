package org.example.utilidades;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Arrays;

import org.example.userInterface.Draw;
import org.example.userInterface.Menus;

import graphics.Align;
import graphics.DrawPrimitives;

public class MyCanvas
{
	int[] TitlePos;
	String title;
	private Point pos;
	int[] size;
	double[] dimension;
	int[] DrawingPos;
	int[] Center;
	double[] GridSpacing;
	double zoom;
	double[] angles;
	
	public MyCanvas(Point pos, int[] Size, double[] Dim, int[] DrawingPos)
	{
		TitlePos = null;
		title = null;
		this.pos = pos;
		this.size = Size;
		this.dimension = Dim;
		this.DrawingPos = DrawingPos;
		Center = new int[] {pos.x + Size[0] / 2, pos.y + Size[1] / 2};
		GridSpacing = new double[] {5, 5, 0};
		zoom = 1;
		angles = new double[] {0.0, 0.0, 0.0};
	}

	public void draw(String Title, double[] PointDist, DrawPrimitives DP)
	{
		int[] NPoints = new int[] {(int) (size[0]/PointDist[0]), (int) (size[1]/PointDist[1])};
		PointDist[0] = dimension[0]/NPoints[0];
		PointDist[1] = dimension[1]/NPoints[1];
		if (Title != null)
		{
			DP.drawText(new Point(pos.x + size[0] / 2, pos.y), Align.center, Title, Menus.palette[6]) ;
		}
		// DP.drawRect(pos, Align.topLeft, new Dimension(size[0], size[1]), null, Menus.palette[0]) ;
	}

	public void draw(double[] PointDist, DrawPrimitives DP)
	{
		draw(title, PointDist, DP) ;
	}
	
	public void drawGrid(int pointSize, DrawPrimitives DP)
	{
		int[] NPoints = CalculateNumberOfGridPoints(dimension);
		double[] PointsDist = new double[2];
		PointsDist[0] = size[0]/(double)(NPoints[0]);
		PointsDist[1] = size[1]/(double)(NPoints[1]);		
		for (int i = 0; i <= NPoints[0]; i += 1)
		{	
			for (int j = 0; j <= NPoints[1]; j += 1)
			{	
				Point Pos = new Point((int) (pos.x + i*PointsDist[0]), (int) (pos.y + j*PointsDist[1])) ;
				DP.drawCircle(Pos, pointSize, 1, Color.black, Color.black);
			}
		}
	}	
	
	public void drawCenter(DrawPrimitives DP)
	{
		DP.drawCircle(new Point(Center[0], Center[1]), 10, 1, Menus.palette[7], null);
	}

	public static int[] CalculateNumberOfGridPoints(double[] CanvasDim)
	{
		int[] NPointsMin = new int[] {6, 6}, NPointsMax = new int[] {46, 46};
		int[] NPoints = new int[2];
		NPoints[0] = (int) (NPointsMin[0] + (NPointsMax[0] - NPointsMin[0]) * (CanvasDim[0] % 100) / 100.0);
		NPoints[1] = (int) (NPointsMin[1] + (NPointsMax[1] - NPointsMin[1]) * (CanvasDim[1] % 100) / 100.0);	
		return NPoints;
	}
	


	public Point2D.Double inRealCoords(Point drawingPos)
	{
		Point2D.Double realPos = new Point2D.Double();
		realPos.x = (drawingPos.x - pos.x) / (double)size[0] * dimension[0] ;
		realPos.y = (-drawingPos.y + pos.y + size[1]) / (double)size[1] * dimension[1] ;
		return realPos;
	}

	public Point inDrawingCoords(Point2D.Double realPos)
	{
		Point drawingPos = new Point();
		drawingPos.x = (int) (pos.x + drawingPos.x + realPos.x / dimension[0] * size[0]) ;
		drawingPos.y = (int) (pos.y + drawingPos.y + size[1] - realPos.y / dimension[1] * size[1]) ;
		return drawingPos;
	}
	
	public Point inDrawingCoords(Point3D realPos)
	{
		return inDrawingCoords(new Point2D.Double(realPos.x, realPos.y)) ;
	}

	public void incAngles(double dX, double dY, double dZ)
	{
		angles[0] += dX;
		angles[1] += dY;
		angles[2] += dZ;
	}

	public void topView()
	{
		angles[0] = 0;
		angles[1] = 0;
		angles[2] = 0;
	}

	public void frontView()
	{
		angles[0] = 0;
		angles[1] = -Math.PI/2;
		angles[2] = 0;
	}

	public void sideView()
	{
		angles[0] = -Math.PI/2;
		angles[1] = 0;
		angles[2] = 0;
	}

	public void incDrawingPos(int dX, int dY)
	{
		DrawingPos[0] += dX;
		DrawingPos[1] += dY;
	}

	public int[] getTitlePos() {return TitlePos;}
	public String getTitle() {return title;}
	public Point getPos() {return pos;}
	public int[] getSize() {return size;}
	public double[] getDimension() {return dimension;}
	public int[] getDrawingPos() {return DrawingPos;}
	public int[] getCenter() {return Center;}
	public double[] getGridSpacing() {return GridSpacing;}
	public double getZoom() {return zoom;}
	public double[] getAngles() {return angles;}
	public void setTitlePos(int[] T) {TitlePos = T;}
	public void setTitle(String T) {title = T;}
	public void setPos(Point P) {pos = P;}
	public void setSize(int[] S) {size = S;}
	public void setDimension(double[] D) {dimension = D;}
	public void setDrawingPos(int[] D) {DrawingPos = D;}
	public void setCenter(int[] C) {Center = C;}
	public void setCenter(Point C) {Center = new int[] {C.x, C.y} ;}
	public void setGridSpacing(double[] G) {GridSpacing = G;}
	public void setZoom(double Z) {zoom = Z;}
	public void setAngles(double[] a) {angles = a;}

	@Override
	public String toString() {
		return "MyCanvas [title=" + title + ", pos=" + pos + ", size=" + Arrays.toString(size) + ", dimension="
				+ Arrays.toString(dimension) + ", DrawingPos=" + Arrays.toString(DrawingPos) + ", Center="
				+ Arrays.toString(Center) + ", zoom=" + zoom + "]";
	}


	
}
