package service;

import model.Pagamento;
import service.interfaces.IFaturavel;

import java.io.*;
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
            throw new IOException("Diretório (" + path + ") é inválido!");
        }
        pathProcessados = diretorio.getParent() + File.separator + PATH_PROCESSADOS;
        File[] arquivos = diretorio.listFiles();
        for (File f : arquivos) {
            ProcessaArquivoPagamento gerarPagamento = new ProcessaArquivoPagamento(f, pathProcessados);
            Thread processaArquivo = new Thread(gerarPagamento);
            processaArquivo.start();
        }
    }
    @Override
    public void processar() throws IOException {
        gerarPagamentos();
    }

}
