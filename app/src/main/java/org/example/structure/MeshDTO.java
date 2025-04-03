package org.example.structure;

import java.util.ArrayList;
import java.util.List;

public class MeshDTO
{
	private List<NodeDTO> nodesDTO;
	private List<ElementDTO> elemsDTO;

    public MeshDTO()
    {

    }

    public MeshDTO(Mesh mesh)
    {
        this.nodesDTO = new ArrayList<>() ;
        for (Node node : mesh.getNodes())
        {
            this.nodesDTO.add(new NodeDTO(node)) ;
        }

        this.elemsDTO = new ArrayList<>() ;
        for (Element elem : mesh.getElements())
        {
            this.elemsDTO.add(new ElementDTO(elem)) ;
        }
    }

    public List<Node> createNodes()
    {
        List<Node> nodes = new ArrayList<>() ;
        for (NodeDTO dto : nodesDTO)
        {
            nodes.add(new Node(dto)) ;
        }

        return nodes ;
    }

    public List<Element> createElements()
    {
        List<Element> elems = new ArrayList<>() ;
        for (ElementDTO dto : elemsDTO)
        {
            elems.add(new Element(dto)) ;
        }

        return elems ;
    }
}
