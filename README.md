# seven

Trading simulator (attempt #7). This time in Scala.

Seven is my seventh attempt at building an easy to use backtesting tool. One of the primary design goals is to support trading a real account.

Seven is similar to Quantopian's zipline backtesting tool (see https://github.com/quantopian/zipline).

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
