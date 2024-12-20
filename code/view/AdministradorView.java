package view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import model.UsuarioModel;

public class AdministradorView {
    private Scanner scanner;

    public AdministradorView() {
        scanner = new Scanner(System.in);
    }

    public int exibirMenu() {
        System.out.println("\n--- Menu do Administrador ---");
        System.out.println("1. Adicionar Usuário");
        System.out.println("2. Listar Usuários");
        System.out.println("3. Atualizar Usuário");
        System.out.println("4. Remover Usuário");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
        return scanner.nextInt();
    }

    public void listar(ResultSet rs) throws SQLException {
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.printf("| %-12s | %-30s | %-30s | %-15s |%n",
                "Cpf", "Nome", "Email", "Senha");
        System.out.println("---------------------------------------------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("| %-12s | %-30s | %-30s | %-15s |%n",
                rs.getString("cpf"), rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
        }
    }
}
