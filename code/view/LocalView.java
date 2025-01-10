package view;

import model.LocalModel;
import validations.TimeHourValidation;

import java.sql.SQLException;
import java.time.LocalTime;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class LocalView {
    TimeHourValidation validation = new TimeHourValidation();

    public void cadastrar(Scanner scanner, LocalModel local) {
        System.out.print("Nome: ");
        local.setNome(scanner.nextLine());
        System.out.print("Cep: ");
        local.setCep(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Tipo: ");
        local.setTipo(scanner.nextLine());
        System.out.print("Numero: ");
        local.setNumero(scanner.nextInt()); 
        scanner.nextLine();
        System.out.println("Tempo máximo de uma reserva (em horas): ");
        local.setTempoMaximo(obterHorario(scanner));
        System.out.println("Horario de abertura: ");
        local.setHorarioAbertura(obterHorario(scanner));
        System.out.println("Horario de fechamento: ");
        local.setHorarioFechamento(obterHorario(scanner));
    }

    public int getId(Scanner scanner) {
        int id;
        while (true) {
            System.out.print("Digite o ID da reserva que deseja cancelar: ");
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                break;
            } else {
                System.out.println("Por favor, insira um número válido para o ID.");
                scanner.next();
            }
        }
        return id;
    }

    private LocalTime obterHorario(Scanner scanner) {
        String horarioInput;
        do {
            System.out.print("Horas (HH:mm): ");
            horarioInput = scanner.nextLine();
        } while (!validation.validarHorario(horarioInput));

        return LocalTime.parse(horarioInput);
    }

    public void listar(ResultSet rs) throws SQLException {
        String line = "----------------------------------------------------------------------------------------------------------------------------------------------";
        String padrao = "| %-5s | %-30s | %-20s | %-10s | %-7s | %-12s | %-16s | %-18s |%n";
        System.out.println(line);

        System.out.printf(padrao,
                "ID", "Nome", "Tipo", "CEP", "Número", "Tempo máximo", "Horário abertura",
                "Horário fechamento");

        System.out.println(line);

        while (rs.next()) {
            System.out.printf(padrao,
                    rs.getInt("idLocal"), rs.getString("nome"), rs.getString("tipo"),
                    rs.getString("cep"), rs.getInt("numero"), rs.getTime("tempo_maximo"),
                    rs.getTime("horario_abertura"), rs.getTime("horario_fechamento"));
        }

        System.out.println(line);
    }

    public void listarReservas(ResultSet rs) throws SQLException {
        String line = "--------------------------------------------------------------------------------------------------";
        String padrao = "| %-5s | %-10s | %-8s | %-8s | %-9s | %-12s | %-9s | %-12s |%n";

        System.out.println(line);
        System.out.printf(padrao,
                "ID", "Data", "Inicio", "Fim", "Status", "DataRegistro", "ID local",
                "CPF User");

        System.out.println(line);

        while (rs.next()) {
            System.out.printf(padrao,
                    rs.getInt("idReserva"), rs.getDate("data"), rs.getTime("horario_inicio"),
                    rs.getTime("horario_fim"), rs.getString("status"), rs.getDate("data_registro"),
                    rs.getInt("idLocal"), rs.getString("cpfUsuario"));
        }

        System.out.println(line);
    }

    public void exibirHorariosDisponiveis(ArrayList<LocalTime> horariosReservados, ResultSet rs) {
        try {
            System.out.println("Horários disponíveis para o local no dia:");

            while (rs.next()) {
                LocalTime horarioAbertura = rs.getTime("horario_abertura").toLocalTime();
                LocalTime horarioFechamento = rs.getTime("horario_fechamento").toLocalTime();

                exibirHorariosDisponiveisPorIntervalo(horariosReservados, horarioAbertura, horarioFechamento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao processar os horários: " + e.getMessage());
        }
    }

    private void exibirHorariosDisponiveisPorIntervalo(ArrayList<LocalTime> horariosReservados,
            LocalTime horarioAbertura, LocalTime horarioFechamento) {
        LocalTime horarioAtual = horarioAbertura;

        while (horarioAtual.isBefore(horarioFechamento)) {
            if (!horariosReservados.contains(horarioAtual)) {
                System.out.println(horarioAtual);
            }
            horarioAtual = horarioAtual.plusMinutes(60);
        }
    }

}
