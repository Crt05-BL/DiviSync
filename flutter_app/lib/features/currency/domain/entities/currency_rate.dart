import 'package:equatable/equatable.dart';

class CurrencyRate extends Equatable {
  final String fromCurrency;
  final String toCurrency;
  final double rate;
  final DateTime timestamp;
  final String source;
  final bool isOffline;

  const CurrencyRate({
    required this.fromCurrency,
    required this.toCurrency,
    required this.rate,
    required this.timestamp,
    required this.source,
    this.isOffline = false,
  });

  @override
  List<Object?> get props => [
        fromCurrency,
        toCurrency,
        rate,
        timestamp,
        source,
        isOffline,
      ];

  CurrencyRate copyWith({
    String? fromCurrency,
    String? toCurrency,
    double? rate,
    DateTime? timestamp,
    String? source,
    bool? isOffline,
  }) {
    return CurrencyRate(
      fromCurrency: fromCurrency ?? this.fromCurrency,
      toCurrency: toCurrency ?? this.toCurrency,
      rate: rate ?? this.rate,
      timestamp: timestamp ?? this.timestamp,
      source: source ?? this.source,
      isOffline: isOffline ?? this.isOffline,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'fromCurrency': fromCurrency,
      'toCurrency': toCurrency,
      'rate': rate,
      'timestamp': timestamp.toIso8601String(),
      'source': source,
      'isOffline': isOffline,
    };
  }

  factory CurrencyRate.fromJson(Map<String, dynamic> json) {
    return CurrencyRate(
      fromCurrency: json['fromCurrency'] as String,
      toCurrency: json['toCurrency'] as String,
      rate: (json['rate'] as num).toDouble(),
      timestamp: DateTime.parse(json['timestamp'] as String),
      source: json['source'] as String,
      isOffline: json['isOffline'] as bool? ?? false,
    );
  }

  @override
  String toString() {
    return 'CurrencyRate(fromCurrency: $fromCurrency, toCurrency: $toCurrency, rate: $rate, timestamp: $timestamp, source: $source, isOffline: $isOffline)';
  }
}
