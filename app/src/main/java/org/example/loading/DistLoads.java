package org.example.loading;

import java.awt.Color;

import org.example.userInterface.Menus;

public class DistLoads
{
	private int ID;
	private int Elem;			// Elem
	private int Type;			// Type: 0 = Conc load paralel to elem, 1 = Conc load perpendicular to elem, 2 = Conc moment, 3 = UDL paralel to elem, 4 = UDL perpendicular to elem, 5 = triangular
	private double Intensity;

	public static Color color = Menus.palette[7];
	public DistLoads(int ID, int Elem, int Type, double Intensity)
	{
		this.ID = ID;
		this.Elem = Elem;
		this.Type = Type;
		this.Intensity = Intensity;
	}

	public int getID() {return ID;}
	public int getElem() {return Elem;}
	public int getType() {return Type;}
	public double getIntensity() {return Intensity;}
	public void setID(int I) {ID = I;}
	public void setElem(int E) {Elem = E;}
	public void setType(int T) {Type = T;}
	public void setIntensity(double I) {Intensity = I;}

	@Override
	public String toString() {
		return ID + "	" + Elem + "	" + Type + "	" + Intensity ;
	}

}
