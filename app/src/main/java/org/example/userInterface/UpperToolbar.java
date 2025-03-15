package org.example.userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.example.mainTCC.MenuFunctions;
import org.example.structure.Element;
import org.example.view.MainPanel;

public class UpperToolbar extends JPanel
{
    private static final String[] ButtonNames = new String[]
    {
        "Ligar ima",
        "Desligar ima",
        "Atribuir aos elementos",
        "Atribuir aos nos",
        "+escala",
        "-escala",
        "Concluir",
        "Limpar"
    };

    JButton[] buttons = new JButton[ButtonNames.length];
    private boolean MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn;

    public UpperToolbar()
    {
        this.setLayout(new GridBagLayout());
		this.setBackground(Menus.palette[2]);
		this.setPreferredSize(new Dimension(580, 30));

		int[] ButtonLength = new int[] {62, 80, 138, 100, 50, 52, 50, 50};
		Color ButtonBgColor = Menus.palette[8];
		
		for (int b = 0; b <= buttons.length - 1; b += 1)
		{
			buttons[b] = ToolbarButtons.AddButton(ButtonNames[b], new int[2], new int[] {ButtonLength[b], 30}, 11, new int[] {2, 2, 2, 2}, ButtonBgColor);
			buttons[b].setEnabled(false);
			buttons[b].setVisible(false);
			buttons[b].setFocusable(false);
		}
		
		/* Buttons: 
		 * 0: snip to grid on
		 * 1: snip to grid off
		 * 2: add materials, sections and  dist loads to elements
		 * 3: add supports, concentrated loads and nodal displacements to nodes
		 * 4: increase diagrams scale
		 * 5: decrease diagrams scale
		 * 6: done
		 * 7: clean
		 * */
		
		buttons[0].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.SnipToGridIsOn = true;
				buttons[0].setEnabled(false);
				buttons[0].setVisible(false);
				buttons[1].setEnabled(true);
				buttons[1].setVisible(true);
			}
		});
		buttons[1].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.SnipToGridIsOn = false;
				buttons[1].setEnabled(false);
				buttons[1].setVisible(false);
				buttons[0].setEnabled(true);
				buttons[0].setVisible(true);
			}
		});
		buttons[2].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (MatAssignmentIsOn)
				{
					MainPanel.AddMaterialToElements(MenuFunctions.SelectedElems, MenuFunctions.matTypes.get(MainPanel.selectedMatID));
				}
				if (SecAssignmentIsOn)
				{
					MainPanel.AddSectionsToElements(MenuFunctions.SelectedElems, MenuFunctions.secTypes.get(MainPanel.selectedSecID));
				}
				if (DistLoadsAssignmentIsOn)
				{
					MainPanel.AddDistLoads();
				}
			}
		});
		buttons[3].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (SupAssignmentIsOn)
				{
					MainPanel.AddSupports();					
				}
				if (ConcLoadsAssignmentIsOn)
				{
					MainPanel.AddConcLoads();
				}
				if (NodalDispsAssignmentIsOn)
				{
					MainPanel.AddNodalDisps();
				}
			}
		});
		buttons[4].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.DiagramScales[1] += 0.1*MenuFunctions.DiagramScales[1];
			}
		});
		buttons[5].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.DiagramScales[1] += -0.1*MenuFunctions.DiagramScales[1];
			}
		});
		buttons[6].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				assignToElement() ;
			}
		});
		buttons[7].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.Clean(MainPanel.structure, new boolean[] {MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn});
			}
		});
		
		for (int b = 0; b <= buttons.length - 1; b += 1)
		{
			this.add(buttons[b]);
		}
    }

    private void assignToElement()
    {
        if (!MatAssignmentIsOn && !SecAssignmentIsOn && !DistLoadsAssignmentIsOn) { return ;}
        
        buttons[2].setEnabled(false);
        buttons[2].setVisible(false);
        
        if (MatAssignmentIsOn)
        {
            System.out.println("Mat types at menus: " + MenuFunctions.matTypes);
            Element.createMatColors(MenuFunctions.matTypes);
            for (Element elem : MainPanel.structure.getMesh().getElements())
            {
                int colorID = MenuFunctions.matTypes.indexOf(elem.getMat()) ;
                if (colorID != -1)
                {
                    elem.setMatColor(Element.matColors[colorID]);
                }
            }
        }
        if (SecAssignmentIsOn)
        {
            Element.setSecColors(MenuFunctions.secTypes);
            for (Element elem : MainPanel.structure.getMesh().getElements())
            {
                int colorID = MenuFunctions.secTypes.indexOf(elem.getSec()) ;
                elem.setSecColor(Element.SecColors[colorID]);
            }
        }
    
        if (SupAssignmentIsOn | ConcLoadsAssignmentIsOn | NodalDispsAssignmentIsOn)
        {
            buttons[3].setEnabled(false);
            buttons[3].setVisible(false);
        }
        Menus.getInstance().E1.remove(Menus.getInstance().bp1);
        Menus.getInstance().E1.remove(Menus.getInstance().bp2);
        buttons[6].setEnabled(false);
        buttons[6].setVisible(false);
        buttons[7].setEnabled(false);
        buttons[7].setVisible(false);
        MenuFunctions.selectedNodes = null;
        MenuFunctions.SelectedElems = null;
        MatAssignmentIsOn = false;
        SecAssignmentIsOn = false;
        SupAssignmentIsOn = false;
        ConcLoadsAssignmentIsOn = false;
        DistLoadsAssignmentIsOn = false;
        NodalDispsAssignmentIsOn = false;
        MenuFunctions.NodeSelectionIsOn = false;
        MenuFunctions.ElemSelectionIsOn = false;
        Menus.getInstance().getInstructionsPanel().updateStepsCompletion() ;
        boolean ReadyForAnalysis = MenuFunctions.CheckIfAnalysisIsReady(MainPanel.structure, MainPanel.loading);
        
        if (ReadyForAnalysis)
        {
            Menus.getInstance().setRunAnalysis(true) ;
        }
    }

    public void enableMaterialAssignment() { MatAssignmentIsOn = true ;}
    public void enableSectionAssignment() { SecAssignmentIsOn = true ;}
    public void enableSupportAssignment() { SupAssignmentIsOn = true ;}
    public void enableConcLoadAssignment() { ConcLoadsAssignmentIsOn = true ;}
    public void enableDistLoadAssignment() { DistLoadsAssignmentIsOn = true ;}
    public void enableNodalDispAssignment() { NodalDispsAssignmentIsOn = true ;}

	public void showButtonSnipToGridOn() { buttons[0].setVisible(true) ;}
	
	public void enableButtonsSnipToGrid()
	{
		buttons[0].setEnabled(true) ;
		buttons[0].setVisible(true) ;
		buttons[1].setEnabled(true) ;
		buttons[1].setVisible(true) ;
	}

	public void enableButtonsScale()
	{
		buttons[4].setEnabled(true) ;
		buttons[4].setVisible(true) ;
		buttons[5].setEnabled(true) ;
		buttons[5].setVisible(true) ;
	}

    public void assignToNodeView()
    {
		buttons[3].setEnabled(SupAssignmentIsOn);
		buttons[3].setVisible(SupAssignmentIsOn);
		buttons[6].setEnabled(SupAssignmentIsOn);
		buttons[6].setVisible(SupAssignmentIsOn);
		buttons[7].setEnabled(SupAssignmentIsOn);
		buttons[7].setVisible(SupAssignmentIsOn);
    }

    public void assignToElemView()
    {
		buttons[2].setEnabled(SupAssignmentIsOn);
		buttons[2].setVisible(SupAssignmentIsOn);
		buttons[6].setEnabled(SupAssignmentIsOn);
		buttons[6].setVisible(SupAssignmentIsOn);
		buttons[7].setEnabled(SupAssignmentIsOn);
		buttons[7].setVisible(SupAssignmentIsOn);
    }

    
	public boolean[] getAqueleBooleanGrande() { return new boolean[] {MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn} ;}

}
