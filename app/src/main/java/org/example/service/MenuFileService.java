package org.example.service;

import java.io.FileWriter;
import java.io.IOException;

import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.Loading;
import org.example.mainTCC.InputFunctions;
import org.example.mainTCC.MainPanel;
import org.example.structure.Structure;
import org.example.structure.StructureDTO;
import org.example.userInterface.MenuAnalysis;
import org.example.userInterface.MenuBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MenuFileService
{

	public static void loadStructure()
	{
		
		StructureDTO input = (StructureDTO) InputFunctions.loadFromJson("viga", StructureDTO.class) ;
		Structure structure = new Structure(input) ;
		// TODO fazer o parse dos materiais numa lista específica e depois atribuir aos elementos. Evita criar 100 materiais idênticos
		structure.updateMaxCoords() ;
		// structure.getMesh().getNodes().forEach(node -> node.updateDrawingPos(MainPanel.getInstance().getMainCanvas(), false, 1)) ;
		MainPanel.getInstance().getCentralPanel().setStructure(structure) ;
		
		Loading loading = new Loading() ;
		structure.getMesh().getNodes().stream().filter(node -> node.getConcLoads() != null).forEach(node -> node.getConcLoads().forEach(load -> loading.addConcLoad(load))) ;
		MainPanel.getInstance().getCentralPanel().setLoading(loading) ;

		MenuAnalysis.CalcAnalysisParameters(structure, loading, ConcLoad.getTypes(), DistLoad.getTypes()) ;

		MenuViewService.getInstance().switchSupView() ;
		MenuViewService.getInstance().switchConcLoadsView() ;
		MenuViewService.getInstance().switchDistLoadsView() ;
		MenuViewService.getInstance().switchNodalDispsView() ;

		// Menus.getInstance().getMainCanvas().setDimension(new Point2D.Double(1.2 * MainPanel.structure.getMaxCoords().x, 1.2 * MainPanel.structure.getMaxCoords().y)) ;

		MainPanel.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion(structure, MainPanel.getInstance().getCentralPanel().getLoading()) ;
        MenuBar.getInstance().getMenuAnalysis().updateIsReadyForAnalysis(MainPanel.getInstance().getCentralPanel().getStructure(), MainPanel.getInstance().getCentralPanel().getLoading()) ;
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