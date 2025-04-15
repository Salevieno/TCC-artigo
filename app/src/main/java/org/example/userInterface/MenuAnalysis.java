package org.example.userInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.example.Main;
import org.example.analysis.Analysis;
import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.Force;
import org.example.loading.Loading;
import org.example.mainTCC.MainPanel;
import org.example.mainTCC.MenuFunctions;
import org.example.service.MenuViewService;
import org.example.structure.Element;
import org.example.structure.Node;
import org.example.structure.Reactions;
import org.example.structure.Structure;
import org.example.userInterface.InputDialogs.DefineAnalysisDialog;
import org.example.utilidades.Util;
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
	private static MenuViewService view = MenuViewService.getInstance() ;

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

	
	public static void PostAnalysis(Structure structure, boolean nonlinearMat, boolean nonlinearGeo)
	{
		/* Record results and set up their view*/
		structure.setU(structure.getU());
		Reactions[] reactions = Analysis.Reactions(structure.getMesh(), structure.getSupports(), nonlinearMat, nonlinearGeo, structure.getU());
		structure.setReactions(reactions);
		for (Element elem : structure.getMesh().getElements())
		{
			elem.RecordResults(structure.getMesh().getNodes(), structure.getU(), nonlinearMat, nonlinearGeo);
		}
		structure.getResults().register(structure.getMesh(), structure.getSupports(), structure.getU(), nonlinearMat, nonlinearGeo);
		System.out.println(structure.getResults()) ;

		view.nodes = true;
		view.elems = true;
		view.reactionArrows = true;
		view.deformedStructure = true;

		CentralPanel.activateNodeSelection() ;
		CentralPanel.activateElemSelection() ;
		double MaxDisp = Util.FindMaxAbs(structure.getU());
		MenuFunctions.updateDiagramScaleY(MaxDisp) ;
		MenuFunctions.AnalysisIsComplete = true ;
		Reactions.setSumReactions(Analysis.SumReactions(reactions));

		MainPanel.getInstance().getWestPanel().setStructure(structure) ;
		CentralPanel.activateNodeSelection() ;
		CentralPanel.deactivateElemSelection() ;
				
		System.out.println("Max disp: " + MaxDisp);
	}	
	public static void CalcAnalysisParameters(Structure structure, Loading loading, List<Force> concLoadTypes, double[][] DistLoadType)
	{
		structure.calcAndAssignDOFs() ;
		structure.getMesh().assignElementDOFs() ;
		for (Element elem : structure.getMesh().getElements())
		{
			int[][] ElemNodeDOF = null;
			for (Node node : elem.getExternalNodes())
	    	{
				ElemNodeDOF = Util.AddElem(ElemNodeDOF, structure.getMesh().getNodes().get(node.getID()).getDofs());
	    	}
			elem.setNodeDOF(ElemNodeDOF);
		}
		if (concLoadTypes != null)
		{
			for (int loadid = 0; loadid <= concLoadTypes.size() - 1; loadid += 1)
			{
				int nodeid = (int) concLoadTypes.get(loadid).y;
				if (-1 < nodeid)
				{
					loading.getConcLoads().set(loadid, new ConcLoad(concLoadTypes.get(loadid))) ;
					structure.getMesh().getNodes().get(nodeid).addConcLoad(loading.getConcLoads().get(loadid));
				}
			}
		}

		// for (DistLoad distLoad : loading.getDistLoads())
		// {
		// 	int elemid = distLoad.getElem() ;
		// 	if (-1 < elemid)
		// 	{
		// 		struct.getMesh().getElements().get(elemid).addDistLoad(distLoad) ;
		// 	}
		// }

		if (DistLoadType != null)
		{
			for (int loadid = 0; loadid <= DistLoadType.length - 1; loadid += 1)
			{
				int elemid = (int) DistLoadType[loadid][0];
				int LoadType = (int) DistLoadType[loadid][1];
				double Intensity = DistLoadType[loadid][2];
				if (-1 < elemid)
				{
					DistLoad distLoad = new DistLoad(LoadType, Intensity) ;
					loading.getDistLoads().set(loadid, distLoad) ;
					structure.getMesh().getElements().get(elemid).addDistLoad(distLoad);
				}
			}
		}
	}

	public void runAnalysis(int analysisTypeID, List<Force> concLoadTypes, double[][] DistLoadType)
	{
		// geometryIsNonLinear = analysisTypeID == 1 || analysisTypeID == 3 ;
		// matIsNonLinear = analysisTypeID == 2 || analysisTypeID == 3 ;
		CalcAnalysisParameters(MainPanel.getInstance().getCentralPanel().getStructure(), MainPanel.getInstance().getCentralPanel().getLoading(), concLoadTypes, DistLoadType);
		Analysis.run(MainPanel.getInstance().getCentralPanel().getStructure(), MainPanel.getInstance().getCentralPanel().getLoading(), MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, qtdIterations, qtdLoadSteps, maxLoadFactor);
		PostAnalysis(MainPanel.getInstance().getCentralPanel().getStructure(), MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo);
		// for (Element elem : MainPanel.structure.getMesh().getElements())
		// {
		// 	elem.RecordResults(MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getU(), matIsNonLinear, geometryIsNonLinear);
		// }
		MainPanel.getInstance().ActivatePostAnalysisView(MainPanel.getInstance().getCentralPanel().getStructure());
		MenuBar.getInstance().updateEnabledMenus() ;
	}

	public void setAnalysisTypeID(int analysisTypeID) { this.analysisTypeID = analysisTypeID ;}
	public void setRunAnalysisEnabled(boolean state) { runAnalysis.setEnabled(state) ;}

}
