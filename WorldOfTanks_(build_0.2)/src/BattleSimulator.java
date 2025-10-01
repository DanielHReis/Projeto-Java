import java.util.Scanner;
import java.util.List;
import java.util.Random;

public class BattleSimulator {
    private static Random random = new Random();

    public static void simularBatalha(Scanner scanner, CadastroService cadastroService) {
        System.out.println("\n===== SIMULAÇÃO DE BATALHA =====");

        // Listar tanques disponíveis
        List<Tanque> tanquesDisponiveis = cadastroService.getTanques();
        if (tanquesDisponiveis.size() < 2) {
            System.out.println("É necessário ter pelo menos 2 tanques cadastrados!");
            return;
        }

        System.out.println("Tanques disponíveis:");
        for (int i = 0; i < tanquesDisponiveis.size(); i++) {
            Tanque t = tanquesDisponiveis.get(i);
            System.out.printf("%d. %s (%s) - Piloto: %s - Integridade: %d%%\n",
                    i, t.getCodinome(), t.getClasse(), t.getPiloto(), t.getIntegridade());
        }

        // Escolher atacante
        System.out.print("Escolha o tanque ATACANTE: ");
        int indexAtacante = scanner.nextInt();
        if (indexAtacante < 0 || indexAtacante >= tanquesDisponiveis.size()) {
            System.out.println("Tanque inválido!");
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
        System.out.println("ATACANTE: " + atacante.getCodinome() + " (" + atacante.getClasse() + ") - Piloto: "
                + atacante.getPiloto());
        System.out.println("DEFENSOR: " + defensor.getCodinome() + " (" + defensor.getClasse() + ") - Piloto: "
                + defensor.getPiloto());
        System.out.println("================================");

        boolean continuar = true;
        int turno = 1;

        while (continuar && atacante.getIntegridade() > 0 && defensor.getIntegridade() > 0) {
            System.out.println("\n--- TURNO " + turno + " ---");

            // Turno do atacante
            System.out.println("Vez de " + atacante.getCodinome() + " atacar:");

            if (atacante.getPiloto().equalsIgnoreCase("IA")) {
                // ✅ TANQUE IA - Ataque automático
                executarTurnoIA(atacante, defensor);
            } else {
                // ✅ TANQUE HUMANO - Escolha do jogador
                executarTurnoHumano(scanner, atacante, defensor);
            }

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

            // Perguntar se quer continuar (apenas se ambos são humanos)
            if (turno > 10 && atacante.getPiloto().equalsIgnoreCase("HUMANO")) {
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

    // ✅ NOVO MÉTODO: Executar turno para tanque IA
    private static void executarTurnoIA(Tanque atacante, Tanque defensor) {
        System.out.println("🤖 " + atacante.getCodinome() + " (IA) está calculando o ataque...");

        // IA escolhe arma aleatoriamente (com preferência por armas mais fortes)
        int escolhaArma = escolherArmaIA(atacante);

        // IA escolhe setor baseado na classe do defensor
        String setor = escolherSetorIA(defensor);

        // IA escolhe distância baseada na arma escolhida
        int distancia = escolherDistanciaIA(atacante.getArmas().get(escolhaArma));

        // IA escolhe terreno (aleatório)
        String terreno = escolherTerrenoIA();

        System.out.println("IA escolheu: " + atacante.getArmas().get(escolhaArma).getNome() +
                " | Setor: " + setor + " | Distância: " + distancia + "m | Terreno: " + terreno);

        // Executar o ataque
        executarAtaque(atacante, defensor, escolhaArma, setor, distancia, terreno);
    }

    // ✅ NOVO MÉTODO: Escolha inteligente de arma pela IA
    private static int escolherArmaIA(Tanque atacante) {
        List<Arma> armas = atacante.getArmas();

        // Prioridade: Míssil > Canhão > Metralhadora
        for (int i = 0; i < armas.size(); i++) {
            if (armas.get(i) instanceof Missil && armas.get(i).getMunicaoAtual() > 0) {
                return i;
            }
        }
        for (int i = 0; i < armas.size(); i++) {
            if (armas.get(i) instanceof Canhao && armas.get(i).getMunicaoAtual() > 0) {
                return i;
            }
        }
        for (int i = 0; i < armas.size(); i++) {
            if (armas.get(i) instanceof Metralhadora && armas.get(i).getMunicaoAtual() > 0) {
                return i;
            }
        }

        // Se não há munição, retorna a primeira arma disponível
        return 0;
    }

    // ✅ NOVO MÉTODO: Escolha inteligente de setor pela IA
    private static String escolherSetorIA(Tanque defensor) {
        // IA prioriza setores mais vulneráveis baseado na classe do inimigo
        String classeDefensor = defensor.getClasse().toLowerCase();

        switch (classeDefensor) {
            case "pesado":
                // Tanques pesados são vulneráveis na lateral/traseiro
                return random.nextDouble() < 0.7 ? "lateral" : "traseiro";
            case "médio":
                // Tanques médios são balanceados
                String[] setoresMedio = { "frontal", "lateral", "traseiro" };
                return setoresMedio[random.nextInt(setoresMedio.length)];
            case "leve":
                // Tanques leves são muito vulneráveis no traseiro
                return random.nextDouble() < 0.8 ? "traseiro" : "lateral";
            default:
                return "lateral";
        }
    }

    // ✅ NOVO MÉTODO: Escolha inteligente de distância pela IA
    private static int escolherDistanciaIA(Arma arma) {
        if (arma instanceof Missil) {
            // Míssil é melhor em média/longa distância
            return 300 + random.nextInt(200);
        } else if (arma instanceof Canhao) {
            // Canhão é versátil
            return 100 + random.nextInt(200);
        } else if (arma instanceof Metralhadora) {
            // Metralhadora é melhor em curta distância
            return 50 + random.nextInt(100);
        }
        return 150; // Distância padrão
    }

    // ✅ NOVO MÉTODO: Escolha de terreno pela IA
    private static String escolherTerrenoIA() {
        String[] terrenos = { "campo aberto", "deserto", "urbano" };
        return terrenos[random.nextInt(terrenos.length)];
    }

    // ✅ MÉTODO EXISTENTE: Executar turno para tanque humano (atualizado)
    private static void executarTurnoHumano(Scanner scanner, Tanque atacante, Tanque defensor) {
        atacante.listarArmas();

        System.out.print("Escolha a arma (0-" + (atacante.getArmas().size() - 1) + "): ");
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

        // Executar o ataque
        executarAtaque(atacante, defensor, escolhaArma, setor, distancia, terreno);
    }

    // ✅ NOVO MÉTODO: Executar ataque (comum para IA e humano)
    private static void executarAtaque(Tanque atacante, Tanque defensor, int escolhaArma,
            String setor, int distancia, String terreno) {
        Arma armaEscolhida = atacante.getArmas().get(escolhaArma);
        int dano = armaEscolhida.calcularDano(setor, terreno, distancia);

        // Aplicar dano no defensor (considerando blindagem)
        int danoFinal = calcularDanoFinal(dano, defensor.getBlindagem(), setor);
        int novaIntegridade = defensor.getIntegridade() - danoFinal;

        // VERIFICAR SE FOI ABATE
        boolean abate = (novaIntegridade <= 0);

        defensor.setIntegridade(Math.max(0, novaIntegridade));

        System.out.println("\n" + atacante.getCodinome() + " atacou com " + armaEscolhida.getNome());
        System.out.println("Dano base: " + dano);
        System.out.println("Dano final (com blindagem): " + danoFinal);
        System.out.println("Integridade de " + defensor.getCodinome() + ": " + defensor.getIntegridade() + "%");

        // Atualizar estatísticas
        RankingService.atualizarEstatisticasBatalha(atacante, defensor, danoFinal, abate);

        // Atualizar estados (panes, superaquecimento)
        atacante.atualizarEstado();
        defensor.atualizarEstado();
    }

    private static int calcularDanoFinal(int danoBase, int blindagem, String setor) {
        double modificadorBlindagem = 1.0 - (blindagem / 200.0);
        double modificadorSetor = getModificadorSetor(setor);

        return (int) (danoBase * modificadorBlindagem * modificadorSetor);
    }

    private static double getModificadorSetor(String setor) {
        return switch (setor.toLowerCase()) {
            case "frontal" -> 0.7;
            case "lateral" -> 1.0;
            case "traseiro" -> 1.3;
            default -> 1.0;
        };
    }
}