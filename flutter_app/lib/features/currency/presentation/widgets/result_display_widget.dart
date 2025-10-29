import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/utils/app_constants.dart';
import '../../domain/entities/currency.dart';

class ResultDisplayWidget extends StatelessWidget {
  final double amount;
  final Currency currency;

  const ResultDisplayWidget({
    super.key,
    required this.amount,
    required this.currency,
  });

  @override
  Widget build(BuildContext context) {
    final formatter = NumberFormat.currency(
      locale: 'en_US',
      symbol: currency.symbol,
      decimalDigits: currency.decimals,
    );

    return Container(
      key: const Key(AppConstants.resultDisplayKey),
      height: 64,
      decoration: BoxDecoration(
        color: AppTheme.primaryBlue.withOpacity(0.1),
        borderRadius: BorderRadius.circular(AppConstants.defaultRadius),
        border: Border.all(
          color: AppTheme.primaryBlue.withOpacity(0.3),
        ),
      ),
      child: Center(
        child: Text(
          formatter.format(amount),
          style: Theme.of(context).textTheme.headlineLarge?.copyWith(
            fontWeight: FontWeight.bold,
            color: AppTheme.primaryBlue,
          ),
          textAlign: TextAlign.center,
        ),
      ),
    );
  }
}
