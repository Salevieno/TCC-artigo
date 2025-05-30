package org.example.structure;

import java.util.List;

import org.example.loading.DistLoad;

public class ElementDTO
{
	private int ID;
	private ElemType type;			// Type of element
	private ElemShape Shape;
    
    private List<Node> ExternalNodes;	// Nodes on the contour (along the edges) in the counter-clockwise direction
	private Material mat ;
	private Section sec;
	private List<DistLoad> distLoads;	// Distributed loads in the node

    public ElementDTO()
    {

    }

    public ElementDTO(Element element)
    {
        this.ID = element.getId();
        this.type = element.getType();	// Type of element
        this.Shape = element.getShape();
        
        this.ExternalNodes = element.getExternalNodes();	// Nodes on the contour (along the edges) in the counter-clockwise direction
        this.mat = element.getMat() ;
        this.sec = element.getSec() ;
        this.distLoads = element.getDistLoads();
    }

    public int getID() {
        return ID;
    }

    public ElemType getType() {
        return type;
    }

    public ElemShape getShape() {
        return Shape;
    }

    public List<Node> getExternalNodes() {
        return ExternalNodes;
    }

    public Material getMat() {
        return mat;
    }

    public Section getSec() {
        return sec;
    }

    public List<DistLoad> getDistLoads() {
        return distLoads;
    }

    
}
