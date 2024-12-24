package controller;

import model.LocalModel;
import view.LocalView;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class LocalController {
    private LocalView view = new LocalView();
    private LocalModel model = new LocalModel();

    // Método principal para cadastrar um local
    public void cadastrar(Scanner scanner) {
        view.cadastrar(scanner, model);
        String sql = "INSERT INTO local (nome, tipo, cep, numero, limite_por_dia, tempo_maximo, horario_abertura, horario_funcionamento) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            setCadastrarLocalParams(stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void setCadastrarLocalParams(PreparedStatement stmt) throws SQLException {
        stmt.setString(1, model.getNome());
        stmt.setString(2, model.getTipo());
        stmt.setString(3, model.getCep());
        stmt.setInt(4, model.getNumero());
        stmt.setInt(5, model.getLimiteDia());
        stmt.setTime(6, Time.valueOf(model.getTempoMaximo()));
        stmt.setTime(7, Time.valueOf(model.getHorarioAbertura()));
        stmt.setTime(8, Time.valueOf(model.getHorarioFechamento()));
    }

    // Método para listar todos os locais
    public void listar() {
        String sql = "SELECT * FROM local";
        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            view.listar(rs);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // Método para listar um local específico por id
    public void listarReservas(int idLocal) {
        String sql = "SELECT * FROM reserva WHERE idLocal = ?";
        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLocal);
            try (ResultSet rs = stmt.executeQuery()) {
                view.listar(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public LocalModel infoLocal(int idLocal) {
        LocalModel localModel = new LocalModel();
        String sql = "SELECT * FROM local WHERE idLocal = ?";
        try (Connection conn = Conector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idLocal);
            
            try (ResultSet rs = stmt.executeQuery()) {
                // Verifica se há resultados no ResultSet
                if (rs.next()) {
                    localModel.setNome(rs.getString("nome"));
                    localModel.setTipo(rs.getString("tipo"));
                    localModel.setCep(rs.getString("cep"));
                    localModel.setNumero(rs.getInt("numero"));
                    localModel.setLimiteDia(rs.getInt("limite_por_dia"));
                    localModel.setTempoMaximo(rs.getTime("tempo_maximo").toLocalTime());
                    localModel.setHorarioAbertura(rs.getTime("horario_abertura").toLocalTime());
                    localModel.setHorarioFechamento(rs.getTime("horario_fechamento").toLocalTime());
                } else {
                    System.out.println("Local não encontrado para o ID fornecido.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        
        return localModel;
    }
    

    // Método para listar as reservas do dia
    public ArrayList<LocalTime> listarReservasDia(int idLocal, Date data) {
        ArrayList<LocalTime> horariosReservados = new ArrayList<>();
        String sql = "SELECT horario_inicio, horario_fim FROM reserva WHERE idLocal = ? AND data = ?";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLocal);
            stmt.setDate(2, data);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalTime horarioInicio = rs.getTime("horario_inicio").toLocalTime();
                    LocalTime horarioFim = rs.getTime("horario_fim").toLocalTime();
                    horariosReservados.addAll(getHorariosReservados(horarioInicio, horarioFim));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar reservas do dia: " + e.getMessage());
        }
        return horariosReservados;
    }

    private ArrayList<LocalTime> getHorariosReservados(LocalTime horarioInicio, LocalTime horarioFim) {
        ArrayList<LocalTime> horarios = new ArrayList<>();
        while (horarioInicio.isBefore(horarioFim)) {
            horarios.add(horarioInicio);
            horarioInicio = horarioInicio.plusMinutes(60);
        }
        return horarios;
    }

    public void listarHorariosDisponiveis(int idLocal, Date data) {
        ArrayList<LocalTime> horariosReservados = listarReservasDia(idLocal, data);
        String sql = "SELECT horario_abertura, horario_fechamento FROM local WHERE idLocal = ?";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLocal);
            try (ResultSet rs = stmt.executeQuery()) {
                view.exibirHorariosDisponiveis(horariosReservados, rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar horários disponíveis: " + e.getMessage());
        }
    }
}
