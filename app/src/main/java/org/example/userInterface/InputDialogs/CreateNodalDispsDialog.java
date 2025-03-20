package org.example.userInterface.InputDialogs;

import javax.swing.JLabel;

import org.example.userInterface.MenuStructure;
import org.example.view.MainPanel;

public class CreateNodalDispsDialog extends InputDialogWithGrid
{

    private static final JLabel[] labels = new JLabel[] {new JLabel ("disp x"), new JLabel ("disp y"), new JLabel ("disp z"), new JLabel ("rot x"), new JLabel ("rot y"), new JLabel ("rot z")};
		
    public CreateNodalDispsDialog()
    {
        super("Nodal displacements", "Nodal disp", labels, true);
        
    }

    @Override
    public void onOkClick(double[][] input)
    {
		MainPanel.DefineNodalDispTypes(input);
		MenuStructure.updateEnabledSubMenus();
    }

}
