package view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import model.EventModel;
import validations.TimeHourValidation;

public class EventView {
    TimeHourValidation validation = new TimeHourValidation();

    public void cadastrar(Scanner scanner, EventModel model) {
        System.out.print("Nome: ");
        model.setNome(scanner.nextLine());
        model.setData(obterData(scanner));
        model.setHorario(obterHorario(scanner));
        System.out.print("Capacidade: ");
        model.setCapacidade(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Descrição: ");
        model.setDescricao(scanner.nextLine());
        System.out.print("ID do local: ");
        model.setIdLocal(scanner.nextInt());
    }

    private LocalTime obterHorario(Scanner scanner) {
        String horarioInput;
        do {
            System.out.print("Escolha o horário da reserva (HH:mm): ");
            horarioInput = scanner.nextLine();
        } while (!validation.validarHorario(horarioInput));

        return LocalTime.parse(horarioInput);
    }

    public LocalDate obterData(Scanner scanner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataInput;

        do {
            System.out.print("Escolha uma data para sua reserva (dd-MM-yyyy): ");
            dataInput = scanner.nextLine();
        } while (!validation.validarData(scanner, LocalDate.now(), dataInput, formatter));

        LocalDate data = LocalDate.parse(dataInput, formatter);
        return data;
    }

    public void infoUser(String cpf, String nome, String email) {
        String line = "-------------------------------------------------------------------";
        String padrao = "| %-12s | %-20s | %-20s |%n";
        System.out.println(line);
        System.out.printf(padrao, cpf, nome, email);
        System.out.println(line);
    }

    public void listar(ResultSet rs) throws SQLException {
        String line = "----------------------------------------------------------------------------------------------------------------------------";
        String padrao = "| %-5s | %-30s | %-10s | %-10s | %-10s | %-8s | %-30s |%n";
        System.out.println(line);

        System.out.printf(padrao,
                "ID", "Nome", "Data", "Horario", "Vagas", "IdLocal", "Descrição");

        System.out.println(line);

        while (rs.next()) {
            System.out.printf(padrao,
                    rs.getInt("idLocal"), rs.getString("nome"), rs.getDate("data"),
                    rs.getString("horario"), rs.getInt("capacidade") - rs.getInt("quantReservados"), rs.getInt("idLocal"), rs.getString("descricao"));
        }

        System.out.println(line);
    }
}
