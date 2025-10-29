import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/utils/app_constants.dart';
import '../../domain/entities/currency.dart';

class AmountInputWidget extends StatefulWidget {
  final double amount;
  final ValueChanged<double> onAmountChanged;

  const AmountInputWidget({
    super.key,
    required this.amount,
    required this.onAmountChanged,
  });

  @override
  State<AmountInputWidget> createState() => _AmountInputWidgetState();
}

class _AmountInputWidgetState extends State<AmountInputWidget> {
  late TextEditingController _controller;
  late FocusNode _focusNode;

  @override
  void initState() {
    super.initState();
    _controller = TextEditingController(
      text: widget.amount.toStringAsFixed(2),
    );
    _focusNode = FocusNode();
  }

  @override
  void dispose() {
    _controller.dispose();
    _focusNode.dispose();
    super.dispose();
  }

  @override
  void didUpdateWidget(AmountInputWidget oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.amount != widget.amount) {
      _controller.text = widget.amount.toStringAsFixed(2);
    }
  }

  void _onAmountChanged(String value) {
    final amount = double.tryParse(value) ?? 0.0;
    if (amount != widget.amount) {
      widget.onAmountChanged(amount);
    }
  }

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      key: const Key(AppConstants.amountInputKey),
      controller: _controller,
      focusNode: _focusNode,
      onChanged: _onAmountChanged,
      keyboardType: const TextInputType.numberWithOptions(decimal: true),
      inputFormatters: [
        FilteringTextInputFormatter.allow(RegExp(r'^\d*\.?\d{0,2}')),
        LengthLimitingTextInputFormatter(12),
      ],
      style: Theme.of(context).textTheme.headlineLarge?.copyWith(
        fontWeight: FontWeight.bold,
        color: AppTheme.textPrimary,
      ),
      decoration: InputDecoration(
        hintText: '0.00',
        hintStyle: Theme.of(context).textTheme.headlineLarge?.copyWith(
          color: AppTheme.textLight,
        ),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(AppConstants.defaultRadius),
          borderSide: const BorderSide(color: AppTheme.borderColor),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(AppConstants.defaultRadius),
          borderSide: const BorderSide(color: AppTheme.borderColor),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(AppConstants.defaultRadius),
          borderSide: const BorderSide(
            color: AppTheme.primaryBlue,
            width: 2,
          ),
        ),
        contentPadding: const EdgeInsets.symmetric(
          horizontal: AppConstants.defaultPadding,
          vertical: AppConstants.defaultPadding,
        ),
        filled: true,
        fillColor: AppTheme.backgroundSecondary,
      ),
    );
  }
}
