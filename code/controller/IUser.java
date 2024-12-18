package controller;

import model.GenericModel;

import java.util.Scanner;

public interface IUser {
    void cadastrar(Scanner scanner);
    boolean logar(Scanner scanner, GenericModel model);
    void mostraInfo(GenericModel model);
    void atualizarInfo(Scanner scanner, GenericModel model);
}
