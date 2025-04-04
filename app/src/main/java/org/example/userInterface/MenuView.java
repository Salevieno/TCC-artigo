package org.example.userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.service.MenuViewService;

public class MenuView extends JMenu
{

    String[] ViewMenuItemsNames = new String[] {
        "Nos",
        "Elementos",
        "Graus de liberdade",
        "Numeros dos nos",
        "Numeros dos elementos",
        "Materiais",
        "Secoes",
        "Contorno dos elementos",
        "Apoios",
        "Cargas concentradas",
        "Cargas distribuidas",
        "Deslocamentos nodais",
        "Valores das cargas",
        "Reacoes"
    } ;

	private JMenuItem DOFNumberView ;
    private JMenuItem NodeNumberView, ElemNumberView, MatView, SecView, NodeView, ElemView, ElemContourView, SupView, LoadsValuesView, ConcLoadsView, DistLoadsView, NodalDispsView, ReactionsView;
	private JMenuItem ReactionValues, ReactionArrows;

	private MenuViewService view = MenuViewService.getInstance() ;

    public MenuView()
    {
        this.setMnemonic(KeyEvent.VK_V);
        this.setText("Visão");

		NodeView = new JMenuItem(ViewMenuItemsNames[0]);
		ElemView = new JMenuItem(ViewMenuItemsNames[1]);
		ConcLoadsView = new JMenuItem(ViewMenuItemsNames[9], KeyEvent.VK_C);
		DistLoadsView = new JMenuItem(ViewMenuItemsNames[10]);
		NodalDispsView = new JMenuItem(ViewMenuItemsNames[11]);
		ReactionsView = new JMenu(ViewMenuItemsNames[13]);
		LoadsValuesView = new JMenuItem(ViewMenuItemsNames[12]);
		SupView = new JMenuItem(ViewMenuItemsNames[8]);
		DOFNumberView = new JMenuItem(ViewMenuItemsNames[2], KeyEvent.VK_D);
		NodeNumberView = new JMenuItem(ViewMenuItemsNames[3], KeyEvent.VK_N);
		ElemNumberView = new JMenuItem(ViewMenuItemsNames[4], KeyEvent.VK_E);
		ElemContourView = new JMenuItem(ViewMenuItemsNames[7]);
		MatView = new JMenuItem(ViewMenuItemsNames[5], KeyEvent.VK_M);
		SecView = new JMenuItem(ViewMenuItemsNames[6], KeyEvent.VK_S);
		DOFNumberView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowDOFNumber = !ShowDOFNumber;
				view.switchDOFNumberView() ;
			}
		});
		NodeNumberView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowNodeNumber = !ShowNodeNumber;
				view.switchNodeNumberView();
			}
		});
		ElemNumberView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowElemNumber = !ShowElemNumber;
				view.switchElemNumberView();
			}
		});
		MatView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowMatColor = !ShowMatColor;
				view.switchMatView();
			}
		});
		SecView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowSecColor = !ShowSecColor;
				view.switchSecView();
			}
		});
		NodeView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowNodes = !ShowNodes;
				view.switchNodeView();
			}
		});
		ElemView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowElems = !ShowElems;
				view.switchElemView();
			}
		});
		ElemContourView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowElemContour = !ShowElemContour;
				view.switchElemContourView();
			}
		});
		SupView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowSup = !ShowSup;
				view.switchSupView();
			}
		});
		LoadsValuesView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowLoadsValues = !ShowLoadsValues;
				view.switchLoadsValuesView();
			}
		});
		ConcLoadsView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowConcLoads = !ShowConcLoads;
				view.switchConcLoadsView();
			}
		});
		DistLoadsView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowDistLoads = !ShowDistLoads;
				view.switchDistLoadsView();
			}
		});
		NodalDispsView.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//ShowNodalDisps = !ShowNodalDisps;
				view.switchNodalDispsView();
			}
		});
		DOFNumberView.setForeground(Menus.palette[5]);
		NodeNumberView.setForeground(Menus.palette[5]);
		ElemNumberView.setForeground(Menus.palette[5]);
		MatView.setForeground(Menus.palette[5]);
		SecView.setForeground(Menus.palette[5]);
		NodeView.setForeground(Menus.palette[5]);
		ElemView.setForeground(Menus.palette[5]);
		ElemContourView.setForeground(Menus.palette[5]);
		SupView.setForeground(Menus.palette[5]);
		ConcLoadsView.setForeground(Menus.palette[5]);
		DistLoadsView.setForeground(Menus.palette[5]);
		NodalDispsView.setForeground(Menus.palette[5]);
		LoadsValuesView.setForeground(Menus.palette[5]);
		ReactionsView.setForeground(Menus.palette[5]);
		DOFNumberView.setEnabled(false);
		this.add(DOFNumberView);
		this.add(NodeNumberView);
		this.add(ElemNumberView);
		this.add(MatView);
		this.add(SecView);
		this.add(NodeView);
		this.add(ElemView);
		this.add(ElemContourView);
		this.add(SupView);
		this.add(ConcLoadsView);
		this.add(DistLoadsView);
		this.add(NodalDispsView);
		this.add(LoadsValuesView);
		this.add(ReactionsView);

		/* Defining subitems in the menu ReactionsView */

	    String[] ReactionsViewMenuNames = new String[] {"Desenhos", "Valores"};
		ReactionArrows = new JMenuItem(ReactionsViewMenuNames[0], KeyEvent.VK_C);
		ReactionValues = new JMenuItem(ReactionsViewMenuNames[1], KeyEvent.VK_C);
		ReactionArrows.setForeground(Menus.palette[5]);
		ReactionValues.setForeground(Menus.palette[5]);
		ReactionArrows.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				// ShowReactionArrows = !ShowReactionArrows;
				view.switchReactionArrowsView() ;
			}
		});
		ReactionValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				// ShowReactionValues = !ShowReactionValues;
				view.switchReactionValuesView() ;
			}
		});
		ReactionsView.add(ReactionArrows);
		ReactionsView.add(ReactionValues);
    }

    public void enableDofNumberView() { DOFNumberView.setEnabled(true) ;}
    public void disableDofNumberView() { DOFNumberView.setEnabled(false) ;}

}
