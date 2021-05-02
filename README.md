# CryptoArch

This project was created as a sample android app about a year ago. The
main app uses [CoinMarketCab](https://coinmarketcap.com/api/) api to
receive list of famous crypto currencies with latest price updates. As
you mentioned that I can use one of my own projects to complete the task
I decided to use this project as the code base and add a feature to
receive historical updates of bitcoin from
[BlockChain Info](https://www.blockchain.com/api/charts_api) api and
show the api via AnyChart library.

### Architecture

The core app architecture is Clean Architecture with MVVM as the
presentation layer, Dagger2 as the DI framework, Coroutines as async
programming framework, and Android Data Binding using Kotlin programming
language.

### Instructions

The `master` branch is my sample app. In order to run the task app you
should switch to branch `n26-task` and simply build and run the app and
view the list of top crypto currencies. When you click on Bitcoin item a
line chart of historical price updates will be shown.
