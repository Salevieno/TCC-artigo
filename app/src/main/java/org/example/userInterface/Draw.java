package org.example.userInterface;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.example.loading.DistLoad;
import org.example.loading.NodalDisp;
import org.example.structure.Element;
import org.example.structure.Mesh;
import org.example.structure.Node;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

import graphics.Align;
import graphics.DrawPrimitives;

public abstract class Draw
{	
	public static int TextHeight(int TextSize)
	{
		return (int)(0.8*TextSize);
	}

	public static void DrawAxisArrow3D(int[] Pos, int thickness, double[] theta, boolean fill, double Size, double ArrowSize, Color color, DrawPrimitives DP)
    {
    	double thetaop = Math.PI/8.0;	// opening
    	double[][] Coords = new double[6][3];
    	int[] xCoords = new int[Coords.length], yCoords = new int[Coords.length];
    	Coords[0][0] = Pos[0] - Size;
    	Coords[0][1] = Pos[1];
    	Coords[0][2] = Pos[2];
    	Coords[1][0] = Pos[0];
    	Coords[1][1] = Pos[1];
    	Coords[1][2] = Pos[2];
    	Coords[2][0] = (int)(Pos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[2][1] = (int)(Pos[1] - ArrowSize*Math.sin(thetaop));
    	Coords[2][2] = Pos[2];
    	Coords[3][0] = (int)(Pos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[3][1] = (int)(Pos[1] + ArrowSize*Math.sin(thetaop));
    	Coords[3][2] = Pos[2];
    	Coords[4][0] = (int)(Pos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[4][1] = Pos[1];
    	Coords[4][2] = (int)(Pos[2] - ArrowSize*Math.sin(thetaop));
    	Coords[5][0] = (int)(Pos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[5][1] = Pos[1];
    	Coords[5][2] = (int)(Pos[2] + ArrowSize*Math.sin(thetaop));
    	for (int c = 0; c <= Coords.length - 1; c += 1)
    	{
         	Coords[c] = Util.RotateCoord(Coords[c], Coords[0], theta);
         	xCoords[c] = (int) Coords[c][0];
         	yCoords[c] = (int) Coords[c][1];
    	}
     	// DrawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, thickness, color);
     	// DrawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, thickness, color);
     	// DrawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, thickness, color);
     	// DrawPolyLine(new int[] {xCoords[1], xCoords[4]}, new int[] {yCoords[1], yCoords[4]}, thickness, color);
     	// DrawPolyLine(new int[] {xCoords[1], xCoords[5]}, new int[] {yCoords[1], yCoords[5]}, thickness, color);
		DP.drawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, color);
		DP.drawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, color);
		DP.drawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, color);
		DP.drawPolyLine(new int[] {xCoords[1], xCoords[4]}, new int[] {yCoords[1], yCoords[4]}, color);
		DP.drawPolyLine(new int[] {xCoords[1], xCoords[5]}, new int[] {yCoords[1], yCoords[5]}, color);
    }

	public static void DrawArrow3Dto(Point3D Pos, Point3D theta, double Size, Color color, MyCanvas canvas, DrawPrimitives DP)
    {
		DrawArrow3Dto(Pos, 2, theta, Size, color, canvas, DP) ;
	}

	public static void DrawArrow3Dto(Point3D Pos, int stroke, Point3D theta, double Size, Color color, MyCanvas canvas, DrawPrimitives DP)
    {
		DrawArrow3Dto(Pos, stroke, theta, Size, Size / 4.0, color, canvas, DP) ;
	}
    
	public static void DrawArrow3Dto(Point3D Pos, int stroke, Point3D theta, double Size, double ArrowSize, Color color, MyCanvas canvas, DrawPrimitives DP)
    {
    	double thetaop = Math.PI / 8.0;	// arrow opening
		List<Point> pos = new ArrayList<>() ;
    	List<Point3D> realCoords = new ArrayList<>();
		Point3D canvasRealCenter = new Point3D(canvas.inRealCoords(new Point(canvas.getCenter()[0], canvas.getCenter()[1])).x, canvas.inRealCoords(new Point(canvas.getCenter()[0], canvas.getCenter()[1])).y, 0.0) ;

    	realCoords.add(new Point3D(Pos.x - Size, Pos.y, Pos.z)) ;
    	realCoords.add(new Point3D(Pos.x, Pos.y, Pos.z)) ;
    	realCoords.add(new Point3D(Pos.x - ArrowSize*Math.cos(thetaop), Pos.y - ArrowSize*Math.sin(thetaop), Pos.z)) ;
    	realCoords.add(new Point3D(Pos.x - ArrowSize*Math.cos(thetaop), Pos.y + ArrowSize*Math.sin(thetaop), Pos.z)) ;
    	realCoords.add(new Point3D(Pos.x - ArrowSize*Math.cos(thetaop), Pos.y, Pos.z - ArrowSize*Math.sin(thetaop))) ;
    	realCoords.add(new Point3D(Pos.x - ArrowSize*Math.cos(thetaop), Pos.y, Pos.z + ArrowSize*Math.sin(thetaop))) ;

		realCoords.forEach(coord -> coord.rotate(Pos, theta)) ;
		realCoords.forEach(coord -> coord.rotate(canvasRealCenter, new Point3D(canvas.getAngles()[0], canvas.getAngles()[1], canvas.getAngles()[2]))) ;
		realCoords.forEach(coord -> pos.add(canvas.inDrawingCoords(coord))) ;

		DP.drawLine(pos.get(0), pos.get(1), stroke, color);
		DP.drawLine(pos.get(1), pos.get(2), stroke, color);
		DP.drawLine(pos.get(1), pos.get(3), stroke, color);
		DP.drawLine(pos.get(1), pos.get(4), stroke, color);
		DP.drawLine(pos.get(1), pos.get(5), stroke, color);
    }
    
	public static void DrawArrow3Dfrom(double[] Pos, int thickness, double[] theta, double Size, double ArrowSize, Color color, MyCanvas canvas, DrawPrimitives DP)
    {
    	double thetaop = Math.PI / 8.0;	// opening
    	double[][] RealCoords = new double[6][3];
    	int[][] DrawingCoords = new int[6][3];
    	int[] xCoords = new int[RealCoords.length], yCoords = new int[RealCoords.length];
    	RealCoords[0] = new double[] {Pos[0], Pos[1], Pos[2]};
    	RealCoords[1] = new double[] {Pos[0] + Size, Pos[1], Pos[2]};
    	RealCoords[2] = new double[] {Pos[0] + Size - ArrowSize*Math.cos(thetaop), Pos[1] - ArrowSize*Math.sin(thetaop), Pos[2]};
    	RealCoords[3] = new double[] {Pos[0] + Size - ArrowSize*Math.cos(thetaop), Pos[1] + ArrowSize*Math.sin(thetaop), Pos[2]};
    	RealCoords[4] = new double[] {Pos[0] + Size - ArrowSize*Math.cos(thetaop), Pos[1], Pos[2] - ArrowSize*Math.sin(thetaop)};
    	RealCoords[5] = new double[] {Pos[0] + Size - ArrowSize*Math.cos(thetaop), Pos[1], Pos[2] + ArrowSize*Math.sin(thetaop)};
    	
		double[] RealCanvasCenter = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
         	RealCoords[c] = Util.RotateCoord(RealCoords[c], Pos, theta);
    	}
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
         	RealCoords[c] = Util.RotateCoord(RealCoords[c], RealCanvasCenter, canvas.getAngles());
    	}
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
    		DrawingCoords[c] = Util.ConvertToDrawingCoords2Point3D(RealCoords[c], MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
    	}
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
         	xCoords[c] = DrawingCoords[c][0];
         	yCoords[c] = DrawingCoords[c][1];
    	}
     	DP.drawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, thickness, color);
     	DP.drawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, thickness, color);
     	DP.drawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, thickness, color);
     	DP.drawPolyLine(new int[] {xCoords[1], xCoords[4]}, new int[] {yCoords[1], yCoords[4]}, thickness, color);
     	DP.drawPolyLine(new int[] {xCoords[1], xCoords[5]}, new int[] {yCoords[1], yCoords[5]}, thickness, color);
    }

	public static void DrawDOFNumbers(List<Node> Node, Color NodeColor, boolean deformed, MyCanvas canvas, DrawPrimitives DP)
	{
		int Offset = 16;
		int FontSize = 13;
		for (int node = 0; node <= Node.size() - 1; node += 1)
		{
			int[][] DrawingNodePos = new int[Node.size()][];
			DrawingNodePos[node] = Util.ConvertToDrawingCoords2Point3D(Util.GetNodePos(Node.get(node), deformed), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			for (int dof = 0; dof <= Node.get(node).dofs.length - 1; dof += 1)
			{
				double angle = 2 * Math.PI * dof / Node.get(node).dofs.length;
				// int[] Pos = new int[] {(int)(DrawingNodePos[node][0] + Offset*Math.cos(angle)), (int)(DrawingNodePos[node][1] + Offset*Math.sin(angle) + 1.1*TextHeight(FontSize))};
				// DrawText(Pos, Integer.toString(Node.get(node).dofs[dof]), "Left", 0, "Bold", FontSize, NodeColor);
				Point pos = new Point((int)(DrawingNodePos[node][0] + Offset*Math.cos(angle)), (int)(DrawingNodePos[node][1] + Offset*Math.sin(angle) + 1.1*TextHeight(FontSize))) ;
				DP.drawText(pos, Align.topLeft, String.valueOf(Node.get(node).dofs[dof]), NodeColor) ;
			}	
		}	
	}
	
	public static void DrawDistLoads3D(Mesh mesh, boolean ShowValues, boolean condition, int[] DOFsPerNode, double Defscale, MyCanvas canvas, DrawPrimitives DP)
	{
		// List<Element> Elem = mesh.getElements();
		List<DistLoad> distLoads = new ArrayList<>() ;
		for (Element elem : mesh.getElements())
		{
			DistLoad[] elemDistLoads = elem.getDistLoads() ;

			if (elemDistLoads == null) { continue ;}
			
			for (DistLoad distLoad : elemDistLoads)
			{
				distLoads.add(distLoad) ;
			}
		}
		double MaxLoad = Util.FindMaxDistLoad(distLoads);

		for (Element elem : mesh.getElements())
		{
			DistLoad[] elemDistLoads = elem.getDistLoads() ;

			if (elemDistLoads == null) { continue ;}

			for (DistLoad distLoad : elemDistLoads)
			{
				if (distLoad.getType() == 0)
				{
					
				}
				if (distLoad.getType() == 4)
				{
					distLoad.display(elem, condition, DOFsPerNode, Defscale, MaxLoad, canvas, DP) ;
				}
			}
		}

		// for (int l = 0; l <= distLoads.size() -  1; l += 1)
		// {
		// 	int elem = distLoads.get(l).getElem();
		// 	List<Node> nodes = Elem.get(elem).getExternalNodes();
		// 	double[] RealLeftBotDefCoords = Util.ScaledDefCoords(nodes.get(3).getOriginalCoords().asArray(), Util.GetNodePos(nodes.get(3), condition), DOFsPerNode, Defscale);
		// 	double[] RealRightTopDefCoords = Util.ScaledDefCoords(nodes.get(1).getOriginalCoords().asArray(), Util.GetNodePos(nodes.get(1), condition), DOFsPerNode, Defscale);
		// 	//int[] DrawingLeftBotCoords = Util.ConvertToDrawingCoords2Point3D(RealLeftBotDefCoords, MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
		// 	//int[] DrawingRightTopCoords = Util.ConvertToDrawingCoords2Point3D(RealRightTopDefCoords, MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
		// 	if (distLoads.get(l).getType() == 0)
		// 	{
				
		// 	}
		// 	if (distLoads.get(l).getType() == 4)
		// 	{
		// 		//double LoadIntensity = DistLoads[l].getIntensity();
		// 		for (int i = 0; i <= NArrows[0] - 1; i += 1)
		// 		{
		// 			for (int j = 0; j <= NArrows[1] - 1; j += 1)
		// 			{
		// 				double x = (RealLeftBotDefCoords[0] + (RealRightTopDefCoords[0] - RealLeftBotDefCoords[0])*(i/(double)(NArrows[0] - 1)));
		// 				double y = (RealLeftBotDefCoords[1] + (RealRightTopDefCoords[1] - RealLeftBotDefCoords[1])*(j/(double)(NArrows[1] - 1)));
		// 				double z = RealLeftBotDefCoords[2];
		// 				ConcLoad.DrawPL3D(new double[] {x, y, z}, MaxArrowSize*distLoads.get(l).getIntensity()/MaxLoad, thickness, canvas.getAngles(), 2, DistLoadsColor, canvas, DP);
		// 			}
		// 		}
		// 	}
		// }
	}

	public static void DrawNodalDisps3D (List<Node> Node, List<NodalDisp> NodalDisps, int[] DOFsPerNode, boolean ShowValues, Color ConcLoadsColor, boolean condition, double Defscale)
	{/*
		int MaxArrowSize = 40;
		//int thickness = 2;
		double MaxLoad = Util.FindMaxNodalDisp(NodalDisps);
		double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
		for (int l = 0; l <= NodalDisps.length - 1; l += 1)
		{
			int node = NodalDisps[l].getNode();
			double[] RealDefCoords = Util.RotateCoord(Util.ScaledDefCoords(Node.get(node).getOriginalCoords(), Util.GetNodePos(Node.get(node), condition), DOFsPerNode, Defscale), Center, canvas.getAngles());
			//int[] DrawingDefCoords = Util.ConvertToDrawingCoords2Point3D(RealDefCoords, MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			for (int dof = 0; dof <= DOFsPerNode.length - 1; dof += 1)
			{
				double LoadIntensity = NodalDisps[l].getDisps()[DOFsPerNode[dof]];
				if (0 < Math.abs(LoadIntensity))
				{
					int size = (int)(MaxArrowSize * LoadIntensity / MaxLoad);
					if (DOFsPerNode[dof] <= 2)
					{
						//DrawPL3D(new int[] {DrawingDefCoords[0], DrawingDefCoords[1], 0}, size, thickness, canvas.getAngles(), DOFsPerNode[dof], ConcLoadsColor);
					}
					else
					{
						//DrawMoment3D(DrawingDefCoords, thickness, canvas.getAngles(), DOFsPerNode[dof], true, size, size / 4.0, ConcLoadsColor);
					}
				}
			}
		}*/
	}
	
	public static void DrawLoadValues(int[] LoadDrawingPos, int[] DOFsPerNode, int dof, double LoadValue, Color color, DrawPrimitives DP)
	{
		// int FontSize = 13;
		// int[] DrawingTextPos = Arrays.copyOf(LoadDrawingPos, LoadDrawingPos.length);
		if (dof <= 2)
		{
			DP.drawText(new Point(LoadDrawingPos[0], LoadDrawingPos[1]), Align.center, String.valueOf(Util.Round(LoadValue, 2)), color) ;
		}
	}
	
	
}
