import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class GUI extends JPanel {
    Undirected_Graph graph;
    LinkedList<NodeData> GroupA;
    LinkedList<NodeData> GroupB;
    boolean EdgeCover;
    int maxGroup;

    public GUI(Undirected_Graph g) {
        this.graph=g;

        GroupA=new LinkedList<>();
        GroupB=new LinkedList<>();

        for (NodeData n: g.get_all_V()){
               if(n.group==Group.A){
                GroupA.add(n);
            }else{
                GroupB.add(n);
            }
        }
        EdgeCover=false;
        maxGroup=Math.max(GroupA.size(), GroupB.size());
    }

    public void setEdgeCover(boolean b){
        EdgeCover=b;
    }

    public void paint(Graphics g){
        setSize(1000,500);
        drawAugmentingPath(g);
        drawNodes(g);

        drawEdges(g);
        drawDetails(g);

    }

    private void drawDetails(Graphics g) {
        Font f=new Font("SansSerif", Font.BOLD, 18);
        g.setFont(f);
        g.setColor(Color.black);
        if(EdgeCover) {
            int size=graph.getAllEdgesCover().size()+graph.getAllMatchedEdges().size();
            g.drawString("τ(G): "+size, 100, 400);
        }
        g.drawString("α(G): "+graph.getAllMatchedEdges().size(), 100, 430);
        g.drawString("ν(G): "+graph.get_all_V().size(), 100, 460);
    }

    private void drawEdges(Graphics g) {
        g.setColor(Color.BLACK);
        Graphics2D g2= (Graphics2D)g;
        g2.setStroke(new BasicStroke(3));
        for (NodeData n: GroupA){
            for (NodeData ni: graph.getNi(n)){
                int x1=n.getP().getX(),
                    y1=n.getP().getY(),
                    x2=ni.getP().getX(),
                    y2=ni.getP().getY();
                 g2.setColor(Color.BLACK);
                 edgeData e=graph.getEdge(n.getKey(), ni.getKey());
                 if(e.getMatched()){
                     g2.setColor(new Color(201,62,7));
                 }
                 else if(e.getEdgeCover()){
                     g2.setColor(new Color(0,79,139));
                 }
                 g2.drawLine(x1, y1, x2, y2);
            }
        }
    }

    private void drawAugmentingPath(Graphics g) {
        Graphics2D g2d= (Graphics2D)g;
        g2d.setStroke(new BasicStroke(10));
        g2d.setColor(new Color(150,150,150));
        List<NodeData> path = graph.getCurrAugmentingPath();
        for(int i = 0;i<path.size()-1;i++){
            int x1=path.get(i).getP().getX(),
                    y1=path.get(i).getP().getY(),
                    x2=path.get(i+1).getP().getX(),
                    y2=path.get(i+1).getP().getY();
            g2d.drawLine(x1,y1,x2,y2);
        }
    }

    private void drawNodes(Graphics g) {
        int part = 900/maxGroup;
        int nodeWidth;
        if(maxGroup<=8) {
            nodeWidth = 40;
        }else {
            nodeWidth = (int) (0.6 * part);
        }


        Graphics2D g2= (Graphics2D)g;
        g2.setStroke(new BasicStroke(3));
        int i=1;

        for(NodeData n: GroupA){
            g2.setColor(Color.white);

            if(n.getMatch()){ g2.setColor(new Color(201,62,7)); }

            g2.fillOval(50+(i*part),100,nodeWidth,nodeWidth);
            g2.setColor(Color.black);
            g2.drawOval(50+(i*part),100,nodeWidth,nodeWidth);
            n.setP((50+(i*part)+nodeWidth/2), 102+nodeWidth);
            g2.setColor(Color.black);
            Font f=new Font("SansSerif", Font.BOLD, (int)(0.45*nodeWidth));
            g2.setFont(f);
            int key=n.getKey();
            g2.drawString(""+key, (int)(50+(i*part)+(0.3*nodeWidth)), 105+nodeWidth/2);
            i++;
        }

        i=1;
        for(NodeData n: GroupB){
            g2.setColor(Color.WHITE);
            if(n.getMatch()){ g2.setColor(new Color(201,62,7)); }
            g2.fillOval(50+(i*part),300,nodeWidth,nodeWidth);
            g2.setColor(Color.BLACK);
            g2.drawOval(50+(i*part),300,nodeWidth,nodeWidth);
            n.setP((50+(i*part)+nodeWidth/2), 299);
            g2.setColor(Color.black);
            Font f=new Font("SansSerif", Font.BOLD, (int)(0.45*nodeWidth));
            g2.setFont(f);
            int key=n.getKey();
            g2.drawString(""+key, (int)(50+(i*part)+0.3*nodeWidth), 305+nodeWidth/2);
            i++;
        }
    }
}

