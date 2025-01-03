package controller;

import model.ReservaModel;
import model.UsuarioModel;
import view.UsuarioView;
import utils.UserUtils;
import utils.CRUD;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class UsuarioController implements IUserAdmController {
    private final LocalController localController = new LocalController();
    private final ReservaModel modelReserva = new ReservaModel();
    private final UsuarioModel model = new UsuarioModel();
    private final UsuarioView view = new UsuarioView();
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
        values.clear();
        view.cadastrar(scanner, model);

        String query = "INSERT INTO usuario (cpf, nome, email, senha, tipo) VALUES (?, ?, ?, ?, ?)";
        String queryT = "INSERT INTO telefoneusuario (numero, cpfUsuario) VALUES (?, ?)";

        utils.cadastrar(model, values);

        try {
            crud.insert(query, values);
            utils.telefone(model, values);
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
        values.clear();
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

    public void listarReservas(String cpf) {
        values.clear();
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
        }
    }

    @Override
    public void cancelarReserva(Scanner scanner, String cpf) {
        values.clear();
        listarReservas(cpf);
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
