package com.battletanks;

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

    // NOVO MÉTODO: Obter lista de tanques para o AgendamentoService
    public List<Tanque> getTanques() {
        return new ArrayList<>(tanques);
    }

    // NOVO MÉTODO: Buscar tanque por ID
    public Tanque buscarTanquePorId(int id) {
        for (Tanque tanque : tanques) {
            if (tanque.getId() == id) {
                return tanque;
            }
        }
        return null;
    }

    // NOVO MÉTODO: Buscar tanque por codinome
    public Tanque buscarTanquePorCodinome(String codinome) {
        for (Tanque tanque : tanques) {
            if (tanque.getCodinome().equalsIgnoreCase(codinome)) {
                return tanque;
            }
        }
        return null;
    }
}