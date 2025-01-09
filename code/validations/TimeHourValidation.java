package validations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import model.ReservaModel;

public class TimeHourValidation {

    public boolean validarHorario(String horarioInput) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime duracao = null;
        try {
            duracao = LocalTime.parse(horarioInput, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Formato inválido. Por favor, insira a duração no formato HH:mm.");
        }
        return duracao != null ? true : false;
    }

    public boolean validarData(Scanner scanner, LocalDate currentDate, String dataStr, DateTimeFormatter formatter) {
        try {
            LocalDate data = LocalDate.parse(dataStr, formatter);

            if (data.isBefore(currentDate)) {
                System.out.println("A data não pode ser anterior à data atual. Tente novamente.");
                return false;
            } else {
                return true;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Use o formato dd-MM-yyyy.");
            return false;
        }
    }

    public LocalTime calcularHorarioFim(ReservaModel model, String duracao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime duracaoTime = LocalTime.parse(duracao, formatter);

        LocalTime inicio = model.getHorarioInicio();
        LocalTime fim = inicio.plusHours(duracaoTime.getHour()).plusMinutes(duracaoTime.getMinute());

        return fim;
    }
}
