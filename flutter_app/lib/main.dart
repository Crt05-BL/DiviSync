import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:workmanager/workmanager.dart';

import 'core/config/app_config.dart';
import 'core/di/injection_container.dart';
import 'core/theme/app_theme.dart';
import 'core/utils/app_constants.dart';
import 'features/currency/presentation/bloc/currency_bloc.dart';
import 'features/currency/presentation/pages/home_page.dart';
import 'features/settings/presentation/bloc/settings_bloc.dart';
import 'features/history/presentation/bloc/history_bloc.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // Initialize Hive for local storage
  await Hive.initFlutter();
  
  // Initialize dependency injection
  await initializeDependencies();
  
  // Initialize WorkManager for background tasks
  await Workmanager().initialize(callbackDispatcher);
  
  // Register periodic task for currency updates
  await Workmanager().registerPeriodicTask(
    AppConstants.currencyUpdateTaskId,
    AppConstants.currencyUpdateTaskName,
    frequency: const Duration(hours: 1),
  );
  
  // Set preferred orientations
  await SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitUp,
    DeviceOrientation.portraitDown,
  ]);
  
  // Set system UI overlay style
  SystemChrome.setSystemUIOverlayStyle(
    const SystemUiOverlayStyle(
      statusBarColor: Colors.transparent,
      statusBarIconBrightness: Brightness.dark,
      systemNavigationBarColor: Colors.transparent,
      systemNavigationBarIconBrightness: Brightness.dark,
    ),
  );
  
  runApp(const DiviSyncApp());
}

class DiviSyncApp extends StatelessWidget {
  const DiviSyncApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiBlocProvider(
      providers: [
        BlocProvider<CurrencyBloc>(
          create: (context) => getIt<CurrencyBloc>()..add(LoadCurrencies()),
        ),
        BlocProvider<SettingsBloc>(
          create: (context) => getIt<SettingsBloc>()..add(LoadSettings()),
        ),
        BlocProvider<HistoryBloc>(
          create: (context) => getIt<HistoryBloc>()..add(LoadHistory()),
        ),
      ],
      child: MaterialApp(
        title: AppConfig.appName,
        debugShowCheckedModeBanner: false,
        theme: AppTheme.lightTheme,
        darkTheme: AppTheme.darkTheme,
        themeMode: ThemeMode.system,
        home: const HomePage(),
        builder: (context, child) {
          return MediaQuery(
            data: MediaQuery.of(context).copyWith(
              textScaler: TextScaler.linear(
                MediaQuery.of(context).textScaleFactor.clamp(0.8, 1.2),
              ),
            ),
            child: child!,
          );
        },
      ),
    );
  }
}

// Background task callback
@pragma('vm:entry-point')
void callbackDispatcher() {
  Workmanager().executeTask((task, inputData) async {
    switch (task) {
      case AppConstants.currencyUpdateTaskName:
        // Update currency rates in background
        await _updateCurrencyRates();
        break;
    }
    return Future.value(true);
  });
}

Future<void> _updateCurrencyRates() async {
  // Implementation for background currency rate updates
  // This would typically involve calling the currency service
  // and updating the local cache
}
