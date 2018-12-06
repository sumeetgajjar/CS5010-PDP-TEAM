GSON : https://github.com/google/gson/blob/master/LICENSE
jFreeChart : http://www.jfree.org/gpl.php


Changes to the previous design:

1. We are now returning a Portfolio bean instead of returning Strings from UserModel:
Justification: In order to incorporate feedback from the CodeWalk, our design now returns an
immutable portfolio instead of strings. The portfolio has no setters and hence is safe with any client.

2. moved method get composition, get cost basis, get portfolio value to Portfolio
Moved all state describing methods of individual portfolio into the Portfolio class
Justification: To abstract the logic of above mentioned operations from the User model.

3. Our commands change in the sense that we removed UserModel as a param to execute() method in Command Interface,
as the Command can now be executed on both UserModel and EnhancedUserModel. This makes the command free of any dependencies and
allows us to scale our design.
Justification: Now a command interface is not coupled to UserModel.

The previous controllers and views have no changes to their public API.

Enhancements to the Trading App:

1. We have added EnhancedUserModel which extends UserModel according to the open close principle
and adds methods that allow the user to buy shares by specifying commission percentage and another
method that allows the user to buy using a strategy.

2. Implemented strategy interface that has a single method by the name of execute - this is the strategy that is passed
to the enhanced user model. Strategy#execute takes in the amount to invest per transaction that is executed by the strategy.

There are then two implementations of the strategy - OneTimeWeightedInvestmentStrategy that extends the RecurringWeightedInvestmentStrategy.
These implementations are initialized with the date and stock weights in terms of a map of tickerName (String) -> Double.

3. We added a new EnhancedTradingController which extends TradingController and constraints the user to buy stocks with commission (possibly 0)
and adds a new feature that allows the user to buy stocks using strategies, according to our previous Command Design Pattern, we
add new Commands to buy stocks using a strategy, since the model is generic and takes a map of ticker names to weights (percentage allocation),
here we have 2 separate commands for equi-weighted strategy and allocations with unequal weights.

4. The controllers now extend an abstract controller that shares the common logic of interacting with the view.

5. We have added an OrchestratorController which uses Controller chaining to select the data source initially,
then hands control to EnhancedTradingController.

6. Our previous design helped us a lot in that we merely had to implement a new StockDataSource - AlphaVantageAPIStockDataSource
and it worked out of the box with our design.

7. The controller now takes input (without changes to the public API) in a more user friendly manner
(allows the user to execute commands step by step).
In the previous design, we were sending the commands in a single line like in a terminal.

Internal implementation changes: (Changes that do not affect the clients of the models, views, or controllers)
We are now using an immutable data class - StockPrice instead of returning BigDecimal from the StockDataSource, and StockDAO.
This change allows us to maintain data locality since any stock price has to have an associated date.

Utility features added:
1. We added a multi-layer caching mechanism with following Levels in order to save time on expensive API calls:
    a. In memory (JVM) in form of an LRU cache.
    b. On Disk in form of CSV files.

2. We have added a BiFunctionRetryer that takes in any BiFunction and retries it in case of failures.
We use the Builder pattern in order to instantiate the Retryer.

AlphaVantageAPIStockDataSource uses the retryer(10 retries) and Multilayer caching



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
