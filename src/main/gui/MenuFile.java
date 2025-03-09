package main.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import main.utilidades.Util;
import main.mainTCC.MainPanel;
import main.mainTCC.MainTCC;
import main.mainTCC.MenuFunctions;

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
				MenuFunctions.SaveFile(filename, Menus.getMainCanvas(), MenuFunctions.struct, MenuFunctions.struct.getMesh().getNodes(), MenuFunctions.struct.getMesh().getElements(),
						MenuFunctions.struct.getSupports(), MenuFunctions.ConcLoad, MenuFunctions.DistLoad, MenuFunctions.NodalDisp, MenuFunctions.matTypes,
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
		MenuFunctions.ResetStructure();
		
		String filename = Menus.getInstance().getSaveLoadFile().run().getText();
		MenuFunctions.LoadFile("", filename);
		MenuFunctions.struct.updateMaxCoords() ;
		Menus.getMainCanvas().setDimension(new double[] {1.2 * MenuFunctions.struct.getMaxCoords().x, 1.2 * MenuFunctions.struct.getMaxCoords().y, 1});
		Menus.getInstance().setRunAnalysis(MenuFunctions.CheckIfAnalysisIsReady());
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
		Menus.getInstance().setStepIsComplete(MenuFunctions.CheckSteps());
		Menus.getInstance().DisableButtons();
		Menus.getInstance().EnableButtons();
		Menus.getInstance().updateInstructionPanel();
	}
	
}
