package com.battletanks;

public class Missil extends Arma {
    public Missil() {
        super("Lançador de Mísseis", 120, 12, 500, "explosiva", 10);
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
        double modificadorSuperaquecimento = superaquecimento ? 1.8 : 1.0;

        return (int) (tempoRecargaBase * modificadorClasse * modificadorSuperaquecimento);
    }

    private double getModificadorSetor(String setor) {
        
        return switch (setor.toLowerCase()) {
            case "frontal" -> 0.9;
            case "lateral" -> 1.1;
            case "traseiro" -> 1.2;
            default -> 1.0;
        };
    }

    private double getModificadorDistancia(int distancia) {
        if (distancia <= 200)
            return 0.8;
        if (distancia <= 400)
            return 1.2;
        if (distancia <= 500)
            return 1.0;
        return 0.5;
    }

    private double getModificadorTerreno(String terreno) {
        return switch (terreno.toLowerCase()) {
            case "campo aberto" -> 1.3;
            case "deserto" -> 1.0;
            case "urbano" -> 0.7;
            default -> 1.0;
        };
    }

    private double getModificadorClasse(String classe) {
        return switch (classe.toLowerCase()) {
            case "leve" -> 1.0;
            case "médio" -> 1.1;
            case "pesado" -> 0.9;
            default -> 1.0;
        };
    }
}