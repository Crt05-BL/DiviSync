import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/utils/app_constants.dart';
import '../../domain/entities/currency.dart';

class RateDisplayWidget extends StatelessWidget {
  final Currency fromCurrency;
  final Currency toCurrency;
  final double rate;

  const RateDisplayWidget({
    super.key,
    required this.fromCurrency,
    required this.toCurrency,
    required this.rate,
  });

  @override
  Widget build(BuildContext context) {
    final formatter = NumberFormat.currency(
      locale: 'en_US',
      symbol: '',
      decimalDigits: 4,
    );

    return Card(
      key: const Key(AppConstants.rateDisplayKey),
      elevation: AppConstants.smallElevation,
      child: Padding(
        padding: const EdgeInsets.all(AppConstants.defaultPadding),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text(
              '1 ${fromCurrency.code} = ${formatter.format(rate)} ${toCurrency.code}',
              style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                color: AppTheme.textSecondary,
              ),
            ),
            Text(
              'Rate',
              style: Theme.of(context).textTheme.bodySmall?.copyWith(
                color: AppTheme.textLight,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
