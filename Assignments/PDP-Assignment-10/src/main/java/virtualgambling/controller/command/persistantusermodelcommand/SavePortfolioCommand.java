package virtualgambling.controller.command.persistantusermodelcommand;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

import util.Constants;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.persistence.PortfolioPersister;
import virtualgambling.model.persistence.serdes.JSONSerDes;

/**
 * Created by gajjar.s, on 10:54 PM, 12/5/18
 */
public class SavePortfolioCommand extends AbstractPersistableUserModelCommand {

  private final PersistableUserModel persistableUserModel;
  private final String portfolioName;
  private final Path path;
  private final Consumer<String> consumer;

  public SavePortfolioCommand(PersistableUserModel persistableUserModel, String portfolioName,
                              Path path, Consumer<String> consumer) throws IllegalArgumentException {
    super(persistableUserModel);
    this.persistableUserModel = persistableUserModel;
    this.portfolioName = portfolioName;
    this.path = path;
    this.consumer = consumer;
  }

  @Override
  public void execute() {
    Portfolio portfolio = this.persistableUserModel.getPortfolio(portfolioName);
    JSONSerDes<Portfolio> serDes = new JSONSerDes<>(path, new TypeToken<Portfolio>() {
    }.getType());

    try {
      this.persistableUserModel.persistFromModel(new PortfolioPersister(serDes,
              portfolio));
      this.consumer.accept(Constants.PORTFOLIO_SUCCESSFULLY_SAVED_MESSAGE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}