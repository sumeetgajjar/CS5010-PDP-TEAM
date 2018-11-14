package virtualgambling.controller.command;

import virtualgambling.model.UserModel;

/**
 * Created by gajjar.s, on 12:49 AM, 11/14/18
 */
public interface Command {
  void execute(UserModel userModel);
}
