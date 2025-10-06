package com.battletanks;

public class Canhao extends Arma {
    public Canhao() {
        super("Canhão Principal", 80, 8, 300, "perfurante", 30);
    }

    @Override
    public int calcularDano(String setorAlvo, String terreno, int distancia) {
        double modificadorSetor = getModificadorSetor(setorAlvo);
        double modificadorDistancia = getModificadorDistancia(distancia);
        double modificadorTerreno = getModificadorTerreno(terreno);

        return (int) (danoBase * modificadorSetor * modificadorDistancia * modificadorTerreno);
    }

    @Override
    public int calcularRecarga(String classeTanque, boolean superaquecimento) {
        double modificadorClasse = getModificadorClasse(classeTanque);
        double modificadorSuperaquecimento = superaquecimento ? 1.5 : 1.0;

        return (int) (tempoRecargaBase * modificadorClasse * modificadorSuperaquecimento);
    }

    private double getModificadorSetor(String setor) {
        return switch (setor.toLowerCase()) {
            case "frontal" -> 1.0;
            case "lateral" -> 1.2;
            case "traseiro" -> 1.4;
            default -> 1.0;
        };
    }

    private double getModificadorDistancia(int distancia) {
        if (distancia <= 100)
            return 1.2; // Curta distância - mais dano
        if (distancia <= 200)
            return 1.0; // Distância ideal
        if (distancia <= 300)
            return 0.8; // Longa distância - menos dano
        return 0.5; // Distância muito longa
    }

    private double getModificadorTerreno(String terreno) {
        return switch (terreno.toLowerCase()) {
            case "campo aberto" -> 1.1; // Bônus em campo aberto
            case "deserto" -> 0.9; // Penalidade na areia
            case "urbano" -> 1.0; // Neutro em urbano
            default -> 1.0;
        };
    }

    private double getModificadorClasse(String classe) {
        return switch (classe.toLowerCase()) {
            case "leve" -> 0.8; // Recarga mais rápida
            case "médio" -> 1.0; // Recarga normal
            case "pesado" -> 1.3; // Recarga mais lenta
            default -> 1.0;
        };
    }
}