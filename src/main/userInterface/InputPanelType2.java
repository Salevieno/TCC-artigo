package main.userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.view.MainPanel;

public class InputPanelType2 extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JLabel[] Labels;
	private String Input;
	private List<JButton> Buttons;

	public InputPanelType2 (String PanelName, Point location, JLabel[] Labels, List<JButton> Buttons)
	{
		setTitle(PanelName);
		this.Labels = Labels;
		this.Buttons = Buttons ;
		Buttons.forEach(button -> button.addActionListener(this)) ;
		JPanel panel = DrawScreen();
		setLocation(location);
		getContentPane().add(panel);
		pack();
	}


	public InputPanelType2 (String PanelName, JLabel[] Labels, List<JButton> Buttons)
	{
		this(PanelName, Menus.frameTopLeft, Labels, Buttons);
	}
	
	public InputPanelType2 (String PanelName, List<JButton> Buttons)
	{
		this(PanelName, null,  Buttons);
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		Object source = ae.getSource();
		for (int i = 0; i <= Buttons.size() - 1; i += 1)
		{
			if (source == Buttons.get(i) & !Buttons.get(i).getText().equals("Cancel")) 
			{
				this.Input = Buttons.get(i).getText();
				dispose();
			}
			else if(Buttons.get(i).getText().equals("Cancel"))
			{
				dispose();
			}
		}
	}
	
	
	private JPanel DrawScreen()
	{	
		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(new GridBagLayout());
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.weightx = 1;
		if (Labels != null)
		{
			for (int i = 0; i <= Labels.length - 1; i += 1)
			{
				gbc.gridx = i + 1;
				gbc.gridy = 0;
				panel.add(Labels[i], gbc);
			}
		}
		int gridx = 0, gridy = 1;
		for (int i = 0; i <= Buttons.size() - 1; i += 1)
		{
			gbc.gridx = gridx;
			gbc.gridy = gridy;
			panel.add(Buttons.get(i), gbc);
			gridx += 1;
		}
		return panel;
	}

	public String run() 
	{
		this.setVisible(true);
		return Input;
	}
}
