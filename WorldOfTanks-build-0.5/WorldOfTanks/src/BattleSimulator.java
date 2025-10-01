import java.util.Scanner;
import java.util.List;

public class BattleSimulator {
    
    public static void simularBatalha(Scanner scanner, CadastroService cadastroService) {
        System.out.println("\n===== SIMULAÇÃO DE BATALHA =====");
        
        // Listar tanques disponíveis
        List<Tanque> tanquesDisponiveis = cadastroService.getTanques();
        if (tanquesDisponiveis.size() < 2) {
            System.out.println("❌ É necessário ter pelo menos 2 tanques cadastrados!");
            return;
        }

        System.out.println("Tanques disponíveis:");
        for (int i = 0; i < tanquesDisponiveis.size(); i++) {
            Tanque t = tanquesDisponiveis.get(i);
            System.out.printf("%d. %s (%s) - Integridade: %d%%\n", 
                i, t.getCodinome(), t.getClasse(), t.getIntegridade());
        }

        // Escolher atacante
        System.out.print("Escolha o tanque ATACANTE: ");
        int indexAtacante = scanner.nextInt();
        if (indexAtacante < 0 || indexAtacante >= tanquesDisponiveis.size()) {
            System.out.println("❌ Tanque inválido!");
            return;
        }
        Tanque atacante = tanquesDisponiveis.get(indexAtacante);

        // Escolher defensor
        System.out.print("Escolha o tanque DEFENSOR: ");
        int indexDefensor = scanner.nextInt();
        if (indexDefensor < 0 || indexDefensor >= tanquesDisponiveis.size() || indexDefensor == indexAtacante) {
            System.out.println("❌ Tanque inválido!");
            return;
        }
        Tanque defensor = tanquesDisponiveis.get(indexDefensor);

        scanner.nextLine(); // Limpar buffer

        // Iniciar simulação
        simularBatalhaEntreTanques(scanner, atacante, defensor);
    }

    public static void simularBatalhaEntreTanques(Scanner scanner, Tanque atacante, Tanque defensor) {
        System.out.println("\n===== SIMULAÇÃO DE BATALHA =====");
        System.out.println("ATACANTE: " + atacante.getCodinome() + " (" + atacante.getClasse() + ")");
        System.out.println("DEFENSOR: " + defensor.getCodinome() + " (" + defensor.getClasse() + ")");
        System.out.println("================================");

        boolean continuar = true;
        int turno = 1;
        
        while (continuar && atacante.getIntegridade() > 0 && defensor.getIntegridade() > 0) {
            System.out.println("\n--- TURNO " + turno + " ---");
            
            // Turno do atacante
            System.out.println("Vez de " + atacante.getCodinome() + " atacar:");
            executarTurno(scanner, atacante, defensor);
            
            // Verificar se o defensor foi destruído
            if (defensor.getIntegridade() <= 0) {
                System.out.println("\n🎉 " + atacante.getCodinome() + " VENCEU A BATALHA!");
                break;
            }

            // Trocar turnos
            Tanque temp = atacante;
            atacante = defensor;
            defensor = temp;
            
            turno++;

            // Perguntar se quer continuar
            if (turno > 10) { // Limite de turnos para evitar loop infinito
                System.out.print("Continuar? (s/n): ");
                String resposta = scanner.nextLine();
                if (!resposta.equalsIgnoreCase("s")) {
                    continuar = false;
                }
            }
        }

        // Resultado final
        System.out.println("\n===== RESULTADO FINAL =====");
        System.out.println(atacante.getCodinome() + ": " + atacante.getIntegridade() + "% de integridade");
        System.out.println(defensor.getCodinome() + ": " + defensor.getIntegridade() + "% de integridade");
    }

    private static void executarTurno(Scanner scanner, Tanque atacante, Tanque defensor) {
        atacante.listarArmas();
        
        System.out.print("Escolha a arma (0-" + (atacante.getArmas().size()-1) + "): ");
        int escolhaArma = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer
        
        if (escolhaArma < 0 || escolhaArma >= atacante.getArmas().size()) {
            System.out.println("Arma inválida!");
            return;
        }

        System.out.print("Setor do alvo (frontal/lateral/traseiro): ");
        String setor = scanner.nextLine();
        
        System.out.print("Distância até o alvo (metros): ");
        int distancia = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Terreno (campo aberto/deserto/urbano): ");
        String terreno = scanner.nextLine();

        // Executar o tiro
        Arma armaEscolhida = atacante.getArmas().get(escolhaArma);
        int dano = armaEscolhida.calcularDano(setor, terreno, distancia);
        
        // Aplicar dano no defensor (considerando blindagem)
        int danoFinal = calcularDanoFinal(dano, defensor.getBlindagem(), setor);
        int novaIntegridade = defensor.getIntegridade() - danoFinal;
        defensor.setIntegridade(Math.max(0, novaIntegridade));

        System.out.println("\n" + atacante.getCodinome() + " atacou com " + armaEscolhida.getNome());
        System.out.println("Dano base: " + dano);
        System.out.println("Dano final (com blindagem): " + danoFinal);
        System.out.println("Integridade de " + defensor.getCodinome() + ": " + defensor.getIntegridade() + "%");

        // Atualizar estados (panes, superaquecimento)
        atacante.atualizarEstado();
        defensor.atualizarEstado();
    }

    private static int calcularDanoFinal(int danoBase, int blindagem, String setor) {
        double modificadorBlindagem = 1.0 - (blindagem / 200.0); // Blindagem reduz 0.5% por ponto
        double modificadorSetor = getModificadorSetor(setor);
        
        return (int) (danoBase * modificadorBlindagem * modificadorSetor);
    }

    private static double getModificadorSetor(String setor) {
        return switch (setor.toLowerCase()) {
            case "frontal" -> 0.7;  // Menos dano no frontal
            case "lateral" -> 1.0;  // Dano normal no lateral
            case "traseiro" -> 1.3; // Mais dano no traseiro
            default -> 1.0;
        };
    }
}