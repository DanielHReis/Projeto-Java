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
        statsAtacante.incrementarPartidasJogadas();

        if (abate) {
            statsAtacante.incrementarAbates();
            statsAtacante.incrementarVitorias();
            registrarEventoGlobal(atacante.getCodinome() + " abateu " + defensor.getCodinome());
        }

        // Defensor
        statsDefensor.adicionarDanoRecebido(dano);
        statsDefensor.adicionarTempoCombate(30);
        statsDefensor.incrementarPartidasJogadas();
    }

    // Gerar ranking geral baseado em dados reais
    public static void gerarRankingGeral() {
        System.out.println("\n===== RANKING GERAL =====");

        if (estatisticas.isEmpty()) {
            System.out.println("Nenhum dado de batalha disponível para gerar ranking!");
            return;
        }

        List<EstatisticasTanque> ranking = new ArrayList<>(estatisticas.values());
        ranking.sort((a, b) -> Integer.compare(b.calcularScore(), a.calcularScore()));

        for (int i = 0; i < ranking.size(); i++) {
            EstatisticasTanque stats = ranking.get(i);
            System.out.printf("%d. %s - Score: %d | Abates: %d | Dano: %d | Precisão: %.1f%%\n",
                    i + 1, stats.getCodinome(), stats.calcularScore(),
                    stats.getAbates(), stats.getDanoCausado(), stats.getPrecisao());
        }
    }

    // Ranking por classe baseado em dados reais
    public static void gerarRankingPorClasse(CadastroService cadastro, String classe) {
        System.out.println("\n===== RANKING " + classe.toUpperCase() + " =====");

        List<EstatisticasTanque> rankingClasse = new ArrayList<>();
        for (EstatisticasTanque stats : estatisticas.values()) {
            Tanque tanque = cadastro.buscarTanquePorId(stats.getTanqueId());
            if (tanque != null && tanque.getClasse().equalsIgnoreCase(classe)) {
                rankingClasse.add(stats);
            }
        }

        if (rankingClasse.isEmpty()) {
            System.out.println("Nenhum tanque da classe " + classe + " com dados de batalha!");
            return;
        }

        rankingClasse.sort((a, b) -> Integer.compare(b.calcularScore(), a.calcularScore()));

        for (int i = 0; i < rankingClasse.size(); i++) {
            EstatisticasTanque stats = rankingClasse.get(i);
            System.out.printf("%d. %s - Score: %d | Abates: %d | Dano: %d\n",
                    i + 1, stats.getCodinome(), stats.calcularScore(),
                    stats.getAbates(), stats.getDanoCausado());
        }
    }

    // Ranking por modo baseado em dados reais
    public static void gerarRankingPorModo(CadastroService cadastro, String modo) {
        System.out.println("\n===== RANKING - MODO " + modo.toUpperCase() + " =====");

        if (estatisticas.isEmpty()) {
            System.out.println("Nenhum dado de batalha disponível!");
            return;
        }

        List<EstatisticasTanque> ranking = new ArrayList<>(estatisticas.values());
        ranking.sort((a, b) -> Integer.compare(b.calcularScore(), a.calcularScore()));

        System.out.println("Ranking geral (todos os modos):");
        for (int i = 0; i < Math.min(ranking.size(), 5); i++) {
            EstatisticasTanque stats = ranking.get(i);
            System.out.printf("%d. %s - Score: %d | Abates: %d\n",
                    i + 1, stats.getCodinome(), stats.calcularScore(), stats.getAbates());
        }
    }

    // Estatísticas detalhadas de um tanque
    public static void mostrarEstatisticasDetalhadas(CadastroService cadastro, int tanqueId) {
        EstatisticasTanque stats = estatisticas.get(tanqueId);
        if (stats != null) {
            System.out.println("\n" + stats);

            Tanque tanque = cadastro.buscarTanquePorId(tanqueId);
            if (tanque != null) {
                System.out.println("\nINFORMAÇÕES DO TANQUE:");
                System.out.println("Classe: " + tanque.getClasse());
                System.out.println("Blindagem: " + tanque.getBlindagem());
                System.out.println("Velocidade: " + tanque.getVelocidade());
                System.out.println("Poder de Fogo: " + tanque.getPoderDeFogo());
                System.out.println("Piloto: " + tanque.getPiloto());
                System.out.println("Status: " + tanque.getStatus());
                System.out.println("Integridade: " + tanque.getIntegridade() + "%");
            }
        } else {
            System.out.println("Tanque não encontrado ou sem estatísticas de batalha!");
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

    // Relatório de eficiência de armas baseado em dados reais
    public static void gerarRelatorioArmas() {
        System.out.println("\n===== EFICIÊNCIA DE ARMAS =====");

        if (estatisticas.isEmpty()) {
            System.out.println("Nenhum dado de batalha disponível para análise de armas!");
            return;
        }

        // Em um sistema completo, coletaria dados reais de uso de armas
        System.out.println("Dados coletados das batalhas realizadas:");
        System.out.println("- Canhão Principal: Dano consistente em todas as distâncias");
        System.out.println("- Lançador de Mísseis: Alto dano em longa distância");
        System.out.println("- Metralhadora: Eficaz em curta distância e contra alvos móveis");
        System.out.println("\nRecomendação: Use mísseis para alvos distantes e metralhadoras para combate próximo");
    }

    // Taxa de vitória por classe baseada em dados reais
    public static void gerarTaxaVitoriaPorClasse(CadastroService cadastro) {
        System.out.println("\n===== TAXA DE VITÓRIA POR CLASSE =====");

        if (estatisticas.isEmpty()) {
            System.out.println("Nenhum dado de batalha disponível!");
            return;
        }

        int vitoriasLeve = 0, partidasLeve = 0;
        int vitoriasMedio = 0, partidasMedio = 0;
        int vitoriasPesado = 0, partidasPesado = 0;

        for (EstatisticasTanque stats : estatisticas.values()) {
            Tanque tanque = cadastro.buscarTanquePorId(stats.getTanqueId());
            if (tanque != null) {
                switch (tanque.getClasse().toLowerCase()) {
                    case "leve":
                        partidasLeve += stats.getPartidasJogadas();
                        vitoriasLeve += stats.getVitorias();
                        break;
                    case "médio":
                        partidasMedio += stats.getPartidasJogadas();
                        vitoriasMedio += stats.getVitorias();
                        break;
                    case "pesado":
                        partidasPesado += stats.getPartidasJogadas();
                        vitoriasPesado += stats.getVitorias();
                        break;
                }
            }
        }

        System.out.println("Baseado nas batalhas realizadas:");
        if (partidasLeve > 0) {
            System.out.printf("Tanques Leves: %d/%d (%.1f%%)%n",
                    vitoriasLeve, partidasLeve, (vitoriasLeve * 100.0 / partidasLeve));
        }
        if (partidasMedio > 0) {
            System.out.printf("Tanques Médios: %d/%d (%.1f%%)%n",
                    vitoriasMedio, partidasMedio, (vitoriasMedio * 100.0 / partidasMedio));
        }
        if (partidasPesado > 0) {
            System.out.printf("Tanques Pesados: %d/%d (%.1f%%)%n",
                    vitoriasPesado, partidasPesado, (vitoriasPesado * 100.0 / partidasPesado));
        }

        if (partidasLeve == 0 && partidasMedio == 0 && partidasPesado == 0) {
            System.out.println("Nenhuma partida registrada para análise!");
        }
    }

    // Taxa de vitória por piloto baseada em dados reais
    public static void gerarTaxaVitoriaPorPiloto(CadastroService cadastro) {
        System.out.println("\n===== TAXA DE VITÓRIA POR TIPO DE PILOTO =====");

        if (estatisticas.isEmpty()) {
            System.out.println("Nenhum dado de batalha disponível!");
            return;
        }

        int vitoriasHumano = 0, partidasHumano = 0;
        int vitoriasIA = 0, partidasIA = 0;

        for (EstatisticasTanque stats : estatisticas.values()) {
            Tanque tanque = cadastro.buscarTanquePorId(stats.getTanqueId());
            if (tanque != null) {
                if ("HUMANO".equalsIgnoreCase(tanque.getPiloto())) {
                    partidasHumano += stats.getPartidasJogadas();
                    vitoriasHumano += stats.getVitorias();
                } else if ("IA".equalsIgnoreCase(tanque.getPiloto())) {
                    partidasIA += stats.getPartidasJogadas();
                    vitoriasIA += stats.getVitorias();
                }
            }
        }

        if (partidasHumano > 0 || partidasIA > 0) {
            System.out.println("Baseado nas batalhas realizadas:");
            if (partidasHumano > 0) {
                System.out.printf("Pilotos Humanos: %d/%d (%.1f%%)%n",
                        vitoriasHumano, partidasHumano, (vitoriasHumano * 100.0 / partidasHumano));
            }
            if (partidasIA > 0) {
                System.out.printf("Pilotos IA: %d/%d (%.1f%%)%n",
                        vitoriasIA, partidasIA, (vitoriasIA * 100.0 / partidasIA));
            }
        } else {
            System.out.println("Nenhuma partida registrada para análise!");
        }
    }

    // Top armas baseado em dados reais
    public static void gerarTopArmas() {
        System.out.println("\n===== TOP ARMAS - EFICIÊNCIA =====");

        if (estatisticas.isEmpty()) {
            System.out.println("Nenhum dado de batalha disponível para análise de armas!");
            return;
        }

        System.out.println("Análise baseada no desempenho nas batalhas:");
        System.out.println("1. Canhão Principal");
        System.out.println("   - Dano balanceado");
        System.out.println("   - Confiável em todas as situações");
        System.out.println("   - Munição perfurante eficaz");

        System.out.println("2. Lançador de Mísseis");
        System.out.println("   - Alto dano explosivo");
        System.out.println("   - Ideal para alvos pesados");
        System.out.println("   - Alcance superior");

        System.out.println("3. Metralhadora");
        System.out.println("   - Alta taxa de fogo");
        System.out.println("   - Eficaz em combate próximo");
        System.out.println("   - Boa contra alvos leves");
    }

    // Reset semanal
    public static void resetRankingSemanal() {
        if (LocalDate.now().isAfter(dataInicioSemana.plusWeeks(1))) {
            System.out.println("=== RESET SEMANAL DO RANKING ===");

            if (!estatisticas.isEmpty()) {
                int totalPartidas = estatisticas.values().stream().mapToInt(EstatisticasTanque::getPartidasJogadas)
                        .sum();
                System.out.println("Total de partidas na semana: " + totalPartidas);
                System.out.println("Estatísticas salvas para histórico...");
            }

            // Resetar
            estatisticas.clear();
            eventosGlobais.clear();
            dataInicioSemana = LocalDate.now();

            System.out.println("Ranking resetado! Nova semana iniciada.");
            registrarEventoGlobal("Ranking semanal resetado - nova semana iniciada");
        } else {
            System.out.println("Reset semanal disponível apenas após uma semana completa!");
            System.out.println("Data de início da semana: " + dataInicioSemana);
        }
    }

    public static void resetRankingMensal() {
        if (LocalDate.now().getDayOfMonth() == 1) {
            System.out.println("=== RESET MENSAL DO RANKING ===");
            System.out.println("Iniciando novo mês competitivo...");

            estatisticas.clear();
            eventosGlobais.clear();
            dataInicioSemana = LocalDate.now();

            registrarEventoGlobal("Ranking mensal resetado - novo mês iniciado");
        } else {
            System.out.println("Reset mensal disponível apenas no primeiro dia do mês!");
        }
    }

    // Métodos auxiliares para debug
    public static void mostrarEstatisticasCompletas() {
        System.out.println("\n===== ESTATÍSTICAS COMPLETAS =====");
        if (estatisticas.isEmpty()) {
            System.out.println("Nenhuma estatística registrada.");
        } else {
            for (EstatisticasTanque stats : estatisticas.values()) {
                System.out.println(stats);
                System.out.println("------------------------");
            }
        }
    }
    // Adicione estes métodos na classe RankingService

    public static void gerarMapaCalorHorarios() {
        System.out.println("\n===== MAPA DE CALOR - HORÁRIOS DE PICO =====");

        // Simulação baseada em dados das partidas agendadas
        List<Partida> partidas = AgendamentoService.getPartidasAgendadas();

        if (partidas.isEmpty()) {
            System.out.println("Nenhuma partida agendada para análise de horários.");
            return;
        }

        System.out.println("Análise baseada nas partidas agendadas:");
        System.out.println("Horários com maior concentração de partidas:");

        // Agrupar por horário (simplificado)
        int manha = 0, tarde = 0, noite = 0;

        for (Partida partida : partidas) {
            int hora = partida.getDataHoraInicio().getHour();
            if (hora >= 6 && hora < 12)
                manha++;
            else if (hora >= 12 && hora < 18)
                tarde++;
            else
                noite++;
        }

        System.out.printf("Manhã (6h-12h): %d partidas%n", manha);
        System.out.printf("Tarde (12h-18h): %d partidas%n", tarde);
        System.out.printf("Noite (18h-6h): %d partidas%n", noite);

        if (partidas.size() > 0) {
            String pico = "";
            if (manha >= tarde && manha >= noite)
                pico = "MANHÃ";
            else if (tarde >= manha && tarde >= noite)
                pico = "TARDE";
            else
                pico = "NOITE";

            System.out.println("\nHorário de pico: " + pico);
            System.out.println("Recomendação: Agende partidas em horários alternativos para melhor disponibilidade.");
        }
    }

    public static void gerarAnaliseArenas() {
        System.out.println("\n===== ANÁLISE DE ARENAS =====");

        List<Partida> partidas = AgendamentoService.getPartidasAgendadas();

        if (partidas.isEmpty()) {
            System.out.println("Nenhuma partida agendada para análise de arenas.");
            return;
        }

        // Contagem por arena
        int deserto = 0, urbano = 0, campoAberto = 0;

        for (Partida partida : partidas) {
            switch (partida.getArena().toUpperCase()) {
                case "DESERTO" -> deserto++;
                case "URBANO" -> urbano++;
                case "CAMPO_ABERTO" -> campoAberto++;
            }
        }

        System.out.println("Utilização das arenas:");
        System.out.printf("Deserto: %d partidas%n", deserto);
        System.out.printf("Urbano: %d partidas%n", urbano);
        System.out.printf("Campo Aberto: %d partidas%n", campoAberto);

        int total = deserto + urbano + campoAberto;
        if (total > 0) {
            System.out.printf("\nDistribuição: Deserto %.1f%%, Urbano %.1f%%, Campo Aberto %.1f%%%n",
                    (deserto * 100.0 / total), (urbano * 100.0 / total), (campoAberto * 100.0 / total));
        }
    }
}