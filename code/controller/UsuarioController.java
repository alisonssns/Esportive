package controller;

import model.ReservaModel;
import model.UsuarioModel;
import utils.CRUD;
import utils.UserUtils;
import view.UsuarioView;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class UsuarioController implements IUserAdmController {
    private final UsuarioView view = new UsuarioView();
    private final UsuarioModel model = new UsuarioModel();
    private final LocalController localController = new LocalController();
    private final ReservaModel modelReserva = new ReservaModel();
    private final CRUD crud = new CRUD();
    private final UserUtils utils = new UserUtils();

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
        ArrayList<Object> values = new ArrayList<>();
        view.cadastrar(scanner, model);

        String query = "INSERT INTO usuario (cpf, nome, email, senha, tipo) VALUES (?, ?, ?, ?, ?)";
        String queryT = "INSERT INTO telefoneusuario (numero, cpfUsuario) VALUES (?, ?)";

        utils.cadastrar(model, values);

        try {
            crud.insert(query, values);
            utils.cadastrarTelefone(model, values);
            crud.insert(queryT, values);
        } catch (SQLException e) {
            System.err.println("Erro ao executar a consulta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Usuário cadastrado com sucesso!");
        }
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
        ArrayList<Object> values = new ArrayList<>();
        localController.listar();

        view.fazerReserva(scanner, modelReserva);
        utils.fazerReserva(model.getCpf(), modelReserva, values);

        String query = "INSERT INTO reserva (cpfUsuario, data, horario_inicio, horario_fim, status, idLocal, data_registro, hora_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            crud.insert(query, values);
        } catch (SQLException e) {
            System.out.println("Erro ao realizar a reserva: " + e.getMessage());
        }
    }

    public void listarReservas(String cpf) {
        ArrayList<Object> values = new ArrayList<>();
        String query = "SELECT * FROM reserva WHERE cpfUsuario = ?";
        values.add(cpf);

        ResultSet rs = null;
        try {
            rs = crud.select(query, values);
            if (rs != null) {
                view.listarReservas(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao executar a consulta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
