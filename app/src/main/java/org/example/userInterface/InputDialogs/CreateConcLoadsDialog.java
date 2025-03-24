package org.example.userInterface.InputDialogs;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.example.loading.Force;
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
        List<Force> forces = new ArrayList<>() ;
        for (double[] inp : input)
        {
            forces.add(new Force(inp)) ;
        }
        MainPanel.setConcLoadTypes(forces);
		MenuStructure.updateEnabledSubMenus();
    }		
}
