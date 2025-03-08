package main.structure;

import java.util.List;

public class Mesh
{
	private List<Node> nodes;
	private List<Element> elems;

    public Mesh(List<Node> node, List<Element> elem)
    {
        this.nodes = node;
        this.elems = elem;
    }

    public List<Node> getNodes()  { return nodes ;}
    public List<Element> getElements() { return elems ;}

    public void reset()
    {
        nodes = null ;
        elems = null ;
    }

    @Override
    public String toString()
    {
        return "Mesh: \nnodes: " + nodes + "\nelements: " + elems;
    }

}
