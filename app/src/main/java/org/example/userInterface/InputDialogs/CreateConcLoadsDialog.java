package org.example.userInterface.InputDialogs;

import javax.swing.JLabel;

import org.example.userInterface.MenuStructure;
import org.example.view.MainPanel;

public class CreateConcLoadsDialog extends InputDialogWithGrid
{

    private static final JLabel[] labels = new JLabel[] {new JLabel ("Fx (kN)"), new JLabel ("Fy (kN)"), new JLabel ("Fz (kN)"), new JLabel ("Mx (kNm)"), new JLabel ("My (kNm)"), new JLabel ("Mz (kNm)")};

    public CreateConcLoadsDialog()
    {
        super("Conc loads", "Conc load", labels, true);
    }

    @Override
    public void onOkClick(double[][] input)
    {
        MainPanel.DefineConcLoadTypes(input);
		MenuStructure.updateEnabledSubMenus();
    }		
}
