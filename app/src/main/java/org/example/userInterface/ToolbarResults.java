package org.example.userInterface;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.example.structure.Structure;
import org.example.view.MainPanel;

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
                    MainPanel.SelectedDiagram = cbResults.getSelectedIndex();
                    MainPanel.SelectedVar = cbSubRes.getSelectedIndex();
                    MainPanel.ShowResult(structure);
                }
            }
        });
        btnCalcular.setFocusable(false);
        this.add(btnCalcular);
    }

    public void setStructure(Structure structure) { this.structure = structure ;}
}
