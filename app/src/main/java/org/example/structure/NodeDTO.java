package org.example.structure;

import java.util.List;

import org.example.loading.ConcLoad;
import org.example.loading.NodalDisp;
import org.example.utilidades.Point3D;

public class NodeDTO
{
	private int ID;				// ID
	private Point3D coords;		// undeformed coordinates
	private int[] Sup;			// Support in the node
	private List<ConcLoad> ConcLoad;	// Concentrated loads in the node
	private List<NodalDisp> NodalDisp;// Nodal displacements in the node
	public int[] dofs;

    public NodeDTO()
    {

    }

    public NodeDTO(Node node)
    {
        this.ID = node.getID() ;
        this.coords = node.getOriginalCoords() ;
        this.Sup = node.getSup() ;
        this.ConcLoad = node.getConcLoads() ;
        this.NodalDisp = node.getNodalDisps() ;
        this.dofs = node.getDOFs() ;
    }
}
