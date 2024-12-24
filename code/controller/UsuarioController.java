package controller;

import model.ReservaModel;
import model.UsuarioModel;
import view.UsuarioView;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

public class UsuarioController implements IUserAdmController {
    private final UsuarioView view = new UsuarioView();
    private final UsuarioModel model = new UsuarioModel();
    private final LocalController localController = new LocalController();
    private final ReservaModel modelReserva = new ReservaModel();

    public void iniciar(Scanner scanner) {
    boolean logado = false;
    boolean sair = false;

    while (!sair) {
        if (!logado) {
            logado = exibirTelaInicial(scanner);
        } else {
            logado = exibirMenuPrincipal(scanner);
        }
    }
}

    @Override
    public void cadastrar(Scanner scanner) {
        view.cadastrar(scanner, model);

        String sqlUsuario = "INSERT INTO usuario (cpf, nome, email, senha, tipo) VALUES (?, ?, ?, ?, ?)";
        String sqlTelefone = "INSERT INTO telefoneusuario (numero, cpfUsuario) VALUES (?, ?)";

        try (Connection conn = Conector.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                stmt.setString(1, model.getCpf());
                stmt.setString(2, model.getNome());
                stmt.setString(3, model.getEmail());
                stmt.setString(4, model.getSenha());
                stmt.setString(5, "cli");
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
    public boolean logar(Scanner scanner) {
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
                    loginSuccess(rsUsuario);
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

    private void loginSuccess(ResultSet rsUsuario) throws SQLException {
        model.setCpf(rsUsuario.getString("cpf"));
        model.setNome(rsUsuario.getString("nome"));
        model.setEmail(rsUsuario.getString("email"));
        model.setSenha(rsUsuario.getString("senha"));
    }

    @Override
    public void fazerReserva(Scanner scanner) {
        localController.listar();
        view.fazerReserva(scanner, modelReserva);

        String sql = "INSERT INTO reserva (cpfUsuario, data, horario_inicio, horario_fim, status, idLocal) VALUES (?, ?, ?, ?, ?,?)";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, model.getCpf());
            stmt.setDate(2, modelReserva.getData());
            stmt.setTime(3, Time.valueOf(modelReserva.getHorarioInicio()));
            stmt.setTime(4, Time.valueOf(modelReserva.getHorarioFim()));
            stmt.setString(5, modelReserva.getStatus());
            stmt.setInt(6, modelReserva.getIdLocal());
            stmt.executeUpdate();

            System.out.println("Reserva realizada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao realizar a reserva: " + e.getMessage());
        }
    }

    public void listarReservas(String cpf) {
        String sql = "SELECT * FROM reserva WHERE cpfUsuario = ?";
        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                view.listarReservas(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelarReserva(Scanner scanner, String cpf) {
        listarReservas(cpf);
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
    public void exibirInfo() {
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
    public void atualizarInfo(Scanner scanner) {
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
    @Override
    public boolean exibirTelaInicial(Scanner scanner) {
        int opcao;
        view.exibirMenuInicial();
        opcao = lerOpcaoDoUsuario(scanner);

        if (opcao != 3) {
            scanner.nextLine();
            return (executarOpcaoTelaInicial(scanner, opcao));
        } else {
            System.out.println("Saindo...");
            System.exit(0);
            return false;
        }
    }
    @Override
    public boolean exibirMenuPrincipal(Scanner scanner) {
        int opcao;
        do {
            view.exibirMenuPrincipal();
            opcao = lerOpcaoDoUsuario(scanner);
            if (opcao != 6) {
                executarOpcaoMenuPrincipal(scanner, opcao);
            }
        } while (opcao != 6);
        return false;
    }
    
    private int lerOpcaoDoUsuario(Scanner scanner) {
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            System.out.println("Entrada inválida! Por favor, insira um número.");
            scanner.nextLine();
            return 0;
        }
    }
    
    private boolean executarOpcaoTelaInicial(Scanner scanner,
            int opcao) {
        boolean logado = false;
        switch (opcao) {
            case 1:
                cadastrar(scanner);
                break;
            case 2:
                logado = logar(scanner);
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
                break;
        }
        return logado;
    }
    
    private void executarOpcaoMenuPrincipal(Scanner scanner, int opcao) {
        switch (opcao) {
            case 1:
                fazerReserva(scanner);
                break;
            case 2:
                listarReservas(model.getCpf());
                break;
            case 3:
                cancelarReserva(scanner, model.getCpf());
                break;
            case 4:
                exibirInfo();
                break;
            case 5:
                atualizarInfo(scanner);
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
                break;
        }
    }

}
