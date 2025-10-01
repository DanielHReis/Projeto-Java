import java.time.LocalDateTime;

public class TanquePesado extends Tanque {

    public TanquePesado(int id, String codinome, String piloto, LocalDateTime horaEntradaArena, int integridade,
            String status) {
        super(id, codinome, "Pesado", 100, 40, 90, piloto, horaEntradaArena, integridade = 100, status = "Ativo");
    }

    @Override
    public int calcularRecarga() {
        return 6;
    }

    @Override
    public int calcularDano() {
        return poderDeFogo;
    }
}
