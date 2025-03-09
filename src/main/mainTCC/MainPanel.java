package main.mainTCC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout;

import main.gui.DrawingOnAPanel;
import main.gui.Menus;
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
import main.utilidades.UtilComponents;

public class MainPanel extends JPanel
{
	private static final long serialVersionUID = 1L;	
	private static final Dimension initialSize = new Dimension(582, 610) ;
	private static final Color bgColor = Menus.palette[2] ;
	private static final DrawingOnAPanel DP = new DrawingOnAPanel() ;
	
	private MyCanvas canvas ;
	private int[] panelPos ;
	private JTextArea mouseXPosTextArea = new JTextArea();
	private JTextArea mouseYPosTextArea = new JTextArea();
	private boolean showCanvas, showGrid, showMousePos;
	private boolean showStructure = true, showElems = true, showDeformedStructure = true ;
	private boolean showMatColor = true, showSecColor = true, showElemContour = true ;
	private boolean showNodeSelectionWindow ;

	private int[] nodeSelectionWindowInitialPos = new int[2] ;
	
	public MainPanel(Point frameTopLeftPos)
	{
		showCanvas = true ;
		showGrid = true ;
		showMousePos = true ;
		
		int[] screenTopLeft = new int[] {0, 0, 0} ;
		canvas = new MyCanvas (new Point(575, 25), new int[] {(int) (0.4 * initialSize.width), (int) (0.8 * initialSize.height), 0}, new double[] {10, 10, 0}, screenTopLeft);
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
		
		this.setBackground(bgColor);
	    this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	    this.addMouseListener(new MouseAdapter() 
	    {
			public void mousePressed(MouseEvent evt)
			{
				requestFocusInWindow();
				if (evt.getButton() == 1)	// Left click
				{
					if (MenuFunctions.StructureCreationIsOn)
					{
						MenuFunctions.StructureCreation(panelPos, canvas);
						Menus.getInstance().EnableButtons();
						Menus.getInstance().updateInstructionPanel();
					}
					if (!MenuFunctions.StructureCreationIsOn)
					{
						Menus.getInstance().StepIsComplete = MenuFunctions.CheckSteps();
						Menus.getInstance().UpperToolbarButton[0].setEnabled(false);
						Menus.getInstance().UpperToolbarButton[0].setVisible(false);
						Menus.getInstance().UpperToolbarButton[1].setEnabled(false);
						Menus.getInstance().UpperToolbarButton[1].setVisible(false);
					}				
					if (MenuFunctions.NodeSelectionIsOn)
					{
						NodeAddition(canvas, panelPos);
						if (MenuFunctions.SelectedNodes != null)
						{
							if (-1 < MenuFunctions.SelectedNodes[0])
							{
								Menus.getInstance().ResetEastPanels();
								//AddNodeInfoPanel(MenuFunctions.Node[MenuFunctions.SelectedNodes[0]]);
							}
						}
					}
					if (MenuFunctions.ElemSelectionIsOn)
					{
						MenuFunctions.ElemAddition(canvas, panelPos);
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
					UtilComponents.PrintStructure(Menus.getInstance().StructureMenu.getName(), MenuFunctions.Struct.getMesh(),
					MenuFunctions.matTypes, MenuFunctions.secTypes, MenuFunctions.Sup, MenuFunctions.ConcLoad, MenuFunctions.DistLoad, MenuFunctions.NodalDisp);
					MenuFunctions.ElemDetailsView();
				}
			}
			public void mouseReleased(MouseEvent evt) 
			{
		    }
		});
	    this.addMouseMotionListener(new MouseMotionAdapter() 
	    {
	        public void mouseDragged(MouseEvent evt) 
	        {

	        }
	    });
	    this.addMouseWheelListener(new MouseWheelListener()
	    {
			@Override
			public void mouseWheelMoved(MouseWheelEvent evt) 
			{
				MenuFunctions.MouseWheel(panelPos, canvas, evt.getWheelRotation(),
				new boolean[] {Menus.getInstance().MatAssignmentIsOn, Menus.getInstance().SecAssignmentIsOn, Menus.getInstance().SupAssignmentIsOn,
					Menus.getInstance().ConcLoadsAssignmentIsOn, Menus.getInstance().DistLoadsAssignmentIsOn, Menus.getInstance().NodalDispsAssignmentIsOn});
				MenuFunctions.updateDiagramScale(canvas, evt.getWheelRotation());
			}       	
	    });
	    this.addKeyListener(new KeyListener()
	    {
			@Override
			public void keyPressed(KeyEvent evt)
			{
				int keyCode = evt.getKeyCode();
				char keychar = evt.getKeyChar();
				char[] ActionKeys = new char[] {'Q', 'W', 'A', 'S', 'Z', 'X','E', 'D', 'C'};	// Q, W, A, S, Z, X: rotation, E: top view, D: front view, C: side view
				if (keychar == Character.toLowerCase(ActionKeys[0]) | keychar == Character.toUpperCase(ActionKeys[0]))
				{
					canvas.setAngles(new double[] {canvas.getAngles()[0] - Math.PI/180.0, canvas.getAngles()[1], canvas.getAngles()[2]});
				}
				if (keychar == Character.toLowerCase(ActionKeys[1]) | keychar == Character.toUpperCase(ActionKeys[1]))
				{
					canvas.setAngles(new double[] {canvas.getAngles()[0] + Math.PI/180.0, canvas.getAngles()[1], canvas.getAngles()[2]});
				}
				if (keychar == Character.toLowerCase(ActionKeys[2]) | keychar == Character.toUpperCase(ActionKeys[2]))
				{
					canvas.setAngles(new double[] {canvas.getAngles()[0], canvas.getAngles()[1] + Math.PI/180.0, canvas.getAngles()[2]});
				}
				if (keychar == Character.toLowerCase(ActionKeys[3]) | keychar == Character.toUpperCase(ActionKeys[3]))
				{
					canvas.setAngles(new double[] {canvas.getAngles()[0], canvas.getAngles()[1] - Math.PI/180.0, canvas.getAngles()[2]});
				}
				if (keychar == Character.toLowerCase(ActionKeys[4]) | keychar == Character.toUpperCase(ActionKeys[4]))
				{
					canvas.setAngles(new double[] {canvas.getAngles()[0], canvas.getAngles()[1], canvas.getAngles()[2] + Math.PI/180.0});
				}
				if (keychar == Character.toLowerCase(ActionKeys[5]) | keychar == Character.toUpperCase(ActionKeys[5]))
				{
					canvas.setAngles(new double[] {canvas.getAngles()[0], canvas.getAngles()[1], canvas.getAngles()[2] - Math.PI/180.0});
				}
				if (keychar == Character.toLowerCase(ActionKeys[6]) | keychar == Character.toUpperCase(ActionKeys[6]))
				{
					canvas.setAngles(new double[] {0, 0, 0});
				}
				if (keychar == Character.toLowerCase(ActionKeys[7]) | keychar == Character.toUpperCase(ActionKeys[7]))
				{
					canvas.setAngles(new double[] {0, -Math.PI/2.0, 0});
				}
				if (keychar == Character.toLowerCase(ActionKeys[8]) | keychar == Character.toUpperCase(ActionKeys[8]))
				{
					canvas.setAngles(new double[] {-Math.PI/2.0, 0, 0});
				}
				if (keyCode == KeyEvent.VK_RIGHT)
				{
					canvas.setDrawingPos(new int[] {canvas.getDrawingPos()[0] + 10, canvas.getDrawingPos()[1], canvas.getDrawingPos()[2]});
				}
				if (keyCode == KeyEvent.VK_LEFT)
				{
					canvas.setDrawingPos(new int[] {canvas.getDrawingPos()[0] - 10, canvas.getDrawingPos()[1], canvas.getDrawingPos()[2]});
				}
				if (keyCode == KeyEvent.VK_UP)
				{
					canvas.setDrawingPos(new int[] {canvas.getDrawingPos()[0], canvas.getDrawingPos()[1] - 10, canvas.getDrawingPos()[2]});
				}
				if (keyCode == KeyEvent.VK_DOWN)
				{
					canvas.setDrawingPos(new int[] {canvas.getDrawingPos()[0], canvas.getDrawingPos()[1] + 10, canvas.getDrawingPos()[2]});
				}
				if (keyCode == KeyEvent.VK_ESCAPE)
				{
					MenuFunctions.SelectedNodes = null;
					MenuFunctions.SelectedElems = null;
				}
			}
	
			@Override
			public void keyReleased(KeyEvent evt)
			{
				
			}
	
			@Override
			public void keyTyped(KeyEvent evt)
			{
				
			}        	
	    });
 
	    this.setSize(initialSize);
	    this.setFocusable(true);
	    this.requestFocusInWindow();
	    this.setPreferredSize(initialSize);
		
	}
		
	public void displayCanvasElements(MyCanvas canvas,  boolean ShowCanvas, boolean ShowGrid, boolean ShowMousePos)
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
		double[] RealMousePos = Util.ConvertToRealCoords(MenuFunctions.MousePos, new int[] {canvas.getPos().x, canvas.getPos().y}, canvas.getSize(), canvas.getDimension());
		DrawMousePos(new int[] {BigAxisPos[0] + canvas.getSize()[0] / 2 - 60, BigAxisPos[1] + 40}, RealMousePos, Menus.palette[3], Menus.palette[0], DP);
	}
	
