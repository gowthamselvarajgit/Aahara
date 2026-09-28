# Aahara UI Design System

## Visual Identity
Aahara is a premium, modern nutrition and fitness application. The visual identity relies on large rounded cards, strong typography, beautiful 3D visual assets, clear hierarchy, and generous whitespace. It has a sophisticated Indian-focused wellness aesthetic.

## Color System
Semantic tokens are used to adapt to light and dark modes.

* **Primary:** Deep natural green (`#2B5A41` light / `#85C7A3` dark)
* **Background:** Warm cream / soft off-white (`#F9FAED` light / `#121614` dark)
* **Surface:** Pure white or elevated dark (`#FFFFFF` light / `#1E2421` dark)
* **Text:** Charcoal for readability (`#2D332F` light / `#F2F5F3` dark)
* **Accents:** 
  * Nutrition: Fresh green (`#4A7C59` / `#66A979`)
  * Workout: Subtle warm accent (`#D97757` / `#E29A80`)

## Typography
Strong emphasis on readability and data visualization. 
Styles: `display`, `largeNumber`, `heading`, `subheading`, `body`, `bodySmall`, `caption`, `button`, `metric`.
Numbers visually dominate standard text.

## Cards
Large rounded corners (`24dp`), subtle elevations, and generous padding (`md` to `lg`).
Types include: `AaharaCard`, `HeroCard` (macro), `MetricCard`, `MealCard`, etc.

## 3D Asset Strategy
Instead of stock photos, original premium 3D assets are used.
* **Food:** Idli, Dosa, Pongal (Photorealistic, clean lighting, no text).
* **Fitness:** Dumbbells, etc.
* **Hydration:** Clean water representations.
Managed via `<ThreeDAsset />` component for consistent sizing, loading states, and radii.

## Navigation
Minimal 5-tab bottom navigation (`Home`, `Food`, `Workout`, `Progress`, `Profile`).

## Tamil Support
The typography and components fully support rendering Tamil characters (e.g., இட்லி, தோசை). UI layers accommodate the height and baseline differences of Indian scripts.

## Accessibility & Performance
* Semantic contrast ratios.
* Local optimized assets where possible.
* WebP/JPG usage over huge PNGs.
* No overwhelming background animations.
