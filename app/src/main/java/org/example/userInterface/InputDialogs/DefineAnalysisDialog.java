package org.example.userInterface.InputDialogs;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;

import org.example.userInterface.Menus;

public class DefineAnalysisDialog extends InputDialogWithButtons
{

    private static final List<String> buttonNames = List.of("Linear elástica", "Geometria não-linear", "Material não-linear", "Ambos não-lineares") ;

    public DefineAnalysisDialog()
    {
        super("Analysis types", null);
        
		List<JButton> modifiableButtons = new ArrayList<>();
		for (String buttonName : buttonNames)
        {
            modifiableButtons.add(new JButton (buttonName)) ;
        }
        
        setButtons(Collections.unmodifiableList(modifiableButtons));
        
    }

    @Override
    public void onOkClick(ActionEvent ae)
    {
        String analysisType = clickedButtonText(ae) ;
        int analysisTypeID = buttonNames.indexOf(analysisType) ;
        Menus.getInstance().getMenuAnalysis().setAnalysisTypeID(analysisTypeID) ;        
        Menus.getInstance().getMenuAnalysis().runAnalysis(analysisTypeID) ;
    }

}
