package controller;

import model.ReservaModel;
import model.GenericModel;
import view.UsuarioView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UsuarioController implements IUser {

    private final UsuarioView view = new UsuarioView();
    private final GenericModel model = new GenericModel();
    private final LocalController localController = new LocalController();
    private final ReservaModel modelReserva = new ReservaModel();

    @Override
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

    @Override
    public boolean logar(Scanner scanner, GenericModel model) {
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
                                System.out.println(rsTelefone.getString("numero"));
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

    public void fazerReserva(Scanner scanner, GenericModel model) {
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

    public void listarReservas(Scanner scanner, GenericModel model) {
        String sql = "SELECT * FROM reserva WHERE cpfUsuario = ?";
        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, model.getCpf());
            try (ResultSet rs = stmt.executeQuery()) {
                view.listarReservas(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void cancelarReserva(Scanner scanner, GenericModel model) {
        listarReservas(scanner, model);
        System.out.println("Digite o ID da reserva que deseja cancelar: ");
        int idReserva = scanner.nextInt();

        String sql = "DELETE FROM reserva WHERE idreserva = ? AND cpfUsuario = ?";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReserva);
            stmt.setString(2, model.getCpf());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Reserva cancelada com sucesso!");
            } else {
                System.out.println("Reserva não encontrada ou não pertence ao usuário.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cancelar reserva: " + e.getMessage());
        }
    }

    @Override
    public void mostraInfo(GenericModel model) {
        System.out.printf("CPF:           %s%n", model.getCpf());
        System.out.printf("Nome:          %s%n", model.getNome());
        System.out.printf("E-mail:        %s%n", model.getEmail());
        System.out.printf("Senha:         %s%n", model.getSenha());
        System.out.println("------------------------------------");
        System.out.printf("Rua:           %s%n", model.getRua());
        System.out.printf("Bairro:        %s%n", model.getBairro());
        System.out.printf("Cidade:        %s%n", model.getCidade());
        System.out.printf("CEP:           %s%n", model.getCep());
        System.out.printf("Estado:        %s%n", model.getEstado());
        System.out.printf("Número:        %s%n", model.getNumero());
        System.out.println("------------------------------------");
        System.out.printf("Telefone:      %s%n", model.getTelefone());
        System.out.println("====================================");
    }

    @Override
    public void atualizarInfo(Scanner scanner, GenericModel model) {
        System.out.println("Digite o novo nome: ");
        String novoNome = scanner.nextLine();
        System.out.println("Digite o novo e-mail: ");
        String novoEmail = scanner.nextLine();
        System.out.println("Digite a nova senha: ");
        String novaSenha = scanner.nextLine();

        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE cpf = ?";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setString(2, novoEmail);
            stmt.setString(3, novaSenha);
            stmt.setString(4, model.getCpf());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                model.setNome(novoNome);
                model.setEmail(novoEmail);
                model.setSenha(novaSenha);
                System.out.println("Informações atualizadas com sucesso!");
            } else {
                System.out.println("Erro ao atualizar informações. Usuário não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar informações: " + e.getMessage());
        }
    }

}
