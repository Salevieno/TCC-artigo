package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.utilidades.Point3D;
import org.junit.jupiter.api.Test;

public class UtilTest
{
    
    @Test
    void rotatingCoordTrivial()
    {
        Point3D refPoint = new Point3D(0, 0, 0) ;
        Point3D point = new Point3D(1, 2, 3) ;
        Point3D angle = new Point3D(0, 0, 0) ;
        Point3D expectedRotated = new Point3D(1, 2, 3) ;
        point.rotate(refPoint, angle);
    
        assertEquals(expectedRotated, point);    
    }

    @Test
    void rotatingCoordX()
    {
        Point3D refPoint = new Point3D(0, 0, 0) ;
        Point3D point = new Point3D(1, 1, 1) ;
        Point3D angle = new Point3D(Math.PI / 4, 0, 0) ;
        Point3D expectedRotated = new Point3D(1, 0, Math.sqrt(2)) ;
        point.rotate(refPoint, angle);
    
        assertEquals(expectedRotated, point);
    }

    @Test
    void rotatingCoordY()
    {
        Point3D refPoint = new Point3D(0, 0, 0) ;
        Point3D point = new Point3D(1, 1, 1) ;
        Point3D angle = new Point3D(0, -Math.PI / 4, 0) ;
        Point3D expectedRotated = new Point3D(Math.sqrt(2), 1, 0) ;
        point.rotate(refPoint, angle);
    
        assertEquals(expectedRotated, point);
    }

    @Test
    void rotatingCoordZ()
    {
        Point3D refPoint = new Point3D(0, 0, 0) ;
        Point3D point = new Point3D(1, 1, 1) ;
        Point3D angle = new Point3D(0, 0, Math.PI / 4) ;
        Point3D expectedRotated = new Point3D(0, Math.sqrt(2), 1) ;
        point.rotate(refPoint, angle);
    
        assertEquals(expectedRotated, point);
    }
    
    @Test
    void rotatingCoordXY_PiOver6()
    {
        Point3D refPoint = new Point3D(0, 0, 0);
        Point3D point = new Point3D(0, 1, 1);
        Point3D angle = new Point3D(Math.PI / 6, Math.PI / 6, 0);
        Point3D expectedRotated = new Point3D(
            -0.5,
            Math.sqrt(3) / 4.0,
            1.25
        );
        point.rotate(refPoint, angle);
        assertEquals(expectedRotated, point);
    }

    @Test
    void rotatingCoordXZ_PiOver6()
    {
        Point3D refPoint = new Point3D(0, 0, 0);
        Point3D point = new Point3D(1, 1, 0);
        Point3D angle = new Point3D(Math.PI / 6, 0, Math.PI / 6);
        Point3D expectedRotated = new Point3D(
            (Math.sqrt(3) - 1) / 2,
            (Math.sqrt(3) + 3) / 4,
            (1 + Math.sqrt(3)) / 4
        );
        point.rotate(refPoint, angle);
        assertEquals(expectedRotated, point);
    }

    @Test
    void rotatingCoordYZ_PiOver6()
    {
        Point3D refPoint = new Point3D(0, 0, 0);
        Point3D point = new Point3D(1, 0, 1);
        Point3D angle = new Point3D(0, Math.PI / 6, Math.PI / 6);
        Point3D expectedRotated = new Point3D(
            1.0 / 4.0,
            1.0 / 2.0,
            (3 * Math.sqrt(3)) / 4.0
        );
        point.rotate(refPoint, angle);
        assertEquals(expectedRotated, point);
    }
    
    @Test
    void rotatingCoordYX_PiOver6()
    {
        Point3D refPoint = new Point3D(0, 0, 0);
        Point3D point = new Point3D(0, 1, 1);
        Point3D angle = new Point3D(Math.PI / 6, Math.PI / 6, 0); // ordem Y depois X
        Point3D expectedRotated = new Point3D(
            -0.5,
            Math.sqrt(3) / 4.0,
            1.25
        );
        point.rotate(refPoint, angle);
        assertEquals(expectedRotated, point);
    }

    @Test
    void rotatingCoordZX_PiOver6()
    {
        Point3D refPoint = new Point3D(0, 0, 0);
        Point3D point = new Point3D(1, 1, 0);
        Point3D angle = new Point3D(Math.PI / 6, 0, Math.PI / 6);
        Point3D expectedRotated = new Point3D(
            (Math.sqrt(3) - 1) / 2.0,
            (Math.sqrt(3) + 3) / 4.0,
            (1 + Math.sqrt(3)) / 4.0
        );
        point.rotate(refPoint, angle);
        assertEquals(expectedRotated, point);
    }

    @Test
    void rotatingCoordZY_PiOver6()
    {
        Point3D refPoint = new Point3D(0, 0, 0);
        Point3D point = new Point3D(1, 0, 1);
        Point3D angle = new Point3D(0, Math.PI / 6, Math.PI / 6);
        Point3D expectedRotated = new Point3D(
            1.0 / 4.0,
            1.0 / 2.0,
            (3 * Math.sqrt(3)) / 4.0
        );
        point.rotate(refPoint, angle);
        assertEquals(expectedRotated, point);
    }

    @Test
    void rotatingCoordXYZ_PiOver6()
    {
        Point3D refPoint = new Point3D(0, 0, 0);
        Point3D point = new Point3D(1, 1, 1);
        Point3D angle = new Point3D(Math.PI / 6, Math.PI / 6, Math.PI / 6);
        Point3D expectedRotated = new Point3D(
            (1 - Math.sqrt(3)) / 4.0,
            (7 - Math.sqrt(3)) / 8.0,
            (11 + Math.sqrt(3)) / 8.0
        );
        point.rotate(refPoint, angle);
        assertEquals(expectedRotated, point);
    }



    // TODO order of rotation matters O.o
    // @Test
    // void rotatingCoord45()
    // {
    //     Point3D refPoint = new Point3D(0, 0, 0) ;
    //     Point3D point = new Point3D(1, 1, 1) ;
    //     Point3D angle = new Point3D(Math.PI / 4, Math.PI / 4, Math.PI / 4) ;
    //     Point3D expectedRotated = new Point3D(1 + Math.sqrt(2) / 2.0, 1 - Math.sqrt(2) / 2.0, 1 + Math.sqrt(2) / 2.0) ;
    //     point.rotate(refPoint, angle);
    
    //     assertEquals(expectedRotated, point);
    // }
    
}
