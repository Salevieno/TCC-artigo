package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.example.Main;
import org.example.loading.Force;
import org.example.mainTCC.MainPanel;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.Material;
import org.example.structure.Section;
import org.example.userInterface.MenuBar;
import org.example.utilidades.Util;

import graphics.Align;
import graphics.DrawPrimitives;

public class ListPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private static final Dimension initialSize = new Dimension(10, 100) ;

	private List<Material> matTypes ;
	private List<Section> secTypes ;
	private int selectedID ;

	// private final DrawPrimitives DP ;

    public ListPanel()
    {
		matTypes = new ArrayList<>() ;
		secTypes = new ArrayList<>() ;

        this.setPreferredSize(initialSize);
        this.setBackground(Main.palette[2]);
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    }

	public void resetSelectedID() { selectedID = 0 ;}
	
	public void addMaterials(List<Material> newMaterials) { matTypes.addAll(newMaterials) ;}

	public void addMaterial(Material newMaterial) { matTypes.add(newMaterial) ;}

	public void addSections(List<Section> newSections) { secTypes.addAll(newSections) ;}

	public void addSection(Section newSection) { secTypes.add(newSection) ;}

	public void handleMouseWheel(MouseWheelEvent evt)
	{
		// boolean MouseIsInMainCanvas = Util.MouseIsInside(MenuFunctions.mousePos, panelPos, canvas.getPos(), canvas.getSize().width, canvas.getSize().height);
		Assignable assignable = MainPanel.getInstance().getNorthPanel().getUpperToolbar().getAssignable() ;
		double qtdRotation = evt.getWheelRotation() ;

		if (!false & assignable != null) // MouseIsInMainCanvas
		{
			selectedID += qtdRotation;

			switch (assignable)
			{
				case materials: selectedID = Util.clamp(selectedID, 0, matTypes.size() - 1) ; return ;

				case sections: selectedID = Util.clamp(selectedID, 0, secTypes.size() - 1) ; return ;

				case supports: selectedID = Util.clamp(selectedID, 0, MenuFunctions.SupType.length - 1) ; return ;

				case concLoads: selectedID = Util.clamp(selectedID, 0, MenuFunctions.concLoadTypes.size() - 1) ; return ;

				case distLoads: selectedID = Util.clamp(selectedID, 0, MenuFunctions.DistLoadType.length - 1) ; return ;

				case nodalDisps: selectedID = Util.clamp(selectedID, 0, MenuFunctions.NodalDispType.length - 1) ; return ;
			
				default: System.out.println("Warn: No assignable selected when moving mouse wheel") ; break ;
			}
		}
	}

	private void displayMaterialOptions(Dimension panelSize, int selectedItemID, DrawPrimitives DP)
	{
		String[] MatNames = new String[] {
				"Nome",
				"E (GPa)",
				"v",
				"fu (MPa)"
			};
		List<String[]> matTypesProp = new ArrayList<>() ;
		for (Material mat : matTypes)
		{
			matTypesProp.add(new String[] {"Mat", String.valueOf(mat.getE()), String.valueOf(mat.getV()), String.valueOf(mat.getG())}) ;
		}
		displayTable(panelSize, selectedItemID, MatNames, "Materials list", matTypesProp, DP);
	}

	private void displaySectionOptions(Dimension panelSize, int selectedItemID, DrawPrimitives DP)
	{
		String[] SecNames = new String[] {"Nome", "Espessura (mm)"};
		List<String[]> secTypesProp = new ArrayList<>() ;
		for (Section sec : secTypes)
		{
			secTypesProp.add(new String[] {"Sec", String.valueOf(sec.getT())}) ;
		}
		displayTable(panelSize, selectedItemID, SecNames, "Sections list", secTypesProp, DP);
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
		List<String[]> supTypes = new ArrayList<>() ;
		for (int[] supType : MenuFunctions.SupType)
		{
			String[] stringArray = Arrays.stream(supType).mapToObj(String::valueOf).toArray(String[]::new) ;
			supTypes.add(stringArray) ;
		}
		// displayTable(panelSize, selectedItemID, SupNames, "Supports list", "Sup", supTypes, DP);
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
		List<Force> ConcLoadTypes = MenuFunctions.concLoadTypes;
		List<String[]> loadTypes = new ArrayList<>() ;
		for (Force load : ConcLoadTypes)
		{
			loadTypes.add(new String[] {"Conc load", String.valueOf(load.x), String.valueOf(load.y), String.valueOf(load.z), String.valueOf(load.tx), String.valueOf(load.ty), String.valueOf(load.tz)}) ;
		}
		displayTable(panelSize, selectedItemID, ConcLoadNames, "Conc loads list", loadTypes, DP);
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
		List<String[]> distLoadTypes = new ArrayList<>() ;
		for (double[] loadType : MenuFunctions.DistLoadType)
		{
			String[] stringArray = Arrays.stream(loadType).mapToObj(String::valueOf).toArray(String[]::new) ;
			distLoadTypes.add(stringArray) ;
		}
		// displayTable(panelSize, selectedItemID, DistLoadNames, "Dist loads list", "Dist load", distLoadTypes, DP);
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
		List<String[]> nodalDispTypes = new ArrayList<>() ;
		for (double[] dispType : MenuFunctions.NodalDispType)
		{
			String[] stringArray = Arrays.stream(dispType).mapToObj(String::valueOf).toArray(String[]::new) ;
			nodalDispTypes.add(stringArray) ;
		}
		// displayTable(panelSize, selectedItemID, NodalDispNames, "Nodal disps list", "Nodal disp", nodalDispTypes, DP);
	}
    
	public void display(Assignable assignable, DrawPrimitives DP)
	{

		if (assignable == null) { this.setBackground(null) ; return ;}

		this.setBackground(new Color(50, 100, 120)) ;
		Dimension panelSize = getSize() ;

		switch (assignable)
		{
			case materials: displayMaterialOptions(panelSize, selectedID, DP) ; return ;

			case sections: displaySectionOptions(panelSize, selectedID, DP) ; return ;

			case supports: displaySupportOptions(panelSize, selectedID, DP) ; return ;

			case concLoads: displayConcLoadOptions(panelSize, selectedID, DP) ; return ;

			case distLoads: displayDistLoadOptions(panelSize, selectedID, DP) ; return ;

			case nodalDisps: displayNodalDispOptions(panelSize, selectedID, DP) ; return ;
		
			default: return ;
		}
	}

	private void displayTable(Dimension panelSize, int selectedRow, String[] PropName, String Title, List<String[]> rowData, DrawPrimitives DP)
	{
		Point pos = new Point((int) (0.025 * panelSize.getWidth()), (int) (0.15 * panelSize.getHeight())) ;
		Dimension size = new Dimension((int) (0.95 * panelSize.getWidth()), (int) (0.9 * panelSize.getHeight())) ;
		displayTable(pos, size, selectedRow, PropName, Title, rowData, DP);
	}

	private void displayTable(Point pos, Dimension size, int selectedRow, String[] header, String Title, List<String[]> rowData, DrawPrimitives DP)
	{
		int Ncol = header.length;
		Point padding = new Point(20, 10) ;
		int sx = (int) (size.width - padding.x) / Ncol ;
		int sy = 16 ;
		Color stdItemColor = Main.palette[0] ;
		Color selectedItemColor = Main.palette[10] ;
		
		Point titlePos = new Point(pos.x + size.width / 2, (int) (pos.y - 1.3 * 11 / 2 - padding.y / 2)) ;
		DP.drawText(titlePos, Align.center, Title, Main.palette[1]);
		
		Point textPos = new Point(pos.x + padding.x, pos.y + padding.y) ;
		for (int col = 0; col <= rowData.get(0).length - 1; col += 1)
		{
			DP.drawText(textPos, Align.center, header[col], Main.palette[0]);
			textPos.translate(sx, 0) ;
		}

		textPos = new Point(pos.x + padding.x, pos.y + padding.y) ;
		textPos.translate(0, sy) ;
		for (int row = 0; row <= rowData.size() - 1 ; row += 1)
		{						
			Color textColor = selectedRow == row ? selectedItemColor : stdItemColor;
			for (int col = 0; col <= rowData.get(row).length - 1; col += 1)
			{
				String text = rowData.get(row)[col] + (col == 0 ? " " + String.valueOf(row) : "") ;
				
				DP.drawText(textPos, Align.center, text, textColor) ;
				textPos.translate(sx, 0) ;
			}
			textPos.translate(-sx * rowData.get(row).length, sy) ;
		}
	}

	public List<Material> getMatTypes() { return matTypes ;}
	public List<Section> getSecTypes() { return secTypes ;}
	public int getSelectedID() { return selectedID ;}
	public void setSelectedID(int selectedID) { this.selectedID = selectedID ;}

    @Override
    public void paintComponent(Graphics graphs) 
    {
        super.paintComponent(graphs);
        // DP.setG(g);
        if (MenuBar.getInstance() != null)
        {
			DrawPrimitives DP = MainPanel.getInstance().getCentralPanel().getDP() ;
			DP.setGraphics((Graphics2D) graphs);
            display(MainPanel.getInstance().getNorthPanel().getUpperToolbar().getAssignable(), DP);
        }
        repaint();
    }
}
