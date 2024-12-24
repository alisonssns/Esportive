package view;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.text.ParseException;
import java.util.regex.Pattern;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Scanner;

import controller.LocalController;
import model.UsuarioModel;
import model.LocalModel;
import model.ReservaModel;

public class UsuarioView implements IView {

    // Constantes para validação de entrada
    private static final String PADRAO_EMAIL = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
    private static final String PADRAO_TELEFONE = "^\\(\\d{2}\\) 9\\d{4}-\\d{4}$";

    // ================================
    // Métodos de Cadastro e Login
    // ================================

    public void cadastrar(Scanner scanner, UsuarioModel usuario) {

        usuario.setCpf(validarCpfUsuario(scanner));

        usuario.setNome(lerNomeUsuario(scanner));

        usuario.setEmail(lerEntradaComValidacaoRegex(scanner, "Email (EX: user@gmail.com): ", PADRAO_EMAIL,
                "O email inserido não é válido. Tente novamente!"));

        usuario.setSenha(lerSenhaUsuario(scanner));

        usuario.setTelefone(lerEntradaComValidacaoRegex(scanner, "Telefone (EX: (xx) 9xxxx-xxxx: ) ", PADRAO_TELEFONE,
                "O telefone inserido não segue o padrão. Tente novamente!"));
    }

    public void logar(Scanner scanner, UsuarioModel usuario) {
        System.out.print("Insira seu email: ");
        usuario.setEmail(scanner.nextLine());
        System.out.print("Insira sua Senha: ");
        usuario.setSenha(scanner.nextLine());
    }

    // ================================
    // Métodos de Reserva
    // ================================
    public void listarReservas(ResultSet rs) throws SQLException {
        String line = "-------------------------------------------------------------------";
        System.out.println(line);
        System.out.printf("| %-5s | %-10s | %-8s | %-8s | %-10s | %-5s |%n", "ID", "Data", "Inicio",
                "Fim", "Status", "IdLocal");
        System.out.println(line);
        while (rs.next()) {
            System.out.printf("| %-5s | %-10s | %-8s | %-8s | %-10s | %-7s |%n",
                    rs.getInt("idreserva"),
                    rs.getDate("data"),
                    rs.getTime("horario_inicio"),
                    rs.getTime("horario_fim"),
                    rs.getString("status"),
                    rs.getInt("idLocal"));
        }
        System.out.println(line);
    }

    public void fazerReserva(Scanner scanner, ReservaModel reserva) {
        LocalController localController = new LocalController();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        java.util.Date now = new java.util.Date();
        java.sql.Date currentDate = new java.sql.Date(now.getTime());
        Time currentTime = new Time(now.getTime());

        System.out.println("Qual o local gostaria de fazer a reserva? Escolha pelo ID:");
        reserva.setIdLocal(scanner.nextInt());
        scanner.nextLine();

        LocalModel localModel = localController.infoLocal(reserva.getIdLocal());

        reserva.setData(obterData(scanner, dateFormat, currentDate));

        localController.listarHorariosDisponiveis(reserva.getIdLocal(), reserva.getData());

        reserva.setHorarioInicio(obterHorario(scanner, timeFormat, currentDate, currentTime).toLocalTime());

        reserva.setHorarioFim(calcularHorarioFim(scanner, localModel, reserva));

        reserva.setStatus("PENDENTE");
    }

    private LocalTime calcularHorarioFim(Scanner scanner, LocalModel localModel, ReservaModel reserva) {
        LocalTime horarioFim;

        if (localModel.getTempoMaximo().getHour() > 1) {
            System.out.println("Quanto tempo deseja reservar?");
            System.out.println("Este local permite reserva até " + localModel.getTempoMaximo() + " horas.");

            String horarioInput = scanner.nextLine();
            LocalTime duracao = obterDuracao(horarioInput);

            horarioFim = reserva.getHorarioInicio().plusHours(duracao.getHour()).plusMinutes(duracao.getMinute());
        } else {

            horarioFim = reserva.getHorarioInicio().plusHours(1);
        }

        return horarioFim;
    }

    private LocalTime obterDuracao(String horarioInput) {
        LocalTime duracao = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            duracao = LocalTime.parse(horarioInput, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Formato inválido. Por favor, insira a duração no formato HH:mm.");
        }

        return duracao != null ? duracao : LocalTime.of(1, 0);
    }

    // ================================
    // Métodos Utilitários
    // ================================

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
            if ((nome.length()) > 3 && (!nome.matches(".*[^a-zA-Z ]+.*"))) {
                break;
            }
            System.out
                    .println("Nome inválido, tente novamente! O nome não pode conter números ou caracteres especiais.");
        }
        return nome;
    }

    private String lerSenhaUsuario(Scanner scanner) {
        String senha;
        while (true) {
            System.out.print("Senha: ");
            senha = scanner.nextLine();

            if (senha.contains(" ")) {
                System.out.println("A senha não pode conter espaços. Tente novamente!");
                continue;
            }

            if (senha.length() >= 8) {
                break;
            } else {
                System.out.println("Senha muito curta, tente novamente!");
            }
        }
        return senha;
    }

    public String validarCpfUsuario(Scanner scanner) {
        while (true) {
            System.out.print("CPF (11 dígitos): ");
            String cpf = scanner.nextLine();
            if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
                System.out.println("Este Cpf é inválido, tente novamente!");
            } else if (cpf.chars().distinct().count() == 1) {
                System.out.println("Este Cpf é inválido, tente novamente!");
            } else {
                int fDigit = calcularDigitoVerificadorCpf(cpf, 10, 9);
                int sDigit = calcularDigitoVerificadorCpf(cpf, 11, 10);
                if (!(fDigit == (cpf.charAt(9) - '0') && sDigit == (cpf.charAt(10) - '0'))) {
                    System.out.println("Este Cpf é inválido, tente novamente!");
                } else {
                    return cpf;
                }
            }
        }
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

    // ================================
    // Menus de Navegação
    // ================================

    @Override
    public void exibirMenuInicial() {
        System.out.println("\n--- Menu Usuario ---");
        System.out.println("1. Cadastrar");
        System.out.println("2. Logar");
        System.out.println("3. Sair");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    public void exibirMenuPrincipal() {
        System.out.println("\n--- Menu Usuario ---");
        System.out.println("1. Fazer reserva");
        System.out.println("2. Listar Reservas");
        System.out.println("3. Cancelar Reserva");
        System.out.println("4. Informações pessoais");
        System.out.println("5. Alterar informações");
        System.out.println("6. Sair");
        System.out.print("Escolha uma opção: ");
    }
}
