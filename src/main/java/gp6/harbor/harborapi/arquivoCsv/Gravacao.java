package gp6.harbor.harborapi.arquivoCsv;

import gp6.harbor.harborapi.domain.prestador.Prestador;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Gravacao {


    public static void gravaArquivosCsv(List<Prestador> lista, String nomeArq) {
        FileWriter arq = null;
        Formatter saida = null;
        Boolean deuRuim = false;

        nomeArq += ".csv";

        try {
            arq = new FileWriter(nomeArq);
            saida = new Formatter(arq);
        }
        catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            erro.printStackTrace();
            System.exit(1);
        }

        try {
            for (Prestador prestador : lista) {
                saida.format("%d;%s;%s;%s;%s;%s\n", prestador.getId(), prestador.getNome(), prestador.getSobrenome(), prestador.getTelefone(), prestador.getCpf(), prestador.getEmail());
            }
        }
        catch (FormatterClosedException erro) {
            System.out.println("Erro ao gravar o arquivo");
            erro.printStackTrace();
            deuRuim = true;
        }
        finally {
            saida.close();
            try {
                arq.close();
            }
            catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
    }

    // Método para gravar informações de uma matriz em um arquivo CSV
    public static void gravaArquivosCsvMatriz(Map<String, Map<String, Integer>> matriz, List<String> meses, String nomeArq, String nomeEmpresa, String cnpj, String outrosDados) {
        FileWriter arq = null;
        Formatter saida = null;
        Boolean deuRuim = false;

        // Obtém a data atual
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date dataAtual = new Date();
        String dataFormatada = formatter.format(dataAtual);

        nomeArq = "Relatorio_" + dataFormatada + "_CNPJ_" + cnpj + ".csv";

        try {
            arq = new FileWriter(nomeArq);
            saida = new Formatter(arq);
        } catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            erro.printStackTrace();
            System.exit(1);
        }

        try {
            // Escreve informações da empresa
            saida.format("Empresa:;%s\nCNPJ:;%s\n%s\n", nomeEmpresa, cnpj, outrosDados);

            // Escreve os meses na segunda linha
            saida.format(";"); // célula vazia para a primeira coluna (para os nomes dos funcionários)
            for (String mes : meses) {
                saida.format("%s;", mes);
            }
            saida.format("\n");

            // Escreve os dados
            for (Map.Entry<String, Map<String, Integer>> entry : matriz.entrySet()) {
                String funcionario = entry.getKey();
                Map<String, Integer> mapaMes = entry.getValue();

                saida.format("%s;", funcionario);

                for (String mes : meses) {
                    Integer valor = mapaMes.getOrDefault(mes, 0);
                    saida.format("%d;", valor);
                }
                saida.format("\n");
            }
        } catch (FormatterClosedException erro) {
            System.out.println("Erro ao gravar o arquivo");
            erro.printStackTrace();
            deuRuim = true;
        } finally {
            saida.close();
            try {
                arq.close();
            } catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
    }


    // Método para gravar informações de faturamento em um arquivo CSV
    public static void gravaArquivoCsvFaturamento(Double faturamento, String nomeArq) {
        FileWriter arq = null;
        Formatter saida = null;
        Boolean deuRuim = false;

        // Obtenção da data atual omitida para manter a consistência com o método gravaArquivosCsvMatriz

        nomeArq += ".csv";

        try {
            arq = new FileWriter(nomeArq);
            saida = new Formatter(arq);
        } catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            erro.printStackTrace();
            System.exit(1);
        }

        try {
            saida.format("%.2f\n", faturamento);
        } catch (FormatterClosedException erro) {
            System.out.println("Erro ao gravar o arquivo");
            erro.printStackTrace();
            deuRuim = true;
        } finally {
            saida.close();
            try {
                arq.close();
            } catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
    }

    // Método para gravar informações de ticket médio em um arquivo CSV
    public static void gravaArquivoCsvTicketMedio(Double ticketMedio, String nomeArq) {
        // Similar ao método gravaArquivoCsvFaturamento, mas escrevendo o ticket médio
        gravaArquivoCsvFaturamento(ticketMedio, nomeArq);
    }

    // Método para gravar informações de prestadores em um arquivo CSV
    public static void gravaArquivosCsvPrestador(List<Prestador> lista, String nomeArq) {
        FileWriter arq = null;
        Formatter saida = null;
        Boolean deuRuim = false;

        // Obtenção da data atual e inclusão no nome do arquivo omitida para manter a consistência com o método gravaArquivosCsvMatriz

        nomeArq += ".csv";

        try {
            arq = new FileWriter(nomeArq);
            saida = new Formatter(arq);
        } catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            erro.printStackTrace();
            System.exit(1);
        }

        try {
            for (Prestador prestador : lista) {
                saida.format("%d;%s;%s;%s;%s;%s\n", prestador.getId(), prestador.getNome(), prestador.getSobrenome(), prestador.getTelefone(), prestador.getCpf(), prestador.getEmail());
            }
        } catch (FormatterClosedException erro) {
            System.out.println("Erro ao gravar o arquivo");
            erro.printStackTrace();
            deuRuim = true;
        } finally {
            saida.close();
            try {
                arq.close();
            } catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
    }

}
