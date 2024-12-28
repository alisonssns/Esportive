package view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import model.AdministradorModel;

public class AdministradorView implements IView {
    Scanner scanner = new Scanner(System.in);

    public void logar(Scanner scanner, AdministradorModel model){
        System.out.print("Email: ");
        model.setEmail(scanner.nextLine());

        System.out.print("Senha: ");
        model.setSenha(scanner.nextLine());
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

    public int listarReservas(){
        System.out.println("Quer listar reservas de usuario ou local?");
        System.out.println("1 - Usuario");
        System.out.println("2 - Local");
        int opcao = scanner.nextInt();
        return opcao;
    }
}
