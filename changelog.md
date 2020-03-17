# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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

## [1.15.2-1.0.0] - 2020-02-08

### Added
- Initial release.
