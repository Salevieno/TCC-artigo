package org.example.userInterface.InputDialogs;

import javax.swing.JLabel;

import org.example.mainTCC.MainPanel;
import org.example.structure.Material;
import org.example.userInterface.MenuStructure;

public class CreateMaterialsDialog extends InputDialogWithGrid
{
	
    private static final JLabel[] labels = new JLabel[] {new JLabel ("E (GPa)"), new JLabel ("  v  "), new JLabel ("fu (MPa)")};		

    public CreateMaterialsDialog()
    {
        super("Materials", "Mat", labels, true) ;
    }

    @Override
    public void onOkClick(double[][] input)
    {
        for (int i = 0 ; i <= input.length - 1 ; i += 1)
        {
            MainPanel.getInstance().getWestPanel().getListsPanel().addMaterial(new Material(input[i][0], input[i][1], input[i][2])) ;
        }
        MenuStructure.updateEnabledSubMenus();
    }
}
