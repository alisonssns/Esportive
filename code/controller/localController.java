package controller;

import model.LocalModel;
import view.LocalView;
import utils.CRUD;
import utils.LocalUtils;

import java.sql.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class LocalController {
    private LocalModel model = new LocalModel();
    private LocalUtils utils = new LocalUtils();
    private LocalView view = new LocalView();
    private CRUD crud = new CRUD();
    private ArrayList<Object> values = new ArrayList<>();

    public void cadastrar(Scanner scanner) {
        values.clear();
        view.cadastrar(scanner, model);
        utils.cadastrar(model, values);
        String sql = "INSERT INTO local (nome, cep,tipo, numero, tempo_maximo, horario_abertura, horario_fechamento) VALUES (?,?,?,?,?,?,?)";

        try {
            crud.insert(sql, values);
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    public void listar() {
        String sql = "SELECT * FROM local";
        values.clear();

        try (ResultSet rs = crud.select(sql, values)) {
            view.listar(rs);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public boolean listarReservas(int idLocal) {
        String query = "SELECT * FROM reserva WHERE idLocal = ?";
        values.clear();
        values.add(idLocal);

        ResultSet rs = null;
        try {
            rs = crud.select(query, values);
            if (rs != null && rs.next()) {
                view.listarReservas(rs);
                return true;
            } else {
                System.out.println("Nenhuma reserva encontrada para este ID");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao executar a consulta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void cancelarReserva(Scanner scanner, int id) {
        listarReservas(id);
        int idreserva = view.getId(scanner);

        values.clear();
        values.add(idreserva);
        values.add(id);

        if (!existeReserva(values)) {
            System.out.println("Reserva não encontrada.");
            return;
        }

        String query = "UPDATE reserva SET status = 'CANCELADA' WHERE idreserva = ? and idLocal = ?";

        try {
            crud.update(query, values);
        } catch (SQLException e) {
            System.out.println("Erro ao cancelar a reserva: " + e.getMessage());
        }
    }

    private boolean existeReserva(ArrayList<Object> values) {
        String query = "SELECT COUNT(*) FROM reserva WHERE idreserva = ? AND idLocal = ?";
        try {
            ResultSet rs = crud.select(query, values);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar a reserva: " + e.getMessage());
        }
        return false;
    }    

    public LocalModel infoLocal(int idLocal) {
        LocalModel localModel = null;
        String sql = "SELECT * FROM local WHERE idLocal = ?";
        values.clear();
        values.add(idLocal);

        try (ResultSet rs = crud.select(sql, values)) {
            if (rs.next()) {
                localModel = new LocalModel();
                utils.setLocal(localModel, rs);
            }else {
                System.out.println("Local não encontrado para o ID fornecido.");
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return localModel;
    }

    public ArrayList<LocalTime> listarReservasDia(int idLocal, LocalDate data) {
        ArrayList<LocalTime> horariosReservados = new ArrayList<>();
        String sql = "SELECT horario_inicio, horario_fim FROM reserva WHERE idLocal = ? AND data = ?";
        values.clear();
        values.add(idLocal);
        values.add(java.sql.Date.valueOf(data));

        try (ResultSet rs = crud.select(sql, values)) {
            while (rs.next()) {
                LocalTime horarioInicio = rs.getTime("horario_inicio").toLocalTime();
                LocalTime horarioFim = rs.getTime("horario_fim").toLocalTime();
                horariosReservados.addAll(getHorariosReservados(horarioInicio, horarioFim));
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
        values.clear();
        values.add(idLocal);

        try (ResultSet rs = crud.select(sql, values)) {
            view.exibirHorariosDisponiveis(horariosReservados, rs);
        } catch (SQLException e) {
            System.err.println("Erro ao listar horários disponíveis: " + e.getMessage());
        }
    }
}
