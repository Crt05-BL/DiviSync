import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:equatable/equatable.dart';
import '../../domain/entities/currency.dart';
import '../../domain/entities/currency_rate.dart';
import '../../domain/entities/conversion_result.dart';
import '../../domain/usecases/get_currency_rates.dart';
import '../../domain/usecases/convert_currency.dart';
import '../../domain/usecases/get_currency_history.dart';

part 'currency_event.dart';
part 'currency_state.dart';

class CurrencyBloc extends Bloc<CurrencyEvent, CurrencyState> {
  final GetCurrencyRates getCurrencyRates;
  final ConvertCurrency convertCurrency;
  final GetCurrencyHistory getCurrencyHistory;

  CurrencyBloc({
    required this.getCurrencyRates,
    required this.convertCurrency,
    required this.getCurrencyHistory,
  }) : super(CurrencyInitial()) {
    on<LoadCurrencies>(_onLoadCurrencies);
    on<GetCurrencyRate>(_onGetCurrencyRate);
    on<ConvertCurrencyAmount>(_onConvertCurrencyAmount);
    on<SwapCurrencies>(_onSwapCurrencies);
    on<UpdateAmount>(_onUpdateAmount);
    on<SelectFromCurrency>(_onSelectFromCurrency);
    on<SelectToCurrency>(_onSelectToCurrency);
    on<RefreshRates>(_onRefreshRates);
  }

  Future<void> _onLoadCurrencies(
    LoadCurrencies event,
    Emitter<CurrencyState> emit,
  ) async {
    emit(CurrencyLoading());
    
    try {
      // Load supported currencies
      final currencies = _getSupportedCurrencies();
      
      emit(CurrencyLoaded(
        currencies: currencies,
        fromCurrency: currencies.firstWhere((c) => c.code == 'USD'),
        toCurrency: currencies.firstWhere((c) => c.code == 'EUR'),
        amount: 100.0,
        convertedAmount: 0.0,
        rate: 0.0,
        lastUpdate: null,
        source: 'N/A',
        isOffline: false,
      ));
    } catch (e) {
      emit(CurrencyError(message: 'Failed to load currencies: $e'));
    }
  }

  Future<void> _onGetCurrencyRate(
    GetCurrencyRate event,
    Emitter<CurrencyState> emit,
  ) async {
    if (state is! CurrencyLoaded) return;
    
    final currentState = state as CurrencyLoaded;
    emit(currentState.copyWith(isLoading: true));
    
    try {
      final result = await getCurrencyRates(
        GetCurrencyRatesParams(
          fromCurrency: event.fromCurrency,
          toCurrency: event.toCurrency,
        ),
      );
      
      result.fold(
        (failure) => emit(CurrencyError(message: failure.message)),
        (rate) {
          final newState = currentState.copyWith(
            rate: rate.rate,
            lastUpdate: rate.timestamp,
            source: rate.source,
            isOffline: rate.isOffline,
            isLoading: false,
          );
          emit(newState);
        },
      );
    } catch (e) {
      emit(CurrencyError(message: 'Failed to get currency rate: $e'));
    }
  }

  Future<void> _onConvertCurrencyAmount(
    ConvertCurrencyAmount event,
    Emitter<CurrencyState> emit,
  ) async {
    if (state is! CurrencyLoaded) return;
    
    final currentState = state as CurrencyLoaded;
    emit(currentState.copyWith(isLoading: true));
    
    try {
      final result = await convertCurrency(
        ConvertCurrencyParams(
          fromCurrency: currentState.fromCurrency.code,
          toCurrency: currentState.toCurrency.code,
          amount: event.amount,
        ),
      );
      
      result.fold(
        (failure) => emit(CurrencyError(message: failure.message)),
        (conversionResult) {
          final newState = currentState.copyWith(
            convertedAmount: conversionResult.toAmount,
            rate: conversionResult.rate,
            lastUpdate: conversionResult.timestamp,
            source: conversionResult.source,
            isOffline: conversionResult.isOffline,
            isLoading: false,
          );
          emit(newState);
        },
      );
    } catch (e) {
      emit(CurrencyError(message: 'Failed to convert currency: $e'));
    }
  }

  Future<void> _onSwapCurrencies(
    SwapCurrencies event,
    Emitter<CurrencyState> emit,
  ) async {
    if (state is! CurrencyLoaded) return;
    
    final currentState = state as CurrencyLoaded;
    final newState = currentState.copyWith(
      fromCurrency: currentState.toCurrency,
      toCurrency: currentState.fromCurrency,
      convertedAmount: 0.0,
      rate: 0.0,
    );
    emit(newState);
    
    // Get new rate
    add(GetCurrencyRate(
      fromCurrency: newState.fromCurrency.code,
      toCurrency: newState.toCurrency.code,
    ));
  }

  Future<void> _onUpdateAmount(
    UpdateAmount event,
    Emitter<CurrencyState> emit,
  ) async {
    if (state is! CurrencyLoaded) return;
    
    final currentState = state as CurrencyLoaded;
    final newState = currentState.copyWith(amount: event.amount);
    emit(newState);
    
    // Convert with new amount
    add(ConvertCurrencyAmount(amount: event.amount));
  }

