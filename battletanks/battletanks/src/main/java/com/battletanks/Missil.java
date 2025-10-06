package com.battletanks;

public class Missil extends Arma {
    public Missil() {
        super("Lançador de Mísseis", 120, 12, 500, "explosiva", 10);
    }

    @Override
    public int calcularDano(String setorAlvo, String terreno, int distancia) {
        // Mísseis ignoram parcialmente a blindagem do setor
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
        // Mísseis são menos afetados pelo setor (dano explosivo)
        return switch (setor.toLowerCase()) {
            case "frontal" -> 0.9;
            case "lateral" -> 1.1;
            case "traseiro" -> 1.2;
            default -> 1.0;
        };
    }

    private double getModificadorDistancia(int distancia) {
        if (distancia <= 200)
            return 0.8; // Muito perto - menos efetivo
        if (distancia <= 400)
            return 1.2; // Distância ideal - mais efetivo
        if (distancia <= 500)
            return 1.0; // Alcance máximo
        return 0.5; // Fora do alcance
    }

    private double getModificadorTerreno(String terreno) {
        return switch (terreno.toLowerCase()) {
            case "campo aberto" -> 1.3; // Muito efetivo em campo aberto
            case "deserto" -> 1.0; // Neutro no deserto
            case "urbano" -> 0.7; // Penalidade em urbano (edifícios bloqueiam)
            default -> 1.0;
        };
    }

    private double getModificadorClasse(String classe) {
        return switch (classe.toLowerCase()) {
            case "leve" -> 1.0; // Recarga normal
            case "médio" -> 1.1; // Recarga um pouco lenta
            case "pesado" -> 0.9; // Tanques pesados são otimizados para mísseis
            default -> 1.0;
        };
    }
}