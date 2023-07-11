import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Sintatico {
    private final static int SIZEOF_INT = 4;
    private int nivel;
    private TabelaSimbolos tabela;

    private int address;
    private int offsetVariavel;
    private int contRotulo = 1;
    private List<Registro> ultimasVariaveisDeclaradas = new ArrayList<>();
    private Tipo ultimoTipoUsado = null;
    private List<String> sectionData = new ArrayList<>();

    private String nomeArquivoSaida;
    private String caminhoArquivoSaida;
    private BufferedWriter bw;
    private FileWriter fw;

    private String rotulo = "";
    private String rotFim;
    private String rotElse;
    private String operadorRelacional;
    private boolean writeln;
    // ------
    private Lexico lexico;
    private  Token token;
    private int line;

    private int column;

    public void read(){
        token = lexico.getToken(line, column);
        column = token.getColumn() + token.getTokenLenght();
        line = token.getLine();
        System.out.println(token);
    }

    public Sintatico(String fileName) {
        line = 1;
        column = 1;
        this.lexico = new Lexico(fileName);
    }

    public void analyse() {
        read();
        address=1;
        nomeArquivoSaida = "exitCode/compiladores.c";
        caminhoArquivoSaida = Paths.get(nomeArquivoSaida).toAbsolutePath().toString();
        bw = null;
        fw = null;
        try {
            fw = new FileWriter(caminhoArquivoSaida, Charset.forName("UTF-8"));
            bw = new BufferedWriter(fw);
            program();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gerarCodigo(String instrucoes) {
        try {
            bw.write(instrucoes + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void program() {
        System.out.println("program");
        if (validaPrograma("program")) {
            read();
            if (token.getToken() == TokenEnum.cId) {
                read();
                A1();
                corpo();
                if (token.getToken() == TokenEnum.cPonto) {
                    read();
                } else {
                    showError(" - faltou encerrar com ponto.");
                }
                A2();
            } else {
                showError(" - faltou identificar o nome do programa.");
            }
        } else {
            showError(" - faltou começar com programa.");
        }
    }

    public void corpo() {
        System.out.println("dentro corpo");
        declara();
        if (validaPrograma("begin")) {
            read();
            sentencas();
            if (validaPrograma("end")) {
                read();
            } else {
                showError(" - faltou finalizar com end.");
            }
        } else {
            showError(" - faltou começar com begin.");
        }
    }

    public void declara() {
        if (validaPrograma("var")) {
            read();
            dvar();
            maisDc();
        }
    }

    public void maisDc() {
        if (token.getToken() == TokenEnum.cPontoVirgula) {
            read();
            contDc();
        } else {
            showError(" - faltou colocar o ponto e virgula.");
        }
    }

    public void contDc() {
        if (token.getToken() == TokenEnum.cId) {
            dvar();
            maisDc();
        }
    }

    public void dvar() {
        variaveis();
        if (token.getToken() == TokenEnum.cDoisPontos) {
            read();
            tipo_var();
        } else {
            showError(" - faltou colocar o dois pontos.");
        }
    }

    public void tipo_var() {
        if (validaPrograma("integer")) {
            A3("int");
            read();
        } else if (validaPrograma("real")) {
            A3("float");
            read();
        } else {
            showError(" - faltou a declaração do integer.");
        }
    }

    public void variaveis() {
        if (token.getToken() == TokenEnum.cId) {
            A4();
            read();
            mais_var();
        } else {
            showError(" - faltou identificador.");
        }
    }

    public void mais_var() {
        if (token.getToken() == TokenEnum.cVirgula) {
            read();
            variaveis();
        }
    }

    public void sentencas() {
        comando();
        mais_sentencas();
    }

    public void mais_sentencas() {
        if (token.getToken() == TokenEnum.cPontoVirgula) {
            read();
            cont_sentencas();
        } else {
            showError(" - faltou o ponto e virgula");
        }
    }

    public void cont_sentencas() {
        if (validaPrograma("read") ||
                validaPrograma("write") ||
                validaPrograma("for") ||
                validaPrograma("repeat") ||
                validaPrograma("while") ||
                validaPrograma("if") ||
                ((token.getToken() == TokenEnum.cId))) {
            sentencas();
        }
    }

    public void var_read() {
        if (token.getToken() == TokenEnum.cId) {
            read();
            mais_var_read();
        } else {
            showError(" - faltou o identificador.");
        }
    }

    public void mais_var_read() {
        if (token.getToken() == TokenEnum.cVirgula) {
            read();
            var_read();
        }
    }

    public void var_write() {
        if (token.getToken() == TokenEnum.cId) {
            read();
            mais_var_write();
        } else {
            showError(" -sem identificador.");
        }
    }

    public void mais_var_write() {
        if (token.getToken() == TokenEnum.cVirgula) {
            read();
            var_write();
        }
    }

    public void comando() {
        if (validaPrograma("read")) {
            read();
            if (token.getToken() == TokenEnum.cParEsq) {
                read();
                var_read();
                if (token.getToken() == TokenEnum.cParDir) {
                    read();
                } else {
                    showError(" - sem parentese direito.");
                }
            } else {
                showError(" - sem parentese esquerdo.");
            }
        } else if (validaPrograma("write")) {
            read();
            if (token.getToken() == TokenEnum.cParEsq) {
                read();
                var_write();
                if (token.getToken() == TokenEnum.cParDir) {
                    read();
                } else {
                    showError(" - faltou o parentese direito.");
                }
            } else {
                showError(" - faltou o parentese esquerdo.");
            }
        } else

        if (validaPrograma("for")) {
            read();
            if (token.getToken() == TokenEnum.cId) {
                read();

                if (token.getToken() == TokenEnum.cAtribuicao) {
                    read();
                    expressao();
                    if (validaPrograma("to")) {
                        read();
                        expressao();
                        if (validaPrograma("do")) {
                            read();
                            if (validaPrograma("begin")) {
                                read();
                                sentencas();
                                if (validaPrograma("end")) {
                                    read();
                                } else {
                                    showError(" - faltou o end no for.");
                                }
                            } else {
                                showError(" - faltou o begin no for.");
                            }
                        } else {
                            showError(" - faltou o do no for.");
                        }
                    } else {
                        showError(" - faltou o to no for.");
                    }
                } else {
                    showError(" - faltou o dois pontos e igual no for");
                }
            } else {
                showError(" - faltou o identificador no inicio do for.");
            }
        } else

        if (validaPrograma("repeat")) {
            read();
            sentencas();
            if (validaPrograma("until")) {
                read();
                if (token.getToken() == TokenEnum.cParEsq) {
                    read();
                    condicao();
                    if (token.getToken() == TokenEnum.cParDir) {
                        read();
                    } else {
                        showError(" - faltou fechar o parentese no repeat");
                    }
                } else {
                    showError(" - faltou abrir parentese no repeat.");
                }
            } else {
                showError(" - faltou until no repeat.");
            }
        }

        else if (validaPrograma("while")) {
            read();
            if (token.getToken() == TokenEnum.cParEsq) {
                read();
                condicao();
                if (token.getToken() == TokenEnum.cParDir) {
                    read();
                    if (validaPrograma("do")) {
                        read();
                        if (validaPrograma("begin")) {
                            read();
                            sentencas();
                            if (validaPrograma("end")) {
                                read();
                            } else {
                                showError(" - faltou end no while.");
                            }
                        } else {
                            showError(" - faltou begin no while.");
                        }
                    } else {
                        showError(" - faltou do no while.");
                    }
                } else {
                    showError(" - faltou o parentese direito no while.");
                }
            } else {
                showError(" - faltou o parentese esquerdo no while.");
            }
        } else if (validaPrograma("if")) {
            read();
            if (token.getToken() == TokenEnum.cParEsq) {
                read();
                condicao();
                if (token.getToken() == TokenEnum.cParDir) {
                    read();
                    if (validaPrograma("then")) {
                        read();
                        if (validaPrograma("begin")) {
                            read();
                            sentencas();
                            if (validaPrograma("end")) {
                                read();
                                pfalsa();
                            } else {
                                showError(" - faltou end no if.");
                            }
                        } else {
                            showError(" - faltou begin no if.");
                        }
                    } else {
                        showError(" - faltou o then no if.");
                    }
                } else {
                    showError(" - faltou o parentese direito no if.");
                }
            } else {
                showError(" - faltou o parentese esquerdo no if.");
            }
        } else if (token.getToken() == TokenEnum.cId) {
            read();
            if (token.getToken() == TokenEnum.cAtribuicao) {
                read();
                expressao();
            } else {
                showError(" - faltou atribuição.");
            }
        }
    }

    public void condicao() {
        expressao();
        relacao();
        expressao();
    }

    public void pfalsa() {
        if (validaPrograma("else")) {
            read();
            if (validaPrograma("begin")) {
                read();
                sentencas();
                if (validaPrograma("end")) {
                    read();
                } else {
                    showError(" - faltou finalizar com end.");
                }
            } else {
                showError(" - faltou inicia com begin.");
            }
        }
    }

    public void relacao() {
        if (validaTipos(1, TokenEnum.cIgual, TokenEnum.cIgual)) {
            read();
        } else if (validaTipos(1, TokenEnum.cMaior, TokenEnum.cMaior)) {
            read();
        } else if (validaTipos(1, TokenEnum.cMenor, TokenEnum.cMenor)) {
            read();
        } else if (validaTipos(1, TokenEnum.cMaiorIgual, TokenEnum.cMaiorIgual)) {
            read();
        } else if (validaTipos(1, TokenEnum.cMenorIgual, TokenEnum.cMenorIgual)) {
            read();
        } else if (validaTipos(1, TokenEnum.cDiferente, TokenEnum.cDiferente)) {
            read();
        } else {
            showError(" - faltou o operador de relação.");
        }
    }

    public void expressao() {
        termo();
        outros_termos();
    }

    public void outros_termos() {
        if (validaTipos(2, TokenEnum.cMais, TokenEnum.cMenos)) {
            op_ad();
            termo();
            outros_termos();
        }
    }

    public void op_ad() {
        if (validaTipos(2, TokenEnum.cMais, TokenEnum.cMenos)) {
            read();
        } else {
            showError(" - faltou colocar o operador de adição ou subtração.");
        }
    }

    public void termo() {
        fator();
        mais_fatores();
    }

    public void mais_fatores() {
        if (validaTipos(2, TokenEnum.cMultiplicacao, TokenEnum.cDivisao)) {
            op_mul();
            fator();
            mais_fatores();
        }
    }

    public void op_mul() {
        if (validaTipos(2, TokenEnum.cMultiplicacao, TokenEnum.cDivisao)) {
            read();
        } else {
            showError(" - faltou colocar o operador de multiplicação ou divisão.");
        }
    }

    public void fator() {
        if (validaTipos(1, TokenEnum.cId, TokenEnum.cId)) {
            read();
        } else if (validaTipos(2, TokenEnum.cInt, TokenEnum.cReal)) {
            read();
        } else if (validaTipos(1, TokenEnum.cParEsq, TokenEnum.cParEsq)) {
            read();
            expressao();
            if (validaTipos(1, TokenEnum.cParDir, TokenEnum.cParDir)) {
                read();
            } else {
                showError(" - faltou o parentese direito.");
            }
        } else {
            showError(" - faltou fator in num exp.");
        }
    }

    public Boolean validaTipos(int numeroDeValidacoes, TokenEnum token1, TokenEnum token2) {
        if (numeroDeValidacoes == 1) {
            return token.getToken() == token1;
        }

        return token.getToken() == token1 || token.getToken() == token2;
    }

    public Boolean validaPrograma(String valor) {
        return (token.getToken() == TokenEnum.cPalRes)
                && (token.getValue().getValorIdentificador().equalsIgnoreCase(valor));
    }

    public void showError(String mensagem) {
        System.out.println("Error na linha: " + token.getLine() + " e na coluna: " + token.getColumn() + mensagem);
    }

    private void A1() {
        // Criar uma tabela de símbolos.
        tabela = new TabelaSimbolos();
        // Fazer o campo tabelaPai ser null.
        tabela.setTabelaPai(null);
        // Inserir o ID na tabela de símbolos.
        Registro registro = new Registro();
        registro.setNome(token.getValue().getValorIdentificador());
        // Incializar o campo categoria com a informação que se trata do programa
        // principal.
        registro.setCategoria(Categoria.PROGRAMAPRINCIPAL);
        // Incializar o campo nivel com zero.
        registro.setNivel(0);
        registro.setOffset(0);
        registro.setTabelaSimbolos(tabela);
        // Incializar o campo rotulo com "main".
        registro.setRotulo("main");
        tabela.inserirRegistro(registro);
        // Inicializar nivel com zero.
        nivel = 0;
        // Declarar a variável offsetVariavel e iniciá-la com zero.
        offsetVariavel = 0;
        // Gerar o cabeçalho do programa, contendo a seção de instruções (global,
        // section .text, etc.).
        String codigo = "#include <stdio.h>" +
                "\n\nint main(void){ \n";

        gerarCodigo(codigo);
    }

    private void A2() {
        Registro registro=new Registro();
        registro.setNome(null);
        registro.setCategoria(Categoria.PROGRAMAPRINCIPAL);
        registro.setNivel(0);
        registro.setOffset(0);
        registro.setTabelaSimbolos(tabela);
        registro.setRotulo("finalCode");
        tabela.inserirRegistro(registro);
        nivel=0;
        offsetVariavel=0;
        String codigo = "\n\treturn 0;\n}\n";
        gerarCodigo(codigo);
    }

    private void A3(String type) {
        String codigo= '\t'+type;
        for(int i=0;i<this.ultimasVariaveisDeclaradas.size();i++)
        {
            codigo=codigo+' '+ this.ultimasVariaveisDeclaradas.get(i).getNome();
            if(i == this.ultimasVariaveisDeclaradas.size()-1)
            {
                codigo=codigo + ';';
            }
            else{
                codigo=codigo + ',';
            }
        }
        gerarCodigo(codigo);
    }

    private void A4(){
        Registro registro=new Registro();
        registro.setNome(token.getValue().getValorIdentificador());
        registro.setCategoria(Categoria.VARIAVEL);
        registro.setNivel(0);
        registro.setOffset(0);
        registro.setTabelaSimbolos(tabela);
        this.address++;
        registro.setRotulo("variavel"+this.address);
        ultimasVariaveisDeclaradas.add(registro);
        this.tabela.inserirRegistro(registro);
    }
}
