package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import model.EventModel;
import utils.CRUD;
import utils.EventUtils;
import view.EventView;

public class EventController {
    private EventModel model = new EventModel();
    private EventUtils utils = new EventUtils();
    private EventView view = new EventView();
    private CRUD crud = new CRUD();
    private ArrayList<Object> values = new ArrayList<>();

    public void cadastrar(Scanner scanner) {
        values.clear();
        view.cadastrar(scanner, model);
        utils.cadastrar(model, values);
        String sql = "INSERT INTO evento (nome, data, horario, capacidade, descricao, idLocal) VALUES (?,?,?,?,?,?)";

        try {
            crud.insert(sql, values);
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    public void listar() {
        String sql = "SELECT * FROM evento";
        values.clear();

        try (ResultSet rs = crud.select(sql, values)) {
            view.listar(rs);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void reservar(int id) {
        String query = "SELECT COUNT(*) from reservaevento where idEvento = ?";
        String queryQ = "UPDATE evento SET quantReservados = ?";
        int num = 0;

        values.clear();
        values.add(id);

        ResultSet rs = null;
        try {
            rs = crud.select(query, values);
            if(rs.next()){
                num = rs.getInt("COUNT(*)");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao executar a consulta: " + e.getMessage());
            e.printStackTrace();
        }

        values.clear();
        values.add(num);

        try {
            crud.update(queryQ, values);
        } catch (SQLException e) {
            System.err.println("Erro ao executar a consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void listarReservas(int idEvento) {
        String query = "SELECT * FROM reservaevento WHERE idEvento = ?";
        values.clear();
        values.add(idEvento);

        ResultSet rs = null;
        try {
            rs = crud.select(query, values);
            if (rs != null && rs.next()) {
                while (rs.next()) {
                    listarUsuarios(rs.getString("cpfUsuario"));
                }
            } else {
                System.out.println("Nenhuma reserva encontrada para este ID");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao executar a consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void listarUsuarios(String cpf) {
        String query = "SELECT * FROM usuario WHERE cpf = ?";
        values.clear();
        values.add(cpf);
        ResultSet rs = null;
        try {
            rs = crud.select(query, values);
            if (rs != null && rs.next()) {
                view.infoUser(rs.getString("cpf"), rs.getString("nome"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao executar a consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cancelarReserva(int id) {
        values.clear();
        values.add(id);

        if (!existeReserva(values)) {
            System.out.println("Reserva não encontrada.");
            return;
        }

        String query = "UPDATE reservaevento SET status = 'CANCELADA' WHERE idEvento = ? and cpfUsuario = ?";

        try {
            crud.update(query, values);
        } catch (SQLException e) {
            System.out.println("Erro ao cancelar a reserva: " + e.getMessage());
        }
    }

    private boolean existeReserva(ArrayList<Object> values) {
        String query = "SELECT COUNT(*) FROM reservaevento WHERE cpfUsuario = ? AND idLocal = ?";
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

    public boolean reservaDuplicada(String cpf, int idLocal) {
        values.clear();
        values.add(cpf);
        values.add(idLocal);
        String query = "SELECT COUNT(*) FROM reservaEvento WHERE cpfUsuario = ? and idEvento = ?";

        try {
            ResultSet rs = crud.select(query, values);
            if (rs.next()) {
                if(rs.getInt("COUNT(*)") > 0){
                    System.out.println("Você já fez reserva neste local!");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao realizar a reserva: " + e.getMessage());
        }
        return false;
    }

    public boolean verificarVagasEvento(int id) {
        values.clear();
        values.add(id);
        String query = "SELECT capacidade, quantReservados FROM evento WHERE id = ?";

        try {
            ResultSet rs = crud.select(query, values);
            if (rs.next()) {
                if (((rs.getInt("capacidade") - rs.getInt("quantReservados")) > 0)) {
                    return true;
                } else {
                    System.out.println("Este local não tem vagas suficientes");
                }
            } else {
                System.out.println("Evento não encontrado!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao realizar a reserva: " + e.getMessage());
        }
        return false;
    }

    public EventModel infoEvento(int idLocal) {
        EventModel model = null;
        String sql = "SELECT * FROM evento WHERE id = ?";
        values.clear();
        values.add(idLocal);

        try (ResultSet rs = crud.select(sql, values)) {
            if (rs.next()) {
                model = new EventModel();
                utils.setLocal(model, rs);
            } else {
                System.out.println("Local não encontrado para o ID fornecido.");
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return model;
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
}
