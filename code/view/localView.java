package view;

import model.LocalModel;

import java.sql.SQLException;
import java.time.LocalTime;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class LocalView {

    public LocalModel local;

    public void cadastrar(Scanner scanner, LocalModel local) {
        System.out.print("Nome: ");
        local.setNome(scanner.nextLine());
        System.out.print("Cep: ");
        local.setCep(scanner.nextLine());
        System.out.print("Tipo: ");
        local.setTipo(scanner.nextLine());
        System.out.print("Numero: ");
        local.setNumero(scanner.nextInt());
        System.out.print("Limites de reservas por dia (por usuário): ");
        local.setNumero(scanner.nextInt());
        System.out.print("Tempo máximo de uma reserva (em horas): ");
        local.setNumero(scanner.nextInt());
        System.out.print("Horario de abertura: ");
        local.setNumero(scanner.nextInt());
        System.out.print("Horario de fechamento: ");
        local.setNumero(scanner.nextInt());
    }

    public void listar(ResultSet rs) throws SQLException {
        String line = "----------------------------------------------------------------------------------------------------------------------------------------------";

        System.out.println(line);

        System.out.printf("| %-5s | %-30s | %-20s | %-10s | %-7s | %-12s | %-16s | %-18s |%n",
                "ID", "Nome", "Tipo", "CEP", "Número", "Tempo máximo", "Horário abertura",
                "Horário fechamento");

        System.out.println(line);

        while (rs.next()) {
            System.out.printf("| %-5s | %-30s | %-20s | %-10s | %-7s | %-12s | %-16s | %-18s |%n",
                    rs.getInt("idLocal"), rs.getString("nome"), rs.getString("tipo"),
                    rs.getString("cep"), rs.getInt("numero"), rs.getTime("tempo_maximo"),
                    rs.getTime("horario_abertura"), rs.getTime("horario_fechamento"));
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
