package compiler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

public class Scanner {

    private static FileReader fr;
    private static PushbackReader ps;

    public Scanner() throws FileNotFoundException {
        fr = new FileReader("tinyprogram.txt");
        ps = new PushbackReader(fr);
    }

    //SCANNER ATTRIBUTES
    private enum state {
        START, INNUM, INID, DONE, INCOMMENT, INASSIGN
    }
    private state currentState;
    private char c;

    //OUTPUT TOKEN ATTRIBUTES
    private String stringvalue;
    private Token.tokenType tokenval;
    private Token.tokencategory tokencategoty;

    //MAIN LOOP
    public Token getToken() throws IOException {
        currentState = state.START;
        stringvalue = "";
        tokenval = null;
        tokencategoty = null;

        while (currentState != state.DONE) {
            switch (currentState) {
                case START:
                    start();
                    break;

                case INNUM:
                    innum();
                    break;

                case INID:
                    inid();
                    break;

                case INASSIGN:
                    inassign();
                    break;

                case INCOMMENT:
                    incomment();
                    break;

                case DONE:
                    break;

                default:
                    break;
            }
        }
        Token t = new Token(stringvalue, tokenval, tokencategoty);
        return t;
    }

    //STATE TRANSITONS
    private void start() throws IOException {
        c = (char) ps.read();
        if (c == 0 || c == 9 || c == 32 || c == 13 || c == 13 || c == 10 || c == 65279) //white spce charecters
        {
            currentState = state.START;
        } else if (Character.isDigit(c)) {
            stringvalue = stringvalue + c;
            currentState = state.INNUM;
        } else if (Character.isLetter(c)) {
            stringvalue = stringvalue + c;
            currentState = state.INID;
        } else if (c == ':') {
            stringvalue = stringvalue + c;
            currentState = state.INASSIGN;
        } else if (c == '{') {
            currentState = state.INCOMMENT;
        } else {
            stringvalue = stringvalue + c;
            currentState = state.DONE;
            tokencategoty = Token.tokencategory.SPECIALSYMBOL;
        }
    }

    private void innum() throws IOException {
        c = (char) ps.read();
        if (!Character.isDigit(c)) {
            ps.unread(c);
            tokenval = Token.tokenType.NUMBER;
            tokencategoty = Token.tokencategory.OTHER;
            currentState = state.DONE;
        } else {
            stringvalue = stringvalue + c;
        }
    }

    private void inid() throws IOException {
        c = (char) ps.read();
        if (!(Character.isLetter(c))) {
            ps.unread(c);
            tokenval = Token.tokenType.IDENTIFIER;
            tokencategoty = Token.tokencategory.OTHER;
            currentState = state.DONE;
        } else {
            stringvalue = stringvalue + c;
        }
    }

    private void inassign() throws IOException {
        c = (char) ps.read();
        tokencategoty = Token.tokencategory.SPECIALSYMBOL;
        if (c != '=') {
            ps.unread(c);
            currentState = state.DONE;
        } else {
            tokenval = Token.tokenType.ASSIGNOPERATOR;
            stringvalue = stringvalue + c;
        }
    }

    private void incomment() throws IOException {
        c = (char) ps.read();
        if (c == '}') {
            currentState = state.START;
        }
    }

    //FUNCTION THAT REMOVES STRANGE END OF FILE CHARECTER
    public static boolean isReady() throws IOException {
        char r = (char) ps.read();
        if (r != (char) 65535) {
            ps.unread(r);
            return true;
        } else {
            return false;
        }
    }
}
