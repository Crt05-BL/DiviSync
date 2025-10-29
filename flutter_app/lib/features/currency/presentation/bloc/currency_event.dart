part of 'currency_bloc.dart';

abstract class CurrencyEvent extends Equatable {
  const CurrencyEvent();

  @override
  List<Object?> get props => [];
}

class LoadCurrencies extends CurrencyEvent {
  const LoadCurrencies();
}

class GetCurrencyRate extends CurrencyEvent {
  final String fromCurrency;
  final String toCurrency;

  const GetCurrencyRate({
    required this.fromCurrency,
    required this.toCurrency,
  });

  @override
  List<Object?> get props => [fromCurrency, toCurrency];
}

class ConvertCurrencyAmount extends CurrencyEvent {
  final double amount;

  const ConvertCurrencyAmount({required this.amount});

  @override
  List<Object?> get props => [amount];
}

class SwapCurrencies extends CurrencyEvent {
  const SwapCurrencies();
}

class UpdateAmount extends CurrencyEvent {
  final double amount;

  const UpdateAmount({required this.amount});

  @override
  List<Object?> get props => [amount];
}

class SelectFromCurrency extends CurrencyEvent {
  final Currency currency;

  const SelectFromCurrency({required this.currency});

  @override
  List<Object?> get props => [currency];
}

class SelectToCurrency extends CurrencyEvent {
  final Currency currency;

  const SelectToCurrency({required this.currency});

  @override
  List<Object?> get props => [currency];
}

class RefreshRates extends CurrencyEvent {
  const RefreshRates();
}
