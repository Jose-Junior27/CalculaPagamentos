package model;

import java.time.LocalDate;

public class Pagamento {
    private String clienteNome;
    private LocalDate dataVencimento;
    private Double valor;
    private int classificacao;
    private Double jurosMora;
    private Double multa;
    private Double desconto;
    private int pontosDesclassificacao;
    private Double valorTotal;
    private boolean isProcessed;

    public Pagamento(String clienteNome,
                     LocalDate dataVencimento,
                     Double valor,
                     int classificacao){
        this.clienteNome = clienteNome;
        this.dataVencimento = dataVencimento;
        this.valor = valor;
        this.classificacao = classificacao;
        this.jurosMora = .0;
        this.multa = .0;
        this.desconto = .0;
        this.pontosDesclassificacao = 0;
        this.valorTotal = .0;
        this.isProcessed = false;
    }

    public String getClienteNome(){
        return clienteNome;
    }

    public LocalDate getDataVencimento(){
        return dataVencimento;
    }

    public Double getValor(){
        return valor;
    }

    public int getClassificacao(){
        return classificacao;
    }

    public Double getJurosMora() {
        return jurosMora;
    }

    public Double getMulta() {
        return multa;
    }

    public Double getDesconto() {
        return desconto;
    }

    public int getPontosDesclassificacao() {
        return pontosDesclassificacao;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setJurosMora(Double jurosMora) {
        this.jurosMora = jurosMora;
        AtualizaValorTotal();
    }

    public void setMulta(Double multa) {
        this.multa = multa;
        AtualizaValorTotal();
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
        AtualizaValorTotal();
    }

    public void setPontosDesclassificacao(int pontosDesclassificacao) {
        this.pontosDesclassificacao = pontosDesclassificacao;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    @Override
    public String toString() {
        return "model.Pagamento{" +
                "clienteNome='" + clienteNome + '\'' +
                ", dataVencimento=" + dataVencimento +
                ", valor=" + valor +
                ", classificacao=" + classificacao +
                ", isProcessed=" + isProcessed +
                '}';
    }

    private void AtualizaValorTotal(){

        valorTotal = (valor - desconto ) + (jurosMora + multa );
    }

}
