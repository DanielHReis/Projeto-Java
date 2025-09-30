public class TanquePesado extends Tanque {

    public TanquePesado(int id, String codinome, String piloto, String horaEntradaArena) {
        super(id, codinome, "Pesado", 100, 40, 90, piloto, horaEntradaArena);
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
