package main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
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
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;

import main.gui.DrawingOnAPanel;
import main.gui.Menus;
import main.loading.Loading;
import main.mainTCC.Analysis;
import main.mainTCC.MenuFunctions;
import main.mainTCC.SelectionWindow;
import main.output.Results;
import main.structure.ConcLoads;
import main.structure.DistLoads;
import main.structure.ElemShape;
import main.structure.ElemType;
import main.structure.Element;
import main.structure.Material;
import main.structure.Mesh;
import main.structure.MeshType;
import main.structure.MyCanvas;
import main.structure.NodalDisps;
import main.structure.Node;
import main.structure.Reactions;
import main.structure.Section;
import main.structure.Structure;
import main.structure.StructureShape;
import main.structure.Supports;
import main.utilidades.Point3D;
import main.utilidades.Util;

public class MainPanel extends JPanel
{
	private static final long serialVersionUID = 1L;	
	private static final Dimension initialSize = new Dimension(582, 610) ;
	private static final Color bgColor = Menus.palette[2] ;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;
	
	private MyCanvas canvas ;
	private int[] panelPos ;
	private JTextArea mouseXPosTextArea ;
	private JTextArea mouseYPosTextArea ;	
	
	public static int selectedMatID;
	public static int selectedSecID;
	public static int selectedSupID;
	public static int selectedConcLoadID;
	public static int selectedDistLoadID;
	public static int selectedNodalDispID;
	private static boolean canvasIsVisible;

	private boolean showCanvas, showGrid, showMousePos;
	private boolean showStructure, showElems, showDeformedStructure ;
	private boolean showMatColor, showSecColor, showElemContour ;
	private boolean showNodeSelectionWindow ;
	public static boolean ShowDisplacementContour, ShowStressContour, ShowStrainContour, ShowInternalForces;

	public static int SelectedDiagram = -1;
	public static int SelectedVar = -1;
	
	private SelectionWindow selectionWindow ;
	public static boolean showElemSelectionWindow;
	
	public static boolean StructureCreationIsOn = false;
	
	public static Structure structure ;
	public static Loading loading ;
	
