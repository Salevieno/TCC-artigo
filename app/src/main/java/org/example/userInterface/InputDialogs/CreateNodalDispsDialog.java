package org.example.userInterface.InputDialogs;

import javax.swing.JLabel;

import org.example.userInterface.MenuBar;
import org.example.view.CentralPanel;

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
      CentralPanel.DefineNodalDispTypes(input);
      MenuBar.getInstance().getMenuStructure().updateEnabledSubMenus();
    }

}
