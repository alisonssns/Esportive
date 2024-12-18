package view;

import controller.UsuarioController;
import model.GenericModel;
import model.ReservaModel;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.regex.Pattern;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.Time;

public class UsuarioView {

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

    public void cadastrar(Scanner scanner, GenericModel usuario) {

        do {
            System.out.print("Cpf: ");
            usuario.setCpf(scanner.nextLine());
        } while (!validarCpf(usuario));

        while (true) {
            System.out.print("Nome: ");
            usuario.setNome(scanner.nextLine());
            if (!(usuario.getNome().length() <= 3) && !(usuario.getNome().matches(".*\\d.*"))) {
                break;
            }else{
                System.out.println("Nome inválido, tente novamente!");
            }
        }

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

    public void logar(Scanner scanner, GenericModel usuario) {
        System.out.print("Email: ");
        usuario.setEmail(scanner.nextLine());
        System.out.print("Senha: ");
        usuario.setSenha(scanner.nextLine());
    }

    public void logSuccess(ResultSet rs, GenericModel model) throws SQLException {
        model.setCpf(rs.getString("cpf"));
        model.setNome(rs.getString("nome"));
        model.setRua(rs.getString("rua"));
        model.setBairro(rs.getString("bairro"));
        model.setCidade(rs.getString("cidade"));
        model.setCep(rs.getString("cep"));
        model.setEstado(rs.getString("estado"));
        model.setNumero(rs.getString("numero"));
    }

    public void showInterface(GenericModel model, Scanner scanner, UsuarioController controller) {
        int opcao;
        do {
            System.out.println("\n--- Menu Usuario ---");
            System.out.println("1. Fazer reserva");
            System.out.println("2. Listar Reservas");
            System.out.println("3. Cancelar Reserva");
            System.out.println("4. Informações pessoais");
            System.out.println("5. Alterar informações");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        controller.fazerReserva(scanner, model);
                        break;
                    case 2:
                        controller.listarReservas(scanner, model);
                        break;
                    case 3:
                        controller.cancelarReserva(scanner, model);
                        break;
                    case 4:
                        controller.mostraInfo(model);
                        break;
                    case 5:
                        controller.atualizarInfo(scanner, model);
                        break;
                    case 6:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                        break;
                }
            } else {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                scanner.nextLine();
                opcao = 0;
            }
        } while (opcao != 6);
    }

    public void fazerReserva(Scanner scanner, ReservaModel reserva) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        // Data e hora atuais
        java.util.Date now = new java.util.Date();
        java.sql.Date currentDate = new java.sql.Date(now.getTime());
        Time currentTime = new Time(now.getTime());

        System.out.println("Qual o local gostaria de fazer a reserva? Escolha pelo ID:");
        reserva.setIdLocal(scanner.nextInt());
        scanner.nextLine();

        while (true) {
            try {
                System.out.print("Escolha uma data para sua reserva (dd-MM-yyyy): ");
                String dataInput = scanner.nextLine();
                java.util.Date utilDate = dateFormat.parse(dataInput);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                // Verifica se a data é anterior à data atual
                if (sqlDate.before(currentDate)) {
                    System.out.println("A data não pode ser anterior à data atual. Tente novamente.");
                    continue;
                }

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

                // Verifica se o horário é anterior ao horário atual se a data for igual à atual
                if (reserva.getData().equals(currentDate) && horario.before(currentTime)) {
                    System.out.println("O horário não pode ser anterior ao horário atual. Tente novamente.");
                    continue;
                }

                reserva.setHorario(horario);
                break;
            } catch (ParseException e) {
                System.out.println("Formato de horário inválido. Tente novamente.");
            }
        }

        reserva.setStatus("PENDENTE");
    }

    public void listarReservas(ResultSet rs) throws SQLException {
        System.out.printf("| %-5s | %-10s | %-8s | %-10s | %-5s |%n", "ID", "Data", "Horario", "Status", "IdLocal");
        while (rs.next()) {
            System.out.printf("| %-5s | %-10s | %-8s | %-10s | %-7s |%n",
                    rs.getInt("idreserva"),
                    rs.getDate("data"),
                    rs.getTime("horario"),
                    rs.getString("status"),
                    rs.getInt("idLocal"));
        }
    }

    public boolean validarCpf(GenericModel model) {
        String cpf = model.getCpf();
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            System.out.println("Este Cpf é inválido, tente novamente!");
            return false;
        }
        if (cpf.chars().distinct().count() == 1) {
            System.out.println("Este Cpf é inválido, tente novamente!");
            return false;
        }
        int fDigit = calcularDigitoVerificador(cpf, 10, 9);
        int sDigit = calcularDigitoVerificador(cpf, 11, 10);
        if (!(fDigit == (cpf.charAt(9) - '0') && sDigit == (cpf.charAt(10) - '0'))) {
            System.out.println("Este Cpf é inválido, tente novamente!");
            return false;
        } else {
            return true;
        }
    }

    private int calcularDigitoVerificador(String cpf, int pesoInicial, int tamanho) {
        int soma = 0;

        for (int i = 0; i < tamanho; i++) {
            soma += (cpf.charAt(i) - '0') * pesoInicial;
            pesoInicial--;
        }

        int resto = 11 - (soma % 11);
        return (resto >= 10) ? 0 : resto;
    }
}
