package org.example.userInterface.InputDialogs;

import javax.swing.JLabel;

import org.example.main.MainPanel;
import org.example.service.MenuViewService;
import org.example.structure.ElemType;
import org.example.structure.MeshType;
import org.example.userInterface.MenuBar;
import org.example.utilidades.Util;


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
        MainPanel.getInstance().getCentralPanel().getStructure().removeSupports() ;
        MainPanel.getInstance().getCentralPanel().getLoading().clearLoads() ;
        MainPanel.getInstance().getCentralPanel().getStructure().createMesh(meshType, Util.MatrixDoubleToInt(input), ElemType.valueOf(MainPanel.getInstance().getCentralPanel().getElemType().toUpperCase())) ;
        MainPanel.getInstance().getCentralPanel().updateDrawings() ;
        MenuViewService.getInstance().switchNodeView();
        MenuViewService.getInstance().switchElemView();
        MainPanel.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion(MainPanel.getInstance().getCentralPanel().getStructure(), MainPanel.getInstance().getCentralPanel().getLoading()) ;
        MenuBar.getInstance().getMenuStructure().updateEnabledSubMenus();
    }

}
