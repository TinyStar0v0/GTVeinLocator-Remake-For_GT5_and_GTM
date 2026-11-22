# GT Vein Locator

GT Vein Locator is a Minecraft Forge addon that adds tiered locator items and detection cores for GregTech ore veins.

This repository contains two independent editions:

| Edition | Path | Minecraft | GregTech |
|---|---|---|---|
| GT5u / GTNH | `gt5/` | 1.7.10 | GT5-Unofficial |
| GTCEu Modern / GTM | `gtm/` | 1.20.1 | GTCEu Modern |

## Downloads

- GT5u 1.7.10: https://github.com/TinyStar0v0/GTVeinLocator-GTMv/releases/tag/v1.0.0-gt5
- GTCEu Modern 1.20.1: https://github.com/TinyStar0v0/GTVeinLocator-GTMv/releases/tag/v1.0.0-gtm

## Features

- Three locator tiers: LV / MV / HV.
- Three matching detection cores.
- LV / MV / HV scan radii: 3 / 5 / 7 chunks.
- Ore veins grouped by origin; all layers of one origin count as one vein.
- GT5u edition:
  - Scans loaded GT5u ore blocks.
  - Resolves vein names through `GT5OreLayerHelper`.
  - Writes results into VisualProspecting when installed.
  - Creates JourneyMap 5 waypoints when VisualProspecting is absent.
- GTM edition:
  - Uses GTCEu `GeneratedVeinMetadata`.
  - Writes results into VisualVein when installed.
  - Creates JourneyMap or Xaero waypoints when VisualVein is absent.

## Recipes

Locators are assembler-only.

- Detection core: sensors, emitters, matching circuit, steel plates, quartz.
- Locator: matching plates, cables, matching circuit, matching detection core, 144L molten tin.

## Building

### GT5u 1.7.10

Requirements:

- JDK 8
- GTNH development environment / GT5-Unofficial Maven dependencies

```bash
cd gt5
./gradlew build
```

### GTM 1.20.1

Requirements:

- JDK 17
- Forge 47.4.16
- GTCEu Modern 1.4.4
- The dependency jars referenced by `gtm/build.gradle` in `gtm/libs/`

```bash
cd gtm
./gradlew build
```

## Credits

Original GTVeinLocator by GWYOG, MIT licensed.

GT5u and GTM ports by VisualVein contributors.

## License

MIT. See [LICENSE](LICENSE).
