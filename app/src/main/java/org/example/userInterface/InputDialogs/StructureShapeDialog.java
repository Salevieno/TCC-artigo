package org.example.userInterface.InputDialogs;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;

import org.example.mainTCC.MainPanel;
import org.example.structure.StructureShape;
import org.example.view.CentralPanel;

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
        CentralPanel.CreateStructureOnClick(structureShape) ;
        MainPanel.getInstance().getNorthPanel().getUpperToolbar().showButtonSnipToGridOn();
        MainPanel.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion(CentralPanel.structure, CentralPanel.loading) ;
    }

}
