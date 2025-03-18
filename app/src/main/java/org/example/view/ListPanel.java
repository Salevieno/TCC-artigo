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

    
	public static void display(boolean[] AssignmentIsOn, int SelectedSup, int SelectedConcLoad, int SelectedDistLoad, int SelectedNodalDisp, DrawingOnAPanel DP)
	{
		
		List<Material> matType = MenuFunctions.matTypes;
		List<Section> SecType = MenuFunctions.secTypes;
		int[][] SupType = MenuFunctions.SupType;
		double[][] ConcLoadType = MenuFunctions.ConcLoadType;
		double[][] DistLoadType = MenuFunctions.DistLoadType;
		double[][] NodalDispType = MenuFunctions.NodalDispType;

		if (AssignmentIsOn[0] && matType != null)
		{
			String[] MatNames = new String[] {
				    "Nome",
				    "E (GPa)",
				    "v",
				    "fu (MPa)"
				};
//			TODO DP.DrawLists(jpListSize, SelectedMat, MatNames, "Materials list", "Mat", matType);
		}
		if (AssignmentIsOn[1] && SecType != null)
		{
			String[] SecNames = new String[] {"Nome", "Espessura (mm)"};
//			DP.DrawLists(jpListSize, SelectedSec, SecNames, "Sections list", "Sec", SecType);
		}
		if (AssignmentIsOn[2] && SupType != null)
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
			DP.DrawLists(initialSize, SelectedSup, SupNames, "Supports list", "Sup", SupType);
		}
		if (AssignmentIsOn[3] && ConcLoadType != null)
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
			DP.DrawLists(initialSize, SelectedConcLoad, ConcLoadNames, "Conc loads list", "Conc load", ConcLoadType);
		}
		if (AssignmentIsOn[4] && DistLoadType != null)
		{
			String[] DistLoadNames = new String[] {
				    "Nome",
				    "Tipo",
				    "Pini (kN/m)",
				    "Pfin (kN/m)",
				    "Distini (m)",
				    "Distfin (m)"
				};
			DP.DrawLists(initialSize, SelectedDistLoad, DistLoadNames, "Dist loads list", "Dist load", DistLoadType);
		}
		if (AssignmentIsOn[5] && NodalDispType != null)
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
			DP.DrawLists(initialSize, SelectedNodalDisp, NodalDispNames, "Nodal disps list", "Nodal disp", NodalDispType);
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
            display(Menus.getInstance().getNorthPanel().getUpperToolbar().getAqueleBooleanGrande(), MainPanel.selectedSupID, MainPanel.selectedConcLoadID, MainPanel.selectedDistLoadID, MainPanel.selectedNodalDispID, DP);
        }
        repaint();
    }
}
