class AppConstants {
  // App Information
  static const String appName = 'DiviSync';
  static const String appVersion = '1.0.0';
  static const String appBuildNumber = '1';
  
  // Storage Keys
  static const String currencyRatesKey = 'currency_rates';
  static const String lastUpdateKey = 'last_update';
  static const String settingsKey = 'app_settings';
  static const String historyKey = 'conversion_history';
  static const String favoritesKey = 'favorite_currencies';
  
  // WorkManager
  static const String currencyUpdateTaskId = 'currency_update_task';
  static const String currencyUpdateTaskName = 'updateCurrencyRates';
  
  // API Configuration
  static const String fixerApiUrl = 'https://data.fixer.io/api';
  static const String exchangeHostApiUrl = 'https://api.exchangerate.host';
  static const String fixerApiKey = 'YOUR_FIXER_API_KEY_HERE';
  
  // Cache Configuration
  static const Duration cacheTtl = Duration(hours: 1);
  static const Duration updateInterval = Duration(hours: 1);
  static const Duration connectionTimeout = Duration(seconds: 30);
  static const Duration receiveTimeout = Duration(seconds: 30);
  
  // UI Constants
  static const double defaultPadding = 16.0;
  static const double smallPadding = 8.0;
  static const double largePadding = 24.0;
  static const double extraLargePadding = 32.0;
  
  static const double defaultRadius = 12.0;
  static const double smallRadius = 8.0;
  static const double largeRadius = 16.0;
  static const double extraLargeRadius = 24.0;
  
  static const double defaultElevation = 2.0;
  static const double smallElevation = 1.0;
  static const double largeElevation = 4.0;
  static const double extraLargeElevation = 8.0;
  
  // Animation Durations
  static const Duration shortAnimation = Duration(milliseconds: 200);
  static const Duration mediumAnimation = Duration(milliseconds: 300);
  static const Duration longAnimation = Duration(milliseconds: 500);
  
  // Limits
  static const int maxHistoryItems = 100;
  static const int maxFavorites = 20;
  static const int maxRetryAttempts = 3;
  static const double maxAmount = 999999999.99;
  static const double minAmount = 0.01;
  
  // Default Values
  static const double defaultSpread = 0.0;
  static const String defaultFromCurrency = 'USD';
  static const String defaultToCurrency = 'EUR';
  static const double defaultAmount = 100.0;
  
  // Error Messages
  static const String networkError = 'Network connection error';
  static const String serverError = 'Server error occurred';
  static const String cacheError = 'Cache error occurred';
  static const String unknownError = 'Unknown error occurred';
  static const String invalidAmount = 'Invalid amount entered';
  static const String currencyNotFound = 'Currency not found';
  static const String rateNotFound = 'Exchange rate not found';
  
  // Success Messages
  static const String conversionSuccessful = 'Conversion completed successfully';
  static const String ratesUpdated = 'Exchange rates updated';
  static const String settingsSaved = 'Settings saved successfully';
  static const String historyCleared = 'History cleared successfully';
  
  // Validation
  static const String amountRegex = r'^\d+(\.\d{1,2})?$';
  static const String currencyRegex = r'^[A-Z]{3}$';
  
  // Feature Flags
  static const bool enableOfflineMode = true;
  static const bool enableHistory = true;
  static const bool enableFavorites = true;
  static const bool enableCharts = true;
  static const bool enableNotifications = true;
  static const bool enableAnalytics = false;
  
  // Chart Configuration
  static const int chartDataPoints = 30;
  static const Duration chartUpdateInterval = Duration(hours: 6);
  
  // Notification Configuration
  static const String notificationChannelId = 'currency_updates';
  static const String notificationChannelName = 'Currency Updates';
  static const String notificationChannelDescription = 'Notifications for currency rate updates';
  
  // Analytics Events
  static const String eventConversion = 'conversion';
  static const String eventCurrencyChanged = 'currency_changed';
  static const String eventAmountChanged = 'amount_changed';
  static const String eventHistoryViewed = 'history_viewed';
  static const String eventSettingsChanged = 'settings_changed';
  static const String eventAppOpened = 'app_opened';
  static const String eventRatesUpdated = 'rates_updated';
  
  // Deep Links
  static const String deepLinkScheme = 'divisync';
  static const String deepLinkHost = 'convert';
  
  // URLs
  static const String privacyPolicyUrl = 'https://divisync.app/privacy';
  static const String termsOfServiceUrl = 'https://divisync.app/terms';
  static const String supportUrl = 'https://divisync.app/support';
  static const String githubUrl = 'https://github.com/divisync/app';
  
  // Social Media
  static const String twitterUrl = 'https://twitter.com/divisync';
  static const String linkedinUrl = 'https://linkedin.com/company/divisync';
  static const String email = 'support@divisync.app';
  
  // App Store URLs
  static const String playStoreUrl = 'https://play.google.com/store/apps/details?id=com.divisync.app';
  static const String appStoreUrl = 'https://apps.apple.com/app/divisync/id123456789';
  
  // Rate Limiting
  static const int maxApiCallsPerMinute = 60;
  static const int maxApiCallsPerHour = 1000;
  static const int maxApiCallsPerDay = 10000;
  
  // Encryption
  static const String encryptionKey = 'divisync_encryption_key';
  static const String encryptionAlgorithm = 'AES-256-GCM';
  
  // File Paths
  static const String databasePath = 'divisync.db';
  static const String cachePath = 'divisync_cache';
  static const String logsPath = 'divisync_logs';
  
  // Widget Keys
  static const String homePageKey = 'home_page';
  static const String historyPageKey = 'history_page';
  static const String settingsPageKey = 'settings_page';
  static const String currencySelectorKey = 'currency_selector';
  static const String amountInputKey = 'amount_input';
  static const String convertButtonKey = 'convert_button';
  static const String swapButtonKey = 'swap_button';
  static const String resultDisplayKey = 'result_display';
  static const String rateDisplayKey = 'rate_display';
  static const String lastUpdateKey = 'last_update';
  static const String statusIndicatorKey = 'status_indicator';
}
