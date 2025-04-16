package org.example.userInterface.InputDialogs;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;

import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.userInterface.MenuBar;

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
        MenuBar.getInstance().getMenuAnalysis().setAnalysisTypeID(analysisTypeID) ;        
        MenuBar.getInstance().getMenuAnalysis().runAnalysis(analysisTypeID, ConcLoad.getTypes(), DistLoad.getTypes()) ;
    }

}
