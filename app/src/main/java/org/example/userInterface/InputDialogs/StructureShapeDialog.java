package org.example.userInterface.InputDialogs;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;

import org.example.mainTCC.MenuFunctions;
import org.example.structure.StructureShape;
import org.example.userInterface.Menus;
import org.example.view.MainPanel;

public class StructureShapeDialog extends InputDialogWithButtons
{

    public StructureShapeDialog()
    {
        super("Structure shape", null) ;
        
		List<JButton> buttons = new ArrayList<>();
		for (int b = 0; b <= StructureShape.values().length - 1; b += 1)
		{
			buttons.add(new JButton (StructureShape.values()[b].toString())) ;
		}

        setButtons(Collections.unmodifiableList(buttons));
    }

    @Override
    public void onOkClick(ActionEvent ae)
    {
        String inputStructureShape = clickedButtonText(ae) ;
        StructureShape structureShape = StructureShape.valueOf(inputStructureShape) ;
        MainPanel.CreateStructureOnClick(structureShape) ;
        Menus.getInstance().getNorthPanel().getUpperToolbar().showButtonSnipToGridOn();
        Menus.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion() ;
    }

}
