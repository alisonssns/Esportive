package utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import model.EventModel;

public class EventUtils {
    public void setLocal(EventModel model, ResultSet rs) throws SQLException {
        model.setNome(rs.getString("nome"));
        model.setData(rs.getDate("data").toLocalDate());
        model.setHorario(rs.getTime("horario").toLocalTime());
        model.setCapacidade(rs.getInt("capacidade"));
        model.setDescricao(rs.getString("descricao"));
    }

    public void cadastrar(EventModel local, ArrayList<Object> values) {  
        values.add(local.getNome());
        values.add(Date.valueOf(local.getData()));
        values.add(Time.valueOf(local.getHorario()));
        values.add(local.getCapacidade());
        values.add(local.getDescricao());
        values.add(local.getIdLocal());
    }
}