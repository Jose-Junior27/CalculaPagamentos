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
    protected String path;
    protected String pathProcessados;

    Faturamento(String path){
        this.path = path + File.separator;
        this.pathProcessados = path + PATH_PROCESSADOS;
    }
    protected abstract void gerarPagamentos() throws Exception;

}
