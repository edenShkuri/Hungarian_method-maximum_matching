public class edgeData{
    private final NodeData src;
    private final NodeData dest;
    private boolean matched;
    private double weight;
    private boolean inEdgeCover;

    public edgeData(NodeData src, NodeData dest) {
        this.src = src;
        this.dest = dest;
        matched=false;
        weight=1.0;
        inEdgeCover=false;
    }

    public double getWeight() {
        return weight;
    }
    public void setWeight(double w) {
        this.weight = w;
    }

    public int getSrc() {return src.getKey();}

    public int getDest() {return dest.getKey();}

    public NodeData getSrcNode(){return src;}

    public NodeData getDestNode() {return dest;}

    public boolean getMatched(){return matched; }

    public void setMatched(boolean newMatch){matched=newMatch;}

    public boolean getEdgeCover(){return inEdgeCover; }

    public void setEdgeCover(boolean b){inEdgeCover=b;}

    public String toString(){
        return "("+src+","+dest+")";
    }
}
