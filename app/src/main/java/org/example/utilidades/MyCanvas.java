package org.example.utilidades;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

import org.example.Main;
import org.example.mainTCC.MainPanel;
import org.example.mainTCC.MenuFunctions;
import org.example.userInterface.Draw;

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
		MainPanel.getInstance().getCentralPanel().getStructure().updateDrawings(this) ;
	}

	public void topView()
	{
		angles.translateTo(0, 0, 0) ;
		MainPanel.getInstance().getCentralPanel().getStructure().updateDrawings(this) ;
	}

	public void frontView()
	{
		angles.translateTo(0, -Math.PI/2, 0) ;
		MainPanel.getInstance().getCentralPanel().getStructure().updateDrawings(this) ;
	}

	public void sideView()
	{
		angles.translateTo(-Math.PI/2, 0, 0) ;
		MainPanel.getInstance().getCentralPanel().getStructure().updateDrawings(this) ;
	}

	public void incDrawingPos(int dx, int dy)
	{
		drawingPos.x += dx;
		drawingPos.y += dy;
		MainPanel.getInstance().getCentralPanel().getStructure().updateDrawings(this) ;
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
	
	public void drawAxis(Point pos, int sizex, int sizey, int sizez, double[] CanvasAngles, DrawPrimitives DP)
	{
    	int thickness = 2;
		Draw.DrawAxisArrow3D(new Point3D(pos.x + sizex, pos.y, 0.0), thickness, new Point3D(CanvasAngles[0], CanvasAngles[1], CanvasAngles[2]), true, sizex, sizex / 40.0, Color.red, DP);
		Draw.DrawAxisArrow3D(new Point3D(pos.x + sizey, pos.y, 0.0), thickness, new Point3D(CanvasAngles[0], CanvasAngles[1], CanvasAngles[2] - Math.PI/2.0), true, sizey, sizey / 40.0, Color.green, DP);
		Draw.DrawAxisArrow3D(new Point3D(pos.x + sizez, pos.y, 0.0), thickness, new Point3D(CanvasAngles[0], CanvasAngles[1] - Math.PI/2.0, CanvasAngles[2]), true, sizez, sizez / 40.0, Color.blue, DP);	// z points outward
	}

	private static void drawMousePosWindow(Point pos, Point2D.Double RealMousePos, Color bgcolor, Color contourcolor, DrawPrimitives DP)
	{
		Dimension windowSize = new Dimension(200, 24) ;
		DP.drawRoundRect(pos, Align.topLeft, windowSize, bgcolor, true);
		DP.drawText(new Point(pos.x + 5, pos.y + windowSize.height / 2), Align.centerLeft, "Mouse at:", Main.palette[0]) ;
		DP.drawText(new Point(pos.x + 85, pos.y + windowSize.height / 2), Align.centerLeft, String.valueOf(Util.Round(RealMousePos.x, 2)) + " m", Main.palette[0]) ;
		DP.drawText(new Point(pos.x + 130, pos.y + windowSize.height / 2), Align.centerLeft, String.valueOf(Util.Round(RealMousePos.y, 2)) + " m", Main.palette[0]) ;
	}

	public void display(boolean displayGrid, DrawPrimitives DP)
	{

		Point LittleAxisPos = new Point(pos.x + size.width + 10, pos.y - 10);
		Point BigAxisPos = new Point(pos.x, pos.y + size.height);

		drawAxis(LittleAxisPos, size.width / 15, size.height / 15, 10, angles.asArray(), DP);
		drawAxis(BigAxisPos, size.width + 20, size.height + 20, 20, new double[] {0, 0, 0}, DP);
		
		Point posCanvasDimX = new Point(pos.x + size.width, pos.y + size.height + 15) ;
		Point posCanvasDimY = new Point(pos.x + 30, pos.y - 10) ;
		String canvasDimX = String.valueOf(Util.Round(dimension.x, 3)) + " m" ;
		String canvasDimY = String.valueOf(Util.Round(dimension.y, 3)) + " m" ;

		DP.drawText(posCanvasDimX, Align.center, canvasDimX, Main.palette[7]) ;
		DP.drawText(posCanvasDimY, Align.center, canvasDimY, Main.palette[10]) ;
		if (title != null)
		{
			DP.drawText(new Point(pos.x + size.width / 2, pos.y), Align.center, title, Main.palette[6]) ;
		}
		if (displayGrid)
		{
			drawGrid(2, DP) ;
		}

		Point2D.Double RealMousePos = inRealCoords(MenuFunctions.mousePos) ;
		drawMousePosWindow(new Point(BigAxisPos.x + size.width / 2 - 60, BigAxisPos.y + 20), RealMousePos, Main.palette[3], Main.palette[0], DP);
	}
	
	public void display(DrawPrimitives DP)
	{
		display(true, DP) ;
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
