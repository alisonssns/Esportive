import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ContadorDeLinhasJava {
    public static void main(String[] args) {
        String diretorioProjeto = "code";
        File diretorio = new File(diretorioProjeto);

        if (!diretorio.exists() || !diretorio.isDirectory()) {
            System.out.println("Diretório inválido!");
            return;
        }

        int totalLinhas = contarLinhasDeCodigo(diretorio);
        System.out.println("Total de linhas de código (sem linhas vazias) em arquivos .java: " + totalLinhas);
    }

    private static int contarLinhasDeCodigo(File diretorio) {
        int linhas = 0;

        // Lista todos os arquivos e subdiretórios
        File[] arquivos = diretorio.listFiles();
        if (arquivos != null) {
            for (File arquivo : arquivos) {
                if (arquivo.isDirectory()) {
                    // Chamada recursiva para subdiretórios
                    linhas += contarLinhasDeCodigo(arquivo);
                } else if (arquivo.getName().endsWith(".java")) {
                    // Conta as linhas de arquivos .java
                    linhas += contarLinhasArquivo(arquivo);
                }
            }
        }

        return linhas;
    }

    private static int contarLinhasArquivo(File arquivo) {
        int linhas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Remove espaços em branco e verifica se a linha não está vazia
                if (!linha.trim().isEmpty()) {
                    linhas++;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + arquivo.getName());
        }

        return linhas;
    }
}