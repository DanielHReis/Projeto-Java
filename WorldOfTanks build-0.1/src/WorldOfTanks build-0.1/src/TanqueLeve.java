import java.time.LocalDateTime;

public class TanqueLeve extends Tanque {

    public TanqueLeve(int id, String codinome, String piloto, LocalDateTime horaEntradaArena, int integridade, String status) {
        super(id, codinome, "Leve", 50, 80, 40, piloto, horaEntradaArena, integridade = 100, status = "Ativo");
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
