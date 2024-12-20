package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import view.AdministradorView;
import view.UsuarioView;
import model.AdministradorModel;
import model.ReservaModel;
import model.UsuarioModel;

public class AdministradorController implements IUserAdmController {
    private AdministradorView view = new AdministradorView();
    private AdministradorModel model = new AdministradorModel();
    private LocalController localController = new LocalController();  
    private final ReservaModel modelReserva = new ReservaModel();

    @Override
    public void cadastrar(Scanner scanner) {
        // Lógica de cadastro de um novo usuário
        System.out.println("Cadastro de novo usuário: ");
        System.out.print("Digite o nome: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Digite o email: ");
        String email = scanner.nextLine();
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        String sql = "INSERT INTO usuario (nome, cpf, email, senha, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, email);
            stmt.setString(4, senha);
            stmt.setString(5, "Ativo");

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Novo usuário cadastrado com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    
    @Override
    public boolean logar(Scanner scanner) {
        // Lógica de login de administrador
        System.out.println("Digite seu CPF: ");
        String cpf = scanner.nextLine();
        System.out.println("Digite sua senha: ");
        String senha = scanner.nextLine();

        String sql = "SELECT * FROM usuario WHERE cpf = ? AND senha = ? and tipo = 'admin'";

        try (Connection conn = Conector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login realizado com sucesso!");
                    return true;
                } else {
                    System.out.println("Credenciais inválidas.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao realizar login: " + e.getMessage());
            return false;
        }
    }


    // Método para listar todos os usuários
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

    // Método para bloquear um usuário
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

    // Método para atualizar informações de um usuário
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
    public void fazerReserva(Scanner scanner) {
        UsuarioView userView = new UsuarioView();
        // Listar locais disponíveis
        localController.listar();
    
        // Solicitar o CPF do usuário para fazer a reserva
        System.out.println("Digite o CPF do usuário para o qual deseja fazer a reserva: ");
        String cpfUsuario = scanner.nextLine();
    
        // Obter a data, horário e local da reserva
        userView.fazerReserva(scanner, modelReserva);
    
        // Preparar a SQL para inserir a reserva
        String sql = "INSERT INTO reserva (cpfUsuario, data, horario, status, idLocal) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Setar os parâmetros para a SQL
            stmt.setString(1, cpfUsuario);  // Usar o CPF do usuário fornecido pelo administrador
            stmt.setDate(2, modelReserva.getData());
            stmt.setTime(3, modelReserva.getHorario());
            stmt.setString(4, modelReserva.getStatus());
            stmt.setInt(5, modelReserva.getIdLocal());
    
            // Executar a SQL para inserir a reserva
            stmt.executeUpdate();
    
            System.out.println("Reserva realizada com sucesso para o usuário: " + cpfUsuario);
    
        } catch (SQLException e) {
            System.out.println("Erro ao realizar a reserva: " + e.getMessage());
        }
    }
    

    @Override
    public void listarReservas(Scanner scanner) {
        localController.listar();
        System.out.println("Digite o ID do local para listar as reservas: ");
        int idLocal = scanner.nextInt();
        localController.listarReservas(idLocal);
    }

    @Override
    public void cancelarReserva(Scanner scanner) {
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
}
