import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class Lexico {

    private String filePath;
    private String fileName;
    private int c;
    BufferedReader bufferedReader;

    public Lexico(String fileName){
        System.out.println(fileName);
        this.filePath = Paths.get(fileName).toAbsolutePath().toString();
        this.fileName = fileName;

        try{
            this.bufferedReader = new BufferedReader(new FileReader(this.filePath,StandardCharsets.UTF_8));
            this.c = this.bufferedReader.read();
        }catch (IOException err){
            System.err.println("Não é possivel abrir o arquivo " + this.fileName);
            err.printStackTrace();
        }
    }

    public Token getToken(){
        StringBuilder lexema = new StringBuilder("");
        char caracter;
        Token token = new Token();

        try {

            while (c != -1) {
                caracter = (char) c;
                if (!(c == 13 || c == 10)) {
                    if (caracter == ' ') {
                        while (caracter == ' ') {
                            c = this.bufferedReader.read();
                            caracter = (char) c;
                        }
                    } else if (Character.isLetter(caracter)) {
                        while (Character.isLetter(caracter) || Character.isDigit(caracter)) {
                            lexema.append(caracter);
                            c = this.bufferedReader.read();
                            caracter = (char) c;
                        }
                        Value valor = new Value();
                        token.setToken(TokenEnum.cId);
                        valor.setValorIdentificador(lexema.toString());
                        token.setValue(valor);
                        return token;
                    } else if (Character.isDigit(caracter)) {
                        while (Character.isDigit(caracter) || caracter=='.') {
                            lexema.append(caracter);
                            c = this.bufferedReader.read();
                            caracter = (char) c;
                        }
                        Value valor = new Value();
                        token.setToken(TokenEnum.cInt);
                        valor.setValorInteiro(Integer.parseInt(lexema.toString()));
                        token.setValue(valor);
                        return token;

                    }else {
                        if(caracter==':'){
                            while(caracter == ':'){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cDoisPontos);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else if(caracter=='+'){
                            while(caracter == '+'){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cMais);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else if(caracter=='-'){
                            while(caracter == '-'){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cMenor);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else if(caracter=='/'){
                            while(caracter == '/'){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cDivisao);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else if(caracter=='*'){
                            while(caracter == '*'){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cMultiplicacao);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else if(caracter=='>'){
                            while(caracter == '>'){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cMaior);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else if(caracter=='<'){
                            while(caracter == '<'){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            valor.setValorIdentificador(lexema.toString());
                            token.setToken(TokenEnum.cMenor);
                            token.setValue(valor);
                            return token;
                        }else if(caracter=='='){
                            while(caracter == '='){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cIgual);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else if(caracter==','){
                            while(caracter == ','){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cVirgula);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else if(caracter==';'){
                            while(caracter == ';'){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cPontoVirgula);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else if(caracter=='.'){
                            while(caracter == '.'){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cPonto);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else if(caracter=='('){
                            while(caracter == '('){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cParEsq);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else if(caracter==')'){
                            while(caracter == ')'){
                                lexema.append(caracter);
                                c = this.bufferedReader.read();
                                caracter = (char) c;
                            }
                            Value valor = new Value();
                            token.setToken(TokenEnum.cParDir);
                            valor.setValorIdentificador(lexema.toString());
                            token.setValue(valor);
                            return token;
                        }else{
                            token.setToken(TokenEnum.cEOF);
                        }

                        token.setValue(null);
                        c = this.bufferedReader.read();
                        return token;
                    }
                }else{
                    c = this.bufferedReader.read();
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
