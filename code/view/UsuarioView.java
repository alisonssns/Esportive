package view;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.regex.Pattern;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.Time;

import controller.UsuarioController;
import model.UsuarioModel;
import model.ReservaModel;

public class UsuarioView {

    private static final String PADRAO_EMAIL = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
    private static final String PADRAO_TELEFONE = "^\\(\\d{2}\\) 9\\d{4}-\\d{4}$";

    public void cadastrar(Scanner scanner, UsuarioModel usuario) {
        do {
            System.out.print("Cpf: ");
            usuario.setCpf(scanner.nextLine());
        } while (!validarCpfUsuario(usuario));

        usuario.setNome(lerNomeUsuario(scanner));

        usuario.setEmail(lerEntradaComValidacaoRegex(scanner, "Email (user@gmail.com): ", PADRAO_EMAIL,
                "O email inserido não é válido. Tente novamente!"));

        System.out.print("Senha: ");
        usuario.setSenha(scanner.nextLine());

        usuario.setTelefone(lerEntradaComValidacaoRegex(scanner, "Telefone ((xx) 9xxxx-xxxx): ", PADRAO_TELEFONE,
                "O telefone inserido não segue o padrão. Tente novamente!"));
    }

    public void logar(Scanner scanner, UsuarioModel usuario) {
        usuario.setEmail(scanner.nextLine());
        usuario.setSenha(scanner.nextLine());
    }

    public void logSuccess(ResultSet rs, UsuarioModel model) throws SQLException {
        model.setCpf(rs.getString("cpf"));
        model.setNome(rs.getString("nome"));
        model.setRua(rs.getString("rua"));
        model.setBairro(rs.getString("bairro"));
        model.setCidade(rs.getString("cidade"));
        model.setCep(rs.getInt("cep"));
        model.setEstado(rs.getString("estado"));
        model.setNumero(rs.getInt("numero"));
    }

    public boolean exibirTelaInicial(UsuarioModel model, Scanner scanner, UsuarioController controller) {
        int opcao;
        exibirMenuInicial();
        opcao = lerOpcaoDoUsuario(scanner);

        if (opcao != 6) {
            return (executarOpcaoTelaInicial(scanner, controller, model, opcao));
        } else {
            return false;
        }
    }

