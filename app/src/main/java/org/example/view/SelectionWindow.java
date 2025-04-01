package org.example.view;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.example.structure.Element;
import org.example.structure.Mesh;
import org.example.structure.Node;
import org.example.userInterface.Menus;
import org.example.utilidades.MyCanvas;

import graphics.Align;
import graphics.DrawPrimitives;

public class SelectionWindow
{
    private Point topLeftPos ;
    private Point bottomRightPos ;

    public SelectionWindow()
    {
        this(null) ;
    }

    public SelectionWindow(Point topLeft)
    {
        this.topLeftPos = topLeft;
        this.bottomRightPos = null;
    }

    
	public void display(Point mousePos, DrawPrimitives DP)
	{
        if (topLeftPos == null || mousePos == null) { return ; }

		Dimension size = new Dimension(mousePos.x - topLeftPos.x, mousePos.y - topLeftPos.y) ;
		if (topLeftPos.x <= mousePos.x && topLeftPos.y <= mousePos.y)
		{
            DP.drawRect(topLeftPos, Align.topLeft, size, null, Menus.palette[0]) ;
		}
	}

    private void close()
    {
        topLeftPos = null;
        bottomRightPos = null;
    }

    public void selectNodesInside(Mesh mesh, MyCanvas canvas, Point mousePos)
    {
        if (mesh == null)
        {
            close() ;
            return ;
        }

        bottomRightPos = new Point(mousePos) ;
        mesh.unselectAllNodes() ;
        for (Node node : mesh.getNodes())
		{
			if (contains(node.getDrawingPos()))
			{
				node.select() ;
			}
		}
        close() ;
    }

    public void selectElementsInside(Mesh mesh, MyCanvas canvas, Point mousePos)
    {
        if (mesh == null)
        {
            close() ;
            return ;
        }
        
        bottomRightPos = new Point(mousePos) ;
        mesh.unselectAllElements() ;
        for (Element elem : mesh.getElements())
		{
			if (elem.isInside(this, canvas, mesh.getNodes()))
			{
				elem.select() ;
			}
		}
        close() ;
    }

    public boolean contains(Point point)
    {
        if (topLeftPos == null || bottomRightPos == null) { System.out.println("Warn: selection window does not have all edges while trying to check if it contains something") ; return false ;}

        return topLeftPos.x <= point.x && point.x <= bottomRightPos.x && point.y <= bottomRightPos.y && topLeftPos.y <= point.y;
    }

    public boolean isActive() { return topLeftPos != null ;}

    public void setTopLeft(Point topLeft) { this.topLeftPos = topLeft ;}

    @Override
    public String toString()
    {
        return "SelectionWindow [topLeftPos=" + topLeftPos + ", bottomRightPos=" + bottomRightPos + ", isActive()=" + isActive() + "]";
    }
    
}
