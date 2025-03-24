package org.example.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.example.mainTCC.MenuFunctions;
import org.example.structure.Element;
import org.example.structure.Node;
import org.example.userInterface.Menus;
import org.example.utilidades.Util;

public class EastPanel extends JPanel
{
	private JPanel bp1 ;
    private JPanel bp2 ;
    private JPanel bp3 ;
	private JPanel LDpanel ;
	private LegendPanel legendPanel ;

	private DiagramsPanel diagramsPanel;

    public EastPanel()
    {

        this.setLayout(new GridLayout(0, 1)) ;
        
		legendPanel = new LegendPanel();
		bp1 = Menus.stdPanel(Menus.defaultPanelSize, Menus.palette[2]);
		bp2 = Menus.stdPanel(Menus.defaultPanelSize, Menus.palette[2]);
		bp3 = Menus.stdPanel(Menus.defaultPanelSize, Menus.palette[2]);
		LDpanel = Menus.stdPanel(Menus.defaultPanelSize, Menus.palette[2]);
		bp1.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[1]));
		bp2.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[1]));
		bp3.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[1]));
		LDpanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[1]));

		this.add(bp1);
		this.add(bp2);
		this.add(bp3);
		this.add(legendPanel) ;
		this.add(LDpanel);

        
		diagramsPanel = new DiagramsPanel();

    }

    
	
	public void reset()
	{

		if (MenuFunctions.selectedNodes != null)
		{
			int nodeID = MenuFunctions.selectedNodes.get(0).getID() ;
			if (-1 < nodeID)
			{
				bp1 = createNodeInfoPanel(MainPanel.structure.getMesh().getNodes().get(nodeID)) ;
			}
		}
		if (MainPanel.structure.getMesh().hasElementsSelected())
		{
			Element elem = MainPanel.structure.getMesh().getSelectedElements().get(0) ;
			bp2 = createElemInfoPanel(elem) ;
		}
		this.removeAll();
		this.add(bp1);
		this.add(bp2);

		this.add(bp3);
		this.add(LDpanel);

	}

    public void activatePostAnalysisView()
    {
        this.remove(LDpanel);
        this.remove(bp3);
        LDpanel = diagramsPanel;
        bp3 = legendPanel;
        this.add(bp3);
        this.add(LDpanel);
    }

    public void removeBp1Bp2()
    {
        remove(bp1);
        remove(bp2);
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
					ConcLoads += String.valueOf(Util.Round(Node.getConcLoads().get(load).getLoads()[dof], 2) + ", ");
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
		for (int node = 0; node <= elem.getExternalNodes().length - 1; node += 1)
		{
			NodesText += String.valueOf(elem.getExternalNodes()[node] + " ");
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
		iInfo[0] = new JLabel(" Elem: " + String.valueOf(elem.getID()));
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

}
