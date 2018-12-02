package virtualgambling.model;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by gajjar.s, on 11:45 AM, 12/2/18
 */
public interface UserModelV3 extends EnhancedUserModel {

  void savePortfolioToFile(String portfolioName, Path path) throws IOException;

  void loadPortfolioFromFileInModel(Path path) throws IOException, IllegalStateException;
}
