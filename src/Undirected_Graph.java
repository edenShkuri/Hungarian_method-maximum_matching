import org.w3c.dom.Node;

import java.util.*;

/**
 * Graph class implements graph interface, that displays a undirectional unweighted graph.
 * this class use a dynamic data structure, which allow to contain a large graph.
 * In addition contains the total actions performed, and the number of edges.
 */

public class Undirected_Graph {
    HashMap<Integer, NodeData> vertices;
    HashMap<Integer, List<edgeData>> edges;

    public Undirected_Graph() {
        vertices = new HashMap<>();
        edges = new HashMap<>();
    }

    public NodeData getNode(int key) {
        return vertices.get(key);
    }

    public void addNode(NodeData n) {
        vertices.put(n.getKey(), n);
        edges.put(n.getKey(), new LinkedList<>());
    }

    public void addEdge(int n1, int n2) {
        if(getEdge(n1,n2) == null){
            edgeData e1 = new edgeData(getNode(n1), getNode(n2));
            edgeData e2 = new edgeData(getNode(n2), getNode(n1));

            edges.get(n1).add(e1);
            edges.get(n2).add(e2);
        }
    }

    public void removeEdge(int n1, int n2) {
        edgeData e = getEdge(n1,n2);
        if(e != null){
            edges.get(n1).remove(e);
            e = getEdge(n2,n1);
            edges.get(n2).remove(e);
        }
    }

    public void removeNode(int key) {
        vertices.remove(key);
        for (edgeData e : edges.get(key)) {
            edges.get(e.getDest()).remove(e.getSrc());
        }
        edges.remove(key);
    }

    public Collection<NodeData> get_all_V() {
        return vertices.values();
    }

    public Collection<edgeData> get_all_E(int key) {
        return edges.get(key);
    }

    public edgeData getEdge(int key1, int key2) {
        if(!vertices.containsKey(key1) || !vertices.containsKey(key2)) {
            return null;
        }else{
            for (edgeData e : edges.get(key1)) {
                if (e.getDest() == key2)
                    return e;
            }
        }
        return null;
    }

    public Collection<NodeData> getNi(NodeData n) {
        Collection<NodeData> res = new HashSet<>();
        for (edgeData e : this.get_all_E(n.getKey())) {
            res.add(e.getDestNode());
        }
        return res;
    }

    private void resetTagAndGroup() {
        for(NodeData n : vertices.values()){
            n.group=Group.A;
            n.setTag(0);
        }
    }

    public boolean setBipartite(){
        resetTagAndGroup();
        LinkedList<NodeData> q = new LinkedList<>();

        for (NodeData n : get_all_V()) {
            if(get_all_E(n.getKey()).size()==0 || n.getTag()==1){
                continue;
            }
            q.add(n);
            n.setTag(1);
            n.group=Group.B;
            while(!q.isEmpty()){
                NodeData tmp=q.poll();
                for(edgeData e : get_all_E(tmp.getKey())){
                    NodeData ni =e.getDestNode();
                    if(ni.getTag()!=1){//not been visited yet
                        q.add(ni);
                        ni.setTag(1);
                        if(tmp.group == Group.A){
                            ni.group=Group.B;
                        }else { ni.group=Group.A;}
                    }
                    else if(ni.group==tmp.group){
                        return false;
                    }
                }
            }
        }
        return true;
    }


    @Override
    public String toString() {
        String s = "Nodes     Edges\n";
                for(int key : this.edges.keySet()){
                    s+="key: "+key+" | ";

                    for(edgeData e : edges.get(key)){
                        s+=""+e.getDest()+" ";
                    }
                    s+="\n";
                }
                return s;
    }

    public LinkedList<edgeData> getAllMatchedEdges() {
        LinkedList<edgeData> matchedEdges=new LinkedList<>();
        for(NodeData n:vertices.values()){
            if(n.group==Group.A){
                for (edgeData e: get_all_E(n.getKey())){
                    if(e.getMatched()){
                        matchedEdges.add(e);
                    }
                }

            }
        }
        return matchedEdges;
    }


    public LinkedList<edgeData> getAllEdgesCover() {
        LinkedList<edgeData> EdgesCover=new LinkedList<>();
        for(NodeData n:vertices.values()){
                for (edgeData e: get_all_E(n.getKey())){
                    if(e.getEdgeCover()){
                        if(!EdgesCover.contains(getEdge(e.getDest(), e.getSrc())))
                        EdgesCover.add(e);
                    }
                }
        }
        return EdgesCover;
    }

    public LinkedList<NodeData> getAllMatchedNodes() {
        LinkedList<NodeData> matchedNodes=new LinkedList<>();
        for(NodeData n:vertices.values()){
            if(n.getMatch()){
                matchedNodes.add(n);
            }
        }
        return matchedNodes;
    }

    public LinkedList<NodeData> getUnMatchedNodes() {
        LinkedList<NodeData> UnmatchedNodes=new LinkedList<>();
        for(NodeData n:vertices.values()){
            if(!n.getMatch()){
                UnmatchedNodes.add(n);
            }
        }
        return UnmatchedNodes;
    }


    public void clearUnCovered() {
        LinkedList<edgeData> UnCovered=new LinkedList<>();
        for(NodeData n: get_all_V()){
            for(edgeData e: get_all_E(n.getKey())){
                if(!e.getMatched()) {
                    if (!e.getEdgeCover()) {
                        UnCovered.add(e);
                    }
                }
            }
        }
        for(edgeData e: UnCovered){
            removeEdge(e.getSrc(), e.getDest());
        }
    }
}
