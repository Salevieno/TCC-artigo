package org.example.structure;

import java.awt.Color;

import org.example.utilidades.Util;

public class Section
{
	private double t;
	private transient Color color ;
	
	public Section(double t)
	{
		this.t = t;
		color = Util.RandomColors(1)[0] ;
	}

	public double getT()
	{
		return t;
	}

	public void setT(double t)
	{
		this.t = t;
	}
	
	public Color getColor() { return color ;}
	
	
}
