class ExchangeHostResponseModel {
  final String base;
  final String date;
  final Map<String, double> rates;

  const ExchangeHostResponseModel({
    required this.base,
    required this.date,
    required this.rates,
  });

  factory ExchangeHostResponseModel.fromJson(Map<String, dynamic> json) {
    return ExchangeHostResponseModel(
      base: json['base'] as String,
      date: json['date'] as String,
      rates: Map<String, double>.from(
        (json['rates'] as Map<String, dynamic>).map(
          (key, value) => MapEntry(key, (value as num).toDouble()),
        ),
      ),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'base': base,
      'date': date,
      'rates': rates,
    };
  }
}
