There are 2 possible files - 1 for Portfolio and 1 for Strategy.

A file for a valid Portfolio looks like this:

{
  "name": "manual", // portfolio name
  "purchases": [ // a list of stock purchases
    { // an object that has the tickerName and other details that describe the purchase
      "tickerName": "AAPL",
      "quantity": 10,
      "commissionPercentage": 0.0,
      "stockPrice": {
        "date": "2018-11-01",
        "stockPrice": 10
      }
    },
    {
      "tickerName": "GOOG",
      "quantity": 100,
      "commissionPercentage": 10.0,
      "stockPrice": {
        "date": "2018-11-02",
        "stockPrice": 10
      }
    }
  ],
  "stockDAOType": "SIMPLE", // the DAO type that fetches stock data - this should be SIMPLE
  "stockDataSourceType": "ALPHA_VANTAGE" // the data source that this portfolio should use
}



A file for Strategy looks like this:

{
  "startDate": "2018-01-01", // start date
  "stockWeights": { // the distribution of the stock weights - these should sum to 100%
    "GOOG": 50.0,
    "AAPL": 50.0
  },
  "dayFrequency": 1, // the frequency with which to buy from startDate to endDate (inclusive)
  "endDate": "2018-11-30" // the endDate (this is optional and may be skipped)
}