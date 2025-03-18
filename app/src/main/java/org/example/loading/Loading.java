package org.example.loading;

import java.util.ArrayList;
import java.util.List;

public class Loading
{

	private List<ConcLoads> concLoads;
	private List<DistLoads> distLoads;
	private List<NodalDisps> nodalDisps;

    public Loading()
    {
        concLoads = new ArrayList<ConcLoads>();
        distLoads = new ArrayList<DistLoads>();
        nodalDisps = new ArrayList<NodalDisps>();
    }

    public Loading(List<ConcLoads> concLoads, List<DistLoads> distLoads, List<NodalDisps> nodalDisps)
    {
        this.concLoads = concLoads;
        this.distLoads = distLoads;
        this.nodalDisps = nodalDisps;
    }

    public void addConcLoad(ConcLoads concLoad)
    {
        if (concLoads == null)
        {
            concLoads = new ArrayList<ConcLoads>();
        }
        concLoads.add(concLoad);
    }

    public void addDistLoad(DistLoads distLoad)
    {
        if (distLoads == null)
        {
            distLoads = new ArrayList<DistLoads>();
        }
        distLoads.add(distLoad);
    }

    public void addNodalDisp(NodalDisps nodalDisp)
    {
        if (nodalDisps == null)
        {
            nodalDisps = new ArrayList<NodalDisps>();
        }
        nodalDisps.add(nodalDisp);
    }

    public void clearLoads()
    {
        concLoads = new ArrayList<ConcLoads>();
        distLoads = new ArrayList<DistLoads>();
        nodalDisps = new ArrayList<NodalDisps>();
    }

    public List<ConcLoads> getConcLoads() { return concLoads ;}

    public void setConcLoads(List<ConcLoads> concLoads) { this.concLoads = concLoads ;}

    public List<DistLoads> getDistLoads() { return distLoads ;}

    public void setDistLoads(List<DistLoads> distLoads) { this.distLoads = distLoads ;}

    public List<NodalDisps> getNodalDisps() { return nodalDisps ;}

    public void setNodalDisps(List<NodalDisps> nodalDisps) { this.nodalDisps = nodalDisps ;}

    @Override
    public String toString() {
        return "Loading [concLoads=" + concLoads + ", distLoads=" + distLoads + ", nodalDisps=" + nodalDisps + "]";
    }

}
