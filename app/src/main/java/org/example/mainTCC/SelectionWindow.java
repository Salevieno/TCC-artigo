package org.example.mainTCC;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.example.structure.Node;
import org.example.userInterface.Draw;
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

    public List<Node> selectNodes(List<Node> allNodes, MyCanvas canvas, Point mousePos)
    {

        if (topLeftPos == null || mousePos == null) { return null ; }

        List<Node> selectedNodes = new ArrayList<>();
        bottomRightPos = new Point(mousePos) ;
        for (Node node : allNodes)
        {
            Point nodeDrawingCoords = canvas.inDrawingCoords(node.getOriginalCoords()) ;
            if (isInside(nodeDrawingCoords))
            {
                selectedNodes.add(node);
            }
        }

        close() ;

        return selectedNodes;
    }

    private boolean isInside(Point point)
    {
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
