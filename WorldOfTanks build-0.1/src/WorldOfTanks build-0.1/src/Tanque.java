import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Tanque {
    protected int id;
    protected String codinome;
    protected String classe;
    protected int blindagem;
    protected int velocidade;
    protected int poderDeFogo;
    protected String piloto;
    protected  LocalDateTime horaEntradaArena;
    protected int integridade;
    protected String status;

    public Tanque(int id, String codinome, String classe, int blindagem, int velocidade, int poderDeFogo, String piloto,
             LocalDateTime horaEntradaArena, int integridade, String status) {
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

     public String getHoraEntradaFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return horaEntradaArena.format(formatter);
    }

    public abstract int calcularRecarga();

    public abstract int calcularDano();

    @Override
    public String toString() {
        return "Tanque [id=" + id + ", codinome=" + codinome + ", classe=" + classe + ", blindagem=" + blindagem
                + ", velocidade=" + velocidade + ", poderDeFogo=" + poderDeFogo + ", piloto=" + piloto
                + ", horaEntradaArena=" + getHoraEntradaFormatada() + ", integridade=" + integridade + ", status=" + status
                + "]";
    }

}
