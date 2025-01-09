package controller;

import view.AdminView;
import view.UserView;
import model.AdminModel;
import model.ReservaModel;
import model.UserModel;
import utils.AdminUtils;
import utils.CRUD;
import utils.UserUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.Scanner;

public class AdminController implements IUserAdmController {
    private UserController usuarioController = new UserController();
    private LocalController localController = new LocalController();
    private EventController eventoController = new EventController();
    private final ReservaModel reservaModel = new ReservaModel();
    private AdminModel model = new AdminModel();
    private AdminView view = new AdminView();
    private AdminUtils utils = new AdminUtils();
    private CRUD crud = new CRUD();
    private final ArrayList<Object> values = new ArrayList<>();

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

    public void cadastrarEvento(Scanner scanner) {
        eventoController.cadastrar(scanner);
    }

    public void cadastrarLocal(Scanner scanner) {
        localController.cadastrar(scanner);
    }

    public void removerLocal(Scanner scanner) {
        values.clear();
        localController.listar();
        int id = view.getId(scanner);
        values.add(id);

        String query = "DELETE FROM local where idLocal = ?";

        try {
            crud.delete(query, values);
        } catch (SQLException e) {
            System.out.println("Erro ao deletar local: " + e.getMessage());
        }
    }

    @Override
    public boolean logar(Scanner scanner) {
        values.clear();
        view.logar(scanner, model);
        utils.logar(model, values);

        String query = "SELECT * FROM usuario WHERE email = ? AND senha = ? AND tipo = 'admin'";

        try (ResultSet rsUsuario = crud.select(query, values)) {
            if (rsUsuario.next()) {
                System.out.println("Seja bem-vindo: " + rsUsuario.getString("nome"));
                addTelefone();
                utils.loginSuccess(rsUsuario, model);
                return true;
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao logar: " + e.getMessage());
        }

        return false;
    }

    private void addTelefone() {
        values.clear();
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
        values.clear();
        UserView userView = new UserView();

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
            scanner.nextLine();
            String cpf = view.getCpf(scanner);
            usuarioController.listarReservas(cpf);

        } else if (opcao == 2) {
            localController.listar();
            int id = view.getId(scanner);
            localController.listarReservas(id);

        } else {
            System.out.println("Opção inválida! Tente novamente.");
            scanner.nextLine();
        }
    }

    public void cancelarReserva(Scanner scanner) {
        int opcao = view.listarReservas(scanner);

        if (opcao == 1) {
            listarUsuarios();
            scanner.nextLine();
            String cpf = view.getCpf(scanner);
            usuarioController.cancelarReserva(scanner, cpf);
        } else if (opcao == 2) {
            localController.listar();
            int id = view.getId(scanner);
            localController.cancelarReserva(scanner, id);
        } else {
            System.out.println("Opção inválida! Tente novamente.");
            scanner.nextLine();
        }
    }

    @Override
    public void exibirInfo() {
        view.exibirInfo(model);
    }

    @Override
    public void atualizarInfo(Scanner scanner) {
        UserView usuarioView = new UserView();
        UserUtils userUtils = new UserUtils();
        values.clear();
        listarUsuarios();
        String cpf = view.getCpf(scanner);
        UserModel usuarioModel = buscarUsuarioPorCpf(cpf);
        if (usuarioModel == null) {
            return;
        }

        usuarioView.atualizarInfo(usuarioModel, scanner);
        userUtils.atualizarInfo(usuarioModel, values);

        String query = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE cpf = ?";
        String queryT = "UPDATE telefoneusuario SET numero = ? WHERE cpfUsuario = ?";

        try {
            crud.update(query, values);
        } catch (SQLException e) {
            System.out.println("Erro ao Atualizar Info: " + e.getMessage());
        }

        utils.telefone(usuarioModel, values);

        try {
            crud.update(queryT, values);
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar Telefone: " + e.getMessage());
        }
    }

    private UserModel buscarUsuarioPorCpf(String cpf) {
        values.clear();
        values.add(cpf);

        String query = "SELECT * FROM usuario WHERE cpf = ?";

        try (ResultSet rs = crud.select(query, values)) {
            if (rs.next()) {
                UserModel usuario = new UserModel();
                utils.setUsuario(usuario, rs);
                return usuario;
            } else {
                System.out.println("Usuário não encontrado.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }
        return null;
    }

    public void listarUsuarios() {
        values.clear();
        String query = "SELECT * FROM usuario where tipo = 'cli'";

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
            if (opcao != 12) {
                executarOpcaoMenuPrincipal(scanner, opcao);
            }
        } while (opcao != 12);
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
                cadastrarLocal(scanner);
                break;
            case 3:
                removerLocal(scanner);
                break;
            case 4:
                listarUsuarios();
                break;
            case 5:
                bloquearUsuario(scanner);
                break;
            case 6:
                atualizarInfo(scanner);
                break;
            case 7:
                fazerReserva(scanner);
                break;
            case 8:
                listarReservas(scanner);
                break;
            case 9:
                cancelarReserva(scanner);
                break;
            case 10:
                exibirInfo();
                break;
            case 11:
                cadastrarEvento(scanner);
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
                break;
        }
    }
}
