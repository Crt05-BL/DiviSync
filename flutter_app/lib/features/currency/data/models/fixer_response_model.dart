class FixerResponseModel {
  final bool success;
  final int timestamp;
  final String base;
  final String date;
  final Map<String, double> rates;

  const FixerResponseModel({
    required this.success,
    required this.timestamp,
    required this.base,
    required this.date,
    required this.rates,
  });

  factory FixerResponseModel.fromJson(Map<String, dynamic> json) {
    return FixerResponseModel(
      success: json['success'] as bool,
      timestamp: json['timestamp'] as int,
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
      'success': success,
      'timestamp': timestamp,
      'base': base,
      'date': date,
      'rates': rates,
    };
  }
}
