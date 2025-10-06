package com.battletanks;
import java.time.LocalDateTime;

public class TanquePadrao extends Tanque {
    
    public TanquePadrao() {
    }

    public TanquePadrao(int id, String codinome, String classe, int blindagem, int velocidade, int poderDeFogo,
            String piloto, int integridade, String status, LocalDateTime horaEntradaArena ) {
        super(id, codinome, classe, blindagem, velocidade, poderDeFogo, piloto, integridade, status);

        this.id = id;
        this.codinome = codinome;
        this.classe = classe;
        this.blindagem = blindagem;
        this.velocidade = velocidade;
        this.poderDeFogo = poderDeFogo;
        this.piloto = piloto;
        this.horaEntradaArena = horaEntradaArena;
        this.integridade = integridade;
        this.status = status;
    }

    @Override
    public int calcularRecarga() {
        // ImplementaÃ§Ã£o padrÃ£o baseada na classe do tanque
        return switch (this.classe.toLowerCase()) {
            case "leve" -> 3;
            case "mÃ©dio" -> 5;
            case "pesado" -> 7;
            default -> 4;
        };
    }

    @Override
    public int calcularDano() {
        // Dano base + bÃ´nus baseado na classe
        int bonus = switch (this.classe.toLowerCase()) {
            case "leve" -> 10;
            case "mÃ©dio" -> 20;
            case "pesado" -> 30;
            default -> 15;
        };
        return this.poderDeFogo + bonus;
    }

}