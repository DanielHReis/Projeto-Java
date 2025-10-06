package com.battletanks;

import java.time.LocalDateTime;

public class TanqueLeve extends Tanque {

    public TanqueLeve(int id, String codinome, String piloto, LocalDateTime horaEntradaArena, int integridade,
            String status) {
        super(id, codinome, "Leve", 50, 80, 40, piloto,
                (integridade == 0 ? 100 : integridade),
                (status == null ? "Ativo" : status));

        // Inicializar a hora de entrada na arena
        this.horaEntradaArena = horaEntradaArena != null ? horaEntradaArena : LocalDateTime.now();
    }

    @Override
    public int calcularRecarga() {
        return 2;
    }

    @Override
    public int calcularDano() {
        return poderDeFogo;
    }
}