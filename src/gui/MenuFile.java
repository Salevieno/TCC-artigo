package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import main.MainTCC;
import main.MenuFunctions;
import utilidades.Util;

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
				String filename = MainTCC.getMenus().getSaveLoadFile().run().getText();
				MenuFunctions.SaveFile(filename, Menus.getMainCanvas(), MenuFunctions.Struct, MenuFunctions.Node, MenuFunctions.Elem,
						MenuFunctions.Sup, MenuFunctions.ConcLoad, MenuFunctions.DistLoad, MenuFunctions.NodalDisp, MenuFunctions.matTypes,
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
		Save.setForeground(Menus.getPalette()[3]);
		Load.setForeground(Menus.getPalette()[3]);
		FileMenu.add(Save);
		FileMenu.add(Load);
	}
	

	public static void loadStructure()
	{
		MenuFunctions.ResetStructure();
		
		String filename = MainTCC.getMenus().getSaveLoadFile().run().getText();
		MenuFunctions.LoadFile("", filename);
		Menus.getMainCanvas().setDim(new double[] {1.2*Util.FindMaxInPos(MenuFunctions.Struct.getCoords(), 0), 1.2*Util.FindMaxInPos(MenuFunctions.Struct.getCoords(), 1)});
		MainTCC.getMenus().setRunAnalysis(MenuFunctions.CheckIfAnalysisIsReady());
		MainTCC.getMenus().showCanvasOn() ;
		MainTCC.getMenus().showGrid() ;
		MainTCC.getMenus().showMousePos() ;
		MenuFunctions.NodeView();
		MenuFunctions.ElemView();
		MenuFunctions.ElemContourView();
		MenuFunctions.SupView();
		MenuFunctions.ConcLoadsView();
		MenuFunctions.DistLoadsView();
		MenuFunctions.NodalDispsView();
		MainTCC.getMenus().setStepIsComplete(MenuFunctions.CheckSteps());
		MainTCC.getMenus().DisableButtons();
		MainTCC.getMenus().EnableButtons();
		MainTCC.getMenus().updateInstructionPanel();
	}
	
}
