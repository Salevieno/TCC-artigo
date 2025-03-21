package org.example.utilidades;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Point3D
{
	public double x;
	public double y;
	public double z;
	
	public Point3D(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double[] asArray() { return new double[] {x, y, z} ;}
	public Point asPoint() { return new Point((int)x, (int)y) ;}
	public Point2D.Double asDoublePoint() { return new Point2D.Double(x, y) ;}

	public void translate(double dx, double dy, double dz)
	{
		x += dx ;
		y += dy ;
		z += dz ;
	}

	public static double dist2D(Point3D p1, Point3D p2) { return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)) ;}
	public static double dist3D(Point3D p1, Point3D p2) { return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + Math.pow(p1.z - p2.z, 2));}

	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}

}
