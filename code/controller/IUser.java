package controller;

import java.util.Scanner;

import model.GenericModel;

public interface IUser {
    void cadastrar(Scanner scanner);
    boolean logar(Scanner scanner, GenericModel model);
    void mostraInfo(GenericModel model);
    void atualizarInfo(Scanner scanner, GenericModel model);
}
