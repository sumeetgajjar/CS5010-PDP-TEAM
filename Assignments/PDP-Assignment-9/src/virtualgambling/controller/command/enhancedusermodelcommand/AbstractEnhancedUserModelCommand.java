package virtualgambling.controller.command.enhancedusermodelcommand;

import virtualgambling.controller.command.Command;
import virtualgambling.model.EnhancedUserModel;

/**
 * This class represents a Abstract Command which has a {@link EnhancedUserModel}. It implements
 * {@link Command} interface.
 */
public abstract class AbstractEnhancedUserModelCommand implements Command {

  protected final EnhancedUserModel enhancedUserModel;

  /**
   * Constructs a {@link AbstractEnhancedUserModelCommand} object with given params.
   *
   * @param enhancedUserModel the enhanced user model
   */
  protected AbstractEnhancedUserModelCommand(EnhancedUserModel enhancedUserModel) {
    this.enhancedUserModel = enhancedUserModel;
  }
}
