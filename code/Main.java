import java.util.Scanner;
import controller.UsuarioController;
import model.UsuarioModel;
import view.UsuarioView;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    UsuarioController controller = new UsuarioController();
    UsuarioModel model = new UsuarioModel();
    UsuarioView view = new UsuarioView();
    boolean logado = false;

    while (true) {
      if (!logado) {
        logado = view.exibirTelaInicial(model, scanner, controller);
      } else {
        view.exibirMenuPrincipal(model, scanner, controller);
      }
    }
  }
}