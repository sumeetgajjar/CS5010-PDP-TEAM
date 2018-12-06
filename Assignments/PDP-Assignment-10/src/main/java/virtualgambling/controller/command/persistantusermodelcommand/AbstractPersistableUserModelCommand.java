package virtualgambling.controller.command.persistantusermodelcommand;

import util.Utils;
import virtualgambling.controller.command.Command;
import virtualgambling.model.PersistableUserModel;

/**
 * Created by gajjar.s, on 10:56 PM, 12/5/18
 */
public abstract class AbstractPersistableUserModelCommand implements Command {

  protected final PersistableUserModel persistableUserModel;

  protected AbstractPersistableUserModelCommand(PersistableUserModel persistableUserModel)
          throws IllegalArgumentException {
    this.persistableUserModel = Utils.requireNonNull(persistableUserModel);
  }
}
