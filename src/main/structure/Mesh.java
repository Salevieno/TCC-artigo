package main.structure;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.gui.DrawingOnAPanel;
import main.gui.Menus;
import main.utilidades.Point3D;
import main.utilidades.Util;

public class Mesh
{
	private List<Node> nodes;
	private List<Element> elems;

    public Mesh(List<Node> node, List<Element> elem)
    {
        this.nodes = node;
        this.elems = elem;
    }

    
	public static void DrawElemDetails(ElemType elemType, MyCanvas canvas, DrawingOnAPanel DP)
	{
		Point3D RealStructCenter = new Point3D(5, 5, 0);
		double[] Center = Util.ConvertToRealCoordsPoint3D(canvas.getCenter(), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		ElemShape elemShape = Element.typeToShape(elemType);
		List<Node> nodes = new ArrayList<>();
		Element Elem = null;
		if (elemShape.equals(ElemShape.rectangular))
		{
			nodes.add(new Node(0, new Point3D(1, 1, 0))) ;
			nodes.add(new Node(1, new Point3D(9, 1, 0))) ;
			nodes.add(new Node(2, new Point3D(9, 9, 0))) ;
			nodes.add(new Node(3, new Point3D(1, 9, 0))) ;
			Elem = new Element(0, new int[] {0, 1, 2, 3}, elemType);
		}
		else if (elemShape.equals(ElemShape.quad))
		{
			nodes.add(new Node(0, new Point3D(1, 1, 0))) ;
			nodes.add(new Node(1, new Point3D(9, 3, 0))) ;
			nodes.add(new Node(2, new Point3D(7, 9, 0))) ;
			nodes.add(new Node(3, new Point3D(3, 7, 0))) ;
			Elem = new Element(0, new int[] {0, 1, 2, 3}, elemType);
		}
		else if (elemShape.equals(ElemShape.triangular))
		{
			nodes.add(new Node(0, new Point3D(1, 1, 0))) ;
			nodes.add(new Node(1, new Point3D(9, 5, 0))) ;
			nodes.add(new Node(2, new Point3D(1, 9, 0))) ;
			Elem = new Element(0, new int[] {0, 1, 2}, elemType);
		}
		else if (elemShape.equals(ElemShape.r8))
		{
			nodes.add(new Node(0, new Point3D(1, 1, 0))) ;
			nodes.add(new Node(1, new Point3D(9, 1, 0))) ;
			nodes.add(new Node(2, new Point3D(9, 9, 0))) ;
			nodes.add(new Node(3, new Point3D(1, 9, 0))) ;
			nodes.add(new Node(3, new Point3D(5, 1, 0))) ;
			nodes.add(new Node(3, new Point3D(5, 9, 0))) ;
			nodes.add(new Node(3, new Point3D(1, 5, 0))) ;
			nodes.add(new Node(3, new Point3D(9, 5, 0))) ;
			Elem = new Element(0, new int[] {0, 4, 1, 7, 2, 5, 3, 6}, elemType);
		}
		int[] DrawingStructCenter = Util.ConvertToDrawingCoords2Point3D(Util.RotateCoord(RealStructCenter.asArray(), Center, canvas.getAngles()), RealStructCenter, canvas.getPos(), canvas.getSize(), canvas.getDimension(), canvas.getCenter(), canvas.getDrawingPos());
		int textSize = 16;
		Color textColor = Menus.palette[8];
		
		for (int node = 0; node <= nodes.size() - 1; node += 1)
		{
			nodes.get(node).setDOFType(Elem.getDOFsPerNode()[node]);
		}
		Elem.setCumDOFs(Util.CumDOFsOnElem(nodes, Elem.getExternalNodes().length));
		for (int node = 0; node <= nodes.size() - 1; node += 1)
		{
			nodes.get(node).dofs = new int[Elem.getDOFsPerNode()[node].length];
			for (int dof = 0; dof <= Elem.getDOFsPerNode()[node].length - 1; dof += 1)
			{
				nodes.get(node).dofs[dof] = Elem.getCumDOFs()[node] + dof;
			}
		}
		DP.DrawNodes3D(nodes, null, Node.color, false, nodes.get(0).getDOFType(), 1, canvas);
		DP.DrawElements3D(new Mesh(nodes, List.of(Elem)), null, false, false, true, false, 1, canvas);
		DP.DrawDOFNumbers(nodes, Node.color, false, canvas);
		DP.DrawDOFSymbols(nodes, Node.color, false, canvas);
		DP.DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1] - (int) (1 * 1.5 * textSize)}, elemType.toString(), "Center", 0, "Bold", textSize, textColor);
		DP.DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1]}, "Graus de liberdade: " + Arrays.toString(Elem.getDOFs()), "Center", 0, "Bold", textSize, textColor);
		DP.DrawText(new int[] {DrawingStructCenter[0], DrawingStructCenter[1] + (int) (1 * 1.5 * textSize)}, "Deformaçõs: " + Arrays.toString(Elem.getStrainTypes()), "Center", 0, "Bold", textSize, textColor);
	}

    public void reset()
    {
        nodes = null ;
        elems = null ;
    }

    public List<Node> getNodes()  { return nodes ;}
    public List<Element> getElements() { return elems ;}

	private void printNodes()
	{
		System.out.println("\nNodes");
		System.out.println("ID	Original coords (m)	Displacements (m)	Sup ConcLoads (kN)	NodalDisps (m)");		
		nodes.forEach(System.out::println);
	}
	
	private void printElems()
	{
		System.out.println("\nElems");
		System.out.println("ID	Type	Nodes		Mat	Sec	DistLoads (kN/m)");
		elems.forEach(System.out::println);
	}

	public void print()
	{
		printNodes();
		printElems();
	}
	

    @Override
    public String toString()
    {
        return "Mesh: \nnodes: " + nodes + "\nelements: " + elems;
    }

}
