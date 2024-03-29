package virtualgambling.controller.command.usermodelcommand;

import util.Utils;
import virtualgambling.controller.command.Command;
import virtualgambling.model.UserModel;

/**
 * This class represents a Abstract Command which has a {@link UserModel}. It implements {@link
 * Command} interface. It minimize the effort required to implement {@link Command} interface.
 */
public abstract class AbstractUserModelCommand implements Command {

  protected final UserModel userModel;

  /**
   * Constructs a {@link AbstractUserModelCommand} object with given params.
   *
   * @param userModel the user model
   * @throws IllegalArgumentException if the given model is null
   */
  protected AbstractUserModelCommand(UserModel userModel) throws IllegalArgumentException {
    this.userModel = Utils.requireNonNull(userModel);
  }
}
