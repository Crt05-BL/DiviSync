part of 'currency_bloc.dart';

abstract class CurrencyState extends Equatable {
  const CurrencyState();

  @override
  List<Object?> get props => [];
}

class CurrencyInitial extends CurrencyState {}

class CurrencyLoading extends CurrencyState {}

class CurrencyLoaded extends CurrencyState {
  final List<Currency> currencies;
  final Currency fromCurrency;
  final Currency toCurrency;
  final double amount;
  final double convertedAmount;
  final double rate;
  final DateTime? lastUpdate;
  final String source;
  final bool isOffline;
  final bool isLoading;

  const CurrencyLoaded({
    required this.currencies,
    required this.fromCurrency,
    required this.toCurrency,
    required this.amount,
    required this.convertedAmount,
    required this.rate,
    required this.lastUpdate,
    required this.source,
    required this.isOffline,
    this.isLoading = false,
  });

  CurrencyLoaded copyWith({
    List<Currency>? currencies,
    Currency? fromCurrency,
    Currency? toCurrency,
    double? amount,
    double? convertedAmount,
    double? rate,
    DateTime? lastUpdate,
    String? source,
    bool? isOffline,
    bool? isLoading,
  }) {
    return CurrencyLoaded(
      currencies: currencies ?? this.currencies,
      fromCurrency: fromCurrency ?? this.fromCurrency,
      toCurrency: toCurrency ?? this.toCurrency,
      amount: amount ?? this.amount,
      convertedAmount: convertedAmount ?? this.convertedAmount,
      rate: rate ?? this.rate,
      lastUpdate: lastUpdate ?? this.lastUpdate,
      source: source ?? this.source,
      isOffline: isOffline ?? this.isOffline,
      isLoading: isLoading ?? this.isLoading,
    );
  }

  @override
  List<Object?> get props => [
        currencies,
        fromCurrency,
        toCurrency,
        amount,
        convertedAmount,
        rate,
        lastUpdate,
        source,
        isOffline,
        isLoading,
      ];
}

class CurrencyError extends CurrencyState {
  final String message;

  const CurrencyError({required this.message});

  @override
  List<Object?> get props => [message];
}