	public MainPanel(Point frameTopLeftPos)
	{
		showCanvas = true ;
		showGrid = true ;
		showMousePos = true ;
		
		int[] screenTopLeft = new int[] {0, 0, 0} ;
		canvas = new MyCanvas(new Point(575, 25), new int[] {(int) (0.4 * initialSize.width), (int) (0.8 * initialSize.height), 0}, new double[] {10, 10, 0}, screenTopLeft);
		panelPos = new int[] {frameTopLeftPos.x + 7 * Menus.buttonSize + 8, frameTopLeftPos.y + 76 + 8, 0};
		int insets = 5;
		
		mouseXPosTextArea = new JTextArea();
		mouseXPosTextArea.setEditable(false);
		mouseXPosTextArea.setMargin(new Insets(insets, insets, insets, insets));
		mouseXPosTextArea.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Menus.palette[0]));
		mouseXPosTextArea.setPreferredSize(new Dimension(30, 20));

		mouseYPosTextArea = new JTextArea();
		mouseYPosTextArea.setEditable(false);
		mouseYPosTextArea.setMargin(new Insets(insets, insets, insets, insets));
		mouseYPosTextArea.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Menus.palette[0]));
		mouseYPosTextArea.setPreferredSize(new Dimension(30, 20));
		
		structure = new Structure(null, null, null);
		loading = new Loading() ;
		
		selectionWindow = new SelectionWindow() ;
		showStructure = true ;
		showElems = true ;
		showDeformedStructure = true ;
		showMatColor = true ;
		showSecColor = true ;
		showElemContour = true ;
		canvasIsVisible = true;
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
			if (ElemMouseIsOn == -1 | (showElemSelectionWindow & -1 < ElemMouseIsOn))
			{
				showElemSelectionWindow = !showElemSelectionWindow;
				MenuFunctions.ElemSelectionWindowInitialPos = MenuFunctions.mousePos;
			}
		}
	}

	public static void StructureCreation(int[] MainPanelPos, MyCanvas canvas, Point mousePos, boolean SnipToGridIsOn)
	{
		List<Point3D> StructCoords = structure.getCoords();
		   
		if (Util.MouseIsInside(mousePos, new int[2], canvas.getPos(), canvas.getSize()[0], canvas.getSize()[1]))
	    {
			if (structure.getShape().equals(StructureShape.rectangular) | structure.getShape().equals(StructureShape.circular))
			{
				if (StructCoords != null)
				{
    				StructureCreationIsOn = false;
				}
				Point3D newCoord = MenuFunctions.getCoordFromMouseClick(canvas, mousePos, SnipToGridIsOn) ;
				structure.addCoordFromMouseClick(newCoord) ;
				System.out.println("Mouse pos: " + mousePos);
				System.out.println("New coord: " + newCoord);
				System.out.println(structure);
			}
			else if (structure.getShape().equals(StructureShape.polygonal))
			{
				int prec = 10;
				if (StructCoords != null)
				{
					if (Util.dist(new double[] {mousePos.x, mousePos.y}, new double[] {StructCoords.get(0).x, StructCoords.get(0).y}) < prec)
					{
	    				StructureCreationIsOn = false;
					}
				}
				Point3D newCoord = MenuFunctions.getCoordFromMouseClick(canvas, mousePos, SnipToGridIsOn) ;
				structure.addCoordFromMouseClick(newCoord) ;
			}
			else
			{
				System.out.println("Structure shape not identified at Menus -> CreateStructure");
			}
	    }

		if (!StructureCreationIsOn)
		{
			structure.updateCenter() ;
			canvas.setCenter(canvas.inDrawingCoords(structure.getCenter()));
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

	public void displayContent(Dimension jpMainSize, int[] MainPanelPos, DrawingOnAPanel DP)
	{
		displayCanvasElements(canvas, showCanvas, showGrid, showMousePos);
		// System.out.println(MenuFunctions.struct.getCoords());
		if (showStructure && MainPanel.structure.getCoords() != null && MainPanel.structure.getCenter() != null)
		{
			// System.out.println(canvas);
			DP.DrawStructureContour3D(MainPanel.structure.getCoords(), Structure.color, canvas);
		}

		DP.DrawCircle(canvas.getCenter(), 10, 1, false, true, Menus.palette[0], Menus.palette[7]);
		if (StructureCreationIsOn & MainPanel.structure.getCoords() != null)
		{
			drawStructureCreationWindow(MainPanel.structure.getCoords(), MenuFunctions.mousePos, 2, MainPanel.structure.getShape(), Menus.palette[6], DP);
		}
		if (showElems && MainPanel.structure.getMesh() != null && MainPanel.structure.getMesh().getElements() != null)
		{
			if (showDeformedStructure)
			{
				canvas.setTitle("Estrutura deformada (x " + String.valueOf(Util.Round(MenuFunctions.DiagramScales[1], 3)) + ")");
			}
			DP.DrawElements3D(MainPanel.structure.getMesh(), MenuFunctions.SelectedElems,
			showMatColor, showSecColor, showElemContour, showDeformedStructure, MenuFunctions.DiagramScales[1], canvas);
		}
		if (MenuFunctions.ShowNodes && MainPanel.structure.getMesh() != null && MainPanel.structure.getMesh().getNodes() != null)
		{
			DP.DrawNodes3D(MainPanel.structure.getMesh().getNodes(), MenuFunctions.selectedNodes, Node.color, showDeformedStructure,
			MainPanel.structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.DiagramScales[1], canvas);
		}
		if (MenuFunctions.AnalysisIsComplete)
		{
			DrawResults(canvas, MainPanel.structure, MenuFunctions.SelectedElems, SelectedVar,
			MenuFunctions.ShowElemContour, showDeformedStructure,
			MenuFunctions.DiagramScales, ShowDisplacementContour, ShowStressContour, ShowStrainContour, ShowInternalForces,
			MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, DP);
			
			if (MenuFunctions.ShowReactionArrows & structure.getReactions() != null)
			{
				DP.DrawReactions3D(MainPanel.structure.getMesh().getNodes(), structure.getReactions(),
				MainPanel.structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.ShowReactionValues,
				Reactions.color, MenuFunctions.ShowDeformedStructure, MenuFunctions.DiagramScales[1], canvas);
			}
		}
		if (MenuFunctions.ShowSup & MainPanel.structure.getSupports() != null)
		{
			DP.DrawSup3D(MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getSupports(), Supports.color, canvas);
		}
		if (MenuFunctions.ShowConcLoads & loading != null && loading.getConcLoads() != null)
		{
			DP.DrawConcLoads3D(MainPanel.structure.getMesh().getNodes(), loading.getConcLoads(),
			MainPanel.structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.ShowLoadsValues,
			ConcLoads.color, showDeformedStructure, MenuFunctions.DiagramScales[1], canvas);
		}
		if (MenuFunctions.ShowDistLoads & loading != null && loading.getDistLoads() != null)
		{
			DP.DrawDistLoads3D(MainPanel.structure.getMesh(), loading.getDistLoads(), MenuFunctions.ShowLoadsValues, DistLoads.color,
			showDeformedStructure, MainPanel.structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.DiagramScales[1], canvas);
		}
		if (MenuFunctions.ShowNodalDisps & loading != null && loading.getNodalDisps() != null)
		{
			DP.DrawNodalDisps3D(MainPanel.structure.getMesh().getNodes(), loading.getNodalDisps(), MainPanel.structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.ShowLoadsValues,
			NodalDisps.color, showDeformedStructure, MenuFunctions.DiagramScales[1]);
		}
		if (MenuFunctions.ShowDOFNumber && MainPanel.structure.getMesh() != null && MainPanel.structure.getMesh().getNodes() != null)
		{
			DP.DrawDOFNumbers(MainPanel.structure.getMesh().getNodes(), Node.color, showDeformedStructure, canvas);
		}
		if (MenuFunctions.ShowNodeNumber && MainPanel.structure.getMesh() != null && MainPanel.structure.getMesh().getNodes() != null)
		{
			DP.DrawNodeNumbers(MainPanel.structure.getMesh().getNodes(), Node.color, showDeformedStructure, canvas);
		}
		if (MenuFunctions.ShowElemNumber && MainPanel.structure.getMesh() != null &&  MainPanel.structure.getMesh().getElements() != null)
		{
			DP.DrawElemNumbers(MainPanel.structure.getMesh(), Node.color, showDeformedStructure, canvas);
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
		
		if (showMousePos)
		{
			Point2D.Double mousePos = canvas.inRealCoords(MenuFunctions.mousePos) ;
			mouseXPosTextArea.setText(String.valueOf(Util.Round(mousePos.x, 3)));
			mouseYPosTextArea.setText(String.valueOf(Util.Round(mousePos.y, 3)));
		}
	}

	public static void DrawResults(MyCanvas canvas, Structure structure, int[] SelectedElems, int selectedvar,
			boolean ShowElemContour, boolean ShowDeformedStructure,
			double[] DiagramsScales, boolean ShowDisplacementContour, boolean ShowStressContour,
			boolean ShowStrainContour, boolean ShowInternalForces,
			boolean NonlinearMat, boolean NonlinearGeo, DrawingOnAPanel DP)
	{
		List<Node> nodes = structure.getMesh().getNodes();
		List<Element> elems = structure.getMesh().getElements();
		if (-1 < selectedvar & nodes != null & elems != null)
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
			System.out.println("\nMouse pos: " + canvas.inRealCoords(MenuFunctions.mousePos) + " -> " + canvas.inDrawingCoords(canvas.inRealCoords(MenuFunctions.mousePos)));
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


	public static void setElemType(String ElemType)
	{
		MenuFunctions.SelectedElemType = ElemType;
	}


	
 	public static void AddMaterialToElements(int[] Elems, Material mat)
	{
		if (MainPanel.structure.getMesh().getElements() != null & Elems != null & mat != null)
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
		if (MainPanel.structure.getMesh().getElements() != null & sec != null & Elems != null)
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
		if (-1 < selectedSupID & MenuFunctions.selectedNodes != null & MenuFunctions.SupType != null )
		{
			for (int i = 0; i <= MenuFunctions.selectedNodes.size() - 1; i += 1)
			{
				if (-1 < MenuFunctions.selectedNodes.get(i).getID())
				{
					int supid = MainPanel.structure.getSupports().size() - MenuFunctions.selectedNodes.size() + i;
					Supports newSupport = new Supports(supid, MenuFunctions.selectedNodes.get(i), MenuFunctions.SupType[selectedSupID]);
					MainPanel.structure.addSupport(newSupport) ;
					MenuFunctions.selectedNodes.get(i).setSup(MenuFunctions.SupType[selectedSupID]);
				}
			}
			MenuFunctions.ShowSup = true;
			MenuFunctions.selectedNodes = null;
		}
	}
	
	public static void AddConcLoads()
	{
		if (-1 < selectedConcLoadID & MenuFunctions.selectedNodes != null & MenuFunctions.ConcLoadType != null)
		{
			for (int i = 0; i <= MenuFunctions.selectedNodes.size() - 1; i += 1)
			{
				int loadid = loading.getConcLoads().size() - MenuFunctions.selectedNodes.size() + i;
				if (-1 < MenuFunctions.selectedNodes.get(i).getID())
				{
					ConcLoads newConcLoad = new ConcLoads(loadid, MenuFunctions.selectedNodes.get(i), MenuFunctions.ConcLoadType[selectedConcLoadID]);
					loading.getConcLoads().add(newConcLoad);
					MenuFunctions.selectedNodes.get(i).AddConcLoads(newConcLoad);
				}
			}
			MenuFunctions.ShowConcLoads = true;
			MenuFunctions.selectedNodes = null;
		}
	}
	
	public static void AddDistLoads()
	{
		if (-1 < selectedDistLoadID & MenuFunctions.SelectedElems != null & MenuFunctions.DistLoadType != null)
		{
			for (int i = 0; i <= MenuFunctions.SelectedElems.length - 1; i += 1)
			{
				int loadid = loading.getDistLoads().size() - MenuFunctions.SelectedElems.length + i;
				if (-1 < MenuFunctions.SelectedElems[i])
				{
					int elem = MenuFunctions.SelectedElems[i];
					int LoadType = (int) MenuFunctions.DistLoadType[selectedDistLoadID][0];
					double Intensity = MenuFunctions.DistLoadType[selectedDistLoadID][1];
					DistLoads newDistLoad = new DistLoads(loadid, MenuFunctions.SelectedElems[i], LoadType, Intensity) ;
					loading.getDistLoads().add(newDistLoad);
					MainPanel.structure.getMesh().getElements().get(elem).setDistLoads(Util.AddElem(MainPanel.structure.getMesh().getElements().get(elem).getDistLoads(), newDistLoad));
				}
			}
			MenuFunctions.ShowDistLoads = true;
			MenuFunctions.SelectedElems = null;
		}
	}
	
	public static void AddNodalDisps()
	{
		if (-1 < selectedNodalDispID & MenuFunctions.selectedNodes != null & MenuFunctions.NodalDispType != null)
		{
			for (int i = 0; i <= MenuFunctions.selectedNodes.size() - 1; i += 1)
			{
				int dispid = loading.getNodalDisps().size() - MenuFunctions.selectedNodes.size() + i;
				if (-1 < MenuFunctions.selectedNodes.get(i).getID())
				{
					NodalDisps newNodalDisp = new NodalDisps(dispid, MenuFunctions.selectedNodes.get(i), MenuFunctions.NodalDispType[selectedNodalDispID]) ;
					loading.getNodalDisps().add(newNodalDisp);
					MenuFunctions.selectedNodes.get(i).AddNodalDisps(newNodalDisp);
				}
			}
			MenuFunctions.ShowNodalDisps = true;
			MenuFunctions.selectedNodes = null;
		}
	}
	
	public static void setMaterials(List<Material> materials)
	{
		MenuFunctions.matTypes = materials;
	}

	public static void setSections(List<Section> sections)
	{
		MenuFunctions.secTypes = sections;
	}

	public static void DefineSupportTypes(int[][] Supports)
	{
		MenuFunctions.SupType = Supports;
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
	
	public static void StructureMenuAssignMaterials()
	{
		MenuFunctions.ElemSelectionIsOn = !MenuFunctions.ElemSelectionIsOn;
		selectedMatID = 0;
	}
	
	public static void StructureMenuAssignSections()
	{
		MenuFunctions.ElemSelectionIsOn = !MenuFunctions.ElemSelectionIsOn;
		selectedSecID = 0;
	}
	
	public static void StructureMenuAssignSupports()
	{
		MenuFunctions.NodeSelectionIsOn = !MenuFunctions.NodeSelectionIsOn;
		selectedSupID = 0;
	}
	
	public static void StructureMenuAssignConcLoads()
	{
		MenuFunctions.NodeSelectionIsOn = !MenuFunctions.NodeSelectionIsOn;
		selectedConcLoadID = 0;
	}
	
	public static void StructureMenuAssignDistLoads()
	{
		MenuFunctions.ElemSelectionIsOn = !MenuFunctions.ElemSelectionIsOn;
		selectedDistLoadID = 0;
	}
	
	public static void StructureMenuAssignNodalDisps()
	{
		MenuFunctions.NodeSelectionIsOn = !MenuFunctions.NodeSelectionIsOn;
		selectedNodalDispID = 0;
	}
	
	public static Object[] CreatureStructure(List<Point3D> StructCoords, MeshType meshType, int[] MeshSizes, ElemType elemType,
			Material CurrentMatType, List<Material> matTypes, Section CurrentSecType, List<Section> secTypes,
			int SupConfig, int SelConcLoad, int SelDistLoad, int ConcLoadConfig, int DistLoadConfig)
	{
		/* Tipo de elemento, materiais, seçõs, apoios e cargas jâ estâo definidos */
		
		/* 1. Criar polâgono */
		MainPanel.structure = new Structure("Especial", StructureShape.rectangular, StructCoords);
		
		/* 2. Criar malha */		
		MainPanel.structure.removeSupports() ;
		loading.clearLoads() ;
		MainPanel.structure.createMesh(meshType, new int[][] {MeshSizes}, elemType);
		
		/* 3. Atribuir materiais */
		for (int elem = 0; elem <= MainPanel.structure.getMesh().getElements().size() - 1; elem += 1)
		{
			MenuFunctions.SelectedElems = Util.AddElem(MenuFunctions.SelectedElems, elem);
		}
		AddMaterialToElements(MenuFunctions.SelectedElems, CurrentMatType);
		Element.createMatColors(matTypes);
		for (Element elem : MainPanel.structure.getMesh().getElements())
		{
			int matColorID = matTypes.indexOf(elem.getMat()) ;
			elem.setMatColor(Element.matColors[matColorID]);
		}

		/* 4. Atribuir seçõs */
		for (int elem = 0; elem <= MainPanel.structure.getMesh().getElements().size() - 1; elem += 1)
		{
			MenuFunctions.SelectedElems = Util.AddElem(MenuFunctions.SelectedElems, elem);
		}
		AddSectionsToElements(MenuFunctions.SelectedElems, CurrentSecType);
		Element.setSecColors(secTypes);
		for (Element elem : MainPanel.structure.getMesh().getElements())
		{
			int secID = secTypes.indexOf(elem.getSec()) ;
			elem.setSecColor(Element.SecColors[secID]);
		}
		
		/* 5. Atribuir apoios */
		Supports[] supports = Util.AddEspecialSupports(MainPanel.structure.getMesh().getNodes(), Element.typeToShape(elemType), meshType, new int[] {MeshSizes[0], MeshSizes[1]}, SupConfig);
		for (Supports sup : supports)
		{
			MainPanel.structure.addSupport(sup);
		}

		/* 6. Atribuir cargas */
		if (ConcLoadConfig == 1)
		{
			MenuFunctions.selectedNodes = new ArrayList<Node>();
			if (MainPanel.structure.getMesh().getElements().get(0).getShape().equals(ElemShape.rectangular))
			{
				int nodeID = (MeshSizes[1] / 2 * (MeshSizes[0] + 1) + MeshSizes[0] / 2) ;
				MenuFunctions.selectedNodes.add(MainPanel.structure.getMesh().getNodes().get(nodeID));
			}
			else if (MainPanel.structure.getMesh().getElements().get(0).getShape().equals(ElemShape.r8))
			{
				int nodeID = (MeshSizes[1] / 2 * (2 * MeshSizes[0] + 1 + MeshSizes[0] + 1) + MeshSizes[0]) ;
				MenuFunctions.selectedNodes.add(MainPanel.structure.getMesh().getNodes().get(nodeID));
			}
		}
		selectedConcLoadID = SelConcLoad;
		AddConcLoads();
		selectedDistLoadID = SelDistLoad;
		if (-1 < SelDistLoad)
		{
			MenuFunctions.SelectedElems = null;
			for (int elem = 0; elem <= MainPanel.structure.getMesh().getElements().size() - 1; elem += 1)
			{
				MenuFunctions.SelectedElems = Util.AddElem(MenuFunctions.SelectedElems, elem);
			}
			AddDistLoads();
		}
		
		/* 7. Calcular parâmetros para a anâlise */
		MenuFunctions.CalcAnalysisParameters(MainPanel.structure);
		
		MenuFunctions.selectedNodes = null;
		MenuFunctions.SelectedElems = null;
		return new Object[] {MainPanel.structure.getMesh().getNodes(), MainPanel.structure.getMesh().getElements(), MainPanel.structure.getSupports(),
			loading.getConcLoads(), loading.getDistLoads(), null};
	}


	public static void ShowResult(Structure struct)
	{
		MenuFunctions.ShowElems = false;
		for (int node = 0; node <= struct.getMesh().getElements().get(0).getExternalNodes().length - 1; node += 1)
		{
			SelectedVar = Util.ElemPosInArray(struct.getMesh().getElements().get(0).getDOFsPerNode()[node], SelectedVar);
			if (-1 < SelectedVar)
			{
				node = struct.getMesh().getElements().get(0).getExternalNodes().length - 1;
			}
		}
		if (SelectedDiagram == 0 & -1 < SelectedVar)
		{
			//DrawDisplacementContours(SelectedVar);
			struct.getResults().setDispMin(Results.FindMinDisps(struct.getU(), struct.getMesh().getElements().get(0).getDOFs(), Analysis.DefineFreeDoFTypes(struct.getMesh().getNodes()))) ;
			struct.getResults().setDispMax(Results.FindMaxDisps(struct.getU(), struct.getMesh().getElements().get(0).getDOFs(), Analysis.DefineFreeDoFTypes(struct.getMesh().getNodes()))) ;
			ShowDisplacementContour = false;
			ShowDisplacementContour = true;
			ShowStrainContour = false;
			ShowInternalForces = false;
		}
		else if (SelectedDiagram == 1 & -1 < SelectedVar)
		{
			ShowDisplacementContour = false;
			ShowStressContour = true;
			ShowStrainContour = false;
			ShowInternalForces = false;
		}
		else if (SelectedDiagram == 2 & -1 < SelectedVar)
		{
			ShowDisplacementContour = false;
			ShowStressContour = false;
			ShowStrainContour = true;
			ShowInternalForces = false;
		}
		else if (SelectedDiagram == 3 & -1 < SelectedVar)
		{
			ShowDisplacementContour = false;
			ShowStressContour = false;
			ShowStrainContour = false;
			ShowInternalForces = true;
		}
	}

	public static void resetDisplay()
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
				Menus.getInstance().updateInstructionPanel();
			}
			if (!StructureCreationIsOn)
			{
				Menus.getInstance().StepIsComplete = MenuFunctions.CheckSteps(MainPanel.structure);
				Menus.getInstance().UpperToolbarButton[0].setEnabled(false);
				Menus.getInstance().UpperToolbarButton[0].setVisible(false);
				Menus.getInstance().UpperToolbarButton[1].setEnabled(false);
				Menus.getInstance().UpperToolbarButton[1].setVisible(false);
			}
			if (MenuFunctions.NodeSelectionIsOn)
			{
				NodeAddition(panelPos);
				if (MenuFunctions.selectedNodes != null)
				{
					if (!MenuFunctions.selectedNodes.isEmpty() && -1 < MenuFunctions.selectedNodes.get(0).getID())
					{
						Menus.getInstance().ResetEastPanels();
						//AddNodeInfoPanel(MenuFunctions.Node[MenuFunctions.SelectedNodes[0]]);
					}
				}
			}
			if (MenuFunctions.ElemSelectionIsOn)
			{
				ElemAddition(MainPanel.structure, canvas, panelPos);
				if (MenuFunctions.SelectedElems != null)
				{
					if (-1 < MenuFunctions.SelectedElems[0])
					{
						Menus.getInstance().ResetEastPanels();
						//AddElemInfoPanel(MenuFunctions.Elem[MenuFunctions.SelectedElems[0]]);
					}
				}
			}
		}

		if (evt.getButton() == 3)	// Right click
		{
			MainPanel.structure.printStructure(MenuFunctions.matTypes, MenuFunctions.secTypes, MainPanel.structure.getSupports(), loading);
			MenuFunctions.ElemDetailsView();
		}
	}

	private void handleMouseWheel(MouseWheelEvent evt)
	{
		boolean[] assignmentIsOn = new boolean[] {Menus.getInstance().MatAssignmentIsOn, Menus.getInstance().SecAssignmentIsOn, Menus.getInstance().SupAssignmentIsOn,
			Menus.getInstance().ConcLoadsAssignmentIsOn, Menus.getInstance().DistLoadsAssignmentIsOn, Menus.getInstance().NodalDispsAssignmentIsOn} ;
		double qtdRotation = evt.getWheelRotation() ;

		boolean MouseIsInMainCanvas = Util.MouseIsInside(MenuFunctions.mousePos, panelPos, canvas.getPos(), canvas.getSize()[0], canvas.getSize()[1]);
		if (canvasIsVisible & Util.MouseIsInside(MenuFunctions.mousePos, panelPos, canvas.getPos(), canvas.getSize()[0], canvas.getSize()[1]))
		{
			canvas.getDimension()[0] += Util.Round(0.2*Math.log10(canvas.getDimension()[0])*qtdRotation, 1);
			canvas.getDimension()[1] += Util.Round(0.2*Math.log10(canvas.getDimension()[1])*qtdRotation, 1);
		}
		if (assignmentIsOn[0] & !MouseIsInMainCanvas)
		{
			selectedMatID += qtdRotation;
			selectedMatID = Math.min(Math.max(selectedMatID, 0), MenuFunctions.matTypes.size() - 1);
		}
		if (assignmentIsOn[1] & !MouseIsInMainCanvas)
		{
			selectedSecID += qtdRotation;
			selectedSecID = Math.min(Math.max(selectedSecID, 0), MenuFunctions.secTypes.size() - 1);
		}
		if (assignmentIsOn[2] & !MouseIsInMainCanvas)
		{
			selectedSupID += qtdRotation;
			selectedSupID = Math.min(Math.max(selectedSupID, 0), MenuFunctions.SupType.length - 1);
		}
		if (assignmentIsOn[3] & !MouseIsInMainCanvas)
		{
			selectedConcLoadID += qtdRotation;
			selectedConcLoadID = Math.min(Math.max(selectedConcLoadID, 0), MenuFunctions.ConcLoadType.length - 1);
		}
		if (assignmentIsOn[4] & !MouseIsInMainCanvas)
		{
			selectedDistLoadID += qtdRotation;
			selectedDistLoadID = Math.min(Math.max(selectedDistLoadID, 0), MenuFunctions.DistLoadType.length - 1);
		}
		if (assignmentIsOn[5] & !MouseIsInMainCanvas)
		{
			selectedNodalDispID += qtdRotation;
			selectedNodalDispID = Math.min(Math.max(selectedNodalDispID, 0), MenuFunctions.NodalDispType.length - 1);
		}
	

		MenuFunctions.updateDiagramScale(canvas, evt.getWheelRotation());
	}

	@Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        MenuFunctions.updateMousePosRelToPanelPos(panelPos) ;
        DP.setG(g);
        DP.setRealStructCenter(MainPanel.structure.getCenter());
		this.displayContent(initialSize, panelPos, DP);
		repaint();
    }

}
