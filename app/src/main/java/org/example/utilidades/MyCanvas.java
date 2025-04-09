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
	private double zoom;
	private double snipPower = 1 ;
	
	private Point2D.Double gridSpacing;
	private final int qtdPointsMin = 6 ;
	private final int qtdPointsMax = 46 ;
	// TODO classe para grid
	public MyCanvas(Point pos, Dimension size, Point2D.Double dimension, Point drawingPos)
	{
		this.title = null;
		this.pos = pos;
		this.size = size;
		this.dimension = dimension;
		this.drawingPos = drawingPos;
		Point qtdPoints = calculateNumberOfGridPoints();
		this.gridSpacing = new Point2D.Double(dimension.x / qtdPoints.x, dimension.y / qtdPoints.y) ;	
		this.zoom = 1;
		this.angles = new Point3D(0.0, 0.0, 0.0) ;

		updateCenter() ;
	}

	private void updateCenter() { center = new Point(pos.x + size.width / 2, pos.y + size.height / 2) ;}

	public Point2D.Double centerInRealCoords() { return inRealCoords(center) ;}

	public Point calculateNumberOfGridPoints()
	{
		Point qtdPoints = new Point();
		qtdPoints.x = (int) (qtdPointsMin + (qtdPointsMax - qtdPointsMin) * (dimension.x % 100) / 100.0);
		qtdPoints.y = (int) (qtdPointsMin + (qtdPointsMax - qtdPointsMin) * (dimension.y % 100) / 100.0);	
		return qtdPoints;
	}
	
	public Point2D.Double closestGridNodePos(Point2D.Double point)
	{
		Point qtdGridPoints = calculateNumberOfGridPoints();

		for (int i = 0; i <= qtdGridPoints.x; i += 1)
		{	
			for (int j = 0; j <= qtdGridPoints.y; j += 1)
			{	
				Point2D.Double pos = new Point2D.Double(i * gridSpacing.x, j * gridSpacing.x) ;
				double dx = Math.abs(point.x - pos.x) ;
				double dy = Math.abs(point.y - pos.y) ;
				if (dx <= snipPower * gridSpacing.x / 2.0 && dy <= snipPower * gridSpacing.y / 2.0)
				{
					return pos ;
				}
			}
		}

		return point ;
	}

	public Point2D.Double getCoordFromMouseClick(Point MousePos, boolean SnipToGridIsOn)
	{
		Point2D.Double mousePosRealCoords = inRealCoords(MousePos) ;

		return SnipToGridIsOn ? closestGridNodePos(mousePosRealCoords) : mousePosRealCoords ;

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

	
	public void drawGrid(int pointSize, DrawPrimitives DP)
	{
		Point qtdPoints = calculateNumberOfGridPoints();	
		for (int i = 0; i <= qtdPoints.x; i += 1)
		{	
			for (int j = 0; j <= qtdPoints.y; j += 1)
			{	
				Point2D.Double realPos = new Point2D.Double(i * gridSpacing.x, j * gridSpacing.y) ;
				Point drawingPos = inDrawingCoords(realPos) ;
				DP.drawCircle(drawingPos, pointSize, 1, Color.black, Color.black);
			}
		}
	}	
	
	public void drawCenter(DrawPrimitives DP)
	{
		DP.drawCircle(new Point(center.x, center.y), 10, 1, Main.palette[7], null);
	}

	public void display(String Title, boolean displayGrid, DrawPrimitives DP)
	{
		if (Title != null)
		{
			DP.drawText(new Point(pos.x + size.width / 2, pos.y), Align.center, Title, Main.palette[6]) ;
		}
		if (displayGrid)
		{
			drawGrid(2, DP) ;
		}
	}

	public void display(boolean displayGrid, DrawPrimitives DP)
	{
		display(title, displayGrid, DP) ;
	}

	public Point getPos() {return pos;}
	public Dimension getSize() {return size;}
	public Point2D.Double getDimension() {return dimension;}
	public Point getDrawingPos() {return drawingPos;}
	public Point getCenter() {return center;}
	public Point2D.Double getGridSpacing() {return gridSpacing;}
	public double getZoom() {return zoom;}
	public Point3D getAngles() {return angles;}
	public void setTitle(String T) {title = T;}
	public void setPos(Point P) {pos = P;}
	public void setSize(Dimension S) {size = S;}
	public void setDimension(Point2D.Double D) {dimension = D;}
	public void setDrawingPos(Point D) {drawingPos = D;}
	public void setCenter(Point C) {center = C;}
	public void setGridSpacing(Point2D.Double G) {gridSpacing = G;}
	public void setZoom(double Z) {zoom = Z;}
	public void setAngles(Point3D a) {angles = a;}

	@Override
	public String toString() {
		return "MyCanvas [title=" + title + ", pos=" + pos + ", size=" + size + ", dimension="
				+ dimension + ", DrawingPos=" + drawingPos + ", Center="
				+ center + ", zoom=" + zoom + "]";
	}
	
}
