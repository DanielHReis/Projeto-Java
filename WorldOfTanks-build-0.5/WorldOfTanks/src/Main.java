import java.util.Scanner;
import java.time.LocalDateTime;
import java.util.Random;

public class Main {
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
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        scanner.close();
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