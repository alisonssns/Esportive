import java.util.Scanner;
import controller.UsuarioController;
import model.UsuarioModel;
import view.UsuarioView;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    UsuarioController controller = new UsuarioController();
    UsuarioView view = new UsuarioView();
    UsuarioModel model = new UsuarioModel();
    int opcao;
    boolean logado = false;

    do {
      System.out.println("\n--- Menu ---");
      System.out.println("1. Cadastrar");
      System.out.println("2. Logar");
      System.out.println("3. Sair");
      System.out.print("Escolha uma opção: ");
      opcao = scanner.nextInt();
      scanner.nextLine();

      switch (opcao) {
        case 1:
          controller.cadastrar(scanner);
          break;
        case 2:
          logado = controller.logar(scanner, model);
          if (logado) {
            view.showInterface(model, scanner, controller);
          }
          break;
        case 3:
          System.out.println("Saindo...");
          break;
        default:
          System.out.println("Opção inválida!");
          break;
      }
    } while (opcao != 3);
    scanner.close();
  }
}
