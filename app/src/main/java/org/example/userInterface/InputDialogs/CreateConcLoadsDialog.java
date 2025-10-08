package org.example.userInterface.InputDialogs;

import java.util.Arrays;

import javax.swing.JLabel;

import org.example.loading.ConcLoad;
import org.example.loading.Force;
import org.example.userInterface.MenuBar;

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
        Arrays.asList(input).forEach(inp -> ConcLoad.addType(new Force(inp))) ;
        MenuBar.getInstance().getMenuStructure().updateEnabledSubMenus() ;
    }
}
