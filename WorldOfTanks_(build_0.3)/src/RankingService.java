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
            System.out.println("Ranking semanal resetado!");
            estatisticas.clear();
            eventosGlobais.clear();
            dataInicioSemana = LocalDate.now();
        }
    }
}