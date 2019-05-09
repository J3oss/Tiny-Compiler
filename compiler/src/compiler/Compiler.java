package compiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Compiler
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        //scanner part
        ArrayList<Token> tokens = new ArrayList<>();
        Scanner sc = new Scanner();
        while (Scanner.isReady())
        {
            Token t = sc.getToken();
            tokens.add(t);
        }

        //parser part
        Parser ps = new Parser(tokens);

        //GUI part
        JFrame j = new JFrame();
        j.setDefaultCloseOperation(3);
        j.add(new Drawer());
        j.setSize(1120, 700);
        j.setVisible(true);
    }
}
