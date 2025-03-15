package org.example.structure;

import org.example.mainTCC.InputFunctions;

public class Material
{
	private double E;
	private double v;
	private double G;	
	
	public Material(double E, double v, double G)
	{
		this.E = E;
		this.v = v;
		this.G = G;
	}
	
	public static Material loadFromJson(String filename)
	{
		return (Material) InputFunctions.loadFromJson(filename, Material.class) ;
	}
	
	public double getE()
	{
		return E;
	}
	public void setE(double e)
	{
		E = e;
	}
	public double getV()
	{
		return v;
	}
	public void setV(double v)
	{
		this.v = v;
	}
	public double getG()
	{
		return G;
	}
	public void setG(double fu)
	{
		this.G = fu;
	}
	
	
}
