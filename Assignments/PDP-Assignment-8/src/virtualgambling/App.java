package virtualgambling;

import java.io.InputStreamReader;

import virtualgambling.controller.Controller;
import virtualgambling.controller.InitializationController;
import virtualgambling.view.TextView;
import virtualgambling.view.View;

/**
 * Created by gajjar.s, on 9:41 PM, 11/12/18
 */
public class App {
  public static void main(String[] args) {
    View view = new TextView(new InputStreamReader(System.in), System.out);
    Controller controller = new InitializationController(view);
    controller.go();
  }
}
