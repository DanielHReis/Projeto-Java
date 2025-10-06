package com.battletanks;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleSimulator {
    private static Random random = new Random();

    public static void simularBatalha(Scanner scanner, CadastroService cadastroService) {
        System.out.println("\n===== SIMULAÇÃO DE BATALHA =====");
        System.out.println("Modos disponíveis:");
        System.out.println("1. TREINO (1-4 tanques)");
        System.out.println("2. UM_VS_UM (2 tanques)");
        System.out.println("3. EQUIPES_3V3 (6 tanques)");
        System.out.println("4. EQUIPES_5V5 (10 tanques)");
        System.out.print("Escolha o modo: ");

        String modo = scanner.nextLine();

        switch (modo) {
            case "1" -> simularModoTreino(scanner, cadastroService);
            case "2" -> simularModo1v1(scanner, cadastroService);
            case "3" -> simularModo3v3(scanner, cadastroService);
            case "4" -> simularModo5v5(scanner, cadastroService);
            default -> System.out.println("Opção inválida!");
        }
    }
    private static void simularModoTreino(Scanner scanner, CadastroService cadastroService) {
        System.out.println("\n===== MODO TREINO =====");
        List<Tanque> tanquesDisponiveis = cadastroService.getTanques();

        if (tanquesDisponiveis.isEmpty()) {
            System.out.println("Nenhum tanque cadastrado!");
            return;
        }

        listarTanquesDisponiveis(tanquesDisponiveis);
        System.out.print("Quantos tanques para treino (1-4): ");
        int quantidade = Integer.parseInt(scanner.nextLine());

        if (quantidade < 1 || quantidade > 4) {
            System.out.println("Quantidade inválida! Use 1-4 tanques.");
            return;
        }

        List<Tanque> tanquesTreino = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            System.out.print("Escolha o tanque " + (i + 1) + ": ");
            int index = Integer.parseInt(scanner.nextLine());
            if (index >= 0 && index < tanquesDisponiveis.size()) {
                tanquesTreino.add(tanquesDisponiveis.get(index));
            }
        }

        executarTreino(scanner, tanquesTreino);
    }

    private static void simularModo1v1(Scanner scanner, CadastroService cadastroService) {
        System.out.println("\n===== MODO 1 VS 1 =====");
        List<Tanque> tanquesDisponiveis = cadastroService.getTanques();

        if (tanquesDisponiveis.size() < 2) {
            System.out.println("É necessário ter pelo menos 2 tanques!");
            return;
        }

        listarTanquesDisponiveis(tanquesDisponiveis);
        Tanque atacante = selecionarTanque("ATACANTE", tanquesDisponiveis, scanner);
        Tanque defensor = selecionarTanque("DEFENSOR", tanquesDisponiveis, scanner);

        if (atacante != null && defensor != null && atacante != defensor) {
            simularBatalhaEntreTanques(scanner, atacante, defensor);
        } else {
            System.out.println("Seleção inválida!");
        }
    }

    private static void simularModo3v3(Scanner scanner, CadastroService cadastroService) {
        System.out.println("\n===== MODO EQUIPES 3V3 =====");
        List<Tanque> tanquesDisponiveis = cadastroService.getTanques();

        if (tanquesDisponiveis.size() < 6) {
            System.out.println("É necessário ter pelo menos 6 tanques!");
            return;
        }

        simularBatalhaEmEquipes(scanner, tanquesDisponiveis, 3);
    }

    private static void simularModo5v5(Scanner scanner, CadastroService cadastroService) {
        System.out.println("\n===== MODO EQUIPES 5V5 =====");
        List<Tanque> tanquesDisponiveis = cadastroService.getTanques();

        if (tanquesDisponiveis.size() < 10) {
            System.out.println("É necessário ter pelo menos 10 tanques!");
            return;
        }

        simularBatalhaEmEquipes(scanner, tanquesDisponiveis, 5);
    }

    private static void simularBatalhaEmEquipes(Scanner scanner, List<Tanque> tanquesDisponiveis, int tamanhoEquipe) {
        System.out.println("Selecionando equipe A (" + tamanhoEquipe + " tanques):");
        List<Tanque> equipeA = selecionarEquipe(scanner, tanquesDisponiveis, tamanhoEquipe, "A");

        System.out.println("Selecionando equipe B (" + tamanhoEquipe + " tanques):");
        List<Tanque> equipeB = selecionarEquipe(scanner, tanquesDisponiveis, tamanhoEquipe, "B");

        if (equipeA.size() == tamanhoEquipe && equipeB.size() == tamanhoEquipe) {
            executarBatalhaEquipes(scanner, equipeA, equipeB);
        } else {
            System.out.println("Não foi possível formar as equipes!");
        }
    }

    private static List<Tanque> selecionarEquipe(Scanner scanner, List<Tanque> tanquesDisponiveis, int tamanho,
            String nomeEquipe) {
        List<Tanque> equipe = new ArrayList<>();
        List<Tanque> disponiveis = new ArrayList<>(tanquesDisponiveis);

        for (int i = 0; i < tamanho; i++) {
            listarTanquesDisponiveis(disponiveis);
            System.out.print("Escolha tanque " + (i + 1) + " para equipe " + nomeEquipe + ": ");
            int index = Integer.parseInt(scanner.nextLine());

            if (index >= 0 && index < disponiveis.size()) {
                Tanque selecionado = disponiveis.get(index);
                equipe.add(selecionado);
                disponiveis.remove(selecionado);
                System.out.println(selecionado.getCodinome() + " adicionado à equipe " + nomeEquipe);
            } else {
                System.out.println("Seleção inválida!");
                i--; // Repetir esta seleção
            }
        }
        return equipe;
    }

    private static void executarTreino(Scanner scanner, List<Tanque> tanques) {
        System.out.println("\n===== INICIANDO TREINO =====");
        System.out.println("Tanques participantes:");
        for (Tanque tanque : tanques) {
            System.out.println("- " + tanque.getCodinome() + " (" + tanque.getClasse() + ")");
        }

        // Simulação simplificada de treino
        for (Tanque tanque : tanques) {
            System.out.println("\n--- " + tanque.getCodinome() + " em treino ---");
            tanque.listarArmas();

            // Disparos de treino
            for (int i = 0; i < 3; i++) {
                if (tanque.getArmas().size() > 0) {
                    Arma arma = tanque.getArmas().get(0);
                    if (arma.getMunicaoAtual() > 0) {
                        arma.atirar();
                        System.out.println("Alvo atingido! Dano simulado: " +
                                arma.calcularDano("frontal", "campo aberto", 100));
                    }
                }
            }

            tanque.atualizarEstado();
        }

        System.out.println("\nTreino concluído!");
    }

    private static void executarBatalhaEquipes(Scanner scanner, List<Tanque> equipeA, List<Tanque> equipeB) {
        System.out.println("\n===== BATALHA DE EQUIPES =====");
        System.out.println("EQUIPE A:");
        for (Tanque tanque : equipeA) {
            System.out.println("- " + tanque.getCodinome());
        }

        System.out.println("EQUIPE B:");
        for (Tanque tanque : equipeB) {
            System.out.println("- " + tanque.getCodinome());
        }

        // Simulação simplificada de batalha entre equipes
        int turnos = 5;
        Random random = new Random();

        for (int turno = 1; turno <= turnos; turno++) {
            System.out.println("\n--- TURNO " + turno + " ---");

            // Equipe A ataca
            if (!equipeA.isEmpty() && !equipeB.isEmpty()) {
                Tanque atacante = equipeA.get(random.nextInt(equipeA.size()));
                Tanque defensor = equipeB.get(random.nextInt(equipeB.size()));

                System.out.println(
                        atacante.getCodinome() + " (Equipe A) ataca " + defensor.getCodinome() + " (Equipe B)");
                executarAtaqueSimulado(atacante, defensor);

                // Verificar se defensor foi destruído
                if (defensor.getIntegridade() <= 0) {
                    System.out.println(defensor.getCodinome() + " DESTRUÍDO!");
                    equipeB.remove(defensor);
                }
            }

            // Equipe B ataca
            if (!equipeA.isEmpty() && !equipeB.isEmpty()) {
                Tanque atacante = equipeB.get(random.nextInt(equipeB.size()));
                Tanque defensor = equipeA.get(random.nextInt(equipeA.size()));

                System.out.println(
                        atacante.getCodinome() + " (Equipe B) ataca " + defensor.getCodinome() + " (Equipe A)");
                executarAtaqueSimulado(atacante, defensor);

                // Verificar se defensor foi destruído
                if (defensor.getIntegridade() <= 0) {
                    System.out.println(defensor.getCodinome() + " DESTRUÍDO!");
                    equipeA.remove(defensor);
                }
            }

            // Verificar fim da batalha
            if (equipeA.isEmpty() || equipeB.isEmpty()) {
                break;
            }
        }

        // Resultado final
        System.out.println("\n===== RESULTADO FINAL =====");
        if (equipeA.isEmpty() && equipeB.isEmpty()) {
            System.out.println("EMPATE! Ambas as equipes foram destruídas.");
        } else if (equipeA.isEmpty()) {
            System.out.println("VITÓRIA DA EQUIPE B!");
        } else if (equipeB.isEmpty()) {
            System.out.println("VITÓRIA DA EQUIPE A!");
        } else {
            System.out.println("TEMPO ESGOTADO! ");
            System.out.println("Equipe A restante: " + equipeA.size() + " tanques");
            System.out.println("Equipe B restante: " + equipeB.size() + " tanques");
        }
    }

    private static void executarAtaqueSimulado(Tanque atacante, Tanque defensor) {
    if (atacante.getArmas().isEmpty())
        return;

    Arma arma = atacante.getArmas().get(0);
    if (arma.getMunicaoAtual() > 0) {
        int dano = arma.calcularDano("frontal", "campo aberto", 150);
        int danoFinal = Math.max(5, dano / 2); // Dano reduzido para simulação

        int novaIntegridade = defensor.getIntegridade() - danoFinal;
        
        boolean abate = (novaIntegridade <= 0);
        defensor.setIntegridade(Math.max(0, novaIntegridade));

        System.out.println("Dano causado: " + danoFinal + " | Integridade: " + defensor.getIntegridade() + "%");

        // Atualizar estatísticas de ranking
        RankingService.atualizarEstatisticasBatalha(atacante, defensor, danoFinal, abate);
        
        arma.atirar();
        atacante.atualizarEstado();
        defensor.atualizarEstado();
    }
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
                System.out.println("\n " + atacante.getCodinome() + " VENCEU A BATALHA!");
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

    // Atualizar estatísticas de ranking
    RankingService.atualizarEstatisticasBatalha(atacante, defensor, danoFinal, abate);
    
    atacante.atualizarEstado();
    defensor.atualizarEstado();
    atacante.atualizarStatusAutomatico();
    defensor.atualizarStatusAutomatico();
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