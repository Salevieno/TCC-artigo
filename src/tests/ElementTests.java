package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import structure.ElemShape;
import structure.ElemType;
import structure.Element;
import structure.Nodes;

public class ElementTests
{
	@Test
	void testShapeToNumberNodes()
	{
		assertEquals(4, Element.shapeToNumberNodes(ElemShape.rectangular, null)) ;
		assertEquals(3, Element.shapeToNumberNodes(ElemShape.triangular, ElemType.T3G)) ;
		assertEquals(6, Element.shapeToNumberNodes(ElemShape.triangular, ElemType.T6G)) ;
		assertEquals(8, Element.shapeToNumberNodes(ElemShape.r8, null)) ;
		assertEquals(-1, Element.shapeToNumberNodes(ElemShape.r9, null)) ;
	}
	
	@Test
	void calcSize()
	{
		Nodes node0 = new Nodes(0, new double[] {0.0, 0.0, 0.0}) ;
		Nodes node1 = new Nodes(1, new double[] {5.0, 0.0, 0.0}) ;
		Nodes node2 = new Nodes(2, new double[] {5.0, 8.0, 0.0}) ;
		Nodes node3 = new Nodes(3, new double[] {0.0, 8.0, 0.0}) ;
		Element elem = new Element(1, new int[] {0, 1, 2, 3}, new int[0], null, null, ElemType.KR1) ;
		
		double[] elemSize = elem.calcHalfSize(new Nodes[] {node0, node1, node2, node3}) ;
		assertEquals(2.5, elemSize[0], Math.pow(10, -8)) ;
		assertEquals(4.0, elemSize[1], Math.pow(10, -8)) ;
	}
}
