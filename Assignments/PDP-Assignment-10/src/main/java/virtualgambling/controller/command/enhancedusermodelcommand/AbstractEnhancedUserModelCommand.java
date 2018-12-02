package virtualgambling.controller.command.enhancedusermodelcommand;

import util.Utils;
import virtualgambling.controller.command.Command;
import virtualgambling.model.EnhancedUserModel;

/**
 * This class represents a Abstract Command which has a {@link EnhancedUserModel}. It implements
 * {@link Command} interface. It minimize the effort required to implement {@link Command}
 * interface.
 */
public abstract class AbstractEnhancedUserModelCommand implements Command {

  protected final EnhancedUserModel enhancedUserModel;

  /**
   * Constructs a {@link AbstractEnhancedUserModelCommand} object with given params.
   *
   * @param enhancedUserModel the enhanced user model
   * @throws IllegalArgumentException if the given model is null
   */
  protected AbstractEnhancedUserModelCommand(EnhancedUserModel enhancedUserModel)
          throws IllegalArgumentException {
    this.enhancedUserModel = Utils.requireNonNull(enhancedUserModel);
  }
}
