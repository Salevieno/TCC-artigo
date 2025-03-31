package org.example.service;

import java.awt.geom.Point2D;
import java.io.FileWriter;
import java.io.IOException;

import org.example.mainTCC.MenuFunctions;
import org.example.structure.StructureDTO;
import org.example.userInterface.Menus;
import org.example.view.MainPanel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MenuFileService
{

	public static void loadStructure()
	{
		
		// MainPanel.structure = new Structure(null, null, null);
		MainPanel.loading.clearLoads() ;		
		Menus.getInstance().getMainPanel().resetDisplay() ;
		MenuFunctions.resetDisplay();
		
		String filename = Menus.getInstance().getSaveLoadFile().run().getText();
		MainPanel.structure = MenuFunctions.LoadFile("", filename);


		if (MainPanel.structure == null) { System.out.println("Error: Structure is null after loading") ; return ;}

		MainPanel.structure.updateMaxCoords() ;
		Menus.getInstance().getMainCanvas().setDimension(new Point2D.Double(1.2 * MainPanel.structure.getMaxCoords().x, 1.2 * MainPanel.structure.getMaxCoords().y)) ;
		Menus.getInstance().getMenuAnalysis().setRunAnalysis(MenuFunctions.CheckIfAnalysisIsReady(MainPanel.structure, MainPanel.loading));
		// Menus.getInstance().showCanvasOn() ;
		// Menus.getInstance().showGrid() ;
		// Menus.getInstance().showMousePos() ;
		MenuFunctions.NodeView();
		MenuFunctions.ElemView();
		MenuFunctions.ElemContourView();
		MenuFunctions.SupView();
		MenuFunctions.ConcLoadsView();
		MenuFunctions.DistLoadsView();
		MenuFunctions.NodalDispsView();
		Menus.getInstance().getWestPanel().getInstructionsPanel().updateSteps(MainPanel.structure, MainPanel.loading) ;
		Menus.getInstance().DisableButtons();
		Menus.getInstance().EnableButtons();
		Menus.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion() ;
	}

	public static void saveStructure(String filename, StructureDTO structureDTO)
	{
        
        try (FileWriter writer = new FileWriter(filename + ".json"))
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(java.lang.reflect.Modifier.STATIC, java.lang.reflect.Modifier.TRANSIENT).create();
            gson.toJson(structureDTO, writer);
            System.out.println("\nStructure saved!") ;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}