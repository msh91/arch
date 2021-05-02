# CryptoArch

This project was created as a sample android app a few years ago. The
main app uses [CoinMarketCab](https://coinmarketcap.com/api/) api to
receive list of famous crypto currencies with latest price updates.
Later, I decided to use this project as the code base and add a feature to
receive historical updates of bitcoin from
[BlockChain Info](https://www.blockchain.com/api/charts_api) api and
show the api via AnyChart library.

### Architecture

The core app architecture is Clean Architecture with MVVM as the
presentation layer, Dagger2 as the DI framework, Coroutines as async
programming framework, and Android Data Binding using Kotlin programming
language.

### Instructions

Switch to the `master` branch and simply build and run the app to
view the list of top crypto currencies. When you click on Bitcoin item a
line chart of historical price updates will be shown.