  Future<void> _onSelectFromCurrency(
    SelectFromCurrency event,
    Emitter<CurrencyState> emit,
  ) async {
    if (state is! CurrencyLoaded) return;
    
    final currentState = state as CurrencyLoaded;
    final newState = currentState.copyWith(
      fromCurrency: event.currency,
      convertedAmount: 0.0,
      rate: 0.0,
    );
    emit(newState);
    
    // Get new rate
    add(GetCurrencyRate(
      fromCurrency: event.currency.code,
      toCurrency: currentState.toCurrency.code,
    ));
  }

  Future<void> _onSelectToCurrency(
    SelectToCurrency event,
    Emitter<CurrencyState> emit,
  ) async {
    if (state is! CurrencyLoaded) return;
    
    final currentState = state as CurrencyLoaded;
    final newState = currentState.copyWith(
      toCurrency: event.currency,
      convertedAmount: 0.0,
      rate: 0.0,
    );
    emit(newState);
    
    // Get new rate
    add(GetCurrencyRate(
      fromCurrency: currentState.fromCurrency.code,
      toCurrency: event.currency.code,
    ));
  }

  Future<void> _onRefreshRates(
    RefreshRates event,
    Emitter<CurrencyState> emit,
  ) async {
    if (state is! CurrencyLoaded) return;
    
    final currentState = state as CurrencyLoaded;
    emit(currentState.copyWith(isLoading: true));
    
    try {
      // Get fresh rate
      add(GetCurrencyRate(
        fromCurrency: currentState.fromCurrency.code,
        toCurrency: currentState.toCurrency.code,
      ));
    } catch (e) {
      emit(CurrencyError(message: 'Failed to refresh rates: $e'));
    }
  }

  List<Currency> _getSupportedCurrencies() {
    // This would typically come from the repository
    // For now, we'll return a hardcoded list
    return [
      const Currency(
        code: 'USD',
        name: 'US Dollar',
        symbol: '\$',
        decimals: 2,
        flag: '🇺🇸',
      ),
      const Currency(
        code: 'EUR',
        name: 'Euro',
        symbol: '€',
        decimals: 2,
        flag: '🇪🇺',
      ),
      const Currency(
        code: 'GBP',
        name: 'British Pound',
        symbol: '£',
        decimals: 2,
        flag: '🇬🇧',
      ),
      const Currency(
        code: 'JPY',
        name: 'Japanese Yen',
        symbol: '¥',
        decimals: 0,
        flag: '🇯🇵',
      ),
      const Currency(
        code: 'AUD',
        name: 'Australian Dollar',
        symbol: 'A\$',
        decimals: 2,
        flag: '🇦🇺',
      ),
      const Currency(
        code: 'CAD',
        name: 'Canadian Dollar',
        symbol: 'C\$',
        decimals: 2,
        flag: '🇨🇦',
      ),
      const Currency(
        code: 'CHF',
        name: 'Swiss Franc',
        symbol: 'CHF',
        decimals: 2,
        flag: '🇨🇭',
      ),
      const Currency(
        code: 'CNY',
        name: 'Chinese Yuan',
        symbol: '¥',
        decimals: 2,
        flag: '🇨🇳',
      ),
      const Currency(
        code: 'SEK',
        name: 'Swedish Krona',
        symbol: 'kr',
        decimals: 2,
        flag: '🇸🇪',
      ),
      const Currency(
        code: 'NZD',
        name: 'New Zealand Dollar',
        symbol: 'NZ\$',
        decimals: 2,
        flag: '🇳🇿',
      ),
      const Currency(
        code: 'MXN',
        name: 'Mexican Peso',
        symbol: '\$',
        decimals: 2,
        flag: '🇲🇽',
      ),
      const Currency(
        code: 'SGD',
        name: 'Singapore Dollar',
        symbol: 'S\$',
        decimals: 2,
        flag: '🇸🇬',
      ),
      const Currency(
        code: 'HKD',
        name: 'Hong Kong Dollar',
        symbol: 'HK\$',
        decimals: 2,
        flag: '🇭🇰',
      ),
      const Currency(
        code: 'NOK',
        name: 'Norwegian Krone',
        symbol: 'kr',
        decimals: 2,
        flag: '🇳🇴',
      ),
      const Currency(
        code: 'TRY',
        name: 'Turkish Lira',
        symbol: '₺',
        decimals: 2,
        flag: '🇹🇷',
      ),
      const Currency(
        code: 'RUB',
        name: 'Russian Ruble',
        symbol: '₽',
        decimals: 2,
        flag: '🇷🇺',
      ),
      const Currency(
        code: 'INR',
        name: 'Indian Rupee',
        symbol: '₹',
        decimals: 2,
        flag: '🇮🇳',
      ),
      const Currency(
        code: 'BRL',
        name: 'Brazilian Real',
        symbol: 'R\$',
        decimals: 2,
        flag: '🇧🇷',
      ),
      const Currency(
        code: 'ZAR',
        name: 'South African Rand',
        symbol: 'R',
        decimals: 2,
        flag: '🇿🇦',
      ),
      const Currency(
        code: 'KRW',
        name: 'South Korean Won',
        symbol: '₩',
        decimals: 0,
        flag: '🇰🇷',
      ),
    ];
  }
}
