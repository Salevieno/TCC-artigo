package org.example.userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.example.mainTCC.MenuFunctions;
import org.example.structure.Element;
import org.example.view.MainPanel;
import org.example.view.NorthPanel;

public class UpperToolbar extends JPanel
{
    private static final String[] buttonNames = new String[]
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
	private static final Color stdButtonColor = Menus.palette[8];
	private static final Font stdButtonFont = new Font(Font.SANS_SERIF, Font.BOLD, 11) ;

    private List<JButton> buttons = new ArrayList<>();
    private boolean MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn;

    public UpperToolbar()
    {
        this.setLayout(new GridBagLayout());
		this.setBackground(Menus.palette[2]);
		this.setPreferredSize(new Dimension(580, NorthPanel.stdButtonSize.height));

		int[] ButtonLength = new int[] {62, 80, 138, 100, 50, 52, 50, 50};
		
		for (int b = 0; b <= buttonNames.length - 1; b += 1)
		{
			JButton newButton = createStdButton(buttonNames[b], new Dimension(ButtonLength[b], NorthPanel.stdButtonSize.height)) ;
			buttons.add(newButton) ;
		}

		buttons.forEach(button -> button.setEnabled(false)) ;
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
				MenuFunctions.SnipToGridIsOn = true;
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
				MenuFunctions.SnipToGridIsOn = false;
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
					MainPanel.AddDistLoads(MainPanel.structure, MainPanel.loading);
				}
			}
		});
		buttons.get(3).addActionListener(new ActionListener()
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
					MainPanel.AddConcLoads(MainPanel.loading);
				}
				if (NodalDispsAssignmentIsOn)
				{
					MainPanel.AddNodalDisps();
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
				MenuFunctions.Clean(MainPanel.structure, new boolean[] {MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn});
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
        if (!MatAssignmentIsOn && !SecAssignmentIsOn && !DistLoadsAssignmentIsOn) { return ;}
        
        buttons.get(2).setEnabled(false);
        buttons.get(2).setVisible(false);
        
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
            buttons.get(3).setEnabled(false);
            buttons.get(3).setVisible(false);
        }
        Menus.getInstance().getEastPanel().removeBp1Bp2();
        Menus.getInstance().getEastPanel().removeBp1Bp2();
        buttons.get(6).setEnabled(false);
        buttons.get(6).setVisible(false);
        buttons.get(7).setEnabled(false);
        buttons.get(7).setVisible(false);
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
        Menus.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion() ;
        boolean ReadyForAnalysis = MenuFunctions.CheckIfAnalysisIsReady(MainPanel.structure, MainPanel.loading);
        
        if (ReadyForAnalysis)
        {
            Menus.getInstance().getMenuAnalysis().setRunAnalysis(true) ;
        }
    }

    public void enableMaterialAssignment() { MatAssignmentIsOn = true ;}
    public void enableSectionAssignment() { SecAssignmentIsOn = true ;}
    public void enableSupportAssignment() { SupAssignmentIsOn = true ;}
    public void enableConcLoadAssignment() { ConcLoadsAssignmentIsOn = true ;}
    public void enableDistLoadAssignment() { DistLoadsAssignmentIsOn = true ;}
    public void enableNodalDispAssignment() { NodalDispsAssignmentIsOn = true ;}

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

    public void assignToNodeView()
    {
		buttons.get(3).setEnabled(SupAssignmentIsOn);
		buttons.get(3).setVisible(SupAssignmentIsOn);
		buttons.get(6).setEnabled(SupAssignmentIsOn);
		buttons.get(6).setVisible(SupAssignmentIsOn);
		buttons.get(7).setEnabled(SupAssignmentIsOn);
		buttons.get(7).setVisible(SupAssignmentIsOn);
    }

    public void assignToElemView()
    {
		buttons.get(2).setEnabled(SupAssignmentIsOn);
		buttons.get(2).setVisible(SupAssignmentIsOn);
		buttons.get(6).setEnabled(SupAssignmentIsOn);
		buttons.get(6).setVisible(SupAssignmentIsOn);
		buttons.get(7).setEnabled(SupAssignmentIsOn);
		buttons.get(7).setVisible(SupAssignmentIsOn);
    }

    
	public boolean[] getAqueleBooleanGrande() { return new boolean[] {MatAssignmentIsOn, SecAssignmentIsOn, SupAssignmentIsOn, ConcLoadsAssignmentIsOn, DistLoadsAssignmentIsOn, NodalDispsAssignmentIsOn} ;}

}
