package controller;

import model.LocalModel;
import view.LocalView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LocalController {
    Scanner scanner = new Scanner(System.in);

    private LocalView view = new LocalView();
    private LocalModel model = new LocalModel();

    public void cadastrar(Scanner scanner) {
        view.cadastrar(scanner, model);

        String sql = "Insert into local (nome, tipo, capacidade, cep, numero) values (?,?,?,?,?)";

        try (Connection conn = Conector.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, model.getNome());
                stmt.setString(2, model.getTipo());
                stmt.setInt(3, model.getCapacidade());
                stmt.setString(4, model.getCep());
                stmt.setInt(5, model.getNumero());
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    public void listar() {
        String sql = "SELECT * FROM local";

        try (Connection conn = Conector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();) {
            view.listar(rs);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        ;
    }
}
