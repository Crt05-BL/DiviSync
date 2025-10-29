import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:hive/hive.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

import '../config/app_config.dart';
import '../../features/currency/data/datasources/currency_local_datasource.dart';
import '../../features/currency/data/datasources/currency_remote_datasource.dart';
import '../../features/currency/data/repositories/currency_repository_impl.dart';
import '../../features/currency/domain/repositories/currency_repository.dart';
import '../../features/currency/domain/usecases/get_currency_rates.dart';
import '../../features/currency/domain/usecases/convert_currency.dart';
import '../../features/currency/domain/usecases/get_currency_history.dart';
import '../../features/currency/presentation/bloc/currency_bloc.dart';
import '../../features/settings/data/datasources/settings_local_datasource.dart';
import '../../features/settings/data/repositories/settings_repository_impl.dart';
import '../../features/settings/domain/repositories/settings_repository.dart';
import '../../features/settings/domain/usecases/get_settings.dart';
import '../../features/settings/domain/usecases/save_settings.dart';
import '../../features/settings/presentation/bloc/settings_bloc.dart';
import '../../features/history/data/datasources/history_local_datasource.dart';
import '../../features/history/data/repositories/history_repository_impl.dart';
import '../../features/history/domain/repositories/history_repository.dart';
import '../../features/history/domain/usecases/get_conversion_history.dart';
import '../../features/history/domain/usecases/save_conversion.dart';
import '../../features/history/domain/usecases/clear_history.dart';
import '../../features/history/presentation/bloc/history_bloc.dart';

final getIt = GetIt.instance;

Future<void> initializeDependencies() async {
  // External dependencies
  final sharedPreferences = await SharedPreferences.getInstance();
  getIt.registerLazySingleton(() => sharedPreferences);
  
  getIt.registerLazySingleton(() => http.Client());
  getIt.registerLazySingleton(() => Connectivity());
  
  // Hive boxes
  final currencyBox = await Hive.openBox('currency_rates');
  final settingsBox = await Hive.openBox('settings');
  final historyBox = await Hive.openBox('conversion_history');
  
  getIt.registerLazySingleton(() => currencyBox);
  getIt.registerLazySingleton(() => settingsBox);
  getIt.registerLazySingleton(() => historyBox);
  
  // Data sources
  getIt.registerLazySingleton<CurrencyRemoteDataSource>(
    () => CurrencyRemoteDataSourceImpl(
      client: getIt(),
      fixerApiUrl: AppConfig.fixerApiUrl,
      exchangeHostApiUrl: AppConfig.exchangeHostApiUrl,
      fixerApiKey: AppConfig.fixerApiKey,
    ),
  );
  
  getIt.registerLazySingleton<CurrencyLocalDataSource>(
    () => CurrencyLocalDataSourceImpl(
      currencyBox: getIt(),
      sharedPreferences: getIt(),
    ),
  );
  
  getIt.registerLazySingleton<SettingsLocalDataSource>(
    () => SettingsLocalDataSourceImpl(
      settingsBox: getIt(),
      sharedPreferences: getIt(),
    ),
  );
  
  getIt.registerLazySingleton<HistoryLocalDataSource>(
    () => HistoryLocalDataSourceImpl(
      historyBox: getIt(),
    ),
  );
  
  // Repositories
  getIt.registerLazySingleton<CurrencyRepository>(
    () => CurrencyRepositoryImpl(
      remoteDataSource: getIt(),
      localDataSource: getIt(),
      connectivity: getIt(),
    ),
  );
  
  getIt.registerLazySingleton<SettingsRepository>(
    () => SettingsRepositoryImpl(
      localDataSource: getIt(),
    ),
  );
  
  getIt.registerLazySingleton<HistoryRepository>(
    () => HistoryRepositoryImpl(
      localDataSource: getIt(),
    ),
  );
  
  // Use cases
  getIt.registerLazySingleton(() => GetCurrencyRates(getIt()));
  getIt.registerLazySingleton(() => ConvertCurrency(getIt()));
  getIt.registerLazySingleton(() => GetCurrencyHistory(getIt()));
  
  getIt.registerLazySingleton(() => GetSettings(getIt()));
  getIt.registerLazySingleton(() => SaveSettings(getIt()));
  
  getIt.registerLazySingleton(() => GetConversionHistory(getIt()));
  getIt.registerLazySingleton(() => SaveConversion(getIt()));
  getIt.registerLazySingleton(() => ClearHistory(getIt()));
  
  // Blocs
  getIt.registerFactory(
    () => CurrencyBloc(
      getCurrencyRates: getIt(),
      convertCurrency: getIt(),
      getCurrencyHistory: getIt(),
    ),
  );
  
  getIt.registerFactory(
    () => SettingsBloc(
      getSettings: getIt(),
      saveSettings: getIt(),
    ),
  );
  
  getIt.registerFactory(
    () => HistoryBloc(
      getConversionHistory: getIt(),
      saveConversion: getIt(),
      clearHistory: getIt(),
    ),
  );
}
