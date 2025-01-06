package utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import model.AdminModel;
import model.ReservaModel;
import model.UserModel;

public class AdminUtils {
    public void logar(AdminModel model, ArrayList<Object> values) {
        values.clear();
        values.add(model.getEmail());
        values.add(model.getSenha());
    }
    
    public void loginSuccess(ResultSet rsUsuario, AdminModel model) throws SQLException {
        model.setCpf(rsUsuario.getString("cpf"));
        model.setNome(rsUsuario.getString("nome"));
        model.setEmail(rsUsuario.getString("email"));
        model.setSenha(rsUsuario.getString("senha"));
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

    public void telefone(UserModel model, ArrayList<Object> values) {
        values.clear();
        values.add(model.getTelefone());
        values.add(model.getCpf());
    }

    public void setUsuario(UserModel model, ResultSet rs) throws SQLException {
        model.setNome(rs.getString("nome"));
        model.setEmail(rs.getString("tipo"));
        model.setCpf(rs.getString("cep"));
        model.setRua(rs.getString("rua"));
        model.setBairro(rs.getString("bairro"));
        model.setCidade(rs.getString("cidade"));
        model.setCep(rs.getInt("cep"));
        model.setEstado(rs.getString("estado"));
        model.setNumero(rs.getInt("numero"));
    }

}
