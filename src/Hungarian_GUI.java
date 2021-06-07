import javax.swing.*;
import java.awt.*;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.LinkedList;

public class Hungarian_GUI extends JPanel {
    Undirected_Graph graph;
    LinkedList<NodeData> GroupA;
    LinkedList<NodeData> GroupB;

    public Hungarian_GUI(Undirected_Graph g) {
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
    }

    public void paint(Graphics g){
        int max=Math.max(GroupA.size(), GroupB.size());
        System.out.println("max= "+max);
        int w=max*40+(max+1)*60+200;
        setSize(w,500);

        drawNodes(g);
        drawEdges(g);

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
                 if(graph.getEdge(n.getKey(), ni.getKey()).getMatched()){
                     g2.setColor(new Color(201,62,7));
                 }
                 g2.drawLine(x1, y1, x2, y2);
            }
        }
    }

    private void drawNodes(Graphics g) {
        Graphics2D g2= (Graphics2D)g;
        g2.setStroke(new BasicStroke(3));
        int i=1;
        System.out.println("draw A, "+GroupA.size());
        for(NodeData n: GroupA){
            g2.setColor(Color.white);
            if(n.getMatch()){ g2.setColor(new Color(201,62,7)); }
            g2.fillOval(100*i,100,40,40);
            g2.setColor(Color.black);
            g2.drawOval(100*i,100,40,40);
            n.setP(100*i+20, 143);
            g2.setColor(Color.black);
            Font f=new Font("SansSerif", Font.BOLD, 18);
            g2.setFont(f);
            int key=n.getKey();
            g2.drawString(""+key, (100*i)+15, 126);
            i++;
        }

        System.out.println("draw B, "+GroupB.size());

        i=1;
        for(NodeData n: GroupB){
            g2.setColor(Color.WHITE);
            if(n.getMatch()){ g2.setColor(new Color(201,62,7)); }
            g2.fillOval(100*i,300,40,40);
            g2.setColor(Color.BLACK);
            g2.drawOval(100*i,300,40,40);
            n.setP(100*i+20, 298);
            g2.setColor(Color.black);
            Font f=new Font("SansSerif", Font.BOLD, 18);
            g2.setFont(f);
            int key=n.getKey();
            g2.drawString(""+key, (100*i)+15, 326);
            i++;
        }
    }
}

