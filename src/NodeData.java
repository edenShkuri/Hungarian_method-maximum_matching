
enum Group{
    A,B
}
public class NodeData implements Comparable<Object>{
    private static int index = 0; // static variable, to be sure that is unique key
    private final int key;
    private boolean match;
    private int tag;
    Group group;
    private double weight;
    Point p;


    public NodeData(){
        this.key = index++;
        this.match = false;
        weight=0.0;
        p=new Point();
    }

    public NodeData(int key){
        this.key = key;
        this.match = false;
        weight=0.0;
        p=new Point();
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double w) {
        this.weight = w;
    }



    public void setP(int x, int y) {
        p.setX(x);
        p.setY(y);
    }

    public Point getP() {
        return p;
    }

    public int getKey(){
        return key;
    }

    public boolean getMatch(){
        return match;
    }

    @Override
    public String toString() {
        return ""+key;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    @Override
    public int compareTo(Object o) {
        NodeData n =(NodeData) (o);
        if(weight-n.getWeight()>0) return 1;
        return -1;
    }

}
