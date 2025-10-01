public class Metralhadora extends Arma {
    public Metralhadora() {
        super("Metralhadora", 15, 2, 150, "fragmentação", 200);
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
        double modificadorSuperaquecimento = superaquecimento ? 1.3 : 1.0;

        return (int) (tempoRecargaBase * modificadorClasse * modificadorSuperaquecimento);
    }

    private double getModificadorSetor(String setor) {
        return switch (setor.toLowerCase()) {
            case "frontal" -> 0.8; // Menos efetivo contra blindagem frontal
            case "lateral" -> 1.1;
            case "traseiro" -> 1.3; // Muito efetivo na retaguarda
            default -> 1.0;
        };
    }

    private double getModificadorDistancia(int distancia) {
        if (distancia <= 50)
            return 1.3; // Muito efetivo perto
        if (distancia <= 100)
            return 1.1; // Efetivo
        if (distancia <= 150)
            return 0.9; // Pouco efetivo longe
        return 0.6; // Muito inefetivo
    }

    private double getModificadorTerreno(String terreno) {
        return switch (terreno.toLowerCase()) {
            case "urbano" -> 1.2; // Bônus em ambiente urbano
            case "campo aberto" -> 0.9;
            case "deserto" -> 0.8; // Penalidade na areia
            default -> 1.0;
        };
    }

    private double getModificadorClasse(String classe) {
        return switch (classe.toLowerCase()) {
            case "leve" -> 0.7; // Recarga muito rápida
            case "médio" -> 0.9; // Recarga rápida
            case "pesado" -> 1.1; // Recarga normal
            default -> 1.0;
        };
    }
}