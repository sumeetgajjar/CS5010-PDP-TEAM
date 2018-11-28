package virtualgambling.controller.command.usermodelcommand;

import virtualgambling.controller.command.Command;
import virtualgambling.model.UserModel;

/**
 * This class represents a Abstract Command which has a {@link UserModel}. It implements {@link
 * Command} interface.
 */
public abstract class AbstractUserModelCommand implements Command {

  protected final UserModel userModel;

  /**
   * Constructs a {@link AbstractUserModelCommand} object with given params.
   *
   * @param userModel the user model
   */
  protected AbstractUserModelCommand(UserModel userModel) {
    this.userModel = userModel;
  }
}
