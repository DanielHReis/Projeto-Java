package com.battletanks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class Tanque {
    protected int id;
    protected String codinome;
    protected String classe;
    protected int blindagem;
    protected int velocidade;
    protected int poderDeFogo;
    protected String piloto;
    protected LocalDateTime horaEntradaArena;
    protected int integridade;
    protected String status;
    protected List<Arma> armas;
    protected boolean superaquecimento;
    protected int tempoSuperaquecimento;

    public Tanque(int id, String codinome, String classe, int blindagem, int velocidade, int poderDeFogo, String piloto,
            int integridade, String status) {
        this.id = id;
        this.codinome = codinome;
        this.classe = classe;
        this.blindagem = blindagem;
        this.velocidade = velocidade;
        this.poderDeFogo = poderDeFogo;
        this.piloto = piloto;
        this.integridade = integridade;
        this.status = status;
        this.armas = new ArrayList<>();
        this.superaquecimento = false;
        this.tempoSuperaquecimento = 0;
        inicializarArmas();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodinome() {
        return codinome;
    }

    public void setCodinome(String codinome) {
        this.codinome = codinome;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public int getBlindagem() {
        return blindagem;
    }

    public void setBlindagem(int blindagem) {
        this.blindagem = blindagem;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    public int getPoderDeFogo() {
        return poderDeFogo;
    }

    public void setPoderDeFogo(int poderDeFogo) {
        this.poderDeFogo = poderDeFogo;
    }

    public String getPiloto() {
        return piloto;
    }

    public void setPiloto(String piloto) {
        this.piloto = piloto;
    }

    public LocalDateTime getHoraEntradaArena() {
        return horaEntradaArena;
    }

    public void setHoraEntradaArena(LocalDateTime horaEntradaArena) {
        this.horaEntradaArena = horaEntradaArena;
    }

    public int getIntegridade() {
        return integridade;
    }

    public void setIntegridade(int integridade) {
        this.integridade = integridade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setArmas(List<Arma> armas) {
        this.armas = armas;
    }

    public void setSuperaquecimento(boolean superaquecimento) {
        this.superaquecimento = superaquecimento;
    }

    public int getTempoSuperaquecimento() {
        return tempoSuperaquecimento;
    }

    public void setTempoSuperaquecimento(int tempoSuperaquecimento) {
        this.tempoSuperaquecimento = tempoSuperaquecimento;
    }

    protected void inicializarArmas() {
        // Cada classe de tanque tem armas diferentes
        if (this.classe.equals("Pesado")) {
            armas.add(new Canhao());
            armas.add(new Metralhadora());
            armas.add(new Missil());
        } else if (this.classe.equals("Médio")) {
            armas.add(new Canhao());
            armas.add(new Metralhadora());
        } else if (this.classe.equals("Leve")) {
            armas.add(new Canhao());
            armas.add(new Metralhadora());
        }
    }

    public void atirar(int indexArma, String setorAlvo, String terreno, int distancia) {
        if (indexArma < 0 || indexArma >= armas.size()) {
            System.out.println("Arma inválida!");
            return;

        }
        Arma arma = armas.get(indexArma);

        if (arma.atirar()) {
            int dano = arma.calcularDano(setorAlvo, terreno, distancia);
            int recarga = arma.calcularRecarga(this.classe, this.superaquecimento);

            System.out.println(arma.getNome() + " causou " + dano + " de dano!");
            System.out.println("Tempo de recarga: " + recarga + " segundos");

            // Chance de superaquecimento (10% por tiro)
            if (Math.random() < 0.1) {
                causarSuperaquecimento();
            }
        }
    }

    public void causarSuperaquecimento() {
        this.superaquecimento = true;
        this.tempoSuperaquecimento = 15; // 15 segundos de superaquecimento
        System.out.println("SUPERAQUECIMENTO! Recargas mais lentas por " + tempoSuperaquecimento + "s");
    }

    public void atualizarEstado() {
        // Atualizar panes das armas
        for (Arma arma : armas) {
            arma.atualizarPane();
        }

        // Atualizar superaquecimento
        if (superaquecimento && tempoSuperaquecimento > 0) {
            tempoSuperaquecimento--;
            if (tempoSuperaquecimento <= 0) {
                superaquecimento = false;
                System.out.println("Superaquecimento normalizado.");
            }
        }
    }

    public void listarArmas() {
        System.out.println("\n===== ARMAS DO " + codinome + " =====");
        for (int i = 0; i < armas.size(); i++) {
            Arma arma = armas.get(i);
            String statusPane = arma.isPane() ? " [PANE - " + arma.getTempoPane() + "s]" : "";
            System.out.printf("%d. %s - Dano: %d | Recarga: %ds | Munição: %d/%d | Tipo: %s%s\n",
                    i, arma.getNome(), arma.calcularDano("frontal", "campo aberto", 100),
                    arma.calcularRecarga(this.classe, false), arma.getMunicaoAtual(),
                    arma.getMunicaoMaxima(), arma.getTipoMunição(), statusPane);
        }
        if (superaquecimento) {
            System.out.println("TANQUE SUPERAQUECIDO! Tempo restante: " + tempoSuperaquecimento + "s");
        }
    }

    // Getters para armas
    public List<Arma> getArmas() {
        return armas;
    }

    public boolean isSuperaquecimento() {
        return superaquecimento;
    }

    public String getHoraEntradaFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return horaEntradaArena.format(formatter);
    }

    public abstract int calcularRecarga();

    public abstract int calcularDano();

    @Override
    public String toString() {
        return "TANQUE" + "\n" +
                "ID = " + String.valueOf(id).toUpperCase() + "\n" +
                "CODINOME = " + codinome.toUpperCase() + "\n" +
                "CLASSE = " + classe.toUpperCase() + "\n" +
                "BLINDAGEM = " + String.valueOf(blindagem).toUpperCase() + "\n" +
                "VELOCIDADE = " + String.valueOf(velocidade).toUpperCase() + "\n" +
                "PODER DE FOGO = " + String.valueOf(poderDeFogo).toUpperCase() + "\n" +
                "PILOTO = " + piloto.toUpperCase() + "\n" +
                "HORA ENTRADA ARENA = " + getHoraEntradaFormatada().toUpperCase() + "\n" +
                "INTEGRIDADE = " + String.valueOf(integridade).toUpperCase() + "\n" +
                "STATUS = " + status.toUpperCase() + "\n" +
                "==============================";
    }

    // Método para verificar necessidade de manutenção
    public boolean precisaManutencao() {
        // Tanques com integridade baixa ou muitas batalhas precisam de manutenção
        if (integridade < 30) {
            return true;
        }

        // Simulação - tanques com mais de 10 partidas precisam de manutenção
        int partidasSimuladas = (int) (Math.random() * 15);
        return partidasSimuladas > 10;
    }

    // Método para realizar manutenção
    public void realizarManutencao() {
        if (status.equals("EM MANUTENCAO")) {
            integridade = 100; // Restaura integridade
            status = "ATIVO";
            System.out.println("Manutenção concluída! " + codinome + " está como novo.");
        }
    }

    // Método automático para atualizar status baseado em condições
    public void atualizarStatusAutomatico() {
        if (integridade <= 0) {
            status = "DESTRUIDO";
        } else if (precisaManutencao() && !status.equals("DESTRUIDO")) {
            status = "EM MANUTENCAO";
            System.out.println("⚠️  " + codinome + " enviado para manutenção automática");
        } else if (integridade > 0 && status.equals("DESTRUIDO")) {
            status = "ATIVO"; // Ressuscita se integridade foi restaurada
        }
    }
}
