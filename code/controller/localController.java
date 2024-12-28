package controller;

import model.LocalModel;
import view.LocalView;
import utils.CRUD;

import java.sql.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class LocalController {
    private LocalView view = new LocalView();
    private LocalModel model = new LocalModel();
    private CRUD crud = new CRUD();

    // Método principal para cadastrar um local
    public void cadastrar(Scanner scanner) {
        ArrayList<Object> values = new ArrayList<>();
        view.cadastrar(scanner, model, values);
        String sql = "INSERT INTO local (nome, tipo, cep, numero, limite_por_dia, tempo_maximo, horario_abertura, horario_funcionamento) VALUES (?,?,?,?,?,?,?,?)";
        
        try {
            crud.insert(sql, values);
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    // Método para ArrayListar todos os locais
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

    // Método para ArrayListar um local específico por id
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

    // Método para ArrayListar as reservas do dia
    public ArrayList<LocalTime> listarReservasDia(int idLocal, LocalDate data) {
        ArrayList<LocalTime> horariosReservados = new ArrayList<>();
        String sql = "SELECT horario_inicio, horario_fim FROM reserva WHERE idLocal = ? AND data = ?";

        try (Connection conn = Conector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLocal);
            stmt.setDate(2, java.sql.Date.valueOf(data)); // Conversão correta para java.sql.Date
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

    public void listarHorariosDisponiveis(int idLocal, LocalDate data) {
        ArrayList<LocalTime> horariosReservados = listarReservasDia(idLocal, data);
        String sql = "SELECT horario_abertura, horario_fechamento FROM local WHERE idLocal = ?";

        try (Connection conn = Conector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLocal);
            try (ResultSet rs = stmt.executeQuery()) {
                view.exibirHorariosDisponiveis(horariosReservados, rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao ArrayListar horários disponíveis: " + e.getMessage());
        }
    }
}
