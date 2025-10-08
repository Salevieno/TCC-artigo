package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.example.Main;
import org.example.loading.DOF;
import org.example.main.MainPanel;
import org.example.structure.Node;
import org.example.structure.Structure;
import org.example.userInterface.MenuBar;

import charts.Chart;
import charts.ChartType;
import charts.Dataset;
import graphics.DrawPrimitives;

public class DiagramsPanel extends JPanel
{
	
	private int selectedDiagram = - 1 ;
	private int selectedDOF = -1 ;
	private Chart chart = new Chart(new Point(0, 0), ChartType.point, "", 0) ;
	private Dataset data = new Dataset() ;
	private List<Dataset> datasets = new ArrayList<>() ;
    // private static final long serialVersionUID = 1L;
    
    private static final Dimension initialSize = new Dimension(0, 100) ;
	private static final String title = "Curva carga deslocamento" ;
	private static final Color gridColor = new Color(Main.palette[10].getRed(), Main.palette[10].getGreen(), Main.palette[10].getBlue(), 50) ;

    public DiagramsPanel()
    {
        this.setSize(initialSize);
        this.setBackground(Main.palette[3]);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), title, TitledBorder.CENTER, TitledBorder.CENTER));

		// chart.setTitle("load") ;
		chart.setGridColor(gridColor);
		chart.setTitleColor(Main.palette[10]) ;
		chart.setLineColor(Main.palette[10]) ;
		chart.setDataSetColor(List.of(Main.palette[0])) ;

    }

    // private static void DrawArrow2D(int[] Pos, int thickness, double[] theta, double Size, double ArrowSize, String dir, Color color, DrawPrimitives DP)
    // {
    // 	int[] PointPos = Arrays.copyOf(Pos, Pos.length);
    // 	double thetaop = Math.PI/8.0;	// opening
    // 	double[][] Coords = new double[6][2];
    // 	int[] xCoords = new int[Coords.length], yCoords = new int[Coords.length];
    // 	if (dir.equals("FromPoint"))
    // 	{
    // 		PointPos[0] += Size;
    // 	}
    // 	Coords[0][0] = PointPos[0] - Size;
    // 	Coords[0][1] = PointPos[1];
    // 	Coords[1][0] = PointPos[0];
    // 	Coords[1][1] = PointPos[1];
    // 	Coords[2][0] = (int)(PointPos[0] - ArrowSize*Math.cos(thetaop));
    // 	Coords[2][1] = (int)(PointPos[1] - ArrowSize*Math.sin(thetaop));
    // 	Coords[3][0] = (int)(PointPos[0] - ArrowSize*Math.cos(thetaop));
    // 	Coords[3][1] = (int)(PointPos[1] + ArrowSize*Math.sin(thetaop));
    // 	for (int c = 0; c <= Coords.length - 1; c += 1)
    // 	{
    //      	Coords[c] = Util.RotateCoord(Coords[c], new double[] {PointPos[0] - Size, PointPos[1], 0}, theta);
    //      	xCoords[c] = (int) Coords[c][0];
    //      	yCoords[c] = (int) Coords[c][1];
    // 	}
    //  	// DrawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, thickness, color);
    //  	// DrawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, thickness, color);
    //  	// DrawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, thickness, color);
	// 	DP.drawPolyLine(new int[] {xCoords[0], xCoords[1]}, new int[] {yCoords[0], yCoords[1]}, color);
	// 	DP.drawPolyLine(new int[] {xCoords[1], xCoords[2]}, new int[] {yCoords[1], yCoords[2]}, color);
	// 	DP.drawPolyLine(new int[] {xCoords[1], xCoords[3]}, new int[] {yCoords[1], yCoords[3]}, color);
    // }
	    
	public void updateData(List<Double> xData, List<Double> yData)
	{
		for (int i = 0 ; i <= xData.size() ; i += 1)
		{
			this.data.addPoint(xData.get(i), yData.get(i)) ;
		}
		datasets = List.of(data) ;
		chart.setData(datasets) ;
	}

	public void updateData(List<Double> yData)
	{
		this.data.addYData(yData) ;
		datasets = List.of(data) ;
		chart.setData(datasets) ;
		chart.setMaxX(yData.size()) ;
		chart.setMaxY(yData.stream().map(i -> i).max(Double::compare).get()) ;
	}

	public void updateData()
	{
		// List<Double> yData = new ArrayList<>() ;
		// for (double u : MainPanel.structure.getU())
		// {
		// 	yData.add(u) ;
		// }
		// updateData(yData) ;
		// Node firstNode = MainPanel.structure.getMesh().getNodes().get(25) ;
		// if (!firstNode.getLoadDisp2().isEmpty())
		// {
		// 	System.out.println(firstNode.getLoadDisp2().get(DOF.z));
		// }

		if (!MainPanel.getInstance().getCentralPanel().getStructure().getMesh().hasNodesSelected()) { return ;}

		Node selectedNode = MainPanel.getInstance().getCentralPanel().getStructure().getMesh().getSelectedNodes().get(0) ;
		Dataset dataset = selectedNode.getLoadDisp2().get(DOF.z) ;
		if (dataset != null && 0 < dataset.size())
		{
			chart.setData(List.of(dataset)) ;
		}
		chart.setMaxX(dataset.getX().stream().map(i -> i).max(Double::compare).get()) ;
		chart.setMaxY(dataset.getY().stream().map(i -> i).max(Double::compare).get()) ;
		System.out.println("\n\n\nmax");
		System.out.println(chart.getMaxX());
		System.out.println(chart.getMaxY());
		// 		Yaxisvalues[i] = structure.getMesh().getNodes().get(nodeID).getDisp().asArray()[dof];
		// 		Yaxisvalues[node] = structure.getMesh().getElements().get(elemID).getStress()[dof];
		
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
	}

	public void display(Structure structure, int SelectedDiagram, boolean AnalysisIsComplete, DrawPrimitives DP)
	{

		if (!AnalysisIsComplete) { return ;}
		if (structure == null) { return ;}
		if (structure.getMesh() == null) { return ;}
		if (!structure.getMesh().hasNodesSelected()) { return ;}
		if (selectedDOF <= -1) { return ;}
		
		Dimension panelSize = getSize() ;

		chart.setPos(new Point((int) (0.35 * panelSize.getWidth()), (int) (0.9 * panelSize.getHeight()))) ;
		chart.setSize((int) (0.65 * panelSize.getHeight())) ;

		chart.display(DP) ;
	}
	
	
    public void setSelectedDiagram(int selectedDiagram) {this.selectedDiagram = selectedDiagram ;}
	public void setSelectedDOF(int selectedDOF) {this.selectedDOF = selectedDOF ;}

	@Override
    public void paintComponent(Graphics graphs) 
    {
        super.paintComponent(graphs);
		if (MenuBar.getInstance() != null)
		{
			DrawPrimitives DP = MainPanel.getInstance().getCentralPanel().getDP() ;
			DP.setGraphics((Graphics2D) graphs);
			display(MainPanel.getInstance().getCentralPanel().getStructure(), selectedDiagram, MenuBar.getInstance().getMenuAnalysis().isAnalysisIsComplete(), DP) ;
			repaint();
		}
    }
}
