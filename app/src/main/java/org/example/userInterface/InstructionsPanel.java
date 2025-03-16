package org.example.userInterface;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.example.mainTCC.MenuFunctions;
import org.example.structure.Structure;
import org.example.utilidades.Util;
import org.example.view.DiagramsPanel;
import org.example.view.MainPanel;

public class InstructionsPanel extends JPanel
{

    List<String> stepText = Collections.unmodifiableList(new ArrayList<String>()
    {{
        add("1. Tipo de elemento");
        add("2. Estrutura");
        add("3. Malha");
        add("4. Elementos com materiais");
        add("5. Elementos com seçõs");
        add("6. Apoios");
        add("7. Cargas concentradas");
        add("8. Cargas distribuâdas");
        add("9. Deslocamentos nodais");
    }}) ;

    List<Boolean> stepIsComplete = new ArrayList<>(Collections.nCopies(9, false)) ;
    private static final JLabel title ;
    private static final ImageIcon okIcon ;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;

    static
    {
        title = new JLabel("Passo a passo", 2) ;
		title.setForeground(Menus.palette[0]);
        okIcon = new ImageIcon("./Icons/OkIcon.png");
    }

    public InstructionsPanel()
    {
        this.setLayout(new GridLayout(0,1));
		this.setBackground(Menus.palette[3]);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, null, Menus.palette[1], null, Menus.palette[2])) ;
		this.setSize(Menus.defaultPanelSize);
        // setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[1]));
        
		this.add(title);

        stepText.forEach(step -> {
            JLabel label = new JLabel("    " + step);
            label.setForeground(Menus.palette[0]);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            this.add(label);
        }) ;
    }

    public void updateStepsCompletion()
    {
        stepIsComplete = CheckSteps(MainPanel.structure);
        for (int i = 0; i < stepIsComplete.size(); i++)
        {
            JLabel label = (JLabel) this.getComponent(i+1);
            ImageIcon updatedIcon = stepIsComplete.get(i) ? okIcon : null;
            label.setIcon(updatedIcon);
        }
        
        // boolean ReadyForAnalysis = MenuFunctions.CheckIfAnalysisIsReady(MainPanel.structure, MainPanel.loading);
    }
    
	public List<Boolean> CheckSteps(Structure structure)
	{
		List<Boolean> stepIsComplete = new ArrayList<>(Collections.nCopies(9, false)) ;
        if (structure.getMesh() != null && structure.getMesh().getElements() != null)
        {
    		if (Util.AllElemsHaveElemType(structure.getMesh().getElements()))
    		{
    			stepIsComplete.set(0, true) ;
    		}
        }
		if (MenuFunctions.SelectedElemType != null)
		{
			stepIsComplete.set(0, true) ;
		}
        if (structure.getCoords() != null)
        {
    		stepIsComplete.set(1, true) ;
        }
        if (structure.getMesh() != null && structure.getMesh().getNodes() != null && structure.getMesh().getElements() != null)
        {
    		stepIsComplete.set(2, true) ;
        }
        if (structure.getMesh() != null && structure.getMesh().getElements() != null)
        {
    		if (Util.AllElemsHaveMat(structure.getMesh().getElements()))
    		{
    			stepIsComplete.set(3, true) ;
    		}
    		if (Util.AllElemsHaveSec(structure.getMesh().getElements()))
    		{
    			stepIsComplete.set(4, true) ;
    		}
        }
		if (structure.getSupports() != null)
		{
			stepIsComplete.set(5, true) ;
		}
		if (MainPanel.loading != null && MainPanel.loading.getConcLoads() != null)
		{
			stepIsComplete.set(6, true) ;
		}
		if (MainPanel.loading != null && MainPanel.loading.getDistLoads() != null)
		{
			stepIsComplete.set(7, true) ;
		}
		if (MainPanel.loading != null && MainPanel.loading.getNodalDisps() != null)
		{
			stepIsComplete.set(8, true) ;
		}

		return stepIsComplete;
	}

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        DP.setG(g);
        DP.setRealStructCenter(MainPanel.structure.getCenter());
        DiagramsPanel.display(MainPanel.structure, MenuFunctions.selectedNodes, MainPanel.SelectedVar, MainPanel.SelectedDiagram, MenuFunctions.AnalysisIsComplete, DP);
        repaint();
    }
}
