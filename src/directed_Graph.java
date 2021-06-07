import java.util.*;

public class directed_Graph {
    private HashMap<Integer, NodeData> vertices;
    private HashMap<Integer, HashMap<Integer, edgeData>> edges;
    private ArrayList<ArrayList<Integer> > adj;
    private LinkedList<NodeData> list;
    HashMap<Integer, NodeData> re;


    public directed_Graph() {
            vertices = new HashMap<>();
            edges = new HashMap<>();
        }

        public NodeData getNode(int key) {
            if (!(vertices.containsKey(key)))
                return null;
            return vertices.get(key);
        }

        public edgeData getEdge(int src, int dest) {
            if (!(vertices.containsKey(src)) || !(vertices.containsKey(dest)) ||
                    !(edges.get(src).containsKey(dest)))
                return null;
            return edges.get(src).get(dest);
        }

        public void addNode(NodeData n) {
            if(n == null) return;
            vertices.put(n.getKey(), n);
            edges.put(n.getKey(), new HashMap<>());
        }

        public void connect(int src, int dest) {
            if (!(vertices.containsKey(src)) || !(vertices.containsKey(dest)) || getEdge(src, dest) != null)
                return;
            edgeData e = new edgeData(getNode(src), getNode(dest));
            edges.get(src).put(dest, e);
        }


        public Collection<NodeData> getV() {
            return vertices.values();
        }

        public Collection<edgeData> getE(int node_id) {
            if (!(vertices.containsKey(node_id)))
                return null;
            return edges.get(node_id).values();
        }

        public NodeData removeNode(int key) {
            if (!(vertices.containsKey(key)))
                return null;
            NodeData tmp = vertices.get(key);

            for (NodeData n : getV()) {
                if (edges.get(n.getKey()).containsKey(key)) {
                    edges.get(n.getKey()).remove(key);
                }
            }

            edges.remove(key);
            vertices.remove(key);
            return tmp;
        }

        public edgeData removeEdge(int src, int dest) {
            edgeData e = getEdge(src, dest);
            if (e == null)
                return null;
            edges.get(src).remove(dest);
            return e;
        }

        private void resetTagTo0() {    //O(v)
            for (NodeData n : getV())
                n.setTag(0);
        }

        private void Dijkstra(int src) {
            final int mark = 1;
            PriorityQueue<NodeData> dist = new PriorityQueue<>();
            re = new HashMap<>(); //save the node that we came for to each key {sun, father}
            resetTagTo0();
            for (NodeData n : getV()) // go over all the graph's nodes
            {
                n.setWeight(Double.MAX_VALUE); //reset the weight to infinity
                re.put(n.getKey(), null); //reset all nodes to null (the "father")
            }
            NodeData tmp = getNode(src);
            tmp.setWeight(0.0); //set the tag-the distance of the ex1.ex1.src from the ex1.ex1.src to 0
            dist.add(tmp); //add him to the the queue of the distances
            while (!dist.isEmpty()) {
                tmp = dist.poll(); //poll and save the node with the lowest distance from the ex1.ex1.src in the queue
                for (edgeData e : getE(tmp.getKey())) //go over temp's neighbor
                {
                    int destKey = e.getDest();
                    if (getNode(destKey).getTag() != mark) //if the neighbor hasn't been visited
                    {
                        double NewDist = e.getWeight() + tmp.getWeight(); //temp's distance from the ex1.ex1.src+ the edge between the tmp and the neighbor
                        if (NewDist < getNode(destKey).getWeight()) { //if the NewDist < from the present distance of the neighbor
                            getNode(destKey).setWeight(NewDist);
                            re.replace(destKey, tmp); //save tmp ad the node that we came for to this neighbor
                            dist.add(getNode(destKey));
                        }
                    }
                }
                tmp.setTag(mark);
            }
        }

        public double shortestPathDist(int src, int dest) {
            if (getNode(src) == null || getNode(dest) == null) //if the src or the dest isn't at the graph return -1
                return -1;

            Dijkstra(src);
            //if the dest tag remain infinity- he has no path from src to dest
            if (getNode(dest).getWeight() == Double.MAX_VALUE)
                return -1;
            //return the dest tag(his distance from the src)
            return getNode(dest).getWeight();
        }

        public List<NodeData> shortestPath(int src, int dest) {
            List<NodeData> path = new ArrayList<>();
            //If src=dest, the path is only this node
            if (src == dest) {
                path.add(getNode(src));
                return path;
            }
            //If shortest path is (-1), it mean that no path available
            if (shortestPathDist(src, dest) == -1) //if there is no nodes with ex1.ex1.src or dest key return null
                return null;
            path.add(getNode(dest));
            //AWARE! - special rule to this for loop!
            for (NodeData n = re.get(dest); n != null; n = re.get(n.getKey())) //reconstruct the path from dest to ex1.ex1.src
                path.add(n);

            //need to reverse the path, because it was from dest to src
            Collections.reverse(path);
            return path;
        }

        public String toString() {
            String s = "";
            for (NodeData n : getV()) {
                s += "KEY: " + n.getKey() + "\n";
                s += "EDGES: ";
                for (edgeData e : getE(n.getKey())) {
                    s += "(" + e.getSrc() + "->" + e.getDest()+")";
                }
                s += "\n";
            }
            return s;
        }

    }
