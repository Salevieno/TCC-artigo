package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;

import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.Loading;
import org.example.loading.NodalDisp;
import org.example.mainTCC.Analysis;
import org.example.mainTCC.MenuFunctions;
import org.example.mainTCC.SelectionWindow;
import org.example.output.ColorSystem;
import org.example.output.Diagram;
import org.example.output.Results;
import org.example.structure.ElemShape;
import org.example.structure.ElemType;
import org.example.structure.Element;
import org.example.structure.Material;
import org.example.structure.Mesh;
import org.example.structure.Node;
import org.example.structure.Reactions;
import org.example.structure.Section;
import org.example.structure.Structure;
import org.example.structure.StructureShape;
import org.example.structure.Supports;
import org.example.userInterface.Draw;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;

import graphics.Align;
import graphics.DrawPrimitives ;

public class MainPanel extends JPanel
{
	private static final long serialVersionUID = 1L;	
	private static final Dimension initialSize = new Dimension(582, 610) ;
	private static final Color bgColor = Menus.palette[2] ;
	private static final DrawPrimitives DP = new DrawPrimitives() ;
	
	private final MyCanvas canvas ;
	private final int[] panelPos ;
	
	public static int selectedMatID;
	public static int selectedSecID;
	public static int selectedSupID;
	public static int selectedConcLoadID;
	public static int selectedDistLoadID;
	public static int selectedNodalDispID;
	
	private boolean showCanvas, showGrid, showMousePos;
	private boolean showElems, showDeformedStructure ;
	private boolean showMatColor, showSecColor, showElemContour ;
	private Diagram diagram ;
	
	public static boolean nodeSelectionIsActive ;
	public static boolean elemSelectionIsActive;
	
	public static int SelectedDiagram = -1;
	public static int SelectedVar = -1;
	
	private SelectionWindow selectionWindow ;
	private boolean showNodeSelectionWindow ;
	private boolean showElemSelectionWindow;
	
	private static boolean StructureCreationIsOn = false;
	public static List<Material> matTypes ;
	public static List<Section> secTypes ;
	
	public static Structure structure ;
	public static Loading loading ;
	
	static
	{
		matTypes = new ArrayList<>() ;
		secTypes = new ArrayList<>() ;
	}
	public MainPanel(Point frameTopLeftPos)
	{
		showCanvas = true ;
		showGrid = true ;
		showMousePos = true ;
		
		int[] screenTopLeft = new int[] {0, 0, 0} ;
		canvas = new MyCanvas(new Point(575, 25), new int[] {(int) (0.4 * initialSize.width), (int) (0.8 * initialSize.height), 0}, new double[] {10, 10, 0}, screenTopLeft);
		panelPos = new int[] {frameTopLeftPos.x + 7 * NorthPanel.stdButtonSize.width + 8, frameTopLeftPos.y + 76 + 8, 0};
		
		structure = new Structure(null, null, null);
		loading = new Loading() ;
		
		selectionWindow = new SelectionWindow() ;
		showElems = true ;
		showDeformedStructure = false ;
		showMatColor = true ;
		showSecColor = true ;
		showElemContour = true ;
		selectedMatID = -1;
		selectedSecID = -1;
		selectedSupID = -1;
		selectedConcLoadID = -1;
		selectedDistLoadID = -1;
		selectedNodalDispID = -1;

		this.setBackground(bgColor);
	    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	    this.addMouseListener(new MouseAdapter() 
	    {
			public void mousePressed(MouseEvent evt)
			{
				requestFocusInWindow();
				handleMousePress(evt) ;
			}
			public void mouseReleased(MouseEvent evt) 
			{
		    }
		}) ;
	    this.addMouseWheelListener(new MouseWheelListener()
	    {
			@Override
			public void mouseWheelMoved(MouseWheelEvent evt) 
			{
				handleMouseWheel(evt) ;
			}       	
	    }) ;
	   
		int translationSpeed = 10 ;
		double rotationSpeed = Math.PI/180.0 ;
		
		bindKey(KeyEvent.VK_Q,  () -> canvas.incAngles(- rotationSpeed, 0, 0)) ;
		bindKey(KeyEvent.VK_W,  () -> canvas.incAngles(+ rotationSpeed, 0, 0)) ;
		bindKey(KeyEvent.VK_A,  () -> canvas.incAngles(0,  + rotationSpeed, 0)) ;
		bindKey(KeyEvent.VK_S,  () -> canvas.incAngles(0,  - rotationSpeed, 0)) ;
		bindKey(KeyEvent.VK_Z,  () -> canvas.incAngles(0,  0,  + rotationSpeed)) ;
		bindKey(KeyEvent.VK_X,  () -> canvas.incAngles(0,  0,  - rotationSpeed)) ;
		
		bindKey(KeyEvent.VK_E,  () -> canvas.topView()) ;
		bindKey(KeyEvent.VK_D,  () -> canvas.frontView()) ;
		bindKey(KeyEvent.VK_C,  () -> canvas.sideView()) ;

		bindKey(KeyEvent.VK_RIGHT,  () -> canvas.incDrawingPos(+ translationSpeed, 0)) ;
		bindKey(KeyEvent.VK_LEFT,  () -> canvas.incDrawingPos(- translationSpeed, 0)) ;
		bindKey(KeyEvent.VK_UP,  () -> canvas.incDrawingPos(0, - translationSpeed)) ;
		bindKey(KeyEvent.VK_DOWN,  () -> canvas.incDrawingPos(0, + translationSpeed)) ;

		bindKey(KeyEvent.VK_ESCAPE,  () -> {
			MenuFunctions.selectedNodes = null ;
			MenuFunctions.SelectedElems = null ;
		}) ;

	    this.setSize(initialSize);
	    this.setFocusable(true);
	    this.requestFocusInWindow();
	    this.setPreferredSize(initialSize);
		
	}

