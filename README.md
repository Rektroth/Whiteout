# Whiteout

Whiteout is an attempt to implement the gameplay and mechanic consistency patches included in the [Paper](https://github.com/PaperMC/Paper) project stream in Fabric.

## Patches

### Present

| Bug                                                   | Upstream Source | Enabled by Default | Name                                                                                                                                              |
|-------------------------------------------------------|-----------------|--------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| [MC-4](https://bugs.mojang.com/browse/MC-4)           | Paper           | ❌                  | Item drops sometimes appear at the wrong location                                                                                                 |
| [MC-27056](https://bugs.mojang.com/browse/MC-27056)   | Paper           | ❌                  | You can blow the extension off a piston                                                                                                           |
| [MC-84789](https://bugs.mojang.com/browse/MC-84789)   | Paper           | ✔️                 | Tamed wolves beg for bones while wild wolves don't *(this has been partially fixed in the vanilla game, so this fix is no longer included Paper)* |
| [MC-108513](https://bugs.mojang.com/browse/MC-108513) | Paper           | ❌                  | End crystal did not spawn/despawn correct after enderdragon respawned                                                                             |
| [MC-123450](https://bugs.mojang.com/browse/MC-123450) | Paper           | ✔️                 | Item frames play sounds when the item within them is read from NBT                                                                                |
| [MC-123848](https://bugs.mojang.com/browse/MC-123848) | Paper           | ✔️                 | Item frames (and items within) when removed from a ceiling, drop atop, not under, the block                                                       |
| [MC-153086](https://bugs.mojang.com/browse/MC-153086) | Paper           | ✔️                 | Beacons always play deactivating sound when broken, even when not powered                                                                         |
| [MC-157464](https://bugs.mojang.com/browse/MC-157464) | Paper           | ✔️                 | Villagers move around in bed or even leave the bed when food is thrown at them                                                                    |
| [MC-158900](https://bugs.mojang.com/browse/MC-158900) | Paper           | ❌                  | "bad packet id 26" upon connecting after tempban expire                                                                                           |
| [MC-159283](https://bugs.mojang.com/browse/MC-159283) | Paper           | ✔️                 | The End terrain does not generate in multiple rings centered around the world center                                                              |
| [MC-163962](https://bugs.mojang.com/browse/MC-163962) | Paper           | ❌                  | Villager demand values decrease indefinitely                                                                                                      |
| [MC-167279](https://bugs.mojang.com/browse/MC-167279) | Paper           | ✔️                 | Bees get stuck in the void                                                                                                                        |
| [MC-171420](https://bugs.mojang.com/browse/MC-171420) | Paper           | ✔️                 | OP players get kicked for not being on the whitelist (enforce = on)                                                                               |
| [MC-188840](https://bugs.mojang.com/browse/MC-188840) | Paper           | ❌                  | *This ticket covered piston-based block duplication. It is unknown why the ticket was privated/deleted.*                                          |
| [MC-210802](https://bugs.mojang.com/browse/MC-210802) | Paper           | ❌                  | Inactive sheep eat grass                                                                                                                          |
| [MC-235045](https://bugs.mojang.com/browse/MC-235045) | Paper           | ✔️                 | Entity type tags suggestions don't work in target selectors                                                                                       |
| [MC-243057](https://bugs.mojang.com/browse/MC-243057) | Paper           | ✔️                 | Furnace recipes don't consider alternatives when first option of recipe is used for fuel                                                          |
| [MC-244739](https://bugs.mojang.com/browse/MC-244739) | Paper           | ✔️                 | Goat eating sounds aren't played when feeding them the last item of wheat within a stack                                                          |
| [MC-248588](https://bugs.mojang.com/browse/MC-248588) | Paper           | ✔️                 | The "mobGriefing" gamerule doesn't prevent burning entities from being able to decrease the levels of water or powder snow cauldrons              |
| [MC-253721](https://bugs.mojang.com/browse/MC-253721) | Paper           | ✔️                 | Wrong logs when running /op @a                                                                                                                    |
| [MC-257487](https://bugs.mojang.com/browse/MC-257487) | Paper           | ✔️                 | The ender dragon's name is not reset when it is respawned in the end                                                                              |
| [MC-259571](https://bugs.mojang.com/browse/MC-259571) | Paper           | ✔️                 | Last player game mode not saved after player dies or the game is reloaded                                                                         |
| [MC-262422](https://bugs.mojang.com/browse/MC-262422) | Paper           | ✔️                 | Lightning bolts during thunderstorms can strike players in spectator game mode                                                                    |
| [MC-263999](https://bugs.mojang.com/browse/MC-263999) | Paper           | ✔️                 | Zombies breaking doors do not show break particles                                                                                                |
| *N/A*                                                 | Paper           | ❌                  | Accessing the nether roof                                                                                                                         |
| *N/A*                                                 | Paper           | ❌                  | Breaking permanent blocks                                                                                                                         |

Patches that aren't enabled by default generally meet at least one of the following criteria:

- The new behavior created by the patch is (possibly) not what's actually intended by Mojang
- The patch *circumvents* the issue but doesn't truly correct it
- The manner in which the patch is applied is not technically elegant and might cause unforeseen issues and/or performance degradation
- Mojang has not assigned the ticket a priority, potentially meaning that a Mojang employee has not yet personally reviewed the ticket and confirmed that the described behavior is unintentional

If you wish to enable them, you will have to do so in `.minecraft/configs/whiteout.properties`.

### Planned

| Bug                                                   | Source | Name                                                                                  |
|-------------------------------------------------------|--------|---------------------------------------------------------------------------------------|
| [MC-33041](https://bugs.mojang.com/browse/MC-33041)   | Spigot | Dedicated server logs "java.io.IOException: The handle is invalid" on startup         |
| [MC-92282](https://bugs.mojang.com/browse/MC-92282)   | Spigot | Mob spawner block_entity_data does not copy SpawnData data to missing SpawnPotentials |
| [MC-99075](https://bugs.mojang.com/browse/MC-99075)   | Paper  | Cancelled block place (spawn protection) causes inventory desync                      |
| [MC-121706](https://bugs.mojang.com/browse/MC-121706) | Purpur | Skeletons and illusioners aren't looking up / down at their target while strafing     |
| [MC-145260](https://bugs.mojang.com/browse/MC-145260) | Paper  | Whitelist on state inconsistency                                                      |
| [MC-147659](https://bugs.mojang.com/browse/MC-147659) | Paper  | Some witch huts spawn the incorrect cat                                               |
| [MC-157395](https://bugs.mojang.com/browse/MC-157395) | Spigot | Small armor stands drop no loot table                                                 |
| [MC-174630](https://bugs.mojang.com/browse/MC-174630) | Paper  | Secondary beacon effect remains when switching primary effect                         |
| [MC-179072](https://bugs.mojang.com/browse/MC-179072) | Paper  | Creepers do not defuse when switching from Survival to Creative/Spectator             |
| [MC-200092](https://bugs.mojang.com/browse/MC-200092) | Paper  | /setworldspawn seems to ignore the 'angle' parameter                                  |
| [MC-264285](https://bugs.mojang.com/browse/MC-264285) | Spigot | Unbreakable flint and steels are completely consumed when igniting a creeper          |
| [MC-264979](https://bugs.mojang.com/browse/MC-264979) | Spigot | Fresh installations print NoSuchFileException for server.properties                   |
| [MC-273635](https://bugs.mojang.com/browse/MC-273635) | Paper  | Trial Spawner keeps flashing after cooldown ends                                      |

### Not Planned

| Bug                                                   | Name                                                                                                                                                                                            |
|-------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [MC-11193](https://bugs.mojang.com/browse/MC-11193)   | The order in which powerable blocks (e.g. redstone dust blocks) along a wire are powered or de-powered is not clearly defined and causes a non-deterministic behavior for redstone contraptions |
| [MC-81098](https://bugs.mojang.com/browse/MC-81098)   | Redstone dust updates cause lag                                                                                                                                                                 |

These patches are not planned for any future release. They are currently being addressed by Mojang in the experimental snapshots.

## Disclaimers

Whiteout is in no way associated with the Paper project. Please do not contact the maintainers of the Paper project about Whiteout.

### License

Since all bug fixes provided by this mod are ported from Paper, said fixes inherit their licensing from the Paper project, which is licensed under the [GPLv3](./LICENSE).

[While some contributors to the Paper project chose to release their code under the more permissive MIT License](https://github.com/PaperMC/Paper/blob/master/LICENSE.md), it is easier, and legally permissible, to release this entire project under the GPLv3. Patches are however still annotated to attribute appropriate credit to their original authors.

### Compatibility

Best effort is made to keep Whiteout compatible with [Lithium](https://github.com/caffeinemc/lithium-fabric). Reporting of issues with compatibility is appreciated.

Compatibility with other mods is not guaranteed, but if you make an issue ticket I may get around to it. Attempts at compatibility are at my discretion. I generally will not make an effort maintain compatibility with very niche mods or large mod-packs, and will make no attempt at compatibility with mods that are fundamentally incompatible with the goals of Whiteout.

Compatibility with Sinytra Connector and similar projects is **not supported at all**. If you have issues using Whiteout with such layers, that is your problem.

### Backporting

Deprecated versions of Minecraft will **never** be supported. ~~This is a hobby project.~~ *Actually, it's more accurate to say that I made this mod - and even learned how to mod in the first place - because <u>I personally</u> needed these fixes in Fabric. I only made this project public because I figured there might be at least a handful of others that want/need the same.* I do not care nor have the time to maintain backports. Please don't bother asking.
