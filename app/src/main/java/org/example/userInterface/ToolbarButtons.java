package org.example.userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.example.mainTCC.MenuFunctions;
import org.example.service.MenuViewService;
import org.example.view.MainPanel;

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
        buttonsBGColor = Menus.palette[1];
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
                Menus.getInstance().ActivatePostAnalysisView(MainPanel.structure);
            }
        } ;
		ActionWithString defineExampleID = (String exampleID) -> exampleid = exampleID ;
        // exampleInputPanel = new InputDialogWithButtons("Example input panel", Buttons, defineExampleID, updateInstructionsPanel) ;
    }

    public ToolbarButtons()
    {        
        this.setLayout(new GridLayout(4, 0));
        this.setBackground(Menus.palette[1]);
        this.setVisible(true);
        this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Menus.palette[1]));
    
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
        
        buttonEspecial.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                MenuFunctions.Especial();
            }
        });
        buttonExample.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // exampleInputPanel.activate() ;                
            }
        });
        buttonCreateMesh.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                //StructureMenuCreateMesh("Cartesian", ElemType, ElemShape, Anal);
                // Menus.getInstance().getMenuStructure().StructureMenuCreateMesh(MeshType.radial);
            }
        });
        buttonCreateMat.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Menus.getInstance().getMenuStructure().createMaterials();
            }
        });
        buttonCreateSec.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Menus.getInstance().getMenuStructure().createSections();
            }
        });
        buttonCreateConcLoad.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Menus.getInstance().getMenuStructure().createConcLoads();
            }
        });
        buttonCreateDistLoad.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Menus.getInstance().getMenuStructure().createDistLoads();
            }
        });
        buttonCreateNodalDisp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                // Menus.getInstance().getMenuStructure().createNodalDisp();
            }
        });
        buttonAddMat.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Menus.getInstance().getMainPanel().activateMaterialAssignment();
            }
        });
        buttonAddSec.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Menus.getInstance().getMainPanel().activateSectionAssignment();
            }
        });
        buttonAddSup.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Menus.getInstance().getMainPanel().activateSupportAssignment();
            }
        });
        buttonAddConcLoads.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Menus.getInstance().getMainPanel().activateConcLoadAssignment();
            }
        });
        buttonAddDistLoads.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Menus.getInstance().getMainPanel().activateDistLoadAssignment();
            }
        });
        buttonAddNodalDisps.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Menus.getInstance().getMainPanel().activateNodalDispAssignment();
            }
        });
        buttonShowDOFs.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchDOFNumberView();
            }
        });
        buttonShowNodeNumbers.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchNodeNumberView();
            }
        });
        buttonShowElemNumbers.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchElemNumberView();
            }
        });
        buttonShowElemMats.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchMatView();
            }
        });
        buttonShowNodes.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchSecView();
            }
        });
        buttonShowElems.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchNodeView();
            }
        });
        buttonShowElemContours.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchElemView();
            }
        });
        buttonShowSups.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchElemContourView();
            }
        });
        buttonShowConcLoads.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchSupView();
            }
        });
        buttonShowDistLoads.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchConcLoadsView();
            }
        });
        buttonShowNodalDisps.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchDistLoadsView();
            }
        });
        buttonShowLoadsReactions.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchNodalDispsView();
            }
        });
        buttonShowReactions.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                view.switchLoadsValuesView();
                view.switchReactionValuesView();
            }
        });
        // buttonShowReactions.addActionListener(new ActionListener()
        // {
        //     @Override
        //     public void actionPerformed(ActionEvent e) 
        //     {
        //         MenuFunctions.ReactionArrowsView();
        //     }
        // });
    }
    
}
