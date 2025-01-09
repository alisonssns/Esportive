package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import model.LocalModel;

public class LocalUtils {
    public void setLocal(LocalModel model, ResultSet rs) throws SQLException {
        model.setNome(rs.getString("nome"));
        model.setTipo(rs.getString("tipo"));
        model.setCep(rs.getInt("cep"));
        model.setNumero(rs.getInt("numero"));
        model.setTempoMaximo(rs.getTime("tempo_maximo").toLocalTime());
        model.setHorarioAbertura(rs.getTime("horario_abertura").toLocalTime());
        model.setHorarioFechamento(rs.getTime("horario_fechamento").toLocalTime());
    }

    public void cadastrar(LocalModel local, ArrayList<Object> values) {  
        values.add(local.getNome());
        values.add(local.getCep());
        values.add(local.getTipo());
        values.add(local.getNumero());
        values.add(Time.valueOf(local.getTempoMaximo()));
        values.add(Time.valueOf(local.getHorarioAbertura()));
        values.add(Time.valueOf(local.getHorarioFechamento()));
    }
}
