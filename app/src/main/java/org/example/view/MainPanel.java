package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
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
import org.example.mainTCC.MenuFunctions;
import org.example.mainTCC.SelectionWindow;
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
import org.example.userInterface.DrawingOnAPanel;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;
// import graphics.DrawPrimitives ;

public class MainPanel extends JPanel
{
	private static final long serialVersionUID = 1L;	
	private static final Dimension initialSize = new Dimension(582, 610) ;
	private static final Color bgColor = Menus.palette[2] ;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;
	// private static final DrawPrimitives DPP = new DrawPrimitives() ;
	
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
	private static boolean ShowDisplacementContour, ShowStressContour, ShowStrainContour, ShowInternalForces;
	
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


	public void displayCanvasElements(MyCanvas canvas, boolean ShowCanvas, boolean ShowGrid, boolean ShowMousePos)
	{
		canvas.setPos(new Point((int) (0.1 * initialSize.width), (int) (0.1 * initialSize.height)));
		canvas.setSize(new int[] {(int) (0.8 * initialSize.width), (int) (0.8 * initialSize.height)});
		canvas.setCenter(new int[] {canvas.getPos().x + canvas.getSize()[0] / 2, canvas.getPos().y + canvas.getSize()[1] / 2});
		
		int[] LittleAxisPos = new int[] {canvas.getPos().x + canvas.getSize()[0] + 10, canvas.getPos().y - 10, 0};
		int[] BigAxisPos = new int[] {canvas.getPos().x, canvas.getPos().y + canvas.getSize()[1], 0};		
		DrawAxis(LittleAxisPos, canvas.getSize()[0] / 15, canvas.getSize()[1] / 15, 10, canvas.getAngles(), DP);
		DrawAxis(BigAxisPos, canvas.getSize()[0] + 20, canvas.getSize()[1] + 20, 20, new double[] {0, 0, 0}, DP);
		
		DP.DrawText(new int[] {canvas.getPos().x + canvas.getSize()[0], canvas.getPos().y + canvas.getSize()[1] + 15, 0}, String.valueOf(Util.Round(canvas.getDimension()[0], 3)) + " m", "Center", 0, "Bold", 13, Menus.palette[7]);
		DP.DrawText(new int[] {canvas.getPos().x + 30, canvas.getPos().y - 10, 0}, String.valueOf(Util.Round(canvas.getDimension()[1], 3)) + " m", "Center", 0, "Bold", 13, Menus.palette[10]);
		if (ShowCanvas)
		{
			canvas.draw( new double[] {Util.Round(canvas.getGridSpacing()[0], 1), Util.Round(canvas.getGridSpacing()[1], 1)}, DP);
		}
		if (ShowGrid)
		{
			canvas.drawGrid(2, DP) ;
		}
		Point2D.Double RealMousePos = canvas.inRealCoords(MenuFunctions.mousePos) ; // Util.ConvertToRealCoords(MenuFunctions.mousePos, new int[] {canvas.getPos().x, canvas.getPos().y}, canvas.getSize(), canvas.getDimension());
		DrawMousePos(new int[] {BigAxisPos[0] + canvas.getSize()[0] / 2 - 60, BigAxisPos[1] + 40}, RealMousePos, Menus.palette[3], Menus.palette[0], DP);
	}
	
