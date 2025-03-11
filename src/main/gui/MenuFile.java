package main.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import main.utilidades.Util;
import main.view.MainPanel;
import main.mainTCC.MainTCC;
import main.mainTCC.MenuFunctions;
import main.structure.Structure;

public abstract class MenuFile
{
	private static JMenu FileMenu = new JMenu("Arquivo");
	private static JMenuItem Save, Load;

	public static JMenu create()
	{
		FileMenu.setMnemonic(KeyEvent.VK_A);
		AddFileMenuItems() ;
		return FileMenu ;
	}
	
	public static void AddFileMenuItems()
	{
		/* Defining items in the menu File */
		Save = new JMenuItem("Save file", KeyEvent.VK_S);
		Load = new JMenuItem("Load file", KeyEvent.VK_L);
		Save.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String filename = Menus.getInstance().getSaveLoadFile().run().getText();
				MenuFunctions.SaveFile(filename, Menus.getMainCanvas(), MainPanel.structure, MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getMesh().getElements(),
						MainPanel.structure.getSupports(), MainPanel.loading, MenuFunctions.matTypes,
						MenuFunctions.secTypes);
			}
		});
		Load.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				loadStructure() ;
			}
		});
		Save.setForeground(Menus.palette[3]);
		Load.setForeground(Menus.palette[3]);
		FileMenu.add(Save);
		FileMenu.add(Load);
	}
	

	public static void loadStructure()
	{
		
		MainPanel.structure = new Structure(null, null, null);
		MainPanel.loading.clearLoads() ;
		MenuFunctions.resetDisplay();
		
		String filename = Menus.getInstance().getSaveLoadFile().run().getText();
		MainPanel.structure = MenuFunctions.LoadFile("", filename);
		MainPanel.structure.updateMaxCoords() ;
		Menus.getMainCanvas().setDimension(new double[] {1.2 * MainPanel.structure.getMaxCoords().x, 1.2 * MainPanel.structure.getMaxCoords().y, 1});
		Menus.getInstance().setRunAnalysis(MenuFunctions.CheckIfAnalysisIsReady(MainPanel.structure, MainPanel.loading));
		Menus.getInstance().showCanvasOn() ;
		Menus.getInstance().showGrid() ;
		Menus.getInstance().showMousePos() ;
		MenuFunctions.NodeView();
		MenuFunctions.ElemView();
		MenuFunctions.ElemContourView();
		MenuFunctions.SupView();
		MenuFunctions.ConcLoadsView();
		MenuFunctions.DistLoadsView();
		MenuFunctions.NodalDispsView();
		Menus.getInstance().setStepIsComplete(MenuFunctions.CheckSteps(MainPanel.structure));
		Menus.getInstance().DisableButtons();
		Menus.getInstance().EnableButtons();
		Menus.getInstance().updateInstructionPanel();
	}
	
}
