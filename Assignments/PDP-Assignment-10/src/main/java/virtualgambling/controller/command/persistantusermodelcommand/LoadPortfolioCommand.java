package virtualgambling.controller.command.persistantusermodelcommand;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

import util.Constants;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.persistence.PortfolioLoader;
import virtualgambling.model.persistence.serdes.JSONSerDes;

/**
 * Created by gajjar.s, on 10:54 PM, 12/5/18
 */
public class LoadPortfolioCommand extends AbstractPersistableUserModelCommand {

  private final Path path;
  private final Consumer<String> consumer;

  public LoadPortfolioCommand(PersistableUserModel persistableUserModel, Path path,
                              Consumer<String> consumer) throws IllegalArgumentException {
    super(persistableUserModel);
    this.path = path;
    this.consumer = consumer;
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
