package virtualgambling.view;

import java.io.IOException;

/**
 * Created by gajjar.s, on 8:42 PM, 11/12/18
 */
public interface View {

  String getInput() throws IOException;

  void display(String text) throws IOException;
}
