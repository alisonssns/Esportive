import java.util.Scanner;
import controller.UserController;
import controller.AdminController;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Você é administrador ou usuario?");
    System.out.println("1 - Administrador");
    System.out.println("2 - Usuario");
    System.out.println("3 - Sair");
    while (true) {
      System.out.print("Escolha uma opção: ");
      int opcao = scanner.nextInt();
      if (opcao == 1) {
        AdminController controller = new AdminController();
        controller.iniciar(scanner);
      } else if (opcao == 2) {
        UserController controller = new UserController();
        controller.iniciar(scanner);
      } else if (opcao == 3) {
        break;
      } else {
        System.out.println("Opção inválida");
      }
    }
  }
}