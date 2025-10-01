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
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Codinome: ");
                    String codinomeL = scanner.nextLine();
                    System.out.print("Piloto (HUMANO/IA): ");
                    String pilotoL = scanner.nextLine();
                    pilotoL = pilotoL.toUpperCase();
                    int idL = rand.nextInt(12) + 1;
                    System.out.println("Id gerado : " + idL);

                    TanqueLeve tanqueleve = new TanqueLeve(idL, codinomeL, pilotoL, LocalDateTime.now(), 100, "Ativo");
                    cadastro.cadastrarTanque(tanqueleve);

                    break;
                case 2:
                    System.out.print("Codinome: ");
                    String codinomeM = scanner.nextLine();
                    System.out.print("Piloto (HUMANO/IA): ");
                    String pilotoM = scanner.nextLine();
                    pilotoM = pilotoM.toUpperCase();
                    int idM = rand.nextInt(12) + 1;
                    System.out.println("Id gerado : " + idM);
                    

                    TanqueMedio tanquemedio = new TanqueMedio(idM, codinomeM, pilotoM, LocalDateTime.now(), 100, "Ativo");
                    cadastro.cadastrarTanque(tanquemedio);

                    break;
                case 3:
                    System.out.print("Codinome: ");
                    String codinomeP = scanner.nextLine();
                    System.out.print("Piloto (HUMANO/IA): ");
                    String pilotoP = scanner.nextLine();
                    pilotoP = pilotoP.toUpperCase();
                    int idP = rand.nextInt(12) + 1;
                    System.out.println("Id gerado : " + idP);
                    
                    
                    TanquePesado tanquepesado = new TanquePesado(idP, codinomeP, pilotoP, LocalDateTime.now(), 100,"Ativo");
                    cadastro.cadastrarTanque(tanquepesado);

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
