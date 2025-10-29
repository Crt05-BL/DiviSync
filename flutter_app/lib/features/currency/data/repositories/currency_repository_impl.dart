import 'package:dartz/dartz.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import '../../../../core/config/app_config.dart';
import '../../../../core/error/error_handler.dart';
import '../../../../core/error/failures.dart';
import '../../domain/entities/currency.dart';
import '../../domain/entities/currency_rate.dart';
import '../../domain/entities/conversion_result.dart';
import '../../domain/repositories/currency_repository.dart';
import '../datasources/currency_local_datasource.dart';
import '../datasources/currency_remote_datasource.dart';

class CurrencyRepositoryImpl implements CurrencyRepository {
  final CurrencyRemoteDataSource remoteDataSource;
  final CurrencyLocalDataSource localDataSource;
  final Connectivity connectivity;

  CurrencyRepositoryImpl({
    required this.remoteDataSource,
    required this.localDataSource,
    required this.connectivity,
  });

  @override
  Future<Either<Failure, List<Currency>>> getSupportedCurrencies() async {
    try {
      final currencies = <Currency>[];
      
      for (final code in AppConfig.supportedCurrencies) {
        final name = AppConfig.currencyNames[code] ?? code;
        final symbol = AppConfig.currencySymbols[code] ?? code;
        final decimals = AppConfig.currencyDecimals[code] ?? 2;
        final flag = _getFlagEmoji(code);
        
        currencies.add(Currency(
          code: code,
          name: name,
          symbol: symbol,
          decimals: decimals,
          flag: flag,
        ));
      }
      
      return Right(currencies);
    } catch (e) {
      return Left(UnknownFailure(
        message: 'Failed to get supported currencies: $e',
        code: 'UNKNOWN_ERROR',
      ));
    }
  }

  @override
  Future<Either<Failure, CurrencyRate>> getCurrencyRate({
    required String fromCurrency,
    required String toCurrency,
  }) async {
    try {
      // Check if we have a cached rate first
      final cachedRate = await localDataSource.getCachedRate(
        fromCurrency: fromCurrency,
        toCurrency: toCurrency,
      );
      
      if (cachedRate != null) {
        return Right(cachedRate);
      }

      // Check connectivity
      final connectivityResult = await connectivity.checkConnectivity();
      if (connectivityResult == ConnectivityResult.none) {
        return Left(NetworkFailure(
          message: 'No internet connection',
          code: 'NO_CONNECTION',
        ));
      }

      // Get rate from remote source
      final rate = await remoteDataSource.getCurrencyRate(
        fromCurrency: fromCurrency,
        toCurrency: toCurrency,
      );

      // Cache the rate
      await localDataSource.cacheRate(rate);
      await localDataSource.setLastUpdateTime(DateTime.now());

      return Right(rate);
    } catch (e) {
      return ErrorHandler.handleException(e);
    }
  }

  @override
  Future<Either<Failure, ConversionResult>> convertCurrency({
    required String fromCurrency,
    required String toCurrency,
    required double amount,
  }) async {
    try {
      final rateResult = await getCurrencyRate(
        fromCurrency: fromCurrency,
        toCurrency: toCurrency,
      );

      return rateResult.fold(
        (failure) => Left(failure),
        (rate) {
          final convertedAmount = amount * rate.rate;
          final result = ConversionResult(
            fromCurrency: fromCurrency,
            toCurrency: toCurrency,
            fromAmount: amount,
            toAmount: convertedAmount,
            rate: rate.rate,
            timestamp: DateTime.now(),
            source: rate.source,
            isOffline: rate.isOffline,
          );
          return Right(result);
        },
      );
    } catch (e) {
      return ErrorHandler.handleException(e);
    }
  }

  @override
  Future<Either<Failure, Map<String, double>>> getAllRates({
    required String baseCurrency,
  }) async {
    try {
      // Check if we have cached rates first
      final cachedRates = await localDataSource.getCachedRates(
        baseCurrency: baseCurrency,
      );
      
      if (cachedRates.isNotEmpty) {
        return Right(cachedRates);
      }

      // Check connectivity
      final connectivityResult = await connectivity.checkConnectivity();
      if (connectivityResult == ConnectivityResult.none) {
        return Left(NetworkFailure(
          message: 'No internet connection',
          code: 'NO_CONNECTION',
        ));
      }

      // Get rates from remote source
      final rates = await remoteDataSource.getAllRates(
        baseCurrency: baseCurrency,
      );

      // Cache the rates
      await localDataSource.cacheRates(
        baseCurrency: baseCurrency,
        rates: rates,
      );
      await localDataSource.setLastUpdateTime(DateTime.now());

      return Right(rates);
    } catch (e) {
      return ErrorHandler.handleException(e);
    }
  }

  @override
  Future<Either<Failure, DateTime>> getLastUpdateTime() async {
    try {
      final lastUpdate = await localDataSource.getLastUpdateTime();
      if (lastUpdate != null) {
        return Right(lastUpdate);
      }
      return Left(NotFoundFailure(
        message: 'Last update time not found',
        code: 'NOT_FOUND',
      ));
    } catch (e) {
      return ErrorHandler.handleException(e);
    }
  }

