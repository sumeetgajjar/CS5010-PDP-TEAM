package virtualgambling.controller.command.persistantusermodelcommand;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

import util.Constants;
import util.Utils;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.persistence.PortfolioLoader;
import virtualgambling.model.persistence.serdes.JSONSerDes;

/**
 * This class represents a command to Load a Portfolio in Model. It extends {@link
 * AbstractPersistableUserModelCommand}.
 */
public class LoadPortfolioCommand extends AbstractPersistableUserModelCommand {

  private final Path path;
  private final Consumer<String> consumer;

  /**
   * Constructs the object of LoadPortfolioCommand with the given params.
   *
   * @param persistableUserModel the persistable user model
   * @param path                 the path of the strategy file to load
   * @param consumer             the consumer
   * @throws IllegalArgumentException if any of the given params are null
   */
  public LoadPortfolioCommand(PersistableUserModel persistableUserModel, Path path,
                              Consumer<String> consumer) throws IllegalArgumentException {
    super(persistableUserModel);
    this.path = Utils.requireNonNull(path);
    this.consumer = Utils.requireNonNull(consumer);
  }

  /**
   * Executes this command and consumes the result of the command using the consumer.
   *
   * @throws RuntimeException if any IOException occurs while writing to view then the exception is
   *                          wrapped into RuntimeException and thrown
   */
  @Override
  public void execute() throws RuntimeException {

    try {
      JSONSerDes<Portfolio> serDes = new JSONSerDes<>(this.path, Constants.PORTFOLIO_TYPE);
      this.persistableUserModel.loadIntoModel(new PortfolioLoader(serDes));
      consumer.accept(Constants.PORTFOLIO_SUCCESSFULLY_LOADED_MESSAGE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
