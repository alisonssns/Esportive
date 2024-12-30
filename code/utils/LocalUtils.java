package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.LocalModel;

public class LocalUtils {
    public void setLocal(LocalModel model, ResultSet rs) throws SQLException {
    if (rs.next()) {
        model.setNome(rs.getString("nome"));
        model.setTipo(rs.getString("tipo"));
        model.setCep(rs.getString("cep"));
        model.setNumero(rs.getInt("numero"));
        model.setLimiteDia(rs.getInt("limite_por_dia"));
        model.setTempoMaximo(rs.getTime("tempo_maximo").toLocalTime());
        model.setHorarioAbertura(rs.getTime("horario_abertura").toLocalTime());
        model.setHorarioFechamento(rs.getTime("horario_fechamento").toLocalTime());
    } else {
        System.out.println("Local não encontrado para o ID fornecido.");
    }
}

}
