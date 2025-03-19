package org.example.userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.mainTCC.Analysis;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.Element;
import org.example.userInterface.InputDialogs.InputDialogWithButtons;
import org.example.view.MainPanel;

public class MenuAnalysis extends JMenu
{
    
	private JMenuItem runAnalysis;
    private boolean isReadyForAnalysis;
	private static boolean geometryIsNonLinear ;
	private static boolean matIsNonLinear ;
	private static int qtdIterations = 1 ;
	private static int qtdLoadSteps = 1;
	private static double maxLoadFactor = 1;
	private static String analysisType ;
	private static Runnable updateInstructionsPanel = () -> runAnalysis() ;
	
	private static final String[] buttonNames ;
	// private static final InputDialogWithButtons analysisTypeInputPanel ;

	static
	{
		buttonNames = new String[] {"Linear elástica", "Geometria não-linear", "Material não-linear", "Ambos não-lineares"} ;

		List<JButton> buttons = new ArrayList<>();
		for (int b = 0; b <= buttons.size() - 1; b += 1)
		{
			buttons.add(new JButton (buttonNames[b])) ;
		}

		ActionWithString defineAnalysisType = (String inputAnalysisType) -> analysisType  = inputAnalysisType ;

		// analysisTypeInputPanel = new InputDialogWithButtons("Analysis types", buttons, defineAnalysisType, updateInstructionsPanel) ;
	}
    public MenuAnalysis()
    {
        this.setText("Analysis") ;

        
	    String[] AnalysisMenuItemsNames = new String[] {"Rodar análise", "Opções"};
		runAnalysis = new JMenuItem(AnalysisMenuItemsNames[0], KeyEvent.VK_R);
		runAnalysis.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (isReadyForAnalysis)
				{					
					// analysisTypeInputPanel.activate() ;					
				}
				else
				{
					System.out.println("Structure is not ready for analysis");
				}
			}
		});
		runAnalysis.setEnabled(false);
		runAnalysis.setForeground(Menus.palette[5]);
		this.add(runAnalysis);
    }    
	
	private static void runAnalysis()
	{
		geometryIsNonLinear = buttonNames[1].equals(analysisType) || buttonNames[3].equals(analysisType) ;
		matIsNonLinear = buttonNames[2].equals(analysisType) || buttonNames[3].equals(analysisType) ;

		MenuFunctions.CalcAnalysisParameters(MainPanel.structure, MainPanel.loading);
		Analysis.run(MainPanel.structure, MainPanel.loading, MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, qtdIterations, qtdLoadSteps, maxLoadFactor);
		MenuFunctions.PostAnalysis(MainPanel.structure);
		for (Element elem : MainPanel.structure.getMesh().getElements())
		{
			elem.RecordResults(MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getU(), matIsNonLinear, geometryIsNonLinear);
		}
		Menus.getInstance().ActivatePostAnalysisView(MainPanel.structure);
	}

	public void setRunAnalysis(boolean state) { runAnalysis.setEnabled(state) ;}

}
