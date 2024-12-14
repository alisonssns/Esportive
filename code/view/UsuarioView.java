package view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Pattern;
import controller.UsuarioController;
import model.ReservaModel;
import model.UsuarioModel;

public class UsuarioView {

    private static final String PADRAO_CPF = "^\\d{9}\\-\\d{2}$";
    private static final String PADRAO_EMAIL = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
    private static final String PADRAO_TELEFONE = "^\\(\\d{2}\\) 9\\d{4}-\\d{4}$";

    private String lerEntradaComValidacao(Scanner scanner, String mensagem, String padrao, String erro) {
        String entrada;
        while (true) {
            System.out.print(mensagem);
            entrada = scanner.nextLine();
            if (Pattern.matches(padrao, entrada)) {
                break;
            }
            System.out.println(erro);
        }
        return entrada;
    }

    public void cadastrar(Scanner scanner, UsuarioModel usuario) {

        usuario.setCpf(lerEntradaComValidacao(
                scanner,
                "CPF (xxxxxxxxx-xx): ",
                PADRAO_CPF,
                "O CPF inserido não segue o padrão. Tente novamente!"));

        System.out.print("Nome: ");
        usuario.setNome(scanner.nextLine());

        usuario.setEmail(lerEntradaComValidacao(
                scanner,
                "Email (user@gmail.com): ",
                PADRAO_EMAIL,
                "O email inserido não é válido. Tente novamente!"));

        System.out.print("Senha: ");
        usuario.setSenha(scanner.nextLine());

        usuario.setTelefone(lerEntradaComValidacao(
                scanner,
                "Telefone ((xx) 9xxxx-xxxx): ",
                PADRAO_TELEFONE,
                "O telefone inserido não segue o padrão. Tente novamente!"));
    }

    public void logar(Scanner scanner, UsuarioModel usuario) {
        System.out.print("Email: ");
        usuario.setEmail(scanner.nextLine());
        System.out.print("Senha: ");
        usuario.setSenha(scanner.nextLine());
    }

    public void logSuccess(ResultSet rs, UsuarioModel model) throws SQLException {
        model.setCpf(rs.getString("cpf"));
        model.setNome(rs.getString("nome"));
        model.setRua(rs.getString("rua"));
        model.setBairro(rs.getString("bairro"));
        model.setCidade(rs.getString("cidade"));
        model.setCep(rs.getString("cep"));
        model.setEstado(rs.getString("estado"));
        model.setNumero(rs.getString("numero"));
    }

    public void showInterface(UsuarioModel model, Scanner scanner, UsuarioController controller) {
        int opcao;
        do {
            System.out.println("\n--- Menu Usuario ---");
            System.out.println("1. Fazer reserva");
            System.out.println("2. Listar Reservas");
            System.out.println("3. Informações pessoais");
            System.out.println("4. Alterar informações");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    controller.fazerReserva(scanner, model);
                    break;
            }
        } while (opcao != 5);
    }

    public void fazerReserva(Scanner scanner, ReservaModel reserva) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        System.out.println("Qual o local gostaria de fazer a reserva? Escolha pelo ID:");
        reserva.setIdLocal(scanner.nextInt());
        scanner.nextLine();

        while (true) {
            try {
                System.out.print("Escolha uma data para sua reserva (dd-MM-yyyy): ");
                String dataInput = scanner.nextLine();
                java.util.Date utilDate = dateFormat.parse(dataInput);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                reserva.setData(sqlDate);
                break;
            } catch (ParseException e) {
                System.out.println("Formato de data inválido. Tente novamente.");
            }
        }

        while (true) {
            try {
                System.out.print("Escolha o horário da reserva (HH:mm): ");
                String horarioInput = scanner.nextLine();
                Time horario = new Time(timeFormat.parse(horarioInput).getTime());
                reserva.setHorario(horario);
                break;
            } catch (ParseException e) {
                System.out.println("Formato de horário inválido. Tente novamente.");
            }
        }

        reserva.setStatus("PENDENTE");
    }

}
