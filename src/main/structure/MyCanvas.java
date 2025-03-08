package main.structure;

import java.awt.Point;

public class MyCanvas
{
	int[] TitlePos;
	String Title;
	private Point pos;
	int[] Size;
	double[] Dim;
	int[] DrawingPos;
	int[] Center;
	double[] GridSpacing;
	double zoom;
	double[] angles;
	
	public MyCanvas(Point pos, int[] Size, double[] Dim, int[] DrawingPos)
	{
		TitlePos = null;
		Title = null;
		this.pos = pos;
		this.Size = Size;
		this.Dim = Dim;
		this.DrawingPos = DrawingPos;
		Center = new int[] {pos.x + Size[0] / 2, pos.y + Size[1] / 2};
		GridSpacing = new double[] {5, 5, 0};
		zoom = 1;
		angles = new double[] {0.0, 0.0, 0.0};
	}
	
	public int[] getTitlePos() {return TitlePos;}
	public String getTitle() {return Title;}
	public Point getPos() {return pos;}
	public int[] getSize() {return Size;}
	public double[] getDim() {return Dim;}
	public int[] getDrawingPos() {return DrawingPos;}
	public int[] getCenter() {return Center;}
	public double[] getGridSpacing() {return GridSpacing;}
	public double getZoom() {return zoom;}
	public double[] getAngles() {return angles;}
	public void setTitlePos(int[] T) {TitlePos = T;}
	public void setTitle(String T) {Title = T;}
	public void setPos(Point P) {pos = P;}
	public void setSize(int[] S) {Size = S;}
	public void setDim(double[] D) {Dim = D;}
	public void setDrawingPos(int[] D) {DrawingPos = D;}
	public void setCenter(int[] C) {Center = C;}
	public void setGridSpacing(double[] G) {GridSpacing = G;}
	public void setZoom(double Z) {zoom = Z;}
	public void setAngles(double[] a) {angles = a;}
}
