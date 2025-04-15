package org.example.mainTCC;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.example.analysis.Analysis;
import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.Force;
import org.example.loading.NodalDisp;
import org.example.service.MenuViewService;
import org.example.structure.ElemType;
import org.example.structure.Element;
import org.example.structure.Material;
import org.example.structure.Node;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.structure.Supports;
import org.example.userInterface.MenuAnalysis;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;

public abstract class MenuFunctions
{

	private static Point mousePos;
	private static boolean AnalysisIsComplete;
	
	private static String SelectedElemType;
	private static List<Force> concLoadTypes ;
	private static double[][] DistLoadType, NodalDispType;
	private static int[][] SupType;
	private static boolean NonlinearMat;
	private static boolean NonlinearGeo;
	
	private static Point ElemSelectionWindowInitialPos;
	
	static
	{
		mousePos = new Point();
		
		SupType = Supports.Types;
		
		NonlinearMat = false;
		NonlinearGeo = false;
		
		AnalysisIsComplete = false;

	    ElemSelectionWindowInitialPos = new Point();
	}

	public static Point GetRelMousePos(int[] PanelPos)
 	{
		return new Point(MouseInfo.getPointerInfo().getLocation().x - PanelPos[0], MouseInfo.getPointerInfo().getLocation().y - PanelPos[1]) ;
 	}

	public static void updateMousePosRelToPanelPos(int[] panelPos) { mousePos = GetRelMousePos(panelPos) ;}

	public static Point getMousePos() {
		return mousePos;
	}

	public static void setMousePos(Point mousePos) {
		MenuFunctions.mousePos = mousePos;
	}

	public static boolean isAnalysisIsComplete() {
		return AnalysisIsComplete;
	}

	public static void setAnalysisIsComplete(boolean analysisIsComplete) {
		AnalysisIsComplete = analysisIsComplete;
	}

	public static String getSelectedElemType() {
		return SelectedElemType;
	}

	public static void setSelectedElemType(String selectedElemType) {
		SelectedElemType = selectedElemType;
	}

	public static List<Force> getConcLoadTypes() {
		return concLoadTypes;
	}

	public static void setConcLoadTypes(List<Force> concLoadTypes) {
		MenuFunctions.concLoadTypes = concLoadTypes;
	}

	public static double[][] getDistLoadType() {
		return DistLoadType;
	}

	public static void setDistLoadType(double[][] distLoadType) {
		DistLoadType = distLoadType;
	}

	public static double[][] getNodalDispType() {
		return NodalDispType;
	}

	public static void setNodalDispType(double[][] nodalDispType) {
		NodalDispType = nodalDispType;
	}

	public static int[][] getSupType() {
		return SupType;
	}

	public static void setSupType(int[][] supType) {
		SupType = supType;
	}

	public static boolean isNonlinearMat() {
		return NonlinearMat;
	}

	public static void setNonlinearMat(boolean nonlinearMat) {
		NonlinearMat = nonlinearMat;
	}

	public static boolean isNonlinearGeo() {
		return NonlinearGeo;
	}

	public static void setNonlinearGeo(boolean nonlinearGeo) {
		NonlinearGeo = nonlinearGeo;
	}

	public static Point getElemSelectionWindowInitialPos() {
		return ElemSelectionWindowInitialPos;
	}

	public static void setElemSelectionWindowInitialPos(Point elemSelectionWindowInitialPos) {
		ElemSelectionWindowInitialPos = elemSelectionWindowInitialPos;
	}




	

}
