package org.example.userInterface.InputDialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.example.userInterface.ActionWithDoubleArray;
import org.example.userInterface.Menus;
import org.example.view.MainPanel;

public abstract class InputDialogWithGrid extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JLabel[] Labels;
	private String NameLabel;
	private ArrayList<JTextField[]> Lines = new ArrayList<JTextField[]>();	
	private double[][] Input;
	private List<JButton> Buttons;
	private ActionWithDoubleArray okActionWithInput ;
	private Runnable okAction ;

	private static final JButton addButton ;
	private static final JButton removeButton ;
	private static final JButton okButton ;
	private static final JButton cancelButton ;

	static
	{
		addButton = new JButton("Add");
		addButton.setSize(100, 20);
		removeButton = new JButton("Remove");
		removeButton.setSize(30, 20);
		okButton = new JButton("Ok");
		okButton.setSize(30, 20);
		cancelButton = new JButton("Cancel");
		cancelButton.setSize(30, 20);
	}
	
	public InputDialogWithGrid(String PanelName, String NameLabel, Point location, JLabel[] Labels, boolean AddRemoveButtons, ActionWithDoubleArray okActionWithInput, Runnable okAction)
	{
		setTitle(PanelName);
		this.Lines.add(CreateLineTextFields(Labels.length));
		this.NameLabel = NameLabel;
		this.Labels = Labels;
		this.Buttons = new ArrayList<>();
		
		if (AddRemoveButtons)
		{
			this.Buttons.add(addButton);
			this.Buttons.add(removeButton);
		}
		
		this.Buttons.add(okButton);
		this.Buttons.add(cancelButton);
		Buttons.forEach(button -> button.addActionListener(this));
		this.okActionWithInput = okActionWithInput ;
		this.okAction = okAction ;

		JPanel panel = DrawScreen();
		setLocation(location);
		getContentPane().add(panel);
		pack();
	}

	public InputDialogWithGrid(String PanelName, String NameLabel, JLabel[] Labels, boolean AddRemoveButtons, ActionWithDoubleArray okActionWithInput, Runnable okAction)
	{
		this(PanelName, NameLabel, Menus.frameTopLeft, Labels, AddRemoveButtons, okActionWithInput, okAction);
	}

	public InputDialogWithGrid(String PanelName, String NameLabel, JLabel[] Labels, boolean AddRemoveButtons, ActionWithDoubleArray okActionWithInput)
	{
		this(PanelName, NameLabel, Menus.frameTopLeft, Labels, AddRemoveButtons, okActionWithInput, null);
	}
	public InputDialogWithGrid(String PanelName, String NameLabel, JLabel[] Labels, boolean AddRemoveButtons)
	{
		this(PanelName, NameLabel, Menus.frameTopLeft, Labels, AddRemoveButtons, null, null);
	}

	public abstract void onOkClick(double[][] input) ;

	private JTextField[] CreateLineTextFields(int NTextField)
	{
		JTextField[] TF = new JTextField[NTextField];
		for (int i = 0; i <= NTextField - 1; i += 1)
		{
			TF[i] = new JTextField();
		}
		return TF;
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		Object source = ae.getSource();
		for (int i = 0; i <= Buttons.size() - 1; i += 1)
		{
			if (source == Buttons.get(i) && Buttons.get(i).getText().contains("Add"))
			{
				Lines.add(CreateLineTextFields(Labels.length));
				getContentPane().removeAll();
				JPanel panel = DrawScreen();
				getContentPane().add(panel);
				pack();
			}
			if(source == Buttons.get(i) && Buttons.get(i).getText().contains("Remove"))
			{
				Lines.remove(Lines.size() - 1);
				getContentPane().removeAll();
				JPanel panel = DrawScreen();
				getContentPane().add(panel);
				pack();
			}
			if (source == Buttons.get(i) && Buttons.get(i).getText().equals("Ok")) 
			{
				this.Input = new double[Lines.size()][Lines.get(0).length];
				for(int j = 0; j < Lines.size(); j += 1)
				{
					for(int k = 0; k < Lines.get(j).length; k += 1)
					{
						this.Input[j][k] = Double.parseDouble(Lines.get(j)[k].getText());
					}
				}
				onOkClick(Input) ;
				// okActionWithInput.act(this.Input) ;
				// okAction.run() ;
				System.out.println(MainPanel.structure);
				dispose();
			}
			if(source == Buttons.get(i) && Buttons.get(i).getText().equals("Cancel"))
			{
				System.out.println("Cancel clicado");
				dispose();
			}
		}
	}
	
	private GridBagConstraints SetGridPos(GridBagConstraints gbc, int gridx, int gridy)
	{
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		return gbc;
	}
	
	private void AddButton(JPanel panel, GridBagConstraints gbc, JButton Button, int gridx, int gridy)
	{
		gbc = SetGridPos(gbc, gridx, gridy);
		panel.add(Button, gbc);
	}
	
	private JPanel DrawScreen()
	{	
		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(new GridBagLayout());
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.weightx = 1;
		for (int i = 0; i <= Labels.length - 1; i += 1)
		{
			gbc = SetGridPos(gbc, i + 1, 0);
			panel.add(Labels[i], gbc);
		}
		int gridx = 0, gridy = 1;
		for (int i = 0; i <= Lines.size() - 1; i += 1)
		{
			JLabel MatLabel = new JLabel (NameLabel + " " + (i + 1));
			JTextField[] line = Lines.get(i);
			gbc = SetGridPos(gbc, gridx, gridy);
			panel.add(MatLabel, gbc);
			for(int j = 0; j <= line.length - 1; j += 1)
			{
				gridx += 1;
				gbc = SetGridPos(gbc, gridx, gridy);
				panel.add(line[j], gbc);
			}
			gridx = 0;
			gridy += 1;
		}
		AddButton(panel, gbc, Buttons.get(0), gridx, gridy);
		gridx += 1;
		if (1 < Lines.size())
		{
			AddButton(panel, gbc, Buttons.get(1), gridx, gridy);
			gridx += 1;
		}
		if (2 < Buttons.size())
		{
			AddButton(panel, gbc, Buttons.get(2), gridx, gridy);
			gridx += 1;
		}
		if (3 < Buttons.size())
		{
			AddButton(panel, gbc, Buttons.get(3), gridx, gridy);
			gridx += 1;
		}
		return panel;
	}

	public void activate() 
	{
		this.setVisible(true);
	}
}