	public DrawPrimitives getDP() { return DP ;}

	private void bindKey(int keyCode, Runnable action)
	{
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyCode, 0), String.valueOf(keyCode));
		
		AbstractAction abstractAction = new AbstractAction()
		{
            @Override
            public void actionPerformed(ActionEvent e)
			{
                action.run();
            }
        };

        getActionMap().put(String.valueOf(keyCode), abstractAction);
    }

	public void ElemAddition(Structure structure, MyCanvas MainCanvas, int[] MainPanelPos)
	{
		if (structure.getMesh().getElements() != null)
		{
			MenuFunctions.SelectedElems = null;
			MenuFunctions.SelectedElems = Mesh.ElemsSelection(MainCanvas, structure.getCenter().asArray(), structure.getMesh(),
			MenuFunctions.mousePos, MainPanelPos, MenuFunctions.SelectedElems, MenuFunctions.ElemSelectionWindowInitialPos, MenuFunctions.DiagramScales, showElemSelectionWindow, showDeformedStructure);
			int ElemMouseIsOn = Mesh.ElemMouseIsOn(structure.getMesh(), MenuFunctions.mousePos, structure.getCenter().asArray(), MainCanvas, showDeformedStructure);
			if (ElemMouseIsOn == -1 | (showElemSelectionWindow && -1 < ElemMouseIsOn))
			{
				showElemSelectionWindow = !showElemSelectionWindow;
				MenuFunctions.ElemSelectionWindowInitialPos = MenuFunctions.mousePos;
			}
		}
	}

	public static void StructureCreation(int[] MainPanelPos, MyCanvas canvas, Point mousePos, boolean SnipToGridIsOn)
	{		   
		if (!Util.MouseIsInside(mousePos, new int[2], canvas.getPos(), canvas.getSize()[0], canvas.getSize()[1])) { return ;}
		
		Point3D newCoord ;
		switch(structure.getShape())
		{
			case rectangular, circular:

				if (structure.getCoords() != null && !structure.getCoords().isEmpty())
				{
					StructureCreationIsOn = false;
					structure.updateCenter() ;
				}
				newCoord = MenuFunctions.getCoordFromMouseClick(canvas, mousePos, SnipToGridIsOn) ;
				structure.addCoordFromMouseClick(newCoord) ;

				return ;

			case polygonal:

				int precision = 10;
				if (structure.getCoords() != null && !structure.getCoords().isEmpty())
				{
					if (mousePos.distance(structure.getCoords().get(0).asPoint()) < precision)
					{
						StructureCreationIsOn = false;
						structure.updateCenter() ;
					}
				}
				newCoord = MenuFunctions.getCoordFromMouseClick(canvas, mousePos, SnipToGridIsOn) ;
				structure.addCoordFromMouseClick(newCoord) ;

				return ;

			default: 
				System.out.println("Structure shape not identified at Menus -> CreateStructure") ;
				return ;
		}
	}


	public void displayCanvasElements(MyCanvas canvas, boolean ShowCanvas, boolean ShowGrid, boolean ShowMousePos, DrawPrimitives DP)
	{
		canvas.setPos(new Point((int) (0.1 * initialSize.width), (int) (0.1 * initialSize.height)));
		canvas.setSize(new int[] {(int) (0.8 * initialSize.width), (int) (0.8 * initialSize.height)});
		canvas.setCenter(new int[] {canvas.getPos().x + canvas.getSize()[0] / 2, canvas.getPos().y + canvas.getSize()[1] / 2});
		
		int[] LittleAxisPos = new int[] {canvas.getPos().x + canvas.getSize()[0] + 10, canvas.getPos().y - 10, 0};
		int[] BigAxisPos = new int[] {canvas.getPos().x, canvas.getPos().y + canvas.getSize()[1], 0};		
		DrawAxis(LittleAxisPos, canvas.getSize()[0] / 15, canvas.getSize()[1] / 15, 10, canvas.getAngles(), DP);
		DrawAxis(BigAxisPos, canvas.getSize()[0] + 20, canvas.getSize()[1] + 20, 20, new double[] {0, 0, 0}, DP);
		
		Point posCanvasDimX = new Point(canvas.getPos().x + canvas.getSize()[0], canvas.getPos().y + canvas.getSize()[1] + 15) ;
		Point posCanvasDimY = new Point(canvas.getPos().x + 30, canvas.getPos().y - 10) ;
		String canvasDimX = String.valueOf(Util.Round(canvas.getDimension()[0], 3)) + " m" ;
		String canvasDimY = String.valueOf(Util.Round(canvas.getDimension()[1], 3)) + " m" ;
		DP.drawText(posCanvasDimX, Align.center, canvasDimX, Menus.palette[7]) ;
		DP.drawText(posCanvasDimY, Align.center, canvasDimY, Menus.palette[10]) ;
		if (ShowCanvas)
		{
			canvas.draw( new double[] {Util.Round(canvas.getGridSpacing()[0], 1), Util.Round(canvas.getGridSpacing()[1], 1)}, DP);
		}
		if (ShowGrid)
		{
			canvas.drawGrid(2, DP) ;
		}
		Point2D.Double RealMousePos = canvas.inRealCoords(MenuFunctions.mousePos) ; // Util.ConvertToRealCoords(MenuFunctions.mousePos, new int[] {canvas.getPos().x, canvas.getPos().y}, canvas.getSize(), canvas.getDimension());
		drawMousePosWindow(new Point(BigAxisPos[0] + canvas.getSize()[0] / 2 - 60, BigAxisPos[1] + 20), RealMousePos, Menus.palette[3], Menus.palette[0]);
	}
	
	public void DrawAxis(int[] Pos, int sizex, int sizey, int sizez, double[] CanvasAngles, DrawPrimitives DP)
	{
    	int thickness = 2;
		Draw.DrawAxisArrow3D(new int[] {Pos[0] + sizex, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1], CanvasAngles[2]}, true, sizex, sizex / 40.0, Color.red, DP);
		Draw.DrawAxisArrow3D(new int[] {Pos[0] + sizey, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1], CanvasAngles[2] - Math.PI/2.0}, true, sizey, sizey / 40.0, Color.green, DP);
		Draw.DrawAxisArrow3D(new int[] {Pos[0] + sizez, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1] - Math.PI/2.0, CanvasAngles[2]}, true, sizez, sizez / 40.0, Color.blue, DP);	// z points outward
	}
	
	private static void drawMousePosWindow(Point pos, Point2D.Double RealMousePos, Color bgcolor, Color contourcolor)
	{
		Dimension windowSize = new Dimension(200, 24) ;
		DP.drawRoundRect(pos, Align.topLeft, windowSize, bgcolor, true);
		DP.drawText(new Point(pos.x + 5, pos.y + windowSize.height / 2), Align.centerLeft, "Mouse at:", Menus.palette[0]) ;
		DP.drawText(new Point(pos.x + 85, pos.y + windowSize.height / 2), Align.centerLeft, String.valueOf(Util.Round(RealMousePos.x, 2)) + " m", Menus.palette[0]) ;
		DP.drawText(new Point(pos.x + 130, pos.y + windowSize.height / 2), Align.centerLeft, String.valueOf(Util.Round(RealMousePos.y, 2)) + " m", Menus.palette[0]) ;
	}
	
	
	private void drawStructureCreationWindow(List<Point3D> initalCoords, Point MousePos, int MemberThickness, StructureShape structshape, Color color)
	{
		Point topLeftCorner = canvas.inDrawingCoords(initalCoords.get(0));
		switch (structshape)
		{
			case rectangular:
				DP.drawRect(topLeftCorner, Align.topLeft, new Dimension(MousePos.x - topLeftCorner.x, MousePos.y - topLeftCorner.y), color, color) ;
				return ;

			case circular:
				DP.drawCircle(topLeftCorner, (int)(2*Util.dist(MousePos, topLeftCorner)), color);
				return ;

			case polygonal:				
				Point FinalPoint = canvas.inDrawingCoords(initalCoords.get(initalCoords.size() - 1)) ;
				List<Point> drawingPoints = new ArrayList<>() ;
				for (int i = 0; i <= initalCoords.size() - 1; i += 1)
				{
					drawingPoints.add(canvas.inDrawingCoords(initalCoords.get(i))) ;
				}
				DP.drawPolyLine(drawingPoints, MemberThickness, color) ;
				DP.drawLine(FinalPoint, MousePos, color) ;

				return ;
		
			default: System.out.println("Structure shape not identified at Visuals -> DrawElemAddition"); return;
		}
	}

	public void displayContent(Structure structure, int[] MainPanelPos, DrawPrimitives DP)
	{
		displayCanvasElements(canvas, showCanvas, showGrid, showMousePos, DP);
		if (structure.getCoords() != null && structure.getCenter() != null)
		{
			structure.displayShape(canvas, DP) ;
		}

		canvas.drawCenter(DP) ;
		if (StructureCreationIsOn && structure.getCoords() != null)
		{
			drawStructureCreationWindow(structure.getCoords(), MenuFunctions.mousePos, 2, structure.getShape(), Menus.palette[6]);
		}
		if (showElems && structure.getMesh() != null && structure.getMesh().getElements() != null)
		{
			if (showDeformedStructure)
			{
				canvas.setTitle("Estrutura deformada (x " + String.valueOf(Util.Round(MenuFunctions.DiagramScales[1], 3)) + ")");
			}
			structure.displayMesh(canvas, MenuFunctions.DiagramScales[1], showMatColor, showSecColor, showElemContour, showDeformedStructure, DP) ;
		}
		if (MenuFunctions.ShowNodes && structure.getMesh() != null && structure.getMesh().getNodes() != null)
		{
			// DP.DrawNodes3D(structure.getMesh().getNodes(), MenuFunctions.selectedNodes, Node.color, showDeformedStructure,
			// structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.DiagramScales[1], canvas);
		}
		if (MenuFunctions.AnalysisIsComplete)
		{
			// DrawResults(canvas, structure, MenuFunctions.SelectedElems, SelectedVar,
			// MenuFunctions.ShowElemContour, showDeformedStructure,
			// MenuFunctions.DiagramScales, ShowDisplacementContour, ShowStressContour, ShowStrainContour, ShowInternalForces,
			// MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo);

			// Results.displayContours(canvas, structure, MenuFunctions.SelectedElems, SelectedVar,
			// 						MenuFunctions.ShowElemContour, showDeformedStructure,
			// 						MenuFunctions.DiagramScales, ShowDisplacementContour, ShowStressContour, ShowStrainContour, ShowInternalForces,
			// 						MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, DP);
			structure.displayDiagrams(canvas, diagram, SelectedVar, DP) ;
			
			if (MenuFunctions.ShowReactionArrows && structure.getReactions() != null)
			{
				// DP.DrawReactions3D(structure.getMesh().getNodes(), structure.getReactions(),
				// 	structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.ShowReactionValues,
				// 	Reactions.color, MenuFunctions.ShowDeformedStructure, MenuFunctions.DiagramScales[1], canvas, DPP);

				Reactions.display3D(structure.getMesh().getNodes(), structure.getReactions(),
						structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.ShowReactionValues,
						Reactions.color, MenuFunctions.ShowDeformedStructure, MenuFunctions.DiagramScales[1], canvas, DP);
			}
		}
		if (MenuFunctions.ShowSup && structure.getSupports() != null)
		{
			structure.displaySupports(canvas, DP) ;
		}
		if (MenuFunctions.ShowConcLoads && loading != null && loading.getConcLoads() != null)
		{
			structure.displayConcLoads(canvas, showDeformedStructure, DP) ;
		}
		if (MenuFunctions.ShowDistLoads && loading != null && loading.getDistLoads() != null)
		{
			Draw.DrawDistLoads3D(structure.getMesh(), loading.getDistLoads(), MenuFunctions.ShowLoadsValues, DistLoad.color,
				showDeformedStructure, structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.DiagramScales[1], canvas, DP);
		}
		if (MenuFunctions.ShowNodalDisps && loading != null && loading.getNodalDisps() != null)
		{
			Draw.DrawNodalDisps3D(structure.getMesh().getNodes(), loading.getNodalDisps(), structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.ShowLoadsValues,
			NodalDisp.color, showDeformedStructure, MenuFunctions.DiagramScales[1]);
		}
		if (MenuFunctions.ShowDOFNumber && structure.getMesh() != null && structure.getMesh().getNodes() != null)
		{
			Draw.DrawDOFNumbers(structure.getMesh().getNodes(), Node.color, showDeformedStructure, canvas, DP);
		}
		if (MenuFunctions.ShowNodeNumber && structure.getMesh() != null && structure.getMesh().getNodes() != null)
		{
			// DP.DrawNodeNumbers(structure.getMesh().getNodes(), Node.color, showDeformedStructure, canvas);
			structure.getMesh().displayNodeNumbers(structure.getMesh().getNodes(), Node.color, showDeformedStructure, canvas, DP) ;
		}
		if (MenuFunctions.ShowElemNumber && structure.getMesh() != null &&  structure.getMesh().getElements() != null)
		{
			// DP.DrawElemNumbers(structure.getMesh(), Node.color, showDeformedStructure, canvas);
			structure.getMesh().displayElemNumbers(structure.getMesh(), Node.color, showDeformedStructure, canvas, DP) ;
		}
		// if (showNodeSelectionWindow)
		// {
		// 	DP.DrawSelectionWindow(new Point(nodeSelectionWindowInitialPos[0], nodeSelectionWindowInitialPos[1]), MenuFunctions.mousePos);
		// }
		if (showElemSelectionWindow)
		{
			DrawSelectionWindow(MenuFunctions.ElemSelectionWindowInitialPos, MenuFunctions.mousePos, DP);
		}
		if (MenuFunctions.ShowElemDetails && MenuFunctions.SelectedElemType != null)
		{
			Mesh.DrawElemDetails(ElemType.valueOf(MenuFunctions.SelectedElemType.toUpperCase()), canvas, DP);
		}

		if (selectionWindow != null && selectionWindow.isActive())
		{
			selectionWindow.display(MenuFunctions.mousePos, DP) ;
		}

	}

	public void DrawSelectionWindow(Point InitPos, Point FinalPos, DrawPrimitives DP)
	{
		Dimension size = new Dimension(FinalPos.x - InitPos.x, FinalPos.y - InitPos.y) ;
		if (InitPos.x <= FinalPos.x && InitPos.y <= FinalPos.y)
		{
			DP.drawRect(InitPos, Align.topLeft, size, Menus.palette[0], null) ;
		}
	}

	public void switchDisplay(int diagramID, int selectedVar)
	{
		if (diagramID <= -1 || selectedVar <= -1) { return ;}
		
		SelectedVar = selectedVar ;
		diagram = Diagram.values()[diagramID] ;
	}


	// Results

	// public static void DrawResults(MyCanvas canvas, Structure structure, int[] SelectedElems, int selectedvar,
	// 		boolean ShowElemContour, boolean ShowDeformedStructure,
	// 		double[] DiagramsScales, boolean ShowDisplacementContour, boolean ShowStressContour,
	// 		boolean ShowStrainContour, boolean ShowInternalForces,
	// 		boolean NonlinearMat, boolean NonlinearGeo)
	// {
	// 	List<Node> nodes = structure.getMesh().getNodes();
	// 	List<Element> elems = structure.getMesh().getElements();
	// 	if (-1 < selectedvar && nodes != null && elems != null)
	// 	{
	// 		if (ShowDisplacementContour)
	// 		{
	// 			canvas.setTitle("Deslocamentos (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
	// 			DrawContours3D(elems, nodes, SelectedElems, ShowElemContour, ShowDeformedStructure, DiagramsScales[1],
	// 			structure.getResults().getDispMin()[selectedvar], structure.getResults().getDispMax()[selectedvar], "Displacement", selectedvar,
	// 			NonlinearMat, NonlinearGeo, ColorSystem.redToGreen, canvas, DP);
	// 		}
	// 		else if (ShowStressContour)
	// 		{
	// 			canvas.setTitle("Tensâes (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
	// 			DrawContours3D(elems, nodes, SelectedElems, ShowElemContour, ShowDeformedStructure, DiagramsScales[1],
	// 			structure.getResults().getStressMin()[selectedvar], structure.getResults().getStressMax()[selectedvar], "Stress", selectedvar, 
	// 			NonlinearMat, NonlinearGeo, ColorSystem.redToGreen, canvas, DP);
	// 		}
	// 		else if (ShowStrainContour)
	// 		{
	// 			canvas.setTitle("Deformaçõs (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
	// 			DrawContours3D(elems, nodes, SelectedElems, ShowElemContour, ShowDeformedStructure, DiagramsScales[1],
	// 			structure.getResults().getStrainMin()[selectedvar], structure.getResults().getStrainMax()[selectedvar], "Strain", selectedvar,
	// 			NonlinearMat, NonlinearGeo, ColorSystem.redToGreen, canvas, DP);
	// 		}
	// 		else if (ShowInternalForces)
	// 		{
	// 			canvas.setTitle("Forâas internas (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
	// 			DrawContours3D(elems, nodes, SelectedElems, ShowElemContour, ShowDeformedStructure, DiagramsScales[1],
	// 			structure.getResults().getInternalForcesMin()[selectedvar], structure.getResults().getInternalForcesMax()[selectedvar], "Force", selectedvar,
	// 			NonlinearMat, NonlinearGeo, ColorSystem.redToGreen, canvas, DP);
	// 		}
	// 	}
	// }


	public static void DrawContours3D(List<Element> Elem, List<Node> nodes, int[] SelectedElems, boolean showelemcontour, boolean condition,
			double Defscale, double minvalue, double maxvalue, String ResultType, int selecteddof, boolean NonlinearMat, boolean NonlinearGeo, ColorSystem colorSystem,
			MyCanvas canvas, DrawPrimitives DP)
	{
		int Ninterpoints = 0;
		for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
		{
			/* Get edge nodes and coordinates*/
			int[] EdgeNodes = Elem.get(elem).getExternalNodes();
			double[][] EdgeCoords = new double[EdgeNodes.length][3];
			for (int node = 0; node <= EdgeNodes.length - 1; node += 1)
			{
				if (condition)
				{
					EdgeCoords[node] = Util.ScaledDefCoords(nodes.get(EdgeNodes[node]).getOriginalCoords(), nodes.get(EdgeNodes[node]).getDisp(), nodes.get(node).getDOFType(), Defscale);
				}
				else
				{
					EdgeCoords[node] = Util.GetNodePos(nodes.get(EdgeNodes[node]), condition);
				}
			}
			
			/* Get contour coordinates */
			double[][] ContourCoords = new double[EdgeNodes.length * (1 + Ninterpoints)][3];
			for (int node = 0; node <= EdgeNodes.length - 2; node += 1)
			{
				double[] Line = new double[] {EdgeCoords[node][0], EdgeCoords[node][1], EdgeCoords[node][2], EdgeCoords[node + 1][0], EdgeCoords[node + 1][1], EdgeCoords[node + 1][2]};
				for (int i = 0; i <= Ninterpoints; i += 1)
				{
					double offset = i / (double)(Ninterpoints + 1);
					double[] NewCoord = Util.CreatePointInLine(Line, offset);
					ContourCoords[node * (Ninterpoints + 1) + i] = NewCoord;
				}
			}			
			double[] Line = new double[] {EdgeCoords[EdgeNodes.length - 1][0], EdgeCoords[EdgeNodes.length - 1][1], EdgeCoords[EdgeNodes.length - 1][2], EdgeCoords[0][0], EdgeCoords[0][1], EdgeCoords[0][2]};
			for (int i = 0; i <= Ninterpoints; i += 1)
			{
				double offset = i / (double)(Ninterpoints + 1);
				double[] NewCoord = Util.CreatePointInLine(Line, offset);
				ContourCoords[(EdgeNodes.length - 1) * (Ninterpoints + 1) + i] = NewCoord;
			}

			/* Get displacements on contour */
			double[] ContourValue = new double[ContourCoords.length];
			if (Elem.get(elem).getShape().equals(ElemShape.rectangular) | Elem.get(elem).getShape().equals(ElemShape.r8))
			{
				double L = 2 * Elem.get(elem).calcHalfSize(nodes)[0];
				double H = 2 * Elem.get(elem).calcHalfSize(nodes)[1];
				double[] CenterCoords = Elem.get(elem).getCenterCoords();
				for (int point = 0; point <= ContourCoords.length - 1; point += 1)
				{
					double[] natCoords = Util.InNaturalCoordsRect(CenterCoords, L, H, ContourCoords[point]);
					double e = natCoords[0];
					double n = natCoords[1];
					
					if (-1 < selecteddof)
					{
						if (ResultType.equals("Displacement"))
						{
							double[] disp = Elem.get(elem).getDisp();
							ContourValue[point] = Analysis.DispOnPoint(nodes, Elem.get(elem), e, n, selecteddof, disp);
						}
						else if (ResultType.equals("Strain"))
						{
							double[] strain = Elem.get(elem).getStrain();
							ContourValue[point] = Analysis.StrainOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, strain);
						}
						else if (ResultType.equals("Stress"))
						{
							double[] stress = Elem.get(elem).getStress();
							ContourValue[point] = Analysis.StressOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, stress);
						}
						else if (ResultType.equals("Force"))
						{
							double[] force = Elem.get(elem).getIntForces();
							ContourValue[point] = Analysis.ForceOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, force);
						}
					}
					ContourCoords[point][2] = ContourValue[point] * Defscale;
				}
			}
			else if (Elem.get(elem).getShape().equals(ElemShape.triangular))
			{			
				for (int point = 0; point <= ContourCoords.length - 1; point += 1)
				{
					double[] natCoords = Util.InNaturalCoordsTriangle(EdgeCoords, ContourCoords[point]);
					double[] u = Elem.get(elem).getDisp();
					ContourValue[point] = Analysis.DispOnPoint(nodes, Elem.get(elem), natCoords[0], natCoords[1], selecteddof, u);
				}
			}

			/* Draw the contour */
			int[][] DrawingCoords = new int[ContourCoords.length][3];
			int[] xCoords = new int[ContourCoords.length], yCoords = new int[ContourCoords.length];
			double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			Color[] colors = new Color[ContourCoords.length];
			Arrays.fill(colors, new Color(0, 100, 55));
			
			for (int point = 0; point <= ContourCoords.length - 1; point += 1)
			{
				DrawingCoords[point] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(ContourCoords[point], Center, canvas.getAngles()), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
				xCoords[point] = DrawingCoords[point][0];
				yCoords[point] = DrawingCoords[point][1];
				colors[point] = Util.FindColor(ContourValue[point], minvalue, maxvalue, colorSystem);
			}
			
			// DP.DrawGradPolygon(xCoords, yCoords, thick, false, true, Color.black, colors);
			double equivalentDiameter = (Util.FindMax(xCoords) - Util.FindMin(xCoords) + Util.FindMax(yCoords) - Util.FindMin(yCoords)) / 2.0 ;
			Color avrColor = Util.AverageColor(colors) ;
			DP.drawGradPolygon(xCoords, yCoords, equivalentDiameter, avrColor, colors) ;
			if (showelemcontour)
			{
				// DrawPolygon(xCoords, yCoords, thick, true, false, Color.black, null);
				DP.drawPolygon(xCoords, yCoords, Menus.palette[0]) ;
			}
			if (SelectedElems != null)
			{
				for (int i = 0; i <= SelectedElems.length - 1; i += 1)
				{
					if (elem == SelectedElems[i])
					{
						// DrawPolygon(xCoords, yCoords, thick, false, true, Color.black, Color.red);
						DP.drawPolygon(xCoords, yCoords, Menus.palette[4]) ;
					}
				}
			}		
		}
		
	}

	

	public void NodeAddition(int[] MainPanelPos)
	{
		if (MainPanel.structure.getMesh().getNodes() == null) { return ;}
		
		if (!selectionWindow.isActive())
		{
			selectionWindow.setTopLeft(MenuFunctions.mousePos);
			return ;
		}

		MenuFunctions.selectedNodes = selectionWindow.selectNodes(MainPanel.structure.getMesh().getNodes(), canvas, MenuFunctions.mousePos) ;
		// int NodeMouseIsOn = Mesh.NodeMouseIsOn(MenuFunctions.struct.getMesh().getNodes(), MenuFunctions.mousePos, canvas, 4, MenuFunctions.ShowDeformedStructure);
		showNodeSelectionWindow = !showNodeSelectionWindow;
		
		
	}

	public static void CreateStructureOnClick(StructureShape structureShape)
	{
		MainPanel.structure.setShape(structureShape);
		StructureCreationIsOn = !StructureCreationIsOn;
	}



	public void activateMaterialAssignment()
	{
		MainPanel.elemSelectionIsActive = !MainPanel.elemSelectionIsActive;
		MainPanel.selectedMatID = 0;
		Menus.getInstance().getNorthPanel().getUpperToolbar().enableMaterialAssignment() ;	
		Menus.getInstance().getNorthPanel().getUpperToolbar().assignToElemView() ;
	}
	
	public void activateSectionAssignment()
	{
		MainPanel.elemSelectionIsActive = !MainPanel.elemSelectionIsActive;
		MainPanel.selectedSecID = 0;
		Menus.getInstance().getNorthPanel().getUpperToolbar().enableSectionAssignment() ;
		Menus.getInstance().getNorthPanel().getUpperToolbar().assignToElemView() ;
	}
	
	public void activateSupportAssignment()
	{
		MainPanel.nodeSelectionIsActive = !MainPanel.nodeSelectionIsActive;
		MainPanel.selectedSupID = 0;
		Menus.getInstance().getNorthPanel().getUpperToolbar().enableSupportAssignment() ;
		Menus.getInstance().getNorthPanel().getUpperToolbar().assignToNodeView() ;
	}
	
	public void activateConcLoadAssignment()
	{
		MainPanel.nodeSelectionIsActive = !MainPanel.nodeSelectionIsActive;
		MainPanel.selectedConcLoadID = 0;
		Menus.getInstance().getNorthPanel().getUpperToolbar().enableConcLoadAssignment() ;
		Menus.getInstance().getNorthPanel().getUpperToolbar().assignToNodeView() ;
	}
	
	public void activateDistLoadAssignment()
	{
		MainPanel.elemSelectionIsActive = !MainPanel.elemSelectionIsActive;
		MainPanel.selectedDistLoadID = 0;
		Menus.getInstance().getNorthPanel().getUpperToolbar().enableDistLoadAssignment() ;
		Menus.getInstance().getNorthPanel().getUpperToolbar().assignToElemView() ;
	}
	
	public void activateNodalDispAssignment()
	{
		Menus.getInstance().getNorthPanel().getUpperToolbar().enableNodalDispAssignment() ;
		MainPanel.nodeSelectionIsActive = !MainPanel.nodeSelectionIsActive;
		MainPanel.selectedNodalDispID = 0;
		Menus.getInstance().getNorthPanel().getUpperToolbar().assignToNodeView() ;
	}

	public static void setElemType(String ElemType)
	{
		MenuFunctions.SelectedElemType = ElemType;
	}


	
 	public static void AddMaterialToElements(int[] Elems, Material mat)
	{
		if (MainPanel.structure.getMesh().getElements() != null && Elems != null && mat != null)
		{
			for (int i = 0; i <= Elems.length - 1; i += 1)
			{
				if (-1 < Elems[i])
				{
					MainPanel.structure.getMesh().getElements().get(Elems[i]).setMat(mat);
				}
			}
			MenuFunctions.SelectedElems = null;
		}
	}

	public static void AddSectionsToElements(int[] Elems, Section sec)
	{
		if (MainPanel.structure.getMesh().getElements() != null && sec != null && Elems != null)
		{
			for (int i = 0; i <= Elems.length - 1; i += 1)
			{
				if (-1 < Elems[i])
				{
					MainPanel.structure.getMesh().getElements().get(Elems[i]).setSec(sec);
				}
			}
			MenuFunctions.SelectedElems = null;
		}
	}
	
	public static void AddSupports()
	{
		if (-1 < selectedSupID && MenuFunctions.selectedNodes != null && MenuFunctions.SupType != null )
		{
			for (int i = 0; i <= MenuFunctions.selectedNodes.size() - 1; i += 1)
			{
				if (-1 < MenuFunctions.selectedNodes.get(i).getID())
				{
					// int supid = MainPanel.structure.getSupports().size() - MenuFunctions.selectedNodes.size() + i;
					Supports newSupport = new Supports(1, MenuFunctions.selectedNodes.get(i), MenuFunctions.SupType[selectedSupID]);
					MainPanel.structure.addSupport(newSupport) ;
					MenuFunctions.selectedNodes.get(i).setSup(MenuFunctions.SupType[selectedSupID]);
				}
			}
			MenuFunctions.ShowSup = true;
			MenuFunctions.selectedNodes = null;
		}
	}
	
	public static void AddConcLoads(Loading loading, List<Node> selectedNodes, double[][] ConcLoadType)
	{
		if (-1 < selectedConcLoadID && selectedNodes != null && ConcLoadType != null && 1 <= ConcLoadType.length)
		{
			for (int i = 0; i <= selectedNodes.size() - 1; i += 1)
			{
				int loadid = loading.getConcLoads().size() - selectedNodes.size() + i;
				if (-1 < selectedNodes.get(i).getID())
				{
					ConcLoad newConcLoad = new ConcLoad(loadid, selectedNodes.get(i), ConcLoadType[selectedConcLoadID]);
					loading.getConcLoads().add(newConcLoad);
					selectedNodes.get(i).addConcLoad(newConcLoad);
				}
			}
			MenuFunctions.ShowConcLoads = true;
			selectedNodes = null;
		}
	}
	
	public static void AddDistLoads(Structure structure, Loading loading, List<Element> selectedElems, double[][] DistLoadType)
	{
		if (-1 < selectedDistLoadID && selectedElems != null && DistLoadType != null)
		{
			for (int i = 0; i <= selectedElems.size() - 1; i += 1)
			{
				int loadid = loading.getDistLoads().size() - selectedElems.size() + i;
				Element elem = selectedElems.get(i);
				int LoadType = (int) DistLoadType[selectedDistLoadID][0];
				double Intensity = DistLoadType[selectedDistLoadID][1];
				DistLoad newDistLoad = new DistLoad(loadid, selectedElems.get(i).getID(), LoadType, Intensity) ;
				loading.getDistLoads().add(newDistLoad);
				elem.setDistLoads(Util.AddElem(elem.getDistLoads(), newDistLoad));
				
			}
			MenuFunctions.ShowDistLoads = true;
			selectedElems = null;
		}
	}
	
	public static void AddNodalDisps()
	{
		if (-1 < selectedNodalDispID && MenuFunctions.selectedNodes != null && MenuFunctions.NodalDispType != null)
		{
			for (int i = 0; i <= MenuFunctions.selectedNodes.size() - 1; i += 1)
			{
				int dispid = loading.getNodalDisps().size() - MenuFunctions.selectedNodes.size() + i;
				if (-1 < MenuFunctions.selectedNodes.get(i).getID())
				{
					NodalDisp newNodalDisp = new NodalDisp(dispid, MenuFunctions.selectedNodes.get(i), MenuFunctions.NodalDispType[selectedNodalDispID]) ;
					loading.getNodalDisps().add(newNodalDisp);
					MenuFunctions.selectedNodes.get(i).addNodalDisp(newNodalDisp);
				}
			}
			MenuFunctions.ShowNodalDisps = true;
			MenuFunctions.selectedNodes = null;
		}
	}
	
	public static void addMaterials(List<Material> newMaterials)
	{
		matTypes.addAll(newMaterials) ;
	}

	public static void addMaterial(Material newMaterial)
	{
		matTypes.add(newMaterial) ;
	}

	public static void addSections(List<Section> newSections)
	{
		secTypes.addAll(newSections);
	}

	public static void addSection(Section newSection)
	{
		secTypes.add(newSection);
	}

	public static void DefineConcLoadTypes(double[][] ConcLoads)
	{
		MenuFunctions.ConcLoadType = ConcLoads;
	}

	public static void DefineDistLoadTypes(double[][] DistLoads)
	{
		MenuFunctions.DistLoadType = DistLoads;
	}

	public static void DefineNodalDispTypes(double[][] NodalDisps)
	{
		MenuFunctions.NodalDispType = NodalDisps;
	}

	public void resetDisplay()
	{
		showElemSelectionWindow = false;
		diagram = null ;
	}


	private void handleMousePress(MouseEvent evt)
	{
		if (evt.getButton() == 1)	// Left click
		{
			if (StructureCreationIsOn)
			{
				StructureCreation(panelPos, canvas, MenuFunctions.mousePos, MenuFunctions.SnipToGridIsOn);
				Menus.getInstance().EnableButtons();
				Menus.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion() ;
			}
			if (!StructureCreationIsOn)
			{
				Menus.getInstance().getWestPanel().getInstructionsPanel().updateSteps(MainPanel.structure, MainPanel.loading) ;
				Menus.getInstance().getNorthPanel().getUpperToolbar().disableButtonsSnipToGrid() ;
			}
			if (nodeSelectionIsActive)
			{
				NodeAddition(panelPos);
				if (MenuFunctions.selectedNodes != null)
				{
					if (!MenuFunctions.selectedNodes.isEmpty() && -1 < MenuFunctions.selectedNodes.get(0).getID())
					{
						Menus.getInstance().getEastPanel().reset() ;
						//AddNodeInfoPanel(MenuFunctions.Node[MenuFunctions.SelectedNodes[0]]);
					}
				}
			}
			if (elemSelectionIsActive)
			{
				ElemAddition(MainPanel.structure, canvas, panelPos);
				if (MenuFunctions.SelectedElems != null)
				{
					if (-1 < MenuFunctions.SelectedElems[0])
					{
						Menus.getInstance().getEastPanel().reset() ;
						//AddElemInfoPanel(MenuFunctions.Elem[MenuFunctions.SelectedElems[0]]);
					}
				}
			}
		}

		if (evt.getButton() == 3)	// Right click
		{
			MainPanel.structure.printStructure(matTypes, secTypes, MainPanel.structure.getSupports(), loading);
			MenuFunctions.ElemDetailsView();
		}
	}

	private void handleMouseWheel(MouseWheelEvent evt)
	{
		Assignable assignable = Menus.getInstance().getNorthPanel().getUpperToolbar().getAssignable() ;
		double qtdRotation = evt.getWheelRotation() ;

		boolean MouseIsInMainCanvas = Util.MouseIsInside(MenuFunctions.mousePos, panelPos, canvas.getPos(), canvas.getSize()[0], canvas.getSize()[1]);
		if (Util.MouseIsInside(MenuFunctions.mousePos, panelPos, canvas.getPos(), canvas.getSize()[0], canvas.getSize()[1]))
		{
			canvas.getDimension()[0] += Util.Round(0.2*Math.log10(canvas.getDimension()[0])*qtdRotation, 1);
			canvas.getDimension()[1] += Util.Round(0.2*Math.log10(canvas.getDimension()[1])*qtdRotation, 1);
		}
		if (!MouseIsInMainCanvas)
		{
			switch (assignable)
			{
				case materials:
					selectedMatID += qtdRotation;
					selectedMatID = Util.clamp(selectedMatID, 0, matTypes.size() - 1) ;
					
					break;

				case sections:
					selectedSecID += qtdRotation;
					selectedSecID = Util.clamp(selectedSecID, 0, secTypes.size() - 1) ;
					
					break;

				case supports:
					selectedSupID += qtdRotation;
					selectedSupID = Util.clamp(selectedSupID, 0, MenuFunctions.SupType.length - 1) ;
					
					break;

				case concLoads:
					selectedConcLoadID += qtdRotation;
					selectedConcLoadID = Util.clamp(selectedConcLoadID, 0, MenuFunctions.ConcLoadType.length - 1) ;
					
					break;

				case distLoads:
					selectedDistLoadID += qtdRotation;
					selectedDistLoadID = Util.clamp(selectedDistLoadID, 0, MenuFunctions.DistLoadType.length - 1) ;
					
					break;

				case nodalDisps:
					selectedNodalDispID += qtdRotation;
					selectedNodalDispID = Util.clamp(selectedNodalDispID, 0, MenuFunctions.NodalDispType.length - 1) ;
					
					break;
			
				default: System.out.println("Warn: No assignable selected when moving mouse wheel") ; break ;
			}
		}	

		MenuFunctions.updateDiagramScale(canvas, evt.getWheelRotation());
	}

	@Override
    public void paintComponent(Graphics graphs) 
    {
        super.paintComponent(graphs);
        MenuFunctions.updateMousePosRelToPanelPos(panelPos) ;
		DP.setGraphics((Graphics2D) graphs) ;
		this.displayContent(MainPanel.structure, panelPos, DP);
		repaint();
    }

}
