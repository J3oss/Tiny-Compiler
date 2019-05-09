package compiler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Parser
{

    private ArrayList<Token> tokens = new ArrayList<>();
    private Token currentToken;

    private int token_counter = 0, error_counter = 0;
    public BufferedWriter bw;

    public Parser(ArrayList<Token> tokens) throws IOException
    {
        bw = new BufferedWriter(new FileWriter("parser_output.txt"));
        this.currentToken = tokens.get(token_counter);
        this.tokens = tokens;
        program();
        bw.close();
    }
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    private void program() throws IOException
    {
        bw.write("program is found \n");
        Node.root_node.children.add(stmtSequence());
        finalLogger();
    }

    private void match(Token token, Token.tokenType expectedType) throws IOException
    {
        if (token.tokenval == expectedType)
        {
            bw.write(expectedType.toString() + " was found \n");
            getNextToken();
        }
        else
        {
            bw.write(expectedType.toString() + " was not found \n");
            errorLogger(expectedType.toString());
        }
    }

    private void getNextToken()
    {
        if (token_counter < tokens.size() - 1)
        {
            token_counter++;
            currentToken = tokens.get(token_counter);
        }
    }

    private void errorLogger(String expecting) throws IOException
    {
        error_counter++;
        bw.write(error_counter + ") error expecting " + expecting + "\n");
        System.out.print(error_counter + ") error expecting " + expecting);
        bw.write(" between " + tokens.get(token_counter - 1).stringVal + " and " + tokens.get(token_counter).stringVal + "\n");
        System.out.println(" between " + tokens.get(token_counter - 1).stringVal + " and " + tokens.get(token_counter).stringVal);
    }

    private void finalLogger() throws IOException
    {
        bw.write((token_counter + 1) + " compiled tokens out of " + tokens.size() + " total tokens" + "\n");
        System.out.println((token_counter + 1) + " compiled tokens out of " + tokens.size() + " total tokens");
        if (token_counter == tokens.size() - 1 && error_counter == 0)
        {
            bw.write("all code compiled successfully");
            System.out.println("all code compiled successfully");
        }
        else
        {
            bw.write("code didn't compile successfully");
            System.out.println("code didn't compile successfully");
            if (token_counter < tokens.size() - 1 && error_counter == 0)
            {
                bw.write("please check semiclons");
                System.out.println("please check semiclons");
            }
            else
            {
                bw.write("please correct all errors");
                System.out.println("please correct all errors");
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    private Node stmtSequence() throws IOException
    {
        bw.write("stmt_seq is found\n");
        Node root, temp;
        root = statment();
        temp = root;
        while (currentToken.tokenval == Token.tokenType.SEMICOLON && token_counter < tokens.size() - 1)
        {
            match(currentToken, Token.tokenType.SEMICOLON);
            temp.sibiling = statment();
            temp = temp.sibiling;
        }
        return root;
    }

    private Node statment() throws IOException
    {
        bw.write("Statement is found\n");
        switch (currentToken.tokenval)
        {
            case IF:
                return ifStmt();
            case REPEATE:
                return repeatStmt();
            case READ:
                return readStmt();
            case WRITE:
                return writeStmt();
            case IDENTIFIER:
                return assignStmt();
            default:
                errorLogger("if,repeate,read,write or indentifier");
                Node n_e = new Node("ERROR");
                return n_e;
        }
    }

    private Node ifStmt() throws IOException
    {
        bw.write("if_stmt is found\n");
        Node temp = new Node("if");
        match(currentToken, Token.tokenType.IF);
        temp.children.add(exp());
        match(tokens.get(token_counter), Token.tokenType.THEN);
        temp.children.add(stmtSequence());
        if (currentToken.tokenval == Token.tokenType.ELSE)
        {
            match(currentToken, Token.tokenType.ELSE);
            temp.children.add(stmtSequence());
        }
        match(tokens.get(token_counter), Token.tokenType.END);
        return temp;
    }

    private Node repeatStmt() throws IOException
    {
        bw.write("repeat_stmt is found\n");
        Node temp = new Node("repeat");
        match(currentToken, Token.tokenType.REPEATE);
        temp.children.add(stmtSequence());
        match(currentToken, Token.tokenType.UNTIL);
        temp.children.add(exp());
        return temp;
    }

    private Node assignStmt() throws IOException
    {
        bw.write("assign_stmt is found\n");
        Node temp = new Node("ass(" + currentToken.stringVal + ")");
        match(currentToken, Token.tokenType.IDENTIFIER);
        match(currentToken, Token.tokenType.ASSIGNOPERATOR);
        temp.children.add(exp());
        return temp;
    }

    private Node readStmt() throws IOException
    {
        bw.write("read_stmt is found\n");
        match(currentToken, Token.tokenType.READ);
        Node temp = new Node("read (" + currentToken.stringVal + ")");
        match(currentToken, Token.tokenType.IDENTIFIER);
        return temp;
    }

    private Node writeStmt() throws IOException
    {
        bw.write("write_stmt is found\n");
        Node temp = new Node("write");
        match(currentToken, Token.tokenType.WRITE);
        temp.children.add(exp());
        return temp;
    }
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    private Node factor() throws IOException
    {
        bw.write("factor is found\n");
        switch (currentToken.tokenval)
        {
            case NUMBER:
                match(currentToken, Token.tokenType.NUMBER);
                return new Node("const (" + tokens.get(token_counter - 1).stringVal + ")");
            case IDENTIFIER:
                match(currentToken, Token.tokenType.IDENTIFIER);
                return new Node("id (" + tokens.get(token_counter - 1).stringVal + ")");
            case OPENBRACKET:
                match(tokens.get(token_counter), Token.tokenType.OPENBRACKET);
                Node temp = exp();
                match(tokens.get(token_counter), Token.tokenType.CLOSEBRACKET);
                return temp;
            default:
                bw.write("number ,indentifier or (\n");
                errorLogger("number ,indentifier or (");
                Node n_e = new Node("ERROR");
                return n_e;
        }
    }

    private Node term() throws IOException
    {
        bw.write("term is found\n");
        Node temp = new Node("temp");
        temp.children.add(factor());
        while (currentToken.tokenval == Token.tokenType.MULTIPLY || currentToken.tokenval == Token.tokenType.DIVIDE)
        {
            temp.children.add(mulOP());
            temp.children.add(factor());
        }
        return Node.reducer(temp);
    }

    private Node exp() throws IOException
    {
        bw.write("exp is found\n");
        Node temp = new Node("temp");
        temp.children.add(simpleExp());
        while (currentToken.tokenval == Token.tokenType.EQUAL || currentToken.tokenval == Token.tokenType.LESSTHAN)
        {
            temp.children.add(comparisonOP());
            temp.children.add(simpleExp());
        }
        return Node.reducer(temp);
    }

    private Node simpleExp() throws IOException
    {
        bw.write("simple_exp is found\n");
        Node temp = new Node("temp");
        temp.children.add(term());
        while (currentToken.tokenval == Token.tokenType.PLUS || currentToken.tokenval == Token.tokenType.MINUS)
        {
            temp.children.add(addOP());
            temp.children.add(term());
        }
        return Node.reducer(temp);
    }
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    private Node mulOP() throws IOException
    {
        switch (currentToken.tokenval)
        {
            case MULTIPLY:
                match(tokens.get(token_counter), Token.tokenType.MULTIPLY);
                return new Node("op (*)");
            case DIVIDE:
                match(tokens.get(token_counter), Token.tokenType.DIVIDE);
                return new Node("op (/)");
            default:
                errorLogger("* or /");
                Node n_e = new Node("ERROR");
                return n_e;
        }
    }

    private Node comparisonOP() throws IOException
    {
        switch (currentToken.tokenval)
        {
            case LESSTHAN:
                match(tokens.get(token_counter), Token.tokenType.LESSTHAN);
                return new Node("op (<)");
            case EQUAL:
                match(tokens.get(token_counter), Token.tokenType.EQUAL);
                return new Node("op (=)");
            default:
                errorLogger("< or =");
                Node n_e = new Node("ERROR");
                return n_e;
        }
    }

    private Node addOP() throws IOException
    {
        switch (currentToken.tokenval)
        {
            case PLUS:
                match(currentToken, Token.tokenType.PLUS);
                return new Node("op (+)");
            case MINUS:
                match(currentToken, Token.tokenType.MINUS);
                return new Node("op (-)");
            default:
                errorLogger("+ or -");
                Node n_e = new Node("ERROR");
                return n_e;
        }
    }
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
}
