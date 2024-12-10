import java.util.Scanner;
import controller.UsuarioController;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    UsuarioController controller = new UsuarioController();
    int opcao;

    do {
      System.out.println("\n--- Menu ---");
      System.out.println("1. Cadastrar");
      System.out.println("2. Listar");
      System.out.println("3. Fazer Reserva");
      System.out.println("4. Remover Usuário");
      System.out.println("5. Sair");
      System.out.print("Escolha uma opção: ");
      opcao = scanner.nextInt();
      scanner.nextLine();

      switch (opcao) {
        case 1:
          controller.cadastrar(scanner);
          break;
        case 2:
          // controller.logar(scanner);
          break;
        case 3:
          controller.fazerReserva(scanner);
          break;
        case 4:
          // controller.remover(scanner);
          break;
        case 5:
          System.out.println("Saindo...");
          break;
        default:
          System.out.println("Opção inválida!");
          break;
      }
    } while (opcao != 5);
    scanner.close();
  }
}
