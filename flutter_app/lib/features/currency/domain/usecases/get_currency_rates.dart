import 'package:dartz/dartz.dart';
import '../../../../core/error/failures.dart';
import '../entities/currency_rate.dart';
import '../repositories/currency_repository.dart';

class GetCurrencyRates {
  final CurrencyRepository repository;

  GetCurrencyRates(this.repository);

  Future<Either<Failure, CurrencyRate>> call({
    required String fromCurrency,
    required String toCurrency,
  }) async {
    return await repository.getCurrencyRate(
      fromCurrency: fromCurrency,
      toCurrency: toCurrency,
    );
  }
}