  @override
  Future<Either<Failure, void>> updateRates() async {
    try {
      // Check connectivity
      final connectivityResult = await connectivity.checkConnectivity();
      if (connectivityResult == ConnectivityResult.none) {
        return Left(NetworkFailure(
          message: 'No internet connection',
          code: 'NO_CONNECTION',
        ));
      }

      // Update rates for major currencies
      final majorCurrencies = ['USD', 'EUR', 'GBP', 'JPY', 'AUD', 'CAD'];
      
      for (final currency in majorCurrencies) {
        await getAllRates(baseCurrency: currency);
      }

      return const Right(null);
    } catch (e) {
      return ErrorHandler.handleException(e);
    }
  }

  @override
  Future<Either<Failure, bool>> isOfflineMode() async {
    try {
      final connectivityResult = await connectivity.checkConnectivity();
      return Right(connectivityResult == ConnectivityResult.none);
    } catch (e) {
      return ErrorHandler.handleException(e);
    }
  }

  @override
  Future<Either<Failure, String>> getRateSource() async {
    try {
      // This would typically be determined by which API was used last
      // For now, we'll return a default value
      return const Right('FIXER');
    } catch (e) {
      return ErrorHandler.handleException(e);
    }
  }

  String _getFlagEmoji(String currencyCode) {
    // This is a simplified mapping - in a real app, you'd want a more comprehensive mapping
    final flagMap = {
      'USD': '🇺🇸',
      'EUR': '🇪🇺',
      'GBP': '🇬🇧',
      'JPY': '🇯🇵',
      'AUD': '🇦🇺',
      'CAD': '🇨🇦',
      'CHF': '🇨🇭',
      'CNY': '🇨🇳',
      'SEK': '🇸🇪',
      'NZD': '🇳🇿',
      'MXN': '🇲🇽',
      'SGD': '🇸🇬',
      'HKD': '🇭🇰',
      'NOK': '🇳🇴',
      'TRY': '🇹🇷',
      'RUB': '🇷🇺',
      'INR': '🇮🇳',
      'BRL': '🇧🇷',
      'ZAR': '🇿🇦',
      'KRW': '🇰🇷',
      'DKK': '🇩🇰',
      'PLN': '🇵🇱',
      'TWD': '🇹🇼',
      'THB': '🇹🇭',
      'MYR': '🇲🇾',
      'PHP': '🇵🇭',
      'IDR': '🇮🇩',
      'HUF': '🇭🇺',
      'CZK': '🇨🇿',
      'ILS': '🇮🇱',
      'CLP': '🇨🇱',
      'PKR': '🇵🇰',
      'BGN': '🇧🇬',
      'RON': '🇷🇴',
      'ISK': '🇮🇸',
      'HRK': '🇭🇷',
      'UAH': '🇺🇦',
      'COP': '🇨🇴',
      'ARS': '🇦🇷',
      'PEN': '🇵🇪',
      'UYU': '🇺🇾',
      'BOB': '🇧🇴',
      'VEF': '🇻🇪',
      'PYG': '🇵🇾',
      'GTQ': '🇬🇹',
      'HNL': '🇭🇳',
      'NIO': '🇳🇮',
      'CRC': '🇨🇷',
      'PAB': '🇵🇦',
      'DOP': '🇩🇴',
      'JMD': '🇯🇲',
      'TTD': '🇹🇹',
      'BBD': '🇧🇧',
      'XCD': '🇦🇬',
      'AWG': '🇦🇼',
      'KYD': '🇰🇾',
      'BZD': '🇧🇿',
      'BMD': '🇧🇲',
      'BAM': '🇧🇦',
      'MKD': '🇲🇰',
      'RSD': '🇷🇸',
      'MNT': '🇲🇳',
      'AMD': '🇦🇲',
      'GEL': '🇬🇪',
      'AZN': '🇦🇿',
      'KZT': '🇰🇿',
      'UZS': '🇺🇿',
      'KGS': '🇰🇬',
      'TJS': '🇹🇯',
      'TMT': '🇹🇲',
      'AFN': '🇦🇫',
      'LKR': '🇱🇰',
      'BDT': '🇧🇩',
      'NPR': '🇳🇵',
      'BTN': '🇧🇹',
      'MVR': '🇲🇻',
      'SCR': '🇸🇨',
      'MUR': '🇲🇺',
      'KMF': '🇰🇲',
      'DJF': '🇩🇯',
      'ETB': '🇪🇹',
      'SOS': '🇸🇴',
      'TZS': '🇹🇿',
      'UGX': '🇺🇬',
      'RWF': '🇷🇼',
      'BIF': '🇧🇮',
      'CDF': '🇨🇩',
      'AOA': '🇦🇴',
      'ZMW': '🇿🇲',
      'BWP': '🇧🇼',
      'SZL': '🇸🇿',
      'LSL': '🇱🇸',
      'NAD': '🇳🇦',
      'MAD': '🇲🇦',
      'TND': '🇹🇳',
      'DZD': '🇩🇿',
      'LYD': '🇱🇾',
      'SDG': '🇸🇩',
      'SSP': '🇸🇸',
      'EGP': '🇪🇬',
      'ERI': '🇪🇷',
      'DJI': '🇩🇯',
      'SOM': '🇸🇴',
      'YER': '🇾🇪',
      'OMR': '🇴🇲',
      'QAR': '🇶🇦',
      'BHD': '🇧🇭',
      'KWD': '🇰🇼',
      'AED': '🇦🇪',
      'SAR': '🇸🇦',
      'JOD': '🇯🇴',
      'LBP': '🇱🇧',
      'SYP': '🇸🇾',
      'IQD': '🇮🇶',
      'IRR': '🇮🇷',
    };
    
    return flagMap[currencyCode] ?? '🌍';
  }
}
