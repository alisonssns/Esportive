package controller;

import model.AdministradorModel;
import model.ReservaModel;
import model.UsuarioModel;
import view.AdministradorView;
import view.UsuarioView;
import utils.AdmUtils;
import utils.CRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;

public class AdministradorController implements IUserAdmController {
    private UsuarioController usuarioController = new UsuarioController();
    private LocalController localController = new LocalController();
    private final ReservaModel reservaModel = new ReservaModel();
    private AdministradorModel model = new AdministradorModel();
    private AdministradorView view = new AdministradorView();
    private AdmUtils utils = new AdmUtils();
    private CRUD crud = new CRUD();

    public void iniciar(Scanner scanner) {
        boolean logado = false;
        while (true) {
            if (!logado) {
                logado = exibirTelaInicial(scanner);
            } else {
                logado = exibirMenuPrincipal(scanner);
            }
        }
    }

    @Override
    public void cadastrar(Scanner scanner) {
        usuarioController.cadastrar(scanner);
    }

    @Override
    public boolean logar(Scanner scanner) {
        ArrayList<Object> values = new ArrayList<>();

        view.logar(scanner, model);
        utils.logar(model, values);

        String query = "SELECT * FROM usuario WHERE email = ? AND senha = ? AND tipo = 'cli'";

        try (ResultSet rsUsuario = crud.select(query, values)) {
            if (rsUsuario.next()) {
                System.out.println("Seja bem-vindo: " + rsUsuario.getString("nome"));
                addTelefone();
                loginSuccess(rsUsuario);
                return true;
            } else {
                System.out.println("Usuário não encontrado.");
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

    private void addTelefone() {
        ArrayList<Object> values = new ArrayList<>();
        values.add(model.getCpf());

        String query = "SELECT numero FROM telefoneusuario WHERE cpfUsuario = ?";

        try (ResultSet rsUsuario = crud.select(query, values)) {
            if (rsUsuario.next()) {
                model.setTelefone(rsUsuario.getString("numero"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao procurar telefone: " + e.getMessage());
        }
    }

    @Override
    public void fazerReserva(Scanner scanner) {
        UsuarioView userView = new UsuarioView();
        ArrayList<Object> values = new ArrayList<>();

        listarUsuarios();
        localController.listar();

        System.out.println("Digite o CPF do usuário para o qual deseja fazer a reserva: ");
        String cpfUsuario = scanner.nextLine();

        userView.fazerReserva(scanner, reservaModel);
        utils.fazerReserva(cpfUsuario, reservaModel, values);

        String query = "INSERT INTO reserva (cpfUsuario, data, horario_inicio, horario_fim, status, idLocal, data_registro, hora_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            crud.insert(query, values);
        } catch (SQLException e) {
            System.out.println("Erro ao realizar a reserva: " + e.getMessage());
        }
    }

    public void listarReservas(Scanner scanner) {
        int opcao = view.listarReservas();
        if (opcao == 1) {
            listarUsuarios();
            System.out.println("Digite o CPF do usuario para listar as reservas: ");
            String cpf = scanner.nextLine();
            usuarioController.listarReservas(cpf);
        } else if (opcao == 2) {
            localController.listar();
            System.out.println("Digite o ID do local para listar as reservas: ");
            int id = scanner.nextInt();
            localController.listarReservas(id);
        }
    }

    @Override
    public void cancelarReserva(Scanner scanner, String cpf) {
        listarReservas(scanner);
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
        listarUsuarios();
        System.out.println("Digite o CPF do usuário que deseja alterar informações: ");
        String cpf = scanner.nextLine();

        UsuarioModel usuario = new UsuarioModel();
        buscarUsuarioPorCpf(cpf, usuario);

        if (!usuario.getNome().isEmpty()) {
            System.out.println("Digite o novo nome (ou pressione Enter para manter o atual): ");
            String novoNome = scanner.nextLine();
            System.out.println("Digite o novo e-mail (ou pressione Enter para manter o atual): ");
            String novoEmail = scanner.nextLine();
            System.out.println("Digite a nova senha (ou pressione Enter para manter a atual): ");
            String novaSenha = scanner.nextLine();

            atualizarUsuario(usuario, cpf, novoNome, novoEmail, novaSenha);
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    private void atualizarUsuario(UsuarioModel usuario, String cpf, String novoNome, String novoEmail,
            String novaSenha) {
        String nomeFinal = (novoNome != null && !novoNome.isEmpty()) ? novoNome : usuario.getNome();
        String emailFinal = (novoEmail != null && !novoEmail.isEmpty()) ? novoEmail : usuario.getEmail();
        String senhaFinal = (novaSenha != null && !novaSenha.isEmpty()) ? novaSenha : usuario.getSenha();

        String atualizarSql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE cpf = ?";

        try (Connection conn = Conector.getConnection();
                PreparedStatement atualizarStmt = conn.prepareStatement(atualizarSql)) {
            atualizarStmt.setString(1, nomeFinal);
            atualizarStmt.setString(2, emailFinal);
            atualizarStmt.setString(3, senhaFinal);
            atualizarStmt.setString(4, cpf);

            int rowsUpdated = atualizarStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Usuário atualizado com sucesso!");
            } else {
                System.out.println("Nenhum usuário encontrado com o CPF informado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    private UsuarioModel buscarUsuarioPorCpf(String cpf, UsuarioModel usuario) {
        String buscarSql = "SELECT * FROM usuario WHERE cpf = ?";

        try (Connection conn = Conector.getConnection();
                PreparedStatement buscarStmt = conn.prepareStatement(buscarSql)) {
            buscarStmt.setString(1, cpf);
            try (ResultSet rs = buscarStmt.executeQuery()) {
                if (rs.next()) {
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }
        return usuario;
    }

    public void listarUsuarios() {
        String sql = "SELECT * FROM usuario";
        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            view.listar(rs);
        } catch (SQLException e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }
    }

    public void bloquearUsuario(Scanner scanner) {
        listarUsuarios();
        System.out.println("Digite o CPF do usuário que deseja bloquear: ");
        String cpf = scanner.nextLine();
        String sql = "UPDATE usuario SET status = ? WHERE cpf = ?";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Bloqueado");
            stmt.setString(2, cpf);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Usuário bloqueado com sucesso!");
            } else {
                System.out.println("Nenhum usuário encontrado com o CPF informado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao bloquear usuário: " + e.getMessage());
        }
    }

    @Override
    public boolean exibirTelaInicial(Scanner scanner) {
        int opcao;
        view.exibirMenuInicial();
        opcao = lerOpcaoDoUsuario(scanner);

        if (opcao != 2) {
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
            scanner.nextLine();
            if (opcao != 9) {
                executarOpcaoMenuPrincipal(scanner, opcao);
            }
        } while (opcao != 9);
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
                logado = logar(scanner);
                break;
            case 2:
                System.out.println("Saindo...");
                logado = false;
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
                cadastrar(scanner);
                break;
            case 2:
                listarUsuarios();
                break;
            case 3:
                bloquearUsuario(scanner);
                break;
            case 4:
                atualizarInfo(scanner);
                break;
            case 5:
                fazerReserva(scanner);
                break;
            case 6:
                listarReservas(scanner);
                break;
            case 7:
                cancelarReserva(scanner, model.getCpf());
                break;
            case 8:
                exibirInfo();
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
                break;
        }
    }
}
