package org.example.userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.example.Main;
import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.Loading;
import org.example.mainTCC.MainPanel;
import org.example.structure.Material;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.utilidades.ButtonOnOff;
import org.example.view.Assignable;
import org.example.view.CentralPanel;
import org.example.view.NorthPanel;

public class UpperToolbar extends JPanel
{

	private Assignable assignable ;
	private final ButtonOnOff buttonMagnet ;
	private final JButton buttonAssign ;
	private final JButton buttonDone ;
	private final JButton buttonClean ;

	private static final Color stdButtonColor ;
	private static final Font stdButtonFont ;
	private static final String pathPrefix ;


	static
	{
		stdButtonColor = Main.palette[8];
		stdButtonFont = new Font(Font.SANS_SERIF, Font.BOLD, 11) ;
		pathPrefix = "./assets/" ;
	}

    public UpperToolbar()
    {
        this.setLayout(new GridBagLayout());
		this.setBackground(Main.palette[2]);
		this.setPreferredSize(new Dimension(580, NorthPanel.stdButtonSize.height));

		buttonMagnet = new ButtonOnOff(pathPrefix + "BtnMagnetOff.png", pathPrefix + "BtnMagnetOn.png") ;
		buttonAssign = createStdButton("Atribuir", new Dimension(50, NorthPanel.stdButtonSize.height)) ;
		buttonDone = createStdButton("Concluir", new Dimension(50, NorthPanel.stdButtonSize.height)) ;
		buttonClean = createStdButton("Limpar", new Dimension(50, NorthPanel.stdButtonSize.height)) ;

		buttonMagnet.addActionListener(e -> buttonMagnet.switchState()) ;
		buttonAssign.addActionListener(e -> assign(assignable)) ;
		buttonDone.addActionListener(e -> finishAssignment()) ;
		buttonClean.addActionListener(e -> clean(MainPanel.getInstance().getCentralPanel().getStructure(), MainPanel.getInstance().getCentralPanel().getLoading(), assignable)) ;

		this.add(buttonMagnet) ;
		this.add(buttonAssign) ;
		this.add(buttonDone) ;
		this.add(buttonClean) ;
    }
	
	private static void assign(Assignable assignable)
	{
		int assignableID = MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID() ;
		switch (assignable)
		{
			case materials:
				Material mat = MainPanel.getInstance().getWestPanel().getListsPanel().getMatTypes().get(assignableID) ;
				MainPanel.getInstance().getCentralPanel().getStructure().getMesh().assignMaterials(mat) ;
				return ;

			case sections:
				Section sec = MainPanel.getInstance().getWestPanel().getListsPanel().getSecTypes().get(assignableID) ;
				MainPanel.getInstance().getCentralPanel().getStructure().getMesh().assignSections(sec) ;
				return ;
		
			case supports:			
				MainPanel.getInstance().getCentralPanel().AddSupports();
				return ;
			
			case concLoads:
				MainPanel.getInstance().getCentralPanel().AddConcLoads(MainPanel.getInstance().getCentralPanel().getLoading(), MainPanel.getInstance().getCentralPanel().getStructure().getMesh().getSelectedNodes(), ConcLoad.getTypes());
				return ;
		
			case distLoads:
				MainPanel.getInstance().getCentralPanel().AddDistLoads(MainPanel.getInstance().getCentralPanel().getStructure(),
										MainPanel.getInstance().getCentralPanel().getLoading(),
										MainPanel.getInstance().getCentralPanel().getStructure().getMesh().getElements(), DistLoad.getTypes());
				return ;
	
			case nodalDisps:			
				MainPanel.getInstance().getCentralPanel().AddNodalDisps();
				return ;

			default: return ;
		}
	}
    
	public static JButton createStdButton(String Text, Dimension size)
	{
		JButton newButton = new JButton(Text);
		newButton.setFont(stdButtonFont);
		newButton.setVerticalAlignment(0);
		newButton.setHorizontalAlignment(0);
		newButton.setBackground(stdButtonColor);
		newButton.setPreferredSize(size);
		newButton.setMargin(new Insets(2, 2, 2, 2));
		newButton.setVisible(false) ;
		newButton.setFocusable(false) ;
		return newButton;
	}

    private void finishAssignment()
    {
		assignable = null ;
        disableAssignmentButtons() ;
        CentralPanel.deactivateNodeSelection() ;
        CentralPanel.deactivateElemSelection() ;
        MainPanel.getInstance().getCentralPanel().getStructure().getMesh().unselectAllNodes() ;
        MainPanel.getInstance().getCentralPanel().getStructure().getMesh().unselectAllElements() ;
        MainPanel.getInstance().getEastPanel().removeBp1Bp2();
        MainPanel.getInstance().getEastPanel().removeBp1Bp2();
        MainPanel.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion(MainPanel.getInstance().getCentralPanel().getStructure(), MainPanel.getInstance().getCentralPanel().getLoading()) ;
        MenuBar.getInstance().getMenuAnalysis().updateIsReadyForAnalysis(MainPanel.getInstance().getCentralPanel().getStructure(), MainPanel.getInstance().getCentralPanel().getLoading()) ;
    }

	public static void clean(Structure structure, Loading loading, Assignable assignable)
	{
		switch(assignable)
		{
			case materials:
				structure.getMesh().getElements().forEach(elem -> elem.setMat(null)) ;
				return ;
			
			case sections:
				structure.getMesh().getElements().forEach(elem -> elem.setSec(null)) ;
				return ;
		
			case supports:
				structure.getMesh().getNodes().forEach(node -> node.setSup(null)) ;
				structure.removeSupports() ;
				return ;
		
			case concLoads:
				loading.setConcLoads(new ArrayList<>()) ;
				return ;
		
			case distLoads:
				loading.setDistLoads(new ArrayList<>()) ;
				return ;
		
			case nodalDisps:
				loading.setNodalDisps(new ArrayList<>()) ;
				return ;

			default: return ;
		}
	}	

    public void enableMaterialAssignment() { assignable = Assignable.materials ;}
    public void enableSectionAssignment() { assignable = Assignable.sections ;}
    public void enableSupportAssignment() { assignable = Assignable.supports ;}
    public void enableConcLoadAssignment() { assignable = Assignable.concLoads ;}
    public void enableDistLoadAssignment() { assignable = Assignable.distLoads ;}
    public void enableNodalDispAssignment() { assignable = Assignable.nodalDisps ;}

	public void enableMagnet() { buttonMagnet.setVisible(true) ;}
	public void disableMagnet() { buttonMagnet.setVisible(false) ;}

    public void enableAssignmentButtons()
    {
		buttonAssign.setVisible(true);
		buttonDone.setVisible(true);
		buttonClean.setVisible(true);
    }

    public void disableAssignmentButtons()
    {
		buttonAssign.setVisible(false);
		buttonDone.setVisible(false);
		buttonClean.setVisible(false);
    }

	public Assignable getAssignable() { return assignable ;}

	public boolean getButtonMagnetState() { return buttonMagnet.getActive() ;}
}