	public void DrawAxis(int[] Pos, int sizex, int sizey, int sizez, double[] CanvasAngles, DrawingOnAPanel DP)
	{
    	int thickness = 2;
		DP.DrawAxisArrow3D(new int[] {Pos[0] + sizex, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1], CanvasAngles[2]}, true, sizex, sizex / 40.0, Color.red);
		DP.DrawAxisArrow3D(new int[] {Pos[0] + sizey, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1], CanvasAngles[2] - Math.PI/2.0}, true, sizey, sizey / 40.0, Color.green);
		DP.DrawAxisArrow3D(new int[] {Pos[0] + sizez, Pos[1], Pos[2]}, thickness, new double[] {CanvasAngles[0], CanvasAngles[1] - Math.PI/2.0, CanvasAngles[2]}, true, sizez, sizez / 40.0, Color.blue);	// z points outward
	}
	
	private static void DrawMousePos(int[] Pos, double[] MousePos, Color bgcolor, Color contourcolor, DrawingOnAPanel DP)
	{
		int[] RectSize = new int[] {40, 18};
		int RectThick = 1;
		int FontSize = 13;
		DP.DrawRoundRect(new int[] {Pos[0] - 20, Pos[1] - RectSize[1] / 2 - 8}, "Left", 200, 24, 1, 5, 5, new Color[] {bgcolor}, contourcolor, true);
		DP.DrawText(new int[] {Pos[0], Pos[1]}, "Mouse Pos:", "Left", 0, "Bold", FontSize, Color.black);
		DP.DrawRect(new int[] {Pos[0] + 80, Pos[1] - RectSize[1] + FontSize / 3}, RectSize[0], RectSize[1], RectThick, "Left", 0, false, Color.black, null);
		DP.DrawText(new int[] {Pos[0] + 85, Pos[1]}, String.valueOf(Util.Round(MousePos[0], 2)), "Left", 0, "Bold", FontSize, Color.black);
		DP.DrawRect(new int[] {Pos[0] + 125, Pos[1] - RectSize[1] + FontSize / 3}, RectSize[0], RectSize[1], RectThick, "Left", 0, false, Color.black, null);
		DP.DrawText(new int[] {Pos[0] + 130, Pos[1]}, String.valueOf(Util.Round(MousePos[1], 2)), "Left", 0, "Bold", FontSize, Color.black);
	}
	
	public void displayContent(Dimension jpMainSize, int[] MainPanelPos, int[] MainCanvasCenter, MyCanvas MainCanvas, DrawingOnAPanel DP)
	{
		displayCanvasElements(MainCanvas, showCanvas, showGrid, showMousePos);
		if (showStructure && MenuFunctions.Struct.getCoords() != null && MenuFunctions.Struct.getCenter() != null &&
			MenuFunctions.Struct.getMesh() != null && MenuFunctions.Struct.getMesh().getElements() == null)
		{
			DP.DrawStructureContour3D(MenuFunctions.Struct.getCoords(), Structure.color, canvas);
		}
		
		DP.DrawCircle(MainCanvasCenter, 10, 1, false, true, Menus.palette[0], Menus.palette[7]);
		if (MenuFunctions.StructureCreationIsOn & MenuFunctions.Struct.getCoords() != null)
		{
			DP.DrawElemAddition(MenuFunctions.Struct.getCoords(), MenuFunctions.MousePos, 2, MenuFunctions.Struct.getShape(), Menus.palette[6]);
		}
		if (showElems && MenuFunctions.Struct.getMesh() != null && MenuFunctions.Struct.getMesh().getElements() != null)
		{
			if (showDeformedStructure)
			{
				MainCanvas.setTitle("Estrutura deformada (x " + String.valueOf(Util.Round(MenuFunctions.DiagramScales[1], 3)) + ")");
			}
			DP.DrawElements3D(MenuFunctions.Struct.getMesh(), MenuFunctions.SelectedElems,
			showMatColor, showSecColor, showElemContour, showDeformedStructure, MenuFunctions.DiagramScales[1], canvas);
		}
		if (MenuFunctions.ShowNodes && MenuFunctions.Struct.getMesh() != null && MenuFunctions.Struct.getMesh().getNodes() != null)
		{
			DP.DrawNodes3D(MenuFunctions.Struct.getMesh().getNodes(), MenuFunctions.SelectedNodes, Node.color, showDeformedStructure,
			MenuFunctions.Struct.getMesh().getElements().get(0).getDOFs(), MenuFunctions.DiagramScales[1], canvas);
		}
		if (MenuFunctions.AnalysisIsComplete)
		{
			DrawResults(MainCanvas, MenuFunctions.Struct, MenuFunctions.SelectedElems, MenuFunctions.SelectedVar,
			MenuFunctions.ShowElemContour, showDeformedStructure,
			MenuFunctions.DiagramScales, MenuFunctions.ShowDisplacementContour, MenuFunctions.ShowStressContour,
			MenuFunctions.ShowStrainContour, MenuFunctions.ShowInternalForces,
			MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, DP);
			if (MenuFunctions.ShowReactionArrows & MenuFunctions.Reaction != null)
			{
				DP.DrawReactions3D(MenuFunctions.Struct.getMesh().getNodes(), MenuFunctions.Reaction,
				MenuFunctions.Struct.getMesh().getElements().get(0).getDOFs(), MenuFunctions.ShowReactionValues,
				Reactions.color, MenuFunctions.ShowDeformedStructure, MenuFunctions.DiagramScales[1], canvas);
			}
		}
		if (MenuFunctions.ShowSup & MenuFunctions.Sup != null)
		{
			DP.DrawSup3D(MenuFunctions.Struct.getMesh().getNodes(), MenuFunctions.Sup, Supports.color, canvas);
		}
		if (MenuFunctions.ShowConcLoads & MenuFunctions.ConcLoad != null)
		{
			DP.DrawConcLoads3D(MenuFunctions.Struct.getMesh().getNodes(), MenuFunctions.ConcLoad,
			MenuFunctions.Struct.getMesh().getElements().get(0).getDOFs(), MenuFunctions.ShowLoadsValues,
			ConcLoads.color, showDeformedStructure, MenuFunctions.DiagramScales[1], canvas);
		}
		if (MenuFunctions.ShowDistLoads & MenuFunctions.DistLoad != null)
		{
			DP.DrawDistLoads3D(MenuFunctions.Struct.getMesh(), MenuFunctions.DistLoad, MenuFunctions.ShowLoadsValues, DistLoads.color,
			showDeformedStructure, MenuFunctions.Struct.getMesh().getElements().get(0).getDOFs(), MenuFunctions.DiagramScales[1], canvas);
		}
		if (MenuFunctions.ShowNodalDisps & MenuFunctions.NodalDisp != null)
		{
			DP.DrawNodalDisps3D(MenuFunctions.Struct.getMesh().getNodes(), MenuFunctions.NodalDisp, MenuFunctions.Struct.getMesh().getElements().get(0).getDOFs(), MenuFunctions.ShowLoadsValues,
			NodalDisps.color, showDeformedStructure, MenuFunctions.DiagramScales[1]);
		}
		if (MenuFunctions.ShowDOFNumber && MenuFunctions.Struct.getMesh() != null && MenuFunctions.Struct.getMesh().getNodes() != null)
		{
			DP.DrawDOFNumbers(MenuFunctions.Struct.getMesh().getNodes(), Node.color, showDeformedStructure, canvas);
		}
		if (MenuFunctions.ShowNodeNumber && MenuFunctions.Struct.getMesh() != null && MenuFunctions.Struct.getMesh().getNodes() != null)
		{
			DP.DrawNodeNumbers(MenuFunctions.Struct.getMesh().getNodes(), Node.color, showDeformedStructure, canvas);
		}
		if (MenuFunctions.ShowElemNumber && MenuFunctions.Struct.getMesh() != null &&  MenuFunctions.Struct.getMesh().getElements() != null)
		{
			DP.DrawElemNumbers(MenuFunctions.Struct.getMesh(), Node.color, showDeformedStructure, canvas);
		}
		if (showNodeSelectionWindow)
		{
			DP.DrawSelectionWindow(nodeSelectionWindowInitialPos, MenuFunctions.MousePos);
		}
		if (MenuFunctions.ShowElemSelectionWindow)
		{
			DP.DrawSelectionWindow(MenuFunctions.ElemSelectionWindowInitialPos, MenuFunctions.MousePos);
		}
		if (MenuFunctions.ShowElemDetails)
		{
			Mesh.DrawElemDetails(ElemType.valueOf(MenuFunctions.SelectedElemType.toUpperCase()), canvas, DP);
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
	

	public void NodeAddition(MyCanvas MainCanvas, int[] MainPanelPos)
	{
		if (MenuFunctions.Struct.getMesh().getNodes() != null)
		{
			MenuFunctions.SelectedNodes = null;
			MenuFunctions.SelectedNodes = Util.NodesSelection(MainCanvas, MenuFunctions.Struct.getCenter().asArray(), MenuFunctions.Struct.getMesh().getNodes(), MenuFunctions.MousePos,
			MainPanelPos, MenuFunctions.SelectedNodes,
			nodeSelectionWindowInitialPos, MenuFunctions.Struct.getMesh().getElements().get(0).getDOFs(), MenuFunctions.DiagramScales, showNodeSelectionWindow, showDeformedStructure);
			
			int NodeMouseIsOn = Util.NodeMouseIsOn(MenuFunctions.Struct.getMesh().getNodes(), MenuFunctions.MousePos, MainCanvas.getPos(), MainCanvas.getSize(), MainCanvas.getDimension(),
			MainCanvas.getDrawingPos(), 4, MenuFunctions.ShowDeformedStructure);

			if (NodeMouseIsOn == -1 | (showNodeSelectionWindow & -1 < NodeMouseIsOn))
			{
				showNodeSelectionWindow = !showNodeSelectionWindow;
				nodeSelectionWindowInitialPos = MenuFunctions.MousePos;
			}
		}
	}

	public static void CreateStructureOnClick(StructureShape structureShape)
	{
		MenuFunctions.Struct.setShape(structureShape);
		MenuFunctions.StructureCreationIsOn = !MenuFunctions.StructureCreationIsOn;
	}


	public static void setElemType(String ElemType)
	{
		MenuFunctions.SelectedElemType = ElemType;
	}


	
 	public static void AddMaterialToElements(int[] Elems, Material mat)
	{
		if (MenuFunctions.Struct.getMesh().getElements() != null & Elems != null & mat != null)
		{
			for (int i = 0; i <= Elems.length - 1; i += 1)
			{
				if (-1 < Elems[i])
				{
					MenuFunctions.Struct.getMesh().getElements().get(Elems[i]).setMat(mat);
				}
			}
			MenuFunctions.SelectedElems = null;
		}
	}

	public static void AddSectionsToElements(int[] Elems, Section sec)
	{
		if (MenuFunctions.Struct.getMesh().getElements() != null & sec != null & Elems != null)
		{
			for (int i = 0; i <= Elems.length - 1; i += 1)
			{
				if (-1 < Elems[i])
				{
					MenuFunctions.Struct.getMesh().getElements().get(Elems[i]).setSec(sec);
				}
			}
			MenuFunctions.SelectedElems = null;
		}
	}
	
	public static void AddSupports()
	{
		if (-1 < MenuFunctions.SelectedSup & MenuFunctions.SelectedNodes != null & MenuFunctions.SupType != null )
		{
			MenuFunctions.Sup = Util.IncreaseArraySize(MenuFunctions.Sup, MenuFunctions.SelectedNodes.length);
			for (int i = 0; i <= MenuFunctions.SelectedNodes.length - 1; i += 1)
			{
				int supid = MenuFunctions.Sup.length - MenuFunctions.SelectedNodes.length + i;
				if (-1 < MenuFunctions.SelectedNodes[i])
				{
					MenuFunctions.Sup[supid] = new Supports(supid, MenuFunctions.SelectedNodes[i], MenuFunctions.SupType[MenuFunctions.SelectedSup]);
					MenuFunctions.Struct.getMesh().getNodes().get(MenuFunctions.SelectedNodes[i]).setSup(MenuFunctions.SupType[MenuFunctions.SelectedSup]);
				}
			}
			MenuFunctions.ShowSup = true;
			MenuFunctions.SelectedNodes = null;
		}
	}
	
	public static void AddConcLoads()
	{
		if (-1 < MenuFunctions.SelectedConcLoad & MenuFunctions.SelectedNodes != null & MenuFunctions.ConcLoadType != null)
		{
			MenuFunctions.ConcLoad = Util.IncreaseArraySize(MenuFunctions.ConcLoad, MenuFunctions.SelectedNodes.length);
			for (int i = 0; i <= MenuFunctions.SelectedNodes.length - 1; i += 1)
			{
				int loadid = MenuFunctions.ConcLoad.length - MenuFunctions.SelectedNodes.length + i;
				if (-1 < MenuFunctions.SelectedNodes[i])
				{
					MenuFunctions.ConcLoad[loadid] = new ConcLoads(loadid, MenuFunctions.SelectedNodes[i], MenuFunctions.ConcLoadType[MenuFunctions.SelectedConcLoad]);
					MenuFunctions.Struct.getMesh().getNodes().get(MenuFunctions.SelectedNodes[i]).AddConcLoads(MenuFunctions.ConcLoad[loadid]);
				}
			}
			MenuFunctions.ShowConcLoads = true;
			MenuFunctions.SelectedNodes = null;
		}
	}
	
	public static void AddDistLoads()
	{
		if (-1 < MenuFunctions.SelectedDistLoad & MenuFunctions.SelectedElems != null & MenuFunctions.DistLoadType != null)
		{
			MenuFunctions.DistLoad = Util.IncreaseArraySize(MenuFunctions.DistLoad, MenuFunctions.SelectedElems.length);
			for (int i = 0; i <= MenuFunctions.SelectedElems.length - 1; i += 1)
			{
				int loadid = MenuFunctions.DistLoad.length - MenuFunctions.SelectedElems.length + i;
				if (-1 < MenuFunctions.SelectedElems[i])
				{
					int elem = MenuFunctions.SelectedElems[i];
					int LoadType = (int) MenuFunctions.DistLoadType[MenuFunctions.SelectedDistLoad][0];
					double Intensity = MenuFunctions.DistLoadType[MenuFunctions.SelectedDistLoad][1];
					MenuFunctions.DistLoad[loadid] = new DistLoads(loadid, MenuFunctions.SelectedElems[i], LoadType, Intensity);
					MenuFunctions.Struct.getMesh().getElements().get(elem).setDistLoads(Util.AddElem(MenuFunctions.Struct.getMesh().getElements().get(elem).getDistLoads(), MenuFunctions.DistLoad[loadid]));
				}
			}
			MenuFunctions.ShowDistLoads = true;
			MenuFunctions.SelectedElems = null;
		}
	}
	
	public static void AddNodalDisps()
	{
		if (-1 < MenuFunctions.SelectedNodalDisp & MenuFunctions.SelectedNodes != null & MenuFunctions.NodalDispType != null)
		{
			MenuFunctions.NodalDisp = Util.IncreaseArraySize(MenuFunctions.NodalDisp, MenuFunctions.SelectedNodes.length);
			for (int i = 0; i <= MenuFunctions.SelectedNodes.length - 1; i += 1)
			{
				int dispid = MenuFunctions.NodalDisp.length - MenuFunctions.SelectedNodes.length + i;
				if (-1 < MenuFunctions.SelectedNodes[i])
				{
					MenuFunctions.NodalDisp[dispid] = new NodalDisps(dispid, MenuFunctions.SelectedNodes[i], MenuFunctions.NodalDispType[MenuFunctions.SelectedNodalDisp]);
					MenuFunctions.Struct.getMesh().getNodes().get(MenuFunctions.SelectedNodes[i]).AddNodalDisps(MenuFunctions.NodalDisp[dispid]);
				}
			}
			MenuFunctions.ShowNodalDisps = true;
			MenuFunctions.SelectedNodes = null;
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
		MenuFunctions.SelectedMat = 0;
	}
	
	public static void StructureMenuAssignSections()
	{
		MenuFunctions.ElemSelectionIsOn = !MenuFunctions.ElemSelectionIsOn;
		MenuFunctions.SelectedSec = 0;
	}
	
	public static void StructureMenuAssignSupports()
	{
		MenuFunctions.NodeSelectionIsOn = !MenuFunctions.NodeSelectionIsOn;
		MenuFunctions.SelectedSup = 0;
	}
	
	public static void StructureMenuAssignConcLoads()
	{
		MenuFunctions.NodeSelectionIsOn = !MenuFunctions.NodeSelectionIsOn;
		MenuFunctions.SelectedConcLoad = 0;
	}
	
	public static void StructureMenuAssignDistLoads()
	{
		MenuFunctions.ElemSelectionIsOn = !MenuFunctions.ElemSelectionIsOn;
		MenuFunctions.SelectedDistLoad = 0;
	}
	
	public static void StructureMenuAssignNodalDisps()
	{
		MenuFunctions.NodeSelectionIsOn = !MenuFunctions.NodeSelectionIsOn;
		MenuFunctions.SelectedNodalDisp = 0;
	}
	
	public static Object[] CreatureStructure(List<Point3D> StructCoords, MeshType meshType, int[] MeshSizes, ElemType elemType,
			Material CurrentMatType, List<Material> matTypes, Section CurrentSecType, List<Section> secTypes,
			int SupConfig, int SelConcLoad, int SelDistLoad, int ConcLoadConfig, int DistLoadConfig)
	{
		/* Tipo de elemento, materiais, seçõs, apoios e cargas jâ estâo definidos */
		
		/* 1. Criar polâgono */
		MenuFunctions.Struct = new Structure("Especial", StructureShape.rectangular, StructCoords);
		
		/* 2. Criar malha */		
		MenuFunctions.Sup = null;
		MenuFunctions.ConcLoad = null;
		MenuFunctions.DistLoad = null;
		MenuFunctions.NodalDisp = null;
		MenuFunctions.Struct.createMesh(meshType, new int[][] {MeshSizes}, elemType);
		
		/* 3. Atribuir materiais */
		for (int elem = 0; elem <= MenuFunctions.Struct.getMesh().getElements().size() - 1; elem += 1)
		{
			MenuFunctions.SelectedElems = Util.AddElem(MenuFunctions.SelectedElems, elem);
		}
		AddMaterialToElements(MenuFunctions.SelectedElems, CurrentMatType);
		Element.createMatColors(matTypes);
		for (Element elem : MenuFunctions.Struct.getMesh().getElements())
		{
			int matColorID = matTypes.indexOf(elem.getMat()) ;
			elem.setMatColor(Element.matColors[matColorID]);
		}

		/* 4. Atribuir seçõs */
		for (int elem = 0; elem <= MenuFunctions.Struct.getMesh().getElements().size() - 1; elem += 1)
		{
			MenuFunctions.SelectedElems = Util.AddElem(MenuFunctions.SelectedElems, elem);
		}
		AddSectionsToElements(MenuFunctions.SelectedElems, CurrentSecType);
		Element.setSecColors(secTypes);
		for (Element elem : MenuFunctions.Struct.getMesh().getElements())
		{
			int secID = secTypes.indexOf(elem.getSec()) ;
			elem.setSecColor(Element.SecColors[secID]);
		}
		
		/* 5. Atribuir apoios */
		MenuFunctions.Sup = Util.AddEspecialSupports(MenuFunctions.Struct.getMesh().getNodes(), Element.typeToShape(elemType), meshType, new int[] {MeshSizes[0], MeshSizes[1]}, SupConfig);

		/* 6. Atribuir cargas */
		if (ConcLoadConfig == 1)
		{
			if (MenuFunctions.Struct.getMesh().getElements().get(0).getShape().equals(ElemShape.rectangular))
			{
				MenuFunctions.SelectedNodes = Util.AddElem(MenuFunctions.SelectedNodes, (MeshSizes[1] / 2 * (MeshSizes[0] + 1) + MeshSizes[0] / 2));
			}
			else if (MenuFunctions.Struct.getMesh().getElements().get(0).getShape().equals(ElemShape.r8))
			{
				MenuFunctions.SelectedNodes = Util.AddElem(MenuFunctions.SelectedNodes, (MeshSizes[1] / 2 * (2 * MeshSizes[0] + 1 + MeshSizes[0] + 1) + MeshSizes[0]));
			}
		}
		MenuFunctions.SelectedConcLoad = SelConcLoad;
		AddConcLoads();
		MenuFunctions.SelectedDistLoad = SelDistLoad;
		if (-1 < SelDistLoad)
		{
			MenuFunctions.SelectedElems = null;
			for (int elem = 0; elem <= MenuFunctions.Struct.getMesh().getElements().size() - 1; elem += 1)
			{
				MenuFunctions.SelectedElems = Util.AddElem(MenuFunctions.SelectedElems, elem);
			}
			AddDistLoads();
		}
		
		/* 7. Calcular parâmetros para a anâlise */
		MenuFunctions.CalcAnalysisParameters();
		
		MenuFunctions.SelectedNodes = null;
		MenuFunctions.SelectedElems = null;
		return new Object[] {MenuFunctions.Struct.getMesh().getNodes(), MenuFunctions.Struct.getMesh().getElements(), MenuFunctions.Sup, MenuFunctions.ConcLoad, MenuFunctions.DistLoad, null};
	}

	@Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        MenuFunctions.updateMousePosRelToPanelPos(panelPos) ;
        DP.setG(g);
        DP.setRealStructCenter(MenuFunctions.Struct.getCenter());
		this.displayContent(initialSize, panelPos, canvas.getCenter(), canvas, DP);
		if (MenuFunctions.ShowMousePos)
		{
			double[] mousePos = Util.ConvertToRealCoords(MenuFunctions.MousePos, new int[] {canvas.getPos().x, canvas.getPos().y}, canvas.getSize(), canvas.getDimension());
			mouseXPosTextArea.setText(String.valueOf(Util.Round(mousePos[0], 3)));
			mouseYPosTextArea.setText(String.valueOf(Util.Round(mousePos[1], 3)));
		}
		repaint();
    }

}
