package org.example.loading;

public class Force extends DOFs
{
    
    public Force()
    {
        this(0, 0, 0, 0, 0, 0) ;
    }
    
    public Force(double x, double y, double z, double tx, double ty, double tz)
    {
        this.x = x ;
        this.y = y ;
        this.z = z ;
        this.tx = tx ;
        this.ty = ty ;
        this.tz = tz ;
    }

    public Force(double[] force)
    {
        if (force.length <= 5) { System.out.println("Error: trying to create force with less than 6 dofs") ; return ;}

        this.x = force[0] ;
        this.y = force[1] ;
        this.z = force[2] ;
        this.tx = force[3] ;
        this.ty = force[4] ;
        this.tz = force[5] ;
    }

    @Override
    public String toString()
    {
        return "Force = " + x + "N " + y + "N " + z + "N " + tx + "Nm " + ty + "Nm " + tz + "Nm";
    }

}
