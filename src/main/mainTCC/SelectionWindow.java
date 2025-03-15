package main.mainTCC;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.structure.MyCanvas;
import main.structure.Node;
import main.userInterface.DrawingOnAPanel;
import main.userInterface.Menus;

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

    
	public void display(Point mousePos, DrawingOnAPanel DP)
	{
        if (topLeftPos == null || mousePos == null) { return ; }

		int[] RectPos = new int[] {topLeftPos.x, topLeftPos.y};
		int l = mousePos.x - topLeftPos.x, h = mousePos.y - topLeftPos.y;
		if (topLeftPos.x <= mousePos.x & topLeftPos.y <= mousePos.y)
		{
			DP.DrawRect(RectPos, l, h, 1, "Left", 0, false, Menus.palette[0], null);
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
        System.out.println("\nWindow topLeft: " + topLeftPos);
        System.out.println("\nWindow bottomRight: " + bottomRightPos);
        for (Node node : allNodes)
        {
            Point nodeDrawingCoords = canvas.inDrawingCoords(node.getOriginalCoords()) ;
            System.out.println("\node pos: " + nodeDrawingCoords);
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
