package utils;

import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import model.ReservaModel;
import model.UsuarioModel;

public class UserUtils {
    public void cadastrar(UsuarioModel model, ArrayList<Object> values) {
        values.clear();
        values.add(model.getCpf());
        values.add(model.getNome());
        values.add(model.getEmail());
        values.add(model.getSenha());
        values.add("cli");
    }

    public void cadastrarTelefone(UsuarioModel model, ArrayList<Object> values) {
        values.clear();
        values.add(model.getTelefone());
        values.add(model.getCpf());
    }

    public void logar(UsuarioModel model, ArrayList<Object> values) {
        values.clear();
        values.add(model.getEmail());
        values.add(model.getSenha());
    }

    public void fazerReserva(String cpf, ReservaModel reserva, ArrayList<Object> values) {
        LocalDate creationDate = LocalDate.now();
        LocalTime creationTime = LocalTime.now().minusHours(1);

        values.clear();
        values.add(cpf);
        values.add(Date.valueOf(reserva.getData()));
        values.add(Time.valueOf(reserva.getHorarioInicio()));
        values.add(Time.valueOf(reserva.getHorarioFim()));
        values.add(reserva.getStatus());
        values.add(reserva.getIdLocal());
        values.add(Date.valueOf(creationDate));
        values.add(Time.valueOf(creationTime));
    }
}
