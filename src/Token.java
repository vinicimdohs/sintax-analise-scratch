public class Token {
    private TokenEnum token;
    private Value value;
    private int line;
    private int column;
    private int tokenLenght;

    public TokenEnum getToken() {
        return token;
    }

    public void setToken(TokenEnum token) {
        this.token = token;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public int getLine(){
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public  int getColumn(){
        return column;
    }

    public void setColumn(int column){
        this.column = column;
    }

    public int getTokenLenght() {
        return tokenLenght;
    }
    public  void setTokenLength(int length){
        this.tokenLenght = length;
    }

    @Override
    public String toString() {
        return "Token { classe..: " + token + ", valor..: " + value + ", linha..:" + line + ", coluna..:" + column
                + " }";
    }
}
