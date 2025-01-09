package utils;

import java.sql.Time;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import model.ReservaModel;
import model.UserModel;

public class UserUtils {
    CRUD crud = new CRUD();
    public void cadastrar(UserModel model, ArrayList<Object> values) {
        values.clear();
        values.add(model.getCpf());
        values.add(model.getNome());
        values.add(model.getEmail());
        values.add(model.getSenha());
        values.add("cli");
    }

    public void cadastrarEndereco(UserModel model, ArrayList<Object> values) {
        values.clear();
        values.add(model.getCep());
        values.add(model.getEstado());
        values.add(model.getCidade());
        values.add(model.getBairro());
        values.add(model.getRua());
        values.add(model.getNumero());
    }

    public void telefone(UserModel model, ArrayList<Object> values) {
        values.clear();
        values.add(model.getTelefone());
        values.add(model.getCpf());
    }

    public void logar(UserModel model, ArrayList<Object> values) {
        values.clear();
        values.add(model.getEmail());
        values.add(model.getSenha());
    }

    public void loginSuccess(ResultSet rsUsuario, UserModel model) throws SQLException {
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

    public void atualizarInfo(UserModel model, ArrayList<Object> values) {
        values.clear();
        values.add(model.getNome());
        values.add(model.getEmail());
        values.add(model.getSenha());
        values.add(model.getCpf());
    }

    public boolean verificarEmailExistente(String cpf) {
        ArrayList<Object> values = new ArrayList<>();
        values.add(cpf);
        String query = "SELECT COUNT(*) FROM usuario WHERE cpf = ?";
        try {
            ResultSet rs = crud.select(query, values);
             if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar a existência do usuário: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean verficiarCpfExistente(String cpf) {
        ArrayList<Object> values = new ArrayList<>();
        values.add(cpf);
        String query = "SELECT COUNT(*) FROM usuario WHERE cpf = ?";
        try {
            ResultSet rs = crud.select(query, values);
             if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar a existência do usuário: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean verificarTelefoneExistente(String telefone) {
        ArrayList<Object> values = new ArrayList<>();
        values.add(telefone);
        String query = "SELECT COUNT(*) FROM telefoneusuario WHERE numero = ?";
        try {
            ResultSet rs = crud.select(query, values);
             if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar a existência do usuário: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
