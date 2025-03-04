package GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Arrays;

import javax.swing.ImageIcon;

import Main.Analysis;
import Utilidades.Util;
import structure.ConcLoads;
import structure.DistLoads;
import structure.ElemShape;
import structure.ElemType;
import structure.Element;
import structure.MyCanvas;
import structure.NodalDisps;
import structure.Nodes;
import structure.Reactions;
import structure.StructureShape;
import structure.Supports;

public class DrawingOnAPanel
{
	private Font TextFont = new Font("SansSerif", Font.PLAIN, 20);
    private Font BoldTextFont = new Font("SansSerif", Font.BOLD, 20);
	private int stdStroke = 1;
	private MyCanvas canvas;
	private double[] RealStructCenter;
	private Graphics2D G;		
	
	public DrawingOnAPanel(Graphics g, MyCanvas canvas, double[] RealStructCenter)
	{
		G = (Graphics2D) g;
		this.canvas = canvas;
		this.RealStructCenter = RealStructCenter;
	}
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
		float TextLength = Util.TextLength(Text, TextFont, size, G), TextHeight = Util.TextHeight(size);
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
    		Offset[0] = -Util.TextLength(Text, BoldTextFont, size, G)/2;
    		Offset[1] = Util.TextHeight(size)/2;
    	}
    	else if (Alignment.equals("Right"))
    	{
			a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos[0], Pos[1] + 0.5*TextHeight);	// Rotate text
    		Offset[0] = -Util.TextLength(Text, BoldTextFont, size, G);
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
    public void DrawFitText(int[] Pos, String Text, String Alignment, float angle, String Style, int sy, int length, int size, Color color)
	{
		String[] FitText = Util.FitText(Text, length);
		for (int i = 0; i <= FitText.length - 1; i += 1)
		{
			DrawText(new int[] {Pos[0], Pos[1] + i*sy}, FitText[i], Alignment, angle, Style, size, color);						
		}
	}
    public void DrawPoint(int[] Pos, int size, boolean fill, Color ContourColor, Color FillColor)
    {
    	G.setColor(ContourColor);
    	G.drawOval(Pos[0] - size/2, Pos[1] - size/2, size, size);
    	if (fill)
    	{
        	G.setColor(FillColor);
        	G.fillOval(Pos[0] - size/2, Pos[1] - size/2, size, size);
    	}
    }
    public void DrawLine(int[] PosInit, int[] PosFinal, int thickness, Color color)
    {
    	G.setColor(color);
    	G.setStroke(new BasicStroke(thickness));
    	G.drawLine(PosInit[0], PosInit[1], PosFinal[0], PosFinal[1]);
    	G.setStroke(new BasicStroke(stdStroke));
    }
    public void DrawGradRect(int[] Pos, int l, int h, Color leftTop, Color rightTop, Color leftBottom, Color rightBottom)
    {
    	float size = (float) ((l + h) / 2.0);
    	float radius = size-(size/4);
        float[] dist = {0f, 1.0f};
        Point2D center = new Point2D.Float(Pos[0], Pos[1]);
        Color noColor = new Color(1f, 1f, 1f, 0f);
    	GradientPaint twoColorGradient = new GradientPaint(Pos[0] + l, Pos[1], rightTop, Pos[0], Pos[1] + h, leftBottom);
        Color[] colors = {leftTop, noColor};
        RadialGradientPaint thirdColor = new RadialGradientPaint(center, radius, dist, colors);


        center = new Point2D.Float(Pos[0] + l, Pos[1] + h);
        Color[] colors2 = {rightBottom, noColor};
        RadialGradientPaint fourthColor = new RadialGradientPaint(center, radius, dist, colors2);
        
    	G.setPaint(twoColorGradient);
    	G.fillRect(Pos[0], Pos[1], l, h);
    	
    	G.setPaint(thirdColor);
    	G.fillRect(Pos[0], Pos[1], l, h);
    	
    	G.setPaint(fourthColor);
    	G.fillRect(Pos[0], Pos[1], l, h);
    }
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
			if (colors[0] != null & colors[1] != null)
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
    public void DrawCircle3D(int[] Pos, int r, int thickness, double[] theta, Color color)
    {
    	int NPoints = 10;
    	int[] Center = new int[] {Pos[0] - r, Pos[1], Pos[2]};
    	int[][] Coord = new int[NPoints][2];
    	for (int p = 0; p <= NPoints - 1; p += 1)
    	{
    		double a = 2*Math.PI*p/(double)(NPoints - 1);
    		Coord[p] = new int[] {(int) (Center[0] + r*Math.cos(a)), (int) (Center[1] + r*Math.sin(a)), Center[2]};
    		Coord[p] = Util.RotateCoord(Coord[p], Pos, theta);
    	}
    	DrawPolyLine(Coord, thickness, color);
    }
    public void DrawPolyLine(int[] x, int[] y, int thickness, Color color)
    {
    	G.setColor(color);
    	G.setStroke(new BasicStroke(thickness));
    	G.drawPolyline(x, y, x.length);
    	G.setStroke(new BasicStroke(stdStroke));
    }
    public void DrawPolyLine(int[][] Coords, int thickness, Color color)
    {
    	int[] x = new int[Coords.length];
    	int[] y = new int[Coords.length];
    	for (int c = 0; c <= Coords.length - 1; c += 1)
    	{
    		x[c] = Coords[c][0];
    		y[c] = Coords[c][1];
    	}
    	G.setColor(color);
    	G.setStroke(new BasicStroke(thickness));
    	G.drawPolyline(x, y, x.length);
    	G.setStroke(new BasicStroke(stdStroke));
    }
    public void DrawGradPolygon(int[] x, int[] y, int thickness, boolean contour, boolean fill, Color ContourColor, Color[] FillColor)
    {
    	int npoints = x.length;
    	int[] Center = new int[2];
    	Center[0] = Util.Average(x);
    	Center[1] = Util.Average(y);
    	Color CenterColor = Color.black;
    	if (contour)
    	{
        	G.setColor(ContourColor);
        	G.setStroke(new BasicStroke(thickness));
        	G.drawPolygon(x, y, x.length);
    	}
    	if (fill)
    	{
    		for (int p = 0; p <= npoints - 2; p += 1)
    		{
        		GradientPaint ColorGradient = new GradientPaint((x[p] + x[p + 1]) / 2, (y[p] + y[p + 1]) / 2, FillColor[p], Center[0], Center[1], CenterColor);
        		G.setPaint(ColorGradient);
            	G.fillPolygon(new int[] {x[p], x[p + 1], Center[0]}, new int[] {y[p], y[p + 1], Center[1]}, 3);
    		}
    		GradientPaint ColorGradient = new GradientPaint((x[npoints - 1] + x[0]) / 2, (y[npoints - 1] + y[0]) / 2, FillColor[npoints - 1], Center[0], Center[1], CenterColor);
    		G.setPaint(ColorGradient);
        	G.fillPolygon(new int[] {x[npoints - 1], x[0], Center[0]}, new int[] {y[npoints - 1], y[0], Center[1]}, 3);
    	}
    	G.setStroke(new BasicStroke(stdStroke));
    }
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
    public void DrawArc(int[] Pos, int l, int h, double[] angle, String unit, Color color)
    {
    	if (unit.equals("rad"))
    	{
    		angle[0] = 180.0/Math.PI*angle[0];
    		angle[1] = 180.0/Math.PI*angle[1];
    	}
    	G.drawArc(Pos[0] - Math.abs(l/2), Pos[1] - Math.abs(h/2), l, h, (int) angle[0], (int) angle[1]);
    }
    public void DrawArc3D(int[] Pos, int rx, int ry, double[] angle, double[] theta, String unit, Color color)
    {
    	if (unit.equals("rad"))
    	{
    		angle[0] = 180.0/Math.PI*angle[0];
    		angle[1] = 180.0/Math.PI*angle[1];
    	}
    	int thick = 2;
    	int NPoints = 10;
    	double[][] Coord = new double[NPoints][2];
    	int[] xCoord = new int[NPoints];
    	int[] yCoord = new int[NPoints];
    	for (int p = 0; p <= NPoints - 1; p += 1)
    	{
    		double a = angle[0] + (angle[1] - angle[0])*p/(double)(NPoints - 1);
    		Coord[p] = new double[] {Pos[0] + rx*Math.cos(a), Pos[1] + ry*Math.sin(a)};
    	}
    	for (int p = 0; p <= NPoints - 1; p += 1)
    	{
    		Coord[p] = Util.RotateCoord(Coord[p], new double[] {Pos[0], Pos[1], Pos[2]}, theta);
    		xCoord[p] = (int) (Coord[p][0]);
    		yCoord[p] = (int) (Coord[p][1]);
    	}
    	DrawPolyLine(xCoord, yCoord, thick, color);
    }
    public void DrawTriangle(int[] Pos, int size, int thickness, double theta, boolean fill, double ArrowSize, Color color)
    {
    	double thetaop = Math.PI/8.0;	// opening
    	int ax1 = (int)(Pos[0] - ArrowSize*Math.cos(thetaop)*Math.cos(theta) + ArrowSize*Math.sin(thetaop)*Math.sin(theta));
    	int ay1 = (int)(Pos[1] - ArrowSize*Math.cos(thetaop)*Math.sin(theta) - ArrowSize*Math.sin(thetaop)*Math.cos(theta));
    	int ax2 = Pos[0];
    	int ay2 = Pos[1];
     	int ax3 = (int)(Pos[0] - ArrowSize*Math.cos(thetaop)*Math.cos(theta) - ArrowSize*Math.sin(thetaop)*Math.sin(theta));
     	int ay3 = (int)(Pos[1] - ArrowSize*Math.cos(thetaop)*Math.sin(theta) + ArrowSize*Math.sin(thetaop)*Math.cos(theta));
     	DrawPolygon(new int[] {ax1, ax2, ax3}, new int[] {ay1, ay2, ay3}, thickness, true, fill, color, color);
    }
    public void DrawTriangle3D(int[] Pos, int size, int thickness, double theta2D, double[] theta3D, boolean fill, double ArrowSize, Color color)
    {
    	double thetaop = Math.PI/8.0;	// opening
    	int ax1 = (int)(Pos[0] - ArrowSize*Math.cos(thetaop)*Math.cos(theta2D) + ArrowSize*Math.sin(thetaop)*Math.sin(theta2D));
    	int ay1 = (int)(Pos[1] - ArrowSize*Math.cos(thetaop)*Math.sin(theta2D) - ArrowSize*Math.sin(thetaop)*Math.cos(theta2D));
    	int az1 = Pos[2];
    	int ax2 = Pos[0];
    	int ay2 = Pos[1];
    	int az2 = Pos[2];
     	int ax3 = (int)(Pos[0] - ArrowSize*Math.cos(thetaop)*Math.cos(theta2D) - ArrowSize*Math.sin(thetaop)*Math.sin(theta2D));
     	int ay3 = (int)(Pos[1] - ArrowSize*Math.cos(thetaop)*Math.sin(theta2D) + ArrowSize*Math.sin(thetaop)*Math.cos(theta2D));
    	int az3 = Pos[2];
     	
     	double[] P1 = Util.RotateCoord(new double[] {ax1, ay1, az1}, new double[] {Pos[0], Pos[1], Pos[2]}, theta3D);
     	double[] P2 = Util.RotateCoord(new double[] {ax2, ay2, az2}, new double[] {Pos[0], Pos[1], Pos[2]}, theta3D);
     	double[] P3 = Util.RotateCoord(new double[] {ax3, ay3, az3}, new double[] {Pos[0], Pos[1], Pos[2]}, theta3D);
     	/*double[] P1 = new double[] {ax1, ay1, az1};
     	double[] P2 = new double[] {ax2, ay2, az2};
     	double[] P3 = new double[] {ax3, ay3, az3};*/
     	DrawPolygon(new int[] {(int) P1[0], (int) P2[0], (int) P3[0]}, new int[] {(int) P1[1], (int) P2[1], (int) P3[1]}, thickness, true, fill, color, color);
    }
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
    public void DrawArrow3Dto(double[] Pos, int thickness, double[] theta, double Size, double ArrowSize, Color color)
    {
    	double thetaop = Math.PI / 8.0;	// opening
    	double[][] RealCoords = new double[6][3];
    	int[][] DrawingCoords = new int[6][3];
    	int[] xCoords = new int[RealCoords.length], yCoords = new int[RealCoords.length];
    	RealCoords[0] = new double[] {Pos[0] - Size, Pos[1], Pos[2]};
    	RealCoords[1] = new double[] {Pos[0], Pos[1], Pos[2]};
    	RealCoords[2] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1] - ArrowSize*Math.sin(thetaop), Pos[2]};
    	RealCoords[3] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1] + ArrowSize*Math.sin(thetaop), Pos[2]};
    	RealCoords[4] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1], Pos[2] - ArrowSize*Math.sin(thetaop)};
    	RealCoords[5] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1], Pos[2] + ArrowSize*Math.sin(thetaop)};
    	
		double[] RealCanvasCenter = Util.ConvertToRealCoords2(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
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
    		DrawingCoords[c] = Util.ConvertToDrawingCoords2(RealCoords[c], RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
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
    public void DrawArrow3Dfrom(double[] Pos, int thickness, double[] theta, double Size, double ArrowSize, Color color)
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
    	
		double[] RealCanvasCenter = Util.ConvertToRealCoords2(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
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
    		DrawingCoords[c] = Util.ConvertToDrawingCoords2(RealCoords[c], RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
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
    public void DrawMoment3D(int[] Pos, int thickness, double[] angles, int dof, double Size, double ArrowSize, Color color)
    {
    	double thetaop = Math.PI/8.0;	// opening
    	double offset = 0.2;
    	double[][] Coords = new double[7][3];
    	int[] xCoords = new int[Coords.length], yCoords = new int[Coords.length];
     	double[] canvasAngles = new double[3];
     	if (dof == 3)
     	{
     		canvasAngles = new double[] {angles[0], angles[1] - Math.PI/2.0, angles[2]};
     	}
     	else if (dof == 4)
     	{
     		canvasAngles = new double[] {angles[0], angles[1], angles[2] - Math.PI/2.0};
     	}
     	else if (dof == 5)
     	{
     		canvasAngles = new double[] {angles[0], angles[1], angles[2]};
     	}

    	Coords[0] = new double[] {Pos[0] - Size, Pos[1], Pos[2]};
    	Coords[1] = new double[] {Pos[0], Pos[1], Pos[2]};
    	Coords[2] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1] - ArrowSize*Math.sin(thetaop), Pos[2]};
    	Coords[3] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1] + ArrowSize*Math.sin(thetaop), Pos[2]};
    	Coords[4] = new double[] {Pos[0] - offset*Size, Pos[1], Pos[2]};
    	Coords[5] = new double[] {Pos[0] - offset*Size - ArrowSize*Math.cos(thetaop), Pos[1] - ArrowSize*Math.sin(thetaop), Pos[2]};
    	Coords[6] = new double[] {Pos[0] - offset*Size - ArrowSize*Math.cos(thetaop), Pos[1] + ArrowSize*Math.sin(thetaop), Pos[2]};
    	for (int c = 0; c <= Coords.length - 1; c += 1)
    	{
         	Coords[c] = Util.RotateCoord(Coords[c], new double[] {Pos[0], Pos[1], Pos[2]}, canvasAngles);
         	xCoords[c] = (int) Coords[c][0];
         	yCoords[c] = (int) Coords[c][1];
    	}   	
     	DrawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[4], xCoords[5]}, new int[] {yCoords[4], yCoords[5]}, thickness, color);
     	DrawPolyLine(new int[] {xCoords[4], xCoords[6]}, new int[] {yCoords[4], yCoords[6]}, thickness, color);
     	
     	int[] Center = new int[] {Pos[0], Pos[1], Pos[2]};
    	int r = 20;
    	int asize = r/2;
    	double arcanglei = 80*Math.PI/180.0, arcanglef = 280*Math.PI/180.0;
     	double[] TriPos = new double[] {Center[0] + r*Math.cos(arcanglei) + asize*Math.sin(arcanglei), Center[1] - r*Math.sin(arcanglei) + asize*Math.cos(arcanglei), Center[2]};    	
     	TriPos = Util.RotateCoord(TriPos, new double[] {Center[0], Center[1], Center[2]}, canvasAngles);
     	DrawArc3D(Center, r, r, new double[] {arcanglei, arcanglef}, canvasAngles, "degree", color);
     	DrawTriangle3D(new int[] {(int) TriPos[0], (int) TriPos[1], (int) TriPos[2]}, asize, thickness, Math.PI/2.0 - arcanglei, canvasAngles, true, asize, color);
    }
    public void DrawPL3D(double[] RealPos, double size, int thickness, double[] CanvasAngles, int dof, Color color)
    {
    	if (dof == 0)		// Fx
    	{
			double[] angle = new double[] {0, 0, 0};
			DrawArrow3Dto(RealPos, thickness, angle, size, size / 4.0, color);
    	}
    	else if (dof == 1)	// Fy
    	{
			double[] angle = new double[] {0, 0, 0 - Math.PI/2.0};
			DrawArrow3Dto(RealPos, thickness, angle, size, size / 4.0, color);	
    	}
    	else if (dof == 2)	// Fz
    	{
			double[] angle = new double[] {0, 0 + Math.PI/2.0, 0};
			DrawArrow3Dto(RealPos, thickness, angle, size, size / 4.0, color);
    	}
    }
       
    // Composed functions
    public void DrawClock(int[] Pos, int[] size, int time)
    {
    	// Tempo em segundos
    	int FontSize = Math.max(size[0], size[1]) / 10;
    	String Text;
    	if (time < 60)
    	{
    		Text = String.valueOf(time);
    	}
    	else if (time < 3600)
    	{
    		Text = String.valueOf(time / 60) + ":" + String.valueOf(time % 60);
    	}
    	else
    	{
    		Text = String.valueOf(time / 3600) + ":" + String.valueOf((time % 3600) / 60) + ":" + String.valueOf(time % 60);
    	}
    	DrawText(Pos, Text, "Center", 0, "Bold", FontSize, Color.blue);
    }
    public void DrawPoints(int[][] Pos, int size, boolean fill, Color ContourColor, Color FillColor)
    {
    	for (int i = 0; i <= Pos.length - 1; i += 1)
    	{
    		DrawPoint(Pos[i], size, fill, ContourColor, FillColor);
    	}
    }
    public void DrawBase(int[] Pos, int thickness, double angle, int size, Color color)
    {
    	int[][] PosInit = new int[][] {{(int) (Pos[0] - 0.5*size), Pos[1]}};
    	int[][] PosFinal = new int[][] {{(int) (Pos[0] + 0.5*size), Pos[1]}};
    	PosInit = Util.Rotation2D(Pos, PosInit, angle);
    	PosFinal = Util.Rotation2D(Pos, PosFinal, angle);
    	DrawLine(PosInit[0], PosFinal[0], thickness, color);
    	for (int i = 0; i <= 10; i += 1)
    	{
        	DrawLine(new int[] {(int) (PosInit[0][0] + size*i/10*Math.cos(angle)), (int) (PosInit[0][1] - size*i/10*Math.sin(angle))}, new int[] {(int) (PosInit[0][0] - 0.12*size + size*i/10*Math.cos(angle)), (int) (PosInit[0][1] + 0.12*size - size*i/10*Math.sin(angle))}, thickness, color);
 		}    	
    }
    public void DrawBase3D(int[] Pos, int thickness, double[] angles, int size, Color color)
    {
    	int[][] Points = new int[][] {{Pos[0] - 2*size, Pos[1] - 5*size/4, Pos[2]}, {Pos[0] - 2*size, Pos[1] + 5*size/4, Pos[2]}};
    	int NHair = 6;
    	double HairInclination = 0.12;
    	Points[0] = Util.RotateCoord(Points[0], Pos, angles);
    	Points[1] = Util.RotateCoord(Points[1], Pos, angles);
    	DrawLine(Points[0], Points[1], thickness, color);
    	for (int i = 0; i <= NHair - 1; i += 1)
    	{
    		double inc = i/(double)(NHair - 1);		// From 0 to 1
    		int[] sizes = new int[] {Points[1][0] - Points[0][0], Points[1][1] - Points[0][1], Points[1][2] - Points[0][2]};
    		int[] LineInitPos = new int[] {(int) (Points[0][0] + inc*sizes[0]*Math.cos(angles[0])), (int) (Points[0][1] + inc*sizes[1]*Math.cos(angles[1])), (int) (Points[0][2] + inc*sizes[2]*Math.cos(angles[2]))};
    		int[] LineFinalPos = new int[] {(int) (Points[0][0] + inc*sizes[0]*Math.cos(angles[0]) - HairInclination*size), (int) (Points[0][1] + inc*sizes[1]*Math.cos(angles[1]) + HairInclination*size), (int) (Points[0][2] + inc*sizes[2]*Math.cos(angles[2]))};
        	DrawLine(LineInitPos, LineFinalPos, thickness, color);
 		}    	
    }
    public void DrawRoller(int[] Pos, int thickness, double angle, int size, Color color)
    {
    	DrawCircle(new int[] {(int) (Pos[0] + size/2*Math.sin(angle)), (int) (Pos[1] + size/2*Math.cos(angle))}, size, thickness, true, true, color, color);
    	DrawBase(new int[] {(int) (Pos[0] + size*Math.sin(angle)), (int) (Pos[1] + size*Math.cos(angle))}, thickness, angle, (int) (1.8*size), color);
    }
    public void DrawRoller3D(int[] Pos, int thickness, double[] angles, int size, Color color)
    {
    	DrawCircle3D(new int[] {Pos[0], Pos[1], 0}, size, thickness, angles, color);
    	DrawBase3D(new int[] {Pos[0], Pos[1], 0}, thickness, angles, size, color);
    }
    public void DrawPin(int[] Pos, int thickness, double angle, int size, Color color)
    {
    	DrawPolygon(new int[] {Pos[0] - size/2, Pos[0] + size/2,  Pos[0]}, new int[] {Pos[1] + size, Pos[1] + size, Pos[1]}, thickness, true, true, color, color);  
    	DrawBase(new int[] {Pos[0], Pos[1] + size}, thickness, angle, (int) (1.8*size), color);
    }
    public void DrawCantilever(int[] Pos, int thickness, double angle, int size, Color color)
    {
    	DrawBase(Pos, thickness, angle, (int) (1.8*size), color);
    }

    // Visual functions
    public void DrawWindow(int[] Pos, int L, int H, int boardthick, Color FillColor, Color ContourColor)
	{
		/*Color colorbot = new Color (Math.min(colortop.getRed() + 100, 255), Math.min(colortop.getGreen() + 100, 255), colortop.getBlue());
		Color[] ColorGradient = new Color[] {colortop, colorbot};
		DrawRoundRect(Pos, "Left", L, H, boardthick, 3, 3, ColorGradient, ContourColor, true);*/
		int t = 1;
		int offset = 1;		
		Color Light = Util.AddColor(FillColor, new double[] {50, 50, 50});
		Color Shade = Util.AddColor(FillColor, new double[] {-50, -50, -50});
		DrawRect(Pos, L, H, boardthick, "Left", 0, true, ContourColor, FillColor);
		DrawLine(new int[] {Pos[0] + offset, Pos[1] + offset}, new int[] {Pos[0] + L - offset - t, Pos[1] + offset}, t, Light);
		DrawLine(new int[] {Pos[0] + L - offset - t, Pos[1] + offset}, new int[] {Pos[0] + L - offset - t, Pos[1] + H - offset - t}, t, Light);
		DrawLine(new int[] {Pos[0] + offset, Pos[1] + H - offset - t}, new int[] {Pos[0] + offset, Pos[1] + offset}, t, Shade);
		DrawLine(new int[] {Pos[0] + offset, Pos[1] + H - offset - t}, new int[] {Pos[0] + L - offset - t, Pos[1] + H - offset - t}, t, Shade);
	}
	
	public void DrawMousePos(int[] Pos, double[] MousePos, Color bgcolor, Color contourcolor)
	{
		int[] RectSize = new int[] {40, 18};
		int RectThick = 1;
		int FontSize = 13;
		DrawRoundRect(new int[] {Pos[0] - 20, Pos[1] - RectSize[1] / 2 - 8}, "Left", 200, 24, 1, 5, 5, new Color[] {bgcolor}, contourcolor, true);
		DrawText(new int[] {Pos[0], Pos[1]}, "Mouse Pos:", "Left", 0, "Bold", FontSize, Color.black);
		DrawRect(new int[] {Pos[0] + 80, Pos[1] - RectSize[1] + FontSize / 3}, RectSize[0], RectSize[1], RectThick, "Left", 0, false, Color.black, null);
		DrawText(new int[] {Pos[0] + 85, Pos[1]}, String.valueOf(Util.Round(MousePos[0], 2)), "Left", 0, "Bold", FontSize, Color.black);
		DrawRect(new int[] {Pos[0] + 125, Pos[1] - RectSize[1] + FontSize / 3}, RectSize[0], RectSize[1], RectThick, "Left", 0, false, Color.black, null);
		DrawText(new int[] {Pos[0] + 130, Pos[1]}, String.valueOf(Util.Round(MousePos[1], 2)), "Left", 0, "Bold", FontSize, Color.black);
	}
	
	public void DrawAxis(int[] Pos, int sizex, int sizey, int sizez, double[] CanvasAngles)
	{
    	int thickness = 2;
		DrawAxisArrow3D(new int[] {Pos[0] + sizex, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1], CanvasAngles[2]}, true, sizex, sizex / 40.0, Color.red);
		DrawAxisArrow3D(new int[] {Pos[0] + sizey, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1], CanvasAngles[2] - Math.PI/2.0}, true, sizey, sizey / 40.0, Color.green);
		DrawAxisArrow3D(new int[] {Pos[0] + sizez, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1] - Math.PI/2.0, CanvasAngles[2]}, true, sizez, sizez / 40.0, Color.blue);	// z points outward
	}
	
	public void DrawNodeInfo(Nodes Node, int[] Pos)
	{
		String Title = "Node info";
		int L = 250, H;
		int sx = 20, sy = 20;
		int TitleSize = 20;
		int FontSize = 13;
		String OriginalCoords = "", DeformedCoords = "";
		String ConcLoads = String.valueOf(0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0);
		for (int c = 0; c <= Node.getOriginalCoords().length - 1; c+= 1)
		{
			OriginalCoords += String.valueOf(Util.Round(Node.getOriginalCoords()[c], 2));
			DeformedCoords += String.valueOf(Util.Round(Node.getDisp()[c], 2));
			if (c < Node.getOriginalCoords().length - 1)
			{
				OriginalCoords += ",";
				DeformedCoords += ",";
			}
		}
		if (Node.getConcLoads() != null)
		{
			ConcLoads = "";
			for (int load = 0; load <= Node.getConcLoads().length - 1; load += 1)
			{
				for (int dof = 0; dof <= 6 - 1; dof += 1)
				{
					ConcLoads += String.valueOf(Util.Round(Node.getConcLoads()[load].getLoads()[dof], 2) + ", ");
				}
			}
		}
		String[] Text = new String[] {"Node", "Original pos", "Deslocamentos", "Total cargas conc (Fx Fy Fz Mx My Mz)"};
		String[] Info = new String[] {String.valueOf(Node.getID()), OriginalCoords, DeformedCoords, ConcLoads};
		Color TitleColor = Color.blue, TextColor = Color.blue;
		H = 2*Text.length*sy + 10;
		DrawText(new int[] {Pos[0] + L/2, Pos[1] - TitleSize}, Title, "Center", 0, "Bold", TitleSize, TitleColor);
		DrawWindow(Pos, L, H, 3, Color.orange, new Color(180, 0, 100));
		for (int row = 0; row <= Text.length - 1; row += 1)
		{
			DrawText(new int[] {(int) (Pos[0] + sx/2), Pos[1] + sy*(2*row + 1)}, Text[row], "Left", 0, "Bold", FontSize, TextColor);
			DrawText(new int[] {(int) (Pos[0] + sx/2), Pos[1] + sy*(2*row + 2)}, Info[row], "Left", 0, "Bold", FontSize, TextColor);
		}
	}
	
	public void DrawElemInfo(Element Elem, int[] Pos)
	{
		String Title = "Elem info";
		int L = 200, H = 10;
		int sx = 20, sy = 20;
		int TitleSize = 20;
		int FontSize = 13;
		String ElemNodes = "";
		for (int node = 0; node <= Elem.getExternalNodes().length - 1; node += 1)
		{
			ElemNodes += String.valueOf(Elem.getExternalNodes()[node] + " ");
		}
		String[] Text = new String[] {"Elem ", "Nodes = ", "Mat = ", "Sec = "};
		String[] Info = new String[] {String.valueOf(Elem.getID()), ElemNodes, String.valueOf(Elem.getMat()), String.valueOf(Elem.getSec())};
		Color TitleColor = Color.blue, TextColor = Color.blue;
		H = Text.length*sy + 10;
		DrawText(new int[] {Pos[0] + L/2, Pos[1] - TitleSize}, Title, "Center", 0, "Bold", TitleSize, TitleColor);
		DrawWindow(Pos, L, H, 3, Color.orange, new Color(180, 0, 100));
		for (int row = 0; row <= Text.length - 1; row += 1)
		{
			DrawText(new int[] {(int) (Pos[0] + sx/2), Pos[1] + sy + row*sy}, Text[row] + " " + Info[row], "Left", 0, "Bold", FontSize, TextColor);
		}
	}
	
	public void DrawList(int[] Pos, int[] Size, int SelectedItem, String[] PropName, String Title, String ItemName, int[][] Input)
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

	public void DrawList(int[] Pos, int[] Size, int SelectedItem, String[] PropName, String Title, String ItemName, double[][] Input)
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
	
	public void DrawLegend(int[] Pos, String ColorSystem, String Title, int[] Size, double MinValue, double MaxValue, double unitfactor)
	{
		double sx, sy;
		int BarLength;
		int TitleSize = Math.max(Size[0], Size[1]) / 16;
		int FontSize = Math.max(Size[0], Size[1]) / 18;
		int NumCat = 10, NumLines, NumColumns;
		NumLines = 2;
		NumColumns = (1 + NumCat) / NumLines;
		BarLength = (Size[0] / NumColumns)/2;
		sx = BarLength;
		sy = Size[1] / (double)(NumLines);
		DrawText(new int[] {Pos[0] + Size[0] / 2, (int) (Pos[1] - 1.3 * FontSize)}, Title, "Center", 0, "Bold", TitleSize, Color.magenta);
		DrawWindow(Pos, Size[0], Size[1], 1, Color.white, Color.blue);
		for (int i = 0; i <= NumCat - 1; i += 1)
		{
			double value = (MaxValue - MinValue)*i/(NumCat - 1) + MinValue;
			Color color = Util.FindColor(value, MinValue, MaxValue, ColorSystem);
			int[] InitPos = new int[] {(int) (Pos[0] + 2*(i % NumColumns)*sx + sx/2), (int) (Pos[1] + (i / NumColumns) * sy + sy / 4)};
			DrawLine(InitPos, new int[] {InitPos[0] + BarLength, InitPos[1]}, 2, color);
			DrawText(new int[] {InitPos[0] + BarLength/2, (int) (InitPos[1] + FontSize / 2 + FontSize / 4)}, String.valueOf(Util.Round(value / unitfactor, 2)), "Center", 0, "Plain", FontSize, color);
		}
	}
	
	public void DrawCanvas(String Title, double[] PointDist)
	{
		int[] NPoints = new int[] {(int) (canvas.getSize()[0]/PointDist[0]), (int) (canvas.getSize()[1]/PointDist[1])};
		int CanvasThick = 1;
		PointDist[0] = canvas.getDim()[0]/NPoints[0];
		PointDist[1] = canvas.getDim()[1]/NPoints[1];
		if (Title != null)
		{
			int FontSize = 18;
			Color TextColor = Color.blue;
			DrawText(new int[] {canvas.getPos()[0] + canvas.getSize()[0] / 2, canvas.getPos()[1] - FontSize, 0}, Title, "Center", 0, "Bold", FontSize, TextColor);
		}
		DrawRect(canvas.getPos(), canvas.getSize()[0], canvas.getSize()[1], CanvasThick, "Left", 0, false, Color.black, Color.blue);
	}

	public void DrawGrid(int PointSize)
	{
		int[] NPoints = Util.CalculateNumberOfGridPoints(canvas.getDim());
		double[] PointsDist = new double[2];
		PointsDist[0] = canvas.getSize()[0]/(double)(NPoints[0]);
		PointsDist[1] = canvas.getSize()[1]/(double)(NPoints[1]);		
		for (int i = 0; i <= NPoints[0]; i += 1)
		{	
			for (int j = 0; j <= NPoints[1]; j += 1)
			{	
				int[] Pos = new int[] {(int) (canvas.getPos()[0] + i*PointsDist[0]), (int) (canvas.getPos()[1] + j*PointsDist[1])};
				DrawCircle(Pos, PointSize, 1, true, true, Color.black, Color.black);
			}
		}
	}

	public void DrawSteps(int[] Pos, String[] Text, boolean[] StepIsComplete, boolean AnalysisIsReady, ImageIcon OKIcon)
	{
		String Title = "Passo a passo";
		int L = 220, H;
		int sx = 20, sy = 10;
		int TitleSize = 20;
		int FontSize = 13;
		Color TextColor = Color.blue;
		Color TitleColor = Color.blue;
		H = 2*Text.length*sy + sy / 2;
		DrawText(new int[] {Pos[0] + L/2, Pos[1] - TitleSize}, Title, "Center", 0, "Bold", TitleSize, TitleColor);
		DrawWindow(Pos, L, H, 3, Color.green, new Color(180, 0, 100));
		for (int row = 0; row <= Text.length - 1; row += 1)
		{
			DrawText(new int[] {(int) (Pos[0] + sx/2), Pos[1] + sy*(2*row + 2)}, Text[row], "Left", 0, "Bold", FontSize, TextColor);
		}
		for (int step = 0; step <= StepIsComplete.length - 1; step += 1)
		{
			if (StepIsComplete[step])
			{			
				DrawImage(OKIcon.getImage(), new int[] {(int) (Pos[0] + L - sx), (int) (Pos[1] + sy*(2*step + 2.5))}, 0, new float[] {1, 1}, "Center");
			}
		}
		if (AnalysisIsReady)
		{			
			DrawImage(OKIcon.getImage(), new int[] {(int) (Pos[0] + L - sx), (int) (Pos[1] + sy*(2*StepIsComplete.length + 2.5))}, 0, new float[] {1, 1}, "Center");
		}
	}
	
	public void DrawSelectionWindow(int[] InitPos, int[] FinalPos)
	{
		int[] RectPos = new int[] {InitPos[0], InitPos[1]};
		int l = FinalPos[0] - InitPos[0], h = FinalPos[1] - InitPos[1];
		if (InitPos[0] <= FinalPos[0] & InitPos[1] <= FinalPos[1])
		{
			DrawRect(RectPos, l, h, 1, "Left", 0, false, Color.black, null);
		}
	}
		
	public void DrawNodeNumbers(Nodes[] Node, Color NodeColor, boolean deformed)
	{
		int Offset = 6;
		int FontSize = 13;
		for (int node = 0; node <= Node.length - 1; node += 1)
		{
			int[][] DrawingCoords = new int[Node.length][];
			DrawingCoords[node] = Util.ConvertToDrawingCoords2(Util.GetNodePos(Node[node], deformed), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			DrawText(new int[] {(int)(DrawingCoords[node][0] + Offset), (int)(DrawingCoords[node][1] + 1.1*Util.TextHeight(FontSize))}, Integer.toString(node), "Left", 0, "Bold", FontSize, NodeColor);		
		}	
	}
	
	public void DrawElemNumbers(Nodes[] Node, Element[] Elem, Color NodeColor, boolean deformed)
	{
		int FontSize = 13;
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			int[] DrawingCoords = new int[2];
			for (int elemnode = 0; elemnode <= Elem[elem].getExternalNodes().length - 1; elemnode += 1)
			{
				int node = Elem[elem].getExternalNodes()[elemnode];
				DrawingCoords[0] += Util.ConvertToDrawingCoords(Util.GetNodePos(Node[node], deformed), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos())[0];
				DrawingCoords[1] += Util.ConvertToDrawingCoords(Util.GetNodePos(Node[node], deformed), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos())[1];
			}
			DrawingCoords[0] = DrawingCoords[0] / Elem[elem].getExternalNodes().length;
			DrawingCoords[1] = DrawingCoords[1] / Elem[elem].getExternalNodes().length;
			DrawText(DrawingCoords, Integer.toString(elem), "Center", 0, "Bold", FontSize, NodeColor);		
		}	
	}

	public void DrawDOFNumbers(Nodes[] Node, Color NodeColor, boolean deformed)
	{
		int Offset = 16;
		int FontSize = 13;
		for (int node = 0; node <= Node.length - 1; node += 1)
		{
			int[][] DrawingNodePos = new int[Node.length][];
			DrawingNodePos[node] = Util.ConvertToDrawingCoords2(Util.GetNodePos(Node[node], deformed), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			for (int dof = 0; dof <= Node[node].dofs.length - 1; dof += 1)
			{
				double angle = 2 * Math.PI * dof / Node[node].dofs.length;
				int[] Pos = new int[] {(int)(DrawingNodePos[node][0] + Offset*Math.cos(angle)), (int)(DrawingNodePos[node][1] + Offset*Math.sin(angle) + 1.1*Util.TextHeight(FontSize))};
				DrawText(Pos, Integer.toString(Node[node].dofs[dof]), "Left", 0, "Bold", FontSize, NodeColor);	
			}	
		}	
	}
	
	public void DrawDOFSymbols(Nodes[] Node, Color NodeColor, boolean deformed)
	{
		Color ForceDOFColor = Util.ColorPalette()[8];
		Color MomentDOFColor = Util.ColorPalette()[9];
		Color CrossDerivativeDOFColor = Util.ColorPalette()[11];
		Color ShearRotationDOFColor = Util.ColorPalette()[11];
		int thickness = 2;
		double arrowsize = 0.5;
		for (int node = 0; node <= Node.length - 1; node += 1)
		{
			double[] NodeRealPos = Util.GetNodePos(Node[node], deformed);
			for (int dof = 0; dof <= Node[node].dofs.length - 1; dof += 1)
			{
				if (Node[node].getDOFType()[dof] == 0)
				{
					DrawArrow3Dto(NodeRealPos, thickness, new double[] {0, 0, 0}, arrowsize, 0.3 * arrowsize, ForceDOFColor);
				}
				if (Node[node].getDOFType()[dof] == 1)
				{
					DrawArrow3Dto(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, arrowsize, 0.3 * arrowsize, ForceDOFColor);
				}
				if (Node[node].getDOFType()[dof] == 2)
				{
					DrawArrow3Dto(NodeRealPos, thickness, new double[] {0, Math.PI / 2, 0}, arrowsize, 0.3 * arrowsize, ForceDOFColor);
				}
				if (Node[node].getDOFType()[dof] == 3)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 1.5 * arrowsize, 0.3 * arrowsize, MomentDOFColor);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 1.8 * arrowsize, 0.3 * arrowsize, MomentDOFColor);
				}
				if (Node[node].getDOFType()[dof] == 4)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 1.5 * arrowsize, 0.3 * arrowsize, MomentDOFColor);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 1.8 * arrowsize, 0.3 * arrowsize, MomentDOFColor);
				}
				if (Node[node].getDOFType()[dof] == 5)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, Math.PI / 2, 0}, 1.5 * arrowsize, 0.3 * arrowsize, MomentDOFColor);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, Math.PI / 2, 0}, 1.8 * arrowsize, 0.3 * arrowsize, MomentDOFColor);
				}
				if (Node[node].getDOFType()[dof] == 6)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 4}, 1.5 * arrowsize, 0.3 * arrowsize, CrossDerivativeDOFColor);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 4}, 1.8 * arrowsize, 0.3 * arrowsize, CrossDerivativeDOFColor);
				}
				if (Node[node].getDOFType()[dof] == 7)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 0.7 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, 0}, 1.0 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor);
				}
				if (Node[node].getDOFType()[dof] == 8)
				{
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 0.7 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor);
					DrawArrow3Dfrom(NodeRealPos, thickness, new double[] {0, 0, Math.PI / 2}, 1.0 * arrowsize, 0.3 * arrowsize, ShearRotationDOFColor);
				}
			}	
		}	
	}
	
	public void DrawStructure(String Structshape, double[][] Coords, Color StructureColor)
	{
		int thick = 2;
		if (Structshape.equals("Rectangle"))
		{
			int[] InitPoint = Util.ConvertToDrawingCoords(Coords[0], canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos());
			int[] FinalPoint = Util.ConvertToDrawingCoords(Coords[2], canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos());
			DrawRect(InitPoint, (int) Math.abs(FinalPoint[0] - InitPoint[0]), (int) Math.abs(FinalPoint[1] - InitPoint[1]), thick, "Left", 0, false, StructureColor, StructureColor);
		}
		else if (Structshape.equals("Circle"))
		{
			int NPoints = Coords.length;
			double angle = 2 * Math.PI / NPoints;
			double corda = Util.dist(Coords[0], Coords[1]);
			double r = Math.sqrt(Math.pow(corda / 2.0,  2) + Math.pow(corda / 2.0 / Math.tan(angle / 2.0), 2));
			int[] InitPoint = Util.ConvertToDrawingCoords(new double[] {Coords[0][0] - r, Coords[0][1]}, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos());
			int[] FinalPoint = Util.ConvertToDrawingCoords(Coords[0], canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos());
			DrawCircle(InitPoint, (int)(2*Util.dist(FinalPoint, InitPoint)), thick, true, false, StructureColor, StructureColor);
		}
		else if (Structshape.equals("Polygon"))
		{
			int[] Xcoords = new int[Coords.length];
			int[] Ycoords = new int[Coords.length];
			for (int i = 0; i <= Xcoords.length - 1; i += 1)
			{
				Xcoords[i] = Util.ConvertToDrawingCoords(Coords[0], canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos())[0];
				Ycoords[i] = Util.ConvertToDrawingCoords(Coords[0], canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos())[1];
			}
			DrawPolygon(Xcoords, Ycoords, thick, true, false, StructureColor, StructureColor);
		}
		else
		{
			System.out.println("Structure shape not identified at Visuals -> DrawStructure");
		}
	}

	public void DrawStructureContour3D(double[][] coords, Color structureColor)
	{
		int thick = 2;
		int[] Xcoords = new int[coords.length];
		int[] Ycoords = new int[coords.length];
		for (int c = 0; c <= Xcoords.length - 1; c += 1)
		{
			int[] Coord = Util.ConvertToDrawingCoords2(coords[c], RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			Xcoords[c] = Coord[0];
			Ycoords[c] = Coord[1];
		}
		DrawPolygon(Xcoords, Ycoords, thick, true, true, structureColor, structureColor);
	}
	
	public void DrawElemAddition(double[][] InitCoords, int[] MousePos, int MemberThickness, StructureShape structshape, Color color)
	{
		int[] InitPoint = new int[] {(int) InitCoords[0][0], (int) InitCoords[0][1]};
		int[] mousePos = new int[] {(int) MousePos[0], (int) MousePos[1]};
		if (structshape.equals(StructureShape.rectangular))
		{
			DrawLine(InitPoint, new int[] {mousePos[0], InitPoint[1]}, MemberThickness, color);
			DrawLine(InitPoint, new int[] {InitPoint[0], mousePos[1]}, MemberThickness, color);
			DrawLine(new int[] {mousePos[0], InitPoint[1]}, mousePos, MemberThickness, color);
			DrawLine(new int[] {InitPoint[0], mousePos[1]}, mousePos, MemberThickness, color);
		}
		else if (structshape.equals(StructureShape.circular))
		{
			DrawCircle(InitPoint, (int)(2*Util.dist(mousePos, InitPoint)), MemberThickness, true, true, Color.black, color);
		}
		else if (structshape.equals(StructureShape.polygonal))
		{
			int[] FinalPoint = new int[] {(int) InitCoords[InitCoords.length - 1][0], (int) InitCoords[InitCoords.length - 1][1]};
			int[] xCoords = new int[InitCoords.length], yCoords = new int[InitCoords.length];
			for (int i = 0; i <= InitCoords.length - 1; i += 1)
			{
				xCoords[i] = (int) InitCoords[i][0];
				yCoords[i] = (int) InitCoords[i][1];
			}
			DrawPolyLine(xCoords, yCoords, MemberThickness, color);
			DrawLine(FinalPoint, new int[] {mousePos[0], mousePos[1]}, MemberThickness, color);
		}
		else
		{
			System.out.println("Structure shape not identified at Visuals -> DrawElemAddition");
		}
	}
	
	public void DrawNodes(Nodes[] Node, int[] SelectedNodes, Color NodeColor, boolean deformed)
	{
		int size = 2;
		int thick = 1;
		for (int node = 0; node <= Node.length - 1; node += 1)
		{
			int[][] DrawingCoords = new int[Node.length][];
			DrawingCoords[node] = Util.ConvertToDrawingCoords(Util.GetNodePos(Node[node], deformed), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos());
			DrawCircle(DrawingCoords[node], size, thick, false, true, Color.black, NodeColor);
			if (SelectedNodes != null)
			{
				for (int i = 0; i <= SelectedNodes.length - 1; i += 1)
				{
					if (node == SelectedNodes[i])
					{
						DrawCircle(DrawingCoords[node], size, thick, false, true, Color.black, Color.red);
					}
				}
			}
		}
	}
	
	public void DrawNodes3D(Nodes[] Node, int[] SelectedNodes, Color NodeColor, boolean deformed, int[] DOFsPerNode, double Defscale)
	{
		int size = 6;
		int thick = 1;
		double[] Center = Util.ConvertToRealCoords2(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
		for (int node = 0; node <= Node.length - 1; node += 1)
		{
			int[][] DrawingCoords = new int[Node.length][3];
			if (deformed)
			{
				double[] DeformedCoords = Util.ScaledDefCoords(Node[node].getOriginalCoords(), Node[node].getDisp(), DOFsPerNode, Defscale);
				DrawingCoords[node] = Util.ConvertToDrawingCoords2(Util.RotateCoord(DeformedCoords, Center, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			}
			else
			{
				DrawingCoords[node] = Util.ConvertToDrawingCoords2(Util.RotateCoord(Util.GetNodePos(Node[node], deformed), Center, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			}
			DrawCircle(DrawingCoords[node], size, thick, false, true, Color.black, NodeColor);
			if (SelectedNodes != null)
			{
				for (int i = 0; i <= SelectedNodes.length - 1; i += 1)
				{
					if (node == SelectedNodes[i])
					{
						DrawCircle(DrawingCoords[node], 2*size, thick, false, true, Color.black, Color.red);
					}
				}
			}
		}
	}
	
	public void DrawElements(Element[] Elem, Nodes[] Node, int[] SelectedElems, boolean showcontour, boolean condition)
	{
		int thick = 1;
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			int[] Nodes = Elem[elem].getExternalNodes();
			int[][] DrawingCoord = new int[Nodes.length][2]; 
			int[] xCoords = new int[Nodes.length + 1], yCoords = new int[Nodes.length + 1];
			Color color = new Color(0, 100, 55);
			if (Elem[elem].getMat() != null)
			{
				color = Util.AddColor(color, new double[] {0, -50, 100});
			}
			if (Elem[elem].getSec() != null)
			{
				color = Util.AddColor(color, new double[] {0, -50, 100});
			}
			for (int node = 0; node <= Nodes.length - 1; node += 1)
			{
				DrawingCoord[node] = Util.ConvertToDrawingCoords(Util.GetNodePos(Node[Nodes[node]], condition), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos());
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
	
	public void DrawElements3D(Nodes[] Node, Element[] Elem, int[] SelectedElems, boolean showmatcolor, boolean showseccolor, boolean showcontour, boolean showdeformed, double Defscale)
	{
		int thick = 1;
		double[] RealCanvasCenter = Util.ConvertToRealCoords2(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			int[] Nodes = Elem[elem].getExternalNodes();
			int[] ElemDOFs = Elem[elem].getDOFs();
			int[][] DrawingCoord = new int[Nodes.length][2]; 
			int[] xCoords = new int[Nodes.length + 1], yCoords = new int[Nodes.length + 1];
			Color color = new Color(0, 100, 55);
			if (Elem[elem].getMat() != null)
			{
				color = Util.AddColor(color, new double[] {0, -50, 100});
			}
			if (Elem[elem].getSec() != null)
			{
				color = Util.AddColor(color, new double[] {0, -50, 100});
			}
			if (showmatcolor & Elem[elem].getMat() != null)
			{
				color = Elem[elem].getMatColor();
			}
			if (showseccolor & Elem[elem].getSec() != null)
			{
				color = Elem[elem].getSecColor();
			}
			for (int node = 0; node <= Nodes.length - 1; node += 1)
			{
				if (showdeformed)
				{
					double[] DeformedCoords = Util.ScaledDefCoords(Node[Nodes[node]].getOriginalCoords(), Node[Nodes[node]].getDisp(), ElemDOFs, Defscale);
					DrawingCoord[node] = Util.ConvertToDrawingCoords2(Util.RotateCoord(DeformedCoords, RealCanvasCenter, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
				}
				else
				{
					double[] OriginalCoords = Util.GetNodePos(Node[Nodes[node]], showdeformed);
					DrawingCoord[node] = Util.ConvertToDrawingCoords2(Util.RotateCoord(OriginalCoords, RealCanvasCenter, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
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

	public void DrawSup(Nodes[] Node, Supports[] Sup, Color SupColor)
	{
		int size = 6;
		int thick = 2;
		for (int s = 0; s <= Sup.length - 1; s += 1)
		{
			int node = Sup[s].getNode();
			int[][] Coords = new int[Node.length][];
			int suptype = Util.SupType(Sup[s].getDoFs());
			Coords[node] = Util.ConvertToDrawingCoords(Node[node].getOriginalCoords(), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos());
			if (suptype == 0)
			{
				DrawRoller(Coords[node], thick, -Math.PI/2, size, SupColor);
			}
			else if (suptype == 1)
			{
				DrawRoller(Coords[node], thick, 0, size, SupColor);
			}
			else if (suptype == 3)
			{
				DrawPin(Coords[node], thick, 0, size, SupColor);
			}
			else if (suptype == 4)
			{
				DrawCantilever(Coords[node], thick, -Math.PI/2, size, SupColor);
			}
			else if (suptype == 5)
			{
				DrawCantilever(Coords[node], thick, -Math.PI/2, size, SupColor);
			}
		}
	}

	public void DrawSup3D(Nodes[] Node, Supports[] Sup, Color SupColor)
	{
		int size = 6;
		int thick = 2;
		double[] Center = Util.ConvertToRealCoords2(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
		for (int s = 0; s <= Sup.length - 1; s += 1)
		{
			int node = Sup[s].getNode();
			int[][] Coords = new int[Node.length][];
			int suptype = Util.SupType(Sup[s].getDoFs());
			Coords[node] = Util.ConvertToDrawingCoords2(Util.RotateCoord(Node[node].getOriginalCoords(), Center, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			if (suptype == 0)
			{
				double[] angles = new double[] {-canvas.getAngles()[0], canvas.getAngles()[1], -canvas.getAngles()[2]};
				DrawRoller3D(Coords[node], thick, angles, size, SupColor);
			}
			else if (suptype == 1)
			{
				double[] angles = new double[] {-canvas.getAngles()[0], canvas.getAngles()[1], -canvas.getAngles()[2] - Math.PI/2.0};
				DrawRoller3D(Coords[node], thick, angles, size, SupColor);
			}
			else if (suptype == 2)
			{
				double[] angles = new double[] {-canvas.getAngles()[0], canvas.getAngles()[1] + Math.PI/2.0, -canvas.getAngles()[2]};
				DrawRoller3D(Coords[node], thick, angles, size, SupColor);
			}
			else if (suptype == 3)
			{
				DrawPin(Coords[node], thick, 0, size, SupColor);
			}
			else if (suptype == 4)
			{
				DrawCantilever(Coords[node], thick, -Math.PI/2, size, SupColor);
			}
			else if (suptype == 5)
			{
				DrawCantilever(Coords[node], thick, -Math.PI/2, size, SupColor);
			}
		}
	}
	
	public void DrawConcLoads(Nodes[] Node, ConcLoads[] ConcLoads, int[] DOFsPerNode, boolean ShowValues, Color ConcLoadsColor, boolean condition)
	{
		int MaxArrowSize = 40;
		int thickness = 2;
		double MaxLoad = Util.FindMaxConcLoad(ConcLoads);
		for (int l = 0; l <= ConcLoads.length - 1; l += 1)
		{
			int node = ConcLoads[l].getNode();
			int[][] Coords = new int[Node.length][];
			Coords[node] = Util.ConvertToDrawingCoords(Util.GetNodePos(Node[node], condition), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos());
			if (0 < Math.abs(ConcLoads[l].getLoads()[0]) & Util.ArrayContains(DOFsPerNode, 0))
			{
				int size = (int)(Math.abs(MaxArrowSize*ConcLoads[l].getLoads()[0]/MaxLoad));
				double angle = 0;
				if (ConcLoads[l].getLoads()[0] < 0)
				{
					angle += Math.PI;
				}
				DrawTriangle(Coords[node], size, thickness, angle, true, size/4.0, ConcLoadsColor);
				DrawLine(Coords[node], new int[] {Coords[node][0] - size, Coords[node][1]}, thickness, ConcLoadsColor);
			}
			if (0 < Math.abs(ConcLoads[l].getLoads()[1]) & Util.ArrayContains(DOFsPerNode, 1))
			{
				int size = (int)(Math.abs(MaxArrowSize*ConcLoads[l].getLoads()[1]/MaxLoad));
				double angle = -Math.PI/2.0;
				if (ConcLoads[l].getLoads()[1] < 0)
				{
					angle += Math.PI;
				}
				DrawLine(Coords[node], new int[] {Coords[node][0], Coords[node][1] + size}, thickness, ConcLoadsColor);
				DrawTriangle(Coords[node], size, thickness, angle, true, size/4.0, ConcLoadsColor);
			}
			if (0 < Math.abs(ConcLoads[l].getLoads()[2]) & Util.ArrayContains(DOFsPerNode, 2))
			{
				int size = (int)(Math.abs(MaxArrowSize*ConcLoads[l].getLoads()[2]/MaxLoad));
				double angle = -5*Math.PI/4.0;
				if (ConcLoads[l].getLoads()[2] < 0)
				{
					angle += Math.PI;
				}
				DrawTriangle(Coords[node], size, thickness, angle, true, size/4.0, ConcLoadsColor);
			}
			if (0 < ConcLoads[l].getLoads()[3] & Util.ArrayContains(DOFsPerNode, 3))
			{
			}
			if (0 < ConcLoads[l].getLoads()[4] & Util.ArrayContains(DOFsPerNode, 4))
			{
			}
			if (0 < ConcLoads[l].getLoads()[5] & Util.ArrayContains(DOFsPerNode, 5))
			{
			}
		}
	}
	
	public void DrawConcLoads3D (Nodes[] Node, ConcLoads[] ConcLoads, int[] ElemDOFs, boolean ShowValues, Color ConcLoadsColor, boolean condition, double Defscale)
	{
		int MaxArrowSize = 1;
		int thickness = 2;
		double MaxLoad = Util.FindMaxConcLoad(ConcLoads);
		for (int l = 0; l <= ConcLoads.length - 1; l += 1)
		{
			int node = ConcLoads[l].getNode();
			double[] RealDefCoords = Node[node].getOriginalCoords();
			if (condition)
			{
				RealDefCoords = Util.ScaledDefCoords(Node[node].getOriginalCoords(), Node[node].getDisp(), ElemDOFs, Defscale);
			}
			for (int dof = 0; dof <= ElemDOFs.length - 1; dof += 1)
			{
				if (ElemDOFs[dof] < ConcLoads[l].getLoads().length)
				{
					double LoadIntensity = ConcLoads[l].getLoads()[ElemDOFs[dof]];
					if (0 < Math.abs(LoadIntensity))
					{
						int size = (int)(MaxArrowSize * LoadIntensity / MaxLoad);
						if (ElemDOFs[dof] <= 2)
						{
							DrawPL3D(RealDefCoords, size, thickness, canvas.getAngles(), ElemDOFs[dof], ConcLoadsColor);
						}
						else
						{
							//DrawMoment3D(DrawingDefCoords, thickness, canvas.getAngles(), DOFsPerNode[dof], true, size, size / 4.0, ConcLoadsColor);
						}
					}
					if (ShowValues)
					{
						int[] DrawingDefCoords = Util.ConvertToDrawingCoords2(RealDefCoords, RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
						DrawLoadValues(new int[] {DrawingDefCoords[0], DrawingDefCoords[1], 0}, ElemDOFs, dof, LoadIntensity, ConcLoadsColor);
					}
				}
			}
		}
	}
	
	public void DrawDistLoads3D (Nodes[] Node, Element[] Elem, DistLoads[] DistLoads, boolean ShowValues, Color DistLoadsColor, boolean condition, int[] DOFsPerNode, double Defscale)
	{
		int[] NArrows = new int[] {4, 4};
		int MaxArrowSize = 1;
		int thickness = 2;
		double MaxLoad = Util.FindMaxDistLoad(DistLoads);
		for (int l = 0; l <= DistLoads.length - 1; l += 1)
		{
			int elem = DistLoads[l].getElem();
			int[] nodes = Elem[elem].getExternalNodes();
			double[] RealLeftBotDefCoords = Util.ScaledDefCoords(Node[nodes[3]].getOriginalCoords(), Util.GetNodePos(Node[nodes[3]], condition), DOFsPerNode, Defscale);
			double[] RealRightTopDefCoords = Util.ScaledDefCoords(Node[nodes[1]].getOriginalCoords(), Util.GetNodePos(Node[nodes[1]], condition), DOFsPerNode, Defscale);
			//int[] DrawingLeftBotCoords = Util.ConvertToDrawingCoords2(RealLeftBotDefCoords, RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			//int[] DrawingRightTopCoords = Util.ConvertToDrawingCoords2(RealRightTopDefCoords, RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			if (DistLoads[l].getType() == 0)
			{
				
			}
			if (DistLoads[l].getType() == 4)
			{
				//double LoadIntensity = DistLoads[l].getIntensity();
				for (int i = 0; i <= NArrows[0] - 1; i += 1)
				{
					for (int j = 0; j <= NArrows[1] - 1; j += 1)
					{
						double x = (RealLeftBotDefCoords[0] + (RealRightTopDefCoords[0] - RealLeftBotDefCoords[0])*(i/(double)(NArrows[0] - 1)));
						double y = (RealLeftBotDefCoords[1] + (RealRightTopDefCoords[1] - RealLeftBotDefCoords[1])*(j/(double)(NArrows[1] - 1)));
						double z = RealLeftBotDefCoords[2];
						DrawPL3D(new double[] {x, y, z}, MaxArrowSize*DistLoads[l].getIntensity()/MaxLoad, thickness, canvas.getAngles(), 2, DistLoadsColor);
					}
				}
			}
		}
	}

	public void DrawNodalDisps3D (Nodes[] Node, NodalDisps[] NodalDisps, int[] DOFsPerNode, boolean ShowValues, Color ConcLoadsColor, boolean condition, double Defscale)
	{/*
		int MaxArrowSize = 40;
		//int thickness = 2;
		double MaxLoad = Util.FindMaxNodalDisp(NodalDisps);
		double[] Center = Util.ConvertToRealCoords2(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
		for (int l = 0; l <= NodalDisps.length - 1; l += 1)
		{
			int node = NodalDisps[l].getNode();
			double[] RealDefCoords = Util.RotateCoord(Util.ScaledDefCoords(Node[node].getOriginalCoords(), Util.GetNodePos(Node[node], condition), DOFsPerNode, Defscale), Center, canvas.getAngles());
			//int[] DrawingDefCoords = Util.ConvertToDrawingCoords2(RealDefCoords, RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
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
	
	public void DrawReactions3D (Nodes[] Node, Reactions[] Reactions, int[] ElemDOFs, boolean ShowValues, Color ReactionsColor, boolean condition, double Defscale)
	{
		int MaxArrowSize = 1;
		int thickness = 2;
		double MaxAbsLoad = Util.FindMaxReaction(Reactions);
		for (int l = 0; l <= Reactions.length - 1; l += 1)
		{
			int node = Reactions[l].getNode();
			double[] RealDefCoords = Util.ScaledDefCoords(Node[node].getOriginalCoords(), Node[node].getDisp(), ElemDOFs, Defscale);
			for (int r = 0; r <= Reactions[l].getLoads().length - 1; r += 1)
			{
				double LoadIntensity = Reactions[l].getLoads()[r];
				if (0 < Math.abs(LoadIntensity))
				{
					double size = MaxArrowSize * LoadIntensity / (double) MaxAbsLoad;
					if (r <= 2)
					{
						DrawPL3D(RealDefCoords, size, thickness, canvas.getAngles(), r, ReactionsColor);
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
						int[] DrawingDefCoords = Util.ConvertToDrawingCoords2(Util.RotateCoord(RealTextPos, RealStructCenter, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());						
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
	
	public void DrawElemDetails(ElemType elemType)
	{
		RealStructCenter = new double[] {5, 5, 0};
		double[] Center = Util.ConvertToRealCoords2(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
		ElemShape elemShape = Element.typeToShape(elemType);
		Nodes[] Node = null;
		Element Elem = null;
		if (elemShape.equals(ElemShape.rectangular))
		{
			Node = new Nodes[4];
			Node[0] = new Nodes(0, new double[] {1, 1, 0});
			Node[1] = new Nodes(1, new double[] {9, 1, 0});
			Node[2] = new Nodes(2, new double[] {9, 9, 0});
			Node[3] = new Nodes(3, new double[] {1, 9, 0});
			Elem = new Element(0, new int[] {0, 1, 2, 3}, null, null, null, elemType);
		}
		else if (elemShape.equals(ElemShape.quad))
		{
			Node = new Nodes[4];
			Node[0] = new Nodes(0, new double[] {1, 1, 0});
			Node[1] = new Nodes(1, new double[] {9, 3, 0});
			Node[2] = new Nodes(2, new double[] {7, 9, 0});
			Node[3] = new Nodes(3, new double[] {3, 7, 0});
			Elem = new Element(0, new int[] {0, 1, 2, 3}, null, null, null, elemType);
		}
		else if (elemShape.equals(ElemShape.triangular))
		{
			Node = new Nodes[3];
			Node[0] = new Nodes(0, new double[] {1, 1, 0});
			Node[1] = new Nodes(1, new double[] {9, 5, 0});
			Node[2] = new Nodes(2, new double[] {1, 9, 0});
			Elem = new Element(0, new int[] {0, 1, 2}, null, null, null, elemType);
		}
		else if (elemShape.equals(ElemShape.r8))
		{
			Node = new Nodes[8];
			Node[0] = new Nodes(0, new double[] {1, 1, 0});
			Node[1] = new Nodes(1, new double[] {9, 1, 0});
			Node[2] = new Nodes(2, new double[] {9, 9, 0});
			Node[3] = new Nodes(3, new double[] {1, 9, 0});
			Node[4] = new Nodes(3, new double[] {5, 1, 0});
			Node[5] = new Nodes(3, new double[] {5, 9, 0});
			Node[6] = new Nodes(3, new double[] {1, 5, 0});
			Node[7] = new Nodes(3, new double[] {9, 5, 0});
			Elem = new Element(0, new int[] {0, 4, 1, 7, 2, 5, 3, 6}, null, null, null, elemType);
		}
		int[] DrawingStructCenter = Util.ConvertToDrawingCoords2(Util.RotateCoord(RealStructCenter, Center, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
		int textSize = 16;
		Color textColor = Util.ColorPalette()[8];
		
		for (int node = 0; node <= Node.length - 1; node += 1)
		{
			Node[node].setDOFType(Elem.getDOFsPerNode()[node]);
		}
		Elem.setCumDOFs(Util.CumDOFsOnElem(Node, Elem.getExternalNodes().length));
		for (int node = 0; node <= Node.length - 1; node += 1)
		{
			Node[node].dofs = new int[Elem.getDOFsPerNode()[node].length];
			for (int dof = 0; dof <= Elem.getDOFsPerNode()[node].length - 1; dof += 1)
			{
				Node[node].dofs[dof] = Elem.getCumDOFs()[node] + dof;
			}
		}
		DrawNodes3D(Node, null, Nodes.color, false, Node[0].getDOFType(), 1);
		DrawElements3D(Node, new Element[] {Elem}, null, false, false, true, false, 1);
		DrawDOFNumbers(Node, Nodes.color, false);
		DrawDOFSymbols(Node, Nodes.color, false);
		DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1] - (int) (1 * 1.5 * textSize)}, elemType.toString(), "Center", 0, "Bold", textSize, textColor);
		DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1]}, "Graus de liberdade: " + Arrays.toString(Elem.getDOFs()), "Center", 0, "Bold", textSize, textColor);
		DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1] + (int) (1 * 1.5 * textSize)}, "Deformaçõs: " + Arrays.toString(Elem.getStrainTypes()), "Center", 0, "Bold", textSize, textColor);
	}
	
	public void DrawGrid(int[] InitPos, int[] FinalPos, int NumSpacing, Color color)
	{
		int LineThickness = 1;
		int[] Length = new int[] {FinalPos[0] - InitPos[0], InitPos[1] - FinalPos[1]};
		for (int i = 0; i <= NumSpacing - 1; i += 1)
		{
			DrawLine(new int[] {InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1]}, new int[] {InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1] - Length[1]}, LineThickness, color);						
			DrawLine(new int[] {InitPos[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing}, new int[] {InitPos[0] + Length[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing}, LineThickness, color);						
		}
	}
	
    public void DrawGraph(int[] Pos, int size, int type, Color TitleColor, Color GridColor)
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
		if (XValues != null & YValues != null)
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

	public void DrawContours(Element[] Elem, Nodes[] Node, int[] SelectedElems, boolean showcontour, boolean condition, double[] values, double minvalue, double maxvalue, String ColorSystem)
	{
		int thick = 1;
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			int[] Nodes = Elem[elem].getExternalNodes();
			int[][] DrawingCoord = new int[Nodes.length][2]; 
			int[] xCoords = new int[Nodes.length + 1], yCoords = new int[Nodes.length + 1];
			Color color = new Color(0, 100, 55);
			color = Util.FindColor(values[elem], minvalue, maxvalue, ColorSystem);
			for (int node = 0; node <= Nodes.length - 1; node += 1)
			{
				DrawingCoord[node] = Util.ConvertToDrawingCoords(Util.GetNodePos(Node[Nodes[node]], condition), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getDrawingPos());
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
	
	public void DrawContours3D(Element[] Elem, Nodes[] Node, int[] SelectedElems, double[] RealStructCenter, boolean showelemcontour, boolean condition, double Defscale, double minvalue, double maxvalue, String ResultType, int selecteddof, boolean NonlinearMat, boolean NonlinearGeo, String ColorSystem)
	{
		int Ninterpoints = 0;
		for (int elem = 0; elem <= Elem.length - 1; elem += 1)
		{
			/* Get edge nodes and coordinates*/
			int[] EdgeNodes = Elem[elem].getExternalNodes();
			double[][] EdgeCoords = new double[EdgeNodes.length][3];
			for (int node = 0; node <= EdgeNodes.length - 1; node += 1)
			{
				if (condition)
				{
					EdgeCoords[node] = Util.ScaledDefCoords(Node[EdgeNodes[node]].getOriginalCoords(), Node[EdgeNodes[node]].getDisp(), Node[node].getDOFType(), Defscale);
				}
				else
				{
					EdgeCoords[node] = Util.GetNodePos(Node[EdgeNodes[node]], condition);
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
			if (Elem[elem].getShape().equals("Rectangular") | Elem[elem].getShape().equals("R8"))
			{
				double L = 2 * Elem[elem].calcHalfSize(Node)[0];
				double H = 2 * Elem[elem].calcHalfSize(Node)[1];
				double[] CenterCoords = Elem[elem].getCenterCoords();
				for (int point = 0; point <= ContourCoords.length - 1; point += 1)
				{
					double[] natCoords = Util.InNaturalCoordsRect(CenterCoords, L, H, ContourCoords[point]);
					double e = natCoords[0];
					double n = natCoords[1];
					
					if (-1 < selecteddof)
					{
						if (ResultType.equals("Displacement"))
						{
							double[] disp = Elem[elem].getDisp();
							ContourValue[point] = Analysis.DispOnPoint(Node, Elem[elem], e, n, selecteddof, disp);
						}
						else if (ResultType.equals("Strain"))
						{
							double[] strain = Elem[elem].getStrain();
							ContourValue[point] = Analysis.StrainOnElemContour(Node, Elem[elem], e, n, selecteddof, strain);
						}
						else if (ResultType.equals("Stress"))
						{
							double[] stress = Elem[elem].getStress();
							ContourValue[point] = Analysis.StressOnElemContour(Node, Elem[elem], e, n, selecteddof, stress);
						}
						else if (ResultType.equals("Force"))
						{
							double[] force = Elem[elem].getIntForces();
							ContourValue[point] = Analysis.ForceOnElemContour(Node, Elem[elem], e, n, selecteddof, force);
						}
					}
					ContourCoords[point][2] = ContourValue[point] * Defscale;
				}
			}
			else if (Elem[elem].getShape().equals("Triangular"))
			{			
				for (int point = 0; point <= ContourCoords.length - 1; point += 1)
				{
					double[] natCoords = Util.InNaturalCoordsTriangle(EdgeCoords, ContourCoords[point]);
					double[] u = Elem[elem].getDisp();
					ContourValue[point] = Analysis.DispOnPoint(Node, Elem[elem], natCoords[0], natCoords[1], selecteddof, u);
				}
			}

			/* Draw the contour */
			int[][] DrawingCoords = new int[ContourCoords.length][3];
			int[] xCoords = new int[ContourCoords.length], yCoords = new int[ContourCoords.length];
			double[] Center = Util.ConvertToRealCoords2(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			int thick = 1;
			Color[] colors = new Color[ContourCoords.length];
			Arrays.fill(colors, new Color(0, 100, 55));
			for (int point = 0; point <= ContourCoords.length - 1; point += 1)
			{
				DrawingCoords[point] = Util.ConvertToDrawingCoords2(Util.RotateCoord(ContourCoords[point], Center, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
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
