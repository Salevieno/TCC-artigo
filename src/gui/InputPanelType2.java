package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InputPanelType2 extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JLabel[] Labels;
	private String Input;
	private JButton[] Buttons;
	
	public InputPanelType2 (JFrame parent, String PanelName, int[] Location, JLabel[] Labels, JButton[] Buttons, boolean[] Enabled, int[][] ButtonSizes)
	{
		super(parent, PanelName, true);
		this.Labels = Labels;
		this.Buttons = Buttons;
		for (int i = 0; i <= Buttons.length - 1; i += 1)
		{
			this.Buttons[i].setSize(ButtonSizes[i][0], ButtonSizes[i][1]);
			this.Buttons[i].setEnabled(Enabled[i]);
			this.Buttons[i].addActionListener(this);
		}
		JPanel panel = DrawScreen();
		setLocation(Location[0], Location[1]);
		getContentPane().add(panel);
		pack();
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		Object source = ae.getSource();
		for (int i = 0; i <= Buttons.length - 1; i += 1)
		{
			if (source == Buttons[i] & !Buttons[i].getText().equals("Cancel")) 
			{
				this.Input = Buttons[i].getText();
				dispose();
			}
			else if(Buttons[i].getText().equals("Cancel"))
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
		for (int i = 0; i <= Buttons.length - 1; i += 1)
		{
			AddButton(panel, gbc, Buttons[i], gridx, gridy);
			gridx += 1;
		}
		return panel;
	}

	public String run() 
	{
		this.setVisible(true);
		//Here the program will "stop" until it's "dispose()"d, when the "dispose()" happens, the next line will happen
		return Input;
	}
}
