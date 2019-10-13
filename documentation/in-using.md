> ## Documentation for basically using Home module.

## 1. For playing and running minecraft:

#### 1.1 Download Home mod module.

Visit **Home** repository on github, visit **releases** tab and download `.jar` files of latest _pre-release_ / release (**recommended**)

Releases page: https://github.com/ProjectEssentials/ProjectEssentials-Home/releases

#### 1.2 Install Home modification.

The minecraft forge folder structure below will help you understand what is written below.

> ##### Important note: don't forget install mod dependencies!
  - core: https://github.com/ProjectEssentials/ProjectEssentials-Core/releases
  - permissions: https://github.com/ProjectEssentials/ProjectEssentials-Permissions/releases


```
.
├── assets
├── config
├── libraries
├── mods (that's how it should be)
│   ├── Project Essentials Home-1.14.4-1.X.X.X.jar
│   ├── Project Essentials Core-MOD-1.14.4-1.X.X.X.jar.
│   └── Project Essentials Permissions-1.14.4-1.X.X.X.jar.
└── ...
```

Place your mods and Home mods according to the structure above.

#### 1.3 Verifying mod on the correct installation.

Run the game, check the number of mods, if the list of mods contains `Project Essentials Home` mod, then the mod has successfully passed the initialization of the modification.

After that, go into a single world, then try to write the `/home` command, if you **get an error** that you do not have permissions, then the modification works as it should.

#### 1.4 Control spawn via minecraft commands.

We made the commands for you:

```
/home (it also alias for /home home)

OR

/home <home name>

- description: base command of home module; just teleport you to your home.

- permission: ess.home
```

```
/sethome (it also alias for /sethome home)

OR

/sethome <home name>

- description: set new home with name point for command sender.

- note: if you set a new point with the same name as the old point, the old point **will be overwritten**!

- permission: ess.home.set
```

```
/delhome (it also alias for /delhome home)

OR

/delhome <home name>

- description: remove named home point.

- permission: ess.home.remove
```

### For all questions, be sure to write issues!
