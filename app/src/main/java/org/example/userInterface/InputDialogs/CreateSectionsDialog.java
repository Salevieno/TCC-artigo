package org.example.userInterface.InputDialogs;

import javax.swing.JLabel;

import org.example.structure.Section;
import org.example.userInterface.MenuStructure;
import org.example.view.MainPanel;

public class CreateSectionsDialog extends InputDialogWithGrid
{

    private static final JLabel[] labels = new JLabel[] {new JLabel ("espessura (mm)")};
		
    public CreateSectionsDialog()
    {
        super("Cross sections", "Sec", labels, true);
    }
    
    @Override
    public void onOkClick(double[][] input)
    {
        for (int i = 0 ; i <= input.length - 1 ; i += 1)
		{
            MainPanel.addSection(new Section(input[i][0])) ;
		}
		MenuStructure.updateEnabledSubMenus();
    }

}
