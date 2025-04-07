package org.example.userInterface.InputDialogs;

import javax.swing.JLabel;

import org.example.userInterface.MenuStructure;
import org.example.view.CentralPanel;

public class CreateDistLoadsDialog extends InputDialogWithGrid
{

    private static final JLabel[] labels = new JLabel[] {new JLabel ("Load type"), new JLabel ("Load i (kN / kNm)")};

    public CreateDistLoadsDialog()
    {
        super("Dist loads", "Dist load", labels, true);
    }

    @Override
    public void onOkClick(double[][] input)
    {
		CentralPanel.DefineDistLoadTypes(input);
		MenuStructure.updateEnabledSubMenus();
    }
}
