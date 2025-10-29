import 'package:equatable/equatable.dart';

class ConversionResult extends Equatable {
  final String fromCurrency;
  final String toCurrency;
  final double fromAmount;
  final double toAmount;
  final double rate;
  final DateTime timestamp;
  final String source;
  final bool isOffline;

  const ConversionResult({
    required this.fromCurrency,
    required this.toCurrency,
    required this.fromAmount,
    required this.toAmount,
    required this.rate,
    required this.timestamp,
    required this.source,
    this.isOffline = false,
  });

  @override
  List<Object?> get props => [
        fromCurrency,
        toCurrency,
        fromAmount,
        toAmount,
        rate,
        timestamp,
        source,
        isOffline,
      ];

  ConversionResult copyWith({
    String? fromCurrency,
    String? toCurrency,
    double? fromAmount,
    double? toAmount,
    double? rate,
    DateTime? timestamp,
    String? source,
    bool? isOffline,
  }) {
    return ConversionResult(
      fromCurrency: fromCurrency ?? this.fromCurrency,
      toCurrency: toCurrency ?? this.toCurrency,
      fromAmount: fromAmount ?? this.fromAmount,
      toAmount: toAmount ?? this.toAmount,
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
      'fromAmount': fromAmount,
      'toAmount': toAmount,
      'rate': rate,
      'timestamp': timestamp.toIso8601String(),
      'source': source,
      'isOffline': isOffline,
    };
  }

  factory ConversionResult.fromJson(Map<String, dynamic> json) {
    return ConversionResult(
      fromCurrency: json['fromCurrency'] as String,
      toCurrency: json['toCurrency'] as String,
      fromAmount: (json['fromAmount'] as num).toDouble(),
      toAmount: (json['toAmount'] as num).toDouble(),
      rate: (json['rate'] as num).toDouble(),
      timestamp: DateTime.parse(json['timestamp'] as String),
      source: json['source'] as String,
      isOffline: json['isOffline'] as bool? ?? false,
    );
  }

  @override
  String toString() {
    return 'ConversionResult(fromCurrency: $fromCurrency, toCurrency: $toCurrency, fromAmount: $fromAmount, toAmount: $toAmount, rate: $rate, timestamp: $timestamp, source: $source, isOffline: $isOffline)';
  }
}
