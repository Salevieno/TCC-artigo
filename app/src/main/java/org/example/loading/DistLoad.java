package org.example.loading;

import java.awt.Color;
import java.awt.Point;

import org.example.structure.Element;
import org.example.structure.Node;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;

import graphics.DrawPrimitives;

public class DistLoad
{
	private int id;
	private int Type;			// Type: 0 = Conc load paralel to elem, 1 = Conc load perpendicular to elem, 2 = Conc moment, 3 = UDL paralel to elem, 4 = UDL perpendicular to elem, 5 = triangular
	private double Intensity;

	private static int currentID = 1 ;
	private static int MaxArrowSize = 1;
	private static int stroke = 2;
	private static Point nArrows = new Point(4, 4) ;
	public static Color color = Menus.palette[7];

	public DistLoad(int Type, double Intensity)
	{
		this.id = currentID;
		this.Type = Type;
		this.Intensity = Intensity;
		currentID += 1;
	}

	public int getId() {return id;}
	public int getType() {return Type;}
	public double getIntensity() {return Intensity;}
	public void setId(int I) {id = I;}
	public void setType(int T) {Type = T;}
	public void setIntensity(double I) {Intensity = I;}

	public void display(Element elem, boolean condition, int[] DOFsPerNode, double Defscale, double MaxLoad, MyCanvas canvas, DrawPrimitives DP)
	{
		// double[] RealLeftBotDefCoords = Util.ScaledDefCoords(nodes.get(3).getOriginalCoords().asArray(), Util.GetNodePos(nodes.get(3), condition), DOFsPerNode, Defscale);
		// double[] RealRightTopDefCoords = Util.ScaledDefCoords(nodes.get(1).getOriginalCoords().asArray(), Util.GetNodePos(nodes.get(1), condition), DOFsPerNode, Defscale);
		Node bottomLeftNode = elem.getBottomLeftNode() ;
		Node topRightNode = elem.getTopRightNode() ;
		Point3D botLeftPos = bottomLeftNode.getOriginalCoords() ;
		Point3D topRightPos = topRightNode.getOriginalCoords() ;

		for (int i = 0; i <= nArrows.x - 1; i += 1)
		{
			for (int j = 0; j <= nArrows.y - 1; j += 1)
			{
				double x = (botLeftPos.x + (topRightPos.x - botLeftPos.x)*(i/(double)(nArrows.x - 1)));
				double y = (botLeftPos.y + (topRightPos.y - botLeftPos.y)*(j/(double)(nArrows.y - 1)));
				double z = botLeftPos.z;
				ConcLoad.DrawPL3D(new Point3D(x, y, z), MaxArrowSize*Intensity/MaxLoad, stroke, canvas.getAngles(), 2, color, canvas, DP);
			}
		}
	}

	@Override
	public String toString() {
		return id  + "	" + Type + "	" + Intensity ;
	}

}
