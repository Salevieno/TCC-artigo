package org.example.utilidades;

public class Dimension3D
{
    private final double width;
    private final double height;
    private final double depth;

    public Dimension3D(double width, double height, double depth)
    {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getDepth() { return depth; }

    @Override
    public String toString()
    {
        return "Dimension3D{" + "width=" + width + ", height=" + height + ", depth=" + depth + '}';
    }
}
