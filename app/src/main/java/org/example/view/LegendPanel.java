package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.example.Main;
import org.example.mainTCC.MainPanel;
import org.example.output.ColorSystem;
import org.example.structure.Structure;
import org.example.userInterface.MenuBar;
import org.example.utilidades.Util;

import graphics.Align;
import graphics.DrawPrimitives;

public class LegendPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private static final Dimension initialSize = new Dimension(10, 100) ;
	private static final int titleSize = 13;
	private static final int fontSize = 13;
	private Structure structure ;

	private int selectedVar ;
	private boolean showDisplacementContour ; 
	private boolean showStressContour ; 
	private boolean showStrainContour ; 
	private boolean showInternalForces  ;

    public LegendPanel()
    {
        setPreferredSize(initialSize);
        setBackground(Main.palette[3]);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Legenda", TitledBorder.CENTER, TitledBorder.CENTER));
		setVisible(true);
    }

	public void switchDisplay(int selectedDiagram, int selectedVar)
	{
		if (selectedVar <= -1) { return ;}
		
		this.selectedVar = selectedVar ;
		showDisplacementContour = selectedDiagram == 0;
		showStressContour = selectedDiagram == 1;
		showStrainContour = selectedDiagram == 2;
		showInternalForces = selectedDiagram == 3;
	}


	public void DrawLegend(int[] Pos, ColorSystem colorSystem, String title, double MinValue, double MaxValue, double unitfactor, DrawPrimitives DP)
	{

		Dimension panelSize = getSize() ;

		double sx, sy;
		int BarLength;
		int NumCat = 10 ;
		int NumLines = 2;
		int NumColumns = (1 + NumCat) / NumLines;
		BarLength = (panelSize.width / NumColumns)/2;
		sx = BarLength;
		sy = panelSize.height / (double)(NumLines);
		// DP.DrawText(new int[] {}, title, "Center", 0, "Bold", titleSize, Main.palette[6]);
		DP.drawText(new Point(Pos[0] + panelSize.width / 2, (int) (Pos[1])), Align.center, title, Main.palette[6]);

		// DP.DrawWindow(Pos, panelSize.width, panelSize.height, 1, null, Color.blue);
		for (int i = 0; i <= NumCat - 1; i += 1)
		{
			double value = (MaxValue - MinValue)*i/(NumCat - 1) + MinValue;
			Color color = Util.FindColor(value, MinValue, MaxValue, colorSystem);
			Point InitPos = new Point((int) (Pos[0] + 2*(i % NumColumns)*sx + sx/2), (int) (Pos[1] + (i / NumColumns) * sy + sy / 4)) ;
			DP.drawLine(InitPos, new Point(InitPos.x + BarLength, InitPos.y), 2, color);
			// DP.DrawText(new int[] {}, , "Center", 0, "Plain", fontSize, color);
			DP.drawText(new Point(InitPos.x + BarLength/2, (int) (InitPos.y + fontSize / 2 + fontSize / 4)), Align.center, String.valueOf(Util.Round(value / unitfactor, 2)), color);
		}
	}

	public void display(Structure structure, int selectedVar, boolean showDisplacementContour, boolean showStressContour, boolean showStrainContour, boolean showInternalForces, DrawPrimitives DP)
	{
		if (-1 < selectedVar)
		{
			int[] LegendPos = new int[] {(int) (0.1 * initialSize.getWidth()), (int) (0.3 * initialSize.getHeight())};
			if (showDisplacementContour)
			{
				DrawLegend(LegendPos, ColorSystem.redToGreen, "Campo de deslocamentos (m)",
				structure.getResults().getDispMin()[selectedVar], structure.getResults().getDispMax()[selectedVar], 1, DP);
			}
			if (showStressContour && structure.getMesh().getNodes() != null && structure.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, ColorSystem.redToGreen, "Campo de tensoes (MPa)",
				structure.getResults().getStressMin()[selectedVar], structure.getResults().getStressMax()[selectedVar], 1000, DP);
			}
			if (showStrainContour && structure.getMesh().getNodes() != null && structure.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, ColorSystem.redToGreen, "Campo de deformacoes",
				structure.getResults().getStrainMin()[selectedVar], structure.getResults().getStrainMax()[selectedVar], 1, DP);
			}
			if (showInternalForces && structure.getMesh().getNodes() != null && structure.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, ColorSystem.redToGreen, "Forcas internas (kN ou kNm)",
				structure.getResults().getInternalForcesMin()[selectedVar], structure.getResults().getInternalForcesMax()[selectedVar], 1, DP);
			}
		}
	}

	public void setStructure(Structure structure) { this.structure = structure ;}	

    @Override
    public void paintComponent(Graphics graphs) 
    {
        super.paintComponent(graphs);
		if (MenuBar.getInstance() != null)
		{
			DrawPrimitives DP = MainPanel.getInstance().getCentralPanel().getDP() ;
			DP.setGraphics((Graphics2D) graphs) ;
			display(structure, selectedVar, showDisplacementContour, showStressContour, showStrainContour, showInternalForces, DP);
			repaint();
		}
    }

}
