package org.example.structure;

import java.awt.Color;

import org.example.mainTCC.InputFunctions;
import org.example.utilidades.Util;

public class Material
{
	private double E;
	private double v;
	private double G;

	private transient Color color ;
	
	public Material(double E, double v, double G)
	{
		this.E = E;
		this.v = v;
		this.G = G;

		color = Util.RandomColors(1)[0] ;
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
	
	public Color getColor() { return color ;}
}
