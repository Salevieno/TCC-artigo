package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.example.loading.Force;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.Material;
import org.example.structure.Section;
import org.example.userInterface.Draw;
import org.example.userInterface.Menus;
import org.example.utilidades.Util;

import graphics.Align;
import graphics.DrawPrimitives;

public class ListPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private static final Dimension initialSize = new Dimension(10, 100) ;
	// private final DrawPrimitives DP ;

    public ListPanel()
    {
        this.setPreferredSize(initialSize);
        this.setBackground(Menus.palette[2]);
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		// DP = Menus.getInstance().getMainPanel().getDP() ;
		// setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[1]));
    }

	private void displayMaterialOptions(Dimension panelSize, int selectedItemID)
	{
		String[] MatNames = new String[] {
				"Nome",
				"E (GPa)",
				"v",
				"fu (MPa)"
			};
		List<Material> matType = MainPanel.matTypes;
//			TODO DP.DrawLists(jpListSize, SelectedMat, MatNames, "Materials list", "Mat", matType);
	}

	private void displaySectionOptions(Dimension panelSize, int selectedItemID)
	{
		String[] SecNames = new String[] {"Nome", "Espessura (mm)"};
		List<Section> SecType = MainPanel.secTypes;
//			DP.DrawLists(jpListSize, SelectedSec, SecNames, "Sections list", "Sec", SecType);
	}

	private void displaySupportOptions(Dimension panelSize, int selectedItemID, DrawPrimitives DP)
	{
		String[] SupNames = new String[] {
				"Nome",
				"Rx",
				"Ry",
				"Rz",
				"Tetax",
				"Tetay",
				"Tetaz"
			};
		int[][] SupType = MenuFunctions.SupType;
		DrawLists(panelSize, selectedItemID, SupNames, "Supports list", "Sup", SupType, DP);
	}

	private void displayConcLoadOptions(Dimension panelSize, int selectedItemID, DrawPrimitives DP)
	{
		String[] ConcLoadNames = new String[] {
				"Nome",
				"Fx (kN)",
				"Fy (kN)",
				"Fz (kN)",
				"Mx (kN)",
				"My (kN)",
				"Mz (kN)"
			};
		List<Force> ConcLoadType = MenuFunctions.concLoadTypes;
		DrawLists(panelSize, selectedItemID, ConcLoadNames, "Conc loads list", "Conc load", ConcLoadType, DP);
	}

	private void displayDistLoadOptions(Dimension panelSize, int selectedItemID, DrawPrimitives DP)
	{
		String[] DistLoadNames = new String[] {
				"Nome",
				"Tipo",
				"Pini (kN/m)",
				"Pfin (kN/m)",
				"Distini (m)",
				"Distfin (m)"
			};
		double[][] DistLoadType = MenuFunctions.DistLoadType;
		DrawLists(panelSize, selectedItemID, DistLoadNames, "Dist loads list", "Dist load", DistLoadType, DP);
	}

	private void displayNodalDispOptions(Dimension panelSize, int selectedItemID, DrawPrimitives DP)
	{
		String[] NodalDispNames = new String[] {
				"Nome",
				"desl x (m)",
				"desl y (m)",
				"desl z (m)",
				"rot x",
				"rot y",
				"rot z"
			};
		double[][] NodalDispType = MenuFunctions.NodalDispType;
		DrawLists(panelSize, selectedItemID, NodalDispNames, "Nodal disps list", "Nodal disp", NodalDispType, DP);
	}
    
	public void display(Assignable assignable, int selectedSup, int selectedConcLoad, int selectedDistLoad, int selectedNodalDisp, DrawPrimitives DP)
	{

		if (assignable == null) { this.setBackground(null) ; return ;}

		this.setBackground(new Color(50, 100, 120)) ;
		Dimension panelSize = getSize() ;

		switch (assignable)
		{
			// case materials:
			// 	displayMaterialOptions(panelSize, selectedMat) ;
			// 	return;

			// case sections:
			// 	displaySectionOptions(panelSize, selectedSec) ;
			// 	return;

			case supports:
				displaySupportOptions(panelSize, selectedSup, DP) ;
				return;

			case concLoads:
				displayConcLoadOptions(panelSize, selectedConcLoad, DP) ;
				return;

			case distLoads:
				displayDistLoadOptions(panelSize, selectedDistLoad, DP) ;
				return;

			case nodalDisps:
				displayNodalDispOptions(panelSize, selectedNodalDisp, DP) ;
				return;
		
			default: return;
		}
	}
	
	private void DrawLists(Dimension PanelSize, int SelectedItem, String[] ItemNames, String Title, String Label, List<Force> ItemTypes, DrawPrimitives DP)
	{
		int[] ListPos = new int[] {(int) (0.025 * PanelSize.getWidth()), (int) (0.15 * PanelSize.getHeight())};
		int[] ListSize = new int[] {(int) (0.95 * PanelSize.getWidth()), (int) (0.9 * PanelSize.getHeight())};
		DrawList(ListPos, ListSize, SelectedItem, ItemNames, Title, Label, ItemTypes.toArray(new double[0][]), DP);
	}
	
	private void DrawLists(Dimension PanelSize, int SelectedItem, String[] ItemNames, String Title, String Label, double[][] ItemTypes, DrawPrimitives DP)
	{
		int[] ListPos = new int[] {(int) (0.025 * PanelSize.getWidth()), (int) (0.15 * PanelSize.getHeight())};
		int[] ListSize = new int[] {(int) (0.95 * PanelSize.getWidth()), (int) (0.9 * PanelSize.getHeight())};
		DrawList(ListPos, ListSize, SelectedItem, ItemNames, Title, Label, ItemTypes, DP);
	}

	private void DrawLists(Dimension PanelSize, int SelectedItem, String[] ItemNames, String Title, String Label, int[][] ItemTypes, DrawPrimitives DP)
	{
		int[] ListPos = new int[] {(int) (0.025 * PanelSize.getWidth()), (int) (0.15 * PanelSize.getHeight())};
		int[] ListSize = new int[] {(int) (0.95 * PanelSize.getWidth()), (int) (0.9 * PanelSize.getHeight())};
		DrawList(ListPos, ListSize, SelectedItem, ItemNames, Title, Label, ItemTypes, DP);
	}
		
	private void DrawList(int[] Pos, int[] Size, int SelectedItem, String[] PropName, String Title, String ItemName, double[][] Input, DrawPrimitives DP)
	{
		int FontSize = 11;
		int offset = 10;
		int sx = 0 ;
		int sy = 0;
		int Nrow = Input.length ;
		int Ncol = PropName.length;
		Color TitleColor = Color.blue ;
		Color PropNameColor = new Color(200, 180, 150) ;
		Color UnselectedItemColor = Color.cyan ;
		Color SelectedItemColor = Color.green;

		sx = (int) (Size[0] - 2 * offset) / Ncol;
		sy = FontSize + 5;
		Size[1] = Nrow * sy + FontSize + 2 * offset;
		
		DP.drawText(new Point(Pos[0] + Size[0] / 2, (int) (Pos[1] - 1.3 * FontSize / 2 - offset / 2)), Align.center, Title, TitleColor);
		// DrawText(new int[] {Pos[0] + Size[0] / 2, (int) (Pos[1] - 1.3 * FontSize / 2 - offset / 2)}, Title, "Center", 0, "Bold", (int) (1.3 * FontSize), TitleColor);
		// DrawWindow(Pos, Size[0], Size[1], 2, new Color(50, 100, 120), null);		
		for (int row = 0; row <= Nrow - 1; row += 1)
		{
			int[] TextPos = new int[] {Pos[0] + offset + sx / 2, Pos[1] + offset + FontSize / 2};
			if (row == 0)
			{
				for (int prop = 0; prop <= Input[row].length; prop += 1)
				{
					DP.drawText(new Point(TextPos[0] + prop*sx, TextPos[1]), Align.center, PropName[prop], PropNameColor);			
					// DrawText(new int[] {TextPos[0] + prop*sx, TextPos[1]}, PropName[prop], "Center", 0, "Bold", FontSize, PropNameColor);					
				}
			}							
			Color TextColor = UnselectedItemColor;
			if (SelectedItem == row)
			{
				TextColor = SelectedItemColor;
			}
			DP.drawText(new Point(TextPos[0], TextPos[1] + (row + 1) * sy), Align.center, ItemName + " " + String.valueOf(row), TextColor);	
			// DrawText(new int[] {TextPos[0], TextPos[1] + (row + 1) * sy}, ItemName + " " + String.valueOf(row), "Center", 0, "Bold", FontSize, TextColor);
			for (int prop = 0; prop <= Input[row].length - 1; prop += 1)
			{
				DP.drawText(new Point(TextPos[0] + (prop + 1) * sx, TextPos[1] + (row + 1) * sy), Align.center, String.valueOf(Input[row][prop]), TextColor);	
				// DrawText(new int[] {TextPos[0] + (prop + 1) * sx, TextPos[1] + (row + 1) * sy}, String.valueOf(Input[row][prop]), "Center", 0, "Bold", FontSize, TextColor);			
			}
		}
	}

	private void DrawList(int[] Pos, int[] Size, int SelectedItem, String[] PropName, String Title, String ItemName, int[][] Input, DrawPrimitives DP)
	{
		double[][] inputAsDouble = new double[Input.length][Input[0].length] ;
		for (int i = 0 ; i <= Input.length - 1 ; i += 1)
		{
			for (int j = 0 ; j <= Input.length - 1 ; j += 1)
			{
				inputAsDouble[i][j] = Input[i][j] ;
 			}
		}
		DrawList(Pos, Size, SelectedItem, PropName, Title, ItemName, inputAsDouble, DP) ;
		// int FontSize = 11;
		// int offset = 10;
		// int sx = 0, sy = 0;
		// int Nrow = Input.length, Ncol = PropName.length;
		// Color TitleColor = Color.blue, PropNameColor = new Color(200, 180, 150), UnselectedItemColor = Color.cyan, SelectedItemColor = Color.green;
		// sx = (int) (Size[0] - 2 * offset) / Ncol;
		// sy = FontSize + 5;
		// Size[1] = Nrow * sy + FontSize + 2 * offset;
		
		// DrawText(new int[] {Pos[0] + Size[0] / 2, (int) (Pos[1] - 1.3 * FontSize / 2 - offset / 2)}, Title, "Center", 0, "Bold", (int) (1.3 * FontSize), TitleColor);
		// DrawWindow(Pos, Size[0], Size[1], 2, new Color(50, 100, 120), null);		
		// for (int row = 0; row <= Nrow - 1; row += 1)
		// {
		// 	int[] TextPos = new int[] {Pos[0] + offset + sx / 2, Pos[1] + offset + FontSize / 2};
		// 	if (row == 0)
		// 	{
		// 		for (int prop = 0; prop <= Input[row].length; prop += 1)
		// 		{					
		// 			DrawText(new int[] {TextPos[0] + prop*sx, TextPos[1]}, PropName[prop], "Center", 0, "Bold", FontSize, PropNameColor);					
		// 		}
		// 	}							
		// 	Color TextColor = UnselectedItemColor;
		// 	if (SelectedItem == row)
		// 	{
		// 		TextColor = SelectedItemColor;
		// 	}
		// 	DrawText(new int[] {TextPos[0], TextPos[1] + (row + 1) * sy}, ItemName + " " + String.valueOf(row), "Center", 0, "Bold", FontSize, TextColor);
		// 	for (int prop = 0; prop <= Input[row].length - 1; prop += 1)
		// 	{
		// 		DrawText(new int[] {TextPos[0] + (prop + 1) * sx, TextPos[1] + (row + 1) * sy}, String.valueOf(Util.Round(Input[row][prop], 1)), "Center", 0, "Bold", FontSize, TextColor);			
		// 	}
		// }
	}

    @Override
    public void paintComponent(Graphics graphs) 
    {
        super.paintComponent(graphs);
        // DP.setG(g);
        if (Menus.getInstance() != null)
        {
			DrawPrimitives DP = Menus.getInstance().getMainPanel().getDP() ;
			DP.setGraphics((Graphics2D) graphs);
            display(Menus.getInstance().getNorthPanel().getUpperToolbar().getAssignable(), MainPanel.selectedSupID, MainPanel.selectedConcLoadID, MainPanel.selectedDistLoadID, MainPanel.selectedNodalDispID, DP);
        }
        repaint();
    }
}
