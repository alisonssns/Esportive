import java.util.Scanner;
import controller.UsuarioController;
import controller.AdministradorController;

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
        AdministradorController controller = new AdministradorController();
        controller.iniciar(scanner);
      } else if (opcao == 2) {
        UsuarioController controller = new UsuarioController();
        controller.iniciar(scanner);
      } else if (opcao == 3) {
        break;
      } else {
        System.out.println("Opção inválida");
      }
    }
  }
}