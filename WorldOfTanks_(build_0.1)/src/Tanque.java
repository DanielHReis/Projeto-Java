public abstract class Tanque {
    protected int id;
    protected String codinome;
    protected String classe;
    protected int blindagem;
    protected int velocidade;
    protected int poderDeFogo;
    protected String piloto;
    protected String horaEntradaArena;

    public Tanque(int id, String codinome, String classe, int blindagem, int velocidade, int poderDeFogo, String piloto, String horaEntradaArena) {
        this.id = id;
        this.codinome = codinome;
        this.classe = classe;
        this.blindagem = blindagem;
        this.velocidade = velocidade;
        this.poderDeFogo = poderDeFogo;
        this.piloto = piloto;
        this.horaEntradaArena = horaEntradaArena;
    }

    public abstract int calcularRecarga();
    public abstract int calcularDano();

    @Override
    public String toString() {
        return "Tanque{" +
                "id=" + id +
                ", codinome='" + codinome + '\'' +
                ", classe='" + classe + '\'' +
                ", blindagem=" + blindagem +
                ", velocidade=" + velocidade +
                ", poderDeFogo=" + poderDeFogo +
                ", piloto='" + piloto + '\'' +
                ", horaEntradaArena='" + horaEntradaArena + '\'' +
                '}';
    }
}
