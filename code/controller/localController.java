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

    public void listar() {
        String sql = "SELECT * FROM local";
        ArrayList<Object> values = new ArrayList<>();

        try (ResultSet rs = crud.select(sql, values)) {
            view.listar(rs);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void listarReservas(int idLocal) {
        String sql = "SELECT * FROM reserva WHERE idLocal = ?";
        ArrayList<Object> values = new ArrayList<>();
        values.add(idLocal);

        try (ResultSet rs = crud.select(sql, values)) {
            view.listarReservas(rs);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public LocalModel infoLocal(int idLocal) {
        LocalModel localModel = new LocalModel();
        String sql = "SELECT * FROM local WHERE idLocal = ?";
        ArrayList<Object> values = new ArrayList<>();
        values.add(idLocal);

        try (ResultSet rs = crud.select(sql, values)) {
            utils.setLocal(localModel, rs);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return localModel;
    }

    public ArrayList<LocalTime> listarReservasDia(int idLocal, LocalDate data) {
        ArrayList<LocalTime> horariosReservados = new ArrayList<>();
        String sql = "SELECT horario_inicio, horario_fim FROM reserva WHERE idLocal = ? AND data = ?";
        ArrayList<Object> values = new ArrayList<>();
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
        ArrayList<Object> values = new ArrayList<>();
        values.add(idLocal);

        try (ResultSet rs = crud.select(sql, values)) {
            view.exibirHorariosDisponiveis(horariosReservados, rs);
        } catch (SQLException e) {
            System.err.println("Erro ao listar horários disponíveis: " + e.getMessage());
        }
    }
}
