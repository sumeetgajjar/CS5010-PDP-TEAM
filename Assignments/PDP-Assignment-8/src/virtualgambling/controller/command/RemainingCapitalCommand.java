package virtualgambling.controller.command;

import java.io.IOException;

import virtualgambling.model.UserModel;
import virtualgambling.view.View;

public class RemainingCapitalCommand implements Command {
  private final View view;

  public RemainingCapitalCommand(View view) {
    this.view = view;
  }

  @Override
  public void execute(UserModel userModel) throws IOException {
    this.view.display(userModel.getRemainingCapital().toPlainString());
  }
}
