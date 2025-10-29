import 'package:dartz/dartz.dart';
import '../../../../core/error/failures.dart';
import '../entities/conversion_result.dart';
import '../repositories/currency_repository.dart';

class GetCurrencyHistory {
  final CurrencyRepository repository;

  GetCurrencyHistory(this.repository);

  Future<Either<Failure, List<ConversionResult>>> call() async {
    // This would typically get conversion history from a different repository
    // For now, we'll return an empty list
    return const Right([]);
  }
}
