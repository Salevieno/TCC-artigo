package org.example.view;

import java.awt.GridLayout;

import javax.swing.JPanel;

import org.example.userInterface.InstructionsPanel;
import org.example.userInterface.ToolbarButtons;
import org.example.userInterface.ToolbarResults;

public class WestPanel extends JPanel
{

	private final InstructionsPanel instructionsPanel = new InstructionsPanel() ;
	private final ToolbarButtons toolbarButtons = new ToolbarButtons() ;
	private final ToolbarResults toolbarResults = new ToolbarResults() ;
	private ListPanel listsPanel = new ListPanel() ;

    public WestPanel()
    {        
		this.setLayout(new GridLayout(0, 1)) ;
		this.add(toolbarButtons);
		this.add(toolbarResults);
		this.add(listsPanel);
		this.add(instructionsPanel);
    }

    public InstructionsPanel getInstructionsPanel() { return instructionsPanel ;}

    public ToolbarButtons getToolbarButtons() { return toolbarButtons ;}

    public ToolbarResults getToolbarResults() { return toolbarResults ;}

    public ListPanel getListsPanel() { return listsPanel ;}

    

}
