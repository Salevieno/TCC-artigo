package org.example.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.example.structure.Element;
import org.example.structure.Node;
import org.example.userInterface.Menus;
import org.example.utilidades.Util;

public class EastPanel extends JPanel
{
	private JPanel nodeInfoPanel ;
    private JPanel elemInfoPanel ;
	private LegendPanel legendPanel ;

	private DiagramsPanel diagramsPanel;

    public EastPanel()
    {

        this.setLayout(new GridLayout(0, 1)) ;
        
		legendPanel = new LegendPanel();
		nodeInfoPanel = Menus.stdPanel(Menus.defaultPanelSize, Menus.palette[2]);
		elemInfoPanel = Menus.stdPanel(Menus.defaultPanelSize, Menus.palette[2]);
		diagramsPanel = new DiagramsPanel();
		nodeInfoPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[1]));
		elemInfoPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[1]));

		this.add(nodeInfoPanel);
		this.add(elemInfoPanel);
		this.add(legendPanel) ;
		this.add(diagramsPanel);
    }
	
	public void reset()
	{

		if (MainPanel.structure.getMesh().hasNodesSelected())
		{
			Node node = MainPanel.structure.getMesh().getSelectedNodes().get(0) ;
			nodeInfoPanel = createNodeInfoPanel(node) ;
		}
		if (MainPanel.structure.getMesh().hasElementsSelected())
		{
			Element elem = MainPanel.structure.getMesh().getSelectedElements().get(0) ;
			elemInfoPanel = createElemInfoPanel(elem) ;
		}
		// this.removeAll();
		// this.add(nodeInfoPanel);
		// this.add(elemInfoPanel);
		// this.add(legendPanel) ;
		// this.add(diagramsPanel);

	}

    public void activatePostAnalysisView()
    {
        // this.remove(diagramsPanel);
        // this.remove(bp3);
        // bp3 = legendPanel;
        // this.add(bp3);
        // this.add(diagramsPanel);
    }

    public void removeBp1Bp2()
    {
        remove(nodeInfoPanel);
        remove(elemInfoPanel);
    }
	
	private JPanel createNodeInfoPanel(Node Node)
	{
		JPanel NodeInfoPanel = new JPanel(new GridLayout(0,1));
		Color TextColor = Menus.palette[4];
		NodeInfoPanel.setBackground(Menus.palette[9]);
		NodeInfoPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[5]));
		NodeInfoPanel.setSize(Menus.defaultPanelSize);		

		String OriginalCoords = "", DeformedCoords = "";
		String ConcLoads = String.valueOf(0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0);
		OriginalCoords += String.valueOf(Util.Round(Node.getOriginalCoords().x, 2)) + "," ;
		OriginalCoords += String.valueOf(Util.Round(Node.getOriginalCoords().y, 2)) + "," ;
		OriginalCoords += String.valueOf(Util.Round(Node.getOriginalCoords().z, 2)) ;
		DeformedCoords += String.valueOf(Util.Round(Node.getDisp().x, 2)) + "," ;
		DeformedCoords += String.valueOf(Util.Round(Node.getDisp().y, 2)) + "," ;
		DeformedCoords += String.valueOf(Util.Round(Node.getDisp().z, 2)) + "," ;
		if (Node.getConcLoads() != null)
		{
			ConcLoads = "";
			for (int load = 0; load <= Node.getConcLoads().size() - 1; load += 1)
			{
				for (int dof = 0; dof <= 6 - 1; dof += 1)
				{
					ConcLoads += String.valueOf(Util.Round(Node.getConcLoads().get(load).getForce().array()[dof], 2) + ", ");
				}
			}
		}
		
		JLabel iLabel = new JLabel("Informaçõs do nâ");
		NodeInfoPanel.add(iLabel);
		iLabel.setForeground(TextColor);
		
		JLabel[] iInfo = new JLabel[4];
		iInfo[0] = new JLabel(" Nâ: " + String.valueOf(Node.getID()));
		iInfo[1] = new JLabel(" Original pos: " + OriginalCoords);
		iInfo[2] = new JLabel(" Deslocamentos: " + DeformedCoords);
		iInfo[3] = new JLabel(" Forâas: " + ConcLoads);
		for (int i = 0; i <= iInfo.length - 1; i += 1)
		{
			NodeInfoPanel.add(iInfo[i]);
		}
		
		return NodeInfoPanel;
	}
	
	private JPanel createElemInfoPanel(Element elem)
	{
		JPanel ElemInfoPanel = new JPanel(new GridLayout(0,1));
		Color TextColor = Menus.palette[4];
		
		ElemInfoPanel.setBackground(Menus.palette[9]);
		ElemInfoPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[5]));
		ElemInfoPanel.setSize(Menus.defaultPanelSize);		

		String NodesText = "";
		for (int i = 0; i <= elem.getExternalNodes().size() - 1; i += 1)
		{
			NodesText += String.valueOf(elem.getExternalNodes().get(i) + " ");
		}
		String MatText = null;
		if (elem.getMat() != null)
		{
			MatText = String.valueOf(Util.Round(elem.getMat().getE(), 2)) + " MPa v = " + String.valueOf(Util.Round(elem.getMat().getV(), 1)) + " G = " + String.valueOf(Util.Round(elem.getMat().getG(), 2)) + " Mpa";
		}
		String SecText = null;
		if (elem.getSec() != null)
		{
			SecText = String.valueOf(Util.Round(elem.getSec().getT(), 0)) + " mm";
		}
		
		JLabel iLabel = new JLabel("Informaçõs do elemento");
		ElemInfoPanel.add(iLabel);
		iLabel.setForeground(TextColor);
		
		JLabel[] iInfo = new JLabel[4];
		iInfo[0] = new JLabel(" Elem: " + String.valueOf(elem.getId()));
		iInfo[1] = new JLabel(" Nâs: " + NodesText);
		iInfo[2] = new JLabel(" Material: E = " + MatText);
		iInfo[3] = new JLabel(" Seââo: t = " + SecText);
		
		for (int i = 0; i <= iInfo.length - 1; i += 1)
		{
			ElemInfoPanel.add(iInfo[i]);
		}
		
		return ElemInfoPanel;
	}



	public LegendPanel getLegendPanel() { return legendPanel ;}
	public DiagramsPanel getDiagramsPanel() { return diagramsPanel ;}

}
