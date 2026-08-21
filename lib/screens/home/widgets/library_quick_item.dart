import 'package:flutter/material.dart';
import '../../../utils/colors.dart';
import '../../../widgets/glass_card.dart';

class LibraryQuickItem extends StatelessWidget {
  final IconData icon;
  final String label;
  final VoidCallback? onTap;

  const LibraryQuickItem({super.key, required this.icon, required this.label, this.onTap});

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: GlassCard(
        onTap: onTap,
        child: Padding(
          padding: const EdgeInsets.symmetric(vertical: 20),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Icon(icon, color: XolericColors.neonCyan, size: 28),
              const SizedBox(height: 8),
              Text(label, style: const TextStyle(color: XolericColors.textPrimary, fontSize: 12, fontWeight: FontWeight.w500)),
            ],
          ),
        ),
      ),
    );
  }
}
