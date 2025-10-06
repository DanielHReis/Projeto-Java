package com.battletanks;

public class EstatisticasTanque {
    private int tanqueId;
    private String codinome;
    private int abates;
    private int assistencias;
    private int danoCausado;
    private int danoRecebido;
    private int tirosDisparados;
    private int tirosAcertados;
    private int objetivosCompletos;
    private int tempoEmCombate; // em segundos
    private int partidasJogadas;
    private int vitorias;

    public EstatisticasTanque(int tanqueId, String codinome) {
        this.tanqueId = tanqueId;
        this.codinome = codinome;
        this.abates = 0;
        this.assistencias = 0;
        this.danoCausado = 0;
        this.danoRecebido = 0;
        this.tirosDisparados = 0;
        this.tirosAcertados = 0;
        this.objetivosCompletos = 0;
        this.tempoEmCombate = 0;
        this.partidasJogadas = 0;
        this.vitorias = 0;
    }

    // Getters e Setters
    public int getTanqueId() {
        return tanqueId;
    }

    public String getCodinome() {
        return codinome;
    }

    public int getAbates() {
        return abates;
    }

    public int getAssistencias() {
        return assistencias;
    }

    public int getDanoCausado() {
        return danoCausado;
    }

    public int getDanoRecebido() {
        return danoRecebido;
    }

    public int getTirosDisparados() {
        return tirosDisparados;
    }

    public int getTirosAcertados() {
        return tirosAcertados;
    }

    public int getObjetivosCompletos() {
        return objetivosCompletos;
    }

    public int getTempoEmCombate() {
        return tempoEmCombate;
    }

    public int getPartidasJogadas() {
        return partidasJogadas;
    }

    public int getVitorias() {
        return vitorias;
    }

    public void incrementarAbates() {
        this.abates++;
    }

    public void incrementarAssistencias() {
        this.assistencias++;
    }

    public void adicionarDanoCausado(int dano) {
        this.danoCausado += dano;
    }

    public void adicionarDanoRecebido(int dano) {
        this.danoRecebido += dano;
    }

    public void incrementarTirosDisparados() {
        this.tirosDisparados++;
    }

    public void incrementarTirosAcertados() {
        this.tirosAcertados++;
    }

    public void incrementarObjetivos() {
        this.objetivosCompletos++;
    }

    public void adicionarTempoCombate(int segundos) {
        this.tempoEmCombate += segundos;
    }

    public void incrementarPartidasJogadas() {
        this.partidasJogadas++;
    }

    public void incrementarVitorias() {
        this.vitorias++;
    }

    // Cálculo de precisão.
    public double getPrecisao() {
        if (tirosDisparados == 0)
            return 0.0;
        return (double) tirosAcertados / tirosDisparados * 100;
    }

    // Cálculo de score.
    public int calcularScore() {
        int score = 0;
        score += abates * 100; // 100 pontos por abate
        score += assistencias * 40; // 40 pontos por assistência
        score += objetivosCompletos * 120; // 120 pontos por objetivo
        score += danoCausado / 10; // 1 ponto a cada 10 de dano
        score += (int) (getPrecisao() * 5); // Bônus por precisão

        // Bônus de eficiência (dano recebido vs causado)
        if (danoRecebido > 0) {
            double eficiencia = (double) danoCausado / danoRecebido;
            score += (int) (eficiencia * 50);
        }

        return score;
    }

    // Taxa de vitória
    public double getTaxaVitoria() {
        if (partidasJogadas == 0)
            return 0.0;
        return (double) vitorias / partidasJogadas * 100;
    }

    @Override
    public String toString() {
        return String.format(
                "ESTATÍSTICAS - %s\n" +
                        "Score: %d | Abates: %d | Assists: %d\n" +
                        "Dano: %d/%d | Precisão: %.1f%%\n" +
                        "Partidas: %d | Vitórias: %d (%.1f%%) | Tempo Combate: %dmin",
                codinome, calcularScore(), abates, assistencias,
                danoCausado, danoRecebido, getPrecisao(),
                partidasJogadas, vitorias, getTaxaVitoria(), tempoEmCombate / 60);
    }
}