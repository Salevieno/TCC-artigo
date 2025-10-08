package org.example.userInterface.InputDialogs;

import java.awt.Dimension;
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

import org.example.main.MainFrame;
import org.example.userInterface.ActionWithString;

public abstract class InputDialogWithButtons extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JLabel[] Labels;
	protected List<JButton> buttons;
	// private ActionWithString okActionWithInput ;
	// private Runnable okAction ;

	protected static final Dimension stdButtonDimension = new Dimension(30, 20) ;

	public InputDialogWithButtons (String PanelName, Point location, JLabel[] Labels, List<JButton> buttons, ActionWithString okActionWithInput, Runnable okAction)
	{
		setTitle(PanelName);
		this.Labels = Labels;
		this.buttons = buttons ;
		// this.okActionWithInput = okActionWithInput ;
		// this.okAction = okAction ;
		// buttons.forEach(button -> button.addActionListener(this)) ;
		// JPanel panel = createDialog();
		this.setLocation(location);
		// getContentPane().add(panel);
	}


	public InputDialogWithButtons (String PanelName, JLabel[] Labels, List<JButton> Buttons, ActionWithString okActionWithInput, Runnable okAction)
	{
		this(PanelName, MainFrame.getTopLeft(), Labels, Buttons, okActionWithInput, okAction) ;
	}
	
	public InputDialogWithButtons (String PanelName, List<JButton> Buttons, ActionWithString okActionWithInput, Runnable okAction)
	{
		this(PanelName, null, Buttons, okActionWithInput, okAction) ;
	}

	public InputDialogWithButtons (String PanelName, List<JButton> Buttons, ActionWithString okActionWithInput)
	{
		this(PanelName, null, Buttons, okActionWithInput, null) ;
	}

	public InputDialogWithButtons (String PanelName, List<JButton> Buttons)
	{
		this(PanelName, null, Buttons, null, null) ;
	}
	
	protected void setButtons(List<JButton> buttons)
	{
		this.buttons = buttons ;
		buttons.forEach(button -> button.addActionListener(this)) ;
	}

	public void actionPerformed(ActionEvent ae) 
	{
		Object source = ae.getSource();
		for (int i = 0; i <= buttons.size() - 1; i += 1)
		{
			if (source == buttons.get(i) && !buttons.get(i).getText().equals("Cancel")) 
			{
				onOkClick(ae) ;
				dispose();
			}
			else if(buttons.get(i).getText().equals("Cancel"))
			{
				dispose();
			}
		}
	}


    protected String clickedButtonText(ActionEvent ae)
    {
		Object source = ae.getSource();
        for (int i = 0; i <= buttons.size() - 1; i += 1)
		{
			if (source == buttons.get(i)) 
			{
                return buttons.get(i).getText() ;
            }
        }

		return "" ;
    }

	public abstract void onOkClick(ActionEvent ae) ;
	
	
	private JPanel createPanel()
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
		for (int i = 0; i <= buttons.size() - 1; i += 1)
		{
			gbc.gridx = gridx;
			gbc.gridy = gridy;
			panel.add(buttons.get(i), gbc);
			gridx += 1;
		}
		return panel;
	}

	public void activate() 
	{
		JPanel panel = createPanel() ;
		getContentPane().add(panel);
		this.setVisible(true);
		pack();
	}
}
