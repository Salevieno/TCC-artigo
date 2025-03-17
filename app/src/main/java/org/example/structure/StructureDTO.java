package org.example.structure;

import java.util.List;

import org.example.output.Results;
import org.example.utilidades.Point3D;

public class StructureDTO
{
	private String name;
	private StructureShape shape;
	private List<Point3D> coords;
	// private Mesh mesh ;
	private List<Supports> supports;
	private Results results ;

    public StructureDTO()
    {

    }
    
    public StructureDTO(Structure structure)
    {
        this.name = structure.getName() ;
        this.shape = structure.getShape() ;
        this.coords = structure.getCoords() ;
        // this.mesh = structure.getMesh() ;
        this.supports = structure.getSupports() ;
        this.results = structure.getResults() ;
    }
}
