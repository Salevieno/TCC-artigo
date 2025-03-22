package org.example.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.example.mainTCC.MenuFunctions;
import org.example.structure.Material;
import org.example.structure.Section;
import org.example.userInterface.DrawingOnAPanel;
import org.example.userInterface.Menus;

public class ListPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private static final Dimension initialSize = new Dimension(10, 100) ;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;

    public ListPanel()
    {
        setPreferredSize(initialSize);
        setBackground(Menus.palette[2]);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
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

	private void displaySupportOptions(Dimension panelSize, int selectedItemID)
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
		DP.DrawLists(panelSize, selectedItemID, SupNames, "Supports list", "Sup", SupType);
	}

	private void displayConcLoadOptions(Dimension panelSize, int selectedItemID)
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
		double[][] ConcLoadType = MenuFunctions.ConcLoadType;
		DP.DrawLists(panelSize, selectedItemID, ConcLoadNames, "Conc loads list", "Conc load", ConcLoadType);
	}

	private void displayDistLoadOptions(Dimension panelSize, int selectedItemID)
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
		DP.DrawLists(panelSize, selectedItemID, DistLoadNames, "Dist loads list", "Dist load", DistLoadType);
	}

	private void displayNodalDispOptions(Dimension panelSize, int selectedItemID)
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
		DP.DrawLists(panelSize, selectedItemID, NodalDispNames, "Nodal disps list", "Nodal disp", NodalDispType);
	}
    
	public void display(Assignable assignable, int selectedSup, int selectedConcLoad, int selectedDistLoad, int selectedNodalDisp, DrawingOnAPanel DP)
	{

		if (assignable == null) { return ;}

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
				displaySupportOptions(panelSize, selectedSup) ;
				return;

			case concLoads:
				displayConcLoadOptions(panelSize, selectedConcLoad) ;
				return;

			case distLoads:
				displayDistLoadOptions(panelSize, selectedDistLoad) ;
				return;

			case nodalDisps:
				displayNodalDispOptions(panelSize, selectedNodalDisp) ;
				return;
		
			default: return;
		}
	}
	

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        DP.setG(g);
        DP.setRealStructCenter(MainPanel.structure.getCenter());
        if (Menus.getInstance() != null)
        {
            display(Menus.getInstance().getNorthPanel().getUpperToolbar().getAssignable(), MainPanel.selectedSupID, MainPanel.selectedConcLoadID, MainPanel.selectedDistLoadID, MainPanel.selectedNodalDispID, DP);
        }
        repaint();
    }
}
