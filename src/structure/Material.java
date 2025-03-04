package structure;

public class Material
{
	private double E;
	private double v;
	private double G;	
	
	public Material(double e, double v, double G)
	{
		E = e;
		this.v = v;
		this.G = G;
	}
	
	public double getE()
	{
		return E;
	}
	public void setE(double e)
	{
		E = e;
	}
	public double getV()
	{
		return v;
	}
	public void setV(double v)
	{
		this.v = v;
	}
	public double getG()
	{
		return G;
	}
	public void setG(double fu)
	{
		this.G = fu;
	}
	
	
}
