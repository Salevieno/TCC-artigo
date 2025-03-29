package org.example.userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.analysis.Analysis;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.Element;
import org.example.userInterface.InputDialogs.DefineAnalysisDialog;
import org.example.view.MainPanel;

public class MenuAnalysis extends JMenu
{
    
	private JMenuItem runAnalysis;
    private boolean isReadyForAnalysis;
	private int analysisTypeID = -1 ;
	private static boolean geometryIsNonLinear ;
	private static boolean matIsNonLinear ;
	private static int qtdIterations = 1 ;
	private static int qtdLoadSteps = 1;
	private static double maxLoadFactor = 1;
	
	private static final DefineAnalysisDialog analysisTypeInputPanel ;

	static
	{
		analysisTypeInputPanel = new DefineAnalysisDialog() ;
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
					analysisTypeInputPanel.activate() ;
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

	public void updateIsReadyForAnalysis()
	{
        isReadyForAnalysis = MenuFunctions.CheckIfAnalysisIsReady(MainPanel.structure, MainPanel.loading);
	}

	public void setAnalysisTypeID(int analysisTypeID) { this.analysisTypeID = analysisTypeID ;}
	
	public static void runAnalysis(int analysisTypeID)
	{
		geometryIsNonLinear = analysisTypeID == 1 || analysisTypeID == 3 ;
		matIsNonLinear = analysisTypeID == 2 || analysisTypeID == 3 ;
		MenuFunctions.CalcAnalysisParameters(MainPanel.structure, MainPanel.loading);
		Analysis.run(MainPanel.structure, MainPanel.loading, MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, qtdIterations, qtdLoadSteps, maxLoadFactor);
		MenuFunctions.PostAnalysis(MainPanel.structure);
		// for (Element elem : MainPanel.structure.getMesh().getElements())
		// {
		// 	elem.RecordResults(MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getU(), matIsNonLinear, geometryIsNonLinear);
		// }
		// Menus.getInstance().ActivatePostAnalysisView(MainPanel.structure);
	}

	public void setRunAnalysis(boolean state) { runAnalysis.setEnabled(state) ;}

}
