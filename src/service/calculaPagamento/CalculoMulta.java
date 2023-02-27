package service.calculaPagamento;

import model.Pagamento;
import service.interfaces.IPagamentoCalculavel;

import java.time.LocalDate;

public class CalculoMulta implements IPagamentoCalculavel<Pagamento> {
    private static final Double MULTA_FIXA_ATRASO = 50.00;
    @Override
    public void calcularFatura(Pagamento pag) {
        if (pag.getDataVencimento().isBefore(LocalDate.now())){
            pag.setMulta( MULTA_FIXA_ATRASO );
        }
    }
}
