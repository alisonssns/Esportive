package view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import model.AdminModel;
import validations.UserValidation;

public class AdminView implements IView {
    UserValidation validation = new UserValidation();

    public void logar(Scanner scanner, AdminModel model) {
        model.setEmail(lerEmail(scanner));
        model.setSenha(lerSenha(scanner));
    }

    public String lerEmail(Scanner scanner) {
        final String PADRAO_EMAIL = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        String email;
        do {
            System.out.print("Email (Ex: user@gmail.com): ");
            email = scanner.nextLine();
        } while (!validation.validarEmail(email, PADRAO_EMAIL));
        return email;
    }

    public String lerSenha(Scanner scanner) {
        String senha;
        do {
            System.out.print("Senha: ");
            senha = scanner.nextLine();
        } while (!validation.validarSenha(senha));
        return senha;
    }

    public int getId(Scanner scanner) {
        System.out.print("Digite o ID:  ");
        if (scanner.hasNextInt()) {
            int id = scanner.nextInt();
            return id;
        } else {
            return 0;
        }
    }

    public String getCpf(Scanner scanner) {
        System.out.print("Digite o cpf do Usuario: ");
        String cpf = scanner.nextLine();
        return cpf;
    }

    public void listar(ResultSet rs) throws SQLException {
        String line = "---------------------------------------------------------------------------------------------------";
        System.out.println(line);
        System.out.printf("| %-12s | %-30s | %-30s | %-15s |%n",
                "Cpf", "Nome", "Email", "Senha");
        System.out.println(line);

        while (rs.next()) {
            System.out.printf("| %-12s | %-30s | %-30s | %-15s |%n",
                    rs.getString("cpf"), rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
        }
        System.out.println(line);
    }

    public int listarReservas(Scanner scanner) {
        System.out.println("Quer listar reservas de usuario ou local?");
        System.out.println("1 - Usuario");
        System.out.println("2 - Local");
        System.out.print("Escolha: ");
        if (scanner.hasNextInt()) {
            int opcao = scanner.nextInt();
            return opcao;
        } else {
            return 0;
        }
    }

    @Override
    public void exibirMenuInicial() {
        System.out.println("\n--- Menu Usuario ---");
        System.out.println("1. Logar");
        System.out.println("2. Sair");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    public void exibirMenuPrincipal() {
        System.out.println("\n--- Menu do Administrador ---");
        System.out.println("1. Adicionar Usuário");
        System.out.println("2. Adicionar Local");
        System.out.println("3. Remover Local");
        System.out.println("4. Listar Usuários");
        System.out.println("5. Bloquear Usuário");
        System.out.println("6. Atualizar Usuário");
        System.out.println("7. Fazer Reserva");
        System.out.println("8. Listar Reservas");
        System.out.println("9. Cancelar Reserva");
        System.out.println("10. Exibir Info");
        System.out.println("11. Cadastrar Evento");
        System.out.println("12. Sair");
        System.out.print("Escolha uma opção: ");
    }

    public void exibirInfo(AdminModel model) {
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
}
