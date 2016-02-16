# seven

Trading simulator (attempt #7). This time in Scala.

Seven is my seventh attempt at building an easy to use backtesting tool. A secondary design goal is to support live-trading a real account.

Seven is not ready for use yet. If you need something now, https://www.quantstart.com/articles/Choosing-a-Platform-for-Backtesting-and-Automated-Execution suggests several options:
- Algo-Trader (http://www.algotrader.ch/)
- Marketcetera's Strategy Studio (http://www.marketcetera.org/Strategy%20Studio)
- Quantopian's Zipline library (https://github.com/quantopian/zipline)
- OpenQuant (https://code.google.com/archive/p/openquant/)
- TradeLink (http://tradelink.org/)
- PyAlgoTrade (http://gbeced.github.io/pyalgotrade/)


## Setup

### Prerequisites

1. Install Java
2. Install SBT
3. Set up a local copy of https://github.com/davidkellis/securitiesdb
4. Download and import all the historical data you're interested in using the securitiesdb project from step (1).

### Install seven

1. git clone git@github.com:davidkellis/seven.git
2. sbt compile


### Implementation Notes
