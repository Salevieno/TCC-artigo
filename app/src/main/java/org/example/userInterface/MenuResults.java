package org.example.userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.Main;
import org.example.mainTCC.MainPanel;
import org.example.output.SaveOutput;
import org.example.service.MenuViewService;
import org.example.structure.Element;
import org.example.structure.Reactions;
import org.example.structure.Structure;

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
				ResultsMenuSaveResults(MainPanel.getInstance().getCentralPanel().getStructure());
			}
		});
		SaveLoadDispCurve.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				SaveLoadDispCurve(MainPanel.getInstance().getCentralPanel().getStructure());
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
					// CentralPanel.SelectedVar = Util.ElemPosInArray(MainPanel.getInstance().getCentralPanel().getStructure().getMesh().getElements().get(0).getDOFs(), d2);
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

					// TODO check
					// CentralPanel.SelectedVar = Util.ElemPosInArray(MainPanel.getInstance().getCentralPanel().getStructure().getMesh().getElements().get(0).getDOFs(), s2);
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
					// CentralPanel.SelectedVar = Util.ElemPosInArray(MainPanel.getInstance().getCentralPanel().getStructure().getMesh().getElements().get(0).getDOFs(), s2);
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
					// CentralPanel.SelectedVar = Util.ElemPosInArray(MainPanel.getInstance().getCentralPanel().getStructure().getMesh().getElements().get(0).getDOFs(), f2);
					//DrawInternalForcesContours(Util.ElemPosInArray(Elem[0].getDOFs(), f2));
					//ResetE1Panel();
				}
			});
			SubMenuInternalForces[f].setEnabled(false);
			SubMenuInternalForces[f].setForeground(Main.palette[7]);
			InternalForcesContours.add(SubMenuInternalForces[f]);
		}
    }

	public static void SaveLoadDispCurve(Structure structure)
	{
		if (structure.getResultDiagrams().getSelectedVar() <= -1) { return ;}
		
		int nodeid = structure.getMesh().getSelectedNodes().get(0).getID();
		double[][][] loaddisp = structure.getMesh().getNodes().get(nodeid).getLoadDisp();
		String[] Sections = new String[] {"Deslocamentos", "Fator de carga"};
		String[][][] vars = new String[Sections.length][structure.getMesh().getNodes().get(nodeid).getDofs().length][loaddisp[0][0].length];
		for (int sec = 0; sec <= Sections.length - 1; sec += 1)
		{
			for (int dof = 0; dof <= structure.getMesh().getNodes().get(nodeid).getDofs().length - 1; dof += 1)
			{
				for (int loadinc = 0; loadinc <= loaddisp[dof][sec].length - 1; loadinc += 1)
				{
					vars[sec][dof][loadinc] = String.valueOf(loaddisp[dof][sec][loadinc]);
				}
			}
		}
		SaveOutput.SaveOutput(structure.getName(), Sections, vars);
	}

	public static void ResultsMenuSaveResults(Structure structure)
	{
		String[] Sections = new String[] {"Min Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Max Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Deslocamentos \nno	w (m)	Tx (rad)	Ty (rad)", "Min Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Max Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Forcas Internas \nno	Fz (kN)	Mx (kNm)	My (kNm)", "Reacoes \nno	Fx (kN)	Fy (kN)	Fz (kN)	Mx (kNm)	My (kNm)	Mz (kNm)", "Soma das reacoes \nFx (kN)	Fy (kN)	Fz (kN)	Mx (kNm)	My (kNm)	Mz (kNm)"};
		String[][][] vars = new String[Sections.length][][];
		vars[0] = new String[1][3];
		vars[1] = new String[1][3];
		vars[2] = new String[structure.getMesh().getNodes().size()][];
		vars[3] = new String[1][3];
		vars[4] = new String[1][3];
		vars[5] = new String[structure.getMesh().getNodes().size()][];
		vars[6] = new String[structure.getReactions().length][];
		vars[7] = new String[1][Reactions.SumReactions.length];
		for (int sec = 0; sec <= Sections.length - 1; sec += 1)
		{
			if (sec == 0)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(structure.getResults().getDispMin()[dof]);
				}
			}
			if (sec == 1)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(structure.getResults().getDispMax()[dof]);
				}
			}
			if (sec == 3)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(structure.getResults().getInternalForcesMin()[dof]);
				}
			}
			if (sec == 4)
			{
				for (int dof = 0; dof <= 3 - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(structure.getResults().getInternalForcesMax()[dof]);
				}
			}
			for (int node = 0; node <= vars[sec].length - 1; node += 1)
			{
				if (sec == 2)
				{
					vars[sec][node] = new String[structure.getMesh().getNodes().get(node).getDOFType().length + 1];
					vars[sec][node][0] = String.valueOf(node);
					for (int dof = 0; dof <= structure.getMesh().getNodes().get(node).getDOFType().length - 1; dof += 1)
					{
						if (-1 < structure.getMesh().getNodes().get(node).getDofs()[dof])
						{
							vars[sec][node][dof + 1] = String.valueOf(structure.getU()[structure.getMesh().getNodes().get(node).getDofs()[dof]]);
						}
						else
						{
							vars[sec][node][dof + 1] = String.valueOf(0);
						}
					}
				}
				if (sec == 5)
				{
					vars[sec][node] = new String[structure.getMesh().getNodes().get(node).getDOFType().length + 1];
					vars[sec][node][0] = String.valueOf(node);
					for (int i = 0; i <= structure.getMesh().getElements().size() - 1; i += 1)
					{
						Element elem = structure.getMesh().getElements().get(i);
						for (int elemnode = 0; elemnode <= elem.getExternalNodes().size() - 1; elemnode += 1)
						{
							if (node == elem.getExternalNodes().get(elemnode).getID())
							{
								for (int dof = 0; dof <= structure.getMesh().getNodes().get(node).getDOFType().length - 1; dof += 1)
								{
									vars[sec][node][dof + 1] = String.valueOf(elem.getIntForces()[elemnode*structure.getMesh().getNodes().get(node).getDOFType().length + dof]);
								}
							}
						}
					}
				}
				if (sec == 6)
				{
					vars[sec][node] = new String[structure.getReactions()[node].getLoads().length + 1];
					vars[sec][node][0] = String.valueOf(node);
					for (int dof = 0; dof <= structure.getReactions()[node].getLoads().length - 1; dof += 1)
					{
						vars[sec][node][dof + 1] = String.valueOf(structure.getReactions()[node].getLoads()[dof]);
					}
				}
			}
			if (sec == 7)
			{
				for (int dof = 0; dof <= Reactions.SumReactions.length - 1; dof += 1)
				{
					vars[sec][0][dof] = String.valueOf(Reactions.SumReactions[dof]);
				}
			}
		}
		SaveOutput.SaveOutput(structure.getName(), Sections, vars);
	}

    public void enableButtons()
    {
		List<Element> elems = MainPanel.getInstance().getCentralPanel().getStructure().getMesh() != null ? MainPanel.getInstance().getCentralPanel().getStructure().getMesh().getElements() : null;
        
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
