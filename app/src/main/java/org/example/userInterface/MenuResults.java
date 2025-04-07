package org.example.userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.Main;
import org.example.mainTCC.MenuFunctions;
import org.example.service.MenuViewService;
import org.example.structure.Element;
import org.example.utilidades.Util;
import org.example.view.CentralPanel;

public class MenuResults extends JMenu
{
    
    private static final String[] menuItemNames = new String[] {
        "Estrutura deformada",
        "Deslocamentos",
        "Tensoes",
        "Deformacoes",
        "Forcas internas",
        "Salvar resultados",
        "Salvar curva carga-desl"
    };
    
	private JMenuItem DisplacementContours, DeformedShape, StressContours, StrainContours, InternalForcesContours, SaveResults, SaveLoadDispCurve;
	private JMenuItem[] SubMenuDisp;				// ux, uy, uz, tetax, tetay, tetaz
	private JMenuItem[] SubMenuStresses;			// Sigmax, Sigmay, Sigmaz, Taux, Tauy, Tauz
	private JMenuItem[] SubMenuStrains;				// ex, ey, ez, gxy, gxz, gyz
	private JMenuItem[] SubMenuInternalForces;		// Fx, Fy, Fz, Mx, My, Mz

    public MenuResults()
    {
        this.setMnemonic(KeyEvent.VK_R) ;
        this.setText("Results");

		DeformedShape = new JMenuItem(menuItemNames[0], KeyEvent.VK_D);
		DisplacementContours = new JMenu(menuItemNames[1]);
		StressContours = new JMenu(menuItemNames[2]);
		StrainContours = new JMenu(menuItemNames[3]);
		InternalForcesContours  = new JMenu(menuItemNames[4]);
		SaveResults = new JMenuItem(menuItemNames[5], KeyEvent.VK_S);
		SaveLoadDispCurve = new JMenuItem(menuItemNames[6]);
		DeformedShape.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuViewService.getInstance().switchDeformedStructureView();
			}
		});
		/*DisplacementContours.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				SelectedDiagram = 0;
				SelectedVar = 2;
				DrawDisplacementContours(Util.ElemPosInArray(DOFsPerNode, 2), DOFsPerNode);
				MainCanvas.setTitle("Deslocamentos (x" + String.valueOf(DiagramsScales[1]) + ")");
			}
		});*/
		SaveResults.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.ResultsMenuSaveResults(CentralPanel.structure);
			}
		});
		SaveLoadDispCurve.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.SaveLoadDispCurve(CentralPanel.structure);
			}
		});
		DeformedShape.setEnabled(false);
		DisplacementContours.setEnabled(false);
		StressContours.setEnabled(false);
		StrainContours.setEnabled(false);
		InternalForcesContours.setEnabled(false);
		SaveResults.setEnabled(false);
		SaveLoadDispCurve.setEnabled(false);
		DeformedShape.setForeground(Main.palette[7]);
		DisplacementContours.setForeground(Main.palette[7]);
		StressContours.setForeground(Main.palette[7]);
		StrainContours.setForeground(Main.palette[7]);
		InternalForcesContours.setForeground(Main.palette[7]);
		SaveResults.setForeground(Main.palette[7]);
		SaveLoadDispCurve.setForeground(Main.palette[7]);
		this.add(DeformedShape);
		this.add(DisplacementContours);
		this.add(StressContours);
		this.add(StrainContours);
		this.add(InternalForcesContours);
		this.add(SaveResults);
		this.add(SaveLoadDispCurve);

        
		SubMenuDisp = new JMenuItem[6];				// ux, uy, uz, tetax, tetay, tetaz
		SubMenuStresses = new JMenuItem[6];			// Sigmax, Sigmay, Sigmaz, Taux, Tauy, Tauz
		SubMenuStrains = new JMenuItem[6];			// ex, ey, ez, gxy, gxz, gyz
		SubMenuInternalForces = new JMenuItem[6];	// Fx, Fy, Fz, Mx, My, Mz
		

		/* Defining subitems in the menu DisplacementContour */
		for (int d = 0; d <= SubMenuDisp.length - 1; d += 1)
		{
			int d2 = d;
			String[] DisplacementContourMenuNames = new String[] {
				    "ux",
				    "uy",
				    "uz",
				    "tetax",
				    "tetay",
				    "tetaz"
				};
			SubMenuDisp[d] = new JMenuItem(DisplacementContourMenuNames[d]);
			SubMenuDisp[d].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 0;
					CentralPanel.SelectedVar = Util.ElemPosInArray(CentralPanel.structure.getMesh().getElements().get(0).getDOFs(), d2);
					//DrawDisplacementContours(SelectedVar);
					//ResetE1Panel();
				}
			});
			SubMenuDisp[d].setEnabled(false);
			SubMenuDisp[d].setForeground(Main.palette[7]);
			DisplacementContours.add(SubMenuDisp[d]);
		}
		
		/* Defining subitems in the menu StressContours */
		for (int s = 0; s <= SubMenuStresses.length - 1; s += 1)
		{
			int s2 = s;
			String[] StressContoursMenuNames = new String[] {
				    "Sigma x",
				    "Sigma y",
				    "Sigma z",
				    "Tau xy",
				    "Tau xz",
				    "Tau yz"
				};
			SubMenuStresses[s] = new JMenuItem(StressContoursMenuNames[s]);
			SubMenuStresses[s].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 1;
					CentralPanel.SelectedVar = Util.ElemPosInArray(CentralPanel.structure.getMesh().getElements().get(0).getDOFs(), s2);
					//DrawStressContours(SelectedVar);
					//ResetE1Panel();
				}
			});
			SubMenuStresses[s].setEnabled(false);
			SubMenuStresses[s].setForeground(Main.palette[7]);
			StressContours.add(SubMenuStresses[s]);
		}
		
		/* Defining subitems in the menu StrainContours */
		for (int s = 0; s <= SubMenuStrains.length - 1; s += 1)
		{
			int s2 = s;
			String[] StrainContoursMenuNames = new String[] {
				    "Deformacao x",
				    "Deformacao y",
				    "Deformacao z",
				    "Deformacao xy",
				    "Deformacao xz",
				    "Deformacao yz"
				};
			SubMenuStrains[s] = new JMenuItem(StrainContoursMenuNames[s]);
			SubMenuStrains[s].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 2;
					CentralPanel.SelectedVar = Util.ElemPosInArray(CentralPanel.structure.getMesh().getElements().get(0).getDOFs(), s2);
					//DrawStrainContours(SelectedVar);
					//ResetE1Panel();
				}
			});
			SubMenuStrains[s].setEnabled(false);
			SubMenuStrains[s].setForeground(Main.palette[7]);
			StrainContours.add(SubMenuStrains[s]);
		}
		
		/* Defining subitems in the menu InternalForcesContours */
		for (int f = 0; f <= SubMenuInternalForces.length - 1; f += 1)
		{
			int f2 = f;
			String[] InternalForcesContoursMenuNames = new String[] {
				    "Fx",
				    "Fy",
				    "Fz",
				    "Mx",
				    "My",
				    "Mz"
				} ;
			SubMenuInternalForces[f] = new JMenuItem(InternalForcesContoursMenuNames[f]);
			SubMenuInternalForces[f].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//SelectedDiagram = 3;
					CentralPanel.SelectedVar = Util.ElemPosInArray(CentralPanel.structure.getMesh().getElements().get(0).getDOFs(), f2);
					//DrawInternalForcesContours(Util.ElemPosInArray(Elem[0].getDOFs(), f2));
					//ResetE1Panel();
				}
			});
			SubMenuInternalForces[f].setEnabled(false);
			SubMenuInternalForces[f].setForeground(Main.palette[7]);
			InternalForcesContours.add(SubMenuInternalForces[f]);
		}
    }

    public void enableButtons()
    {
		List<Element> elems = CentralPanel.structure.getMesh() != null ? CentralPanel.structure.getMesh().getElements() : null;
        
        DeformedShape.setEnabled(true);
        DisplacementContours.setEnabled(true);
        StressContours.setEnabled(true);
        StrainContours.setEnabled(true);
        InternalForcesContours.setEnabled(true);
        for (int i = 0; i <= elems.get(0).getStrainTypes().length - 1; i += 1)
        {
            if (elems.get(0).getStrainTypes()[i] <= 5)
            {
                SubMenuStresses[elems.get(0).getStrainTypes()[i]].setEnabled(true);
                SubMenuStrains[elems.get(0).getStrainTypes()[i]].setEnabled(true);
            }
        }
        SaveResults.setEnabled(true);
        SaveLoadDispCurve.setEnabled(true);
    }

    public void disableButtons()
    {
		DeformedShape.setEnabled(false);
		DisplacementContours.setEnabled(false);
		StressContours.setEnabled(false);
		StrainContours.setEnabled(false);
		InternalForcesContours.setEnabled(false);
		for (int i = 0; i <= SubMenuDisp.length - 1; i += 1)
		{
			SubMenuDisp[i].setEnabled(false);
			SubMenuInternalForces[i].setEnabled(false);
		}
		for (int i = 0; i <= SubMenuStresses.length - 1; i += 1)
		{
			SubMenuStresses[i].setEnabled(false);
			SubMenuStrains[i].setEnabled(false);
		}
		SaveResults.setEnabled(false);
    }
}
