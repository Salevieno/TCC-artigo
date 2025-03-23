package org.example.output;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.example.mainTCC.Analysis;
import org.example.structure.ElemShape;
import org.example.structure.Element;
import org.example.structure.Mesh;
import org.example.structure.Node;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;
import org.example.utilidades.Util;
import org.example.view.MainPanel;

import graphics.DrawPrimitives;

public class ResultDiagrams
{

	private double scale ;
    private Diagram diagram ;
    private int selectedVar ;
	private ColorSystem colorSystem ;
    

    public ResultDiagrams(double scale, Diagram diagram, int selectedVar, ColorSystem colorSystem)
    {
        this.scale = scale;
        this.diagram = diagram;
        this.selectedVar = selectedVar;
        this.colorSystem = colorSystem;
    }

    public ResultDiagrams(Diagram diagram, int selectedVar)
    {
        this(1.0, diagram, selectedVar, ColorSystem.redToGreen) ;
    }

    public ResultDiagrams()
    {
        this(1.0, null, -1, ColorSystem.redToGreen) ;
    }


	public void display(MyCanvas canvas, Mesh mesh, Results results, DrawPrimitives DP)
	{
		displayContours(canvas, mesh, results, null, selectedVar, false, true, scale, diagram, colorSystem, DP) ;
	}

    private static String diagramTitle(Diagram diagram, double scale)
    {
        double roundedScale = Util.Round(scale, 3) ;
        switch (diagram)
        {
            case displacements:
                return "Deslocamentos (x " + roundedScale + ")" ;
        
            case strains:
                return "Deformações (x " + roundedScale + ")" ;
            
            case stresses:
                return "Tensões (x " + roundedScale + ")" ;
        
            case internalForces:
                return "Forças internas (x " + roundedScale + ")" ;

            default: System.out.println("Warn: No diagram selected at diagramTitle") ; return "" ;
        }
    }

    
	public static void displayContours(MyCanvas canvas, Mesh mesh, Results results, int[] SelectedElems, int selectedvar, boolean ShowElemContour, boolean showDeformed, double scale, Diagram diagram, ColorSystem colorSystem, DrawPrimitives DP)
	{
		List<Node> nodes = mesh.getNodes();
		List<Element> elems = mesh.getElements();

		if (selectedvar <= -1 || nodes == null || elems == null) { return ;}
        
        canvas.setTitle(diagramTitle(diagram, scale)) ;
        double minValue = results.getMin(diagram, selectedvar) ;
        double maxValue = results.getMax(diagram, selectedvar) ;
        displayContour(elems, nodes, SelectedElems, ShowElemContour, showDeformed, scale, minValue, maxValue, diagram, selectedvar, colorSystem, canvas, DP) ;
	}


	private static void displayContour(List<Element> Elem, List<Node> nodes, int[] SelectedElems, boolean showelemcontour, boolean condition,
			double Defscale, double minvalue, double maxvalue, Diagram diagram, int selecteddof, ColorSystem ColorSystem, MyCanvas canvas, DrawPrimitives DP)
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
                        switch (diagram)
                        {
                            case displacements:
                                double[] disp = Elem.get(elem).getDisp();
                                ContourValue[point] = Analysis.DispOnPoint(nodes, Elem.get(elem), e, n, selecteddof, disp);
                                
                                break;
                        
                            case strains:
                                double[] strain = Elem.get(elem).getStrain();
                                ContourValue[point] = Analysis.StrainOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, strain);
                            
                                break;

                            case stresses:
                                double[] stress = Elem.get(elem).getStress();
                                ContourValue[point] = Analysis.StressOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, stress);
                                
                                break;

                            case internalForces:
                                double[] force = Elem.get(elem).getIntForces();
                                ContourValue[point] = Analysis.ForceOnElemContour(nodes, Elem.get(elem), e, n, selecteddof, force);
                                
                                break;

                            default: break;
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
			int[] xCoords = new int[ContourCoords.length] ;
            int[] yCoords = new int[ContourCoords.length];
			double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
			Color[] colors = new Color[ContourCoords.length];
			Arrays.fill(colors, new Color(0, 100, 55));
			
			for (int point = 0; point <= ContourCoords.length - 1; point += 1)
			{
				DrawingCoords[point] = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(ContourCoords[point], Center, canvas.getAngles()), MainPanel.structure.getCenter(), canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
				xCoords[point] = DrawingCoords[point][0];
				yCoords[point] = DrawingCoords[point][1];
				colors[point] = Util.FindColor(ContourValue[point], minvalue, maxvalue, ColorSystem);
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


}
