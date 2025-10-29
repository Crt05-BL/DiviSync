import 'package:dartz/dartz.dart';
import '../../../../core/error/failures.dart';
import '../entities/currency.dart';
import '../entities/currency_rate.dart';
import '../entities/conversion_result.dart';

abstract class CurrencyRepository {
  Future<Either<Failure, List<Currency>>> getSupportedCurrencies();
  Future<Either<Failure, CurrencyRate>> getCurrencyRate({
    required String fromCurrency,
    required String toCurrency,
  });
  Future<Either<Failure, ConversionResult>> convertCurrency({
    required String fromCurrency,
    required String toCurrency,
    required double amount,
  });
  Future<Either<Failure, Map<String, double>>> getAllRates({
    required String baseCurrency,
  });
  Future<Either<Failure, DateTime>> getLastUpdateTime();
  Future<Either<Failure, void>> updateRates();
  Future<Either<Failure, bool>> isOfflineMode();
  Future<Either<Failure, String>> getRateSource();
}
