package org.example.userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.mainTCC.MenuFunctions;
import org.example.service.MenuFileService;
import org.example.structure.StructureDTO;
import org.example.view.MainPanel;

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
				// MenuFileService.SaveFile(filename, Menus.getInstance().getMainCanvas(), MainPanel.structure, MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getMesh().getElements(),
				// 		MainPanel.structure.getSupports(), MainPanel.loading, MenuFunctions.matTypes,
				// 		MenuFunctions.secTypes);
				MenuFileService.saveStructure(filename, new StructureDTO(MainPanel.structure)) ;
			}
		});
		Load.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MenuFileService.loadStructure() ;
			}
		});
		Save.setForeground(Menus.palette[3]);
		Load.setForeground(Menus.palette[3]);
		FileMenu.add(Save);
		FileMenu.add(Load);
	}

	
}
