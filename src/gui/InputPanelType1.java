package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputPanelType1 extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JLabel[] Labels;
	private String NameLabel;
	private ArrayList<JTextField[]> Lines = new ArrayList<JTextField[]>();	
	private double[][] Input;
	private JButton[] Buttons;
	
	public InputPanelType1 (JFrame parent, String PanelName, String NameLabel, int[] Location, JLabel[] Labels, JButton[] Buttons, int[][] ButtonSizes)
	{
		super(parent, PanelName, true);
		this.Lines.add(CreateLineTextFields(Labels.length));
		this.NameLabel = NameLabel;
		this.Labels = Labels;
		this.Buttons = Buttons;
		for (int i = 0; i <= Buttons.length - 1; i += 1)
		{
			this.Buttons[i].setSize(ButtonSizes[i][0], ButtonSizes[i][1]);
			this.Buttons[i].addActionListener(this);
		}
		JPanel panel = DrawScreen();
		setLocation(Location[0], Location[1]);
		getContentPane().add(panel);
		pack();
	}

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
		for (int i = 0; i <= Buttons.length - 1; i += 1)
		{
			if (source == Buttons[i] & Buttons[i].getText().contains("Add"))
			{
				Lines.add(CreateLineTextFields(Labels.length));
				getContentPane().removeAll();
				JPanel panel = DrawScreen();
				getContentPane().add(panel);
				pack();
			}
			if(source == Buttons[i] & Buttons[i].getText().contains("Remove"))
			{
				Lines.remove(Lines.size() - 1);
				getContentPane().removeAll();
				JPanel panel = DrawScreen();
				getContentPane().add(panel);
				pack();
			}
			if (source == Buttons[i] & Buttons[i].getText().equals("Ok")) 
			{
				this.Input = new double[Lines.size()][Lines.get(0).length];
				for(int j = 0; j < Lines.size(); j += 1)
				{
					for(int k = 0; k < Lines.get(j).length; k += 1)
					{
						this.Input[j][k] = Double.parseDouble(Lines.get(j)[k].getText());
					}
				}
				dispose();
			}
			if(source == Buttons[i] & Buttons[i].getText().equals("Cancel"))
			{
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
		AddButton(panel, gbc, Buttons[0], gridx, gridy);
		gridx += 1;
		if (1 < Lines.size())
		{
			AddButton(panel, gbc, Buttons[1], gridx, gridy);
			gridx += 1;
		}
		if (2 < Buttons.length)
		{
			AddButton(panel, gbc, Buttons[2], gridx, gridy);
			gridx += 1;
		}
		if (3 < Buttons.length)
		{
			AddButton(panel, gbc, Buttons[3], gridx, gridy);
			gridx += 1;
		}
		return panel;
	}

	public double[][] run() 
	{
		this.setVisible(true);
		//Here the program will "stop" until it's "dispose()"d, when the "dispose()" happens, the next line will happen
		return Input;
	}
}
