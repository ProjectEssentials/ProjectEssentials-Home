# ![image](assets/home_social.png)

## Lightweight home controller provider for forge

### Explore

#### [Download mod](https://github.com/ProjectEssentials/ProjectEssentials-Home/releases/download/v1.14.4-1.1.0/Project.Essentials.Home-1.14.4-1.1.0.jar) · [How to install](documentation/how-install.md) · [Troubleshooting](https://github.com/ProjectEssentials/ProjectEssentials-Home/issues/new/choose) · [CurseForge](https://www.curseforge.com/minecraft/mc-mods/ProjectEssentials-Home) · [Change log](changelog.md)

### Commands and Permissions

|Command name  |Aliases                                 |Permission         |Op level  |Description  |
|-----         |-----                                   |----               |----      |----         |
|`/home`       |`/ehome`                                |`ess.home`         |`2`       |Teleport you to your home. If command executed without arguments then will be used default home name `home`. Execution example with home name `/home home2`.|
|`/sethome`    |`/esethome`                             |`ess.home.set`     |`2`       |Create new home point at player position. If command executed without argument `name` then will be used default home name `home`. If home with some name exist, then command execution will be aborted if you not use argument `override`, for overriding existing home, just type `true` after home name. (`boolean` type argument).|
|`/delhome`    |`/edelhome`, `/removehome`, `/remhome`  |`ess.home.remove`  |`2`       |Remove existing home point with name. If command executed without arguments then home with name `home` will be removed.|

### Compatibility

This mod branch supported forge version `28.0.X` and `28.1.X` (Minecraft `1.14.4`).

### Credits

- Author: Pavel Erokhin [@mairwunnx](https://github.com/mairwunnx)
- [JetBrains](https://www.jetbrains.com/) for Licenses

[![image](https://github.com/ProjectEssentials/ProjectEssentials-Core/raw/MC-1.14.4/assets/support_social.png)](https://ko-fi.com/mairwunnx)
