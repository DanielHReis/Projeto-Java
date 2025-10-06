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

    public Tanque() {
        this.armas = new ArrayList<>();
        this.superaquecimento = false;
        this.tempoSuperaquecimento = 0;
    }
    public Tanque(int id, String codinome, String classe, int blindagem, int velocidade,
            int poderDeFogo, String piloto, int integridade, String status) { this();
        this.id = id;
        this.codinome = codinome;
        this.classe = classe;
        this.blindagem = blindagem;
        this.velocidade = velocidade;
        this.poderDeFogo = poderDeFogo;
        this.piloto = piloto;
        this.horaEntradaArena = LocalDateTime.now();
        this.integridade = integridade;
        this.status = status;
        this.armas = new ArrayList<>();
        this.superaquecimento = false;
        this.tempoSuperaquecimento = 0;
        
        inicializarArmas();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getCodinome() {
        return codinome;
    }

    public String getClasse() {
        return classe;
    }

    public int getBlindagem() {
        return blindagem;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public int getPoderDeFogo() {
        return poderDeFogo;
    }

    public String getPiloto() {
        return piloto;
    }

    public LocalDateTime getHoraEntradaArena() {
        return horaEntradaArena;
    }

    public int getIntegridade() {
        return integridade;
    }

    public String getStatus() {
        return status;
    }

    public List<Arma> getArmas() {
        return armas;
    }

    public boolean isSuperaquecimento() {
        return superaquecimento;
    }

    public int getTempoSuperaquecimento() {
        return tempoSuperaquecimento;
    }
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setCodinome(String codinome) {
        this.codinome = codinome;
    }
    public void setClasse(String classe) {
        this.classe = classe;
    }
    public void setBlindagem(int blindagem) {
        this.blindagem = blindagem;
    }
    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }
    public void setPoderDeFogo(int poderDeFogo) {
        this.poderDeFogo = poderDeFogo;
    }
    public void setPiloto(String piloto) {
        this.piloto = piloto;
    }
    public void setHoraEntradaArena(LocalDateTime horaEntradaArena) {
        this.horaEntradaArena = horaEntradaArena;
    }
    public void setIntegridade(int integridade) {
        this.integridade = integridade;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    protected void inicializarArmas() {
        armas.add(new Canhao());
        armas.add(new Metralhadora());
        if ("Pesado".equals(classe)) {
            armas.add(new Missil());
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
            System.out.println(arma.getNome() + " causou " + dano + " de dano!");

            if (Math.random() < 0.1) {
                causarSuperaquecimento();
            }
        }
    }

    public void causarSuperaquecimento() {
        this.superaquecimento = true;
        this.tempoSuperaquecimento = 15;
        System.out.println("SUPERAQUECIMENTO! Recargas mais lentas por " + tempoSuperaquecimento + "s");
    }

    public void atualizarEstado() {
        armas.forEach(Arma::atualizarPane);

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
            System.out.printf("%d. %s - Munição: %d/%d%s\n",
                    i, arma.getNome(), arma.getMunicaoAtual(), arma.getMunicaoMaxima(), statusPane);
        }
        if (superaquecimento) {
            System.out.println("TANQUE SUPERAQUECIDO! Tempo restante: " + tempoSuperaquecimento + "s");
        }
    }

    public String getHoraEntradaFormatada() {
        return horaEntradaArena.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public boolean precisaManutencao() {
        return integridade < 30 || (int) (Math.random() * 15) > 10;
    }

    public void realizarManutencao() {
        if ("EM MANUTENCAO".equals(status)) {
            integridade = 100;
            status = "ATIVO";
            System.out.println("Manutenção concluída! " + codinome + " está como novo.");
        }
    }

    public void atualizarStatusAutomatico() {
        if (integridade <= 0) {
            status = "DESTRUIDO";
        } else if (precisaManutencao() && !"DESTRUIDO".equals(status)) {
            status = "EM MANUTENCAO";
            System.out.println(" " + codinome + " enviado para manutenção automática");
        } else if (integridade > 0 && "DESTRUIDO".equals(status)) {
            status = "ATIVO";
        }
    }

    public abstract int calcularRecarga();

    public abstract int calcularDano();

    @Override
    public String toString() {
        return String.format("""
                TANQUE
                ID = %d
                CODINOME = %s
                CLASSE = %s
                BLINDAGEM = %d
                VELOCIDADE = %d
                PODER DE FOGO = %d
                PILOTO = %s
                HORA ENTRADA ARENA = %s
                INTEGRIDADE = %d
                STATUS = %s
                ==============================
                """, id, codinome.toUpperCase(), classe.toUpperCase(), blindagem,
                velocidade, poderDeFogo, piloto.toUpperCase(), getHoraEntradaFormatada(),
                integridade, status.toUpperCase());
    }
}