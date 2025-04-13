package org.example.userInterface.InputDialogs;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.example.mainTCC.MainFrame;
import org.example.view.CentralPanel;

public abstract class InputDialogWithGrid extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JLabel[] headers;
	private String name;
	private List<JTextField[]> rows ;	
	private double[][] input;
	private List<JButton> buttons;

	private final JButton addButton ;
	private final JButton removeButton ;
	private final JButton okButton ;
	private final JButton cancelButton ;

	public InputDialogWithGrid(String name, String label, Point location, JLabel[] headers, boolean multiLine)
	{
		
		setTitle(name);
		this.rows = new ArrayList<>() ;
		this.rows.add(CreateLineTextFields(headers.length));
		this.name = label;
		this.headers = headers;
		this.buttons = new ArrayList<>();

		addButton = new JButton("Add");
		addButton.setSize(100, 20);
		removeButton = new JButton("Remove");
		removeButton.setSize(30, 20);
		okButton = new JButton("Ok");
		okButton.setSize(30, 20);
		cancelButton = new JButton("Cancel");
		cancelButton.setSize(30, 20);
		
		if (multiLine)
		{
			this.buttons.add(addButton);
			this.buttons.add(removeButton);
		}
		
		this.buttons.add(okButton);
		this.buttons.add(cancelButton);
		buttons.forEach(button -> button.addActionListener(this));

		JPanel panel = createPanel();
		
		this.setLocation(location) ;
		this.getContentPane().add(panel) ;
		this.setLayout(new FlowLayout()) ;
		pack();
	}

	public InputDialogWithGrid(String PanelName, String NameLabel, JLabel[] Labels, boolean multiLine)
	{
		this(PanelName, NameLabel, MainFrame.getTopLeft(), Labels, multiLine);
	}

	public abstract void onOkClick(double[][] input) ;

	private JTextField[] CreateLineTextFields(int qtdCol)
	{
		JTextField[] TF = new JTextField[qtdCol];
		for (int i = 0; i <= qtdCol - 1; i += 1)
		{
			TF[i] = new JTextField();
		}
		return TF;
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		Object source = ae.getSource();
		for (int i = 0; i <= buttons.size() - 1; i += 1)
		{
			if (source == buttons.get(i) && buttons.get(i).getText().contains("Add"))
			{
				rows.add(CreateLineTextFields(headers.length));
				getContentPane().removeAll();
				JPanel panel = createPanel();
				getContentPane().add(panel);
				pack();
			}
			if(source == buttons.get(i) && buttons.get(i).getText().contains("Remove"))
			{
				rows.remove(rows.size() - 1);
				getContentPane().removeAll();
				JPanel panel = createPanel();
				getContentPane().add(panel);
				pack();
			}
			if (source == buttons.get(i) && buttons.get(i).getText().equals("Ok")) 
			{
				this.input = new double[rows.size()][rows.get(0).length];
				for(int j = 0; j < rows.size(); j += 1)
				{
					for(int k = 0; k < rows.get(j).length; k += 1)
					{
						this.input[j][k] = Double.parseDouble(rows.get(j)[k].getText());
					}
				}
				onOkClick(input) ;
				// okActionWithInput.act(this.Input) ;
				// okAction.run() ;
				System.out.println(CentralPanel.structure);
				dispose();
			}
			if(source == buttons.get(i) && buttons.get(i).getText().equals("Cancel"))
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
	
	private JPanel createPanel()
	{	
		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		panel.setLayout(new GridBagLayout());
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.weightx = 1;
		
		int gridy = 0;
		for (int i = 0; i <= headers.length - 1; i += 1)
		{
			gbc = SetGridPos(gbc, i + 1, gridy);
			panel.add(headers[i], gbc);
		}
		
		gridy += 1;
		for (int i = 0; i <= rows.size() - 1; i += 1)
		{
			JLabel rowLabel = new JLabel (name + " " + (i + 1));
			gbc = SetGridPos(gbc, 0, gridy);
			panel.add(rowLabel, gbc);

			int gridx = 1 ;
			JTextField[] cols = rows.get(i);
			for (JTextField col : cols)
			{
				gbc = SetGridPos(gbc, gridx, gridy);
				panel.add(col, gbc);
				gridx += 1;
			}
			gridy += 1;
		}

		// gbc = SetGridPos(gbc, 0, gridy);
		int gridx = 1 ;
		for (JButton button : buttons)
		{
			gbc = SetGridPos(gbc, gridx, gridy);
			panel.add(button, gbc);
			gridx += 1;
		}
		return panel;
	}

	public void activate() 
	{
		this.setVisible(true);
	}
}
