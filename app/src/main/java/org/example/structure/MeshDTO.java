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
}
