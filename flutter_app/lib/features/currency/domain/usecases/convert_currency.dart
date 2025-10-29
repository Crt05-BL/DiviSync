import 'package:dartz/dartz.dart';
import '../../../../core/error/failures.dart';
import '../entities/conversion_result.dart';
import '../repositories/currency_repository.dart';

class ConvertCurrency {
  final CurrencyRepository repository;

  ConvertCurrency(this.repository);

  Future<Either<Failure, ConversionResult>> call({
    required String fromCurrency,
    required String toCurrency,
    required double amount,
  }) async {
    return await repository.convertCurrency(
      fromCurrency: fromCurrency,
      toCurrency: toCurrency,
      amount: amount,
    );
  }
}
