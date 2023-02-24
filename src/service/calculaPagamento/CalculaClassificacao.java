package service.calculaPagamento;

import model.Pagamento;
import service.interfaces.IPagamentoCalculavel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculaClassificacao implements IPagamentoCalculavel<Pagamento> {
    private static final int MAX_PONTOS_CLASSIFICACAO = 10;

    @Override
    public void calcularFatura(Pagamento pag) {
        long mesAtraso;
        mesAtraso = pag.getDataVencimento().until(LocalDate.now(), ChronoUnit.MONTHS);
        if (mesAtraso > 0) {
            if (mesAtraso <= MAX_PONTOS_CLASSIFICACAO) {
                pag.setPontosDesclassificacao(pag.getClassificacao() - (int) mesAtraso);
            } else if (mesAtraso > MAX_PONTOS_CLASSIFICACAO) {
                pag.setPontosDesclassificacao(pag.getClassificacao() - MAX_PONTOS_CLASSIFICACAO);
            }
            if (pag.getPontosDesclassificacao() < 0){
                pag.setPontosDesclassificacao(0);
            }
        } else {
            pag.setPontosDesclassificacao(pag.getClassificacao());
        }

    }

}
