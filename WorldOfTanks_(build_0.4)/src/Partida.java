import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Partida {
    private int id;
    private String modo; // TREINO, UM_VS_UM, EQUIPES
    private LocalDateTime dataHoraInicio;
    private int duracaoMinutos;
    private String arena; // DESERTO, URBANO, CAMPO_ABERTO
    private List<Tanque> tanquesParticipantes;
    private String status; // AGENDADA, EM_ANDAMENTO, CONCLUIDA, CANCELADA
    private List<String> eventos;

    public Partida(int id, String modo, LocalDateTime dataHoraInicio, int duracaoMinutos, String arena) {
        this.id = id;
        this.modo = modo.toUpperCase();
        this.dataHoraInicio = dataHoraInicio;
        this.duracaoMinutos = duracaoMinutos;
        this.arena = arena.toUpperCase();
        this.tanquesParticipantes = new ArrayList<>();
        this.status = "AGENDADA";
        this.eventos = new ArrayList<>();
        adicionarEvento("Partida agendada para " + getDataHoraFormatada());
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getModo() { return modo; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public int getDuracaoMinutos() { return duracaoMinutos; }
    public String getArena() { return arena; }
    public List<Tanque> getTanquesParticipantes() { return tanquesParticipantes; }
    public String getStatus() { return status; }
    public List<String> getEventos() { return eventos; }

    public void setStatus(String status) { 
        this.status = status; 
        adicionarEvento("Status alterado para: " + status);
    }

    // Métodos de negócio
    public boolean adicionarTanque(Tanque tanque) {
        if (tanquesParticipantes.size() >= getLimiteTanques()) {
            adicionarEvento("Falha ao adicionar " + tanque.getCodinome() + " - Limite atingido");
            return false;
        }
        
        if (tanquesParticipantes.contains(tanque)) {
            adicionarEvento("Falha ao adicionar " + tanque.getCodinome() + " - Tanque já participa");
            return false;
        }

        tanquesParticipantes.add(tanque);
        adicionarEvento("Tanque " + tanque.getCodinome() + " adicionado à partida");
        return true;
    }

    public boolean removerTanque(Tanque tanque) {
        boolean removido = tanquesParticipantes.remove(tanque);
        if (removido) {
            adicionarEvento("Tanque " + tanque.getCodinome() + " removido da partida");
        }
        return removido;
    }

    public boolean verificarConflito(Tanque tanque, LocalDateTime dataHora) {
        LocalDateTime fimPartida = dataHoraInicio.plusMinutes(duracaoMinutos);
        LocalDateTime fimNovaPartida = dataHora.plusMinutes(duracaoMinutos);
        
        // Verifica se o tanque já está em outra partida no mesmo horário
        for (Partida p : AgendamentoService.getPartidasAgendadas()) {
            if (p.getTanquesParticipantes().contains(tanque)) {
                LocalDateTime fimP = p.getDataHoraInicio().plusMinutes(p.getDuracaoMinutos());
                if (dataHora.isBefore(fimP) && fimNovaPartida.isAfter(p.getDataHoraInicio())) {
                    return true; // Conflito detectado
                }
            }
        }
        return false;
    }

    public int getLimiteTanques() {
        return switch (modo) {
            case "TREINO" -> 4;
            case "UM_VS_UM" -> 2;
            case "EQUIPES_3V3" -> 6;
            case "EQUIPES_5V5" -> 10;
            default -> 2;
        };
    }

    public String getDataHoraFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataHoraInicio.format(formatter);
    }

    public void adicionarEvento(String evento) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        eventos.add("[" + timestamp + "] " + evento);
    }

    public void iniciarPartida() {
        this.status = "EM_ANDAMENTO";
        adicionarEvento("=== PARTIDA INICIADA ===");
        adicionarEvento("Modo: " + modo + " | Arena: " + arena);
        adicionarEvento("Participantes: " + tanquesParticipantes.size() + " tanques");
    }

    public void finalizarPartida() {
        this.status = "CONCLUIDA";
        adicionarEvento("=== PARTIDA CONCLUÍDA ===");
    }

    @Override
    public String toString() {
        return "PARTIDA id : " + id + "\n" +
               "Modo: " + modo + "\n" +
               "Data/Hora: " + getDataHoraFormatada() + "\n" +
               "Duração: " + duracaoMinutos + "min\n" +
               "Arena: " + arena + "\n" +
               "Status: " + status + "\n" +
               "Tanques: " + tanquesParticipantes.size() + "/" + getLimiteTanques() + "\n" +
               "==============================";
    }

    public String getDetalhesCompletos() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString()).append("\n");
        sb.append("PARTICIPANTES:\n");
        for (Tanque tanque : tanquesParticipantes) {
            sb.append(" - ").append(tanque.getCodinome())
              .append(" (").append(tanque.getClasse()).append(")\n");
        }
        sb.append("\nEVENTOS:\n");
        for (String evento : eventos) {
            sb.append(evento).append("\n");
        }
        return sb.toString();
    }
}