import 'package:flutter/material.dart';
import '../utils/colors.dart';

class GlassCard extends StatelessWidget {
  final Widget child;
  final EdgeInsetsGeometry padding;
  final VoidCallback? onTap;

  const GlassCard({super.key, required this.child, this.padding = const EdgeInsets.all(12), this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: XolericColors.glass,
          borderRadius: BorderRadius.circular(16),
          border: Border.all(color: XolericColors.glassBorder, width: 0.5),
        ),
        padding: padding,
        child: child,
      ),
    );
  }
}
