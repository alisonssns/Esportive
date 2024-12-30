package view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import model.AdministradorModel;

public class AdministradorView implements IView {

    public void logar(Scanner scanner, AdministradorModel model) {
        System.out.print("Email: ");
        model.setEmail(scanner.nextLine());

        System.out.print("Senha: ");
        model.setSenha(scanner.nextLine());
    }

    public int getId(Scanner scanner) {
        System.out.println("Digite o ID da reserva que deseja cancelar: ");
        int id = scanner.nextInt();
        return id;
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
        int opcao = scanner.nextInt();
        return opcao;
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
        System.out.println("2. Listar Usuários");
        System.out.println("3. Bloquear Usuário");
        System.out.println("4. Atualizar Usuário");
        System.out.println("5. Fazer Reserva");
        System.out.println("6. Listar Reservas");
        System.out.println("7. Cancelar Reserva");
        System.out.println("8. Exibir Info");
        System.out.println("9. Sair");
        System.out.print("Escolha uma opção: ");
    }

    public void exibirInfo(AdministradorModel model) {
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
