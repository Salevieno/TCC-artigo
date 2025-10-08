package org.example.userInterface.InputDialogs;

import java.util.Arrays;

import javax.swing.JLabel;

import org.example.loading.DistLoad;
import org.example.userInterface.MenuBar;

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
      Arrays.stream(input).forEach(row -> DistLoad.addType(Arrays.stream(row).boxed().toArray(Double[]::new))) ;
      MenuBar.getInstance().getMenuStructure().updateEnabledSubMenus();
    }
}
