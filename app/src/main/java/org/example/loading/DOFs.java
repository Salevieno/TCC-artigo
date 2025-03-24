package org.example.loading;

public abstract class DOFs
{
    public static final int qtdDOFs = 6 ;

    public double x ;
    public double y ;
    public double z ;
    public double tx ;
    public double ty ;
    public double tz ;

    public double[] array() { return new double[] {x, y, z, tx, ty, tz} ;}
}
