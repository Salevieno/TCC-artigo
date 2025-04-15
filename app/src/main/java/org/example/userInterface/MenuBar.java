package org.example.userInterface;
import javax.swing.JMenuBar;

import org.example.mainTCC.MenuFunctions;

public class MenuBar extends JMenuBar
{

	private static final MenuBar instance ;

	private MenuStructure menuStructure ;
    private MenuView viewMenu ;
	private MenuAnalysis analysisMenu ;
	private MenuResults resultsMenu ;
	private MenuEspecial especialMenu;	


	static
	{
		instance = new MenuBar();
	}
	
	private MenuBar()
	{
		menuStructure = new MenuStructure();
		viewMenu = new MenuView() ;
		analysisMenu = new MenuAnalysis() ;
		resultsMenu = new MenuResults() ;
		especialMenu = new MenuEspecial() ;
		
		this.add(MenuFile.create());
		this.add(menuStructure);
		this.add(viewMenu);
		this.add(analysisMenu);
		this.add(resultsMenu);
		this.add(especialMenu);
	}
		
	public void updateEnabledMenus()
	{
		boolean AnalysisIsComplete = MenuFunctions.isAnalysisIsComplete();
		MenuBar.getInstance().getMenuStructure().updateEnabledSubMenus() ;
		if (AnalysisIsComplete)
		{
			viewMenu.enableDofNumberView() ;
			resultsMenu.enableButtons() ;
		}
	}
	
	public void DisableButtons()
	{
		viewMenu.disableDofNumberView() ;
		resultsMenu.disableButtons() ;
	}

	public MenuStructure getMenuStructure() { return menuStructure ;}
	public MenuAnalysis getMenuAnalysis() { return analysisMenu ;}
	public static MenuBar getInstance() { return instance ;}

}