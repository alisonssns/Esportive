package controller;

import java.util.Scanner;

public interface IUserAdmController {

    void cadastrar(Scanner scanner);

    void atualizarInfo(Scanner scanner);

    boolean logar(Scanner scanner);

    void fazerReserva(Scanner scanner);

    void listarReservas(Scanner scanner);

    void cancelarReserva(Scanner scanner);

    void exibirInfo();
}
