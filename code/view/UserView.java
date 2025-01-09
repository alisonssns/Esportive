package view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.LocalController;

import model.LocalModel;
import model.ReservaModel;
import model.UserModel;
import validations.TimeHourValidation;
import validations.UserValidation;

public class UserView implements IView {
    TimeHourValidation reservaValidator = new TimeHourValidation();
    UserValidation validation = new UserValidation();

    public void cadastrar(Scanner scanner, UserModel model) {
        model.setCpf(lerCpf(scanner));

        model.setNome(lerNome(scanner));

        model.setEmail(lerEmail(scanner));

        model.setSenha(lerSenha(scanner));

        model.setTelefone(lerTelefone(scanner));
    }

    public void cadastrarEndereco(UserModel model, Scanner scanner) {
        model.setCep(lerInt(scanner, "Cep: "));
        scanner.nextLine();
        model.setEstado(lerString(scanner, "Estado: "));
        model.setCidade(lerString(scanner, "Cidade: "));
        model.setRua(lerString(scanner, "Rua: "));
        model.setBairro(lerString(scanner, "Bairro: "));
        model.setNumero(lerInt(scanner, "Numero: "));
    }

    public void logar(Scanner scanner, UserModel model) {
        model.setEmail(lerEmail(scanner));
        model.setSenha(lerSenha(scanner));
    }

