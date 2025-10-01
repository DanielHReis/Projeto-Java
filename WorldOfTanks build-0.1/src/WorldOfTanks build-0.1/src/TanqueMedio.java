import java.time.LocalDateTime;

public class TanqueMedio extends Tanque {

    public TanqueMedio(int id, String codinome, String piloto, LocalDateTime horaEntradaArena, int integridade,
            String status) {
        super(id, codinome, "Médio", 70, 60, 60, piloto, horaEntradaArena, integridade = 100, status = "Ativo");
    }

    @Override
    public int calcularRecarga() {
        return 4;
    }

    @Override
    public int calcularDano() {
        return poderDeFogo;
    }
}
