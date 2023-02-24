package service;

import model.Pagamento;
import service.interfaces.IPagamentoCalculavel;
import service.calculaPagamento.CalculaClassificacao;
import service.calculaPagamento.CalculaDesconto;
import service.calculaPagamento.CalculoJuros;
import service.calculaPagamento.CalculoMulta;

import java.io.File;
import java.io.IOException;

public abstract class GeraPagamento {
    protected File file;
    protected String pathProcessados;
    protected static final String PAGAMENTOS_ATUALIZADOS = "pagamentosProcessados_";

    public GeraPagamento(File file, String pathProcessados){
        this.file = file;
        this.pathProcessados = pathProcessados;
    }

    protected abstract void gravarPagamentoProcessado(String dataArquivo, Pagamento pagamento) throws IOException;

    protected <T extends Pagamento>  T processaFatura(T pag){
        IPagamentoCalculavel<Pagamento> pagamentoCalculavel;
        //Juros Mora
        pagamentoCalculavel = new CalculoJuros();
        pagamentoCalculavel.calcularFatura(pag);

        //Multa
        pagamentoCalculavel = new CalculoMulta();
        pagamentoCalculavel.calcularFatura(pag);

        //Classificacao
        pagamentoCalculavel = new CalculaClassificacao();
        pagamentoCalculavel.calcularFatura(pag);

        //Desconto
        pagamentoCalculavel = new CalculaDesconto();
        pagamentoCalculavel.calcularFatura(pag);

        pag.setProcessed(true);

        return pag;
    }
}