    public int getId(Scanner scanner) {
        int id;
        while (true) {
            System.out.print("Digite o ID: ");
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                break;
            } else {
                System.out.println("Por favor, insira um número válido para o ID.");
                scanner.next();
            }
        }
        return id;
    }

    public String lerCpf(Scanner scanner) {
        String cpf;
        do {
            System.out.print("CPF (11 dígitos): ");
            cpf = scanner.nextLine();
        } while (!validation.validarCpf(cpf));
        return cpf;
    }

    public String lerNome(Scanner scanner) {
        String nome;
        do {
            System.out.print("Nome: ");
            nome = scanner.nextLine();
            nome = nome.trim();
        } while (!validation.validarNome(nome));
        return nome;
    }

    public String lerSenha(Scanner scanner) {
        String senha;
        do {
            System.out.print("Senha: ");
            senha = scanner.nextLine();
        } while (!validation.validarSenha(senha));
        return senha;
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

    public String lerTelefone(Scanner scanner) {
        final String PADRAO_TELEFONE = "^\\d{2} \\d{5}\\-\\d{4}$";
        String telefone;
        do {
            System.out.print("Telefone (Ex: 41 99999-9999): ");
            telefone = scanner.nextLine();
        } while (!validation.validarTelefone(telefone, PADRAO_TELEFONE));
        return telefone;
    }

    public String lerString(Scanner scanner, String nome) {
        String string;
        while(true){
            System.out.print(nome);
            string = scanner.nextLine();
            string = string.trim();
            if(!(string.length() > 0)){
                System.out.println("Nome do(a) " + nome + " invalido, tente novamente!");
            }else{
                break;
            }
            
        }
        return string;
    }

    public int lerInt(Scanner scanner, String nome) {
        int integer;
        while (true) {
            System.out.print(nome);
            if (scanner.hasNextInt()) {
                integer = scanner.nextInt();
                break;
            } else {
                System.out.println("Por favor, insira um número válido.");
                scanner.next();
            }
        }
        return integer;
    }
    
    public void fazerReserva(Scanner scanner, ReservaModel reserva) {
        LocalController localController = new LocalController();

        LocalModel localModel = null;
        do {
            System.out.print("Qual o local gostaria de fazer a reserva? Escolha pelo ID: ");
            while (!scanner.hasNextInt()) {
                System.out
                        .println("Por favor, insira um número válido para o ID (apenas números inteiros são aceitos).");
                scanner.next();
                System.out.print("Qual o local gostaria de fazer a reserva? Escolha pelo ID: ");
            }
            int idLocal = scanner.nextInt();

            reserva.setIdLocal(idLocal);
            localModel = localController.infoLocal(reserva.getIdLocal());

        } while (localModel == null);

        scanner.nextLine();

        reserva.setData(obterData(scanner));

        localController.listarHorariosDisponiveis(reserva.getIdLocal(), reserva.getData());

        reserva.setHorarioInicio(obterHorario(scanner));

        reserva.setHorarioFim(obterDuracao(localModel, scanner, reserva));

        reserva.setStatus("PENDENTE");
    }

    private LocalTime obterHorario(Scanner scanner) {
        String horarioInput;
        do {
            System.out.print("Escolha o horário da reserva (HH:mm): ");
            horarioInput = scanner.nextLine();
        } while (!reservaValidator.validarHorario(horarioInput));

        return LocalTime.parse(horarioInput);
    }

    public LocalTime obterDuracao(LocalModel localModel, Scanner scanner, ReservaModel reserva) {
        String horarioInput;

        if (localModel.getTempoMaximo().getHour() > 1) {
            System.out.println("Este local permite reserva até " + localModel.getTempoMaximo() + " horas.");

            do {
                System.out.println("Quanto tempo deseja reservar? (formato HH:mm): ");
                horarioInput = scanner.nextLine();
            } while (!reservaValidator.validarHorario(horarioInput));

        } else {
            System.out.println("Este local permite reserva de até " + localModel.getTempoMaximo() + " hora.");
            System.out.println(localModel.getTempoMaximo() + " hora foi reservada automaticamente");
            horarioInput = localModel.getTempoMaximo().toString();
        }

        return reservaValidator.calcularHorarioFim(reserva, horarioInput);
    }

    public LocalDate obterData(Scanner scanner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dataInput;

        do {
            System.out.print("Escolha uma data para sua reserva (dd-MM-yyyy): ");
            dataInput = scanner.nextLine();
        } while (!reservaValidator.validarData(scanner, LocalDate.now(), dataInput, formatter));

        LocalDate data = LocalDate.parse(dataInput, formatter);
        return data;
    }

    public void listarReservas(ResultSet rs) throws SQLException {
        String line = "-------------------------------------------------------------------";
        String padrao = "| %-5s | %-10s | %-8s | %-8s | %-10s | %-5s |%n";
        System.out.println(line);
        System.out.printf(padrao, "ID", "Data", "Inicio", "Fim", "Status",
                "IdLocal");
        System.out.println(line);
        while (rs.next()) {
            System.out.printf(padrao,
                    rs.getInt("idreserva"),
                    rs.getDate("data"),
                    rs.getTime("horario_inicio"),
                    rs.getTime("horario_fim"),
                    rs.getString("status"),
                    rs.getInt("idLocal"));
        }
        System.out.println(line);
    }

    public void listarReservasEvento(ResultSet rs) throws SQLException {
        String line = "-------------------------------------------------------------------";
        String padrao = "| %-5s | %-10s | %-8s | %-8s | %-10s | %-5s |%n";
        System.out.println(line);
        System.out.printf(padrao, "ID", "Data", "Inicio", "Fim", "Status",
                "IdLocal");
        System.out.println(line);
        while (rs.next()) {
            System.out.printf(padrao,
                    rs.getInt("idreserva"),
                    rs.getDate("data"),
                    rs.getTime("horario_inicio"),
                    rs.getTime("horario_fim"),
                    rs.getString("status"),
                    rs.getInt("idLocal"));
        }
        System.out.println(line);
    }

    public void atualizarInfo(UserModel model, Scanner scanner) {
        System.out.println("1. Nome");
        System.out.println("2. E-mail");
        System.out.println("3. Senha");
        System.out.println("4. Telefone");
        System.out.print("Qual informação você deseja atulizar: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                model.setNome(lerNome(scanner));
                break;
            case 2:
                model.setEmail(lerEmail(scanner));
                break;
            case 3:
                model.setSenha(lerSenha(scanner));
                break;
            case 4:
                model.setTelefone(lerTelefone(scanner));
                break;
            default:
                System.out.println("Opção invalida");
        }
    }

    public void exibirInfo(UserModel model) {
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
        System.out.println("6. Fazer Reserva Evento");
        System.out.println("7. Sair");
        System.out.print("Escolha uma opção: ");
    }
}
