package org.example.userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.Main;
import org.example.analysis.Analysis;
import org.example.loading.Loading;
import org.example.mainTCC.MainPanel;
import org.example.mainTCC.MenuFunctions;
import org.example.structure.Structure;
import org.example.userInterface.InputDialogs.DefineAnalysisDialog;
import org.example.view.CentralPanel;

public class MenuAnalysis extends JMenu
{
    
	private JMenuItem runAnalysis;
    private boolean isReadyForAnalysis;
	private int analysisTypeID = -1 ;
	// private static boolean geometryIsNonLinear ;
	// private static boolean matIsNonLinear ;
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
		runAnalysis.setForeground(Main.palette[5]);
		this.add(runAnalysis);
    }

	public void updateIsReadyForAnalysis(Structure structure, Loading loading)
	{
        isReadyForAnalysis = MenuFunctions.CheckIfAnalysisIsReady(structure, loading);
		setRunAnalysisEnabled(isReadyForAnalysis) ;
	}

	public void setAnalysisTypeID(int analysisTypeID) { this.analysisTypeID = analysisTypeID ;}
	
	public static void runAnalysis(int analysisTypeID)
	{
		// geometryIsNonLinear = analysisTypeID == 1 || analysisTypeID == 3 ;
		// matIsNonLinear = analysisTypeID == 2 || analysisTypeID == 3 ;
		MenuFunctions.CalcAnalysisParameters(CentralPanel.structure, CentralPanel.loading);
		Analysis.run(CentralPanel.structure, CentralPanel.loading, MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, qtdIterations, qtdLoadSteps, maxLoadFactor);
		MenuFunctions.PostAnalysis(CentralPanel.structure);
		// for (Element elem : MainPanel.structure.getMesh().getElements())
		// {
		// 	elem.RecordResults(MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getU(), matIsNonLinear, geometryIsNonLinear);
		// }
		MainPanel.getInstance().ActivatePostAnalysisView(CentralPanel.structure);
		MenuBar.getInstance().updateEnabledMenus() ;
	}

	public void setRunAnalysisEnabled(boolean state) { runAnalysis.setEnabled(state) ;}

}
