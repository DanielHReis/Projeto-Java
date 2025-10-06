package com.battletanks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AgendamentoService {
    private static List<Partida> partidas = new ArrayList<>();
    private static int proximoId = 1;

    public static void agendarPartida(Scanner scanner, CadastroService cadastroService) {
        System.out.println("\n===== AGENDAR NOVA PARTIDA =====");

        // Verificar se há tanques cadastrados
        if (cadastroService.getTanques().isEmpty()) {
            System.out.println("Nenhum tanque cadastrado! Cadastre tanques primeiro.");
            return;
        }

        // Modo da partida
        System.out.println("Modos disponíveis:");
        System.out.println("1. TREINO (até 4 tanques)");
        System.out.println("2. UM_VS_UM (2 tanques)");
        System.out.println("3. EQUIPES_3V3 (6 tanques)");
        System.out.println("4. EQUIPES_5V5 (10 tanques)");
        System.out.print("Escolha o modo: ");
        int opcaoModo = scanner.nextInt();
        scanner.nextLine();

        String modo = switch (opcaoModo) {
            case 1 -> "TREINO";
            case 2 -> "UM_VS_UM";
            case 3 -> "EQUIPES_3V3";
            case 4 -> "EQUIPES_5V5";
            default -> "TREINO";
        };

        // Data e hora
        System.out.print("Data (DD/MM/AAAA): ");
        String dataStr = scanner.nextLine();
        System.out.print("Hora (HH:MM): ");
        String horaStr = scanner.nextLine();

        LocalDateTime dataHora = parseDataHora(dataStr, horaStr);
        if (dataHora == null) {
            System.out.println("Data/hora inválida! Use o formato DD/MM/AAAA HH:MM");
            return;
        }

        // Verificar se a data não é no passado
        if (dataHora.isBefore(LocalDateTime.now())) {
            System.out.println("Não é possível agendar partidas no passado!");
            return;
        }

        // Duração
        System.out.print("Duração (minutos): ");
        int duracao = scanner.nextInt();
        scanner.nextLine();

        if (duracao <= 0) {
            System.out.println("Duração deve ser maior que zero!");
            return;
        }

        // Arena
        System.out.println("Arenas disponíveis:");
        System.out.println("1. DESERTO");
        System.out.println("2. URBANO");
        System.out.println("3. CAMPO_ABERTO");
        System.out.print("Escolha a arena: ");
        String opcaoArena = scanner.nextLine();

        String arena = switch (opcaoArena) {
            case "1" -> "DESERTO";
            case "2" -> "URBANO";
            case "3" -> "CAMPO_ABERTO";
            default -> "CAMPO_ABERTO";
        };

        // Verificar conflito de arena
        if (verificarConflitoArena(arena, dataHora, duracao)) {
            System.out.println("Conflito: Arena já ocupada neste horário!");
            return;
        }

        // Criar partida
        Partida partida = new Partida(proximoId++, modo, dataHora, duracao, arena);

        // Adicionar tanques - MUDANÇA PRINCIPAL AQUI
        adicionarTanquesPartida(scanner, partida, cadastroService, dataHora, duracao);

        int minimoTanques = getMinimoTanques(modo);
        int tanquesAdicionados = partida.getTanquesParticipantes().size();

        if (tanquesAdicionados >= minimoTanques) {
            partidas.add(partida);
            System.out.println("\nPartida agendada com sucesso!");
            System.out.println(partida);
        } else {
            System.out.println("Partida cancelada - número insuficiente de tanques");
            System.out.println("Tanques adicionados: " + tanquesAdicionados + " | Mínimo necessário: " + minimoTanques);
        }
    }

    // MÉTODO ATUALIZADO: Agora recebe dataHora e duração para verificação correta
    private static void adicionarTanquesPartida(Scanner scanner, Partida partida, CadastroService cadastroService,
            LocalDateTime dataHora, int duracao) {
        System.out.println("\n===== ADICIONAR TANQUES =====");

        // Listar tanques disponíveis - USANDO MÉTODO SIMPLIFICADO
        List<Tanque> tanquesDisponiveis = getTodosTanquesDisponiveis(cadastroService);

        if (tanquesDisponiveis.isEmpty()) {
            System.out.println("Nenhum tanque cadastrado!");
            return;
        }

        System.out.println("Tanques disponíveis (" + tanquesDisponiveis.size() + "):");
        for (int i = 0; i < tanquesDisponiveis.size(); i++) {
            Tanque t = tanquesDisponiveis.get(i);
            System.out.printf("%d. %s (%s) - %s - Integridade: %d%%\n",
                    i, t.getCodinome(), t.getClasse(), t.getPiloto(), t.getIntegridade());
        }

        int limite = partida.getLimiteTanques();
        System.out.println("\nAdicione tanques (máximo: " + limite + ", -1 para terminar):");

        while (partida.getTanquesParticipantes().size() < limite) {
            System.out.print("Escolha o tanque (" + partida.getTanquesParticipantes().size() + "/" + limite + "): ");
            int escolha = scanner.nextInt();
            scanner.nextLine();

            if (escolha == -1) {
                break;
            }

            if (escolha >= 0 && escolha < tanquesDisponiveis.size()) {
                Tanque tanqueEscolhido = tanquesDisponiveis.get(escolha);

                // Verificar se o tanque já foi adicionado
                if (partida.getTanquesParticipantes().contains(tanqueEscolhido)) {
                    System.out.println("Este tanque já foi adicionado!");
                    continue;
                }

                // Verificar conflito específico para este tanque
                if (verificarConflitoTanque(tanqueEscolhido, dataHora, duracao)) {
                    System.out.println(tanqueEscolhido.getCodinome() + " já está em outra partida neste horário!");
                    continue;
                }

                if (partida.adicionarTanque(tanqueEscolhido)) {
                    System.out.println(tanqueEscolhido.getCodinome() + " adicionado!");
                }
            } else {
                System.out.println("Opção inválida! Escolha entre 0 e " + (tanquesDisponiveis.size() - 1));
            }
        }
    }

    // MÉTODO SIMPLIFICADO: Retorna todos os tanques ativos sem verificar conflitos
    // complexos
    private static List<Tanque> getTodosTanquesDisponiveis(CadastroService cadastroService) {
        List<Tanque> tanquesDisponiveis = new ArrayList<>();
        List<Tanque> todosTanques = cadastroService.getTanques();

        for (Tanque tanque : todosTanques) {
            // Apenas verifica se está ativo e com integridade suficiente
            if ("ATIVO".equalsIgnoreCase(tanque.getStatus()) && tanque.getIntegridade() > 0) {
                tanquesDisponiveis.add(tanque);
            }
        }
        return tanquesDisponiveis;
    }

    // MÉTODO ATUALIZADO: Verificação de conflito mais precisa
    private static boolean verificarConflitoTanque(Tanque tanque, LocalDateTime dataHora, int duracao) {
        for (Partida p : partidas) {
            if (p.getTanquesParticipantes().contains(tanque) &&
                    !"CONCLUIDA".equals(p.getStatus()) &&
                    !"CANCELADA".equals(p.getStatus())) {

                LocalDateTime fimExistente = p.getDataHoraInicio().plusMinutes(p.getDuracaoMinutos());
                LocalDateTime fimNova = dataHora.plusMinutes(duracao);

                // Verifica se há sobreposição de horários
                boolean conflito = (dataHora.isBefore(fimExistente) && fimNova.isAfter(p.getDataHoraInicio()));
                if (conflito) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean verificarConflitoArena(String arena, LocalDateTime dataHora, int duracao) {
        for (Partida p : partidas) {
            if (p.getArena().equals(arena) &&
                    !"CONCLUIDA".equals(p.getStatus()) &&
                    !"CANCELADA".equals(p.getStatus())) {

                LocalDateTime fimExistente = p.getDataHoraInicio().plusMinutes(p.getDuracaoMinutos());
                LocalDateTime fimNova = dataHora.plusMinutes(duracao);

                if (dataHora.isBefore(fimExistente) && fimNova.isAfter(p.getDataHoraInicio())) {
                    return true; // Conflito detectado
                }
            }
        }
        return false;
    }

    private static int getMinimoTanques(String modo) {
        return switch (modo) {
            case "UM_VS_UM" -> 2;
            case "EQUIPES_3V3" -> 6;
            case "EQUIPES_5V5" -> 10;
            default -> 1; // TREINO pode ter 1 tanque
        };
    }

    private static LocalDateTime parseDataHora(String dataStr, String horaStr) {
        try {
            String[] dataParts = dataStr.split("/");
            String[] horaParts = horaStr.split(":");

            int dia = Integer.parseInt(dataParts[0]);
            int mes = Integer.parseInt(dataParts[1]);
            int ano = Integer.parseInt(dataParts[2]);
            int hora = Integer.parseInt(horaParts[0]);
            int minuto = Integer.parseInt(horaParts[1]);

            return LocalDateTime.of(ano, mes, dia, hora, minuto);
        } catch (Exception e) {
            System.out.println("Erro ao converter data/hora: " + e.getMessage());
            return null;
        }
    }

    public static void listarPartidas() {
        System.out.println("\n===== PARTIDAS AGENDADAS =====");
        if (partidas.isEmpty()) {
            System.out.println("Nenhuma partida agendada.");
        } else {
            for (Partida partida : partidas) {
                System.out.println(partida);
            }
        }
    }

    public static void gerenciarPartida(Scanner scanner, CadastroService cadastroService) {
        System.out.println("\n===== GERENCIAR PARTIDA =====");
        listarPartidas();

        if (partidas.isEmpty()) {
            System.out.println("Nenhuma partida para gerenciar.");
            return;
        }

        System.out.print("ID da partida para gerenciar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Partida partida = buscarPartidaPorId(id);
        if (partida == null) {
            System.out.println("Partida não encontrada!");
            return;
        }

        System.out.println("\n" + partida.getDetalhesCompletos());
        System.out.println("Ações:");
        System.out.println("1. Iniciar partida");
        System.out.println("2. Finalizar partida");
        System.out.println("3. Cancelar partida");
        System.out.println("4. Adicionar evento");
        System.out.println("5. Ver detalhes completos");
        System.out.print("Escolha: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
            if("AGENDADA".equals(partida.getStatus())) {
                if (partida.getTanquesParticipantes().size() >= getMinimoTanques(partida.getModo())) {
                    partida.iniciarPartida();
                    System.out.println("Partida iniciada!");

                    System.out.println("Modo: " + partida.getModo());
                    System.out.println("Arena: " + partida.getArena());
                
                    if(partida.getModo().equals("UM_VS_UM") && partida.getTanquesParticipantes().size() == 2){

                        System.out.println("\n INICIANDO BATALHA 1V1 INTERATIVA!");
                        Tanque tanque1 = partida.getTanquesParticipantes().get(0);
                        Tanque tanque2 = partida.getTanquesParticipantes().get(1);

                        System.out.println(" " + tanque1.getCodinome() + " vs " + tanque2.getCodinome());
                        System.out.println("Pressione Enter para começar...");
                        scanner.nextLine();
                        
                        BattleSimulator.simularBatalhaEntreTanques(scanner, tanque1, tanque2);

                    } else if (partida.getModo().equals("Treino")){
                        System.out.println("Partida Iniciada treino interativo...");
                        BattleSimulator.simularBatalha(scanner, cadastroService);

                    } else {
                        System.out.println("Modo " + partida.getModo() + " - Executando simulação automática...");
                            partida.adicionarEvento("Partida executada em modo automático");
                    }

                    partida.finalizarPartida();
                    
                    RankingService.registrarEventoGlobal("Partida #" + partida.getId() + " iniciada - " + partida.getModo() + " na arena " + partida.getArena());
                } else {
                        System.out.println("Não é possível iniciar - número insuficiente de tanques!");
                        System.out.println("Tanques presentes: " + partida.getTanquesParticipantes().size());
                        System.out.println("Mínimo necessário: " + getMinimoTanques(partida.getModo()));
                }
            } else {
                    System.out.println("Partida não pode ser iniciada. Status atual: " + partida.getStatus());
                }
                break;
            case 2:
                partida.finalizarPartida();
                System.out.println("Partida finalizada!");
                break;
            case 3:
                partida.setStatus("CANCELADA");
                System.out.println("Partida cancelada!");
                break;
            case 4:
                System.out.print("Descrição do evento: ");
                String evento = scanner.nextLine();
                partida.adicionarEvento(evento);
                System.out.println("Evento adicionado!");
                break;
            case 5:
                System.out.println(partida.getDetalhesCompletos());
                break;
            default:
                System.out.println("Opção inválida!");
        }

    }

    public static Partida buscarPartidaPorId(int id) {
        for (Partida partida : partidas) {
            if (partida.getId() == id) {
                return partida;
            }
        }
        return null;
    }

    public static List<Partida> getPartidasAgendadas() {
        return new ArrayList<>(partidas);
    }

    // NOVO MÉTODO: Para debug - mostrar todos os tanques disponíveis
    public static void debugTanquesDisponiveis(CadastroService cadastroService) {
        System.out.println("\n===== DEBUG - TANQUES DISPONÍVEIS =====");
        List<Tanque> todosTanques = cadastroService.getTanques();
        System.out.println("Total de tanques cadastrados: " + todosTanques.size());

        for (Tanque tanque : todosTanques) {
            System.out.printf("- %s (%s) | Status: %s | Integridade: %d%%\n",
                    tanque.getCodinome(), tanque.getClasse(), tanque.getStatus(), tanque.getIntegridade());
        }
    }
}