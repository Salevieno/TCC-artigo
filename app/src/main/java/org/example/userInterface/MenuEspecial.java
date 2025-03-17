package org.example.userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.mainTCC.MenuFunctions;
import org.example.view.MainPanel;

public class MenuEspecial extends JMenu
{

	private JMenuItem Star;

    public MenuEspecial()
    {
        this.setText("Especial") ;
        this.setMnemonic(KeyEvent.VK_E) ;

		Star = new JMenuItem("Estrela", KeyEvent.VK_S);
		Star.setForeground(Menus.palette[7]);
		this.add(Star);
		Star.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				MainPanel.structure = MenuFunctions.Especial();
				Menus.getInstance().ActivatePostAnalysisView(MainPanel.structure);
			}
		}) ;	

    }
}
