package org.example.service;

public class MenuViewService
{
    
	public boolean nodes ;
	public boolean elems, concLoads, distLoads, nodalDisps, reactionArrows, reactionValues, loadsValues, sup;
	public boolean DOFNumber, nodeNumber, elemNumber, elemContour, matColor, secColor, elemDetails;
	public boolean deformedStructure ;

    private static final MenuViewService view ;

    static
    {
        view = new MenuViewService() ;
    }

    private MenuViewService()
    {

    }

    public static MenuViewService getInstance() { return view ;}

    public void reset()
    {
        nodes = false;
        elems = false;
        concLoads = false;
        distLoads = false;
        nodalDisps = false;
        reactionArrows = false;
        reactionValues = false;
        loadsValues = false;
        sup = false;
        DOFNumber = false;
        nodeNumber = false;
        elemNumber = false;
        elemContour = false;
        matColor = false;
        secColor = false;
        elemDetails = false;
        deformedStructure = false;
    }

	public void switchDOFNumberView() { DOFNumber = !DOFNumber ;}	
	public void switchNodeNumberView() { nodeNumber = !nodeNumber ;}	
	public void switchElemNumberView() { elemNumber = !elemNumber ;}	
	public void switchMatView() { matColor = !matColor ;}	
	public void switchSecView() { secColor = !secColor ;}	
	public void switchNodeView() { nodes = !nodes ;}	
	public void switchElemView() { elems = !elems ;}	
	public void switchElemContourView() { elemContour = !elemContour ;}	
	public void switchSupView() { sup = !sup ;}	
	public void switchConcLoadsView() { concLoads = !concLoads ;}	
	public void switchDistLoadsView() { distLoads = !distLoads ;}	
	public void switchNodalDispsView() { nodalDisps = !nodalDisps ;}	
	public void switchLoadsValuesView() { loadsValues = !loadsValues ;}	
	public void switchReactionArrowsView() { reactionArrows= !reactionArrows ;}	
	public void switchReactionValuesView() { reactionValues = !reactionValues ;}	
	public void switchElemDetailsView() { elemDetails = !elemDetails ;}
	public void switchDeformedStructureView() { deformedStructure = !deformedStructure ;}

}
