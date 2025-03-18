package org.example.userInterface;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.example.mainTCC.Analysis;
import org.example.output.Results;
import org.example.structure.Structure;
import org.example.utilidades.Util;

public class ToolbarResults extends JPanel
{
    Structure structure ;

    public ToolbarResults()
    {
        /* Listas no segundo painel*/
        this.setLayout(new GridLayout(5, 0));
        this.setBackground(Menus.palette[1]);
        this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[1]));
		this.setVisible(false);
        
        String[] ResultsNames = new String[]{"Deslocamentos", "Tensâes", "Deformaçõs", "Forâas Internas"};
        JComboBox<String> cbResults = new JComboBox<>(ResultsNames);
        cbResults.setFocusable(false);
        this.add(cbResults);
        
        String[] SubResultsNames = new String[]{"X", "Y", "Z", "Tx", "Ty", "Tz"};
        JComboBox<String> cbSubRes = new JComboBox<>(SubResultsNames);
        cbSubRes.setFocusable(false);
        this.add(cbSubRes);
        
        JButton btnCalcular = new JButton("Calcular");
        btnCalcular.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if (structure != null)
                {
                    int selectedDiagram = cbResults.getSelectedIndex();
                    int selectedVar = cbSubRes.getSelectedIndex();

                    //DrawDisplacementContours(selectedVar);
                    structure.getResults().setDispMin(Results.FindMinDisps(structure.getU(), structure.getMesh().getElements().get(0).getDOFs(), Analysis.DefineFreeDoFTypes(structure.getMesh().getNodes()))) ;
                    structure.getResults().setDispMax(Results.FindMaxDisps(structure.getU(), structure.getMesh().getElements().get(0).getDOFs(), Analysis.DefineFreeDoFTypes(structure.getMesh().getNodes()))) ;
                    
                    for (int node = 0; node <= structure.getMesh().getElements().get(0).getExternalNodes().length - 1; node += 1)
                    {
                        selectedVar = Util.ElemPosInArray(structure.getMesh().getElements().get(0).getDOFsPerNode()[node], selectedVar);
                        if (-1 < selectedVar)
                        {
                            break ;
                        }
                    }

                    // ShowResult(structure);
                    Menus.getInstance().getEastPanel().getLegendPanel().switchDisplay(selectedDiagram, selectedVar) ;
                    Menus.getInstance().getMainPanel().switchDisplay(selectedDiagram, selectedVar) ;
                }
            }
        });
        btnCalcular.setFocusable(false);
        this.add(btnCalcular);
    }

    public void setStructure(Structure structure) { this.structure = structure ;}
}
