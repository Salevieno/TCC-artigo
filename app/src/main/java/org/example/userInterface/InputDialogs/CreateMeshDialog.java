package org.example.userInterface.InputDialogs;

import java.util.Arrays;

import javax.swing.JLabel;

import org.example.mainTCC.MenuFunctions;
import org.example.structure.ElemType;
import org.example.structure.MeshType;
import org.example.userInterface.MenuStructure;
import org.example.userInterface.Menus;
import org.example.utilidades.Util;
import org.example.view.MainPanel;


public class CreateMeshDialog extends InputDialogWithGrid
{	
		
    // JLabel[] createMeshLabels = new JLabel[2];
    // if (MeshType.cartesian.equals(meshType))
    // {
    // 	createMeshLabels = new JLabel[] {new JLabel ("N° pontos em x"), new JLabel ("N° pontos em y")};
    // }
    // else if (MeshType.radial.equals(meshType))
    // {
    // 	createMeshLabels = new JLabel[] {new JLabel ("N° camadas"), new JLabel ("N° pontos por camada")};
    // }
    private static final JLabel[] labels = new JLabel[] {new JLabel ("N° pontos em x"), new JLabel ("N° pontos em y")};

    public CreateMeshDialog()
    {
        super("Propriedades da malha", "Malha", labels, false) ;
    }

    @Override
    public void onOkClick(double[][] input)
    {        
			MainPanel.structure.removeSupports() ;
			MainPanel.loading.clearLoads() ;
			MainPanel.structure.createMesh(MeshType.cartesian, Util.MatrixDoubleToInt(input), ElemType.valueOf(MenuFunctions.SelectedElemType.toUpperCase())) ;
            Menus.getInstance().getMainPanel().updateDrawings() ;
			MenuFunctions.NodeView();
			MenuFunctions.ElemView();
			Menus.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion() ;
			MenuStructure.updateEnabledSubMenus();
    }

}