	public void DrawAxis(int[] Pos, int sizex, int sizey, int sizez, double[] CanvasAngles, DrawingOnAPanel DP)
	{
    	int thickness = 2;
		DP.DrawAxisArrow3D(new int[] {Pos[0] + sizex, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1], CanvasAngles[2]}, true, sizex, sizex / 40.0, Color.red);
		DP.DrawAxisArrow3D(new int[] {Pos[0] + sizey, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1], CanvasAngles[2] - Math.PI/2.0}, true, sizey, sizey / 40.0, Color.green);
		DP.DrawAxisArrow3D(new int[] {Pos[0] + sizez, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1] - Math.PI/2.0, CanvasAngles[2]}, true, sizez, sizez / 40.0, Color.blue);	// z points outward
	}
	
	private static void DrawMousePos(int[] Pos, Point2D.Double RealMousePos, Color bgcolor, Color contourcolor, DrawingOnAPanel DP)
	{
		int[] RectSize = new int[] {40, 18};
		int RectThick = 1;
		int FontSize = 13;
		DP.DrawRoundRect(new int[] {Pos[0] - 20, Pos[1] - RectSize[1] / 2 - 8}, "Left", 200, 24, 1, 5, 5, new Color[] {bgcolor}, contourcolor, true);
		DP.DrawText(new int[] {Pos[0], Pos[1]}, "Mouse Pos:", "Left", 0, "Bold", FontSize, Color.black);
		DP.DrawRect(new int[] {Pos[0] + 80, Pos[1] - RectSize[1] + FontSize / 3}, RectSize[0], RectSize[1], RectThick, "Left", 0, false, Color.black, null);
		DP.DrawText(new int[] {Pos[0] + 85, Pos[1]}, String.valueOf(Util.Round(RealMousePos.x, 2)), "Left", 0, "Bold", FontSize, Color.black);
		DP.DrawRect(new int[] {Pos[0] + 125, Pos[1] - RectSize[1] + FontSize / 3}, RectSize[0], RectSize[1], RectThick, "Left", 0, false, Color.black, null);
		DP.DrawText(new int[] {Pos[0] + 130, Pos[1]}, String.valueOf(Util.Round(RealMousePos.y, 2)), "Left", 0, "Bold", FontSize, Color.black);
	}
	
	
	private void drawStructureCreationWindow(List<Point3D> initalCoords, Point MousePos, int MemberThickness, StructureShape structshape, Color color, DrawingOnAPanel DP)
	{
		Point InitPoint = canvas.inDrawingCoords(initalCoords.get(0));
		// Util.ConvertToDrawingCoords(new double[] {InitCoords.get(0).x, InitCoords.get(0).y}, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getDrawingPos());
		int[] mousePos = new int[] {(int) MousePos.x, (int) MousePos.y};
		if (structshape.equals(StructureShape.rectangular))
		{
			DP.DrawLine(InitPoint, new Point(MousePos.x, InitPoint.y), MemberThickness, color);
			DP.DrawLine(InitPoint, new Point(InitPoint.x, MousePos.y), MemberThickness, color);
			DP.DrawLine(new int[] {MousePos.x, InitPoint.y}, mousePos, MemberThickness, color);
			DP.DrawLine(new int[] {InitPoint.x, MousePos.y}, mousePos, MemberThickness, color);
		}
		else if (structshape.equals(StructureShape.circular))
		{
			DP.DrawCircle(InitPoint, (int)(2*Util.dist(MousePos, InitPoint)), MemberThickness, true, true, Color.black, color);
		}
		else if (structshape.equals(StructureShape.polygonal))
		{
			int[] FinalPoint = new int[] {(int) initalCoords.get(initalCoords.size() - 1).x, (int) initalCoords.get(initalCoords.size() - 1).y};
			int[] xCoords = new int[initalCoords.size()], yCoords = new int[initalCoords.size()];
			for (int i = 0; i <= initalCoords.size() - 1; i += 1)
			{
				xCoords[i] = (int) initalCoords.get(i).x;
				yCoords[i] = (int) initalCoords.get(i).y;
			}
			DP.DrawPolyLine(xCoords, yCoords, MemberThickness, color);
			DP.DrawLine(FinalPoint, new int[] {MousePos.x, MousePos.y}, MemberThickness, color);
		}
		else
		{
			System.out.println("Structure shape not identified at Visuals -> DrawElemAddition");
		}
	}

	public void displayContent(Structure structure, int[] MainPanelPos, DrawingOnAPanel DP)
	{
		displayCanvasElements(canvas, showCanvas, showGrid, showMousePos);
		if (structure.getCoords() != null && structure.getCenter() != null)
		{
			structure.displayShape(canvas, DP) ;
		}

		canvas.drawCenter(DP) ;
		if (StructureCreationIsOn && structure.getCoords() != null)
		{
			drawStructureCreationWindow(structure.getCoords(), MenuFunctions.mousePos, 2, structure.getShape(), Menus.palette[6], DP);
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
			DrawResults(canvas, structure, MenuFunctions.SelectedElems, SelectedVar,
			MenuFunctions.ShowElemContour, showDeformedStructure,
			MenuFunctions.DiagramScales, ShowDisplacementContour, ShowStressContour, ShowStrainContour, ShowInternalForces,
			MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, DP);
			
			if (MenuFunctions.ShowReactionArrows && structure.getReactions() != null)
			{
				DP.DrawReactions3D(structure.getMesh().getNodes(), structure.getReactions(),
				structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.ShowReactionValues,
				Reactions.color, MenuFunctions.ShowDeformedStructure, MenuFunctions.DiagramScales[1], canvas);
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
			DP.DrawDistLoads3D(structure.getMesh(), loading.getDistLoads(), MenuFunctions.ShowLoadsValues, DistLoad.color,
			showDeformedStructure, structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.DiagramScales[1], canvas);
		}
		if (MenuFunctions.ShowNodalDisps && loading != null && loading.getNodalDisps() != null)
		{
			DP.DrawNodalDisps3D(structure.getMesh().getNodes(), loading.getNodalDisps(), structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.ShowLoadsValues,
			NodalDisp.color, showDeformedStructure, MenuFunctions.DiagramScales[1]);
		}
		if (MenuFunctions.ShowDOFNumber && structure.getMesh() != null && structure.getMesh().getNodes() != null)
		{
			DP.DrawDOFNumbers(structure.getMesh().getNodes(), Node.color, showDeformedStructure, canvas);
		}
		if (MenuFunctions.ShowNodeNumber && structure.getMesh() != null && structure.getMesh().getNodes() != null)
		{
			DP.DrawNodeNumbers(structure.getMesh().getNodes(), Node.color, showDeformedStructure, canvas);
		}
		if (MenuFunctions.ShowElemNumber && structure.getMesh() != null &&  structure.getMesh().getElements() != null)
		{
			DP.DrawElemNumbers(structure.getMesh(), Node.color, showDeformedStructure, canvas);
		}
		// if (showNodeSelectionWindow)
		// {
		// 	DP.DrawSelectionWindow(new Point(nodeSelectionWindowInitialPos[0], nodeSelectionWindowInitialPos[1]), MenuFunctions.mousePos);
		// }
		if (showElemSelectionWindow)
		{
			DP.DrawSelectionWindow(MenuFunctions.ElemSelectionWindowInitialPos, MenuFunctions.mousePos);
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

	public void switchDisplay(int selectedDiagram, int selectedVar)
	{
		if (selectedVar <= -1) { return ;}
		
		SelectedVar = selectedVar ;
		ShowDisplacementContour = selectedDiagram == 0;
		ShowStressContour = selectedDiagram == 1;
		ShowStrainContour = selectedDiagram == 2;
		ShowInternalForces = selectedDiagram == 3;
	}

	public static void DrawResults(MyCanvas canvas, Structure structure, int[] SelectedElems, int selectedvar,
			boolean ShowElemContour, boolean ShowDeformedStructure,
			double[] DiagramsScales, boolean ShowDisplacementContour, boolean ShowStressContour,
			boolean ShowStrainContour, boolean ShowInternalForces,
			boolean NonlinearMat, boolean NonlinearGeo, DrawingOnAPanel DP)
	{
		List<Node> nodes = structure.getMesh().getNodes();
		List<Element> elems = structure.getMesh().getElements();
		if (-1 < selectedvar && nodes != null && elems != null)
		{
			if (ShowDisplacementContour)
			{
				canvas.setTitle("Deslocamentos (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
				DP.DrawContours3D(elems, nodes, SelectedElems, ShowElemContour, ShowDeformedStructure, DiagramsScales[1],
				structure.getResults().getDispMin()[selectedvar], structure.getResults().getDispMax()[selectedvar], "Displacement", selectedvar,
				NonlinearMat, NonlinearGeo, "Red to green", canvas);
			}
			else if (ShowStressContour)
			{
				canvas.setTitle("Tensâes (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
				DP.DrawContours3D(elems, nodes, SelectedElems, ShowElemContour, ShowDeformedStructure, DiagramsScales[1],
				structure.getResults().getStressMin()[selectedvar], structure.getResults().getStressMax()[selectedvar], "Stress", selectedvar, 
				NonlinearMat, NonlinearGeo, "Red to green", canvas);
			}
			else if (ShowStrainContour)
			{
				canvas.setTitle("Deformaçõs (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
				DP.DrawContours3D(elems, nodes, SelectedElems, ShowElemContour, ShowDeformedStructure, DiagramsScales[1],
				structure.getResults().getStrainMin()[selectedvar], structure.getResults().getStrainMax()[selectedvar], "Strain", selectedvar,
				NonlinearMat, NonlinearGeo, "Red to green", canvas);
			}
			else if (ShowInternalForces)
			{
				canvas.setTitle("Forâas internas (x " + String.valueOf(Util.Round(DiagramsScales[1], 3)) + ")");
				DP.DrawContours3D(elems, nodes, SelectedElems, ShowElemContour, ShowDeformedStructure, DiagramsScales[1],
				structure.getResults().getInternalForcesMin()[selectedvar], structure.getResults().getInternalForcesMax()[selectedvar], "Force", selectedvar,
				NonlinearMat, NonlinearGeo, "Red to green", canvas);
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
		ShowDisplacementContour = false;
		ShowStressContour = false;
		ShowStrainContour = false;
		ShowInternalForces = false;
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
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        MenuFunctions.updateMousePosRelToPanelPos(panelPos) ;
        DP.setG(g);
		this.displayContent(MainPanel.structure, panelPos, DP);
		repaint();
    }

}
