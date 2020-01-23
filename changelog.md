# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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
