import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/utils/app_constants.dart';
import '../bloc/currency_bloc.dart';
import '../widgets/currency_converter_widget.dart';
import '../widgets/currency_selector_widget.dart';
import '../widgets/rate_display_widget.dart';
import '../widgets/status_indicator_widget.dart';
import '../widgets/convert_button_widget.dart';
import '../widgets/swap_button_widget.dart';
import '../widgets/amount_input_widget.dart';
import '../widgets/result_display_widget.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Theme.of(context).colorScheme.background,
      appBar: AppBar(
        title: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: 32,
              height: 32,
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  colors: [
                    AppTheme.primaryBlue,
                    AppTheme.secondaryGreen,
                  ],
                ),
                borderRadius: BorderRadius.circular(8),
              ),
              child: const Icon(
                Icons.currency_exchange,
                color: Colors.white,
                size: 20,
              ),
            ),
            const SizedBox(width: 12),
            Text(
              AppConfig.appName,
              style: Theme.of(context).textTheme.headlineMedium?.copyWith(
                fontWeight: FontWeight.bold,
                color: AppTheme.primaryBlue,
              ),
            ),
          ],
        ),
        centerTitle: true,
        elevation: 0,
        backgroundColor: Colors.transparent,
        actions: [
          IconButton(
            icon: const Icon(Icons.info_outline),
            onPressed: () => _showInfoDialog(context),
          ),
        ],
      ),
      body: BlocBuilder<CurrencyBloc, CurrencyState>(
        builder: (context, state) {
          if (state is CurrencyLoading) {
            return const Center(
              child: CircularProgressIndicator(),
            );
          }

          if (state is CurrencyError) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    Icons.error_outline,
                    size: 64,
                    color: AppTheme.errorRed,
                  ),
                  const SizedBox(height: 16),
                  Text(
                    'Error',
                    style: Theme.of(context).textTheme.headlineMedium,
                  ),
                  const SizedBox(height: 8),
                  Text(
                    state.message,
                    style: Theme.of(context).textTheme.bodyMedium,
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 24),
                  ElevatedButton(
                    onPressed: () {
                      context.read<CurrencyBloc>().add(const LoadCurrencies());
                    },
                    child: const Text('Retry'),
                  ),
                ],
              ),
            );
          }

          if (state is CurrencyLoaded) {
            return SingleChildScrollView(
              padding: const EdgeInsets.all(AppConstants.defaultPadding),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  // Status Indicator
                  StatusIndicatorWidget(
                    isOffline: state.isOffline,
                    source: state.source,
                    lastUpdate: state.lastUpdate,
                  ),
                  
                  const SizedBox(height: 24),
                  
                  // Main Converter Card
                  Card(
                    elevation: AppConstants.defaultElevation,
                    child: Padding(
                      padding: const EdgeInsets.all(AppConstants.defaultPadding),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          // From Currency Section
                          Text(
                            'You Send',
                            style: Theme.of(context).textTheme.titleMedium?.copyWith(
                              color: AppTheme.textSecondary,
                              fontWeight: FontWeight.w600,
                            ),
                          ),
                          const SizedBox(height: 12),
                          
                          Row(
                            children: [
                              Expanded(
                                flex: 2,
                                child: AmountInputWidget(
                                  amount: state.amount,
                                  onAmountChanged: (amount) {
                                    context.read<CurrencyBloc>().add(
                                      UpdateAmount(amount: amount),
                                    );
                                  },
                                ),
                              ),
                              const SizedBox(width: 12),
                              Expanded(
                                child: CurrencySelectorWidget(
                                  currency: state.fromCurrency,
                                  currencies: state.currencies,
                                  onCurrencySelected: (currency) {
                                    context.read<CurrencyBloc>().add(
                                      SelectFromCurrency(currency: currency),
                                    );
                                  },
                                ),
                              ),
                            ],
                          ),
                          
                          const SizedBox(height: 24),
                          
                          // Swap Button
                          Center(
                            child: SwapButtonWidget(
                              onPressed: () {
                                context.read<CurrencyBloc>().add(
                                  const SwapCurrencies(),
                                );
                              },
                            ),
                          ),
                          
                          const SizedBox(height: 24),
                          
                          // To Currency Section
                          Text(
                            'You Receive',
                            style: Theme.of(context).textTheme.titleMedium?.copyWith(
                              color: AppTheme.textSecondary,
                              fontWeight: FontWeight.w600,
                            ),
                          ),
                          const SizedBox(height: 12),
                          
                          Row(
                            children: [
                              Expanded(
                                flex: 2,
                                child: ResultDisplayWidget(
                                  amount: state.convertedAmount,
                                  currency: state.toCurrency,
                                ),
                              ),
                              const SizedBox(width: 12),
                              Expanded(
                                child: CurrencySelectorWidget(
                                  currency: state.toCurrency,
                                  currencies: state.currencies,
                                  onCurrencySelected: (currency) {
                                    context.read<CurrencyBloc>().add(
                                      SelectToCurrency(currency: currency),
                                    );
                                  },
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ),
                  
                  const SizedBox(height: 24),
                  
                  // Rate Display
                  if (state.rate > 0)
                    RateDisplayWidget(
                      fromCurrency: state.fromCurrency,
                      toCurrency: state.toCurrency,
                      rate: state.rate,
                    ),
                  
                  const SizedBox(height: 24),
                  
                  // Convert Button
                  ConvertButtonWidget(
                    onPressed: state.isLoading ? null : () {
                      context.read<CurrencyBloc>().add(
                        ConvertCurrencyAmount(amount: state.amount),
                      );
                    },
                    isLoading: state.isLoading,
                  ),
                  
                  const SizedBox(height: 24),
                  
                  // Refresh Button
                  OutlinedButton.icon(
                    onPressed: state.isLoading ? null : () {
                      context.read<CurrencyBloc>().add(
                        const RefreshRates(),
                      );
                    },
                    icon: const Icon(Icons.refresh),
                    label: const Text('Refresh Rates'),
                  ),
                ],
              ),
            );
          }

          return const SizedBox.shrink();
        },
      ),
    );
  }

  void _showInfoDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('About DiviSync'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'Version: ${AppConfig.appVersion}',
              style: Theme.of(context).textTheme.bodyMedium,
            ),
            const SizedBox(height: 8),
            Text(
              'A professional currency conversion app with real-time rates, offline support, and secure local storage.',
              style: Theme.of(context).textTheme.bodyMedium,
            ),
            const SizedBox(height: 16),
            Text(
              'Features:',
              style: Theme.of(context).textTheme.titleSmall?.copyWith(
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 8),
            const Text('• Real-time exchange rates'),
            const Text('• Offline mode support'),
            const Text('• 150+ supported currencies'),
            const Text('• Secure local storage'),
            const Text('• Professional UI/UX'),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Close'),
          ),
        ],
      ),
    );
  }
}
