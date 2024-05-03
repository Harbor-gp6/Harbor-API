package gp6.harbor.harborapi.arquivoCsv;

import gp6.harbor.harborapi.domain.prestador.Prestador;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
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
}