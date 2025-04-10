package org.example.userInterface.InputDialogs;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.example.mainTCC.MainPanel;
import org.example.mainTCC.MenuFunctions;
import org.example.service.MenuViewService;
import org.example.structure.ElemType;
import org.example.structure.MeshType;
import org.example.userInterface.MenuBar;
import org.example.utilidades.Util;
import org.example.view.CentralPanel;


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
    private MeshType meshType ;
    private static final JLabel[] labels = new JLabel[] {new JLabel ("N° pontos em x"), new JLabel ("N° pontos em y")};

    public CreateMeshDialog()
    {
        super("Propriedades da malha", "Malha", labels, false) ;
    }
    
    public void setMeshType(MeshType meshType) { this.meshType = meshType ;}

    @Override
    public void onOkClick(double[][] input)
    {        
        CentralPanel.structure.removeSupports() ;
        CentralPanel.loading.clearLoads() ;
        CentralPanel.structure.createMesh(meshType, Util.MatrixDoubleToInt(input), ElemType.valueOf(MenuFunctions.SelectedElemType.toUpperCase())) ;
        MainPanel.getInstance().getCentralPanel().updateDrawings() ;
        MenuViewService.getInstance().switchNodeView();
        MenuViewService.getInstance().switchElemView();
        MainPanel.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion(CentralPanel.structure, CentralPanel.loading) ;
        MenuBar.getInstance().getMenuStructure().updateEnabledSubMenus();
    }

}
