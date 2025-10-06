package com.battletanks;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.sql.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static CadastroService cadastro = new CadastroService();

    public static void limparConsole() {
        try {
            final String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Linux, Mac, Unix
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final Exception e) {
            // Fallback: imprime várias linhas em branco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        String opcao;
        do {
            exibirMenuPrincipal();
            opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> {
                    limparConsole();
                    cadastrarTanque();
                }
                case "2" -> {
                    limparConsole();
                    cadastro.listarTanques();
                }
                case "3" -> {
                    limparConsole();
                    AgendamentoService.agendarPartida(scanner, cadastro);
                }
                case "4" -> {
                    limparConsole();
                    AgendamentoService.listarPartidas();
                }
                case "5" -> {
                    limparConsole();
                    AgendamentoService.gerenciarPartida(scanner);
                }
                case "6" -> {
                    limparConsole();
                    BattleSimulator.simularBatalha(scanner, cadastro);
                }
                case "7" -> {
                    limparConsole();
                    menuRankingEstatisticas();
                }
                case "8" -> {
                    limparConsole();
                    menuRelatoriosDetalhados();
                }
                case "9" -> {
                    limparConsole();
                    System.out.print("Digite o nome do arquivo CSV (ex: tanques.csv): ");
                    String nomeArquivo = scanner.nextLine();
                    ExportadorCSV.exportarTanques(cadastro.getTanques(), nomeArquivo);
                }
                case "0" -> {
                    limparConsole();
                    System.out.println("Saindo...");
                }
                default -> {
                    limparConsole();
                    System.out.println("Opção inválida!");
                }
            }

            if (!opcao.equals("0")) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        } while (!opcao.equals("0"));

        scanner.close();
    }

    private static void exibirMenuPrincipal() {
        limparConsole();
        System.out.println("\n===== World Of Tanks =====");
        System.out.println("1. Cadastrar Tanque");
        System.out.println("2. Listar Tanques");
        System.out.println("3. Agendar Partida");
        System.out.println("4. Listar Partidas");
        System.out.println("5. Gerenciar Partida");
        System.out.println("6. Simular Batalha");
        System.out.println("7. Ranking e Estatísticas");
        System.out.println("8. Relatórios Detalhados");
        System.out.println("9. Exportar Tanques para CSV");
        System.out.println("0. Sair");
        System.out.print("Escolha: ");
    }

    private static void cadastrarTanque() {
        System.out.println("\n===== CADASTRAR TANQUE =====");
        System.out.println("1. Tanque Leve");
        System.out.println("2. Tanque Médio");
        System.out.println("3. Tanque Pesado");
        System.out.println("0. Voltar");
        System.out.print("Escolha o tipo: ");

        String tipo = scanner.nextLine();

        System.out.print("Codinome: ");
        String codinome = scanner.nextLine();

        System.out.print("Piloto (HUMANO/IA): ");
        String piloto = scanner.nextLine().toUpperCase();

        int id = gerarIdUnico();

        Tanque tanque = criarTanque(tipo, id, codinome, piloto);
        if (tanque != null) {
            cadastro.cadastrarTanque(tanque);
        }
    }

    private static int gerarIdUnico() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Conexao.getConexao();
            Random rand = new Random();
            int tentativas = 0;
            int maxTentativas = 100;

            while (tentativas < maxTentativas) {
                int id = rand.nextInt(1000) + 1;

                String sql = "SELECT COUNT(*) FROM tanques WHERE id = ?";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                rs = ps.executeQuery();

                if (rs.next() && rs.getInt(1) == 0) {
                    return id;
                }

                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
                tentativas++;
            }

            System.out.println("Não foi possível gerar ID único após " + maxTentativas + " tentativas");
            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static Tanque criarTanque(String tipo, int id, String codinome, String piloto) {
        return switch (tipo) {
            case "1" -> new TanqueLeve(id, codinome, piloto, LocalDateTime.now(), 100, "Ativo");
            case "2" -> new TanqueMedio(id, codinome, piloto, LocalDateTime.now(), 100, "Ativo");
            case "3" -> new TanquePesado(id, codinome, piloto, LocalDateTime.now(), 100, "Ativo");
            default -> {
                System.out.println("Tipo inválido!");
                yield null;
            }
        };
    }

    private static void menuRankingEstatisticas() {
        String opcao;
        do {
            limparConsole();
            System.out.println("\n===== RANKING E ESTATÍSTICAS =====");
            System.out.println("1. Ranking Geral");
            System.out.println("2. Ranking por Modo");
            System.out.println("3. Estatísticas de Tanque");
            System.out.println("4. Timeline de Eventos");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> {
                    RankingService.gerarRankingGeral();
                }
                case "2" -> {
                    System.out.print("Modo (TREINO/UM_VS_UM/EQUIPES_3V3/EQUIPES_5V5): ");
                    String modo = scanner.nextLine();
                    RankingService.gerarRankingPorModo(cadastro, modo);
                }
                case "3" -> {
                    System.out.print("ID do Tanque: ");
                    int idTanque = Integer.parseInt(scanner.nextLine());
                    RankingService.mostrarEstatisticasDetalhadas(cadastro, idTanque);
                }
                case "4" -> {
                    RankingService.mostrarTimelineEventos();
                }
                case "0" -> {
                }
                default -> System.out.println("Opção inválida!");
            }
            if (!opcao.equals("0")) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        } while (!opcao.equals("0"));
    }

    private static void menuRelatoriosDetalhados() {
        String opcao;
        do {
            System.out.println("\n===== RELATÓRIOS DETALHADOS =====");
            System.out.println("1. Eficiência de Armas");
            System.out.println("2. Horários de Pico");
            System.out.println("3. Disponibilidade da Frota");
            System.out.println("4. Taxa de Vitória por Classe");
            System.out.println("5. Taxa de Vitória por Piloto");
            System.out.println("6. Top Armas");
            System.out.println("7. Resetar Ranking Semanal");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> RankingService.gerarRelatorioArmas();
                case "2" -> {
                    RankingService.gerarMapaCalorHorarios();
                    RankingService.gerarAnaliseArenas();
                }
                case "3" -> gerarRelatorioDisponibilidadeFrota();
                case "4" -> RankingService.gerarTaxaVitoriaPorClasse(cadastro);
                case "5" -> RankingService.gerarTaxaVitoriaPorPiloto(cadastro);
                case "6" -> RankingService.gerarTopArmas();
                case "7" -> RankingService.resetRankingSemanal();
                case "0" -> {
                }
                default -> System.out.println("Opção inválida!");
            }
        } while (!opcao.equals("0"));
    }

    private static void gerarRelatorioDisponibilidadeFrota() {
        System.out.println("\n===== DISPONIBILIDADE DA FROTA =====");
        List<Tanque> tanques = cadastro.getTanques();

        int ativos = 0, emManutencao = 0, destruidos = 0;

        for (Tanque tanque : tanques) {
            switch (tanque.getStatus().toUpperCase()) {
                case "ATIVO" -> ativos++;
                case "EM MANUTENCAO" -> emManutencao++;
                case "DESTRUIDO" -> destruidos++;
            }
        }

        System.out.println("Total de Tanques: " + tanques.size());
        System.out.println("Ativos: " + ativos);
        System.out.println("Em Manutenção: " + emManutencao);
        System.out.println("Destruídos: " + destruidos);

        if (!tanques.isEmpty()) {
            System.out.println("Taxa de Disponibilidade: " + (ativos * 100 / tanques.size()) + "%");
        } else {
            System.out.println("Taxa de Disponibilidade: 0%");
        }
    }

}