package com.battletanks;

import java.util.Scanner;
import java.util.List;
import java.util.Random;

public class BattleSimulator {
    private static Random random = new Random();

    public static void simularBatalha(Scanner scanner, CadastroService cadastroService) {
        System.out.println("\n===== SIMULAÇÃO DE BATALHA =====");

        List<Tanque> tanquesDisponiveis = cadastroService.getTanques();
        if (tanquesDisponiveis.size() < 2) {
            System.out.println("É necessário ter pelo menos 2 tanques cadastrados!");
            return;
        }

        listarTanquesDisponiveis(tanquesDisponiveis);

        Tanque atacante = selecionarTanque("ATACANTE", tanquesDisponiveis, scanner);
        Tanque defensor = selecionarTanque("DEFENSOR", tanquesDisponiveis, scanner);

        if (atacante == null || defensor == null || atacante == defensor) {
            System.out.println("❌ Seleção inválida!");
            return;
        }

        simularBatalhaEntreTanques(scanner, atacante, defensor);
    }

    private static void listarTanquesDisponiveis(List<Tanque> tanques) {
        System.out.println("Tanques disponíveis:");
        for (int i = 0; i < tanques.size(); i++) {
            Tanque t = tanques.get(i);
            System.out.printf("%d. %s (%s) - Piloto: %s - Integridade: %d%%\n",
                    i, t.getCodinome(), t.getClasse(), t.getPiloto(), t.getIntegridade());
        }
    }

    private static Tanque selecionarTanque(String tipo, List<Tanque> tanques, Scanner scanner) {
        System.out.print("Escolha o tanque " + tipo + ": ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            return (index >= 0 && index < tanques.size()) ? tanques.get(index) : null;
        } catch (NumberFormatException e) {
            return null;
        }
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

            if (atacante.getPiloto().equalsIgnoreCase("IA")) {
                executarTurnoIA(atacante, defensor);
            } else {
                executarTurnoHumano(scanner, atacante, defensor);
            }

            if (defensor.getIntegridade() <= 0) {
                System.out.println("\n🎉 " + atacante.getCodinome() + " VENCEU A BATALHA!");
                break;
            }

            // Trocar turnos
            Tanque temp = atacante;
            atacante = defensor;
            defensor = temp;
            turno++;

            if (turno > 10 && atacante.getPiloto().equalsIgnoreCase("HUMANO")) {
                System.out.print("Continuar? (s/n): ");
                continuar = scanner.nextLine().equalsIgnoreCase("s");
            }
        }

        exibirResultadoFinal(atacante, defensor);
    }

    private static void executarTurnoIA(Tanque atacante, Tanque defensor) {
        System.out.println(atacante.getCodinome() + " (IA) está calculando o ataque...");

        int escolhaArma = escolherArmaIA(atacante);
        String setor = escolherSetorIA(defensor);
        int distancia = escolherDistanciaIA(atacante.getArmas().get(escolhaArma));
        String terreno = escolherTerrenoIA();

        System.out.println("IA escolheu: " + atacante.getArmas().get(escolhaArma).getNome() +
                " | Setor: " + setor + " | Distância: " + distancia + "m | Terreno: " + terreno);

        executarAtaque(atacante, defensor, escolhaArma, setor, distancia, terreno);
    }

    private static int escolherArmaIA(Tanque atacante) {
        List<Arma> armas = atacante.getArmas();

        for (int i = 0; i < armas.size(); i++) {
            if (armas.get(i) instanceof Missil && armas.get(i).getMunicaoAtual() > 0)
                return i;
        }
        for (int i = 0; i < armas.size(); i++) {
            if (armas.get(i) instanceof Canhao && armas.get(i).getMunicaoAtual() > 0)
                return i;
        }
        for (int i = 0; i < armas.size(); i++) {
            if (armas.get(i) instanceof Metralhadora && armas.get(i).getMunicaoAtual() > 0)
                return i;
        }

        return 0;
    }

    private static String escolherSetorIA(Tanque defensor) {
        String classe = defensor.getClasse().toLowerCase();

        return switch (classe) {
            case "pesado" -> random.nextDouble() < 0.7 ? "lateral" : "traseiro";
            case "leve" -> random.nextDouble() < 0.8 ? "traseiro" : "lateral";
            default -> {
                String[] setores = { "frontal", "lateral", "traseiro" };
                yield setores[random.nextInt(setores.length)];
            }
        };
    }

    private static int escolherDistanciaIA(Arma arma) {
        if (arma instanceof Missil)
            return 300 + random.nextInt(200);
        if (arma instanceof Canhao)
            return 100 + random.nextInt(200);
        if (arma instanceof Metralhadora)
            return 50 + random.nextInt(100);
        return 150;
    }

    private static String escolherTerrenoIA() {
        String[] terrenos = { "campo aberto", "deserto", "urbano" };
        return terrenos[random.nextInt(terrenos.length)];
    }

    private static void executarTurnoHumano(Scanner scanner, Tanque atacante, Tanque defensor) {
        atacante.listarArmas();

        System.out.print("Escolha a arma (0-" + (atacante.getArmas().size() - 1) + "): ");
        int escolhaArma = Integer.parseInt(scanner.nextLine());

        System.out.print("Setor do alvo (frontal/lateral/traseiro): ");
        String setor = scanner.nextLine();

        System.out.print("Distância até o alvo (metros): ");
        int distancia = Integer.parseInt(scanner.nextLine());

        System.out.print("Terreno (campo aberto/deserto/urbano): ");
        String terreno = scanner.nextLine();

        executarAtaque(atacante, defensor, escolhaArma, setor, distancia, terreno);
    }

    private static void executarAtaque(Tanque atacante, Tanque defensor, int escolhaArma,
            String setor, int distancia, String terreno) {
        Arma arma = atacante.getArmas().get(escolhaArma);
        int dano = arma.calcularDano(setor, terreno, distancia);
        int danoFinal = calcularDanoFinal(dano, defensor.getBlindagem(), setor);
        int novaIntegridade = defensor.getIntegridade() - danoFinal;

        boolean abate = (novaIntegridade <= 0);
        defensor.setIntegridade(Math.max(0, novaIntegridade));

        System.out.println("\n" + atacante.getCodinome() + " atacou com " + arma.getNome());
        System.out.println("Dano base: " + dano + " | Dano final: " + danoFinal);
        System.out.println("Integridade de " + defensor.getCodinome() + ": " + defensor.getIntegridade() + "%");

        RankingService.atualizarEstatisticasBatalha(atacante, defensor, danoFinal, abate);
        atacante.atualizarEstado();
        defensor.atualizarEstado();
        atacante.atualizarStatusAutomatico();
        defensor.atualizarStatusAutomatico();

        RankingService.resetRankingSemanal();
        RankingService.resetRankingMensal();
    }

    private static int calcularDanoFinal(int danoBase, int blindagem, String setor) {
        double modificadorBlindagem = 1.0 - (blindagem / 200.0);
        double modificadorSetor = switch (setor.toLowerCase()) {
            case "frontal" -> 0.7;
            case "traseiro" -> 1.3;
            default -> 1.0;
        };
        return (int) (danoBase * modificadorBlindagem * modificadorSetor);
    }

    private static void exibirResultadoFinal(Tanque atacante, Tanque defensor) {
        System.out.println("\n===== RESULTADO FINAL =====");
        System.out.println(atacante.getCodinome() + ": " + atacante.getIntegridade() + "%");
        System.out.println(defensor.getCodinome() + ": " + defensor.getIntegridade() + "%");
    }
}