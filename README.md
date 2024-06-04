# Currency Exchange App

### Implemented Features:
- Display multi-currency account with predefined balances (swipe balances horizontally)
- Conversion for currencies available in the account
- Conversion calculation works both sides - change sell amount -> receive amount will be calculated/ change receive amount -> sell amount will be calculated
- Balances are updated after currency exchange (but will be overwritten with fake network response after relaunching the application - data from the server is considered as a single source of trust)
- Same currency conversion and balance below zero are not allowed (Submit button disabled)

### NOT Implemented Features:
- Regular currency exchange rates synchronisation
- Commission is not taken into account

