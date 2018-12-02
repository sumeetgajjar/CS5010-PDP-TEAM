package virtualgambling.model;

import java.io.IOException;
import java.nio.file.Path;

/**
 * This interface represents an enhancement over {@link EnhancedUserModel}. It implements {@link
 * EnhancedUserModel} interface. It adds the ability to persist and a given portfolio to a file and
 * to load it back into the model.
 */
public interface UserModelV3 extends EnhancedUserModel {

  /**
   * Saves the given portfolio in a file at the give path.
   *
   * @param portfolioName the portfolio to save
   * @param path          the path of the file to save portfolio
   * @throws IOException if unable to save the portfolio to file
   */
  void savePortfolioToFile(String portfolioName, Path path) throws IOException;

  /**
   * Load the portfolio from the file at the given path into this Model.
   *
   * @param path the path of the file
   * @throws IOException if unable to read from the file
   */
  void loadPortfolioFromFileInModel(Path path) throws IOException;
}
