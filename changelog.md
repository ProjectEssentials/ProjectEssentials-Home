# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Since 2.0.0 versions change log same for all supported minecraft versions.

## [Unreleased]

## [2.0.1] - 2020-06-20

### Added
- Chinese simplified localization by @KuroNoSeiHai

### Fixed
- Stupidly mistake in removing own home. (Removes all homes!)

## [2.0.0] - 2020-06-08

### Added
- `configure-home` command.

### Changed
- Back location committing before teleporting to home.
- Providers and localizations resolution strategy changed.

### Fixed
- `HomeConfiguration.kt` incorrect saving fixed.
- `HomeSettingsConfiguration.kt` incorrect saving fixed.
- `respawnAtHomeAfterDeath` setting ignoring fixed.

## [2.0.0-SNAPSHOT.1] - 2020-06-03

### Added
- Chinese simplified translation added by @KuroNoSeiHai.
- gradle wrapper added.
- `updatev2.json` added.
- Sending providers and localization added.
- `HomeSelectStrategy` enum added.
- `validateAndExecute` added.
- Effects and sound on teleporting added.
- Limitations checking added.
- Re spawning at home after death added.

### Changed
- License now is MIT
- `.gitignore` synced with core module.
- `.github` files updated.
- `gradle.properties` updated.
- `build.gradle` updated.
- Version now will take value from jar version.
- updateJSONURL uses v2 json.
- credits updated in `mods.toml`.
- Required core version now `2.X`.
- description updated.
- Home literal now `inline`.
- `worldId` renamed to `dimensionId`.
- Home command re-written.
- Set-home command re-written.
- del-home command re-written.
- Localization updated.

### Fixed
- Fixed crash with `server.commandManager.dispatcher`.
- Incorrect behavior with different dimensions.

### Removed
- Internal assets removed.
- An internal documentation removed.
- `Messaging.kt` removed.
- Old source files.

## [1.15.2-1.1.0] - 2020-03-17

### Added
- Documentation for `HomeModel.kt`.
- `HomeAPI` implemented `save` method.
- `HomeAPI` implemented `reload` method.
- Implemented `HomeAPI.take` method.
- Implemented `HomeAPI.contains` method.
- Implemented `HomeAPI.takeAll` method.
- Implemented `HomeAPI.remove` method.
- Implemented `HomeAPI.create` method.
- Supporting `/back` command.
- Localization processing.
- `Messaging.kt` implemented.
- `Dokka` added to `build.gradle`.

### Changed
- Kotlin version updated to `1.3.70`.
- KotlinX Serialization version updated to `0.20.0`.
- Forge API version updated to `28.2.0`.
- `StorageBase` now is internal.
- `EntryPoint.kt` now is internal.
- `SetHomeCommand.kt` now is internal.
- `HomeCommand.kt` now is internal.
- `DelHomeCommand.kt` now is internal.

### Removed
- `curseforge` dependency removed from dependencies in `build.gradle`.

## [1.14.4-1.2.0] - 2020-03-16

### Added
- Documentation for `HomeModel.kt`.
- `HomeAPI` implemented `save` method.
- `HomeAPI` implemented `reload` method.
- Implemented `HomeAPI.take` method.
- Implemented `HomeAPI.contains` method.
- Implemented `HomeAPI.takeAll` method.
- Implemented `HomeAPI.remove` method.
- Implemented `HomeAPI.create` method.
- Supporting `/back` command.
- Localization processing.
- `Messaging.kt` implemented.
- `Dokka` added to `build.gradle`.

### Changed
- Kotlin version updated to `1.3.70`.
- KotlinX Serialization version updated to `0.20.0`.
- Forge API version updated to `28.2.0`.
- `StorageBase` now is internal.
- `EntryPoint.kt` now is internal.
- `SetHomeCommand.kt` now is internal.
- `HomeCommand.kt` now is internal.
- `DelHomeCommand.kt` now is internal.

### Removed
- `curseforge` dependency removed from dependencies in `build.gradle`.

## [1.15.2-1.0.0] - 2020-02-08

### Added
- Initial release.

## [1.14.4-1.1.2] - 2020-02-08

### Changed
- Uses `permissionAPIClassPath` from CoreAPI.
- Uses `cooldownAPIClassPath` from CoreAPI.

## [1.14.4-1.1.1] - 2020-01-27

### Changed
- Logger message with logging new home data.

### Fixed
- Incorrect operator level for executing home commands.

## [1.14.4-1.1.0] - 2020-01-23

### Added
- German support by [@BixelPitch](https://github.com/BixelPitch).
- Checks for availability `cooldown` and `permissions` modules.
- Compatibility with core `1.1.0.0` and permissions `1.1.0.0`.
- Checking on already exist home.
- `override` argument for overriding existing home.
- Removing previous home if override enabled.

### Changed
- Gradle wrapper version updated.
- Java plugin removed from buildscript.
- Project Essentials dependencies updated.
- Kotlin and Kotlinx Serialization dependencies updated.
- `gradle.properties` variable naming improved.
- Usings `jsonInstance` from core module.

### Fixed
- Issue tracker url in `mods.toml` file.

### Removed
- Permissions module from mandatory dependencies.
- Redundant logger messages.
- Kotlin compiler option `-Xuse-experimental` from buildscript.
- `createConfigDirs` method from `StorageBase`.
- `UseExperimental` annotation for `StorageBase` class.

## [1.14.4-1.0.0 ~~.0~~] - 2019-10-13

### Added
- Initial release of Project Essentials Home as Project Essentials part.
