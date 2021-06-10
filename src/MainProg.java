import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class MainProg {


    public static directed_Graph build_Directed_Graph(Undirected_Graph g) {
        directed_Graph newGraph=new directed_Graph();
        for(NodeData n: g.get_all_V()) {
            newGraph.addNode(n);
        }
        for(NodeData n: g.get_all_V()) {
            if(n.group==Group.A) {
                for (edgeData e : g.get_all_E(n.getKey())) {
                    if(e.getMatched()){
                        newGraph.connect(e.getDest(), e.getSrc());
                    }
                    else{
                        newGraph.connect(e.getSrc(), e.getDest());
                    }
                }
            }
        }
        return newGraph;
    }

    public static void SetAugmentingPath(Undirected_Graph g, List<NodeData> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            edgeData e1 = g.getEdge(path.get(i).getKey(), path.get(i + 1).getKey());
            edgeData e2 = g.getEdge(path.get(i + 1).getKey(), path.get(i).getKey());
            if (e1 != null && e2 != null) {
                e1.setMatched(!e1.getMatched());
                e2.setMatched(!e2.getMatched());
            }
        }
        g.getNode(path.get(0).getKey()).setMatch(true);
        g.getNode(path.get(path.size() - 1).getKey()).setMatch(true);
    }


    public static List<NodeData> getAnyPath(directed_Graph g){
        LinkedList<NodeData> unsaturated_in_A=new LinkedList<>();
        LinkedList<NodeData> unsaturated_in_B=new LinkedList<>();
        List<NodeData> path=new LinkedList<>();

        for (NodeData n: g.getV()){//fill the lists
            if(!n.getMatch()){
                if(n.group==Group.A){unsaturated_in_A.add(n);}
                else{unsaturated_in_B.add(n);}
            }
        }

        for(NodeData src: unsaturated_in_A){
            for (NodeData dest: unsaturated_in_B){
                path=g.shortestPath(src.getKey(), dest.getKey());
                if (path!=null){
                    return path;
                }
            }
        }
        return null;
    }

    /**check if bipartite and set groups
     while there is a path from A/Am to B/Bm
     find one and improve it
     build a new directed graph
     set the matches in the original graph
     build a directed graph
     return the graph
     */
    public static void Hungarian_m(Undirected_Graph g, JFrame f) throws InterruptedException {
        directed_Graph Matching_Graph =build_Directed_Graph(g);//build a directed graph
        List<NodeData> path=getAnyPath(Matching_Graph);
        while(path!=null){//while there is a path from Am to Bm
            SetAugmentingPath(g, path);
            f.repaint();
            Thread.sleep(500);
            Matching_Graph =build_Directed_Graph(g); //build a new directed graph
            path=getAnyPath(Matching_Graph);
        }
    }

    public static void MinimumEdgeCover(Undirected_Graph g, JFrame f) throws InterruptedException {
        Hungarian_m(g, f);
        LinkedList<NodeData> unMatched =g.getUnMatchedNodes();
        for(NodeData n: unMatched){
//            for(NodeData nei: g.getNi(n)){
//                if(nei.getMatch()){
                NodeData nei =g.getNi(n).stream().findFirst().get();
                g.getEdge(n.getKey(), nei.getKey()).setEdgeCover(true);
                g.getEdge(nei.getKey(), n.getKey()).setEdgeCover(true);
                f.repaint();
                Thread.sleep(500);
//                    break;
//                }
//            }
        }
    }

    public static void TestHungarian(Undirected_Graph g) throws InterruptedException {
        JFrame f =new JFrame();
        f.setSize(1100,600);
        GUI gui=new GUI(g);
        f.add(gui);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Thread.sleep(1500);
        Hungarian_m(g,f);
        g.clearUnCovered();
        f.repaint();

        System.out.println(g+"\n");
        LinkedList<NodeData> A=new LinkedList<>();
        LinkedList<NodeData> B=new LinkedList<>();
        for (NodeData n: g.get_all_V()){//fill the lists
            if(n.group==Group.A){A.add(n);}
            else{B.add(n);}
        }
        System.out.println("A: "+A.toString());
        System.out.println("B: "+B.toString()+"\n");

        System.out.println("matched edges: \n"+g.getAllMatchedEdges().toString());
        System.out.println("\nmatched nodes: \n"+g.getAllMatchedNodes().toString());
    }

    public static void TestEdgeCover(Undirected_Graph g) throws Exception {
        if(g.hasLonely()){
            throw new Exception("This graph had node with no edges!");
        }
        JFrame f =new JFrame();
        f.setSize(1100,600);
        GUI gui=new GUI(g);
        gui.setEdgeCover(true);
        f.add(gui);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Thread.sleep(1500);
        MinimumEdgeCover(g,f);
        g.clearUnCovered();
        f.repaint();
        LinkedList<edgeData> Edge_cover =new LinkedList<>();
        Edge_cover.addAll(g.getAllEdgesCover());
        Edge_cover.addAll(g.getAllMatchedEdges());
        System.out.println("Edges in Edge cover:\n"+ Edge_cover.toString());
    }

    public static Undirected_Graph CreateGraph(){
        Undirected_Graph g=new Undirected_Graph();
        for (int i=0; i<14; i++){
            NodeData n=new NodeData(i);
            g.addNode(n);
        }
        g.addEdge(6,3);
        g.addEdge(0,3);
        g.addEdge(0,7);
        g.addEdge(0,11);
        g.addEdge(0,13);
        g.addEdge(2,1);
        g.addEdge(2,5);
        g.addEdge(2,9);
        g.addEdge(4,3);
        g.addEdge(4,9);
        g.addEdge(4,11);
        g.addEdge(8,3);
        g.addEdge(10,3);
        g.addEdge(10,13);
        g.addEdge(12,1);
        return g;
    }

    /**
     * Create random bipartite graph.
     * NOTE: if E > V(a)*V(b), throw exception.
     *
     * @param Va - Sum of nodes in group A.
     * @param Vb - Sum of nodes in group B.
     * @param E - Sum of edges.
     * @return Bipartite Undirected Graph.
     */
    public static Undirected_Graph BipartiteGraphCreator(int Va,int Vb, int E) throws Exception {
        if(E > Va*Vb){ throw new Exception("This graph, cannot have "+E+" edges");}
        Undirected_Graph g = new Undirected_Graph();
        //Add nodes to the graph
        List<NodeData> nodesA = new LinkedList<>();
        List<NodeData> nodesB = new LinkedList<>();
        for(int i = 0;i<Va;++i){
            NodeData n = new NodeData();
            g.addNode(n);
            nodesA.add(n);
        }
        for(int i = 0;i<Vb;++i){
            NodeData n = new NodeData();
            g.addNode(n);
            nodesB.add(n);
        }
        Random r = new Random();
        //Connect more random edges to the graph
        while(E>0){
            int src = nodesA.get(r.nextInt(nodesA.size())).getKey();
            int dest = nodesB.get(r.nextInt(nodesB.size())).getKey();
            if(g.getEdge(src,dest) == null) {
                g.addEdge(src,dest);
                E--;
            }
        }
        return g;
    }
    public static void main(String[] args) throws Exception {

//        Undirected_Graph g=CreateGraph();
        Undirected_Graph g = BipartiteGraphCreator(5,3,12);

        g.setBipartite();

//        TestHungarian(g);
        TestEdgeCover(g);
    }
}
