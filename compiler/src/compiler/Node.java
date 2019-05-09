package compiler;

import java.util.ArrayList;

public class Node
{
    //Node properites
    public static Node root_node = new Node("root");
    public ArrayList<Node> children = new ArrayList<>();
    public String name;
    public Node sibiling;

    //Node GUI properties
    public static ArrayList<ArrayList<Node>> arranged = new ArrayList<ArrayList<Node>>();
    public static int Treedepth;
    public boolean oval = true;
    public int x = 0, y = 0;
    public int size;
    public int depth = 0;

    public Node(String name)
    {
        this.name = name;
        if (name.contains("read") || name.equals("if") || name.contains("assign") || name.contains("repeat") || name.contains("write"))
        {
            this.oval = false;
        }
    }

    //function reduces parse tree to syntax
    public static Node reducer(Node n)
    {
        if (n.children.size() == 1)
        {
            return n.children.get(0);
        }
        else
        {
            n.name = n.children.get(1).name;
            n.children.remove(1);
        }
        return n;
    }

    //function that arranges all nodes into levels according to their depth
    public static void arranger()
    {
        Finddepth(Node.root_node, 0);
        for (int i = 0; i < Node.Treedepth + 1; i++)
        {
            arranged.add(new ArrayList<Node>());
        }
        Size(Node.root_node);
    }

    //function that sets all coordinates of all nodes
    public static void coordnator()
    {
        for (int i = 0; i < Node.Treedepth + 1; i++)
        {
            int temp = 0;
            temp = temp + getParentStartPosition(Node.arranged.get(i).get(0));
            for (int j = 0; j < Node.arranged.get(i).size(); j++)
            {
                if (temp < getParentStartPosition(Node.arranged.get(i).get(j)))
                {
                    temp = getParentStartPosition(Node.arranged.get(i).get(j));
                }
                Node.arranged.get(i).get(j).x = temp;
                Node.arranged.get(i).get(j).y = i * 100;
                temp = temp + 100 * Node.arranged.get(i).get(j).size;
            }
        }
    }

    //function that gets parent starting position
    public static int getParentStartPosition(Node n)
    {
        if (n != Node.root_node)
        {
            for (Node parent : Node.arranged.get(n.depth - 1))
            {
                for (Node child : parent.children)
                {
                    if (child == n)
                    {
                        return parent.x;
                    }
                }
            }
        }
        return 0;
    }

    //function that get the size of all nodes in the tree
    public static int Size(Node n)
    {
        if (n.children.size() == 0)
        {
            n.size = 1;
        }
        else
        {
            for (Node child : n.children)
            {
                n.size = n.size + child.Size(child);
            }
        }
        Node.arranged.get(n.depth).add(n);
        int myparentsize = n.size;
        if (n.sibiling != null)
        {
            return myparentsize + Size(n.sibiling);
        }
        return n.size;
    }

    //function that gets the depth of all nodes in a tree
    public static void Finddepth(Node n, int depth)
    {
        n.depth = depth;
        if (depth > Node.Treedepth)
        {
            Node.Treedepth = depth;
        }
        for (Node child : n.children)
        {
            Finddepth(child, ++depth);
            --depth;
        }
        if (n.sibiling != null)
        {
            Finddepth(n.sibiling, depth);
        }
    }
}
