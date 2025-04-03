package org.example.userInterface.InputDialogs;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;

import org.example.structure.ElemType;
import org.example.userInterface.MenuStructure;
import org.example.userInterface.Menus;
import org.example.view.MainPanel;

public class DefineElementTypeDialog extends InputDialogWithButtons
{
    
    public DefineElementTypeDialog()
    {
        super("Elem types", null);
        List<JButton> modifiableButtons = new ArrayList<>() ;
        for (ElemType elemType : ElemType.values())
        {
            JButton newButton = new JButton(elemType.toString()) ;
            newButton.setSize(stdButtonDimension) ;
            newButton.setEnabled(true) ;
            modifiableButtons.add(newButton) ;
        }

        setButtons(Collections.unmodifiableList(modifiableButtons));
    }

    @Override
    public void onOkClick(ActionEvent ae)
    {
        String elemType = clickedButtonText(ae) ;
        MainPanel.setElemType(elemType) ;
        Menus.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion(MainPanel.structure, MainPanel.loading) ;
        MenuStructure.updateEnabledSubMenus() ;
    }
}
