public class edgeData{
    private NodeData src;
    private NodeData dest;
    private boolean matched;
    private double weight;

    public edgeData(NodeData src, NodeData dest) {
        this.src = src;
        this.dest = dest;
        matched=false;
        weight=1.0;
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

    public String toString(){
        return "("+src+","+dest+")";
    }
}
