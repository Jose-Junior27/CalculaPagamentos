package service;

import service.calculaPagamento.CalculaClassificacao;
import service.calculaPagamento.CalculaDesconto;
import service.calculaPagamento.CalculoJuros;
import service.calculaPagamento.CalculoMulta;
import model.Pagamento;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public abstract class Faturamento {
    protected static final String PATH_PROCESSADOS = "Processados";
    protected static final String PAGAMENTOS_ATUALIZADOS = "pagamentosProcessados_";
    protected String path;
    protected String pathProcessados;

    Faturamento(String path){
        this.path = path + File.separator;
        this.pathProcessados = path + PATH_PROCESSADOS;
    }
    protected abstract void gerarPagamentos() throws Exception;

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

    protected void validaNomeArquivo(String nome){
        nome = nome.substring(0, nome.lastIndexOf('.'));
        try{
            int mes = Integer.parseInt(nome.substring( nome.length() - 7, nome.length() - 5));
            int ano = Integer.parseInt(nome.substring( nome.length() - 4, nome.length() ));
            LocalDate data = LocalDate.of(ano,mes,1);
        }catch (RuntimeException  e){
            throw new RuntimeException("Nome arquivo com padrão inválido " + nome);
        }

    }


}
