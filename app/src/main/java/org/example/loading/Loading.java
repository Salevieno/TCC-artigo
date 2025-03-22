package org.example.loading;

import java.util.ArrayList;
import java.util.List;

public class Loading
{

	private List<ConcLoad> concLoads;
	private List<DistLoad> distLoads;
	private List<NodalDisp> nodalDisps;

    public Loading()
    {
        concLoads = new ArrayList<ConcLoad>();
        distLoads = new ArrayList<DistLoad>();
        nodalDisps = new ArrayList<NodalDisp>();
    }

    public Loading(List<ConcLoad> concLoads, List<DistLoad> distLoads, List<NodalDisp> nodalDisps)
    {
        this.concLoads = concLoads;
        this.distLoads = distLoads;
        this.nodalDisps = nodalDisps;
    }

    public void addConcLoad(ConcLoad concLoad)
    {
        if (concLoads == null)
        {
            concLoads = new ArrayList<ConcLoad>();
        }
        concLoads.add(concLoad);
    }

    public void addDistLoad(DistLoad distLoad)
    {
        if (distLoads == null)
        {
            distLoads = new ArrayList<DistLoad>();
        }
        distLoads.add(distLoad);
    }

    public void addNodalDisp(NodalDisp nodalDisp)
    {
        if (nodalDisps == null)
        {
            nodalDisps = new ArrayList<NodalDisp>();
        }
        nodalDisps.add(nodalDisp);
    }

    public void clearLoads()
    {
        concLoads = new ArrayList<ConcLoad>();
        distLoads = new ArrayList<DistLoad>();
        nodalDisps = new ArrayList<NodalDisp>();
    }

    public List<ConcLoad> getConcLoads() { return concLoads ;}

    public void setConcLoads(List<ConcLoad> concLoads) { this.concLoads = concLoads ;}

    public List<DistLoad> getDistLoads() { return distLoads ;}

    public void setDistLoads(List<DistLoad> distLoads) { this.distLoads = distLoads ;}

    public List<NodalDisp> getNodalDisps() { return nodalDisps ;}

    public void setNodalDisps(List<NodalDisp> nodalDisps) { this.nodalDisps = nodalDisps ;}

    @Override
    public String toString() {
        return "Loading [concLoads=" + concLoads + ", distLoads=" + distLoads + ", nodalDisps=" + nodalDisps + "]";
    }

}
