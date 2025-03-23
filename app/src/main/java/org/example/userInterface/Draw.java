package org.example.userInterface;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;

import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.NodalDisp;
import org.example.structure.Element;
import org.example.structure.Mesh;
import org.example.structure.Node;
import org.example.utilidades.MyCanvas;
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
    
	public static void DrawArrow3Dto(double[] Pos, int thickness, double[] theta, double Size, double ArrowSize, Color color, MyCanvas canvas, DrawPrimitives DP)
    {
    	double thetaop = Math.PI / 8.0;	// opening
    	double[][] RealCoords = new double[6][3];
    	// int[][] DrawingCoords = new int[6][3];
    	int[] xCoords = new int[RealCoords.length], yCoords = new int[RealCoords.length];
    	RealCoords[0] = new double[] {Pos[0] - Size, Pos[1], Pos[2]};
    	RealCoords[1] = new double[] {Pos[0], Pos[1], Pos[2]};
    	RealCoords[2] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1] - ArrowSize*Math.sin(thetaop), Pos[2]};
    	RealCoords[3] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1] + ArrowSize*Math.sin(thetaop), Pos[2]};
    	RealCoords[4] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1], Pos[2] - ArrowSize*Math.sin(thetaop)};
    	RealCoords[5] = new double[] {Pos[0] - ArrowSize*Math.cos(thetaop), Pos[1], Pos[2] + ArrowSize*Math.sin(thetaop)};
    	
		// double[] RealCanvasCenter = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
    	// for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	// {
        //  	RealCoords[c] = Util.RotateCoord(RealCoords[c], Pos, theta);
    	// }
    	// for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	// {
        //  	RealCoords[c] = Util.RotateCoord(RealCoords[c], RealCanvasCenter, canvas.getAngles());
    	// }
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
    		// DrawingCoords[c] = Util.ConvertToDrawingCoords2Point3D(RealCoords[c], MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			// DrawingCoords[c] = canvas.inDrawingCoords(new Point2D.Double(RealCoords[c][0], RealCoords[c][1])) ;
		}
    	for (int c = 0; c <= RealCoords.length - 1; c += 1)
    	{
			Point drawingCoods = canvas.inDrawingCoords(new Point2D.Double(RealCoords[c][0], RealCoords[c][1])) ;
         	xCoords[c] = drawingCoods.x;
         	yCoords[c] = drawingCoods.y;
    	}
     	DP.drawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, thickness, color);
     	DP.drawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, thickness, color);
     	DP.drawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, thickness, color);
     	DP.drawPolyLine(new int[] {xCoords[1], xCoords[4]}, new int[] {yCoords[1], yCoords[4]}, thickness, color);
     	DP.drawPolyLine(new int[] {xCoords[1], xCoords[5]}, new int[] {yCoords[1], yCoords[5]}, thickness, color);
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
	
	public static void DrawDistLoads3D (Mesh mesh, List<DistLoad> distLoads, boolean ShowValues, Color DistLoadsColor, boolean condition,
									int[] DOFsPerNode, double Defscale, MyCanvas canvas, DrawPrimitives DP)
	{
		int[] NArrows = new int[] {4, 4};
		int MaxArrowSize = 1;
		int thickness = 2;
		double MaxLoad = Util.FindMaxDistLoad(distLoads);
		List<Node> Node = mesh.getNodes();
		List<Element> Elem = mesh.getElements();
		for (int l = 0; l <= distLoads.size() - 1; l += 1)
		{
			int elem = distLoads.get(l).getElem();
			int[] nodes = Elem.get(elem).getExternalNodes();
			double[] RealLeftBotDefCoords = Util.ScaledDefCoords(Node.get(nodes[3]).getOriginalCoords().asArray(), Util.GetNodePos(Node.get(nodes[3]), condition), DOFsPerNode, Defscale);
			double[] RealRightTopDefCoords = Util.ScaledDefCoords(Node.get(nodes[1]).getOriginalCoords().asArray(), Util.GetNodePos(Node.get(nodes[1]), condition), DOFsPerNode, Defscale);
			//int[] DrawingLeftBotCoords = Util.ConvertToDrawingCoords2Point3D(RealLeftBotDefCoords, MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			//int[] DrawingRightTopCoords = Util.ConvertToDrawingCoords2Point3D(RealRightTopDefCoords, MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDim(), canvas.getCenter(), canvas.getDrawingPos());
			if (distLoads.get(l).getType() == 0)
			{
				
			}
			if (distLoads.get(l).getType() == 4)
			{
				//double LoadIntensity = DistLoads[l].getIntensity();
				for (int i = 0; i <= NArrows[0] - 1; i += 1)
				{
					for (int j = 0; j <= NArrows[1] - 1; j += 1)
					{
						double x = (RealLeftBotDefCoords[0] + (RealRightTopDefCoords[0] - RealLeftBotDefCoords[0])*(i/(double)(NArrows[0] - 1)));
						double y = (RealLeftBotDefCoords[1] + (RealRightTopDefCoords[1] - RealLeftBotDefCoords[1])*(j/(double)(NArrows[1] - 1)));
						double z = RealLeftBotDefCoords[2];
						ConcLoad.DrawPL3D(new double[] {x, y, z}, MaxArrowSize*distLoads.get(l).getIntensity()/MaxLoad, thickness, canvas.getAngles(), 2, DistLoadsColor, canvas, DP);
					}
				}
			}
		}
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
