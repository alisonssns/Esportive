package controller;

import model.ReservaModel;
import model.UsuarioModel;
import view.UsuarioView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class UsuarioController {
    Scanner scanner = new Scanner(System.in);

    private UsuarioView view = new UsuarioView();
    private UsuarioModel model = new UsuarioModel();
    private ReservaModel modelReserva = new ReservaModel();

    public void cadastrar(Scanner scanner) {
        view.cadastrar(scanner, model);

        String sql = "INSERT INTO usuario (cpf, nome, email, senha) VALUES (?, ?, ?, ?)";
        String sql2 = "INSERT INTO telefoneusuario (numero, cpfUsuario) VALUES (?, ?)";

        try (Connection conn = Conector.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, model.getCpf());
                stmt.setString(2, model.getNome());
                stmt.setString(3, model.getEmail());
                stmt.setString(4, model.getSenha());
                stmt.executeUpdate();
            }

            try (PreparedStatement tstmt = conn.prepareStatement(sql2)) {
                tstmt.setString(1, model.getTelefone());
                tstmt.setString(2, model.getCpf());
                tstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
            try (Connection conn = Conector.getConnection()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Erro ao reverter a transação: " + rollbackEx.getMessage());
            }
        }
    }

    public void logar(Scanner scanner) {
        view.logar(scanner, model);

        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, model.getEmail());
            stmt.setString(2, model.getSenha());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Usuário encontrado!");
                view.listar(rs);
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao Logar: " + e.getMessage());
        }
    }

    public void fazerReserva(Scanner scanner) {
        view.fazerReserva(scanner, modelReserva);
        String sql = "INSERT INTO reserva (cpfUsuario, data,horario, status, idLocal) values (?,?,?,?,?)";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "6");
            stmt.setDate(2, modelReserva.getData());
            stmt.setTime(3, modelReserva.getHorario());
            stmt.setString(4, modelReserva.getStatus());
            stmt.setInt(5, modelReserva.getIdLocal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao Logar: " + e.getMessage());
        }
    }

    public void cancelarReserva() {
    }

    public void atulizarInfo() {
    }

    public void avaliarEspaco() {
    }
}
