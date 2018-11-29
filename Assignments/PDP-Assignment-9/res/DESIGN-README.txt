Our application uses the Model-View-Controller architecture in order to implement a virtual trading application.

According to the assignment description, the user can create and examine portfolios, get cost and value of a portfolio
and buy shares into a portfolio. Accordingly, in order to promote high cohesion, the User becomes a model that in turn delegates
it's operations to other helper services in order to ensure low coupling.

The UserModel uses a StockDAO which is a data access object that acts as a link between the model and a dataSource. This decision ensures
that the data source is free to change from in-memory data to files to a web API, but the model remains unaffected.

The StockDAO is internally initialized with a data source and it is trivial to add a setter to the
DAO to change it's data source at runtime. Therefore, it uses composition in order to fulfill it's contract.
Also, the StockDAO represents a wall of abstraction between the data source and the model, in future it can contain methods to support
sale of stocks with no change in design.

We follow the dependency inversion principle and make our model depend on the StockDAO interface that in turn depends on a StockDataSource
without coupling our design to any concrete dependencies.

Other than the UserModel, StockDAO, and StockDataSource - all other classes are data classes and do not house any business logic.
All accessors to portfolio methods in the user model offer read-only access to the portfolio and do not offer access to the Portfolio
instance directly to avoid rogue additions to the portfolio.

Lastly, our design is generic in that all models accept a Date object that represents date and time, thus we don't
lose generality to restrict usage to a date without time - at the same time, the current implementation uses only dates and
disregard time - in order to start using time, we will require 0 design change.

Our Controller is initialized with a model and a view and it waits for inputs from the view and calls the appropriate methods of the
model and send output to the view.

The controller in turn uses the command design pattern in order to unify our commands that have the same flow -
1. wait for command from the view
2. wait for parameters if any (depending on the command)
3. call the model method
4. pass output to the view

The design is such that the view is abstracted from the command - that is the command has no access to the View.
The controller in turn uses a Supplier that supplies inputs to the command and a consumer that accepts outputs from the command.

We have tried our best to keep the design such that it remains generic and one that will require minimal changes in the future.

Changes in UserModel
1. returning a Portfolio bean instead of returning String from UserModel
2. moved method get composition, get cost basis, get portfolio value to Portfolio

Added EnhancedUserModel which extends UserModel.
Abstracted Strategy out

No change in Controller Design
Added EnhancedTradingController which extends TradingController

Added OrchestratorController which uses Controller chaining to select the data source initially,
then hands controller to EnhancedTradingController.

Removed UserModel as a param to execute() method in Command Interface,
as the Command can now be executed on both UserModel and EnhancedUserModel.

No change in the View Interface

