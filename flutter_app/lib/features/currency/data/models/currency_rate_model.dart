import '../../domain/entities/currency_rate.dart';

class CurrencyRateModel extends CurrencyRate {
  const CurrencyRateModel({
    required super.fromCurrency,
    required super.toCurrency,
    required super.rate,
    required super.timestamp,
    required super.source,
    super.isOffline = false,
  });

  factory CurrencyRateModel.fromJson(Map<String, dynamic> json) {
    return CurrencyRateModel(
      fromCurrency: json['fromCurrency'] as String,
      toCurrency: json['toCurrency'] as String,
      rate: (json['rate'] as num).toDouble(),
      timestamp: DateTime.parse(json['timestamp'] as String),
      source: json['source'] as String,
      isOffline: json['isOffline'] as bool? ?? false,
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

  factory CurrencyRateModel.fromEntity(CurrencyRate entity) {
    return CurrencyRateModel(
      fromCurrency: entity.fromCurrency,
      toCurrency: entity.toCurrency,
      rate: entity.rate,
      timestamp: entity.timestamp,
      source: entity.source,
      isOffline: entity.isOffline,
    );
  }
}
