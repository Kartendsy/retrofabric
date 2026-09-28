# RetroFabric API

Essential hooks for modding on 1.7.10 (forked from Legacy Fabric API).

RetroFabric API is the library for essential hooks and interoperability mechanisms for RetroFabric mods. Examples include:

- Exposing functionality that is useful but difficult to access for many mods such as biomes and enchantments
- Adding events, hooks, APIs and commonly used hacks to improve interoperability between mods such as resource loading, networking and gamerules
- Adding APIs that help circumvent difficult-to-use minecraft APIs such as commands

## Playing (Prism Launcher)

1. Download a 1.7.10 + loader **0.13.3** instance from https://legacyfabric.net/downloads.html and import it into Prism (Add Instance - Import from zip).
2. Copy `retrofabric-api-2.0.0-retrofabric.1+1.7.10.jar` from the GitHub release into the instance's `mods/` folder.
3. Remove any `legacy-fabric-api` jar if present, and run the instance with **Java 8**.

## Developing a mod

Requires Minecraft 1.7.10, Legacy Fabric loader 0.13.3 and Java 17 to build.
See `retrofabric-example-mod` (Java) and `retrofabric-example-mod-kotlin`
(Kotlin + `fabric-language-kotlin:1.7.4+kotlin.1.6.21`) for copy-paste templates.

Included modules: core, logger, crash-report-info, entity-events, gamerule,
item-groups, keybinding, lifecycle-events, networking, permissions, rendering,
resource-loader, sponge-command, vanilla-command, mod-menu (built-in, client),
registry, loot, recipe, biome.

For support, open an issue or discussion at https://github.com/Kartendsy/retrofabric.
