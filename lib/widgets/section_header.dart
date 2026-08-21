import 'package:flutter/material.dart';
import '../utils/colors.dart';

class SectionHeader extends StatelessWidget {
  final String title;
  final String? actionText;
  final VoidCallback? onAction;

  const SectionHeader({super.key, required this.title, this.actionText, this.onAction});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(title, style: const TextStyle(color: XolericColors.textPrimary, fontSize: 18, fontWeight: FontWeight.w600)),
          if (actionText != null && onAction != null)
            TextButton(onPressed: onAction,
                child: Text(actionText!, style: const TextStyle(color: XolericColors.neonCyan, fontSize: 14))),
        ],
      ),
    );
  }
}
