import javax.swing.*;
import java.awt.*;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.LinkedList;

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

    private void drawNodesUpto8(Graphics g) {
        Graphics2D g2= (Graphics2D)g;
        g2.setStroke(new BasicStroke(3));
        int i=1;
        for(NodeData n: GroupA){
            g2.setColor(Color.white);
            if(n.getMatch()){ g2.setColor(new Color(201,62,7)); }
            g2.fillOval(40+100*i,100,40,40);
            g2.setColor(Color.black);
            g2.drawOval(40+100*i,100,40,40);
            n.setP(100*i+60, 142);
            g2.setColor(Color.black);
            Font f=new Font("SansSerif", Font.BOLD, 18);
            g2.setFont(f);
            int key=n.getKey();
            g2.drawString(""+key, (100*i)+55, 126);
            i++;
        }

        i=1;
        for(NodeData n: GroupB){
            g2.setColor(Color.WHITE);
            if(n.getMatch()){ g2.setColor(new Color(201,62,7)); }
            g2.fillOval(40+100*i,300,40,40);
            g2.setColor(Color.BLACK);
            g2.drawOval(40+100*i,300,40,40);
            n.setP(100*i+60, 299);
            g2.setColor(Color.black);
            Font f=new Font("SansSerif", Font.BOLD, 18);
            g2.setFont(f);
            int key=n.getKey();
            g2.drawString(""+key, (100*i)+55, 326);
            i++;
        }
    }


    private void drawNodes(Graphics g) {
        int part = 950/maxGroup;
        int nodeWidth, space;
        if(maxGroup<=8) {
            nodeWidth = 40;
        }else {
            nodeWidth = (int) (0.4 * part);
        }


        Graphics2D g2= (Graphics2D)g;
        g2.setStroke(new BasicStroke(3));
        int i=1;

        for(NodeData n: GroupA){
            g2.setColor(Color.white);

            if(n.getMatch()){ g2.setColor(new Color(201,62,7)); }

            g2.fillOval(25+(i*part),100,nodeWidth,nodeWidth);
            g2.setColor(Color.black);
            g2.drawOval(25+(i*part),100,nodeWidth,nodeWidth);
            n.setP((int)(25+(i*part)+nodeWidth/2), 102+nodeWidth);
            g2.setColor(Color.black);
            Font f=new Font("SansSerif", Font.BOLD, (int)(0.45*nodeWidth));
            g2.setFont(f);
            int key=n.getKey();
            g2.drawString(""+key, (int)(25+(i*part)+(0.3*nodeWidth)), 105+nodeWidth/2);
            i++;
        }

        i=1;
        for(NodeData n: GroupB){
            g2.setColor(Color.WHITE);
            if(n.getMatch()){ g2.setColor(new Color(201,62,7)); }
            g2.fillOval(25+(i*part),300,nodeWidth,nodeWidth);
            g2.setColor(Color.BLACK);
            g2.drawOval(25+(i*part),300,nodeWidth,nodeWidth);
            n.setP((int)(25+(i*part)+nodeWidth/2), 299);
            g2.setColor(Color.black);
            Font f=new Font("SansSerif", Font.BOLD, (int)(0.45*nodeWidth));
            g2.setFont(f);
            int key=n.getKey();
            g2.drawString(""+key, (int)(25+(i*part)+0.3*nodeWidth), 305+nodeWidth/2);
            i++;
        }
    }
}

