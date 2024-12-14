package controller;

import model.ReservaModel;
import model.UsuarioModel;
import view.UsuarioView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UsuarioController {

    private final UsuarioView view = new UsuarioView();
    private final UsuarioModel model = new UsuarioModel();
    private final LocalController localController = new LocalController();
    private final ReservaModel modelReserva = new ReservaModel();

    public void cadastrar(Scanner scanner) {
        view.cadastrar(scanner, model);

        String sqlUsuario = "INSERT INTO usuario (cpf, nome, email, senha) VALUES (?, ?, ?, ?)";
        String sqlTelefone = "INSERT INTO telefoneusuario (numero, cpfUsuario) VALUES (?, ?)";

        try (Connection conn = Conector.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                stmt.setString(1, model.getCpf());
                stmt.setString(2, model.getNome());
                stmt.setString(3, model.getEmail());
                stmt.setString(4, model.getSenha());
                stmt.executeUpdate();
            }

            try (PreparedStatement tstmt = conn.prepareStatement(sqlTelefone)) {
                tstmt.setString(1, model.getTelefone());
                tstmt.setString(2, model.getCpf());
                tstmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Cadastro realizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
            try (Connection conn = Conector.getConnection()) {
                conn.rollback();
                System.out.println("Transação revertida.");
            } catch (SQLException rollbackEx) {
                System.out.println("Erro ao reverter a transação: " + rollbackEx.getMessage());
            }
        }
    }

    public boolean logar(Scanner scanner, UsuarioModel model) {
        view.logar(scanner, model);

        String sqlUsuario = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        String sqlTelefone = "SELECT numero FROM telefoneusuario WHERE cpfUsuario = ?";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario)) {

            stmtUsuario.setString(1, model.getEmail());
            stmtUsuario.setString(2, model.getSenha());

            try (ResultSet rsUsuario = stmtUsuario.executeQuery()) {
                if (rsUsuario.next() && "cli".equals(rsUsuario.getString("tipo"))) {
                    try (PreparedStatement stmtTelefone = conn.prepareStatement(sqlTelefone)) {
                        stmtTelefone.setString(1, rsUsuario.getString("cpf"));

                        try (ResultSet rsTelefone = stmtTelefone.executeQuery()) {
                            if (rsTelefone.next()) {
                                model.setTelefone(rsTelefone.getString("numero"));
                            }
                        }
                    }

                    System.out.println();
                    System.out.println("Seja bem-vindo: " + rsUsuario.getString("nome"));
                    view.logSuccess(rsUsuario, model);
                    return true;
                    
                } else {
                    System.out.println("Usuário não encontrado.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao logar: " + e.getMessage());
        }
        return false;
    }

    public void fazerReserva(Scanner scanner, UsuarioModel model) {
        localController.listar();
        view.fazerReserva(scanner, modelReserva);

        String sql = "INSERT INTO reserva (cpfUsuario, data, horario, status, idLocal) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, model.getCpf());
            stmt.setDate(2, modelReserva.getData());
            stmt.setTime(3, modelReserva.getHorario());
            stmt.setString(4, modelReserva.getStatus());
            stmt.setInt(5, modelReserva.getIdLocal());
            stmt.executeUpdate();

            System.out.println("Reserva realizada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao realizar a reserva: " + e.getMessage());
        }
    }

    public void cancelarReserva() {

    }
    public void atualizarInfo() {
    }

    public void avaliarEspaco() {

    }
}
