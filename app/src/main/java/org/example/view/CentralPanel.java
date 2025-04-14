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
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;

import org.example.Main;
import org.example.loading.ConcLoad;
import org.example.loading.DistLoad;
import org.example.loading.Force;
import org.example.loading.Loading;
import org.example.loading.NodalDisp;
import org.example.mainTCC.MainPanel;
import org.example.mainTCC.MenuFunctions;
import org.example.service.MenuViewService;
import org.example.structure.ElemType;
import org.example.structure.Element;
import org.example.structure.Material;
import org.example.structure.Node;
import org.example.structure.Reactions;
import org.example.structure.Structure;
import org.example.structure.StructureShape;
import org.example.structure.Supports;
import org.example.userInterface.Draw;
import org.example.userInterface.MenuBar;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Point3D;
import org.example.utilidades.Util;

import graphics.Align;
import graphics.DrawPrimitives ;

public class CentralPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private static final Dimension initialSize = new Dimension(582, 610) ;
	private static final Color bgColor = Main.palette[2] ;
	private static final DrawPrimitives DP = new DrawPrimitives() ;
	
	private final MyCanvas canvas ;
	private final int[] panelPos ;

	private static boolean structureCreationIsOn ;
	private static boolean nodeSelectionIsActive ;
	private static boolean elemSelectionIsActive ;

	private SelectionWindow selectionWindow ;
	private MenuViewService view = MenuViewService.getInstance() ;

	public static Structure structure ;
	public static Loading loading ;

	public CentralPanel(Point frameTopLeftPos)
	{		
		Point screenTopLeft = new Point() ;
		Dimension size = new Dimension((int) (0.4 * initialSize.width), (int) (0.8 * initialSize.height)) ;
		canvas = new MyCanvas(new Point(575, 25), size, new Point2D.Double(10, 10), screenTopLeft);
		panelPos = new int[] {frameTopLeftPos.x + 7 * NorthPanel.stdButtonSize.width + 8, frameTopLeftPos.y + 76 + 8, 0};
		
		structure = new Structure(null, null, null);
		loading = new Loading() ;
		
		selectionWindow = new SelectionWindow() ;

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
			structure.getMesh().unselectAllNodes() ;
			structure.getMesh().unselectAllElements() ;
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

	public static void createStructure(int[] MainPanelPos, MyCanvas canvas, Point mousePos)
	{		   
		if (!Util.MouseIsInside(mousePos, new int[2], canvas.getPos(), canvas.getSize())) { return ;}
		
		boolean snipToGridIsActive = MainPanel.getInstance().getNorthPanel().getUpperToolbar().getButtonMagnetState() ;
		Point3D newCoord ;

		switch(structure.getShape())
		{
			case rectangular, circular:

				if (structure.getCoords() != null && !structure.getCoords().isEmpty())
				{
					finishStructureCreation() ;
				}
				newCoord = new Point3D(canvas.getCoordFromMouseClick(mousePos, snipToGridIsActive)) ;
				structure.addCoordFromMouseClick(newCoord) ;

				return ;

			case polygonal:

				int precision = 10;
				if (structure.getCoords() != null && !structure.getCoords().isEmpty())
				{
					if (mousePos.distance(canvas.inDrawingCoords(structure.getCoords().get(0))) < precision)
					{
						finishStructureCreation() ;
					}
				}
				newCoord = new Point3D(canvas.getCoordFromMouseClick(mousePos, snipToGridIsActive)) ;
				structure.addCoordFromMouseClick(newCoord) ;

				return ;

			default: 
				System.out.println("Structure shape not identified at Menus -> CreateStructure") ;
				return ;
		}
	}

	public void updateDrawings()
	{
		structure.updateDrawings(canvas) ;
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
				DP.drawCircle(topLeftCorner, (int)(2 * MousePos.distance(topLeftCorner)), color);
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

	public void display(Structure structure, int[] MainPanelPos, DrawPrimitives DP)
	{
		canvas.setPos(new Point((int) (0.1 * initialSize.width), (int) (0.1 * initialSize.height)));
		canvas.setSize(new Dimension((int) (0.8 * initialSize.width), (int) (0.8 * initialSize.height))) ;
		canvas.setCenter(new Point(canvas.getPos().x + canvas.getSize().width / 2, canvas.getPos().y + canvas.getSize().height / 2)) ;
		canvas.display(DP);
		// if (structure != null && structure.getCoords() != null && structure.getCenter() != null)
		// {
		// 	structure.displayShape(canvas, DP) ;
		// }

		if (structure != null)
		{
			structure.display(canvas, view.matColor, view.secColor, view.elemContour, view.deformedStructure, DP) ;
		}

		canvas.drawCenter(DP) ;
		if (structureCreationIsOn && structure != null && structure.getCoords() != null)
		{
			drawStructureCreationWindow(structure.getCoords(), MenuFunctions.mousePos, 2, structure.getShape(), Main.palette[6]);
		}
		if (view.elems && structure != null && structure.getMesh() != null && structure.getMesh().getElements() != null)
		{
			if (view.deformedStructure)
			{
				canvas.setTitle("Estrutura deformada (x " + String.valueOf(Util.Round(MenuFunctions.DiagramScales[1], 3)) + ")");
			}
			// structure.displayMesh(canvas, MenuFunctions.DiagramScales[1], showMatColor, showSecColor, showElemContour, showDeformedStructure, DP) ;
		}
		if (view.nodes && structure != null && structure.getMesh() != null && structure.getMesh().getNodes() != null)
		{
			// DP.DrawNodes3D(structure.getMesh().getNodes(), MenuFunctions.selectedNodes, Node.color, showDeformedStructure,
			// structure.getMesh().getElements().get(0).getDOFs(), MenuFunctions.DiagramScales[1], canvas);
		}
		if (MenuFunctions.AnalysisIsComplete)
		{
			// DrawResults(canvas, structure, MenuFunctions.SelectedElems, SelectedVar,
			// view.ElemContour, showDeformedStructure,
			// MenuFunctions.DiagramScales, ShowDisplacementContour, ShowStressContour, ShowStrainContour, ShowInternalForces,
			// MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo);

			// Results.displayContours(canvas, structure, MenuFunctions.SelectedElems, SelectedVar,
			// 						view.ElemContour, showDeformedStructure,
			// 						MenuFunctions.DiagramScales, ShowDisplacementContour, ShowStressContour, ShowStrainContour, ShowInternalForces,
			// 						MenuFunctions.NonlinearMat, MenuFunctions.NonlinearGeo, DP);
			structure.displayDiagrams(canvas, DP) ;
			
			if (view.reactionArrows && structure != null && structure.getReactions() != null)
			{
				// DP.DrawReactions3D(structure.getMesh().getNodes(), structure.getReactions(),
				// 	structure.getMesh().getElements().get(0).getDOFs(), view.ReactionValues,
				// 	Reactions.color, view.DeformedStructure, MenuFunctions.DiagramScales[1], canvas, DPP);

				Reactions.display3D(structure.getMesh().getNodes(), structure.getReactions(),
						structure.getMesh().getElements().get(0).getDOFs(), view.reactionValues,
						Reactions.color, view.deformedStructure, MenuFunctions.DiagramScales[1], canvas, DP);
			}
		}
		if (view.sup && structure != null && structure.getSupports() != null)
		{
			structure.displaySupports(canvas, DP) ;
		}
		if (view.concLoads && loading != null && loading.getConcLoads() != null)
		{
			structure.displayConcLoads(canvas, view.deformedStructure, DP) ;
		}
		if (view.distLoads && loading != null && loading.getDistLoads() != null)
		{
			Draw.DrawDistLoads3D(structure.getMesh(), view.loadsValues,
							view.deformedStructure, structure.getMesh().getElements().get(0).getDOFs(),
							MenuFunctions.DiagramScales[1], canvas, DP);
		}
		if (view.nodalDisps && loading != null && loading.getNodalDisps() != null)
		{
			Draw.DrawNodalDisps3D(structure.getMesh().getNodes(), loading.getNodalDisps(),
							structure.getMesh().getElements().get(0).getDOFs(), view.loadsValues,
							NodalDisp.color, view.deformedStructure, MenuFunctions.DiagramScales[1]);
		}
		if (view.DOFNumber && structure != null && structure.getMesh() != null && structure.getMesh().getNodes() != null)
		{
			Draw.DrawDOFNumbers(structure.getMesh().getNodes(), Node.color, view.deformedStructure, canvas, DP);
		}
		if (view.nodeNumber && structure != null && structure.getMesh() != null && structure.getMesh().getNodes() != null)
		{
			structure.getMesh().getNodes().forEach(node -> node.displayNumber(canvas, view.deformedStructure, DP)) ;
		}
		if (view.elemNumber && structure != null && structure.getMesh() != null &&  structure.getMesh().getElements() != null)
		{
			// DP.DrawElemNumbers(structure.getMesh(), Node.color, view.deformedStructure, canvas);
			structure.getMesh().displayElemNumbers(structure.getMesh(), Node.color, view.deformedStructure, canvas, DP) ;
		}
		if (view.elemDetails && MenuFunctions.SelectedElemType != null)
		{
			Element.displayTypeDetails(ElemType.valueOf(MenuFunctions.SelectedElemType.toUpperCase()), canvas, DP);
		}

		if (selectionWindow != null && selectionWindow.isActive())
		{
			selectionWindow.display(MenuFunctions.mousePos, DP) ;
		}

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


	// public static void DrawContours3D(List<Element> Elem, List<Node> nodes, int[] SelectedElems, boolean showelemcontour, boolean condition,
	// 		double Defscale, double minvalue, double maxvalue, String ResultType, int selecteddof, boolean NonlinearMat, boolean NonlinearGeo, ColorSystem colorSystem,
	// 		MyCanvas canvas, DrawPrimitives DP)
	// {
	// 	int Ninterpoints = 0;
	// 	for (int elem = 0; elem <= Elem.size() - 1; elem += 1)
	// 	{
	// 		/* Get edge nodes and coordinates*/
	// 		int[] EdgeNodes = Elem.get(elem).getExternalNodes();
	// 		double[][] EdgeCoords = new double[EdgeNodes.length][3];
	// 		for (int node = 0; node <= EdgeNodes.length - 1; node += 1)
	// 		{
	// 			if (condition)
	// 			{
	// 				EdgeCoords[node] = Util.ScaledDefCoords(nodes.get(EdgeNodes[node]).getOriginalCoords(), nodes.get(EdgeNodes[node]).getDisp(), nodes.get(node).getDOFType(), Defscale);
	// 			}
	// 			else
	// 			{
	// 				EdgeCoords[node] = Util.GetNodePos(nodes.get(EdgeNodes[node]), condition);
	// 			}
	// 		}
			
	// 		/* Get contour coordinates */
	// 		double[][] ContourCoords = new double[EdgeNodes.length * (1 + Ninterpoints)][3];
	// 		for (int node = 0; node <= EdgeNodes.length - 2; node += 1)
	// 		{
	// 			double[] Line = new double[] {EdgeCoords[node][0], EdgeCoords[node][1], EdgeCoords[node][2], EdgeCoords[node + 1][0], EdgeCoords[node + 1][1], EdgeCoords[node + 1][2]};
	// 			for (int i = 0; i <= Ninterpoints; i += 1)
	// 			{
	// 				double offset = i / (double)(Ninterpoints + 1);
	// 				double[] NewCoord = Util.CreatePointInLine(Line, offset);
	// 				ContourCoords[node * (Ninterpoints + 1) + i] = NewCoord;
	// 			}
	// 		}			
	// 		double[] Line = new double[] {EdgeCoords[EdgeNodes.length - 1][0], EdgeCoords[EdgeNodes.length - 1][1], EdgeCoords[EdgeNodes.length - 1][2], EdgeCoords[0][0], EdgeCoords[0][1], EdgeCoords[0][2]};
	// 		for (int i = 0; i <= Ninterpoints; i += 1)
	// 		{
	// 			double offset = i / (double)(Ninterpoints + 1);
	// 			double[] NewCoord = Util.CreatePointInLine(Line, offset);
	// 			ContourCoords[(EdgeNodes.length - 1) * (Ninterpoints + 1) + i] = NewCoord;
	// 		}

	// 		/* Get displacements on contour */
	// 		double[] ContourValue = new double[ContourCoords.length];
	// 		if (Elem.get(elem).getShape().equals(ElemShape.rectangular) | Elem.get(elem).getShape().equals(ElemShape.r8))
	// 		{
	// 			double L = 2 * Elem.get(elem).calcHalfSize(nodes)[0];
	// 			double H = 2 * Elem.get(elem).calcHalfSize(nodes)[1];
	// 			double[] CenterCoords = Elem.get(elem).getCenterCoords();
	// 			for (int point = 0; point <= ContourCoords.length - 1; point += 1)
	// 			{
	// 				double[] natCoords = Util.InNaturalCoordsRect(CenterCoords, L, H, ContourCoords[point]);
	// 				double e = natCoords[0];
	// 				double n = natCoords[1];
					
	// 				if (-1 < selecteddof)
	// 				{
	// 					if (ResultType.equals("Displacement"))
	// 					{
	// 						double[] disp = Elem.get(elem).getDisp();
	// 						ContourValue[point] = Analysis.DispOnPoint(nodes, Elem.get(elem), e, n, selecteddof, disp);
	// 					}
	// 					else if (ResultType.equals("Strain"))
	// 					{
	// 						double[] strain = Elem.get(elem).getStrain();
	// 						ContourValue[point] = Analysis.StrainOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, strain);
	// 					}
	// 					else if (ResultType.equals("Stress"))
	// 					{
	// 						double[] stress = Elem.get(elem).getStress();
	// 						ContourValue[point] = Analysis.StressOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, stress);
	// 					}
	// 					else if (ResultType.equals("Force"))
	// 					{
	// 						double[] force = Elem.get(elem).getIntForces();
	// 						ContourValue[point] = Analysis.ForceOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, force);
	// 					}
	// 				}
	// 				ContourCoords[point][2] = ContourValue[point] * Defscale;
	// 			}
	// 		}
	// 		else if (Elem.get(elem).getShape().equals(ElemShape.triangular))
	// 		{			
	// 			for (int point = 0; point <= ContourCoords.length - 1; point += 1)
	// 			{
	// 				double[] natCoords = Util.InNaturalCoordsTriangle(EdgeCoords, ContourCoords[point]);
	// 				double[] u = Elem.get(elem).getDisp();
	// 				ContourValue[point] = Analysis.DispOnPoint(nodes, Elem.get(elem), natCoords[0], natCoords[1], selecteddof, u);
	// 			}
	// 		}

	// 		/* Draw the contour */
	// 		int[][] DrawingCoords = new int[ContourCoords.length][3];
	// 		int[] xCoords = new int[ContourCoords.length], yCoords = new int[ContourCoords.length];
	// 		double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
	// 		Color[] colors = new Color[ContourCoords.length];
	// 		Arrays.fill(colors, new Color(0, 100, 55));
			
	// 		for (int point = 0; point <= ContourCoords.length - 1; point += 1)
	// 		{
	// 			DrawingCoords[point] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(ContourCoords[point], Center, canvas.getAngles()), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
	// 			xCoords[point] = DrawingCoords[point][0];
	// 			yCoords[point] = DrawingCoords[point][1];
	// 			colors[point] = Util.FindColor(ContourValue[point], minvalue, maxvalue, colorSystem);
	// 		}
			
	// 		// DP.DrawGradPolygon(xCoords, yCoords, thick, false, true, Color.black, colors);
	// 		double equivalentDiameter = (Util.FindMax(xCoords) - Util.FindMin(xCoords) + Util.FindMax(yCoords) - Util.FindMin(yCoords)) / 2.0 ;
	// 		Color avrColor = Util.AverageColor(colors) ;
	// 		DP.drawGradPolygon(xCoords, yCoords, equivalentDiameter, avrColor, colors) ;
	// 		if (showelemcontour)
	// 		{
	// 			// DrawPolygon(xCoords, yCoords, thick, true, false, Color.black, null);
	// 			DP.drawPolygon(xCoords, yCoords, Main.palette[0]) ;
	// 		}
	// 		if (SelectedElems != null)
	// 		{
	// 			for (int i = 0; i <= SelectedElems.length - 1; i += 1)
	// 			{
	// 				if (elem == SelectedElems[i])
	// 				{
	// 					// DrawPolygon(xCoords, yCoords, thick, false, true, Color.black, Color.red);
	// 					DP.drawPolygon(xCoords, yCoords, Main.palette[4]) ;
	// 				}
	// 			}
	// 		}		
	// 	}
		
	// }

	public static void startStructureCreation(StructureShape structureShape)
	{
		CentralPanel.structure.setShape(structureShape) ;
		structureCreationIsOn = true ;
	}

	private static void finishStructureCreation()
	{		
		structureCreationIsOn = false;
		structure.updateCenter() ;
		MainPanel.getInstance().getWestPanel().getInstructionsPanel().updateStepsCompletion(CentralPanel.structure, CentralPanel.loading) ;
		MenuBar.getInstance().updateEnabledMenus() ;
	}

	public static void activateNodeSelection() { nodeSelectionIsActive = true ;}
	public static void deactivateNodeSelection() { nodeSelectionIsActive = false ;}
	public static void activateElemSelection() { elemSelectionIsActive = true ;}
	public static void deactivateElemSelection() { elemSelectionIsActive = false ;}

	public void activateMaterialAssignment()
	{
		CentralPanel.elemSelectionIsActive = !CentralPanel.elemSelectionIsActive;
		MainPanel.getInstance().getWestPanel().getListsPanel().resetSelectedID() ;
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableMaterialAssignment() ;	
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableAssignmentButtons() ;
	}
	
	public void activateSectionAssignment()
	{
		CentralPanel.elemSelectionIsActive = !CentralPanel.elemSelectionIsActive;
		MainPanel.getInstance().getWestPanel().getListsPanel().resetSelectedID() ;
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableSectionAssignment() ;
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableAssignmentButtons() ;
	}
	
	public void activateSupportAssignment()
	{
		CentralPanel.nodeSelectionIsActive = !CentralPanel.nodeSelectionIsActive;
		MainPanel.getInstance().getWestPanel().getListsPanel().resetSelectedID() ;
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableSupportAssignment() ;
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableAssignmentButtons() ;
	}
	
	public void activateConcLoadAssignment()
	{
		CentralPanel.nodeSelectionIsActive = !CentralPanel.nodeSelectionIsActive;
		MainPanel.getInstance().getWestPanel().getListsPanel().resetSelectedID() ;
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableConcLoadAssignment() ;
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableAssignmentButtons() ;
	}
	
	public void activateDistLoadAssignment()
	{
		CentralPanel.elemSelectionIsActive = !CentralPanel.elemSelectionIsActive;
		MainPanel.getInstance().getWestPanel().getListsPanel().resetSelectedID() ;
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableDistLoadAssignment() ;
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableAssignmentButtons() ;
	}
	
	public void activateNodalDispAssignment()
	{
		CentralPanel.nodeSelectionIsActive = !CentralPanel.nodeSelectionIsActive;
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableNodalDispAssignment() ;
		MainPanel.getInstance().getWestPanel().getListsPanel().resetSelectedID() ;
		MainPanel.getInstance().getNorthPanel().getUpperToolbar().enableAssignmentButtons() ;
	}

	public static void setElemType(String ElemType)
	{
		MenuFunctions.SelectedElemType = ElemType;
	}

	private void handleSelectionWindow()
	{
		if (!nodeSelectionIsActive && !elemSelectionIsActive) { return ;}

		if (!selectionWindow.isActive())
		{
			selectionWindow.setTopLeft(MenuFunctions.mousePos) ;
			return ;
		}
		if (nodeSelectionIsActive)
		{
			selectionWindow.selectNodesInside(structure.getMesh(), canvas, MenuFunctions.mousePos) ;
		}
		if (elemSelectionIsActive)
		{
			selectionWindow.selectElementsInside(structure.getMesh(), canvas, MenuFunctions.mousePos) ;
		}
		MainPanel.getInstance().getEastPanel().reset() ;
	}

	
 	public static void AddMaterialToElements(List<Element> elems, Material mat)
	{
		if (mat == null || elems == null || elems.isEmpty()) { return ;}

		for (Element elem : elems)
		{
			if (elem.isSelected())
			{
				elem.setMat(mat) ;
			}
		}

		structure.getMesh().unselectAllElements() ;
	}

	
	public void AddSupports()
	{
		if (MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID() <= -1 || !structure.getMesh().hasNodesSelected() || MenuFunctions.SupType == null) { return ;}

		for (Node node : structure.getMesh().getSelectedNodes())
		{
			// int supid = MainPanel.structure.getSupports().size() - MenuFunctions.selectedNodes.size() + i;
			Supports newSupport = new Supports(1, node, MenuFunctions.SupType[MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID()]);
			CentralPanel.structure.addSupport(newSupport) ;
			node.setSup(MenuFunctions.SupType[MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID()]);
		}

		view.sup = true;
		structure.getMesh().unselectAllNodes() ;
	}
	
	public void AddConcLoads(Loading loading, List<Node> selectedNodes, List<Force> ConcLoadType)
	{
		if (-1 < MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID() && selectedNodes != null && ConcLoadType != null && 1 <= ConcLoadType.size())
		{
			for (int i = 0; i <= selectedNodes.size() - 1; i += 1)
			{
				if (-1 < selectedNodes.get(i).getID())
				{
					ConcLoad newConcLoad = new ConcLoad(ConcLoadType.get(MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID()));
					loading.getConcLoads().add(newConcLoad);
					selectedNodes.get(i).addConcLoad(newConcLoad);
				}
			}
			view.concLoads = true;
			selectedNodes = null;
		}
	}
	
	public void AddDistLoads(Structure structure, Loading loading, List<Element> selectedElems, double[][] DistLoadType)
	{
		if (-1 < MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID() && selectedElems != null && DistLoadType != null)
		{
			for (int i = 0; i <= selectedElems.size() - 1; i += 1)
			{
				Element elem = selectedElems.get(i);
				int LoadType = (int) DistLoadType[MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID()][0];
				double Intensity = DistLoadType[MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID()][1];
				DistLoad newDistLoad = new DistLoad(LoadType, Intensity) ;
				loading.getDistLoads().add(newDistLoad);
				elem.addDistLoad(newDistLoad) ;
				
			}
			view.distLoads = true;
			selectedElems = null;
		}
	}
	
	public void AddNodalDisps()
	{
		if (MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID() <= -1 || !structure.getMesh().hasNodesSelected() || MenuFunctions.NodalDispType == null ) { return ;}
		
		for (Node node : structure.getMesh().getSelectedNodes())
		{
			// int dispid = loading.getNodalDisps().size() - MenuFunctions.selectedNodes.size() + i;			
			NodalDisp newNodalDisp = new NodalDisp(1, node, MenuFunctions.NodalDispType[MainPanel.getInstance().getWestPanel().getListsPanel().getSelectedID()]) ;
			loading.getNodalDisps().add(newNodalDisp);
			node.addNodalDisp(newNodalDisp);
		}
		view.nodalDisps = true;
		structure.getMesh().unselectAllNodes() ;
	}
	public static void setConcLoadTypes(List<Force> ConcLoads)
	{
		MenuFunctions.concLoadTypes = ConcLoads;
	}

	public static void DefineDistLoadTypes(double[][] DistLoads)
	{
		MenuFunctions.DistLoadType = DistLoads;
	}

	public static void DefineNodalDispTypes(double[][] NodalDisps)
	{
		MenuFunctions.NodalDispType = NodalDisps;
	}




	private void handleMousePress(MouseEvent evt)
	{
		if (evt.getButton() == 1)	// Left click
		{
			if (structureCreationIsOn)
			{
				createStructure(panelPos, canvas, MenuFunctions.mousePos) ;
			}

			if (selectionWindow != null)
			{
				handleSelectionWindow() ;
			}

		}

		if (evt.getButton() == 3)	// Right click
		{
			// CentralPanel.structure.printStructure(matTypes, secTypes, CentralPanel.structure.getSupports(), loading);
		}
	}

	private void handleMouseWheel(MouseWheelEvent evt)
	{
		double qtdRotation = evt.getWheelRotation() ;
		MainPanel.getInstance().getWestPanel().getListsPanel().handleMouseWheel(evt) ;

		if (Util.MouseIsInside(MenuFunctions.mousePos, panelPos, canvas.getPos(), canvas.getSize().width, canvas.getSize().height))
		{
			// canvas.getDimension()[0] += Util.Round(0.2*Math.log10(canvas.getDimension()[0])*qtdRotation, 1);
			// canvas.getDimension()[1] += Util.Round(0.2*Math.log10(canvas.getDimension()[1])*qtdRotation, 1);
			if (structure.getResultDiagrams() != null)
			{
				structure.getResultDiagrams().incScale(Util.Round(Math.log10(structure.getResultDiagrams().getScale()) * qtdRotation, 1)) ;
			}
		}		

		MenuFunctions.updateDiagramScale(canvas, evt.getWheelRotation());
	}

	// public void activateSnipToClick() { snipToGridIsActive = true ;}
	// public void deactivateSnipToClick() { snipToGridIsActive = false ;}
	// public void setSnipToGridIsActive(boolean snipToGridIsActive) { this.snipToGridIsActive = snipToGridIsActive ;}

	@Override
    public void paintComponent(Graphics graphs) 
    {
        super.paintComponent(graphs);
        MenuFunctions.updateMousePosRelToPanelPos(panelPos) ;
		DP.setGraphics((Graphics2D) graphs) ;
		this.display(structure, panelPos, DP);
		repaint();
    }

}
