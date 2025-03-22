package org.example.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.example.mainTCC.MenuFunctions;
import org.example.structure.Node;
import org.example.structure.Structure;
import org.example.userInterface.DrawingOnAPanel;
import org.example.userInterface.Menus;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;

public class DiagramsPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    
    private static final Dimension initialSize = new Dimension(0, 100) ;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;

    public DiagramsPanel()
    {
        setSize(initialSize);
        setBackground(Menus.palette[3]);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Curva carga deslocamento", TitledBorder.CENTER, TitledBorder.CENTER));

    }

    
	
	public static void display(Structure structure, List<Node> selectedNodes, int SelectedVar, int SelectedDiagram, boolean AnalysisIsComplete, DrawingOnAPanel DP)
	{
		if (AnalysisIsComplete)
		{
			if (selectedNodes != null)
			{
				int nodeid = selectedNodes.get(0).getID() ;
				if (structure.getMesh().getNodes().get(nodeid) != null)
				{
					Dimension panelSize = initialSize;
					int[] CurvePos = new int[] {(int) (0.38 * panelSize.getWidth()), (int) (0.8 * panelSize.getHeight())};
					int[] CurveSize = new int[] {(int) (0.8 * panelSize.getWidth()), (int) (0.6 * panelSize.getHeight())};
					if (1 < selectedNodes.size())
					{
						double[] Xaxisvalues = new double[selectedNodes.size()], Yaxisvalues = new double[selectedNodes.size()];
						int dir = -1;
						int dof = SelectedVar;
						
						Point3D FirstNodePos = selectedNodes.get(0).getOriginalCoords();
						Point3D FinalNodePos = selectedNodes.get(selectedNodes.size() - 1).getOriginalCoords();
						if (FinalNodePos.y - FirstNodePos.y <= FinalNodePos.x - FirstNodePos.x)
						{
							dir = 0;
						}
						else
						{
							dir = 1;
						}
						
						for (int i = 0; i <= selectedNodes.size() - 1; i += 1)
						{
							int nodeID = selectedNodes.get(i).getID();
							double minCoord = dir == 0 ? Structure.calcMinCoords(structure.getCoords()).x : Structure.calcMinCoords(structure.getCoords()).y ;
							if (dir == 0)
							{
								Xaxisvalues[i] = structure.getMesh().getNodes().get(nodeID).getOriginalCoords().x - minCoord;
							}
							else
							{
								Xaxisvalues[i] = structure.getMesh().getNodes().get(nodeID).getOriginalCoords().y - minCoord;
							}
						}
						if (SelectedDiagram == 0)
						{
							for (int i = 0; i <= selectedNodes.size() - 1; i += 1)
							{
								int nodeID = selectedNodes.get(i).getID();
								Yaxisvalues[i] = structure.getMesh().getNodes().get(nodeID).getDisp().asArray()[dof];
							}
						}
						else if (SelectedDiagram == 1)
						{
							for (int node = 0; node <= selectedNodes.size() - 1; node += 1)
							{
								int nodeID = selectedNodes.get(node).getID();
								int elemID = -1;
								for (int i = 0; i <= structure.getMesh().getElements().size() - 1; i += 1)
								{
									if (Util.ArrayContains(structure.getMesh().getElements().get(i).getExternalNodes(), nodeID))
									{
										elemID = i;
									}
								}
								Yaxisvalues[node] = structure.getMesh().getElements().get(elemID).getStress()[dof];
							}
						}
						else if (SelectedDiagram == 2)
						{
							for (int node = 0; node <= selectedNodes.size() - 1; node += 1)
							{
								int nodeID = selectedNodes.get(node).getID();
								int elemID = -1;
								for (int i = 0; i <= structure.getMesh().getElements().size() - 1; i += 1)
								{
									if (Util.ArrayContains(structure.getMesh().getElements().get(i).getExternalNodes(), nodeID))
									{
										elemID = i;
									}
								}
								Yaxisvalues[node] = structure.getMesh().getElements().get(elemID).getStrain()[dof];
							}
						}
						else if (SelectedDiagram == 3)
						{
							for (int node = 0; node <= selectedNodes.size() - 1; node += 1)
							{
								int nodeID = selectedNodes.get(node).getID();
								int elemID = -1;
								for (int i = 0; i <= structure.getMesh().getElements().size() - 1; i += 1)
								{
									if (Util.ArrayContains(structure.getMesh().getElements().get(i).getExternalNodes(), nodeID))
									{
										elemID = i;
									}
								}
								Yaxisvalues[node] = structure.getMesh().getElements().get(elemID).getIntForces()[dof];
							}
						}
						DP.Draw2DPlot(CurvePos, Math.min(CurveSize[0], CurveSize[1]), "Resultados na seââo", "x var", "y var",
						Xaxisvalues, Yaxisvalues, Util.FindMin(Xaxisvalues), Util.FindMin(Yaxisvalues),
						Util.FindMaxAbs(Xaxisvalues), Util.FindMaxAbs(Yaxisvalues), 2, 2, Menus.palette[5], Menus.palette[10]);
					}
					else if (-1 < selectedNodes.get(0).getID())
					{
						if (-1 < SelectedVar)
						{						
							double[] XValues = structure.getMesh().getNodes().get(nodeid).LoadDisp[SelectedVar][0];
							double[] YValues = structure.getMesh().getNodes().get(nodeid).LoadDisp[SelectedVar][1];
							DP.Draw2DPlot(CurvePos, Math.min(CurveSize[0], CurveSize[1]),
							"Curva carga-deslocamento", "u (mm)", "Fator de carga", XValues, YValues,
							Util.FindMin(XValues), Util.FindMin(YValues), Util.FindMaxAbs(XValues), Util.FindMaxAbs(YValues), 3, 3, Menus.palette[5], Menus.palette[10]);					
						}
					}
				}
			}
		}						
	}

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        DP.setG(g);
        display(MainPanel.structure, MenuFunctions.selectedNodes, MainPanel.SelectedVar, MainPanel.SelectedDiagram, MenuFunctions.AnalysisIsComplete, DP) ;
        repaint();
    }
}
