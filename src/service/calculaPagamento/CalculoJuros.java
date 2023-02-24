package service.calculaPagamento;

import model.Pagamento;
import service.IPagamentoCalculavel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculoJuros implements IPagamentoCalculavel<Pagamento> {

    @Override
    public void calcularFatura(Pagamento pag) {
        long semanaAtraso;
        semanaAtraso = pag.getDataVencimento().until(LocalDate.now(), ChronoUnit.WEEKS);
        if (semanaAtraso > 0) {
            pag.setJurosMora( pag.getValor() * (semanaAtraso / 100.0) );
        }
    }
}
