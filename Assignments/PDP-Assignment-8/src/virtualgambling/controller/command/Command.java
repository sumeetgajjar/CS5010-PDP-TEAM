package virtualgambling.controller.command;

import virtualgambling.model.UserModel;

/**
 * This interface represents a command which can be executed by a controller.
 */
public interface Command {

  /**
   * Executes the command.
   *
   * @param userModel the userModel
   */
  void execute(UserModel userModel);
}
