package com.battletanks;

import java.util.ArrayList;
import java.util.Scanner;

public class CadastroService {
    private static final int LIMITE_TANQUES = 12;
    private TanqueDAO dao = new TanqueDAO();
    private ArrayList<Tanque> tanques = new ArrayList<>();
    private Scanner scan = new Scanner(System.in);

    public void cadastrarTanque(Tanque tanque) {
        int totalTanques = dao.contarTanques();

        if (totalTanques >= LIMITE_TANQUES) {
            System.out.println("Limite máximo de " + LIMITE_TANQUES + " tanques atingido!");
            System.out.println("Não é possível cadastrar mais tanques no momento.");
            
        } else {
            dao.inserirTanque(tanque);
            System.out.println("Tanque salvo no banco com sucesso!");
        }
    }

    public void listarTanques() {
        tanques = dao.listar();

        if(tanques.isEmpty()) {
            System.out.println("Nenhum tanque cadastrado no banco.");
        }
        else {
            for (Tanque tanque : tanques) {
                System.out.println(tanque);
            }
            System.out.println("1. Excluir Tanque (Id)");
            System.out.println("0. Pular");

            String escolha = scan.nextLine();

            switch (escolha) {
                case "1" -> excluirTanquePorID();
                case "0" -> { 
                    System.out.println("Voltando ao menu principal...");
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }
    public ArrayList<Tanque> getTanques() {
        tanques = dao.listar(); // mantém sincronizado
        return tanques;
    }

    // NOVO MÉTODO: Buscar tanque por ID
    public Tanque buscarTanquePorId(int id) {
       Tanque tanque = dao.buscarPorId(id);
        if (tanque == null) {
            System.out.println("Nenhum tanque encontrado com o ID " + id);
        }

        return tanque;
    }

    // NOVO MÉTODO: Buscar tanque por codinome
    public Tanque buscarTanquePorCodinome(String codinome) {
        ArrayList<Tanque> tanquesAtuais = dao.listar();

        for (Tanque tanque : tanquesAtuais) {
            if (tanque.getCodinome().equalsIgnoreCase(codinome)) {
                return tanque;
            }
        }
        return null;
    }

    public void excluirTanquePorID() {
        System.out.println("Digite o ID do tanque a ser excluído: ");

        try {
            int id = Integer.parseInt(scan.nextLine());
        
            Tanque tanque = dao.buscarPorId(id);
            if (tanque == null) {
                System.out.println("Tanque com ID " + id + " não encontrado!");
                return;
            }
                System.out.println("Tanque encontrado:");
                System.out.println(tanque);

                System.out.print("Tem certeza que deseja excluir este tanque? (S/N): ");
                String confirmacao = scan.nextLine();

                if (confirmacao.equalsIgnoreCase("S")) {
                dao.excluirTanque(id);
                
                tanques = dao.listar();
            } else {
                System.out.println("Operação cancelada.");
            }
        
        } catch (NumberFormatException e) {
            System.out.println("ID inválido! Digite um número.");
        }
    }   
}