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
import org.example.main.MainPanel;
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
    private boolean analysisIsComplete;
	// private int analysisTypeID = -1 ;
	private static boolean geometryIsNonLinear ;
	private static boolean matIsNonLinear ;
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
        isReadyForAnalysis = structure.getMesh() != null &&
							structure.getMesh().getNodes() != null && structure.getMesh().getElements() != null &&
							structure.getMesh().allElementsHaveMat() && structure.getMesh().allElementsHaveSec() && structure.getSupports() != null &&
							loading != null;
		setRunAnalysisEnabled(isReadyForAnalysis) ;
	}

	
	public void PostAnalysis(Structure structure, boolean nonlinearMat, boolean nonlinearGeo)
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
		MainPanel.getInstance().getCentralPanel().updateDiagramScaleY(MaxDisp) ;
		analysisIsComplete = true ;
		Reactions.setSumReactions(Analysis.SumReactions(reactions));

		MainPanel.getInstance().getWestPanel().setStructure(structure) ;
		CentralPanel.activateNodeSelection() ;
		CentralPanel.deactivateElemSelection() ;
				
		System.out.println("Max disp: " + MaxDisp);
	}	
	public static void CalcAnalysisParameters(Structure structure, Loading loading, List<Force> concLoadTypes, List<Double[]> DistLoadType)
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
			for (int loadid = 0; loadid <= DistLoadType.size() - 1; loadid += 1)
			{
				int elemid = (int) (double) DistLoadType.get(loadid)[0];
				int LoadType = (int) (double) DistLoadType.get(loadid)[1];
				double Intensity = DistLoadType.get(loadid)[2];
				if (-1 < elemid)
				{
					DistLoad distLoad = new DistLoad(LoadType, Intensity) ;
					loading.getDistLoads().set(loadid, distLoad) ;
					structure.getMesh().getElements().get(elemid).addDistLoad(distLoad);
				}
			}
		}
	}

	public void runAnalysis(int analysisTypeID, List<Force> concLoadTypes, List<Double[]> DistLoadType)
	{
		// geometryIsNonLinear = analysisTypeID == 1 || analysisTypeID == 3 ;
		// matIsNonLinear = analysisTypeID == 2 || analysisTypeID == 3 ;
		CalcAnalysisParameters(MainPanel.getInstance().getCentralPanel().getStructure(), MainPanel.getInstance().getCentralPanel().getLoading(), concLoadTypes, DistLoadType);
		Analysis.run(MainPanel.getInstance().getCentralPanel().getStructure(), MainPanel.getInstance().getCentralPanel().getLoading(), matIsNonLinear, geometryIsNonLinear, qtdIterations, qtdLoadSteps, maxLoadFactor);
		PostAnalysis(MainPanel.getInstance().getCentralPanel().getStructure(), matIsNonLinear, geometryIsNonLinear);
		// for (Element elem : MainPanel.structure.getMesh().getElements())
		// {
		// 	elem.RecordResults(MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getU(), matIsNonLinear, geometryIsNonLinear);
		// }
		MainPanel.getInstance().ActivatePostAnalysisView(MainPanel.getInstance().getCentralPanel().getStructure());
		MenuBar.getInstance().updateEnabledMenus() ;
	}

	
	public boolean isAnalysisIsComplete() { return analysisIsComplete ;}
	public void setAnalysisIsComplete(boolean analysisIsComplete) { this.analysisIsComplete = analysisIsComplete ;}
	
	// public void setAnalysisTypeID(int analysisTypeID) { this.analysisTypeID = analysisTypeID ;}
	public void setRunAnalysisEnabled(boolean state) { runAnalysis.setEnabled(state) ;}

}
