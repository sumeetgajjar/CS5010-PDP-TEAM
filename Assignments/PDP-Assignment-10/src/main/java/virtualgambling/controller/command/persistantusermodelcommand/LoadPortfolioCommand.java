package virtualgambling.controller.command.persistantusermodelcommand;

import com.google.gson.reflect.TypeToken;

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

  @Override
  public void execute() {
    JSONSerDes<Portfolio> serDes = new JSONSerDes<>(this.path, new TypeToken<Portfolio>() {
    }.getType());

    try {
      this.persistableUserModel.loadIntoModel(new PortfolioLoader(persistableUserModel, serDes));
      consumer.accept(Constants.PORTFOLIO_SUCCESSFULLY_LOADED_MESSAGE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
