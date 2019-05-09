package compiler;

public class Token {

    protected enum tokenType {
        IF, THEN, ELSE, END, REPEATE, UNTIL, READ, WRITE, PLUS, MINUS, MULTIPLY, DIVIDE, EQUAL, LESSTHAN, OPENBRACKET, CLOSEBRACKET, SEMICOLON, ASSIGNOPERATOR, NUMBER, IDENTIFIER, ERROR
    }
    protected enum tokencategory {
        RESRVEDWORD, SPECIALSYMBOL, OTHER
    }

    public String stringVal;
    private tokencategory tokencategoty;
    public tokenType tokenval;

    public Token(String stringVal, tokenType tokenval, tokencategory tokencategoty) {

        this.stringVal = stringVal;
        this.tokencategoty = tokencategoty;
        this.tokenval = tokenval;

        //SEPARATES IDENTIFIERS FROM RESERVED WODS 
        if (this.tokencategoty == tokencategory.OTHER && this.tokenval == tokenType.IDENTIFIER) {
            switch (stringVal) {
                case "if":
                    this.tokencategoty = tokencategory.RESRVEDWORD;
                    this.tokenval = tokenType.IF;
                    break;
                case "then":
                    this.tokencategoty = tokencategory.RESRVEDWORD;
                    this.tokenval = tokenType.THEN;
                    break;
                case "else":
                    this.tokencategoty = tokencategory.RESRVEDWORD;
                    this.tokenval = tokenType.ELSE;
                    break;
                case "end":
                    this.tokencategoty = tokencategory.RESRVEDWORD;
                    this.tokenval = tokenType.END;
                    break;
                case "repeat":
                    this.tokencategoty = tokencategory.RESRVEDWORD;
                    this.tokenval = tokenType.REPEATE;
                    break;
                case "until":
                    this.tokencategoty = tokencategory.RESRVEDWORD;
                    this.tokenval = tokenType.UNTIL;
                    break;
                case "read":
                    this.tokencategoty = tokencategory.RESRVEDWORD;
                    this.tokenval = tokenType.READ;
                    break;
                case "write":
                    this.tokencategoty = tokencategory.RESRVEDWORD;
                    this.tokenval = tokenType.WRITE;
                    break;
                default:
                    break;
            }
        }

        //SEPARATES SPECIALSYMBOLS FROM EACH OTHER AND UNINDENTIFIED SPECIALSYMBOLS(ERROR) SPECIAL SYMBOL
        if (this.tokencategoty == tokencategory.SPECIALSYMBOL) {
            switch (stringVal) {
                case "+":
                    this.tokenval = tokenType.PLUS;
                    break;
                case "-":
                    this.tokenval = tokenType.MINUS;
                    break;
                case "*":
                    this.tokenval = tokenType.MULTIPLY;

                    break;
                case "/":
                    this.tokenval = tokenType.DIVIDE;

                    break;
                case "=":
                    this.tokenval = tokenType.EQUAL;
                    break;
                case "<":
                    this.tokenval = tokenType.LESSTHAN;
                    break;
                case "(":
                    this.tokenval = tokenType.OPENBRACKET;
                    break;
                case ")":
                    this.tokenval = tokenType.CLOSEBRACKET;
                    break;
                case ";":
                    this.tokenval = tokenType.SEMICOLON;
                    break;
                case ":=":
                    this.tokenval = tokenType.ASSIGNOPERATOR;
                    break;
                default:
                    this.tokenval = tokenType.ERROR;
                    break;
            }
        }
    }

    public void printToken() {
        System.out.println("*************************************************");
        System.out.println("Token string : " + stringVal);
        System.out.println("Token category : " + tokencategoty);
        System.out.println("Token type : " + tokenval);
        System.out.println("*************************************************");
    }
}
