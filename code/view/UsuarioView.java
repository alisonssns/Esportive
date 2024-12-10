package view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import model.UsuarioModel;
import model.ReservaModel;
import java.sql.Date;
import java.text.ParseException;

public class UsuarioView {
    public UsuarioModel usuario;

    public void cadastrar(Scanner scanner, UsuarioModel usuario) {
        System.out.print("Cpf: ");
        usuario.setCpf(scanner.nextLine());
        System.out.print("Nome: ");
        usuario.setNome(scanner.nextLine());
        System.out.print("Email: ");
        usuario.setEmail(scanner.nextLine());
        System.out.print("Senha: ");
        usuario.setSenha(scanner.nextLine());
        System.out.print("Telefone: ");
        usuario.setTelefone(scanner.nextLine());
    }

    public void logar(Scanner scanner, UsuarioModel usuario) {
        System.out.println("Email: ");
        usuario.setEmail(scanner.nextLine());
        System.out.println("Senha: ");
        usuario.setSenha(scanner.nextLine());
    }

    public void listar(ResultSet rs) throws SQLException {
        System.out.println("Nome: " + rs.getString("nome"));
    }
    
    public void fazerReserva(Scanner scanner, ReservaModel reserva) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        System.out.println("Qual o local gostaria de fazer a reserva? escolha pelo ID:");
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

    public void infoUser() {
    }

    public void visualizarReservas() {

    }
}
