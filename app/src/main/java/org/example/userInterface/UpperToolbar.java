package org.example.userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.example.Main;
import org.example.mainTCC.MainPanel;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.Element;
import org.example.structure.Material;
import org.example.structure.Section;
import org.example.view.Assignable;
import org.example.view.CentralPanel;
import org.example.view.NorthPanel;

public class UpperToolbar extends JPanel
{
    private static final String[] buttonNames ;
	private static final Color stdButtonColor ;
	private static final Font stdButtonFont ;

    private List<JButton> buttons = new ArrayList<>();
	private Assignable assignable ;

	static
	{
		buttonNames = new String[]
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
		stdButtonColor = Main.palette[8];
		stdButtonFont = new Font(Font.SANS_SERIF, Font.BOLD, 11) ;
	}

    public UpperToolbar()
    {
        this.setLayout(new GridBagLayout());
		this.setBackground(Main.palette[2]);
		this.setPreferredSize(new Dimension(580, NorthPanel.stdButtonSize.height));

		int[] ButtonLength = new int[] {62, 80, 138, 100, 50, 52, 50, 50};
		
		for (int b = 0; b <= buttonNames.length - 1; b += 1)
		{
			JButton newButton = createStdButton(buttonNames[b], new Dimension(ButtonLength[b], NorthPanel.stdButtonSize.height)) ;
			buttons.add(newButton) ;
		}

		// buttons.forEach(button -> button.setEnabled(false)) ;
		buttons.forEach(button -> button.setVisible(false)) ;
		buttons.forEach(button -> button.setFocusable(false)) ;
		
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
		
		buttons.get(0).addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MainPanel.getInstance().getCentralPanel().activateSnipToClick() ;
				buttons.get(0).setEnabled(false);
				buttons.get(0).setVisible(false);
				buttons.get(1).setEnabled(true);
				buttons.get(1).setVisible(true);
			}
		});
		buttons.get(1).addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MainPanel.getInstance().getCentralPanel().deactivateSnipToClick() ;
				buttons.get(1).setEnabled(false);
				buttons.get(1).setVisible(false);
				buttons.get(0).setEnabled(true);
				buttons.get(0).setVisible(true);
			}
		});
		buttons.get(2).addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				int selectedID = MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID() ;
				if (Assignable.materials.equals(assignable))
				{
					Material mat = MainPanel.getInstance().getWestPanel().getListsPanel().getMatTypes().get(selectedID) ;
					CentralPanel.structure.getMesh().assignMaterials(mat) ;
				}
				if (Assignable.sections.equals(assignable))
				{
					Section sec = MainPanel.getInstance().getWestPanel().getListsPanel().getSecTypes().get(selectedID) ;
					CentralPanel.structure.getMesh().assignSections(sec) ;
				}
				if (Assignable.distLoads.equals(assignable))
				{
					MainPanel.getInstance().getCentralPanel().AddDistLoads(CentralPanel.structure, CentralPanel.loading, CentralPanel.structure.getMesh().getElements(), MenuFunctions.DistLoadType);
				}
			}
		});
		buttons.get(3).addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (Assignable.supports.equals(assignable))
				{
					MainPanel.getInstance().getCentralPanel().AddSupports();					
				}
				if (Assignable.concLoads.equals(assignable))
				{
					MainPanel.getInstance().getCentralPanel().AddConcLoads(CentralPanel.loading, CentralPanel.structure.getMesh().getSelectedNodes(), MenuFunctions.concLoadTypes);
				}
				if (Assignable.nodalDisps.equals(assignable))
				{
					MainPanel.getInstance().getCentralPanel().AddNodalDisps();
				}
			}
		});
		buttons.get(4).addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.DiagramScales[1] += 0.1*MenuFunctions.DiagramScales[1];
			}
		});
		buttons.get(5).addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.DiagramScales[1] += -0.1*MenuFunctions.DiagramScales[1];
			}
		});
		buttons.get(6).addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				assignToElement() ;
			}
		});
		buttons.get(7).addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFunctions.Clean(CentralPanel.structure, assignable);
			}
		});
		
		buttons.forEach(this::add) ;
    }
	
    
	public static JButton createStdButton(String Text, Dimension size)
	{
		JButton NewButton = new JButton(Text);
		NewButton.setFont(stdButtonFont);
		NewButton.setVerticalAlignment(0);
		NewButton.setHorizontalAlignment(0);
		NewButton.setBackground(stdButtonColor);
		NewButton.setPreferredSize(size);
		NewButton.setMargin(new Insets(2, 2, 2, 2));
		return NewButton;
	}

    private void assignToElement()
    {
        // if (!MatAssignmentIsOn && !SecAssignmentIsOn && !DistLoadsAssignmentIsOn) { return ;}
        
        buttons.get(2).setEnabled(false);
        buttons.get(2).setVisible(false);
        
        if (Assignable.materials.equals(assignable))
        {
            Element.createRandomMatColors(MainPanel.getInstance().getWestPanel().getListsPanel().getMatTypes());
            // for (Element elem : MainPanel.structure.getMesh().getElements())
            // {
                // int colorID = MenuFunctions.matTypes.indexOf(elem.getMat()) ;
                // if (colorID != -1)
                // {
                //     elem.setMatColor(Element.matColors[colorID]);
                // }
            // }
        }
        if (Assignable.sections.equals(assignable))
        {
            // Element.setSecColors(MenuFunctions.secTypes);
            // for (Element elem : MainPanel.structure.getMesh().getElements())
            // {
            //     int colorID = MenuFunctions.secTypes.indexOf(elem.getSec()) ;
            //     elem.setSecColor(Element.SecColors[colorID]);
            // }
        }
    
        if (Assignable.supports.equals(assignable) | Assignable.concLoads.equals(assignable) | Assignable.nodalDisps.equals(assignable))
        {
            buttons.get(3).setEnabled(false);
            buttons.get(3).setVisible(false);
        }
        MainPanel.getInstance().getEastPanel().removeBp1Bp2();
        MainPanel.getInstance().getEastPanel().removeBp1Bp2();
        buttons.get(6).setEnabled(false);
        buttons.get(6).setVisible(false);
        buttons.get(7).setEnabled(false);
        buttons.get(7).setVisible(false);
        CentralPanel.structure.getMesh().unselectAllNodes() ;
        CentralPanel.structure.getMesh().unselectAllElements() ;
		assignable = null ;
        CentralPanel.nodeSelectionIsActive = false;
        CentralPanel.elemSelectionIsActive = false;
        MainPanel.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion(CentralPanel.structure, CentralPanel.loading) ;
        MenuBar.getInstance().getMenuAnalysis().updateIsReadyForAnalysis(CentralPanel.structure, CentralPanel.loading) ;
    }

    public void enableMaterialAssignment() { assignable = Assignable.materials ;}
    public void enableSectionAssignment() { assignable = Assignable.sections ;}
    public void enableSupportAssignment() { assignable = Assignable.supports ;}
    public void enableConcLoadAssignment() { assignable = Assignable.concLoads ;}
    public void enableDistLoadAssignment() { assignable = Assignable.distLoads ;}
    public void enableNodalDispAssignment() { assignable = Assignable.nodalDisps ;}

	public void showButtonSnipToGridOn() { buttons.get(0).setVisible(true) ;}
	
	public void enableButtonsSnipToGrid()
	{
		buttons.get(0).setEnabled(true) ;
		buttons.get(0).setVisible(true) ;
		buttons.get(1).setEnabled(true) ;
		buttons.get(1).setVisible(true) ;
	}

	public void enableButtonsScale()
	{
		buttons.get(4).setEnabled(true) ;
		buttons.get(4).setVisible(true) ;
		buttons.get(5).setEnabled(true) ;
		buttons.get(5).setVisible(true) ;
	}

	public void disableButtonsSnipToGrid()
	{
		buttons.get(0).setEnabled(false) ;
		buttons.get(0).setVisible(false) ;
		buttons.get(1).setEnabled(false) ;
		buttons.get(1).setVisible(false) ;
	}

    public void assignToNodeView()
    {
		buttons.get(3).setEnabled(true);
		buttons.get(3).setVisible(true);
		buttons.get(6).setEnabled(true);
		buttons.get(6).setVisible(true);
		buttons.get(7).setEnabled(true);
		buttons.get(7).setVisible(true);
    }

    public void assignToElemView()
    {
		buttons.get(2).setEnabled(true);
		buttons.get(2).setVisible(true);
		buttons.get(6).setEnabled(true);
		buttons.get(6).setVisible(true);
		buttons.get(7).setEnabled(true);
		buttons.get(7).setVisible(true);
    }

    
	// public boolean[] getAqueleBooleanGrande() { return new boolean[] {MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn} ;}

	public Assignable getAssignable() { return assignable ;}
}
