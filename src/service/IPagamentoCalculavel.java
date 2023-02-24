package service;

import model.Pagamento;

public interface IPagamentoCalculavel<T extends Pagamento> {
    void calcularFatura(T pag);

}
