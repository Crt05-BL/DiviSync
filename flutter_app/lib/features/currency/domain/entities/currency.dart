import 'package:equatable/equatable.dart';

class Currency extends Equatable {
  final String code;
  final String name;
  final String symbol;
  final int decimals;
  final String flag;

  const Currency({
    required this.code,
    required this.name,
    required this.symbol,
    required this.decimals,
    required this.flag,
  });

  @override
  List<Object?> get props => [code, name, symbol, decimals, flag];

  Currency copyWith({
    String? code,
    String? name,
    String? symbol,
    int? decimals,
    String? flag,
  }) {
    return Currency(
      code: code ?? this.code,
      name: name ?? this.name,
      symbol: symbol ?? this.symbol,
      decimals: decimals ?? this.decimals,
      flag: flag ?? this.flag,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'code': code,
      'name': name,
      'symbol': symbol,
      'decimals': decimals,
      'flag': flag,
    };
  }

  factory Currency.fromJson(Map<String, dynamic> json) {
    return Currency(
      code: json['code'] as String,
      name: json['name'] as String,
      symbol: json['symbol'] as String,
      decimals: json['decimals'] as int,
      flag: json['flag'] as String,
    );
  }

  @override
  String toString() {
    return 'Currency(code: $code, name: $name, symbol: $symbol, decimals: $decimals, flag: $flag)';
  }
}
