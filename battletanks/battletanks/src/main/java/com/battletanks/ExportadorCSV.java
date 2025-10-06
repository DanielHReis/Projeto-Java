package com.battletanks;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class ExportadorCSV {

    public static void exportarTanques(List<Tanque> tanques, String nomeArquivo) {
        // monta o caminho completo na Área de Trabalho do usuário "mathe"
        String caminho = Paths.get(System.getProperty("user.home"), "Documents" , nomeArquivo + ".csv").toString();

        try (FileWriter writer = new FileWriter(caminho)) {
            
            writer.append("ID,Codinome,Classe,Piloto,HoraEntrada,Integridade,Status\n");

            for (Tanque t : tanques) {
                writer.append(t.getId() + ",");
                writer.append(t.getCodinome() + ",");
                writer.append(t.getClasse() + ",");
                writer.append(t.getPiloto() + ",");
                writer.append(t.getHoraEntradaArena().toString() + ",");
                writer.append(t.getIntegridade() + ",");
                writer.append(t.getStatus() + "\n");
            }

            System.out.println("Arquivo CSV exportado com sucesso para: " + caminho);

        } catch (IOException e) {
            System.err.println("Erro ao exportar: " + e.getMessage());
        }
    }
}
