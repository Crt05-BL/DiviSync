import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/utils/app_constants.dart';

class StatusIndicatorWidget extends StatelessWidget {
  final bool isOffline;
  final String source;
  final DateTime? lastUpdate;

  const StatusIndicatorWidget({
    super.key,
    required this.isOffline,
    required this.source,
    required this.lastUpdate,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      key: const Key(AppConstants.statusIndicatorKey),
      elevation: AppConstants.smallElevation,
      child: Padding(
        padding: const EdgeInsets.all(AppConstants.defaultPadding),
        child: Row(
          children: [
            Container(
              width: 8,
              height: 8,
              decoration: BoxDecoration(
                color: isOffline ? AppTheme.warningYellow : AppTheme.successGreen,
                shape: BoxShape.circle,
              ),
            ),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    isOffline ? 'Offline Mode' : 'Online Mode',
                    style: Theme.of(context).textTheme.titleSmall?.copyWith(
                      fontWeight: FontWeight.bold,
                      color: isOffline ? AppTheme.warningYellow : AppTheme.successGreen,
                    ),
                  ),
                  if (lastUpdate != null)
                    Text(
                      'Last update: ${DateFormat('MMM dd, yyyy HH:mm').format(lastUpdate!)}',
                      style: Theme.of(context).textTheme.bodySmall?.copyWith(
                        color: AppTheme.textLight,
                      ),
                    ),
                  Text(
                    'Source: $source',
                    style: Theme.of(context).textTheme.bodySmall?.copyWith(
                      color: AppTheme.textLight,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
