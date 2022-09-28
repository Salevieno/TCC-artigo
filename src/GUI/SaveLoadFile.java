package GUI;

import java.awt.Dimension;
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
import javax.swing.JTextField;

public class SaveLoadFile extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	/*private JFrame parent;
	private JDialog dialog;
	private JLabel FileNameLabel;*/
	private JTextField FileName = new JTextField();	
	private JButton OKButton;
	private JButton CancelButton;
	
	private JTextField CreateLineTextField()
	{
		JTextField File = new JTextField();
		File.setPreferredSize(new Dimension(50, 20));       
		return File;
	}
	
	public SaveLoadFile (JFrame parent, int[] Location)
	{
		super(parent,"File Name",true);
		//this.parent = parent;
		this.FileName.add(CreateLineTextField());
		JPanel panel = DrawScreen();

		setLocation(Location[0], Location[1]);
		getContentPane().add(panel);
		pack();
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		Object source = ae.getSource();

		if (source == OKButton) 
		{
			this.FileName.add(CreateLineTextField());
			dispose();
		}
		else if(source == CancelButton)
		{
			this.FileName = null;
			dispose();
		}
	}
	
	private JPanel DrawScreen()
	{	
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		JLabel FileNameLabel = new JLabel ("Name ");
		gbc.weightx = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(FileNameLabel, gbc);
		
		JTextField textfield = FileName;
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		panel.add(textfield, gbc);
		
		this.OKButton = new JButton ("OK");
		this.OKButton.setSize(30, 20);
		this.OKButton.addActionListener(this);
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel.add(OKButton, gbc);

		this.CancelButton = new JButton ("Cancel");
		this.CancelButton.setSize(50, 20);
		this.CancelButton.addActionListener(this);
		gbc.gridx = 2;
		gbc.gridy = 1;
		panel.add(CancelButton, gbc);
		
		return panel;
	}

	public JTextField run() 
	{
		this.setVisible(true);
		//Here the program will "stop" until it's "dispose()"d, when the "dispose()" happens, the next line will happen
		if (FileName != null)
		{
			return FileName;
		}
		else
		{
			return new JTextField("");
		}
	}
}
