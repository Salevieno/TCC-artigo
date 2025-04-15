package org.example.userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.example.Main;
import org.example.mainTCC.MainPanel;
import org.example.mainTCC.MenuFunctions;
import org.example.service.MenuViewService;
import org.example.view.CentralPanel;

public class ToolbarButtons extends JPanel
{
    private static final Color buttonsBGColor ;
    
    private static final JButton buttonEspecial ;
    private static final JButton buttonExample ;
    private static final JButton buttonCreateMesh ;
    private static final JButton buttonCreateMat ;
    private static final JButton buttonCreateSec ;
    private static final JButton buttonCreateConcLoad ;
    private static final JButton buttonCreateDistLoad ;
    private static final JButton buttonCreateNodalDisp ;
    private static final JButton buttonAddMat ;
    private static final JButton buttonAddSec ;
    private static final JButton buttonAddSup ;
    private static final JButton buttonAddConcLoads ;
    private static final JButton buttonAddDistLoads ;
    private static final JButton buttonAddNodalDisps ;
    private static final JButton buttonShowDOFs ;
    private static final JButton buttonShowNodeNumbers ;
    private static final JButton buttonShowElemNumbers ;
    private static final JButton buttonShowElemMats ;
    private static final JButton buttonShowNodes ;
    private static final JButton buttonShowElems ;
    private static final JButton buttonShowElemContours ;
    private static final JButton buttonShowSups ;
    private static final JButton buttonShowConcLoads ;
    private static final JButton buttonShowDistLoads ;
    private static final JButton buttonShowNodalDisps ;
    private static final JButton buttonShowLoadsReactions ;
    private static final JButton buttonShowReactions ;
    
    private static String exampleid ;
    // private static final InputDialogWithButtons exampleInputPanel ;
    private static final String assetsPath = "./assets/Tb1B" ;
    private static final MenuViewService view = MenuViewService.getInstance() ;

    static
    {
        buttonsBGColor = Main.palette[1];
        buttonEspecial = new ButtonToolbarButtons("Especial");
        buttonExample = new ButtonToolbarButtons("Exemplo");
        buttonCreateMesh = new ButtonToolbarButtons("Criar malha");
        buttonCreateMat = new ButtonToolbarButtons("Criar materiais");
        buttonCreateSec = new ButtonToolbarButtons("Criar seções");
        buttonCreateConcLoad = new ButtonToolbarButtons("Criar cargas concentradas");
        buttonCreateDistLoad = new ButtonToolbarButtons("Criar cargas distribuídas");
        buttonCreateNodalDisp = new ButtonToolbarButtons("Criar deslocamentos nodais");
        buttonAddMat = new ButtonToolbarButtons("Adicionar materiais aos elementos");
        buttonAddSec = new ButtonToolbarButtons("Adicionar seções aos elementos");
        buttonAddSup = new ButtonToolbarButtons("Adicionar apoios aos nós");
        buttonAddConcLoads = new ButtonToolbarButtons("Adicionar cargas concentradas aos nós");
        buttonAddDistLoads = new ButtonToolbarButtons("Adicionar cargas distribuídas aos elementos");
        buttonAddNodalDisps = new ButtonToolbarButtons("Adicionar deslocamentos nodais");
        buttonShowDOFs = new ButtonToolbarButtons("Mostrar graus de liberdade");
        buttonShowNodeNumbers = new ButtonToolbarButtons("Mostrar números dos nós");
        buttonShowElemNumbers = new ButtonToolbarButtons("Mostrar números dos elementos");
        buttonShowElemMats = new ButtonToolbarButtons("Mostrar materiais dos elementos");
        buttonShowNodes = new ButtonToolbarButtons("Mostrar nós");
        buttonShowElems = new ButtonToolbarButtons("Mostrar elementos");
        buttonShowElemContours = new ButtonToolbarButtons("Mostrar contornos dos elementos");
        buttonShowSups = new ButtonToolbarButtons("Mostrar apoios");
        buttonShowConcLoads = new ButtonToolbarButtons("Mostrar cargas concentradas");
        buttonShowDistLoads = new ButtonToolbarButtons("Mostrar cargas distribuídas");
        buttonShowNodalDisps = new ButtonToolbarButtons("Mostrar deslocamentos nodais");
        buttonShowLoadsReactions = new ButtonToolbarButtons("Mostrar valores das cargas e reações");
        buttonShowReactions = new ButtonToolbarButtons("Mostrar reações");

        List<JButton> Buttons = new ArrayList<>();
        for (int b = 0; b <= 14 - 1; b += 1) // 14 = qtd tipos de elementos
        {
            Buttons.add(new JButton (String.valueOf(b))) ;
        }

        Runnable updateInstructionsPanel = () -> {
            if (exampleid != null)
            {
                MenuFunctions.RunExample(Integer.parseInt(exampleid));
                MainPanel.getInstance().ActivatePostAnalysisView(MainPanel.getInstance().getCentralPanel().getStructure());
                MenuBar.getInstance().updateEnabledMenus() ;
            }
        } ;
		ActionWithString defineExampleID = (String exampleID) -> exampleid = exampleID ;
        // exampleInputPanel = new InputDialogWithButtons("Example input panel", Buttons, defineExampleID, updateInstructionsPanel) ;
    }

