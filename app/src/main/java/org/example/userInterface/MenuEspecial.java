package org.example.userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.Main;
import org.example.mainTCC.MainPanel;
import org.example.mainTCC.MenuFunctions;
import org.example.view.CentralPanel;

public class MenuEspecial extends JMenu
{

	private JMenuItem Star;

    public MenuEspecial()
    {
        this.setText("Especial") ;
        this.setMnemonic(KeyEvent.VK_E) ;

		Star = new JMenuItem("Estrela", KeyEvent.VK_S);
		Star.setForeground(Main.palette[7]);
		this.add(Star);
		Star.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MainPanel.getInstance().getCentralPanel().setStructure(MenuFunctions.Especial());
				MainPanel.getInstance().ActivatePostAnalysisView(MainPanel.getInstance().getCentralPanel().getStructure());
				MenuBar.getInstance().updateEnabledMenus() ;
			}
		}) ;	

    }
}
