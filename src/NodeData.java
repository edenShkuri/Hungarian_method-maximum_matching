/**
 * NodeData class implements node_data interface, that displays a one node.
 * this class use a dynamic data structure, which allow to contain a lot of neighbors nodes.
 * In addition contains index that raise at 1 when create new node, that allow each node have a unique key.
 * and private string data (info) and int data (tag).
 */
enum Group{
    A,B
}
public class NodeData implements Comparable<Object>{
    private static int index = 0; // static variable, to be sure that is unique key
    private int key;
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
