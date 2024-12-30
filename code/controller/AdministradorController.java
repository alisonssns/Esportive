package controller;

import model.AdministradorModel;
import view.AdministradorView;
import model.ReservaModel;
import model.UsuarioModel;
import view.UsuarioView;
import utils.AdmUtils;
import utils.CRUD;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
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
            logado = logado ? exibirMenuPrincipal(scanner) : exibirTelaInicial(scanner);
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

        String query = "SELECT * FROM usuario WHERE email = ? AND senha = ? AND tipo = 'admin'";

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
        int opcao = view.listarReservas(scanner);
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
        ArrayList<Object> values = new ArrayList<>();
        int idreserva = view.getId(scanner);

        values.add(idreserva);
        values.add(cpf);

        String query = "UPDATE reserva SET status = 'Cancelada' WHERE idreserva = ? and cpfusuario = ?";

        try {
            crud.update(query, values);
        } catch (SQLException e) {
            System.out.println("Erro ao cancelar a reserva: " + e.getMessage());
        }
    }

    @Override
    public void exibirInfo() {
        view.exibirInfo(model);
    }

    @Override
    public void atualizarInfo(Scanner scanner) {
        listarUsuarios();
        System.out.println("Digite o CPF do usuário que deseja alterar informações: ");
        String cpf = scanner.nextLine();

        UsuarioModel usuario = buscarUsuarioPorCpf(cpf);

        if (usuario != null) {
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
        ArrayList<Object> values = new ArrayList<>();
        String nomeFinal = (novoNome != null && !novoNome.isEmpty()) ? novoNome : usuario.getNome();
        String emailFinal = (novoEmail != null && !novoEmail.isEmpty()) ? novoEmail : usuario.getEmail();
        String senhaFinal = (novaSenha != null && !novaSenha.isEmpty()) ? novaSenha : usuario.getSenha();
        values.add(nomeFinal);
        values.add(emailFinal);
        values.add(senhaFinal);
        values.add(cpf);

        String query = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE cpf = ?";

        try {
            crud.update(query, values);
        } catch (SQLException e) {
            System.out.println("Erro ao alterar usuario: " + e.getMessage());
        }
    }

    private UsuarioModel buscarUsuarioPorCpf(String cpf) {
        ArrayList<Object> values = new ArrayList<>();
        values.add(cpf);

        String query = "SELECT * FROM usuario WHERE cpf = ?";
        UsuarioModel usuario = new UsuarioModel();

        try (ResultSet rs = crud.select(query, values)) {
            if (rs.next()) {
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                return usuario;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }
        return null;
    }

    public void listarUsuarios() {
        String query = "SELECT * FROM usuario";
        ArrayList<Object> values = new ArrayList<>();

        try (ResultSet rs = crud.select(query, values)) {
            view.listar(rs);
        } catch (SQLException e) {
            System.out.println("Erro ao listar usuarios: " + e.getMessage());
        }
    }

    public void bloquearUsuario(Scanner scanner) {
        listarUsuarios();
        System.out.println("Digite o CPF do usuário que deseja bloquear: ");
        String cpf = scanner.nextLine();

        ArrayList<Object> values = new ArrayList<>();
        values.add(cpf);

        String sql = "UPDATE usuario SET status = 'Bloqueado' WHERE cpf = ?";

        try {
            int rowsUpdated = crud.update(sql, values);
            if (rowsUpdated > 0) {
                System.out.println("Usuário bloqueado com sucesso!");
            } else {
                System.out.println("Nenhum usuário encontrado com o CPF informado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao realizar a reserva: " + e.getMessage());
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
