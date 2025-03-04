package structure;

public class MyCanvas
{
	int[] TitlePos;
	String Title;
	int[] Pos;
	int[] Size;
	double[] Dim;
	int[] DrawingPos;
	int[] Center;
	double[] GridSpacing;
	double zoom;
	double[] angles;
	
	public MyCanvas(int[] Pos, int[] Size, double[] Dim, int[] DrawingPos)
	{
		TitlePos = null;
		Title = null;
		this.Pos = Pos;
		this.Size = Size;
		this.Dim = Dim;
		this.DrawingPos = DrawingPos;
		Center = new int[] {Pos[0] + Size[0] / 2, Pos[1] + Size[1] / 2};
		GridSpacing = new double[] {5, 5, 0};
		zoom = 1;
		angles = new double[] {0.0, 0.0, 0.0};
	}
	
	public int[] getTitlePos() {return TitlePos;}
	public String getTitle() {return Title;}
	public int[] getPos() {return Pos;}
	public int[] getSize() {return Size;}
	public double[] getDim() {return Dim;}
	public int[] getDrawingPos() {return DrawingPos;}
	public int[] getCenter() {return Center;}
	public double[] getGridSpacing() {return GridSpacing;}
	public double getZoom() {return zoom;}
	public double[] getAngles() {return angles;}
	public void setTitlePos(int[] T) {TitlePos = T;}
	public void setTitle(String T) {Title = T;}
	public void setPos(int[] P) {Pos = P;}
	public void setSize(int[] S) {Size = S;}
	public void setDim(double[] D) {Dim = D;}
	public void setDrawingPos(int[] D) {DrawingPos = D;}
	public void setCenter(int[] C) {Center = C;}
	public void setGridSpacing(double[] G) {GridSpacing = G;}
	public void setZoom(double Z) {zoom = Z;}
	public void setAngles(double[] a) {angles = a;}
}
