import 'dart:convert';
import 'package:http/http.dart' as http;
import '../../../../core/config/app_config.dart';
import '../../../../core/error/exceptions.dart';
import '../models/currency_rate_model.dart';
import '../models/fixer_response_model.dart';
import '../models/exchange_host_response_model.dart';

abstract class CurrencyRemoteDataSource {
  Future<CurrencyRateModel> getCurrencyRate({
    required String fromCurrency,
    required String toCurrency,
  });
  Future<Map<String, double>> getAllRates({
    required String baseCurrency,
  });
}

class CurrencyRemoteDataSourceImpl implements CurrencyRemoteDataSource {
  final http.Client client;
  final String fixerApiUrl;
  final String exchangeHostApiUrl;
  final String fixerApiKey;

  CurrencyRemoteDataSourceImpl({
    required this.client,
    required this.fixerApiUrl,
    required this.exchangeHostApiUrl,
    required this.fixerApiKey,
  });

  @override
  Future<CurrencyRateModel> getCurrencyRate({
    required String fromCurrency,
    required String toCurrency,
  }) async {
    try {
      // Try Fixer.io first
      final fixerRate = await _getRateFromFixer(fromCurrency, toCurrency);
      if (fixerRate != null) {
        return fixerRate;
      }

      // Fallback to ExchangeRate.host
      final exchangeHostRate = await _getRateFromExchangeHost(fromCurrency, toCurrency);
      if (exchangeHostRate != null) {
        return exchangeHostRate;
      }

      throw const NotFoundException(
        message: 'Exchange rate not found',
        code: 'RATE_NOT_FOUND',
      );
    } catch (e) {
      if (e is Exception) {
        rethrow;
      }
      throw UnknownException(
        message: 'Failed to get currency rate: $e',
        code: 'UNKNOWN_ERROR',
      );
    }
  }

  @override
  Future<Map<String, double>> getAllRates({
    required String baseCurrency,
  }) async {
    try {
      // Try Fixer.io first
      final fixerRates = await _getAllRatesFromFixer(baseCurrency);
      if (fixerRates.isNotEmpty) {
        return fixerRates;
      }

      // Fallback to ExchangeRate.host
      final exchangeHostRates = await _getAllRatesFromExchangeHost(baseCurrency);
      if (exchangeHostRates.isNotEmpty) {
        return exchangeHostRates;
      }

      throw const NotFoundException(
        message: 'Exchange rates not found',
        code: 'RATES_NOT_FOUND',
      );
    } catch (e) {
      if (e is Exception) {
        rethrow;
      }
      throw UnknownException(
        message: 'Failed to get exchange rates: $e',
        code: 'UNKNOWN_ERROR',
      );
    }
  }

  Future<CurrencyRateModel?> _getRateFromFixer(
    String fromCurrency,
    String toCurrency,
  ) async {
    try {
      final url = Uri.parse('$fixerApiUrl/latest?access_key=$fixerApiKey&base=$fromCurrency&symbols=$toCurrency');
      final response = await client.get(url).timeout(
        const Duration(seconds: 30),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        final fixerResponse = FixerResponseModel.fromJson(data);
        
        if (fixerResponse.success && fixerResponse.rates.isNotEmpty) {
          final rate = fixerResponse.rates[toCurrency];
          if (rate != null) {
            return CurrencyRateModel(
              fromCurrency: fromCurrency,
              toCurrency: toCurrency,
              rate: rate,
              timestamp: DateTime.fromMillisecondsSinceEpoch(fixerResponse.timestamp * 1000),
              source: 'FIXER',
              isOffline: false,
            );
          }
        }
      }
      return null;
    } catch (e) {
      return null;
    }
  }

  Future<CurrencyRateModel?> _getRateFromExchangeHost(
    String fromCurrency,
    String toCurrency,
  ) async {
    try {
      final url = Uri.parse('$exchangeHostApiUrl/latest?base=$fromCurrency&symbols=$toCurrency');
      final response = await client.get(url).timeout(
        const Duration(seconds: 30),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        final exchangeHostResponse = ExchangeHostResponseModel.fromJson(data);
        
        if (exchangeHostResponse.rates.isNotEmpty) {
          final rate = exchangeHostResponse.rates[toCurrency];
          if (rate != null) {
            return CurrencyRateModel(
              fromCurrency: fromCurrency,
              toCurrency: toCurrency,
              rate: rate,
              timestamp: DateTime.now(),
              source: 'EXCHANGE_HOST',
              isOffline: false,
            );
          }
        }
      }
      return null;
    } catch (e) {
      return null;
    }
  }

  Future<Map<String, double>> _getAllRatesFromFixer(String baseCurrency) async {
    try {
      final url = Uri.parse('$fixerApiUrl/latest?access_key=$fixerApiKey&base=$baseCurrency');
      final response = await client.get(url).timeout(
        const Duration(seconds: 30),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        final fixerResponse = FixerResponseModel.fromJson(data);
        
        if (fixerResponse.success) {
          return fixerResponse.rates;
        }
      }
      return {};
    } catch (e) {
      return {};
    }
  }

  Future<Map<String, double>> _getAllRatesFromExchangeHost(String baseCurrency) async {
    try {
      final url = Uri.parse('$exchangeHostApiUrl/latest?base=$baseCurrency');
      final response = await client.get(url).timeout(
        const Duration(seconds: 30),
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        final exchangeHostResponse = ExchangeHostResponseModel.fromJson(data);
        
        return exchangeHostResponse.rates;
      }
      return {};
    } catch (e) {
      return {};
    }
  }
}
