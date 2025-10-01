public abstract class Arma {
    protected String nome;
    protected int danoBase;
    protected int tempoRecargaBase; // em segundos
    protected int alcance;
    protected String tipoMunição; // "perfurante", "explosiva", "fragmentação"
    protected int municaoAtual;
    protected int municaoMaxima;
    protected boolean pane; // se true, arma não funciona
    protected int tempoPane; // tempo restante de pane em segundos

    public Arma(String nome, int danoBase, int tempoRecargaBase, int alcance,
            String tipoMunição, int municaoMaxima) {
        this.nome = nome;
        this.danoBase = danoBase;
        this.tempoRecargaBase = tempoRecargaBase;
        this.alcance = alcance;
        this.tipoMunição = tipoMunição;
        this.municaoMaxima = municaoMaxima;
        this.municaoAtual = municaoMaxima;
        this.pane = false;
        this.tempoPane = 0;
    }

    // Método abstrato para cálculo de dano (polimorfismo)
    public abstract int calcularDano(String setorAlvo, String terreno, int distancia);

    // Método para calcular recarga considerando classe do tanque e efeitos
    public abstract int calcularRecarga(String classeTanque, boolean superaquecimento);

    // Método para atirar
    public boolean atirar() {
        if (pane) {
            System.out.println("ARMA EM PANE! Não pode atirar. Tempo restante: " + tempoPane + "s");
            return false;
        }

        if (municaoAtual <= 0) {
            System.out.println("SEM MUNIÇÃO! Recarregue a arma.");
            return false;
        }

        municaoAtual--;
        System.out.println(nome + " disparou! Munição restante: " + municaoAtual + "/" + municaoMaxima);

        // Chance de pane (5%)
        if (Math.random() < 0.05) {
            causarPane();
            return false;
        }

        return true;
    }

    public void recarregar() {
        municaoAtual = municaoMaxima;
        System.out.println(nome + " recarregada! Munição: " + municaoAtual + "/" + municaoMaxima);
    }

    public void causarPane() {
        this.pane = true;
        this.tempoPane = 10 + (int) (Math.random() * 20); // Pane de 10-30 segundos
        System.out.println("⚠️  " + nome + " ENTROU EM PANE! Tempo para recuperação: " + tempoPane + "s");
    }

    public void atualizarPane() {
        if (pane && tempoPane > 0) {
            tempoPane--;
            if (tempoPane <= 0) {
                pane = false;
                System.out.println("✅ " + nome + " RECUPERADA DA PANE!");
            }
        }
    }

    public String getNome() {
        return nome;
    }

    public int getTempoRecargaBase() {
        return tempoRecargaBase;
    }

    public boolean isPane() {
        return pane;
    }

    public int getTempoPane() {
        return tempoPane;
    }

    public int getMunicaoAtual() {
        return municaoAtual;
    }

    public int getMunicaoMaxima() {
        return municaoMaxima;
    }

    public String getTipoMunição() {
        return tipoMunição;
    }
}