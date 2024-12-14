package view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import model.LocalModel;

public class LocalView {

    public LocalModel local;

    public void cadastrar(Scanner scanner, LocalModel local) {
        System.out.print("Nome: ");
        local.setNome(scanner.nextLine());
        System.out.print("Cep: ");
        local.setCep(scanner.nextLine());
        System.out.print("Tipo: ");
        local.setTipo(scanner.nextLine());
        System.out.print("Capacidade: ");
        local.setCapacidade(scanner.nextInt());
        System.out.print("Numero: ");
        local.setNumero(scanner.nextInt());
    }

    public void listar(ResultSet rs) throws SQLException {
        System.out.printf("| %-5s | %-30s | %-20s | %-10s | %-10s | %-6s |%n",
                "ID", "Nome", "Tipo", "Capacidade", "CEP", "Número");
        System.out.println("---------------------------------------------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("| %-5s | %-30s | %-20s | %-10s | %-10s | %-6s |%n",
                rs.getInt("idLocal"), rs.getString("nome"), rs.getString("tipo"), rs.getInt("capacidade"), rs.getString("cep"), rs.getInt("numero"));
        }
    }
}