    public ToolbarButtons()
    {        
        this.setLayout(new GridLayout(4, 0));
        this.setBackground(Main.palette[1]);
        this.setVisible(true);
        this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Main.palette[1]));
    
        ButtonToolbarButtons.getAll().forEach(button -> {
            button.setToolTipText(button.getText());
            button.setIcon(new ImageIcon(assetsPath + button.getText() + ".png"));
            button.setFocusable(false);
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            button.setVerticalAlignment(0);
            button.setHorizontalAlignment(0);
            button.setBackground(buttonsBGColor);
            button.setPreferredSize(new Dimension(32, 32));
            button.setMargin(new Insets(0, 0, 0, 0));
            this.add(button);
        }) ;
        
        buttonEspecial.addActionListener(e -> MenuEspecial.Especial()) ;
        // buttonExample.addActionListener(e -> // exampleInputPanel.activate() ;               ) ;
        buttonCreateMesh.addActionListener(e ->
        {
            //StructureMenuCreateMesh("Cartesian", ElemType, ElemShape, Anal);
            // Menus.getInstance().getMenuStructure().StructureMenuCreateMesh(MeshType.radial)
        }) ;
        // buttonCreateMat.addActionListener(e -> // Menus.getInstance().getMenuStructure().createMaterials()) ;
        // buttonCreateSec.addActionListener(e -> // Menus.getInstance().getMenuStructure().createSections()) ;
        // buttonCreateConcLoad.addActionListener(e -> // Menus.getInstance().getMenuStructure().createConcLoads()) ;
        // buttonCreateDistLoad.addActionListener(e -> // Menus.getInstance().getMenuStructure().createDistLoads()) ;
        // buttonCreateNodalDisp.addActionListener(e -> // Menus.getInstance().getMenuStructure().createNodalDisp()) ;
        buttonAddMat.addActionListener(e -> MainPanel.getInstance().getCentralPanel().activateMaterialAssignment()) ;
        buttonAddSec.addActionListener(e -> MainPanel.getInstance().getCentralPanel().activateSectionAssignment()) ;
        buttonAddSup.addActionListener(e -> MainPanel.getInstance().getCentralPanel().activateSupportAssignment()) ;
        buttonAddConcLoads.addActionListener(e -> MainPanel.getInstance().getCentralPanel().activateConcLoadAssignment()) ;
        buttonAddDistLoads.addActionListener(e -> MainPanel.getInstance().getCentralPanel().activateDistLoadAssignment()) ;
        buttonAddNodalDisps.addActionListener(e -> MainPanel.getInstance().getCentralPanel().activateNodalDispAssignment()) ;
        buttonShowDOFs.addActionListener(e -> view.switchDOFNumberView()) ;
        buttonShowNodeNumbers.addActionListener(e -> view.switchNodeNumberView()) ;
        buttonShowElemNumbers.addActionListener(e -> view.switchElemNumberView()) ;
        buttonShowElemMats.addActionListener(e -> view.switchMatView()) ;
        buttonShowNodes.addActionListener(e -> view.switchSecView()) ;
        buttonShowElems.addActionListener(e -> view.switchNodeView()) ;
        buttonShowElemContours.addActionListener(e -> view.switchElemView()) ;
        buttonShowSups.addActionListener(e -> view.switchElemContourView()) ;
        buttonShowConcLoads.addActionListener(e -> view.switchSupView()) ;
        buttonShowDistLoads.addActionListener(e -> view.switchConcLoadsView()) ;
        buttonShowNodalDisps.addActionListener(e -> view.switchDistLoadsView()) ;
        buttonShowLoadsReactions.addActionListener(e -> view.switchNodalDispsView()) ;
        buttonShowReactions.addActionListener(e ->
        {
            view.switchLoadsValuesView() ;
            view.switchReactionValuesView() ;
        }) ;
        // buttonShowReactions.addActionListener(new ActionListener()
        // {
        //     @Override
        //     public void actionPerformed(ActionEvent e) 
        //     {
        //         MenuFunctions.ReactionArrowsView();
        //     }
    }    
}
