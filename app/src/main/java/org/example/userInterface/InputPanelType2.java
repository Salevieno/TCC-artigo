package org.example.userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.example.mainTCC.MenuFunctions;

public class InputPanelType2 extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JLabel[] Labels;
	private List<JButton> Buttons;
	private ActionWithString okActionWithInput ;
	private Runnable okAction ;

	public InputPanelType2 (String PanelName, Point location, JLabel[] Labels, List<JButton> Buttons, ActionWithString okActionWithInput, Runnable okAction)
	{
		setTitle(PanelName);
		this.Labels = Labels;
		this.Buttons = Buttons ;
		this.okActionWithInput = okActionWithInput ;
		this.okAction = okAction ;
		Buttons.forEach(button -> button.addActionListener(this)) ;
		JPanel panel = DrawScreen();
		setLocation(location);
		getContentPane().add(panel);
		pack();
	}


	public InputPanelType2 (String PanelName, JLabel[] Labels, List<JButton> Buttons, ActionWithString okActionWithInput, Runnable okAction)
	{
		this(PanelName, Menus.frameTopLeft, Labels, Buttons, okActionWithInput, okAction) ;
	}
	
	public InputPanelType2 (String PanelName, List<JButton> Buttons, ActionWithString okActionWithInput, Runnable okAction)
	{
		this(PanelName, null, Buttons, okActionWithInput, okAction) ;
	}

	public InputPanelType2 (String PanelName, List<JButton> Buttons, ActionWithString okActionWithInput)
	{
		this(PanelName, null, Buttons, okActionWithInput, null) ;
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		Object source = ae.getSource();
		for (int i = 0; i <= Buttons.size() - 1; i += 1)
		{
			if (source == Buttons.get(i) && !Buttons.get(i).getText().equals("Cancel")) 
			{
				okActionWithInput.act(Buttons.get(i).getText()) ;
				if (okAction != null)
				{
					okAction.run() ;
				}
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

	public void activate() 
	{
		this.setVisible(true);
	}
}
