package main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import main.gui.DrawingOnAPanel;
import main.gui.Menus;
import main.mainTCC.MenuFunctions;
import main.structure.Structure;
import main.utilidades.Util;

public class LegendPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private static final Dimension initialSize = new Dimension(10, 100) ;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;

    public LegendPanel()
    {
        setPreferredSize(initialSize);
        setBackground(Menus.palette[3]);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Legenda", TitledBorder.CENTER, TitledBorder.CENTER));
		
    }


	public static void DrawLegend(int[] Pos, String ColorSystem, String Title, int[] Size, double MinValue, double MaxValue, double unitfactor, DrawingOnAPanel DP)
	{
		double sx, sy;
		int BarLength;
		int TitleSize = Math.max(Size[0], Size[1]) / 16;
		int FontSize = Math.max(Size[0], Size[1]) / 18;
		int NumCat = 10, NumLines, NumColumns;
		NumLines = 2;
		NumColumns = (1 + NumCat) / NumLines;
		BarLength = (Size[0] / NumColumns)/2;
		sx = BarLength;
		sy = Size[1] / (double)(NumLines);
		DP.DrawText(new int[] {Pos[0] + Size[0] / 2, (int) (Pos[1] - 1.3 * FontSize)}, Title, "Center", 0, "Bold", TitleSize, Color.magenta);
		DP.DrawWindow(Pos, Size[0], Size[1], 1, Color.white, Color.blue);
		for (int i = 0; i <= NumCat - 1; i += 1)
		{
			double value = (MaxValue - MinValue)*i/(NumCat - 1) + MinValue;
			Color color = Util.FindColor(value, MinValue, MaxValue, ColorSystem);
			int[] InitPos = new int[] {(int) (Pos[0] + 2*(i % NumColumns)*sx + sx/2), (int) (Pos[1] + (i / NumColumns) * sy + sy / 4)};
			DP.DrawLine(InitPos, new int[] {InitPos[0] + BarLength, InitPos[1]}, 2, color);
			DP.DrawText(new int[] {InitPos[0] + BarLength/2, (int) (InitPos[1] + FontSize / 2 + FontSize / 4)}, String.valueOf(Util.Round(value / unitfactor, 2)), "Center", 0, "Plain", FontSize, color);
		}
	}

	public static void display(Structure structure, int SelectedVar,
    boolean ShowDisplacementContour, boolean ShowStressContour, boolean ShowStrainContour, boolean ShowInternalForces,
    DrawingOnAPanel DP)
	{
		if (-1 < SelectedVar)
		{
			int[] LegendPos = new int[] {(int) (0.1 * initialSize.getWidth()), (int) (0.3 * initialSize.getHeight())};
			int[] LegendSize = new int[] {(int) (0.8 * initialSize.getWidth()), (int) (0.6 * initialSize.getHeight())};
			if (ShowDisplacementContour)
			{
				DrawLegend(LegendPos, "Red to green", "Campo de deslocamentos (m)", LegendSize,
				structure.getResults().getDispMin()[SelectedVar], structure.getResults().getDispMax()[SelectedVar], 1, DP);
			}
			if (ShowStressContour & structure.getMesh().getNodes() != null & structure.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, "Red to green", "Campo de tensoes (MPa)", LegendSize,
				structure.getResults().getStressMin()[SelectedVar], structure.getResults().getStressMax()[SelectedVar], 1000, DP);
			}
			if (ShowStrainContour & structure.getMesh().getNodes() != null & structure.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, "Red to green", "Campo de deformacoes", LegendSize,
				structure.getResults().getStrainMin()[SelectedVar], structure.getResults().getStrainMax()[SelectedVar], 1, DP);
			}
			if (ShowInternalForces & structure.getMesh().getNodes() != null & structure.getMesh().getElements() != null)
			{
				DrawLegend(LegendPos, "Red to green", "Forcas internas (kN ou kNm)", LegendSize,
				structure.getResults().getInternalForcesMin()[SelectedVar], structure.getResults().getInternalForcesMax()[SelectedVar], 1, DP);
			}
		}
	}

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        DP.setG(g);
        DP.setRealStructCenter(MainPanel.structure.getCenter());
        display(MainPanel.structure, MainPanel.SelectedVar, MainPanel.ShowDisplacementContour, MainPanel.ShowStressContour, MainPanel.ShowStrainContour, MainPanel.ShowInternalForces, DP);
        repaint();
    }
}
