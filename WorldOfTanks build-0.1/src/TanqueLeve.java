public class TanqueLeve extends Tanque {

    public TanqueLeve(int id, String codinome, String piloto, String horaEntradaArena) {
        super(id, codinome, "Leve", 50, 80, 40, piloto, horaEntradaArena);
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
