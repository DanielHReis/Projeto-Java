public class TanqueMedio extends Tanque {

    public TanqueMedio(int id, String codinome, String piloto, String horaEntradaArena) {
        super(id, codinome, "Médio", 70, 60, 60, piloto, horaEntradaArena);
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
