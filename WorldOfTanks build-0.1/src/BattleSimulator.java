import java.util.Scanner;

public class BattleSimulator {
    public static void simularBatalha(Scanner scanner, Tanque atacante, Tanque defensor) {
        System.out.println("\n===== SIMULAÇÃO DE BATALHA =====");
        System.out.println("ATACANTE: " + atacante.getCodinome() + " (" + atacante.getClasse() + ")");
        System.out.println("DEFENSOR: " + defensor.getCodinome() + " (" + defensor.getClasse() + ")");

        boolean continuar = true;
        while (continuar) {
            // Atacante escolhe arma e ação
            System.out.println("\n--- Turno do " + atacante.getCodinome() + " ---");
            atacante.listarArmas();
            
            System.out.print("Escolha a arma (0-" + (atacante.getArmas().size()-1) + ") ou -1 para sair: ");
            int escolhaArma = scanner.nextInt();
            
            if (escolhaArma == -1) {
                continuar = false;
                break;
            }

            System.out.print("Setor do alvo (frontal/lateral/traseiro): ");
            String setor = scanner.next();
            
            System.out.print("Distância até o alvo (metros): ");
            int distancia = scanner.nextInt();
            
            System.out.print("Terreno (campo aberto/deserto/urbano): ");
            String terreno = scanner.next();

            // Executar o tiro
            atacante.atirar(escolhaArma, setor, terreno, distancia);
            
            // Atualizar estados
            atacante.atualizarEstado();
            defensor.atualizarEstado();

            // Verificar condições de vitória
            if (defensor.getIntegridade() <= 0) {
                System.out.println("🎉 " + atacante.getCodinome() + " VENCEU A BATALHA!");
                continuar = false;
            }

            // Trocar turnos (simples alternância)
            Tanque temp = atacante;
            atacante = defensor;
            defensor = temp;
        }
    }
}