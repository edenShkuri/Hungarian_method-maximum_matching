import javax.swing.*;
import java.util.LinkedList;
import java.util.List;


public class Hungarian_method {


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
     define a match?
     while there is a path from Am to Bm
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
            Thread.sleep(1500);
            Matching_Graph =build_Directed_Graph(g); //build a new directed graph
            path=getAnyPath(Matching_Graph);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Undirected_Graph g=new Undirected_Graph();
        for (int i=0; i<14; i++){
            NodeData n=new NodeData(i);
            g.addNode(n);
        }
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

        g.setBipartite();


        JFrame frame =new JFrame();
        frame.setSize(1100,600);
        Hungarian_GUI gui=new Hungarian_GUI(g);
        frame.add(gui);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        System.out.println(g);
        LinkedList<NodeData> A=new LinkedList<>();
        LinkedList<NodeData> B=new LinkedList<>();
        Hungarian_m(g, frame);

//        g.clearUnmached();
//        frame.repaint();

        for (NodeData n: g.get_all_V()){//fill the lists
            if(n.group==Group.A){A.add(n);}
            else{B.add(n);}
        }
        System.out.println("A: "+A.toString());
        System.out.println("B: "+B.toString()+"\n");

        System.out.println("matched edges: \n"+g.getAllMatchedEdges().toString());
        System.out.println("\nmatched nodes: \n"+g.getAllMatchedNodes().toString());

    }
}
