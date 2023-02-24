package service;

import model.Pagamento;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProcessaArquivoPagamento extends GeraPagamento implements Runnable{
    private static final String CSC_DIVISOR = ";";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");

    public ProcessaArquivoPagamento(File file, String pathProcessados){
        super(file, pathProcessados);

    }
    private void validaNomeArquivo(String nome){
        nome = nome.substring(0, nome.lastIndexOf('.'));
        try{
            int mes = Integer.parseInt(nome.substring( nome.length() - 7, nome.length() - 5));
            int ano = Integer.parseInt(nome.substring( nome.length() - 4, nome.length() ));
            LocalDate data = LocalDate.of(ano,mes,1);
        }catch (RuntimeException  e){
            throw new RuntimeException("Nome arquivo com padrão inválido " + nome);
        }

    }
    private void processarAqruivo(){
        String nomeArquivo = file.getName();
        validaNomeArquivo(nomeArquivo);
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            while (br.ready()) {

                String linhaPagamento = null;
                try {
                    linhaPagamento = br.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (linhaPagamento.isBlank() || linhaPagamento.isEmpty()) {
                    break;
                }
                String[] rowPag = linhaPagamento.split(CSC_DIVISOR);
                if (rowPag.length < 4) {
                    break;
                }
                Pagamento registroPagamento = processaFatura(new Pagamento(rowPag[rowPag.length - 4]
                                , LocalDate.parse(rowPag[rowPag.length - 3], DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                , Double.valueOf(rowPag[rowPag.length - 2])
                                , Integer.parseInt(rowPag[rowPag.length - 1])
                        )
                );

                try {
                    gravarPagamentoProcessado(registroPagamento.getDataVencimento().format(formatter)
                            , registroPagamento);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            br.close();
            fr.close();
            System.out.println("Arquivo -> " + nomeArquivo + " Processado com sucesso!");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void gravarPagamentoProcessado(String dataArquivo, Pagamento pagamento) throws IOException {
        File diretorio = new File(pathProcessados);
        try {
            if (!diretorio.exists()) {
                diretorio.mkdir();
            }

            File arquivo = new File(diretorio, PAGAMENTOS_ATUALIZADOS + dataArquivo + ".csv");
            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }

            FileWriter fw = new FileWriter(arquivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuilder registroPagamentoAtualizado = new StringBuilder();
            registroPagamentoAtualizado.append(pagamento.getClienteNome())
                    .append(CSC_DIVISOR)
                    .append(pagamento.getDataVencimento())
                    .append(CSC_DIVISOR)
                    .append(pagamento.getValorTotal())
                    .append(CSC_DIVISOR)
                    .append(pagamento.getPontosDesclassificacao());
            bw.write(registroPagamentoAtualizado.toString());
            bw.newLine();

            bw.close();
            fw.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        processarAqruivo();
    }

}
