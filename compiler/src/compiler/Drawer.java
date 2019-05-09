package compiler;

import java.awt.Graphics;
import javax.swing.JPanel;

public class Drawer extends JPanel
{
    public Drawer()
    {
        Node.arranger();
        Node.coordnator();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        transverseDraw(Node.root_node, g);
    }

    public void transverseDraw(Node n, Graphics g)
    {
        for (Node child : n.children)
        {
            if (n != Node.root_node)
            {
                g.drawLine(n.x + 50 * n.size, n.y + 50, child.x + 50 * child.size, child.y);
            }
            transverseDraw(child, g);
        }
        if (n.sibiling != null)
        {

            g.drawLine(n.x + 50 * (n.size) + 40, n.y + 25, n.sibiling.x + 50 * (n.sibiling.size - 1), n.sibiling.y + 25);
            transverseDraw(n.sibiling, g);
        }
        if (n != Node.root_node)
        {
            if (n.oval)
            {
                g.drawOval(n.x + 50 * (n.size - 1), n.y, 100 - 10, 50);
            }
            else
            {
                g.drawRect(n.x + 50 * (n.size - 1), n.y, 100 - 10, 50);
            }
            g.drawString(n.name, n.x + 15 + 50 * (n.size - 1), n.y + 30);
        }
    }
}
