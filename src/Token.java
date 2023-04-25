public class Token {
    private TokenEnum token;
    private Value value;

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

    @Override
    public String toString() {
        return "Token { classe..: " + token + ", valor..: " + value + " }";
    }
}
