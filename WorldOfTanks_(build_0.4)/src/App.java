import java.util.Scanner;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        Random rand = new Random();
        Scanner scanner = new Scanner(System.in);
        CadastroService cadastro = new CadastroService();

        int opcao;
        do {
            System.out.println("\n===== World Of Tanks =====");
            System.out.println("1. Cadastrar Tanque Leve");
            System.out.println("2. Cadastrar Tanque Médio");
            System.out.println("3. Cadastrar Tanque Pesado");
            System.out.println("4. Listar Tanques");
            System.out.println("5. Agendar Partida");
            System.out.println("6. Listar Partidas");
            System.out.println("7. Gerenciar Partida");
            System.out.println("8. Simular Batalha");
            System.out.println("9. Ranking e Estatísticas");
            System.out.println("10. Relatórios Detalhados");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarTanqueLeve(scanner, rand, cadastro);
                    break;
                case 2:
                    cadastrarTanqueMedio(scanner, rand, cadastro);
                    break;
                case 3:
                    cadastrarTanquePesado(scanner, rand, cadastro);
                    break;
                case 4:
                    cadastro.listarTanques();
                    break;
                case 5:
                    AgendamentoService.agendarPartida(scanner, cadastro);
                    break;
                case 6:
                    AgendamentoService.listarPartidas();
                    break;
                case 7:
                    AgendamentoService.gerenciarPartida(scanner);
                    break;
                case 8:
                    BattleSimulator.simularBatalha(scanner, cadastro);
                    break;
                case 9:
                    menuRankingEstatisticas(scanner, cadastro);
                    break;
                case 10:
                    menuRelatoriosDetalhados(scanner, cadastro);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void menuRankingEstatisticas(Scanner scanner, CadastroService cadastro) {
        int opcao;
        do {
            System.out.println("\n===== RANKING E ESTATÍSTICAS =====");
            System.out.println("1. Ranking Geral");
            System.out.println("2. Ranking por Classe");
            System.out.println("3. Estatísticas de um Tanque");
            System.out.println("4. Timeline de Eventos");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    RankingService.gerarRankingGeral();
                    break;
                case 2:
                    System.out.print("Classe (Leve/Médio/Pesado): ");
                    String classe = scanner.nextLine();
                    RankingService.gerarRankingPorClasse(cadastro, classe);
                    break;
                case 3:
                    System.out.print("ID do Tanque: ");
                    int idTanque = scanner.nextInt();
                    RankingService.mostrarEstatisticasDetalhadas(cadastro, idTanque);
                    break;
                case 4:
                    RankingService.mostrarTimelineEventos();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void menuRelatoriosDetalhados(Scanner scanner, CadastroService cadastro) {
        int opcao;
        do {
            System.out.println("\n===== RELATÓRIOS DETALHADOS =====");
            System.out.println("1. Eficiência de Armas");
            System.out.println("2. Horários de Pico (Mapa de Calor)");
            System.out.println("3. Disponibilidade da Frota");
            System.out.println("4. Taxa de Vitória por Classe");
            System.out.println("5. Taxa de Vitória por Piloto");
            System.out.println("6. Top Armas por Eficiência");
            System.out.println("7. Resetar Ranking Semanal");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    RankingService.gerarRelatorioArmas();
                    break;
                case 2:
                    RankingService.gerarMapaCalorHorarios();
                    RankingService.gerarAnaliseArenas();
                    break;
                case 3:
                    gerarRelatorioDisponibilidadeFrota(cadastro);
                    break;
                case 4:
                    RankingService.gerarTaxaVitoriaPorClasse(cadastro);
                    break;
                case 5:
                    RankingService.gerarTaxaVitoriaPorPiloto(cadastro);
                    break;
                case 6:
                    RankingService.gerarTopArmas();
                    break;
                case 7:
                    RankingService.resetRankingSemanal();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void gerarRelatorioDisponibilidadeFrota(CadastroService cadastro) {
        System.out.println("\n===== DISPONIBILIDADE DA FROTA =====");
        List<Tanque> tanques = cadastro.getTanques();

        int ativos = 0, emManutencao = 0, destruidos = 0;

        for (Tanque tanque : tanques) {
            switch (tanque.getStatus().toUpperCase()) {
                case "ATIVO":
                    ativos++;
                    break;
                case "EM MANUTENCAO":
                    emManutencao++;
                    break;
                case "DESTRUIDO":
                    destruidos++;
                    break;
            }
        }

        System.out.println("Total de Tanques: " + tanques.size());
        System.out.println("Ativos: " + ativos + " (" + (ativos * 100 / tanques.size()) + "%)");
        System.out.println("Em Manutenção: " + emManutencao);
        System.out.println("Destruídos: " + destruidos);
        System.out.println("Taxa de Disponibilidade: " + (ativos * 100 / tanques.size()) + "%");
    }

    private static void cadastrarTanqueLeve(Scanner scanner, Random rand, CadastroService cadastro) {
        System.out.print("Codinome: ");
        String codinome = scanner.nextLine();
        System.out.print("Piloto (HUMANO/IA): ");
        String piloto = scanner.nextLine().toUpperCase();
        int id = rand.nextInt(12) + 1;
        System.out.println("Id gerado: " + id);

        TanqueLeve tanque = new TanqueLeve(id, codinome, piloto, LocalDateTime.now(), 100, "Ativo");
        cadastro.cadastrarTanque(tanque);
    }

    private static void cadastrarTanqueMedio(Scanner scanner, Random rand, CadastroService cadastro) {
        System.out.print("Codinome: ");
        String codinome = scanner.nextLine();
        System.out.print("Piloto (HUMANO/IA): ");
        String piloto = scanner.nextLine().toUpperCase();
        int id = rand.nextInt(12) + 1;
        System.out.println("Id gerado: " + id);

        TanqueMedio tanque = new TanqueMedio(id, codinome, piloto, LocalDateTime.now(), 100, "Ativo");
        cadastro.cadastrarTanque(tanque);
    }

    private static void cadastrarTanquePesado(Scanner scanner, Random rand, CadastroService cadastro) {
        System.out.print("Codinome: ");
        String codinome = scanner.nextLine();
        System.out.print("Piloto (HUMANO/IA): ");
        String piloto = scanner.nextLine().toUpperCase();
        int id = rand.nextInt(12) + 1;
        System.out.println("Id gerado: " + id);

        TanquePesado tanque = new TanquePesado(id, codinome, piloto, LocalDateTime.now(), 100, "Ativo");
        cadastro.cadastrarTanque(tanque);
    }
}