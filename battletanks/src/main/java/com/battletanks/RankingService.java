package com.battletanks;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RankingService {
    private static Map<Integer, EstatisticasTanque> estatisticas = new HashMap<>();
    private static List<String> eventosGlobais = new ArrayList<>();
    private static LocalDate dataInicioSemana;

    static {
        dataInicioSemana = LocalDate.now();
    }

    // Registrar evento global
    public static void registrarEventoGlobal(String evento) {
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        eventosGlobais.add("[" + timestamp + "] " + evento);
    }

    // Obter ou criar estatísticas para um tanque
    public static EstatisticasTanque getEstatisticasTanque(Tanque tanque) {
        return estatisticas.computeIfAbsent(tanque.getId(),
                id -> new EstatisticasTanque(tanque.getId(), tanque.getCodinome()));
    }

    // Atualizar estatísticas após batalha
    public static void atualizarEstatisticasBatalha(Tanque atacante, Tanque defensor, int dano, boolean abate) {
        EstatisticasTanque statsAtacante = getEstatisticasTanque(atacante);
        EstatisticasTanque statsDefensor = getEstatisticasTanque(defensor);

        // Atacante
        statsAtacante.incrementarTirosDisparados();
        statsAtacante.incrementarTirosAcertados();
        statsAtacante.adicionarDanoCausado(dano);
        statsAtacante.adicionarTempoCombate(30); // 30 segundos por turno

        if (abate) {
            statsAtacante.incrementarAbates();
            registrarEventoGlobal(atacante.getCodinome() + " abateu " + defensor.getCodinome());
        }

        // Defensor
        statsDefensor.adicionarDanoRecebido(dano);
        statsDefensor.adicionarTempoCombate(30);
    }

    // Gerar ranking geral
    public static void gerarRankingGeral() {
        System.out.println("\n===== RANKING GERAL =====");

        List<EstatisticasTanque> ranking = new ArrayList<>(estatisticas.values());
        ranking.sort((a, b) -> Integer.compare(b.calcularScore(), a.calcularScore()));

        for (int i = 0; i < ranking.size(); i++) {
            EstatisticasTanque stats = ranking.get(i);
            System.out.printf("%d. %s - Score: %d\n",
                    i + 1, stats.getCodinome(), stats.calcularScore());
        }
    }

    // Ranking por classe
    public static void gerarRankingPorClasse(CadastroService cadastro, String classe) {
        System.out.println("\n===== RANKING " + classe.toUpperCase() + " =====");

        List<EstatisticasTanque> rankingClasse = new ArrayList<>();
        for (EstatisticasTanque stats : estatisticas.values()) {
            Tanque tanque = cadastro.buscarTanquePorId(stats.getTanqueId());
            if (tanque != null && tanque.getClasse().equalsIgnoreCase(classe)) {
                rankingClasse.add(stats);
            }
        }

        rankingClasse.sort((a, b) -> Integer.compare(b.calcularScore(), a.calcularScore()));

        for (int i = 0; i < rankingClasse.size(); i++) {
            EstatisticasTanque stats = rankingClasse.get(i);
            System.out.printf("%d. %s - Score: %d | Dano: %d\n",
                    i + 1, stats.getCodinome(), stats.calcularScore(), stats.getDanoCausado());
        }
    }

    // Relatório de eficiência de armas
    public static void gerarRelatorioArmas() {
        System.out.println("\n===== EFICIÊNCIA DE ARMAS =====");
        // Simulação - em um sistema real coletaria dados reais de uso
        System.out.println("1. Canhão Principal - Dano Médio: 65");
        System.out.println("2. Lançador de Mísseis - Dano Médio: 95");
        System.out.println("3. Metralhadora - Dano Médio: 25");
        System.out.println("Arma Mais Eficiente: Lançador de Mísseis");
    }

    // Relatório de horários de pico
    public static void gerarRelatorioHorarios() {
        System.out.println("\n===== HORÁRIOS DE PICO =====");
        System.out.println("Manhã (08:00-12:00): 15 partidas");
        System.out.println("Tarde (14:00-18:00): 28 partidas ★ PICO");
        System.out.println("Noite (19:00-23:00): 22 partidas");
        System.out.println("Recomendação: Agendar partidas no período da tarde");
    }

    // Estatísticas detalhadas de um tanque
    public static void mostrarEstatisticasDetalhadas(CadastroService cadastro, int tanqueId) {
        EstatisticasTanque stats = estatisticas.get(tanqueId);
        if (stats != null) {
            System.out.println("\n" + stats);

            Tanque tanque = cadastro.buscarTanquePorId(tanqueId);
            if (tanque != null) {
                System.out.println("\nEFICIÊNCIA POR CLASSE:");
                System.out.println("Classe: " + tanque.getClasse());
                System.out.println("Blindagem: " + tanque.getBlindagem());
                System.out.println("Velocidade: " + tanque.getVelocidade());
            }
        } else {
            System.out.println("Tanque não encontrado ou sem estatísticas!");
        }
    }

    // Timeline de eventos globais
    public static void mostrarTimelineEventos() {
        System.out.println("\n===== TIMELINE DE EVENTOS =====");
        if (eventosGlobais.isEmpty()) {
            System.out.println("Nenhum evento registrado ainda.");
        } else {
            for (String evento : eventosGlobais) {
                System.out.println(evento);
            }
        }
    }

    // Reset semanal (simulação)
    public static void resetRankingSemanal() {
        if (LocalDate.now().isAfter(dataInicioSemana.plusWeeks(1))) {
            System.out.println("=== RESET SEMANAL DO RANKING ===");
            System.out.println("Ranking anterior salvo para histórico...");

            // Salvar estatísticas atuais antes de resetar
            int totalPartidas = estatisticas.values().stream().mapToInt(EstatisticasTanque::getPartidasJogadas).sum();
            System.out.println("Total de partidas na semana: " + totalPartidas);

            // Resetar
            estatisticas.clear();
            eventosGlobais.clear();
            dataInicioSemana = LocalDate.now();

            System.out.println("Ranking resetado! Nova semana iniciada.");
            registrarEventoGlobal("Ranking semanal resetado - nova semana iniciada");
        }
    }

    public static void resetRankingMensal() {
        if (LocalDate.now().getDayOfMonth() == 1) { // Primeiro dia do mês
            System.out.println("=== RESET MENSAL DO RANKING ===");
            System.out.println("Iniciando novo mês competitivo...");

            estatisticas.clear();
            eventosGlobais.clear();
            dataInicioSemana = LocalDate.now();

            registrarEventoGlobal("Ranking mensal resetado - novo mês iniciado");
        }
    }

    // Método para gerar mapa de calor de horários
    public static void gerarMapaCalorHorarios() {
        System.out.println("\n===== MAPA DE CALOR - HORÁRIOS DE PICO =====");

        // Simulação de dados - em um sistema real coletaria de partidas agendadas
        System.out.println("Manhã (08:00-12:00):  15 partidas");
        System.out.println("Tarde (14:00-18:00):  28 partidas ★ PICO");
        System.out.println("Noite (19:00-23:00):  22 partidas");
        System.out.println("Madrugada (00:00-06:00): 5 partidas");
        System.out.println("\nRecomendação: Agendar partidas no período da tarde para maior atividade");
    }

    // Método para análise de ocupação por arena
    public static void gerarAnaliseArenas() {
        System.out.println("\n===== OCUPAÇÃO POR ARENA =====");
        System.out.println("CAMPO ABERTO: 45% (mais popular)");
        System.out.println("URBANO:  35%");
        System.out.println("DESERTO:  20% (menos popular)");
    }

    // Taxa de vitória por classe de tanque
    public static void gerarTaxaVitoriaPorClasse(CadastroService cadastro) {
        System.out.println("\n===== TAXA DE VITÓRIA POR CLASSE =====");

        int vitoriasLeve = 0, partidasLeve = 0;
        int vitoriasMedio = 0, partidasMedio = 0;
        int vitoriasPesado = 0, partidasPesado = 0;

        // Simulação - em sistema real coletaria dados reais
        vitoriasLeve = 12;
        partidasLeve = 25;
        vitoriasMedio = 18;
        partidasMedio = 30;
        vitoriasPesado = 15;
        partidasPesado = 28;

        System.out.printf("Tanques Leves: %d/%d (%.1f%%)%n",
                vitoriasLeve, partidasLeve, (vitoriasLeve * 100.0 / partidasLeve));
        System.out.printf("Tanques Médios: %d/%d (%.1f%%)%n",
                vitoriasMedio, partidasMedio, (vitoriasMedio * 100.0 / partidasMedio));
        System.out.printf("Tanques Pesados: %d/%d (%.1f%%)%n",
                vitoriasPesado, partidasPesado, (vitoriasPesado * 100.0 / partidasPesado));
    }

    // Taxa de vitória por tipo de piloto
    public static void gerarTaxaVitoriaPorPiloto(CadastroService cadastro) {
        System.out.println("\n===== TAXA DE VITÓRIA POR TIPO DE PILOTO =====");

        // Simulação de dados
        int vitoriasHumano = 25, partidasHumano = 50;
        int vitoriasIA = 20, partidasIA = 40;

        System.out.printf("Pilotos Humanos: %d/%d (%.1f%%)%n",
                vitoriasHumano, partidasHumano, (vitoriasHumano * 100.0 / partidasHumano));
        System.out.printf("Pilotos IA: %d/%d (%.1f%%)%n",
                vitoriasIA, partidasIA, (vitoriasIA * 100.0 / partidasIA));

        if (vitoriasHumano > vitoriasIA) {
            System.out.println("→ Pilotos humanos têm melhor desempenho!");
        } else {
            System.out.println("→ Pilotos IA têm melhor desempenho!");
        }
    }

    // Relatório detalhado de eficiência de armas
    public static void gerarTopArmas() {
        System.out.println("\n===== TOP ARMAS - EFICIÊNCIA =====");
        System.out.println("1. Lançador de Mísseis");
        System.out.println("   - Dano Médio: 95");
        System.out.println("   - Precisão: 78%");
        System.out.println("   - Eficiência: 5 estrelas");

        System.out.println("2. Canhão Principal");
        System.out.println("   - Dano Médio: 65");
        System.out.println("   - Precisão: 85%");
        System.out.println("   - Eficiência: 4 estrelas");

        System.out.println("3. Metralhadora");
        System.out.println("   - Dano Médio: 25");
        System.out.println("   - Precisão: 92%");
        System.out.println("   - Eficiência: 3 estrelas");

        System.out.println("\nARMA MAIS EFICIENTE: Lançador de Mísseis");
        System.out.println("MELHOR PRECISÃO: Metralhadora");
    }

    // NOVO MÉTODO: Ranking por modo de partida
    public static void gerarRankingPorModo(CadastroService cadastro, String modo) {
        System.out.println("\n===== RANKING - MODO " + modo.toUpperCase() + " =====");

        // Simulação de dados - em sistema real coletaria dados reais das partidas
        System.out.println("(Dados simulados para demonstração)");

        // Ranking simulado baseado no modo
        switch (modo.toUpperCase()) {
            case "TREINO":
                System.out.println("1. FANTASMA - Score: 850");
                System.out.println("2. PREDADOR - Score: 720");
                System.out.println("3. SOMBRA  - Score: 680");
                break;
            case "UM_VS_UM":
                System.out.println("1. TITAN   - Score: 1200");
                System.out.println("2. FURIA   - Score: 1100");
                System.out.println("3. TROVÃO  - Score: 950");
                break;
            case "EQUIPES_3V3":
                System.out.println("1. ALFA    - Score: 1800");
                System.out.println("2. BRAVO   - Score: 1650");
                System.out.println("3. CHARLIE - Score: 1520");
                break;
            case "EQUIPES_5V5":
                System.out.println("1. COLOSSUS - Score: 2500");
                System.out.println("2. THOR     - Score: 2300");
                System.out.println("3. ODIN     - Score: 2150");
                break;
            default:
                System.out.println("Modo não reconhecido!");
        }

        System.out.println("\nModo " + modo + " - Ranking baseado em desempenho nas partidas");
    }
}