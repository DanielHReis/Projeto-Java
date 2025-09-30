import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CadastroService cadastro = new CadastroService();

        int opcao;
        do {
            System.out.println("\n=== Arena Battle Tanks ===");
            System.out.println("1. Cadastrar Tanque Leve");
            System.out.println("2. Cadastrar Tanque Médio");
            System.out.println("3. Cadastrar Tanque Pesado");
            System.out.println("4. Listar Tanques");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Codinome: ");
                    String codinomeL = scanner.nextLine();
                    System.out.print("Piloto (Humano/IA): ");
                    String pilotoL = scanner.nextLine();
                    cadastro.cadastrarTanque(new TanqueLeve(cadastro.hashCode(), codinomeL, pilotoL, "10:00"));
                    break;
                case 2:
                    System.out.print("Codinome: ");
                    String codinomeM = scanner.nextLine();
                    System.out.print("Piloto (Humano/IA): ");
                    String pilotoM = scanner.nextLine();
                    cadastro.cadastrarTanque(new TanqueMedio(cadastro.hashCode(), codinomeM, pilotoM, "10:00"));
                    break;
                case 3:
                    System.out.print("Codinome: ");
                    String codinomeP = scanner.nextLine();
                    System.out.print("Piloto (Humano/IA): ");
                    String pilotoP = scanner.nextLine();
                    cadastro.cadastrarTanque(new TanquePesado(cadastro.hashCode(), codinomeP, pilotoP, "10:00"));
                    break;
                case 4:
                    cadastro.listarTanques();
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
}
