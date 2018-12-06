package virtualgambling.controller.command.persistantusermodelcommand;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

import util.Constants;
import util.Utils;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.persistence.PortfolioPersister;
import virtualgambling.model.persistence.serdes.JSONSerDes;

/**
 * This class represents a command to Save a Portfolio to a file. It extends {@link
 * AbstractPersistableUserModelCommand}.
 */
public class SavePortfolioCommand extends AbstractPersistableUserModelCommand {

  private final PersistableUserModel persistableUserModel;
  private final String portfolioName;
  private final Path path;
  private final Consumer<String> consumer;

  /**
   * Constructs a Object of SavePortfolioCommand with the given params.
   *
   * @param persistableUserModel the persistable user model
   * @param portfolioName        the name of the portfolio to save
   * @param path                 the path at which the portfolio is to be saved
   * @param consumer             the consumer
   * @throws IllegalArgumentException if any of the given params are null
   */
  public SavePortfolioCommand(PersistableUserModel persistableUserModel, String portfolioName,
                              Path path, Consumer<String> consumer)
          throws IllegalArgumentException {

    super(persistableUserModel);
    this.persistableUserModel = Utils.requireNonNull(persistableUserModel);
    this.portfolioName = Utils.requireNonNull(portfolioName);
    this.path = Utils.requireNonNull(path);
    this.consumer = Utils.requireNonNull(consumer);
  }

  @Override
  public void execute() {
    Portfolio portfolio = this.persistableUserModel.getPortfolio(portfolioName);

    try {
      JSONSerDes<Portfolio> serDes = new JSONSerDes<>(path, Constants.PORTFOLIO_TYPE);
      this.persistableUserModel.persistFromModel(new PortfolioPersister(serDes,
              portfolio));
      this.consumer.accept(Constants.PORTFOLIO_SUCCESSFULLY_SAVED_MESSAGE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}