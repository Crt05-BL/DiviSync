import 'package:flutter/material.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/utils/app_constants.dart';
import '../../domain/entities/currency.dart';

class CurrencySelectorWidget extends StatelessWidget {
  final Currency currency;
  final List<Currency> currencies;
  final ValueChanged<Currency> onCurrencySelected;

  const CurrencySelectorWidget({
    super.key,
    required this.currency,
    required this.currencies,
    required this.onCurrencySelected,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 64,
      decoration: BoxDecoration(
        border: Border.all(color: AppTheme.borderColor),
        borderRadius: BorderRadius.circular(AppConstants.defaultRadius),
        color: AppTheme.backgroundSecondary,
      ),
      child: DropdownButtonHideUnderline(
        child: DropdownButton<Currency>(
          key: const Key(AppConstants.currencySelectorKey),
          value: currency,
          isExpanded: true,
          icon: const Icon(
            Icons.keyboard_arrow_down,
            color: AppTheme.textSecondary,
          ),
          items: currencies.map((Currency currency) {
            return DropdownMenuItem<Currency>(
              value: currency,
              child: Row(
                children: [
                  Text(
                    currency.flag,
                    style: const TextStyle(fontSize: 20),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          currency.code,
                          style: Theme.of(context).textTheme.titleMedium?.copyWith(
                            fontWeight: FontWeight.bold,
                            color: AppTheme.textPrimary,
                          ),
                        ),
                        Text(
                          currency.name,
                          style: Theme.of(context).textTheme.bodySmall?.copyWith(
                            color: AppTheme.textSecondary,
                          ),
                          overflow: TextOverflow.ellipsis,
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            );
          }).toList(),
          onChanged: (Currency? newCurrency) {
            if (newCurrency != null) {
              onCurrencySelected(newCurrency);
            }
          },
        ),
      ),
    );
  }
}
