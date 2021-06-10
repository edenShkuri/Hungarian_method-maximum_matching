import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Undirected_Graph {
    HashMap<Integer, NodeData> vertices;
    HashMap<Integer, List<edgeData>> edges;
    List<NodeData> currAugmentingPath;

    public Undirected_Graph() {
        vertices = new HashMap<>();
        edges = new HashMap<>();
        currAugmentingPath = new LinkedList<>();
    }

    public NodeData getNode(int key) {
        return vertices.get(key);
    }

    public void addNode(NodeData n) {
        vertices.put(n.getKey(), n);
        edges.put(n.getKey(), new LinkedList<>());
    }

    public void addEdge(int n1, int n2) {
        if(getEdge(n1,n2) == null && n1!=n2){
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



    public boolean load(String file) {
        try {
            //JSONObject that represent the graph from JSON file
            JSONObject graph = new JSONObject(new String(Files.readAllBytes(Paths.get(file))));

            //Two JSONArray that represents the Edges and Nodes
            JSONArray edges = graph.getJSONArray("Edges");
            JSONArray nodes = graph.getJSONArray("Nodes");

            //For each Node, get the data ,make new node and add him to the graph
            for (int i = 0; i < nodes.length(); i++) {
                JSONObject nJSON = nodes.getJSONObject(i);
                //Build node that contain the id an pos
                NodeData n = new NodeData(nJSON.getInt("id"));
                try {
                    JSONObject pointJSON = nJSON.getJSONObject("point");
                    int x = pointJSON.getInt("X");
                    int y = pointJSON.getInt("Y");
                    n.setP(x,y);
                }catch(Exception e){
                }
                //Add this node to the graph
                addNode(n);
            }
            //For each edge, get the data and connect two vertex by the data
            for (int i = 0; i < edges.length(); i++) {
                JSONObject edge = edges.getJSONObject(i);
                int src = edge.getInt("src");
                int dest = edge.getInt("dest");
                addEdge(src, dest);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean save(String file) {
        //Create new Json object - graph
        JSONObject graph = new JSONObject();
        //Declare two Json arrays
        JSONArray edges = new JSONArray();
        JSONArray nodes = new JSONArray();
        try {
            //For each node
            for (NodeData n : get_all_V()) {
                //Scan all his edges
                for (edgeData e : get_all_E(n.getKey())) {
                    //Declare Json object - edge
                    JSONObject edge = new JSONObject();
                    //Insert the data to this object
                    edge.put("src", e.getSrc());
                    edge.put("dest", e.getDest());
                    //Insert this object to edges array
                    edges.put(edge);
                }
                //Declare Json object - node
                JSONObject node = new JSONObject();
                //Insert the data to this object
                node.put("id", n.getKey());
                JSONObject point = new JSONObject();
                point.put("X",n.getP().getX());
                point.put("Y",n.getP().getY());
                node.put("point",point);
                //Insert this object to nodes array
                nodes.put(node);
            }
            //Insert this both arrays to the graph object
            graph.put("Edges", edges);
            graph.put("Nodes", nodes);

            PrintWriter pw = new PrintWriter(new File(file));
            pw.write(graph.toString());
            pw.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<NodeData> getCurrAugmentingPath() {
        return this.currAugmentingPath;
    }

    public void setCurrAugmentingPath(List<NodeData> path){
        this.currAugmentingPath = path;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Nodes     Edges\n");
        for(int key : this.edges.keySet()){
            s.append("key: ").append(key).append(" | ");

            for(edgeData e : edges.get(key)){
                s.append("").append(e.getDest()).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }


}
