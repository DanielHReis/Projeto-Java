import java.util.ArrayList;
import java.util.List;

public class CadastroService {
    private List<Tanque> tanques = new ArrayList<>();

    public void cadastrarTanque(Tanque tanque) {
        if (tanques.size() < 12) {
            tanques.add(tanque);
            System.out.println("\nTanque cadastrado com sucesso:\n" + tanque);
        } else {
            System.out.println("Limite de 12 tanques atingido!");
        }
    }

    public void listarTanques() {
        if (tanques.isEmpty()) {
            System.out.println("Nenhum tanque cadastrado por enquanto.");
        } else {
            System.out.println("==== Lista de Tanques ====");
            for (Tanque t : tanques) {
                System.out.println(t);
            }
        }
    }
}
