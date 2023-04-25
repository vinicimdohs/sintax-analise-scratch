public class Value {
    private int valorInteiro;
    private double valorDecimal;
    private String valorIdentificador;

    public int getValorInteiro() {
        return valorInteiro;
    }
    public void setValorInteiro(int valorInteiro) {
        this.valorInteiro = valorInteiro;
    }
    public double getValorDecimal() {
        return valorDecimal;
    }
    public void setValorDecimal(double valorDecimal) {
        this.valorDecimal = valorDecimal;
    }
    public String getValorIdentificador() {
        return valorIdentificador;
    }
    public void setValorIdentificador(String valorIdentificador) {
        this.valorIdentificador = valorIdentificador;
    }

    @Override
    public String toString() {
        return "Valor [ valorInteiro..: " + valorInteiro + ", valorDecimal..: " + valorDecimal + ", valorIdentificador..: "
                + valorIdentificador + " ]";
    }
}
