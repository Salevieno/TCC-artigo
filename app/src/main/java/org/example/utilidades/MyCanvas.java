package org.example.utilidades;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

import org.example.Main;
import org.example.view.CentralPanel;

import graphics.Align;
import graphics.DrawPrimitives;

public class MyCanvas
{
	private String title;
	private Point pos;
	private Point3D angles;
	private Dimension size;
	private Point2D.Double dimension;
	private Point drawingPos;
	private Point center;
	private Point2D.Double GridSpacing;
	private double zoom;
	
	public MyCanvas(Point pos, Dimension size, Point2D.Double dimension, Point drawingPos)
	{
		this.title = null;
		this.pos = pos;
		this.size = size;
		this.dimension = dimension;
		this.drawingPos = drawingPos;
		this.GridSpacing = new Point2D.Double(5.0, 5.0);
		this.zoom = 1;
		this.angles = new Point3D(0.0, 0.0, 0.0) ;

		updateCenter() ;
	}

	private void updateCenter() { center = new Point(pos.x + size.width / 2, pos.y + size.height / 2) ;}

	public Point2D.Double centerInRealCoords() { return inRealCoords(center) ;}

	public void draw(String Title, double[] PointDist, DrawPrimitives DP)
	{
		int[] NPoints = new int[] {(int) (size.width/PointDist[0]), (int) (size.height / PointDist[1])};
		PointDist[0] = dimension.x/NPoints[0];
		PointDist[1] = dimension.y/NPoints[1];
		if (Title != null)
		{
			DP.drawText(new Point(pos.x + size.width / 2, pos.y), Align.center, Title, Main.palette[6]) ;
		}
		// DP.drawRect(pos, Align.topLeft, new Dimension(size.width, size.height), null, Main.palette[0]) ;
	}

	public void draw(double[] PointDist, DrawPrimitives DP)
	{
		draw(title, PointDist, DP) ;
	}
	
	public void drawGrid(int pointSize, DrawPrimitives DP)
	{
		int[] NPoints = CalculateNumberOfGridPoints(dimension);
		double[] PointsDist = new double[2];
		PointsDist[0] = size.width/(double)(NPoints[0]);
		PointsDist[1] = size.height/(double)(NPoints[1]);		
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
		DP.drawCircle(new Point(center.x, center.y), 10, 1, Main.palette[7], null);
	}

	public static int[] CalculateNumberOfGridPoints(Point2D.Double CanvasDim)
	{
		int[] NPointsMin = new int[] {6, 6}, NPointsMax = new int[] {46, 46};
		int[] NPoints = new int[2];
		NPoints[0] = (int) (NPointsMin[0] + (NPointsMax[0] - NPointsMin[0]) * (CanvasDim.x % 100) / 100.0);
		NPoints[1] = (int) (NPointsMin[1] + (NPointsMax[1] - NPointsMin[1]) * (CanvasDim.y % 100) / 100.0);	
		return NPoints;
	}
	


	public Point2D.Double inRealCoords(Point drawingPos)
	{
		Point2D.Double realPos = new Point2D.Double();
		realPos.x = (drawingPos.x - pos.x) / (double)size.width * dimension.x ;
		realPos.y = (-drawingPos.y + pos.y + size.height) / (double)size.height * dimension.y ;
		return realPos;
	}

	public Point inDrawingCoords(Point2D.Double realPos)
	{
		Point drawingPos = new Point();
		drawingPos.x = (int) (pos.x + drawingPos.x + realPos.x / dimension.x * size.width) ;
		drawingPos.y = (int) (pos.y + drawingPos.y + size.height - realPos.y / dimension.y * size.height) ;
		return drawingPos;
	}
	
	public Point inDrawingCoords(Point3D realPos)
	{
		return inDrawingCoords(realPos.asDoublePoint()) ;
	}

	public void incAngles(double dX, double dY, double dZ)
	{
		angles.translate(dX, dY, dZ) ;
		CentralPanel.structure.updateDrawings(this) ;
	}

	public void topView()
	{
		angles.translateTo(0, 0, 0) ;
		CentralPanel.structure.updateDrawings(this) ;
	}

	public void frontView()
	{
		angles.translateTo(0, -Math.PI/2, 0) ;
		CentralPanel.structure.updateDrawings(this) ;
	}

	public void sideView()
	{
		angles.translateTo(-Math.PI/2, 0, 0) ;
		CentralPanel.structure.updateDrawings(this) ;
	}

	public void incDrawingPos(int dx, int dy)
	{
		drawingPos.x += dx;
		drawingPos.y += dy;
		CentralPanel.structure.updateDrawings(this) ;
	}

	public Point getPos() {return pos;}
	public Dimension getSize() {return size;}
	public Point2D.Double getDimension() {return dimension;}
	public Point getDrawingPos() {return drawingPos;}
	public Point getCenter() {return center;}
	public Point2D.Double getGridSpacing() {return GridSpacing;}
	public double getZoom() {return zoom;}
	public Point3D getAngles() {return angles;}
	public void setTitle(String T) {title = T;}
	public void setPos(Point P) {pos = P;}
	public void setSize(Dimension S) {size = S;}
	public void setDimension(Point2D.Double D) {dimension = D;}
	public void setDrawingPos(Point D) {drawingPos = D;}
	public void setCenter(Point C) {center = C;}
	public void setGridSpacing(Point2D.Double G) {GridSpacing = G;}
	public void setZoom(double Z) {zoom = Z;}
	public void setAngles(Point3D a) {angles = a;}

	@Override
	public String toString() {
		return "MyCanvas [title=" + title + ", pos=" + pos + ", size=" + size + ", dimension="
				+ dimension + ", DrawingPos=" + drawingPos + ", Center="
				+ center + ", zoom=" + zoom + "]";
	}


	
}
