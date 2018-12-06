package virtualgambling.controller.command.persistantusermodelcommand;

import util.Utils;
import virtualgambling.controller.command.Command;
import virtualgambling.model.PersistableUserModel;

/**
 * This class represents a Abstract Command which has a {@link PersistableUserModel}. It implements
 * {@link Command} interface. It minimize the effort required to implement {@link Command}
 * interface.
 */
public abstract class AbstractPersistableUserModelCommand implements Command {

  protected final PersistableUserModel persistableUserModel;

  /**
   * Constructs a {@link AbstractPersistableUserModelCommand} object with given params.
   *
   * @param persistableUserModel the persistable user model
   * @throws IllegalArgumentException if the given param is null
   */
  protected AbstractPersistableUserModelCommand(PersistableUserModel persistableUserModel)
          throws IllegalArgumentException {
    this.persistableUserModel = Utils.requireNonNull(persistableUserModel);
  }
}
