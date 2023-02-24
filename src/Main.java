import service.ProcessarFaturamento;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ProcessarFaturamento faturamento = new ProcessarFaturamento("/home/j2/Documents/Pagamentos/");
        try {
            faturamento.processar();

        } catch (IOException e){
            throw new RuntimeException("Falha no processamento dos pagamentos: " + e.getMessage() );
        }
    }
}