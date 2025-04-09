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

	public Point3D(Point2D.Double point)
	{
		this(point.x, point.y, 0.0) ;
	}
	
	public double[] asArray() { return new double[] {x, y, z} ;}
	public Point asPoint() { return new Point((int)x, (int)y) ;}
	public Point2D.Double asDoublePoint() { return new Point2D.Double(x, y) ;}

	public void translateTo(double x, double y, double z)
	{
		this.x = x ;
		this.y = y ;
		this.z = z ;
	}

	public void translate(double dx, double dy, double dz)
	{
		x += dx ;
		y += dy ;
		z += dz ;
	}

	public void add(Point3D delta)
	{
		translate(delta.x, delta.y, delta.z);
	}

	public static Point3D rotate(Point3D point, Point3D refPoint, Point3D angle)
	{

		// Rotaciona o ponto informado (OriCoord) ao redor do ponto de referância (RefPoint)
		Point3D rotatedPoint = new Point3D(point.x, point.y, point.z) ;
		Point3D originalPoint = new Point3D(point.x, point.y, point.z) ;
		// Rotation around z
		rotatedPoint.x = (originalPoint.x - refPoint.x) * Math.cos(angle.z) - (originalPoint.y - refPoint.y) * Math.sin(angle.z);
		rotatedPoint.y = (originalPoint.x - refPoint.x) * Math.sin(angle.z) + (originalPoint.y - refPoint.y) * Math.cos(angle.z);
		originalPoint.x = rotatedPoint.x + refPoint.x;
		originalPoint.y = rotatedPoint.y + refPoint.y;
		// Rotation around y
		rotatedPoint.x = (originalPoint.x - refPoint.x) * Math.cos(angle.y) - (originalPoint.z - refPoint.z) * Math.sin(angle.y);
		rotatedPoint.z = (originalPoint.x - refPoint.x) * Math.sin(angle.y) + (originalPoint.z - refPoint.z) * Math.cos(angle.y);
		originalPoint.x = rotatedPoint.x + refPoint.x;
		originalPoint.z = rotatedPoint.z + refPoint.z;
		// Rotation around x
		rotatedPoint.y = (originalPoint.y - refPoint.y) * Math.cos(angle.x) - (originalPoint.z - refPoint.z) * Math.sin(angle.x);
		rotatedPoint.z = (originalPoint.y - refPoint.y) * Math.sin(angle.x) + (originalPoint.z - refPoint.z) * Math.cos(angle.x);
		originalPoint.y = rotatedPoint.y + refPoint.y;
		originalPoint.z = rotatedPoint.z + refPoint.z;
		
		rotatedPoint.x = originalPoint.x;
		rotatedPoint.y = originalPoint.y;
		rotatedPoint.z = originalPoint.z;

		return rotatedPoint ;
	}

	public void rotate(Point3D refPoint, Point3D angle)
	{
		Point3D rotatedPoint = rotate(this, refPoint, angle) ;
		x = rotatedPoint.x ;
		y = rotatedPoint.y ;
		z = rotatedPoint.z ;
	}

	public static double dist2D(Point3D p1, Point3D p2) { return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)) ;}
	public static double dist3D(Point3D p1, Point3D p2) { return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + Math.pow(p1.z - p2.z, 2));}


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point3D other = (Point3D) obj;
		double tol = Math.pow(10, -8) ;
		if (tol <= Math.abs(x - other.x))
			return false;
		if (tol <= Math.abs(y - other.y))
			return false;
		if (tol <= Math.abs(z - other.z))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")";
	}

}