    public void exibirMenuPrincipal(UsuarioModel model, Scanner scanner, UsuarioController controller) {
        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = lerOpcaoDoUsuario(scanner);

            if (opcao != 6) {
                executarOpcaoMenuPrincipal(scanner, controller, model, opcao);
            }
        } while (opcao != 6);
    }

    private void exibirMenuInicial() {
        System.out.println("\n--- Menu Usuario ---");
        System.out.println("1. Cadastrar");
        System.out.println("2. Logar");
        System.out.println("3. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n--- Menu Usuario ---");
        System.out.println("1. Fazer reserva");
        System.out.println("2. Listar Reservas");
        System.out.println("3. Cancelar Reserva");
        System.out.println("4. Informações pessoais");
        System.out.println("5. Alterar informações");
        System.out.println("6. Sair");
        System.out.print("Escolha uma opção: ");
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

    private boolean executarOpcaoTelaInicial(Scanner scanner, UsuarioController controller, UsuarioModel model,
            int opcao) {
        boolean logado = false;
        switch (opcao) {
            case 1:
                controller.cadastrar(scanner);
                break;
            case 2:
                logado = controller.logar(scanner);
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
                break;
        }
        return logado;
    }

    private void executarOpcaoMenuPrincipal(Scanner scanner, UsuarioController controller, UsuarioModel model,
            int opcao) {
        switch (opcao) {
            case 1:
                controller.fazerReserva(scanner);
                break;
            case 2:
                controller.listarReservas(scanner);
                break;
            case 3:
                controller.cancelarReserva(scanner);
                break;
            case 4:
                controller.exibirInfo();
                break;
            case 5:
                controller.atualizarInfo(scanner);
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
                break;
        }
    }

    public void fazerReserva(Scanner scanner, ReservaModel reserva) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        java.util.Date now = new java.util.Date();
        java.sql.Date currentDate = new java.sql.Date(now.getTime());
        Time currentTime = new Time(now.getTime());

        System.out.println("Qual o local gostaria de fazer a reserva? Escolha pelo ID:");
        reserva.setIdLocal(scanner.nextInt());
        scanner.nextLine();

        reserva.setData(obterData(scanner, dateFormat, currentDate));

        reserva.setHorario(obterHorario(scanner, timeFormat, currentDate, currentTime));

        reserva.setStatus("PENDENTE");
    }

    private java.sql.Date obterData(Scanner scanner, SimpleDateFormat dateFormat, java.sql.Date currentDate) {
        java.sql.Date sqlDate;
        while (true) {
            try {
                System.out.print("Escolha uma data para sua reserva (dd-MM-yyyy): ");
                String dataInput = scanner.nextLine();
                java.util.Date utilDate = dateFormat.parse(dataInput);
                sqlDate = new java.sql.Date(utilDate.getTime());

                if (sqlDate.before(currentDate)) {
                    System.out.println("A data não pode ser anterior à data atual. Tente novamente.");
                    continue;
                }
                break;
            } catch (ParseException e) {
                System.out.println("Formato de data inválido. Tente novamente.");
            }
        }
        return sqlDate;
    }

    private Time obterHorario(Scanner scanner, SimpleDateFormat timeFormat, java.sql.Date currentDate,
            Time currentTime) {
        Time horario;
        while (true) {
            try {
                System.out.print("Escolha o horário da reserva (HH:mm): ");
                String horarioInput = scanner.nextLine();
                horario = new Time(timeFormat.parse(horarioInput).getTime());

                if (horario.before(currentTime) && currentDate.equals(new java.sql.Date(System.currentTimeMillis()))) {
                    System.out.println("O horário não pode ser anterior ao horário atual. Tente novamente.");
                    continue;
                }
                break;
            } catch (ParseException e) {
                System.out.println("Formato de horário inválido. Tente novamente.");
            }
        }
        return horario;
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

    private String lerEntradaComValidacaoRegex(Scanner scanner, String mensagem, String padrao, String erro) {
        String entrada;
        while (true) {
            System.out.print(mensagem);
            entrada = scanner.nextLine();
            if (Pattern.matches(padrao, entrada)) {
                return entrada;
            }
            System.out.println(erro);
        }
    }

    private String lerNomeUsuario(Scanner scanner) {
        String nome;
        while (true) {
            System.out.print("Nome: ");
            nome = scanner.nextLine();
            if (nome.length() > 3 && !nome.matches(".*\\d.*")) {
                break;
            }
            System.out.println("Nome inválido, tente novamente!");
        }
        return nome;
    }

    public boolean validarCpfUsuario(UsuarioModel model) {
        String cpf = model.getCpf();
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            System.out.println("Este Cpf é inválido, tente novamente!");
            return false;
        }
        if (cpf.chars().distinct().count() == 1) {
            System.out.println("Este Cpf é inválido, tente novamente!");
            return false;
        }
        int fDigit = calcularDigitoVerificadorCpf(cpf, 10, 9);
        int sDigit = calcularDigitoVerificadorCpf(cpf, 11, 10);
        if (!(fDigit == (cpf.charAt(9) - '0') && sDigit == (cpf.charAt(10) - '0'))) {
            System.out.println("Este Cpf é inválido, tente novamente!");
            return false;
        }
        return true;
    }

    private int calcularDigitoVerificadorCpf(String cpf, int pesoInicial, int tamanho) {
        int soma = 0;

        for (int i = 0; i < tamanho; i++) {
            soma += (cpf.charAt(i) - '0') * pesoInicial;
            pesoInicial--;
        }

        int resto = 11 - (soma % 11);
        return (resto >= 10) ? 0 : resto;
    }

    public boolean validarSenhaUsuario(String senha) {
        if (senha == null || senha.length() < 8) {
            System.out.println("A senha deve ter pelo menos 8 caracteres. Tente novamente.");
            return false;
        }
        return true;
    }
}
