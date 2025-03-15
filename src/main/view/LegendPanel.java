package main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import main.structure.Structure;
import main.userInterface.DrawingOnAPanel;
import main.userInterface.Menus;
import main.utilidades.Util;

public class LegendPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private static final Dimension initialSize = new Dimension(10, 100) ;
	private static final int titleSize = 13;
	private static final int fontSize = 13;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;

    public LegendPanel()
    {
        setPreferredSize(initialSize);
        setBackground(Menus.palette[3]);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Legenda", TitledBorder.CENTER, TitledBorder.CENTER));
		setVisible(true);
    }


	public void DrawLegend(int[] Pos, String ColorSystem, String title, double MinValue, double MaxValue, double unitfactor)
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
		DP.DrawText(new int[] {Pos[0] + panelSize.width / 2, (int) (Pos[1])}, title, "Center", 0, "Bold", titleSize, Menus.palette[6]);
		DP.DrawWindow(Pos, panelSize.width, panelSize.height, 1, null, Color.blue);
		for (int i = 0; i <= NumCat - 1; i += 1)
		{
			double value = (MaxValue - MinValue)*i/(NumCat - 1) + MinValue;
			Color color = Util.FindColor(value, MinValue, MaxValue, ColorSystem);
			int[] InitPos = new int[] {(int) (Pos[0] + 2*(i % NumColumns)*sx + sx/2), (int) (Pos[1] + (i / NumColumns) * sy + sy / 4)};
			DP.DrawLine(InitPos, new int[] {InitPos[0] + BarLength, InitPos[1]}, 2, color);
			DP.DrawText(new int[] {InitPos[0] + BarLength/2, (int) (InitPos[1] + fontSize / 2 + fontSize / 4)}, String.valueOf(Util.Round(value / unitfactor, 2)), "Center", 0, "Plain", fontSize, color);
		}
	}

	public void display(Structure structure, int SelectedVar,
    					boolean ShowDisplacementContour, boolean ShowStressContour, boolean ShowStrainContour, boolean ShowInternalForces)
	{
		if (-1 < SelectedVar)
		{
			int[] LegendPos = new int[] {(int) (0.1 * initialSize.getWidth()), (int) (0.3 * initialSize.getHeight())};
			if (ShowDisplacementContour)
			{
				DrawLegend(LegendPos, "Red to green", "Campo de deslocamentos (m)",
				structure.getResults().getDispMin()[SelectedVar], structure.getResults().getDispMax()[SelectedVar], 1);
			}
			if (ShowStressContour & structure.getMesh().getNodes() != null & structure.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, "Red to green", "Campo de tensoes (MPa)",
				structure.getResults().getStressMin()[SelectedVar], structure.getResults().getStressMax()[SelectedVar], 1000);
			}
			if (ShowStrainContour & structure.getMesh().getNodes() != null & structure.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, "Red to green", "Campo de deformacoes",
				structure.getResults().getStrainMin()[SelectedVar], structure.getResults().getStrainMax()[SelectedVar], 1);
			}
			if (ShowInternalForces & structure.getMesh().getNodes() != null & structure.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, "Red to green", "Forcas internas (kN ou kNm)",
				structure.getResults().getInternalForcesMin()[SelectedVar], structure.getResults().getInternalForcesMax()[SelectedVar], 1);
			}
		}
	}

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        DP.setG(g);
        DP.setRealStructCenter(MainPanel.structure.getCenter());
        display(MainPanel.structure, MainPanel.SelectedVar, MainPanel.ShowDisplacementContour, MainPanel.ShowStressContour, MainPanel.ShowStrainContour, MainPanel.ShowInternalForces);
        repaint();
    }
}
