package org.example.loading;

import java.awt.Color;

import org.example.userInterface.Menus;

public class DistLoad
{
	private int id;
	private int Elem;			// Elem
	private int Type;			// Type: 0 = Conc load paralel to elem, 1 = Conc load perpendicular to elem, 2 = Conc moment, 3 = UDL paralel to elem, 4 = UDL perpendicular to elem, 5 = triangular
	private double Intensity;

	private static int currentID = 1 ;
	public static Color color = Menus.palette[7];

	public DistLoad(int Elem, int Type, double Intensity)
	{
		this.id = currentID;
		this.Elem = Elem;
		this.Type = Type;
		this.Intensity = Intensity;
		currentID += 1;
	}

	public int getId() {return id;}
	public int getElem() {return Elem;}
	public int getType() {return Type;}
	public double getIntensity() {return Intensity;}
	public void setId(int I) {id = I;}
	public void setElem(int E) {Elem = E;}
	public void setType(int T) {Type = T;}
	public void setIntensity(double I) {Intensity = I;}

	@Override
	public String toString() {
		return id + "	" + Elem + "	" + Type + "	" + Intensity ;
	}

}
