import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Lexico {
    private String fileName;
    private int c;
    BufferedReader bufferedReader;
    PushbackReader br;

    private ArrayList<String> reserved = new ArrayList<String>(
            Arrays.asList(
                    "and", "array", "begin", "case", "const", "div",
                    "do", "downto", "else", "end", "file", "for",
                    "function", "goto", "if", "in", "label", "mod",
                    "nil", "not", "of", "or", "packed", "procedure",
                    "program", "record", "repeat", "set", "then",
                    "to", "type", "until", "var", "while", "with",
                    "read", "write", "real", "integer"));

    public Lexico(String fileName){
        System.out.println(fileName);
        String filePath = Paths.get(fileName).toAbsolutePath().toString();
        this.fileName = fileName;

        try{
            this.bufferedReader = new BufferedReader(new FileReader(filePath,StandardCharsets.UTF_8));
            this.br = new PushbackReader(bufferedReader);
            this.c = this.br.read();
        }catch (IOException err){
            System.err.println("Não é possivel abrir o arquivo " + this.fileName);
            err.printStackTrace();
        }
    }

    private void updateToken(Token token, int line, int column, int tokenLenth,
                                       int qtdSpaces) {
        token.setTokenLength(tokenLenth);
        token.setColumn(column + qtdSpaces);
        token.setLine(line);
    }

    public Token getToken(int line, int column){
        StringBuilder lexema = new StringBuilder();
        char caracter;
        Token token = new Token();
        int tokenLenght = 0;
        int qtdSpaces = 0;

        try {

            while (c != -1) {
                caracter = (char) c;
                if (!(c == 13 || c == 10)) {
                    if (caracter == ' ') {
                        while (caracter == ' ') {
                            c = this.br.read();
                            caracter = (char) c;
                            qtdSpaces++;
                        }
                    } else if (Character.isLetter(caracter)) {
                        while (Character.isLetter(caracter) || Character.isDigit(caracter)) {
                            lexema.append(caracter);
                            c = this.br.read();
                            caracter = (char) c;
                            tokenLenght++;
                        }
                        if (reserved.contains(lexema.toString().toLowerCase())) {
                            token.setToken(TokenEnum.cPalRes);
                        } else {
                            token.setToken(TokenEnum.cId);
                        }
                        updateToken(token,line,column,tokenLenght,qtdSpaces);
                        Value valor = new Value();
                        valor.setValorIdentificador(lexema.toString());
                        token.setValue(valor);
                        return token;
                    } else if (Character.isDigit(caracter)) {
                        int qtdPoint = 0;
                        while (Character.isDigit(caracter) || caracter=='.') {
                            if(caracter == '.') {
                                qtdPoint++;
                            }
                            lexema.append(caracter);
                            c = this.br.read();
                            tokenLenght ++;
                            caracter = (char) c;
                        }
                        if(qtdPoint <= 1){
                            Value valor = new Value();
                            if(qtdPoint == 0){
                                token.setToken(TokenEnum.cInt);
                                valor.setValorInteiro(Integer.parseInt(lexema.toString()));
                                token.setValue(valor);
                            }else {
                                token.setToken(TokenEnum.cReal);
                                valor.setValorDecimal(Float.parseFloat(lexema.toString()));
                                token.setValue(valor);
                            }
                            updateToken(token,line,column,tokenLenght,qtdSpaces);
                            return token;
                        }
                    }else {
                        tokenLenght++;
                        if (caracter == ':' | caracter == '>' | caracter == '<') {
                            if (caracter == ':') {
                                int next = this.br.read();
                                caracter = (char) next;
                                if (caracter == '=') {
                                    tokenLenght++;
                                    token.setToken(TokenEnum.cAtribuicao);
                                } else {
                                    this.br.unread(next);
                                    token.setToken(TokenEnum.cDoisPontos);
                                }
                            } else if (caracter == '>') {
                                int proximo = this.br.read();
                                caracter = (char) proximo;
                                if (caracter == '=') {
                                    tokenLenght++;
                                    token.setToken(TokenEnum.cMaiorIgual);
                                } else {
                                    this.br.unread(proximo);
                                    token.setToken(TokenEnum.cMaior);
                                }
                            } else if (caracter == '<') {
                                int proximo = this.br.read();
                                caracter = (char) proximo;
                                if (caracter == '=') {
                                    tokenLenght++;
                                    token.setToken(TokenEnum.cMenorIgual);
                                } else if (caracter == '>') {
                                    tokenLenght++;
                                    token.setToken(TokenEnum.cDiferente);
                                } else {
                                    this.br.unread(proximo);
                                    token.setToken(TokenEnum.cMenor);
                                }
                            }
                        } else {
                            if (caracter == '+') {
                                token.setToken(TokenEnum.cMais);
                            } else if (caracter == '-') {
                                token.setToken(TokenEnum.cMenos);
                            } else if (caracter == '/') {
                                token.setToken(TokenEnum.cDivisao);
                            } else if (caracter == '*') {
                                token.setToken(TokenEnum.cMultiplicacao);
                            } else if (caracter == '=') {
                                token.setToken(TokenEnum.cIgual);
                            } else if (caracter == ',') {
                                token.setToken(TokenEnum.cVirgula);
                            } else if (caracter == ';') {
                                token.setToken(TokenEnum.cPontoVirgula);
                            } else if (caracter == '.') {
                                token.setToken(TokenEnum.cPonto);
                            } else if (caracter == '(') {
                                token.setToken(TokenEnum.cParEsq);
                            } else if (caracter == ')') {
                                token.setToken(TokenEnum.cParDir);
                            } else {
                                token.setToken(TokenEnum.cEOF);
                            }
                        }
                        token.setValue(null);
                        updateToken(token,line,column,tokenLenght,qtdSpaces);
                        c = this.br.read();
                        tokenLenght++;
                        return token;
                    }
                }else{
                    c = this.br.read();
                    line++;
                    qtdSpaces = 0;
                    tokenLenght = 0;
                    column = 1;
                }
            }

            token.setToken(TokenEnum.cEOF);
            return token;
        } catch (IOException err) {
            System.err.println("Não é possivel abrir o arquivo " + this.fileName);
            err.printStackTrace();
        }

        return null;
    }
}
