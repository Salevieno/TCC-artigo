package org.example.userInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.NodalDisp;
import org.example.mainTCC.Analysis;
import org.example.structure.ElemShape;
import org.example.structure.Element;
import org.example.structure.Mesh;
import org.example.structure.Node;
import org.example.structure.Reactions;
import org.example.structure.Supports;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;

public class DrawingOnAPanel
{
	private Font TextFont = new Font("SansSerif", Font.PLAIN, 20);
    private Font BoldTextFont = new Font("SansSerif", Font.BOLD, 20);
	private int stdStroke = 1;
	private Point3D RealStructCenter;
	private Graphics2D G;		
	
	public DrawingOnAPanel()
	{

	}

	// public DrawingOnAPanel(Graphics g, Point3D RealStructCenter)
	// {
	// 	G = (Graphics2D) g;
	// 	this.RealStructCenter = RealStructCenter;
	// }

	public void setG(Graphics g)
	{
		G = (Graphics2D) g;
	}	

	public Point3D getRealStructCenter() {
		return RealStructCenter;
	}

	public void setRealStructCenter(Point3D realStructCenter)
	{
		RealStructCenter = realStructCenter;
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

	// public void setCanvas(MyCanvas canvas)
	// {
	// 	this.canvas = canvas;
	// }

	public void paint(Graphics g) 
    { 
    	// This method is necessary, don't delete it
    } 	
	public void refresh()
	{
		
	}   
	
	// Simple functions
	public void DrawImage(Image File, int[] Pos, float angle, float[] Scale, String Alignment)
	{       
		if (File != null)
		{
			int l = (int)(Scale[0]*File.getWidth(null)), h = (int)(Scale[1]*File.getHeight(null));		
			AffineTransform a = null;	// Rotate image
			AffineTransform backup = G.getTransform();
			if (Alignment.equals("Left"))
			{
				a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos[0] + l/2, Pos[1] - h/2);	// Rotate image
				G.setTransform(a);
				G.drawImage(File, Pos[0], Pos[1] + h/2, l, h, null);
			}
			if (Alignment.equals("Center"))
			{
				a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos[0], Pos[1]);	// Rotate image
				G.setTransform(a);
				G.drawImage(File, Pos[0] - l/2, Pos[1], l, h, null);
			}
			if (Alignment.equals("Right"))
			{
				a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos[0] - l/2, Pos[1] + h/2);	// Rotate image
				G.setTransform(a);
				G.drawImage(File, Pos[0] + l, Pos[1] + h/2, l, h, null);
			}
	        G.setTransform(backup);
		}
	}
    public void DrawText(int[] Pos, String Text, String Alignment, float angle, String Style, int size, Color color)
    {
		float TextLength = TextLength(Text, TextFont, size, G), TextHeight = TextHeight(size);
    	int[] Offset = new int[2];
		AffineTransform a = null;	// Rotate rectangle
		AffineTransform backup = G.getTransform();
		if (Alignment.equals("Left"))
    	{
			a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos[0] - 0.5*TextLength, Pos[1] + 0.5*TextHeight);	// Rotate text
    	}
		else if (Alignment.equals("Center"))
    	{
			a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos[0], Pos[1] + 0.5*TextHeight);	// Rotate text
    		Offset[0] = -TextLength(Text, BoldTextFont, size, G)/2;
    		Offset[1] = TextHeight(size)/2;
    	}
    	else if (Alignment.equals("Right"))
    	{
			a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos[0], Pos[1] + 0.5*TextHeight);	// Rotate text
    		Offset[0] = -TextLength(Text, BoldTextFont, size, G);
    	}
    	if (Style.equals("Bold"))
    	{
    		G.setFont(new Font(BoldTextFont.getName(), BoldTextFont.getStyle(), size));
    	}
    	else
    	{
    		G.setFont(new Font(TextFont.getName(), TextFont.getStyle(), size));
    	}
    	if (0 < Math.abs(angle))
    	{
    		G.setTransform(a);
    	}
    	G.setColor(color);
    	G.drawString(Text, Pos[0] + Offset[0], Pos[1] + Offset[1]);
        G.setTransform(backup);
    }
    // public void DrawFitText(int[] Pos, String Text, String Alignment, float angle, String Style, int sy, int length, int size, Color color)
	// {
	// 	String[] FitText = Util.FitText(Text, length);
	// 	for (int i = 0; i <= FitText.length - 1; i += 1)
	// 	{
	// 		DrawText(new int[] {Pos[0], Pos[1] + i*sy}, FitText[i], Alignment, angle, Style, size, color);						
	// 	}
	// }
    // public void DrawPoint(int[] Pos, int size, boolean fill, Color ContourColor, Color FillColor)
    // {
    // 	G.setColor(ContourColor);
    // 	G.drawOval(Pos[0] - size/2, Pos[1] - size/2, size, size);
    // 	if (fill)
    // 	{
    //     	G.setColor(FillColor);
    //     	G.fillOval(Pos[0] - size/2, Pos[1] - size/2, size, size);
    // 	}
    // }
    
	public void DrawLine(int[] PosInit, int[] PosFinal, int thickness, Color color)
    {
    	G.setColor(color);
    	G.setStroke(new BasicStroke(thickness));
    	G.drawLine(PosInit[0], PosInit[1], PosFinal[0], PosFinal[1]);
    	G.setStroke(new BasicStroke(stdStroke));
    }
    public void DrawLine(Point PosInit, Point PosFinal, int thickness, Color color)
    {
    	DrawLine(new int[] {PosInit.x, PosInit.y}, new int[] {PosFinal.x, PosFinal.y}, thickness, color) ;
    }
    // public void DrawGradRect(int[] Pos, int l, int h, Color leftTop, Color rightTop, Color leftBottom, Color rightBottom)
    // {
    // 	float size = (float) ((l + h) / 2.0);
    // 	float radius = size-(size/4);
    //     float[] dist = {0f, 1.0f};
    //     Point2D center = new Point2D.Float(Pos[0], Pos[1]);
    //     Color noColor = new Color(1f, 1f, 1f, 0f);
    // 	GradientPaint twoColorGradient = new GradientPaint(Pos[0] + l, Pos[1], rightTop, Pos[0], Pos[1] + h, leftBottom);
    //     Color[] colors = {leftTop, noColor};
    //     RadialGradientPaint thirdColor = new RadialGradientPaint(center, radius, dist, colors);


    //     center = new Point2D.Float(Pos[0] + l, Pos[1] + h);
    //     Color[] colors2 = {rightBottom, noColor};
    //     RadialGradientPaint fourthColor = new RadialGradientPaint(center, radius, dist, colors2);
        
    // 	G.setPaint(twoColorGradient);
    // 	G.fillRect(Pos[0], Pos[1], l, h);
    	
    // 	G.setPaint(thirdColor);
    // 	G.fillRect(Pos[0], Pos[1], l, h);
    	
    // 	G.setPaint(fourthColor);
    // 	G.fillRect(Pos[0], Pos[1], l, h);
    // }
   
	public void DrawRect(int[] Pos, int l, int h, int thickness, String Alignment, float angle, boolean fill, Color ContourColor, Color FillColor)
    {
		AffineTransform Tx = new AffineTransform();
		Tx.translate(0, 22);    // Translation to correct positions
		//AffineTransform a = null;	// Rotate rectangle
		AffineTransform backup = G.getTransform();
    	int[] Offset = new int[2];
    	if (Alignment.equals("Left"))
    	{
    	     Tx.translate(Pos[0], Pos[1]);    // S3: final translation
    	     Tx.rotate(-angle*Math.PI/180.0);                  // S2: rotate around anchor
    	     Tx.translate(-Pos[0], -Pos[1]);  // S1: translate anchor to origin
			//a = AffineTransform.getRotateInstance(angle*Math.PI/180.0, Pos[0], Pos[1]);	// Rotate rectangle
    		Offset = new int[] {0, 0};
    	}
    	else if (Alignment.equals("Center"))
    	{
			//a = AffineTransform.getRotateInstance(-angle*Math.PI/180.0, Pos[0], Pos[1]);	// Rotate rectangle
    		Offset = new int[] {l/2, h/2};
    	}
    	else if (Alignment.equals("Right"))
    	{
			//a = AffineTransform.getRotateInstance(-angle*Math.PI/180.0, Pos[0], Pos[1] + h/2);	// Rotate rectangle
    		Offset = new int[] {l, 0};
    	}
    	if (0 < Math.abs(angle))
    	{
    		G.setTransform(Tx);
    	}
    	G.setStroke(new BasicStroke(thickness));
    	if (fill)
    	{
    		G.setColor(FillColor);
        	G.fillRect(Pos[0] - Offset[0], Pos[1] - Offset[1], l, h);
    	}
    	G.setColor(ContourColor);
    	G.drawRect(Pos[0] - Offset[0], Pos[1] - Offset[1], l, h);
    	G.setStroke(new BasicStroke(stdStroke));
        G.setTransform(backup);
    }
    
    public void DrawRect(Point Pos, int l, int h, int thickness, String Alignment, float angle, boolean fill, Color ContourColor, Color FillColor)
    {
    	DrawRect(new int[] {Pos.x, Pos.y}, l, h, thickness, Alignment, angle, fill, ContourColor, FillColor) ;
    }
    
    public void DrawRoundRect(int[] Pos, String Alignment, int l, int h, int Thickness, int ArcWidth, int ArcHeight, Color[] colors, Color ContourColor, boolean contour)
	{
		int[] Corner = new int[2];
		if (Alignment.equals("Left"))
		{
			Corner[0] = Pos[0];
			Corner[1] = Pos[1];
		}
		else if (Alignment.equals("Center"))
		{
			Corner[0] = Pos[0] - l/2;
			Corner[1] = Pos[1] - h/2;
		}
		else if (Alignment.equals("Right"))
		{
			Corner[0] = Pos[0] - l;
			Corner[1] = Pos[1] - h;
		}
		G.setStroke(new BasicStroke(Thickness));
		if (contour)
		{
			G.setColor(ContourColor);
			G.fillRoundRect(Corner[0] - Thickness, Corner[1] - Thickness, l + 2 * Thickness, h + 2 * Thickness, ArcWidth, ArcHeight);
		}
		if (2 <= colors.length)
		{
			if (colors[0] != null && colors[1] != null)
			{
			    GradientPaint gradient = new GradientPaint(Corner[0], Corner[1], colors[0], Corner[0], Corner[1] + h, colors[1]);
			    G.setPaint(gradient);
				G.fillRoundRect(Corner[0], Corner[1], l, h, ArcWidth, ArcHeight);
			}
		}
		else
		{
			if (colors[0] != null)
			{
				G.setColor(colors[0]);
				G.fillRoundRect(Corner[0], Corner[1], l, h, ArcWidth, ArcHeight);
			}
		}
		G.setStroke(new BasicStroke(1));
	}
    public void DrawCircle(int[] Pos, int diam, int thickness, boolean contour, boolean fill, Color ContourColor, Color FillColor)
    {
    	G.setStroke(new BasicStroke(thickness));
    	if (contour)
    	{
        	G.setColor(ContourColor);
        	G.drawOval(Pos[0] - diam/2, Pos[1] - diam/2, diam, diam);
    	}
    	if (fill)
    	{
        	G.setColor(FillColor);
        	G.fillOval(Pos[0] - diam/2, Pos[1] - diam/2, diam, diam);
    	}
    	G.setStroke(new BasicStroke(stdStroke));
    }
    public void DrawCircle(Point Pos, int diam, int thickness, boolean contour, boolean fill, Color ContourColor, Color FillColor)
    {
    	G.setStroke(new BasicStroke(thickness));
    	if (contour)
    	{
        	G.setColor(ContourColor);
        	G.drawOval(Pos.x - diam/2, Pos.y - diam/2, diam, diam);
    	}
    	if (fill)
    	{
        	G.setColor(FillColor);
        	G.fillOval(Pos.x - diam/2, Pos.y - diam/2, diam, diam);
    	}
    	G.setStroke(new BasicStroke(stdStroke));
    }
	public void DrawCircle(Point Pos, int r, int thickness, double[] theta, Color color)
	{
		DrawCircle(Pos, 2*r, thickness, true, true, color, color) ;
	}
    // public void DrawCircle3D(int[] Pos, int r, int thickness, double[] theta, Color color)
    // {
    // 	int NPoints = 10;
    // 	int[] Center = new int[] {Pos[0] - r, Pos[1], Pos[2]};
    // 	int[][] Coord = new int[NPoints][2];
    // 	for (int p = 0; p <= NPoints - 1; p += 1)
    // 	{
    // 		double a = 2*Math.PI*p/(double)(NPoints - 1);
    // 		Coord[p] = new int[] {(int) (Center[0] + r*Math.cos(a)), (int) (Center[1] + r*Math.sin(a)), Center[2]};
    // 		Coord[p] = Util.RotateCoord(Coord[p], Pos, theta);
    // 	}
    // 	DrawPolyLine(Coord, thickness, color);
    // }
    // public void DrawCircle3D(Point Pos, int r, int thickness, double[] theta, Color color)
    // {
    // 	DrawCircle3D(new int[] {Pos.x, Pos.y}, r, thickness, theta, color) ;
    // }
    public void DrawPolyLine(int[] x, int[] y, int thickness, Color color)
    {
    	G.setColor(color);
    	G.setStroke(new BasicStroke(thickness));
    	G.drawPolyline(x, y, x.length);
    	G.setStroke(new BasicStroke(stdStroke));
    }
    // public void DrawPolyLine(int[][] Coords, int thickness, Color color)
    // {
    // 	int[] x = new int[Coords.length];
    // 	int[] y = new int[Coords.length];
    // 	for (int c = 0; c <= Coords.length - 1; c += 1)
    // 	{
    // 		x[c] = Coords[c][0];
    // 		y[c] = Coords[c][1];
    // 	}
    // 	G.setColor(color);
    // 	G.setStroke(new BasicStroke(thickness));
    // 	G.drawPolyline(x, y, x.length);
    // 	G.setStroke(new BasicStroke(stdStroke));
    // }
    // public void DrawGradPolygon(int[] x, int[] y, int thickness, boolean contour, boolean fill, Color ContourColor, Color[] FillColor)
    // {
    // 	int npoints = x.length;
    // 	int[] Center = new int[2];
    // 	Center[0] = Util.Average(x);
    // 	Center[1] = Util.Average(y);
    // 	Color CenterColor = Color.black;
    // 	if (contour)
    // 	{
    //     	G.setColor(ContourColor);
    //     	G.setStroke(new BasicStroke(thickness));
    //     	G.drawPolygon(x, y, x.length);
    // 	}
    // 	if (fill)
    // 	{
    // 		for (int p = 0; p <= npoints - 2; p += 1)
    // 		{
    //     		GradientPaint ColorGradient = new GradientPaint((x[p] + x[p + 1]) / 2, (y[p] + y[p + 1]) / 2, FillColor[p], Center[0], Center[1], CenterColor);
    //     		G.setPaint(ColorGradient);
    //         	G.fillPolygon(new int[] {x[p], x[p + 1], Center[0]}, new int[] {y[p], y[p + 1], Center[1]}, 3);
    // 		}
    // 		GradientPaint ColorGradient = new GradientPaint((x[npoints - 1] + x[0]) / 2, (y[npoints - 1] + y[0]) / 2, FillColor[npoints - 1], Center[0], Center[1], CenterColor);
    // 		G.setPaint(ColorGradient);
    //     	G.fillPolygon(new int[] {x[npoints - 1], x[0], Center[0]}, new int[] {y[npoints - 1], y[0], Center[1]}, 3);
    // 	}
    // 	G.setStroke(new BasicStroke(stdStroke));
    // }
    public void DrawGradPolygon2(int[] x, int[] y, int thickness, boolean contour, boolean fill, Color ContourColor, Color[] FillColor)
    {
    	int npoints = x.length;
    	int[] Center = new int[2];
    	Center[0] = Util.Average(x);
    	Center[1] = Util.Average(y);
    	
    	double xmin = Util.FindMin(x), xmax = Util.FindMax(x);
    	double ymin = Util.FindMin(y), ymax = Util.FindMax(y);
    	float size = (float) ((xmax - xmin + ymax - ymin) / 2);
    	float radius = size-(size/4);
        float[] dist = {0f, 1.0f};
        Point2D center = new Point2D.Float(Center[0], Center[1]);
        Color AvrColor = Util.AverageColor(FillColor);
        Color CenterColor = new Color(AvrColor.getRed()/255.0f, AvrColor.getGreen()/255.0f, AvrColor.getBlue()/255.0f, 0f);
    	GradientPaint twoColorGradient = new GradientPaint(x[0], y[1], FillColor[0], x[1], y[1], FillColor[1]);
    	
        RadialGradientPaint[] newGradColors = new RadialGradientPaint[npoints - 2];

        for (int p = 0; p <= npoints - 3; p += 1)
		{
            center = new Point2D.Float(x[p + 2], y[p + 2]);
            Color[] pointscolors = {FillColor[p + 2], CenterColor};
            newGradColors[p] = new RadialGradientPaint(center, radius, dist, pointscolors);
		}
        
    	G.setPaint(twoColorGradient);
    	G.fillPolygon(x, y, x.length);
    	
    	for (int p = 0; p <= npoints - 3; p += 1)
		{
        	G.setPaint(newGradColors[p]);
        	G.fillPolygon(x, y, x.length);
		}
    	
    }
    public void DrawPolygon(int[] x, int[] y, int thickness, boolean contour, boolean fill, Color ContourColor, Color FillColor)
    {
    	if (contour)
    	{
        	G.setColor(ContourColor);
        	G.setStroke(new BasicStroke(thickness));
        	G.drawPolygon(x, y, x.length);
    	}
    	if (fill)
    	{
    		G.setColor(FillColor);
        	G.fillPolygon(x, y, x.length);
    	}
    	G.setStroke(new BasicStroke(stdStroke));
    }
    public void DrawPolygon(List<Point> points, int thickness, boolean contour, boolean fill, Color ContourColor, Color FillColor)
    {
		int[] x = points.stream().mapToInt(p -> p.x).toArray();
        int[] y = points.stream().mapToInt(p -> p.y).toArray();
    	DrawPolygon(x, y, thickness, contour, fill, ContourColor, FillColor) ;
    }

    // public void DrawArc(int[] Pos, int l, int h, double[] angle, String unit, Color color)
    // {
    // 	if (unit.equals("rad"))
    // 	{
    // 		angle[0] = 180.0/Math.PI*angle[0];
    // 		angle[1] = 180.0/Math.PI*angle[1];
    // 	}
    // 	G.drawArc(Pos[0] - Math.abs(l/2), Pos[1] - Math.abs(h/2), l, h, (int) angle[0], (int) angle[1]);
    // }
    
	// public void DrawArc3D(int[] Pos, int rx, int ry, double[] angle, double[] theta, String unit, Color color)
    // {
    // 	if (unit.equals("rad"))
    // 	{
    // 		angle[0] = 180.0/Math.PI*angle[0];
    // 		angle[1] = 180.0/Math.PI*angle[1];
    // 	}
    // 	int thick = 2;
    // 	int NPoints = 10;
    // 	double[][] Coord = new double[NPoints][2];
    // 	int[] xCoord = new int[NPoints];
    // 	int[] yCoord = new int[NPoints];
    // 	for (int p = 0; p <= NPoints - 1; p += 1)
    // 	{
    // 		double a = angle[0] + (angle[1] - angle[0])*p/(double)(NPoints - 1);
    // 		Coord[p] = new double[] {Pos[0] + rx*Math.cos(a), Pos[1] + ry*Math.sin(a)};
    // 	}
    // 	for (int p = 0; p <= NPoints - 1; p += 1)
    // 	{
    // 		Coord[p] = Util.RotateCoord(Coord[p], new double[] {Pos[0], Pos[1], Pos[2]}, theta);
    // 		xCoord[p] = (int) (Coord[p][0]);
    // 		yCoord[p] = (int) (Coord[p][1]);
    // 	}
    // 	DrawPolyLine(xCoord, yCoord, thick, color);
    // }
    // public void DrawTriangle(int[] Pos, int size, int thickness, double theta, boolean fill, double ArrowSize, Color color)
    // {
    // 	double thetaop = Math.PI/8.0;	// opening
    // 	int ax1 = (int)(Pos[0] - ArrowSize*Math.cos(thetaop)*Math.cos(theta) + ArrowSize*Math.sin(thetaop)*Math.sin(theta));
    // 	int ay1 = (int)(Pos[1] - ArrowSize*Math.cos(thetaop)*Math.sin(theta) - ArrowSize*Math.sin(thetaop)*Math.cos(theta));
    // 	int ax2 = Pos[0];
    // 	int ay2 = Pos[1];
    //  	int ax3 = (int)(Pos[0] - ArrowSize*Math.cos(thetaop)*Math.cos(theta) - ArrowSize*Math.sin(thetaop)*Math.sin(theta));
    //  	int ay3 = (int)(Pos[1] - ArrowSize*Math.cos(thetaop)*Math.sin(theta) + ArrowSize*Math.sin(thetaop)*Math.cos(theta));
    //  	DrawPolygon(new int[] {ax1, ax2, ax3}, new int[] {ay1, ay2, ay3}, thickness, true, fill, color, color);
    // }
    
	// public void DrawTriangle3D(int[] Pos, int size, int thickness, double theta2D, double[] theta3D, boolean fill, double ArrowSize, Color color)
    // {
    // 	double thetaop = Math.PI/8.0;	// opening
    // 	int ax1 = (int)(Pos[0] - ArrowSize*Math.cos(thetaop)*Math.cos(theta2D) + ArrowSize*Math.sin(thetaop)*Math.sin(theta2D));
    // 	int ay1 = (int)(Pos[1] - ArrowSize*Math.cos(thetaop)*Math.sin(theta2D) - ArrowSize*Math.sin(thetaop)*Math.cos(theta2D));
    // 	int az1 = Pos[2];
    // 	int ax2 = Pos[0];
    // 	int ay2 = Pos[1];
    // 	int az2 = Pos[2];
    //  	int ax3 = (int)(Pos[0] - ArrowSize*Math.cos(thetaop)*Math.cos(theta2D) - ArrowSize*Math.sin(thetaop)*Math.sin(theta2D));
    //  	int ay3 = (int)(Pos[1] - ArrowSize*Math.cos(thetaop)*Math.sin(theta2D) + ArrowSize*Math.sin(thetaop)*Math.cos(theta2D));
    // 	int az3 = Pos[2];
     	
    //  	double[] P1 = Util.RotateCoord(new double[] {ax1, ay1, az1}, new double[] {Pos[0], Pos[1], Pos[2]}, theta3D);
    //  	double[] P2 = Util.RotateCoord(new double[] {ax2, ay2, az2}, new double[] {Pos[0], Pos[1], Pos[2]}, theta3D);
    //  	double[] P3 = Util.RotateCoord(new double[] {ax3, ay3, az3}, new double[] {Pos[0], Pos[1], Pos[2]}, theta3D);
    //  	/*double[] P1 = new double[] {ax1, ay1, az1};
    //  	double[] P2 = new double[] {ax2, ay2, az2};
    //  	double[] P3 = new double[] {ax3, ay3, az3};*/
    //  	DrawPolygon(new int[] {(int) P1[0], (int) P2[0], (int) P3[0]}, new int[] {(int) P1[1], (int) P2[1], (int) P3[1]}, thickness, true, fill, color, color);
    // }
    
	public void DrawAxisArrow3D(int[] Pos, int thickness, double[] theta, boolean fill, double Size, double ArrowSize, Color color)
    {
    	double thetaop = Math.PI/8.0;	// opening
    	double[][] Coords = new double[6][3];
    	int[] xCoords = new int[Coords.length], yCoords = new int[Coords.length];
    	Coords[0][0] = Pos[0] - Size;
    	Coords[0][1] = Pos[1];
    	Coords[0][2] = Pos[2];
    	Coords[1][0] = Pos[0];
    	Coords[1][1] = Pos[1];
    	Coords[1][2] = Pos[2];
    	Coords[2][0] = (int)(Pos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[2][1] = (int)(Pos[1] - ArrowSize*Math.sin(thetaop));
    	Coords[2][2] = Pos[2];
    	Coords[3][0] = (int)(Pos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[3][1] = (int)(Pos[1] + ArrowSize*Math.sin(thetaop));
    	Coords[3][2] = Pos[2];
    	Coords[4][0] = (int)(Pos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[4][1] = Pos[1];
    	Coords[4][2] = (int)(Pos[2] - ArrowSize*Math.sin(thetaop));
    	Coords[5][0] = (int)(Pos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[5][1] = Pos[1];
    	Coords[5][2] = (int)(Pos[2] + ArrowSize*Math.sin(thetaop));
    	for (int c = 0; c <= Coords.length - 1; c += 1)
    	{
         	Coords[c] = Util.RotateCoord(Coords[c], Coords[0], theta);
         	xCoords[c] = (int) Coords[c][0];
         	yCoords[c] = (int) Coords[c][1];
    	}
     	DrawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[4]}, new int[] {yCoords[1], yCoords[4]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[5]}, new int[] {yCoords[1], yCoords[5]}, thickness, color);
    }
    public void DrawArrow2D(int[] Pos, int thickness, double[] theta, double Size, double ArrowSize, String dir, Color color)
    {
    	int[] PointPos = Arrays.copyOf(Pos, Pos.length);
    	double thetaop = Math.PI/8.0;	// opening
    	double[][] Coords = new double[6][2];
    	int[] xCoords = new int[Coords.length], yCoords = new int[Coords.length];
    	if (dir.equals("FromPoint"))
    	{
    		PointPos[0] += Size;
    	}
    	Coords[0][0] = PointPos[0] - Size;
    	Coords[0][1] = PointPos[1];
    	Coords[1][0] = PointPos[0];
    	Coords[1][1] = PointPos[1];
    	Coords[2][0] = (int)(PointPos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[2][1] = (int)(PointPos[1] - ArrowSize*Math.sin(thetaop));
    	Coords[3][0] = (int)(PointPos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[3][1] = (int)(PointPos[1] + ArrowSize*Math.sin(thetaop));
    	for (int c = 0; c <= Coords.length - 1; c += 1)
    	{
         	Coords[c] = Util.RotateCoord(Coords[c], new double[] {PointPos[0] - Size, PointPos[1], 0}, theta);
         	xCoords[c] = (int) Coords[c][0];
         	yCoords[c] = (int) Coords[c][1];
    	}
     	DrawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, thickness, color);
    }
    public void DrawArrow3Dto(double[] Pos, int thickness, double[] theta, double Size, double ArrowSize, Color color, MyCanvas canvas)
    {
    	double thetaop = Math.PI / 8.0;	// opening
    	double[][] RealCoords = new double[6][3];
    	// int[][] DrawingCoords = new int[6][3];
    	int[] xCoords = new int[RealCoords.length], yCoords = new int[RealCoords.length];
    	RealCoords[0] = new double[] {Pos[0] - Size, Pos[1], Pos[2]};
    	RealCoords[1] = new double[] {Pos[0], Pos[1], Pos[2]};
    	RealCoords[2] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1] - ArrowSize*Math.sin(thetaop), Pos[2]};
    	RealCoords[3] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1] + ArrowSize*Math.sin(thetaop), Pos[2]};
    	RealCoords[4] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1], Pos[2] - ArrowSize*Math.sin(thetaop)};
    	RealCoords[5] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1], Pos[2] + ArrowSize*Math.sin(thetaop)};
    	
		// double[] RealCanvasCenter = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
    	// for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	// {
        //  	RealCoords[c] = Util.RotateCoord(RealCoords[c], Pos, theta);
    	// }
    	// for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	// {
        //  	RealCoords[c] = Util.RotateCoord(RealCoords[c], RealCanvasCenter, canvas.getAngles());
    	// }
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
    		// DrawingCoords[c] = Util.ConvertToDrawingCoords2Point3D(RealCoords[c], RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			// DrawingCoords[c] = canvas.inDrawingCoords(new Point2D.Double(RealCoords[c][0], RealCoords[c][1])) ;
		}
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
			Point drawingCoods = canvas.inDrawingCoords(new Point2D.Double(RealCoords[c][0], RealCoords[c][1])) ;
         	xCoords[c] = drawingCoods.x;
         	yCoords[c] = drawingCoods.y;
    	}
     	DrawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[4]}, new int[] {yCoords[1], yCoords[4]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[5]}, new int[] {yCoords[1], yCoords[5]}, thickness, color);
    }
    public void DrawArrow3Dfrom(double[] Pos, int thickness, double[] theta, double Size, double ArrowSize, Color color, MyCanvas canvas)
    {
    	double thetaop = Math.PI / 8.0;	// opening
    	double[][] RealCoords = new double[6][3];
    	int[][] DrawingCoords = new int[6][3];
    	int[] xCoords = new int[RealCoords.length], yCoords = new int[RealCoords.length];
    	RealCoords[0] = new double[] {Pos[0], Pos[1], Pos[2]};
    	RealCoords[1] = new double[] {Pos[0] + Size, Pos[1], Pos[2]};
    	RealCoords[2] = new double[] {Pos[0] + Size - ArrowSize*Math.cos(thetaop), Pos[1] - ArrowSize*Math.sin(thetaop), Pos[2]};
    	RealCoords[3] = new double[] {Pos[0] + Size - ArrowSize*Math.cos(thetaop), Pos[1] + ArrowSize*Math.sin(thetaop), Pos[2]};
    	RealCoords[4] = new double[] {Pos[0] + Size - ArrowSize*Math.cos(thetaop), Pos[1], Pos[2] - ArrowSize*Math.sin(thetaop)};
    	RealCoords[5] = new double[] {Pos[0] + Size - ArrowSize*Math.cos(thetaop), Pos[1], Pos[2] + ArrowSize*Math.sin(thetaop)};
    	
		double[] RealCanvasCenter = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
         	RealCoords[c] = Util.RotateCoord(RealCoords[c], Pos, theta);
    	}
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
         	RealCoords[c] = Util.RotateCoord(RealCoords[c], RealCanvasCenter, canvas.getAngles());
    	}
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
    		DrawingCoords[c] = Util.ConvertToDrawingCoords2Point3D(RealCoords[c], RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
    	}
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
         	xCoords[c] = DrawingCoords[c][0];
         	yCoords[c] = DrawingCoords[c][1];
    	}
     	DrawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[4]}, new int[] {yCoords[1], yCoords[4]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[5]}, new int[] {yCoords[1], yCoords[5]}, thickness, color);
    }
    // public void DrawMoment3D(int[] Pos, int thickness, double[] angles, int dof, double Size, double ArrowSize, Color color)
    // {
    // 	double thetaop = Math.PI/8.0;	// opening
    // 	double offset = 0.2;
    // 	double[][] Coords = new double[7][3];
    // 	int[] xCoords = new int[Coords.length], yCoords = new int[Coords.length];
    //  	double[] canvasAngles = new double[3];
    //  	if (dof == 3)
    //  	{
    //  		canvasAngles = new double[] {angles[0], angles[1] - Math.PI/2.0, angles[2]};
    //  	}
    //  	else if (dof == 4)
    //  	{
    //  		canvasAngles = new double[] {angles[0], angles[1], angles[2] - Math.PI/2.0};
    //  	}
    //  	else if (dof == 5)
    //  	{
    //  		canvasAngles = new double[] {angles[0], angles[1], angles[2]};
    //  	}

    // 	Coords[0] = new double[] {Pos[0] - Size, Pos[1], Pos[2]};
    // 	Coords[1] = new double[] {Pos[0], Pos[1], Pos[2]};
    // 	Coords[2] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1] - ArrowSize*Math.sin(thetaop), Pos[2]};
    // 	Coords[3] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1] + ArrowSize*Math.sin(thetaop), Pos[2]};
    // 	Coords[4] = new double[] {Pos[0] - offset*Size, Pos[1], Pos[2]};
    // 	Coords[5] = new double[] {Pos[0] - offset*Size - ArrowSize*Math.cos(thetaop), Pos[1] - ArrowSize*Math.sin(thetaop), Pos[2]};
    // 	Coords[6] = new double[] {Pos[0] - offset*Size - ArrowSize*Math.cos(thetaop), Pos[1] + ArrowSize*Math.sin(thetaop), Pos[2]};
    // 	for (int c = 0; c <= Coords.length - 1; c += 1)
    // 	{
    //      	Coords[c] = Util.RotateCoord(Coords[c], new double[] {Pos[0], Pos[1], Pos[2]}, canvasAngles);
    //      	xCoords[c] = (int) Coords[c][0];
    //      	yCoords[c] = (int) Coords[c][1];
    // 	}   	
    //  	DrawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, thickness, color);
    //  	DrawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, thickness, color);
    //  	DrawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, thickness, color);
    //  	DrawPolyLine(new int[] {xCoords[4], xCoords[5]}, new int[] {yCoords[4], yCoords[5]}, thickness, color);
    //  	DrawPolyLine(new int[] {xCoords[4], xCoords[6]}, new int[] {yCoords[4], yCoords[6]}, thickness, color);
     	
    //  	int[] Center = new int[] {Pos[0], Pos[1], Pos[2]};
    // 	int r = 20;
    // 	int asize = r/2;
    // 	double arcanglei = 80*Math.PI/180.0, arcanglef = 280*Math.PI/180.0;
    //  	double[] TriPos = new double[] {Center[0] + r*Math.cos(arcanglei) + asize*Math.sin(arcanglei), Center[1] - r*Math.sin(arcanglei) + asize*Math.cos(arcanglei), Center[2]};    	
    //  	TriPos = Util.RotateCoord(TriPos, new double[] {Center[0], Center[1], Center[2]}, canvasAngles);
    //  	DrawArc3D(Center, r, r, new double[] {arcanglei, arcanglef}, canvasAngles, "degree", color);
    //  	DrawTriangle3D(new int[] {(int) TriPos[0], (int) TriPos[1], (int) TriPos[2]}, asize, thickness, Math.PI/2.0 - arcanglei, canvasAngles, true, asize, color);
    // }
   
	public void DrawPL3D(double[] RealPos, double size, int thickness, double[] CanvasAngles, int dof, Color color, MyCanvas canvas)
    {
    	if (dof == 0)		// Fx
    	{
			double[] angle = new double[] {0, 0, 0};
			DrawArrow3Dto(RealPos, thickness, angle, size, size / 4.0, color, canvas);
    	}
    	else if (dof == 1)	// Fy
    	{
			double[] angle = new double[] {0, 0, 0 - Math.PI/2.0};
			DrawArrow3Dto(RealPos, thickness, angle, size, size / 4.0, color, canvas);	
    	}
    	else if (dof == 2)	// Fz
    	{
			double[] angle = new double[] {0, 0 + Math.PI/2.0, 0};
			DrawArrow3Dto(RealPos, thickness, angle, size, size / 4.0, color, canvas);
    	}
    }
       


    // Composed functions
 
    // public void DrawPoints(int[][] Pos, int size, boolean fill, Color ContourColor, Color FillColor)
    // {
    // 	for (int i = 0; i <= Pos.length - 1; i += 1)
    // 	{
    // 		DrawPoint(Pos[i], size, fill, ContourColor, FillColor);
    // 	}
    // }

    // Visual functions
    public void DrawWindow(int[] Pos, int L, int H, int boardthick, Color FillColor, Color ContourColor)
	{
		/*Color colorbot = new Color (Math.min(colortop.getRed() + 100, 255), Math.min(colortop.getGreen() + 100, 255), colortop.getBlue());
		Color[] ColorGradient = new Color[] {colortop, colorbot};
		DrawRoundRect(Pos, "Left", L, H, boardthick, 3, 3, ColorGradient, ContourColor, true);*/
		int t = 1;
		int offset = 1;		
		if (FillColor != null)
		{
			Color Light = Util.AddColor(FillColor, new double[] {50, 50, 50});
			Color Shade = Util.AddColor(FillColor, new double[] {-50, -50, -50});
			DrawRect(Pos, L, H, boardthick, "Left", 0, true, ContourColor, FillColor);
			DrawLine(new int[] {Pos[0] + offset, Pos[1] + offset}, new int[] {Pos[0] + L - offset - t, Pos[1] + offset}, t, Light);
			DrawLine(new int[] {Pos[0] + L - offset - t, Pos[1] + offset}, new int[] {Pos[0] + L - offset - t, Pos[1] + H - offset - t}, t, Light);
			DrawLine(new int[] {Pos[0] + offset, Pos[1] + H - offset - t}, new int[] {Pos[0] + offset, Pos[1] + offset}, t, Shade);
			DrawLine(new int[] {Pos[0] + offset, Pos[1] + H - offset - t}, new int[] {Pos[0] + L - offset - t, Pos[1] + H - offset - t}, t, Shade);
		}
	}
	
	private void DrawList(int[] Pos, int[] Size, int SelectedItem, String[] PropName, String Title, String ItemName, int[][] Input)
	{
		int FontSize = 11;
		int offset = 10;
		int sx = 0, sy = 0;
		int Nrow = Input.length, Ncol = PropName.length;
		Color TitleColor = Color.blue, PropNameColor = new Color(200, 180, 150), UnselectedItemColor = Color.cyan, SelectedItemColor = Color.green;
		sx = (int) (Size[0] - 2 * offset) / Ncol;
		sy = FontSize + 5;
		Size[1] = Nrow * sy + FontSize + 2 * offset;
		
		DrawText(new int[] {Pos[0] + Size[0] / 2, (int) (Pos[1] - 1.3 * FontSize / 2 - offset / 2)}, Title, "Center", 0, "Bold", (int) (1.3 * FontSize), TitleColor);
		DrawWindow(Pos, Size[0], Size[1], 2, new Color(50, 100, 120), null);		
		for (int row = 0; row <= Nrow - 1; row += 1)
		{
			int[] TextPos = new int[] {Pos[0] + offset + sx / 2, Pos[1] + offset + FontSize / 2};
			if (row == 0)
			{
				for (int prop = 0; prop <= Input[row].length; prop += 1)
				{					
					DrawText(new int[] {TextPos[0] + prop*sx, TextPos[1]}, PropName[prop], "Center", 0, "Bold", FontSize, PropNameColor);					
				}
			}							
			Color TextColor = UnselectedItemColor;
			if (SelectedItem == row)
			{
				TextColor = SelectedItemColor;
			}
			DrawText(new int[] {TextPos[0], TextPos[1] + (row + 1) * sy}, ItemName + " " + String.valueOf(row), "Center", 0, "Bold", FontSize, TextColor);
			for (int prop = 0; prop <= Input[row].length - 1; prop += 1)
			{
				DrawText(new int[] {TextPos[0] + (prop + 1) * sx, TextPos[1] + (row + 1) * sy}, String.valueOf(Input[row][prop]), "Center", 0, "Bold", FontSize, TextColor);			
			}
		}
	}

	private void DrawList(int[] Pos, int[] Size, int SelectedItem, String[] PropName, String Title, String ItemName, double[][] Input)
	{
		int FontSize = 11;
		int offset = 10;
		int sx = 0, sy = 0;
		int Nrow = Input.length, Ncol = PropName.length;
		Color TitleColor = Color.blue, PropNameColor = new Color(200, 180, 150), UnselectedItemColor = Color.cyan, SelectedItemColor = Color.green;
		sx = (int) (Size[0] - 2 * offset) / Ncol;
		sy = FontSize + 5;
		Size[1] = Nrow * sy + FontSize + 2 * offset;
		
		DrawText(new int[] {Pos[0] + Size[0] / 2, (int) (Pos[1] - 1.3 * FontSize / 2 - offset / 2)}, Title, "Center", 0, "Bold", (int) (1.3 * FontSize), TitleColor);
		DrawWindow(Pos, Size[0], Size[1], 2, new Color(50, 100, 120), null);		
		for (int row = 0; row <= Nrow - 1; row += 1)
		{
			int[] TextPos = new int[] {Pos[0] + offset + sx / 2, Pos[1] + offset + FontSize / 2};
			if (row == 0)
			{
				for (int prop = 0; prop <= Input[row].length; prop += 1)
				{					
					DrawText(new int[] {TextPos[0] + prop*sx, TextPos[1]}, PropName[prop], "Center", 0, "Bold", FontSize, PropNameColor);					
				}
			}							
			Color TextColor = UnselectedItemColor;
			if (SelectedItem == row)
			{
				TextColor = SelectedItemColor;
			}
			DrawText(new int[] {TextPos[0], TextPos[1] + (row + 1) * sy}, ItemName + " " + String.valueOf(row), "Center", 0, "Bold", FontSize, TextColor);
			for (int prop = 0; prop <= Input[row].length - 1; prop += 1)
			{
				DrawText(new int[] {TextPos[0] + (prop + 1) * sx, TextPos[1] + (row + 1) * sy}, String.valueOf(Util.Round(Input[row][prop], 1)), "Center", 0, "Bold", FontSize, TextColor);			
			}
		}
	}
		
	


	public void DrawSelectionWindow(Point InitPos, Point FinalPos)
	{
		int[] RectPos = new int[] {InitPos.x, InitPos.y};
		int l = FinalPos.x - InitPos.x, h = FinalPos.y - InitPos.y;
		if (InitPos.x <= FinalPos.x && InitPos.y <= FinalPos.y)
		{
			DrawRect(RectPos, l, h, 1, "Left", 0, false, Color.black, null);
		}
	}
		
	public void DrawNodeNumbers(List<Node> Node, Color NodeColor, boolean deformed, MyCanvas canvas)
	{
		int Offset = 6;
		int FontSize = 13;
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			int[][] DrawingCoords = new int[Node.size()][];
			DrawingCoords[node] = Util.ConvertToDrawingCoords2Point3D(Util.GetNodePos(Node.get(node), deformed), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			DrawText(new int[] {(int)(DrawingCoords[node][0] + Offset), (int)(DrawingCoords[node][1] + 1.1*TextHeight(FontSize))}, Integer.toString(node), "Left", 0, "Bold", FontSize, NodeColor);		
		}	
	}
	
	public void DrawElemNumbers(Mesh mesh, Color NodeColor, boolean deformed, MyCanvas canvas)
	{
		List<Node> Node = mesh.getNodes();
		List<Element> Elem = mesh.getElements();
		int FontSize = 13;
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
		{
			int[] DrawingCoords = new int[2];
			for (int elemnode = 0; elemnode <= Elem.get(elem).getExternalNodes().length - 1; elemnode += 1)
			{
				int nodeID = Elem.get(elem).getExternalNodes()[elemnode];
				Node node = Node.get(nodeID) ;
				Point3D nodeDeformedPos = new Point3D(Util.GetNodePos(node, deformed)[0], Util.GetNodePos(node, deformed)[1], Util.GetNodePos(node, deformed)[2]) ;
				// Point3D nodeDeformedPos = new Point3D(node.deformedPos()[0], deformed)[0], Util.GetNodePos(Node.get(nodeID), deformed)[1], Util.GetNodePos(Node.get(nodeID), deformed)[2]) ;
				Point nodeDeformedPosInDrawingCoords = canvas.inDrawingCoords(nodeDeformedPos) ;
				DrawingCoords[0] += nodeDeformedPosInDrawingCoords.x;
				DrawingCoords[1] += nodeDeformedPosInDrawingCoords.y;
				// DrawingCoords[0] += Util.ConvertToDrawingCoords(Util.GetNodePos(Node.get(node), deformed), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getDrawingPos())[0];
				// DrawingCoords[1] += Util.ConvertToDrawingCoords(Util.GetNodePos(Node.get(node), deformed), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getDrawingPos())[1];
			}
			DrawingCoords[0] = DrawingCoords[0] / Elem.get(elem).getExternalNodes().length;
			DrawingCoords[1] = DrawingCoords[1] / Elem.get(elem).getExternalNodes().length;
			DrawText(DrawingCoords, Integer.toString(elem), "Center", 0, "Bold", FontSize, NodeColor);		
		}	
	}

	public void DrawDOFNumbers(List<Node> Node, Color NodeColor, boolean deformed, MyCanvas canvas)
	{
		int Offset = 16;
		int FontSize = 13;
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			int[][] DrawingNodePos = new int[Node.size()][];
			DrawingNodePos[node] = Util.ConvertToDrawingCoords2Point3D(Util.GetNodePos(Node.get(node), deformed), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			for (int dof = 0; dof <= Node.get(node).dofs.length - 1; dof += 1)
			{
				double angle = 2 * Math.PI * dof / Node.get(node).dofs.length;
				int[] Pos = new int[] {(int)(DrawingNodePos[node][0] + Offset*Math.cos(angle)), (int)(DrawingNodePos[node][1] + Offset*Math.sin(angle) + 1.1*TextHeight(FontSize))};
				DrawText(Pos, Integer.toString(Node.get(node).dofs[dof]), "Left", 0, "Bold", FontSize, NodeColor);	
			}	
		}	
	}
	
	public void DrawDOFSymbols(List<Node> Node, Color NodeColor, boolean deformed, MyCanvas canvas)
	{
		Color ForceDOFColor = Menus.palette[8];
		Color MomentDOFColor = Menus.palette[9];
		Color CrossDerivativeDOFColor = Menus.palette[11];
		Color ShearRotationDOFColor = Menus.palette[11];
		int thickness = 2;
		double arrowsize = 0.5;
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			double[] NodeRealPos = Util.GetNodePos(Node.get(node), deformed);
			for (int dof = 0; dof <= Node.get(node).dofs.length - 1; dof += 1)
			{
				if (Node.get(node).getDOFType()[dof] == 0)
				{
					DrawArrow3Dto(NodeRealPos, thickness, new double[] {0, 0, 0}, arrowsize, 0.3 * arrowsize, ForceDOFColor, canvas);
				}
				if (Node.get(node).getDOFType()[dof] == 1)
				{
					DrawArrow3Dto(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, arrowsize, 0.3 * arrowsize, ForceDOFColor, canvas);
				}
				if (Node.get(node).getDOFType()[dof] == 2)
				{
					DrawArrow3Dto(NodeRealPos, thickness, new double[] {0, Math.PI / 2, 0}, arrowsize, 0.3 * arrowsize, ForceDOFColor, canvas);
				}
				if (Node.get(node).getDOFType()[dof] == 3)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 1.5 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 1.8 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas);
				}
				if (Node.get(node).getDOFType()[dof] == 4)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 1.5 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 1.8 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas);
				}
				if (Node.get(node).getDOFType()[dof] == 5)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, Math.PI / 2, 0}, 1.5 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, Math.PI / 2, 0}, 1.8 * arrowsize, 0.3 * arrowsize, MomentDOFColor, canvas);
				}
				if (Node.get(node).getDOFType()[dof] == 6)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 4}, 1.5 * arrowsize, 0.3 * arrowsize, CrossDerivativeDOFColor, canvas);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 4}, 1.8 * arrowsize, 0.3 * arrowsize, CrossDerivativeDOFColor, canvas);
				}
				if (Node.get(node).getDOFType()[dof] == 7)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 0.7 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor, canvas);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 1.0 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor, canvas);
				}
				if (Node.get(node).getDOFType()[dof] == 8)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 0.7 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor, canvas);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 1.0 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor, canvas);
				}
			}	
		}	
	}
	
	// public void DrawStructureContour3D(List<Point3D> coords, Color structureColor, MyCanvas canvas)
	// {
	// 	int thick = 2;
	// 	int[] Xcoords = new int[coords.size()];
	// 	int[] Ycoords = new int[coords.size()];
	// 	List<Point> drawingCoords = new ArrayList<>() ;

	// 	for (Point3D coord : coords)
	// 	{
	// 		Point drawingCoord = canvas.inDrawingCoords(new Point2D.Double(coord.x, coord.y)) ;
	// 		drawingCoords.add(drawingCoord) ;
	// 	}

	// 	for (int c = 0; c <= Xcoords.length - 1; c += 1)
	// 	{
	// 		// int[] Coord = Util.ConvertToDrawingCoords2Point3D(coords.get(c).asArray(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(),
	// 		// 													canvas.getCenter(), canvas.getDrawingPos());
	// 		Point Coord = canvas.inDrawingCoords(new Point2D.Double(coords.get(c).x, coords.get(c).y)) ;
	// 		Xcoords[c] = Coord.x ;
	// 		Ycoords[c] = Coord.y ;
	// 		// Xcoords[c] = Coord[0];
	// 		// Ycoords[c] = Coord[1];
	// 	}
	// 	DrawPolygon(Xcoords, Ycoords, thick, true, true, structureColor, structureColor);
	// }
	
	public void DrawNodes3D(List<Node> Node, List<Node> selectedNodes, Color NodeColor, boolean deformed, int[] DOFsPerNode, double Defscale, MyCanvas canvas)
	{
		int size = 6;
		int thick = 1;
		double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			int[][] DrawingCoords = new int[Node.size()][3];
			if (deformed)
			{
				double[] DeformedCoords = Util.ScaledDefCoords(Node.get(node).getOriginalCoords(), Node.get(node).getDisp(), DOFsPerNode, Defscale);
				DrawingCoords[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(DeformedCoords, Center, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			}
			else
			{
				DrawingCoords[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(Util.GetNodePos(Node.get(node), deformed), Center, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			}
			DrawCircle(DrawingCoords[node], size, thick, false, true, Color.black, NodeColor);
			if (selectedNodes != null)
			{
				for (int i = 0; i <= selectedNodes.size() - 1; i += 1)
				{
					if (node == selectedNodes.get(i).getID())
					{
						DrawCircle(DrawingCoords[node], 2*size, thick, false, true, Color.black, Color.red);
					}
				}
			}
		}
	}
	
	public void DrawElements3D(Mesh mesh, int[] SelectedElems, boolean showmatcolor, boolean showseccolor, boolean showcontour, boolean showdeformed, double Defscale, MyCanvas canvas)
	{
		int thick = 1;
		List<Node> Node = mesh.getNodes();
		List<Element> Elem = mesh.getElements();
		double[] RealCanvasCenter = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
		{
			int[] Nodes = Elem.get(elem).getExternalNodes();
			int[] ElemDOFs = Elem.get(elem).getDOFs();
			int[][] DrawingCoord = new int[Nodes.length][2]; 
			int[] xCoords = new int[Nodes.length + 1], yCoords = new int[Nodes.length + 1];
			Color color = new Color(0, 100, 55);
			if (Elem.get(elem).getMat() != null)
			{
				color = Util.AddColor(color, new double[] {0, -50, 100});
			}
			if (Elem.get(elem).getSec() != null)
			{
				color = Util.AddColor(color, new double[] {0, -50, 100});
			}
			if (showmatcolor && Elem.get(elem).getMat() != null)
			{
				color = Elem.get(elem).getMat().getColor() ;
			}
			if (showseccolor && Elem.get(elem).getSec() != null)
			{
				color = Elem.get(elem).getSec().getColor() ;
			}
			for (int node = 0; node <= Nodes.length - 1; node += 1)
			{
				if (showdeformed)
				{
					double[] DeformedCoords = Util.ScaledDefCoords(Node.get(Nodes[node]).getOriginalCoords(), Node.get(Nodes[node]).getDisp(), ElemDOFs, Defscale);
					DrawingCoord[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(DeformedCoords, RealCanvasCenter, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
				}
				else
				{
					double[] OriginalCoords = Util.GetNodePos(Node.get(Nodes[node]), showdeformed);
					DrawingCoord[node] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(OriginalCoords, RealCanvasCenter, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
				}
				xCoords[node] = DrawingCoord[node][0];
				yCoords[node] = DrawingCoord[node][1];
			}
			xCoords[Nodes.length] = DrawingCoord[0][0];
			yCoords[Nodes.length] = DrawingCoord[0][1];
			DrawPolygon(xCoords, yCoords, thick, false, true, Color.black, color);
			if (showcontour)
			{
				DrawPolygon(xCoords, yCoords, thick, true, false, Color.black, color);
			}
			if (SelectedElems != null)
			{
				for (int i = 0; i <= SelectedElems.length - 1; i += 1)
				{
					if (elem == SelectedElems[i])
					{
						DrawPolygon(xCoords, yCoords, thick, false, true, Color.black, Color.red);
					}
				}
			}
		}
	}
	
	public void DrawDistLoads3D (Mesh mesh, List<DistLoad> distLoads, boolean ShowValues, Color DistLoadsColor, boolean condition,
	int[] DOFsPerNode, double Defscale, MyCanvas canvas)
	{
		int[] NArrows = new int[] {4, 4};
		int MaxArrowSize = 1;
		int thickness = 2;
		double MaxLoad = Util.FindMaxDistLoad(distLoads);
		List<Node> Node = mesh.getNodes();
		List<Element> Elem = mesh.getElements();
		for (int l = 0; l <= distLoads.size() - 1; l += 1)
		{
			int elem = distLoads.get(l).getElem();
			int[] nodes = Elem.get(elem).getExternalNodes();
			double[] RealLeftBotDefCoords = Util.ScaledDefCoords(Node.get(nodes[3]).getOriginalCoords().asArray(), Util.GetNodePos(Node.get(nodes[3]), condition), DOFsPerNode, Defscale);
			double[] RealRightTopDefCoords = Util.ScaledDefCoords(Node.get(nodes[1]).getOriginalCoords().asArray(), Util.GetNodePos(Node.get(nodes[1]), condition), DOFsPerNode, Defscale);
			//int[] DrawingLeftBotCoords = Util.ConvertToDrawingCoords2Point3D(RealLeftBotDefCoords, RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			//int[] DrawingRightTopCoords = Util.ConvertToDrawingCoords2Point3D(RealRightTopDefCoords, RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			if (distLoads.get(l).getType() == 0)
			{
				
			}
			if (distLoads.get(l).getType() == 4)
			{
				//double LoadIntensity = DistLoads[l].getIntensity();
				for (int i = 0; i <= NArrows[0] - 1; i += 1)
				{
					for (int j = 0; j <= NArrows[1] - 1; j += 1)
					{
						double x = (RealLeftBotDefCoords[0] + (RealRightTopDefCoords[0] - RealLeftBotDefCoords[0])*(i/(double)(NArrows[0] - 1)));
						double y = (RealLeftBotDefCoords[1] + (RealRightTopDefCoords[1] - RealLeftBotDefCoords[1])*(j/(double)(NArrows[1] - 1)));
						double z = RealLeftBotDefCoords[2];
						DrawPL3D(new double[] {x, y, z}, MaxArrowSize*distLoads.get(l).getIntensity()/MaxLoad, thickness, canvas.getAngles(), 2, DistLoadsColor, canvas);
					}
				}
			}
		}
	}

	public void DrawNodalDisps3D (List<Node> Node, List<NodalDisp> NodalDisps, int[] DOFsPerNode, boolean ShowValues, Color ConcLoadsColor, boolean condition, double Defscale)
	{/*
		int MaxArrowSize = 40;
		//int thickness = 2;
		double MaxLoad = Util.FindMaxNodalDisp(NodalDisps);
		double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
		for (int l = 0; l <= NodalDisps.length - 1; l += 1)
		{
			int node = NodalDisps[l].getNode();
			double[] RealDefCoords = Util.RotateCoord(Util.ScaledDefCoords(Node.get(node).getOriginalCoords(), Util.GetNodePos(Node.get(node), condition), DOFsPerNode, Defscale), Center, canvas.getAngles());
			//int[] DrawingDefCoords = Util.ConvertToDrawingCoords2Point3D(RealDefCoords, RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			for (int dof = 0; dof <= DOFsPerNode.length - 1; dof += 1)
			{
				double LoadIntensity = NodalDisps[l].getDisps()[DOFsPerNode[dof]];
				if (0 < Math.abs(LoadIntensity))
				{
					int size = (int)(MaxArrowSize * LoadIntensity / MaxLoad);
					if (DOFsPerNode[dof] <= 2)
					{
						//DrawPL3D(new int[] {DrawingDefCoords[0], DrawingDefCoords[1], 0}, size, thickness, canvas.getAngles(), DOFsPerNode[dof], ConcLoadsColor);
					}
					else
					{
						//DrawMoment3D(DrawingDefCoords, thickness, canvas.getAngles(), DOFsPerNode[dof], true, size, size / 4.0, ConcLoadsColor);
					}
				}
			}
		}*/
	}
	
	public void DrawReactions3D (List<Node> Node, Reactions[] Reactions, int[] ElemDOFs, boolean ShowValues, Color ReactionsColor, boolean condition, double Defscale, MyCanvas canvas)
	{
		int MaxArrowSize = 1;
		int thickness = 2;
		double MaxAbsLoad = Util.FindMaxReaction(Reactions);
		for (int l = 0; l <= Reactions.length - 1; l += 1)
		{
			int node = Reactions[l].getNode();
			double[] RealDefCoords = Util.ScaledDefCoords(Node.get(node).getOriginalCoords(), Node.get(node).getDisp(), ElemDOFs, Defscale);
			for (int r = 0; r <= Reactions[l].getLoads().length - 1; r += 1)
			{
				double LoadIntensity = Reactions[l].getLoads()[r];
				if (0 < Math.abs(LoadIntensity))
				{
					double size = MaxArrowSize * LoadIntensity / (double) MaxAbsLoad;
					if (r <= 2)
					{
						DrawPL3D(RealDefCoords, size, thickness, canvas.getAngles(), r, ReactionsColor, canvas);
					}
					else if (r <= 5)
					{
						//DrawMoment3D(DrawingDefCoords, thickness, canvas.getAngles(), DOFsPerNode[dof], true, size, size / 4.0, ReactionsColor);
					}
					if (ShowValues)
					{
						double[] RealTextPos = Arrays.copyOf(RealDefCoords, 3);
						if (r == 0)
						{
							RealTextPos[0] += -2*Math.signum(LoadIntensity);
							RealTextPos[1] += -1*Math.signum(LoadIntensity);
						}
						if (r == 1)
						{
							RealTextPos[0] += 2*Math.signum(LoadIntensity);
						}
						if (r == 2)
						{
							RealTextPos[2] += -0.5*Math.signum(LoadIntensity);
						}
						int[] DrawingDefCoords = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(RealTextPos, RealStructCenter.asArray(), canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());						
						DrawLoadValues(DrawingDefCoords, ElemDOFs, r, LoadIntensity, ReactionsColor);
					}
				}
			}
		}
	}
	
	public void DrawLoadValues(int[] LoadDrawingPos, int[] DOFsPerNode, int dof, double LoadValue, Color color)
	{
		int FontSize = 13;
		int[] DrawingTextPos = Arrays.copyOf(LoadDrawingPos, LoadDrawingPos.length);
		if (dof <= 2)
		{
			DrawText(DrawingTextPos, String.valueOf(Util.Round(LoadValue, 2)), "Center", 0, "Bold", FontSize, color);
		}
	}
	
	
	private void DrawGrid(int[] InitPos, int[] FinalPos, int NumSpacing, Color color)
	{
		int LineThickness = 1;
		int[] Length = new int[] {FinalPos[0] - InitPos[0], InitPos[1] - FinalPos[1]};
		for (int i = 0; i <= NumSpacing - 1; i += 1)
		{
			DrawLine(new int[] {InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1]}, new int[] {InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1] - Length[1]}, LineThickness, color);						
			DrawLine(new int[] {InitPos[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing}, new int[] {InitPos[0] + Length[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing}, LineThickness, color);						
		}
	}
	
    private void DrawGraph(int[] Pos, int size, int type, Color TitleColor, Color GridColor)
	{
		int NumSpacing = 10;
		//DrawLine(Pos, new int[] {Pos[0], (int) (Pos[1] - 1.1*size)}, 1, AxisColor);
		//DrawLine(Pos, new int[] {(int) (Pos[0] + 1.1*size), Pos[1]}, 1, AxisColor);
		//DrawPolyLine(new int[] {Pos[0] - asize, Pos[0], Pos[0] + asize}, new int[] {(int) (Pos[1] - 1.1*size) + asize, (int) (Pos[1] - 1.1*size), (int) (Pos[1] - 1.1*size) + asize}, 1, AxisColor);
		//DrawPolyLine(new int[] {(int) (Pos[0] + 1.1*size - asize), (int) (Pos[0] + 1.1*size), (int) (Pos[0] + 1.1*size - asize)}, new int[] {Pos[1] - asize, Pos[1], Pos[1] + asize}, 1, AxisColor);
		if (type == 0)		// All axis positive
		{
			DrawArrow2D(Pos, 1, new double[] {0, 0, -Math.PI / 2.0}, 1.1 * size, 1.1 * size / 10.0, "FromPoint", Color.black);
			DrawArrow2D(Pos, 1, new double[] {0, 0, 0}, 1.2 * size, 1.1 * size / 10.0, "FromPoint", Color.black);
		}
		else if (type == 1)		// X axis negative and Y axis positive
		{
			DrawArrow2D(new int[] {Pos[0] + size, Pos[1]}, 1, new double[] {0, 0, -Math.PI / 2.0}, 1.1 * size, 1.1 * size / 10.0, "FromPoint", Color.black);
			DrawArrow2D(new int[] {Pos[0] + size, Pos[1]}, 1, new double[] {0, 0, -Math.PI}, 1.1 * size, 1.1 * size / 10.0, "FromPoint", Color.black);
		}
		DrawGrid(Pos, new int[] {Pos[0] + size, Pos[1] - size}, NumSpacing, GridColor);
	}
    
    public void Draw2DPlot(int[] Pos, int size, String Title, String XaxisName, String YaxisName, double[] XValues, double[] YValues, double XMin, double YMin, double XMaxAbs, double YMaxAbs, int xprec, int yprec, Color TextColor, Color GridColor)
	{
    	int TextSize = size / 6;
    	int GraphType = 0;
    	if (XMin < 0)
    	{
    		GraphType = 1;
    	}
    	DrawGraph(Pos, size, GraphType, TextColor, GridColor);
		if (XValues != null && YValues != null)
		{
			if (XValues.length == YValues.length)
			{
				if (1 <= XValues.length)
				{
					int[] x = new int[XValues.length], y = new int[YValues.length];
					if (GraphType == 0)
			    	{
						DrawText(new int[] {(int) (Pos[0] + size), (int) (Pos[1] - 0.1*size)}, XaxisName, "Left", 0, "Bold", TextSize, TextColor);
						DrawText(new int[] {(int) (Pos[0] + size), (int) (Pos[1] + 0.2*size)}, String.valueOf(Util.Round(XMaxAbs, xprec)), "Center", 0, "Bold", TextSize, TextColor);
						DrawText(new int[] {(int) (Pos[0]), (int) (Pos[1] + 0.2*size)}, String.valueOf(0), "Center", 0, "Bold", TextSize, TextColor);
						DrawText(new int[] {(int) (Pos[0] - 0.12*size), (int) (Pos[1] - size)}, YaxisName, "Right", 0, "Bold", TextSize, TextColor);
						DrawText(new int[] {(int) (Pos[0] - 0.12*size), (int) (Pos[1] - 0.8*size)}, String.valueOf(Util.Round(YMaxAbs, yprec)), "Right", 0, "Bold", TextSize, TextColor);
			    	}
					else if (GraphType == 1)
			    	{
						DrawText(new int[] {(int) (Pos[0]), (int) (Pos[1] - 0.1*size)}, XaxisName, "Right", 0, "Bold", TextSize, TextColor);
						DrawText(new int[] {(int) (Pos[0]), (int) (Pos[1] + 0.2*size)}, String.valueOf(Util.Round(XMin, xprec)), "Center", 0, "Bold", TextSize, TextColor);
						DrawText(new int[] {(int) (Pos[0] + size), (int) (Pos[1] + 0.2*size)}, String.valueOf(0), "Center", 0, "Bold", TextSize, TextColor);
						DrawText(new int[] {(int) (Pos[0] + size), (int) (Pos[1] - size)}, YaxisName, "Left", 0, "Bold", TextSize, TextColor);
						DrawText(new int[] {(int) (Pos[0] + size), (int) (Pos[1] - 0.8*size)}, String.valueOf(Util.Round(YMaxAbs, yprec)), "Left", 0, "Bold", TextSize, TextColor);
			    	}
						
					for (int j = 0; j <= XValues.length - 1; j += 1)
					{
						x[j] = Pos[0] + (int) (size*(XValues[j] - XMin)/XMaxAbs);
						y[j] = Pos[1] - (int) (size*(YValues[j] - YMin)/YMaxAbs);
					}
					DrawPolyLine(x, y, 2, TextColor);
				}	
			}
			else
			{
				System.out.println("The size of Xvalues and Yvalues are different at Visuals -> DrawVarGraph");
			}
		}
	}

	public void DrawContours3D(List<Element> Elem, List<Node> nodes, int[] SelectedElems, boolean showelemcontour, boolean condition,
			double Defscale, double minvalue, double maxvalue, String ResultType, int selecteddof, boolean NonlinearMat, boolean NonlinearGeo, String ColorSystem,
			MyCanvas canvas)
	{
		int Ninterpoints = 0;
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
		{
			/* Get edge nodes and coordinates*/
			int[] EdgeNodes = Elem.get(elem).getExternalNodes();
			double[][] EdgeCoords = new double[EdgeNodes.length][3];
			for (int node = 0; node <= EdgeNodes.length - 1; node += 1)
			{
				if (condition)
				{
					EdgeCoords[node] = Util.ScaledDefCoords(nodes.get(EdgeNodes[node]).getOriginalCoords(), nodes.get(EdgeNodes[node]).getDisp(), nodes.get(node).getDOFType(), Defscale);
				}
				else
				{
					EdgeCoords[node] = Util.GetNodePos(nodes.get(EdgeNodes[node]), condition);
				}
			}
			
			/* Get contour coordinates */
			double[][] ContourCoords = new double[EdgeNodes.length * (1 + Ninterpoints)][3];
			for (int node = 0; node <= EdgeNodes.length - 2; node += 1)
			{
				double[] Line = new double[] {EdgeCoords[node][0], EdgeCoords[node][1], EdgeCoords[node][2], EdgeCoords[node + 1][0], EdgeCoords[node + 1][1], EdgeCoords[node + 1][2]};
				for (int i = 0; i <= Ninterpoints; i += 1)
				{
					double offset = i / (double)(Ninterpoints + 1);
					double[] NewCoord = Util.CreatePointInLine(Line, offset);
					ContourCoords[node * (Ninterpoints + 1) + i] = NewCoord;
				}
			}			
			double[] Line = new double[] {EdgeCoords[EdgeNodes.length - 1][0], EdgeCoords[EdgeNodes.length - 1][1], EdgeCoords[EdgeNodes.length - 1][2], EdgeCoords[0][0], EdgeCoords[0][1], EdgeCoords[0][2]};
			for (int i = 0; i <= Ninterpoints; i += 1)
			{
				double offset = i / (double)(Ninterpoints + 1);
				double[] NewCoord = Util.CreatePointInLine(Line, offset);
				ContourCoords[(EdgeNodes.length - 1) * (Ninterpoints + 1) + i] = NewCoord;
			}

			/* Get displacements on contour */
			double[] ContourValue = new double[ContourCoords.length];
			if (Elem.get(elem).getShape().equals(ElemShape.rectangular) | Elem.get(elem).getShape().equals(ElemShape.r8))
			{
				double L = 2 * Elem.get(elem).calcHalfSize(nodes)[0];
				double H = 2 * Elem.get(elem).calcHalfSize(nodes)[1];
				double[] CenterCoords = Elem.get(elem).getCenterCoords();
				for (int point = 0; point <= ContourCoords.length - 1; point += 1)
				{
					double[] natCoords = Util.InNaturalCoordsRect(CenterCoords, L, H, ContourCoords[point]);
					double e = natCoords[0];
					double n = natCoords[1];
					
					if (-1 < selecteddof)
					{
						if (ResultType.equals("Displacement"))
						{
							double[] disp = Elem.get(elem).getDisp();
							ContourValue[point] = Analysis.DispOnPoint(nodes, Elem.get(elem), e, n, selecteddof, disp);
						}
						else if (ResultType.equals("Strain"))
						{
							double[] strain = Elem.get(elem).getStrain();
							ContourValue[point] = Analysis.StrainOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, strain);
						}
						else if (ResultType.equals("Stress"))
						{
							double[] stress = Elem.get(elem).getStress();
							ContourValue[point] = Analysis.StressOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, stress);
						}
						else if (ResultType.equals("Force"))
						{
							double[] force = Elem.get(elem).getIntForces();
							ContourValue[point] = Analysis.ForceOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, force);
						}
					}
					ContourCoords[point][2] = ContourValue[point] * Defscale;
				}
			}
			else if (Elem.get(elem).getShape().equals(ElemShape.triangular))
			{			
				for (int point = 0; point <= ContourCoords.length - 1; point += 1)
				{
					double[] natCoords = Util.InNaturalCoordsTriangle(EdgeCoords, ContourCoords[point]);
					double[] u = Elem.get(elem).getDisp();
					ContourValue[point] = Analysis.DispOnPoint(nodes, Elem.get(elem), natCoords[0], natCoords[1], selecteddof, u);
				}
			}

			/* Draw the contour */
			int[][] DrawingCoords = new int[ContourCoords.length][3];
			int[] xCoords = new int[ContourCoords.length], yCoords = new int[ContourCoords.length];
			double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			int thick = 1;
			Color[] colors = new Color[ContourCoords.length];
			Arrays.fill(colors, new Color(0, 100, 55));
			for (int point = 0; point <= ContourCoords.length - 1; point += 1)
			{
				DrawingCoords[point] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(ContourCoords[point], Center, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
				xCoords[point] = DrawingCoords[point][0];
				yCoords[point] = DrawingCoords[point][1];
				colors[point] = Util.FindColor(ContourValue[point], minvalue, maxvalue, ColorSystem);
			}
			DrawGradPolygon2(xCoords, yCoords, thick, false, true, Color.black, colors);
			if (showelemcontour)
			{
				DrawPolygon(xCoords, yCoords, thick, true, false, Color.black, null);
			}
			if (SelectedElems != null)
			{
				for (int i = 0; i <= SelectedElems.length - 1; i += 1)
				{
					if (elem == SelectedElems[i])
					{
						DrawPolygon(xCoords, yCoords, thick, false, true, Color.black, Color.red);
					}
				}
			}		
		}
		
	}

	public void DrawLists(Dimension PanelSize, int SelectedItem, String[] ItemNames, String Title, String Label, double[][] ItemTypes)
	{
		int[] ListPos = new int[] {(int) (0.025 * PanelSize.getWidth()), (int) (0.15 * PanelSize.getHeight())};
		int[] ListSize = new int[] {(int) (0.95 * PanelSize.getWidth()), (int) (0.9 * PanelSize.getHeight())};
		DrawList(ListPos, ListSize, SelectedItem, ItemNames, Title, Label, ItemTypes);
	}
	
	public void DrawLists(Dimension PanelSize, int SelectedItem, String[] ItemNames, String Title, String Label, int[][] ItemTypes)
	{
		int[] ListPos = new int[] {(int) (0.025 * PanelSize.getWidth()), (int) (0.15 * PanelSize.getHeight())};
		int[] ListSize = new int[] {(int) (0.95 * PanelSize.getWidth()), (int) (0.9 * PanelSize.getHeight())};
		DrawList(ListPos, ListSize, SelectedItem, ItemNames, Title, Label, ItemTypes);
	}
}
