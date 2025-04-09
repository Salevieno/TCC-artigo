package org.example.utilidades;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

public class MyCanvasTest
{
    private static final Dimension initialSize = new Dimension(582, 610) ;
    private static final Dimension size = new Dimension((int) (0.4 * initialSize.width), (int) (0.8 * initialSize.height)) ;
    private static final MyCanvas canvas = new MyCanvas(new Point(575, 25), size, new Point2D.Double(10, 10), new Point()) ;
    
    @Test
    void snipToGridCalculatesPointCorrectly()
    {
        Point2D.Double realMousePos = new Point2D.Double(3.0, 9.0) ;
        Point2D.Double expectedResult = new Point2D.Double(3.0, 9.0) ;

        Point2D.Double result = canvas.closestGridNodePos(realMousePos) ;

        assertEquals(expectedResult, result) ;
    }
}
