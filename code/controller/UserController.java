package controller;

import model.ReservaModel;
import model.UserModel;
import view.UserView;
import utils.UserUtils;
import utils.CRUD;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class UserController implements IUserAdmController {
    private final LocalController localController = new LocalController();
    private final EventController eventController = new EventController();
    private final ReservaModel modelReserva = new ReservaModel();
    private final UserModel model = new UserModel();
    private final UserView view = new UserView();
    private final UserUtils utils = new UserUtils();
    private final CRUD crud = new CRUD();
    private final ArrayList<Object> values = new ArrayList<>();

    public void iniciar(Scanner scanner) {
        boolean logado = false;

        while (true) {
            logado = logado ? exibirMenuPrincipal(scanner) : exibirTelaInicial(scanner);
        }
    }

    @Override
    public void cadastrar(Scanner scanner) {
        view.cadastrar(scanner, model);

        if (utils.verficiarCpfExistente(model.getCpf())) {
            System.out.println("Este CPF já está associado a uma conta.");
            return;
        } else if (utils.verificarEmailExistente(model.getEmail())) {
            System.out.println("Este e-mail já está associado a uma conta.");
            return;
        } else if (utils.verificarTelefoneExistente(model.getTelefone())) {
            System.out.println("Este telefone já está associado a uma conta.");
            return;
        }

        String query = "INSERT INTO usuario (cpf, nome, email, senha, tipo, status) VALUES (?, ?, ?, ?, ?, 'Ativo')";
        String queryT = "INSERT INTO telefoneusuario (numero, cpfUsuario) VALUES (?, ?)";

        values.clear();
        utils.cadastrar(model, values);

        try {
            crud.insert(query, values);
            utils.telefone(model, values);
            crud.insert(queryT, values);
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao executar a consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean logar(Scanner scanner) {
        values.clear();
        view.logar(scanner, model);
        utils.logar(model, values);

        String query = "SELECT * FROM usuario WHERE email = ? AND senha = ? AND tipo = 'cli'";

        try (ResultSet rsUsuario = crud.select(query, values)) {
            if (rsUsuario.next()) {
                if(rsUsuario.getString("status") == "Bloqueado"){
                    System.out.println("");
                }
                System.out.println("Seja bem-vindo: " + rsUsuario.getString("nome"));
                utils.loginSuccess(rsUsuario, model);
                addTelefone();
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

    public boolean listarReservas(String cpf) {
        values.clear();
        String query = "SELECT * FROM reserva WHERE cpfUsuario = ?";
        values.add(cpf);

        ResultSet rs = null;
        try {
            rs = crud.select(query, values);
            if (rs != null) {
                view.listarReservas(rs);
                return true;
            } else {
                System.out.println("Nenhuma reserva encontrada para este CPF.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao executar a consulta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void cancelarReserva(Scanner scanner, String cpf) {
        listarReservas(cpf);
        int idreserva = view.getId(scanner);

        values.clear();
        values.add(idreserva);
        values.add(cpf);

        if (!existeReserva(values)) {
            System.out.println("Reserva não encontrada.");
            return;
        }

        String query = "UPDATE reserva SET status = 'CANCELADA' WHERE idreserva = ? and cpfusuario = ?";

        try {
            crud.update(query, values);
        } catch (SQLException e) {
            System.out.println("Erro ao cancelar a reserva: " + e.getMessage());
        }
    }

    private boolean existeReserva(ArrayList<Object> values) {
        String query = "SELECT COUNT(*) FROM reserva WHERE idreserva = ? AND cpfusuario = ?";
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

    @Override
    public void exibirInfo() {
        view.exibirInfo(model);
    }

    @Override
    public void atualizarInfo(Scanner scanner) {
        values.clear();

        view.atualizarInfo(model, scanner);
        utils.atualizarInfo(model, values);

        String query = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE cpf = ?";
        String queryT = "UPDATE telefoneusuario SET numero = ? WHERE cpfUsuario = ?";

        try {
            crud.update(query, values);
        } catch (SQLException e) {
            System.out.println("Erro ao Atualizar Info: " + e.getMessage());
        }

        utils.telefone(model, values);

        try {
            crud.update(queryT, values);
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar Telefone: " + e.getMessage());
        }
    }

    public void cadastrarEndereco(Scanner scanner) {
        values.clear();

        view.cadastrarEndereco(model, scanner);
        utils.cadastrarEndereco(model, values);
        values.add(model.getCpf());
        String query = "UPDATE usuario SET cep = ?, estado = ?, cidade = ?, bairro = ?,  rua = ?, numero = ? WHERE cpf = ?";

        try {
            crud.update(query, values);
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar Endereço: " + e.getMessage());
        }
    }

    public boolean verificarEndereco(String cpf) {
        String query = "select cep from usuario WHERE cpf = ?";
        values.clear();
        values.add(cpf);

        try {
            ResultSet rs = crud.select(query, values);
            if (rs.next()) {
                int cep = rs.getInt("cep");
                if (cep > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar a reserva: " + e.getMessage());
        }
        return false;
    }

    public void fazerReservaEvento(Scanner scanner) {
        eventController.listar();
        int id = view.getId(scanner);

        if (!eventController.verificarVagasEvento(id)) {
            return;
        }

        if (eventController.reservaDuplicada(model.getCpf(), id)) {
            return;
        }

        values.clear();
        values.add(model.getCpf());
        values.add(id);

        String query = "INSERT INTO reservaEvento(cpfUsuario, idEvento, status) values (?,?,'CONFIRMADO')";

        try {
            crud.insert(query, values);
            eventController.reservar(id);
        } catch (SQLException e) {
            System.out.println("Erro ao realizar a reserva: " + e.getMessage());
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
        if (!verificarEndereco(model.getCpf())) {
            System.out.println(
                    "Você não tem um endereço cadastrado, você será redirecionado a tela de cadastro de endereço!");
            cadastrarEndereco(scanner);
        }

        int opcao;
        do {
            view.exibirMenuPrincipal();
            opcao = lerOpcaoDoUsuario(scanner);
            if (opcao != 7) {
                executarOpcaoMenuPrincipal(scanner, opcao);
            }
        } while (opcao != 7);

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
            case 6:
                fazerReservaEvento(scanner);
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
                break;
        }
    }

}