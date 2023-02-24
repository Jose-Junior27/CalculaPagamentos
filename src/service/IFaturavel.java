package service;

import model.Pagamento;

import java.io.IOException;

public interface  IFaturavel<T extends Pagamento> {

  void processar() throws IOException;


}
