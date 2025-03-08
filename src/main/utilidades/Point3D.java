package main.utilidades;

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
