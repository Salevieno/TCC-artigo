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

	private static double[][] rotationMatrixX(Point3D angle)
	{
		double cosX = Math.cos(angle.x), sinX = Math.sin(angle.x);
		return new double[][] {
			{1, 0, 0},
			{0, cosX, -sinX},
			{0, sinX, cosX}
		} ;
	}

	private static double[][] rotationMatrixY(Point3D angle)
	{
		double cosY = Math.cos(angle.y), sinY = Math.sin(angle.y);
		return new double[][] {
			{cosY, 0, sinY},
			{0, 1, 0},
			{-sinY, 0, cosY}
		} ;
	}

	private static double[][] rotationMatrixZ(Point3D angle)
	{
		double cosZ = Math.cos(angle.z), sinZ = Math.sin(angle.z);
		return new double[][] {
			{cosZ, -sinZ, 0},
			{sinZ, cosZ, 0},
			{0, 0, 1}
		} ;
	}

	public static Point3D rotate(Point3D point, Point3D refPoint, Point3D angle)
	{
		// Translada o ponto para a origem
		double x = point.x - refPoint.x;
		double y = point.y - refPoint.y;
		double z = point.z - refPoint.z;

		// Pré-calcula senos e cossenos
		double cosX = Math.cos(angle.x), sinX = Math.sin(angle.x);
		double cosY = Math.cos(angle.y), sinY = Math.sin(angle.y);
		double cosZ = Math.cos(angle.z), sinZ = Math.sin(angle.z);

		// Rotação em torno de Z (plano XY)
		double x1 = x * cosZ - y * sinZ;
		double y1 = x * sinZ + y * cosZ;
		double z1 = z;

		// Rotação em torno de Y (plano XZ)
		double x2 = x1 * cosY - z1 * sinY;
		double y2 = y1;
		double z2 = x1 * sinY + z1 * cosY;

		// Rotação em torno de X (plano YZ)
		double x3 = x2;
		double y3 = y2 * cosX - z2 * sinX;
		double z3 = y2 * sinX + z2 * cosX;

		// Translada de volta para o ponto de referência
		return new Point3D(x3 + refPoint.x, y3 + refPoint.y, z3 + refPoint.z);
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
