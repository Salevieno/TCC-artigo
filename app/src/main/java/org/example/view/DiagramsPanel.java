package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.example.mainTCC.MenuFunctions;
import org.example.structure.Node;
import org.example.structure.Structure;
import org.example.userInterface.Menus;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;

import charts.Chart;
import charts.Dataset;
import graphics.Align;
import graphics.DrawPrimitives;

public class DiagramsPanel extends JPanel
{
	
	private int selectedDiagram = - 1 ;
	private int selectedDOF = -1 ;
	private Chart chart = new Chart(new Point(0, 0), "", 0) ;
	private Dataset data = new Dataset() ;
	private List<Dataset> datasets = new ArrayList<>() ;
    // private static final long serialVersionUID = 1L;
    
    private static final Dimension initialSize = new Dimension(0, 100) ;
	private static final String title = "Curva carga deslocamento" ;

    public DiagramsPanel()
    {
        this.setSize(initialSize);
        this.setBackground(Menus.palette[3]);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), title, TitledBorder.CENTER, TitledBorder.CENTER));

		// chart.setTitle("load") ;
		chart.setGridColor(new Color(Menus.palette[10].getRed(), Menus.palette[10].getGreen(), Menus.palette[10].getBlue(), 50));
		chart.setTitleColor(Menus.palette[10]) ;
    }

    private static void DrawArrow2D(int[] Pos, int thickness, double[] theta, double Size, double ArrowSize, String dir, Color color, DrawPrimitives DP)
    {
    	int[] PointPos = Arrays.copyOf(Pos, Pos.length);
    	double thetaop = Math.PI/8.0;	// opening
    	double[][] Coords = new double[6][2];
    	int[] xCoords = new int[Coords.length], yCoords = new int[Coords.length];
    	if (dir.equals("FromPoint"))
    	{
    		PointPos[0] += Size;
    	}
    	Coords[0][0] = PointPos[0] - Size;
    	Coords[0][1] = PointPos[1];
    	Coords[1][0] = PointPos[0];
    	Coords[1][1] = PointPos[1];
    	Coords[2][0] = (int)(PointPos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[2][1] = (int)(PointPos[1] - ArrowSize*Math.sin(thetaop));
    	Coords[3][0] = (int)(PointPos[0] - ArrowSize*Math.cos(thetaop));
    	Coords[3][1] = (int)(PointPos[1] + ArrowSize*Math.sin(thetaop));
    	for (int c = 0; c <= Coords.length - 1; c += 1)
    	{
         	Coords[c] = Util.RotateCoord(Coords[c], new double[] {PointPos[0] - Size, PointPos[1], 0}, theta);
         	xCoords[c] = (int) Coords[c][0];
         	yCoords[c] = (int) Coords[c][1];
    	}
     	// DrawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, thickness, color);
     	// DrawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, thickness, color);
     	// DrawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, thickness, color);
		DP.drawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, color);
		DP.drawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, color);
		DP.drawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, color);
    }

	private static void DrawGrid(int[] InitPos, int[] FinalPos, int NumSpacing, Color color, DrawPrimitives DP)
	{
		// int LineThickness = 1;
		int[] Length = new int[] {FinalPos[0] - InitPos[0], InitPos[1] - FinalPos[1]};
		for (int i = 0; i <= NumSpacing - 1; i += 1)
		{
			Point p1 = new Point(InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1]) ;
			Point p2 = new Point(InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1] - Length[1]) ;
			Point p3 = new Point(InitPos[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing) ;
			Point p4 = new Point(InitPos[0] + Length[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing) ;
			DP.drawLine(p1, p2, color) ;
			DP.drawLine(p3, p4, color) ;
			// DrawLine(new int[] {InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1]}, new int[] {InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1] - Length[1]}, LineThickness, color);						
			// DrawLine(new int[] {InitPos[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing}, new int[] {InitPos[0] + Length[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing}, LineThickness, color);						
		
		}
	}
	
    private static void DrawGraph(int[] Pos, int size, int type, Color TitleColor, Color GridColor, DrawPrimitives DP)
	{
		int NumSpacing = 10;
		//DrawLine(Pos, new int[] {Pos[0], (int) (Pos[1] - 1.1*size)}, 1, AxisColor);
		//DrawLine(Pos, new int[] {(int) (Pos[0] + 1.1*size), Pos[1]}, 1, AxisColor);
		//DrawPolyLine(new int[] {Pos[0] - asize, Pos[0], Pos[0] + asize}, new int[] {(int) (Pos[1] - 1.1*size) + asize, (int) (Pos[1] - 1.1*size), (int) (Pos[1] - 1.1*size) + asize}, 1, AxisColor);
		//DrawPolyLine(new int[] {(int) (Pos[0] + 1.1*size - asize), (int) (Pos[0] + 1.1*size), (int) (Pos[0] + 1.1*size - asize)}, new int[] {Pos[1] - asize, Pos[1], Pos[1] + asize}, 1, AxisColor);
		
		// if (type == 0)		// All axis positive
		// {
		// 	DrawArrow2D(Pos, 1, new double[] {0, 0, -Math.PI / 2.0}, 1.1 * size, 1.1 * size / 10.0, "FromPoint", Color.black, DP);
		// 	DrawArrow2D(Pos, 1, new double[] {0, 0, 0}, 1.2 * size, 1.1 * size / 10.0, "FromPoint", Color.black, DP);
		// }
		// else if (type == 1)		// X axis negative and Y axis positive
		// {
		// 	DrawArrow2D(new int[] {Pos[0] + size, Pos[1]}, 1, new double[] {0, 0, -Math.PI / 2.0}, 1.1 * size, 1.1 * size / 10.0, "FromPoint", Color.black, DP);
		// 	DrawArrow2D(new int[] {Pos[0] + size, Pos[1]}, 1, new double[] {0, 0, -Math.PI}, 1.1 * size, 1.1 * size / 10.0, "FromPoint", Color.black, DP);
		// }
		DrawArrow2D(Pos, 1, new double[] {0, 0, -Math.PI / 2.0}, 1.1 * size, 1.1 * size / 10.0, "FromPoint", Color.black, DP);
		DrawArrow2D(Pos, 1, new double[] {0, 0, 0}, 1.2 * size, 1.1 * size / 10.0, "FromPoint", Color.black, DP);
		DrawGrid(Pos, new int[] {Pos[0] + size, Pos[1] - size}, NumSpacing, GridColor, DP);
	}
    
	public void updateData(List<Double> yData)
	{
		this.data.addYData(yData) ;
		datasets = List.of(data) ;
		chart.setData(datasets) ;
	}

    private static void display2DPlot(int[] Pos, int size, String Title, String XaxisName, String YaxisName,
										double[] XValues, double[] YValues, double XMin, double YMin, double XMaxAbs, double YMaxAbs, int xprec, int yprec,
										Color TextColor, Color GridColor, DrawPrimitives DP)
	{
    	// int TextSize = size / 6;
    	int GraphType = 0;
    	if (XMin < 0)
    	{
    		GraphType = 1;
    	}
    	DrawGraph(Pos, size, GraphType, TextColor, GridColor, DP);
		if (XValues != null && YValues != null)
		{
			if (XValues.length == YValues.length)
			{
				if (1 <= XValues.length)
				{
					int[] x = new int[XValues.length], y = new int[YValues.length];
					if (GraphType == 0)
			    	{
						// DrawText(new int[] {(int) (Pos[0] + size), (int) (Pos[1] - 0.1*size)}, XaxisName, "Left", 0, "Bold", TextSize, TextColor);
						// DrawText(new int[] {(int) (Pos[0] + size), (int) (Pos[1] + 0.2*size)}, String.valueOf(Util.Round(XMaxAbs, xprec)), "Center", 0, "Bold", TextSize, TextColor);
						// DrawText(new int[] {(int) (Pos[0]), (int) (Pos[1] + 0.2*size)}, String.valueOf(0), "Center", 0, "Bold", TextSize, TextColor);
						// DrawText(new int[] {(int) (Pos[0] - 0.12*size), (int) (Pos[1] - size)}, YaxisName, "Right", 0, "Bold", TextSize, TextColor);
						// DrawText(new int[] {(int) (Pos[0] - 0.12*size), (int) (Pos[1] - 0.8*size)}, String.valueOf(Util.Round(YMaxAbs, yprec)), "Right", 0, "Bold", TextSize, TextColor);
						DP.drawText(new Point((int) (Pos[0] + size), (int) (Pos[1] - 0.1*size)), Align.topLeft, XaxisName, TextColor) ;
						DP.drawText(new Point((int) (Pos[0] + size), (int) (Pos[1] + 0.2*size)), Align.center, String.valueOf(Util.Round(XMaxAbs, xprec)), TextColor) ;
						DP.drawText(new Point((int) (Pos[0]), (int) (Pos[1] + 0.2*size)), Align.center, "0", TextColor) ;
						DP.drawText(new Point((int) (Pos[0] - 0.12*size), (int) (Pos[1] - size)), Align.centerRight, YaxisName, TextColor) ;
						DP.drawText(new Point((int) (Pos[0] - 0.12*size), (int) (Pos[1] - 0.8*size)), Align.centerRight, String.valueOf(Util.Round(YMaxAbs, yprec)), TextColor) ;
					}
					else if (GraphType == 1)
			    	{
						// DrawText(new int[] {(int) (Pos[0]), (int) (Pos[1] - 0.1*size)}, XaxisName, "Right", 0, "Bold", TextSize, TextColor);
						// DrawText(new int[] {(int) (Pos[0]), (int) (Pos[1] + 0.2*size)}, String.valueOf(Util.Round(XMin, xprec)), "Center", 0, "Bold", TextSize, TextColor);
						// DrawText(new int[] {(int) (Pos[0] + size), (int) (Pos[1] + 0.2*size)}, String.valueOf(0), "Center", 0, "Bold", TextSize, TextColor);
						// DrawText(new int[] {(int) (Pos[0] + size), (int) (Pos[1] - size)}, YaxisName, "Left", 0, "Bold", TextSize, TextColor);
						// DrawText(new int[] {(int) (Pos[0] + size), (int) (Pos[1] - 0.8*size)}, String.valueOf(Util.Round(YMaxAbs, yprec)), "Left", 0, "Bold", TextSize, TextColor);
						DP.drawText(new Point((int) (Pos[0]), (int) (Pos[1] - 0.1*size)), Align.centerRight, XaxisName, TextColor) ;
						DP.drawText(new Point((int) (Pos[0]), (int) (Pos[1] + 0.2*size)), Align.center, String.valueOf(Util.Round(XMin, xprec)), TextColor) ;
						DP.drawText(new Point((int) (Pos[0] + size), (int) (Pos[1] + 0.2*size)), Align.center, "0", TextColor) ;
						DP.drawText(new Point((int) (Pos[0] + size), (int) (Pos[1] - size)), Align.topLeft, YaxisName, TextColor) ;
						DP.drawText(new Point((int) (Pos[0] + size), (int) (Pos[1] - 0.8*size)), Align.topLeft, String.valueOf(Util.Round(YMaxAbs, yprec)), TextColor) ;
					}
						
					for (int j = 0; j <= XValues.length - 1; j += 1)
					{
						x[j] = Pos[0] + (int) (size*(XValues[j] - XMin)/XMaxAbs);
						y[j] = Pos[1] - (int) (size*(YValues[j] - YMin)/YMaxAbs);
					}
					// DrawPolyLine(x, y, 2, TextColor);
					DP.drawPolyLine(x, y, TextColor) ;
				}	
			}
			else
			{
				System.out.println("The size of Xvalues and Yvalues are different at Visuals -> DrawVarGraph");
			}
		}
	}

	private void displayDiagram(int SelectedDiagram, List<Node> selectedNodes)
	{
		// double[] Xaxisvalues = new double[selectedNodes.size()] ;
		// double[] Yaxisvalues = new double[selectedNodes.size()];
		// int dir = -1;
		// int dof = selectedDOF;
		// if (SelectedDiagram == 0)
		// {
		// 	for (int i = 0; i <= selectedNodes.size() - 1; i += 1)
		// 	{
		// 		int nodeID = selectedNodes.get(i).getID();
		// 		Yaxisvalues[i] = structure.getMesh().getNodes().get(nodeID).getDisp().asArray()[dof];
		// 	}
		// }
		// else if (SelectedDiagram == 1)
		// {
		// 	for (int node = 0; node <= selectedNodes.size() - 1; node += 1)
		// 	{
		// 		int elemID = -1;
		// 		for (int i = 0; i <= structure.getMesh().getElements().size() - 1; i += 1)
		// 		{
		// 			if (structure.getMesh().getElements().get(i).getExternalNodes().contains(selectedNodes.get(node)))
		// 			{
		// 				elemID = i;
		// 			}
		// 		}
		// 		Yaxisvalues[node] = structure.getMesh().getElements().get(elemID).getStress()[dof];
		// 	}
		// }
		// else if (SelectedDiagram == 2)
		// {
		// 	for (int node = 0; node <= selectedNodes.size() - 1; node += 1)
		// 	{
		// 		int elemID = -1;
		// 		for (int i = 0; i <= structure.getMesh().getElements().size() - 1; i += 1)
		// 		{
		// 			if (structure.getMesh().getElements().get(i).getExternalNodes().contains(selectedNodes.get(node)))
		// 			{
		// 				elemID = i;
		// 			}
		// 		}
		// 		Yaxisvalues[node] = structure.getMesh().getElements().get(elemID).getStrain()[dof];
		// 	}
		// }
		// else if (SelectedDiagram == 3)
		// {
		// 	for (int node = 0; node <= selectedNodes.size() - 1; node += 1)
		// 	{
		// 		int elemID = -1;
		// 		for (int i = 0; i <= structure.getMesh().getElements().size() - 1; i += 1)
		// 		{
		// 			if (structure.getMesh().getElements().get(i).getExternalNodes().contains(selectedNodes.get(node)))
		// 			{
		// 				elemID = i;
		// 			}
		// 		}
		// 		Yaxisvalues[node] = structure.getMesh().getElements().get(elemID).getIntForces()[dof];
		// 	}
		// }
			
		// Point3D FirstNodePos = selectedNodes.get(0).getOriginalCoords();
		// Point3D FinalNodePos = selectedNodes.get(selectedNodes.size() - 1).getOriginalCoords();
		// dir = (FinalNodePos.y - FirstNodePos.y <= FinalNodePos.x - FirstNodePos.x) ? 0 : 1 ;
		
		// for (int i = 0; i <= selectedNodes.size() - 1; i += 1)
		// {
		// 	int nodeID = selectedNodes.get(i).getID();
		// 	double minCoord = dir == 0 ? Structure.calcMinCoords(structure.getCoords()).x : Structure.calcMinCoords(structure.getCoords()).y ;
		// 	if (dir == 0)
		// 	{
		// 		Xaxisvalues[i] = structure.getMesh().getNodes().get(nodeID).getOriginalCoords().x - minCoord;
		// 	}
		// 	else
		// 	{
		// 		Xaxisvalues[i] = structure.getMesh().getNodes().get(nodeID).getOriginalCoords().y - minCoord;
		// 	}
		// }
		// display2DPlot(CurvePos, Math.min(CurveSize[0], CurveSize[1]), "Resultados na seção", "x var", "y var",
		// 	Xaxisvalues, Yaxisvalues, Util.FindMin(Xaxisvalues), Util.FindMin(Yaxisvalues),
		// 	Util.FindMaxAbs(Xaxisvalues), Util.FindMaxAbs(Yaxisvalues), 2, 2, Menus.palette[5], Menus.palette[10], DP);
	}
	
	public void display(Structure structure, int SelectedDiagram, boolean AnalysisIsComplete, DrawPrimitives DP)
	{

		if (!AnalysisIsComplete) { return ;}
		if (structure == null) { return ;}
		if (structure.getMesh() == null) { return ;}
		if (!structure.getMesh().hasNodesSelected()) { return ;}
		if (selectedDOF <= -1) { return ;}
		
		Dimension panelSize = getSize() ;

		// List<Node> selectedNodes = structure.getMesh().getSelectedNodes() ;

		// int[] CurvePos = new int[] {(int) (0.38 * panelSize.getWidth()), (int) (0.8 * panelSize.getHeight())};
		// int[] CurveSize = new int[] {(int) (0.8 * panelSize.getWidth()), (int) (0.6 * panelSize.getHeight())};

		chart.setPos(new Point((int) (0.35 * panelSize.getWidth()), (int) (0.9 * panelSize.getHeight()))) ;
		chart.setSize((int) (0.65 * panelSize.getHeight())) ;

		// Node selectedNode = structure.getMesh().getNodes().get(0) ;
		// double[] XValues = selectedNode.getLoadDisp()[selectedDOF][0];
		// double[] YValues = selectedNode.getLoadDisp()[selectedDOF][1];

		// if (1 < selectedNodes.size())
		// {
		// 	displayDiagram(SelectedDiagram, selectedNodes) ;
		// }
		// else
		// {
		// 	// Node selectedNode = structure.getMesh().getNodes().get(0) ;

		// 	if (-1 < selectedDOF)
		// 	{
		// 		// double[] XValues = selectedNode.getLoadDisp()[selectedDOF][0];
		// 		// double[] YValues = selectedNode.getLoadDisp()[selectedDOF][1];

		// 	}
		// }

		chart.display(DP) ;

		// display2DPlot(CurvePos, Math.min(CurveSize[0], CurveSize[1]),
		// "Curva carga-deslocamento", "u (mm)", "Fator de carga", XValues, YValues,
		// Util.FindMin(XValues), Util.FindMin(YValues), Util.FindMaxAbs(XValues), Util.FindMaxAbs(YValues), 3, 3, Menus.palette[5], Menus.palette[10], DP);					

	}

	
	
    public void setSelectedDiagram(int selectedDiagram) {this.selectedDiagram = selectedDiagram ;}
	public void setSelectedDOF(int selectedDOF) {this.selectedDOF = selectedDOF ;}

	@Override
    public void paintComponent(Graphics graphs) 
    {
        super.paintComponent(graphs);
		if (Menus.getInstance() != null)
		{
			DrawPrimitives DP = Menus.getInstance().getMainPanel().getDP() ;
			DP.setGraphics((Graphics2D) graphs);
			display(MainPanel.structure, selectedDiagram, MenuFunctions.AnalysisIsComplete, DP) ;
			repaint();
		}
    }
}
