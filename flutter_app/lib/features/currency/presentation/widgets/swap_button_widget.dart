import 'package:flutter/material.dart';
import '../../../../core/theme/app_theme.dart';
import '../../../../core/utils/app_constants.dart';

class SwapButtonWidget extends StatefulWidget {
  final VoidCallback onPressed;

  const SwapButtonWidget({
    super.key,
    required this.onPressed,
  });

  @override
  State<SwapButtonWidget> createState() => _SwapButtonWidgetState();
}

class _SwapButtonWidgetState extends State<SwapButtonWidget>
    with SingleTickerProviderStateMixin {
  late AnimationController _animationController;
  late Animation<double> _rotationAnimation;

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      duration: const Duration(milliseconds: 300),
      vsync: this,
    );
    _rotationAnimation = Tween<double>(
      begin: 0.0,
      end: 0.5,
    ).animate(CurvedAnimation(
      parent: _animationController,
      curve: Curves.easeInOut,
    ));
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  void _onPressed() {
    _animationController.forward().then((_) {
      _animationController.reverse();
    });
    widget.onPressed();
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: _rotationAnimation,
      builder: (context, child) {
        return Transform.rotate(
          angle: _rotationAnimation.value * 2 * 3.14159,
          child: FloatingActionButton(
            key: const Key(AppConstants.swapButtonKey),
            onPressed: _onPressed,
            backgroundColor: AppTheme.primaryBlue,
            foregroundColor: Colors.white,
            elevation: AppConstants.defaultElevation,
            child: const Icon(
              Icons.swap_vert,
              size: 24,
            ),
          ),
        );
      },
    );
  }
}
