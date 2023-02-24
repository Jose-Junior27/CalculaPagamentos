package service.calculaPagamento;

import service.IPagamentoCalculavel;
import model.Pagamento;
import java.time.LocalDate;
public class CalculaDesconto implements IPagamentoCalculavel<Pagamento> {
    private static final Double MAX_DESCONTO = 500.00;
    @Override
    public void calcularFatura(Pagamento pag) {
        if (pag.getDataVencimento().isAfter(LocalDate.now())){
            Double desconto = pag.getValor() * (pag.getClassificacao() / 100.00);
            pag.setDesconto( desconto );
            if (pag.getDesconto() > MAX_DESCONTO) {
                pag.setDesconto(MAX_DESCONTO);
            }
        }
    }
}
