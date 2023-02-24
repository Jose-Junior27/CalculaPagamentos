package service;

import model.Pagamento;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProcessarFaturamento extends Faturamento implements IFaturavel<Pagamento> {
    private String csvDivisor = ";";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
    private String file;

    public ProcessarFaturamento(String path) {
        super(path);
    }

    @Override
    protected void gerarPagamentos() throws IOException {
        File diretorio = new File(path);
        if (!diretorio.exists() && (!diretorio.isDirectory())) {
            throw new IOException("Diretório (" + path + ") inválido!");
        }
        pathProcessados = diretorio.getParent() + File.separator + PATH_PROCESSADOS;
        File[] arquivos = diretorio.listFiles();
        for (File f : arquivos) {
            file = f.getAbsolutePath();
            new Thread(processaArquivoPagamento).start();
        }
    }

    private Runnable processaArquivoPagamento = new Runnable() {
        @Override
        public void run() {
            String pathFile= file;
            File f = new File(pathFile);
            String nomeArquivo = f.getName();
            validaNomeArquivo(nomeArquivo);
            FileReader fr = null;
            try {
                fr = new FileReader(f);
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
                    String[] rowPag = linhaPagamento.split(csvDivisor);
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
    };

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
                    .append(csvDivisor)
                    .append(pagamento.getDataVencimento())
                    .append(csvDivisor)
                    .append(pagamento.getValorTotal())
                    .append(csvDivisor)
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
    public void processar() throws IOException {
        gerarPagamentos();
    }

}
