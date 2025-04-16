package org.example.userInterface.InputDialogs;

import java.util.Arrays;

import javax.swing.JLabel;

import org.example.loading.NodalDisp;
import org.example.userInterface.MenuBar;

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
      Arrays.stream(input).forEach(row -> NodalDisp.addType(Arrays.stream(row).boxed().toArray(Double[]::new))) ;
      MenuBar.getInstance().getMenuStructure().updateEnabledSubMenus();
    }

}
