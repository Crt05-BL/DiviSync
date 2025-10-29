import 'dart:convert';
import 'package:hive/hive.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../../../core/config/app_config.dart';
import '../../../../core/error/exceptions.dart';
import '../models/currency_rate_model.dart';

abstract class CurrencyLocalDataSource {
  Future<CurrencyRateModel?> getCachedRate({
    required String fromCurrency,
    required String toCurrency,
  });
  Future<void> cacheRate(CurrencyRateModel rate);
  Future<Map<String, double>> getCachedRates({
    required String baseCurrency,
  });
  Future<void> cacheRates({
    required String baseCurrency,
    required Map<String, double> rates,
  });
  Future<DateTime?> getLastUpdateTime();
  Future<void> setLastUpdateTime(DateTime time);
  Future<bool> isRateExpired(DateTime timestamp);
  Future<void> clearCache();
}

class CurrencyLocalDataSourceImpl implements CurrencyLocalDataSource {
  final Box currencyBox;
  final SharedPreferences sharedPreferences;

  CurrencyLocalDataSourceImpl({
    required this.currencyBox,
    required this.sharedPreferences,
  });

  @override
  Future<CurrencyRateModel?> getCachedRate({
    required String fromCurrency,
    required String toCurrency,
  }) async {
    try {
      final key = '${fromCurrency}_$toCurrency';
      final cachedData = currencyBox.get(key);
      
      if (cachedData != null) {
        final rateData = json.decode(cachedData as String);
        final rate = CurrencyRateModel.fromJson(rateData);
        
        // Check if rate is still valid
        if (!isRateExpired(rate.timestamp)) {
          return rate;
        }
      }
      return null;
    } catch (e) {
      throw CacheException(
        message: 'Failed to get cached rate: $e',
        code: 'CACHE_GET_ERROR',
      );
    }
  }

  @override
  Future<void> cacheRate(CurrencyRateModel rate) async {
    try {
      final key = '${rate.fromCurrency}_${rate.toCurrency}';
      final rateData = json.encode(rate.toJson());
      await currencyBox.put(key, rateData);
    } catch (e) {
      throw CacheException(
        message: 'Failed to cache rate: $e',
        code: 'CACHE_PUT_ERROR',
      );
    }
  }

  @override
  Future<Map<String, double>> getCachedRates({
    required String baseCurrency,
  }) async {
    try {
      final key = 'rates_$baseCurrency';
      final cachedData = currencyBox.get(key);
      
      if (cachedData != null) {
        final ratesData = json.decode(cachedData as String);
        final timestamp = DateTime.parse(ratesData['timestamp'] as String);
        
        // Check if rates are still valid
        if (!isRateExpired(timestamp)) {
          return Map<String, double>.from(
            (ratesData['rates'] as Map<String, dynamic>).map(
              (key, value) => MapEntry(key, (value as num).toDouble()),
            ),
          );
        }
      }
      return {};
    } catch (e) {
      throw CacheException(
        message: 'Failed to get cached rates: $e',
        code: 'CACHE_GET_ERROR',
      );
    }
  }

  @override
  Future<void> cacheRates({
    required String baseCurrency,
    required Map<String, double> rates,
  }) async {
    try {
      final key = 'rates_$baseCurrency';
      final ratesData = {
        'timestamp': DateTime.now().toIso8601String(),
        'rates': rates,
      };
      await currencyBox.put(key, json.encode(ratesData));
    } catch (e) {
      throw CacheException(
        message: 'Failed to cache rates: $e',
        code: 'CACHE_PUT_ERROR',
      );
    }
  }

  @override
  Future<DateTime?> getLastUpdateTime() async {
    try {
      final timestamp = sharedPreferences.getInt('last_update_timestamp');
      if (timestamp != null) {
        return DateTime.fromMillisecondsSinceEpoch(timestamp);
      }
      return null;
    } catch (e) {
      throw CacheException(
        message: 'Failed to get last update time: $e',
        code: 'CACHE_GET_ERROR',
      );
    }
  }

  @override
  Future<void> setLastUpdateTime(DateTime time) async {
    try {
      await sharedPreferences.setInt(
        'last_update_timestamp',
        time.millisecondsSinceEpoch,
      );
    } catch (e) {
      throw CacheException(
        message: 'Failed to set last update time: $e',
        code: 'CACHE_PUT_ERROR',
      );
    }
  }

  @override
  Future<bool> isRateExpired(DateTime timestamp) async {
    final now = DateTime.now();
    final difference = now.difference(timestamp);
    return difference > AppConfig.cacheTtl;
  }

  @override
  Future<void> clearCache() async {
    try {
      await currencyBox.clear();
      await sharedPreferences.remove('last_update_timestamp');
    } catch (e) {
      throw CacheException(
        message: 'Failed to clear cache: $e',
        code: 'CACHE_CLEAR_ERROR',
      );
    }
  }
}
